package org.fao.aoscs.client.widgetlib.shared.dialog;

import java.util.ArrayList;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;
import org.fao.aoscs.client.widgetlib.shared.tree.LazyLoadingTree;
import org.fao.aoscs.client.widgetlib.shared.tree.TreeAOS;
import org.fao.aoscs.client.widgetlib.shared.tree.TreeItemAOS;
import org.fao.aoscs.domain.LabelObject;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.RelationshipTreeObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

public class RelationshipBrowser extends FormDialogBox {

	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private LoadingDialog ld = new LoadingDialog();
	private ScrollPanel sc = new ScrollPanel();
	private TreeAOS conceptRelTree = null;
	private TreeAOS termRelTree = null;
	private TreeAOS allRelTree = null;
	private RelationshipTreeObject conceptRelObject = null;
	private RelationshipTreeObject termRelObject = null;
	private RelationshipTreeObject allRelObject = null;
	private DecoratedPopupPanel detail = new DecoratedPopupPanel(true);
	private HTML expandAll = new HTML(constants.buttonExpandAll());
	private HTML collapseAll = new HTML(constants.buttonCollapseAll());
	private HorizontalPanel leftBottomWidget = new HorizontalPanel();
	int relType = 2;
	
	final public static int REL_CONCEPT = 0;
	final public static int REL_TERM = 1;
	final public static int REL_ALL = 2;
	
	public RelationshipBrowser() 
	{
		super();
		leftBottomWidget.add(new Spacer("15px", "100%"));
		leftBottomWidget.add(expandAll);
		leftBottomWidget.add(new Spacer("20px", "100%", "|"));
		leftBottomWidget.add(collapseAll);
		setLeftBottomWidget(leftBottomWidget);
		panel.setSize("500px", "400px");
		sc.setSize("500px", "400px");
		addWidget(sc,true);			
	}
	
	public void addLeftBottomWidget(final TreeAOS tree)
	{
		expandAll.setStyleName("quick-link");
		collapseAll.setStyleName("quick-link");
		expandAll.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event) {
				tree.setTreeState(true);
			}
			
		});
		collapseAll.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event) {
				tree.setTreeState(false);
			}
			
		});
	}
	
	public void showBrowser(final int relType) 
	{		
		leftBottomWidget.setVisible(false);
		sc.clear();		
		sc.add(ld);
		show();
		this.relType = relType;		
		setText(constants.relRelationshipBrowser());	
		TreeAOS relTree = getSelectedTree();
		if(relTree == null)
		{								
			initTree();				
		}
		else
		{
			sc.clear();
			sc.add(relTree);	
			leftBottomWidget.setVisible(true);
		}
	}
	
	private void initTree()
	{		
		
		String rootnode = ModelConstants.RWBOBJECTPROPERTY;
		boolean includeSelf = false;
		
		if(relType == REL_CONCEPT)
		{
			rootnode = ModelConstants.RHASRELATEDCONCEPT;
			includeSelf = true;
		}
		else if(relType == REL_TERM)
		{
			rootnode = ModelConstants.RHASRELATEDTERM;
			includeSelf = true;
		}
		
		AsyncCallback<Object> callback = new AsyncCallback<Object>() 
		{
			public void onSuccess(Object result) {	
				final TreeAOS tree;
				if(relType == REL_CONCEPT){
					conceptRelObject = new RelationshipTreeObject();
					conceptRelObject = (RelationshipTreeObject)result;
					conceptRelTree = makeRelationshipTree(conceptRelObject);
					conceptRelTree.setTitle(constants.conceptClickForDefinition());
					tree = conceptRelTree; 
				}
				else if(relType == REL_TERM){
					termRelObject = new RelationshipTreeObject();
					termRelObject = (RelationshipTreeObject)result;
					termRelTree = makeRelationshipTree(termRelObject);
					termRelTree.setTitle(constants.termClickForDefinition());
					tree = termRelTree;
				}	
				else{
					allRelObject = new RelationshipTreeObject();
					allRelObject = (RelationshipTreeObject)result;
					allRelTree = makeRelationshipTree(allRelObject);
					allRelTree.setTitle(constants.termClickForDefinition());
					tree = allRelTree;
				}	
				
				tree.ensureSelectedItemVisible();
				sc.clear();
				sc.add(tree);	
				sc.addScrollHandler(new ScrollHandler()
				{
					public void onScroll(ScrollEvent event) {
						detail.hide();
					}
				});
				leftBottomWidget.setVisible(true);
				addLeftBottomWidget(tree);
			}
			public void onFailure(Throwable caught) {
				Window.alert(constants.relFailRelationshipBrowser());//LANG
			}
		};
		Service.relationshipService.getObjectPropertyTree(rootnode, MainApp.userOntology, includeSelf, callback);
		
	}

	public void onTreeSelection(TreeItemAOS item)
	{
		
		RelationshipTreeObject rto = getSelectedRelationshipTreeObject();
		RelationshipObject rObj = (RelationshipObject) ((TreeItemAOS) item).getValue();
		if(rObj!=null)
		{
			String definition = "";
			ArrayList<LabelObject> labelList = rto.getRelationshipDefinition(rObj.getUri());
			if(labelList!=null)
			{
				for (int i = 0; i < labelList.size(); i++) {
					if (i > 0)
						definition += "; ";
					if (labelList.get(i) != null)
						definition += labelList.get(i).getLabel();
				}

				HTML txt = new HTML();
				txt.setHTML(definition);
				txt.addClickHandler(new ClickHandler()
				{
					public void onClick(ClickEvent event) {
						detail.hide();
					}
				});
				if (definition.length() > 0)
				{
					detail.setPopupPosition(item.getAbsoluteLeft() + 25, item.getAbsoluteTop() + 25);
					detail.clear();
					detail.add(txt);
					detail.show();
				}
				else
				{
					detail.hide();
				}
			}
		}
	}
	
	public HandlerRegistration addSubmitClickHandler(ClickHandler handler) {
		return this.submit.addClickHandler(handler);
	}
	
	public TreeAOS getSelectedTree()
	{
		if(relType == REL_CONCEPT)
		{
			return conceptRelTree;
		}
		else if(relType == REL_TERM)
		{
			return termRelTree;
		}
		else
			return allRelTree;
	}
	
	public RelationshipTreeObject getSelectedRelationshipTreeObject()
	{
		if(relType == REL_CONCEPT)
		{
			return conceptRelObject;
		}
		else if(relType == REL_TERM)
		{
			return termRelObject;
		}
		return allRelObject;
	}

	public String getSelectedItem(){
		TreeItemAOS vItem = (TreeItemAOS) getSelectedTree().getSelectedItem();
		return vItem.getText();
	}
	
	public RelationshipObject getRelationshipObject(){
		TreeItemAOS vItem = (TreeItemAOS) getSelectedTree().getSelectedItem();
		RelationshipObject rObj = (RelationshipObject) vItem.getValue();
		return rObj;
	}

	private TreeAOS makeRelationshipTree(final RelationshipTreeObject rtObj) {
		final TreeAOS tree = new TreeAOS(TreeAOS.TYPE_RELATIONSHIP_BROWSER);
		tree.setSize("100%", "100%");
		Scheduler.get().scheduleDeferred(new Command() {
            public void execute()
            {
				LazyLoadingTree.addTreeItems(tree, rtObj.getRootItem(), rtObj);
			}
		});
		return tree;
	}
}