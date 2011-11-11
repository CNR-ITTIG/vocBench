package org.fao.aoscs.client.module.consistency;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.consistency.widgetlib.ConsistencyCheck;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.domain.ConsistencyInitObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Consistency extends Composite{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private VerticalPanel cpanel = new VerticalPanel();
	private ConsistencyCheck consistencyCheck = new ConsistencyCheck();
	private ArrayList<String> statusList = new ArrayList<String>();
	private ArrayList<String> termCodePropertyList = new ArrayList<String>();
	private ArrayList<String[]> languageList = new ArrayList<String[]>();
	
	public Consistency() {
		init();
		cpanel.add(consistencyCheck);
		consistencyCheck.setVisible(false);
		cpanel.setCellWidth(consistencyCheck, "100%");
		cpanel.setCellHeight(consistencyCheck, "100%");
		cpanel.setSpacing(5);
		cpanel.setSize("100%", "100%");
		cpanel.setCellHorizontalAlignment(consistencyCheck, HasHorizontalAlignment.ALIGN_CENTER);
		cpanel.setCellVerticalAlignment(consistencyCheck, HasVerticalAlignment.ALIGN_MIDDLE);
		initWidget(cpanel);
	}
	
	private void init() {
		initLoading();
		getinitData();
	}
	
	public void getinitData()
	{
		AsyncCallback<Object> callback = new AsyncCallback<Object>(){
			public void onSuccess(Object result) {
				ConsistencyInitObject cio  = (ConsistencyInitObject) result;
				statusList = cio.getStatus();
				termCodePropertyList = cio.getTermCode();
				languageList = MainApp.getLanguage();
				consistencyCheck.init(new HashMap<String, org.fao.aoscs.domain.Consistency>(), statusList, termCodePropertyList, languageList, 0);
				cpanel.clear();
				cpanel.add(consistencyCheck);
				consistencyCheck.setVisible(true);
				
			}
			public void onFailure(Throwable caught) {
				Window.alert(constants.conLoadInitDataFail());
			}
		};
		Service.consistencyService.getInitData(MainApp.userOntology, callback);
	}
	
	public void filterByLanguage(){
		consistencyCheck.filterByLanguage();
	}
	
	public void initLoading(){
		cpanel.clear();
		cpanel.setSize("100%","100%");
		LoadingDialog load = new LoadingDialog();
		cpanel.add(load);
		cpanel.setCellHorizontalAlignment(load, HasHorizontalAlignment.ALIGN_CENTER);
		cpanel.setCellVerticalAlignment(load, HasVerticalAlignment.ALIGN_MIDDLE);
		cpanel.setCellHeight(load, "100%");
	}
}
