package org.fao.aoscs.client.module.logging;

import java.util.ArrayList;

import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.utility.TimeConverter;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.panel.BodyPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LogViewer extends Composite{
	
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private VerticalPanel panel = new VerticalPanel();
	private VerticalPanel table = new VerticalPanel();
	private VerticalPanel tPanel = new VerticalPanel();
	private VerticalPanel vPanel = new VerticalPanel();
	
	UsersVisitsTable visitLogTable = new UsersVisitsTable();
	public LogViewer(){
		populateTable();
		BodyPanel bodyPanel = new BodyPanel(constants.logSiteStatistics() , table , null);
		
		
		panel.clear();
		panel.setSize("100%", "100%");
		panel.add(bodyPanel);
		panel.setCellHeight(bodyPanel, "100%");
		panel.setCellWidth(bodyPanel, "100%");
		panel.setCellHorizontalAlignment(bodyPanel, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(bodyPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		//panel.setSpacing(10);
		initWidget(panel);
	}
	
	public void populateTableModel(int size)
	{
		visitLogTable = new UsersVisitsTable();
		visitLogTable.setTable(size);
		tPanel.clear();
		tPanel.setSize("100%", "100%");
		tPanel.add(visitLogTable.getLayout());
		tPanel.setCellHeight(visitLogTable.getLayout(), "100%");
		tPanel.setCellWidth(visitLogTable.getLayout(), "100%");
	}
	
	public void populateTable()
	{
		table.setSize("100%", "100%");
		table.setSpacing(10);
		table.add(vPanel);
		table.add(tPanel);
		initLoading(vPanel);
		initLoading(tPanel);
		
		AsyncCallback<ArrayList<String>> callback = new AsyncCallback<ArrayList<String>>() {
		    public void onSuccess(ArrayList<String> list) {
		    	vPanel.clear();
		    	VerticalPanel panel = new VerticalPanel();
				panel.setWidth("350px");
				panel.setStyleName("statusbar");
				panel.setSpacing(5);								
				panel.add(new HTML("<b>"+constants.logTotalVisitors()+": </b>"+list.get(0)+"<BR><b>"+constants.logTotalDuration()+": </b>"+TimeConverter.ConvertSecsToTime2(""+list.get(1))));				
				vPanel.add(panel);
				tPanel.clear();
		    	populateTableModel(Integer.parseInt(list.get(2)));
		    	
		    }
		    public void onFailure(Throwable caught) {
		       	Window.alert(constants.logDataFail());
		    }
		 };
		 Service.loggingService.getLogInfo(callback);
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