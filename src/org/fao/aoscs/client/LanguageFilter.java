package org.fao.aoscs.client;

import java.util.ArrayList;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.locale.LocaleMessages;
import org.fao.aoscs.client.module.classification.Classification;
import org.fao.aoscs.client.module.concept.Concept;
import org.fao.aoscs.client.module.concept.widgetlib.InfoTab;
import org.fao.aoscs.client.module.consistency.Consistency;
import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.module.constant.OWLActionConstants;
import org.fao.aoscs.client.module.document.About;
import org.fao.aoscs.client.module.preferences.Preferences;
import org.fao.aoscs.client.module.preferences.service.UsersPreferenceService.UserPreferenceServiceUtil;
import org.fao.aoscs.client.module.relationship.Relationship;
import org.fao.aoscs.client.module.search.Search;
import org.fao.aoscs.client.module.validation.Validation;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.DialogBoxAOS;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.label.ImageAOS;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabelAOS;
import org.fao.aoscs.domain.LanguageCode;
import org.fao.aoscs.domain.UsersLanguage;
import org.fao.aoscs.domain.UsersLanguageId;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.gen2.table.client.FixedWidthFlexTable;
import com.google.gwt.gen2.table.client.FixedWidthGrid;
import com.google.gwt.gen2.table.client.ScrollTable;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LanguageFilter extends DialogBoxAOS {
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private LocaleMessages messages = (LocaleMessages) GWT.create(LocaleMessages.class);
	private VerticalPanel panel = new VerticalPanel();
	private VerticalPanel vp = new VerticalPanel();
	private VerticalPanel tablePanel = new VerticalPanel();
	
	private Button submitButton = new Button(constants.buttonSave());
	private Button cancelButton = new Button(constants.buttonCancel());
	private ArrayList<LangCheckBox> langCheckBoxContainer = new ArrayList<LangCheckBox>(); // LangCheckBox is element object of this container
	private ArrayList<TextBox> langTextBoxContainer = new ArrayList<TextBox>(); 
	
	private RadioButton selectall = new RadioButton("allOpt" , constants.buttonSelectAll());
	private RadioButton clearall = new RadioButton("allOpt" , constants.buttonClearAll());
	

	MainApp mainApp;
			
	private AddLanguage addLang;
	private EditLanguage editLang;
	private DeleteLanguage deleteLang;
	
	
	public LanguageFilter(MainApp mainApp){
		this.mainApp = mainApp;
		this.setText(constants.langFilterSelectLang());		
		this.setWidth("525px");
		init(MainApp.languageCode);
		setWidget(panel);
	}
	
	public void loadTable(ArrayList<LanguageCode> language)
	{
		tablePanel.clear();
		langTextBoxContainer = new ArrayList<TextBox>(); 
		langCheckBoxContainer = new ArrayList<LangCheckBox>();
		
		FixedWidthFlexTable headerTable = new FixedWidthFlexTable();
		int counter=0;
		FixedWidthGrid dataTable = new FixedWidthGrid(language.size() , 5 );
		for(int i=0;i<language.size();i++)
		{
			LanguageCode languageCode = (LanguageCode)language.get(i);
			LangCheckBox lcb = new LangCheckBox(languageCode);			
			lcb.setWidth("100%");
			for(int j=0;j<MainApp.userSelectedLanguage.size();j++){
				if(lcb.getValue().getLanguageCode().equalsIgnoreCase(MainApp.userSelectedLanguage.get(j))){
					lcb.setCheck();
					counter++;
				}
			}
			langCheckBoxContainer.add(lcb);

			if(MainApp.permissionTable.contains(OWLActionConstants.LANGUAGEEDIT, -1))
			{
				TextBox orderBox = new TextBox();
				orderBox.setWidth("35px");
				orderBox.setText((languageCode.getLanguageOrder()==999)?"":""+languageCode.getLanguageOrder());
				langTextBoxContainer.add(orderBox);
				dataTable.setWidget(i, 0 , orderBox);
			}
			else
				dataTable.setWidget(i, 0 , new HTML((languageCode.getLanguageOrder()==999)?"":""+languageCode.getLanguageOrder()));
			
			
			dataTable.getCellFormatter().addStyleName(i, 0,"gwt-NoBorder");
			dataTable.getCellFormatter().setHorizontalAlignment(i, 0, HasHorizontalAlignment.ALIGN_CENTER);
			
			dataTable.setWidget(i, 1 , lcb);
			dataTable.getCellFormatter().addStyleName(i, 1,"gwt-NoBorder");
			
			dataTable.setWidget(i, 2 , new HTML("&nbsp;"+languageCode.getLanguageNote().toLowerCase()));
			dataTable.getCellFormatter().addStyleName(i,2,"gwt-NoBorder");
			dataTable.getCellFormatter().setHorizontalAlignment(i , 2 , HasHorizontalAlignment.ALIGN_LEFT);
			
			dataTable.setWidget(i, 3 , new HTML(languageCode.getLanguageCode().toLowerCase()));
			dataTable.getCellFormatter().addStyleName(i, 3,"gwt-NoBorder");
			dataTable.getCellFormatter().setHorizontalAlignment(i, 3 , HasHorizontalAlignment.ALIGN_CENTER);
		
			dataTable.setWidget(i, 4 , new FunctionPanel(languageCode));
			dataTable.getCellFormatter().addStyleName(i, 4,"gwt-NoBorder");
			dataTable.getCellFormatter().setHorizontalAlignment(i, 4, HasHorizontalAlignment.ALIGN_CENTER);
		}
		if(counter==language.size()){
			selectall.setValue(true);
		}
		
		

		ScrollTable scrollTable = new ScrollTable(dataTable , headerTable);
		
		scrollTable.setResizePolicy(ScrollTable.ResizePolicy.FILL_WIDTH);
		scrollTable.setCellPadding(0);
		scrollTable.setCellSpacing(0);
		
		scrollTable.setWidth("515px");
	    scrollTable.setHeight("300px");
	    
	    headerTable.setText(0, 0, constants.langFilterOrder());		
		headerTable.setText(0, 1, constants.langFilterLang());	
		headerTable.setText(0, 2, constants.langFilterNote());	
		headerTable.setText(0, 3, constants.langFilterCode());
		headerTable.setText(0, 4, " ");			
		headerTable.addStyleName("gwt-NoBorder");
		headerTable.setHeight("25px");
		
		scrollTable.setColumnWidth(0, 35);
		scrollTable.setColumnWidth(1, 195);
		scrollTable.setColumnWidth(2, 195);
		scrollTable.setColumnWidth(3, 50);
		scrollTable.setColumnWidth(4, 40);
		
		
		headerTable.getFlexCellFormatter().setHorizontalAlignment(0, 0 , HasHorizontalAlignment.ALIGN_CENTER);
		headerTable.getFlexCellFormatter().setHorizontalAlignment(0, 1 , HasHorizontalAlignment.ALIGN_CENTER);
		headerTable.getFlexCellFormatter().setHorizontalAlignment(0, 2 , HasHorizontalAlignment.ALIGN_CENTER);
		headerTable.getFlexCellFormatter().setHorizontalAlignment(0, 3 , HasHorizontalAlignment.ALIGN_CENTER);
		
		tablePanel.add(scrollTable);
	}
	
	public void init(ArrayList<LanguageCode> language)
	{
		panel.clear();
		vp.clear();
		
		submitButton.ensureDebugId("cwBasicButton-normal");
		cancelButton.ensureDebugId("cwBasicButton-normal");
				
		LinkLabelAOS addNewLangLabel = new LinkLabelAOS("images/add-grey.gif", "images/add-grey-disabled.gif", constants.langFilterAddLang(), constants.langFilterAddLang(), MainApp.permissionTable.contains(OWLActionConstants.LANGUAGECREATE, -1), new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(addLang == null || !addLang.isLoaded)
					addLang = new AddLanguage();
				addLang.show();
			}
		});
		
		HorizontalPanel addFunc = new HorizontalPanel();
		addFunc.setSpacing(5);
		addFunc.add(addNewLangLabel);
		vp.add(addFunc);
				
		loadTable(language);
		
		
		vp.add(tablePanel);
		
		
		
		clearall.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {			
				for(int i=0;i<langCheckBoxContainer.size();i++){
					LangCheckBox lcb = (LangCheckBox) langCheckBoxContainer.get(i);
					    lcb.setUncheck();
				}
			}
			
		});

		HorizontalPanel allHp = new HorizontalPanel();
		allHp.setWidth("100%");
		DOM.setStyleAttribute(allHp.getElement(), "background", "url('images/bg_headergradient.png')");
		DOM.setStyleAttribute(allHp.getElement(), "border", "1px solid #BBCDF3");

		
		selectall.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(selectall.getValue()){
					for(int i=0;i<langCheckBoxContainer.size();i++){
						LangCheckBox lcb = (LangCheckBox) langCheckBoxContainer.get(i);
						lcb.setCheck();
					}
				}else{
					for(int i=0;i<langCheckBoxContainer.size();i++){
						LangCheckBox lcb = (LangCheckBox) langCheckBoxContainer.get(i);
						lcb.setUncheck();
					}
				}
			}
		});
		 
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(5);			
		buttonPanel.add(submitButton);
		buttonPanel.add(cancelButton);
		
		HorizontalPanel radioPanel = new HorizontalPanel();
		radioPanel.add(selectall);
		radioPanel.add(clearall);
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(0);
		hp.setWidth("100%");
		hp.setStyleName("bottombar");
		
		hp.add(radioPanel);
		hp.add(buttonPanel);
		
		hp.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		hp.setCellHorizontalAlignment(radioPanel, HasHorizontalAlignment.ALIGN_LEFT);
		hp.setCellVerticalAlignment(radioPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		
		//submit.addClickHandler(this);
		//cancel.addClickHandler(this);
		
		submitButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				boolean isLangSelected = false;
				for(int i=0;i<langCheckBoxContainer.size();i++){
	                if(((LangCheckBox)langCheckBoxContainer.get(i)).isCheck()){
	                	isLangSelected = true;
	                    continue;
	                }
	            }
				if(!isLangSelected)
					Window.alert(constants.langFilterSelectLanguage());
				else{
					if(MainApp.permissionTable.contains(OWLActionConstants.LANGUAGEEDIT, -1))
					{
						if(checkOrderValues())
							updateLang(getLangCode());
						else
							updateUsersLang(getNewUsersLang(1));
					}
					else
						updateUsersLang(getNewUsersLang(2));
					hide();
				}
			}
		
		});
		
		cancelButton.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				// Clear history data
				if(langCheckBoxContainer.size()>0){
					for(int i=0;i<langCheckBoxContainer.size();i++ ){
						LangCheckBox lcb = (LangCheckBox) langCheckBoxContainer.get(i);
						lcb.setUncheck();
					}
				}
				selectall.setValue(false);
				if(MainApp.userSelectedLanguage.size()>0){
					for(int i=0;i<MainApp.userSelectedLanguage.size();i++){
						String lang = (String) MainApp.userSelectedLanguage.get(i);
						for(int j=0;j<langCheckBoxContainer.size();j++ ){
							LangCheckBox lcb = (LangCheckBox) langCheckBoxContainer.get(j);
							if(lcb.getValue().equals(lang)){
								lcb.setCheck();
							}
						}
					}
				}
				hide();
				
			}
		
		});
		
		
				
		vp.setSpacing(5);
		
		HTML msg = new HTML(constants.langFilterMessage());
		HorizontalPanel msghp = new HorizontalPanel();
		msghp.setStyleName("language-message");
		msghp.add(msg);
		msghp.setSpacing(10);
		panel.add(msghp);
		panel.add(vp);
		panel.add(hp);	
		panel.setWidth("100%");
		
	}
	
	private class LangCheckBox extends Composite{
		private CheckBox cb = new CheckBox();
		private LanguageCode value;	
		private HorizontalPanel panel = new HorizontalPanel();
		
		public LangCheckBox(LanguageCode languageCode){
			//this.value = languageCode.getLanguageCode().toLowerCase();
			this.value = languageCode;
			panel.add(cb);
			cb.setText(languageCode.getLocalLanguage().toLowerCase());
			cb.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					checkSelectAll();
					if(!isLangSelected())
						Window.alert(constants.langFilterSelectLanguage());
				}
			});
			initWidget(panel);
		}
		public void setCheck(){
			cb.setValue(true);
		}
		public void setUncheck(){
		    cb.setValue(false);		    
		}
		public LanguageCode getValue(){
			return value;
		}
		public boolean isCheck(){
			return cb.getValue();
		}		
		
		public void checkSelectAll()
		{
		    boolean chk = true;
		    for(int i=0;i<langCheckBoxContainer.size();i++){
                if(!((LangCheckBox)langCheckBoxContainer.get(i)).isCheck()){
                    chk = false;                   
                    break;
                }                
            }
		    selectall.setValue(chk);		   
		}
		
		public boolean isLangSelected(){
			boolean isLangSelected = false;
			for(int i=0;i<langCheckBoxContainer.size();i++){
                if(((LangCheckBox)langCheckBoxContainer.get(i)).isCheck()){
                	isLangSelected = true;
                    continue;
                }
            }
			return isLangSelected;
		}
	}
	
	/*public void onClick(ClickEvent event) {
		Widget sender = (Widget) event.getSource();
		if(sender.equals(cancel)){
			
			// Clear history data
			if(langCheckBoxContainer.size()>0){
				for(int i=0;i<langCheckBoxContainer.size();i++ ){
					LangCheckBox lcb = (LangCheckBox) langCheckBoxContainer.get(i);
					lcb.setUncheck();
				}
			}
			selectall.setValue(false);
			if(MainApp.userSelectedLanguage.size()>0){
				for(int i=0;i<MainApp.userSelectedLanguage.size();i++){
					String lang = (String) MainApp.userSelectedLanguage.get(i);
					for(int j=0;j<langCheckBoxContainer.size();j++ ){
						LangCheckBox lcb = (LangCheckBox) langCheckBoxContainer.get(j);
						if(lcb.getValue().equals(lang)){
							lcb.setCheck();
						}
					}
				}
			}
			this.hide();
		}
		else if(sender.equals(submit))
		{
			if(MainApp.permissionTable.contains(OWLActionConstants.LANGUAGEEDIT, -1))
			{
				if(checkOrderValues())
					updateLang(getLangCode());
				else
					updateUsersLang(getNewUsersLang(1));
			}
			else
				updateUsersLang(getNewUsersLang(2));
			this.hide();						
		}
		
	}*/
	
	private ArrayList<LanguageCode> getLangCode()
	{
		ArrayList<LanguageCode> langlist = new ArrayList<LanguageCode>();
		for(int i=0;i<langTextBoxContainer.size();i++){
			LanguageCode lc = langCheckBoxContainer.get(i).getValue();
			lc.setLanguageOrder(Integer.parseInt(langTextBoxContainer.get(i).getValue()));
			if(checkChanges(lc))
				langlist.add(lc);
		}
		return langlist;
	}
	
	public boolean checkChanges(LanguageCode lc)
	{
		for(LanguageCode oldLc : MainApp.languageCode)
		{
			if(oldLc.getLanguageOrder() != lc.getLanguageOrder())
				return true;
		}
		return false;
	}
	
	public boolean checkOrderValues()
	{
		ArrayList<String> orderList = new ArrayList<String>();
		for(TextBox tb : langTextBoxContainer)
		{
			if(!orderList.contains(tb.getValue()))
			{
				orderList.add(tb.getValue());
			}
			else
			{
				Window.alert(messages.langFilterSameOrderAlert(tb.getValue()));
				return false;
			}
		}
		return true;
	}

	public int getNewOrder()
	{
		int order = 0;
		for(LanguageCode lc : MainApp.languageCode)
		{
			if(lc.getLanguageOrder()>order)
			{
				order = lc.getLanguageOrder();
			}
		}
		order++;
		return order;
	}
	
	private ArrayList<String> getNewUsersLang(int chk)
	{
		
		ArrayList<String> langlist = new ArrayList<String>();
		for(int i=0;i<langCheckBoxContainer.size();i++){
			LangCheckBox lcb = (LangCheckBox) langCheckBoxContainer.get(i);
			if(lcb.isCheck()){
				langlist.add(lcb.getValue().getLanguageCode());
			}
		}
		
		return langlist;
		
	}
	
	public boolean containsLang(ArrayList<UsersLanguage> langlist, String lang)
	{
		for(UsersLanguage l: langlist)
		{
			if (l.getId().getLanguageCode().equals(lang))
            	return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	private void updateLang(final ArrayList<LanguageCode> languageCodes)
	{
		
		if(languageCodes.size()>0)
		{
			AsyncCallback<ArrayList<LanguageCode>> callback = new AsyncCallback<ArrayList<LanguageCode>>() {
			    public void onSuccess(ArrayList<LanguageCode> result) {
			    	MainApp.languageCode = result;
			    	updateUsersLang(getNewUsersLang(0));
			    }
			    public void onFailure(Throwable caught) {
			    	Window.alert(constants.prefLanguageSaveFail());
			    }
			};
			Service.systemService.updateLanguages(languageCodes, callback);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void updateUsersLang(ArrayList<String> langlist)
	{
		AsyncCallback<ArrayList<UsersLanguage>> callbackpref = new AsyncCallback<ArrayList<UsersLanguage>>() {
		    public void onSuccess(ArrayList<UsersLanguage> result) {
		    	MainApp.userSelectedLanguage.clear();
		    	for(UsersLanguage userLang: result)
		    	{
		    		UsersLanguageId  userLangID = userLang.getId();
		    		MainApp.userSelectedLanguage.add(userLangID.getLanguageCode().toLowerCase());					
		    	}
		    	reloadLanguages(mainApp);
		    }
		    public void onFailure(Throwable caught) {
		    	Window.alert(constants.prefLanguageSaveFail());
		    }
		};
		UserPreferenceServiceUtil.getInstance().updateUsersLanguage(MainApp.userId, langlist, callbackpref);

	}
	
	public static void reloadLanguages(MainApp mainApp)
	{
		if(mainApp.modulePanel.getWidgetCount()>0){
			if(mainApp.modulePanel.getWidgetIndex(mainApp.concept)!= -1){
				Concept c = (Concept)mainApp.modulePanel.getWidget(mainApp.modulePanel.getWidgetIndex(mainApp.concept));
				boolean chk = mainApp.modulePanel.getVisibleWidget()==mainApp.modulePanel.getWidgetIndex(mainApp.concept);
				String uri = null;
				
				if(c != null)
				{
					if(c.conceptTree!=null && c.conceptTree.getSelectedConceptObject() != null)
					{
						uri = c.conceptTree.getSelectedConceptObject().getUri(); 
					}
				}
				ModuleManager.resetConcept();
				if(chk)
				{
					if(uri != null)
					{
						mainApp.goToModule("Concepts", uri, ""+InfoTab.term, ""+c.conceptTree.showAlsoNonpreferredTerms.getValue());
					}
					else
					{
						mainApp.goToModule("Concepts");
					}
				}	
			}
			if(mainApp.modulePanel.getWidgetIndex(mainApp.classification)!= -1){
				Classification cls = (Classification)mainApp.modulePanel.getWidget(mainApp.modulePanel.getWidgetIndex(mainApp.classification));
				cls.setDisplayLanguage((ArrayList<String>)MainApp.userSelectedLanguage,cls.isShowAlsoNonpreferredTerms());
			}
			if(mainApp.modulePanel.getWidgetIndex(mainApp.consistency)!= -1){
				((Consistency)mainApp.modulePanel.getWidget(mainApp.modulePanel.getWidgetIndex(mainApp.consistency))).filterByLanguage();
			}
			if(mainApp.modulePanel.getWidgetIndex(mainApp.validation)!= -1){
				((Validation)mainApp.modulePanel.getWidget(mainApp.modulePanel.getWidgetIndex(mainApp.validation))).getValidator().filterByLanguage();
			}
			if(mainApp.modulePanel.getWidgetIndex(mainApp.about)!= -1){
				((About)mainApp.modulePanel.getWidget(mainApp.modulePanel.getWidgetIndex(mainApp.about))).filterByLanguage();
			}
			if(mainApp.modulePanel.getWidgetIndex(mainApp.search)!= -1){
				((Search)mainApp.modulePanel.getWidget(mainApp.modulePanel.getWidgetIndex(mainApp.search))).optionTable.filterByLanguage();
			}
			if(mainApp.modulePanel.getWidgetIndex(mainApp.relationship)!= -1){
			     ((Relationship)mainApp.modulePanel.getWidget(mainApp.modulePanel.getWidgetIndex(mainApp.relationship))).setDisplayLanguage((ArrayList<String>)MainApp.userSelectedLanguage);
			}
			if(mainApp.modulePanel.getWidgetIndex(mainApp.preference)!= -1){
			     ((Preferences)mainApp.modulePanel.getWidget(mainApp.modulePanel.getWidgetIndex(mainApp.preference))).getPrefWB().loadLanglistCS();
			}

		}
		mainApp.loadBrowserWindow();
	}

	public class FunctionPanel extends Composite{
	    ImageAOS edit;
	    ImageAOS delete;
	    
		public FunctionPanel(final LanguageCode languageCode){
			HorizontalPanel hp = new HorizontalPanel();
			hp.setSpacing(2);
			edit = new ImageAOS(constants.langFilterEditLanguage(), "images/edit-grey.gif", "images/edit-grey-disabled.gif", MainApp.permissionTable.contains(OWLActionConstants.LANGUAGEEDIT, -1), new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(editLang == null || !editLang.isLoaded )
						editLang = new EditLanguage(languageCode);
					editLang.show();
				}
			});
			delete = new ImageAOS(constants.langFilterDeleteLanguage(), "images/delete-grey.gif", "images/delete-grey-disabled.gif", MainApp.permissionTable.contains(OWLActionConstants.LANGUAGEDELETE, -1), new ClickHandler() {
				public void onClick(ClickEvent event) 
				{
				    if(languageCode.getLanguageCode().toLowerCase().equals("en") || languageCode.getLanguageCode().toLowerCase().equals("la"))
				    {
                        FormDialogBox fdb = new FormDialogBox(constants.buttonOk());
                        HorizontalPanel hp = new HorizontalPanel();
                        Image wImg = new Image("images/Warning.png");
                        HTML wText = new HTML(constants.langFilterDeleteWarning());
                        wText.setHeight("100%");
                        hp.add(wImg);
                        hp.add(wText);
                        hp.setCellVerticalAlignment(wImg, HasVerticalAlignment.ALIGN_MIDDLE);
                        fdb.addWidget(hp);
                        fdb.setText("Warning");                     
                        fdb.show();
                    }				        
				    else{
    					if(deleteLang == null || !deleteLang.isLoaded)
    						deleteLang = new DeleteLanguage(languageCode);
    					deleteLang.show();					
				    }
				}
			});
			delete.setStyleName("cursor-hand");
			hp.add(edit);
			hp.add(delete);	
			initWidget(hp);
		}
		
	}
	
	public void sayLoading(){
		panel.clear();
		LoadingDialog sayLoading = new LoadingDialog();
		panel.setSize("525px", "500px");
		panel.add(sayLoading);
		panel.setCellHorizontalAlignment(sayLoading, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(sayLoading, HasVerticalAlignment.ALIGN_MIDDLE);
	}
	
	public void tableLoading(){
		String width = ""+tablePanel.getOffsetWidth();
		String height = ""+tablePanel.getOffsetHeight();
		tablePanel.clear();
		tablePanel.setSize(width, height);
		LoadingDialog sayLoading = new LoadingDialog();
		tablePanel.add(sayLoading);
		tablePanel.setCellHorizontalAlignment(sayLoading, HasHorizontalAlignment.ALIGN_CENTER);
		tablePanel.setCellVerticalAlignment(sayLoading, HasVerticalAlignment.ALIGN_MIDDLE);
	}
	
	public class AddLanguage extends FormDialogBox implements ClickHandler{
		private TextBox code;
		private TextBox label;
		private TextArea note;
		
		public AddLanguage(){
			super();
			this.setText(constants.langFilterAddLanguage());
			setWidth("400px");
			this.initLayout();
		}
		
		public void initLayout() {
			code = new TextBox();
			code.setWidth("100%");
			
			label = new TextBox();
			label.setWidth("100%");
			
			note = new TextArea();
			note.setWidth("100%");
			note.setHeight("100");
			

			Grid table = new Grid(3,2);
			table.setWidget(0, 0, new HTML(constants.langFilterCode()));			
			table.setWidget(1, 0, new HTML(constants.langFilterLang()));			
			table.setWidget(2, 0, new HTML(constants.langFilterNote()));			
			table.setWidget(0, 1, code);
			table.setWidget(1, 1, label);
			table.setWidget(2, 1, note);
			table.setWidth("100%");
			table.getColumnFormatter().setWidth(1, "80%");
			
			addWidget(GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1"));
		}
		
		public boolean passCheckInput() {
			boolean pass = false;
			
			if(code.getText().length()==0 || label.getText().length()==0){
				pass = false;
			}else {
				pass = true;
			}
			return pass;
		}
		
		public void onSubmit() {
			tableLoading();
			AsyncCallback<ArrayList<LanguageCode>> callback = new AsyncCallback<ArrayList<LanguageCode>>(){
				public void onSuccess(ArrayList<LanguageCode> language){
					reloadLanguage(language);
					if(ConfigConstants.ISINDEXING)
					{
						AsyncCallback<Boolean> callback1 = new AsyncCallback<Boolean>(){
							public void onSuccess(Boolean language){	
							}
							public void onFailure(Throwable caught){
								Window.alert(constants.langFilterAddLangFail());
							}
						};
						
						Service.searchSerice.reloadSearchEngine(MainApp.userOntology, callback1);
					}
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.langFilterAddLangFail());
				}
			};
			LanguageCode languageCode = new LanguageCode();
			languageCode.setLanguageCode(code.getText());
			languageCode.setLocalLanguage(label.getText());
			languageCode.setLanguageNote(note.getText());
			languageCode.setLanguageOrder(getNewOrder());
			
			Service.systemService.addLanguage(languageCode, callback);
		}
		
	}
	
	public class EditLanguage extends FormDialogBox implements ClickHandler{
		private TextBox label;
		private TextArea note;
		private LanguageCode languageCode;
		
		public EditLanguage(LanguageCode languageCode){
			super();
			this.languageCode = languageCode;
			this.setText(constants.langFilterEditLanguage());
			setWidth("400px");
			this.initLayout();
		}
		
		public void initLayout() {
			label = new TextBox();
			label.setWidth("100%");
			label.setText(languageCode.getLocalLanguage());
			
			note = new TextArea();
			note.setWidth("100%");
			note.setHeight("100");
			note.setText(languageCode.getLanguageNote());

			Grid table = new Grid(3,2);
			table.setWidget(0, 0, new HTML(constants.langFilterCode()));			
			table.setWidget(1, 0, new HTML(constants.langFilterLang()));
			table.setWidget(2, 0, new HTML(constants.langFilterNote()));
			table.setWidget(0, 1, new HTML(languageCode.getLanguageCode()));
			table.setWidget(1, 1, label);
			table.setWidget(2, 1, note);
			table.setWidth("100%");
			table.getColumnFormatter().setWidth(1, "80%");
			
			addWidget(GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1"));
		}
		
		public boolean passCheckInput() {
			boolean pass = false;
			if(label.getText().length()==0){
				pass = false;
			}else {
				pass = true;
			}
			return pass;
		}
		
		public void onSubmit(){
			tableLoading();
				AsyncCallback<ArrayList<LanguageCode>> callback = new AsyncCallback<ArrayList<LanguageCode>>(){
					public void onSuccess(ArrayList<LanguageCode> language){
						reloadLanguage(language);
					}
					public void onFailure(Throwable caught){
						Window.alert(constants.langFilterEditLangFail());
					}
				};
				
				LanguageCode newlanguageCode = new LanguageCode();
				newlanguageCode.setLanguageCode(languageCode.getLanguageCode());
				newlanguageCode.setLocalLanguage(label.getText());
				newlanguageCode.setLanguageNote(note.getText());
				newlanguageCode.setLanguageOrder(languageCode.getLanguageOrder());
				
				Service.systemService.editLanguage(newlanguageCode, callback);
			
		}
	}
	
	public class DeleteLanguage extends FormDialogBox implements ClickHandler{
		private LanguageCode langaugeCode;
		
		public DeleteLanguage(LanguageCode langaugeCode){
			super(constants.buttonDelete(), constants.buttonCancel());
			this.langaugeCode = langaugeCode;
			this.setText(constants.langFilterDeleteLanguage());
			setWidth("400px");
			this.initLayout();
			
		}
		public void initLayout() {
			Grid table = new Grid(1,2);
			table.setWidget(0,0,getWarningImage());
			table.setWidget(0,1, new HTML(messages.langFilterDeleteAlert(langaugeCode.getLocalLanguage(), langaugeCode.getLanguageCode().toLowerCase())));
			addWidget(table);
		}

		public void onSubmit() {
			tableLoading();
			AsyncCallback<ArrayList<LanguageCode>> callback = new AsyncCallback<ArrayList<LanguageCode>>(){
				public void onSuccess(ArrayList<LanguageCode> results){
					ArrayList<LanguageCode> language = (ArrayList<LanguageCode>) results;
					reloadLanguage(language);
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.langFilterDeleteLangFail());
				}
			};
			
			Service.systemService.deleteLanguage(langaugeCode, callback);
		}
	}
	
	public void reloadLanguage(ArrayList<LanguageCode> languageCode)
	{
		MainApp.languageCode = languageCode;
		loadTable(MainApp.languageCode);
	}
	

}
