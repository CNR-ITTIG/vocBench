package org.fao.aoscs.client.utility;

import java.util.Iterator;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.domain.ConceptObject;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class ModuleManager {
	
	public static int MODULE_CONCEPT = 1;
	public static int MODULE_TERM = 2;
	public static int MODULE_RELATIONSHIP = 3;
	public static int MODULE_CLASSIFICATION = 4;
	public static int MODULE_CONCEPT_BROWSER = 5;
	public static int MODULE_SEARCH = 6;
	public static int MODULE_CONCEPT_CHECK_EXIST = 7;
	
	/**Use to select any specific item in concept tree from any module of this project*/
	public static void gotoItem(String treeItem, String schemeURI, boolean isAddAction,int infoTab, int belongsToModule, int type){
		
		if(type==MODULE_CONCEPT)
			selectModule(treeItem, schemeURI, isAddAction, infoTab, belongsToModule);
		else if(type==MODULE_TERM)
			selectModule(treeItem, schemeURI, isAddAction, infoTab, belongsToModule);
		else if(type==MODULE_RELATIONSHIP)
			selectModule(treeItem, schemeURI, isAddAction, infoTab, belongsToModule);
		else if(type==MODULE_CLASSIFICATION)
			selectModule(treeItem, schemeURI, isAddAction, infoTab, belongsToModule);
		else if(type==MODULE_CONCEPT_BROWSER)
			gotoConceptBrowserItem(treeItem);
		else if(type==MODULE_SEARCH)
			selectModule(treeItem, schemeURI, isAddAction, infoTab, belongsToModule);
		else if(type==MODULE_CONCEPT_CHECK_EXIST)
			gotoConceptBrowserItem(treeItem);
	}
	
	public static void selectModule(String treeItem, String schemeURI, boolean isAddAction,int infoTab,int belongsToModule)
	{
		if(belongsToModule==ConceptObject.CLASSIFICATIONMODULE)
		{
			gotoClassificationItem(treeItem,schemeURI, infoTab);
		}
		else if(belongsToModule==ConceptObject.CONCEPTMODULE)
		{
			gotoConceptItem(treeItem, isAddAction, infoTab);
		}
	}
	
	public static void gotoConceptBrowserItem(String treeItem){
		MainApp m = getMainApp();
		if(m!=null)	m.goToConceptBrowserModuleWithInitTreeItem(treeItem);
	}
	
	public static void gotoConceptItem(String treeItem, boolean isAddAction, int infoTab){
		MainApp m = getMainApp();
		if(m!=null)	m.goToConceptModuleWithInitTreeItem(treeItem,isAddAction,infoTab);
	}
	
	public static void gotoClassificationItem(String targetItemURI, String schemeURI, int infoTab){
		MainApp m = getMainApp();
		if(m!=null)	m.goToClassificationModuleWithInitTreeItem(targetItemURI, schemeURI, infoTab);
	}
	
	public static void resetConcept(){
		MainApp m = getMainApp();
		if(m!=null)	
		{
			if(m.modulePanel.getWidgetIndex(m.concept) != -1 || m.concept != null) 
			{
				m.modulePanel.remove(m.concept);
				m.concept = null;
			}
		}
	}
	
	public static void resetClassification(){
		MainApp m = getMainApp();
		if(m!=null)	
		{
			if(m.modulePanel.getWidgetIndex(m.classification) != -1 || m.classification != null) 
			{
				m.modulePanel.remove(m.classification);
				m.classification = null;
			}
		}
	}
	
	public static void resetValidation(){
		MainApp m = getMainApp();
		if(m!=null)	
		{
			if(m.modulePanel.getWidgetIndex(m.validation) != -1 || m.validation != null) 
			{
				m.modulePanel.remove(m.validation);
				m.validation = null;
			}
		}
	}
	
	public static Widget getSelectedMainAppWidget()
	{
		MainApp m = getMainApp();
		if(m!=null)
		{
			return m.modulePanel.getWidget(m.modulePanel.getVisibleWidget());	
		}
		else
			return null;
	}
	
	public static MainApp getMainApp()
	{
		MainApp m = null; 
		Iterator<Widget> it = RootPanel.get().iterator();
		while(it.hasNext()){
			Object o = it.next();
			if(o instanceof MainApp)
			{
				m = (MainApp) o;
				
				
			}
		}
		return m;
	}
	
	/*public static void reloadTree(){
	MainApp m = getMainApp();
	if(m!=null)	m.reloadConceptTree();
}

public static void reloadClassificationTree(){
	MainApp m = getMainApp();
	if(m!=null)	m.reloadClassificationTree();
}

public static void reloadValidation()
{
	MainApp m = getMainApp();
	if(m!=null)	m.reloadValidation();
}

public static void reloadRecentChanges()
{
	MainApp m = getMainApp();
	if(m!=null)	m.reloadRecentChanges();
}*/
}
