package org.fao.aoscs.client.module.comment;


import java.util.ArrayList;

import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.domain.UserComments;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CommentViewer extends Composite{

	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private VerticalPanel panel = new VerticalPanel();
	private VerticalPanel panelAbout = new VerticalPanel();
	private VerticalPanel panelSearch = new VerticalPanel();
	private VerticalPanel panelConcept = new VerticalPanel();
	private VerticalPanel panelRelationship = new VerticalPanel();
	private VerticalPanel panelClassification = new VerticalPanel();
	private VerticalPanel panelValidation = new VerticalPanel();
	private VerticalPanel panelImportData = new VerticalPanel();
	private VerticalPanel panelExportData = new VerticalPanel();
	private VerticalPanel panelConsistency = new VerticalPanel();
	private VerticalPanel panelStatistic = new VerticalPanel();
	private VerticalPanel panelUsersmanage = new VerticalPanel();
	private VerticalPanel panelGroupsmanage = new VerticalPanel();
	private VerticalPanel panelPreference = new VerticalPanel();
	private TabPanel tab = new DecoratedTabPanel();
	
	public CommentViewer(){
		
		populateTable(panelAbout, constants.commentAbout(), "home");
		populateTable(panelSearch, constants.commentSearch(), "search");
		populateTable(panelConcept, constants.commentConcepts(), "concept");
		populateTable(panelRelationship, constants.commentRelationships(), "relationship");
		populateTable(panelClassification, constants.commentSchemes(), "classification");
		populateTable(panelValidation, constants.commentValidation(), "validation");
		populateTable(panelImportData, constants.commentImport(), "importData");
		populateTable(panelExportData, constants.commentExport(), "exportData");
		populateTable(panelConsistency, constants.commentConsistency(), "consistency");
		populateTable(panelStatistic, constants.commentStatistics(), "statistic");
		populateTable(panelUsersmanage, constants.commentUsers(), "usersmanage");
		populateTable(panelGroupsmanage, constants.commentGroups(), "groupsmanage");
		populateTable(panelPreference, constants.commentPreferences(), "Preference");
		tab.setSize("99%","100%");
		tab.getDeckPanel().setSize("99%", "100%");
		tab.setAnimationEnabled(true);
		tab.selectTab(0);
		panel.add(tab);
		panel.setSpacing(10);
		panel.setSize("100%", "100%");
		panel.setCellHeight(tab, "100%");
		panel.setCellWidth(tab, "100%");
		panel.setCellHorizontalAlignment(tab, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(tab, HasVerticalAlignment.ALIGN_MIDDLE);
		initWidget(panel);

	}
	
	public void populateTable(final VerticalPanel vpanel, final String tabLabel, final String module)
	{
		vpanel.setSpacing(10);
		tab.add(vpanel, tabLabel);
		initLoading(vpanel);
		AsyncCallback<ArrayList<UserComments>> callback = new AsyncCallback<ArrayList<UserComments>>(){
			public void onSuccess(ArrayList<UserComments> list) {
				vpanel.clear();
				vpanel.add(makeTable(list));
			}

			public void onFailure(Throwable caught) {
				Window.alert(constants.commentListError());
			}
		};
		Service.commentService.getComments(module.toLowerCase(), callback);
	}
	
	public Grid makeTable(ArrayList<UserComments> list)
	{
		Grid table = new Grid(list.size()+1,4);
		table.setWidth("100%");
		
		table.setWidget(0,0,new HTML(constants.commentDate()));
		table.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		table.getCellFormatter().setWidth(0, 0, "150px");
		
		table.setWidget(0,1,new HTML(constants.commentComments()));
		table.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
		
		table.setWidget(0,2,new HTML(constants.commentName()));
		table.getCellFormatter().setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_CENTER);
		table.getCellFormatter().setWidth(0, 2, "100px");
		
		table.setWidget(0,3,new HTML(constants.commentEmail()));
		table.getCellFormatter().setHorizontalAlignment(0, 3, HasHorizontalAlignment.ALIGN_CENTER);
		table.getCellFormatter().setWidth(0, 3, "200px");

		for(int i=0;i<list.size();i++){
			
			UserComments uc = (UserComments)list.get(i);
			
			DateTimeFormat sdf = DateTimeFormat.getFormat("dd-MM-yyyy HH:mm:ss");
			table.setWidget(i+1,0,new HTML("&nbsp;"+sdf.format(uc.getCommentDate())));
			table.setWidget(i+1,1,new HTML("&nbsp;"+uc.getCommentDescription()));
			table.setWidget(i+1,2,new HTML("&nbsp;"+uc.getName()));
			table.setWidget(i+1,3,new HTML("&nbsp;"+uc.getEmail()));
		}
		GridStyle.setTableConceptDetailStyleTop(table,"gstFR1","gstR1","gstR1","gstPanel1",true);
		DOM.setStyleAttribute(table.getElement(), "background", "#D1D1D1");
		return table;
	}
	
	public void initLoading(VerticalPanel panel){
		clearPanel(panel);
		panel.add(new LoadingDialog());
		panel.setCellHorizontalAlignment(panel, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(panel, HasVerticalAlignment.ALIGN_MIDDLE);
	}

	public void clearPanel(VerticalPanel panel){
		panel.clear();
		panel.setSize("100%","100%");
	}
}
