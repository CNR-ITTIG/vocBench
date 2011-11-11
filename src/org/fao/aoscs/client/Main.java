package org.fao.aoscs.client;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.module.logging.LogManager;
import org.fao.aoscs.client.utility.HelpUtility;
import org.fao.aoscs.client.widgetlib.Main.AcknowledgementWidget;
import org.fao.aoscs.client.widgetlib.Main.BrowserCompatibilityInfo;
import org.fao.aoscs.client.widgetlib.Main.Footer;
import org.fao.aoscs.client.widgetlib.Main.Header;
import org.fao.aoscs.client.widgetlib.Main.LoginForm;
import org.fao.aoscs.client.widgetlib.Main.QuickLinks;
import org.fao.aoscs.client.widgetlib.Main.WhatIsNew;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabel;
import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;
import org.fao.aoscs.domain.LanguageInterface;
import org.fao.aoscs.domain.UserLogin;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Main implements EntryPoint {
	
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private static VerticalPanel panel;
	private static HorizontalPanel langInterfacePanel = new HorizontalPanel();
	
	public void onModuleLoad() {		
		/* User data from session for feed to profile query */
		AsyncCallback<UserLogin> callback = new AsyncCallback<UserLogin>() {
		    public void onSuccess(UserLogin userLoginObj) {
		    	// init page layout
		    	if(userLoginObj==null){
		    		initLayout();
		    	}else{
	    			// Get information from session
					MainApp mainApp = new MainApp(userLoginObj);
					mainApp.setWidth("100%");
					Main.replaceRootPanel(mainApp);
		    	}
		    }
		    public void onFailure(Throwable caught) {
		       	Window.alert(constants.mainSessionExpired());
		    }
		 };
		Service.systemService.checkSession(MainApp.USERLOGINOBJECT_SESSIONNAME, callback); // Get userlogin from session
		
		 Window.addWindowClosingHandler(new ClosingHandler() {
				public void onWindowClosing(ClosingEvent event) {
					new LogManager().endLog();
				}
	        });		
	}
	
	private static void initLayout()
	{
		AsyncCallback<HashMap<String, String>> callback = new AsyncCallback<HashMap<String, String>>()
		{
			public void onSuccess(HashMap<String, String> result) {
				ConfigConstants.loadConstants(result);
				HelpUtility.loadHelp();
				Window.setTitle(constants.mainPageTitle()+" :: "+constants.mainVersion()+" "+ConfigConstants.DISPLAYVERSION+"");		 
				panel = new VerticalPanel();
				panel.setSize("100%", "100%");
				final VerticalPanel centerContainer = new VerticalPanel();
				
				// Quick links
				QuickLinks quickLinks = new QuickLinks("header-quickLinks",true);		
				
				// Header		 
				VerticalPanel header = new Header();		 		 
						
				//Main Content
				HTML descTitle = new HTML(constants.mainWelcome());	
				DOM.setStyleAttribute(descTitle.getElement(), "fontWeight", "bold");
				DOM.setStyleAttribute(descTitle.getElement(), "fontSize", "12px");
				
				HTML desc = new HTML(constants.mainBrief());		
				
				LinkLabel learnMore = new LinkLabel(null, constants.mainLearnMore(), constants.mainLearnMore()); 
				learnMore.setLabelStyle("toolbar-link");
				learnMore.addClickHandler(new ClickHandler(){
					public void onClick(ClickEvent arg0) {
						Window.open("http://aims.fao.org/workbench/help/aboutus_help","_blank","schollbars=0,toolbar=0,resizable=1,status=no" );
					}
				});
						
				HTML ca = new HTML("&nbsp;"+constants.mainMustRegister()+"&nbsp;");		
				ca.setStyleName("term-Label");									
				ca.addClickHandler(new ClickHandler(){
			 		public void onClick(ClickEvent event) {
			 			Register register = new Register();
			 			centerContainer.clear();
			 			centerContainer.add(register);	 			
					}
			 	});

				HorizontalPanel briefHp = new HorizontalPanel();
				briefHp.add(new HTML(constants.mainMustLogin() + constants.mainMustLoginOr()));		
				briefHp.add(ca);
				briefHp.add(new HTML(constants.mainToUseWB()));
				
				// Browser warning message
				BrowserCompatibilityInfo bcInfo = new BrowserCompatibilityInfo();
				
				VerticalPanel briefLeft = new VerticalPanel();
				briefLeft.setSpacing(10);
				briefLeft.setSize("100%","100%");
				briefLeft.add(descTitle);
				briefLeft.add(desc);
				briefLeft.add(learnMore);
				if(!ConfigConstants.ISVISITOR){
					briefLeft.add(briefHp);
				}
				briefLeft.add(bcInfo);
				
				briefLeft.setCellVerticalAlignment(learnMore, HasVerticalAlignment.ALIGN_MIDDLE);
				briefLeft.setCellVerticalAlignment(briefHp, HasVerticalAlignment.ALIGN_MIDDLE);
				
				VerticalPanel briefRight= new VerticalPanel();
				briefRight.setSize("100%","100%");
				Image flyer = new Image("images/flyer.jpg");
				DOM.setStyleAttribute(flyer.getElement(), "cursor", "pointer");		
				briefRight.add(flyer);
				flyer.addClickHandler(new ClickHandler()
				{
					public void onClick(ClickEvent event) {
						Window.open("ftp://ftp.fao.org/gi/gil/gilws/aims/references/flyers/csworkbench_en.pdf","_blank","schollbars=0,toolbar=0,resizable=1,status=no" );
					}}
				);	
				
				final HorizontalPanel briefMiddle = new HorizontalPanel();
				briefMiddle.setSize("100%","100%");
				briefMiddle.add(briefLeft);
				briefMiddle.add(new Spacer("20px", "100%"));
				briefMiddle.add(briefRight);
				
				WhatIsNew whatIsNew = new WhatIsNew();
				whatIsNew.addNewItem(new HTML("The "+ constants.mainPageTitle() +" Web Services Beta version has been released for developers of agricultural information management systems to incorporate the " + constants.mainPageTitle() + " into their applications via <a href='"+ConfigConstants.WEBSERVICESINFO+"' target='_blank'>web services</a>."));
				
				VerticalPanel content = new VerticalPanel();		
				content.setStyleName("front-content");	
				content.add(briefMiddle);
				content.add(whatIsNew);		
				
				// Sidebar		
				final LinkLabel help = new LinkLabel("images/help.png", constants.mainHelpTitle(), constants.mainHelp(), "term-Label");
				help.addClickHandler(new ClickHandler(){
					public void onClick(ClickEvent event) {
						HelpUtility.openHelp("LOGIN");
					}
				});
				LoginForm loginForm = new LoginForm();		
				loginForm.setSize("100%","100%");
				
				LinkLabel createAccount = new LinkLabel("images/register.png", constants.mainRegisterTitle(), constants.mainRegister(), "term-Label");
				createAccount.addClickHandler(new ClickHandler(){
			 		public void onClick(ClickEvent event) {
			 			Register register = new Register();
			 			centerContainer.clear();
			 			centerContainer.add(register);				
					}
			 	});	 
				
			 	LinkLabel forgetPassword = new LinkLabel("images/password.png", constants.mainForgotPasswordTitle(), constants.mainForgotPassword(), "term-Label");
			 	forgetPassword.addClickHandler(new ClickHandler(){
						public void onClick(ClickEvent event) {	
							ForgetPassword forgotPwdPanel = new ForgetPassword();
							centerContainer.clear();
							centerContainer.setSpacing(10);
							centerContainer.add(forgotPwdPanel);
					}
			 	}); 	 	
			 	 				 		
				VerticalPanel loginUtility = new VerticalPanel();				
				loginUtility.setSpacing(5);
				loginUtility.setStyleName("loginUtility");
				if(!ConfigConstants.ISVISITOR){
					loginUtility.add(forgetPassword);
					loginUtility.add(createAccount);
				}
				loginUtility.setCellHorizontalAlignment(forgetPassword, HasHorizontalAlignment.ALIGN_CENTER);
				loginUtility.setCellHorizontalAlignment(createAccount, HasHorizontalAlignment.ALIGN_CENTER);
		 
				VerticalPanel sideBarContainer = new VerticalPanel();							
				sideBarContainer.setSize("100%", "100%");
				sideBarContainer.add(help);				
				sideBarContainer.add(loginForm);				
				sideBarContainer.add(new Spacer("100%","10px"));				
				sideBarContainer.add(loginUtility);
				sideBarContainer.setCellHorizontalAlignment(help, HasHorizontalAlignment.ALIGN_RIGHT);
				sideBarContainer.setCellHorizontalAlignment(loginForm, HasHorizontalAlignment.ALIGN_CENTER);
				sideBarContainer.setCellHorizontalAlignment(loginUtility, HasHorizontalAlignment.ALIGN_CENTER);
				
				sideBarContainer.setCellHeight(help , "5px");		
				sideBarContainer.setCellHeight(loginUtility , "30px");
				
				sideBarContainer.setCellWidth(loginForm , "100%");		
				sideBarContainer.setCellWidth(loginUtility , "100%");
		 	
				VerticalPanel sideBar= new VerticalPanel();
				sideBar.setStyleName("sideBar");	
				sideBar.add(sideBarContainer);
				
				HorizontalPanel mainContiner = new HorizontalPanel();		
				mainContiner.setSize("100%","100%");	
				mainContiner.add(content);
				mainContiner.add(sideBar);										
				mainContiner.setCellWidth(sideBarContainer, "225px");
						
				// Acknowledgements		
				HorizontalPanel ackPartner = new HorizontalPanel();		
				ackPartner.setSize("100%", "100%");		 
				ackPartner.add(getPartners());
				
				VerticalPanel ack = new VerticalPanel();		
				ack.setStyleName("ack");		 		 
				ack.add(ackPartner);
			
				VerticalPanel ackContainer = new VerticalPanel();		
				ackContainer.setStyleName("ack-container");	
				ackContainer.add(ack);
				ackContainer.setCellHeight(ack, "100%");
				ackContainer.setCellWidth(ack, "100%");
				
				centerContainer.setSize("100%","100%");		
				centerContainer.add(mainContiner);				
				centerContainer.add(ackContainer);		
				centerContainer.setCellVerticalAlignment(ackContainer, HasVerticalAlignment.ALIGN_BOTTOM);
				
				Footer footer = new Footer();
				
				AsyncCallback<ArrayList<LanguageInterface>> callback = new AsyncCallback<ArrayList<LanguageInterface>>()
				{
					public void onSuccess(ArrayList<LanguageInterface> result) {
						langInterfacePanel.clear();
						langInterfacePanel.add(getLanguageBar(result));			
					}
				    public void onFailure(Throwable caught) {		    	
				    }
				};
				
				Service.systemService.getInterfaceLang(callback);
				langInterfacePanel.setStyleName("header-quickLinks");
				
				HorizontalPanel topPanel = new HorizontalPanel();
				topPanel.add(quickLinks);
				topPanel.add(langInterfacePanel);
				topPanel.setCellVerticalAlignment(quickLinks, HasVerticalAlignment.ALIGN_MIDDLE);
				topPanel.setCellHorizontalAlignment(quickLinks, HasHorizontalAlignment.ALIGN_LEFT);
				topPanel.setCellVerticalAlignment(langInterfacePanel, HasVerticalAlignment.ALIGN_MIDDLE);
				topPanel.setCellHorizontalAlignment(langInterfacePanel, HasHorizontalAlignment.ALIGN_RIGHT);
				topPanel.setCellHeight(quickLinks, "30px");
				topPanel.setCellHeight(langInterfacePanel, "30px");
				topPanel.setCellWidth(quickLinks, "100%");
				
				// Add everything
				panel.add(topPanel);
				panel.add(header);
				panel.add(centerContainer);		
				panel.add(footer);
				
				panel.setCellHeight(header, "40px");
				panel.setCellHeight(topPanel, "30px");
				panel.setCellWidth(header, "100%");		
				panel.setCellWidth(centerContainer, "100%");		
				panel.setCellHeight(centerContainer, "100%");
				panel.setCellVerticalAlignment(footer, HasVerticalAlignment.ALIGN_BOTTOM);
				panel.setCellVerticalAlignment(footer, HasVerticalAlignment.ALIGN_BOTTOM);
				
				RootPanel.get().add(panel);				
			}
		    public void onFailure(Throwable caught) {		    	
		    }
		};
		
		Service.systemService.loadConfigConstants(callback);
	 }
	 
	 public static void replaceRootPanel(Widget object){
		 //RootPanel.get("logcenter").clear();
		 RootPanel.get().clear();
		 RootPanel.get().add(object);
	 }
	 public static void gotoLoginScreen(){
		 RootPanel.get().clear();
		 initLayout();
	 }
	 public static native void openURL(String url) /*-{
	   $wnd.open(url,'_blank','');
	}-*/;
	 public static native void openURL(String url, String target) /*-{
	   $wnd.open(url,target,'');
	}-*/;
	 public static native String getUserAgent() /*-{
	   return $wnd.navigator.appName;
	}-*/;

	 public static void signOut(){
		 RootPanel.get().clear();
		 History.newItem(null);
		 initLayout();
	 }
	 
	 public static void mailAlert(String fname, String lname, String pemail,String userName,String password){
		  String to = pemail;
		  String subject = constants.mainPageTitle() + " change password";
		  String body = "";
		  body += "Dear "+fname+" "+lname+",";
		  body += "\n\nYour password has been successfully changed. "+
		  			"You can now log in to the workbench with the username '" +
		  			userName+"' and your new password '"+password+"'." +
					"\n\nThanks for your interest."+
					"\n\nIf you want to unregister, please send an email with your username and the "+
					"subject: " + constants.mainPageTitle() + " - Unregister to "+
					"FAO-AGRIS-CARIS@fao.org.";
		  body += "\n\nRegards,";
		  body += "\n\nThe " + constants.mainPageTitle() + " team.";

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
		  subject = constants.mainPageTitle() + " :User Request";
		  body = "A new user registration request for " + constants.mainPageTitle() + ".\n\n";
		  body += constants.mainPageTitle() + " URL : " + GWT.getHostPageBaseURL() + "\n\n";
		  body += "WB Version : "+ConfigConstants.DISPLAYVERSION+" \n\n";
		  body += "Username : "+userName+" \n\n";
		  body += "First Name : "+fname+" \n\n";
		  body += "Last Name : "+lname+" \n\n";
		  body += "Email : "+pemail+ "\n\n";
		  body += "Please assign languages, user groups and activate the account.\n";
		  body += 	"\n\n Regards,";
		  body += 	"\n\nThe " + constants.mainPageTitle() + " Team.";
		  
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
	
	public static Widget getPartners()
	{		 		 
		HTML ackTitle = new HTML(constants.mainPartner());
		DOM.setStyleAttribute(ackTitle.getElement(), "paddingTop", "10px");
		DOM.setStyleAttribute(ackTitle.getElement(), "paddingLeft", "30px");
		ackTitle.setStyleName("ack-title");
		 
		HorizontalPanel top = new HorizontalPanel();		
		top.setSize("100%","100%");
		DOM.setStyleAttribute(top.getElement(), "padding", "4px");		 
		DOM.setStyleAttribute(top.getElement(), "paddingLeft", "30px");
		
		AcknowledgementWidget ackFao   	= new AcknowledgementWidget("images/logo_fao.gif","Food and Agriculture Organization of the United Nations","Food and Agriculture Organization of the United Nations","for a world without hunger","http://www.fao.org");
		AcknowledgementWidget ackKu    	= new AcknowledgementWidget("images/logo_ku.gif","Kasetsart University","Kasetsart University","Bangkok, Thailand","http://www.ku.ac.th");
		AcknowledgementWidget ackAgris 	= new AcknowledgementWidget("images/logo_agris.gif","Thai National Agris Center","Thai National Agris Center","Bangkok, Thailand","http://thaiagris.lib.ku.ac.th/");
		AcknowledgementWidget ackMimos 	= new AcknowledgementWidget("images/mimos_logo.jpg","MIMOS Berhad","MIMOS Berhad","Kuala Lumpur, Malaysia","http://www.mimos.my/");
		
		top.add(ackFao);	
		top.add(ackKu);		
		top.add(ackAgris);
		top.add(ackMimos);
				
		top.setCellHorizontalAlignment(top.getWidget(0), HasHorizontalAlignment.ALIGN_LEFT);
		top.setCellHorizontalAlignment(top.getWidget(1), HasHorizontalAlignment.ALIGN_LEFT);
		top.setCellWidth(ackFao, "40%");
		top.setCellWidth(ackKu,  "20%");
		top.setCellWidth(ackAgris,  "20%");
		top.setCellWidth(ackMimos,  "20%");
		
		top.setCellHorizontalAlignment(ackFao, HasHorizontalAlignment.ALIGN_CENTER);
		top.setCellHorizontalAlignment(ackKu, HasHorizontalAlignment.ALIGN_CENTER);
		top.setCellHorizontalAlignment(ackAgris, HasHorizontalAlignment.ALIGN_CENTER);
		top.setCellHorizontalAlignment(ackMimos, HasHorizontalAlignment.ALIGN_CENTER);
		
		HTML contrib = new HTML(constants.mainAck2());
		contrib.setStyleName("ackLabel");
		contrib.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		 
		HTML ack = new HTML(constants.mainAck1());
		ack.setStyleName("ackLabel");
		ack.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		 
		VerticalPanel leftBottom = new VerticalPanel();
		leftBottom.add(contrib);
		leftBottom.add(new Spacer("100%", "15px"));
		leftBottom.add(new AcknowledgementWidget("images/logo_icrisat.png","ICRISAT","ICRISAT","International Crops Research Institute for the Semi-Arid Tropics","http://www.icrisat.org" ));
		 
		VerticalPanel rightBottom = new VerticalPanel();
		rightBottom.add(ack);
		rightBottom.add(new Spacer("100%", "25px"));
		rightBottom.add(new AcknowledgementWidget(null, null, "Prof. Dagobert Soergel","UNIVERSITY OF MARYLAND",null));		
		 
		HorizontalPanel bottom = new HorizontalPanel();		
		DOM.setStyleAttribute(bottom.getElement(), "padding", "10px");
		DOM.setStyleAttribute(bottom.getElement(), "paddingLeft", "30px");
		
		bottom.setSize("100%","100%");		 
		bottom.add(leftBottom);
		bottom.add(rightBottom);
		
		VerticalPanel topContainer = new VerticalPanel();		
		topContainer.setSize("100%", "100%");
		DOM.setStyleAttribute(topContainer.getElement(), "borderBottom", "1px solid #CFD9EB");
		topContainer.add(ackTitle);
		topContainer.add(top);
		topContainer.setCellVerticalAlignment(ackTitle, HasVerticalAlignment.ALIGN_TOP);
		topContainer.setCellVerticalAlignment(top, HasVerticalAlignment.ALIGN_BOTTOM);
		
		HorizontalPanel bottomContainer = new HorizontalPanel();		
		bottomContainer.setSize("100%", "100%");	
		DOM.setStyleAttribute(bottomContainer.getElement(), "borderTop", "1px solid #FFFFFF");
		bottomContainer.add(bottom);
				
		
		
		VerticalPanel hp = new VerticalPanel();		 
		hp.setSize("100%", "100%");		
		hp.add(topContainer);			 
		hp.add(bottomContainer);
		hp.setCellHeight(topContainer, "50%");
		hp.setCellHeight(bottomContainer, "50%");		 
		hp.setCellHorizontalAlignment(topContainer, HasHorizontalAlignment.ALIGN_CENTER);
		hp.setCellHorizontalAlignment(bottomContainer, HasHorizontalAlignment.ALIGN_CENTER);
		return hp;
	}
	 
	public static HorizontalPanel getLanguageBar(final ArrayList<LanguageInterface> langList)
	{
		final ListBox langMenuBar = new ListBox();
		for(int i=0 ; i<langList.size() ; i++)
		{
			LanguageInterface langInterface = (LanguageInterface) langList.get(i);
			langMenuBar.addItem(langInterface.getLocalLanguage(), langInterface.getLanguageCode().toLowerCase());
			if(langList.get(i).getLanguageCode().toLowerCase().equals(constants.mainLocale().toLowerCase()))
				langMenuBar.setSelectedIndex(i);
				
		}
		langMenuBar.addChangeHandler(new ChangeHandler(){
			public void onChange(ChangeEvent event) {
				try 
				{
					Window.open(GWT.getHostPageBaseURL()+"index.html?locale="+langMenuBar.getValue(langMenuBar.getSelectedIndex()), "_self","schollbars=0,toolbar=0,resizable=1,status=no");
				} 
				catch (Throwable e) 
				{
					e.printStackTrace();
				}
			}
		});
		Image map = new Image("images/map-grey.gif");
		
		HorizontalPanel langPanel = new HorizontalPanel();	
		langPanel.setSpacing(5);
		langPanel.add(map);
		langPanel.add(langMenuBar);
		langPanel.setCellVerticalAlignment(langMenuBar, HasVerticalAlignment.ALIGN_MIDDLE);
		langPanel.setCellHorizontalAlignment(langMenuBar, HasHorizontalAlignment.ALIGN_LEFT);
		langPanel.setCellVerticalAlignment(map, HasVerticalAlignment.ALIGN_MIDDLE);

		return langPanel;
		
	 }
}
	 