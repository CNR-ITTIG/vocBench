package org.fao.aoscs.client.module.system;

import java.util.ArrayList;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.OWLActionConstants;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.DialogBoxAOS;
import org.fao.aoscs.client.widgetlib.shared.dialog.FlexDialogBox.FlexDialogClickedHandler;
import org.fao.aoscs.client.widgetlib.shared.label.ImageAOS;
import org.fao.aoscs.client.widgetlib.shared.panel.BodyPanel;
import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;
import org.fao.aoscs.client.widgetlib.shared.panel.TitleBodyWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class GroupsAssignment extends Composite implements ClickHandler, ChangeHandler {

	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);

	private ListBox lstusers= new ListBox(true);
	private ListBox lstgroups = new ListBox();
	private ListBox lstpermission = new ListBox();
	private VerticalPanel panel = new VerticalPanel();

	private Label lblgroupname = new Label();
	private Label lblgroupdesc = new Label();
	private ImageAOS btnaddgroup;
	private ImageAOS btneditgroup;
	private ImageAOS btnremovegroup;
	private ImageAOS btnadduser;
	private ImageAOS btnremoveuser;
	private Image btnaddpermit = new Image("images/add-grey.gif");
	private Image btnremovepermit = new Image("images/delete-grey.gif");
	private Image btnaddaction = new Image("images/add-grey.gif");
	private Image btnremoveaction = new Image("images/delete-grey.gif");
	private GroupActionWidget groupActions = new GroupActionWidget();

	private String sqlStr ; 

	private GroupDialogBox editGroupDialog; 
	private AddNewUserDialogBox newUserDialog; 
	private AddNewPermitDialogBox newPermitDialog; 
	private AddGroupActionsDialog newActionDialog; 

	public GroupsAssignment() {	
		String groupid="";
		String userid = "";

		initGroupList(groupid);
		initUserList(userid);

		//Widget allocation areas		  	 

		//Existing groups		  
		lstgroups.addChangeHandler(this);
		lstgroups.setVisibleItemCount(16);

		// Add new group
		final GroupDialogBox newGroupDialog = new GroupDialogBox("");
		btnaddgroup = new ImageAOS("", "images/add-grey.gif", "images/add-grey-disabled.gif", MainApp.permissionTable.contains(OWLActionConstants.GROUPCREATE, -1), new ClickHandler() 
		{
			public void onClick(ClickEvent event) {
				newGroupDialog.show();
			}
		});
		
		// Edit group
		btneditgroup = new ImageAOS("", "images/edit-grey.gif", "images/edit-grey-disabled.gif", MainApp.permissionTable.contains(OWLActionConstants.GROUPEDIT, -1), new ClickHandler()
		{
			public void onClick(ClickEvent event) {
				if(editGroupDialog == null || !editGroupDialog.isLoaded)
					editGroupDialog = new GroupDialogBox(lstgroups.getValue(lstgroups.getSelectedIndex()));
				editGroupDialog.show();
			}
		});
				
		// Remove group
		btnremovegroup = new ImageAOS("", "images/delete-grey.gif", "images/delete-grey-disabled.gif", MainApp.permissionTable.contains(OWLActionConstants.GROUPDELETE, -1), new ClickHandler()		
		{
			public void onClick(ClickEvent event)
			{
				if(Window.confirm(constants.groupConfirmRemoveUsers()))
				{	
					final String groupid =lstgroups.getValue(lstgroups.getSelectedIndex());					
					int a = new Integer(groupid).intValue();
					if(a<=7){
						Window.alert(constants.groupDeleteMain());
						return;
					}

					AsyncCallback<Object> cbkdelgroup = new AsyncCallback<Object>() 
					{										    
						public void onSuccess(Object result) 
						{
							lstgroups.removeItem(lstgroups.getSelectedIndex());							 
							lstusers.clear();
							lblgroupdesc.setText("");
							lstgroups.setItemSelected(0, true);
							initUserList(lstgroups.getValue(0)) ;
						}

						public void onFailure(Throwable caught) {
							Window.alert(constants.groupRemoveFail());
						}
					};

					Service.systemService.deleteGroup(Integer.parseInt(groupid), 
							lstgroups.getItemText(lstgroups.getSelectedIndex()), 
							lblgroupdesc.getText(), 
							MainApp.userId, cbkdelgroup);										  
				}
			}			
		});
		
		// Remove user
		btnremoveuser = new ImageAOS( "", "images/delete-grey.gif", "images/delete-grey-disabled.gif", MainApp.permissionTable.contains(OWLActionConstants.GROUPMEMBERDELETE, -1), new ClickHandler()
		{
			@SuppressWarnings("unchecked")
			public void onClick(ClickEvent event)
			{
				if(lstusers.getSelectedIndex()==-1){
					Window.alert(constants.groupNoUserSelect());
					return;	
				}
				// get selected actions
				ArrayList<Integer> selectedUsers = new ArrayList<Integer>();
				for(int i=0; i<lstusers.getItemCount(); i++){
					if( lstusers.isItemSelected(i) ){
						selectedUsers.add(Integer.parseInt(lstusers.getValue(i)));
					}
				}
				//String userid =lstusers.getValue(lstusers.getSelectedIndex()); 
				if(Window.confirm(constants.groupConfirmRemoveUser()))
				{	
					AsyncCallback<Object> cbkremoveuser = new AsyncCallback<Object>() 
					{
						public void onSuccess(Object result) {
							initUserList(lstgroups.getValue(lstgroups.getSelectedIndex()));
						}
						public void onFailure(Throwable caught) {
							Window.alert(constants.groupRemoveUserFail());
						}
					};
					Service.systemService.removeUserFromGroup(Integer.parseInt(lstgroups.getValue(lstgroups.getSelectedIndex())),
							selectedUsers,
							//Integer.parseInt(userid),
							lstgroups.getItemText(lstgroups.getSelectedIndex()),
							lstusers.getItemText(lstusers.getSelectedIndex()),
							MainApp.userId,
							cbkremoveuser);

				}	
			}				    			
		});

		btnadduser = new ImageAOS( "", "images/add-grey.gif", "images/add-grey-disabled.gif", MainApp.permissionTable.contains(OWLActionConstants.GROUPMEMBERADD, -1), new ClickHandler()
		{
			public void onClick(ClickEvent event) {
				if(newUserDialog == null || !newUserDialog.isLoaded)
					newUserDialog = new AddNewUserDialogBox(lstgroups.getValue(lstgroups.getSelectedIndex()));				
				newUserDialog.show();
			}
		});
		
		HorizontalPanel boxgroupbtn = new HorizontalPanel();
		boxgroupbtn.add(btnaddgroup);
		boxgroupbtn.add(btneditgroup);
		boxgroupbtn.add(btnremovegroup);
		btnaddgroup.setTitle(constants.buttonAdd());
		btneditgroup.setTitle(constants.buttonEdit());
		btnremovegroup.setTitle(constants.buttonDelete());

		//Groups permission
		lstpermission.addChangeHandler(this);
		lstpermission.setVisibleItemCount(16);

		HorizontalPanel boxpermitbtn = new HorizontalPanel();
		boxpermitbtn.setSpacing(3);		  
		boxpermitbtn.add(btnaddpermit);
		boxpermitbtn.add(btnremovepermit);
		btnaddpermit.setTitle(constants.buttonAdd());
		btnremovepermit.setTitle(constants.buttonRemove());

		//Existing users
		lstusers.addChangeHandler(this);
		lstusers.setVisibleItemCount(16);

		HorizontalPanel boxuserbtn = new HorizontalPanel();
		boxuserbtn.setSpacing(3);		  
		boxuserbtn.add(btnadduser);
		boxuserbtn.add(btnremoveuser);
		btnadduser.setTitle(constants.buttonAdd());
		btnremoveuser.setTitle(constants.buttonRemove());

		lstgroups.setEnabled(true);
		lstusers.addClickHandler(this);
		lstpermission.addClickHandler(this);
		lstgroups.addClickHandler(this);

		HorizontalPanel boxactionbtn = new HorizontalPanel();
		boxactionbtn.setSpacing(3);		  
		boxactionbtn.add(btnaddaction);
		boxactionbtn.add(btnremoveaction);
		btnaddaction.setTitle(constants.buttonAdd());
		btnremoveaction.setTitle(constants.buttonRemove());

		// Group Description
		lblgroupdesc.setHeight("70px");
		VerticalPanel groupDescPanel = new VerticalPanel();
		groupDescPanel.setSpacing(5);
		groupDescPanel.setSize("100%", "100%");
		groupDescPanel.add(lblgroupdesc);
		groupDescPanel.setCellHeight(lblgroupdesc, "100%");

		TitleBodyWidget descPanel = new TitleBodyWidget(constants.groupDescription(), groupDescPanel, null, MainApp.getBodyPanelWidth() - 45 + "px", "100%");

		HorizontalPanel groupPanel = new HorizontalPanel();  	
		groupPanel.setWidth("100%");
		groupPanel.add(new TitleBodyWidget(constants.groupExisting() , lstgroups , boxgroupbtn, ((MainApp.getBodyPanelWidth()-70)*0.20) + "px", "100%"));	
		groupPanel.add(new Spacer("5px","100%"));
		groupPanel.add(new TitleBodyWidget(constants.groupPermissions() , lstpermission , boxpermitbtn, ((MainApp.getBodyPanelWidth()-70)*0.20) + "px", "100%"));
		groupPanel.add(new Spacer("5px","100%"));
		groupPanel.add(new TitleBodyWidget(constants.groupMembers() , lstusers , boxuserbtn, ((MainApp.getBodyPanelWidth()-70)*0.20) + "px", "100%"));	  
		groupPanel.add(new Spacer("5px","100%"));
		groupPanel.add(new TitleBodyWidget(constants.groupActions() , groupActions , boxactionbtn, ((MainApp.getBodyPanelWidth()-70)*0.40) + "px", "100%"));

		VerticalPanel bodyPanel = new VerticalPanel();
		bodyPanel.setSize("100%", "100%");
		bodyPanel.setSpacing(10);
		bodyPanel.add(groupPanel);
		bodyPanel.add(descPanel);
		bodyPanel.setCellHorizontalAlignment(bodyPanel,  HasHorizontalAlignment.ALIGN_CENTER);
		bodyPanel.setCellHorizontalAlignment(descPanel,  HasHorizontalAlignment.ALIGN_CENTER);
		bodyPanel.setCellVerticalAlignment(bodyPanel,  HasVerticalAlignment.ALIGN_TOP);
		bodyPanel.setCellVerticalAlignment(descPanel,  HasVerticalAlignment.ALIGN_TOP);
		bodyPanel.setCellHeight(descPanel, "100%");		

		BodyPanel vpPanel = new BodyPanel(constants.groupManagment() , bodyPanel , null);
		panel.add(vpPanel);	      
		panel.setCellHorizontalAlignment(vpPanel,  HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(vpPanel,  HasVerticalAlignment.ALIGN_TOP);

		initWidget(panel);

		/*
		final GroupDialogBox newGroupDialog = new GroupDialogBox("");
		btnaddgroup.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				newGroupDialog.show();
			}
		});

		btneditgroup.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(editGroupDialog == null || !editGroupDialog.isLoaded)
					editGroupDialog = new GroupDialogBox(lstgroups.getValue(lstgroups.getSelectedIndex()));
				editGroupDialog.show();
			}
		});

		
		btnremovegroup.addClickHandler(new ClickHandler()		
		{
			public void onClick(ClickEvent event)
			{
				if(Window.confirm(constants.groupConfirmRemoveUsers()))
				{	
					final String groupid =lstgroups.getValue(lstgroups.getSelectedIndex());					
					int a = new Integer(groupid).intValue();
					if(a<=7){
						Window.alert(constants.groupDeleteMain());
						return;
					}

					AsyncCallback cbkdelgroup = new AsyncCallback() 
					{										    
						public void onSuccess(Object result) 
						{
							lstgroups.removeItem(lstgroups.getSelectedIndex());							 
							lstusers.clear();
							lblgroupdesc.setText("");
							lstgroups.setItemSelected(0, true);
							initUserList(lstgroups.getValue(0)) ;
						}

						public void onFailure(Throwable caught) {
							Window.alert(constants.groupRemoveFail());
						}
					};

					Service.systemService.deleteGroup(Integer.parseInt(groupid), 
							lstgroups.getItemText(lstgroups.getSelectedIndex()), 
							lblgroupdesc.getText(), 
							MainApp.userId, cbkdelgroup);										  
				}
			}			
		});
		
		btnremoveuser.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event)
			{
				if(lstusers.getSelectedIndex()==-1){
					Window.alert(constants.groupNoUserSelect());
					return;	
				}
				// get selected actions
				ArrayList<Integer> selectedUsers = new ArrayList<Integer>();
				for(int i=0; i<lstusers.getItemCount(); i++){
					if( lstusers.isItemSelected(i) ){
						selectedUsers.add(Integer.parseInt(lstusers.getValue(i)));
					}
				}
				//String userid =lstusers.getValue(lstusers.getSelectedIndex()); 
				if(Window.confirm(constants.groupConfirmRemoveUser()))
				{	
					AsyncCallback<Object> cbkremoveuser = new AsyncCallback<Object>() 
					{
						public void onSuccess(Object result) {
							initUserList(lstgroups.getValue(lstgroups.getSelectedIndex()));
						}
						public void onFailure(Throwable caught) {
							Window.alert(constants.groupRemoveUserFail());
						}
					};
					Service.systemService.removeUserFromGroup(Integer.parseInt(lstgroups.getValue(lstgroups.getSelectedIndex())),
							selectedUsers,
							//Integer.parseInt(userid),
							lstgroups.getItemText(lstgroups.getSelectedIndex()),
							lstusers.getItemText(lstusers.getSelectedIndex()),
							MainApp.userId,
							cbkremoveuser);

				}	
			}				    			
		});

		btnadduser.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(newUserDialog == null || !newUserDialog.isLoaded)
					newUserDialog = new AddNewUserDialogBox(lstgroups.getValue(lstgroups.getSelectedIndex()));				
				newUserDialog.show();
			}
		});
		*/
		//---------- permission listener ----
		btnremovepermit.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(lstpermission.getSelectedIndex()==-1){
					Window.alert(constants.groupNoPermitSelect());
					return;	
				}
				
				String permitid =lstpermission.getValue(lstpermission.getSelectedIndex()); 
				if(Window.confirm(constants.groupConfirmRemovePermission())){	
					AsyncCallback<Object> cbkremovepermit = new AsyncCallback<Object>() {
						public void onSuccess(Object result) {
							lstpermission.removeItem(lstpermission.getSelectedIndex());
						}
						public void onFailure(Throwable caught) {
							Window.alert(constants.groupRemovePermitFail());
						}
					};
					Service.systemService.removeGroupPermission(Integer.parseInt(lstgroups.getValue(lstgroups.getSelectedIndex())), 
							Integer.parseInt(permitid), 
							lstgroups.getItemText(lstgroups.getSelectedIndex()), 
							lstpermission.getItemText(lstpermission.getSelectedIndex()), 
							MainApp.userId, cbkremovepermit);
				}	
			}				    			
		});

		btnaddpermit.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(newUserDialog == null || !newUserDialog.isLoaded)					
					newPermitDialog = new AddNewPermitDialogBox(lstgroups.getValue(lstgroups.getSelectedIndex()));				
				newPermitDialog.show();
			}
		});		
		//-----------------------------------

		// Group Action listener --------
		btnremoveaction.addClickHandler(new ClickHandler()
		{
			@SuppressWarnings("unchecked")
			public void onClick(ClickEvent event)
			{	
				
				if(groupActions.actions.getSelectedIndex() == -1){
					Window.alert(constants.groupNoActionSelect());
					return;	
				}
				// get selected actions
				ArrayList<Integer> selectedActions = new ArrayList<Integer>();
				for(int i=0; i<groupActions.actions.getItemCount(); i++){
					if( groupActions.actions.isItemSelected(i) ){
						selectedActions.add(Integer.parseInt(groupActions.actions.getValue(i)));
					}
				}
					
				if(Window.confirm(constants.groupConfirmRemoveAction()))
				{	
					AsyncCallback<Object> cbkremoveuser = new AsyncCallback<Object>() 
					{
						public void onSuccess(Object result) {
							initActionList(lstgroups.getValue(lstgroups.getSelectedIndex()));
						}
						public void onFailure(Throwable caught) {
							Window.alert(constants.groupRemoveActionFail());
						}
					};
					Service.systemService.removeActionsFromGroup(Integer.parseInt(lstgroups.getValue(lstgroups.getSelectedIndex())), selectedActions, cbkremoveuser);
				
				}
				
			}				    			
		});

		btnaddaction.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				String actionid = groupActions.getSelectedAction();
				newActionDialog = new AddGroupActionsDialog(lstgroups.getValue(lstgroups.getSelectedIndex()), actionid);
				newActionDialog.addFlexDialogClickedHandler(new FlexDialogClickedHandler(){
					public void onFlexDialogCancelClicked(ClickEvent event) {
						
					}
					public void onFlexDialogLoopClicked(ClickEvent event) {
						
					}
					public void onFlexDialogSubmitClicked(ClickEvent event) {
						initActionList(lstgroups.getValue(lstgroups.getSelectedIndex()));
					}
				});
				newActionDialog.show();
			}
		});
		//-----------------------------------

	}	
	
	// Users		
	private void initUserList(String groupid) 	
	{
		AsyncCallback<ArrayList<String[]>> callback = new AsyncCallback<ArrayList<String[]>>(){
			//  @SuppressWarnings("unchecked")
			public void onSuccess(ArrayList<String[]> user) {
				lstusers.clear();
				for(int i=0;i<(user.size());i++){
					String[] item = (String[]) user.get(i);
					lstusers.addItem(item[0],item[1]);
				}
			}
			public void onFailure(Throwable caught) {
				Window.alert(constants.groupLoadUserListFail());
			}
		};

		if(groupid.equals("7")){
			sqlStr = "SELECT DISTINCT users.username, users.user_id FROM users " +
			"WHERE users.user_id NOT IN ( SELECT users_groups_map.users_id FROM users_groups_map) " +
			"ORDER BY users.user_id";				  
		}
		else{
			sqlStr = "SELECT username,users_id FROM users INNER JOIN users_groups_map " +
			"ON users.user_id = users_groups_map.users_id " +
			"WHERE users_groups_map.users_group_id= '"+groupid +"'";
		}	
		Service.queryService.execHibernateSQLQuery(sqlStr, callback);

		//--------- show group description
		lblgroupdesc.setText("");
		AsyncCallback<ArrayList<String[]>> cbkgroupdesc = new AsyncCallback<ArrayList<String[]>>() {
			public void onSuccess(ArrayList<String[]> groupdesc) 
			{
				if(!groupdesc.isEmpty())
				{
					String[] item = (String[]) groupdesc.get(0);
					lblgroupdesc.setText(item[0]);

				}
				else
				{
					lblgroupdesc.setText("");
				}
			}

			public void onFailure(Throwable caught) {
				Window.alert(constants.groupLoadDescprtionFail());
			}
		};
		sqlStr = "SELECT users_groups_desc" + //,users_groups_name,users_groups_id " +
		" FROM users_groups " +
		" WHERE users_groups_id = '"+groupid+"'";			
		if(groupid!=""){
			Service.queryService.execHibernateSQLQuery(sqlStr, cbkgroupdesc);
			//Service.systemService.queryResult(sqlStr,cbkgroupdesc);
		}
	}
	
	// Permits		
	private void initPermitList(String groupid) 	{
		AsyncCallback<ArrayList<String[]>> callback = new AsyncCallback<ArrayList<String[]>>(){
			public void onSuccess(ArrayList<String[]> permitlist) {
				lstpermission.clear();
				int i;
				for(i=0;i<permitlist.size();i++){
					String[] item = (String[]) permitlist.get(i);
					lstpermission.addItem(item[1],item[0]);
				}
			}

			public void onFailure(Throwable caught) {
				Window.alert(constants.groupLoadPermissionFail());
			}
		};
		sqlStr = "SELECT permissiontype.permission_id,permission " +
		"FROM permissiontype INNER JOIN permission_group_map " +
		"ON permissiontype.permission_id = permission_group_map.permission_id " +
		"WHERE permission_group_map.users_groups_id= '"+groupid +"'";
		if(!groupid.equals("7"))
			Service.queryService.execHibernateSQLQuery(sqlStr, callback);			 
	}

	// Groups
	private void initGroupList(String selectid) {
		// Existing groups
		AsyncCallback<ArrayList<String[]>> groupcallback = new AsyncCallback<ArrayList<String[]>>() 
		{
			public void onSuccess(ArrayList<String[]> tmp) 
			{
				int i;
				for(i=0;i<(tmp.size());i++){
					String[] item = (String[]) tmp.get(i);
					lstgroups.addItem(item[0],item[1]);
				}			    	
				lstgroups.setItemSelected(0, true);
				initUserList(lstgroups.getValue(0)) ;
				initPermitList(lstgroups.getValue(0)) ;
				initActionList(lstgroups.getValue(0)) ;
			}
			public void onFailure(Throwable caught) {
				Window.alert(constants.groupLoadGroupsFail());
			}
		};
		sqlStr = "SELECT users_groups_name,users_groups_id FROM users_groups";
		Service.queryService.execHibernateSQLQuery(sqlStr, groupcallback);						
	}

	// Actions
	private void initActionList(String selectid) {
		groupActions.initActionList(selectid);
	}

	public void onChange(ChangeEvent event)	{	
		Widget sender = (Widget) event.getSource(); 
		if(sender == lstgroups){
			lstusers.clear();
			String getnum=lstgroups.getValue(lstgroups.getSelectedIndex());
			if(lstgroups.getSelectedIndex() != -1){
				initUserList(getnum);
				initPermitList(getnum);
				initActionList(getnum);
			}
			lblgroupname.setText(" "+lstgroups.getItemText(lstgroups.getSelectedIndex()));
			if(lstgroups.getItemText(lstgroups.getSelectedIndex()).equals("Unassigned to any group")){
				btnadduser.setVisible(false);
				btnremoveuser.setVisible(false);
				btnaddpermit.setVisible(false);
				btnremovepermit.setVisible(false);
			}
			else{
				btnadduser.setVisible(true);
				btnremoveuser.setVisible(true);
				btnaddpermit.setVisible(true);
				btnremovepermit.setVisible(true);
			}

		}
	}

	/**
	 * Dialog Add New group	 
	 */
	private class GroupDialogBox extends DialogBoxAOS implements ClickHandler{
		private VerticalPanel panel = new VerticalPanel();
		private TextBox txtgroupname = new TextBox() ;
		private TextArea txtgroupdesc = new TextArea();
		private Button btnadd = new Button(constants.buttonAdd());
		private Button btncancel = new Button(constants.buttonCancel());
		private TextBox txthstatus = new TextBox();
		public GroupDialogBox(String groupid){	
			// init layout				
			final Grid table = new Grid(2,2);				
			table.setBorderWidth(0);				table.setCellPadding(0);
			table.setCellSpacing(1);				table. setWidth("400px");
			table.getCellFormatter().setWidth(0,0,"150px");
			txtgroupname.setSize("100%","20");		txtgroupdesc.setSize("100%","80");


			table.setWidget(0, 0, new HTML(constants.groupName()));				
			table.setWidget(1, 0, new HTML(constants.groupDescription()));				

			table.setWidget(0,1,txtgroupname);				
			table.setWidget(1,1, txtgroupdesc);

			VerticalPanel vp = new VerticalPanel();
			vp.add(txthstatus);
			vp.setVisible(false);

			// Popup element
			VerticalPanel form = new VerticalPanel();				
			form.setSpacing(10);
			form.add(GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1"));

			panel.add(form);
			panel.add(vp);

			HorizontalPanel buttonPanel = new HorizontalPanel(); 
			buttonPanel.setWidth("100%");
			buttonPanel.setStyleName("bottombar");

			HorizontalPanel hp = new HorizontalPanel();
			hp.setSpacing(3);
			hp.add(btnadd);		btnadd.addClickHandler(this);
			hp.add(btncancel);	btncancel.addClickHandler(this);

			buttonPanel.add(hp);
			buttonPanel.setCellHorizontalAlignment(hp,  HasHorizontalAlignment.ALIGN_RIGHT);

			panel.add(buttonPanel);
			//panel.setCellHorizontalAlignment(hp,HasHorizontalAlignment.ALIGN_CENTER);
			setWidget(panel);

			if(!groupid.equals("")){	
				this.setText(constants.groupEdit());
				txtgroupname.setText(lstgroups.getItemText(lstgroups.getSelectedIndex()));
				txtgroupdesc.setText(lblgroupdesc.getText());
				txthstatus.setText("EDIT");
				btnadd.setText(constants.buttonUpdate());
				lstgroups.setEnabled(false);
				txtgroupname.setEnabled(false);
			}else{ 		
				this.setText(constants.groupAdd());
				txtgroupname.setEnabled(true);
				txthstatus.setText("ADD"); 
				btnadd.setText(constants.buttonUpdate());
			}

		}

		public void onClick(ClickEvent event) {
			Widget sender = (Widget) event.getSource();
			if(sender.equals(btncancel)){			
				//	this.removeFromParent();
				lstgroups.setEnabled(true);
				this.hide();

			}else if(sender.equals(btnadd)){
				if(this.txtgroupname.getText()=="") {
					Window.alert(constants.groupNoGroupName());
					return;
				}
				//---- Save process
				AsyncCallback<Object> cbknewgroup = new AsyncCallback<Object>() {
					public void onSuccess(Object result) {
						lstgroups.clear();
						initPermitList("");
						lstpermission.clear();
						initGroupList("");
						lstusers.clear();
					}

					public void onFailure(Throwable caught) {
						if(txthstatus.getText().equals("ADD"))
						{
							Window.alert(constants.groupAddFail());					
						}
						else
						{
							Window.alert(constants.groupEditFail());
						}
					}
				};
				if(txthstatus.getText().equals("ADD"))
				{
					Service.systemService.createGroup(this.txtgroupname.getText(), this.txtgroupdesc.getText() , MainApp.userId, cbknewgroup);					
				}else{
					Service.systemService.editGroup(Integer.parseInt(lstgroups.getValue(lstgroups.getSelectedIndex())), 
							this.txtgroupname.getText(), 
							this.txtgroupdesc.getText() ,
							lblgroupdesc.getText(),
							MainApp.userId, cbknewgroup);						
				}

				lstgroups.setEnabled(true);
				this.hide();
			}
			this.txtgroupdesc.setText("");
			this.txtgroupname.setText("");
		}
	} //--- Dialog box
	
	/**
	 *  Dialog Add New User 
	 * 
	 */
	private class AddNewUserDialogBox extends DialogBoxAOS implements ClickHandler{
		private VerticalPanel userpanel = new VerticalPanel();
		private Button btnuseradd = new Button(constants.buttonAdd());
		private Button btnusercancel = new Button(constants.buttonCancel());
		private ListBox lstuser = new ListBox(true);

		public AddNewUserDialogBox(String selectedgroupid){	
			// init layout
			this.setText(constants.groupAddUser());
			userpanel.setSpacing(0);
			final FlexTable table = new FlexTable();
			table.setBorderWidth(0);
			table.setCellPadding(0);
			table.setCellSpacing(1);
			table. setWidth("300px");

			AsyncCallback<ArrayList<String[]>> cbkuserlst = new AsyncCallback<ArrayList<String[]>>() {
				public void onSuccess(ArrayList<String[]> user) {
					for(int i=0;i<(user.size());i++){
						String[] item = (String[]) user.get(i);					    		
						lstuser.addItem(item[0],item[1]);
					}
				}

				public void onFailure(Throwable caught) {
					Window.alert(constants.groupUserListFail());
				}
			};
			sqlStr = "SELECT username,user_id " +
			"FROM users  " +
			"WHERE user_id not in(" +
			"SELECT users_id FROM users_groups_map WHERE users_group_id= '"+selectedgroupid+"')";
			Service.queryService.execHibernateSQLQuery(sqlStr, cbkuserlst);


			table.setWidget(0,0,lstuser);
			lstuser.setVisibleItemCount(20);
			lstuser.setSize("100%", "200px");

			table.getFlexCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
			table.getFlexCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_CENTER);
			// Popup element

			VerticalPanel vp = new VerticalPanel();
			vp.setSpacing(10);
			vp.add(table);

			userpanel.add(vp);				

			/*HorizontalPanel hp = new HorizontalPanel();
hp.add(btnuseradd);
btnuseradd.addClickHandler(this);
hp.add(btnuserclose);
btnuserclose.addClickHandler(this);
userpanel.add(hp);
userpanel.setCellHorizontalAlignment(hp,HasHorizontalAlignment.ALIGN_CENTER);*/

			HorizontalPanel hp = new HorizontalPanel();
			hp.setSpacing(3);
			hp.add(btnuseradd);		btnuseradd.addClickHandler(this);
			hp.add(btnusercancel);	btnusercancel.addClickHandler(this);

			HorizontalPanel buttonPanel = new HorizontalPanel(); 
			buttonPanel.setWidth("100%");
			buttonPanel.setStyleName("bottombar");
			buttonPanel.add(hp);
			buttonPanel.setCellHorizontalAlignment(hp,  HasHorizontalAlignment.ALIGN_RIGHT);

			userpanel.add(buttonPanel);
			setWidget(userpanel);
		}

		public void onClick(ClickEvent event) {
			Widget sender = (Widget) event.getSource();
			if(sender.equals(btnusercancel)){			
				this.hide();
			}else if(sender.equals(btnuseradd)){
				//---- Save process
				if(lstuser.getSelectedIndex()==-1){
					Window.alert(constants.groupSelectUser());
					return;
				}
				int i;String groupid = lstgroups.getValue(lstgroups.getSelectedIndex());
				String userselectlistid = "";
				for(i=0;i<lstuser.getItemCount();i++)
				{						// use all user in one string for send						
					if(lstuser.isItemSelected(i))
					{
						if(userselectlistid.equals("")){
							userselectlistid = lstuser.getValue(i);								
						}else{
							userselectlistid = userselectlistid +","+lstuser.getValue(i);
						}
						AsyncCallback<Object> cbknewuser = new AsyncCallback<Object>() 
						{
							public void onSuccess(Object result) {
								initUserList(lstgroups.getValue(lstgroups.getSelectedIndex()));
							}
							public void onFailure(Throwable caught) {
								Window.alert(constants.groupAddUserFail());
							}
						};
						Service.systemService.addUserToGroup(Integer.parseInt(groupid), 
								Integer.parseInt(lstuser.getValue(i)),
								lstgroups.getItemText(lstgroups.getSelectedIndex()),
								lstuser.getItemText(i),
								MainApp.userId,
								cbknewuser); 

					}
				}
				// ---- Mail to user					
				AsyncCallback<ArrayList<String[]>> cbkmail = new AsyncCallback<ArrayList<String[]>>() {
					public void onSuccess(ArrayList<String[]> usermailarr) {
						for(int i=0;i<(usermailarr.size());i++){
							String[] item = (String[]) usermailarr.get(i);
							mailAlert(item[0],item[1]);
						}
					}

					public void onFailure(Throwable caught) {
						Window.alert(constants.groupGetUserMail());
					}
				};
				String sql1 = "SELECT first_name,email FROM users " +
				"WHERE user_id in("+userselectlistid+")";
				Service.queryService.execHibernateSQLQuery(sql1, cbkmail);

			}
			initUserList(lstgroups.getValue(lstgroups.getSelectedIndex()));

			this.hide();
		} 
	} //--- user Dialog box	
	
	/**
	 *  Dialog Add New Group Permission
	 * 
	 */
	private class AddNewPermitDialogBox extends DialogBoxAOS implements ClickHandler
	{
		private VerticalPanel permitpanel = new VerticalPanel();
		private Button btnpermitadd = new Button(constants.buttonAdd());
		private Button btnpermitclose = new Button(constants.buttonCancel());
		private ListBox lstpermit = new ListBox(true);

		public AddNewPermitDialogBox(String selectedgroupid){	
			// init layout
			this.setText(constants.groupAddPermit());
			permitpanel.setSpacing(0);
			final FlexTable table = new FlexTable();
			table.setBorderWidth(0);
			table.setCellPadding(0);
			table.setCellSpacing(1);
			table. setWidth("300px");

			AsyncCallback<ArrayList<String[]>> cbkpermitlst = new AsyncCallback<ArrayList<String[]>>() {
				public void onSuccess(ArrayList<String[]> permit) {
					for(int i=0;i<(permit.size());i++){
						lstpermit.addItem((String) permit.get(i)[0],(String) permit.get(i)[1]);
					}
				}
				public void onFailure(Throwable caught) {
					Window.alert(constants.groupPermissionListFail());
				}
			};
			sqlStr = "SELECT permission,permission_id " +
			"FROM permissiontype  " +
			"WHERE permission_id not in(" +
			"SELECT permission_id FROM permission_group_map " +
			" WHERE users_groups_id= '"+selectedgroupid+"')";
			Service.queryService.execHibernateSQLQuery(sqlStr,cbkpermitlst);			

			table.setWidget(0,0,lstpermit);
			lstpermit.setVisibleItemCount(20);
			lstpermit.setSize("100%", "200px");

			table.getFlexCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
			table.getFlexCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_CENTER);
			// Popup element		
			VerticalPanel vp = new VerticalPanel();
			vp.setSpacing(10);
			vp.add(table);

			permitpanel.add(vp);

			HorizontalPanel hp = new HorizontalPanel();
			hp.setSpacing(3);
			hp.add(btnpermitadd);		btnpermitadd.addClickHandler(this);
			hp.add(btnpermitclose);		btnpermitclose.addClickHandler(this);

			HorizontalPanel buttonPanel = new HorizontalPanel(); 
			buttonPanel.setWidth("100%");
			buttonPanel.setStyleName("bottombar");
			buttonPanel.add(hp);
			buttonPanel.setCellHorizontalAlignment(hp,  HasHorizontalAlignment.ALIGN_RIGHT);

			permitpanel.add(buttonPanel);								
			setWidget(permitpanel);
		}

		public void onClick(ClickEvent event)
		{
			Widget sender = (Widget) event.getSource();
			if(sender.equals(btnpermitclose))
			{			
				this.hide();
			}else if(sender.equals(btnpermitadd))
			{
				//---- Save process
				if(lstpermit.getSelectedIndex()==-1)
				{
					Window.alert(constants.groupSelectUser());
					return;
				}

				int i;String groupid = lstgroups.getValue(lstgroups.getSelectedIndex());
				String userselectlistid = "";
				for(i=0;i<lstpermit.getItemCount();i++)
				{								
					if(lstpermit.isItemSelected(i))
					{
						if(userselectlistid.equals("")){
							userselectlistid = lstpermit.getValue(i);								
						}else{
							userselectlistid = userselectlistid +","+lstpermit.getValue(i);
						}
						AsyncCallback<Object> cbknewuser = new AsyncCallback<Object>() {
							public void onSuccess(Object result) {
								//-- complete
								initPermitList(lstgroups.getValue(lstgroups.getSelectedIndex()));								
							}

							public void onFailure(Throwable caught) {
								Window.alert(constants.groupAddPermissionFail());
							}
						};
						Service.systemService.addGroupPermission(Integer.parseInt(groupid), 
								Integer.parseInt(lstpermit.getValue(i)) , 
								lstgroups.getItemText(lstgroups.getSelectedIndex()), 
								lstpermit.getItemText(i), 
								MainApp.userId, cbknewuser);
					}
				}
				this.hide();
			}
		}
	}
		
	public void mailAlert(String pname,String pemail){
		//	  String host = "mail.ku.ac.th";
		//	  String from = "FAO-AGRIS-CARIS@fao.org";
		String subject = "VocBench Group ";
		String body = "";
		body += "Dear "+ pname +",";
		body += "VocBench team add you to group " + lstgroups.getItemText(lstgroups.getSelectedIndex());
		body += "\n\n Do you accept this group? so, please confirm to accept or reject this group.";
		body += "\n\nThanks a lot for your interest.";
		body += "\n\nRegards,";
		body += "\nThe VocBench team.";
		AsyncCallback<Object> cbkmail = new AsyncCallback<Object>(){
			public void onSuccess(Object result) {
			}
			public void onFailure(Throwable caught) {
				Window.alert(constants.groupSendMailFail());
			}
		};
		Service.systemService.SendMail(pemail, subject, body, cbkmail);
	}
	
	public void onClick(ClickEvent event) {
	}

}