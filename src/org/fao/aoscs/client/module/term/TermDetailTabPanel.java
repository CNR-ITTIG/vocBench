package org.fao.aoscs.client.module.term;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.term.widgetlib.TermInformation;
import org.fao.aoscs.client.module.term.widgetlib.TermProperty;
import org.fao.aoscs.client.module.term.widgetlib.TermRelationship;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.PermissionObject;
import org.fao.aoscs.domain.TermObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedTabPanel;

public class TermDetailTabPanel extends Composite{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	public DecoratedTabPanel tabPanel;
	public TermInformation tInfo ;
	//public TermProperty tNote;
	public TermRelationship tRel;
	public TermProperty tAttribute ; 
	public PermissionObject permissionTable = new PermissionObject();
	
	public static int history = 0;
	public static int relationship = 1;
	public static int attribute = 2;
	public static int note = 3;
	
	public TermDetailTabPanel(PermissionObject permissionTable,InitializeConceptData initData)
	{
		
		this.permissionTable = permissionTable;
		
		tabPanel = new DecoratedTabPanel();
		tabPanel.setAnimationEnabled(true);
		tabPanel.setSize("100%", "100%");
		tabPanel.getDeckPanel().setSize("600px", "380px");
		
		tInfo = new TermInformation(permissionTable,initData, this);
		tRel = new TermRelationship(permissionTable,initData, this);
		//tNote = new TermProperty(permissionTable,initData, this);
		tAttribute = new TermProperty(permissionTable,initData, this); 
		
		tabPanel.add(tInfo, constants.conceptHistory());
		tabPanel.add(tRel, constants.conceptRelationships());
		//tabPanel.add(tNote, constants.conceptNotes());
		tabPanel.add(tAttribute, constants.conceptAttributes());
		
		tabPanel.selectTab(0);
		initWidget(tabPanel);
	}
	public void setURI(TermObject termObject,ConceptObject conceptObject){
		tInfo.setURI(termObject,conceptObject);
		tRel.setURI(termObject,conceptObject);
		tAttribute.setURI(termObject,conceptObject);
		//tNote.setURI(termObject,conceptObject);
	}
	
	
	
	
	
	
}
