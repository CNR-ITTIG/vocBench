package org.fao.aoscs.client.module.relationship.widgetlib;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.OWLActionConstants;
import org.fao.aoscs.client.module.relationship.RelationshipTemplate;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabelAOS;
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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;

public class RelationshipDefinition extends RelationshipTemplate{
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private HashMap<String, String> languageMap = new HashMap<String, String>();
	
	private AddNewDefinition addNewDefinition;
	private EditDefinition editDefinition;
	private DeleteDefinition deleteDefinition;
	
	public RelationshipDefinition(PermissionObject permissionTable, InitializeRelationshipData initData, RelationshipDetailTab detailPanel) {
		super(permissionTable, initData, detailPanel);
		ArrayList<String[]> lang = (ArrayList<String[]>) MainApp.getLanguage();
		for(int i=0;i<lang.size();i++){
			String [] item = (String[]) lang.get(i);
			String code = item[1]; 
			String label = item[0]; 
			languageMap.put(code, label);
 		}
	}

	private void attachNewImgButton(){
		// create new term button
		functionPanel.clear();
		LinkLabelAOS add = new LinkLabelAOS("images/add-grey.gif", "images/add-grey-disabled.gif", constants.relAddNewDefinition(), constants.relAddNewDefinition(), permissionTable.contains(OWLActionConstants.RELATIONSHIPEDIT_DEFINITIONCREATE, -1), new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(addNewDefinition == null || !addNewDefinition.isLoaded)
					addNewDefinition = new AddNewDefinition();
				addNewDefinition.show();
			}
		});		
		this.functionPanel.add(add);
	}
	
	private HorizontalPanel getFunctionButton(final String definition,final String language){
		HorizontalPanel hp = new HorizontalPanel();
		
		hp.setSpacing(2);
		LinkLabelAOS edit = new LinkLabelAOS("images/edit-grey.gif", "images/edit-grey-disabled.gif", constants.relEditDefinition(), permissionTable.contains(OWLActionConstants.RELATIONSHIPEDIT_DEFINITIONEDIT, -1), new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(editDefinition == null || !editDefinition.isLoaded)
					editDefinition = new EditDefinition(definition,language,relationshipObject);
				editDefinition.show();
			}
		});
		LinkLabelAOS delete = new LinkLabelAOS("images/delete-grey.gif", "images/delete-grey-disabled.gif", constants.relDeleteRelationship(), permissionTable.contains(OWLActionConstants.RELATIONSHIPEDIT_DEFINITIONDELETE, -1), new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(deleteDefinition == null || !deleteDefinition.isLoaded)
					deleteDefinition = new DeleteDefinition(definition,language,relationshipObject);
				deleteDefinition.show();
			}
		});
		
		hp.add(edit);
		hp.add(delete);
		
		hp.add(new HTML(definition));
		
		return hp;
	}
	
	public void initLayout(){
		this.sayLoading();
		AsyncCallback<ArrayList<LabelObject>> callback = new AsyncCallback<ArrayList<LabelObject>>(){
			public void onSuccess(ArrayList<LabelObject> list) {
				clearPanel();
				attachNewImgButton();
				
				if(!list.isEmpty()){
					Grid table = new Grid(list.size()+1,2);
					table.setWidget(0, 0, new HTML(constants.relLanguage()));
					table.setWidget(0, 1, new HTML(constants.relDefinition()));
					
					for (int i = 0; i < list.size(); i++) {
						LabelObject l = (LabelObject) list.get(i);
						table.setWidget(i+1, 0, new HTML((String)languageMap.get(l.getLanguage())));
						table.setWidget(i+1, 1, getFunctionButton(l.getLabel(), l.getLanguage()));
					}
					table.getColumnFormatter().setWidth(1, "80%");
					relationshipRootPanel.add(GridStyle.setTableConceptDetailStyleTop(table,"gstFR1","gstFC1","gstR1","gstPanel1",true));
				}
				else{					
					Label sayNo = new Label(constants.relNoDefinition());
					relationshipRootPanel.add(sayNo);
					relationshipRootPanel.setCellHorizontalAlignment(sayNo, HasHorizontalAlignment.ALIGN_CENTER);
				}
				detailPanel.tabPanel.getTabBar().setTabHTML(1, Convert.replaceSpace(list.size()>1?constants.relDefinitions():constants.relDefinition()+"&nbsp;("+(list.size())+")"));
			}
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
		};
		Service.relationshipService.getRelationshipComments(relationshipObject.getName(), MainApp.userOntology, callback);
	}
	public class AddNewDefinition extends FormDialogBox {  
		public TextArea tb ;
		public ListBox language ;
		
		public AddNewDefinition(){ 
			super(constants.buttonCreate(), constants.buttonCancel());
			setWidth("400px");
			this.setText(constants.relCreateDefinition());
			this.initLayout();
		}
		
		public void initLayout(){
			language = new ListBox();
			language = Convert.makeListBoxWithValue((ArrayList<String[]>)MainApp.getLanguage());
			language.setWidth("100%");
			
			tb = new TextArea();
			tb.setWidth("100%");
			tb.setVisibleLines(3);
			
			Grid table = new Grid(2,2);
			table.setWidget(0, 0, new HTML(constants.relDefinition()));
			table.setWidget(1, 0, new HTML(constants.relLanguage()));
			table.setWidget(0, 1, tb);
			table.setWidget(1, 1, language);
			table.getColumnFormatter().setWidth(1, "80%");
			table.setWidth("100%");
			
			addWidget(GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1"));
		}
	
		  
		public boolean passCheckInput() {
			boolean pass = false;
			if(language.getValue((language.getSelectedIndex())).equals("") || tb.getText().equals("") ){
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
					RelationshipDefinition.this.setURI(relationshipObject);
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.relAddDefinitionFail());
				}
			};
			String label = tb.getText();
			String lang = language.getValue((language.getSelectedIndex()));
		/*	OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditDefinitionCreate);*/
			int actionId = 51;
			
			Service.relationshipService.AddPropertyComment(relationshipObject, label,lang, actionId , MainApp.userId , MainApp.userOntology, callback);
		}
	}
	public class EditDefinition extends FormDialogBox {  
		public TextArea tb ;
		public ListBox language ;
		private String label;
		private String lang;
		private RelationshipObject relationshipObject;
		public EditDefinition(String label, String language,RelationshipObject relationshipObject){ 
			super();
			this.label = label;
			this.lang = language;
			this.relationshipObject = relationshipObject;
			setWidth("400px");
			this.setText(constants.relCreateDefinition());
			this.initLayout();
		}
		
		public void initLayout(){
			language = new ListBox();
			language = Convert.makeSelectedLanguageListBox((ArrayList<String[]>)MainApp.getLanguage(),lang);
			language.setWidth("100%");
			
			tb = new TextArea();
			tb.setVisibleLines(3);
			tb.setText(label);
			tb.setWidth("100%");
			
			Grid table = new Grid(2,2);
			table.setWidget(0, 0, new HTML(constants.relDefinition()));
			table.setWidget(1, 0, new HTML(constants.relLanguage()));
			table.setWidget(0, 1, tb);
			table.setWidget(1, 1, language);
			table.getColumnFormatter().setWidth(1, "80%");
			table.setWidth("100%");
			
			addWidget(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));
		}
		
		public boolean passCheckInput() {
			boolean pass = false;
			if(language.getValue((language.getSelectedIndex())).equals("") || tb.getText().equals("") ){
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
					RelationshipDefinition.this.setURI(relationshipObject);
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.relEditDefinitionFail());
				}
			};

		//	OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditDefinitionCreate);
			int actionId = 52;
			
			String newLabel = tb.getText();
			String newLanguage = language.getValue((language.getSelectedIndex()));
			Service.relationshipService.EditPropertyComment(relationshipObject, label,lang, newLabel,newLanguage,actionId , MainApp.userId, MainApp.userOntology, callback);
		}
	}
	public class DeleteDefinition extends FormDialogBox implements ClickHandler{
		private RelationshipObject relationshipObject ;
		private String label;
		private String lang;
		public DeleteDefinition(String label,String language,RelationshipObject relationshipObject ){
			super(constants.buttonDelete(), constants.buttonCancel());
			this.label = label;
			this.lang = language;
			this.relationshipObject = relationshipObject;
			
			setWidth("400px");
			setText(constants.relDeleteDefinition());
			initLayout();
		}
		
		public void initLayout() {
			HTML message = new HTML(constants.relDeleteDefinitionWarning());
			
			Grid table = new Grid(1,2);
			table.setWidget(0, 0,getWarningImage());
			table.setWidget(0, 1, message);
			
			addWidget(table);
		}

	    
	    public void onSubmit() {
	    	DeleteDefinition.this.hide();
			sayLoading();
			
			
			AsyncCallback<Object> callback = new AsyncCallback<Object>(){
				public void onSuccess(Object results){
					RelationshipDefinition.this.setURI(relationshipObject);
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.relDeleteDefinitionFail());
				}
			};
		//	OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditDefinitionDelete);
			int actionId = 53;			
			Service.relationshipService.DeletePropertyComment(relationshipObject, label, lang, actionId, MainApp.userId, MainApp.userOntology, callback);
	    }
		
	}
}
