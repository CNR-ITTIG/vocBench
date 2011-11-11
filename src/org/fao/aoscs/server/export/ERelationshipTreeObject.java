package org.fao.aoscs.server.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import net.sf.gilead.pojo.gwt.LightEntity;

import org.fao.aoscs.domain.LabelObject;

public class ERelationshipTreeObject extends LightEntity{
 

    private static final long serialVersionUID = 7595175769003791921L;

    private HashMap<String,ERelationshipObject> relationshipList = new HashMap<String,ERelationshipObject>();

	private HashMap<String,ArrayList<LabelObject>> relationshipDefinitionList = new HashMap<String,ArrayList<LabelObject>>();

	private HashMap<String,java.util.ArrayList<ERelationshipObject>> parentChild = new HashMap<String,java.util.ArrayList<ERelationshipObject>>();
	
	private boolean relationshipSelected = false;
	
	public HashMap<String, ERelationshipObject> getRelationshipList() {
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
	
	public ERelationshipObject getERelationshipObject(String relationshipURI){
		if(this.relationshipList.containsKey(relationshipURI)){
			return (ERelationshipObject) relationshipList.get(relationshipURI);
		}else{
			return null;
		}
	}

	public ArrayList<ERelationshipObject> getRootItem(){
		ArrayList<ERelationshipObject> list = new ArrayList<ERelationshipObject>();
		Iterator<String> it = relationshipList.keySet().iterator();
		while(it.hasNext()){
			String key = (String) it.next();
			ERelationshipObject rObj = (ERelationshipObject) relationshipList.get(key);
			if(rObj.isRootItem()){
				list.add(rObj);
			}
		}
		return list;
	}
	public void addRelationshipList(ERelationshipObject rObj) {
		if(!relationshipList.containsKey(rObj.getUri())){
			this.relationshipList.put(rObj.getUri(), rObj);
		}
	}
	
	public boolean hasRelationship(String relationshipURI){
		return relationshipList.containsKey(relationshipURI);
	}

	public void setRelationshipList(
			HashMap<String, ERelationshipObject> relationshipList) {
		this.relationshipList = relationshipList;
	}

	public void setParentChild(
			HashMap<String, java.util.ArrayList<ERelationshipObject>> parentChild) {
		this.parentChild = parentChild;
	}

	public HashMap<String, ArrayList<ERelationshipObject>> getParentChild() {
		return parentChild;
	}
	
	public boolean hasChild(String relationshipURI){
		return parentChild.containsKey(relationshipURI);
	}

	public ArrayList<ERelationshipObject> getChildOf(String parentURI){
		ArrayList<ERelationshipObject> list = new ArrayList<ERelationshipObject>();
		if(parentChild.containsKey(parentURI)){
			list = (ArrayList<ERelationshipObject>) parentChild.get(parentURI);
		}
		return list;
	}
	
	public void addParentChild(String parentURI ,ERelationshipObject childObj) {
		if(!parentChild.containsKey(parentURI)){
			ArrayList<ERelationshipObject> list = new ArrayList<ERelationshipObject>();
			parentChild.put(parentURI, list);
			list.add(childObj);
		}else{
			((ArrayList<ERelationshipObject>)parentChild.get(parentURI)).add(childObj);
		}
	}

	public boolean isRelationshipSelected() {
		return relationshipSelected;
	}

	public void setRelationshipSelected(boolean relationshipSelected) {
		this.relationshipSelected = relationshipSelected;
	}

}
