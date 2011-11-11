package org.fao.aoscs.client.widgetlib.shared.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.LabelObject;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.RelationshipTreeObject;
import org.fao.aoscs.domain.SchemeObject;
import org.fao.aoscs.domain.TermObject;
import org.fao.aoscs.domain.TreeObject;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.widgetideas.client.FastTree;

public class LazyLoadingTree {

	private static int LOADING_ITEM_SIZE = 10;

	// Start Loading Concept Tree
	public static void addTreeItems(final FastTree tree, final ArrayList<TreeObject> startConceptList, final boolean showAlsoNonpreferredTerms) {
		tree.removeItems();
		final Iterator<TreeObject> it = startConceptList.iterator();
		
		Scheduler.get().scheduleIncremental(new RepeatingCommand(){ 
                public boolean execute() { 
                		for (int i = 0; i < LOADING_ITEM_SIZE && it.hasNext(); i++){ 
                			TreeObject cObj = it.next();
                			TreeItemAOS childItem = new TreeItemAOS(cObj, showAlsoNonpreferredTerms);
                   			if(childItem.isHasChild()){
                   				childItem.addItem("blank");
							}
               				tree.addItem(childItem);
           					//if(cObj.getLabel().startsWith("###EMPTY###"))
               					//childItem.setVisible(false);	
                        } 
                		//repeat until no more items 
                		return it.hasNext();
                } 
        }); 
 
	}
	
	public static void addTreeItems(final TreeItemAOS item, final ArrayList<TreeObject> startConceptList, final boolean showAlsoNonpreferredTerms) {
		if(item!=null)
		{
			item.removeItems();
			final Iterator<TreeObject> it = startConceptList.iterator();
			
			Scheduler.get().scheduleIncremental(new RepeatingCommand(){ 
	                public boolean execute() { 
	                		for (int i = 0; i < LOADING_ITEM_SIZE && it.hasNext(); i++){ 
	                			TreeObject cObj = it.next();
	                			TreeItemAOS childItem = new TreeItemAOS(cObj, showAlsoNonpreferredTerms);
	                   			if(childItem.isHasChild()){
									childItem.addItem("blank");
								}
                   				item.addItem(childItem);
               					//if(cObj.getLabel().startsWith("###EMPTY###"))
                   					//childItem.setVisible(false);
	                        } 
	                        //repeat until no more items 
	                        return it.hasNext(); 
	                        
	                } 
	        }); 
		}
	}
	
	// End Loading Concept Tree
	
	// Start Loading Scheme Tree
	
	
	public static void addTreeItems(final FastTree tree, final ArrayList<ConceptObject> startConceptList, final boolean showAlsoNonpreferredTerms, final SchemeObject sObj) {
		tree.removeItems();
		
		final HashMap<String, ConceptObject> cList = new HashMap<String, ConceptObject>();
		final HashMap<String, ConceptObject> emptycList = new HashMap<String, ConceptObject>();
		for (Iterator<ConceptObject> itr = startConceptList.iterator(); itr.hasNext();)
		{ 
			ConceptObject cObj = (ConceptObject) itr.next();
			String label = getConceptLabelWithEmptyCheck(cObj, showAlsoNonpreferredTerms);
			if(label.startsWith("###EMPTY###"))
				emptycList.put(label, cObj);
			else
				cList.put(label, cObj);
		}

		List<String> labelKeys = new ArrayList<String>(); 
		labelKeys.addAll(cList.keySet()); 
		Collections.sort(labelKeys, String.CASE_INSENSITIVE_ORDER);
		labelKeys.addAll(emptycList.keySet()); 
		final Iterator<String> it = labelKeys.iterator();
		
		Scheduler.get().scheduleIncremental(new RepeatingCommand(){ 
                public boolean execute() { 
                		for (int i = 0; i < LOADING_ITEM_SIZE && it.hasNext(); i++){ 
                			String label = (String) it.next();
                			ConceptObject cObj = new ConceptObject();
                			if(cList.containsKey(label))
                				cObj = (ConceptObject) cList.get(label);
                			else if(emptycList.containsKey(label))
                				cObj = (ConceptObject) emptycList.get(label);
                			TreeItemAOS childItem = new TreeItemAOS(cObj, label, showAlsoNonpreferredTerms);
                   			if(sObj.hasChild(cObj.getConceptInstance())){
                   				addTreeItems(childItem, sObj.getChildOf(cObj.getConceptInstance()), showAlsoNonpreferredTerms, sObj);
							}
               				tree.addItem(childItem);
           					//if(label.startsWith("###EMPTY###"))
               					//childItem.setVisible(false);	
                        } 
                        //repeat until no more items 
                        return it.hasNext(); 
                        
                } 
        }); 
	}
	
	public static void addTreeItems(final TreeItemAOS item, final ArrayList<ConceptObject> startConceptList, final boolean showAlsoNonpreferredTerms, final SchemeObject sObj) 
	{
		if(item!=null)
		{
			item.removeItems();
		
			final HashMap<String, ConceptObject> cList = new HashMap<String, ConceptObject>();
			final HashMap<String, ConceptObject> emptycList = new HashMap<String, ConceptObject>();
			for (Iterator<ConceptObject> itr = startConceptList.iterator(); itr.hasNext();)
			{ 
				ConceptObject cObj = (ConceptObject) itr.next();
				String label = getConceptLabelWithEmptyCheck(cObj, showAlsoNonpreferredTerms);
				if(label.startsWith("###EMPTY###"))
					emptycList.put(label, cObj);
				else
					cList.put(label, cObj);
			}
	
			List<String> labelKeys = new ArrayList<String>(); 
			labelKeys.addAll(cList.keySet());
			Collections.sort(labelKeys, String.CASE_INSENSITIVE_ORDER);
			labelKeys.addAll(emptycList.keySet());
	
			final Iterator<String> it = labelKeys.iterator();
			
			Scheduler.get().scheduleIncremental(new RepeatingCommand(){ 
	                public boolean execute() { 
	                		for (int i = 0; i < LOADING_ITEM_SIZE && it.hasNext(); i++){ 
	                			String label = (String) it.next();
	                			ConceptObject cObj = new ConceptObject();
	                			if(cList.containsKey(label))
	                				cObj = (ConceptObject) cList.get(label);
	                			else if(emptycList.containsKey(label))
	                				cObj = (ConceptObject) emptycList.get(label);
	                			TreeItemAOS childItem = new TreeItemAOS(cObj, label, showAlsoNonpreferredTerms);
	                			if(sObj.hasChild(cObj.getConceptInstance())){
	                   				addTreeItems(childItem, sObj.getChildOf(cObj.getConceptInstance()), showAlsoNonpreferredTerms, sObj);
								}
	                   			
                   				item.addItem(childItem);
               					//if(label.startsWith("###EMPTY###"))
                   					//childItem.setVisible(false);
	                        } 
	                        //repeat until no more items 
	                        return it.hasNext(); 
	                        
	                } 
	        }); 
		}
	}
	
	private static String getConceptLabelWithEmptyCheck(ConceptObject cObj, boolean showAlsoNonpreferredTerms)
	{
		String label = getConceptLabel(cObj, showAlsoNonpreferredTerms);
		if(label.length()==0)
			label = "###EMPTY###"+cObj.getName();
		return label;
	}
	
	public static String getConceptLabel(ConceptObject cObj, boolean showAlsoNonpreferredTerms)
	{
		
		ArrayList<String> langList = (ArrayList<String>)MainApp.userSelectedLanguage;
		String label = "";
		if(!cObj.getTerm().values().isEmpty()){
			Iterator<TermObject> it = cObj.getTerm().values().iterator();
			ArrayList<String> sortedList = new ArrayList<String>();
			HashMap<String, Boolean> checkMainLabelList = new HashMap<String, Boolean>();
			while(it.hasNext()){
				TermObject tObj = it.next();
				if(!showAlsoNonpreferredTerms){
					if(tObj.isMainLabel() && langList.contains(tObj.getLang()) && MainApp.checkDeprecated(tObj.getStatus()) ){
						sortedList.add(tObj.getLang()+"###"+tObj.getLabel());
					}			
				}else{
					if(langList.contains(tObj.getLang()) && MainApp.checkDeprecated(tObj.getStatus())){
						sortedList.add(tObj.getLang()+"###"+tObj.getLabel());
						checkMainLabelList.put(tObj.getLang()+"###"+tObj.getLabel(), tObj.isMainLabel());
					}			
				}
			}
			Collections.sort(sortedList, String.CASE_INSENSITIVE_ORDER);
			
			for (int i = 0; i < sortedList.size(); i++) {
				String str =  (String) sortedList.get(i);
				String[] element = str.split("###");
				if(element.length==2){
					if(checkMainLabelList.get(str) != null && checkMainLabelList.get(str))
					{
						if(label.length()==0)
							label = label + "<b>"+ element[1] + " ("+element[0]+")</b>";
						else
							label = label +";&nbsp;"+ "<b>"+ element[1] + " ("+element[0]+")</b>";
						
					}
					else
					{
						if(label.length()==0)
							label = label + element[1] + " ("+element[0]+")";
						else
							label = label +";&nbsp;"+ element[1] + " ("+element[0]+")";
					}
						
				}
			}
		}
		return label;
	}
	
	// End Loading Scheme Tree
	
	// Start Loading Relationship Tree
	
	public static void addTreeItems(final FastTree tree, final ArrayList<RelationshipObject> startList, final RelationshipTreeObject rtObj) {
		tree.removeItems();
		final HashMap<String, RelationshipObject> list = new HashMap<String, RelationshipObject>();
		final HashMap<String, RelationshipObject> emptylist = new HashMap<String, RelationshipObject>();
		for (Iterator<RelationshipObject> itr = startList.iterator(); itr.hasNext();)
		{ 
			RelationshipObject rObj = (RelationshipObject) itr.next();
			String label = getRelationshipLabelWithEmptyCheck(rObj);
			if(label.startsWith("###EMPTY###"))
				emptylist.put(label, rObj);
			else
				list.put(label, rObj);
		}
			
		List<String> labelKeys = new ArrayList<String>(); 
		labelKeys.addAll(list.keySet()); 
		Collections.sort(labelKeys, String.CASE_INSENSITIVE_ORDER);
		labelKeys.addAll(emptylist.keySet());
		final Iterator<String> it = labelKeys.iterator();
		Scheduler.get().scheduleIncremental(new RepeatingCommand(){ 
                public boolean execute() { 
                		for (int i = 0; i < LOADING_ITEM_SIZE && it.hasNext(); i++){ 
                			String label = (String) it.next();
                			RelationshipObject rObj = new RelationshipObject();
                			if(list.containsKey(label))
                				rObj = (RelationshipObject) list.get(label);
                			else if(emptylist.containsKey(label))
                				rObj = (RelationshipObject) emptylist.get(label);
                			TreeItemAOS childItem = new TreeItemAOS(rObj, label);
                			if(rtObj.hasChild(rObj.getUri())){
                   				addTreeItems(childItem, rtObj.getChildOf(rObj.getUri()) , rtObj);
							}
               				tree.addItem(childItem);
           					//if(label.startsWith("###EMPTY###"))
               					//childItem.setVisible(false);	
                        } 
                        //repeat until no more items 
                        return it.hasNext(); 
                        
                } 
        }); 
	}
	
	public static void addTreeItems(final TreeItemAOS item, final ArrayList<RelationshipObject> startList, final RelationshipTreeObject rtObj) {
		if(item!=null)
		{
			item.removeItems();
			final HashMap<String, RelationshipObject> list = new HashMap<String, RelationshipObject>();
			final HashMap<String, RelationshipObject> emptylist = new HashMap<String, RelationshipObject>();
			for (Iterator<RelationshipObject> itr = startList.iterator(); itr.hasNext();)
			{ 
				RelationshipObject rObj = (RelationshipObject) itr.next();
				String label = getRelationshipLabelWithEmptyCheck(rObj);
				if(label.startsWith("###EMPTY###"))
					emptylist.put(label, rObj);
				else
					list.put(label, rObj);
			}
			List<String> labelKeys = new ArrayList<String>(); 
			labelKeys.addAll(list.keySet()); 
			Collections.sort(labelKeys, String.CASE_INSENSITIVE_ORDER);
			labelKeys.addAll(emptylist.keySet());
			final Iterator<String> it = labelKeys.iterator();
			Scheduler.get().scheduleIncremental(new RepeatingCommand(){ 
                public boolean execute() { 
            		for (int i = 0; i < LOADING_ITEM_SIZE && it.hasNext(); i++){ 
            			String label = (String) it.next();
            			RelationshipObject rObj = new RelationshipObject();
            			if(list.containsKey(label))
            				rObj = (RelationshipObject) list.get(label);
            			else if(emptylist.containsKey(label))
            				rObj = (RelationshipObject) emptylist.get(label);
            			TreeItemAOS childItem = new TreeItemAOS(rObj, label);
            			if(rtObj.hasChild(rObj.getUri())){
               				addTreeItems(childItem, rtObj.getChildOf(rObj.getUri()) , rtObj);
						}
           				item.addItem(childItem);
       					//if(label.startsWith("###EMPTY###"))
           					//childItem.setVisible(false);
                    } 
                    //repeat until no more items 
                        return it.hasNext(); 
                } 
	        }); 
		}
	}

	private static String getRelationshipLabelWithEmptyCheck(RelationshipObject rObj)
	{
		String label = getRelationshipLabel(rObj);
		if(label.length()==0)
			label = "###EMPTY###"+rObj.getName();
		return label;
	}
	
	public static String getRelationshipLabel(RelationshipObject rObj)
	{
		String label = "";
		ArrayList<String> langList = (ArrayList<String>)MainApp.userSelectedLanguage;
		if(!rObj.getLabelList().isEmpty())
		{
			ArrayList<String> sortedList = new ArrayList<String>();
			Iterator<LabelObject> it = rObj.getLabelList().iterator();
			while(it.hasNext())
			{
				LabelObject labelObj = (LabelObject) it.next();
				String lang = labelObj.getLanguage();
				String relLabel = labelObj.getLabel();
				if(langList.contains(lang)){
					sortedList.add(lang+"###"+relLabel);
				}
			}
			Collections.sort(sortedList, String.CASE_INSENSITIVE_ORDER);
			for (int i = 0; i < sortedList.size(); i++) {
				String str =  (String) sortedList.get(i);
				String[] element = str.split("###");
				if(element.length==2){
					if(label.length()==0)
						label = label + element[1] + " ("+element[0]+")";
					else
						label = label +"; "+ element[1] + " ("+element[0]+")";
				}
			}
		}	
		return label;
	}
	
	public static String getRelationshipLabel(RelationshipObject rObj, String language)
	{
		String label = "";
		String label_en = "";
		if(!rObj.getLabelList().isEmpty())
		{
			Iterator<LabelObject> it = rObj.getLabelList().iterator();
			while(it.hasNext())
			{
				LabelObject labelObj = (LabelObject) it.next();
				String lang = labelObj.getLanguage();
				if(lang.equals("en")){
					label_en = labelObj.getLabel();
				}
				if(lang.equals(language)){
					label = labelObj.getLabel();
					break;
				}
				
			}
		}	
		if(label.length()==0)
		{
			if(label_en.length()!=0) 
				label = label_en;	
			else
				label = rObj.getName();
		}
		return label;
	}
	
	// End Loading Relationship Tree
}



