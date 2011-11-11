package org.fao.aoscs.client.module.preferences.widgetlib;

import java.util.ArrayList;
import java.util.Iterator;

import org.fao.aoscs.client.LanguageFilter;
import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.classification.widgetlib.OlistBox;
import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.module.preferences.service.UsersPreferenceService.UserPreferenceServiceUtil;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.DialogBoxAOS;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.panel.BodyPanel;
import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;
import org.fao.aoscs.client.widgetlib.shared.panel.TitleBodyWidget;
import org.fao.aoscs.domain.InitializeUsersPreferenceData;
import org.fao.aoscs.domain.LanguageCode;
import org.fao.aoscs.domain.LanguageInterface;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.Users;
import org.fao.aoscs.domain.UsersLanguage;
import org.fao.aoscs.domain.UsersLanguageId;
import org.fao.aoscs.domain.UsersOntology;
import org.fao.aoscs.domain.UsersOntologyId;
import org.fao.aoscs.domain.UsersPreference;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class WBPreferences extends Composite implements ClickHandler{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private VerticalPanel panel = new VerticalPanel();
	private TextBox userlogin = new TextBox();
	private PasswordTextBox newpassword = new PasswordTextBox();
	private PasswordTextBox confirmpassword = new PasswordTextBox();
	private TextBox useremail = new TextBox();
	private TextBox newuseremail = new TextBox();
	private TextBox confirmuseremail = new TextBox();
	private OlistBox langlistCS = new OlistBox(true);
	
	private Image btnaddlang = new Image("images/add-grey.gif");
	private Image btnremovelang = new Image("images/delete-grey.gif");
	
	private Image btnaddonto = new Image("images/add-grey.gif");
	
	private ListBox langlistInterface = new ListBox();
	private OlistBox ontologylistPending = new OlistBox(true);
	private ListBox ontology = new ListBox();
	private CheckBox showURICheck = new CheckBox();
	private CheckBox showAlsoNonPreferredCheck = new CheckBox();
	private CheckBox showAlsoNonselectedLanguages = new CheckBox();
	private CheckBox hideDeprecatedCheck = new CheckBox();
	private ListBox initialScreen = new ListBox();
	private Button savepref = new Button();
	private Button clearpref = new Button();
	private Users user = new Users();
	private LanguageDataDialog newlangDialog;
	private OntologyPendingDialog newontoDialog;
	
	public WBPreferences()
	{
		/*
		 * Load the necessary information 
		 * 
		 * */			
		
		LoadingDialog loadingDialog = new LoadingDialog();
		panel.clear();
		panel.setSize("100%", "100%");
	    panel.add(loadingDialog);
	    panel.setCellHorizontalAlignment(loadingDialog, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(loadingDialog, HasVerticalAlignment.ALIGN_MIDDLE);
		
		final AsyncCallback<Object> callback = new AsyncCallback<Object>() {
			public void onSuccess(Object result) {
				InitializeUsersPreferenceData initUserPreference = (InitializeUsersPreferenceData) result;
				init(initUserPreference);
			}
			public void onFailure(Throwable caught) {
				Window.alert(constants.prefNotLoad());
			}
		};
		Service.userPreferenceService.getInitData(MainApp.userId, callback);		
	    initWidget(panel);		  
	}
	
	public void init(InitializeUsersPreferenceData initUserPreference){  

		final FlexTable detailPanel = new FlexTable();
		detailPanel.setSize("100%", "100%");
			  
		// Login info
		for(int i=0;i<detailPanel.getRowCount();i++)
		{
			detailPanel.getCellFormatter().setWidth(i, 0, "40%");	
			detailPanel.getCellFormatter().setWidth(i, 1, "60%");	
		}
		
		detailPanel.setWidget(0,0, new HTML(constants.prefLogin()));
	    detailPanel.setWidget(0,1, userlogin);
	    userlogin.setWidth("100%");
	    detailPanel.setWidget(1,0, new HTML(constants.prefNewPassword()));
	    detailPanel.setWidget(1,1, newpassword);
	    newpassword.setWidth("100%");
	    newpassword.addValueChangeHandler(new ValueChangeHandler<String>(){public void onValueChange(ValueChangeEvent<String> event){MainApp.dataChanged = true;}});
	    detailPanel.setWidget(2,0, new HTML(constants.prefConfirmPassword()));
	    detailPanel.setWidget(2,1, confirmpassword);
	    confirmpassword.setWidth("100%");
	    	    
	    detailPanel.setWidget(3,0, new HTML(constants.prefEmail()));
	    detailPanel.setWidget(3,1, useremail);
	    useremail.setWidth("100%");
	    detailPanel.setWidget(4,0, new HTML(constants.prefNewEmail()));
	    detailPanel.setWidget(4,1, newuseremail);	    	    
	    newuseremail.setWidth("100%");
	    newuseremail.addValueChangeHandler(new ValueChangeHandler<String>(){public void onValueChange(ValueChangeEvent<String> event){MainApp.dataChanged = true;}});
	    detailPanel.setWidget(5,0, new HTML(constants.prefConfirmEmail()));
	    detailPanel.setWidget(5,1, confirmuseremail);	    	    
	    confirmuseremail.setWidth("100%");
	    
	    // Ontology
	    detailPanel.setWidget(6,0, new HTML(constants.prefOntology()));
	    detailPanel.setWidget(6,1, ontology);
		ontology.setWidth("100%");		
		
		// Initial screen preferences
		detailPanel.setWidget(7, 0, new HTML(constants.prefInitialScreen()));
		detailPanel.setWidget(7, 1, initialScreen);
		initialScreen.setWidth("100%");
		
		// Initial screen preferences
		detailPanel.setWidget(8, 0, new HTML(constants.prefLanguageInterface()));
		detailPanel.setWidget(8, 1, langlistInterface);
		langlistInterface.setWidth("100%");
		
		// FILTER OPTION CHECK BOXES
		showURICheck.setText(constants.prefShowURI());
		showAlsoNonPreferredCheck.setText(constants.prefShowNonPreferredTermsAlso());
		showAlsoNonselectedLanguages.setText(constants.prefShowAlsoNonselectedLanguages());
		hideDeprecatedCheck.setText(constants.prefHideDeprecated());
		
		FlexTable filterOption = new FlexTable();
		filterOption.setSize("100%", "100%");
		filterOption.setCellSpacing(0);
		filterOption.setCellPadding(1);
		filterOption.setWidget(0, 0, showURICheck);
		filterOption.setWidget(0, 1, showAlsoNonPreferredCheck);
		filterOption.setWidget(1, 0, showAlsoNonselectedLanguages);
		filterOption.setWidget(1, 1, hideDeprecatedCheck);
		
		// SAVE and CLEAR
		savepref.setText(constants.buttonSave());
		savepref.addClickHandler(this);
		clearpref.setText(constants.buttonClear());
		clearpref.addClickHandler(this);
		
		FlexTable submit_ft = new FlexTable();
		submit_ft.setSize("100%", "100%");
		submit_ft.setWidget(0, 0, savepref);
		submit_ft.setWidget(0, 1, clearpref);
		submit_ft.setSize("100px", "100%");
		
		HorizontalPanel panel_row3 = new HorizontalPanel();
		panel_row3.setSize("100%", "35px");
		panel_row3.add(submit_ft);
		panel_row3.addStyleName("bottombar");
		panel_row3.setCellHorizontalAlignment(submit_ft, HasHorizontalAlignment.ALIGN_RIGHT);
		
		final VerticalPanel vPanel = new VerticalPanel();		
		vPanel.add(GridStyle.setTableRowStyle(detailPanel, "#F4F4F4", "#E8E8E8", 3));
		vPanel.add(filterOption);
		vPanel.add(panel_row3);
		vPanel.setCellVerticalAlignment(panel_row3,  HasVerticalAlignment.ALIGN_BOTTOM);
		vPanel.setCellHorizontalAlignment(panel_row3,  HasHorizontalAlignment.ALIGN_CENTER);
		vPanel.setCellHeight(panel_row3, "100%");
		
		TitleBodyWidget vpPanel = new TitleBodyWidget(constants.prefDetails(), vPanel, null, ((MainApp.getBodyPanelWidth()-120)/2) + "px", "100%");
		
		
		ArrayList<String> userMenu = (ArrayList<String>) MainApp.userMenu;
		Iterator<String> lst = userMenu.iterator();
    	while(lst.hasNext()){
    		initialScreen.addItem((String) lst.next());
    	}
		initialScreen.setSelectedIndex(1);
      
		// LANGUAGE LIST
		//langlistCS.setVisibleItemCount(25);
		
		

		btnaddlang.setTitle(constants.buttonAdd());
		btnremovelang.setTitle(constants.buttonRemove());
		btnaddlang.addClickHandler(this);
		btnremovelang.addClickHandler(this);
		
		HorizontalPanel hpnbtngroup = new HorizontalPanel();
		hpnbtngroup.add(btnaddlang);
		hpnbtngroup.add(btnremovelang);
		
		TitleBodyWidget vpLang = new TitleBodyWidget(constants.prefLanguageCS(), langlistCS, hpnbtngroup, ((MainApp.getBodyPanelWidth()-120)/4)  + "px", "100%");
	
		//ontologylistPending.setVisibleItemCount(28);
		
		btnaddonto.setTitle(constants.buttonAdd());
		btnaddonto.addClickHandler(this);
		
		HorizontalPanel hpnbtnonto = new HorizontalPanel();
		hpnbtnonto.add(btnaddonto);
		
		TitleBodyWidget ontology_ft_pending = new TitleBodyWidget("Pending ontology request", ontologylistPending, hpnbtnonto, ((MainApp.getBodyPanelWidth()-120)/4)  + "px", "100%");
		
		HorizontalPanel rightPanel = new HorizontalPanel();
		rightPanel.setWidth("100%");
		rightPanel.add(vpLang);		
		rightPanel.add(new Spacer("25px","100%"));		
		rightPanel.add(ontology_ft_pending);
		
		// FLEX PANEL FOR DETIAL, LANGUAGE LIST, INTERFACE LANGUAGE LIST
		HorizontalPanel flexpanel = new HorizontalPanel(); 		
		flexpanel.setSpacing(10);
		flexpanel.setWidth("100%");
		flexpanel.add(vpPanel);	
		flexpanel.add(rightPanel);
		
		Scheduler.get().scheduleDeferred(new Command() {
            public void execute()
            {  
            	langlistCS.setHeight(vPanel.getOffsetHeight()+"px");
            	ontologylistPending.setHeight(vPanel.getOffsetHeight()+"px");
            }
        });		

		flexpanel.setCellVerticalAlignment(vpPanel, HasVerticalAlignment.ALIGN_TOP);
		flexpanel.setCellHorizontalAlignment(vpPanel, HasHorizontalAlignment.ALIGN_CENTER);
		flexpanel.setCellVerticalAlignment(vpLang, HasVerticalAlignment.ALIGN_TOP);
		flexpanel.setCellHorizontalAlignment(vpLang, HasHorizontalAlignment.ALIGN_CENTER);
		flexpanel.setCellVerticalAlignment(ontology_ft_pending, HasVerticalAlignment.ALIGN_TOP);
		flexpanel.setCellHorizontalAlignment(ontology_ft_pending, HasHorizontalAlignment.ALIGN_CENTER);
		
		
		
		VerticalPanel ft_panel = new VerticalPanel();
		ft_panel.setSize("100%", "100%");
		ft_panel.add(flexpanel);
		ft_panel.setCellHorizontalAlignment(flexpanel, HasHorizontalAlignment.ALIGN_CENTER);
		ft_panel.setCellVerticalAlignment(flexpanel, HasVerticalAlignment.ALIGN_TOP);
		ft_panel.setSpacing(5);
		
		
		// Set languages for the CS
		loadLanglistCS();
		
		// Set requested ontology
		loadOntologyPending();
		
		// Set languages for the interface
		Iterator<LanguageInterface> list = initUserPreference.getInterfaceLanguage().iterator();
		while(list.hasNext()){
			LanguageInterface langInterface = (LanguageInterface) list.next();
			langlistInterface.addItem(langInterface.getLocalLanguage()+" ("+langInterface.getLanguageCode().toLowerCase()+")", langInterface.getLanguageCode());
		}
		
		// Fill the ontology
    	for (Iterator<OntologyInfo> iter = initUserPreference.getOntology().iterator(); iter.hasNext();) {
				
    		OntologyInfo ontoInfo = (OntologyInfo) iter.next();
    		ontology.addItem(ontoInfo.getOntologyName(), ""+ontoInfo.getOntologyId());
    	}
		
    	loadUser(initUserPreference.getUsersInfo());
    	MainApp.userPreference = initUserPreference.getUsersPreference();
    	loadUserPref();
		
		VerticalPanel bodyPanel = new VerticalPanel();
	    bodyPanel.setSize("100%", "100%");
		bodyPanel.add(ft_panel);
		bodyPanel.setCellHorizontalAlignment(ft_panel,  HasHorizontalAlignment.ALIGN_CENTER);
		bodyPanel.setCellVerticalAlignment(ft_panel,  HasVerticalAlignment.ALIGN_TOP);
				
		BodyPanel mainPanel = new BodyPanel(constants.preferences() , bodyPanel , null);
		panel.clear();
		panel.add(mainPanel);	      
	    panel.setCellHorizontalAlignment(mainPanel,  HasHorizontalAlignment.ALIGN_CENTER);
	    panel.setCellVerticalAlignment(mainPanel,  HasVerticalAlignment.ALIGN_TOP);
		
	} // end main corpus: WBPreferences
	
	public void loadUser(Users u)
	{
    	// Set user info
    	user = u;
    	// Set values
    	userlogin.setReadOnly(true);
    	userlogin.setText(user.getUsername());		  
		// Set email with current user email
    	useremail.setReadOnly(true);
		useremail.setText(user.getEmail());			
		newpassword.setText("");
		confirmpassword.setText("");
		newuseremail.setText("");
		confirmuseremail.setText("");		
	}
	
	public void loadUserPref()
	{
		// Set user preference
		UsersPreference userPreference = (UsersPreference) MainApp.userPreference;
		if(userPreference.getUserId()!=0)
		{
			ontology.setSelectedIndex(getIndex(ontology, ""+userPreference.getOntologyId()));
			initialScreen.setSelectedIndex(getIndex(initialScreen, ""+userPreference.getInitialPage()));
			langlistInterface.setSelectedIndex(getIndex(langlistInterface, ""+userPreference.getLanguageCodeInterface()));
			showURICheck.setValue(!userPreference.isHideUri());
			showAlsoNonPreferredCheck.setValue(!userPreference.isHideNonpreferred());
			showAlsoNonselectedLanguages.setValue(!userPreference.isHideNonselectedlanguages());
			hideDeprecatedCheck.setValue(userPreference.isHideDeprecated());
		}
		else
		{
			langlistInterface.setSelectedIndex(getIndex(langlistInterface, "en"));
	        showURICheck.setValue(false);
	        showAlsoNonPreferredCheck.setValue(false);
	        showAlsoNonselectedLanguages.setValue(false);
	        hideDeprecatedCheck.setValue(false);
	        initialScreen.setSelectedIndex(0);
		}
		
	}
	
	public void loadOntologyPending()
	{
		
		AsyncCallback<ArrayList<String[]>> callbackpref = new AsyncCallback<ArrayList<String[]>>() {
		    public void onSuccess(ArrayList<String[]> user) {
		    	for(int i=0;i<user.size();i++){
		    		String[] item = (String[]) user.get(i);
		    		ontologylistPending.addItem(item[0], item[1]);					    		
		    	}
		    }

		    public void onFailure(Throwable caught) {
		    	Window.alert(constants.userListOntologyFail());
		    }
		};
		UserPreferenceServiceUtil.getInstance().getPendingOntology(MainApp.userId, callbackpref);
	}
	
	public void loadLanglistCS()
	{
		langlistCS.clear();
		
		ArrayList<LanguageCode> lang = (ArrayList<LanguageCode>) MainApp.languageCode;
		ArrayList<String> userLang = new ArrayList<String>(MainApp.userSelectedLanguage);
		
		
		/*for(int i=0 ; i<MainApp.defaultLang.size() ; i++)
        {
            String dlang = MainApp.defaultLang.get(i).toLowerCase();
            if (!userLang.contains(dlang))
				userLang.add(dlang); 
        }  */
		
		
		Iterator<LanguageCode> list = lang.iterator();
		int cnt = 0;
		while(list.hasNext()){
			LanguageCode lc = (LanguageCode) list.next();
			if(userLang.contains(lc.getLanguageCode().toLowerCase()))	
			{
				langlistCS.addItem(lc.getLanguageNote(), lc);
			}
			cnt++;
		}
		
	}
	
	public int getIndex(ListBox list, String value)
	{
		for(int i=0;i<list.getItemCount();i++)
		{
			if(value.toLowerCase().equals(list.getValue(i).toLowerCase()))
				return i;
		}
		return 0;
	}
	
	// Check form validation
	public boolean validateUser()
	{
		if(newuseremail.getText().equals("") && confirmuseremail.getText().equals("") && newpassword.getText().equals("") && confirmpassword.getText().equals(""))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	
	public boolean validateEmailChange(){	
		boolean isComplete =true;
		if(!newuseremail.getText().equals("") || !confirmuseremail.getText().equals(""))
		{
			if(!newuseremail.getText().equals(confirmuseremail.getText())) {
					Window.alert(constants.prefEmailMismatch());
					newuseremail.setFocus(true);
					isComplete = false;
			}
			else
				user.setEmail(newuseremail.getText());
		}
		return isComplete;
	}
	
	public boolean validatePasswordChange()
	{	
		boolean isComplete =true;		
		if(!newpassword.getText().equals("")  || !confirmpassword.getText().equals(""))
		{
			if(!newpassword.getText().equals(confirmpassword.getText())) {
				Window.alert(constants.prefPasswordMismatch());
				newpassword.setFocus(true);
				isComplete = false;
			}
			else if (newpassword.getText().length()<6){
				Window.alert(constants.prefPasswordMinCharacter());
				newpassword.setFocus(true);
				isComplete=false;
			}
			else
			{
				user.setPassword(newpassword.getText());
			}
		}
		return isComplete;
	}
	
	public int getSelectedItemCnt()
	{
		int cnt = 0;
		for(int i=0;i<langlistCS.getItemCount();i++){
			if(langlistCS.isItemSelected(i)) {
				cnt++;
			}
		}
		return cnt;
	}
	
    /*
     * Functions to manage events 
     * 
     * */
	
	public void onClick(ClickEvent event) {
		Widget sender = (Widget) event.getSource();
		if (sender==btnaddlang){
			if(newlangDialog == null || !newlangDialog.isLoaded)
				newlangDialog = new LanguageDataDialog();				
			newlangDialog.show();
		}
		else if (sender==btnremovelang){
			boolean isAllNotSelected = false;
			for(int i = langlistCS.getItemCount() -1;i>=0; i--){
				if(!langlistCS.isItemSelected(i))
				{
					isAllNotSelected = true;
				}
			}
			if(!isAllNotSelected)
			{
				Window.alert(constants.prefLanguageNoRemoveAll());
				return;
			}
			else if(langlistCS.getSelectedIndex()==-1)
			{
				Window.alert(constants.prefLanguageNotSelected());
				return;
			}
			else if((Window.confirm(constants.prefLanguageSelectRemove()))==false)
			{
				return;
			}
			for(int i = langlistCS.getItemCount() -1;i>=0; i--){
				if( langlistCS.isItemSelected(i))
				{
					langlistCS.removeItem(i);
				}
			}
			updateUsersLang(getUsersLang());
											    
		}
		else if (sender==btnaddonto){
			if(newontoDialog == null || !newontoDialog.isLoaded)
				newontoDialog = new OntologyPendingDialog();				
			newontoDialog.show();
		}
		else if(sender == clearpref){			
			clear();

		} else if(sender == savepref){

			if(validateUser())
			{ 
				if(validatePasswordChange() && validateEmailChange())
					updateUser();
			}
			else
			{
				updatePref();				
			}
		} 

	} 
	
	public void clear()
	{
		// reset default values
		loadUser(user);
		loadUserPref(); 
		
	}
	
	public void updateUser()
	{
		boolean isPasswordChange = false;
		if(!newpassword.getText().equals("")  && !confirmpassword.getText().equals(""))
		{
			isPasswordChange = true;
		}
		
		if(isPasswordChange)
		{
			if(newuseremail.getText().equals("") && confirmuseremail.getText().equals(""))
			{
				user.setEmail(useremail.getText());
			}
		}
		
			
		
		AsyncCallback<Object> callbackuser = new AsyncCallback<Object>() {
		    public void onSuccess(Object result) {
		    	user = (Users) result;
		    	loadUser(user);
		    	
		    	updatePref();
		    }
		    public void onFailure(Throwable caught) {
		    	Window.alert(constants.prefPasswordEmailchangeFail());
		    }
		};
		UserPreferenceServiceUtil.getInstance().updateUsers(user, isPasswordChange ,callbackuser);
	}
	
	private ArrayList<String> getUsersLang()
	{
		
		ArrayList<String> langlist = new ArrayList<String>();
		for(int i=0;i<langlistCS.getItemCount();i++){
			LanguageCode lc = (LanguageCode) langlistCS.getObject(i);
			langlist.add(lc.getLanguageCode());
		}
		return langlist;
	}

	@SuppressWarnings("unchecked")
	private void updateUsersLang(final ArrayList<String> langlist)
	{
		AsyncCallback<ArrayList<UsersLanguage>> callbackpref = new AsyncCallback<ArrayList<UsersLanguage>>() {
		    public void onSuccess(ArrayList<UsersLanguage> result) {
		    	ArrayList<String> userLanguage = new ArrayList<String>();
		    	for(int i=0;i<result.size();i++){		
		    		UsersLanguage userLang = result.get(i);
		    		UsersLanguageId  userLangID = userLang.getId();
					userLanguage.add(userLangID.getLanguageCode().toLowerCase());	
		    	}
		    	MainApp.userSelectedLanguage = userLanguage;
		    	loadLanglistCS();
		    	LanguageFilter.reloadLanguages(ModuleManager.getMainApp());
		    }
		    public void onFailure(Throwable caught) {
		    	Window.alert(constants.prefLanguageSaveFail());
		    }
		};
		UserPreferenceServiceUtil.getInstance().updateUsersLanguage(MainApp.userId, langlist, callbackpref);
	}
	
	public void updatePref()
	{
		AsyncCallback<Object> callbackpref = new AsyncCallback<Object>() {
		    public void onSuccess(Object result) {
		    	MainApp.userPreference = (UsersPreference) result;
		    	loadUserPref();
		    	
		    	Window.alert(constants.prefSaved());
		    }
		    public void onFailure(Throwable caught) {
		    	Window.alert(constants.prefNotSaved());
		    }
		};
		
		UsersPreference userPref = new UsersPreference();
		userPref.setOntologyId(Integer.parseInt(ontology.getValue(ontology.getSelectedIndex())));
		userPref.setFrequency("Daily");		
		userPref.setInitialPage(initialScreen.getValue(initialScreen.getSelectedIndex()));
		userPref.setLanguageCodeInterface(langlistInterface.getValue(langlistInterface.getSelectedIndex()));
		userPref.setHideUri(!showURICheck.getValue());
		userPref.setHideNonpreferred(!showAlsoNonPreferredCheck.getValue());
		userPref.setHideNonselectedlanguages(!showAlsoNonselectedLanguages.getValue());
		userPref.setHideDeprecated(hideDeprecatedCheck.getValue());
		
		if(MainApp.userPreference.getUserId()==0)
		{
			userPref.setUserId(MainApp.userId);
			UserPreferenceServiceUtil.getInstance().addUsersPreference(userPref, callbackpref);
		}
		else
		{
			userPref.setUserId(MainApp.userPreference.getUserId());
			UserPreferenceServiceUtil.getInstance().updateUsersPreference(userPref, callbackpref);
		}

	    // Todo
	    // Update the session variables if password is changed 
	}
	
	public void savePreference()
	{
	    if(validateUser())
        { 
            if(validatePasswordChange() && validateEmailChange())
                updateUser();
            
        }
        else
        {
            updatePref();               
        }
	}
	
	private class LanguageDataDialog extends DialogBoxAOS implements ClickHandler{
		private VerticalPanel userpanel = new VerticalPanel();
		private Button btnAdd = new Button(constants.buttonAdd());
		private Button btnCancel = new Button(constants.buttonCancel());
		private OlistBox lstdata = new OlistBox(true);

		public LanguageDataDialog() {
			this.setText(constants.prefSelectLanguages());				
			final FlexTable table = new FlexTable();
			table.setBorderWidth(0);
			table.setCellPadding(0);
			table.setCellSpacing(1);
			table.setWidth("100%");
			
			lstdata.clear();
			
			ArrayList<LanguageCode> lang = MainApp.languageCode;		
			
			Iterator<LanguageCode> list = lang.iterator();
			while(list.hasNext()){
				LanguageCode lc = (LanguageCode) list.next();				
				if(!langlistCS.hasItem(lc.getLanguageNote()))
				{						
					lstdata.addItem(lc.getLanguageNote(), lc);			
				}
			}
			
			table.setWidget(0, 0, new HTML(""));
			table.setWidget(1,0,lstdata);
		    lstdata.setVisibleItemCount(20);
			lstdata.setSize("250px", "200px");

			table.getFlexCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
			table.getFlexCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_CENTER);
			
			// Popup element
			HorizontalPanel tableHP = new HorizontalPanel();
			tableHP.setSpacing(10);
			tableHP.add(table);
			userpanel.add(tableHP);
			HorizontalPanel hp = new HorizontalPanel();
			hp.add(btnAdd);				
			btnAdd.addClickHandler(new ClickHandler()
			{
				public void onClick(ClickEvent event) {
					if(lstdata.getSelectedIndex()==-1)
					{
						Window.alert(constants.prefLanguageSelectOne());
						return;
					}					
					for(int i=0;i<lstdata.getItemCount();i++)
					{						
						if(lstdata.isItemSelected(i)){
							LanguageCode lc = (LanguageCode) lstdata.getObject(i);							
							langlistCS.addItem(lc.getLanguageNote(), lc);
						}
					}	
					hide();					
					updateUsersLang(getUsersLang());
				}
				
			});
			hp.add(btnCancel);
			hp.setSpacing(5);
			
			VerticalPanel hpVP = new VerticalPanel();			
			hpVP.setStyleName("bottombar");
			hpVP.setWidth("100%");
			hpVP.add(hp);
			hpVP.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_RIGHT);
			
			btnCancel.addClickHandler(new ClickHandler()
			{
				public void onClick(ClickEvent event) {
					hide();
				}
				
			});
			userpanel.add(hpVP);
			
			userpanel.setCellHorizontalAlignment(hp,HasHorizontalAlignment.ALIGN_CENTER);
			setWidget(userpanel);
		}
	}
	
	private class OntologyPendingDialog extends DialogBoxAOS implements ClickHandler{
		private VerticalPanel userpanel = new VerticalPanel();
		private Button btnAdd = new Button(constants.buttonAdd());
		private Button btnCancel = new Button(constants.buttonCancel());
		private OlistBox lstdata = new OlistBox(true);

		public OntologyPendingDialog() {
			this.setText(constants.userSelectOntology());				
			final FlexTable table = new FlexTable();
			table.setBorderWidth(0);
			table.setCellPadding(0);
			table.setCellSpacing(1);
			table.setWidth("100%");
			
			lstdata.clear();
			
			AsyncCallback<ArrayList<String[]>> callbackpref = new AsyncCallback<ArrayList<String[]>>() {
			    public void onSuccess(ArrayList<String[]> user) {
			    	for(int i=0;i<user.size();i++){
			    		String[] item = (String[]) user.get(i);
			    		lstdata.addItem(item[0], item[1]);					    		
			    	}
			    }

			    public void onFailure(Throwable caught) {
			    	Window.alert(constants.userListOntologyFail());
			    }
			};
			UserPreferenceServiceUtil.getInstance().getNonAssignedOntology(MainApp.userId, callbackpref);
			
			table.setWidget(0, 0, new HTML(""));
			table.setWidget(1,0,lstdata);
		    lstdata.setVisibleItemCount(20);
			lstdata.setSize("250px", "200px");

			table.getFlexCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
			table.getFlexCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_CENTER);
			
			// Popup element
			HorizontalPanel tableHP = new HorizontalPanel();
			tableHP.setSpacing(10);
			tableHP.add(table);
			userpanel.add(tableHP);
			HorizontalPanel hp = new HorizontalPanel();
			hp.add(btnAdd);				
			btnAdd.addClickHandler(new ClickHandler()
			{
				@SuppressWarnings("unchecked")
				public void onClick(ClickEvent event) {
					if(lstdata.getSelectedIndex()==-1)
					{
						Window.alert(constants.prefLanguageSelectOne());
						return;
					}
					ArrayList<UsersOntology> uoList = new ArrayList<UsersOntology>();
					final ArrayList<String[]> itemList = new ArrayList<String[]>();
					for(int i=0;i<lstdata.getItemCount();i++)
					{						
						if(lstdata.isItemSelected(i)){
							final int selindex = i;
							UsersOntologyId uoID = new UsersOntologyId();
							uoID.setOntologyId(Integer.parseInt(lstdata.getValue(i)));
							uoID.setUserId(MainApp.userId);
							
							UsersOntology uo = new UsersOntology();
							uo.setId(uoID);
							uo.setStatus(0);
							
							uoList.add(uo);
							
							String[] item = new String[2];
							item[0] = lstdata.getItemText(selindex);
							item[1] = lstdata.getValue(selindex);
							itemList.add(item);
							
						}
					}	
					
					hide();	
					AsyncCallback<Object> callbackpref = new AsyncCallback<Object>() {
					    public void onSuccess(Object result) {
					    	String ontology = "";
					    	for(String[] item: itemList)
					    	{
					    		ontologylistPending.addItem(item[0], item[1]);
					    		if(ontology.length()>0)
					    			ontology += ", "+ item[0];
					    		else
					    			ontology = item[0];
					    	}
					    	mailAlert(ontology);
							
				    	 }
					    public void onFailure(Throwable caught) {
					    	Window.alert(constants.userAddUserGroupFail());
					    }
					};
					
					
					
					UserPreferenceServiceUtil.getInstance().addUsersOntology(uoList, callbackpref);
				}
			});
			hp.add(btnCancel);
			hp.setSpacing(5);
			
			VerticalPanel hpVP = new VerticalPanel();			
			hpVP.setStyleName("bottombar");
			hpVP.setWidth("100%");
			hpVP.add(hp);
			hpVP.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_RIGHT);
			
			btnCancel.addClickHandler(new ClickHandler()
			{
				public void onClick(ClickEvent event) {
					hide();
				}
				
			});
			userpanel.add(hpVP);
			
			userpanel.setCellHorizontalAlignment(hp,HasHorizontalAlignment.ALIGN_CENTER);
			setWidget(userpanel);
		}
	}
	
	public void mailAlert(String ontology) {
		String to = user.getEmail();
		String subject = "Welcome to " + constants.mainPageTitle();
		String body = "";
		body += "Dear "+user.getFirstName()+" "+user.getLastName()+",";
		body += "\n\nThank you for your interest to access the following ontology. \n\n"
			    + "Requested ontology : " + ontology + "\n\n";
		body += constants.mainPageTitle() + " URL : " + GWT.getHostPageBaseURL() + "\n\n";
		body += "Version : "+ConfigConstants.DISPLAYVERSION+" \n\n";
		body += "Your request has been received. Please wait for the administrator to approve it. " 
				+ "You will be informed by email once the privilege to access the requested ontology is approved.\n\n" ;
		body += "\n\nRegards,";
		body += "\n\nThe " + constants.mainPageTitle() + " team.";

		AsyncCallback<Object> cbkmail = new AsyncCallback<Object>() {
			public void onSuccess(Object result) {
				GWT.log("Mail Send Successfully", null);
			}
			public void onFailure(Throwable caught) {
				GWT.log("Mail Send Failed", null);
			}
		};
		Service.systemService.SendMail(to, subject, body, cbkmail);

		to = "ADMIN";
		subject = constants.mainPageTitle() + ":Ontology Request";
		body = "Dear Admin, \n\n";
		body = "A new ontology access request for " + constants.mainPageTitle() + ".\n\n";
		body += constants.mainPageTitle() + " URL : " + GWT.getHostPageBaseURL() + "\n\n";
		body += "Version : "+ConfigConstants.DISPLAYVERSION+" \n\n";
		body += "Username : " + user.getUsername() + "\n\n";
		body += "First name : " + user.getFirstName() + "\n\n";
		body += "Last name : " + user.getLastName() + "\n\n";
		body += "Email : " + user.getEmail() + "\n\n";
		body += "Requested ontology : " + ontology + "\n\n";
		body += "Please assign privilege for the above requested ontologies to this user.\n";
		body += "\n\n Regards,";
		body += "\n\nThe " + constants.mainPageTitle() + " Team.";
		
		AsyncCallback<Object> cbkmail1 = new AsyncCallback<Object>() {
			public void onSuccess(Object result) {
				GWT.log("Mail Send Successfully", null);
			}

			public void onFailure(Throwable caught) {
				GWT.log("Mail Send Failed", null);
			}
		};
		Service.systemService.SendMail(to, subject, body, cbkmail1);
	}
	
} 