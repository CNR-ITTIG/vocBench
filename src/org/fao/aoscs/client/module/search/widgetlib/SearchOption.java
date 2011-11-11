package org.fao.aoscs.client.module.search.widgetlib;

import java.util.ArrayList;
import java.util.Iterator;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.classification.widgetlib.OlistBox;
import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.widgetlib.shared.dialog.AddValue;
import org.fao.aoscs.client.widgetlib.shared.dialog.DialogBoxAOS;
import org.fao.aoscs.client.widgetlib.shared.dialog.RelationshipBrowser;
import org.fao.aoscs.client.widgetlib.shared.label.HelpPanel;
import org.fao.aoscs.client.widgetlib.shared.label.LabelAOS;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabel;
import org.fao.aoscs.client.widgetlib.shared.panel.BodyPanel;
import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;
import org.fao.aoscs.client.widgetlib.shared.panel.TitleBodyWidget;
import org.fao.aoscs.domain.InitializeSearchData;
import org.fao.aoscs.domain.LanguageCode;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.SearchParameterObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SearchOption extends Composite{
		
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	private DeckPanel panel = new DeckPanel();
	
	private BodyPanel vvp;
	
	private ResultPanel rPanel;
	
	private String browseImgPath = "images/browseButton3-grey.gif";
	
	private String trashImgPath = "images/trash-grey.gif";
	
	private SearchParameterObject searchObj = new SearchParameterObject();
	
	private ListBox filterBox = new ListBox();
	
	private SuggestBoxAOSWB keyword = new SuggestBoxAOSWB(new MultiWordSuggestOracle());
	
	private CheckBox opt1 = new CheckBox(constants.searchCaseSensitive());
	
	private CheckBox opt2 = new CheckBox(constants.searchIncludeNotes());
	
	private CheckBox opt3 = new CheckBox(constants.searchPreferredTermsOnly());

	private TextBox termCode = new TextBox();
	
	private OlistBox termCodeRepository = new OlistBox();
	
	private ListBox  status = new ListBox();

	private ListBox  scheme = new ListBox();
	
	private LabelAOS relationshipLabel = new LabelAOS("","");
	
	private VerticalPanel conceptAttributePanel = new VerticalPanel();
	
    private VerticalPanel termAttributePanel = new VerticalPanel();
	
	private OlistBox langlistCS = new OlistBox(true);
	
	private InitializeSearchData initData = new InitializeSearchData();
	
	private LanguageDataDialog newlangDialog;

	private IndexingDialog indexingDialog;
	
	public static String ADVANCED_SEARCH = "ADVANCED_SEARCH";
	
	public static String SEARCH_RESULT = "SEARCH_RESULT";
	
	public SearchOption(InitializeSearchData initData, String show, SearchParameterObject searchObj)
	{
		this.initData = initData;
		loadAdvancedSearch();
		loadResultPanel();
		panel.setSize("100%", "100%");
		panel.add(vvp);
		panel.add(rPanel);
		
		if(show.equals(ADVANCED_SEARCH))
		{
			panel.showWidget(panel.getWidgetIndex(vvp));
		}
		else
		{
			loadSearchResultPanel(searchObj);
		}
		initWidget(panel);		
	}
	
	public void loadResultPanel()
	{
		rPanel = new ResultPanel();
		rPanel.setSize("100%", "100%");
        final VerticalPanel previewPanel = new VerticalPanel();
        previewPanel.setSize("100%", "100%");
        previewPanel.add(rPanel);
        previewPanel.setCellHeight(rPanel, "100%");

        final VerticalPanel dpp = new VerticalPanel();
        dpp.setSize("100%", "100%");
        dpp.add(previewPanel);
        dpp.setCellHeight(previewPanel, "100%");

        final VerticalPanel vp = new VerticalPanel();
        vp.setSize("100%", "100%");
        vp.setSpacing(10);
        vp.add(dpp);
        vp.setCellHorizontalAlignment(dpp, HasHorizontalAlignment.ALIGN_CENTER);
        vp.setCellHeight(dpp, "100%");
        vp.setSize(Window.getClientWidth() - 35 + "px", Window.getClientHeight()
                - 105 - 55 + "px");
        Window.addResizeHandler(new ResizeHandler() {
            public void onResize(ResizeEvent event)
            {
                vp.setSize(Window.getClientWidth() - 35 + "px", Window.getClientHeight()
                        - 105 - 55 + "px");
            }
        });
	}
	
	public void filterByLanguage()
	{	
		rPanel.filterByLanguage();
	}
	
	public void loadAdvancedSearch()
	{			
		VerticalPanel aspPanel = getAdvanceSearchPanel();
		Widget lsPanel = getLanguageSelect();
		
		HorizontalPanel advsearchOptions = new HorizontalPanel();
		advsearchOptions.setHeight("100%");
		advsearchOptions.setSpacing(10);
		advsearchOptions.setVisible(true);
		advsearchOptions.add(aspPanel);
		advsearchOptions.add(lsPanel);
		advsearchOptions.setCellHorizontalAlignment(aspPanel, HasHorizontalAlignment.ALIGN_CENTER);
		advsearchOptions.setCellHorizontalAlignment(lsPanel, HasHorizontalAlignment.ALIGN_CENTER);
		advsearchOptions.setCellVerticalAlignment(lsPanel, HasVerticalAlignment.ALIGN_TOP);
		DOM.setStyleAttribute(advsearchOptions.getElement(), "border", "1px solid #F59131");
		
		HorizontalPanel infoTitle = new HorizontalPanel();
		infoTitle.add(new HTML(constants.searchAdvSearch()));
		infoTitle.setStyleName("loginTitle");
		
		VerticalPanel advsearchPanel = new VerticalPanel();
		advsearchPanel.setSpacing(10);
		advsearchPanel.setVisible(true);
		advsearchPanel.add(infoTitle);
		advsearchPanel.add(advsearchOptions);
		advsearchPanel.setCellHorizontalAlignment(infoTitle, HasHorizontalAlignment.ALIGN_CENTER);
		advsearchPanel.setCellHorizontalAlignment(advsearchOptions, HasHorizontalAlignment.ALIGN_CENTER);
		advsearchPanel.setCellVerticalAlignment(infoTitle, HasVerticalAlignment.ALIGN_TOP);
		advsearchPanel.setCellHeight(advsearchOptions, "100%");

		HorizontalPanel simpleSearchTopPanel = new HorizontalPanel();
		simpleSearchTopPanel.setSpacing(5);
		simpleSearchTopPanel.add(this.getRegexOption());
		simpleSearchTopPanel.add(this.getKeywordOption());	
		simpleSearchTopPanel.add(this.getSearchButton());
		simpleSearchTopPanel.add(this.getClearButton());
		if(MainApp.groupId == 1 ? true : false)
			simpleSearchTopPanel.add(this.getIndexButton());
		
		VerticalPanel simpleSearchPanel = new VerticalPanel();
		simpleSearchPanel.setSpacing(10);
		simpleSearchPanel.add(simpleSearchTopPanel);
		simpleSearchPanel.add(this.getFilterOption());
		simpleSearchPanel.setCellHorizontalAlignment(simpleSearchTopPanel, HasHorizontalAlignment.ALIGN_CENTER);
		simpleSearchPanel.setCellHorizontalAlignment(simpleSearchPanel.getWidget(1), HasHorizontalAlignment.ALIGN_CENTER);

		// simple and advanced search panels 
		VerticalPanel searchPanel = new VerticalPanel();
		searchPanel.setSize("100%", "100%");
		searchPanel.add(simpleSearchPanel);
		searchPanel.add(advsearchPanel);
		searchPanel.setCellHorizontalAlignment(simpleSearchPanel, HasHorizontalAlignment.ALIGN_CENTER);
		searchPanel.setCellHorizontalAlignment(advsearchPanel, HasHorizontalAlignment.ALIGN_CENTER);
		searchPanel.setCellVerticalAlignment(advsearchPanel, HasVerticalAlignment.ALIGN_TOP);
		searchPanel.setCellHeight(advsearchPanel, "100%");
			
		vvp = new BodyPanel(constants.searchShowAdv() , searchPanel , null, MainApp.getBodyPanelWidth()+17, MainApp.getBodyPanelHeight()+5);

	}
	
	public void loadAdvSearchPanel()
	{
		panel.showWidget(panel.getWidgetIndex(vvp));
	}
	
	public void loadSearchResultPanel(SearchParameterObject searchObj)
	{
		panel.showWidget(panel.getWidgetIndex(rPanel));
		if(searchObj!=null)	rPanel.search(searchObj);
	}
	
	private ListBox getRegexOption()
	{
		filterBox = new ListBox();
		filterBox.addItem(constants.searchExactWord(), SearchParameterObject.EXACT_WORD);
		if(!ConfigConstants.ISINDEXING)
		{
			filterBox.addItem(constants.searchExactMatch(), SearchParameterObject.EXACT_MATCH);
		}
		filterBox.addItem(constants.searchContains(), SearchParameterObject.CONTAIN);
		filterBox.addItem(constants.searchStartsWith(), SearchParameterObject.START_WITH);
		filterBox.addItem(constants.searchEndsWith(), SearchParameterObject.END_WITH);
		filterBox.setSelectedIndex(0);
		searchObj.setRegex(filterBox.getValue(filterBox.getSelectedIndex()));
		if(ConfigConstants.ISINDEXING)
		{
			filterBox.addItem(constants.searchFuzzySearch(), SearchParameterObject.FUZZY_SEARCH);
		}
		filterBox.addChangeHandler(new ChangeHandler(){
			public void onChange(ChangeEvent event){
				searchObj.setRegex(filterBox.getValue(filterBox.getSelectedIndex()));
			}
		});
		return filterBox;
	}
	
	private class SuggestBoxAOSWB extends SuggestBoxAOS {
		
		public SuggestBoxAOSWB(MultiWordSuggestOracle oracle){
			super(oracle);
		}
		
		public void onSubmit() {
			
			if(SearchOption.this.keyword.getText().length()!=0)
				searchObj.setKeyword(SearchOption.this.keyword.getText());				
			else
				searchObj.setKeyword("");
			if(SearchOption.this.termCode.getText().length()!=0)
				searchObj.setTermCode(SearchOption.this.termCode.getText());
			
			loadSearchResultPanel(searchObj);
		}
	}
	
	
	private SuggestBox getKeywordOption()
	{   
		return keyword;
	}
	
	private Button getSearchButton()
	{
		Button btn = new Button(constants.searchButton());
		btn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(keyword.getText().length()!=0)
					searchObj.setKeyword(keyword.getText());				
				else
					searchObj.setKeyword("");
				if(SearchOption.this.termCode.getText().length()!=0){
				    if(termCodeRepository.getSelectedIndex()>0)
				        searchObj.setTermCode(SearchOption.this.termCode.getText());
				    else{
				        Window.alert(constants.searchTermCodeRepositoryWarn());
				        return;
				    }
				}	
				loadSearchResultPanel(searchObj);
			}
		});			
		return btn;
	}

  /*private Button getSearchButton(final CheckBox showAdvanceOption,final HorizontalPanel advancePanel)
	{
		Button btn = new Button(constants.searchButton());
		btn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(SearchOption.this.keyword.getText().length()!=0){
					searchObj.setKeyword(SearchOption.this.keyword.getText());
				}
				else
					searchObj.setKeyword("");
				
				if(SearchOption.this.termCode.getText().length()!=0){
					searchObj.setTermCode(SearchOption.this.termCode.getText());
				}
				advancePanel.setVisible(false);
				showAdvanceOption.setValue(false);
				rPanel.search(searchObj);
			}
		});			
		return btn;
	}
*/	
	
	private Button getClearButton()
	{
		Button btn = new Button(constants.buttonClear());
		btn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				searchObj = new SearchParameterObject();
				searchObj.clear();
				if(filterBox.getItemCount()>0)	filterBox.setSelectedIndex(0);
				keyword.setText("");
				opt1.setValue(false);
				opt2.setValue(false);
				opt3.setValue(false);
				relationshipLabel.setText("", "");
				if(termCodeRepository.getItemCount()>0)	termCodeRepository.setSelectedIndex(0);
				termCode.setText("");
				if(status.getItemCount()>0)	status.setSelectedIndex(0);
				if(scheme.getItemCount()>0)	scheme.setSelectedIndex(0);
				conceptAttributePanel.clear();
				termAttributePanel.clear();
				loadLanglistCS();
				panel.showWidget(panel.getWidgetIndex(vvp));				
			}
			
		});	
		return btn;
	}
	
	private Widget getIndexButton()
	{
		boolean chk = false;//ConfigConstants.ISINDEXING
		if(chk)
		{
			Button btn = new Button(constants.buttonIndex());
			btn.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					
					if(Window.confirm(constants.searchIndexConfirm()))
					{
						if(indexingDialog == null || !indexingDialog.isLoaded)
							indexingDialog = new IndexingDialog();
						indexingDialog.startIndex();
					}
				}
			});	
			return btn;
		}
		else 
			return new HTML("");
	}
	
	private HorizontalPanel getFilterOption()
	{
		HorizontalPanel hp = new HorizontalPanel();
		
		opt1 = new CheckBox(constants.searchCaseSensitive());
		opt1.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(opt1.getValue()){
					searchObj.setCaseSensitive(true);
				}else{
					searchObj.setCaseSensitive(false);
				}
			}
		});	
		if(ConfigConstants.ISINDEXING)
		{
			opt1.setVisible(false);
		}
		
		
		opt2 = new CheckBox(constants.searchIncludeNotes());
		opt2.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(opt2.getValue()){
					searchObj.setIncludeNotes(true);
				}else{
					searchObj.setIncludeNotes(false);
				}
				SearchOption.this.keyword.setIncludeNotes(opt2.getValue());
			}
		});
		
		opt3 = new CheckBox(constants.searchPreferredTermsOnly());
        opt3.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if(opt3.getValue()){
                    searchObj.setOnlyPreferredTerm(true);
                }else{
                    searchObj.setOnlyPreferredTerm(false);
                }
            }
        });
		
		
		hp.add(new HTML("&nbsp;&nbsp;&nbsp;"));
		hp.add(opt1);
		if(!ConfigConstants.ISINDEXING)
		{
			hp.add(new HelpPanel(constants.searchHelpCaseSensitive()));
			hp.add(new HTML("&nbsp;&nbsp;"));
		}
		hp.add(opt2);
		hp.add(new HelpPanel(constants.searchHelpIncludeDescription()));
		hp.add(new HTML("&nbsp;&nbsp;"));
		hp.add(opt3);
		hp.add(new HelpPanel(constants.searchHelpSearchPreferredOnly()));
		hp.add(new HTML("&nbsp;&nbsp;&nbsp;"));
		return hp ;
	}
	
	private VerticalPanel getAdvanceSearchPanel()
	{		
		Grid table = new Grid(6,2);
		table.setWidth("580px");
		table.getColumnFormatter().setWidth(0, "130px");
		table.setCellSpacing(6);
		table.setCellPadding(1);		
		
		for (int j = 0; j < table.getRowCount(); j++) {
			DOM.setStyleAttribute(table.getCellFormatter().getElement(j, 0),"backgroundColor", "#F4F4F4");
		//	DOM.setStyleAttribute(table.getCellFormatter().getElement(j, 0),"padding", "3px");
			DOM.setStyleAttribute(table.getCellFormatter().getElement(j, 1),"backgroundColor", "F4F4F4");
			DOM.setStyleAttribute(table.getCellFormatter().getElement(j, 0),"border", "1px solid #E8E8E8");
			DOM.setStyleAttribute(table.getCellFormatter().getElement(j, 1),"border", "1px solid #E8E8E8");			
		}
		
		table.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
		
		table.setWidget(0, 0, new HTML(constants.searchRelationship()));
		table.setWidget(1, 0, new HTML(constants.searchTermCode()));
		table.setWidget(2, 0, new HTML(constants.searchStatus()));
		table.setWidget(3, 0, new HTML(constants.searchScheme()));		
		table.setWidget(4, 0, new HTML(constants.searchConceptAttribute()));
		table.setWidget(5, 0, new HTML(constants.searchTermAttribute()));
		
		table.setWidget(0, 1, this.getRelationPanel());		
		table.setWidget(1, 1, this.getTermCodePanel());
		table.setWidget(2, 1, this.getStatus());
		table.setWidget(3, 1, this.getScheme());
		table.setWidget(4, 1, this.getConceptPropertyPanel());
		table.setWidget(5, 1, this.getTermPropertyPanel());
		
		VerticalPanel masterBox = new VerticalPanel();
		masterBox.setWidth("100%");		
		masterBox.add(table);
		
		// +++ Details Decorator Panel
		VerticalPanel dpanel = new VerticalPanel();
		dpanel.add(masterBox);
		
		return dpanel;
	}
	
	private Widget getLanguageSelect()
	{
		loadLanglistCS();

		LinkLabel btnaddlang = new LinkLabel("images/add-grey.gif");
		LinkLabel btnremovelang = new LinkLabel("images/delete-grey.gif");

		// LANGUAGE LIST			
		
		HorizontalPanel hpnbtnlang = new HorizontalPanel();
		hpnbtnlang.setSpacing(3);
		hpnbtnlang.add(btnaddlang);
		hpnbtnlang.add(btnremovelang);  
		btnaddlang.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event) {
				if(newlangDialog == null || !newlangDialog.isLoaded)
					newlangDialog = new LanguageDataDialog();
				newlangDialog.show();	
			}				
		});
		

		btnremovelang.addClickHandler(new ClickHandler()
		{
			
			public void onClick(ClickEvent event) {
				if((Window.confirm(constants.searchRemoveLang()))==false)
				{
					return;
				}
				if(langlistCS.getSelectedIndex()==-1)
				{
					Window.alert(constants.searchNoLangSelect());
					return;
				}
				for(int i = langlistCS.getItemCount() -1;i>=0; i--)
				{
					if( langlistCS.isItemSelected(i))
					{
						langlistCS.removeItem(i);
					}
				}
				searchObj.clearSelectedLanguage();
				for(int i=0;i<langlistCS.getItemCount();i++)
				{						
					searchObj.addSelectedLanguage(((LanguageCode)langlistCS.getObject(i)).getLanguageCode().toLowerCase());
				}	
			}	
		});
		
		HorizontalPanel buttonBarLang = new HorizontalPanel();
		
		buttonBarLang.setSpacing(5);
		buttonBarLang.add(new HelpPanel(constants.searchHelpLanguage()));
		buttonBarLang.add(btnaddlang);
		buttonBarLang.add(btnremovelang);
		buttonBarLang.setCellVerticalAlignment(btnaddlang, HasVerticalAlignment.ALIGN_MIDDLE);
		buttonBarLang.setCellVerticalAlignment(btnremovelang, HasVerticalAlignment.ALIGN_MIDDLE);
		buttonBarLang.setCellHorizontalAlignment(btnaddlang, HasHorizontalAlignment.ALIGN_RIGHT);
		buttonBarLang.setCellHorizontalAlignment(btnremovelang, HasHorizontalAlignment.ALIGN_LEFT);
		
		langlistCS.setVisibleItemCount(14);			

		
		TitleBodyWidget langPanel = new TitleBodyWidget(constants.searchLanguageFilter(), langlistCS, buttonBarLang, "250px" , "100%");

		VerticalPanel langMainPanel = new VerticalPanel();
		langMainPanel.add(new Spacer("100%","6px"));
		langMainPanel.add(langPanel);
		
		return langMainPanel;
		
	}
	
	public void loadLanglistCS()
	{
		langlistCS.clear();
		
		ArrayList<LanguageCode> lang = MainApp.languageCode;
		ArrayList<String> userLang = MainApp.userSelectedLanguage;
		
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
		searchObj.clearSelectedLanguage();
		for(int i=0;i<langlistCS.getItemCount();i++)
		{						
			searchObj.addSelectedLanguage(((LanguageCode)langlistCS.getObject(i)).getLanguageCode().toLowerCase());
		}	
	}
	
	private Grid getTermCodePanel()
	{
		termCode = new TextBox();
		termCode.setText("");
		termCode.setWidth("100%");
		
		Grid table = new Grid(2,2);
		table.setWidth("100%");
		table.getColumnFormatter().setWidth(0, "70px");
		
		table.setWidget(0, 0, new HTML(constants.searchRepository()));
		table.setWidget(1, 0, new HTML(constants.searchCode()));
		table.setWidget(0, 1, getTermCodeRepository(termCode));
		table.setWidget(1, 1, termCode);
		
		return table;
	}
	
	private ListBox getScheme()
	{
		scheme = Convert.makeListBoxWithValue((ArrayList<String[]>)initData.getScheme());
		scheme.setWidth("100%");
		scheme.addChangeHandler(new ChangeHandler(){
			public void onChange(ChangeEvent event){
				if(scheme.getValue(scheme.getSelectedIndex()).length()!=0){
					searchObj.setScheme(scheme.getValue(scheme.getSelectedIndex()));
				}
			}
		});
		return scheme;
	}
	
	private ListBox getStatus()
	{
		status = Convert.makeListBoxSearchWithSelectedValue((ArrayList<String[]>)initData.getStatus(),"0");
		status.setWidth("100%");
		status.addChangeHandler(new ChangeHandler(){
			public void onChange(ChangeEvent event){
				String st = status.getItemText(status.getSelectedIndex());
				if(st!=null && !st.equals("--All--"))
				{
					searchObj.setStatus(st.toLowerCase());
				}
				else
					searchObj.setStatus(null);
			}
		});
		String sts = status.getValue(status.getSelectedIndex());
		if(sts!=null && !sts.equals("--None--"))
		{
			searchObj.setStatus(sts.toLowerCase());
		}
		else
			searchObj.setStatus(null);
		
		return status;
	}

	private ListBox getTermCodeRepository(final TextBox tb)
	{
		ArrayList<RelationshipObject> code = (ArrayList<RelationshipObject>)initData.getTermCodeProperties();
		termCodeRepository = Convert.makeOListBoxWithValue(code);
		termCodeRepository.setWidth("100%");
		termCodeRepository.addChangeHandler(new ChangeHandler(){
			public void onChange(ChangeEvent event){
				if(termCodeRepository.getSelectedIndex()==0){
					tb.setText("");
					searchObj.setTermCodeRepository(null);
					searchObj.setTermCode(null);
				}else{
					searchObj.setTermCodeRepository(((RelationshipObject) termCodeRepository.getObject(termCodeRepository.getSelectedIndex())).getName());					
				}
			}
		});
		return termCodeRepository;
	}
	
	private Grid getRelationPanel()
	{
		relationshipLabel = new LabelAOS("","");
		
		Image browse = new Image(this.browseImgPath);
		browse.setTitle(constants.searchBrowseRelationship());
		browse.setStyleName(Style.Link);
		browse.addClickHandler(new ClickHandler() 
		{
			public void onClick(ClickEvent event) 
			{
				final RelationshipBrowser rb =((MainApp) RootPanel.get().getWidget(0)).relationshipBrowser; 
				rb.showBrowser(RelationshipBrowser.REL_CONCEPT);
				rb.addSubmitClickHandler(new ClickHandler()
				{
					public void onClick(ClickEvent event) {
						relationshipLabel.setText(rb.getSelectedItem(),rb.getRelationshipObject());
						searchObj.setRelationship(rb.getRelationshipObject().getName());
					}					
				});						
			}
		});
		
		Image clear = new Image(this.trashImgPath);
		clear.setTitle(constants.searchClearRelationship());
		clear.setStyleName(Style.Link);
		clear.addClickHandler(new ClickHandler() 
		{
			public void onClick(ClickEvent event) 
			{
				relationshipLabel.setText("", "");
				searchObj.setRelationship(null);
			}
		});
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(browse);
		hp.add(clear);
		hp.setSpacing(3);
		
		Grid  table = new Grid(1,2);
		table.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		table.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
		table.setWidth("100%");
		table.setWidget(0, 0, relationshipLabel);
		table.setWidget(0, 1, hp);
		
		return table;
	}
		
	private Grid getConceptPropertyPanel()
	{
		conceptAttributePanel = new VerticalPanel();
		
	    LinkLabel btnadd = new LinkLabel("images/add-grey.gif");
	    btnadd.setTitle(constants.searchSelectConceptAttribute());
	    btnadd.setStyleName(Style.Link);
	    btnadd.addClickHandler(new ClickHandler() 
	    {
	        public void onClick(ClickEvent event) 
	        {
	            //if(addValue == null || !addValue.isLoaded)
	            final AddValue addValue = new AddValue(initData, AddValue.CONCEPTDOMAINEDITORIALDATATYPEPROPERTY, constants.searchSelectConceptAttribute());
                addValue.show();
                addValue.submit.addClickHandler(new ClickHandler()
                {   public void onClick(ClickEvent event)
                    {   
                        if(searchObj.addConceptAttribute(addValue.rObj.getName(), addValue.nonFuncObj))
                        {                            
                            final NonFuncObjectEle ele = new NonFuncObjectEle(addValue.rObj.getName(),addValue.nonFuncObj);                        
                            conceptAttributePanel.add(ele);
                            
                            try{
                                ele.delete.addClickHandler(new ClickHandler()
                                {   public void onClick(ClickEvent event)
                                    {
                                        if(searchObj.removeConceptAttribute(addValue.rObj.getName(),addValue.nonFuncObj))
                                        {
                                            conceptAttributePanel.remove(ele);
                                        }
                                        else
                                        {
                                            Window.alert(constants.searchFilterRemoveWarn());
                                        }
                                    }
                                });
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }else
                        {
                            Window.alert(constants.searchFilterAddWarn());
                        }
                    }                    
                });
	        }
	    });
	    
	    Image clear = new Image(this.trashImgPath);
	    clear.setTitle(constants.searchClearRelationship());
	    clear.setStyleName(Style.Link);
	    clear.addClickHandler(new ClickHandler() 
	    {
	        public void onClick(ClickEvent event) 
	        {
	            
	        }
	    });
	    
	    HorizontalPanel hp = new HorizontalPanel();
	    hp.add(btnadd);
	    hp.setSpacing(3);
	    
	    Grid  table = new Grid(1,2);
	    table.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);
	    table.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
	    table.setWidth("100%");
	    table.setWidget(0, 0, conceptAttributePanel);
	    table.setWidget(0, 1, hp);
	    
	    return table;
	}
	
	private Grid getTermPropertyPanel()
    {
		termAttributePanel = new VerticalPanel();
		LinkLabel btnadd = new LinkLabel("images/add-grey.gif");
        btnadd.setTitle(constants.searchSelectTermAttribute());
        btnadd.setStyleName(Style.Link);
        btnadd.addClickHandler(new ClickHandler() 
        {
            public void onClick(ClickEvent event) 
            {
                //if(addValue == null || !addValue.isLoaded)
                final AddValue addValue = new AddValue(initData, AddValue.TERMDOMAINEDITORIALDATATYPEPROPERTY, constants.searchSelectTermAttribute());
                addValue.show();
                addValue.submit.addClickHandler(new ClickHandler()
                {   public void onClick(ClickEvent event)
                    {   
                        if(searchObj.addTermAttribute(addValue.rObj.getName(), addValue.nonFuncObj))
                        {                            
                            final NonFuncObjectEle ele = new NonFuncObjectEle(addValue.rObj.getName(),addValue.nonFuncObj);                        
                            termAttributePanel.add(ele);
                            
                            try{
                                ele.delete.addClickHandler(new ClickHandler()
                                {   public void onClick(ClickEvent event)
                                    {
                                        if(searchObj.removeTermAttribute(addValue.rObj.getName(),addValue.nonFuncObj))
                                        {
                                            termAttributePanel.remove(ele);
                                        }
                                        else
                                        {
                                            Window.alert(constants.searchFilterRemoveWarn());
                                        }
                                    }
                                });
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }else
                        {
                            Window.alert(constants.searchFilterAddWarn());
                        }
                    }                    
                });
            }
        });
        
        Image clear = new Image(this.trashImgPath);
        clear.setTitle(constants.searchClearRelationship());
        clear.setStyleName(Style.Link);
        clear.addClickHandler(new ClickHandler() 
        {
            public void onClick(ClickEvent event) 
            {
                
            }
        });
        
        HorizontalPanel hp = new HorizontalPanel();
        hp.add(btnadd);
        hp.setSpacing(3);
        
        Grid  table = new Grid(1,2);
        table.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);
        table.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
        table.setWidth("100%");
        table.setWidget(0, 0, termAttributePanel);
        table.setWidget(0, 1, hp);
        
        return table;
    }
	
	private class LanguageDataDialog extends DialogBoxAOS implements ClickHandler{

		private VerticalPanel userpanel = new VerticalPanel();
		private Button btnAdd = new Button(constants.buttonAdd());
		private Button btnCancel = new Button(constants.buttonCancel());
		private OlistBox lstdata = new OlistBox(true);

		public LanguageDataDialog() 
		{
			this.setText(constants.searchSelectLang());				
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
				public void onClick(ClickEvent event) 
				{
					if(lstdata.getSelectedIndex()==-1)
					{
						Window.alert(constants.searchNoData());
						return;
					}	
					for(int i=0;i<lstdata.getItemCount();i++)
					{						
						if(lstdata.isItemSelected(i)){
							LanguageCode lc = (LanguageCode) lstdata.getObject(i);							
							langlistCS.addItem(lc.getLanguageNote(), lc);	
						}
					}	
					searchObj.clearSelectedLanguage();
					for(int i=0;i<langlistCS.getItemCount();i++)
					{						
						searchObj.addSelectedLanguage(((LanguageCode)langlistCS.getObject(i)).getLanguageCode().toLowerCase());
					}	
					hide();					
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
	
	private class IndexingDialog extends DialogBoxAOS implements ClickHandler{
		private Button btnCancel = new Button(constants.buttonCancel());
		
		public IndexingDialog() 
		{
			this.setText(constants.searchIndexTitle());	
			btnCancel.addClickHandler(new ClickHandler()
			{
				public void onClick(ClickEvent event) {
					hide();
				}
			});
			
			HorizontalPanel hp = new HorizontalPanel();
			hp.add(btnCancel);
			hp.setSpacing(5);
			hp.setCellHorizontalAlignment(btnCancel, HasHorizontalAlignment.ALIGN_RIGHT);
			
			VerticalPanel bottomPanel = new VerticalPanel();			
			bottomPanel.setStyleName("bottombar");
			bottomPanel.setWidth("100%");
			bottomPanel.add(hp);
			bottomPanel.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_RIGHT);
			
			HorizontalPanel mainPanel = new HorizontalPanel();
			mainPanel.add(getPanel(constants.searchIndexWaitMsg()));
			
			
			VerticalPanel loadingPanel = new VerticalPanel();
			loadingPanel.add(mainPanel);
			loadingPanel.add(bottomPanel);
			loadingPanel.setCellHorizontalAlignment(hp,HasHorizontalAlignment.ALIGN_CENTER);
			
			setWidget(loadingPanel);
		}
		
		public void startIndex()
		{
			show();
			AsyncCallback<Object> callback = new AsyncCallback<Object>()
			{
				public void onSuccess(Object result)
				{
					hide();
					int val = (Integer) result;
					if(val == 1)
						Window.alert(constants.searchIndexComplete());
					else if(val == 0)
						Window.alert(constants.searchIndexFail());
					else
						Window.alert(constants.searchIndexNotAvailable());
				}
				public void onFailure(Throwable caught){
					hide();
					Window.alert(constants.searchIndexFail());
				}
			};
			Service.searchSerice.indexOntology(MainApp.userOntology, callback);
			
		}
		
		private VerticalPanel getPanel(String message)
		{
			HTML label = new HTML("&nbsp;"+message+"&nbsp;");
			label.setWordWrap(false);
		    
		    Image img = new Image("images/loading.gif");
			
			HorizontalPanel hp = new HorizontalPanel();
			hp.addStyleName("loadingDialog");
			hp.add(img);
		    hp.add(label);
		    hp.setSpacing(30);
		    hp.setCellHorizontalAlignment(img, HasHorizontalAlignment.ALIGN_RIGHT);
		    hp.setCellVerticalAlignment(img, HasVerticalAlignment.ALIGN_MIDDLE);
		    hp.setCellHorizontalAlignment(label, HasHorizontalAlignment.ALIGN_LEFT);
		    hp.setCellVerticalAlignment(label, HasVerticalAlignment.ALIGN_MIDDLE);

		    VerticalPanel panel = new VerticalPanel();
		    panel.add(hp);
		    panel.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_CENTER);
			panel.setCellVerticalAlignment(hp, HasVerticalAlignment.ALIGN_MIDDLE);
			panel.setSize("100%", "100%");
			
			return panel;
		}
	}
}
