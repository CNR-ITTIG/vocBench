package org.fao.aoscs.client.module.classification.widgetlib;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.concept.widgetlib.ConceptDefinition;
import org.fao.aoscs.client.module.concept.widgetlib.ConceptInformation;
import org.fao.aoscs.client.module.concept.widgetlib.ConceptMap;
import org.fao.aoscs.client.module.concept.widgetlib.ConceptProperty;
import org.fao.aoscs.client.module.concept.widgetlib.InfoTab;
import org.fao.aoscs.client.module.concept.widgetlib.Term;
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
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ClassificationDetailTab extends Composite{ 
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	public HorizontalPanel selectedConceptPanel ;
	public DecoratedTabPanel tab2Panel ; 
	public ConceptInformation c2Info;
	public ConceptDefinition c2Def ;
	public ConceptProperty c2Note;
	public ConceptProperty c2Attrib;
	public ConceptMap c2Map;
	public Term t2 ;
	
	public  void sayLoading(){
		t2.sayLoading();
		c2Info.sayLoading();
		c2Def.sayLoading();
		c2Map.sayLoading();
		c2Note.sayLoading();
		c2Attrib.sayLoading();
	}
	public void setSetSelectedTab(int index){
		tab2Panel.selectTab(index);
	}
	public void reload(){
		t2.reload();
		c2Info.reload();
		c2Def.reload();
		c2Map.reload();
		c2Note.reload();
		c2Attrib.reload();
	}
	public ClassificationDetailTab(PermissionObject permisstionTable,InitializeConceptData initData){
		
		selectedConceptPanel = new HorizontalPanel();		
		selectedConceptPanel.setWidth("100%");
		selectedConceptPanel.setSpacing(2);
		
		VerticalPanel panel = new VerticalPanel();
		tab2Panel = new DecoratedTabPanel();
		tab2Panel.setSize("99%", "100%");
		tab2Panel.getDeckPanel().setSize("99%", "100%");
		tab2Panel.setAnimationEnabled(true);
		tab2Panel.getDeckPanel().setAnimationEnabled(true);

		
		t2 = new Term(permisstionTable,initData, null, this);
		c2Def = new ConceptDefinition(permisstionTable,initData, null, this);
		c2Note = new ConceptProperty(ModelConstants.RCONCEPTEDITORIALDATATYPEPROPERTY, permisstionTable,initData, null, this);
		c2Attrib = new ConceptProperty(ModelConstants.RCONCEPTDOMAINDATATYPEPROPERTY, permisstionTable,initData, null, this);
		c2Map = new ConceptMap(permisstionTable,initData, null, this);
		c2Info = new ConceptInformation(permisstionTable,initData, null, this);

		tab2Panel.add(t2, Convert.replaceSpace(constants.conceptTerms()));
		tab2Panel.add(c2Def, Convert.replaceSpace(constants.conceptDefinitions()));
		tab2Panel.add(c2Note, Convert.replaceSpace(constants.conceptNotes()));
		tab2Panel.add(c2Attrib, Convert.replaceSpace(constants.conceptAttributes()));
		tab2Panel.add(c2Map, Convert.replaceSpace(constants.conceptMappedConcepts()));
		tab2Panel.add(c2Info, Convert.replaceSpace(constants.conceptHistory()));

		tab2Panel.addSelectionHandler(new SelectionHandler<Integer>()
				{

					public void onSelection(SelectionEvent<Integer> event) {
						switch(event.getSelectedItem())
						{
						case 0: //Term
							if(t2.getConceptObject()!=null)
								t2.initData();
							break;
						case 1: //Definition
							if(c2Def.getConceptObject()!=null)
								c2Def.initData();
							break;
						case 2: //Note
							if(c2Note.getConceptObject()!=null)
								c2Note.initData();
							break;
						case 3: //Attribute
							if(c2Attrib.getConceptObject()!=null)
								c2Attrib.initData();
							break;
						case 4://Mapped Concept
							if(c2Map.getConceptObject()!=null)
								c2Map.initData();
							break;
						case 5://History
							if(c2Info.getConceptObject()!=null)
								c2Info.initData();
							break;
						default:
							if(t2.getConceptObject()!=null)
								t2.initData();
							break;
							
						}
					}
					
				});
		
		HorizontalPanel tabHP = new HorizontalPanel();
		tabHP.setSize("100%", "100%");
		tabHP.setSpacing(5);
		tabHP.add(tab2Panel);
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setStyleName("showuri");
		hp.setWidth("100%");
		hp.add(selectedConceptPanel);					
		hp.setCellHorizontalAlignment(selectedConceptPanel, HasHorizontalAlignment.ALIGN_LEFT);
		
		panel.add(hp);					
		panel.add(tabHP);
		
		panel.setSize("100%", "100%");
		panel.setCellHeight(tab2Panel, "100%");
		panel.setCellWidth(tab2Panel, "100%");		
		panel.setCellHeight(hp, "18px");
		panel.setCellHorizontalAlignment(tab2Panel, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(tab2Panel, HasVerticalAlignment.ALIGN_MIDDLE);		
		initWidget(panel);
	}
	
	public void resetTab()
	{
		tab2Panel.selectTab(0);
		tab2Panel.getTabBar().setTabHTML(InfoTab.term, Convert.replaceSpace(constants.conceptTerm()));
		tab2Panel.getTabBar().setTabHTML(InfoTab.definition, Convert.replaceSpace(constants.conceptDefinition()));
		tab2Panel.getTabBar().setTabHTML(InfoTab.note, Convert.replaceSpace(constants.conceptNote()));
		tab2Panel.getTabBar().setTabHTML(InfoTab.attribute, Convert.replaceSpace(constants.conceptAttribute()));
		tab2Panel.getTabBar().setTabHTML(InfoTab.conceptmap, Convert.replaceSpace(constants.conceptMappedConcept()));
		tab2Panel.getTabBar().setTabHTML(InfoTab.history, Convert.replaceSpace(constants.conceptHistory()));
	}
	
	public void initData(ConceptDetailObject cDetailObj){
		if(cDetailObj!=null)
		{
			t2.setConceptObject(cDetailObj.getConceptObject());
			c2Def.setConceptObject(cDetailObj.getConceptObject());
			c2Note.setConceptObject(cDetailObj.getConceptObject());
			c2Attrib.setConceptObject(cDetailObj.getConceptObject());
			c2Map.setConceptObject(cDetailObj.getConceptObject());
			c2Info.setConceptObject(cDetailObj.getConceptObject());
			t2.setConceptDetailObject(cDetailObj);
			c2Def.setConceptDetailObject(cDetailObj);
			c2Note.setConceptDetailObject(cDetailObj);
			c2Attrib.setConceptDetailObject(cDetailObj);
			c2Map.setConceptDetailObject(cDetailObj);
			c2Info.setConceptDetailObject(cDetailObj);
			t2.initData();
		}
		
	}
	
	public void loadTab(ConceptDetailObject cDetailObj)
	{
		tab2Panel.selectTab(0);
		tab2Panel.getTabBar().setTabHTML(InfoTab.term, Convert.replaceSpace((cDetailObj.getTermCount())>1? constants.conceptTerms():constants.conceptTerm() ) +"&nbsp;("+(cDetailObj.getTermCount())+")" );
		tab2Panel.getTabBar().setTabHTML(InfoTab.definition, Convert.replaceSpace((cDetailObj.getDefinitionCount())>1? constants.conceptDefinitions():constants.conceptDefinition() ) +"&nbsp;("+(cDetailObj.getDefinitionCount())+")" );
		tab2Panel.getTabBar().setTabHTML(InfoTab.note, Convert.replaceSpace((cDetailObj.getNoteCount())>1? constants.conceptNotes():constants.conceptNote() ) +"&nbsp;("+(cDetailObj.getNoteCount())+")" );
		tab2Panel.getTabBar().setTabHTML(InfoTab.attribute, Convert.replaceSpace((cDetailObj.getAttributeCount())>1? constants.conceptAttributes():constants.conceptAttribute() ) +"&nbsp;("+(cDetailObj.getAttributeCount())+")" );
		tab2Panel.getTabBar().setTabHTML(InfoTab.conceptmap, Convert.replaceSpace((cDetailObj.getConceptMappedCount())>1? constants.conceptMappedConcepts():constants.conceptMappedConcept() ) +"&nbsp;("+(cDetailObj.getConceptMappedCount())+")" );
		tab2Panel.getTabBar().setTabHTML(InfoTab.history, Convert.replaceSpace((cDetailObj.getHistoryCount())>1? constants.conceptHistory():constants.conceptHistory() ) +"&nbsp;("+(cDetailObj.getHistoryCount())+")" );
	}
	
	public void clearData(){
		t2.sayLoading();
		c2Def.sayLoading();
		c2Note.sayLoading();
		c2Attrib.sayLoading();
		c2Map.sayLoading();
		c2Info.sayLoading();
	}

}
