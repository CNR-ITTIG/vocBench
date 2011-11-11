package org.fao.aoscs.client.module.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.locale.LocaleMessages;
import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.DialogBoxAOS;
import org.fao.aoscs.client.widgetlib.shared.panel.BodyPanel;
import org.fao.aoscs.client.widgetlib.shared.panel.ButtonbarWidget;
import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;
import org.fao.aoscs.client.widgetlib.shared.panel.TitleBodyWidget;
import org.fao.aoscs.domain.UserLogin;
import org.fao.aoscs.domain.Users;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class UsersAssignment extends Composite implements ClickHandler, ChangeHandler {

	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private LocaleMessages messages = (LocaleMessages) GWT.create(LocaleMessages.class);

	private HorizontalPanel userInfoPanel = new HorizontalPanel();
	private VerticalPanel listInfoPanel = new VerticalPanel();

	private ListBox lstusers= new ListBox();

	private ListBox lstusergroups = new ListBox(true);
	private ListBox lstuserlangs = new ListBox(true);
	private ListBox lstuserontology = new ListBox(true);
	private VerticalPanel panel = new VerticalPanel();

	private Label lblusers = new Label();

	private Button btnsave = new Button(constants.buttonSave());
	private Button btncancel = new Button(constants.buttonCancel());

	private Image btnadduser = new Image("images/add-grey.gif");
	private Image btnedituser = new Image("images/edit-grey.gif");
	private Image btndeluser = new Image("images/delete-grey.gif");

	private Image btnaddlang = new Image("images/add-grey.gif");
	private Image btnremovelang = new Image("images/delete-grey.gif");

	private Image btnaddontology = new Image("images/add-grey.gif");
	private Image btnremoveontology = new Image("images/delete-grey.gif");

	private Image btnaddgroup = new Image("images/add-grey.gif");
	private Image btnremovegroup = new Image("images/delete-grey.gif");

	private TextBox txtusername = new TextBox();
	private TextBox txtfname = new TextBox();
	private TextBox txtlname = new TextBox();
	private TextBox txtemail = new TextBox();
	private TextBox txtaffiliation = new TextBox();
	private TextBox txtaddress = new TextBox();
	private TextBox txtpostcode  = new TextBox();
	private TextBox txturl  = new TextBox();
	private TextBox txtregisdate  = new TextBox();
	private PasswordTextBox txtpassword  = new PasswordTextBox();
	private PasswordTextBox txtcpassword  = new PasswordTextBox();

	private ListBox lstcountry  = new ListBox(); 
	private TextBox txtwphone  = new TextBox();
	private TextBox txtmphone  = new TextBox();
	private TextBox txtchat  = new TextBox();
	private ListBox lstchat = new ListBox();
	private TextArea txtcomment  = new TextArea();
	private TextBox txthstatus  = new TextBox();
	private CheckBox chkactive = new CheckBox(constants.userActivated());

	private CheckBox chksactive = new CheckBox(Convert.replaceSpace(constants.userActive()), true);
	private CheckBox chksinactive = new CheckBox(Convert.replaceSpace(constants.userInActive()), true);
	private CheckBox chksnew = new CheckBox(Convert.replaceSpace(constants.userNew()), true);

	public String loginname ; 
	private String dbActiveStatus = "0";

	private UserAssignDialogBox newUserDialog;

	public UsersAssignment() {					 
		// USER LIST
		lstusers.addChangeHandler(this);
		lstusers.setVisibleItemCount(28);

		chksactive.setWidth("100%");
		chksactive.setWordWrap(false);
		
		chksinactive.setWidth("100%");
		chksinactive.setWordWrap(false);
		
		chksnew.setWidth("100%");
		chksnew.setWordWrap(false);
		
		chksactive.addClickHandler(this);
		chksinactive.addClickHandler(this);
		chksnew.addClickHandler(this);
		chksactive.setValue(true);
		chksinactive.setValue(true);
		chksnew.setValue(true);

		HorizontalPanel showbox = new HorizontalPanel();		
		showbox.setSpacing(3);
		showbox.add(chksactive);
		showbox.add(chksinactive);
		showbox.add(chksnew);	

		// GROUP ASSIGNMENT			

		lstusergroups.addChangeHandler(this);
		//lstusergroups.setVisibleItemCount(7);

		btnaddgroup.setTitle(constants.buttonAdd());
		btnremovegroup.setTitle(constants.buttonRemove());
		btnaddgroup.addClickHandler(this);
		btnremovegroup.addClickHandler(this);

		HorizontalPanel hpnbtngroup = new HorizontalPanel();
		hpnbtngroup.add(btnaddgroup);
		hpnbtngroup.add(btnremovegroup);

		TitleBodyWidget wGroup = new TitleBodyWidget(constants.userGroup(), lstusergroups, hpnbtngroup, ((MainApp.getBodyPanelWidth()-80) * 0.15)  + "px", "100%");

		listInfoPanel.setWidth("100%");
		listInfoPanel.add(wGroup);
		listInfoPanel.add(new Spacer("100%", "11px"));						

		// LANGUAGE LIST			

		lstuserlangs.addChangeHandler(this);
		//lstuserlangs.setVisibleItemCount(7);

		HorizontalPanel hpnbtnlang = new HorizontalPanel();
		hpnbtnlang.add(btnaddlang);
		hpnbtnlang.add(btnremovelang);  

		btnaddlang.setTitle(constants.buttonAdd());
		btnremovelang.setTitle(constants.buttonRemove());
		btnaddlang.addClickHandler(this);
		btnremovelang.addClickHandler(this);

		TitleBodyWidget wLang = new TitleBodyWidget(constants.userLang(), lstuserlangs, hpnbtnlang, ((MainApp.getBodyPanelWidth()-80) * 0.15)  + "px", "100%");

		listInfoPanel.add(wLang);
		listInfoPanel.add(new Spacer("100%", "11px"));	

		// ONTOLOGY LIST			

		lstuserontology.addChangeHandler(this);
		//lstuserontology.setVisibleItemCount(7);

		HorizontalPanel hpnbtnontology = new HorizontalPanel();
		hpnbtnontology.add(btnaddontology);
		hpnbtnontology.add(btnremoveontology);  

		btnaddontology.setTitle(constants.buttonAdd());
		btnremoveontology.setTitle(constants.buttonRemove());
		btnaddontology.addClickHandler(this);
		btnremoveontology.addClickHandler(this);

		TitleBodyWidget wOntology = new TitleBodyWidget(constants.userOntology(), lstuserontology, hpnbtnontology, ((MainApp.getBodyPanelWidth()-80) * 0.15)  + "px", "100%");

		listInfoPanel.add(wOntology);

		// USER DETAILS 
		lstusers.addClickHandler(this);	      
		lstusergroups.addClickHandler(this);

		HorizontalPanel hp = new HorizontalPanel();
		HorizontalPanel space = new HorizontalPanel();
		space.setWidth("10px");						
		hp.add(txtchat);
		hp.add(space);
		hp.add(lstchat);
		hp.add(txthstatus);
		lstchat.addItem(constants.userChatTypes());
		lstchat.addItem(constants.userMSN());
		lstchat.addItem(constants.userYAHOO());
		lstchat.addItem(constants.userSKYPE());
		lstchat.addItem(constants.userGOOGLETALK());

		FlexTable fxpngrid = new FlexTable();
		fxpngrid.setWidth("100%");
		fxpngrid.getColumnFormatter().setWidth(0, "150px");
		fxpngrid.getColumnFormatter().setWidth(2, "210px");
		fxpngrid.setCellSpacing(1);
		fxpngrid.setCellPadding(1);							     

		fxpngrid.setWidget(0, 0, new HTML(constants.userName()+" <font color=red>*</font>"));
		fxpngrid.setWidget(0, 1,txtusername);
		fxpngrid.getFlexCellFormatter().setColSpan(0, 1, 3);


		fxpngrid.setWidget(1,0, new HTML(constants.userPassword()+" <font color=red>*</font>"));
		fxpngrid.setWidget(1,1,txtpassword);
		txtpassword.setWidth("100%");

		fxpngrid.setWidget(1,2, new HTML(constants.userConfirmPassword()+" <font color=red>*</font>")); 	
		fxpngrid.setWidget(1,3,txtcpassword);
		txtcpassword.setWidth("100%");

		fxpngrid.setWidget(2,0, new HTML(constants.userFirstName()+" <font color=red>*</font>"));	
		fxpngrid.setWidget(2,1,txtfname);

		fxpngrid.setWidget(2,2, new HTML(constants.userLastName()+"  <font color=red>*</font>"));	
		fxpngrid.setWidget(2,3,txtlname);

		fxpngrid.setWidget(3,0, new HTML(constants.userEmail()+" <font color=red>*</font>"));		
		fxpngrid.setWidget(3,1,txtemail);  

		fxpngrid.setWidget(3,2, new HTML(constants.userAffiliation()+" <font color=red>*</font>"));	
		fxpngrid.setWidget(3,3,txtaffiliation);

		fxpngrid.setWidget(4,0, new HTML(constants.userAddress()+"  <font color=red>*</font>")); 
		fxpngrid.setWidget(4,1, txtaddress);  

		fxpngrid.setWidget(4,2, new HTML(constants.userPostalCode()));	
		fxpngrid.setWidget(4,3, txtpostcode);

		fxpngrid.setWidget(5, 0, new HTML(constants.userCountry()+" <font color=red>*</font>"));			
		fxpngrid.setWidget(5, 1, lstcountry);
		fxpngrid.getFlexCellFormatter().setColSpan(5, 1, 3);

		fxpngrid.setWidget(6,0, new HTML(constants.userURL()));	
		fxpngrid.setWidget(6,1,txturl);
		fxpngrid.setWidget(6,2, new HTML(constants.userRegistratonDate()));
		fxpngrid.setWidget(6,3,txtregisdate);

		fxpngrid.setWidget(7,0, new HTML(constants.userWorkPhone()));	
		fxpngrid.setWidget(7,1,txtwphone);

		fxpngrid.setWidget(7,2, new HTML(constants.userMobilePhone()));
		fxpngrid.setWidget(7,3,txtmphone); 

		fxpngrid.setWidget(8,0, new HTML(constants.userChat()));
		fxpngrid.setWidget(8,1,hp); 
		fxpngrid.getFlexCellFormatter().setColSpan(8, 1, 3);

		fxpngrid.setWidget(9,0, new HTML(constants.userComment()));
		fxpngrid.setWidget(9,1,txtcomment);			
		fxpngrid.getFlexCellFormatter().setColSpan(9, 1, 3);


		txthstatus.setVisible(false);
		HorizontalPanel fxtbutton=new HorizontalPanel();	      
		txtregisdate.setEnabled(false);   			 

		txtusername.setTabIndex(7);			
		txtpassword.setTabIndex(8);	  		
		txtcpassword.setTabIndex(9);	  		
		txtfname.setTabIndex(10);			
		txtlname.setTabIndex(11);			
		txtemail.setTabIndex(12);			
		txtaffiliation.setTabIndex(13);		
		txtaddress.setTabIndex(14);			
		txtpostcode.setTabIndex(15);		
		lstcountry.setTabIndex(16);
		txturl.setTabIndex(17);				
		txtregisdate.setTabIndex(18);	
		txtwphone.setTabIndex(19);			
		txtmphone.setTabIndex(20);
		txtchat.setTabIndex(21);			
		lstchat.setTabIndex(22);			
		txtcomment.setTabIndex(23);
		chkactive.setTabIndex(24);
		btnsave.setTabIndex(25);
		btncancel.setTabIndex(26);

		lstcountry.addItem("SELECT");
		sqlStr = "SELECT country_name,country_code FROM country_code ORDER BY country_name ";
		generateListData(lstcountry,sqlStr, constants.userListCountryFail());

		txtusername.setMaxLength(30);		
		txtfname.setMaxLength(50);
		txtlname.setMaxLength(50);			
		txtemail.setMaxLength(100);
		txtaffiliation.setMaxLength(50);	
		txtaddress.setMaxLength(255);
		txtpostcode.setMaxLength(80);		
		txturl.setMaxLength(200);
		txtwphone.setMaxLength(100);		
		txtmphone.setMaxLength(100);
		txtchat.setMaxLength(50);			
		txtcomment.setCharacterWidth(250);
		txtcomment.setWidth("250px");
		txtcomment.setVisibleLines(2);
		fxtbutton.add(btnadduser);
		fxtbutton.add(btnedituser);
		fxtbutton.add(btndeluser);	      
		fxtbutton.add(btnsave);	
		fxtbutton.add(btncancel);

		fxtbutton.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		fxtbutton.setSpacing(5);
		btnadduser.setTitle(constants.buttonAdd());
		btnedituser.setTitle(constants.buttonEdit());
		btndeluser.setTitle(constants.buttonDelete());
		btnadduser.addClickHandler(this);
		btnedituser.addClickHandler(this);
		btndeluser.addClickHandler(this);

		btnsave.addClickHandler(this);
		btncancel.addClickHandler(this);

		HorizontalPanel hpnbtnlangcontainer = new HorizontalPanel();
		hpnbtnlangcontainer.setWidth("100%");
		hpnbtnlangcontainer.add(btnsave);
		hpnbtnlangcontainer.add(btncancel);

		VerticalPanel activateOption = new VerticalPanel();
		activateOption.add(chkactive);			

		ButtonbarWidget buttonBarPanel = new ButtonbarWidget(activateOption, hpnbtnlangcontainer);

		final VerticalPanel vpanel = new VerticalPanel();	
		vpanel.setWidth("100%");
		vpanel.add(GridStyle.setTableRowStyle(fxpngrid, "#F4F4F4", "#E8E8E8", 3));
		vpanel.add(new Spacer("100%", "1px"));	
		vpanel.setCellHorizontalAlignment(fxpngrid, HasHorizontalAlignment.ALIGN_CENTER);
		vpanel.add(buttonBarPanel);
		vpanel.setCellWidth(buttonBarPanel, "100%");

		TitleBodyWidget wUserDetail = new TitleBodyWidget(constants.userDetails(), vpanel, fxtbutton,  "100%", "100%");

		VerticalPanel userDetailPanel = new VerticalPanel();
		userDetailPanel.setWidth("100%");
		userDetailPanel.add(wUserDetail);

		TitleBodyWidget wUserList = new TitleBodyWidget(constants.userUsers(), lstusers, showbox, ((MainApp.getBodyPanelWidth()-80) * 0.30)  +"px", "100%");

		userInfoPanel.add(wUserList);					
		userInfoPanel.add(userDetailPanel);	
		userInfoPanel.add(listInfoPanel);
		userInfoPanel.setSpacing(10);
		userInfoPanel.setCellWidth(userDetailPanel, "100%");
		userInfoPanel.setCellVerticalAlignment(listInfoPanel, HasVerticalAlignment.ALIGN_TOP);

		Scheduler.get().scheduleDeferred(new Command() {
            public void execute()
            {  
            	lstusers.setHeight(vpanel.getOffsetHeight()+"px");
            	int height = (vpanel.getOffsetHeight()/3)-28;
            	lstusergroups.setHeight(height+"px");
            	lstuserlangs.setHeight(height+"px");
        		lstuserontology.setHeight(height+"px");
            }
        });	
		
		
		BodyPanel mainPanel = new BodyPanel(constants.userManagement() , userInfoPanel , null);
		panel.clear();
		panel.add(mainPanel);	      
		panel.setCellHorizontalAlignment(mainPanel,  HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(mainPanel,  HasVerticalAlignment.ALIGN_TOP);

		initWidget(panel);
		enableobject(false);  
		initUserList();	 
	}

	String sqlStr ;
	// Existing users
	private boolean isComplete;
	
	public boolean validateSubmit(){
		//final boolean isComplete;
		isComplete =true;
		if(txtusername.getText().equals("")){
			Window.alert(constants.userNoLogin());
			txtusername.setFocus(true);
			isComplete=false;
		}else if(txtfname.getText().equals("")){
			Window.alert(constants.userNoFirstName());
			txtfname.setFocus(true);
			isComplete=false;
		}else if(txtlname.getText().equals("")){
			Window.alert(constants.userNoLastName());
			txtlname.setFocus(true);
			isComplete=false;
		}else if(txtaffiliation.getText().equals("")){
			Window.alert(constants.userNoAffiliation());
			txtaffiliation.setFocus(true);
			isComplete=false;
		}else if (txtaddress.getText().equals("")){
			Window.alert(constants.userNoAddress());
			txtaddress.setFocus(true);
			isComplete=false;
		}else if (lstcountry.getSelectedIndex()==0){
			Window.alert(constants.userNoCountry());
			lstcountry.setFocus(true);
			isComplete=false;
		}else if(txtemail.getText().equals("")){
			Window.alert(constants.userNoEmail());
			txtemail.setFocus(true);
			isComplete=false;
		}else if ( (txtemail.getText() != "") && !(validateemail(txtemail.getText()))) {
			Window.alert(constants.userEmailMismatch());
			txtemail.setFocus(true);				
			isComplete = false;			
		} else if ( txtcomment.getText().length() > 255 ){			
			Window.alert(constants.userCommentMax());
			lstchat.setFocus(true);
			isComplete = false;
		} 

		if(txthstatus.getText().equals("ADD") || (!txtpassword.getText().equals(""))){
			if(txtpassword.getText().equals("")){
				Window.alert(constants.userNoPassword());
				txtpassword.setFocus(true);
				isComplete=false;
			}else if (txtpassword.getText().length()<6){
				Window.alert(constants.userPasswordMin());
				txtpassword.setFocus(true);
				isComplete=false;
			}else{
				if(!txtpassword.getText().equals(txtcpassword.getText())) {
					Window.alert(constants.userPasswordMismatch());
					txtpassword.setFocus(true);
					isComplete = false;
				}
			}
		}

		return isComplete;
	}

	private static native boolean validateemail(String txtemail) /*-{ 
	    var rex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9-.]+\.[a-zA-Z.]{2,5}$/ ; 
	    var ok = (null != rex.exec(txtemail)); 
	    return ok; 
	   }-*/;

	public void generateListData(final Widget sender,String querystr,final String errmsg){

		AsyncCallback<ArrayList<String[]>>cbklist = new AsyncCallback<ArrayList<String[]>>() {
			public void onSuccess(ArrayList<String[]> tmp) {

				int selindex = 0;

				if(((ListBox) sender).getItemCount() > 0 ){
					selindex = ((ListBox) sender).getSelectedIndex();  		
				}

				((ListBox) sender).clear();

				for(int i=0;i<tmp.size();i++){
					String[] item = (String[]) tmp.get(i); 
					String itemstring = "";
					if(item.length==3){				    			
						if(item[2].equals("0"))	itemstring="(n/a)";
						if(item[2].equals("1"))	itemstring="(a)";
						if(item[2].equals("2"))	itemstring="(new)";
					}				    	
					((ListBox) sender).addItem(item[0]+itemstring,item[1]);				    						    		
				}

				if(sender==lstusers){
					if(lstusers.getItemCount()>0)
					{
						if(tmp.size()>0 && selindex>=0 && selindex<lstusers.getItemCount()){
							lstusers.setItemSelected(selindex, true);
							initGroupList(lstusers.getValue(selindex));
							initUserLang(lstusers.getValue(selindex));
							initUserOntology(lstusers.getValue(selindex));
							showUserDetail(lstusers.getValue(selindex));
						}
						btnedituser.setVisible(true);	
						btndeluser.setVisible(true);
						btnaddlang.setVisible(true); 	
						btnremovelang.setVisible(true);
						btnaddontology.setVisible(true); 	
						btnremoveontology.setVisible(true);	
						btnaddgroup.setVisible(true); 	
						btnremovegroup.setVisible(true);
					}
					else
					{ // no data
						lstusergroups.clear();			
						lstuserlangs.clear();
						btnedituser.setVisible(false);	
						btndeluser.setVisible(false);
						txtusername.setText("") ;		
						txtpassword.setText("");		
						txtcpassword.setText("");
						txtfname.setText("") ;  		
						txtlname.setText("") ; 
						txtemail.setText("") ;		 	
						txtaffiliation.setText("") ; 
						txtaddress.setText("") ;  		
						txtpostcode.setText("") ; 
						txturl.setText("") ; 	 		
						txtregisdate.setText("");
						lstcountry.setSelectedIndex(0);
						txtwphone.setText("") ;  		
						txtmphone.setText("") ;  
						txtchat.setText("") ; 	 		
						lstchat.setSelectedIndex(0);
						txtcomment.setText("") ; 	
	
						chkactive.setValue(false);
						btnaddlang.setVisible(false); 	
						btnremovelang.setVisible(false);
						btnaddontology.setVisible(false); 	
						btnremoveontology.setVisible(false);	
						btnaddgroup.setVisible(false); 	
						btnremovegroup.setVisible(false);	
					}
				}
			}

			public void onFailure(Throwable caught) {
				Window.alert(errmsg);
			}


		};
		Service.queryService.execHibernateSQLQuery(querystr, cbklist);			
	}
	
	private void initUserList(){
		AsyncCallback<UserLogin> cbksession = new AsyncCallback<UserLogin>() {
			public void onSuccess(UserLogin userLoginObj) {
				if(userLoginObj!=null){
					loginname = userLoginObj.getUserid();
				}
				String criteria = "";
				if(chksactive.getValue()){		criteria = " WHERE status = '1'";	    }
				if(chksinactive.getValue()){	
					if(criteria.equals("")){
						criteria = " WHERE status = '0'";	    
					}else{
						criteria = criteria + " OR status = '0'";
					}
				}
				if(chksnew.getValue()){
					if(criteria.equals("")){
						criteria = " WHERE status = '2'";	    
					}else{
						criteria = criteria + " OR status = '2'";
					}	    
				}
				if(criteria.equals("")){ criteria = " WHERE status = 'x'"; }// clear value
				sqlStr = "SELECT username,user_id,status FROM users "+ criteria ; //WHERE username <> '" + loginname + "'";

				generateListData(lstusers, sqlStr, constants.userListUserFail());

			}
			public void onFailure(Throwable caught) {
				Window.alert(constants.userSessionExpired());
			}
		};

		Service.systemService.checkSession(MainApp.USERLOGINOBJECT_SESSIONNAME, cbksession); // Get userlogin from session
	}

	private void initGroupList(String userid) {
		lstusergroups.clear();

		sqlStr = "SELECT DISTINCT users_groups_name,users_groups.users_groups_id " +
		" FROM users_groups INNER JOIN users_groups_map " +
		"ON users_groups.users_groups_id = users_groups_map.users_group_id " +
		" WHERE users_groups_map.users_id= '"+userid +"'";
		generateListData(lstusergroups,sqlStr, constants.userListGroupFail());
	}

	private void initUserLang(String userid) {
		lstuserlangs.clear();
		sqlStr = "SELECT local_language,language_code.language_code " +
		"FROM language_code INNER JOIN users_language " +
		"ON language_code.language_code = users_language.language_code " +
		" WHERE users_language.user_id = '"+userid +"'";
		generateListData(lstuserlangs,sqlStr, constants.userListLangFail());
	}   	

	private void initUserOntology(String userid) {
		lstuserlangs.clear();

		String query = "";
		// if VISITOR then load read only ontology
		if(ConfigConstants.ISVISITOR){
			query = "version ='"+ ConfigConstants.VERSION +"' AND ontology_show='2'";
		}
		else{
			query = "version ='"+ ConfigConstants.VERSION +"' AND ontology_show='1'";
		}

		query += "AND ontology_id IN ( SELECT ontology_id FROM users_ontology WHERE user_id =  '"+userid +"' and status=1)"; 

		String sqlStr = "SELECT ontology_name, ontology_id FROM ontology_info " +
		"WHERE "+ query +" order by ontology_name";

		generateListData(lstuserontology, sqlStr, constants.userListOntologyFail());
	}   	

	public void showUserDetail(String userid){

		final AsyncCallback<ArrayList<String[]>> cbkdetail = new AsyncCallback<ArrayList<String[]>>() {
			public void onSuccess(ArrayList<String[]> tmp) {

				String[] item = (String[]) tmp.get(0);
				//-------- Slow process how to up speed
				txtusername.setText(item[0]) ; //(String) tmp.get(0)); //validateNull((String) list.next())); 
				txtpassword.setText(""); txtcpassword.setText("");
				txtfname.setText(item[1]) ;   
				txtlname.setText(item[2]) ; 
				txtemail.setText(item[3]) ; 
				txtaffiliation.setText(item[4]) ; 
				txtaddress.setText(item[5]) ; 
				txtpostcode.setText(item[6]) ; 
				txturl.setText(item[7]) ; 
				String regdate = (item[8]) ; 
				if(!regdate.equals("")){
					regdate = regdate.substring(8,11)+"/"+regdate.substring(5,7)+"/"+regdate.substring(0,4);
				}
				txtregisdate.setText(regdate);

				final String countrycode = item[9] ; //(String) tmp.get(11);
				int selectindex ; selectindex = 0;
				for(int i=0;i<lstcountry.getItemCount();i++){
					if(lstcountry.getValue(i).equals(countrycode)) selectindex = i;
				}

				if(selectindex != -1){ lstcountry.setSelectedIndex(selectindex); }
				txtwphone.setText(item[10]) ; //(String) tmp.get(12));
				txtmphone.setText(item[11]) ; //(String) tmp.get(13)); 
				//txtchat.setText(item[12]) ; //(String) tmp.get(14));
				String str = item[12];
				String lststr = "";
				if(str.indexOf("[")!=-1)
				{
					lststr = str.substring(str.indexOf("["));
					str = str.substring(0,str.indexOf("[")-1);
				}
				txtchat.setText(str);
				lstchat.setSelectedIndex(0);
				for(int i=0;i<lstchat.getItemCount();i++)
				{
					if(lststr.indexOf(lstchat.getItemText(i))!=-1)
					{
						lstchat.setSelectedIndex(i);
					}
				}
				txtcomment.setText(item[13]) ; //(String) tmp.get(15));	

				String chkstatus;
				chkstatus = (item[14]) ; //String) tmp.get(16);
				dbActiveStatus = chkstatus;
				if(chkstatus.equals("1"))
					chkactive.setValue(true);
				else
					chkactive.setValue(false);
			}

			public void onFailure(Throwable caught) {
				Window.alert(constants.userShowUserProfileFail());
			}
		};
		sqlStr = "SELECT username,first_name,last_name,email," +
		"affiliation,contact_address,postal_code,user_url,registration_date," +
		"country_code,work_phone,mobile_phone,chat_address,comment,status" +
		" FROM users WHERE user_id = '"+userid+"'";
		Service.queryService.execHibernateSQLQuery(sqlStr, cbkdetail);

	}

	public void onClick(ClickEvent event) {	
		Widget sender = (Widget) event.getSource();
		String sql;
		if (sender==btnaddlang){
			if(newUserDialog == null || !newUserDialog.isLoaded)
				newUserDialog = new UserAssignDialogBox(2,lstusers.getValue(lstusers.getSelectedIndex()));				
			newUserDialog.show();
		}else if(sender == btnremovelang){	
			if((Window.confirm(constants.userConfirmRemoveLang()))==false){
				return;
			}
			if(lstuserlangs.getSelectedIndex()==-1){
				Window.alert(constants.userNoLang());
				return;
			}
			AsyncCallback<Object> cbkdellang = new AsyncCallback<Object>() {
				public void onSuccess(Object result) {
					lstuserlangs.removeItem(lstuserlangs.getSelectedIndex());
					// create shell command for send mail to user in group 
				}

				public void onFailure(Throwable caught) {
					Window.alert(constants.userLangRemoveFail());
				}
			};
			String sql1 = "DELETE FROM users_language " +
			"WHERE language_code='"+lstuserlangs.getValue(lstuserlangs.getSelectedIndex())+"'" +
			"AND user_id ='"+lstusers.getValue(lstusers.getSelectedIndex())+"'";

			Service.queryService.hibernateExecuteSQLUpdate(sql1,cbkdellang);

		}else if (sender==btnaddontology){
			if(newUserDialog == null || !newUserDialog.isLoaded)
				newUserDialog = new UserAssignDialogBox(3,lstusers.getValue(lstusers.getSelectedIndex()));				
			newUserDialog.show();
		}else if(sender == btnremoveontology){	
			if((Window.confirm(constants.userConfirmRemoveOntology()))==false){
				return;
			}
			if(lstuserontology.getSelectedIndex()==-1){
				Window.alert(constants.userNoOntology());
				return;
			}
			AsyncCallback<Object> cbkdellang = new AsyncCallback<Object>() {
				public void onSuccess(Object result) {
					lstuserontology.removeItem(lstuserontology.getSelectedIndex());
				}

				public void onFailure(Throwable caught) {
					Window.alert(constants.userOntologyRemoveFail());
				}
			};
			String sql1 = "DELETE FROM users_ontology " +
			"WHERE ontology_id='"+lstuserontology.getValue(lstuserontology.getSelectedIndex())+"'" +
			"AND user_id ='"+lstusers.getValue(lstusers.getSelectedIndex())+"'";

			Service.queryService.hibernateExecuteSQLUpdate(sql1,cbkdellang);

		}else if(sender == btnaddgroup){
			if(newUserDialog == null || !newUserDialog.isLoaded)									
				newUserDialog = new UserAssignDialogBox(1,lstusers.getValue(lstusers.getSelectedIndex()));				
			newUserDialog.show();
		}else if(sender == btnremovegroup){
			if((Window.confirm(constants.userConfirmRemoveGroup()))==false){
				return;
			}
			if(lstusergroups.getSelectedIndex()==-1){
				Window.alert(constants.userNoGroup());
				return;
			}
			AsyncCallback<Object> cbkdelgroup = new AsyncCallback<Object>() {
				public void onSuccess(Object result) {
					lstusergroups.removeItem(lstusergroups.getSelectedIndex());
					// create shell command for send mail to user in group 
				}
				public void onFailure(Throwable caught) {
					Window.alert(constants.userGroupRemoveFail());
				}
			};
			sqlStr = "DELETE FROM users_groups_map " +
			"WHERE users_group_id='"+lstusergroups.getValue(lstusergroups.getSelectedIndex())+"'" +
			"AND users_id ='"+lstusers.getValue(lstusers.getSelectedIndex())+"'";
			Service.queryService.hibernateExecuteSQLUpdate(sqlStr,cbkdelgroup);

		}if(sender == btndeluser){
			String userName ;
			userName = lstusers.getItemText(lstusers.getSelectedIndex());
			if(Window.confirm(messages.userConfirmRemoveUser(userName))==true){
				// Check basic users that cannot delete (in list)
				String userid = lstusers.getValue(lstusers.getSelectedIndex());

				if(Integer.parseInt(userid) < 6){
					Window.alert(messages.userNoRemoveUser(userName));
					return;
				}
				AsyncCallback<Object> cbkdelusergroup = new AsyncCallback<Object>() {
					public void onSuccess(Object result) {
					}
					public void onFailure(Throwable caught) {
						Window.alert(constants.userRemoveUserGroupFail());
					}
				};
				sql = "DELETE FROM users_groups_map WHERE users_id='"+userid+"'";
				Service.queryService.hibernateExecuteSQLUpdate(sql,cbkdelusergroup);

				AsyncCallback<Object> cbkdeluserlang = new AsyncCallback<Object>() {
					public void onSuccess(Object result) {
					}

					public void onFailure(Throwable caught) {
						Window.alert(constants.userRemoveUserLangFail());
					}
				};
				String sql2 = "DELETE FROM users_language WHERE user_id='"+userid+"'";
				Service.queryService.hibernateExecuteSQLUpdate(sql2,cbkdeluserlang);

				AsyncCallback<Object> cbkdeluser = new AsyncCallback<Object>() {
					String dUserName = lstusers.getItemText(lstusers.getSelectedIndex());
					public void onSuccess(Object result) {
						Window.alert(messages.userRemoveUserSuccess(dUserName));
						lstusers.removeItem(lstusers.getSelectedIndex());
						if(lstusers.getItemCount()>0){							
							lstusers.setSelectedIndex(0);
							String getnum=lstusers.getValue(0);
							initGroupList(getnum);
							initUserLang(getnum);	
							initUserOntology(getnum);
							showUserDetail(getnum);
						}
					}

					public void onFailure(Throwable caught) {
						Window.alert(constants.userRemoveUserFail());
					}
				};
				String sql3 = "DELETE FROM users WHERE user_id='"+userid+"'";
				Service.queryService.hibernateExecuteSQLUpdate(sql3,cbkdeluser);
			}				
		}else if(sender == btnadduser){				
			if((Window.confirm(constants.userConfirmAddUser()))==true)
			{
				try 
				{
					enableobject(true);	txthstatus.setText("ADD");
					txtusername.setFocus(true);
					txtusername.setText("");	  
					txtfname.setText("");   		  
					txtlname.setText("");
					txtemail.setText("");		  
					txtaffiliation.setText("");	  
					txtaddress.setText("");
					txtpostcode.setText("");	  
					txturl.setText("");    		  
					txtregisdate.setText(DateTimeFormat.getFormat("dd-MM-yyyy").format(new Date()));
					lstcountry.setSelectedIndex(0); 
					txtwphone.setText("");	  	  
					txtmphone.setText("");   		  
					txtchat.setText("");  
					lstchat.setSelectedIndex(0);
					txtcomment.setText("");		  
					chkactive.setValue(true);

				} catch (Throwable e) 
				{
					e.printStackTrace();
				}						
			}				
		}else if(sender==btnedituser)
		{
			txthstatus.setText("EDIT");
			enableobject(true);
			txtusername.setEnabled(false);
		}
		else if(sender == btnsave)
		{
			if((Window.confirm(constants.userConfirmSaveData()))==false){ return ;}
			boolean isValid = validateSubmit();
			if(isValid==false){ return ;}							
			if(txthstatus.getText().equals("ADD")){
				AsyncCallback<Boolean> cbkuser = new AsyncCallback<Boolean>() {
					public void onSuccess(Boolean chk) {
						if(!chk){
							String activestatus = ""; 
							if(chkactive.getValue()) { activestatus = "1"; }else{ activestatus = "0"; }
							Users u = new Users();
							u.setUsername(txtusername.getText());
							u.setPassword(txtpassword.getText());
							u.setFirstName(txtfname.getText());
							u.setLastName(txtlname.getText());
							u.setEmail(txtemail.getText());
							u.setAffiliation(txtaffiliation.getText());
							u.setContactAddress(txtaddress.getText());
							u.setPostalCode(txtpostcode.getText());
							u.setUserUrl(txturl.getText());
							u.setCountryCode(lstcountry.getValue(lstcountry.getSelectedIndex()));
							u.setWorkPhone(txtwphone.getText());
							u.setMobilePhone(txtmphone.getText());
							u.setChatAddress(txtchat.getText()+" ["+lstchat.getValue(lstchat.getSelectedIndex())+"]");
							u.setComment(txtcomment.getText());
							u.setStatus(activestatus);
							u.setRegistrationDate(new Date());
							AsyncCallback<Integer> callback1 = new AsyncCallback<Integer>(){
								public void onSuccess(Integer result) {
									int val = (Integer) result;

									if(val == 0){
										Window.alert(constants.userExits());							
									}
									else if(val == -1)
										Window.alert(constants.registerUserError());
									else{
										txthstatus.setText("SAVE");
										enableobject(false);
										initUserList();
									}
								}
								public void onFailure(Throwable caught){
									Window.alert(constants.registerUserError());
								}
							};
							Service.systemService.addUser(u, callback1);

						}
						else{
							Window.alert(constants.userExits());					
							txtusername.setFocus(true);
						}
					}
					public void onFailure(Throwable caught) {
						Window.alert(constants.userAddFail());
					}
				};	
				Service.systemService.isUserExist(txtusername.getText(),cbkuser);			
			}
			else if(txthstatus.getText().equals("EDIT"))
			{
				String activestatus = ""; 

				if(chkactive.getValue()) { activestatus = "1"; }else{ activestatus = "0"; }

				Users u = new Users();
				u.setUserId(Integer.parseInt(lstusers.getValue(lstusers.getSelectedIndex())));
				u.setUsername(txtusername.getText());
				u.setFirstName(txtfname.getText());
				u.setLastName(txtlname.getText());
				u.setEmail(txtemail.getText());
				u.setAffiliation(txtaffiliation.getText());
				u.setContactAddress(txtaddress.getText());
				u.setPostalCode(txtpostcode.getText());
				u.setUserUrl(txturl.getText());
				u.setCountryCode(lstcountry.getValue(lstcountry.getSelectedIndex()));
				u.setWorkPhone(txtwphone.getText());
				u.setMobilePhone(txtmphone.getText());
				u.setChatAddress(txtchat.getText()+" ["+lstchat.getValue(lstchat.getSelectedIndex())+"]");
				u.setComment(txtcomment.getText());
				u.setPassword(txtpassword.getText());
				u.setStatus(activestatus);

				AsyncCallback<Object> cbkupdateuser = new AsyncCallback<Object>() {
					public void onSuccess(Object result) {	
						if(!dbActiveStatus.equals("1") && chkactive.getValue()){
							mailAlert(txtusername.getText(), txtfname.getText(),txtlname.getText(),txtemail.getText());
							Window.alert(constants.userActivateAdditionalInfo());
						}
						txthstatus.setText("SAVE");
						enableobject(false);
						initUserList();
					}
					public void onFailure(Throwable caught) {
						Window.alert(constants.userUpdateFail());
					}
				};
				Service.systemService.updateUserData(u,cbkupdateuser);
			}
		}
		else if(sender == btncancel)
		{
			String confirmstr = "";
			if(txthstatus.getText().equals("ADD")){
				confirmstr = constants.userCancelAddData();
			}else if(txthstatus.getText().equals("EDIT")){
				confirmstr = constants.userCancelEditData();
			}
			if((Window.confirm(confirmstr))==true){
				enableobject(false);
				if(lstusers.getItemCount()>0)
				{
					String getnum=lstusers.getValue(lstusers.getSelectedIndex());				
					if(getnum!="")
					{		
						showUserDetail(getnum);	
					}
				}
				else
				{
					btnedituser.setVisible(false);	
					btndeluser.setVisible(false);
					btnaddlang.setVisible(false); 	
					btnremovelang.setVisible(false);
					btnaddontology.setVisible(false); 	
					btnremoveontology.setVisible(false);
					btnaddgroup.setVisible(false); 	
					btnremovegroup.setVisible(false);
				}
			}	
		}
		else if(sender == chksactive)
		{		
			initUserList();
		}
		else if(sender == chksinactive)
		{
			initUserList();
		}
		else if(sender == chksnew)
		{
			initUserList();
		}
	}

	public void onChange(ChangeEvent event)
	{			
		Widget sender = (Widget) event.getSource(); 
		if(sender == lstusers){		
			String lbl=lstusers.getItemText(lstusers.getSelectedIndex());
			lblusers.setText(lbl);

			String getnum=lstusers.getValue(lstusers.getSelectedIndex());
			initGroupList(getnum);
			initUserLang(getnum);	
			initUserOntology(getnum);
			showUserDetail(getnum);
		}
	}

	public void enableobject(boolean status)
	{
		txtusername.setEnabled(status);  
		txtpassword.setEnabled(status);
		txtfname.setEnabled(status); 	   
		txtlname.setEnabled(status);
		txtemail.setEnabled(status);     
		txtaffiliation.setEnabled(status);
		txtaddress.setEnabled(status);  
		txtpostcode.setEnabled(status);
		txturl.setEnabled(status);  		 
		lstcountry.setEnabled(status);   		  
		txtwphone.setEnabled(status);
		txtmphone.setEnabled(status);    		  
		txtchat.setEnabled(status);  
		lstchat.setEnabled(status);
		txtcomment.setEnabled(status);   
		chkactive.setEnabled(status); 

		btnadduser.setVisible(!status);  		  
		btnedituser.setVisible(!status);
		btndeluser.setVisible(!status);
		btnsave.setEnabled(status);    		  
		btncancel.setEnabled(status);

		lstusers.setEnabled(!status);   		  
		lstusergroups.setEnabled(!status);
		lstuserlangs.setEnabled(!status);

		btnaddgroup.setVisible(!status); 		  
		btnremovegroup.setVisible(!status);
		btnaddlang.setVisible(!status);  		  
		btnremovelang.setVisible(!status);
		btnaddontology.setVisible(!status);  		  
		btnremoveontology.setVisible(!status);

		chksactive.setEnabled(!status);
		chksinactive.setEnabled(!status);
		chksnew.setEnabled(!status);
	}

	private class UserAssignDialogBox extends DialogBoxAOS implements ClickHandler{
		private VerticalPanel userpanel = new VerticalPanel();
		private Button btngroupadd = new Button(constants.buttonAdd());
		private Button btngroupcancel = new Button(constants.buttonCancel());
		private ListBox lstdata = new ListBox(true);
		private TextBox txthidden = new TextBox();

		public UserAssignDialogBox(final int typeid, String selecteduserid) {
			String titlestr ="";
			String sqlstr="" ;
			switch (typeid) {
			case 1 : // Add user group
			titlestr = constants.userSelectGroup();
			sqlstr = "SELECT distinct users_groups_name,users_groups_id " +
			"FROM users_groups  " +
			"WHERE users_groups_name != 'Non logged-in users' AND users_groups_name != 'Unassigned to any group' AND users_groups_id not in(" +
			"SELECT users_group_id FROM users_groups_map WHERE users_id= '"+selecteduserid+"')";
			break;
			case 2 : // Add user language
				titlestr = constants.userSelectLang();
				sqlstr = "SELECT language_note,language_code " +
				"FROM language_code " +
				"WHERE language_code NOT IN(" +
				"SELECT language_code FROM users_language WHERE user_id='"+selecteduserid+"')";
				break;
			case 3 : // Add user ontology
				titlestr = constants.userSelectOntology();

				// if VISITOR then load read only ontology
				String query = "";
				if(ConfigConstants.ISVISITOR){
					query = " version ='"+ ConfigConstants.VERSION +"' AND ontology_show='2'";
				}
				else{
					query = " version ='"+ ConfigConstants.VERSION +"' AND ontology_show='1'";
				}

				query += "AND ontology_id NOT IN(" +
				"SELECT ontology_id FROM users_ontology WHERE user_id='"+selecteduserid+"' AND status=1)"; 

				sqlstr = "SELECT ontology_name,ontology_id " +
				"FROM ontology_info WHERE "+query;
				break;
			}
			this.setText(titlestr);				
			final FlexTable table = new FlexTable();
			table.setBorderWidth(0);
			table.setCellPadding(0);
			table.setCellSpacing(1);
			table.setWidth("100%");

			AsyncCallback<ArrayList<String[]>> cbkuserlst = new AsyncCallback<ArrayList<String[]>>() {
				public void onSuccess(ArrayList<String[]> user) {
					for(int i=0;i<user.size();i++){
						String[] item = (String[]) user.get(i);
						lstdata.addItem(item[0], item[1]);					    		
					}
				}

				public void onFailure(Throwable caught) {
					if(typeid ==1)
						Window.alert(constants.userListGroupFail());
					else if(typeid ==2)
						Window.alert(constants.userListLangFail());
					else if(typeid ==3)
						Window.alert(constants.userListOntologyFail());
				}
			};
			Service.queryService.execHibernateSQLQuery(sqlstr, cbkuserlst);


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
			hp.add(btngroupadd);				
			btngroupadd.addClickHandler(this);
			hp.add(btngroupcancel);
			hp.setSpacing(5);

			VerticalPanel hpVP = new VerticalPanel();			
			hpVP.setStyleName("bottombar");
			hpVP.setWidth("100%");
			hpVP.add(hp);
			hpVP.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_RIGHT);

			btngroupcancel.addClickHandler(this);
			txthidden.setText(Integer.toString(typeid));
			txthidden.setVisible(false);
			userpanel.add(hpVP);
			userpanel.add(txthidden);

			userpanel.setCellHorizontalAlignment(hp,HasHorizontalAlignment.ALIGN_CENTER);
			setWidget(userpanel);
		}

		@SuppressWarnings("unchecked")
		public void onClick(ClickEvent event) {
			Widget sender = (Widget) event.getSource();
			if(sender.equals(btngroupcancel)){			
				this.hide();
			}else if(sender.equals(btngroupadd)){
				//---- Save process
				if(lstdata.getSelectedIndex()==-1){
					Window.alert(constants.userNoData());
					return;
				}
				String userId = lstusers.getValue(lstusers.getSelectedIndex());
				final HashMap<String,String> data = new HashMap<String,String>();
				for(int i=0;i<lstdata.getItemCount(); i++){
					if(lstdata.isItemSelected(i)){
						data.put(lstdata.getItemText(i), lstdata.getValue(i));
					}
				}
				
				final ArrayList<String> values = new ArrayList<String>(data.values());
				
				if(data.size()>0){
					switch (Integer.parseInt(txthidden.getText())){
					case 1: // groups
						AsyncCallback<Object> callbackAddGroups = new AsyncCallback<Object>(){
							public void onSuccess(Object result) {
								for(String item : data.keySet()){
									lstusergroups.addItem(item, data.get(item));	
								}
							}
							public void onFailure(Throwable caught) {
								Window.alert(constants.userAddUserGroupFail());
							}
						};
						Service.systemService.addGroupsToUser(userId, values, callbackAddGroups);
						break;
					case 2: // languages
						AsyncCallback<Object> callbackAddLanguages = new AsyncCallback<Object>(){
							public void onSuccess(Object result) {
								for(String item : data.keySet()){
									lstuserlangs.addItem(item, data.get(item));	
								}
							}
							public void onFailure(Throwable caught) {
								Window.alert(constants.userAddUserLanguageFail());
							}
						};
						Service.systemService.addLanguagesToUser(userId, values, callbackAddLanguages);						
						break;
					case 3: // ontologies
						AsyncCallback<Object> callbackAddOntologies = new AsyncCallback<Object>(){
							public void onSuccess(Object result) {
								for(String item : data.keySet()){
									lstuserontology.addItem(item, data.get(item));	
								}
							}
							public void onFailure(Throwable caught) {
								Window.alert(constants.userAddUserOntologyFail());
							}
						};
						Service.systemService.addOntologiesToUser(userId, values, callbackAddOntologies);
						break;
					}
				}
				
				this.hide();
				if(Integer.parseInt(txthidden.getText())==3){
					String item = "";
					for(int i=0;i<lstdata.getItemCount();i++){						
						if(lstdata.isItemSelected(i)){

							if(item.length()>0)
								item += ", "+lstdata.getItemText(i);
							else
								item = lstdata.getItemText(i);
						}
					}
					if(chkactive.getValue())
						mailAlert(item);
				}
			}
		} // onclick
	} //--- user Dialog box

	public void mailAlert(String userName, String fname,String lname, String pemail){
		String to = pemail;
		String subject = "Activation of your account on the "+constants.mainPageTitle()+"";
		String body = "";
		String groupnamelist = ""; 
		String Langnamelist = "";
		String ontologynamelist = "";
		int i;
		for(i=0;i<lstusergroups.getItemCount();i++){
			if(i>0)
				groupnamelist += ", "+lstusergroups.getItemText(i);
			else
				groupnamelist += lstusergroups.getItemText(i);
		}
		for(i=0;i<lstuserlangs.getItemCount();i++){
			if(i>0)
				Langnamelist += ", "+lstuserlangs.getItemText(i);
			else
				Langnamelist += lstuserlangs.getItemText(i);
		}
		for(i=0;i<lstuserontology.getItemCount();i++){
			if(i>0)
				ontologynamelist += ", "+lstuserontology.getItemText(i);
			else
				ontologynamelist += lstuserontology.getItemText(i);
		}

		body += 	"Dear "+fname+" "+lname+",";
		body += 	"\n\nYour account has been activated on the " +
		constants.mainPageTitle() + 
		"for the release \""+constants.mainPageTitle()+ " " + ConfigConstants.DISPLAYVERSION+"\" \n\n" +
		"Username: '"+txtusername.getText()+"'\n\n" +
		"You have been assigned the following user group permissions and languages:\n\n" +
		"User Groups: " +groupnamelist+
		"\n\nLanguages: " + Langnamelist+
		"\n\nOntology: " + ontologynamelist;
		body += 	"\n\nIf you have any problem on login in let us know.";
		body += 	"\n\nIf you have any suggestion, requirement, or you want to contribute on " +
		"revising it I will make you member of the project at " +
		"http://code.google.com/p/agrovoc-cs-workbench/ so that you can directly " +
		"post issues at http://code.google.com/p/agrovoc-cs-workbench/issues/list";
		body += 	"\n\nLet me know if you wish that. Emails are also fine.";
		body += 	"\n\nRegards,";
		body += "\n\nThe " + constants.mainPageTitle() + " Team.";

		AsyncCallback<Object> cbkmail = new AsyncCallback<Object>(){
			public void onSuccess(Object result) {
				GWT.log("Mail Send Successfully", null);
			}
			public void onFailure(Throwable caught) {
				GWT.log("Mail Send Failed", null);
			}
		};
		Service.systemService.SendMail(to, subject, body, cbkmail);

		to = "ADMIN";
		subject = constants.mainPageTitle() + ": User Activation";
		body = "The following user has been successfully activated for the "+constants.mainPageTitle()+".\n\n";
		body += constants.mainPageTitle() + " URL : " + GWT.getHostPageBaseURL() + "\n\n";
		body += "WB Version : "+ConfigConstants.DISPLAYVERSION+" \n\n";
		body += "Username : "+userName+" \n\n";
		body += "First Name : "+fname+" \n\n";
		body += "Last Name : "+lname+" \n\n";
		body += "Email : "+pemail+ "\n\n";
		body += "User Groups: " +groupnamelist+ "\n\n";
		body += "Languages: " + Langnamelist+ "\n\n";
		body += "Ontology: " + ontologynamelist+ "\n\n";
		body += 	"\n\n Regards,";
		body += "\n\nThe " + constants.mainPageTitle() + " Team.";


		AsyncCallback<Object> cbkmail1 = new AsyncCallback<Object>(){
			public void onSuccess(Object result) {
				GWT.log("Mail Send Successfully", null);
			}
			public void onFailure(Throwable caught) {
				GWT.log("Mail Send Failed", null);
			}
		};
		Service.systemService.SendMail(to, subject, body, cbkmail1);

	} 

	public void loadSelectedUser(String userID)
	{
		for(int i=0;i<lstusers.getItemCount();i++)
		{
			String user = lstusers.getValue(i);
			if(user.equals(userID))
			{
				lstusers.setItemSelected(i, true);
				lstusers.setSelectedIndex(i);
				String lbl=lstusers.getItemText(i);
				lblusers.setText(lbl);
			}
		}
		initGroupList(userID);
		initUserLang(userID);
		initUserOntology(userID);
		showUserDetail(userID);
	}

	public void loadUser(final String userID)
	{
		if(lstusers.getItemCount()>0)
		{
			loadSelectedUser(userID);
		}
		else
		{
			AsyncCallback<ArrayList<String>> cbksession = new AsyncCallback<ArrayList<String>>() {

				public void onSuccess(ArrayList<String> sessionprofile) {
					if(sessionprofile.size() != 0){
						loginname = (String) sessionprofile.get(0);
					}
					String criteria = "";
					if(chksactive.getValue()){		criteria = " WHERE status = '1'";	    }
					if(chksinactive.getValue()){	
						if(criteria.equals("")){
							criteria = " WHERE status = '0'";	    
						}else{
							criteria = criteria + " OR status = '0'";
						}
					}
					if(chksnew.getValue()){
						if(criteria.equals("")){
							criteria = " WHERE status = '2'";	    
						}else{
							criteria = criteria + " OR status = '2'";
						}	    
					}
					if(criteria.equals("")){ criteria = " WHERE status = 'x'"; }// clear value
					sqlStr = "SELECT username,user_id,status FROM users "+ criteria ; //WHERE username <> '" + loginname + "'";
					lstusers.clear();
					AsyncCallback<ArrayList<String[]>> cbklist = new AsyncCallback<ArrayList<String[]>>() {
						public void onSuccess(ArrayList<String[]> tmp) {
							for(int i=0;i<tmp.size();i++){
								String[] item = (String[]) tmp.get(i); String itemstring = "";
								if(item.length==3){				    			
									if(item[2].equals("0"))	itemstring="(n/a)";
									if(item[2].equals("1"))	itemstring="(a)";
									if(item[2].equals("2"))	itemstring="(new)";
								}

								((ListBox) lstusers).addItem(item[0]+itemstring,item[1]);
							}
							loadSelectedUser(userID);
						}

						public void onFailure(Throwable caught) {
							Window.alert(constants.userListUserFail());
						}
					};
					Service.queryService.execHibernateSQLQuery(sqlStr, cbklist);

				}
				public void onFailure(Throwable caught) {
					Window.alert(constants.userSessionExpired());
				}
			};

			Service.systemService.checkSession(MainApp.USERLOGINOBJECT_SESSIONNAME, cbksession); // Get userlogin from session
		}
	}

	public void mailAlert(String ontology){
		String to = txtemail.getText();
		String subject = "Approval of your access to the requested ontology on the "+constants.mainPageTitle()+"";
		String body = "";
		body += 	"Dear "+txtfname.getText()+" "+txtlname.getText()+",";
		body += 	"\n\nYour request to access following ontology has been approved. " +
		"\n\nOntology: " +ontology;
		body +=   "\n\n"+constants.mainPageTitle() + " URL : " + GWT.getHostPageBaseURL() ;
		body +=   "\n\nVersion : "+ConfigConstants.DISPLAYVERSION;
		body += 	"\n\nIf you have any problem to access the ontology please let us know.";
		body += 	"\n\nRegards,";
		body += "\n\nThe " + constants.mainPageTitle() + " Team.";

		AsyncCallback<Object> cbkmail = new AsyncCallback<Object>(){
			public void onSuccess(Object result) {
				GWT.log("Mail Send Successfully", null);
			}
			public void onFailure(Throwable caught) {
				GWT.log("Mail Send Failed", null);
			}
		};
		Service.systemService.SendMail(to, subject, body, cbkmail);

		to = "ADMIN";
		subject = constants.mainPageTitle() + ": Ontology Approval";
		body = "Dear Admin, \n\n";
		body += "The following ontology has been successfully approved for the user in the "+constants.mainPageTitle()+".\n\n";
		body += constants.mainPageTitle() + " URL : " + GWT.getHostPageBaseURL() + "\n\n";
		body += "WB Version : "+ConfigConstants.DISPLAYVERSION+" \n\n";
		body += "Username : "+txtusername.getText()+" \n\n";
		body += "First Name : "+txtfname.getText()+" \n\n";
		body += "Last Name : "+ txtlname.getText()+" \n\n";
		body += "Email : "+ txtemail.getText() + "\n\n";
		body += "Ontology: " +ontology+ "\n";
		body += 	"\n\n Regards,";
		body += "\n\nThe " + constants.mainPageTitle() + " Team.";


		AsyncCallback<Object> cbkmail1 = new AsyncCallback<Object>(){
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

