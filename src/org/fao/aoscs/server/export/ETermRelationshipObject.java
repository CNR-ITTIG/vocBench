package org.fao.aoscs.server.export;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

import org.fao.aoscs.domain.RelationshipObject;


public class ETermRelationshipObject extends LightEntity{

	private static final long serialVersionUID = 5664689197830339225L;
	
	private HashMap<RelationshipObject, ArrayList<ETermObject>> result = new HashMap<RelationshipObject, ArrayList<ETermObject>>();

	public HashMap<RelationshipObject, ArrayList<ETermObject>> getResult() {
		return result;
	}
	
	public void setResult(HashMap<RelationshipObject, ArrayList<ETermObject>> result) {
		this.result = result;
	}

	public void addResult(RelationshipObject rObj,ArrayList<ETermObject> conceptList) {
		this.result.put(rObj, conceptList);
	}
	public boolean hasValue(){
		return !result.isEmpty();
	}
	public boolean isEmpty(){
		return result.isEmpty();
	}
}
