package org.fao.aoscs.client.module.concept;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.classification.widgetlib.ClassificationDetailTab;
import org.fao.aoscs.client.module.concept.widgetlib.ConceptDetailTabPanel;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.domain.ConceptDetailObject;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.PermissionObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ConceptTemplate extends Composite{
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	public VerticalPanel conceptRootPanel;
	public ScrollPanel sc ;
	public VerticalPanel functionPanel;
	public VerticalPanel rPanel = new VerticalPanel();
	public VerticalPanel panel = new  VerticalPanel();
	public ConceptDetailObject cDetailObj;
	public ConceptObject conceptObject;
	public PermissionObject permissionTable ;
	public HashMap<String, String> languageDict;
	public ConceptDetailTabPanel conceptDetailPanel;
	public ClassificationDetailTab classificationDetailPanel;
	public InitializeConceptData initData ;
	HTML sayWarning = new HTML("");
	
	public ConceptTemplate(PermissionObject permissionTable,InitializeConceptData initData, ConceptDetailTabPanel conceptDetailPanel, ClassificationDetailTab classificationDetailPanel){
		this.initData = initData;
		this.permissionTable = permissionTable; //get user permission
		this.languageDict = getLanguageDict(MainApp.getLanguage());
		this.conceptDetailPanel = conceptDetailPanel;
		this.classificationDetailPanel = classificationDetailPanel;
		
		if(initData.getBelongsToModule()==InitializeConceptData.CLASSIFICATIONMODULE)
			sayWarning = new HTML(constants.conceptSelectCategory());
		else if(initData.getBelongsToModule()==InitializeConceptData.CONCEPTMODULE)
			sayWarning = new HTML(constants.conceptSelectConcept());
		else
			sayWarning = new HTML(constants.conceptSelectTreeItem());
		functionPanel = new VerticalPanel();
		rPanel.add(functionPanel);
		
		conceptRootPanel = new VerticalPanel();
		conceptRootPanel.add(sayWarning);
		conceptRootPanel.setCellHorizontalAlignment(sayWarning, HasHorizontalAlignment.ALIGN_CENTER);
		conceptRootPanel.setCellVerticalAlignment(sayWarning, HasVerticalAlignment.ALIGN_MIDDLE);
		conceptRootPanel.setSize("100%", "100%");
		
		sc = new ScrollPanel();
		sc.add(conceptRootPanel);
		sc.setHeight("100%");
		
		rPanel.add(sc);
		rPanel.setSize("100%", "100%");
		rPanel.setCellHeight(sc, "100%");
		rPanel.setCellWidth(sc, "100%");
		rPanel.setCellVerticalAlignment(sc, HasVerticalAlignment.ALIGN_TOP);
		
		DOM.setStyleAttribute(rPanel.getElement(), "padding", "10px");
		
		panel.add(rPanel);
		panel.setSize("100%","100%");
		DOM.setStyleAttribute(panel.getElement(), "backgroundColor", "#FAFAFA");
		panel.setCellHeight(rPanel, "100%");
		panel.setCellWidth(rPanel, "100%");
		initWidget(panel);
	}
	private HashMap<String, String> getLanguageDict(ArrayList<String[]> language){
		HashMap<String, String> langList = new HashMap<String, String>();
		for(int i=0;i<language.size();i++){
			String [] item = (String[]) language.get(i);
			String code = item[1].toLowerCase(); 
			String label = item[0]; 
			langList.put(code, label);
 		}
		return langList;
	}
	public String getFullnameofLanguage(String langCode){
		if(languageDict.containsKey(langCode.toLowerCase())){
			return (String)languageDict.get(langCode.toLowerCase());
		}else{
			return  "-";
		}
	}
	
	public void reload(){
		functionPanel.clear();
		conceptRootPanel.clear();
		conceptRootPanel.add(sayWarning);
		conceptRootPanel.setCellHorizontalAlignment(sayWarning, HasHorizontalAlignment.ALIGN_CENTER);
		conceptRootPanel.setCellVerticalAlignment(sayWarning, HasVerticalAlignment.ALIGN_MIDDLE);

	}
	
	public void sayLoading(){
		clearPanel();
		LoadingDialog sayLoading = new LoadingDialog();
		conceptRootPanel.add(sayLoading);
		conceptRootPanel.setCellHorizontalAlignment(sayLoading, HasHorizontalAlignment.ALIGN_CENTER);
		conceptRootPanel.setCellVerticalAlignment(sayLoading, HasVerticalAlignment.ALIGN_MIDDLE);
	}

	public void initData(){
		clearPanel();
		initLayout();
	}
	 
	public void initLayout(){
		
	}

	public void clearPanel(){
		conceptRootPanel.clear();
		conceptRootPanel.setSize("100%","100%");
	}
	public void setConceptObject(ConceptObject conceptObject) {
		this.conceptObject = conceptObject;
	}
	public ConceptObject getConceptObject() {
		return conceptObject;
	}
	public void setConceptDetailObject(ConceptDetailObject cDetailObj) {
		this.cDetailObj = cDetailObj;
	}
	public ConceptDetailObject getConceptDetailObject() {
		return cDetailObj;
	}
}
