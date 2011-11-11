package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import net.sf.gilead.pojo.gwt.LightEntity;

public class SchemeObject extends LightEntity{

	private static final long serialVersionUID = 5199893383971344716L;

	private String schemeName;
	
	private String schemeInstance;
	
	private String schemeLabel;
	
	private String nameSpaceCatagoryPrefix;
	
	private String description;
	
	private String namespace;
	
	private String rHasSub;
	
	private String rIsSub;
	
	private HashMap<String,ConceptObject> conceptList = new HashMap<String,ConceptObject>();

	private HashMap<String,ArrayList<ConceptObject>>  parentChild = new HashMap<String,ArrayList<ConceptObject>> ();
	
	private boolean conceptSelected;
	
	public void addCategoryList(String conceptInstance,ConceptObject cObj) {
		if(!this.conceptList.containsKey(conceptInstance)){
			this.conceptList.put(conceptInstance, cObj);
		}
		addParentChild(cObj);
	}

	public void addParentChild(ConceptObject cObj) {
		if(!cObj.isRootItem()){
			if(this.parentChild.containsKey(cObj.getParentInstance())){
				((ArrayList<ConceptObject>) this.parentChild.get(cObj.getParentInstance())).add(cObj);
			}else{
				ArrayList<ConceptObject> list = new ArrayList<ConceptObject>();
				this.parentChild.put(cObj.getParentInstance(),list);
				list.add(cObj);
			}
		}
	}

	public ArrayList<ConceptObject> getChildOf(String parentInstance) {
		ArrayList<ConceptObject> list = new ArrayList<ConceptObject>();
		if(this.parentChild.containsKey(parentInstance)){
			list = (ArrayList<ConceptObject>) this.parentChild.get(parentInstance);
		}
		return list;
	}

	public HashMap<String, ConceptObject> getConceptList() {
		return conceptList;
	}
	
	public ConceptObject getConceptObject(String conceptInstance){
		if(conceptList.containsKey(conceptInstance)){
			return (ConceptObject) this.conceptList.get(conceptInstance);
		}else{
			return null;
		}
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getNamespace() {
		return namespace;
	}
	
	public String getNameSpaceCatagoryPrefix() {
		return nameSpaceCatagoryPrefix;
	}


	public HashMap<String, ArrayList<ConceptObject>> getParentChild() {
		return parentChild;
	}


	public String getRHasSub() {
		return rHasSub;
	}
	
	public String getRIsSub() {
		return rIsSub;
	}

	public ArrayList<ConceptObject> getRootItem(){
		Iterator<String> it = conceptList.keySet().iterator();
		ArrayList<ConceptObject> rootItemList = new ArrayList<ConceptObject>();
		while(it.hasNext()){
			String key = (String)it.next();
			ConceptObject cObj = (ConceptObject) conceptList.get(key);
			if(cObj.isRootItem()){
				rootItemList.add(cObj);
			}
		}
		return rootItemList;
	}

	public String getSchemeInstance() {
		return schemeInstance;
	}

	public String getSchemeLabel() {
		return schemeLabel;
	}

	public String getSchemeName() {
		return schemeName;
	}
	
	public boolean hasChild(String parentInstance){
		if(this.parentChild.containsKey(parentInstance)){
			return true;
		}else{
			return false;
		}
	}

	public boolean hasConcept() {
		if(conceptList.size()>0){
			return true;
		}else{
			return false;
		}
	}

	public boolean hasConceptInstance(String conceptInstance){
		if(this.conceptList.containsKey(conceptInstance)){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isConceptObjectEmpty(){
		if(conceptList.isEmpty()){
			return true;
		}else{
			return false;
		}
	}

	public boolean isConceptSelected() {
		return conceptSelected;
	}
	
	public void setConceptList(HashMap<String, ConceptObject> conceptList) {
		this.conceptList = conceptList;
	}


	public void setConceptSelected(boolean conceptSelected) {
		this.conceptSelected = conceptSelected;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}


	public void setNameSpaceCatagoryPrefix(String nameSpaceCatagoryPrefix) {
		this.nameSpaceCatagoryPrefix = nameSpaceCatagoryPrefix;
	}


	public void setParentChild(HashMap<String, ArrayList<ConceptObject>> parentChild) {
		this.parentChild = parentChild;
	}


	public void setRHasSub(String hasSub) {
		rHasSub = hasSub;
	}


	public void setRIsSub(String isSub) {
		rIsSub = isSub;
	}


	public void setSchemeInstance(String schemeInstance) {
		this.schemeInstance = schemeInstance;
	}


	public void setSchemeLabel(String schemeLabel) {
		this.schemeLabel = schemeLabel;
	}


	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}


 
}
