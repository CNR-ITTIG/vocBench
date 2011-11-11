package org.fao.aoscs.server.export;

import java.util.HashMap;

public class AgrovocObject {
	private HashMap<String, EConceptObject> conceptList = new HashMap<String, EConceptObject>();
	
	public HashMap<String, EConceptObject> getConceptList() {
		return conceptList;
	}
	
	public void addConceptList(EConceptObject cObj) {
		if(!conceptList.containsKey(cObj.getConceptURI())){
			conceptList.put(cObj.getConceptURI(), cObj);
		}
	}
	public boolean hasConcept(String conceptURI){
		return conceptList.containsKey(conceptURI);
	}
	
	public EConceptObject getConceptObject(String conceptURI){
		if(conceptList.containsKey(conceptURI)){
			return (EConceptObject) conceptList.get(conceptURI);
		}else{
			return null;
		}	
	}	
//==============
	private HashMap<String, ETermObject> termList = new HashMap<String, ETermObject>();
	
	public HashMap<String, ETermObject> getTermList() {
		return termList;
	}
	
	public void addTermList(ETermObject tObj) {
		if(!termList.containsKey(tObj.getUri())){
			termList.put(tObj.getUri(), tObj);
		}
	}
	public boolean hasTerm(String termURI){
		return termList.containsKey(termURI);
	}
	
	public ETermObject getTermObject(String termURI){
		if(termList.containsKey(termURI)){
			return (ETermObject) termList.get(termURI);
		}else{
			return null;
		}	
	}	

	
}
