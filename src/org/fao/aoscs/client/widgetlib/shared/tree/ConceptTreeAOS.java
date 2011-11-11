package org.fao.aoscs.client.widgetlib.shared.tree;

import java.util.ArrayList;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.domain.NtreeItemObject;
import org.fao.aoscs.domain.TreeObject;
import org.fao.aoscs.domain.TreePathObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.widgetideas.client.FastTreeItem;

public class ConceptTreeAOS extends Composite{
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	public TreeAOS tree;
	public VerticalPanel panel = new VerticalPanel();
	public LoadingDialog sayLoading = new LoadingDialog();
	private CheckBox showAlsoNonpreferredTerms = new CheckBox();
	
	public ConceptTreeAOS(ArrayList<TreeObject> ctObj, int type){
		this.showAlsoNonpreferredTerms.setValue(!MainApp.userPreference.isHideNonpreferred());
		init(ctObj, type);
		initWidget(panel);
	}
	
	public ConceptTreeAOS(ArrayList<TreeObject> ctObj, int type, CheckBox showAlsoNonpreferredTerms){
		this.showAlsoNonpreferredTerms = showAlsoNonpreferredTerms;
		init(ctObj, type);
		initWidget(panel);
	}
	
	/**Creating dynamic tree with the started selected item*/
	public ConceptTreeAOS(ArrayList<TreeObject> ctObj, int type, CheckBox showAlsoNonpreferredTerms, String initURI, String rootConcept, int infoTab){
		this.showAlsoNonpreferredTerms = showAlsoNonpreferredTerms;
		init(ctObj, type);
		gotoItem(initURI, rootConcept, infoTab, showAlsoNonpreferredTerms.getValue(), MainApp.userPreference.isHideDeprecated(), MainApp.userSelectedLanguage);
		initWidget(panel);
	}
	
	public void init(final ArrayList<TreeObject> startConceptList, int type)
	{
		tree = new TreeAOS(type);
		tree.ensureSelectedItemVisible();
		panel.add(tree);
		tree.setSize("100%", "100%");
		panel.add(sayLoading);
		panel.setSize("100%", "100%");
		panel.setCellHorizontalAlignment(sayLoading, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(sayLoading, HasVerticalAlignment.ALIGN_MIDDLE);
		panel.setCellHeight(sayLoading, "100%");
		panel.setCellWidth(sayLoading, "100%");
		
		reload(startConceptList);
	}
	
	public void clear(){
		this.panel.clear();
	}
	
	public void reload(final ArrayList<TreeObject> startConceptList){
		this.tree.removeItems();
		Scheduler.get().scheduleDeferred(new Command() {
			public void execute() {
				LazyLoadingTree.addTreeItems(tree, startConceptList, showAlsoNonpreferredTerms.getValue());
				showLoading(false);
			}
		});
	}
	
	public void reload(ArrayList<TreeObject> ctObj, String targetItem, String rootConcept, int initTab, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList){
		reload(ctObj);
		gotoItem(targetItem, rootConcept, initTab, showAlsoNonpreferredTerms, isHideDeprecated, langList);
	}
	public void traversePrinting(TreeItemAOS item){
		for (int i = 0; i < item.getChildCount(); i++) {
			Object obj = (Object) item.getChild(i);
			if (obj instanceof TreeItemAOS) {
				TreeItemAOS childItem = (TreeItemAOS) obj;
				traversePrinting(childItem);
			}
		}
	
	}
	/**Go to any item in tree (use with concept tree)*/
	@SuppressWarnings("unchecked")
	public void gotoItem(final String targetItem, String rootConcept, final int initTab, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList){		
	    showLoading(true);
		panel.setWidth("100%");
		panel.setCellHorizontalAlignment(sayLoading, HasHorizontalAlignment.ALIGN_RIGHT);
		AsyncCallback<TreePathObject> callback = new AsyncCallback<TreePathObject>(){
			public void onSuccess(TreePathObject results){
				TreePathObject tpObj = (TreePathObject) results;
				if(!tpObj.isEmpty()){
					TreeItemAOS item = convert2subTree(tpObj);
					SearchTree.searchNReplace(tree, item, targetItem);
					showLoading(false);
				}else{
					showLoading(false);
					Window.alert(constants.sharedConceptNotFound());
				}
			}
			public void onFailure(Throwable caught){
				Window.alert(constants.sharedGetTreePathFail());
			}
		};
		Service.treeService.getTreePath(targetItem, rootConcept, MainApp.userOntology, showAlsoNonpreferredTerms, isHideDeprecated, langList, callback);
	}
	
	private TreeItemAOS convert2subTree(TreePathObject tpObj){
		 NtreeItemObject rootItem = tpObj.getRootItem();
		 TreeItemAOS item = new TreeItemAOS(rootItem.getTreeObject(), showAlsoNonpreferredTerms.getValue());
		 item.setLoad(true);
		 for (int i = 0; i < rootItem.getChildCount(); i++) {
			 item.addItem(new TreeItemAOS(rootItem.getChild(i), showAlsoNonpreferredTerms.getValue()));
		 }
		 addChild(item, tpObj);
		 return item;
	}
	
	private void addChild(TreeItemAOS item,TreePathObject tpObj){
		for (int i = 0; i < item.getChildCount(); i++) {
			Object obj = (Object) item.getChild(i);
			if (obj instanceof TreeItemAOS) {
				TreeItemAOS cItem = (TreeItemAOS) obj;
				TreeObject childObj = (TreeObject) cItem.getValue();
				if(tpObj.hasItemInPath(childObj.getUri())){
					item.removeItem(cItem);
					NtreeItemObject nItem = tpObj.getItemInPath(childObj.getUri());
					TreeItemAOS c = new TreeItemAOS(nItem.getTreeObject(), showAlsoNonpreferredTerms.getValue());
					item.addItem(c);
					for (int j = 0; j < nItem.getChildCount(); j++) {
						c.addItem(new TreeItemAOS(nItem.getChild(j), showAlsoNonpreferredTerms.getValue()));
					}
					addChild(c, tpObj);
					c.setLoad(true);
				}
			}else{
				((FastTreeItem)obj).setVisible(false);
			}
			
		}
	}
	
	public void showLoading(boolean sayLoad){
		if(sayLoad){
			sayLoading.setVisible(true);
			tree.setVisible(false);
		}else{
			sayLoading.setVisible(false);
			tree.setVisible(true);
		}
	}
	
	public Object getSelectedTreeItem(){
		return ((TreeItemAOS) tree.getSelectedItem());
	}
}
