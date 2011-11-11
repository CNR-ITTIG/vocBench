package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

public class InitializeRelationshipData extends LightEntity{

	private static final long serialVersionUID = -6023963657001673192L;
 
	private HashMap<String,String> actionMap = new HashMap<String,String>();

	private RelationshipTreeObject[] relationshipTree = new RelationshipTreeObject[2];

	private ArrayList<ClassObject> classTree = new ArrayList<ClassObject>();

	private HashMap<String,String> allDataType = new HashMap<String,String>();
	
	public HashMap<String, String> getActionMap() {
		return actionMap;
	}
	public HashMap<String, String> getAllDataType() {
		return allDataType;
	}
	public ArrayList<ClassObject> getClassTree() {
		return classTree;
	}
	public RelationshipTreeObject[] getRelationshipTree() {
		return relationshipTree;
	}
	public void setActionMap(HashMap<String, String> actionMap) {
		this.actionMap = actionMap;
	}
	public void setAllDataType(HashMap<String, String> allDataType) {
		this.allDataType = allDataType;
	}
	public void setClassTree(ArrayList<ClassObject> classTree) {
		this.classTree = classTree;
	}
	public void setRelationshipTree(RelationshipTreeObject[] relationshipTree) {
		this.relationshipTree = relationshipTree;
	}

 
	
}
