package org.fao.aoscs.client.module.concept.widgetlib;

import java.util.ArrayList;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.classification.widgetlib.ClassificationDetailTab;
import org.fao.aoscs.client.module.concept.ConceptTemplate;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.domain.ConceptObject;
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

public class ConceptInformation extends ConceptTemplate{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	ConceptHistoryTable rcTable = new ConceptHistoryTable();
	
	private ArrayList<Users> userList = new ArrayList<Users>();
	private ArrayList<OwlAction> actionList = new ArrayList<OwlAction>();


	public ConceptInformation(PermissionObject permisstionTable,InitializeConceptData initData, ConceptDetailTabPanel conceptDetailPanel, ClassificationDetailTab classificationDetailPanel){
		super(permisstionTable,initData, conceptDetailPanel, classificationDetailPanel);
	}

	public void initLayout(){
		this.sayLoading();
		conceptRootPanel.setSpacing(3);
		functionPanel.setVisible(false);
		if(cDetailObj!=null && cDetailObj.getInformationObject()!=null)
		{
			initData(cDetailObj.getInformationObject());
		}
		else
		{
			Service.conceptService.getConceptInformation(conceptObject.getName(), MainApp.userOntology,  new AsyncCallback<InformationObject>(){
				public void onSuccess(InformationObject iObj){
					cDetailObj.setInformationObject(iObj);
					initData(iObj);
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.conceptMakeConceptInfoPanelFail());
				}
			});
		}
		
	}

	public void initData(InformationObject iObj)
	{
		final Grid table = new Grid(3,2);
		table.setSize("100%", "100%");
		table.setWidget(0, 0, new HTML(constants.conceptCreateDate()));
		table.setWidget(0, 1, new HTML(iObj.getCreateDate()));
		table.setWidget(1, 0, new HTML(constants.conceptUpdateDate()));
		table.setWidget(1, 1, new HTML(iObj.getUpdateDate()));
		table.setWidget(2, 0, new HTML(constants.conceptStatus()));
		table.setWidget(2, 1, new HTML(iObj.getStatus()));
	
		Service.conceptService.getConceptHistoryInitData(conceptObject.getUri() , MainApp.userOntology.getOntologyId() , 1 , new AsyncCallback<RecentChangesInitObject>() 
		{
			public void onSuccess(RecentChangesInitObject result) 
			{
				initTable(result, table);
			}
			public void onFailure(Throwable caught) {
				GWT.log(caught.getLocalizedMessage()+"  : GETTING RECENT CHANGES DATA FAIL", null);
			}
		});
	}
	
	public void initTable(RecentChangesInitObject list, Grid table)
	{
		clearPanel(); // clear loading message
		userList = list.getUsers();
		actionList = list.getActions();
		
		rcTable = new ConceptHistoryTable();
		
		rcTable.setTable(userList, actionList, list.getSize(),conceptObject.getUri(),1);						
		
		FlexTable tb = rcTable.getLayout();
		conceptRootPanel.setSize("100%", "100%");
		conceptRootPanel.add(GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1"));
		conceptRootPanel.setCellHorizontalAlignment(table, HasHorizontalAlignment.ALIGN_CENTER);
		
		VerticalPanel spacer = new VerticalPanel();
		conceptRootPanel.add(spacer);
		conceptRootPanel.add(tb);
		
		conceptRootPanel.setCellHeight(spacer, "10px");
		conceptRootPanel.setCellHeight(tb, "100%");						
		conceptRootPanel.setCellWidth(tb, "100%");
		
		if(conceptObject.getBelongsToModule()==ConceptObject.CONCEPTMODULE) conceptDetailPanel.tabPanel.getTabBar().setTabHTML(InfoTab.history, Convert.replaceSpace(constants.conceptHistory())+"&nbsp;("+(list.getSize())+")");
		if(conceptObject.getBelongsToModule()==ConceptObject.CLASSIFICATIONMODULE) classificationDetailPanel.tab2Panel.getTabBar().setTabHTML(InfoTab.history, Convert.replaceSpace(constants.conceptHistory())+"&nbsp;("+(list.getSize())+")");
	
	}
}
