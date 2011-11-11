package org.fao.aoscs.client.module.concept.widgetlib;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.classification.widgetlib.ClassificationDetailTab;
import org.fao.aoscs.client.module.classification.widgetlib.OlistBox;
import org.fao.aoscs.client.module.concept.ConceptTemplate;
import org.fao.aoscs.client.module.constant.ConceptActionKey;
import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.client.module.constant.OWLActionConstants;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.label.LabelAOS;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabelAOS;
import org.fao.aoscs.client.widgetlib.shared.tree.LazyLoadingTree;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.DomainRangeDatatypeObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.NonFuncObject;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.PermissionObject;
import org.fao.aoscs.domain.RelationshipObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ConceptProperty extends ConceptTemplate{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private String property ;
	private int cnt = 0;
	private AddValue addValue ;
	private EditValue editValue ;
	private DeleteValue deleteValue ;
	
	public ConceptProperty(String property, PermissionObject permisstionTable,InitializeConceptData initData, ConceptDetailTabPanel conceptDetailPanel, ClassificationDetailTab classificationDetailPanel){
		super(permisstionTable, initData, conceptDetailPanel, classificationDetailPanel);
		this.property = property ;
	}
	
	private void attachNewImgButton(){
		
		functionPanel.clear();
		String label = "";
		if(property.equals(ModelConstants.RCONCEPTEDITORIALDATATYPEPROPERTY)){
			label = constants.conceptAddNotes();
		}else if(property.equals(ModelConstants.RCONCEPTDOMAINDATATYPEPROPERTY)){
			label = constants.conceptAddAttributes();
		}
		
		boolean permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_NOTECREATE, getConceptObject().getStatusID());
		LinkLabelAOS add = new LinkLabelAOS("images/add-grey.gif", "images/add-grey-disabled.gif", label, label, permission, new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(addValue == null || !addValue.isLoaded)
					addValue = new AddValue();
				addValue.show();
			}
		});
		add.setLabelText(label);
		this.functionPanel.add(add);
	
	}
	
	private VerticalPanel getFuncButtons(RelationshipObject rObj, ArrayList<NonFuncObject> values)
	{
		VerticalPanel vp = new VerticalPanel();
		for(NonFuncObject value:values)
		{
			vp.add(getFuncButton(rObj, value));
			cnt++;
		}
		return vp;
	}
	
	private HorizontalPanel getFuncButton(final RelationshipObject rObj, final NonFuncObject value){
		HorizontalPanel hp = new HorizontalPanel();
		//if(permissionTable.get("concept-nonfunctional").equals("enable")&& permissionTable.get("concept-nonfunctional").equals("enable"))
		{
		    String labelEdit = "";
		    String labelDelete = "";
		    if(property.equals(ModelConstants.RCONCEPTEDITORIALDATATYPEPROPERTY)){
		    	labelEdit = constants.conceptEditNotes();
                labelDelete = constants.conceptDeleteNotes();
                
		    }
		    else if(property.equals(ModelConstants.RCONCEPTDOMAINDATATYPEPROPERTY)){
		    	labelEdit = constants.conceptEditAttributes();
		        labelDelete = constants.conceptDeleteAttributes();
		    }
		    
		    
			hp.setSpacing(3);
			boolean permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_NOTEEDIT, getConceptObject().getStatusID());
			LinkLabelAOS edit = new LinkLabelAOS("images/edit-grey.gif", "images/edit-grey-disabled.gif", labelEdit, permission, new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(editValue == null || !editValue.isLoaded)
						editValue = new EditValue(rObj, value);
					editValue.show();
				}
			});
			
			permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_NOTEDELETE, getConceptObject().getStatusID());
			LinkLabelAOS delete = new LinkLabelAOS("images/delete-grey.gif", "images/delete-grey-disabled.gif", labelDelete, permission, new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(deleteValue == null || !deleteValue.isLoaded)
						deleteValue = new DeleteValue(rObj, value);
					deleteValue.show();
				}
			});			
			hp.add(edit);
			hp.add(delete);
		}
		
		if(value.getLanguage()!=null && !value.getLanguage().equals(""))
		{
			hp.add(new HTML(value.getValue()+" ("+value.getLanguage()+")"));
		}
		else
		{
			hp.add(new HTML(""+value.getValue()));
		}
		return hp;
	}
	public void initLayout(){
		this.sayLoading();
		if(property.equals(ModelConstants.RCONCEPTEDITORIALDATATYPEPROPERTY))
		{
			if(cDetailObj!=null && cDetailObj.getNoteObject()!=null)
			{
				initData(cDetailObj.getNoteObject());
			}
			else
				getData();
		}
		else if(property.equals(ModelConstants.RCONCEPTDOMAINDATATYPEPROPERTY))
		{
			if(cDetailObj!=null && cDetailObj.getAttributeObject()!=null)
			{
				initData(cDetailObj.getAttributeObject());
			}
			else
				getData();
		}
	}
	
	private void getData()
	{
		AsyncCallback<HashMap<RelationshipObject, ArrayList<NonFuncObject>>> callback = new AsyncCallback<HashMap<RelationshipObject, ArrayList<NonFuncObject>>>()
		{
			public void onSuccess(HashMap<RelationshipObject, ArrayList<NonFuncObject>> result) {
				if(property.equals(ModelConstants.RCONCEPTEDITORIALDATATYPEPROPERTY))
				{
					cDetailObj.setNoteObject(result);
				}
				else if(property.equals(ModelConstants.RCONCEPTDOMAINDATATYPEPROPERTY))
				{
					cDetailObj.setAttributeObject(result);
				}
				
				initData(result);
			}
			public void onFailure(Throwable caught) {

			}
		};
		Service.conceptService.getPropertyValue(conceptObject.getName(), property, MainApp.userOntology, callback);
	}
	
	private void initData(HashMap<RelationshipObject, ArrayList<NonFuncObject>> list)
	{
		clearPanel();
		cnt = 0;
		attachNewImgButton();
		Grid table = new Grid(list.size()+1,2);
		if(property.equals(ModelConstants.RCONCEPTEDITORIALDATATYPEPROPERTY)){
			table.setWidget(0, 0, new HTML(constants.conceptNotes()));
		}else if(property.equals(ModelConstants.RCONCEPTDOMAINDATATYPEPROPERTY)){
			table.setWidget(0, 0, new HTML(constants.conceptAttributes()));
		}
		table.setWidget(0, 1, new HTML(constants.conceptValue()));
		
		int i=0;
		if(!list.isEmpty()){
			for(RelationshipObject rObj: list.keySet()){
				ArrayList<NonFuncObject> values = (ArrayList<NonFuncObject>) list.get(rObj);
				table.setWidget(i+1, 0, new HTML(LazyLoadingTree.getRelationshipLabel(rObj, constants.mainLocale())));
				table.setWidget(i+1, 1, getFuncButtons(rObj, values));
				i++;
			}
			
			if(property.equals(ModelConstants.RCONCEPTEDITORIALDATATYPEPROPERTY)){
				if(conceptObject.getBelongsToModule()==ConceptObject.CONCEPTMODULE) conceptDetailPanel.tabPanel.getTabBar().setTabHTML(InfoTab.note, Convert.replaceSpace(cnt>1? constants.conceptNotes():constants.conceptNote())+"&nbsp;("+(cnt)+")");
				if(conceptObject.getBelongsToModule()==ConceptObject.CLASSIFICATIONMODULE) classificationDetailPanel.tab2Panel.getTabBar().setTabHTML(InfoTab.note, Convert.replaceSpace(cnt>1? constants.conceptNotes():constants.conceptNote())+"&nbsp;("+(cnt)+")");
			}else if(property.equals(ModelConstants.RCONCEPTDOMAINDATATYPEPROPERTY)){
				if(conceptObject.getBelongsToModule()==ConceptObject.CONCEPTMODULE) conceptDetailPanel.tabPanel.getTabBar().setTabHTML(InfoTab.attribute, Convert.replaceSpace(cnt>1? constants.conceptAttributes():constants.conceptAttribute())+"&nbsp;("+(cnt)+")");
				if(conceptObject.getBelongsToModule()==ConceptObject.CLASSIFICATIONMODULE) classificationDetailPanel.tab2Panel.getTabBar().setTabHTML(InfoTab.attribute, Convert.replaceSpace(cnt>1? constants.conceptAttributes():constants.conceptAttribute())+"&nbsp;("+(cnt)+")");
				
			}
			conceptRootPanel.add(GridStyle.setTableConceptDetailStyleTop(table,"gstFR1","gstFC1","gstR1","gstPanel1",true));
		}else{
			String label = "No value";
			if(property.equals(ModelConstants.RCONCEPTEDITORIALDATATYPEPROPERTY)){
				if(conceptObject.getBelongsToModule()==ConceptObject.CONCEPTMODULE) conceptDetailPanel.tabPanel.getTabBar().setTabHTML(InfoTab.note, Convert.replaceSpace(constants.conceptNote())+"&nbsp;(0)");
				if(conceptObject.getBelongsToModule()==ConceptObject.CLASSIFICATIONMODULE) classificationDetailPanel.tab2Panel.getTabBar().setTabHTML(InfoTab.note, Convert.replaceSpace(constants.conceptNote())+"&nbsp;(0)");
				label = constants.conceptNoNotes();
			}else if(property.equals(ModelConstants.RCONCEPTDOMAINDATATYPEPROPERTY)){
				if(conceptObject.getBelongsToModule()==ConceptObject.CONCEPTMODULE) conceptDetailPanel.tabPanel.getTabBar().setTabHTML(InfoTab.attribute, Convert.replaceSpace(constants.conceptAttribute())+"&nbsp;(0)");
				if(conceptObject.getBelongsToModule()==ConceptObject.CLASSIFICATIONMODULE) classificationDetailPanel.tab2Panel.getTabBar().setTabHTML(InfoTab.attribute, Convert.replaceSpace(constants.conceptAttribute())+"&nbsp;(0)");
				label = constants.conceptNoAttributes();
			}
			Label sayNo = new Label(label);
			conceptRootPanel.add(sayNo);
			conceptRootPanel.setCellHorizontalAlignment(sayNo, HasHorizontalAlignment.ALIGN_CENTER);
		}	
	}
	
	public class EditValue extends FormDialogBox implements ClickHandler{ 
		private TextArea value;
		private ListBox values;
		private ListBox language;
		private LabelAOS relationship;
		private NonFuncObject oldValue;
		private RelationshipObject rObj;
		private ArrayList<String> list = null;
		private String type = "";
		
		public EditValue(RelationshipObject rObj, NonFuncObject oldValue){
			super();
			this.rObj = rObj;
			this.oldValue = oldValue;
			String label = "";
			if(property.equals(ModelConstants.RCONCEPTEDITORIALDATATYPEPROPERTY)){
                label = constants.conceptEditNotes();
            }else if(property.equals(ModelConstants.RCONCEPTDOMAINDATATYPEPROPERTY)){
                label = constants.conceptEditAttributes();
            }
			this.setText(label);
			this.initLayout();
			setWidth("400px");
			
		}
		public void initLayout() {
			
			relationship = new LabelAOS();
			relationship.setText(LazyLoadingTree.getRelationshipLabel(rObj, constants.mainLocale()), rObj);
			
			FlexTable table = new FlexTable();
			table.setWidth("100%");
			table.getColumnFormatter().setWidth(1, "80%");
			table.setWidget(0, 0, new HTML(constants.conceptRelationship()));
			table.setWidget(0, 1, relationship);

			if(rObj.getName()!=null)
			{
				if(rObj.getDomainRangeDatatypeObject()!=null)
				{
					DomainRangeDatatypeObject drObj = rObj.getDomainRangeDatatypeObject();
					type = drObj.getRangeDataType();
					list = drObj.getRangeValue();
				}
				
				//check value or values
				if(list!=null && list.size()>0)
				{
					values = Convert.makeListBoxSingleValueWithSelectedValue(list, oldValue.getValue());
					values.setWidth("100%");
					table.setWidget(1, 0, new HTML(constants.conceptValue()));
					table.setWidget(1, 1, values);
				}
				else 
				{
					value = new TextArea();
					value.setText(oldValue.getValue());
					value.setVisibleLines(5);
					value.setWidth("100%");
					table.setWidget(1, 0, new HTML(constants.conceptValue()));
					table.setWidget(1, 1, value);
					
					// check language
					if(!rObj.isFunctional() && type.equalsIgnoreCase("string"))
					{
						language = new ListBox();
						language = Convert.makeSelectedLanguageListBox((ArrayList<String[]>)MainApp.getLanguage(),oldValue.getLanguage());
						language.setWidth("100%");
						language.setEnabled(false);
						table.setWidget(2, 0, new HTML(constants.conceptLanguage()));
						table.setWidget(2, 1, language);
					}
				}
				
			}
			addWidget(GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1"));
		}
		public boolean passCheckInput() {
			if(relationship.getValue()==null){
				return false;
			}
			else
			{
				//check value or values
				if(list!=null && list.size()>0)
				{
					if(values.getValue(values.getSelectedIndex()).equals("--None--") || values.getValue(values.getSelectedIndex()).equals(""))
					{
						return false;
					}
				}
				else 
				{
					if(value.getText().length()==0)
					{
						return false;
					}
					// check language
					if(rObj.getName()!=null && !rObj.isFunctional() && type.equalsIgnoreCase("string"))
					{
						if(language.getValue(language.getSelectedIndex()).equals("--None--") || language.getValue(language.getSelectedIndex()).equals(""))
						{
							return false;
						}
					}
				}
				
			}
			return true;
		}
		public void onSubmit() {
			sayLoading();
			
			NonFuncObject nonFuncObj = new NonFuncObject();
			//check value or values
			if(list!=null && list.size()>0)
			{
				nonFuncObj.setValue(values.getValue(values.getSelectedIndex()));
			}
			else 
			{
				nonFuncObj.setValue(value.getText());
				// check language
				if(rObj.getName()!=null && !rObj.isFunctional() && type.equalsIgnoreCase("string"))
				{
					nonFuncObj.setLanguage(language.getValue(language.getSelectedIndex()));
				}
			}
			
			AsyncCallback<HashMap<RelationshipObject, ArrayList<NonFuncObject>>> callback = new AsyncCallback<HashMap<RelationshipObject, ArrayList<NonFuncObject>>>(){
				public void onSuccess(HashMap<RelationshipObject, ArrayList<NonFuncObject>> results){
					if(property.equals(ModelConstants.RCONCEPTEDITORIALDATATYPEPROPERTY))
					{
						cDetailObj.setNoteObject(results);
					}
					else if(property.equals(ModelConstants.RCONCEPTDOMAINDATATYPEPROPERTY))
					{
						cDetailObj.setAttributeObject(results);
					}
					ConceptProperty.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.conceptEditValueFail());
				}
			};
			
			
			OwlStatus status = null;
			int actionId = 0 ;
			
			if(property.equals(ModelConstants.RCONCEPTEDITORIALDATATYPEPROPERTY)){
				status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditNoteEdit);
				actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditNoteEdit));
			}else if(property.equals(ModelConstants.RCONCEPTDOMAINDATATYPEPROPERTY)){
				status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditAttributeEdit);
				actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditAttributeEdit));	
			}
			
			Service.conceptService.editPropertyValue(MainApp.userOntology,actionId, status, MainApp.userId, oldValue, nonFuncObj, rObj, conceptObject, property, callback);
		}
		
		
	}
	public class DeleteValue extends FormDialogBox implements ClickHandler{ 
		private RelationshipObject rObj;
		private NonFuncObject value;
		public DeleteValue(RelationshipObject rObj, NonFuncObject value){
			super(constants.buttonDelete(), constants.buttonCancel());
			this.rObj = rObj;
			this.value = value;
			this.setText(constants.conceptDeleteValue());
			setWidth("400px");
			this.initLayout();
	
		}
		public void initLayout() {
			HTML message = new HTML(constants.conceptValueDeleteWarning());
			
			Grid table = new Grid(1,2);
			table.setWidget(0,0,getWarningImage());
			table.setWidget(0,1, message);
			
			addWidget(table);
		};
		public void onSubmit() {
			sayLoading();
			AsyncCallback<HashMap<RelationshipObject, ArrayList<NonFuncObject>>> callback = new AsyncCallback<HashMap<RelationshipObject, ArrayList<NonFuncObject>>>(){
				public void onSuccess(HashMap<RelationshipObject, ArrayList<NonFuncObject>> results){
					if(property.equals(ModelConstants.RCONCEPTEDITORIALDATATYPEPROPERTY))
					{
						cDetailObj.setNoteObject(results);
					}
					else if(property.equals(ModelConstants.RCONCEPTDOMAINDATATYPEPROPERTY))
					{
						cDetailObj.setAttributeObject(results);
					}
					ConceptProperty.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.conceptDeleteValueFail());
				}
			};
			
			OwlStatus status = null;
			int actionId = 0 ;
			
			if(property.equals(ModelConstants.RCONCEPTEDITORIALDATATYPEPROPERTY)){
				status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditNoteDelete);
				actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditNoteDelete));
			}else if(property.equals(ModelConstants.RCONCEPTDOMAINDATATYPEPROPERTY)){
				status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditAttributeDelete);
				actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditAttributeDelete));
			}


			Service.conceptService.deletePropertyValue(MainApp.userOntology,actionId, status, MainApp.userId, value, rObj, conceptObject, property, callback);
		}

	}
	
	public class AddValue extends FormDialogBox implements ClickHandler{ 
		private TextArea value;
		private ListBox language;
		private OlistBox relationship;
		private ListBox values;
		
		private RelationshipObject rObj = new RelationshipObject();
		private ArrayList<String> list = null;
		private String type = "";
		
		public AddValue(){
			super(constants.buttonCreate(), constants.buttonCancel());
			String label = "";
            if(property.equals(ModelConstants.RCONCEPTEDITORIALDATATYPEPROPERTY)){
                label = constants.conceptAddNotes();
            }else if(property.equals(ModelConstants.RCONCEPTDOMAINDATATYPEPROPERTY)){
                label = constants.conceptAddAttributes();
            }
			this.setText(label);
			setWidth("400px");
			this.initLayout();
		}
		
		public void initLayout() {
			value = new TextArea();
			value.setVisibleLines(3);
			value.setWidth("100%");
			
			language = new ListBox();
			language = Convert.makeListBoxWithValue((ArrayList<String[]>)MainApp.getLanguage());
			language.setWidth("100%");
			
			values = new ListBox();
			values.setWidth("100%");
			
			ArrayList<RelationshipObject> propList = new ArrayList<RelationshipObject>();
			if(property.equals(ModelConstants.RCONCEPTEDITORIALDATATYPEPROPERTY)){
				HashMap<RelationshipObject, ArrayList<NonFuncObject>> currList = cDetailObj.getNoteObject();
				propList = Convert.filterOutAddedFunctionalProperty(new ArrayList<RelationshipObject>(currList.keySet()), initData.getConceptEditorialAttributes());
			}
			else if(property.equals(ModelConstants.RCONCEPTDOMAINDATATYPEPROPERTY)){
				HashMap<RelationshipObject, ArrayList<NonFuncObject>> currList = cDetailObj.getAttributeObject();
				propList = Convert.filterOutAddedFunctionalProperty(new ArrayList<RelationshipObject>(currList.keySet()), initData.getConceptDomainAttributes());
			}
			relationship = Convert.makeOListBoxWithValue(propList);
			relationship.setWidth("100%");
			
			final FlexTable table = new FlexTable();
			table.setWidth("100%");
			table.getColumnFormatter().setWidth(1, "80%");
			table.setWidget(0, 0, new HTML(constants.conceptRelationship()));
			table.setWidget(0, 1, relationship);
			
			
			relationship.addChangeHandler(new ChangeHandler()
			{
				public void onChange(ChangeEvent event) {
					while(table.getRowCount()>1)
					{
						table.removeRow(table.getRowCount()-1);
					}
					type = "";
					list = null;

					rObj = (RelationshipObject) relationship.getObject(relationship.getSelectedIndex());
					
					if(rObj.getName()!=null)
					{
						if(rObj.getDomainRangeDatatypeObject()!=null)
						{
							DomainRangeDatatypeObject drObj = rObj.getDomainRangeDatatypeObject();
							type = drObj.getRangeDataType();
							list = drObj.getRangeValue();
						}
						
						//check value or values
						if(list!=null && list.size()>0)
						{
							values = Convert.makeListBoxSingleValueWithSelectedValue(list, "");
							table.setWidget(1, 0, new HTML(constants.conceptValue()));
							table.setWidget(1, 1, values);
						}
						else 
						{
							table.setWidget(1, 0, new HTML(constants.conceptValue()));
							table.setWidget(1, 1, value);
							// check language
							if(!rObj.isFunctional() && type.equalsIgnoreCase("string"))
							{
								table.setWidget(2, 0, new HTML(constants.conceptLanguage()));
								table.setWidget(2, 1, language);
							}
						}
						GridStyle.updateTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1");
					}
				}
				
			});
			addWidget(GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1"));
		}
		public boolean passCheckInput() {
			if(relationship.getValue(relationship.getSelectedIndex()).equals("--None--")	|| relationship.getValue(relationship.getSelectedIndex()).equals(""))
			{
				return false;
			}
			else
			{
				//check value or values
				if(list!=null && list.size()>0)
				{
					if(values.getValue(values.getSelectedIndex()).equals("--None--") || values.getValue(values.getSelectedIndex()).equals(""))
					{
						return false;
					}
				}
				else 
				{
					if(value.getText().length()==0)
					{
						return false;
					}
					// check language
					if(rObj.getName()!=null && !rObj.isFunctional() && type.equalsIgnoreCase("string"))
					{
						if(language.getValue(language.getSelectedIndex()).equals("--None--") || language.getValue(language.getSelectedIndex()).equals(""))
						{
							return false;
						}
					}
				}
				
			}
			return true;
		}
		public void onSubmit() {
			sayLoading();
			
			NonFuncObject nonFuncObj = new NonFuncObject();
			//check value or values
			if(list!=null && list.size()>0)
			{
				nonFuncObj.setValue(values.getValue(values.getSelectedIndex()));
			}
			else 
			{
				nonFuncObj.setValue(value.getText());
				// check language
				if(rObj.getName()!=null && !rObj.isFunctional() && type.equalsIgnoreCase("string"))
				{
					nonFuncObj.setLanguage(language.getValue(language.getSelectedIndex()));
				}
			}
			
			
			AsyncCallback<HashMap<RelationshipObject, ArrayList<NonFuncObject>>> callback = new AsyncCallback<HashMap<RelationshipObject, ArrayList<NonFuncObject>>>(){
				public void onSuccess(HashMap<RelationshipObject, ArrayList<NonFuncObject>> results){
					if(property.equals(ModelConstants.RCONCEPTEDITORIALDATATYPEPROPERTY))
					{
						cDetailObj.setNoteObject(results);
					}
					else if(property.equals(ModelConstants.RCONCEPTDOMAINDATATYPEPROPERTY))
					{
						cDetailObj.setAttributeObject(results);
					}
					
					ConceptProperty.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.conceptAddValueFail());
				}
			};
			
			OwlStatus status = null;
			int actionId = 0 ;
			
			
			if(property.equals(ModelConstants.RCONCEPTEDITORIALDATATYPEPROPERTY))
			{
				status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditNoteCreate);
				actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditNoteCreate));
			}
			else if(property.equals(ModelConstants.RCONCEPTDOMAINDATATYPEPROPERTY))
			{
				status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditAttributeCreate);
				actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditAttributeCreate));
			}
			
			Service.conceptService.addPropertyValue(MainApp.userOntology,actionId, status, MainApp.userId, nonFuncObj, rObj, conceptObject, property, callback);
			
		}

		
	}
}
