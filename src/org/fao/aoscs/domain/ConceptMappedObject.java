package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import net.sf.gilead.pojo.gwt.LightEntity;

public class ConceptMappedObject extends LightEntity{

	private static final long serialVersionUID = 8691172094330203728L;
	
	private HashMap<String, ConceptObject> conceptList = new HashMap<String, ConceptObject>();

	public HashMap<String, ConceptObject> getConceptList() {
		return conceptList;
	}
	public void setConceptList(HashMap<String, ConceptObject> conceptList) {
		this.conceptList = conceptList;
	}
	public void addConceptList(ConceptObject cObj) {
		if(!this.conceptList.containsKey(cObj.getUri())){
			this.conceptList.put(cObj.getUri(), cObj);
		}
	}
	public ArrayList<ConceptObject> getConceptListOnly() {
		ArrayList<ConceptObject> list = new ArrayList<ConceptObject>();
		Iterator<String> it = conceptList.keySet().iterator();
		while(it.hasNext()){
			list.add((ConceptObject)conceptList.get((String) it.next()));
		}
		return list;
	}
	public ConceptObject getConcept(String conceptURI){
		if(conceptList.containsKey(conceptURI)){
			return (ConceptObject)conceptList.get(conceptURI);
		}else{
			return null;
		}
	}
	public boolean hasConcept(String conceptURI){
		return conceptList.containsKey(conceptURI);
	}
	public boolean isEmpty(){
		return conceptList.isEmpty();
	}
}
