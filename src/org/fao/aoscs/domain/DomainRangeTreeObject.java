package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

public class DomainRangeTreeObject extends LightEntity{

	private static final long serialVersionUID = 5224223791042522086L;

	private HashMap<String, ArrayList<String>> parentChild = new HashMap<String, ArrayList<String>>();

	private ArrayList<String> startNode = new ArrayList<String>();

	private HashMap<String,ClassObject> classList = new HashMap<String, ClassObject>();
	
	public boolean hasClass(String classURI){
		return classList.containsKey(classURI);
	}
	
	public void addClassList(ClassObject cObj) {
		if(!classList.containsKey(cObj.getUri())){
			classList.put(cObj.getUri(), cObj);
		}
	}
	
	public HashMap<String, ClassObject> getClassList() {
		return classList;
	}
	
	public void setClassList(HashMap<String, ClassObject> classList) {
		this.classList = classList;
	}
	
	public ClassObject getClass(String classURI){
		if(classList.containsKey(classURI)){
			return (ClassObject) classList.get(classURI);
		}else{
			return null;
		}
	}
	
	public boolean hasChild(String classURI){
		return this.parentChild.containsKey(classURI);
	}
	
	public ArrayList<String> getChildList(String classURI){
		if(this.parentChild.containsKey(classURI)){
			return (ArrayList<String>) parentChild.get(classURI);
		}else{
			return null;
		}
	}
	
	public HashMap<String, ArrayList<String>> getParentChild() {
		return parentChild;
	}
	
	public void setParentChild(HashMap<String, ArrayList<String>> parentChild) {
		this.parentChild = parentChild;
	}
	
	public void addParentChild(String parentURI,String childURI){
		if(!this.parentChild.containsKey(parentURI)){
			ArrayList<String> list = new ArrayList<String>();
			this.parentChild.put(parentURI, list);
			list.add(childURI);
		}else{
			((ArrayList<String>)parentChild.get(parentURI)).add(childURI);
		}
	}
	
	public ArrayList<String> getStartNode() {
		return startNode;
	}
	
	public void setStartNode(ArrayList<String> startNode) {
		this.startNode = startNode;
	}
	
	public void addStartNode(String classURI) {
		this.startNode.add(classURI);
	}

}
