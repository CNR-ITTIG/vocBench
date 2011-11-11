package org.fao.aoscs.client.module.document;

import java.util.ArrayList;

import net.sf.gilead.pojo.gwt.LightEntity;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.module.document.widgetlib.RecentChangesTable;
import org.fao.aoscs.client.module.validation.widgetlib.Validator;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.panel.BodyPanel;
import org.fao.aoscs.domain.OwlAction;
import org.fao.aoscs.domain.RecentChanges;
import org.fao.aoscs.domain.RecentChangesInitObject;
import org.fao.aoscs.domain.Users;
import org.fao.aoscs.domain.Validation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class About extends Composite{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private VerticalPanel recentChangesPanel   = new VerticalPanel();
	RecentChangesTable rcTable = new RecentChangesTable();
	private ArrayList<Users> userList = new ArrayList<Users>();
	private ArrayList<OwlAction> actionList = new ArrayList<OwlAction>();
	private VerticalPanel mainPanel = new VerticalPanel();	

	public About(){
		initData();
	    initWidget(mainPanel);
	}
	
	public void initData()
	{
		initLoadingTable();
		
		Service.validationService.getRecentChangesInitData(MainApp.userOntology.getOntologyId(), new AsyncCallback<RecentChangesInitObject>() {
			public void onSuccess(RecentChangesInitObject list) {
				userList = list.getUsers();
				actionList = list.getActions();
				
				VerticalPanel bodyPanel = new VerticalPanel();
				recentChangesPanel.setSize("100%", "100%");
				bodyPanel.add(recentChangesPanel);	
				bodyPanel.setSpacing(10);
				bodyPanel.setCellHeight(recentChangesPanel, "100%");
				bodyPanel.setCellWidth(recentChangesPanel, "100%");
				bodyPanel.setSize("100%", "100%");
				bodyPanel.setCellVerticalAlignment(recentChangesPanel, HasVerticalAlignment.ALIGN_TOP);
				bodyPanel.setCellHorizontalAlignment(recentChangesPanel, HasHorizontalAlignment.ALIGN_CENTER);
				
				BodyPanel aboutPanel = new BodyPanel(constants.homeRecentChanges() , bodyPanel , null);				
				mainPanel.clear();
				mainPanel.setSize("100%", "100%");
				mainPanel.add(aboutPanel);
				mainPanel.setCellHeight(aboutPanel, "100%");
				mainPanel.setCellWidth(aboutPanel, "100%");
				mainPanel.setCellHorizontalAlignment(aboutPanel, HasHorizontalAlignment.ALIGN_CENTER);
				mainPanel.setCellVerticalAlignment(aboutPanel, HasVerticalAlignment.ALIGN_MIDDLE);
				
				populateTableModel(list.getSize());
			}
			public void onFailure(Throwable caught) {
				Window.alert(constants.homeRCDataFail());
			}
		});				
	}
	

	public void reLoad()
	{
		initLoadingTable();
		Service.validationService.getRecentChangesSize(MainApp.userOntology.getOntologyId(), new AsyncCallback<Integer>() {
			public void onSuccess(Integer size) {
				populateTableModel(size);
			}

			public void onFailure(Throwable caught) {
				Window.alert(constants.homeRCDataFail());
			}
		});
	}
	
	public void initLoadingTable(){
		LoadingDialog loadingDialog = new LoadingDialog();
		mainPanel.clear();
		mainPanel.setSize("100%", "100%");
		mainPanel.add(loadingDialog);
		mainPanel.setCellHorizontalAlignment(loadingDialog, HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.setCellVerticalAlignment(loadingDialog, HasVerticalAlignment.ALIGN_MIDDLE);
		
	}
	
	public void filterByLanguage()
	{
		for(int i=0;i<rcTable.getDataTable().getRowCount();i++){
			RecentChanges rc = (RecentChanges)rcTable.getPagingScrollTable().getRowValue(i);
			for(int j=0;j<3;j++)
			{
				Widget w = getTablePanel(j, Style.Link, rc.getModifiedObject());
				w.addStyleName("gwt-NoBorder");
				rcTable.getDataTable().setWidget(i, j, w);
			}
		}
	}
	
	public void populateTableModel(int size)
	{
		
		rcTable = new RecentChangesTable();
		rcTable.setTable(userList, actionList, size);
	    recentChangesPanel.clear();
	    recentChangesPanel.add(rcTable.getLayout());
	    recentChangesPanel.setCellWidth(rcTable.getLayout(), "100%");
	}
	
	public Widget getTablePanel(int col, String link, ArrayList<LightEntity> arrayList)
	{
		if(arrayList.size()>0)
		{
			Object obj = arrayList.get(0);
			if(obj instanceof Validation)
			{
				Validation v = (Validation) obj;
				return Validator.getLabelPanel(col, v, Style.Link);
			}
		}
		return new HTML();
	}
}
	
