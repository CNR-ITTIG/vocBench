package org.fao.aoscs.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.fao.aoscs.client.image.AOSImageBundle;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.locale.LocaleMessages;
import org.fao.aoscs.client.module.authoringtool.ImportOntology;
import org.fao.aoscs.client.module.classification.Classification;
import org.fao.aoscs.client.module.comment.CommentViewer;
import org.fao.aoscs.client.module.concept.Concept;
import org.fao.aoscs.client.module.concept.widgetlib.InfoTab;
import org.fao.aoscs.client.module.consistency.Consistency;
import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.client.module.constant.OWLActionConstants;
import org.fao.aoscs.client.module.constant.OWLStatusConstants;
import org.fao.aoscs.client.module.document.About;
import org.fao.aoscs.client.module.export.Export;
import org.fao.aoscs.client.module.logging.LogManager;
import org.fao.aoscs.client.module.logging.LogViewer;
import org.fao.aoscs.client.module.preferences.Preferences;
import org.fao.aoscs.client.module.relationship.Relationship;
import org.fao.aoscs.client.module.search.Search;
import org.fao.aoscs.client.module.search.widgetlib.ResultPanel;
import org.fao.aoscs.client.module.search.widgetlib.SearchOption;
import org.fao.aoscs.client.module.search.widgetlib.SuggestBoxAOS;
import org.fao.aoscs.client.module.statistic.Statistic;
import org.fao.aoscs.client.module.system.GroupsAssignment;
import org.fao.aoscs.client.module.system.UsersAssignment;
import org.fao.aoscs.client.module.validation.Validation;
import org.fao.aoscs.client.utility.HelpUtility;
import org.fao.aoscs.client.widgetlib.Main.Footer;
import org.fao.aoscs.client.widgetlib.Main.QuickLinks;
import org.fao.aoscs.client.widgetlib.Main.ToolBarContainer;
import org.fao.aoscs.client.widgetlib.shared.dialog.ConceptBrowser;
import org.fao.aoscs.client.widgetlib.shared.dialog.RelationshipBrowser;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabel;
import org.fao.aoscs.client.widgetlib.shared.label.MenuBarAOS;
import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.InitializeSystemData;
import org.fao.aoscs.domain.LanguageCode;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.PermissionObject;
import org.fao.aoscs.domain.SearchParameterObject;
import org.fao.aoscs.domain.UserLogin;
import org.fao.aoscs.domain.UsersPreference;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MainApp extends Composite { // Application container

	public static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	public static LocaleMessages messages = (LocaleMessages) GWT.create(LocaleMessages.class);
    public static AOSImageBundle aosImageBundle = (AOSImageBundle) GWT.create(AOSImageBundle.class);
    
    private VerticalPanel panel = new VerticalPanel();
    public static ArrayList<String> userMenu = new ArrayList<String>();
    public static int userId;
    public static int groupId;
    public static String groupName;
    public static String userLogin = "";
    public static String userLanguage = "";
    public static ArrayList<String> userSelectedLanguage = new ArrayList<String>();
    public static OntologyInfo userOntology;
    public static ArrayList<LanguageCode> languageCode = new ArrayList<LanguageCode>();
    public static HashMap<String, String> languageDict = new HashMap<String, String>();
	
    public static PermissionObject permissionTable = new PermissionObject();
    public static UsersPreference userPreference;
    public static String USERLOGINOBJECT_SESSIONNAME = "userloginobj";
    public ToolBarContainer iconContainer;

    private LanguageFilter langFilter;
    private CommentDialogBox commentDialog;
    
    private ConceptNavigationHistory conceptNavigationHistory;
    private static HashMap<Long, ConceptObject> conceptNavigationHistoryList = new HashMap<Long, ConceptObject>();
    private static int conceptNavigationHistoryMaxSize = 25;

    // module
    public DeckPanel modulePanel = new DeckPanel();
    public Search search = null;
    public Concept concept = null;
    public Preferences preference = null;
    public Relationship relationship = null;
    public UsersAssignment usersmanage = null;
    public GroupsAssignment groupsmanage = null;
    public Validation validation = null;
    public ImportOntology importData = null;
    public Export exportData = null;
    public Classification classification = null;
    public Statistic statistic = null;
    public About about = null;
    public Consistency consistency = null;
    public CommentViewer commentViewer = null;
    public LogViewer logViewer = null;
    public ResultPanel resultPanel = null;
    public RelationshipBrowser relationshipBrowser = null;
    public ConceptBrowser conceptBrowser = null;
    public String currentModule;
    
    public UserLogin uo;
    ArrayList<OntologyInfo> ontologies;

    private InitializeSystemData initData;
    private SuggestBoxAOSWB keyword = new SuggestBoxAOSWB(new MultiWordSuggestOracle());
    private ListBox filterBox;
    private HorizontalPanel lastSearchResultPanel = new HorizontalPanel();
    
    //public static ArrayList<String> defaultLang = new ArrayList<String>();
    public static boolean dataChanged = false;
    HashMap<String, String> menuMap = new HashMap<String, String>();
    
    public MainApp(final UserLogin userLoginObj)
    {
        uo = userLoginObj;
        modulePanel.clear();
        modulePanel.setSize("100%", "100%");

        AsyncCallback<InitializeSystemData> callback = new AsyncCallback<InitializeSystemData>() {
            public void onSuccess(InitializeSystemData results)
            {
                initData = (InitializeSystemData) results;
                languageCode = (ArrayList<LanguageCode>) initData.getLanguage();
                for (int i = 0; i < languageCode.size(); i++)
                {
                	LanguageCode langCode = languageCode.get(i);
                    languageDict.put(langCode.getLanguageCode().toLowerCase(), langCode.getLocalLanguage());
                }
                ModelConstants.loadConstants(initData.getModelConstants());
                ConfigConstants.loadConstants(initData.getConfigConstants());
                
                conceptNavigationHistoryMaxSize = ConfigConstants.CONCEPTNAVIGATIONHISTORYMAXSIZE;
                
                OWLStatusConstants.loadOwlStatusConstants(initData.getOwlStatusConstants());
                OWLActionConstants.loadConstants(initData.getOwlActionConstants());
                permissionTable = initData.getPermissionTable();
                
               // defaultLang.add("en");
                
                ontologies = initData.getOntology();
                userLogin = userLoginObj.getLoginname();
                userLanguage = userLoginObj.getLanguage();
                userOntology = userLoginObj.getOntology();
                userMenu = userLoginObj.getMenu();
                userId = Integer.parseInt(userLoginObj.getUserid());
                groupId = Integer.parseInt(userLoginObj.getGroupid());
                groupName = userLoginObj.getGroupname();
                userPreference = userLoginObj.getUsersPreference();
                
                userSelectedLanguage = userLoginObj.getUserSelectedLanguage();
                /*for(int i=0 ; i<defaultLang.size() ; i++)
                {
                    if (!userSelectedLanguage.contains(defaultLang.get(i)))
                        userSelectedLanguage.add(defaultLang.get(i));                                        
                }*/
                
                menuMap = Convert2Map(userMenu);

                QuickLinks quickLinks = new QuickLinks(null, false);
                quickLinks.showLoginOptions(userLoginObj.getLoginname(), userLoginObj.getGroupname());
                quickLinks.setOntologyPanel(getOntologyPanel());
                quickLinks.setStatusBar(getStatusBar());
                

                VerticalPanel top = new VerticalPanel();
                top.setHeight("100%");
                top.setStyleName("header-quickLinks");
                DOM.setStyleAttribute(top.getElement(), "borderBottom", "1px solid #8A8A8A");
                top.add(quickLinks);                
                top.setCellHeight(quickLinks, "30px");
                top.setCellVerticalAlignment(quickLinks, HasVerticalAlignment.ALIGN_TOP);

                // Search
                Spacer spacer = new Spacer("100%", "40px");
                HorizontalPanel search = new HorizontalPanel();
                search.setWidth("100%");
                search.add(spacer);
                search.add(getSearch());
                search.setCellWidth(spacer, "100%");
                search.setCellHorizontalAlignment(search.getWidget(1), HasHorizontalAlignment.ALIGN_RIGHT);
                search.setCellVerticalAlignment(search.getWidget(1), HasVerticalAlignment.ALIGN_MIDDLE);
                DOM.setStyleAttribute(search.getElement(), "marginTop", "10px");

                // Title
                HorizontalPanel appTitle = getAppTitle();
                //appTitle.setWidth("100%");
                Spacer titleSpacer1 = new Spacer("20px", "100%");
                Spacer titleSpacer2 = new Spacer("90%", "100%");

                HorizontalPanel title = new HorizontalPanel();
                DOM.setStyleAttribute(title.getElement(), "background", "transparent url('images/bg_grey2.gif') repeat-x 10px left");
                title.setStyleName("floating");
                title.setWidth("100%");
                title.add(titleSpacer1);
                title.add(appTitle);
                title.add(titleSpacer2);
                title.add(search);
                title.setCellWidth(titleSpacer2, "100%");
                title.setCellHeight(appTitle, "40px");
                title.setCellHeight(titleSpacer2, "50px");
                title.setCellHorizontalAlignment(search, HasHorizontalAlignment.ALIGN_RIGHT);
                title.setCellVerticalAlignment(search, HasVerticalAlignment.ALIGN_MIDDLE);

                HorizontalPanel empty = new HorizontalPanel();
                empty.add(new Spacer("100%", "40px"));
                empty.add(new HTML("&nbsp;"));
                empty.setWidth("100%");
                empty.setCellWidth(empty.getWidget(0), "100%");
                DOM.setStyleAttribute(empty.getElement(), "backgroundColor", "#C9C8C8");

                Footer footer = new Footer();

                panel.add(top);
                panel.add(title);
                panel.add(empty);
                panel.add(getToolBar());
                panel.add(modulePanel);
                panel.add(footer);
                panel.setSize("100%", "100%");
                panel.setCellHeight(top, "40px");
                panel.setCellHeight(modulePanel, "100%");
                panel.setCellHeight(empty, "40px");
                panel.setCellWidth(title, "100%");

                // If the application starts with no history token, start it off
                // in the 'HOME' state.
                String initToken = "Home";
                if (History.getToken() != null && History.getToken().length() > 0)
                {
                    initToken = History.getToken();
                }
                else
                {
                    if (userPreference.getUserId() != 0)
                    {
                    	initToken = userPreference.getInitialPage();
                    }   
                }
                goToModule(initToken);
                History.addValueChangeHandler(new ValueChangeHandler<String>() {
                    public void onValueChange(ValueChangeEvent<String> event)
                    {
                        goToModule(event.getValue());
                    }
                });
                loadBrowserWindow();
            }

            public void onFailure(Throwable caught)
            {
            }
        };
        Service.systemService.initData(uo, callback);
        initWidget(panel);
    }
    
    public void loadBrowserWindow()
    {
    	 relationshipBrowser = new RelationshipBrowser();
         conceptBrowser = new ConceptBrowser();
    }

    public static ArrayList<String[]> getLanguage()
    {
        ArrayList<LanguageCode> languageCodeList = MainApp.languageCode;
        ArrayList<String[]> languageList = new ArrayList<String[]>();
        for (int i = 0; i < languageCodeList.size(); i++)
        {
            String[] item = new String[2];
            LanguageCode languageCode = languageCodeList.get(i);
            item[1] = languageCode.getLanguageCode().toLowerCase();
            item[0] = languageCode.getLocalLanguage();
            languageList.add(item);
        }
        return languageList;
    }
	
	public static String getFullnameofLanguage(String langCode){
		
		if(languageDict.containsKey(langCode.toLowerCase())){
			return (String)languageDict.get(langCode.toLowerCase());
		}else{
			return  "-";
		}
	}

    public static native void close() /*-{
        $wnd.close();
    }-*/;

    public static native void openURL(String url) /*-{
        $wnd.open(url,'_blank','');
    }-*/;

    private HashMap<String, String> Convert2Map(ArrayList<String> list)
    {
        HashMap<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < list.size(); i++)
        {
            String item = (String) list.get(i);
            map.put(item, item);
        }
        return map;
    }
    
    /**
     * use for concept management and relationship management only. And use when
     * MainApp Object already have been created
     */
    public void goToConceptModuleWithInitTreeItem(String initURI, boolean isAddAction, int InfoTab)
    {
        if (modulePanel.getWidgetIndex(concept) != -1)
        {
            modulePanel.showWidget(modulePanel.getWidgetIndex(concept));
            // ConceptTree.treePanel.gotoItem(initURI,InfoTab);
            ((Concept) modulePanel.getWidget(modulePanel.getWidgetIndex(concept))).gotoItem(initURI, InfoTab);
            History.newItem("Concepts");
        }
        else
        {
            concept = new Concept(permissionTable, initURI, InfoTab);
            modulePanel.add(concept);
            modulePanel.showWidget(modulePanel.getWidgetIndex(concept));
            History.newItem("Concepts");
        }
    }

    public void goToClassificationModuleWithInitTreeItem(String targetItemURI,
            String schemeURI, int infoTab)
    {
        if (modulePanel.getWidgetIndex(classification) != -1)
        {
            modulePanel.showWidget(modulePanel.getWidgetIndex(classification));
            classification.scheme.gotoItem(schemeURI, targetItemURI);
            History.newItem("Classifications");
        }
        else
        {
            classification = new Classification(permissionTable, schemeURI, targetItemURI);
            modulePanel.add(classification);
            modulePanel.showWidget(modulePanel.getWidgetIndex(classification));
            History.newItem("Classifications");
        }
    }

    public void loadUser(String userID)
    {
        if (modulePanel.getWidgetIndex(usersmanage) != -1)
        {
            modulePanel.showWidget(modulePanel.getWidgetIndex(usersmanage));
            usersmanage.loadUser(userID);
            History.newItem("usersmanage");
        }
        else
        {
            usersmanage = new UsersAssignment();
            usersmanage.loadUser(userID);
            modulePanel.add(usersmanage);
            modulePanel.showWidget(modulePanel.getWidgetIndex(usersmanage));
            History.newItem("usersmanage");
        }
    }
    
    public void goToConceptBrowserModuleWithInitTreeItem(String targetItem)
    {
    	conceptBrowser.gotoItem(targetItem);
    }

    public void reloadConceptTree()
    {
        if (modulePanel.getWidgetIndex(concept) != -1)
        {
            ((Concept) modulePanel.getWidget(modulePanel.getWidgetIndex(concept))).reload();
        }
    }

    public void reloadClassificationTree()
    {
        if (modulePanel.getWidgetIndex(classification) != -1)
        {
            classification.scheme.reload(0, null);
        }
    }

    public void reloadValidation()
    {
        if (modulePanel.getWidgetIndex(validation) != -1)
        {
            ((org.fao.aoscs.client.module.validation.Validation) modulePanel.getWidget(modulePanel.getWidgetIndex(validation))).getValidator().reLoad();
        }
    }

    public void reloadRecentChanges()
    {
        if (modulePanel.getWidgetIndex(about) != -1)
        {
            ((org.fao.aoscs.client.module.document.About) modulePanel.getWidget(modulePanel.getWidgetIndex(about))).reLoad();
        }
    }

    /**
     * @param name 		Module name
     * @param params	Additional parameters when [name = "Concept"]
     * 					[0] : Concept URI
     * 					[1] : Concept detail tab index
     * 					[2] : Show non-preferred term option 
     */
    public void goToModule(String name, String... params)
    {     
    	
        if (name.equals("Search"))
        {
            iconContainer.deactivate();
        	if (search == null)
            {
                search = new Search(SearchOption.ADVANCED_SEARCH, null);
                modulePanel.add(search);
            }
            modulePanel.showWidget(modulePanel.getWidgetIndex(search) );
            
            Scheduler.get().scheduleDeferred(new Command() {
                public void execute()
                {
                	if (search != null)
                	{
                		if(search.optionTable !=null)
            			{
                			 search.optionTable.loadAdvSearchPanel();
            			}
                	}
                }
            });
        }
        else if (name.equals("Concepts"))
        {
            if (modulePanel.getWidgetIndex(concept) == -1 || concept == null){
            	if(params.length>0){
            		String uri = params[0];
            		if(params.length > 1){
        				int tab = Integer.parseInt(params[1]);
        				if(params.length > 2){
        					boolean showNonPreferedTerm = Boolean.parseBoolean(params[2]);
        					concept = new Concept(permissionTable, uri, tab, showNonPreferedTerm);
        				}
        				else
        					concept = new Concept(permissionTable, uri, tab);
        			}
            		else
            			concept = new Concept(permissionTable, uri, InfoTab.term);
            	}
            	else{
            		concept = new Concept(permissionTable);
            	}
                modulePanel.add(concept);
            }
            modulePanel.showWidget(modulePanel.getWidgetIndex(concept));
        }
        else if (name.equals("Import"))
        {
            if (modulePanel.getWidgetIndex(importData) == -1 || importData == null)
            {
                importData = new ImportOntology();
                modulePanel.add(importData);
            }
            modulePanel.showWidget(modulePanel.getWidgetIndex(importData));
        }
        else if (name.equals("Export"))
        {
            if (modulePanel.getWidgetIndex(exportData) == -1 || exportData == null)
            {
                exportData = new Export();
                modulePanel.add(exportData);
            }
            modulePanel.showWidget(modulePanel.getWidgetIndex(exportData));
        }
        else if (name.equals("Validation"))
        {
            if (modulePanel.getWidgetIndex(validation) == -1 || validation == null)
            {
                validation = new Validation();
                modulePanel.add(validation);
            }
            modulePanel.showWidget(modulePanel.getWidgetIndex(validation));
        }
        else if (name.equals("Consistency"))
        {
            if (modulePanel.getWidgetIndex(consistency) == -1 || consistency == null)
            {
                consistency = new Consistency();
                modulePanel.add(consistency);
            }
            modulePanel.showWidget(modulePanel.getWidgetIndex(consistency));
        }
        else if (name.equals("Classifications"))
        {
            if (modulePanel.getWidgetIndex(classification) == -1 || classification == null)
            {
                classification = new Classification(permissionTable);
                modulePanel.add(classification);
            }
            modulePanel.showWidget(modulePanel.getWidgetIndex(classification));
        }
        else if (name.equals("Statistics"))
        {
            if (modulePanel.getWidgetIndex(statistic) == -1 || statistic == null)
            {
                statistic = new Statistic();
                modulePanel.add(statistic);
            }
            modulePanel.showWidget(modulePanel.getWidgetIndex(statistic));
        }
        else if (name.equals("Relationships"))
        {
            if (modulePanel.getWidgetIndex(relationship) == -1 || relationship == null)
            {
                relationship = new Relationship(permissionTable);
                modulePanel.add(relationship);
            }
            modulePanel.showWidget(modulePanel.getWidgetIndex(relationship));
        }
        else if (name.equals("Users"))
        {
            if (modulePanel.getWidgetIndex(usersmanage) == -1 || usersmanage == null)
            {
                usersmanage = new UsersAssignment();
                modulePanel.add(usersmanage);
            }
            modulePanel.showWidget(modulePanel.getWidgetIndex(usersmanage));
        }
        else if (name.equals("Groups"))
        {
            if (modulePanel.getWidgetIndex(groupsmanage) == -1 || groupsmanage == null)
            {
                groupsmanage = new GroupsAssignment();
                modulePanel.add(groupsmanage);
            }
            modulePanel.showWidget(modulePanel.getWidgetIndex(groupsmanage));

        }
        else if (name.equals("Preferences"))
        {
            //if (modulePanel.getWidgetIndex(preference) == -1 || preference == null)
            {
                preference = new Preferences();
                modulePanel.add(preference);
            }
            modulePanel.showWidget(modulePanel.getWidgetIndex(preference));
        }
        else if (name.equals("Home"))
        {
            // if(modulePanel.getWidgetIndex(about)== -1 || about == null)
            {
                about = new About();
                modulePanel.add(about);
            }
            modulePanel.showWidget(modulePanel.getWidgetIndex(about));
        }
        else if (name.equals("Comments"))
        {
            if (modulePanel.getWidgetIndex(commentViewer) == -1 || commentViewer == null)
            {
                commentViewer = new CommentViewer();
                modulePanel.add(commentViewer);
            }
            modulePanel.showWidget(modulePanel.getWidgetIndex(commentViewer));
        }
        else if (name.equals("Logs"))
        {
            if (modulePanel.getWidgetIndex(logViewer) == -1 || logViewer == null)
            {
                logViewer = new LogViewer();
                modulePanel.add(logViewer);
            }
            modulePanel.showWidget(modulePanel.getWidgetIndex(logViewer));
        }
        iconContainer.activate(name);
        History.newItem(name);
        currentModule = name;
    }
    
    public void saveCurrentModule()
    {        
        if(currentModule.equals("Preferences"))
            preference.savePreference();
    }
    
    public HorizontalPanel getStatusBar()
    {
        HorizontalPanel statusBar = new HorizontalPanel();
        final LinkLabel rss = new LinkLabel("images/feedicon.png", constants.statusRSSTitle(), constants.statusRSS(), "quick-link");
        rss.setLabelStyle("quick-link-text");
        rss.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                openURL(GWT.getHostPageBaseURL()+"DownloadRSS?type=rss_2.0&ontologyId="
                        + MainApp.userOntology.getOntologyId());
            }
        });
        final LinkLabel glossary = new LinkLabel("images/glossary.png", constants.menuGlossary(), constants.menuGlossary(), "quick-link");
        glossary.setLabelStyle("quick-link-text");
        glossary.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
            	HelpUtility.openHelp("GLOSSARY");
            }
        });
        final LinkLabel help = new LinkLabel("images/help.png", constants.statusHelpTitle(), constants.statusHelp(), "quick-link");
        help.setLabelStyle("quick-link-text");
        help.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
            	HelpUtility.openHelp(History.getToken());
            }
        });
        final LinkLabel comment = new LinkLabel("images/comments.gif", constants.statusCommentsTitle(), constants.statusComments(), "quick-link");
        comment.setLabelStyle("quick-link-text");
        comment.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                if (commentDialog == null || !commentDialog.isLoaded)
                    commentDialog = new CommentDialogBox();
                commentDialog.show();
            }
        });
        HorizontalPanel hp = new HorizontalPanel();

        final LinkLabel preferences = new LinkLabel("images/preferences.png", constants.menuPreferences(), constants.menuPreferences(), "quick-link");
        preferences.setLabelStyle("quick-link-text");
        preferences.setStyleName("quick-link");
        preferences.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				goToModule("Preferences");
			}
		});
		
		final LinkLabel signOut = new LinkLabel("images/logout.png", constants.statusSignOut(), constants.statusSignOut(), "quick-link");
		signOut.setLabelStyle("quick-link-text");
		signOut.setStyleName("quick-link");
		signOut.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				new LogManager().endLog();
			}
		});
		HorizontalPanel admin = getAdminsiterMenu();
		AboutVocBenchMenu about = new AboutVocBenchMenu();
        HTML delimiter = new HTML("|");
        HTML delimiter2 = new HTML("|");
        //hp.add(comment);
        //hp.add(new HTML("&nbsp;"));
        //hp.add(glossary);
        //hp.add(new HTML("&nbsp;"));
        //hp.add(help);
		hp.add(admin);
		hp.add(new HTML("&nbsp;"));
		hp.add(delimiter2);
        hp.add(about);
        hp.add(new HTML("&nbsp;"));
        hp.add(delimiter);
        hp.add(rss);
        hp.add(new HTML("&nbsp;"));
        hp.add(preferences);
        hp.add(new HTML("&nbsp;"));
        hp.add(signOut);
        hp.setSpacing(3);
        hp.setCellVerticalAlignment(rss, HasVerticalAlignment.ALIGN_MIDDLE);
        hp.setCellVerticalAlignment(about, HasVerticalAlignment.ALIGN_MIDDLE);
        //hp.setCellVerticalAlignment(comment, HasVerticalAlignment.ALIGN_MIDDLE);
        //hp.setCellVerticalAlignment(glossary, HasVerticalAlignment.ALIGN_MIDDLE);
        //hp.setCellVerticalAlignment(help, HasVerticalAlignment.ALIGN_MIDDLE);
        hp.setCellVerticalAlignment(signOut, HasVerticalAlignment.ALIGN_MIDDLE);
        hp.setCellVerticalAlignment(admin, HasVerticalAlignment.ALIGN_MIDDLE);
        hp.setCellVerticalAlignment(preferences, HasVerticalAlignment.ALIGN_MIDDLE);
        hp.setCellVerticalAlignment(delimiter, HasVerticalAlignment.ALIGN_MIDDLE);
        hp.setCellVerticalAlignment(delimiter2, HasVerticalAlignment.ALIGN_MIDDLE);
        statusBar.add(hp);
        statusBar.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_RIGHT);
        return statusBar;
    }

    private HorizontalPanel getAppTitle()
    {
        HorizontalPanel appTitleContainer = new HorizontalPanel();
        HTML title = new HTML(constants.mainPageTitle());
        title.setStyleName("header-mini-title");
        //title.setWidth("100%");
        title.setWordWrap(false);
        
        HTML version = new HTML(constants.mainVersionAllCaps()+" "+ConfigConstants.DISPLAYVERSION);
        version.setStyleName("header-mini-version");
        version.setWordWrap(false);
        
        HorizontalPanel hp = new HorizontalPanel();
        //hp.setWidth("100%");
        hp.add(title);
        hp.add(new Spacer("10px", "100%"));
        hp.add(version);
        hp.add(new Spacer("20px", "100%"));
        hp.setCellVerticalAlignment(title, HasVerticalAlignment.ALIGN_MIDDLE);
        hp.setCellVerticalAlignment(version, HasVerticalAlignment.ALIGN_TOP);
        hp.setCellHorizontalAlignment(version, HasHorizontalAlignment.ALIGN_LEFT);
        appTitleContainer.setStyleName("header-mini");
        appTitleContainer.add(hp);
        return appTitleContainer;
    }
    
    private class SuggestBoxAOSWB extends SuggestBoxAOS {
		
		public SuggestBoxAOSWB(MultiWordSuggestOracle oracle){
			super(oracle);
		}
		
		public void onSubmit() {
			
			doSearch();
		}
	}
    
    private HorizontalPanel getSearch()
    {

    	keyword.setWidth("200px");
    	filterBox = new ListBox();
    	filterBox.addItem(constants.searchExactWord(), SearchParameterObject.EXACT_WORD);
    	if(!ConfigConstants.ISINDEXING)
		{
    		filterBox.addItem(constants.searchExactMatch(), SearchParameterObject.EXACT_MATCH);
		}
		filterBox.addItem(constants.searchContains(), SearchParameterObject.CONTAIN);
		filterBox.addItem(constants.searchStartsWith(), SearchParameterObject.START_WITH);
		filterBox.addItem(constants.searchEndsWith(), SearchParameterObject.END_WITH);
		if(ConfigConstants.ISINDEXING)
		{
			filterBox.addItem(constants.searchFuzzySearch(), SearchParameterObject.FUZZY_SEARCH);
		}

        Button btn = new Button(constants.searchGoButton());
        btn.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
           		doSearch();
            }
        });
        HorizontalPanel simpleSearch = new HorizontalPanel();
        simpleSearch.setWidth("100%");
        simpleSearch.add(filterBox);
        simpleSearch.add(new Spacer("5px", "100%"));
        simpleSearch.add(keyword);
        simpleSearch.add(new Spacer("5px", "100%"));
        simpleSearch.add(btn);
        simpleSearch.setCellVerticalAlignment(filterBox, HasVerticalAlignment.ALIGN_MIDDLE);
        simpleSearch.setCellVerticalAlignment(keyword, HasVerticalAlignment.ALIGN_MIDDLE);
        simpleSearch.setCellVerticalAlignment(btn, HasVerticalAlignment.ALIGN_MIDDLE);

        HTML adv = new HTML(constants.searchShowAdv());
        adv.setStyleName("advanced-search");
        adv.setWordWrap(false);
        adv.setWidth("100%");

        adv.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                iconContainer.deactivate();
            	if (modulePanel.getWidgetIndex(search) == -1 || search == null)
                {
                    search = new Search(SearchOption.ADVANCED_SEARCH, null);
                    modulePanel.add(search);
                }
                modulePanel.showWidget(modulePanel.getWidgetIndex(search) );
                Scheduler.get().scheduleDeferred(new Command() {
                    public void execute()
                    {
                    	if (search != null)
                    	{
                    		if(search.optionTable !=null)
                			{
                    			 search.optionTable.loadAdvSearchPanel();
                			}
                    	}
                    }
                });
            }
        });

        Spacer spacer = new Spacer("100%", "100%");

        HorizontalPanel hp = new HorizontalPanel();
        hp.setSize("100%", "30px");
        hp.add(spacer);
        hp.setCellWidth(spacer, "100%");
        hp.add(simpleSearch);
        hp.add(new Spacer("5px", "100%"));
        hp.add(adv);
		hp.add(lastSearchResultPanel);
		hp.add(new Spacer("15px", "100%"));
        
        //DOM.setStyleAttribute(hp.getElement(), "paddingRight", "10px");
        //DOM.setStyleAttribute(hp.getElement(), "paddingLeft", "10px");
        hp.setCellVerticalAlignment(simpleSearch, HasVerticalAlignment.ALIGN_MIDDLE);
        hp.setCellVerticalAlignment(adv, HasVerticalAlignment.ALIGN_MIDDLE);
        hp.setCellVerticalAlignment(lastSearchResultPanel, HasVerticalAlignment.ALIGN_MIDDLE);
        return hp;
    }
    
    public void addSearchLastResultPanel()
    {
    	lastSearchResultPanel.clear();
    	HTML viewSearchResult = new HTML(constants.buttonViewSearchResult());
		viewSearchResult.setTitle(constants.buttonViewSearchResult());
		viewSearchResult.setWordWrap(false);
		viewSearchResult.setStyleName("advanced-search");
		viewSearchResult.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (modulePanel.getWidgetIndex(search) == -1 || search == null)
                {
                    search = new Search(SearchOption.SEARCH_RESULT, null);
                    modulePanel.add(search);
                }
				else
		        	search.optionTable.loadSearchResultPanel(null);
                modulePanel.showWidget(modulePanel.getWidgetIndex(search) );
			}
		});
		
		lastSearchResultPanel.add(new Spacer("5px", "100%"));
		lastSearchResultPanel.add(new Spacer("5px", "100%", "|"));
		lastSearchResultPanel.add(new Spacer("5px", "100%"));
		lastSearchResultPanel.add(viewSearchResult);
		lastSearchResultPanel.setCellVerticalAlignment(viewSearchResult, HasVerticalAlignment.ALIGN_MIDDLE);
    }

    private void doSearch()
    {
        if(keyword.getText().length()>0)
        {
	        final SearchParameterObject searchObj = new SearchParameterObject();
	        searchObj.setRegex(filterBox.getValue(filterBox.getSelectedIndex()));
	        searchObj.clearSelectedLanguage();
	        searchObj.setKeyword(keyword.getText());
	        for(String lang : languageDict.keySet())
	        {
	            searchObj.addSelectedLanguage(lang.toLowerCase());
	        }
	        
	        if (search == null)
	        {
	            search = new Search(SearchOption.SEARCH_RESULT, searchObj);
	            modulePanel.add(search);
	        }
	        else
	        {
	        	Scheduler.get().scheduleDeferred(new Command() {
	                public void execute()
	                {
	                	if (search != null)
	                	{
	                		if(search.optionTable !=null)
	            			{
	                			 search.optionTable.loadSearchResultPanel(searchObj);
	            			}
	                	}
	                }
	            });
	        	
	        }
	        modulePanel.showWidget(modulePanel.getWidgetIndex(search));
        }
		
    }

    private HorizontalPanel getToolBar()
    {
        iconContainer = new ToolBarContainer(this);
        iconContainer.setSpacing(3);
        iconContainer.setHeight("100%");
        // Home
        iconContainer.addMenu(constants.homeRecentChanges(), constants.homeRecentChanges(), "Home");
        // Action
        if (menuMap.containsKey("Concepts"))
        {
            iconContainer.addMenu(constants.toolbarConcepts(), constants.toolbarConceptsTitle(), "Concepts");
        }
        else
        {
        	iconContainer.disableMenu(constants.toolbarConcepts(), constants.toolbarConceptsTitle(), "Concepts");
        }
        
        if (menuMap.containsKey("Relationships"))
        {
            iconContainer.addMenu(constants.toolbarRelationships(), constants.toolbarRelationshipsTitle(), "Relationships");
        }
        else
        {
        	iconContainer.disableMenu(constants.toolbarRelationships(), constants.toolbarRelationshipsTitle(), "Relationships");
        }
        
        /*if (menuMap.containsKey("Classifications"))
        {
            iconContainer.addMenu(constants.toolbarSchemes(), constants.toolbarSchemesTitle(), "Classifications");
        }
        else
        {
        	iconContainer.disableMenu(constants.toolbarSchemes(), constants.toolbarSchemesTitle(), "Classifications");
        }
        */
        if (menuMap.containsKey("Validation"))
        {
            iconContainer.addMenu(constants.toolbarValidation(), constants.toolbarValidationTitle(), "Validation");
        }
        else
        {
        	iconContainer.disableMenu(constants.toolbarValidation(), constants.toolbarValidationTitle(), "Validation");
        }
        /*
        if (menuMap.containsKey("Consistency"))
        {
            iconContainer.addMenu(constants.toolbarConsistency(), constants.toolbarConsistencyTitle(), "Consistency");
        }
        else
        {
        	iconContainer.disableMenu(constants.toolbarConsistency(), constants.toolbarConsistencyTitle(), "Consistency");
        }
         
        if (menuMap.containsKey("Import"))
        {
            iconContainer.addMenu(constants.toolbarImport(), constants.toolbarImportTitle(), "Import");
        }
        else
        {
        	iconContainer.disableMenu(constants.toolbarImport(), constants.toolbarImportTitle(), "Import");
        }
         */
        if (menuMap.containsKey("Export"))
        {
            iconContainer.addMenu(constants.toolbarExport(), constants.toolbarExportTitle(), "Export");
        }
        else
        {
        	iconContainer.disableMenu(constants.toolbarExport(), constants.toolbarExportTitle(), "Export");
        }

        if (menuMap.containsKey("Statistics"))
        {
            iconContainer.addMenu(constants.toolbarStatistics(), constants.toolbarStatisticsTitle(), "Statistics");
        }
        else
        {
        	iconContainer.disableMenu(constants.toolbarStatistics(), constants.toolbarStatisticsTitle(), "Statistics");
        }

        for (int i = 0; i < iconContainer.getWidgetCount(); i++)
        {
            iconContainer.setCellVerticalAlignment(iconContainer.getWidget(i), HasVerticalAlignment.ALIGN_MIDDLE);
            iconContainer.setCellHeight(iconContainer.getWidget(i), "100%");
        }

        LinkLabel fb = new LinkLabel(null, constants.menuLanguage(), constants.menuLanguage());
        fb.setLabelStyle("toolbar-link");
        fb.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent arg0){
				if (langFilter == null || !langFilter.isLoaded)
	                langFilter = new LanguageFilter(MainApp.this);
	            langFilter.show();
				
			}
		});
        
        LinkLabel conceptNavigationHistoryButton = new LinkLabel(null, constants.conceptNavigationHistory(), constants.conceptNavigationHistory());
        conceptNavigationHistoryButton.setLabelStyle("toolbar-link");
        conceptNavigationHistoryButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
				if (conceptNavigationHistory == null || !conceptNavigationHistory.isLoaded)
					conceptNavigationHistory = new ConceptNavigationHistory(conceptNavigationHistoryList);
				conceptNavigationHistory.show();
			}
		});
        
        Spacer s1 = new Spacer("10px", "30px");
        Spacer s2 = new Spacer("10px", "30px");
        
        HorizontalPanel toolbarPanel = new HorizontalPanel();
        toolbarPanel.setStyleName("menuBar");
        toolbarPanel.setHeight("30px");
        toolbarPanel.add(iconContainer);        
        toolbarPanel.add(conceptNavigationHistoryButton);
        toolbarPanel.add(s1);
        toolbarPanel.add(fb);
        toolbarPanel.add(s2);
        toolbarPanel.setCellHeight(iconContainer, "100%");
        
        
        toolbarPanel.setCellHorizontalAlignment(fb, HasHorizontalAlignment.ALIGN_RIGHT);
        toolbarPanel.setCellVerticalAlignment(fb, HasVerticalAlignment.ALIGN_MIDDLE);
        
        toolbarPanel.setCellHorizontalAlignment(conceptNavigationHistoryButton, HasHorizontalAlignment.ALIGN_RIGHT);
        toolbarPanel.setCellVerticalAlignment(conceptNavigationHistoryButton, HasVerticalAlignment.ALIGN_MIDDLE);
        
        toolbarPanel.setCellWidth(iconContainer, "100%");
        toolbarPanel.setCellWidth(fb, "100%");
        toolbarPanel.setCellWidth(conceptNavigationHistoryButton, "100%");
        
        toolbarPanel.setCellHorizontalAlignment(s1, HasHorizontalAlignment.ALIGN_RIGHT);
        toolbarPanel.setCellHorizontalAlignment(s2, HasHorizontalAlignment.ALIGN_RIGHT);
        
        
        return toolbarPanel;
    }

    public static boolean checkDeprecated(String status)
    {
        boolean chk = true;
        if (MainApp.userPreference.isHideDeprecated())
        {
            if (/*
                 * status.equals(OWLStatusConstants.PROPOSED_DEPRECATED_STRINGVALUE
                 * ) ||
                 */status.equals(OWLStatusConstants.DEPRECATED))
                chk = false;
        }
        return chk;
    }

    public HorizontalPanel getAdminsiterMenu()
    {

        // FULL MENU ITEM OF USER
    	LinkedHashMap<String, String> user = new LinkedHashMap<String, String>();
        user.put("Users", constants.menuUsers());
        user.put("Groups", constants.menuGroups());
        //user.put("Comments", constants.menuComments());
        //user.put("Logs", constants.menuLogViewer());

        MenuBarAOS adminTab = new MenuBarAOS();

        for (final String item: user.keySet())
        {
        	if (userMenu.contains(item))
            {
            	adminTab.addMenuItem((String) user.get(item), false, true, new Command() {
                    public void execute()
                    {
                        //if(!dataChanged)
                            goToModule(item);
                        /*else{
                            FormDialogBox fdb = new FormDialogBox("Save my changes","Don't save");
                            fdb.setWidth("440px");                                     
                            fdb.addWidget(new HTML("Do you want to save your changes?"));
                            fdb.setText("Save before exit?");
                            fdb.show();
                            fdb.submit.addClickHandler(new ClickHandler()
                            {   public void onClick(ClickEvent event)
                                {
                                    MainApp.dataChanged = false;
                                    goToModule(item);      
                                }
                            });                 
                            fdb.cancel.addClickHandler(new ClickHandler()
                            {
                                public void onClick(ClickEvent event)
                                {
                                    MainApp.dataChanged = false;
                                    goToModule(item);                      
                                }
                            });
                        }*/
                    }
                });
            }
            else
            {
            	adminTab.addMenuItem((String) user.get(item), false, false, null);
            }
        }
        MenuBar menu = new MenuBar();
        menu.setAutoOpen(true);
        menu.ensureDebugId("cwMenuBar");
        menu.setAnimationEnabled(true);
        menu.setSize("100%", "100%");

        HorizontalPanel adminPanel = new HorizontalPanel();
        {
            adminTab.setMainLabel(constants.menuAdministration());
            adminPanel.add(adminTab);
            //adminPanel.setCellVerticalAlignment(adminPanel.getWidget(1), HasVerticalAlignment.ALIGN_MIDDLE);
            adminPanel.setCellVerticalAlignment(adminTab, HasVerticalAlignment.ALIGN_MIDDLE);
            adminPanel.setCellHorizontalAlignment(adminTab, HasHorizontalAlignment.ALIGN_LEFT);
        }
        return adminPanel;
    }
    

    public HorizontalPanel getOntologyPanel()
    {
        MenuBarAOS menuBar = new MenuBarAOS();
        String selected = uo.getOntology().getOntologyName();
        for (int i = 0; i < ontologies.size(); i++)
        {
            if (!ontologies.get(i).getOntologyName().equals(selected))
            {
                final String temp = ontologies.get(i).getOntologyName();
                String desc = ontologies.get(i).getOntologyDescription();
                Command cmd = new Command() {
                    public void execute()
                    {
                        String selected = temp;
                        for (int i = 0; i < ontologies.size(); i++)
                        {
                            if (ontologies.get(i).getOntologyName().equals(selected))
                            {
                                uo.setOntology(ontologies.get(i));
                                AsyncCallback<Object> cbkauthorize = new AsyncCallback<Object>() {
                                    public void onSuccess(Object result)
                                    {
                               		 	History.newItem(null);
                                    	MainApp mainApp = new MainApp(uo);
                                        new LogManager().startLog("" + uo.getUserid());
                                        mainApp.setWidth("100%");
                                        Main.replaceRootPanel(mainApp);
                                    }

                                    public void onFailure(Throwable caught)
                                    {
                                        Window.alert(constants.selPrefFail());
                                    }
                                };
                                Service.systemService.setSessionValue(USERLOGINOBJECT_SESSIONNAME, uo, cbkauthorize);
                                break;
                            }
                        }
                        /*MainApp mainApp = new MainApp(uo);
                        new LogManager().startLog("" + uo.getUserid());
                        mainApp.setWidth("100%");
                        Main.replaceRootPanel(mainApp);*/
                    }
                };
                menuBar.addMenuItem(temp,true,cmd,desc);                
            }
        }
        menuBar.setMainLabel(selected);
        return menuBar;
    }

    public static int getBodyPanelHeight()
    {
        int height = Window.getClientHeight();
        height = height - 150;
        return height;
    }

    public static int getBodyPanelWidth()
    {
        int width = Window.getClientWidth();
        width = width - 20;
        return width;
    }
    
    public static void addToConceptNavigationHistoryList(ConceptObject cObj)
    {
    	
		if(conceptNavigationHistoryList.size()>0)
		{
			List<Long> labelKeys = new ArrayList<Long>(conceptNavigationHistoryList.keySet()); 
			Collections.sort(labelKeys);
			
			long lastTime = labelKeys.get(conceptNavigationHistoryList.size()-1);
			ConceptObject lastCObj = conceptNavigationHistoryList.get(lastTime);
			
			if(!cObj.getUri().equals(lastCObj.getUri()))
			{
				if(conceptNavigationHistoryList.size()>=conceptNavigationHistoryMaxSize)
		    	{
					conceptNavigationHistoryList.remove(labelKeys.get(0));
		    	}
			}
			else
			{
				conceptNavigationHistoryList.remove(lastTime);
			}
		}
		conceptNavigationHistoryList.put(new Date().getTime(), cObj);
    }
    
}
