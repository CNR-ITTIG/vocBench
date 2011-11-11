package org.fao.aoscs.client;

import java.util.ArrayList;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.classification.widgetlib.OlistBox;
import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.module.logging.LogManager;
import org.fao.aoscs.client.module.system.service.SystemService.SystemServiceUtil;
import org.fao.aoscs.client.widgetlib.shared.dialog.DialogBoxAOS;
import org.fao.aoscs.client.widgetlib.shared.panel.TitleBodyWidget;
import org.fao.aoscs.domain.InitializeUsersPreferenceData;
import org.fao.aoscs.domain.LanguageInterface;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.UserLogin;
import org.fao.aoscs.domain.UsersGroups;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SelectPreferenceDlg extends DialogBoxAOS{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);

	private OlistBox lstgroups = new OlistBox();
	private OlistBox lstlang = new OlistBox();
	private OlistBox lstontology = new OlistBox();
	
	private TextArea txtgroups = new TextArea();
	private TextArea txtlang = new TextArea();
	private TextArea txtontology = new TextArea();
	
	private VerticalPanel panelgroups = new VerticalPanel();
	private VerticalPanel panellang = new VerticalPanel();		
	private VerticalPanel panelontology = new VerticalPanel();
	
	private Button btnSubmit = new Button(constants.buttonSubmit());
	private Button btnCancel = new Button(constants.buttonCancel());
	
	ArrayList<String> menu = new ArrayList<String>();	
	
	public SelectPreferenceDlg(final UserLogin  userLoginObj) {				

		//load initial data
		load(userLoginObj);
	}
	
	public void initPanels(final UserLogin userLoginObj){
		// popup element
		this.setText(constants.selPref());
		
		lstgroups.setWidth("200px");
		lstlang.setWidth("200px");
		lstontology.setWidth("200px");
		
		lstgroups.setVisibleItemCount(6);
		lstlang.setVisibleItemCount(6);
		lstontology.setVisibleItemCount(6);
		
		txtgroups.setWidth("200px");
		txtlang.setWidth("200px");
		txtontology.setWidth("200px");
		
		txtgroups.setVisibleLines(4);
		txtlang.setVisibleLines(4);
		txtontology.setVisibleLines(4);
		
		txtgroups.setReadOnly(true);
		txtlang.setReadOnly(true);
		txtontology.setReadOnly(true);
		
		DOM.setStyleAttribute(lstgroups.getElement(), "cursor", "pointer");
	    DOM.setStyleAttribute(lstlang.getElement(), "cursor", "pointer");
	    DOM.setStyleAttribute(lstontology.getElement(), "cursor", "pointer");
	    
		DOM.setStyleAttribute(txtgroups.getElement(), "marginTop", "0px");
		DOM.setStyleAttribute(txtlang.getElement(), "marginTop", "0px");
		DOM.setStyleAttribute(txtontology.getElement(), "marginTop", "0px");
	    
	    TitleBodyWidget tbwidgetgroup = new TitleBodyWidget(constants.selPrefGroup(), lstgroups, null, txtgroups,  "200px", "100%");
	    TitleBodyWidget tbwidgetlang = new TitleBodyWidget(constants.selPrefLanguage(), lstlang, null, txtlang, "200px", "100%");
	    TitleBodyWidget tbwidgetontology = new TitleBodyWidget(constants.selPrefOntology(), lstontology, null, txtontology, "200px", "100%");
	    
		panelgroups.add(tbwidgetgroup);
		panellang.add(tbwidgetlang);
		panelontology.add(tbwidgetontology);

		FlexTable flexpanel = new FlexTable();  		  	 
		flexpanel.setCellSpacing(5);
		flexpanel.setCellPadding(5);
		flexpanel.setWidth("95%");
		int colCount = 0; 
		if(panelgroups.isVisible()){
			flexpanel.setWidget(0,colCount,panelgroups);
			colCount++;
		}
		/*if(panellang.isVisible()){		
			flexpanel.setWidget(0,colCount,panellang);
			colCount++;
		}*/
		if(panelontology.isVisible()){
			flexpanel.setWidget(0,colCount,panelontology);
			colCount++;
		}
		
		for(int i=0; i<colCount; i++){
			int width =  100/colCount;
			flexpanel.getCellFormatter().setWidth(0, i, width+"%");
			flexpanel.getCellFormatter().setVerticalAlignment(0,i,HasVerticalAlignment.ALIGN_TOP);
			flexpanel.getCellFormatter().setHorizontalAlignment(0,i,HasHorizontalAlignment.ALIGN_CENTER);
			flexpanel.getCellFormatter().setHeight(0,i, "100%");
		}
		HorizontalPanel panel = new HorizontalPanel();
		panel.setSpacing(10);
		panel.setWidth("100%");
		panel.add(flexpanel);
				
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(5);
		hp.add(btnSubmit);
		hp.add(btnCancel);
		
		VerticalPanel bottompanel = new VerticalPanel();
		bottompanel.setWidth("100%");
		bottompanel.addStyleName("bottombar");
		bottompanel.add(hp);
		bottompanel.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_RIGHT);
		
		
		VerticalPanel vp = new VerticalPanel();
		vp.add(panel);
		vp.add(bottompanel);
		
		vp.setCellHorizontalAlignment(hp,HasHorizontalAlignment.ALIGN_RIGHT);
		vp.setWidth("100%");
		setWidget(vp);
		btnCancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Main.gotoLoginScreen();
				hide();
			}
		
		});

		btnSubmit.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				hide(); 
				userLoginObj.setOntology((OntologyInfo) lstontology.getObject(lstontology.getSelectedIndex()));
	    		//userLoginObj.setLanguage(((LanguageInterface) lstlang.getObject(lstlang.getSelectedIndex())).getLanguageCode());
				userLoginObj.setLanguage(constants.mainLocale());
				if(userLoginObj.getGroupid() == null){
					userLoginObj.setGroupid(""+((UsersGroups) lstgroups.getObject(lstgroups.getSelectedIndex())).getUsersGroupsId());
					userLoginObj.setGroupname(""+((UsersGroups) lstgroups.getObject(lstgroups.getSelectedIndex())).getUsersGroupsName());
				}
				//Window.open(GWT.getHostPageBaseURL()+"index.html?locale="+userLoginObj.getLanguage().toLowerCase(), "_self","schollbars=0,toolbar=0,resizable=1,status=no");
	    		AsyncCallback<Object> cbkauthorize = new AsyncCallback<Object>() {
	    			public void onSuccess(Object result) {	   				
	    				if(result==null){
		    				try {
								Window.alert(constants.selPrefNoMatch());
							} catch (Exception e) {
								e.printStackTrace();
							}
	    				} else {
	    	  			 	try {	    						
	    						// Create session
	    						/* User data from session for feed to profile query */
	    						AsyncCallback<Object> callback1 = new AsyncCallback<Object>() {
	    						    public void onSuccess(Object sessionresult) {		    						    	
										UserLogin userLoginObj = (UserLogin) sessionresult;
										MainApp mainApp = new MainApp(userLoginObj);
										new LogManager().startLog(""+userLoginObj.getUserid());
										mainApp.setWidth("100%");
										Main.replaceRootPanel(mainApp); 		    						    									
	    						    }
	    						    public void onFailure(Throwable caught) {
	    						       	Window.alert(constants.selPrefFail());
	    						    }
	    						 };
	    						Service.systemService.checkSession(MainApp.USERLOGINOBJECT_SESSIONNAME, callback1); // Get userlogin from session		    						
							} catch (Exception e) {
								e.printStackTrace();
							}
				        }
	    			}
				    public void onFailure(Throwable caught) {
				    	Window.alert(constants.selPrefFail());
				    }
	    		};		
	    		
				Service.systemService.getAuthorization(MainApp.USERLOGINOBJECT_SESSIONNAME, userLoginObj,cbkauthorize);		   				
			}
			
		});
	}
	public void load(final UserLogin userLoginObj)
	{

		final AsyncCallback<Object> callback = new AsyncCallback<Object>() {
			public void onSuccess(Object result) {
				
				InitializeUsersPreferenceData initUserPreference = (InitializeUsersPreferenceData) result;
				
				userLoginObj.setUsersPreference(initUserPreference.getUsersPreference());
				
				//Set user groups
				ArrayList<UsersGroups> listgroups = initUserPreference.getUsergroups();
				for(int i=0;i<listgroups.size();i++){
		    		UsersGroups userGroups = (UsersGroups) listgroups.get(i);
		    		lstgroups.addItem(userGroups.getUsersGroupsName(),userGroups);
		    	}					
		    	
		    	lstgroups.setItemSelected(0, true);
		    	
		    	String descGroups = ((UsersGroups) lstgroups.getObject(lstgroups.getSelectedIndex())).getUsersGroupsDesc();
			    lstgroups.setTitle(descGroups);
			    txtgroups.setText(descGroups);
			    lstgroups.addChangeHandler(new ChangeHandler(){
					public void onChange(ChangeEvent event) {
						String descGroups = ((UsersGroups) lstgroups.getObject(lstgroups.getSelectedIndex())).getUsersGroupsDesc();
						lstgroups.setTitle(descGroups);
						txtgroups.setText(descGroups);
					}
			    	
			    });
			    
			    if(userLoginObj.getNoOfGroup()<2){
					panelgroups.setVisible(false);
				}else{
					if(userLoginObj.getGroupid()!= null &&  userLoginObj.getGroupid().equals(ConfigConstants.VISITORGROUPID)){
						panelgroups.setVisible(false);
					}
				}
				// Set languages for the interface
			    ArrayList<LanguageInterface> listlang = initUserPreference.getInterfaceLanguage();
				for(int i=0;i<listlang.size();i++)
				{
					LanguageInterface langInterface = (LanguageInterface) listlang.get(i);
					lstlang.addItem(langInterface.getLocalLanguage(), langInterface);
				}
				
				int index = 0;
				String defaultlang = "en";
				if(initUserPreference.getUsersPreference().getUserId() != 0)
					defaultlang = initUserPreference.getUsersPreference().getLanguageCodeInterface().toLowerCase();
				for(int i=0;i<lstlang.getItemCount();i++)
				{
					if(defaultlang.equals(((LanguageInterface)lstlang.getObject(i)).getLanguageCode().toLowerCase()))
					{
						index = i;
						break;
					}
				}
				lstlang.setSelectedIndex(index);
				
				String descLang = ((LanguageInterface) lstlang.getObject(lstlang.getSelectedIndex())).getLanguageNote();
				lstlang.setTitle(descLang);
			    txtlang.setText(descLang);
			    lstlang.addChangeHandler(new ChangeHandler(){
					public void onChange(ChangeEvent event) {
						String descLang = ((LanguageInterface) lstlang.getObject(lstlang.getSelectedIndex())).getLanguageNote();
						lstlang.setTitle(descLang);
						txtlang.setText(descLang);
					}
			    	
			    });
			    
				// Fill the ontology
			    ArrayList<OntologyInfo> ontolist = initUserPreference.getOntology();
			    if(ontolist.size() < 1){
			    	hide();
			    	Window.alert(constants.selLoadOntologyFail());
			    	Main.gotoLoginScreen();
			    }
			    			
				for(int i=0;i<ontolist.size();i++)
				{
		    		OntologyInfo ontoInfo = (OntologyInfo) ontolist.get(i);
		    		lstontology.addItem(ontoInfo.getOntologyName(), ontoInfo);
				}
				
				int cnt = 0;
				if(initUserPreference.getUsersPreference().getUserId() != 0)
				{
					for(int i=0;i<lstontology.getItemCount();i++)
					{
						if(initUserPreference.getUsersPreference().getOntologyId()== ((OntologyInfo)lstontology.getObject(i)).getOntologyId())
						{
							cnt = i;
							break;
						}
					}
				}
				if(cnt>0)
					lstontology.setItemSelected(cnt, true);

				String descOntology = ((OntologyInfo) lstontology.getObject(lstontology.getSelectedIndex())).getOntologyDescription();
			    lstontology.setTitle(descOntology);
			    txtontology.setText(descOntology);
			    lstontology.addChangeHandler(new ChangeHandler(){
					public void onChange(ChangeEvent event) {
						String descOntology = ((OntologyInfo) lstontology.getObject(lstontology.getSelectedIndex())).getOntologyDescription();
						lstontology.setTitle(descOntology);
						txtontology.setText(descOntology);
					}
			    });
			    
			    initPanels(userLoginObj);
			    center();
				
			}
			public void onFailure(Throwable caught) {
				Window.alert(constants.selPrefNoLoad());
			}
		};
		SystemServiceUtil.getInstance().initSelectPreferenceData(Integer.parseInt(userLoginObj.getUserid()), callback);
	}
}
