package org.fao.aoscs.client.module.relationship.widgetlib;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.domain.InitializeRelationshipData;
import org.fao.aoscs.domain.PermissionObject;
import org.fao.aoscs.domain.RelationshipObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RelationshipDetailTab extends Composite{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	public DecoratedTabPanel tabPanel ; 
	public HorizontalPanel selectedConceptPanel ;
	public RelationshipLabel rLabel;
	public RelationshipDefinition rDef ;
	public RelationshipInverseProperty rIns ;
	public RelationshipProperty rProp;
	public RelationshipDomainRange rDomain;
	
	public  void sayLoading(){
		rLabel.sayLoading();
		rDef.sayLoading();
		rIns.sayLoading();
		rProp.sayLoading();
		rDomain.sayLoading();
	}
	
	public void setSetSelectedTab(int index){
		tabPanel.selectTab(index);
	}
 
	public void showInverseProperty(boolean show){
		if(show){
			if(tabPanel.getWidgetCount()<5){
				tabPanel.insert(rIns, Convert.replaceSpace(constants.relInvProperty()), 3);
			}
		}else{
			if(tabPanel.getWidgetCount()>4){
				tabPanel.selectTab(0);
				tabPanel.getWidget(tabPanel.getWidgetIndex(rIns)).removeFromParent();
			}
		}
	}
	public void reload(){
		rLabel.reload();
		rDef.reload();
		rIns.reload();
		rProp.reload();
		rDomain.reload();
	}
	
	public RelationshipDetailTab(RelationshipTree rTree, PermissionObject permisstionTable, InitializeRelationshipData initData){
		selectedConceptPanel = new HorizontalPanel();			
		selectedConceptPanel.setWidth("100%");
		selectedConceptPanel.setSpacing(2);
		
		VerticalPanel panel = new VerticalPanel();
		tabPanel = new DecoratedTabPanel();		
		tabPanel.setAnimationEnabled(true);
		tabPanel.setSize("99%", "100%");
		tabPanel.getDeckPanel().setSize("99%", "100%");
		
		rLabel = new RelationshipLabel(rTree, permisstionTable, initData, this);
		rDef = new RelationshipDefinition(permisstionTable, initData, this);
		rIns = new RelationshipInverseProperty(permisstionTable, initData, this);
		rProp = new RelationshipProperty(permisstionTable, initData, this);
		rDomain = new RelationshipDomainRange(permisstionTable, initData, this);

		tabPanel.add(rLabel, Convert.replaceSpace(constants.relLabel()));
		tabPanel.add(rDef, Convert.replaceSpace(constants.relDefinition()));
		tabPanel.add(rProp, Convert.replaceSpace(constants.relProperty()));
		tabPanel.add(rIns, Convert.replaceSpace(constants.relInvProperty()));
		tabPanel.add(rDomain, Convert.replaceSpace(constants.relDomain()+"&nbsp;&nbsp;"+constants.relRange()));
		tabPanel.selectTab(0);
		
		HorizontalPanel tabHP = new HorizontalPanel();
		tabHP.setSize("100%", "100%");
		tabHP.setSpacing(5);
		tabHP.add(tabPanel);
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setStyleName("showuri");
		hp.setWidth("100%");
		hp.add(selectedConceptPanel);					
		hp.setCellHorizontalAlignment(selectedConceptPanel, HasHorizontalAlignment.ALIGN_LEFT);
		
		panel.add(hp);
		panel.add(tabHP);
		panel.setCellHeight(hp, "18px");	
		panel.setSize("100%", "100%");		
		initWidget(panel);
	}
	public void setURI(RelationshipObject rObj){
		rLabel.setURI(rObj);
		rDef.setURI(rObj);
		if(rObj.getType().equals(RelationshipObject.OBJECT)){
			rIns.setURI(rObj);
		}
		rProp.setURI(rObj);
		rDomain.setURI(rObj);
	}
}
