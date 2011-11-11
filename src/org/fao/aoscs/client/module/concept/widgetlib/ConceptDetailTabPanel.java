package org.fao.aoscs.client.module.concept.widgetlib;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.domain.ConceptDetailObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.PermissionObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ConceptDetailTabPanel extends Composite{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	public HorizontalPanel selectedConceptPanel ;	
	public DecoratedTabPanel tabPanel ; 
	
	public ConceptInformation cInfo;
	public ConceptImage cImage ;
	public ConceptDefinition cDef ;
	public ConceptRelationship cRel ;
	public ConceptProperty cNote;
	public ConceptProperty cAttrib;
	public ConceptHierarchy cHier;
	public Term t ;

	
	public  void sayLoading(){
		t.sayLoading();
		cInfo.sayLoading();
		cImage.sayLoading();
		cDef.sayLoading();
		cRel.sayLoading();
		cNote.sayLoading();
		cAttrib.sayLoading();
		cHier.sayLoading();
	}
	public void setSetSelectedTab(int index){
		tabPanel.selectTab(index);
	}
	public void reload(){
		t.reload();
		cInfo.reload();
		cImage.reload();
		cDef.reload();
		cRel.reload();
		cNote.reload();
		cAttrib.reload();
		cHier.reload();
	}
	public ConceptDetailTabPanel(PermissionObject permisstionTable,InitializeConceptData initData){

		selectedConceptPanel = new HorizontalPanel();		
		selectedConceptPanel.setWidth("100%");
		selectedConceptPanel.setSpacing(2);
		
		tabPanel = new DecoratedTabPanel();
		tabPanel.setAnimationEnabled(true);
		tabPanel.setSize("99%", "100%");
		tabPanel.getDeckPanel().setSize("99%", "100%");
		
		cInfo = new ConceptInformation(permisstionTable,initData, this, null);
		cImage = new ConceptImage(permisstionTable,initData, this, null);
		cDef = new ConceptDefinition(permisstionTable,initData, this, null);
		cRel = new ConceptRelationship(permisstionTable,initData, this, null);
		cNote = new ConceptProperty(ModelConstants.RCONCEPTEDITORIALDATATYPEPROPERTY, permisstionTable, initData, this, null);
		cAttrib = new ConceptProperty(ModelConstants.RCONCEPTDOMAINDATATYPEPROPERTY, permisstionTable, initData, this, null);
		cHier = new ConceptHierarchy(permisstionTable, initData, this, null);
		t = new Term(permisstionTable,initData, this, null);

		tabPanel.add(t, Convert.replaceSpace(constants.conceptTerm()));
		tabPanel.add(cDef, Convert.replaceSpace(constants.conceptDefinition()));
		tabPanel.add(cNote, Convert.replaceSpace(constants.conceptNote()));
		tabPanel.add(cAttrib, Convert.replaceSpace(constants.conceptAttribute()));
		tabPanel.add(cRel, Convert.replaceSpace(constants.conceptRelationship()));
		tabPanel.add(cInfo, Convert.replaceSpace(constants.conceptHistory()));
		tabPanel.add(cImage, Convert.replaceSpace(constants.conceptImage()));
		tabPanel.add(cHier, Convert.replaceSpace(constants.conceptHierarchy()));
		
		tabPanel.addSelectionHandler(new SelectionHandler<Integer>()
		{

			public void onSelection(SelectionEvent<Integer> event) {
				switch(event.getSelectedItem())
				{
				case 0: //Term
					if(t.getConceptObject()!=null)
						t.initData();
					break;
				case 1: //Definition
					if(cDef.getConceptObject()!=null)
						cDef.initData();
					break;
				case 2: //Note
					if(cNote.getConceptObject()!=null)
						cNote.initData();
					break;
				case 3: //Attribute
					if(cAttrib.getConceptObject()!=null)
						cAttrib.initData();
					break;
				case 4://Relationship
					if(cRel.getConceptObject()!=null)
						cRel.initData();
					break;
				case 5://History
					if(cInfo.getConceptObject()!=null)
						cInfo.initData();
					break;
				case 6://Image
					if(cImage.getConceptObject()!=null)
						cImage.initData();
					break;
				case 7://Hierarchy
					if(cHier.getConceptObject()!=null)
						cHier.initData();
					break;
				default:
					if(t.getConceptObject()!=null)
						t.initData();
					break;
					
				}
			}
			
		});
		
		HorizontalPanel tabHP = new HorizontalPanel();
		tabHP.setSize("100%", "100%");
		tabHP.setSpacing(5);
		tabHP.add(tabPanel);
		tabHP.setCellHeight(tabPanel, "100%");
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setStyleName("showshort");
		hp.setWidth("100%");
		hp.add(selectedConceptPanel);					
		hp.setCellHorizontalAlignment(selectedConceptPanel, HasHorizontalAlignment.ALIGN_LEFT);
		
		VerticalPanel panel = new VerticalPanel();		
		panel.add(hp);					
		panel.add(tabHP);
		panel.setCellHeight(hp, "18px");		
		panel.setSize("100%", "100%");		
		initWidget(panel);
	}
	
	public void resetTab()
	{
		tabPanel.selectTab(0);
		tabPanel.getTabBar().setTabHTML(InfoTab.term, Convert.replaceSpace(constants.conceptTerm()));
		tabPanel.getTabBar().setTabHTML(InfoTab.definition, Convert.replaceSpace(constants.conceptDefinition()));
		tabPanel.getTabBar().setTabHTML(InfoTab.note, Convert.replaceSpace(constants.conceptNote()));
		tabPanel.getTabBar().setTabHTML(InfoTab.attribute, Convert.replaceSpace(constants.conceptAttribute()));
		tabPanel.getTabBar().setTabHTML(InfoTab.relationship, Convert.replaceSpace(constants.conceptRelationship()));
		tabPanel.getTabBar().setTabHTML(InfoTab.history, Convert.replaceSpace(constants.conceptHistory()));
		tabPanel.getTabBar().setTabHTML(InfoTab.image, Convert.replaceSpace(constants.conceptImage()));
		tabPanel.getTabBar().setTabHTML(InfoTab.hierarchy, Convert.replaceSpace(constants.conceptHierarchy()));
	}
	
	public void initData(ConceptDetailObject cDetailObj){
		if(cDetailObj!=null)
		{
			t.setConceptObject(cDetailObj.getConceptObject());
			cInfo.setConceptObject(cDetailObj.getConceptObject());
			cImage.setConceptObject(cDetailObj.getConceptObject());
			cDef.setConceptObject(cDetailObj.getConceptObject());
			cRel.setConceptObject(cDetailObj.getConceptObject());
			cNote.setConceptObject(cDetailObj.getConceptObject());
			cAttrib.setConceptObject(cDetailObj.getConceptObject());
			cHier.setConceptObject(cDetailObj.getConceptObject());
			
			t.setConceptDetailObject(cDetailObj);
			cInfo.setConceptDetailObject(cDetailObj);
			cImage.setConceptDetailObject(cDetailObj);
			cDef.setConceptDetailObject(cDetailObj);
			cRel.setConceptDetailObject(cDetailObj);
			cNote.setConceptDetailObject(cDetailObj);
			cAttrib.setConceptDetailObject(cDetailObj);
			cHier.setConceptDetailObject(cDetailObj);
			
			t.initData();
		}
		
	}
	
	public void loadTab(ConceptDetailObject cDetailObj)
	{
		tabPanel.selectTab(0);
		tabPanel.getTabBar().setTabHTML(InfoTab.term, Convert.replaceSpace((cDetailObj.getTermCount())>1? constants.conceptTerms():constants.conceptTerm() ) +"&nbsp;("+(cDetailObj.getTermCount())+")" );
		tabPanel.getTabBar().setTabHTML(InfoTab.definition, Convert.replaceSpace((cDetailObj.getDefinitionCount())>1? constants.conceptDefinitions():constants.conceptDefinition() ) +"&nbsp;("+(cDetailObj.getDefinitionCount())+")" );
		tabPanel.getTabBar().setTabHTML(InfoTab.note, Convert.replaceSpace((cDetailObj.getNoteCount())>1? constants.conceptNotes():constants.conceptNote() ) +"&nbsp;("+(cDetailObj.getNoteCount())+")" );
		tabPanel.getTabBar().setTabHTML(InfoTab.attribute, Convert.replaceSpace((cDetailObj.getAttributeCount())>1? constants.conceptAttributes():constants.conceptAttribute() ) +"&nbsp;("+(cDetailObj.getAttributeCount())+")" );
		tabPanel.getTabBar().setTabHTML(InfoTab.relationship, Convert.replaceSpace((cDetailObj.getRelationCount())>1? constants.conceptRelationships():constants.conceptRelationship() ) +"&nbsp;("+(cDetailObj.getRelationCount())+")" );
		tabPanel.getTabBar().setTabHTML(InfoTab.history, Convert.replaceSpace((cDetailObj.getHistoryCount())>1? constants.conceptHistory():constants.conceptHistory() ) +"&nbsp;("+(cDetailObj.getHistoryCount())+")" );
		tabPanel.getTabBar().setTabHTML(InfoTab.image, Convert.replaceSpace((cDetailObj.getImageCount())>1? constants.conceptImages():constants.conceptImage() ) +"&nbsp;("+(cDetailObj.getImageCount())+")" );
		tabPanel.getTabBar().setTabHTML(InfoTab.hierarchy, Convert.replaceSpace(constants.conceptHierarchy()));
	}
	
	public void clearData(){
		t.sayLoading();
		cInfo.sayLoading();
		cImage.sayLoading();
		cDef.sayLoading();
		cRel.sayLoading();
		cNote.sayLoading();
		cAttrib.sayLoading();
		cHier.sayLoading();
	}
}
