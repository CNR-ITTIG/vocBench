package org.fao.aoscs.client.module.term;


import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.PermissionObject;
import org.fao.aoscs.domain.TermObject;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TermTemplate extends Composite{
	public VerticalPanel termRootPanel;
	public ScrollPanel sc ;
	public VerticalPanel functionPanel;
	public VerticalPanel rPanel = new VerticalPanel();
	public VerticalPanel panel = new  VerticalPanel();
	public PermissionObject permissionTable = new PermissionObject();
	public HashMap<String, String>  languageDict;
	public TermObject termObject = new TermObject();
	public ConceptObject conceptObject = new ConceptObject();
	public InitializeConceptData initData;
	public TermDetailTabPanel termDetailPanel;
	
	public TermTemplate(PermissionObject permissionTable, InitializeConceptData initData, TermDetailTabPanel termDetailPanel){
		this.permissionTable = permissionTable; //get user permission
		this.initData  = initData;
		this.termDetailPanel = termDetailPanel;
		this.languageDict = getLanguageDict(MainApp.getLanguage());
		functionPanel = new VerticalPanel();
		sc = new ScrollPanel();
		rPanel.add(functionPanel);
		DOM.setStyleAttribute(functionPanel.getElement(), "marginTop", "4px");
		DOM.setStyleAttribute(functionPanel.getElement(), "marginBottom", "4px");
		rPanel.setSize("100%", "100%");
		
		termRootPanel = new VerticalPanel();
		termRootPanel.setSize("100%", "100%");
		
		sc.add(termRootPanel);
		sc.setSize("100%", "100%");
		rPanel.add(sc);
		rPanel.setCellHeight(sc, "100%");
		rPanel.setCellWidth(sc, "100%");
		rPanel.setSpacing(5);
		
		panel.add(rPanel);
		panel.setSize("100%","100%");
		panel.setCellHeight(rPanel, "100%");
		panel.setCellWidth(rPanel, "100%");
		DOM.setStyleAttribute(panel.getElement(), "backgroundColor", "#FAFAFA");
		initWidget(panel);
	}
	public HashMap<String, String> getLanguageDict(ArrayList<String[]> language){
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
	public void sayLoading(){
		clearPanel();
		LoadingDialog sayLoading = new LoadingDialog();
		termRootPanel.add(sayLoading);
		termRootPanel.setCellHorizontalAlignment(sayLoading, HasHorizontalAlignment.ALIGN_CENTER);
		termRootPanel.setCellVerticalAlignment(sayLoading, HasVerticalAlignment.ALIGN_MIDDLE);
		termRootPanel.setCellHeight(sayLoading, "100%");
		termRootPanel.setCellWidth(sayLoading, "100%");
	}
	public void say(String message){
		clearPanel();
		LoadingDialog hp = new LoadingDialog(message);
		termRootPanel.add(hp);
		termRootPanel.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_CENTER);
		termRootPanel.setCellVerticalAlignment(hp, HasVerticalAlignment.ALIGN_MIDDLE);
	}
	public void setURI(TermObject termObject,ConceptObject conceptObject){
 		this.conceptObject = conceptObject;
 		this.termObject = termObject;
		clearPanel();
		initLayout();
	}
	public void initLayout(){
		
	}
	public void clearPanel(){
		termRootPanel .clear();
		termRootPanel .setSize("100%","100%");
	}
	
}
