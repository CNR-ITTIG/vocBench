package org.fao.aoscs.client.module.classification;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.classification.widgetlib.ClassificationDetailTab;
import org.fao.aoscs.client.module.classification.widgetlib.ClassificationTree;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.PermissionObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class Classification extends Composite{
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private HorizontalPanel panel = new HorizontalPanel();
	public PermissionObject permisstionTable = new PermissionObject();
	public HashMap<String, OwlStatus> actionStatusMap = new HashMap<String, OwlStatus>();
	public HashMap<String, String> actionMap = new HashMap<String, String>();
	public ClassificationTree scheme;
	public ClassificationDetailTab tab;
	
	public Classification(final PermissionObject permissionTable){
		initClassification(permissionTable, null , null);
	}
	
	public Classification(final PermissionObject permissionTable, final String schemeURI, final String targetItem){
		initClassification(permissionTable, schemeURI, targetItem);
	}
	
	public void initClassification(final PermissionObject permissionTable,final String schemeURI,final String targetItem)
	{
		this.permisstionTable = permissionTable;
		panel.setSize("100%", "100%");
		
		LoadingDialog load = new LoadingDialog();
		panel.add(load);
		panel.setCellHorizontalAlignment(load,HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(load, HasVerticalAlignment.ALIGN_MIDDLE);
		
		AsyncCallback<Object> callback = new AsyncCallback<Object>(){
			public void onSuccess(Object results){
				InitializeConceptData initData = (InitializeConceptData) results;
				scheme = new ClassificationTree(permissionTable, initData, schemeURI, targetItem);
				
				actionStatusMap.clear();
				actionMap.clear();
				panel.clear();
				
				actionStatusMap = (HashMap<String, OwlStatus>) initData.getActionStatus(); // status of concept , term ,relationship depend on user group
				actionMap = (HashMap<String, String>) initData.getActionMap();
				panel.add(scheme);
			}
			public void onFailure(Throwable caught){
				Window.alert(constants.schemeInitDataFail());
			}
		};
		Service.classificationService.initData(MainApp.groupId , MainApp.userOntology, callback);
		initWidget(panel);
	}

	public void setDisplayLanguage(ArrayList<String> language,boolean showAlsoNonpreferredTerms){
		scheme.setDisplayLanguage(language, showAlsoNonpreferredTerms);
	}
	public boolean isShowAlsoNonpreferredTerms(){
		return scheme.showAlsoNonpreferredTerms.getValue();
	}
}
