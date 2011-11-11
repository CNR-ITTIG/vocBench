package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

public class RelationObject extends LightEntity{

	private static final long serialVersionUID = 3940812391467681877L;
	
	private HashMap<RelationshipObject, ArrayList<ConceptObject>>  result = new HashMap<RelationshipObject, ArrayList<ConceptObject>>();
	
	public void setResult(HashMap<RelationshipObject, ArrayList<ConceptObject>> result) {
		this.result = result;
	}
	
	public HashMap<RelationshipObject, ArrayList<ConceptObject>> getResult() {
		return result;
	}

	public void addResult(RelationshipObject rObj,ArrayList<ConceptObject> conceptList) {
		this.result.put(rObj, conceptList);
	}
	public boolean hasValue(){
		return !result.isEmpty();
	}
	
	public int getRelationshipCount()
	{
		int cnt=0;
		for(RelationshipObject rObj: result.keySet())
		{
			cnt+= result.get(rObj).size();
		}
		return cnt;
	}
}
