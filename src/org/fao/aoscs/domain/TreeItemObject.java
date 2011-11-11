package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import net.sf.gilead.pojo.gwt.LightEntity;


public class TreeItemObject extends LightEntity{

	private static final long serialVersionUID = 5222662829871812341L;

	private HashMap<String,ConceptObject>  childList = new HashMap<String,ConceptObject> ();
	
	private ConceptObject conceptObject = new ConceptObject();
	
	public ArrayList<ConceptObject>  getChildListOnly() {
		ArrayList<ConceptObject> list = new ArrayList<ConceptObject>();
		Iterator<String> it = childList.keySet().iterator();
		while(it.hasNext()){
			String uri = (String) it.next();
			list.add(childList.get(uri));
		}
		return list;
	}
	
	public HashMap<String, ConceptObject> getChildList() {
		return childList;
	}

	public void setChildList(HashMap<String, ConceptObject> childList) {
		this.childList = childList;
	}
	
	public int getChildCount(){
		return childList.size();
	}
	public ConceptObject getChild(String uri){
		return (ConceptObject) childList.get(uri);
	}
	public void addChildList(ConceptObject cObj) {
		if(!childList.containsKey(cObj.getUri())){
			this.childList.put(cObj.getUri(), cObj);
		}
	}
	public ConceptObject getConceptObject() {
		return conceptObject;
	}
	public void setConceptObject(ConceptObject conceptObject) {
		this.conceptObject = conceptObject;
	}
	public String getItemURI(){
		return conceptObject.getUri();
	}
	
	public boolean hasChild(String uri){
		return childList.containsKey(uri);
	}
}
