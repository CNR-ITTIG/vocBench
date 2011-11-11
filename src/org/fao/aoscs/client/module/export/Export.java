package org.fao.aoscs.client.module.export;
 
import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.export.widgetlib.ExportOptionTable;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.domain.InitializeExportData;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Export extends Composite {
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private VerticalPanel panel = new VerticalPanel();
	
	public Export(){
		
		LoadingDialog sayLoading = new LoadingDialog();
		panel.add(sayLoading);
		panel.setSize("100%", "100%");
		panel.setCellHorizontalAlignment(sayLoading , HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(sayLoading, HasVerticalAlignment.ALIGN_MIDDLE);
		panel.setCellHeight(sayLoading, "100%");
		
		AsyncCallback<Object> callback = new AsyncCallback<Object>(){
			public void onSuccess(Object results){
				panel.clear();
				panel.setSize("100%", "100%");
				InitializeExportData initData = (InitializeExportData) results;
				ExportOptionTable table = new ExportOptionTable(initData);
				panel.add(table);
				panel.setCellHorizontalAlignment(table, HasHorizontalAlignment.ALIGN_CENTER);
				panel.setCellVerticalAlignment(table, HasVerticalAlignment.ALIGN_MIDDLE);
			}
			public void onFailure(Throwable caught){
				Window.alert(constants.exportInitFail());
			}
		};
		Service.exportService.initData(MainApp.userOntology, callback);
		
		initWidget(panel);
	}

}
