package org.fao.aoscs.client.module.concept.widgetlib;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.classification.widgetlib.ClassificationDetailTab;
import org.fao.aoscs.client.module.concept.ConceptTemplate;
import org.fao.aoscs.client.widgetlib.shared.label.HierarchyAOS;
import org.fao.aoscs.domain.HierarchyObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.PermissionObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;

public class ConceptHierarchy extends ConceptTemplate{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	public ConceptHierarchy(PermissionObject permisstionTable,InitializeConceptData initData, ConceptDetailTabPanel conceptDetailPanel, ClassificationDetailTab classificationDetailPanel){
		super(permisstionTable,initData, conceptDetailPanel, classificationDetailPanel);
	}

	@SuppressWarnings("unchecked")
	public void initLayout(){
		this.sayLoading();
		if(cDetailObj!=null && cDetailObj.getHierarchyObject()!=null)
		{
			initData(cDetailObj.getHierarchyObject());
		}
		else
		{
			AsyncCallback<HierarchyObject> callback = new AsyncCallback<HierarchyObject>(){
				public void onSuccess(HierarchyObject results) {
					cDetailObj.setHierarchyObject(results);
					initData((HierarchyObject) results);
				}
				public void onFailure(Throwable caught) {
					Window.alert(constants.conceptHierarchyLoadFail());
				}
			};
			
			Service.conceptService.getConceptHierarchy(MainApp.userOntology, conceptObject.getUri(), false, false, MainApp.userSelectedLanguage, callback);
		}
	}
	
	private void initData(HierarchyObject hObj)
	{
		clearPanel();
		if(hObj!=null){
			conceptRootPanel.add(new HierarchyAOS(hObj, conceptObject));
		}
		else
		{
			Label sayNo = new Label(constants.conceptNoHierarchy());
			conceptRootPanel.add(sayNo);
			conceptRootPanel.setCellHorizontalAlignment(sayNo, HasHorizontalAlignment.ALIGN_CENTER);
		}
	}

	
}
