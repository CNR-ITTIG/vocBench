package org.fao.aoscs.domain;

import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

public class TermCodeObject extends LightEntity {

	private static final long serialVersionUID = 184862873971199626L;
	
	HashMap<RelationshipObject, TermCodesObject>  result = new HashMap<RelationshipObject, TermCodesObject>();

	public void setResult(HashMap<RelationshipObject, TermCodesObject> result) {
		this.result = result;
	}

	public HashMap<RelationshipObject, TermCodesObject> getResult() {
		return result;
	}

	public void addResult(RelationshipObject rObj , TermCodesObject code) {
		this.result.put(rObj, code);
	}
	
	public boolean isEmpty(){
		return result.isEmpty();
	}
}
