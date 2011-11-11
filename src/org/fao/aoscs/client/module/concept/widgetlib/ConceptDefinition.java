package org.fao.aoscs.client.module.concept.widgetlib;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.locale.LocaleMessages;
import org.fao.aoscs.client.module.classification.widgetlib.ClassificationDetailTab;
import org.fao.aoscs.client.module.concept.ConceptTemplate;
import org.fao.aoscs.client.module.constant.ConceptActionKey;
import org.fao.aoscs.client.module.constant.OWLActionConstants;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.utility.TimeConverter;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabelAOS;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.DefinitionObject;
import org.fao.aoscs.domain.IDObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.PermissionObject;
import org.fao.aoscs.domain.TranslationObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ConceptDefinition extends ConceptTemplate{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private LocaleMessages messages = (LocaleMessages) GWT.create(LocaleMessages.class);
	
	
	private AddNewDefinition addNewDefinition;
	private EditExternalSource editExternalSource;
	private DeleteExternalSource deleteExternalSource;
	private AddExternalSource addExternalSource;
	private EditDefinitionLabel editDefinitionLabel;
	private DeleteDefinitionLabel deleteDefinitionLabel;
	private DeleteDefinition deleteDefinition;
	private AddDefinitionLabel addDefinitionLabel;
	
	public ConceptDefinition(PermissionObject permisstionTable,InitializeConceptData initData, ConceptDetailTabPanel conceptDetailPanel, ClassificationDetailTab classificationDetailPanel){
		super(permisstionTable,initData, conceptDetailPanel, classificationDetailPanel);
	}
	
	private void attachNewDefButton(){
		functionPanel.clear();
		boolean permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_DEFINITIONCREATE, getConceptObject().getStatusID());
		// Create definition
		LinkLabelAOS addFunc = new LinkLabelAOS("images/add-grey.gif", "images/add-grey-disabled.gif", constants.conceptAddDefinition(), constants.conceptAddDefinition(), permission, new ClickHandler() 
		{
			public void onClick(ClickEvent event) {
				if(addNewDefinition == null || !addNewDefinition.isLoaded)
					addNewDefinition = new AddNewDefinition();
				addNewDefinition.show();
			}
		});
		functionPanel.add(addFunc);
	}
	
	private VerticalPanel getDateTable(final IDObject dObj){
		Grid table = new Grid(3,2);
		table.setWidget(0, 0, new HTML(constants.conceptCreateDate()));
		table.setWidget(1, 0, new HTML(constants.conceptUpdateDate()));
		table.setWidget(2, 0, new HTML(constants.conceptSource()));
		table.setWidget(0, 1, new HTML(TimeConverter.formatDate(dObj.getIDDateCreate())));
		table.setWidget(1, 1, new HTML(TimeConverter.formatDate(dObj.getIDDateModified())));
		
		HorizontalPanel hp = new HorizontalPanel();
		
		hp.setSpacing(3);
		if(dObj.hasSource())
		{
			boolean permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_EXTSOURCEEDIT, getConceptObject().getStatusID());
			// Source edit
			LinkLabelAOS edit = new LinkLabelAOS("images/edit-grey.gif", "images/edit-grey-disabled.gif", constants.conceptEditSource(), permission, new ClickHandler(){;
				public void onClick(ClickEvent event) {
					if(editExternalSource == null || !editExternalSource.isLoaded)
						editExternalSource = new EditExternalSource(dObj);
					editExternalSource.show();
				}
			});
			
			permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_EXTSOURCEDELETE, getConceptObject().getStatusID());
			// source delete
			LinkLabelAOS delete = new LinkLabelAOS("images/delete-grey.gif", "images/delete-grey-disabled.gif", constants.conceptDeleteSource(), permission, new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(deleteExternalSource == null  || !deleteExternalSource.isLoaded)
						deleteExternalSource = new DeleteExternalSource(dObj);
					deleteExternalSource.show();
				}
			});
			
			hp.add(edit);
			hp.add(delete);
			if(dObj.getIDSource().equals("Book"))
			{
			    HTML link =  new HTML(dObj.getIDSource()+" (" + dObj.getIDSourceURL() +")");
			    hp.add(link);				    
			}else
			{
				HTML link =  new HTML("<A HREF=\""+dObj.getIDSourceURL()+"\" target=\"_blank\">"+dObj.getIDSource()+"</A>");
				hp.add(link);
			}
		}else
		{

			boolean permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_EXTSOURCECREATE, getConceptObject().getStatusID());
			// Source Create
			LinkLabelAOS add = new LinkLabelAOS("images/add-grey.gif", "images/add-grey-disabled.gif", constants.conceptAddNewSource(), permission, new ClickHandler(){
				public void onClick(ClickEvent event) {
					if(addExternalSource == null || !addExternalSource.isLoaded)
						addExternalSource = new AddExternalSource(dObj);
					addExternalSource.show();
				}
			});
			hp.add(add);
		}
		
		table.setWidget(2, 1,hp);
		table.getColumnFormatter().setWidth(1, "80%");
		table.setWidth("100%");
		return GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1");
	}
	private VerticalPanel getTranslationTable(IDObject dObj){
		ArrayList<TranslationObject> list = dObj.getIDTranslationList();
		Grid table = new Grid(list.size()+1,2);
		table.setWidget(0, 0, new HTML(constants.conceptLanguage()));
		table.setWidget(0, 1, new HTML(constants.conceptDefinition()));
		for (int i = 0; i < list.size(); i++) {
			final TranslationObject tObj = (TranslationObject) list.get(i);
			HorizontalPanel hp = new HorizontalPanel();
			hp.setSpacing(3);
			
			boolean permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_DEFINITIONTRANSLATIONEDIT, getConceptObject().getStatusID());
			// Definiation translation edit
			LinkLabelAOS edit = new LinkLabelAOS("images/edit-grey.gif", "images/edit-grey-disabled.gif", constants.conceptEditTranslation(), permission, new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(editDefinitionLabel == null || !editDefinitionLabel.isLoaded)
						editDefinitionLabel = new EditDefinitionLabel(tObj);
					editDefinitionLabel.show();
				}
			});
			
			permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_DEFINITIONTRANSLATIONDELETE, getConceptObject().getStatusID());
			// Definition translation delete
			LinkLabelAOS delete = new LinkLabelAOS("images/delete-grey.gif", "images/delete-grey-disabled.gif", constants.conceptDeleteTranslation(), permission, new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(deleteDefinitionLabel == null || !deleteDefinitionLabel.isLoaded)
						deleteDefinitionLabel = new DeleteDefinitionLabel(tObj);
					deleteDefinitionLabel.show();
				}
			});
			hp.add(edit);
			hp.add(delete);
			
			HTML lang = new HTML(getFullnameofLanguage(tObj.getLang()));
			lang.setWordWrap(false);
			
			HTML label = new HTML(tObj.getLabel());
			label.setWordWrap(true);
			hp.add(label);

			table.getColumnFormatter().setWidth(1, "100%");
			table.setWidget(i+1, 0, lang);
			table.setWidget(i+1, 1, hp);
		}
		table.setWidth("100%");
		return GridStyle.setTableConceptDetailStyleTop(table,"gstFR1","gstFC1","gstR1","gstPanel1",true);
	}
	private HorizontalPanel getDefinitionNumber(int number , final IDObject dObj){
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(3);
		hp.add(new Label(Integer.toString(number)));
		boolean permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_DEFINITIONDELETE, getConceptObject().getStatusID());
		// Definition delete
		LinkLabelAOS delete = new LinkLabelAOS("images/delete-grey.gif", "images/delete-grey-disabled.gif", constants.conceptDeleteDefinition(), permission, new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(deleteDefinition == null || !deleteDefinition.isLoaded)
					deleteDefinition = new DeleteDefinition(dObj);
				deleteDefinition.show();
			}
		});
		hp.add(delete);
		return hp;
	}
	private HorizontalPanel getAddTranslationFunction(final IDObject dObj){
		HorizontalPanel hp = new HorizontalPanel();

		boolean permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_DEFINITIONTRANSLATIONCREATE, getConceptObject().getStatusID());
		// Create translation
		LinkLabelAOS add = new LinkLabelAOS("images/add-grey.gif",  "images/add-grey-disabled.gif",constants.conceptAddNewTranslation(), constants.conceptAddTranslation(), permission, new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(addDefinitionLabel == null || !addDefinitionLabel.isLoaded)
					addDefinitionLabel = new AddDefinitionLabel(dObj,dObj.getExistingLanguage());
				addDefinitionLabel.show();
			}
		});			
		hp.add(add);
		return hp;
	}

	public void initLayout(){
		this.sayLoading();
		if(cDetailObj!=null && cDetailObj.getDefinitionObject()!=null)
		{
			initData(cDetailObj.getDefinitionObject());
		}
		else
		{
			AsyncCallback<DefinitionObject> callback = new AsyncCallback<DefinitionObject>(){
				public void onSuccess(DefinitionObject results) {
					cDetailObj.setDefinitionObject(results);
					initData((DefinitionObject) results);
				}
				public void onFailure(Throwable caught) {
					Window.alert(constants.conceptGetDefinitionFail());
				}
			};
			
			Service.conceptService.getConceptDefinition(conceptObject.getName(), MainApp.userOntology, callback);
		}
	}
	
	private void initData(DefinitionObject dfObj)
	{
		clearPanel();
		if(dfObj!=null && !dfObj.isEmpty()){
			attachNewDefButton();
			ArrayList<IDObject> dObjList = dfObj.getDefinitionListOnly();
			Grid table = new Grid(dObjList.size()+1,2);
			table.setWidget(0, 0, new HTML(constants.conceptNo()));
			table.setWidget(0, 1, new HTML(constants.conceptDefinition()));
			table.setWidth("100%");
			for (int i = 0; i < dObjList.size(); i++) {
				IDObject dObj = (IDObject) dObjList.get(i);
				table.setWidget(i+1, 0, getDefinitionNumber(i+1, dObj));
				
				VerticalPanel vp = new VerticalPanel();
				
				HorizontalPanel func = getAddTranslationFunction(dObj);
				vp.add(func);
				vp.setCellHorizontalAlignment(func, HasHorizontalAlignment.ALIGN_RIGHT);
				vp.add(getTranslationTable(dObj));
				vp.add(getDateTable(dObj));
				vp.setWidth("100%");
				vp.setSpacing(5);
				
				table.setWidget(i+1, 1, vp);
			}
			if(conceptObject.getBelongsToModule()==ConceptObject.CONCEPTMODULE) conceptDetailPanel.tabPanel.getTabBar().setTabHTML(InfoTab.definition, Convert.replaceSpace(dObjList.size()>1? constants.conceptDefinitions():constants.conceptDefinition())+"&nbsp;("+(dObjList.size())+")");
			if(conceptObject.getBelongsToModule()==ConceptObject.CLASSIFICATIONMODULE) classificationDetailPanel.tab2Panel.getTabBar().setTabHTML(InfoTab.definition, Convert.replaceSpace(dObjList.size()>1? constants.conceptDefinitions():constants.conceptDefinition())+"&nbsp;("+(dObjList.size())+")");
			conceptRootPanel.add(GridStyle.setTableConceptDetailStyleTop(table,"gstFR1","gstFC1","gstR1","gstPanel1",true));
		}else{
			attachNewDefButton();
			Label sayNo = new Label(constants.conceptNoDefinition());
			if(conceptObject.getBelongsToModule()==ConceptObject.CONCEPTMODULE) conceptDetailPanel.tabPanel.getTabBar().setTabHTML(InfoTab.definition, Convert.replaceSpace(constants.conceptDefinition())+"&nbsp;(0)");
			if(conceptObject.getBelongsToModule()==ConceptObject.CLASSIFICATIONMODULE) classificationDetailPanel.tab2Panel.getTabBar().setTabHTML(InfoTab.definition, Convert.replaceSpace(constants.conceptDefinition())+"&nbsp;(0)");
			conceptRootPanel.add(sayNo);
			conceptRootPanel.setCellHorizontalAlignment(sayNo, HasHorizontalAlignment.ALIGN_CENTER);
		}
	}

	public class DeleteExternalSource extends FormDialogBox implements ClickHandler{
	
		private IDObject ido;
		
		public DeleteExternalSource(IDObject ido){
			super(constants.buttonDelete(), constants.buttonCancel());
			this.ido = ido;
			this.setText(constants.conceptDeleteSource());
			setWidth("400px");
			initLayout();
		}
		
		public void initLayout() {
			HTML message = new HTML(constants.conceptDefinitionSourceDeleteWarning());

			Grid table = new Grid(1,2);
			table.setWidget(0,0,getWarningImage());
			table.setWidget(0,1,message);
			table.setWidth("100%");
			
			addWidget(table);
		}
		
		public void onSubmit() {
			sayLoading();
			
			AsyncCallback<DefinitionObject>  callback = new AsyncCallback<DefinitionObject>(){
				public void onSuccess(DefinitionObject results){
					cDetailObj.setDefinitionObject(results);
					ConceptDefinition.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.conceptDeleteExternalSourceFail());
				}
			};

			OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditDefinitionExtSourceDelete);
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditDefinitionExtSourceDelete));
			
	
			
			Service.conceptService.deleteDefinitionExternalSource(MainApp.userOntology,actionId, status, MainApp.userId, ido, conceptObject, callback);
		}
	
	}
	public class AddExternalSource extends FormDialogBox implements ClickHandler{
		private ListBox source;
		private TextBox URL;
		private IDObject ido;
		
		public AddExternalSource(IDObject ido){
			super(constants.buttonCreate(), constants.buttonCancel());
			this.ido = ido;
			this.setText(constants.conceptEditExternalSource());
			setWidth("400px");
			initLayout();
		}
		
		public void initLayout() {
			source = new ListBox();
			source = Convert.makeSourceListBox((ArrayList<String[]>)initData.getSource());
			source.setWidth("100%");
			
			URL = new TextBox();	
			URL.setWidth("100%");

			final Grid table = new Grid(2,2);
			table.setWidget(0, 0, new HTML(constants.conceptSource()));
			table.setWidget(1, 0, new HTML(constants.conceptUrl()));
			table.setWidget(0, 1, source);
			table.setWidget(1, 1, URL);
			table.setWidth("100%");
			table.getColumnFormatter().setWidth(1, "80%");
			
			addWidget(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));
			
			source.addChangeHandler(new ChangeHandler()
			{

			    public void onChange(ChangeEvent arg0)
                {			      
                   if(source.getItemText(source.getSelectedIndex()).equals("Book")){                       
                       ((HTML)table.getWidget(1,0)).setText(constants.conceptSourceTitle());
                   }else{
                       ((HTML)table.getWidget(1,0)).setText(constants.conceptUrl());
                   }
                }});
		}
		
		
		public boolean passCheckInput() {
			boolean pass = false;
			if(source.getValue((source.getSelectedIndex())).equals("") || URL.getText().length()==0 ){
				pass = false;
			}else{
				pass = true;
			}
			return pass;
		}
		
		
		public void onSubmit() {
			sayLoading();
			ido.setIDSourceURL(URL.getText());
			ido.setIDSource(source.getValue(source.getSelectedIndex()));
		
			AsyncCallback<DefinitionObject>  callback = new AsyncCallback<DefinitionObject>(){
				public void onSuccess(DefinitionObject results){
					cDetailObj.setDefinitionObject(results);
					ConceptDefinition.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.conceptAddExternalSourceFail());
				}
			};
			
			OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditDefinitionExtSourceCreate);
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditDefinitionExtSourceCreate));

			
			Service.conceptService.addDefinitionExternalSource(MainApp.userOntology,actionId, status, MainApp.userId, ido, conceptObject, callback);
		}
		
	
	}
	public class EditExternalSource extends FormDialogBox implements ClickHandler{
		private ListBox source ;
		private TextBox URL;
		private IDObject ido;
		
		public EditExternalSource(IDObject ido){
			super();
			this.ido = ido;
			this.setText(constants.conceptEditExternalSource());
			setWidth("400px");
			initLayout();
		}
		
		public void initLayout() {
			source = new ListBox();
			source =  Convert.makeSelectedSourceListBox((ArrayList<String[]>)initData.getSource(),ido.getIDSource());
			source.setWidth("100%");
			
			URL = new TextBox();	
			URL.setText(ido.getIDSourceURL());
			URL.setWidth("100%");

			final Grid table = new Grid(2,2);
			table.setWidget(0, 0, new HTML(constants.conceptSource()));
			table.setWidget(1, 0, new HTML(constants.conceptUrl()));
			table.setWidget(0, 1, source);
			table.setWidget(1, 1, URL);
			table.setWidth("100%");
			
			if(source.getValue(source.getSelectedIndex()).equals("Book")){
			    ((HTML)table.getWidget(1,0)).setText(constants.conceptSourceTitle());
			}
			source.addChangeHandler(new ChangeHandler()
            {
                public void onChange(ChangeEvent arg0)
                {
                   if(source.getItemText(source.getSelectedIndex()).equals("Book"))
                   {                       
                       ((HTML)table.getWidget(1,0)).setText(constants.conceptSourceTitle());
                   }else
                   {
                       ((HTML)table.getWidget(1,0)).setText(constants.conceptUrl());
                   }
              }});        			
			addWidget(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));
		}
		
		public boolean passCheckInput() {
			boolean pass = false;
			if(source.getValue((source.getSelectedIndex())).equals("") || URL.getText().equals("") ){
				pass = false;
			}else{
				pass = true;
			}
			return pass;
		}
		
		public void onSubmit() {
			sayLoading();
			
			IDObject idoNew = new IDObject();
			idoNew.setIDType(IDObject.DEFINITION);
			idoNew.setIDUri(ido.getIDUri());
			idoNew.setIDUri(ido.getIDName());
			idoNew.setIDSource(source.getValue(source.getSelectedIndex()));
			idoNew.setIDSourceURL(URL.getText());
			
			AsyncCallback<DefinitionObject>  callback = new AsyncCallback<DefinitionObject>(){
				public void onSuccess(DefinitionObject results){
					cDetailObj.setDefinitionObject(results);
					ConceptDefinition.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.conceptEditTranslationFail());
				}
			};
			
			
			OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditDefinitionExtSourceEdit);
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditDefinitionExtSourceEdit));
		
			
			Service.conceptService.editDefinitionExternalSource(MainApp.userOntology,actionId, status, MainApp.userId, ido, idoNew, conceptObject, callback);
		}
		
	}
	
	public class AddDefinitionLabel extends FormDialogBox{
	
		private ListBox language;
		private TextArea def;
		private IDObject ido;
		private HashMap<String, String> avaribleLanguage;
		
		public AddDefinitionLabel(IDObject ido,HashMap<String, String> avaribleLanguage){  
			super(constants.buttonCreate(), constants.buttonCancel());
			this.ido = ido;
			this.avaribleLanguage = avaribleLanguage;
			this.setText(constants.conceptAddDefinitionLabel()); //"Add new label"
			setWidth("400px");
			initLayout();
		}
		
		public void initLayout() {
			def = new TextArea();	
			def.setWidth("100%");
			def.setVisibleLines(3);

			language = new ListBox();
			language =  Convert.makeListBoxWithValue(Convert.excludeLanguage((ArrayList<String[]>)MainApp.getLanguage(),avaribleLanguage));
			language.setWidth("100%");
			
			Grid table = new Grid(2,2);
			table.setWidget(0, 0, new HTML(constants.conceptDefinition()));
			table.setWidget(1, 0, new HTML(constants.conceptLanguage()));
			table.setWidget(0, 1, def);
			table.setWidget(1, 1, language);
			table.getColumnFormatter().setWidth(1, "80%");
			table.setWidth("100%");
			
			addWidget(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));
		}
		
		public boolean passCheckInput() {
			boolean pass = false;
			if(language.getValue((language.getSelectedIndex())).equals("") || def.getText().length()==0 ){
				pass = false;
			}else{
				pass = true;
			}
			return pass;
		}
		
		public void onSubmit() {
			sayLoading();
			
			TranslationObject transObjNew = new TranslationObject();
			transObjNew.setType(TranslationObject.DEFINITIONTRANSLATION);
			transObjNew.setLabel(def.getText());
			transObjNew.setLang(language.getValue(language.getSelectedIndex()));
			transObjNew.setUri(ido.getIDUri());
		
			AsyncCallback<DefinitionObject>  callback = new AsyncCallback<DefinitionObject>(){
				public void onSuccess(DefinitionObject results){
					cDetailObj.setDefinitionObject(results);
					ConceptDefinition.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.conceptAddTranslationFail());
				}
			};

			OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditDefinitionTranslationCreate);
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditDefinitionTranslationCreate));
			
			
			Service.conceptService.addDefinitionLabel(MainApp.userOntology,actionId, status, MainApp.userId, transObjNew, ido, conceptObject, callback);
		}
	}
	public class DeleteDefinition extends FormDialogBox implements ClickHandler{
		private IDObject ido ;
		
		public DeleteDefinition(IDObject ido){
			super(constants.buttonDelete(), constants.buttonCancel());
			this.ido = ido;
			setWidth("400px");
			setText(constants.conceptDeleteDefinition());
			initLayout();
		}
		
		public void initLayout() {
			HTML message = new HTML(constants.conceptDefinitionDeleteWarning());
			
			Grid table = new Grid(1,2);
			table.setWidget(0, 0,getWarningImage());
			table.setWidget(0, 1, message);
			
			addWidget(table);
		}

	    
	    public void onSubmit() {
	    	DeleteDefinition.this.hide();
			sayLoading();
			
			AsyncCallback<DefinitionObject>  callback = new AsyncCallback<DefinitionObject>(){
				public void onSuccess(DefinitionObject results){
					cDetailObj.setDefinitionObject(results);
					ConceptDefinition.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.conceptAddDefinitionFail());
				}
			};
			OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditDefinitionDelete);
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditDefinitionDelete));
			
			Service.conceptService.deleteDefinition(MainApp.userOntology,actionId, status, MainApp.userId, ido, conceptObject, callback);
	    }
		
	}
	
	public class DeleteDefinitionLabel extends FormDialogBox implements ClickHandler{
		private TranslationObject transObj;
		
		public DeleteDefinitionLabel(TranslationObject transObj){
			super(constants.buttonDelete(), constants.buttonCancel());
			this.transObj = transObj;
			this.setText(constants.conceptDeleteDefinitionLabel());
			setWidth("400px");
			initLayout();
		}
		
		
		public void initLayout() 
		{			
			HTML message = new HTML(messages.conceptDefinitionLabelDeleteWarning(transObj.getLabel(), transObj.getLang()));			
			Grid table = new Grid(1,2);
			table.setWidget(0,0,getWarningImage());
			table.setWidget(0,1, message);
			table.setWidth("100%");
			
			addWidget(table);
		}
		
		
		public void onSubmit() {
			sayLoading();
			
			AsyncCallback<DefinitionObject>  callback = new AsyncCallback<DefinitionObject>(){
				public void onSuccess(DefinitionObject results){
					cDetailObj.setDefinitionObject(results);
					ConceptDefinition.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.conceptDeleteDefinitionFail());
				}
			};

			OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditDefinitionTranslationDelete);
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditDefinitionTranslationDelete));
			
			Service.conceptService.deleteDefinitionLabel(MainApp.userOntology,actionId, status, MainApp.userId, transObj, conceptObject, callback);
		}
	}
	
	public class EditDefinitionLabel extends FormDialogBox implements ClickHandler{
	
		private TextArea def ;
		private ListBox language ;
		private TranslationObject transObj ;
		
		public EditDefinitionLabel(TranslationObject transObj){
			super();
			this.transObj = transObj;
			this.setText(constants.conceptEditDefinition());
			setWidth("400px");
			initLayout();
		}
				
		public void initLayout() {
			language = new ListBox();
			language =  Convert.makeSelectedLanguageListBox((ArrayList<String[]>)MainApp.getLanguage(),transObj.getLang());
			language.setWidth("100%");
			language.setEnabled(false);
			
			def = new TextArea();	
			def.setText(transObj.getLabel());
			def.setWidth("100%");
			def.setVisibleLines(3);

			Grid table = new Grid(2,2);
			table.setWidget(0, 0, new HTML(constants.conceptDefinition()));
			table.setWidget(1, 0, new HTML(constants.conceptLanguage()));
			table.setWidget(0, 1, def);
			table.setWidget(1, 1, language);
			table.setWidth("100%");
			table.getColumnFormatter().setWidth(1, "80%");
			
			addWidget(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));
		}
	
		
		public boolean passCheckInput() {
			boolean pass = false;
			if(language.getValue((language.getSelectedIndex())).equals("") || def.getText().length()==0){
				pass = false;
			}else{
				pass = true;
			}
			return pass;
		}
		
		public void onSubmit() {
			sayLoading();
			
			TranslationObject transObjNew = new TranslationObject();
			transObjNew.setType(TranslationObject.DEFINITIONTRANSLATION);
			transObjNew.setLabel(def.getText().replaceAll("\"", "\\\\\""));
			transObjNew.setLang(language.getValue(language.getSelectedIndex()));
			transObjNew.setUri(transObj.getUri());
	
			AsyncCallback<DefinitionObject>  callback = new AsyncCallback<DefinitionObject>(){
				public void onSuccess(DefinitionObject results){
					cDetailObj.setDefinitionObject(results);
					ConceptDefinition.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.conceptAddDefinitionFail());
				}
			};
			
			OwlStatus status = (OwlStatus) initData.getActionStatus().get("term-edit");
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditDefinitionTranslationEdit));
			
			Service.conceptService.editDefinitionLabel(MainApp.userOntology,actionId, status, MainApp.userId, transObj, transObjNew,  conceptObject, callback);
		}
		
	}
	
	public class AddNewDefinition extends FormDialogBox {  
		public ListBox language ;
		public TextArea def ;
		private ListBox source;
		private TextBox URL;
		
		public AddNewDefinition(){ 
			super(constants.buttonCreate(), constants.buttonCancel());
			setWidth("400px");
			this.setText(constants.conceptAddDefinition());
			this.initLayout();
		}
		
		public void initLayout(){
			language = new ListBox();
			language = Convert.makeListBoxWithValue((ArrayList<String[]>)MainApp.getLanguage());
			language.setWidth("100%");
			
			def = new TextArea();
			def.setVisibleLines(3);
			def.setWidth("100%");
			
			source = new ListBox();
			source = Convert.makeSourceListBox((ArrayList<String[]>)initData.getSource());
			source.setWidth("100%");
			
			URL = new TextBox();	
			URL.setWidth("100%");
	
			
			final Grid table = new Grid(4,2);
			table.setWidget(0, 0, new HTML(constants.conceptDefinition()));
			table.setWidget(1, 0, new HTML(constants.conceptLanguage()));
			table.setWidget(2, 0, new HTML(constants.conceptSource()));
			table.setWidget(3, 0, new HTML(constants.conceptUrl()));
			table.setWidget(0, 1, def);
			table.setWidget(1, 1, language);
			table.setWidget(2, 1, source);
			table.setWidget(3, 1, URL);
			table.getColumnFormatter().setWidth(1, "80%");
			table.setWidth("100%");
			
			addWidget(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));
			
			source.addChangeHandler(new ChangeHandler()
			{

			    public void onChange(ChangeEvent arg0)
                {			      
                   if(source.getItemText(source.getSelectedIndex()).equals("Book")){                       
                       ((HTML)table.getWidget(3,0)).setText(constants.conceptSourceTitle());
                   }else{
                       ((HTML)table.getWidget(3,0)).setText(constants.conceptUrl());
                   }
                }
		    });
		}
		
	
		  
		public boolean passCheckInput() {
			boolean pass = false;
			if(language.getValue((language.getSelectedIndex())).equals("--None--") || language.getValue((language.getSelectedIndex())).equals("") || def.getText().equals("") || source.getValue((source.getSelectedIndex())).equals("") || URL.getText().length()==0 ){
				pass = false;
			}else {
				pass = true;
			}
			return pass;
		}
		  
		public void onSubmit(){
			sayLoading();
			
			TranslationObject transObj = new TranslationObject();
			transObj.setType(TranslationObject.DEFINITIONTRANSLATION);
			transObj.setLabel(def.getText());
			transObj.setLang(language.getValue(language.getSelectedIndex()));
			
			IDObject ido = new IDObject();
			ido.setIDType(IDObject.DEFINITION);
			ido.addIDTranslationList(transObj);
			ido.setIDSourceURL(URL.getText());
			ido.setIDSource(source.getValue(source.getSelectedIndex()));

			AsyncCallback<DefinitionObject>  callback = new AsyncCallback<DefinitionObject>(){
				public void onSuccess(DefinitionObject results){
					cDetailObj.setDefinitionObject(results);
					ConceptDefinition.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.conceptAddDefinitionFail());
				}
			};

			OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditDefinitionCreate);
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditDefinitionCreate));

			Service.conceptService.addDefinition(MainApp.userOntology,actionId, status, MainApp.userId, transObj, ido, conceptObject, callback);
		}
	}
}
