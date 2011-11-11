package org.fao.aoscs.domain;

import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;


public class TreePathObject extends LightEntity{

	private static final long serialVersionUID = -7451048933536412042L;

	private HashMap<String,NtreeItemObject > itemList = new HashMap<String,NtreeItemObject >();


	private NtreeItemObject rootItem = new NtreeItemObject();
	
	public HashMap<String,NtreeItemObject> getItemList() {
		return itemList;
	}
	
	public void setItemList(
			HashMap<String, NtreeItemObject> itemList) {
		this.itemList = itemList;
	}
	
	public void addItemList(NtreeItemObject ntObj) {
		if(!this.itemList.containsKey(ntObj.getURI())){
			this.itemList.put(ntObj.getURI(), ntObj);
		}
	}
	public NtreeItemObject getRootItem() {
		return rootItem;
	}
	public void setRootItem(NtreeItemObject rootItem) {
		this.rootItem = rootItem;
	}
	public boolean isEmpty(){
		return itemList.isEmpty();
	}
	
	public boolean hasItemInPath(String URI){
		return itemList.containsKey(URI);
	}
	public NtreeItemObject getItemInPath(String URI){
		if(itemList.containsKey(URI)){
			return (NtreeItemObject) itemList.get(URI);
		}else{
			return null;
		}
	}
}
