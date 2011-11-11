package org.fao.aoscs.client.widgetlib.shared.tree;

import java.util.ArrayList;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.classification.Classification;
import org.fao.aoscs.client.module.concept.Concept;
import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.client.module.constant.OWLStatusConstants;
import org.fao.aoscs.client.module.constant.TreeItemColor;
import org.fao.aoscs.client.module.relationship.Relationship;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.RelationshipBrowser;
import org.fao.aoscs.domain.ClassObject;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.TreeObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.ListeningFastTreeItem;

public class TreeItemAOS extends ListeningFastTreeItem{
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private Object obj;
	private boolean load = false;
	private String label;
	private boolean hasChild;
	private boolean showAlsoNonpreferredTerms;
	private String loadingLogo = "images/loading.gif";
	
	public TreeItemAOS(String text){
		super(text);
	}
	
	public TreeItemAOS(TreeObject tObject, boolean showAlsoNonpreferredTerms){
		super(convert2Widget(tObject));
		this.obj = tObject;
		this.showAlsoNonpreferredTerms = showAlsoNonpreferredTerms;
		this.setLabel(this.getText());
		if(tObject.isHasChild()){
			this.addItem("blank");
			this.setHasChild(true);
		}
		else
			this.setHasChild(false);
	}
	
	public TreeItemAOS(ConceptObject cObj, String label, boolean showAlsoNonpreferredTerms){
		super((Widget)convert2Widget(cObj, label));
		this.obj = cObj;
		this.showAlsoNonpreferredTerms = showAlsoNonpreferredTerms;
		this.setLabel(this.getText());
		if(cObj.isHasChild()){
			this.addItem("blank");
			this.setHasChild(true);
		}
		else
			this.setHasChild(false);
	}
	
	public TreeItemAOS(RelationshipObject rObj, String label){
		super(convert2Widget(rObj, label));
		this.obj = rObj;
		this.setLabel(this.getText());
	}
	
	public TreeItemAOS(ClassObject clObj){
		super((Widget)convert2Widget(clObj));
		this.obj = clObj;
		this.setLabel(this.getText());
		if(clObj.isHasChild()){
			this.addItem("blank");
			this.setHasChild(true);
		}
		else
			this.setHasChild(false);
	}
	
	public static String convert2Widget(TreeObject tObj){
		String label = tObj.getLabel();
		if(label.startsWith("###EMPTY###"))
			label = "";
		
		if(label.length()==0){
			label = "<img align='top' src='images/label-not-found.gif'>";
		}
		else
		{
			label = getColorForTreeItem(tObj.getStatus(),label).getHTML();
		}
		if(tObj.getUri().startsWith(ModelConstants.ONTOLOGYBASENAMESPACE)){
			label = "<img align='top' src='images/concept_logo.gif'>&nbsp;<span align='middle'>" + label+"</span>";
		}
		else{
			label = "<img align='top' src='images/category_logo.gif'>&nbsp;<span align='middle'>" + label+"</span>";
		}
		return label;
	}
	
	public static Widget convert2Widget(ConceptObject cObj, String label){
		HorizontalPanel hp = new HorizontalPanel();
		if(cObj.getUri().startsWith(ModelConstants.ONTOLOGYBASENAMESPACE)){
			hp.add(MainApp.aosImageBundle.conceptIcon().createImage());
		}else{
			hp.add(MainApp.aosImageBundle.categoryIcon().createImage());
		}
		
		hp.setSpacing(2);
				
		if(label.startsWith("###EMPTY###"))
			label = "";
		if(label.length()==0){
			hp.add(MainApp.aosImageBundle.labelNotFound().createImage());
		}
		else
		{
			hp.add(getColorForTreeItem(cObj.getStatus(),label));
		}
		return hp;
	}
	
	public static String convert2Widget(RelationshipObject rObj, String label){
		/*HorizontalPanel hp = new HorizontalPanel();

		if(rObj.getType().equals(RelationshipObject.OBJECT)){
			hp.add(new Image("images/relationship-object-logo.gif"));
		}else{
			hp.add(new Image("images/relationship-datatype-logo.gif"));
		}
		
		hp.setSpacing(2);
		
		if(label.length()==0){
			hp.add(MainApp.aosImageBundle.labelNotFound().createImage());
		}else{
			if(label.length()>7)
			{
				if(label.substring(label.length()-7).equals(";&nbsp;"))
					label = label.substring(0,label.length()-7);
			}
			hp.add(new HTML(label));	
		}
		return hp;*/
		
		if(label.startsWith("###EMPTY###"))
			label = "";
		if(label.length()==0){
			label = "<img align='top' src='images/label-not-found.gif'>";
		}
		if(rObj.getType().equals(RelationshipObject.OBJECT)){
			label = "<img align='top' src='images/relationship-object-logo.gif'>&nbsp;<span align='middle'>" + label+"</span>";
		}
		else{
			label = "<img align='top' src='images/relationship-datatype-logo.gif'>&nbsp;<span align='middle'>" + label+"</span>";
		}
		return label;
		
		
		
	}
	
	public static Widget convert2Widget(ClassObject clObj){
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(MainApp.aosImageBundle.conceptIcon().createImage());
		hp.setSpacing(2);
		hp.add(new HTML(clObj.getLabel()));
		return hp;
	}
	
	public static HTML getColorForTreeItem(String status,String label){
		HTML item = new HTML();
		if(status!=null){
			if(status.equals(OWLStatusConstants.DEPRECATED)){
				item.setHTML("<font color=\""+TreeItemColor.COLOR_DEPRECATED+"\">"+"<STRIKE>"+label+"</STRIKE>"+"</font>");
			}else if(status.equals(OWLStatusConstants.VALIDATED)){
				item.setHTML("<font color=\""+TreeItemColor.COLOR_VALIDATED+"\">"+label+"</font>");
			}else if(status.equals(OWLStatusConstants.PUBLISHED)){
				item.setHTML("<font color=\""+TreeItemColor.COLOR_PUBLISHED+"\">"+label+"</font>");
			}else if(status.equals(OWLStatusConstants.PROPOSED_DEPRECATED)){
				item.setHTML("<font color=\""+TreeItemColor.COLOR_PROPOSED_DEPRECATED+"\">"+label+"</font>");
			}else if(status.equals(OWLStatusConstants.REVISED)){
				item.setHTML("<font color=\""+TreeItemColor.COLOR_REVISED+"\">"+label+"</font>");
			}else if(status.equals(OWLStatusConstants.PROPOSED)){
				item.setHTML("<font color=\""+TreeItemColor.COLOR_PROPOSED+"\">"+label+"</font>");
			}else if(status.equals(OWLStatusConstants.PROPOSED_GUEST)){
				item.setHTML("<font color=\""+TreeItemColor.COLOR_PROPOSED_GUEST+"\">"+label+"</font>");
			}else if(status.equals(OWLStatusConstants.REVISED_GUEST)){
				item.setHTML("<font color=\""+TreeItemColor.COLOR_REVISED_GUEST+"\">"+label+"</font>");
			}
		}else{
			item.setHTML(label);
		}
		item.setWordWrap(true);
		if(status != null)	item.setTitle(status);
		return item;
	}

	public void setValue(Object obj){
		this.obj = obj;
	}
	public Object getValue(){
		return obj;
	}

	public boolean isLoad() {
		return load;
	}

	public void setLoad(boolean load) {
		this.load = load;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @param hasChild the hasChild to set
	 */
	public void setHasChild(boolean hasChild) {
		this.hasChild = hasChild;
	}

	/**
	 * @return the hasChild
	 */
	public boolean isHasChild() {
		return hasChild;
	}

	@Override
	public void beforeClose() {	
	}

	@Override
	public void beforeOpen() {	
	}

	@Override
	protected boolean beforeSelectionLost() {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void ensureChildren() {
		
		int treeType = ((TreeAOS) getTree()).getType();
		if(treeType == TreeAOS.TYPE_CONCEPT || treeType == TreeAOS.TYPE_CONCEPT_BROWSER || treeType == TreeAOS.TYPE_SUBVOCABULARY_BROWSER)
		{
			Object obj = this.getValue();
			if(obj instanceof TreeObject)
			{
				TreeObject cObj = (TreeObject) obj;
				if(!this.isLoad())
				{
					this.removeItems();
					this.addItem(new Image(loadingLogo));
					AsyncCallback<ArrayList<TreeObject>> callback =  new AsyncCallback<ArrayList<TreeObject>>(){
						public void onSuccess(ArrayList<TreeObject> results){
							TreeItemAOS.this.removeItems(); // clear loading message
							TreeItemAOS.this.setLoad(true);
							final ArrayList<TreeObject> ctObj = (ArrayList<TreeObject>) results;
							Scheduler.get().scheduleDeferred(new Command(){
								public void execute() {
									LazyLoadingTree.addTreeItems(TreeItemAOS.this, ctObj, showAlsoNonpreferredTerms);
								}
							});
						}
						public void onFailure(Throwable caught){
							Window.alert(constants.sharedGetTreeItemFail());
						}
					};
					Service.treeService.getTreeObject(cObj.getName(), MainApp.userOntology, showAlsoNonpreferredTerms, MainApp.userPreference.isHideDeprecated(), MainApp.userSelectedLanguage, callback);

				}
			}
		}
		else if(treeType == TreeAOS.TYPE_ClASS)
		{
			final TreeItemAOS vItem = this;
			Object obj = vItem.getValue();
			if(obj instanceof ClassObject)
			{
				ClassObject cObj = (ClassObject) obj;
				if(!vItem.isLoad()){
					vItem.removeItems();
					vItem.addItem(new Image("images/loading.gif"));
					AsyncCallback<ArrayList<ClassObject>> callback = new AsyncCallback<ArrayList<ClassObject>>(){
						public void onSuccess(ArrayList<ClassObject> results){
							vItem.removeItems(); // clear loading message
							vItem.setLoad(true);
							ArrayList<ClassObject> clsList = (ArrayList<ClassObject>) results;
							for (int i = 0; i < clsList.size(); i++) {
								ClassObject childObj = (ClassObject) clsList.get(i);
								TreeItemAOS childItem = new TreeItemAOS(childObj);
								vItem.addItem(childItem);
							}
						}
						public void onFailure(Throwable caught){
							Window.alert(constants.relGetTreeItemFail());
						}
					};
					Service.relationshipService.getClassItemList(cObj.getName(), MainApp.userOntology, callback);
				}
			}
		}
	}

	@Override
	protected void onSelected() {
	    
	    TreeAOS tree = (TreeAOS) this.getTree();
	    if(tree!=null)
	    {
			if(tree.getType() == TreeAOS.TYPE_CONCEPT)
			{
				Concept concept = (Concept) ModuleManager.getSelectedMainAppWidget();	
				concept.conceptTree.onTreeSelection(this);
			}
			else if(tree.getType() == TreeAOS.TYPE_CATEGORY)
			{
				Classification classification = (Classification) ModuleManager.getSelectedMainAppWidget();
				classification.scheme.onTreeSelection(this);
			}
			else if(tree.getType() == TreeAOS.TYPE_RELATIONSHIP)
			{
				Relationship relationship = (Relationship) ModuleManager.getSelectedMainAppWidget();
				relationship.relationshipTree.onTreeSelection(this);
			}
			else if(tree.getType() == TreeAOS.TYPE_RELATIONSHIP_BROWSER)
			{
				Widget w = tree.getParent().getParent().getParent().getParent().getParent();
				if(w instanceof RelationshipBrowser)
				{
					RelationshipBrowser relationshipBrowser = (RelationshipBrowser) w;
					relationshipBrowser.onTreeSelection(this);
				}
			}
	    }
	}

}