package org.fao.aoscs.client.module.relationship.widgetlib;

import java.util.ArrayList;
import java.util.Iterator;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.locale.LocaleMessages;
import org.fao.aoscs.client.module.constant.OWLActionConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.module.relationship.RelationshipTemplate;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.dialog.RelationshipBrowser;
import org.fao.aoscs.client.widgetlib.shared.label.LabelAOS;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabelAOS;
import org.fao.aoscs.client.widgetlib.shared.tree.LazyLoadingTree;
import org.fao.aoscs.domain.InitializeRelationshipData;
import org.fao.aoscs.domain.LabelObject;
import org.fao.aoscs.domain.PermissionObject;
import org.fao.aoscs.domain.RelationshipObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class RelationshipInverseProperty extends RelationshipTemplate {
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private LocaleMessages messages = (LocaleMessages) GWT.create(LocaleMessages.class);
	private AddNewInverseProperty addNewInverseProperty;
	private EditInverseProperty editInverseProperty;
	private DeleteInverseProperty deleteInverseProperty;


	public RelationshipInverseProperty(PermissionObject permissionTable, InitializeRelationshipData initData, RelationshipDetailTab detailPanel) {
		super(permissionTable, initData, detailPanel);
	}
	private void attachNewImgButton(){
		// create new term button
		
		functionPanel.clear();
		LinkLabelAOS add = new LinkLabelAOS("images/add-grey.gif", "images/add-grey-disabled.gif", constants.relAddNewInvProperty(), permissionTable.contains(OWLActionConstants.RELATIONSHIPEDIT_INVERSEPROPERTYCREATE, -1), new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(addNewInverseProperty == null || !addNewInverseProperty.isLoaded)
					addNewInverseProperty = new AddNewInverseProperty();
				addNewInverseProperty.show();
			}
		});			
		this.functionPanel.add(add);
	}
	
	private HorizontalPanel makeRelationshipIcon(RelationshipObject insRObj){
		HorizontalPanel hp = new HorizontalPanel();
		if(insRObj.getType().equals(RelationshipObject.OBJECT)){
			hp.add(new Image("images/relationship-object-logo.gif"));
		}else{
			hp.add(new Image("images/relationship-datatype-logo.gif"));
		}
		
		ArrayList<LabelObject> list = insRObj.getLabelList();
		Iterator<LabelObject> iter = list.iterator();
		String str = "";
		while (iter.hasNext()) {
			LabelObject labelObj = (LabelObject) iter.next();
			String languageCode = labelObj.getLanguage();
			String label = labelObj.getLabel();
			str = str + label + "("+languageCode+")" + ",";
		}
		if(str.length()>1){
			str = str.substring(0, str.length()-1);
		}
		hp.add(new LabelAOS(str,insRObj));
		return hp;
	}
	
	private HorizontalPanel getFunctionalButton(final RelationshipObject insRObj){
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(2);
		LinkLabelAOS edit = new LinkLabelAOS("images/edit-grey.gif", "images/edit-grey-disabled.gif", constants.relEditInvProperty(), permissionTable.contains(OWLActionConstants.RELATIONSHIPEDIT_INVERSEPROPERTYEDIT, -1), new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(editInverseProperty == null || !editInverseProperty.isLoaded)
					editInverseProperty = new EditInverseProperty(insRObj);
				editInverseProperty.show();
			}
		});
		
		LinkLabelAOS delete = new LinkLabelAOS("images/delete-grey.gif", "images/delete-grey-disabled.gif", constants.relDeleteInvProperty(), permissionTable.contains(OWLActionConstants.RELATIONSHIPEDIT_INVERSEPROPERTYDELETE, -1), new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(deleteInverseProperty == null || !deleteInverseProperty.isLoaded)
					deleteInverseProperty = new DeleteInverseProperty(insRObj);
				deleteInverseProperty.show();
			}
		});
			
		hp.add(edit);
		hp.add(delete);
		
		hp.add(makeRelationshipIcon(insRObj));
		return hp;
	}
	
	public void initLayout(){
		this.sayLoading();
		AsyncCallback<Object> callback = new AsyncCallback<Object>(){
			public void onSuccess(Object result) {
				clearPanel();
				attachNewImgButton();
				RelationshipObject insRobj = (RelationshipObject) result;
				
				if(insRobj!=null){
					functionPanel.clear();
					Grid table = new Grid(2,1);
					table.setWidget(0, 0, new HTML(constants.relInvProperty()));									
					table.setWidget(1, 0, getFunctionalButton(insRobj));
					relationshipRootPanel.add(GridStyle.setTableConceptDetailStyleTop(table,"gstFR1","gstFC1","gstR1","gstPanel1",true));
					detailPanel.tabPanel.getTabBar().setTabHTML(3, Convert.replaceSpace(constants.relInvProperty()) + "&nbsp;(1)");
				}
				else{
					detailPanel.tabPanel.getTabBar().setTabHTML(3, Convert.replaceSpace(constants.relInvProperty()+"&nbsp;(0)"));
					Label sayNo = new Label(constants.relNoInvProperty());
					relationshipRootPanel.add(sayNo);
					relationshipRootPanel.setCellHorizontalAlignment(sayNo, HasHorizontalAlignment.ALIGN_CENTER);
				}
			}
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
		};
		Service.relationshipService.getInverseProperty(MainApp.userOntology, relationshipObject.getName(), callback);
	}
	private class AddNewInverseProperty extends FormDialogBox  {
		private Image relationshipBrowse;
		private String imgPath = "images/browseButton3-grey.gif";
		private LabelAOS relationship;
		
		public AddNewInverseProperty(){
			super(constants.buttonCreate(), constants.buttonCancel());
			this.setText(constants.relCreateInvProperty());
			setWidth("400px");
			this.initLayout();
		}
		private HorizontalPanel getRelationshipBrowserButton(){
			relationship = new LabelAOS("--None--",null);
			
			relationshipBrowse = new Image(imgPath);
			relationshipBrowse.addClickHandler(this);
			relationshipBrowse.setStyleName(Style.Link);
			relationshipBrowse.addClickHandler(new ClickHandler() 
			{
				public void onClick(ClickEvent event) {
					final RelationshipBrowser rb =((MainApp) RootPanel.get().getWidget(0)).relationshipBrowser; 
					rb.showBrowser(RelationshipBrowser.REL_ALL);
					rb.addSubmitClickHandler(new ClickHandler()
					{
						public void onClick(ClickEvent event) {
							relationship.setText(rb.getSelectedItem(),rb.getRelationshipObject());
						}					
					});						
				}
			});
			HorizontalPanel hp = new HorizontalPanel();
			hp.add(relationship);
			hp.add(relationshipBrowse);
			hp.setWidth("100%");
			hp.setCellHorizontalAlignment(relationship, HasHorizontalAlignment.ALIGN_LEFT);
			hp.setCellHorizontalAlignment(relationshipBrowse, HasHorizontalAlignment.ALIGN_RIGHT);
			
			return hp;
		}
		
		public void initLayout() {
			Grid table = new Grid(1,2);
			table.setWidget(0, 0, new HTML(constants.relRelationship()));			
			table.setWidget(0, 1, getRelationshipBrowserButton());
			table.setWidth("100%");
			table.getColumnFormatter().setWidth(1,"80%");
			
			addWidget(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));
		};
		public boolean passCheckInput() {
			boolean pass = false;
			if(relationship.getValue()!=null){
				pass = true;
			}
			return pass;
		}
	
		public void onSubmit() {
			sayLoading();
			RelationshipObject rObj = (RelationshipObject) relationship.getValue();
			
			AsyncCallback<Object>  callback = new AsyncCallback<Object>(){
				public void onSuccess(Object results){
					RelationshipInverseProperty.this.setURI(relationshipObject);
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.relAddInvPropertyFail());
				}
			};
			int actionId = 56;
			Service.relationshipService.setInverseProperty(actionId, MainApp.userId , MainApp.userOntology, relationshipObject, rObj.getName(), callback);
		}
		
	}
	private HorizontalPanel makeRelationshipIcon(RelationshipObject insRObj,LabelAOS lab){
		HorizontalPanel hp = new HorizontalPanel();
		if(insRObj.getType().equals(RelationshipObject.OBJECT)){
			hp.add(new Image("images/relationship-object-logo.gif"));
		}else{
			hp.add(new Image("images/relationship-datatype-logo.gif"));
		}
		
		
		ArrayList<LabelObject> list = insRObj.getLabelList();
		Iterator<LabelObject> iter = list.iterator();
		String str = "";
		while (iter.hasNext()) {
			LabelObject labelObj = (LabelObject) iter.next();
			String languageCode = labelObj.getLanguage();
			String label = labelObj.getLabel();
			str = str + label + "("+languageCode+")" + ",";
		}
		if(str.length()>1){
			str = str.substring(0, str.length()-1);
		}
		lab.setText(str,insRObj);
		hp.add(new HTML("&nbsp"));
		hp.add(lab);
		return hp;
	}
	
	public class EditInverseProperty extends FormDialogBox {  
		private Image relationshipBrowse;
		private String imgPath = "images/browseButton3-grey.gif";
		private LabelAOS relationship;
		private RelationshipObject rObj;
		
		public EditInverseProperty(RelationshipObject rObj){
			super();
			this.rObj = rObj;
			this.setText(constants.relEditInvProperty());
			setWidth("400px");
			this.initLayout();
		}
		private HorizontalPanel getRelationshipBrowserButton(){
			relationship = new LabelAOS();
			makeRelationshipIcon(rObj, relationship);
			
			relationshipBrowse = new Image(imgPath);
			relationshipBrowse.addClickHandler(this);
			relationshipBrowse.setStyleName(Style.Link);
			relationshipBrowse.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					final RelationshipBrowser rb =((MainApp) RootPanel.get().getWidget(0)).relationshipBrowser; 
					rb.showBrowser(RelationshipBrowser.REL_ALL);
					rb.addSubmitClickHandler(new ClickHandler()
					{
						public void onClick(ClickEvent event) {
							relationship.setText(rb.getSelectedItem(),rb.getRelationshipObject());
						}					
					});		
				}
			});
			
			HorizontalPanel hp = new HorizontalPanel();
			hp.add(relationship);
			hp.add(relationshipBrowse);
			hp.setWidth("100%");
			hp.setCellHorizontalAlignment(relationship, HasHorizontalAlignment.ALIGN_LEFT);
			hp.setCellHorizontalAlignment(relationshipBrowse, HasHorizontalAlignment.ALIGN_RIGHT);
			
			return hp;
		}
		
		public void initLayout() {
			Grid table = new Grid(1,2);
			table.setWidget(0, 0, new HTML(constants.relRelationship()));			
			table.setWidget(0, 1, getRelationshipBrowserButton());
			table.setWidth("100%");
			table.getColumnFormatter().setWidth(1,"80%");
			
			addWidget(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));
		};
		
		public boolean passCheckInput() {
			boolean pass = false;
			if(relationship.getValue()!=null){
				pass = true;
			}
			return pass;
		}
	
		public void onSubmit() {
			sayLoading();
			RelationshipObject rObj = (RelationshipObject) relationship.getValue();
			AsyncCallback<Object>  callback = new AsyncCallback<Object>(){
				public void onSuccess(Object results){
					RelationshipInverseProperty.this.setURI(relationshipObject);
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.relEditInvPropertyFail());
				}
			};
			int actionId = 57;
			Service.relationshipService.setInverseProperty(actionId , MainApp.userId, MainApp.userOntology, relationshipObject, rObj.getName(), callback);
		}
	}
	
	private class DeleteInverseProperty extends FormDialogBox implements ClickHandler{
		private RelationshipObject rObj ;
		public DeleteInverseProperty(RelationshipObject rObj){
			super(constants.buttonDelete(), constants.buttonCancel());
			this.rObj = rObj;
			this.setText(constants.relDeleteInvProperty());
			setWidth("400px");
			this.initLayout();
			
		}
	
		public void initLayout() {
			
			HTML message = new HTML(messages.relDeleteInvPropertyWarning(LazyLoadingTree.getRelationshipLabel(rObj)));
			Grid table = new Grid(1,2);
			table.setWidget(0,0,getWarningImage());
			table.setWidget(0,1, message);
			
			addWidget(table);
		}
		public void onSubmit() {
			sayLoading();
			AsyncCallback<Object> callback = new AsyncCallback<Object>(){
				public void onSuccess(Object results){
					RelationshipInverseProperty.this.setURI(relationshipObject);
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.relDeleteInvPropertyFail());
				}
			};
			int actionId = 48;
			Service.relationshipService.deleteInverseProperty(actionId , MainApp.userId, MainApp.userOntology, relationshipObject, callback);
		}
		
	}
}
