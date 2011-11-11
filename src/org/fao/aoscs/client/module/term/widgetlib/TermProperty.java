package org.fao.aoscs.client.module.term.widgetlib;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.classification.widgetlib.OlistBox;
import org.fao.aoscs.client.module.constant.ConceptActionKey;
import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.client.module.constant.OWLActionConstants;
import org.fao.aoscs.client.module.constant.OWLStatusConstants;
import org.fao.aoscs.client.module.term.TermDetailTabPanel;
import org.fao.aoscs.client.module.term.TermTemplate;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.label.LabelAOS;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabelAOS;
import org.fao.aoscs.client.widgetlib.shared.tree.LazyLoadingTree;
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

public class TermProperty extends TermTemplate{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	//private String property ;
	private int cnt = 0;
	private AddValue addValue ;
	private EditValue editValue ;
	private DeleteValue deleteValue ;
	private HashMap<RelationshipObject, ArrayList<NonFuncObject>> propValueList = new HashMap<RelationshipObject, ArrayList<NonFuncObject>>();
	
	public TermProperty(PermissionObject permisstionTable, InitializeConceptData initData, TermDetailTabPanel termDetailPanel){
		super(permisstionTable, initData, termDetailPanel);
		//this.property = property ;
	}
	
	private void attachNewImgButton(){

		functionPanel.clear();
		String label = "";
		boolean permission = false;
		//if(property.equals(ModelConstants.RTERMEDITORIALDATATYPEPROPERTY)){
			//label = constants.conceptAddNotes();
			//permission = permissionTable.contains(OWLActionConstants.TERMNOTECREATE);
		//}else if(property.equals(ModelConstants.RTERMDOMAINDATATYPEPROPERTY)){
			label = constants.conceptAddAttributes();
			permission = permissionTable.contains(OWLActionConstants.TERMATTRIBUTECREATE, OWLStatusConstants.getOWLStatusID(termObject.getStatus()));
		//}
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
		    boolean permissionEdit = false;
		    boolean permissionDelete = false;
		    /*if(property.equals(ModelConstants.RTERMEDITORIALDATATYPEPROPERTY)){
		    	labelEdit = constants.conceptEditNotes();
                labelDelete = constants.conceptDeleteNotes();
            	permissionEdit = permissionTable.contains(OWLActionConstants.TERMNOTEEDIT);
            	permissionDelete = permissionTable.contains(OWLActionConstants.TERMNOTEDELETE);
		    }
		    else if(property.equals(ModelConstants.RTERMDOMAINDATATYPEPROPERTY)){*/
		    	labelEdit = constants.conceptEditAttributes();
		        labelDelete = constants.conceptDeleteAttributes();
		        permissionEdit = permissionTable.contains(OWLActionConstants.TERMATTRIBUTEEDIT, OWLStatusConstants.getOWLStatusID(termObject.getStatus()));
            	permissionDelete = permissionTable.contains(OWLActionConstants.TERMATTRIBUTEDELETE, OWLStatusConstants.getOWLStatusID(termObject.getStatus()));
		   // }
		    
			hp.setSpacing(3);
			
			LinkLabelAOS edit = new LinkLabelAOS("images/edit-grey.gif", "images/edit-grey-disabled.gif", labelEdit, permissionEdit,  new ClickHandler() {
				public void onClick(ClickEvent event) {
					boolean chk = true;
					if(rObj.getName().equals(ModelConstants.RHASCODEAGROVOC) && termObject.isMainLabel())
					{
						chk = Window.confirm(constants.termChangeCodeAGROVOC());
					}
					if(chk)
					{
						if(editValue == null || !editValue.isLoaded)
							editValue = new EditValue(rObj, value);
						editValue.show();
					}
				}
			});
			
			LinkLabelAOS delete = new LinkLabelAOS("images/delete-grey.gif", "images/delete-grey-disabled.gif", labelDelete, permissionDelete, new ClickHandler() {
				public void onClick(ClickEvent event) {
					boolean chk = true;
					if(rObj.getName().equals(ModelConstants.RHASCODEAGROVOC) && termObject.isMainLabel())
					{	
						chk = Window.confirm(constants.termChangeCodeAGROVOC());
					}
					if(chk)
					{
						if(deleteValue == null || !deleteValue.isLoaded)
							deleteValue = new DeleteValue(rObj, value);
						deleteValue.show();
					}
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
	@SuppressWarnings("unchecked")
	public void initLayout(){
		this.sayLoading();
		AsyncCallback<HashMap<RelationshipObject, ArrayList<NonFuncObject>>> callback = new AsyncCallback<HashMap<RelationshipObject, ArrayList<NonFuncObject>>>(){
			public void onSuccess(HashMap<RelationshipObject, ArrayList<NonFuncObject>> list) {
				propValueList = list;
				initData(propValueList);
				/*clearPanel();
				attachNewImgButton();
				Grid table = new Grid(list.size()+1,2);
				table.setWidget(0, 0, new HTML(constants.termValue()));
				table.setWidget(0, 1, new HTML(constants.termLanguage()));			
								
				if(!list.isEmpty()){
					for(int i = 0; i < list.size(); i++) {
						NonFuncObject nObj = (NonFuncObject) list.get(i);
						table.setWidget(i+1, 0, getFuncButtons(nObj));
						table.setWidget(i+1, 1, new HTML(getFullnameofLanguage(nObj.getLanguage())));
					}					
					termRootPanel.add(GridStyle.setTableConceptDetailStyleTop(table,"gstFR1","gstFC1","gstR1","gstPanel1",true));
				}else{					
					Label sayNo = new Label(constants.termNoSpellingVariant());
					termRootPanel.add(sayNo);
					termRootPanel.setCellHorizontalAlignment(sayNo, HasHorizontalAlignment.ALIGN_CENTER);
				}
				TermDetailTabPanel.tabPanel.getTabBar().setTabHTML(2, constants.termSpellingVariations()+"&nbsp;("+list.size()+")");
				 */
			}
			public void onFailure(Throwable caught) {
		
			}
		};
		ArrayList<String> property = new ArrayList<String>();
		property.add(ModelConstants.RTERMEDITORIALDATATYPEPROPERTY);
		property.add(ModelConstants.RTERMDOMAINDATATYPEPROPERTY);
		Service.termService.getPropertyValue(conceptObject.getName(), termObject.getUri(), property, MainApp.userOntology, callback);
	
	}
	
	private void initData(HashMap<RelationshipObject, ArrayList<NonFuncObject>> list)
	{
		clearPanel();
		cnt = 0;
		
		attachNewImgButton();
		Grid table = new Grid(list.size()+1,2);
		/*if(property.equals(ModelConstants.RTERMEDITORIALDATATYPEPROPERTY)){
			table.setWidget(0, 0, new HTML(constants.conceptNotes()));
		}else if(property.equals(ModelConstants.RTERMDOMAINDATATYPEPROPERTY)){*/
			table.setWidget(0, 0, new HTML(constants.conceptAttributes()));
		//}
		table.setWidget(0, 1, new HTML(constants.termValue()));
		
		int i=0;
		if(!list.isEmpty()){
			for(RelationshipObject rObj: list.keySet()){
				ArrayList<NonFuncObject> values = (ArrayList<NonFuncObject>) list.get(rObj);
				table.setWidget(i+1, 0, new HTML(LazyLoadingTree.getRelationshipLabel(rObj, constants.mainLocale())));
				table.setWidget(i+1, 1, getFuncButtons(rObj, values));
				i++;
			}
			/*if(property.equals(ModelConstants.RTERMEDITORIALDATATYPEPROPERTY)){
				termDetailPanel.tabPanel.getTabBar().setTabHTML(TermDetailTabPanel.note, Convert.replaceSpace(cnt>1?constants.conceptNotes():constants.conceptNote())+"&nbsp;("+(cnt)+")");
			}else if(property.equals(ModelConstants.RTERMDOMAINDATATYPEPROPERTY)){*/
				termDetailPanel.tabPanel.getTabBar().setTabHTML(TermDetailTabPanel.attribute, Convert.replaceSpace(cnt>1?constants.conceptAttributes():constants.conceptAttribute())+"&nbsp;("+(cnt)+")");
			//}
			termRootPanel.add(GridStyle.setTableConceptDetailStyleTop(table,"gstFR1","gstFC1","gstR1","gstPanel1",true));
		}else{
			String label = "No value";
			/*if(property.equals(ModelConstants.RTERMEDITORIALDATATYPEPROPERTY)){
				termDetailPanel.tabPanel.getTabBar().setTabHTML(TermDetailTabPanel.note, Convert.replaceSpace(constants.conceptNote())+"&nbsp;(0)");
				label = constants.conceptNoNotes();
			}else if(property.equals(ModelConstants.RTERMDOMAINDATATYPEPROPERTY)){*/
				termDetailPanel.tabPanel.getTabBar().setTabHTML(TermDetailTabPanel.attribute, Convert.replaceSpace(constants.conceptAttribute())+"&nbsp;(0)");
				label = constants.conceptNoAttributes();
			//}
			Label sayNo = new Label(label);
			termRootPanel.add(sayNo);
			termRootPanel.setCellHorizontalAlignment(sayNo, HasHorizontalAlignment.ALIGN_CENTER);
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
			this.setText(constants.conceptEditValue());
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
					table.setWidget(1, 0, new HTML(constants.termValue()));
					table.setWidget(1, 1, values);
				}
				else 
				{
					value = new TextArea();
					value.setText(oldValue.getValue());
					value.setVisibleLines(5);
					value.setWidth("100%");
					table.setWidget(1, 0, new HTML(constants.termValue()));
					table.setWidget(1, 1, value);
					
					// check language
					if(!rObj.isFunctional() && type.equalsIgnoreCase("string"))
					{
						language = new ListBox();
						language = Convert.makeSelectedLanguageListBox((ArrayList<String[]>)MainApp.getLanguage(),oldValue.getLanguage());
						language.setWidth("100%");
						table.setWidget(2, 0, new HTML(constants.termLanguage()));
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
			
			
			AsyncCallback<Object> callback = new AsyncCallback<Object>(){
				public void onSuccess(Object results){
					TermProperty.this.setURI(termObject, conceptObject);
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.conceptEditValueFail());
				}
			};
			
			
			OwlStatus status = null;
			int actionId = 0 ;
			
			/*if(property.equals(ModelConstants.RTERMEDITORIALDATATYPEPROPERTY)){
				status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.termNoteEdit);
				actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.termNoteEdit));
			}else if(property.equals(ModelConstants.RTERMDOMAINDATATYPEPROPERTY)){*/
				status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.termAttributeEdit);
				actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.termAttributeEdit));	
			//}
			
			Service.termService.editPropertyValue(MainApp.userOntology,actionId, status, MainApp.userId, oldValue, nonFuncObj, rObj, termObject, conceptObject, callback);
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
			AsyncCallback<Object> callback = new AsyncCallback<Object>(){
				public void onSuccess(Object results){
					TermProperty.this.setURI(termObject, conceptObject);
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.conceptDeleteValueFail());
				}
			};
			
			OwlStatus status = null;
			int actionId = 0 ;
			
			/*if(property.equals(ModelConstants.RTERMEDITORIALDATATYPEPROPERTY)){
				status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.termNoteDelete);
				actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.termNoteDelete));
			}else if(property.equals(ModelConstants.RTERMDOMAINDATATYPEPROPERTY)){*/
				status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.termAttributeDelete);
				actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.termAttributeDelete));
			//}


			Service.termService.deletePropertyValue(MainApp.userOntology,actionId, status, MainApp.userId, value, rObj, termObject, conceptObject, callback);
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
			/*if(property.equals(ModelConstants.RTERMEDITORIALDATATYPEPROPERTY)){
				label = constants.conceptAddNotes();
			}else if(property.equals(ModelConstants.RTERMDOMAINDATATYPEPROPERTY)){*/
				label = constants.conceptAddAttributes();
			//}
			this.setText(label);
			setWidth("400px");
			initLayout();
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
			ArrayList<RelationshipObject> currList = new ArrayList<RelationshipObject>(propValueList.keySet());
			/*if(property.equals(ModelConstants.RTERMEDITORIALDATATYPEPROPERTY)){
				propList = Convert.filterOutAddedFunctionalProperty(currList, initData.getTermEditorialAttributes());
			}
			else if(property.equals(ModelConstants.RTERMDOMAINDATATYPEPROPERTY)){*/
				propList.addAll(Convert.filterOutAddedFunctionalProperty(currList, initData.getTermEditorialAttributes()));
				propList.addAll(Convert.filterOutAddedFunctionalProperty(currList, initData.getTermDomainAttributes()));
			//}
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
							table.setWidget(1, 0, new HTML(constants.termValue()));
							table.setWidget(1, 1, values);
						}
						else 
						{
							table.setWidget(1, 0, new HTML(constants.termValue()));
							table.setWidget(1, 1, value);
							// check language
							if(!rObj.isFunctional() && type.equalsIgnoreCase("string"))
							{
								table.setWidget(2, 0, new HTML(constants.termLanguage()));
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
			
			
			AsyncCallback<Object> callback = new AsyncCallback<Object>(){
				public void onSuccess(Object results){
					TermProperty.this.setURI(termObject, conceptObject);
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.termAddValueFail());
				}
			};
			
			OwlStatus status = null;
			int actionId = 0 ;
			
			
			/*if(property.equals(ModelConstants.RTERMEDITORIALDATATYPEPROPERTY)){
				status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.termNoteCreate);
				actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.termNoteCreate));
				}else if(property.equals(ModelConstants.RTERMDOMAINDATATYPEPROPERTY)){*/
				status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.termAttributeCreate);
				actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.termAttributeCreate));
			//}
			
			
			Service.termService.addPropertyValue(MainApp.userOntology,actionId, status, MainApp.userId, nonFuncObj, rObj, termObject, conceptObject, callback);
			
		}

		
	}
}
