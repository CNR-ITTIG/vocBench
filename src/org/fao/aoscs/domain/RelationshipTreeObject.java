package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import net.sf.gilead.pojo.gwt.LightEntity;

public class RelationshipTreeObject extends LightEntity{
 
	private static final long serialVersionUID = 8428722795387751725L;

	private HashMap<String,RelationshipObject> relationshipList = new HashMap<String,RelationshipObject>();

	private HashMap<String,ArrayList<LabelObject>> relationshipDefinitionList = new HashMap<String,ArrayList<LabelObject>>();

	private HashMap<String,java.util.ArrayList<RelationshipObject>> parentChild = new HashMap<String,java.util.ArrayList<RelationshipObject>>();
	
	private boolean relationshipSelected = false;
	
	public HashMap<String, RelationshipObject> getRelationshipList() {
		return relationshipList;
	}
	
	public HashMap<String, ArrayList<LabelObject>> getRelationshipDefinitionList() {
		return relationshipDefinitionList;
	}
	
	public void setRelationshipDefinitionList(HashMap<String, ArrayList<LabelObject>> list) {
		this.relationshipDefinitionList  = list;
	}
	
	public void addRelationshipDefinition(String uri , ArrayList<LabelObject> labelList){
		if(! this.relationshipDefinitionList.containsKey(uri))
			relationshipDefinitionList.put(uri , labelList);
	}
	
	public ArrayList<LabelObject> getRelationshipDefinition(String uri){
		return relationshipDefinitionList.get(uri);
	}
	
	public RelationshipObject getRelationshipObject(String relationshipURI){
		if(this.relationshipList.containsKey(relationshipURI)){
			return (RelationshipObject) relationshipList.get(relationshipURI);
		}else{
			return null;
		}
	}

	public ArrayList<RelationshipObject> getRootItem(){
		ArrayList<RelationshipObject> list = new ArrayList<RelationshipObject>();
		Iterator<String> it = relationshipList.keySet().iterator();
		while(it.hasNext()){
			String key = (String) it.next();
			RelationshipObject rObj = (RelationshipObject) relationshipList.get(key);
			if(rObj.isRootItem()){
				list.add(rObj);
			}
		}
		return list;
	}
	public void addRelationshipList(RelationshipObject rObj) {
		if(!relationshipList.containsKey(rObj.getUri())){
			this.relationshipList.put(rObj.getUri(), rObj);
		}
	}
	
	public boolean hasRelationship(String relationshipURI){
		return relationshipList.containsKey(relationshipURI);
	}

	public void setRelationshipList(
			HashMap<String, RelationshipObject> relationshipList) {
		this.relationshipList = relationshipList;
	}

	public void setParentChild(
			HashMap<String, java.util.ArrayList<RelationshipObject>> parentChild) {
		this.parentChild = parentChild;
	}

	public HashMap<String, ArrayList<RelationshipObject>> getParentChild() {
		return parentChild;
	}
	
	public boolean hasChild(String relationshipURI){
		return parentChild.containsKey(relationshipURI);
	}

	public ArrayList<RelationshipObject> getChildOf(String parentURI){
		ArrayList<RelationshipObject> list = new ArrayList<RelationshipObject>();
		if(parentChild.containsKey(parentURI)){
			list = (ArrayList<RelationshipObject>) parentChild.get(parentURI);
		}
		return list;
	}
	
	public void addParentChild(String parentURI ,RelationshipObject childObj) {
		if(!parentChild.containsKey(parentURI)){
			ArrayList<RelationshipObject> list = new ArrayList<RelationshipObject>();
			parentChild.put(parentURI, list);
			list.add(childObj);
		}else{
			((ArrayList<RelationshipObject>)parentChild.get(parentURI)).add(childObj);
		}
	}

	public boolean isRelationshipSelected() {
		return relationshipSelected;
	}

	public void setRelationshipSelected(boolean relationshipSelected) {
		this.relationshipSelected = relationshipSelected;
	}

}
