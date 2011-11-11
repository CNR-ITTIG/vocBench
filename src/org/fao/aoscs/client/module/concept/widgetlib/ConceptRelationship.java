package org.fao.aoscs.client.module.concept.widgetlib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.locale.LocaleMessages;
import org.fao.aoscs.client.module.classification.widgetlib.ClassificationDetailTab;
import org.fao.aoscs.client.module.concept.ConceptTemplate;
import org.fao.aoscs.client.module.constant.ConceptActionKey;
import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.client.module.constant.OWLActionConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.ConceptBrowser;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.dialog.RelationshipBrowser;
import org.fao.aoscs.client.widgetlib.shared.label.LabelAOS;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabelAOS;
import org.fao.aoscs.client.widgetlib.shared.tree.LazyLoadingTree;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.PermissionObject;
import org.fao.aoscs.domain.RelationObject;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.TermObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

class ConceptRelationship extends ConceptTemplate{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private LocaleMessages messages = (LocaleMessages) GWT.create(LocaleMessages.class);
	private AddNewRelationship addNewRelationship;
	private EditRelationship editRelationship;
	private DeleteRelationship deleteRelationship;
	
	public ConceptRelationship(PermissionObject permisstionTable,InitializeConceptData initData, ConceptDetailTabPanel conceptDetailPanel, ClassificationDetailTab classificationDetailPanel){
		super(permisstionTable, initData, conceptDetailPanel, classificationDetailPanel);	
	}
	
	private void attachNewImgButton(){	
		functionPanel.clear();
		boolean permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_RELATIONSHIPCREATE, getConceptObject().getStatusID());
		LinkLabelAOS add = new LinkLabelAOS("images/add-grey.gif", "images/add-grey-disabled.gif", constants.conceptAddNewRelationship(), constants.conceptAddNewRelationship(), permission, new ClickHandler() {
			public void onClick(ClickEvent event){					
				if(addNewRelationship == null || !addNewRelationship.isLoaded)
					addNewRelationship = new AddNewRelationship();
				addNewRelationship.show();
			}
		});			
		this.functionPanel.add(add);
	}
	
	private HorizontalPanel convert2ConceptItem(final ConceptObject cObj){
		HorizontalPanel hp = new HorizontalPanel();
		if(cObj.getUri().startsWith(ModelConstants.ONTOLOGYBASENAMESPACE)){
			hp.add(new Image("images/concept_logo.gif"));
		}else{
			hp.add(new Image("images/category_logo.gif"));
		}
		hp.setSpacing(2);
		String labelstr = "";
		if(!cObj.getTerm().isEmpty())
		{
			Iterator<String> it = cObj.getTerm().keySet().iterator();
			while(it.hasNext()){
				String termIns = (String) it.next();
				TermObject tObj = (TermObject) cObj.getTerm().get(termIns);
				//if(tObj.isMainLabel())
				if(MainApp.userSelectedLanguage.contains(tObj.getLang()))
                {    
					if(!labelstr.equals(""))
						labelstr += ";&nbsp;";
					if(tObj.isMainLabel())
					{	
						labelstr += "<b>"+ tObj.getLabel() + "("+tObj.getLang()+")</b>";
						
					}
					else
					{
						labelstr += tObj.getLabel() + "("+tObj.getLang()+")";
					}
                }
			}
			if(labelstr.length()==0){
				labelstr = "<img src='images/label-not-found.gif'>";
			}
			HTML label = new HTML(labelstr);
			label.setStyleName(Style.Link);
			label.setTitle(cObj.getUri());
			label.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					ModuleManager.gotoItem(cObj.getUri(), cObj.getScheme(), true, InfoTab.relationship, cObj.getBelongsToModule(), ModuleManager.MODULE_CONCEPT);
				}
			});
			
			hp.add(label);
		}
		return hp;
	}
	
	private Grid getDestinationConceptTable(final RelationshipObject rObj,ArrayList<ConceptObject> conceptList)
	{
		Grid table = new Grid(conceptList.size(),1);
		for (int i = 0; i < conceptList.size(); i++) 
		{
			final ConceptObject cObj = (ConceptObject) conceptList.get(i);
			HorizontalPanel hp = new HorizontalPanel();
				 
			hp.clear();
			hp.setSpacing(3);
			boolean permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_RELATIONSHIPEDIT, getConceptObject().getStatusID()); 
			LinkLabelAOS edit = new LinkLabelAOS("images/edit-grey.gif", "images/edit-grey-disabled.gif", constants.conceptEditRelationship(), permission, new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(editRelationship == null || !editRelationship.isLoaded)
						editRelationship = new EditRelationship(rObj,cObj);
					editRelationship.show();
				}
			});
			
			permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_RELATIONSHIPDELETE, getConceptObject().getStatusID());
			LinkLabelAOS delete = new LinkLabelAOS("images/delete-grey.gif", "images/delete-grey-disabled.gif", constants.conceptDeleteRelationship(), permission, new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(deleteRelationship == null || !deleteRelationship.isLoaded)
						deleteRelationship = new DeleteRelationship(rObj,cObj);
					deleteRelationship.show();
				}
			});
			
			hp.add(edit);
			hp.add(delete);
			hp.setCellVerticalAlignment(edit, HasVerticalAlignment.ALIGN_MIDDLE);
			hp.setCellVerticalAlignment(delete, HasVerticalAlignment.ALIGN_MIDDLE);
			
			hp.add(convert2ConceptItem(cObj));
			table.setWidget(i,0,hp);
			table.setWidth("100%");
		}
		return table;
	}
	
	public void initLayout(){
		this.sayLoading();
		if(cDetailObj!=null && cDetailObj.getRelationObject()!=null)
		{
			initData(cDetailObj.getRelationObject());
		}
		else
		{
			AsyncCallback<RelationObject> callback = new AsyncCallback<RelationObject>(){
				public void onSuccess(RelationObject results) {
					cDetailObj.setRelationObject(results);
					initData(results);
				}
				public void onFailure(Throwable caught) {
					Window.alert(constants.conceptGetRelationshipFail());
				}
			};
			Service.conceptService.getConceptRelationship(conceptObject.getName(), MainApp.userOntology, callback);
		}
	}
	
	private void initData(RelationObject relationObj)
	{
		clearPanel();
		 attachNewImgButton(); 
		 
		 if(relationObj.hasValue()){
			 HashMap<RelationshipObject, ArrayList<ConceptObject>> relationConceptList = relationObj.getResult();
			 Grid table = new Grid(relationConceptList.size()+1,2);
			 table.setWidget(0, 0, new HTML(constants.conceptRelationship()));
			 table.setWidget(0, 1, new HTML(constants.conceptTitle()));
			 Iterator<RelationshipObject> it = relationConceptList.keySet().iterator();
			 int count = 0;
			 int i=1;
			 while(it.hasNext()){
				 RelationshipObject rObj = (RelationshipObject) it.next();
				 ArrayList<ConceptObject> conceptList =  (ArrayList<ConceptObject>) relationConceptList.get(rObj);
				 table.setWidget(i, 0, new HTML(LazyLoadingTree.getRelationshipLabel(rObj)));
				 table.setWidget(i, 1, getDestinationConceptTable(rObj,conceptList));
				 i++;
				 count += conceptList.size();
			 }
			 if(conceptObject.getBelongsToModule()==ConceptObject.CONCEPTMODULE) conceptDetailPanel.tabPanel.getTabBar().setTabHTML(InfoTab.relationship, Convert.replaceSpace(count>1?constants.conceptRelationships():constants.conceptRelationship())+"&nbsp;("+(count)+")");
			 if(conceptObject.getBelongsToModule()==ConceptObject.CLASSIFICATIONMODULE) classificationDetailPanel.tab2Panel.getTabBar().setTabHTML(InfoTab.relationship, Convert.replaceSpace(count>1?constants.conceptRelationships():constants.conceptRelationship())+"&nbsp;("+(count)+")");
			 conceptRootPanel.add(GridStyle.setTableConceptDetailStyleTop(table,"gstFR1","gstFC1","gstR1","gstPanel1",true));
		 }else{
			 Label sayNo = new Label(constants.conceptNoRelationships());
			 if(conceptObject.getBelongsToModule()==ConceptObject.CONCEPTMODULE) conceptDetailPanel.tabPanel.getTabBar().setTabHTML(InfoTab.relationship, Convert.replaceSpace(constants.conceptRelationship())+"&nbsp;(0)");
			 if(conceptObject.getBelongsToModule()==ConceptObject.CLASSIFICATIONMODULE) classificationDetailPanel.tab2Panel.getTabBar().setTabHTML(InfoTab.relationship, Convert.replaceSpace(constants.conceptRelationship())+"&nbsp;(0)");
			 conceptRootPanel.add(sayNo);
			 conceptRootPanel.setCellHorizontalAlignment(sayNo, HasHorizontalAlignment.ALIGN_CENTER);
		 }	
	}
	
	private class AddNewRelationship extends FormDialogBox implements ClickHandler{
		private Image browse ;
		private Image relationshipBrowse;
		private String imgPath = "images/browseButton3-grey.gif";
		private LabelAOS destConcept;
		private LabelAOS relationship;
			 
		public AddNewRelationship(){
			super(constants.buttonCreate(), constants.buttonCancel());
			this.setText(constants.conceptCreateRelationshipToAnotheConcept());
			setWidth("400px");
			this.initLayout();
		}
		private HorizontalPanel getRelationshipBrowserButton(){
			relationship = new LabelAOS("--None--",null);
			
			relationshipBrowse = new Image(imgPath);
			relationshipBrowse.addClickHandler(this);
			relationshipBrowse.setStyleName(Style.Link);

			HorizontalPanel hp = new HorizontalPanel();
			hp.add(relationship);
			hp.add(relationshipBrowse);
			hp.setWidth("100%");
			hp.setCellHorizontalAlignment(relationship, HasHorizontalAlignment.ALIGN_LEFT);
			hp.setCellHorizontalAlignment(relationshipBrowse, HasHorizontalAlignment.ALIGN_RIGHT);
			
			return hp;
		}
		
		private HorizontalPanel getConceptBrowseButton()
		{
			destConcept = new LabelAOS("--None--",null);

			browse = new Image(imgPath);
			browse.addClickHandler(this);
			browse.setStyleName(Style.Link);

			HorizontalPanel hp = new HorizontalPanel();
			hp.add(destConcept);
			hp.add(browse);
			hp.setWidth("100%");
			hp.setCellHorizontalAlignment(destConcept, HasHorizontalAlignment.ALIGN_LEFT);
			hp.setCellHorizontalAlignment(browse, HasHorizontalAlignment.ALIGN_RIGHT);
			
			return hp;
		}
		
		public void onButtonClicked(Widget sender) {
			if(sender.equals(relationshipBrowse))
			{
				relationship.setText("--None--");
				final RelationshipBrowser rb =((MainApp) RootPanel.get().getWidget(0)).relationshipBrowser; 
				rb.showBrowser(RelationshipBrowser.REL_CONCEPT);
				rb.addSubmitClickHandler(new ClickHandler()
				{
					public void onClick(ClickEvent event) {
						relationship.setText(rb.getSelectedItem(),rb.getRelationshipObject());
					}					
				});										
			}else if(sender.equals(browse)){
				destConcept.setText("--None--");
				final ConceptBrowser cb =((MainApp) RootPanel.get().getWidget(0)).conceptBrowser; 
				cb.showBrowser();
				cb.addSubmitClickHandler(new ClickHandler()
				{
					public void onClick(ClickEvent event) {
						destConcept.setText(cb.getSelectedItem(),cb.getTreeObject().getName());
					}					
				});								
			}
		}
		public void initLayout() {
			
			Grid table = new Grid(2,2);
			table.setWidget(0, 0, new HTML(constants.conceptRelationship()));
			table.setWidget(1, 0,new HTML(constants.conceptDestination()));
			table.setWidget(0, 1, getRelationshipBrowserButton());
			table.setWidget(1, 1, getConceptBrowseButton());
			table.setWidth("100%");
			table.getColumnFormatter().setWidth(1,"80%");
			
			addWidget(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));
		};
		public boolean passCheckInput() {
			boolean pass = false;
			if(relationship==null ||  destConcept ==null)
			{
				pass = false;
			}
			else
			{
				RelationshipObject rObj = (RelationshipObject) relationship.getValue();
				String dObj = (String)destConcept.getValue();
				if(dObj==null || rObj==null)
				{
					pass = false;
				}
				else
				{
					if(rObj.getUri()==null || dObj==null)
					{
						pass = false;
					}
					else
					{
						if((((String)rObj.getUri()).length()==0) || (((String)dObj).length()==0))
						{
							pass = false;
						}
						else 
						{
							pass = true;
						}
							
					}
				}
			}
			return pass;
		}
	
		public void onSubmit() {
			sayLoading();
			String destConceptName = (String)destConcept.getValue();	
			RelationshipObject rObj = (RelationshipObject) relationship.getValue();
			
			AsyncCallback<RelationObject> callback = new AsyncCallback<RelationObject>(){
				public void onSuccess(RelationObject results){
					cDetailObj.setRelationObject(results);
					ConceptRelationship.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.conceptAddDefinitionFail());
				}
			};
			
			OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditRelationshipCreate);
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditRelationshipCreate));	
			
			Service.conceptService.addNewRelationship(MainApp.userOntology,rObj, conceptObject.getName(), destConceptName, status, actionId,MainApp.userId, callback);
		}
		
	}
	
	private class EditRelationship extends FormDialogBox implements ClickHandler{
		
		private String imgPath = "images/browseButton3-grey.gif";
		private LabelAOS destConcept ;
		private LabelAOS relationship ;
		private Image browse;
		private Image relationshipBrowse;
		private RelationshipObject oldRelationObj;
		private ConceptObject oldConceptObj;
		
		public EditRelationship(RelationshipObject oldRelationObj,ConceptObject oldConceptObj){
			super();
			this.oldConceptObj = oldConceptObj;
			this.oldRelationObj = oldRelationObj;
			//Window.alert(oldRelationObj.getUri());
			this.setText(constants.conceptEditRelationshipToAnotheConcept());
			setWidth("400px");
			this.initLayout();
			
		}
		
		public void initLayout() {
			
			Grid table = new Grid(2,2);
			table.setWidget(0, 0, new HTML(constants.conceptRelationship()));
			table.setWidget(1, 0, new HTML(constants.conceptDestination()));
			table.setWidget(0, 1, getRelationshipBrowserButton());
			table.setWidget(1, 1, getConceptBrowseButton());
			table.setWidth("100%");
			table.getColumnFormatter().setWidth(1,"80%");
			
			addWidget(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));
		};
		
		private HorizontalPanel getRelationshipBrowserButton(){
			relationship = new LabelAOS();
			relationship.setText(LazyLoadingTree.getRelationshipLabel(oldRelationObj), oldRelationObj);
			
			relationshipBrowse = new Image(imgPath);
			relationshipBrowse.addClickHandler(this);
			relationshipBrowse.setStyleName(Style.Link);

			HorizontalPanel hp = new HorizontalPanel();
			hp.add(relationship);
			hp.add(relationshipBrowse);
			hp.setWidth("100%");
			hp.setCellHorizontalAlignment(relationship, HasHorizontalAlignment.ALIGN_LEFT);
			hp.setCellHorizontalAlignment(relationshipBrowse, HasHorizontalAlignment.ALIGN_RIGHT);
			
			return hp;
		}
		
		private String getConceptLabel(ConceptObject cObj){
			String label = "";
			if(!cObj.getTerm().isEmpty()){
				Iterator<String> it = cObj.getTerm().keySet().iterator();
				while(it.hasNext()){
					String termIns = (String) it.next();
					TermObject tObj = (TermObject) cObj.getTerm().get(termIns);
					if(tObj.isMainLabel()){
						label = label + tObj.getLabel() + "("+tObj.getLang()+") ";
					}
				}
			}
			return label;
		}
		
		private HorizontalPanel getConceptBrowseButton(){
			destConcept = new LabelAOS();
			destConcept.setText(getConceptLabel(oldConceptObj),oldConceptObj.getName());
			
			browse = new Image(imgPath);
			browse.addClickHandler(this);
			browse.setStyleName(Style.Link);

			HorizontalPanel hp = new HorizontalPanel();
			hp.add(destConcept);
			hp.add(browse);
			hp.setWidth("100%");
			hp.setCellHorizontalAlignment(destConcept, HasHorizontalAlignment.ALIGN_LEFT);
			hp.setCellHorizontalAlignment(browse, HasHorizontalAlignment.ALIGN_RIGHT);
			
			return hp;
		}
		
		public void onButtonClicked(Widget sender) {
			if(sender.equals(relationshipBrowse))
			{
				final RelationshipBrowser rb =((MainApp) RootPanel.get().getWidget(0)).relationshipBrowser; 
				rb.showBrowser(RelationshipBrowser.REL_CONCEPT);
				rb.addSubmitClickHandler(new ClickHandler()
				{
					public void onClick(ClickEvent event) {
						relationship.setText(rb.getSelectedItem(),rb.getRelationshipObject());
					}					
				});			
			}else if(sender.equals(browse)){
				final ConceptBrowser cb =((MainApp) RootPanel.get().getWidget(0)).conceptBrowser; 
				cb.showBrowser();
				cb.addSubmitClickHandler(new ClickHandler()
				{
					public void onClick(ClickEvent event) {
						destConcept.setText(cb.getSelectedItem(),cb.getTreeObject().getName());
					}					
				});					
			}
		}
		
		public void onSubmit() {
			sayLoading();
			
			RelationshipObject rObjNew = (RelationshipObject)relationship.getValue();
			String destConceptName = (String) destConcept.getValue();
			
			AsyncCallback<RelationObject> callback = new AsyncCallback<RelationObject>(){
				public void onSuccess(RelationObject results){
					cDetailObj.setRelationObject(results);
					ConceptRelationship.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.conceptEditRelationshipFail());
				}
			};
			
			OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditRelationshipEdit);
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditRelationshipEdit));	
			
			Service.conceptService.editRelationship(MainApp.userOntology,oldRelationObj, rObjNew, conceptObject.getName(), oldConceptObj.getName(), destConceptName, status, actionId, MainApp.userId, callback);
		}		
	}
	
	private class DeleteRelationship extends FormDialogBox implements ClickHandler{
		private RelationshipObject rObj ;
		private ConceptObject destConceptObj;
		public DeleteRelationship(RelationshipObject rObj,ConceptObject destConceptObj){
			super(constants.buttonDelete(), constants.buttonCancel());
			this.rObj = rObj;
			this.destConceptObj = destConceptObj;
			this.setText(constants.conceptDeleteRelationship());
			setWidth("400px");
			this.initLayout();
			
		}
		private String getConceptLabel(ConceptObject cObj){
			String label = "";
			if(!cObj.getTerm().isEmpty()){
				Iterator<String> it = cObj.getTerm().keySet().iterator();
				while(it.hasNext()){
					String termIns = (String) it.next();
					TermObject tObj = (TermObject) cObj.getTerm().get(termIns);
					if(tObj.isMainLabel()){
						label = label + tObj.getLabel() + "("+tObj.getLang()+") ";
					}
				}
			}
			return label;
		}
		public void initLayout() {
			HTML message = new HTML(messages.conceptRelationshipDeleteWarning(LazyLoadingTree.getRelationshipLabel(rObj), getConceptLabel(destConceptObj)));
			
			Grid table = new Grid(1,2);
			table.setWidget(0,0,getWarningImage());
			table.setWidget(0,1, message);
			
			addWidget(table);
		}
		public void onSubmit() {
			sayLoading();
			AsyncCallback<RelationObject> callback = new AsyncCallback<RelationObject>(){
				public void onSuccess(RelationObject results){
					cDetailObj.setRelationObject(results);
					ConceptRelationship.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.conceptEditRelationshipFail());
				}
			};
			
			OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditRelationshipDelete);
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditRelationshipDelete));	
			
			Service.conceptService.deleteRelationship(MainApp.userOntology,rObj, conceptObject, destConceptObj, status, actionId, MainApp.userId, callback);
		}
		
	}
	
}
