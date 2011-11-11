package org.fao.aoscs.domain;

import java.util.ArrayList;

import net.sf.gilead.pojo.gwt.LightEntity;


public class NtreeItemObject extends LightEntity{
	
	private static final long serialVersionUID = -8222457005177611892L;

	private TreeObject treeObject = new TreeObject();

	private ArrayList<TreeObject> child = new ArrayList<TreeObject>();
	
	public void setChild(ArrayList<TreeObject> child) {
		this.child = child;
	}
	public ArrayList<TreeObject> getChild() {
		return child;
	}
	public void addChild(TreeObject cObj) {
		this.child.add(cObj);
	}
	public String getURI(){
		return treeObject.getUri();	
	}
	public int getChildCount(){
		return child.size();
	}
	public TreeObject getChild(int index){
		return (TreeObject) child.get(index);
	}
	public TreeObject getTreeObject() {
		return treeObject;
	}
	public void setTreeObject(TreeObject treeObject) {
		this.treeObject = treeObject;
	}
}
