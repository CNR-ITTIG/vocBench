package org.fao.aoscs.client;

import java.util.ArrayList;
import java.util.Date;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.DialogBoxAOS;
import org.fao.aoscs.client.widgetlib.shared.panel.TitleBodyWidget;
import org.fao.aoscs.domain.Users;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
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

public class Register extends Composite implements ClickHandler {
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private VerticalPanel mainPanel = new VerticalPanel();
	private TextBox txtloginname = new TextBox();
	private PasswordTextBox txtpassword = new PasswordTextBox();
	private PasswordTextBox txtconfirmpass = new PasswordTextBox();
	private TextBox txtfname = new TextBox();
	private TextBox txtlname = new TextBox();
	private TextBox txtemail = new TextBox();
	private TextArea txtaddress = new TextArea();
	private TextBox txturl = new TextBox();
	private TextBox txtaffiliation = new TextBox();
	private ListBox lstcountry = new ListBox();
	private TextBox txtpostcode = new TextBox();
	private TextBox txtwphone = new TextBox();
	private TextBox txtmphone = new TextBox();
	private TextBox txtchat = new TextBox();
	private ListBox lstchat = new ListBox();
	private TextArea txtcomment = new TextArea();
	private Label charCount = new Label();

	private ListBox lstuserlangs = new ListBox();
	private ListBox lstuserOntology = new ListBox();
	private ListBox lstusergroups = new ListBox();
	private Image btnaddlang = new Image("images/add-grey.gif");
	private Image btnremovelang = new Image("images/delete-grey.gif");
	private Image btnaddOntology = new Image("images/add-grey.gif");
	private Image btnremoveOntology = new Image("images/delete-grey.gif");
	private Image btnaddgroup = new Image("images/add-grey.gif");
	private Image btnremovegroup = new Image("images/delete-grey.gif");

	private Button btnsave = new Button(constants.buttonSubmit());
	private Button btncancel = new Button(constants.buttonCancel());
	private VerticalPanel panel = new VerticalPanel();
	public String newuserdata;
	public String getyear;
	public String getmonth;
	public String getdate;
	public String countrycode;
	public String countryname;
	
	private UserAssignDialogBox newUserDialog; 

	public Register() 
	{
		lstcountry.addItem(constants.buttonSelect(), "");
		
		getCountryList();

		HTML html = new HTML(constants.registerUser());
		DOM.setStyleAttribute(html.getElement(), "fontFamily", "Arial");
		DOM.setStyleAttribute(html.getElement(), "fontSize", "12");
		
		// Header
		HorizontalPanel header = new HorizontalPanel();
		header.setStyleName("titleLabel");
		header.add(html);										
		
		// USER DETAIL
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(txtchat);
		hp.add(new HTML("&nbsp;&nbsp;"));
		hp.add(lstchat);
		lstchat.addItem(constants.registerChatTypes());
		lstchat.addItem(constants.registerMSN());
		lstchat.addItem(constants.registerYAHOO());
		lstchat.addItem(constants.registerSKYPE());
		lstchat.addItem(constants.registerGOOGLETALK());
		
		FlexTable ft = new FlexTable();
		ft.setWidget(0, 0, new HTML(constants.registerLoginName()+" <font color=red>*</font>"));
		ft.setWidget(0, 1, txtloginname);
		txtloginname.setWidth("100%");
		ft.getFlexCellFormatter().setColSpan(0, 1, 3);

		ft.setWidget(1, 0, new HTML(constants.registerPassword()+" <font color=red>*</font>"));
		ft.setWidget(1, 1, txtpassword);
		txtpassword.setWidth("100%");
		
		ft.setWidget(1, 2, new HTML(constants.registerConfirmPassword()+" <font color=red>*</font>"));
		ft.setWidget(1, 3, txtconfirmpass);		
		txtconfirmpass.setWidth("100%");

		ft.setWidget(2, 0, new HTML(constants.registerFirstName()+" <font color=red>*</font>"));
		ft.setWidget(2, 1, txtfname);
		txtfname.setWidth("100%");
		ft.setWidget(2, 2, new HTML(constants.registerLastName()+" <font color=red>*</font>"));
		ft.setWidget(2, 3, txtlname);
		txtlname.setWidth("100%");

		ft.setWidget(3, 0, new HTML(constants.registerEmail()+" <font color=red>*</font>"));
		ft.setWidget(3, 1, txtemail);
		txtemail.setWidth("100%");
		ft.setWidget(3, 2, new HTML(constants.registerAffiliation()+" <font color=red>*</font>"));
		ft.setWidget(3, 3, txtaffiliation);
		txtaffiliation.setWidth("100%");
				
		ft.setWidget(4, 0, new HTML(constants.registerContactAddress()+" <font color=red>*</font>"));
		ft.setWidget(4, 1, txtaddress);
		txtaddress.setWidth("100%");
		ft.setWidget(4, 2, new HTML(constants.registerPostalCode()));
		ft.setWidget(4, 3, txtpostcode);
		txtpostcode.setWidth("100%");

		ft.setWidget(5, 0, new HTML(constants.registerCountry()+" <font color=red>*</font>"));
		ft.setWidget(5, 1, lstcountry);
		ft.getFlexCellFormatter().setColSpan(5, 1, 3);
		
		ft.setWidget(6, 0, new HTML(constants.registerURL()));
		ft.setWidget(6, 1, txturl);
		txturl.setWidth("100%");
		ft.getFlexCellFormatter().setColSpan(6, 1, 3);
		
		ft.setWidget(7, 0, new HTML(constants.registerWorkPhone()));
		ft.setWidget(7, 1, txtwphone);
		txtwphone.setWidth("100%");
		
		ft.setWidget(7, 2, new HTML(constants.registerMobilePhone()));
		ft.setWidget(7, 3, txtmphone);
		txtmphone.setWidth("100%");
		
		ft.setWidget(8, 0, new HTML(constants.registerChat()));	
		ft.setWidget(8, 1, hp);
		ft.getFlexCellFormatter().setColSpan(8, 1, 3);
		
		
		txtloginname.setFocus(true);
		txtloginname.setMaxLength(30);
		txtfname.setMaxLength(50);
		txtlname.setMaxLength(50);
		txtemail.setMaxLength(100);
		txtaffiliation.setMaxLength(50);
		txtaddress.setCharacterWidth(100);
		txtaddress.setWidth("250px");
		txtpostcode.setMaxLength(80);		
		txturl.setMaxLength(200);
		txtwphone.setMaxLength(100);		
		txtmphone.setMaxLength(100);
		txtchat.setMaxLength(50);
		txtcomment.setCharacterWidth(250);
		txtcomment.setVisibleLines(4);
		
		txtloginname.setTabIndex(1);
		txtpassword.setTabIndex(2);
		txtconfirmpass.setTabIndex(3);
		txtfname.setTabIndex(4);
		txtlname.setTabIndex(5);
		txtemail.setTabIndex(6);
		txtaffiliation.setTabIndex(7);
		txtaddress.setTabIndex(8);
		txtpostcode.setTabIndex(9);
		lstcountry.setTabIndex(10);
		txturl.setTabIndex(11);
		txtwphone.setTabIndex(12);
		txtmphone.setTabIndex(13);
		txtchat.setTabIndex(14);
		lstchat.setTabIndex(15);
		


		// GROUPS 	
		lstusergroups.setVisibleItemCount(5);
		
		btnaddgroup.setTitle(constants.registerSelectGroup());
		btnremovegroup.setTitle(constants.buttonRemove());
		
		HorizontalPanel hpnbtngroup = new HorizontalPanel();
		hpnbtngroup.setSpacing(3);
		hpnbtngroup.add(btnaddgroup);
		hpnbtngroup.add(btnremovegroup);
		btnaddgroup.addClickHandler(this);
		btnremovegroup.addClickHandler(this);
				
		 TitleBodyWidget groupBox = new TitleBodyWidget(constants.registerGroups(), lstusergroups, hpnbtngroup, "230px", "100%");
		
		// LANGUAGES
		lstuserlangs.setVisibleItemCount(5);

		btnaddlang.setTitle(constants.registerSelectLanguage());
		btnremovelang.setTitle(constants.buttonRemove());

		HorizontalPanel hpnbtnlang = new HorizontalPanel();
		hpnbtnlang.setSpacing(3);
		hpnbtnlang.add(btnaddlang);
		hpnbtnlang.add(btnremovelang);
		btnaddlang.addClickHandler(this);
		btnremovelang.addClickHandler(this);
		
		 TitleBodyWidget langBox = new TitleBodyWidget(constants.registerLanguages(), lstuserlangs, hpnbtnlang, "230px", "100%");
		 
		// ONTOLOGY
		lstuserOntology.setVisibleItemCount(5);

		btnaddOntology.setTitle(constants.registerSelectOntology());
		btnremoveOntology.setTitle(constants.buttonRemove());

		HorizontalPanel hpnbtnOntology = new HorizontalPanel();
		hpnbtnOntology.setSpacing(3);
		hpnbtnOntology.add(btnaddOntology);
		hpnbtnOntology.add(btnremoveOntology);
		btnaddOntology.addClickHandler(this);
		btnremoveOntology.addClickHandler(this);
		
		TitleBodyWidget ontologyBox = new TitleBodyWidget(constants.registerOntology(), lstuserOntology, hpnbtnOntology, "230px", "100%");
				
		 
		// COMMENT TEXTAREA					
		DOM.setStyleAttribute(txtcomment.getElement(), "border", "0px");
		DOM.setStyleAttribute(txtcomment.getElement(), "overflow", "hidden");
		txtcomment.setVisibleLines(5);
		txtcomment.addKeyUpHandler(new KeyUpHandler(){
			public void onKeyUp(KeyUpEvent event) {
				Widget sender = (Widget) event.getSource();
				int val = 255 - ((TextArea) sender).getText().length();
				charCount.setText( val<=0 ? constants.registerMaxChar() : val + " "+constants.registerRemainingChar());				
			}
		});
			
		TitleBodyWidget cmtBox = new TitleBodyWidget(constants.registerComment(), txtcomment, null,  "710", "100%");
			 
		HorizontalPanel ftLstbox = new HorizontalPanel();		
		ftLstbox.setSpacing(5);					
		ftLstbox.add(groupBox);
		ftLstbox.add(langBox);
		ftLstbox.add(ontologyBox);
		ftLstbox.setCellHorizontalAlignment(groupBox , HasHorizontalAlignment.ALIGN_CENTER);
		ftLstbox.setCellHorizontalAlignment(langBox , HasHorizontalAlignment.ALIGN_CENTER);		
				
		
		 		
		VerticalPanel middleVP = new VerticalPanel();
		DOM.setElementAttribute(middleVP.getElement(), "backgroundColor", "#FFFFFF");
		middleVP.setSpacing(5);
		middleVP.add(GridStyle.setTableRowStyle(ft, "#F4F4F4", "#E8E8E8", 3));
		middleVP.add(ftLstbox);
		middleVP.add(cmtBox);
		middleVP.setCellHorizontalAlignment(cmtBox , HasHorizontalAlignment.ALIGN_CENTER);
		middleVP.setCellHorizontalAlignment(ftLstbox , HasHorizontalAlignment.ALIGN_CENTER);
		middleVP.setCellHorizontalAlignment(middleVP.getWidget(0) , HasHorizontalAlignment.ALIGN_CENTER);
		middleVP.setCellWidth(middleVP.getWidget(0), "100%");
		
		// SAVE and CANCEL
		HorizontalPanel submithp = new HorizontalPanel();
		submithp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		submithp.setSpacing(5);
		btnsave.setText(constants.buttonSave());
		submithp.add(btnsave);
		submithp.add(btncancel);
		btncancel.addClickHandler(this);
		btnsave.addClickHandler(this);
		
		HorizontalPanel buttonContainer = new HorizontalPanel();
		buttonContainer.setWidth("100%");
		buttonContainer.add(submithp);
		buttonContainer.setCellHorizontalAlignment(submithp, HasHorizontalAlignment.ALIGN_RIGHT);
		
		VerticalPanel content = new VerticalPanel();
		content.add(middleVP);
		DOM.setStyleAttribute(content.getElement(), "border", "1px solid #F59131");
		
		mainPanel.add(header);
		mainPanel.add(content);
		mainPanel.add(buttonContainer);
		mainPanel.setSpacing(5);
				
		panel.add(mainPanel);		
		panel.setSpacing(5);			
		panel.setCellHorizontalAlignment(mainPanel, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(mainPanel, HasVerticalAlignment.ALIGN_MIDDLE);	
		panel.setSize("100%", "100%");
		initWidget(panel);
	}

	public void getCountryList() {
		AsyncCallback<ArrayList<String[]>> cbkcountry = new AsyncCallback<ArrayList<String[]>>() {
			public void onSuccess(ArrayList<String[]> tmp) {
				if (tmp.size() > 0) {
					for (int i = 0; i < tmp.size(); i++) {
						String[] item = (String[]) tmp.get(i);
						((ListBox) lstcountry).addItem(item[0], item[1]);
					}
				}
				txtloginname.setFocus(true);
			}
			public void onFailure(Throwable caught) {
				Window.alert(constants.registerCountryListFail());
			}
		};
		Service.systemService.getCountryCodes(cbkcountry);
	}

	@SuppressWarnings("unchecked")
	public void onClick(ClickEvent event) {
		Widget sender = (Widget) event.getSource();
		if (sender == btnsave) {			
			boolean isComplete = validateSubmit();
			if (isComplete == false) {
				return;
			}			
			
		
			ArrayList<String> userGroups = new ArrayList<String>();			
			for (int i = 0; i < lstusergroups.getItemCount(); i++) 
			{
				userGroups.add(lstusergroups.getValue(i));												
			}
			
			ArrayList<String> userLangs = new ArrayList<String>(); 
			for (int i = 0; i < lstuserlangs.getItemCount(); i++) 
			{
				userLangs.add(lstuserlangs.getValue(i));																																	
			}
			
			ArrayList<String> userOntology = new ArrayList<String>(); 
			for (int i = 0; i < lstuserOntology.getItemCount(); i++) 
			{
				userOntology.add(lstuserOntology.getValue(i));																																	
			}
			
			Users u = new Users();
			u.setUsername(txtloginname.getText());
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
			u.setStatus("2");
			u.setRegistrationDate(new Date());
			
			AsyncCallback<Integer> callback1 = new AsyncCallback<Integer>(){
				public void onSuccess(Integer result) {
					int val = (Integer) result;
					
					if(val == 0){
						Window.alert(constants.registerUserExists());							
					}
					else if(val == -1)
						Window.alert(constants.registerUserError());
					else
					{
						mailAlert(txtfname.getText(), txtlname.getText(), txtemail.getText());
						try {
							Window.alert(constants.registerUserSuccess());
							Window.open(GWT.getHostPageBaseURL()+"index.html", "_self","schollbars=0,toolbar=0,resizable=1,status=no");
						} 
						catch (Throwable e) 
						{
							e.printStackTrace();
						}
					}
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.registerUserError());
				}
			};
			Service.systemService.registerUser(u , userGroups , userLangs , userOntology, callback1);
			
		} else if (sender.equals(btncancel)) {
			txtloginname.setText("");
			txtfname.setText("");
			txtlname.setText("");
			txtemail.setText("");
			txtaffiliation.setText("");
			txtaddress.setText("");
			txtpostcode.setText("");
			txturl.setText("");
			txtwphone.setText("");
			txtmphone.setText("");
			txtchat.setText(""); 
			lstchat.setSelectedIndex(0);
			txtcomment.setText("");
			lstcountry.setItemSelected(0, true);

			Main.gotoLoginScreen();
		} else if (sender == btnaddlang) {
			if(newUserDialog == null || !newUserDialog.isLoaded)
				newUserDialog = new UserAssignDialogBox(2);
			newUserDialog.show();
		} else if (sender == btnremovelang) {
			if ((Window.confirm(constants.registerRemoveLang())) == false) {
				return;
			}
			if (lstuserlangs.getSelectedIndex() == -1) {
				Window.alert(constants.registerNoLangSelect());
				return;
			}
			lstuserlangs.removeItem(lstuserlangs.getSelectedIndex());
		} else if (sender == btnaddOntology) {
			if(newUserDialog == null || !newUserDialog.isLoaded)
				newUserDialog = new UserAssignDialogBox(3);
			newUserDialog.show();
		} else if (sender == btnremoveOntology) {
			if ((Window.confirm(constants.registerRemoveOntology())) == false) {
				return;
			}
			if (lstuserOntology.getSelectedIndex() == -1) {
				Window.alert(constants.registerNoOntologySelect());
				return;
			}
			lstuserOntology.removeItem(lstuserOntology.getSelectedIndex());
		} else if (sender == btnaddgroup) {
			if(newUserDialog == null || !newUserDialog.isLoaded)
				newUserDialog = new UserAssignDialogBox(1);
			newUserDialog.show();
		} else if (sender == btnremovegroup) {
			if ((Window.confirm(constants.registerRemoveGroup())) == false) {
				return;
			}
			if (lstusergroups.getSelectedIndex() == -1) {
				Window.alert(constants.registerNoGroupSelect());
				return;
			}
			lstusergroups.removeItem(lstusergroups.getSelectedIndex());
		}
	}

	public boolean validateSubmit() {		
		boolean isComplete = true;
		if (txtloginname.getText().equals("")) {
			Window.alert(constants.registerNoLogin());
			txtloginname.setFocus(true);
			isComplete = false;
		} else if (txtpassword.getText().equals("")
				|| txtpassword.getText().compareTo(txtconfirmpass.getText()) != 0) {
			Window.alert(constants.registerPasswordMismatch());
			txtpassword.setFocus(true);
			isComplete = false;
		} else if (txtpassword.getText().length() < 6) {
			Window.alert(constants.registerPasswordMin());
			txtpassword.setFocus(true);
			isComplete = false;
		} else if (txtfname.getText().equals("")) {
			Window.alert(constants.registerNoFirstName());
			txtfname.setFocus(true);
			isComplete = false;
		} else if (txtlname.getText().equals("")) {
			Window.alert(constants.registerNoLastName());
			txtlname.setFocus(true);
			isComplete = false;
		} else if (txtaffiliation.getText().equals("")) {
			Window.alert(constants.registerNoAffiliation());
			txtaffiliation.setFocus(true);
			isComplete = false;
		} else if (txtaddress.getText().equals("")) {
			Window.alert(constants.registerNoContactAddress());
			txtaddress.setFocus(true);
			isComplete = false;
		} else if (lstcountry.getSelectedIndex() == 0) {
			Window.alert(constants.registerNoCountry());
			lstcountry.setFocus(true);
			isComplete = false;
		} else if (txtemail.getText().equals("")) {
			Window.alert(constants.registerNoEmail());
			txtemail.setFocus(true);
			isComplete = false;
		} else if ( (!txtemail.getText().equals("")) && !(validateemail(txtemail.getText()))) {
			Window.alert(constants.registerInvalidEmail());
			txtemail.setFocus(true);				
			isComplete = false;
		
		} else if ( txtcomment.getText().length() > 255 ){			
			Window.alert(constants.registerCommentsMax());
			lstchat.setFocus(true);
			isComplete = false;
		} 
				
		return isComplete;
	}

	private static native boolean validateemail(String txtemail) /*-{ 
	    var rex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9-.]+\.[a-zA-Z.]{2,5}$/ ; 
	    var ok = (null != rex.exec(txtemail)); 
	    return ok; 
	   }-*/;

	private class UserAssignDialogBox extends DialogBoxAOS implements ClickHandler {
		private VerticalPanel userpanel = new VerticalPanel();
		private Button btnDialogadd = new Button(constants.buttonAdd());
		private Button btnDialogcancel = new Button(constants.buttonCancel());
		private ListBox lstdata = new ListBox(true);
		private TextBox txthidden = new TextBox();

		public UserAssignDialogBox(final int typeid) {
			String titlestr = "";
			String sqlstr = "";
			switch (typeid) {
			case 1: // Add user group
				titlestr = constants.registerSelectGroup();
				String lstgroupid = "";
				for (int i = 0; i < lstusergroups.getItemCount(); i++) {
					if (lstgroupid.equals("")) {
						lstgroupid = "," + lstusergroups.getValue(i);
					} else {
						lstgroupid = lstgroupid + ","
								+ lstusergroups.getValue(i);
					}
				}
				sqlstr = "SELECT distinct users_groups_name,users_groups_id "
						+ "FROM users_groups  "
						+ "WHERE users_groups_id not in(1,6,7,12" + lstgroupid + ")";
				break;
			case 2: // Add user language
				titlestr = constants.registerSelectLanguage();
				String lstlangid = "";
				for (int i = 0; i < lstuserlangs.getItemCount(); i++) {
					if (lstlangid.equals("")) {
						lstlangid = "'" + lstuserlangs.getValue(i) + "'";
					} else {
						lstlangid = lstlangid + ",'" + lstuserlangs.getValue(i)
								+ "'";
					}
				}
				sqlstr = "SELECT language_note,language_code "
						+ "FROM language_code ";
				if (!lstlangid.equals("")) {
					sqlstr = sqlstr + "WHERE language_code NOT IN(" + lstlangid
							+ ")";
				}

				break;
			case 3: // Add user ontology
				titlestr = constants.registerSelectOntology();
				String lstontologyid = "";
				for (int i = 0; i < lstuserOntology.getItemCount(); i++) {
					if (lstontologyid.equals("")) {
						lstontologyid = "'" + lstuserOntology.getValue(i) + "'";
					} else {
						lstontologyid = lstontologyid + ",'" + lstuserOntology.getValue(i)
								+ "'";
					}
				}
				
				String query = "";
				// if VISITOR then load read only ontology
				if(ConfigConstants.ISVISITOR){
					query = " version ='"+ ConfigConstants.VERSION +"' AND ontology_show='2' ";
				}
				else{
					query = " version ='"+ ConfigConstants.VERSION +"' AND ontology_show='1' ";
				}
				if (!lstontologyid.equals("")) {
					query += " AND ontology_id NOT IN("+lstontologyid+ ")";
				}
				
				sqlstr = "SELECT ontology_name,ontology_id FROM ontology_info WHERE " + query;

				break;
			}
			this.setText(titlestr);

			AsyncCallback<ArrayList<String[]>> cbkuserlst = new AsyncCallback<ArrayList<String[]>>() {
				public void onSuccess(ArrayList<String[]> user) {
					for (int i = 0; i < user.size(); i++) {
						String[] item = (String[]) user.get(i);
						lstdata.addItem(item[0], item[1]);
					}
				}

				public void onFailure(Throwable caught) {
					
					if(typeid==1)
						Window.alert(constants.registerListGroupFail());
					else if(typeid==2)
						Window.alert(constants.registerListLangFail());
					else if(typeid==3)
						Window.alert(constants.registerListOntologyFail());
				}
			};			
			Service.queryService.execHibernateSQLQuery(sqlstr, cbkuserlst);
			
			final FlexTable table = new FlexTable();								
			table.setWidget(0, 0, lstdata);
			table.getFlexCellFormatter().setHorizontalAlignment(0, 0,HasHorizontalAlignment.ALIGN_CENTER);
			
			lstdata.setVisibleItemCount(20);
			lstdata.setSize("200px", "200px");
			
			VerticalPanel vp = new VerticalPanel();
			vp.setSpacing(10);
			vp.add(table);
			
			// Popup element
			userpanel.add(vp);
			HorizontalPanel hp = new HorizontalPanel();
			hp.setSpacing(3);
			hp.add(btnDialogadd);
			btnDialogadd.addClickHandler(this);
			hp.add(btnDialogcancel);
			btnDialogcancel.addClickHandler(this);
			
			HorizontalPanel buttonPanel = new HorizontalPanel(); 
			buttonPanel.setWidth("100%");
			buttonPanel.setStyleName("bottombar");
			buttonPanel.add(hp);
			buttonPanel.setCellHorizontalAlignment(hp,  HasHorizontalAlignment.ALIGN_RIGHT);
			
			txthidden.setText(Integer.toString(typeid));
			txthidden.setVisible(false);
			userpanel.add(buttonPanel);
			userpanel.add(txthidden);

			userpanel.setCellHorizontalAlignment(hp,HasHorizontalAlignment.ALIGN_CENTER);
			setWidget(userpanel);
		}

		public void onClick(ClickEvent event) {
			Widget sender = (Widget) event.getSource();
			if (sender.equals(btnDialogcancel)) {
				this.hide();
			} else if (sender.equals(btnDialogadd)) {
				// ---- Save process
				if (lstdata.getSelectedIndex() == -1) {
					Window.alert(constants.registerNoData());
					return;
				}
				int i;

				for (i = 0; i < lstdata.getItemCount(); i++) {
					if (lstdata.isItemSelected(i)) {
						int selindex = i;
						switch (Integer.parseInt(txthidden.getText())) {
						case 1:
							lstusergroups.addItem(
									lstdata.getItemText(selindex), lstdata
											.getValue(selindex));
							break;
						case 2:
							lstuserlangs.addItem(lstdata.getItemText(selindex),
									lstdata.getValue(selindex));
							break;
						case 3:
							lstuserOntology.addItem(lstdata.getItemText(selindex),
									lstdata.getValue(selindex));
							break;
							
						}
					}// if
				}// for
				this.hide();
			}
		} // onclick
	} // --- user Dialog box

	public void mailAlert(String fname, String lname, String pemail) {
		String to = pemail;
		String subject = "Welcome to " + constants.mainPageTitle();
		String body = "";
		body += "Dear " + fname + " "+lname+",";
		body += "\n\nThank you for registering as a user of the " + constants.mainPageTitle() + "." 
				+ "Your request has been received. Please wait for the administrator to approve it. " 
				+ "You will be informed when you can start to login in the system. " 
				+ "After approval, you can log in to the " + constants.mainPageTitle() + " now with the username '"
				+ txtloginname.getText() + "' and your chosen password."
				+ "\n\nThanks for your interest."
				+ "\n\nIf you want to unregister, please send an email with your username and the "
				+ "subject: " + constants.mainPageTitle() + " - Unregister to "
				+ "FAO-AGRIS-CARIS@fao.org. \n\n" 
				+ constants.mainPageTitle() + " URL : " + GWT.getHostPageBaseURL() + "\n\n";;
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
		subject = constants.mainPageTitle() + ":User Request";
		body = "A new user registration request for " + constants.mainPageTitle() + ".\n\n";
		body += constants.mainPageTitle() + " URL : " + GWT.getHostPageBaseURL() + "\n\n";
		body += "Version : "+ConfigConstants.DISPLAYVERSION+" \n\n";
		body += "Username : " + txtloginname.getText() + "\n\n";
		body += "First Name : " + txtfname.getText() + "\n\n";
		body += "Last Name : " + txtlname.getText() + "\n\n";
		body += "Email : " + txtemail.getText() + "\n\n";
		body += "Please assign languages, ontology, user groups and activate the account.\n";
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

