package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;


public class TermRelationshipObject extends LightEntity{

	private static final long serialVersionUID = 5664689197830339225L;
	
	private HashMap<RelationshipObject, ArrayList<TermObject>> result = new HashMap<RelationshipObject, ArrayList<TermObject>>();

	public HashMap<RelationshipObject, ArrayList<TermObject>> getResult() {
		return result;
	}
	
	public void setResult(HashMap<RelationshipObject, ArrayList<TermObject>> result) {
		this.result = result;
	}

	public void addResult(RelationshipObject rObj,ArrayList<TermObject> conceptList) {
		this.result.put(rObj, conceptList);
	}
	public boolean hasValue(){
		return !result.isEmpty();
	}
	public boolean isEmpty(){
		return result.isEmpty();
	}
}
