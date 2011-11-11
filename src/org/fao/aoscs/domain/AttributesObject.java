package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class AttributesObject extends LightEntity{

	private static final long serialVersionUID = 3487262459731275117L;

	private NonFuncObject value;
	
	private RelationshipObject relationshipObject;
	
	public NonFuncObject getValue() {
		return value;
	}
	public void setValue(NonFuncObject value) {
		this.value = value;
	}
	public void setRelationshipObject(RelationshipObject rObj) {
		this.relationshipObject = rObj;
	}
	public RelationshipObject getRelationshipObject() {
		return relationshipObject;
	}
}
