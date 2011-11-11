package org.fao.aoscs.client.module.consistency;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.consistency.widgetlib.FilterCheckBox;
import org.fao.aoscs.client.module.consistency.widgetlib.FilterGrid;
import org.fao.aoscs.client.module.constant.ConsistencyConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.domain.Consistency;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;

public class ConsistencyTemplate extends Composite{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	public VerticalPanel panel = new VerticalPanel();
	public HorizontalPanel checkPanel = new HorizontalPanel();
	public VerticalPanel statusPanel   = new VerticalPanel();
	public VerticalPanel destStatusPanel   = new VerticalPanel();
	public VerticalPanel termCodePropertyPanel   = new VerticalPanel();
	public VerticalPanel languagePanel   = new VerticalPanel();
	public VerticalPanel datePanel = new VerticalPanel();
	final CheckBox today = new CheckBox(constants.conToday());
	final CheckBox week = new CheckBox(constants.conThisWeek());
	final CheckBox month = new CheckBox(constants.conThisMonth());
	final DateBox startDate = new DateBox();
	final DateBox endDate = new DateBox();
	final CheckBox filter = new CheckBox(constants.buttonApply());
	private Button update = new Button(constants.buttonUpdate());
	private Image reload = new Image("images/reload-grey.gif");
	private Image filterImg = new Image("images/filter-grey.gif");
	public HorizontalPanel SHPanel = new HorizontalPanel();
	public Label showhideFilter = new Label(constants.conFilterHide());
	public VerticalPanel subpanel = new VerticalPanel();
	public ListBox listBox = new ListBox();
	public DeckPanel dp = new DeckPanel();
	public ArrayList<String> userLanguage = new ArrayList<String>();
	public ArrayList<String> selectedLanguage = new ArrayList<String>();
	public ArrayList<String> selectedStatus = new ArrayList<String>();
	public ArrayList<String> selectedDestStatus = new ArrayList<String>();
	public ArrayList<String> selectedTermCodeProperty = new ArrayList<String>();
	
	DateTimeFormat sdf = DateTimeFormat.getFormat("dd/MM/yyyy");
	public FilterGrid table = new FilterGrid();
	private ArrayList<String> completedList = new ArrayList<String>();

	public ConsistencyTemplate(){
		
		final HorizontalPanel dPanel = new HorizontalPanel();
		dPanel.setSize("100%", "100%");
		dPanel.setStyleName("borderbar");
		dPanel.add(panel);
		dPanel.setSize(MainApp.getBodyPanelWidth() -40 +"px", MainApp.getBodyPanelHeight() -30 +"px");
	    Window.addResizeHandler(new ResizeHandler()
	    {
	    	public void onResize(ResizeEvent event) {
	    		dPanel.setSize(MainApp.getBodyPanelWidth() -40 +"px", MainApp.getBodyPanelHeight() -30 +"px");
			}
		});
		
		VerticalPanel tempPanel = new VerticalPanel();
		tempPanel.setSize("100%", "100%");
		tempPanel.add(dPanel);
		tempPanel.setCellWidth(dPanel, "100%");
		tempPanel.setCellHeight(dPanel, "100%");
		tempPanel.setCellHorizontalAlignment(dPanel, HasHorizontalAlignment.ALIGN_CENTER);
		tempPanel.setCellVerticalAlignment(dPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		initWidget(tempPanel);
	}
	
	public void checkConsistency(int selection, ArrayList<String> status, ArrayList<String> termCodeProperty, ArrayList<String[]> language){}
	public void update(ArrayList<Consistency> updateValue, int selection, ArrayList<String> status, ArrayList<String> termCodeProperty, ArrayList<String[]> language){}
	public void makeLayout(HashMap<String, Consistency> row, int selection, boolean filter, boolean loadFilterOnly, ArrayList<String> status){}
	
	public void makeCheckPanel(final ArrayList<String> status, final ArrayList<String> termCodeProperty, final ArrayList<String[]> language){
		checkPanel.clear();		
		checkPanel.setStyleName("maintopbar");
		
		checkPanel.setWidth("100%");
		checkPanel.setSpacing(2);
		
		completedList.add("1");
		completedList.add("2");
		completedList.add("3");
		completedList.add("4");
		completedList.add("5");
		completedList.add("6");
		completedList.add("7");
		completedList.add("8");
		completedList.add("9");
		completedList.add("10");
		completedList.add("11");
		completedList.add("12");
		completedList.add("13");
		completedList.add("14");
		completedList.add("15");
		completedList.add("16");
		completedList.add("17");
		completedList.add("18");
		completedList.add("19");
		
		String cList1[] = ConsistencyConstants.getNoResultConsistencyType();
		String cList2[] = ConsistencyConstants.getResultConsistencyType();
		listBox.setTitle(constants.conSelectType());
		listBox.addItem(constants.conSelectType(), "0");
		ScrollPanel sc = new ScrollPanel();
		HTML html = new HTML("");
		html.setSize("100%", "100%");
		sc.add(html);
		dp.add(sc);
		dp.setSize("100%", "100%");
		for(int i=0;i<cList1.length;i++)
		{
			if(completedList.contains(""+(i+1)))
			{
				listBox.addItem(""+cList1[i], ""+(i+1));
				dp.add(new ScrollPanel());
			}
		}
		for(int i=0;i<cList2.length;i++)
		{
			if(completedList.contains(""+(i+1+cList1.length)))
			{
				listBox.addItem(""+cList2[i], ""+(i+1+cList1.length));
				dp.add(new ScrollPanel());
			}
		}
		listBox.addChangeHandler(new ChangeHandler(){
			public void onChange(ChangeEvent event) {
				int selection = listBox.getSelectedIndex();
				if(selection == 4) 
					update.setVisible(true);
				else
					update.setVisible(false);
				SHPanel.setVisible(true);
				
				showhideFilter.setText(constants.conFilterHide());
				
				ScrollPanel sc = (ScrollPanel)dp.getWidget(selection);
				if(sc.getWidget()==null)
				{
					checkConsistency(selection, status, termCodeProperty, language );
				}
				else
				{
					dp.showWidget(selection);
					subpanel.setVisible(true);
					makeLayout(null, selection, false, true, status);
				}
				if(selection==0)
				{
					SHPanel.setVisible(false);
					subpanel.setVisible(false);
				}
			}
		});


		HTML titleName = new HTML(constants.conTitle());
		titleName.setStyleName("maintopbartitle");
		titleName.setWordWrap(false);
		
		reload.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				int selection = listBox.getSelectedIndex();
				if(selection >0) checkConsistency(selection, status, termCodeProperty, language);
			}
		});
		reload.setTitle(constants.conReload());
		reload.setStyleName(Style.Link);
		update.setVisible(false);
		update.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				int selection = listBox.getSelectedIndex();
				if(selection == 4) 
				{	
					ArrayList<Consistency> updateValue = new ArrayList<Consistency>();
					for(int j=1;j<table.getRowCount();j++)
					{
						org.fao.aoscs.domain.Consistency c = (org.fao.aoscs.domain.Consistency)table.getRowValue(""+j);
						if(c.getStatus()!="")	updateValue.add(c);
					}
					if(updateValue.size()>0) update(updateValue, selection, status, termCodeProperty, language);
				}
			}
			});
		
		showhideFilter.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				subpanel.setVisible(!subpanel.isVisible());
					if(subpanel.isVisible())
						showhideFilter.setText(constants.conFilterHide());
					else
						showhideFilter.setText(constants.conFilterShow());
					
				}
			});
			
		showhideFilter.setStyleName(Style.Link);
		showhideFilter.addStyleName(Style.colorBlack);
		SHPanel.setSpacing(10);
		SHPanel.add(filterImg);
		SHPanel.add(showhideFilter);
		SHPanel.setVisible(false);
		SHPanel.setCellVerticalAlignment(filterImg, HasVerticalAlignment.ALIGN_MIDDLE);
		SHPanel.setCellVerticalAlignment(showhideFilter, HasVerticalAlignment.ALIGN_MIDDLE);
				
		checkPanel.add(titleName);
		checkPanel.setCellWidth(titleName,"100px");
		checkPanel.add(reload);
		checkPanel.add(listBox);
		checkPanel.add(update);
		checkPanel.add(SHPanel);
		checkPanel.setCellVerticalAlignment(titleName, HasVerticalAlignment.ALIGN_MIDDLE);
		checkPanel.setCellHorizontalAlignment(titleName, HasHorizontalAlignment.ALIGN_LEFT);
		checkPanel.setCellVerticalAlignment(reload, HasVerticalAlignment.ALIGN_MIDDLE);
		checkPanel.setCellVerticalAlignment(listBox, HasVerticalAlignment.ALIGN_MIDDLE);
		checkPanel.setCellVerticalAlignment(update, HasVerticalAlignment.ALIGN_MIDDLE);
		checkPanel.setCellVerticalAlignment(SHPanel, HasVerticalAlignment.ALIGN_MIDDLE);
	}
	@SuppressWarnings("deprecation")
	public void makeStatusPanel(final ArrayList<String> status){
		statusPanel.clear();
		statusPanel.setSize("100%","100%");
		VerticalPanel sPanel = new VerticalPanel();
		sPanel.setWidth("100%");
		final CheckBox all = new CheckBox(constants.conAll());
		
	
		final ArrayList<FilterCheckBox> itemContainer = new ArrayList<FilterCheckBox>();
		VerticalPanel vp = new VerticalPanel();
		for(int i=0;i<status.size();i++){
			String statusValue = (String) status.get(i);
			final FilterCheckBox item = new FilterCheckBox(statusValue, statusValue, table, "SHOWSTATUS", selectedLanguage, selectedStatus, selectedDestStatus, selectedTermCodeProperty);			
			final CheckBox cb = item.getCb();
			cb.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					item.setCheck(cb.getValue(), table, selectedLanguage, selectedStatus, selectedDestStatus, selectedTermCodeProperty);
					boolean chk = true;
					if(cb.getValue())
					{							 
						for(int i=0;i<itemContainer.size();i++){							
							FilterCheckBox tmp = (FilterCheckBox) itemContainer.get(i);
							if(!tmp.isCheck()){
								chk = false;
								break;
							}
						}
					}
					else {
						chk = false;
					}
					all.setValue(chk);
				}
			});
			item.setCheck(true, table, selectedLanguage, selectedStatus, selectedDestStatus, selectedTermCodeProperty);
			itemContainer.add(item);
			vp.add(item);
			
		}
		ScrollPanel sc = new ScrollPanel();
		sc.setStyleName(Style.filter_panel_background);
		sc.setSize("100%", "100%");
		sc.add(vp);
		
		
		all.setWidth("100%");						
		all.setValue(true);
		all.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				for(int i=1;i<table.getRowCount();i++){
					org.fao.aoscs.domain.Consistency c = (org.fao.aoscs.domain.Consistency)table.getRowValue(Integer.toString(i));
					c.setShowStatus(new Boolean(all.getValue()));
					table.getRowFormatter().setVisible(i, check(c));
				}
				for(int i=0;i<itemContainer.size();i++){
					FilterCheckBox tmp = (FilterCheckBox) itemContainer.get(i);
					tmp.setCheck(all.getValue(), table, selectedLanguage, selectedStatus, selectedDestStatus, selectedTermCodeProperty);
				}
			}
		});
			
		HorizontalPanel bottomPanel = new HorizontalPanel();
		bottomPanel.setSpacing(3);
		bottomPanel.add(all);		
		bottomPanel.setCellVerticalAlignment(all, HasVerticalAlignment.ALIGN_MIDDLE);
		bottomPanel.setCellHorizontalAlignment(all, HasHorizontalAlignment.ALIGN_RIGHT);
		
		HorizontalPanel lPanel = new HorizontalPanel();
		lPanel.setStyleName("bottombar");
		lPanel.setSize("100%","100%");		
		lPanel.add(bottomPanel);
		lPanel.setCellVerticalAlignment(bottomPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		lPanel.setCellHorizontalAlignment(bottomPanel, HasHorizontalAlignment.ALIGN_LEFT);
		
		sPanel.add(sc);
		sPanel.add(lPanel);
		sPanel.setCellVerticalAlignment(sc, HasVerticalAlignment.ALIGN_TOP);
		sPanel.setCellVerticalAlignment(all, HasVerticalAlignment.ALIGN_TOP);
		
		Label lb = new Label(constants.conFilterStatus());
		lb.setWidth("100%");				
		
		final Image pImage = new Image("images/popen-grey.gif");
		pImage.addStyleName("image-link");
		HorizontalPanel panelHeader = new HorizontalPanel();
		panelHeader.setSpacing(3);
		panelHeader.setSize("100%","100%");
		panelHeader.setStyleName(Style.filter_title_background);
		panelHeader.add(pImage);
		panelHeader.add(lb);
		
		DisclosurePanel disStatusPanel   = new DisclosurePanel(panelHeader, true);
		disStatusPanel.setStyleName(Style.filter_panel_border);
		disStatusPanel.setSize("100%","100%");
		disStatusPanel.setContent(sPanel);
		disStatusPanel.addOpenHandler(new OpenHandler<DisclosurePanel>(){
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				pImage.setUrl("images/popen-grey.gif");
			}
		});
		
		disStatusPanel.addCloseHandler(new CloseHandler<DisclosurePanel>(){
			public void onClose(CloseEvent<DisclosurePanel> event) {
				pImage.setUrl("images/pclose-grey.gif");
			}
		});
		
		statusPanel.add(disStatusPanel);
		//statusPanel.add(new HTML("<BR>"));
	}
	@SuppressWarnings("deprecation")
	public void makeDestStatusPanel(final ArrayList<String> status){
		destStatusPanel.clear();
		destStatusPanel.setWidth("100%");
		VerticalPanel dsPanel = new VerticalPanel();		
		dsPanel.setWidth("100%");
		
		final ArrayList<FilterCheckBox> itemContainer = new ArrayList<FilterCheckBox>();
		VerticalPanel vp = new VerticalPanel();
		for(int i=0;i<status.size();i++){
			String statusValue = (String) status.get(i);
			FilterCheckBox item = new FilterCheckBox(statusValue, statusValue, table, "SHOWDESTSTATUS", selectedLanguage, selectedStatus, selectedDestStatus, selectedTermCodeProperty);
			item.setCheck(true, table, selectedLanguage, selectedStatus, selectedDestStatus, selectedTermCodeProperty);
			itemContainer.add(item);
			vp.add(item);
		}
		ScrollPanel sc = new ScrollPanel();
		sc.setStyleName(Style.filter_panel_background);
		sc.setSize("100%", "100%");
		sc.add(vp);
		
		final CheckBox all = new CheckBox(constants.conAll());
		all.setWidth("100%");				
		all.setValue(true);
		all.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				for(int i=1;i<table.getRowCount();i++){
					org.fao.aoscs.domain.Consistency c = (org.fao.aoscs.domain.Consistency)table.getRowValue(Integer.toString(i));
					c.setShowDestStatus(new Boolean(all.getValue()));
					table.getRowFormatter().setVisible(i, check(c));
				}
				for(int i=0;i<itemContainer.size();i++){
					FilterCheckBox tmp = (FilterCheckBox) itemContainer.get(i);
					tmp.setCheck(all.getValue(), table, selectedLanguage, selectedStatus, selectedDestStatus, selectedTermCodeProperty);
				}
			}
		});
		
		HorizontalPanel bottomPanel = new HorizontalPanel();
		bottomPanel.setSpacing(3);
		bottomPanel.add(all);		
		bottomPanel.setCellVerticalAlignment(all, HasVerticalAlignment.ALIGN_MIDDLE);
		bottomPanel.setCellHorizontalAlignment(all, HasHorizontalAlignment.ALIGN_RIGHT);
		
		HorizontalPanel lPanel = new HorizontalPanel();
		lPanel.setStyleName("bottombar");
		lPanel.setSize("100%","100%");		
		lPanel.add(bottomPanel);
		lPanel.setCellVerticalAlignment(bottomPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		lPanel.setCellHorizontalAlignment(bottomPanel, HasHorizontalAlignment.ALIGN_LEFT);
			
		dsPanel.add(sc);
		dsPanel.add(lPanel);
		dsPanel.setCellVerticalAlignment(sc, HasVerticalAlignment.ALIGN_TOP);
		dsPanel.setCellVerticalAlignment(all, HasVerticalAlignment.ALIGN_TOP);
		
		Label lb = new Label(constants.conFilterStatusDest());
		lb.setWidth("100%");						
		final Image pImage = new Image("images/popen-grey.gif");
		pImage.addStyleName("image-link");
		HorizontalPanel panelHeader = new HorizontalPanel();
		panelHeader.setSpacing(3);
		panelHeader.setSize("100%","100%");
		panelHeader.setStyleName(Style.filter_title_background);
		panelHeader.add(pImage);
		panelHeader.add(lb);
		
		DisclosurePanel disStatusPanel   = new DisclosurePanel(panelHeader, true);
		disStatusPanel.setStyleName(Style.filter_panel_border);		
		disStatusPanel.setSize("100%","100%");
		disStatusPanel.setContent(dsPanel);
		disStatusPanel.addOpenHandler(new OpenHandler<DisclosurePanel>(){
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				pImage.setUrl("images/popen-grey.gif");
			}
		});
		
		disStatusPanel.addCloseHandler(new CloseHandler<DisclosurePanel>(){
			public void onClose(CloseEvent<DisclosurePanel> event) {
				pImage.setUrl("images/pclose-grey.gif");
			}
		});
		
		destStatusPanel.add(disStatusPanel);
		//destStatusPanel.add(new HTML("<BR>"));
	}
	@SuppressWarnings("deprecation")
	public void makeLanguagePanel(final ArrayList<String[]> language){
		languagePanel.clear();
		languagePanel.setSize("100%","100%");
		VerticalPanel dsPanel = new VerticalPanel();		
		dsPanel.setWidth("100%");
		
		Label lb = new Label(constants.conFilterLang());
		lb.setWidth("100%");				
		
		final ArrayList<FilterCheckBox> itemContainer = new ArrayList<FilterCheckBox>();
		VerticalPanel vp = new VerticalPanel();
		for(int i=0;i<language.size();i++){
			String[] value = (String[]) language.get(i);
			FilterCheckBox item = new FilterCheckBox(value[0]+" ("+value[1].toLowerCase()+")", value[1].toLowerCase(), table, "SHOWLANGUAGE", selectedLanguage, selectedStatus, selectedDestStatus, selectedTermCodeProperty);
			item.setCheck(true, table, selectedLanguage, selectedStatus, selectedDestStatus, selectedTermCodeProperty);
			itemContainer.add(item);
			vp.add(item);
		}
		ScrollPanel sc = new ScrollPanel();
		sc.setStyleName(Style.filter_panel_background);
		sc.setSize("100%", "100%");
		sc.add(vp);
		
		final CheckBox all = new CheckBox(constants.conAll());
		all.setWidth("100%");				
		all.setValue(true);
		all.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				for(int i=0;i<itemContainer.size();i++){
					FilterCheckBox tmp = (FilterCheckBox) itemContainer.get(i);
					tmp.setCheck(all.getValue(), table, selectedLanguage, selectedStatus, selectedDestStatus, selectedTermCodeProperty);
				}
				for(int i=1;i<table.getRowCount();i++){
					org.fao.aoscs.domain.Consistency c = (org.fao.aoscs.domain.Consistency)table.getRowValue(Integer.toString(i));
					c.setShowLanguage(new Boolean(!c.getLanguages().containsAll(selectedLanguage)));
					table.getRowFormatter().setVisible(i, check(c));
				}
			}
		});
		
		HorizontalPanel bottomPanel = new HorizontalPanel();
		bottomPanel.setSpacing(3);
		bottomPanel.add(all);		
		bottomPanel.setCellVerticalAlignment(all, HasVerticalAlignment.ALIGN_MIDDLE);
		bottomPanel.setCellHorizontalAlignment(all, HasHorizontalAlignment.ALIGN_RIGHT);
		
		HorizontalPanel lPanel = new HorizontalPanel();
		lPanel.setStyleName("bottombar");
		lPanel.setSize("100%","100%");		
		lPanel.add(bottomPanel);
		lPanel.setCellVerticalAlignment(bottomPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		lPanel.setCellHorizontalAlignment(bottomPanel, HasHorizontalAlignment.ALIGN_LEFT);
	
		dsPanel.add(sc);
		dsPanel.add(lPanel);
		dsPanel.setCellVerticalAlignment(sc, HasVerticalAlignment.ALIGN_TOP);
		dsPanel.setCellVerticalAlignment(all, HasVerticalAlignment.ALIGN_TOP);
		
		final Image pImage = new Image("images/popen-grey.gif");
		pImage.addStyleName("image-link");
		HorizontalPanel panelHeader = new HorizontalPanel();
		panelHeader.setSpacing(3);
		panelHeader.setSize("100%","100%");
		panelHeader.setStyleName(Style.filter_title_background);
		panelHeader.add(pImage);
		panelHeader.add(lb);
		
		DisclosurePanel disStatusPanel   = new DisclosurePanel(panelHeader, true);
		disStatusPanel.setStyleName(Style.filter_panel_border);
		disStatusPanel.setSize("100%","100%");
		disStatusPanel.setContent(dsPanel);
		disStatusPanel.addOpenHandler(new OpenHandler<DisclosurePanel>(){
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				pImage.setUrl("images/popen-grey.gif");
			}
		});
		
		disStatusPanel.addCloseHandler(new CloseHandler<DisclosurePanel>(){
			public void onClose(CloseEvent<DisclosurePanel> event) {
				pImage.setUrl("images/pclose-grey.gif");
			}
		});
		
		
		languagePanel.add(disStatusPanel);
		//languagePanel.add(new HTML("<BR>"));
	}
	@SuppressWarnings("deprecation")
	public void makeTermCodePropertyPanel(final ArrayList<String> termCodeProperty){
		termCodePropertyPanel.clear();
		termCodePropertyPanel.setWidth("100%");
		VerticalPanel tcpPanel = new VerticalPanel();		
		tcpPanel.setWidth("100%");
		
		Label lb = new Label(constants.conFilterTermCodeType());
		lb.setWidth("100%");		
	
		final ArrayList<FilterCheckBox> itemContainer = new ArrayList<FilterCheckBox>();
		VerticalPanel vp = new VerticalPanel();
		for(int i=0;i<termCodeProperty.size();i++){
			String tcpValue = (String) termCodeProperty.get(i);
			FilterCheckBox item = new FilterCheckBox(tcpValue, tcpValue, table, "SHOWTERMCODEPROPERTY", selectedLanguage, selectedStatus, selectedDestStatus, selectedTermCodeProperty);
			item.setCheck(true, table, selectedLanguage, selectedStatus, selectedDestStatus, selectedTermCodeProperty);
			itemContainer.add(item);
			vp.add(item);
		}
		ScrollPanel sc = new ScrollPanel();
		sc.setStyleName(Style.filter_panel_background);
		sc.setSize("100%", "100%");
		sc.add(vp);
		
		final CheckBox all = new CheckBox(constants.conAll());
		all.setWidth("100%");						
		all.setValue(true);
		all.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				for(int i=1;i<table.getRowCount();i++){
					org.fao.aoscs.domain.Consistency c = (org.fao.aoscs.domain.Consistency)table.getRowValue(Integer.toString(i));
					c.setShowTermCodeProperty(new Boolean(all.getValue()));
					table.getRowFormatter().setVisible(i, check(c));
				}
				for(int i=0;i<itemContainer.size();i++){
					FilterCheckBox tmp = (FilterCheckBox) itemContainer.get(i);
					tmp.setCheck(all.getValue(), table, selectedLanguage, selectedStatus, selectedDestStatus, selectedTermCodeProperty);
				}
			}
		});
		
		HorizontalPanel bottomPanel = new HorizontalPanel();
		bottomPanel.setSpacing(3);
		bottomPanel.add(all);		
		bottomPanel.setCellVerticalAlignment(all, HasVerticalAlignment.ALIGN_MIDDLE);
		bottomPanel.setCellHorizontalAlignment(all, HasHorizontalAlignment.ALIGN_RIGHT);
		
		HorizontalPanel lPanel = new HorizontalPanel();
		lPanel.setStyleName("bottombar");
		lPanel.setSize("100%","100%");		
		lPanel.add(bottomPanel);
		lPanel.setCellVerticalAlignment(bottomPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		lPanel.setCellHorizontalAlignment(bottomPanel, HasHorizontalAlignment.ALIGN_LEFT);
	
		tcpPanel.add(sc);
		tcpPanel.add(lPanel);
		tcpPanel.setCellVerticalAlignment(sc, HasVerticalAlignment.ALIGN_TOP);
		tcpPanel.setCellVerticalAlignment(all, HasVerticalAlignment.ALIGN_TOP);
		
		
		final Image pImage = new Image("images/popen-grey.gif");
		pImage.addStyleName("image-link");
		HorizontalPanel panelHeader = new HorizontalPanel();
		panelHeader.setSpacing(3);
		panelHeader.setSize("100%","100%");
		panelHeader.setStyleName(Style.filter_title_background);
		panelHeader.add(pImage);
		panelHeader.add(lb);
		
		DisclosurePanel disStatusPanel   = new DisclosurePanel(panelHeader, true);
		disStatusPanel.setStyleName(Style.filter_panel_border);
		disStatusPanel.setSize("100%","100%");
		disStatusPanel.setContent(tcpPanel);
		disStatusPanel.addOpenHandler(new OpenHandler<DisclosurePanel>(){
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				pImage.setUrl("images/popen-grey.gif");
			}
		});
		
		disStatusPanel.addCloseHandler(new CloseHandler<DisclosurePanel>(){
			public void onClose(CloseEvent<DisclosurePanel> event) {
				pImage.setUrl("images/pclose-grey.gif");
			}
		});
		
		termCodePropertyPanel.add(disStatusPanel);
		//termCodePropertyPanel.add(new HTML("<BR>"));
	}
	@SuppressWarnings("deprecation")
	public void makeDatePanel(){
		datePanel.clear();
		datePanel.setWidth("100%");
		
		VerticalPanel dPanel = new VerticalPanel();
		dPanel.setWidth("100%");
		
		Label lb = new Label(constants.conFilterDate());				
		lb.setWidth("100%");
		
		Image clear = new Image("images/clear-grey.gif");
		clear.setStyleName(Style.Link);
		clear.addStyleName(Style.font_11);		
		clear.addStyleName("image-link");
		clear.setTitle(constants.conClearDateTitle());
		
		Label clearTxt = new Label(" "+constants.conClearDate()+" ");
		clearTxt.setStyleName(Style.Link);		
		clearTxt.addStyleName(Style.colorBlack);
		clearTxt.addStyleName(Style.font_11);
		clearTxt.setTitle(constants.conClearDateTitle());
		
		Label fromDate = new Label(constants.conFrom());
		Label toDate = new Label(constants.conTo());
		
		filter.setWidth("100%");		
		filter.addStyleName(Style.colorBlack);
		filter.addStyleName(Style.font_11);
		
		startDate.setFormat((new DateBox.DefaultFormat (DateTimeFormat.getFormat ("dd/MM/yyyy"))));
		endDate.setFormat((new DateBox.DefaultFormat (DateTimeFormat.getFormat ("dd/MM/yyyy"))));		
		
		
		filter.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(filter.getValue())
					filterDate(startDate.getTextBox().getText(), endDate.getTextBox().getText(), filter);
				else
					clearFilterDate();
			}
		});
		today.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(today.getValue())
				{
					week.setValue(false);
					month.setValue(false);					
					Date sDate = Convert.getBeginDay();					 					
					Date eDate = Convert.getEndDay();					
					//checkDate(sDate, eDate);
					startDate.setValue(sDate);
					endDate.setValue(eDate);
				}
				else
				{
					startDate.getTextBox().setText("");
					endDate.getTextBox().setText("");
					clearFilterDate();
				}
			}
		});
		week.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(week.getValue())
				{
					today.setValue(false);
					month.setValue(false);
					
					Date sDate = Convert.getBeginWeek();					 					
					Date eDate = Convert.getEndWeek();					
					//checkDate(sDate, eDate);
					startDate.setValue(sDate);
					endDate.setValue(eDate);
				}
				else
				{
					startDate.getTextBox().setText("");
					endDate.getTextBox().setText("");
					clearFilterDate();
				}
			}
		});
		month.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(month.getValue())
				{
					today.setValue(false);
					week.setValue(false);
					Date sDate = Convert.getBeginMonth();					 					
					Date eDate = Convert.getEndMonth();					
					//checkDate(sDate, eDate);
					startDate.setValue(sDate);
					endDate.setValue(eDate);
				}
				else
				{
					startDate.getTextBox().setText("");
					endDate.getTextBox().setText("");
					clearFilterDate();
				}
			}
		});
		clear.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				clearDate();
			}
		});
		clearTxt.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				clearDate();
			}
		});
		
		HorizontalPanel checkPanel = new HorizontalPanel();
		checkPanel.setWidth("100%");
		checkPanel.add(today);
		checkPanel.add(week);
		checkPanel.add(month);
		
		Grid gd = new Grid(2,3);
		gd.setCellSpacing(5);
		gd.setWidget(0,0,fromDate);
		gd.setWidget(0,1,startDate);
		gd.setWidget(0,2,new HTML("<font size=1>"+constants.conDateFormat()+"</font>"));
		gd.setWidget(1,0,toDate);		
		gd.setWidget(1,1,endDate);
		gd.setWidget(1,2,new HTML("<font size=1>"+constants.conDateFormat()+"</font>"));
		
		VerticalPanel vp = new VerticalPanel();
		vp.setStyleName(Style.filter_panel_background);
		vp.setSize("100%","100%");
		vp.add(checkPanel);
		vp.add(gd);
		
		HorizontalPanel bottomPanel = new HorizontalPanel();
		bottomPanel.setSpacing(3);
		bottomPanel.add(clear);
		bottomPanel.add(clearTxt);
		bottomPanel.setCellVerticalAlignment(clear, HasVerticalAlignment.ALIGN_MIDDLE);
		bottomPanel.setCellHorizontalAlignment(clear, HasHorizontalAlignment.ALIGN_RIGHT);
		bottomPanel.setCellVerticalAlignment(clearTxt, HasVerticalAlignment.ALIGN_MIDDLE);
		bottomPanel.setCellHorizontalAlignment(clearTxt, HasHorizontalAlignment.ALIGN_RIGHT);
		
		
		dPanel.add(vp);
		HorizontalPanel lPanel = new HorizontalPanel();
		lPanel.setStyleName("bottombar");
		lPanel.setSize("100%","100%");
		lPanel.add(filter);
		lPanel.add(bottomPanel);
		lPanel.setCellHorizontalAlignment(lb, HasHorizontalAlignment.ALIGN_CENTER);
		lPanel.setCellVerticalAlignment(lb, HasVerticalAlignment.ALIGN_MIDDLE);
		lPanel.setCellVerticalAlignment(bottomPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		lPanel.setCellHorizontalAlignment(bottomPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		dPanel.add(lPanel);
		
		final Image pImage = new Image("images/popen-grey.gif");
		pImage.addStyleName("image-link");
		HorizontalPanel panelHeader = new HorizontalPanel();
		panelHeader.setSpacing(3);
		panelHeader.setSize("100%","100%");
		panelHeader.setStyleName(Style.filter_title_background);
		panelHeader.add(pImage);
		panelHeader.add(lb);
		
		DisclosurePanel disStatusPanel   = new DisclosurePanel(panelHeader, true);
		disStatusPanel.setStyleName(Style.filter_panel_border);
		disStatusPanel.setSize("100%","100%");
		disStatusPanel.setContent(dPanel);
		disStatusPanel.addOpenHandler(new OpenHandler<DisclosurePanel>(){
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				pImage.setUrl("images/popen-grey.gif");
			}
		});
		
		disStatusPanel.addCloseHandler(new CloseHandler<DisclosurePanel>(){
			public void onClose(CloseEvent<DisclosurePanel> event) {
				pImage.setUrl("images/pclose-grey.gif");
			}
		});	
		datePanel.add(disStatusPanel);		
		//datePanel.add(new HTML("<BR>"));
		
	}
	public void filterDate(String from, String to, CheckBox filter)
	{
		if(from.length()<10 && to.length()<10)
		{
			filter.setValue(false);
			Window.alert(constants.conDateInvalid());
		}
		else if(from.length()<10)
		{
			filter.setValue(false);
			Window.alert(constants.conDateStartInvalid());
		}
		else if (to.length()<10)
		{
			filter.setValue(false);
			Window.alert(constants.conDateEndInvalid());
		}
		else
		{
			try {
				Date startDate = DateTimeFormat.getFormat("dd/MM/yyyy").parse(from);
				Date endDate = DateTimeFormat.getFormat("dd/MM/yyyy hh:mm:ss").parse(from+" 23:59:59");
				if(endDate.before(startDate))
				{
					filter.setValue(false);
					Window.alert(constants.conDateLaterMax());
				}
				else
				{
					checkDate(startDate, endDate);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void checkDate(Date startDate, Date endDate)
	{
		for(int i=1;i<table.getRowCount();i++){
			org.fao.aoscs.domain.Consistency c = (org.fao.aoscs.domain.Consistency)table.getRowValue(Integer.toString(i));
			Date date = c.getDateModified();
			if(date!=null)
			{
				c.setShowDate(new Boolean(((startDate.before(date) && endDate.after(date)) || startDate.equals(date) || endDate.equals(date))));
			}
			else
				c.setShowDate(new Boolean(false));
			table.getRowFormatter().setVisible(i, check(c));
		}
	}
	
	public void clearFilterDate()
	{
		for(int i=1;i<table.getRowCount();i++)
		{
			org.fao.aoscs.domain.Consistency c = (org.fao.aoscs.domain.Consistency)table.getRowValue(Integer.toString(i));
			c.setShowDate(new Boolean(true));
			table.getRowFormatter().setVisible(i, check(c));
		}
	}
	
	public boolean check(org.fao.aoscs.domain.Consistency c)
	{
		boolean l = true;
		boolean s = true;
		boolean ds = true;
		boolean tc = true;
		boolean d = true;

		Boolean _l = c.getShowLanguage();
		Boolean _s = c.getShowStatus();
		Boolean _ds = c.getShowDestStatus();
		Boolean _tc = c.getShowTermCodeProperty();
		Boolean _d = c.getShowDate();
		
		if(_l!=null) l = _l.booleanValue();
		if(_s!=null) s = _s.booleanValue();
		if(_ds!=null) ds = _ds.booleanValue();
		if(_tc!=null) tc = _tc.booleanValue();
		if(_d!=null) d = _d.booleanValue();

		if(l && s && ds && tc && d)
			return true;
		else 
			return false;
	}
	
	public void clearDate()
	{
		clearFilterDate();
		today.setValue(false);
		week.setValue(false);
		month.setValue(false);
		startDate.getTextBox().setText("");
		endDate.getTextBox().setText("");
		filter.setValue(false);
	}
	
	public void initLoading(){
		clearPanel();
		LoadingDialog load = new LoadingDialog();
		panel.add(load);
		panel.setCellHorizontalAlignment(load, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(load, HasVerticalAlignment.ALIGN_MIDDLE);
	}
	public void clearPanel(){
		panel.clear();
		panel.setStyleName("valinpanel");
		panel.setSize("100%","100%");
	}

}

