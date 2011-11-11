package org.fao.aoscs.client.module.relationship.widgetlib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.OWLActionConstants;
import org.fao.aoscs.client.module.relationship.RelationshipTemplate;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabelAOS;
import org.fao.aoscs.client.widgetlib.shared.tree.SearchTree;
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
import com.google.gwt.user.client.ui.TextBox;

public class RelationshipLabel extends RelationshipTemplate{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private RelationshipTree rTree;
	private AddNewLabel addNewLabel;
	private EditLabel editLabel;
	private DeleteLabel deleteLabel;
	
	public RelationshipLabel(RelationshipTree rTree, PermissionObject permissionTable, InitializeRelationshipData initData, RelationshipDetailTab detailPanel) {
		super(permissionTable, initData, detailPanel);
		this.rTree = rTree;
	}
	
	private void attachNewImgButton(){
		// create new term button
		functionPanel.clear();		
		LinkLabelAOS add = new LinkLabelAOS("images/add-grey.gif", "images/add-grey-disabled.gif", constants.relAddNewLabel(), constants.relAddNewLabel(), permissionTable.contains(OWLActionConstants.RELATIONSHIPEDIT_LABELCREATE, -1), new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(addNewLabel == null || !addNewLabel.isLoaded)
					addNewLabel = new AddNewLabel();
				addNewLabel.show();
			}
		});
		this.functionPanel.add(add);
	}
	
	private HorizontalPanel getFunctionButton(final String label,final String language){
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(2);
		
		LinkLabelAOS edit = new LinkLabelAOS("images/edit-grey.gif", "images/edit-grey-disabled.gif", constants.relEditLabel(), permissionTable.contains(OWLActionConstants.RELATIONSHIPEDIT_LABELEDIT, -1), new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(editLabel == null || !editLabel.isLoaded)
					editLabel = new EditLabel(label,language,relationshipObject);
				editLabel.show();
			}
		});
		
		LinkLabelAOS delete = new LinkLabelAOS("images/delete-grey.gif", "images/delete-grey-disabled.gif", constants.relDeleteLabel(), permissionTable.contains(OWLActionConstants.RELATIONSHIPEDIT_LABELDELETE, -1), new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(deleteLabel == null || !deleteLabel.isLoaded)
					deleteLabel = new DeleteLabel(label,language,relationshipObject);
				deleteLabel.show();
			}
		});
		
		hp.add(edit);
		hp.add(delete);
		hp.add(new HTML(label));
		return hp;
	}
	
	public void initLayout(){
		this.sayLoading();
		AsyncCallback<ArrayList<LabelObject>> callback = new AsyncCallback<ArrayList<LabelObject>>(){
			public void onSuccess(ArrayList<LabelObject> list) {
				HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
				for (Iterator<LabelObject> iter = list.iterator(); iter.hasNext();) {
					LabelObject l = (LabelObject) iter.next();
					String lang = l.getLanguage();
					String label = l.getLabel();
					ArrayList<String> langlist = new ArrayList<String>();
					if(map.containsKey(lang))
					{
						langlist = map.get(lang);
						map.remove(lang);
					}
					langlist.add(label);
					map.put(lang, langlist);
				}
				clearPanel();
				attachNewImgButton();
				
				if(!map.isEmpty()){
					Grid table = new Grid(map.size()+1,2);
					table.setWidget(0, 0, new HTML(constants.relLanguage()));					
					table.setWidget(0, 1, new HTML(constants.relLabel()));					
					int i=0;
					for (Iterator<String> iter = map.keySet().iterator(); iter.hasNext();) {
						String lang = (String) iter.next();
						ArrayList<String> langlist = (ArrayList<String>) map.get(lang);
						table.setWidget(i+1, 0, new HTML(getFullnameofLanguage(lang)));
						table.setWidget(i+1, 1, getTermTable(lang, langlist));
						i++;
					}
					table.getColumnFormatter().setWidth(1, "80%");
					relationshipRootPanel.add(GridStyle.setTableConceptDetailStyleTop(table,"gstFR1","gstFC1","gstR1","gstPanel1",true));
				}
				else{
					Label sayNo = new Label(constants.relNoLabel());
					relationshipRootPanel.add(sayNo);
					relationshipRootPanel.setCellHorizontalAlignment(sayNo, HasHorizontalAlignment.ALIGN_CENTER);
				}
				detailPanel.tabPanel.getTabBar().setTabHTML(0, Convert.replaceSpace(list.size()>1?constants.relLabels():constants.relLabel()+"&nbsp;("+list.size()+")"));
			}
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
		};
		Service.relationshipService.getRelationshipLabels(relationshipObject.getName(),  MainApp.userOntology, callback);
	}
	
	private Grid getTermTable(String lang, ArrayList<String> list){
		Grid table = new Grid(list.size(),1);
		for (int i = 0; i < list.size(); i++) {
			String label = (String) list.get(i);
			table.setWidget(i, 0, getFunctionButton(label, lang));
		}
		table.setWidth("100%");
		return table;
	}
	
	public class AddNewLabel extends FormDialogBox {  
		public ListBox language ;
		public TextBox tb ;
		
		public AddNewLabel(){ 
			super(constants.buttonCreate(), constants.buttonCancel());
			setWidth("400px");
			this.setText(constants.relCreateLabel());
			this.initLayout();
		}
		
		public void initLayout(){
			language = new ListBox();
			language = Convert.makeListBoxWithValue((ArrayList<String[]>)MainApp.getLanguage());
			language.setWidth("100%");
			
			tb = new TextBox();
			tb.setWidth("100%");
			
			Grid table = new Grid(2,2);
			table.setWidget(0, 0, new HTML(constants.relLabel()));
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
					//rTree.reloadWithTargetItem(rObj.getType(), rObj.getUri());
					SearchTree.editTargetItem(rTree.getSelectedTree(relationshipObject.getType()).getSelectedItem(), relationshipObject);
					RelationshipLabel.this.setURI(relationshipObject);
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.relAddLabelFail());
				}
			};
		/*	OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditDefinitionCreate);*/
			int actionId = 48;	
			String label = tb.getText();
			String lang = language.getValue((language.getSelectedIndex()));
			relationshipObject.addLabel(label, lang);
			Service.relationshipService.AddPropertyLabel(relationshipObject, label, lang, actionId , MainApp.userId, MainApp.userOntology, callback );
		}
	}
	public class EditLabel extends FormDialogBox {  
		public ListBox language ;
		public TextBox tb ;
		private String label;
		private String lang;
		private RelationshipObject relationshipObject;
		public EditLabel(String label, String language,RelationshipObject relationshipObject){ 
			super();
			this.label = label;
			this.lang = language;
			this.relationshipObject = relationshipObject;
			setWidth("400px");
			this.setText(constants.relEditLabel());
			this.initLayout();
		}
		
		public void initLayout(){
			language = new ListBox();
			language = Convert.makeSelectedLanguageListBox((ArrayList<String[]>)MainApp.getLanguage(),lang);
			language.setWidth("100%");
			
			tb = new TextBox();
			tb.setText(label);
			tb.setWidth("100%");
			
			Grid table = new Grid(2,2);
			table.setWidget(0, 0, new HTML(constants.relLabel()));			
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
					//rTree.reloadWithTargetItem(rObj.getType(), rObj.getUri());
					SearchTree.editTargetItem(rTree.getSelectedTree(relationshipObject.getType()).getSelectedItem(), relationshipObject);
					RelationshipLabel.this.setURI(relationshipObject);
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.relEditLabelFail());
				}
			};

		/*	OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditDefinitionCreate);*/
			int actionId = 49;
			
			String newLabel = tb.getText();
			String newLanguage = language.getValue((language.getSelectedIndex()));
			relationshipObject.removeLabel(label, lang);
			relationshipObject.addLabel(newLabel, newLanguage);
			Service.relationshipService.EditPropertyLabel(relationshipObject, label, lang, newLabel, newLanguage, actionId , MainApp.userId, MainApp.userOntology, callback);
		}
	}
	public class DeleteLabel extends FormDialogBox implements ClickHandler{
		private RelationshipObject relationshipObject ;
		private String label;
		private String language;
		public DeleteLabel(String label,String language,RelationshipObject relationshipObject ){
			super(constants.buttonDelete(), constants.buttonCancel());
			this.label = label;
			this.language = language;
			this.relationshipObject = relationshipObject;
			
			setWidth("400px");
			setText(constants.relDeleteLabel());
			initLayout();
		}
		
		public void initLayout() {
			HTML message = new HTML(constants.relDeleteLabelWarning());
			
			Grid table = new Grid(1,2);
			table.setWidget(0, 0,getWarningImage());
			table.setWidget(0, 1, message);
			
			addWidget(table);
		}

	    
	    public void onSubmit() {
	    	DeleteLabel.this.hide();
			sayLoading();
			
			AsyncCallback<Object> callback = new AsyncCallback<Object>(){
				public void onSuccess(Object results){
					//rTree.reload();
					SearchTree.editTargetItem(rTree.getSelectedTree(relationshipObject.getType()).getSelectedItem(), relationshipObject);
					RelationshipLabel.this.setURI(relationshipObject);
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.relDeleteReltionshipFail());
				}
			};
		//	OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditDefinitionDelete);
			int actionId = 50;
			relationshipObject.removeLabel(label, language);
			Service.relationshipService.DeletePropertyLabel(relationshipObject, label, language, actionId, MainApp.userId, MainApp.userOntology, callback);
	    }
		
	}
	
}
