package org.fao.aoscs.client.module.relationship.widgetlib;

import java.util.ArrayList;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.OWLActionConstants;
import org.fao.aoscs.client.module.constant.OWLProperties;
import org.fao.aoscs.client.module.relationship.RelationshipTemplate;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabelAOS;
import org.fao.aoscs.domain.InitializeRelationshipData;
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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

public class RelationshipProperty extends RelationshipTemplate{
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private ArrayList<String> propList = new ArrayList<String>();
	
	private AddNewProperty addNewProperty;
	private DeleteProperty deleteProperty;
	
	
	public RelationshipProperty(PermissionObject permissionTable, InitializeRelationshipData initData, RelationshipDetailTab detailPanel) {
		super(permissionTable, initData, detailPanel);
	}
    
	private void attachNewImgButton(){
		// create new term button
		
		functionPanel.clear();
		LinkLabelAOS add = new LinkLabelAOS("images/add-grey.gif", "images/add-grey-disabled.gif", constants.relAddNewProperty(), constants.relAddNewProperty(), permissionTable.contains(OWLActionConstants.RELATIONSHIPEDIT_PROPERTYCREATE, -1), new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(addNewProperty == null || !addNewProperty.isLoaded)
					addNewProperty = new AddNewProperty();
				addNewProperty.show();
			}
		});
		this.functionPanel.add(add);
		
	}
	private HorizontalPanel getFunctionButton(final String label,final String URI){
		HorizontalPanel hp = new HorizontalPanel();
		//if(permissionTable.get("concept-def-edit").equals("enable"))
		{
			hp.setSpacing(2);
		
			LinkLabelAOS delete = new LinkLabelAOS("images/delete-grey.gif", "images/delete-grey-disabled.gif", constants.relDeleteProperty(), permissionTable.contains(OWLActionConstants.RELATIONSHIPEDIT_PROPERTYDELETE, -1), new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(deleteProperty == null || !deleteProperty.isLoaded)
						deleteProperty = new DeleteProperty(label, URI ,relationshipObject);
					deleteProperty.show();
				}
			});			
			hp.add(delete);
		}
		hp.add(new HTML(label));
		
		return hp;
	}
	public void initLayout(){
		this.sayLoading();
		AsyncCallback<ArrayList<String>> callback = new AsyncCallback<ArrayList<String>>(){
			public void onSuccess(ArrayList<String> result) {
				clearPanel();
				attachNewImgButton();
				propList = (ArrayList<String>) result;

				if(!propList.isEmpty()){
					Grid table = new Grid(propList.size()+1,1);
					table.setWidget(0, 0, new HTML(constants.relProperties()));					
					int index = 1;
					for (int i = 0; i < propList.size(); i++) {
						String  column = (String) propList.get(i);
						String propURI = column;
						String propLabel = column.replaceAll("owl:", "");
						table.setWidget(index++, 0, getFunctionButton(propLabel,propURI));
					}
					relationshipRootPanel.add(GridStyle.setTableConceptDetailStyleTop(table,"gstFR1","gstFC1","gstR1","gstPanel1",true));
				}else{
					Label sayNo = new Label(constants.relNoProperty());
					relationshipRootPanel.add(sayNo);
					relationshipRootPanel.setCellHorizontalAlignment(sayNo, HasHorizontalAlignment.ALIGN_CENTER);
				}
				detailPanel.tabPanel.getTabBar().setTabHTML(2, Convert.replaceSpace((propList.size()>1?constants.relProperties():constants.relProperty())+"&nbsp;("+(propList.size())+")"));
			}
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
		};
		
		Service.relationshipService.getRelationshipProperties(relationshipObject.getName(),  MainApp.userOntology, callback);
	}
	public class AddNewProperty extends FormDialogBox {  
		public ListBox prop ;
		
		public AddNewProperty()
		{ 
			super(constants.buttonCreate(), constants.buttonCancel());
			setWidth("400px");
			this.setText(constants.relCreateProperty());
			this.initLayout();
		}
		
		public void initLayout(){
			prop = new ListBox();
			
			prop.addItem("--None--","");
			if(!propList.contains(OWLProperties.FUNCTIONAL)){
				prop.addItem("FunctionalProperty",OWLProperties.FUNCTIONAL);
			}
			if(relationshipObject.getType().equalsIgnoreCase(RelationshipObject.OBJECT)){
				if(!propList.contains(OWLProperties.INVERSEFUNCTIONAL)){
					prop.addItem("InverseFunctionProperty",OWLProperties.INVERSEFUNCTIONAL);
				}
				if(!propList.contains(OWLProperties.SYMMETRIC)){
					prop.addItem("SymmetricProperty",OWLProperties.SYMMETRIC);
				}
				if(!propList.contains(OWLProperties.TRANSITIVE)){
					prop.addItem("TransitiveProperty",OWLProperties.TRANSITIVE);
				}
			}
			prop.setWidth("100%");
			
			Grid table = new Grid(1,2);
			table.setWidget(0, 0, new HTML(constants.relProperty()));			
			table.setWidget(0, 1, prop);
			table.getColumnFormatter().setWidth(1, "80%");
			table.setWidth("100%");
			
			addWidget(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));
		}
		
		public boolean passCheckInput() {
			boolean pass = false;
			if(prop.getValue((prop.getSelectedIndex())).equals("")){
				pass = false;
			}else {
				pass = true;
			}
			return pass;
		}
		  
		public void onSubmit(){
			sayLoading();
			
			AsyncCallback<Object> callback = new AsyncCallback<Object>(){
				public void onSuccess(Object results){
					RelationshipProperty.this.setURI(relationshipObject);
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.relAddPropertyFail());
				}
			};

		//	OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditDefinitionCreate);
			int actionId = 54;
			Service.relationshipService.addProperty(relationshipObject, prop.getValue((prop.getSelectedIndex())), actionId, MainApp.userId, MainApp.userOntology, callback);
		}
	}
	
	public class DeleteProperty extends FormDialogBox implements ClickHandler{
		private RelationshipObject relationshipObject ;
		private String uri;
		public DeleteProperty(String label,String uri,RelationshipObject relationshipObject ){
			super(constants.buttonDelete(), constants.buttonCancel());
			this.uri = uri;
			this.relationshipObject = relationshipObject;
			
			setWidth("400px");
			setText(constants.relDeleteProperty());
			initLayout();
		}
		
		public void initLayout() {
		    HTML message = new HTML(constants.relDeletePropertyWarning());
			
			Grid table = new Grid(1,2);
			table.setWidget(0, 0,getWarningImage());
			table.setWidget(0, 1, message);
			
			addWidget(table);
		}

	    
	    public void onSubmit() {
	    	DeleteProperty.this.hide();
			sayLoading();
			
			AsyncCallback<Object> callback = new AsyncCallback<Object>(){
				public void onSuccess(Object results){
					RelationshipProperty.this.setURI(relationshipObject);
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.relDeletePropertyFail());
				}
			};
			int actionId = 55;
			Service.relationshipService.deleteProperty(relationshipObject, uri, actionId, MainApp.userId, MainApp.userOntology, callback);
			
	    }
		
	}
}
