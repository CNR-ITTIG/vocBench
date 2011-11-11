package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

public class TermMoveObject extends LightEntity{

	
	private static final long serialVersionUID = 12159661673742813L;
	
	private TermObject termObj = new TermObject();
	
	private String newConceptURI = "";
	
	private  String oldConceptURI = "";

	private HashMap<String, ArrayList<TermObject>> termList = new HashMap<String, ArrayList<TermObject>>();
	
	private HashMap<String, TermRelationshipObject> termRelList = new HashMap<String, TermRelationshipObject>();

	public void setTermList(HashMap<String, ArrayList<TermObject>> termList) {
		this.termList = termList;
	}

	public HashMap<String, ArrayList<TermObject>> getTermList() {
		return termList;
	}
	
	public void addTermList(String termLang, TermObject tObj) {
		if(!termList.containsKey(termLang)){
			ArrayList<TermObject> list = new ArrayList<TermObject>();
			termList.put(termLang, list);
			list.add(tObj);
		}else{
			((ArrayList<TermObject>)termList.get(termLang)).add(tObj);
		}
	}
	
	public boolean isTermListEmpty(){
		return termList.isEmpty();
	}

	public void setTermRelList(HashMap<String, TermRelationshipObject> termRelList) {
		this.termRelList = termRelList;
	}

	public HashMap<String, TermRelationshipObject> getTermRelList() {
		return termRelList;
	}
	
	public void addTermRelationship(String termUri, TermRelationshipObject termRelObject) {
		if(!termRelList.containsKey(termUri)){
			termRelList.put(termUri, termRelObject);
		}
	}
	
	public boolean isTermRelListEmpty(){
		return termRelList.isEmpty();
	}

	public void setNewConceptURI(String newConceptURI) {
		this.newConceptURI = newConceptURI;
	}

	public  String getNewConceptURI() {
		return newConceptURI;
	}

	public void setOldConceptURI(String oldConceptURI) {
		this.oldConceptURI = oldConceptURI;
	}

	public String getOldConceptURI() {
		return oldConceptURI;
	}

	public void setTermObj(TermObject termObj) {
		this.termObj = termObj;
	}

	public TermObject getTermObj() {
		return termObj;
	}
	
}
