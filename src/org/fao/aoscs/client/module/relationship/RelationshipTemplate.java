package org.fao.aoscs.client.module.relationship;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.relationship.widgetlib.RelationshipDetailTab;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.domain.InitializeRelationshipData;
import org.fao.aoscs.domain.PermissionObject;
import org.fao.aoscs.domain.RelationshipObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RelationshipTemplate extends Composite{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	public VerticalPanel relationshipRootPanel;
	public ScrollPanel sc ;
	public VerticalPanel functionPanel;
	public VerticalPanel rPanel = new VerticalPanel();
	public VerticalPanel panel = new  VerticalPanel();
	public RelationshipObject relationshipObject ;
	public HashMap<String, String> languageDict;
	public PermissionObject permissionTable ;
	public RelationshipDetailTab detailPanel;
	public InitializeRelationshipData initData ;
	
	public RelationshipTemplate(PermissionObject permissionTable, InitializeRelationshipData initData, RelationshipDetailTab detailPanel)
	{
		this.initData = initData;
		this.detailPanel = detailPanel;
		this.permissionTable = permissionTable; //get user permission
		this.languageDict = getLanguageDict(MainApp.getLanguage());
		
		HTML sayWarning = new HTML(constants.relSelectRelationship());
		functionPanel = new VerticalPanel();
		rPanel.add(functionPanel);
		rPanel.setSize("100%","100%");
		
		relationshipRootPanel = new VerticalPanel();
		relationshipRootPanel.add(sayWarning);
		relationshipRootPanel.setCellHorizontalAlignment(sayWarning, HasHorizontalAlignment.ALIGN_CENTER);
		relationshipRootPanel.setCellVerticalAlignment(sayWarning, HasVerticalAlignment.ALIGN_MIDDLE);
		relationshipRootPanel.setSize("100%", "100%");
		
		sc = new ScrollPanel();
		sc.add(relationshipRootPanel);
		sc.setSize("100%", "100%");
		
		rPanel.add(sc);
		rPanel.setSize("100%", "100%");
		rPanel.setCellHeight(sc, "100%");
		rPanel.setCellWidth(sc, "100%");
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
		HTML sayWarning = new HTML(constants.relSelectRelationship());
		functionPanel.clear();
		relationshipRootPanel.clear();
		relationshipRootPanel.add(sayWarning);
		relationshipRootPanel.setCellHorizontalAlignment(sayWarning, HasHorizontalAlignment.ALIGN_CENTER);
		relationshipRootPanel.setCellVerticalAlignment(sayWarning, HasVerticalAlignment.ALIGN_MIDDLE);

	}
	
	public void sayLoading(){
		clearPanel();
		LoadingDialog sayLoading = new LoadingDialog();
		relationshipRootPanel.add(sayLoading);
		relationshipRootPanel.setCellHorizontalAlignment(sayLoading, HasHorizontalAlignment.ALIGN_CENTER);
		relationshipRootPanel.setCellVerticalAlignment(sayLoading, HasVerticalAlignment.ALIGN_MIDDLE);
	}
	
	public void say(String message){
		clearPanel();
		LoadingDialog hp = new LoadingDialog();
		relationshipRootPanel.add(hp);
		relationshipRootPanel.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_CENTER);
		relationshipRootPanel.setCellVerticalAlignment(hp, HasVerticalAlignment.ALIGN_MIDDLE);
	}
 
	public void setURI(RelationshipObject rObj){
		this.relationshipObject = rObj;
		clearPanel();
		initLayout();
	}
	 
	public void initLayout(){
		
	}
	
	public void clearPanel(){
		relationshipRootPanel.clear();
		relationshipRootPanel.setSize("100%","100%");
	}
	
	public RelationshipObject getConceptObject() {
		return this.relationshipObject;
	}
	
}
