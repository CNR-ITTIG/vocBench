package org.fao.aoscs.client.module.term.widgetlib;

import java.util.ArrayList;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.concept.widgetlib.ConceptHistoryTable;
import org.fao.aoscs.client.module.term.TermDetailTabPanel;
import org.fao.aoscs.client.module.term.TermTemplate;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.domain.InformationObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.OwlAction;
import org.fao.aoscs.domain.PermissionObject;
import org.fao.aoscs.domain.RecentChangesInitObject;
import org.fao.aoscs.domain.Users;

import com.google.gwt.core.client.GWT;
import com.google.gwt.gen2.table.override.client.FlexTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TermInformation extends TermTemplate{
	private ConceptHistoryTable rcTable = new ConceptHistoryTable();	
	private ArrayList<Users> userList = new ArrayList<Users>();
	private ArrayList<OwlAction> actionList = new ArrayList<OwlAction>();
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	public TermInformation(PermissionObject permissionTable, InitializeConceptData initData, TermDetailTabPanel termDetailPanel){
		super(permissionTable,initData, termDetailPanel);		
	}
	public void initLayout(){
		this.sayLoading();
		final Grid table = new Grid(3,2);
		AsyncCallback<Object> callback = new AsyncCallback<Object>(){
			public void onSuccess(Object results){
				clearPanel(); // clear loading message
				InformationObject iObj = (InformationObject) results;
																
				table.setWidget(0, 0, new HTML(constants.conceptCreateDate()));
				table.setWidget(0, 1, new HTML(iObj.getCreateDate()));
				table.setWidget(1, 0, new HTML(constants.conceptUpdateDate()));
				table.setWidget(1, 1, new HTML(iObj.getUpdateDate()));
				table.setWidget(2, 0, new HTML(constants.conceptStatus()));
				table.setWidget(2, 1, new HTML(iObj.getStatus()));
				
				table.setWidth("100%");
				
				Service.conceptService.getConceptHistoryInitData(termObject.getUri() , MainApp.userOntology.getOntologyId() , 2 , new AsyncCallback<RecentChangesInitObject>() 
				{
					public void onSuccess(RecentChangesInitObject list) 
					{
						clearPanel(); // clear loading message
						
						userList = list.getUsers();
						actionList = list.getActions();
															
						rcTable = new ConceptHistoryTable();						
						rcTable.setTable(userList, actionList, list.getSize(),termObject.getUri(),2);
						
						FlexTable tb = rcTable.getLayout();
						
						termRootPanel.add(GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1"));
						termRootPanel.setCellHorizontalAlignment(table, HasHorizontalAlignment.ALIGN_CENTER);
						
						VerticalPanel spacer = new VerticalPanel();
						termRootPanel.add(spacer);
						termRootPanel.add(tb);						
						
						termRootPanel.setCellHeight(spacer, "8px");
						termRootPanel.setCellWidth(tb, "100%");
						termRootPanel.setCellHeight(tb, "100%");
						
						termDetailPanel.tabPanel.getTabBar().setTabHTML(TermDetailTabPanel.history, constants.termHistory()+"&nbsp;("+list.getSize()+")");
					}
					public void onFailure(Throwable caught) {
						GWT.log(caught.getLocalizedMessage()+"  : GETTING RECENT CHANGES DATA FAIL", null);
					}
				});
				
			}
			public void onFailure(Throwable caught){
				Window.alert(constants.termGetTermInfoFail());
			}
		};
		Service.termService.getTermInformation(conceptObject.getName(),termObject.getName(), MainApp.userOntology, callback);
	}
	
}
