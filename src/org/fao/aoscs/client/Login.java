package org.fao.aoscs.client;

import java.util.ArrayList;
import java.util.Date;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.domain.UserLogin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Cookies;
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
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Login extends Composite {
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private PasswordTextBox password = new PasswordTextBox();
	private CheckBox chkRemember = new CheckBox();
	private CheckBox chkAnonymous = new CheckBox();
	private SuggestBox loginname = new SuggestBox();
	private Button signin = new Button();
	private VerticalPanel panel = new VerticalPanel();
	private HorizontalPanel hp ;
	private FlexTable loginForm = new FlexTable();
	private SelectPreferenceDlg selectGroupDlg; 
	
	/**
	 * The timeout before a cookie expires, in milliseconds. Current one week.
	 */
	private static final int COOKIE_TIMEOUT = 1000 * 60 * 60 * 24 * 7;
	
	public Login() {

		MultiWordSuggestOracle userList = new MultiWordSuggestOracle();
		userList.addAll(getLoginCookieList()); 
		
		loginname = new SuggestBox(userList);
		loginname.setSize("120px", "20px");
		loginname.setAnimationEnabled(true);
		loginname.setAutoSelectEnabled(false);
		
		password.addFocusHandler(new FocusHandler(){
			public void onFocus(FocusEvent event) {
				String cookie = Cookies.getCookie(loginname.getText());
				if(cookie!=null)
				{
					chkRemember.setValue(true);
					password.setText(cookie);	
				}
			}
	    });	
		
		password.setSize("120px", "20px");
	    password.addKeyUpHandler(new KeyUpHandler()
	    {
	    	public void onKeyUp(KeyUpEvent event) {
				if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
			    {
					signIn();
					password.setFocus(false);
			    }
			}
	    });
	    
	    chkRemember.setText(constants.loginRememberMe());
	    chkAnonymous.setText(constants.loginSignInAsAnonymous());
	    chkAnonymous.setVisible(ConfigConstants.SHOWGUESTLOGIN);

 	    chkAnonymous.addClickHandler(new ClickHandler() {
	    	public void onClick(ClickEvent event) {
	    		
	    		//password.setEnabled(!chkAnonymous.getValue());
				//loginname.setEnabled(!chkAnonymous.getValue());
				chkRemember.setEnabled(!chkAnonymous.getValue());				
	    		
				if(chkAnonymous.getValue()){
					loginname.setText(ConfigConstants.GUESTUSERNAME);
					password.setText(ConfigConstants.GUESTPASSWORD);
				} else {
					chkAnonymous.setValue(false);
					loginname.setText("");
					password.setText("");					
				}
	        }
	    });	    

	    signin.addClickHandler(new ClickHandler() {
	    	public void onClick(ClickEvent event) {
	    		signIn();
	    	} 
	    });
		
		
		// Construct the widget
	    panel.setStyleName("aos-widgetlib");	    
	    loginForm.setSize("100%", "100%");	   
		loginForm.setWidget(0,0, new HTML(constants.loginLogin()));
	    loginForm.setWidget(1,0, loginname);	    
	    loginForm.setWidget(2,0, new HTML(constants.loginPassword()));	    
	    loginForm.setWidget(3,0, password);
	    
	    /*loginForm.setWidget(4,0, new HTML("Ontology"));
	    loginForm.setWidget(5,0, selectOntology);*/
	    
	    VerticalPanel checkContainer = new VerticalPanel();
	    checkContainer.add(hp = new HorizontalPanel());
	    hp.add(chkRemember);
	    checkContainer.add(hp = new HorizontalPanel());
	    hp.add(chkAnonymous);
	    
	    loginForm.setWidget(4,0,checkContainer);	    	    
	    loginForm.setWidget(5,0,hp = new HorizontalPanel()) ; 	    
	    hp.add(signin);
	    signin.setFocus(true);
	    signin.setText(constants.loginSignIn());
	    for(int i=0; i < 6 ; i++){
	    	loginForm.getCellFormatter().setHorizontalAlignment(i, 0, HasHorizontalAlignment.ALIGN_CENTER);
		    loginForm.getCellFormatter().setVerticalAlignment(i, 0, HasVerticalAlignment.ALIGN_MIDDLE);
	    }
	    panel.add(loginForm);
	    panel.setCellHorizontalAlignment(loginForm, HasHorizontalAlignment.ALIGN_CENTER);
	    panel.setCellVerticalAlignment(loginForm, HasVerticalAlignment.ALIGN_MIDDLE);
	    panel.setSpacing(5);
	    initWidget(panel);
	}
	
	private ArrayList<String> getLoginCookieList()
	{
		ArrayList<String> list = new ArrayList<String>();
		String loginnamecookie = Cookies.getCookie("AGROVOCUSERLOGINNAME");
		if(loginnamecookie!=null)
		{
			String[] logincookielist =  loginnamecookie.split("~");
			for (int i=0;i<logincookielist.length;i++) 
			{
				list.add(logincookielist[i]);
			}
		}
		return list;
	}
	
	private void addLoginToCookieList(String loginname)
	{
		ArrayList<String> list = new ArrayList<String>();
		String loginnamecookie = Cookies.getCookie("AGROVOCUSERLOGINNAME");
		if(loginnamecookie!=null)
		{
			String[] logincookielist =  loginnamecookie.split("~");
			for (int i=0;i<logincookielist.length;i++) 
			{
				list.add(logincookielist[i]);
			}
		}
		
		if(!list.contains(loginname))
			Cookies.setCookie("AGROVOCUSERLOGINNAME", loginnamecookie+"~"+loginname, new Date((new Date()).getTime() + COOKIE_TIMEOUT));
			
	}
	
	private void removeLoginToCookieList(String loginname)
	{
		ArrayList<String> list = new ArrayList<String>();
		String loginnamecookie = Cookies.getCookie("AGROVOCUSERLOGINNAME");
		if(loginnamecookie!=null)
		{
			String[] logincookielist =  loginnamecookie.split("~");
			for (int i=0;i<logincookielist.length;i++) 
			{
				list.add(logincookielist[i]);
			}
		}
		if(list.contains(loginname))
		{
			list.remove(loginname);
		}
		loginnamecookie = "";
		for (int i=0;i<list.size();i++) 
		{
			loginnamecookie += "|~|"+list.get(i);
		}
		Cookies.setCookie("AGROVOCUSERLOGINNAME", loginnamecookie, new Date((new Date()).getTime() + COOKIE_TIMEOUT));
			
	}
	
	
	private void signIn()
	{
		if (loginname.getText()=="" || password.getText()==""){
			Window.alert(constants.loginNoEmpty());
			if(loginname.getText()=="")
			{
				loginname.setFocus(true);
			}
			else if (password.getText()=="")
			{
				password.setFocus(true);
			}
			else
			{
				loginname.setFocus(true);
			}
		}
		else
		{
    		AsyncCallback<Object> callback = new AsyncCallback<Object>() {
    			public void onSuccess(Object result) {	   				
    				if(result==null){
	    				try 
	    				{
							Window.alert(constants.loginNoMatch());
						} 
	    				catch (Exception e) 
	    				{
							e.printStackTrace();
						}
    				} 
    				else 
    				{
    					UserLogin userLoginObj = (UserLogin) result;
    					if(userLoginObj.getNoOfGroup()==0)
    					{
			    			Window.alert(constants.loginNoAssignGroup());
			    		}
    					else if(userLoginObj.getUserSelectedLanguage().size()<1)
    					{
    						Window.alert(constants.loginNoAssignLang());
    					}
    					else 
    					{
    						// Set the cookie value
    			    		if(chkRemember.getValue())
    			    		{
    			    			Cookies.setCookie(loginname.getText(), password.getText(), new Date((new Date()).getTime() + COOKIE_TIMEOUT));
    			    			addLoginToCookieList(loginname.getText());
    			    		}
    			    		else
    			    		{
    			    			Cookies.removeCookie(loginname.getText());
    			    			removeLoginToCookieList(loginname.getText());
    			    		}
    			    		
    			    		if(selectGroupDlg == null || !selectGroupDlg.isLoaded)
    			    			selectGroupDlg = new SelectPreferenceDlg(userLoginObj);
					    	selectGroupDlg.show();	    						    		
				    	}
			        }
    			}
			    public void onFailure(Throwable caught) {
			    	Window.alert(constants.loginFail());
			    }
    		};		    		
			Service.systemService.getAuthorize(loginname.getText(), password.getText(), callback);
		}
	}
	
}