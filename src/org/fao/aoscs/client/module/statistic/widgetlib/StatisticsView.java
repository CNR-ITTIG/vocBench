package org.fao.aoscs.client.module.statistic.widgetlib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.classification.widgetlib.OlistBox;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.module.statistic.service.StatisticsService.StatisticsServiceUtil;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.panel.BodyPanel;
import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;
import org.fao.aoscs.client.widgetlib.shared.tree.LazyLoadingTree;
import org.fao.aoscs.domain.ClassObject;
import org.fao.aoscs.domain.InitializeStatisticalData;
import org.fao.aoscs.domain.LanguageCode;
import org.fao.aoscs.domain.ObjectPerUserStat;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.StatisticalData;
import org.fao.aoscs.domain.Users;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.gen2.table.client.FixedWidthFlexTable;
import com.google.gwt.gen2.table.client.FixedWidthGrid;
import com.google.gwt.gen2.table.client.ScrollTable;
import com.google.gwt.gen2.table.client.AbstractScrollTable.ScrollPolicy;
import com.google.gwt.gen2.table.client.AbstractScrollTable.SortPolicy;
import com.google.gwt.gen2.table.override.client.FlexTable;
import com.google.gwt.gen2.table.override.client.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class StatisticsView extends Composite{

	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private VerticalPanel panel = new VerticalPanel();
	private VerticalPanel bodyPanel = new VerticalPanel();
	private VerticalPanel loadingPanel = new VerticalPanel();
	private InitializeStatisticalData initData = null;
	private StatisticalData statData = new StatisticalData();
	
	private Grid gridUserStat = null;
	private Grid gridStatPerLang =  null;
	private Grid gridStatPerStatus =  null;
	private Grid gridActionPerUser = null;
	private VerticalPanel statVP = new VerticalPanel();
	private Grid gridStatPerRelationship =  null;
	private Grid gridExportStat =  null;

	private PrintPreviewDialogBox ppdb =  null;
	private DeckPanel tablePanel = new DeckPanel();
	private ListBox listBox = new ListBox();
	
	public StatisticsView()
	{  
		LoadingDialog sayLoading = new LoadingDialog();
		panel.add(sayLoading);
		panel.setSize("100%", "100%");    
		panel.setCellHorizontalAlignment(sayLoading, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(sayLoading, HasVerticalAlignment.ALIGN_TOP);
		loadData();
		initWidget(panel);
	}
	
	public void loadData()
	{		
		final AsyncCallback<Object> callback = new AsyncCallback<Object>() 
		{
			public void onSuccess(Object result) 
			{			
				initData = (InitializeStatisticalData) result;
				final HorizontalPanel leftTopWidget = getStatList();
				final HorizontalPanel rightTopWidget = getPrintPanel();
				
				LoadingDialog sayLoading = new LoadingDialog();
				loadingPanel.add(sayLoading);
				loadingPanel.setCellHorizontalAlignment(sayLoading, HasHorizontalAlignment.ALIGN_RIGHT);
				loadingPanel.setCellVerticalAlignment(sayLoading, HasVerticalAlignment.ALIGN_MIDDLE);
				loadingPanel.setSize(MainApp.getBodyPanelWidth()-50 +"px", MainApp.getBodyPanelHeight()-100 +"px");
				
				bodyPanel.add(getTablePanel());
				bodyPanel.setSize(MainApp.getBodyPanelWidth() +"px", MainApp.getBodyPanelHeight()-50 +"px");
			    Window.addResizeHandler(new ResizeHandler()
			    {
			    	public void onResize(ResizeEvent event) {
						bodyPanel.setSize(MainApp.getBodyPanelWidth() +"px", MainApp.getBodyPanelHeight()-50 +"px");
						loadingPanel.setSize(MainApp.getBodyPanelWidth()-50 +"px", MainApp.getBodyPanelHeight()-100 +"px");
					}
			    });
				
			    
			    BodyPanel vpPanel = new BodyPanel(constants.statTitle() , leftTopWidget, bodyPanel , rightTopWidget);
			    panel.clear();
			    panel.setSize("100%", "100%");
			    panel.add(vpPanel);	      
			    panel.setCellHorizontalAlignment(vpPanel,  HasHorizontalAlignment.ALIGN_CENTER);
			    panel.setCellVerticalAlignment(vpPanel,  HasVerticalAlignment.ALIGN_TOP);
				
			}
			public void onFailure(Throwable caught) {
				Window.alert(constants.statLoadFail());
			}
		};
		StatisticsServiceUtil.getInstance().getInitializeStatisticalData(MainApp.userOntology, callback);
	}
	
	public ArrayList<Object> getSelectedList(OlistBox listBox)
	{
		ArrayList<Object> list = new ArrayList<Object>();
		for(int i=0;i<listBox.getItemCount();i++){											
			if(listBox.isItemSelected(i))
			{
				list.add(listBox.getObject(i));
			}
		}
		return list;
	}
	
	public DeckPanel getTablePanel()
	{
		tablePanel.setSize("100%", "100%");
		for(int i=0;i<listBox.getItemCount();i++)
		{
			tablePanel.add(new ScrollPanel());
		}
		
		if(tablePanel.getWidgetCount()>0)
		{
			Object obj = tablePanel.getWidget(0);
			if(obj instanceof ScrollPanel)
			{
				ScrollPanel sc  = (ScrollPanel) obj;
				sc.clear();
				sc.add(new HTML("&nbsp;"));
				tablePanel.showWidget(0);
			}
		}
		return tablePanel;
	}
	
	public void reload()
	{
		int selection = listBox.getSelectedIndex();
		if(selection >0)
		{
			ScrollPanel sc = new ScrollPanel();
			Object obj = tablePanel.getWidget(selection);
			if(obj instanceof ScrollPanel)
			{
				sc = (ScrollPanel) obj;
				sc.clear();
			}
			getData(selection);
		}
	}
	
	public void getData(final int selection)
	{		
		tablePanel.showWidget(selection);
		Widget w = null;
		ScrollPanel sc = new ScrollPanel();
		Object obj = tablePanel.getWidget(selection);
		if(obj instanceof ScrollPanel)
		{
			sc = (ScrollPanel) obj;
			w = sc.getWidget();
		}
		if(w==null)
		{
			sc.clear();
			sc.add(loadingPanel);
			final AsyncCallback<StatisticalData> callback = new AsyncCallback<StatisticalData>() 
			{
				public void onSuccess(StatisticalData stData) 
				{			
					if(stData.getCheckWhoLastConnected()!=null)
						statData.setCheckWhoLastConnected(stData.getCheckWhoLastConnected());
					
					if(stData.getCountNumberOfUser()!=0)
						statData.setCountNumberOfUser(stData.getCountNumberOfUser());
					else
						statData.setCountNumberOfUser(0);
					
					if(stData.getCheckNumberOfConnectionPerUser()!=null)
						statData.setCheckNumberOfConnectionPerUser(stData.getCheckNumberOfConnectionPerUser());
					
					if(stData.getCheckNumberOfLastModificationPerUser()!=null)
						statData.setCheckNumberOfLastModificationPerUser(stData.getCheckNumberOfLastModificationPerUser());
					
					if(stData.getCountNumberOfConceptCreatedByUser()!=null)
						statData.setCountNumberOfConceptCreatedByUser(stData.getCountNumberOfConceptCreatedByUser());
					
					if(stData.getCountNumberOfConceptDeletedByUser()!=null)
						statData.setCountNumberOfConceptDeletedByUser(stData.getCountNumberOfConceptDeletedByUser());
					
					if(stData.getCountNumberOfConceptEditedByUser()!=null){
					    statData.setCountNumberOfConceptEditedByUser(stData.getCountNumberOfConceptEditedByUser());
					}

					if(stData.getCountNumberOfConceptPerRelationship()!=null)
						statData.setCountNumberOfConceptPerRelationship(stData.getCountNumberOfConceptPerRelationship());
					
					if(stData.getCountNumberOfConceptPerStatus()!=null)
						statData.setCountNumberOfConceptPerStatus(stData.getCountNumberOfConceptPerStatus());
					
					if(stData.getCountNumberOfExports()!=null)
						statData.setCountNumberOfExports(stData.getCountNumberOfExports());
					
					if(stData.getCountNumberOfRelationshipsPerUsers()!=null)
						statData.setCountNumberOfRelationshipsPerUsers(stData.getCountNumberOfRelationshipsPerUsers());
					
					if(stData.getCountNumberOfTermCreatedByUser()!=null)
						statData.setCountNumberOfTermCreatedByUser(stData.getCountNumberOfTermCreatedByUser());
					
					if(stData.getCountNumberOfTermDeletedByUser()!=null)
						statData.setCountNumberOfTermDeletedByUser(stData.getCountNumberOfTermDeletedByUser());
					
					if(stData.getCountNumberOfTermEditedByUser()!=null)
						statData.setCountNumberOfTermEditedByUser(stData.getCountNumberOfTermEditedByUser());
					
					if(stData.getCountNumberOfTermPerStatus()!=null)
						statData.setCountNumberOfTermPerStatus(stData.getCountNumberOfTermPerStatus());
					
					if(stData.getCountNumberOfUsersPerLanguage()!=null)
						statData.setCountNumberOfUsersPerLanguage(stData.getCountNumberOfUsersPerLanguage());
					
					if(stData.getListTheDomainAndRangeForRelationship()!=null)
						statData.setListTheDomainAndRangeForRelationship(stData.getListTheDomainAndRangeForRelationship());
						
					loadScrollPanel(selection);
				}
				public void onFailure(Throwable caught) {
					Window.alert(constants.statLoadFail());
				}
			};
			
	
			switch (selection) {
	
			 case 1:     
				 StatisticsServiceUtil.getInstance().getUserStat(callback);
				 break;
				 
			 case 2:     
				 StatisticsServiceUtil.getInstance().getStatPerLang(callback);
				 break;
				 
			 case 3:     
				 StatisticsServiceUtil.getInstance().getStatPerStatus(MainApp.userOntology, callback);
				 break;
				 
			 case 4:     
				 StatisticsServiceUtil.getInstance().StatPerUser(MainApp.userOntology, callback);
				 break;
				 
			 case 5:     
				 StatisticsServiceUtil.getInstance().getActionPerUser(MainApp.userOntology, callback);
				 break;
				 
			 case 6:     
				 StatisticsServiceUtil.getInstance().getStatPerRelationship(MainApp.userOntology, callback);
				 break;
				 
			 case 7:     
				 StatisticsServiceUtil.getInstance().getExportStat(MainApp.userOntology, callback);
				 break;
				 
			 default: 
				 	break;
			}
		}	
	}
	
	public void loadScrollPanel(int selection)
	{
		ScrollPanel sc = new ScrollPanel();
		Object obj = tablePanel.getWidget(selection);
		if(obj instanceof ScrollPanel)
		{
			sc = (ScrollPanel) obj;
			sc.clear();
		}
		switch (selection) {

		 case 1:     
			 gridUserStat = getUserStat();
			 sc.add(setTableConceptDetailStyleTop(gridUserStat, "#FAFAFA", "#D1D1D1", true, true, false));
			 break;
			 
		 case 2:     
			 gridStatPerLang = getStatPerLang();
			 sc.add(setTableConceptDetailStyleTop(gridStatPerLang, "#FAFAFA", "#D1D1D1", true, true, false));
			 break;
			 
		 case 3:     
			 gridStatPerStatus = getStatPerStatus();
			 sc.add(setTableConceptDetailStyleTop(gridStatPerStatus, "#FAFAFA", "#D1D1D1", true, true, true));
			 break;
			 
		 case 4:     
			 statVP.setSize("100%", "100%");
			 statVP.clear();
			 statVP.add(new StatPerUser(false).getTable());	
			 sc.add(statVP);
			 break;
			 
		 case 5:     
			 gridActionPerUser = getActionPerUser();
			 sc.add(setTableConceptDetailStyleTop(gridActionPerUser, "#FAFAFA", "#D1D1D1", true, true, true));
			 break;
			 
		 case 6:     
			 gridStatPerRelationship = getStatPerRelationship();
			 sc.add(setTableConceptDetailStyleTop(gridStatPerRelationship, "#FAFAFA", "#D1D1D1", true, true, true));
			 break;
			 
		 case 7:     
			 gridExportStat = getExportStat();
			 sc.add(setTableConceptDetailStyleTop(gridExportStat, "#FAFAFA", "#D1D1D1", true, true, true));
			 break;
			 
		 default: 
			 	break;
		    }	
	
	}
	
	public static Widget setTableConceptDetailStyleTopPrint(Grid table, boolean hasTopBackground, boolean hasBottomBackground)
	{
		table.setWidth("100%");
		table.setBorderWidth(1);
		
		for (int i = 0; i < table.getColumnCount(); i++) {
			int topCnt = 0;
			int rowCnt = table.getRowCount();
			if(hasTopBackground)
			{
				table.getCellFormatter().setWordWrap(0, i, true);
				DOM.setStyleAttribute(table.getCellFormatter().getElement(0, i), "paddingLeft", "10px");
				DOM.setStyleAttribute(table.getCellFormatter().getElement(0, i), "paddingRight", "10px");
				DOM.setStyleAttribute(table.getCellFormatter().getElement(0, i), "fontWeight", "bold");
				DOM.setStyleAttribute(table.getCellFormatter().getElement(0, i), "height", "25px");
				topCnt = 1;
			}
			
			if(hasBottomBackground)
			{
				table.getCellFormatter().setWordWrap(rowCnt-1, i, true);
				DOM.setStyleAttribute(table.getCellFormatter().getElement(rowCnt-1, i), "paddingLeft", "10px");
				DOM.setStyleAttribute(table.getCellFormatter().getElement(rowCnt-1, i), "paddingRight", "10px");
				DOM.setStyleAttribute(table.getCellFormatter().getElement(rowCnt-1, i), "fontWeight", "bold");
				DOM.setStyleAttribute(table.getCellFormatter().getElement(rowCnt-1, i), "height", "25px");
				rowCnt--;
			}
			
			for (int j = topCnt; j < rowCnt; j++) {
				DOM.setStyleAttribute(table.getCellFormatter().getElement(j, i),"backgroundColor", "#FFFFFF");
				DOM.setStyleAttribute(table.getCellFormatter().getElement(j, i), "paddingLeft", "10px");
				DOM.setStyleAttribute(table.getCellFormatter().getElement(j, i), "paddingRight", "10px");
				table.getCellFormatter().setWordWrap(j, i, true);
			}
		}
		
		
		return table;
	}
	                            		   
	public static VerticalPanel setTableConceptDetailStyleTop(Grid table, String backgroundcolor, String borderColor, boolean wordwrap, boolean hasTopBackground, boolean hasBottomBackground)
	{
		VerticalPanel panel = new VerticalPanel();
		
		table.setBorderWidth(0);
		table.setCellPadding(1);
		table.setCellSpacing(1);
		
		for (int i = 0; i < table.getColumnCount(); i++) {
			int topCnt = 0;
			int rowCnt = table.getRowCount();
			if(hasTopBackground)
			{
				table.getCellFormatter().addStyleName(0, i, "topbar");
				table.getCellFormatter().setWordWrap(0, i, true);
				DOM.setStyleAttribute(table.getCellFormatter().getElement(0, i), "paddingLeft", "10px");
				DOM.setStyleAttribute(table.getCellFormatter().getElement(0, i), "paddingRight", "10px");
				DOM.setStyleAttribute(table.getCellFormatter().getElement(0, i), "fontWeight", "bold");
				DOM.setStyleAttribute(table.getCellFormatter().getElement(0, i), "height", "25px");
				topCnt = 1;
			}
			
			if(hasBottomBackground)
			{
				table.getCellFormatter().addStyleName(rowCnt-1, i, "bottombar");
				table.getCellFormatter().setWordWrap(rowCnt-1, i, true);
				DOM.setStyleAttribute(table.getCellFormatter().getElement(rowCnt-1, i), "paddingLeft", "10px");
				DOM.setStyleAttribute(table.getCellFormatter().getElement(rowCnt-1, i), "paddingRight", "10px");
				DOM.setStyleAttribute(table.getCellFormatter().getElement(rowCnt-1, i), "fontWeight", "bold");
				DOM.setStyleAttribute(table.getCellFormatter().getElement(rowCnt-1, i), "color", "#FF0000");
				DOM.setStyleAttribute(table.getCellFormatter().getElement(rowCnt-1, i), "height", "25px");
				rowCnt--;
			}
			
			for (int j = topCnt; j < rowCnt; j++) {
				DOM.setStyleAttribute(table.getCellFormatter().getElement(j, i),"backgroundColor", backgroundcolor);
				DOM.setStyleAttribute(table.getCellFormatter().getElement(j, i), "paddingLeft", "10px");
				DOM.setStyleAttribute(table.getCellFormatter().getElement(j, i), "paddingRight", "10px");
				table.getCellFormatter().setWordWrap(j, i, true);
			}
		}
		
		panel.add(table);
		table.setWidth("100%");
		panel.setWidth("100%");
		DOM.setStyleAttribute(panel.getElement(), "backgroundColor", borderColor);
		
		return panel;
	}
	
	
	public Widget getPrintTablePanel()
	{
		
		Widget w = new HTML("No information available");
		w.setWidth("100%");
		
		switch (listBox.getSelectedIndex()) {

		 case 1: 
			 Grid gridUserStat = getUserStat();
			 w = setTableConceptDetailStyleTopPrint(gridUserStat, true, false);
			 break;
			 
		 case 2:     
			 Grid gridStatPerLang = getStatPerLang();
			 w = setTableConceptDetailStyleTopPrint(gridStatPerLang, true, false);
			 break;
			 
		 case 3:     
			 Grid gridStatPerStatus = getStatPerStatus();
			 w = setTableConceptDetailStyleTopPrint(gridStatPerStatus, true, true);
			 break;
			 
		 case 4:
			 VerticalPanel statVP = new VerticalPanel();
			 statVP.setSize("100%", "100%");
			 statVP.add(new StatPerUser(true).getTable());	
			 w = statVP;
			 break;
			 
		 case 5:    
			 Grid gridActionPerUser = getActionPerUser();
			 w = setTableConceptDetailStyleTopPrint(gridActionPerUser, true, true);
			 break;
			 
		 case 6:     	
			Grid gridStatPerRelationship = getStatPerRelationship();
			 w = setTableConceptDetailStyleTopPrint(gridStatPerRelationship, true, true);
			 break;
			 
		 case 7:     
			Grid gridExportStat = getExportStat();
			 w = setTableConceptDetailStyleTopPrint(gridExportStat, true, true);
			 break;
			 
		 default: 
			 	w = new HTML("No information available");
			 	break;
		    }	
		
		return w;
	}
	
	public Grid getUserStat()
	{
		Grid grid = new Grid(3, 2);
		
		grid.getCellFormatter().setWidth(0, 0, "75%");
		grid.getCellFormatter().setWidth(0, 1, "25%");
		
		grid.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
		grid.getCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_CENTER);
		grid.getCellFormatter().setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_CENTER);
		
		grid.setWidget(0, 0, new HTML(constants.statUserInfo()));
		grid.setWidget(1, 0, new HTML(constants.statUserTotal()));
		grid.setWidget(2, 0, new HTML(constants.statUserLastConnect()));
		
		grid.setWidget(0, 1, new HTML(constants.statCount()));
		grid.setWidget(1, 1, new HTML(""+statData.getCountNumberOfUser()));
		grid.setWidget(2, 1, new HTML(""+statData.getCheckWhoLastConnected()));
		
		return grid;
	}
	
	public Grid getStatPerLang()
	{
		ArrayList<LanguageCode> langList = initData.getLanguageList();
		
		Grid grid = new Grid(langList.size()+1, 2);
		
		grid.getCellFormatter().setWidth(0, 0, "75%");
		grid.getCellFormatter().setWidth(0, 1, "25%");
		
		grid.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
		
		grid.setWidget(0, 0, new HTML(constants.statLanguage()));
		grid.setWidget(0, 1, new HTML(constants.statUserCount()));
		
		HashMap<String, Integer> upl = statData.getCountNumberOfUsersPerLanguage();
		
		for(int i =0; i<langList.size();i++)
		{
			LanguageCode lang = langList.get(i);
			grid.setWidget(i+1, 0, new HTML(""+lang.getLocalLanguage()+" ("+lang.getLanguageCode().toLowerCase()+")"));
			Integer cntObj = upl.get(lang.getLanguageCode().toLowerCase());
			int cnt = cntObj==null?0:cntObj;
			grid.setWidget(i+1, 1, new HTML(""+cnt));
			grid.getCellFormatter().setHorizontalAlignment(i+1, 1, HasHorizontalAlignment.ALIGN_CENTER);
		}

		return grid;
	}
	
	public Grid getStatPerStatus()
	{
		ArrayList<OwlStatus> statusList = initData.getStatusList();
		
		Grid grid = new Grid(statusList.size()+2, 3);
		
		grid.getCellFormatter().setWidth(0, 0, "50%");
		grid.getCellFormatter().setWidth(0, 1, "25%");
		grid.getCellFormatter().setWidth(0, 2, "25%");
		
		grid.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
		grid.getCellFormatter().setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_CENTER);

		
		grid.setWidget(0, 0, new HTML(constants.statStatus()));
		grid.setWidget(0, 1, new HTML(constants.statCountConcept()));
		grid.setWidget(0, 2, new HTML(constants.statCountTerm()));
		
		HashMap<String, Integer> tps = statData.getCountNumberOfTermPerStatus();
		HashMap<String, Integer> cps = statData.getCountNumberOfConceptPerStatus();
		
		int cpsTotal = 0;
		int tpsTotal = 0;
		
		for(int i =0; i<statusList.size();i++)
		{
			OwlStatus status = statusList.get(i);
			grid.setWidget(i+1, 0, new HTML(""+status.getStatus()));
			 
			int cpsCount = cps.get(status.getStatus())==null?0:cps.get(status.getStatus());
			int tpsCount = tps.get(status.getStatus())==null?0:tps.get(status.getStatus());
			
			cpsTotal += cpsCount;
			tpsTotal += tpsCount;
			
			grid.setWidget(i+1, 1, new HTML(""+cpsCount));
			grid.setWidget(i+1, 2, new HTML(""+tpsCount));
			
			grid.getCellFormatter().setHorizontalAlignment(i+1, 1, HasHorizontalAlignment.ALIGN_CENTER);
			grid.getCellFormatter().setHorizontalAlignment(i+1, 2, HasHorizontalAlignment.ALIGN_CENTER);
		}
		
		grid.setWidget(statusList.size()+1, 0, new HTML(constants.statTotal()));
		grid.setWidget(statusList.size()+1, 1, new HTML(""+cpsTotal));
		grid.setWidget(statusList.size()+1, 2, new HTML(""+tpsTotal));
		
		grid.getCellFormatter().setHorizontalAlignment(statusList.size()+1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		grid.getCellFormatter().setHorizontalAlignment(statusList.size()+1, 1, HasHorizontalAlignment.ALIGN_CENTER);
		grid.getCellFormatter().setHorizontalAlignment(statusList.size()+1, 2, HasHorizontalAlignment.ALIGN_CENTER);


		return grid;
	}
	
	public Grid getActionPerUser()
	{	
		ArrayList<Users> userList = initData.getUserList();
		
		Grid grid = new Grid(userList.size()+2, 4);
		
		grid.getCellFormatter().setWidth(0, 0, "30%");
		grid.getCellFormatter().setWidth(0, 1, "15%");
		grid.getCellFormatter().setWidth(0, 2, "15%");
		grid.getCellFormatter().setWidth(0, 3, "15%");		
		
		grid.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
		grid.getCellFormatter().setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_CENTER);
		grid.getCellFormatter().setHorizontalAlignment(0, 3, HasHorizontalAlignment.ALIGN_CENTER);
		
		
		grid.setWidget(0, 0, new HTML(constants.statUser()));
		grid.setWidget(0, 1, new HTML(constants.statCountRelationship()));
		grid.setWidget(0, 2, new HTML(constants.statCountConnection()));
		grid.setWidget(0, 3, new HTML(constants.statLastModification()));
				
		HashMap<Integer, Integer> rpu = statData.getCountNumberOfRelationshipsPerUsers();
		HashMap<Integer, Integer> cpu = statData.getCheckNumberOfConnectionPerUser();
		HashMap<Integer, String> lmpu = statData.getCheckNumberOfLastModificationPerUser();
				
		int rpuTotal = 0;
		int cpuTotal = 0;
		
		for(int i =0; i<userList.size();i++)
		{
			Users user = userList.get(i);
			grid.setWidget(i+1, 0, new HTML(user.getFirstName()+" "+user.getLastName()+" ("+user.getUsername()+")"));
						
			int rpuCount = rpu.get(user.getUserId())==null?0:rpu.get(user.getUserId());
			int cpuCount = cpu.get(user.getUserId())==null?0:cpu.get(user.getUserId());
			
			rpuTotal += rpuCount;
			cpuTotal += cpuCount;
			
			grid.setWidget(i+1, 1, new HTML(rpuCount+"&nbsp;"));
			grid.setWidget(i+1, 2, new HTML(cpuCount+"&nbsp;"));
			grid.setWidget(i+1, 3, new HTML(lmpu.get(user.getUserId())==null?"-":lmpu.get(user.getUserId())+"&nbsp;"));
			
			grid.getCellFormatter().setHorizontalAlignment(i+1, 1, HasHorizontalAlignment.ALIGN_CENTER);
			grid.getCellFormatter().setHorizontalAlignment(i+1, 2, HasHorizontalAlignment.ALIGN_CENTER);
			grid.getCellFormatter().setHorizontalAlignment(i+1, 3, HasHorizontalAlignment.ALIGN_CENTER);
		}
		
		grid.setWidget(userList.size()+1, 0, new HTML(constants.statTotal()));
		grid.setWidget(userList.size()+1, 1, new HTML(""+rpuTotal));
		grid.setWidget(userList.size()+1, 2, new HTML(""+cpuTotal));
		grid.setWidget(userList.size()+1, 3, new HTML("-"));
		
		grid.getCellFormatter().setHorizontalAlignment(userList.size()+1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		grid.getCellFormatter().setHorizontalAlignment(userList.size()+1, 1, HasHorizontalAlignment.ALIGN_CENTER);
		grid.getCellFormatter().setHorizontalAlignment(userList.size()+1, 2, HasHorizontalAlignment.ALIGN_CENTER);
		grid.getCellFormatter().setHorizontalAlignment(userList.size()+1, 3, HasHorizontalAlignment.ALIGN_CENTER);
		return grid;		
	}
	
	public class StatPerUser
	{		
		public ScrollTable theScrollTable;
		public FixedWidthGrid theDataTable;
		public boolean printable;
		
		StatPerUser(boolean printable)
		{
			this.printable = printable;
			FixedWidthFlexTable headerTable = createHeaderTable();		    			
			theScrollTable = new ScrollTable(createDataTable() , headerTable);
			theScrollTable.setSortPolicy(SortPolicy.DISABLED);
			theScrollTable.setScrollPolicy(ScrollPolicy.BOTH);
			
			theScrollTable.setCellPadding(3);
		    theScrollTable.setCellSpacing(0);
		    theScrollTable.setResizePolicy(ScrollTable.ResizePolicy.FILL_WIDTH);
		    
		    theScrollTable.setHeight("100%");		    
		    theScrollTable.setWidth("99%");
		    
		    
		    theScrollTable.setColumnWidth(0, 180);
		    theScrollTable.setPreferredColumnWidth(0, 180);
		    theScrollTable.setMinimumColumnWidth(0, 180);
		    for(int i=1 ; i < 19 ; i++){
		    	theScrollTable.setPreferredColumnWidth(i, 30);		    	
		    	theScrollTable.setMinimumColumnWidth(i, 30);
		    	theScrollTable.setMaximumColumnWidth(i, 30);
		    }
		    
		    populateDataTable(statData);
		    		    
		    DOM.setStyleAttribute(headerTable.getRowFormatter().getElement(0), "color", "#1F71AD");
			DOM.setStyleAttribute(headerTable.getRowFormatter().getElement(1), "color", "#1F71AD");
			DOM.setStyleAttribute(headerTable.getRowFormatter().getElement(2), "color", "#1F71AD");
		    
		}
		protected FixedWidthFlexTable createHeaderTable()
		{		
			FixedWidthFlexTable headerTable = new FixedWidthFlexTable();			
			FlexCellFormatter headerFormatter = headerTable.getFlexCellFormatter();
			// Level 1 headers
			headerTable.setHTML(0, 0, constants.statUser());
		    headerFormatter.setRowSpan(0, 0, 3);
		    
		    headerTable.setHTML(0, 1, constants.statProposed());
		    headerFormatter.setColSpan(0, 1, 6);		    
		    headerFormatter.setHorizontalAlignment(0, 1,HasHorizontalAlignment.ALIGN_CENTER);
		    
		    headerTable.setHTML(0, 2, constants.statValidated());
		    headerFormatter.setColSpan(0, 2, 6);		    
		    headerFormatter.setHorizontalAlignment(0, 2,HasHorizontalAlignment.ALIGN_CENTER);
		    
		    headerTable.setHTML(0, 3, constants.statPublished());
		    headerFormatter.setColSpan(0, 3, 6);		    
		    headerFormatter.setHorizontalAlignment(0, 3,HasHorizontalAlignment.ALIGN_CENTER);		    
		    // Level 2 headers
		    for(int i=0,j=0; i <3 ; i++ )
		    {		    
		    	headerTable.setHTML(1, j, constants.statCreate());		    
		    	headerFormatter.setColSpan(1, j, 2);		    	
		    	headerFormatter.setHorizontalAlignment(1, j,HasHorizontalAlignment.ALIGN_CENTER);
		    	headerTable.setHTML(1, j+1, constants.statEdit());
		    	headerFormatter.setColSpan(1, j+1, 2);		    		    	
		    	headerFormatter.setHorizontalAlignment(1, j+1,HasHorizontalAlignment.ALIGN_CENTER);
		    	headerTable.setHTML(1, j+2, constants.statDelete());		    
		    	headerFormatter.setColSpan(1, j+2, 2);		    	
		    	headerFormatter.setHorizontalAlignment(1, j+2,HasHorizontalAlignment.ALIGN_CENTER);
		    	j = j+3;
		    }		    		    
		    // Level 3 headers				    
		    for(int i=0,j=0 ; i < 9 ; i++){
		    	headerTable.setWidget(2, j, new Image("images/concept_logo.gif"));		    	
		    	headerTable.setWidget(2, j+1, new Image("images/term-logo.gif"));			    	
		    	headerFormatter.setHorizontalAlignment(2, j,HasHorizontalAlignment.ALIGN_CENTER);
		    	headerFormatter.setHorizontalAlignment(2, j+1,HasHorizontalAlignment.ALIGN_CENTER);		    	
			    j = j + 2;
		    }		    		    
		    // Styling header		
		    if(!printable){
			    headerTable.getCellFormatter().setWordWrap(0, 0, true);	
			    headerTable.getRowFormatter().setStyleName(0, "topbar");
			    headerTable.getRowFormatter().setStyleName(1, "topbar");
			    headerTable.getRowFormatter().setStyleName(2, "topbar");
		    }
					    
		    return headerTable;
		}
		
		private FixedWidthGrid createDataTable() 
		{			
			theDataTable = new FixedWidthGrid(initData.getUserList().size()+1 , 19);
			return theDataTable;
		}
		
		private void populateDataTable(StatisticalData statData)
		{
			ArrayList<Users> userList = initData.getUserList();
			
			ObjectPerUserStat tc = statData.getCountNumberOfTermCreatedByUser();
		    ObjectPerUserStat te = statData.getCountNumberOfTermEditedByUser();
		    ObjectPerUserStat td = statData.getCountNumberOfTermDeletedByUser();
		    
		    ObjectPerUserStat cc = statData.getCountNumberOfConceptCreatedByUser();
		    ObjectPerUserStat ce = statData.getCountNumberOfConceptEditedByUser();
		    ObjectPerUserStat cd = statData.getCountNumberOfConceptDeletedByUser();
			
		    int[] totals = new int[18];
		  
			for(int i=0; i<userList.size();i++)
			{
				Users user = userList.get(i);
				theDataTable.setWidget(i, 0, new HTML(user.getFirstName()+" "+user.getLastName()+" ("+user.getUsername()+")"));				
				// add term stats
				theDataTable.setWidget(i, 2 , new HTML(tc.getCountProposed().get(user.getUserId())+"&nbsp;"));
				theDataTable.setWidget(i, 4 , new HTML(te.getCountProposed().get(user.getUserId())+"&nbsp;"));
				theDataTable.setWidget(i, 6 , new HTML(td.getCountProposed().get(user.getUserId())+"&nbsp;"));
				
				theDataTable.setWidget(i, 8 , new HTML(tc.getCountValidated().get(user.getUserId())+"&nbsp;"));
				theDataTable.setWidget(i, 10 , new HTML(te.getCountValidated().get(user.getUserId())+"&nbsp;"));
				theDataTable.setWidget(i, 12 , new HTML(td.getCountValidated().get(user.getUserId())+"&nbsp;"));
				
				theDataTable.setWidget(i, 14 , new HTML(tc.getCountPublished().get(user.getUserId())+"&nbsp;"));
				theDataTable.setWidget(i, 16 , new HTML(te.getCountPublished().get(user.getUserId())+"&nbsp;"));
				theDataTable.setWidget(i, 18 , new HTML(td.getCountPublished().get(user.getUserId())+"&nbsp;"));				
				// add concept stats
				theDataTable.setWidget(i, 1 , new HTML(cc.getCountProposed().get(user.getUserId())+"&nbsp;"));
				theDataTable.setWidget(i, 3 , new HTML(ce.getCountProposed().get(user.getUserId())+"&nbsp;"));
				theDataTable.setWidget(i, 5 , new HTML(cd.getCountProposed().get(user.getUserId())+"&nbsp;"));
				
				theDataTable.setWidget(i, 7 , new HTML(cc.getCountValidated().get(user.getUserId())+"&nbsp;"));
				theDataTable.setWidget(i, 9 , new HTML(ce.getCountValidated().get(user.getUserId())+"&nbsp;"));
				theDataTable.setWidget(i, 11 , new HTML(cd.getCountValidated().get(user.getUserId())+"&nbsp;"));
				
				theDataTable.setWidget(i, 13 , new HTML(cc.getCountPublished().get(user.getUserId())+"&nbsp;"));
				theDataTable.setWidget(i, 15 , new HTML(ce.getCountPublished().get(user.getUserId())+"&nbsp;"));
				theDataTable.setWidget(i, 17 , new HTML(cd.getCountPublished().get(user.getUserId())+"&nbsp;"));
								
				totals[0] += cc.getCountProposed().get(user.getUserId())==null? 0: cc.getCountProposed().get(user.getUserId());
				totals[1] += tc.getCountProposed().get(user.getUserId())==null? 0: tc.getCountProposed().get(user.getUserId());
				
				totals[2] += ce.getCountProposed().get(user.getUserId())==null? 0: ce.getCountProposed().get(user.getUserId());
				totals[3] += te.getCountProposed().get(user.getUserId())==null? 0: te.getCountProposed().get(user.getUserId());
				
				totals[4] += cd.getCountProposed().get(user.getUserId())==null? 0: cd.getCountProposed().get(user.getUserId());
				totals[5] += td.getCountProposed().get(user.getUserId())==null? 0: td.getCountProposed().get(user.getUserId());
								
				totals[6] += cc.getCountValidated().get(user.getUserId())==null? 0: cc.getCountValidated().get(user.getUserId());
				totals[7] += tc.getCountValidated().get(user.getUserId())==null? 0: tc.getCountValidated().get(user.getUserId());
				
				totals[8] += ce.getCountValidated().get(user.getUserId())==null? 0: ce.getCountValidated().get(user.getUserId());
				totals[9] += te.getCountValidated().get(user.getUserId())==null? 0: te.getCountValidated().get(user.getUserId());
				
				totals[10] += cd.getCountValidated().get(user.getUserId())==null? 0: cd.getCountValidated().get(user.getUserId());
				totals[11] += td.getCountValidated().get(user.getUserId())==null? 0: td.getCountValidated().get(user.getUserId());
				
				totals[12] += cc.getCountPublished().get(user.getUserId())==null? 0: cc.getCountPublished().get(user.getUserId());
				totals[13] += tc.getCountPublished().get(user.getUserId())==null? 0: tc.getCountPublished().get(user.getUserId());
															
				totals[14] += ce.getCountPublished().get(user.getUserId())==null? 0: ce.getCountPublished().get(user.getUserId());
				totals[15] += te.getCountPublished().get(user.getUserId())==null? 0: te.getCountPublished().get(user.getUserId());
								
				totals[16] += cd.getCountPublished().get(user.getUserId())==null? 0: cd.getCountPublished().get(user.getUserId());
				totals[17] += td.getCountPublished().get(user.getUserId())==null? 0: td.getCountPublished().get(user.getUserId());
				for(int j =0 ; j < 18 ; j++)
					theDataTable.getCellFormatter().setHorizontalAlignment(i , j+1 , HasHorizontalAlignment.ALIGN_CENTER);								
			}
			theDataTable.setWidget(userList.size(),0, new HTML(constants.statTotal()));
			theDataTable.getCellFormatter().setHorizontalAlignment(userList.size(), 0 , HasHorizontalAlignment.ALIGN_RIGHT);
			if(!printable){
				DOM.setStyleAttribute(theDataTable.getRowFormatter().getElement(userList.size()), "color", "#FF0000");
				theDataTable.getRowFormatter().addStyleName(userList.size() , "bottombar");
			}
			DOM.setStyleAttribute(theDataTable.getRowFormatter().getElement(userList.size()), "fontWeight", "bold");
			for(int i=0 ; i < 18 ; i++){
				theDataTable.setWidget(userList.size(), i+1, new HTML(""+totals[i]));
				theDataTable.getCellFormatter().setHorizontalAlignment(userList.size(), i+1, HasHorizontalAlignment.ALIGN_CENTER);
			}			
		}
		
		public ScrollTable getTable(){			
			return theScrollTable;
		}
		
	}
	
	public Grid getStatPerRelationship()
	{
		ArrayList<RelationshipObject> relList = initData.getRelationshipList();
		
		Grid grid = new Grid(relList.size()+2, 4);
		grid.setWidget(0, 0, new HTML(constants.statRelationship()));
		grid.setWidget(0, 1, new HTML(constants.statDomain()));
		grid.setWidget(0, 2, new HTML(constants.statRange()));
		grid.setWidget(0, 3, new HTML(constants.statCountConcept()));
		
		grid.getCellFormatter().setWidth(0, 0, "25%");
		grid.getCellFormatter().setWidth(0, 1, "25%");
		grid.getCellFormatter().setWidth(0, 2, "25%");
		grid.getCellFormatter().setWidth(0, 3, "25%");
		
		grid.getCellFormatter().setHorizontalAlignment(0, 3, HasHorizontalAlignment.ALIGN_CENTER);
		
		
		HashMap<String, HashMap<String, ArrayList<ClassObject>>> list = statData.getListTheDomainAndRangeForRelationship();
		HashMap<String, Integer> cpr = statData.getCountNumberOfConceptPerRelationship();
		
		
		
		int cprTotal = 0;
		
		for(int i =0; i<relList.size();i++)
		{
			RelationshipObject rObj = relList.get(i);
			
			grid.setWidget(i+1, 0, new HTML(LazyLoadingTree.getRelationshipLabel(rObj)));
		
			if(list!=null)
			{
			
				HashMap<String, ArrayList<ClassObject>> clsObjList = list.get(rObj.getUri());
				
				if(clsObjList!=null)
				{
				
					ArrayList<ClassObject> domainlist = clsObjList.get("domain");
					if(domainlist!=null)
					{
						FlexTable domaintable = new FlexTable();
						domaintable.setSize("100%", "100%");
						int domainCnt = 0;
						for(Iterator<ClassObject> it = domainlist.iterator();it.hasNext();)
						{
							ClassObject clsObj = it.next();
							domaintable.setWidget(domainCnt, 0, new HTML(""+clsObj.getLabel()));
							domainCnt++;
						}
						grid.setWidget(i+1, 1, domaintable);
						grid.getCellFormatter().setHorizontalAlignment(i+1, 1, HasHorizontalAlignment.ALIGN_CENTER);
					}
					
					ArrayList<ClassObject> rangelist = clsObjList.get("range");
					if(rangelist!=null)
					{
						FlexTable rangetable = new FlexTable();
						rangetable.setSize("100%", "100%");
						int rangeCnt = 0;
						for(Iterator<ClassObject> it = rangelist.iterator();it.hasNext();)
						{
							ClassObject clsObj = it.next();
							rangetable.setWidget(rangeCnt, 0, new HTML(""+clsObj.getLabel()));
							rangeCnt++;
						}
						grid.setWidget(i+1, 2, rangetable);
						grid.getCellFormatter().setHorizontalAlignment(i+1, 2, HasHorizontalAlignment.ALIGN_CENTER);
					}
				}
				
				
			}
			int cprcount = cpr.get(rObj.getUri())==null?0:cpr.get(rObj.getUri());
			cprTotal += cprcount;
			if(cpr!=null)	grid.setWidget(i+1, 3, new HTML(""+cprcount));
			grid.getCellFormatter().setHorizontalAlignment(i+1, 3, HasHorizontalAlignment.ALIGN_CENTER);
			
			
			
		}
		
		grid.setWidget(relList.size()+1, 0, new HTML(constants.statTotal()));
		grid.setWidget(relList.size()+1, 1, new HTML("-"));
		grid.setWidget(relList.size()+1, 2, new HTML("-"));
		grid.setWidget(relList.size()+1, 3, new HTML(""+cprTotal));
		
		grid.getCellFormatter().setHorizontalAlignment(relList.size()+1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		grid.getCellFormatter().setHorizontalAlignment(relList.size()+1, 1, HasHorizontalAlignment.ALIGN_CENTER);
		grid.getCellFormatter().setHorizontalAlignment(relList.size()+1, 2, HasHorizontalAlignment.ALIGN_CENTER);
		grid.getCellFormatter().setHorizontalAlignment(relList.size()+1, 3, HasHorizontalAlignment.ALIGN_CENTER);

		return grid;
	}
	
	public Grid getExportStat()
	{
		HashMap<String,Integer> exportStat = statData.getCountNumberOfExports();
				
		Grid grid = new Grid(exportStat.size() + 2 , 2);
		grid.setWidget(0, 0, new HTML(constants.statExportFormat()));
		grid.setWidget(0, 1, new HTML(constants.statCount()));
		
		grid.getCellFormatter().setWidth(0, 0, "25%");
		grid.getCellFormatter().setWidth(0, 1, "75%");
				
		grid.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
		grid.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);

		
		Iterator<String> iter = exportStat.keySet().iterator();		
		int i = 0;
		int tot = 0;
		while(iter.hasNext()) 
		{
			String key = (String) iter.next();
			int val = exportStat.get(key);
			tot += val;
			grid.setWidget(i+1, 0, new HTML(key));
			grid.setWidget(i+1, 1, new HTML(""+val));
			grid.getCellFormatter().setHorizontalAlignment(i+1, 0, HasHorizontalAlignment.ALIGN_LEFT);
			grid.getCellFormatter().setHorizontalAlignment(i+1, 1, HasHorizontalAlignment.ALIGN_CENTER);
			i++;
		}		
		grid.setWidget(i+1, 0, new HTML(constants.statTotal()));
		grid.setWidget(i+1, 1, new HTML(""+tot));
		grid.getCellFormatter().setHorizontalAlignment(i+1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		grid.getCellFormatter().setHorizontalAlignment(i+1, 1, HasHorizontalAlignment.ALIGN_CENTER);
		return grid;
	}
	
	
	public  VerticalPanel setTableStyle(FlexTable table,String backgroundcolor, String alternateRowcolor, String borderColor, boolean wordwrap){
		VerticalPanel panel = new VerticalPanel();
		
		table.setBorderWidth(0);
		table.setCellPadding(1);
		table.setCellSpacing(1);
		
		int colCount = 0;
		if(table.getRowCount()>0) colCount = table.getCellCount(0);
		
		
		for (int i = 0; i < colCount; i++) {
			
			for (int j = 0; j < table.getRowCount(); j++) {
				String bgcolor = backgroundcolor;
				if(alternateRowcolor!= null)
				{
					if(j % 2 == 0)	bgcolor = alternateRowcolor;
				}
				DOM.setStyleAttribute(table.getCellFormatter().getElement(j, i),"backgroundColor", bgcolor);
				if(colCount>1)table.getCellFormatter().setHorizontalAlignment(j, colCount-1, HasHorizontalAlignment.ALIGN_CENTER);
				table.getCellFormatter().setWordWrap(j, i, wordwrap);
				table.getCellFormatter().setHeight(j, i, "25px");
			}
		}
		
		panel.add(table);
		table.setSize("100%", "100%");
		panel.setSize("100%", "100%");
		DOM.setStyleAttribute(panel.getElement(), "background", borderColor);
		
		return panel;
	}
	
	public HorizontalPanel getStatList(){
	

		listBox.setTitle(constants.statSelectAnyStat());
		listBox.addItem(constants.statSelectStat());
		listBox.addItem(constants.statUserStat());
		listBox.addItem(constants.statPerLang());
		listBox.addItem(constants.statPerStatus());
		listBox.addItem(constants.statPerUser());
		listBox.addItem(constants.statActionPerUser());
		listBox.addItem(constants.statPerRelationship());
		listBox.addItem(constants.statExportStat());

		
		
		listBox.addChangeHandler(new ChangeHandler(){
			public void onChange(ChangeEvent event) {
				getData(listBox.getSelectedIndex());
			}
		});
		
		Image reload = new Image("images/reload-grey.gif");
		reload.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				reload();
			}
		});
		reload.setTitle(constants.statReload());
		reload.setStyleName(Style.Link);
		
		HorizontalPanel leftTopPanel = new HorizontalPanel();
		leftTopPanel.add(reload);
		leftTopPanel.add(new Spacer("15px", "100%"));
		leftTopPanel.add(listBox);
		leftTopPanel.setCellHeight(listBox, "100%");
		leftTopPanel.setCellWidth(listBox, "100%");
		leftTopPanel.setCellVerticalAlignment(listBox, HasVerticalAlignment.ALIGN_MIDDLE);
		leftTopPanel.setCellHorizontalAlignment(listBox, HasHorizontalAlignment.ALIGN_RIGHT);
		leftTopPanel.setSpacing(1);
		
		return leftTopPanel;
		
		
	}
	
	public HorizontalPanel getPrintPanel()
	{
		Image printImg = new Image("images/printerfriendly.png");
		printImg.setSize("16", "16");
		final HTML printerfriendly = new HTML("&nbsp;&nbsp;"+constants.statPrinterFriendly());
		printerfriendly.setSize("180", "100%");
		printerfriendly.addStyleName("cursor-hand");
		printerfriendly.addClickHandler(new ClickHandler() {
			public void onClick(final ClickEvent event) {
				if(initData!=null)
				{
					Scheduler.get().scheduleDeferred(new Command() {
						public void execute() {
							if(ppdb == null || !ppdb.isLoaded)
								ppdb = new PrintPreviewDialogBox();
							ppdb.setPrintWidget(getPrintTablePanel());
							ppdb.show();
						}
				    	
				    });
				}
			}
		});

		HorizontalPanel printPanel = new HorizontalPanel();
		printPanel.add(printImg);
		printPanel.add(printerfriendly);
		printPanel.add(new Spacer("15px","100%"));
		printPanel.setCellWidth(printImg, "100%");
		printPanel.setCellHeight(printImg, "100%");
		printPanel.setCellWidth(printerfriendly, "100%");
		printPanel.setCellHeight(printerfriendly, "100%");
		printPanel.setCellVerticalAlignment(printImg, HasVerticalAlignment.ALIGN_MIDDLE);
		printPanel.setCellVerticalAlignment(printerfriendly, HasVerticalAlignment.ALIGN_MIDDLE);
		printPanel.setCellHorizontalAlignment(printImg, HasHorizontalAlignment.ALIGN_RIGHT);
		printPanel.setCellHorizontalAlignment(printerfriendly, HasHorizontalAlignment.ALIGN_LEFT);
		printPanel.setSpacing(1);
		
		return printPanel;
	}
	

} 