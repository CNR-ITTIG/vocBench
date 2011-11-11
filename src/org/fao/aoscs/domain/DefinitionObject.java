package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import net.sf.gilead.pojo.gwt.LightEntity;

public class DefinitionObject extends LightEntity{

	private static final long serialVersionUID = -4745740101124613743L;
	
	private HashMap<String, IDObject> definitionList = new HashMap<String, IDObject>();
	
	public void setDefinitionList(HashMap<String, IDObject> definitionList) {
		this.definitionList = definitionList;
	}
	
	public HashMap<String, IDObject> getDefinitionList() {
		return definitionList;
	}

	public ArrayList<IDObject> getDefinitionListOnly() {
		ArrayList<IDObject> list = new ArrayList<IDObject>();
		Iterator<String> it = definitionList.keySet().iterator();
		while(it.hasNext()){
			String key = (String) it.next();
			IDObject dObj = (IDObject) definitionList.get(key);
			list.add(dObj);
		}
		return list;
	}
	
	public void addDefinitionList(String defIns,IDObject dObj) {
		if(!definitionList.containsKey(defIns)){
			this.definitionList.put(defIns, dObj);
		}
	}
	
	public boolean hasDefinition(String defIns){
		if(this.definitionList.containsKey(defIns)){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isEmpty(){
		if(definitionList==null) 
			return false;
		else
			return definitionList.isEmpty();
	}
	
	public int getDefinitionCount(){
		if(definitionList==null) 
			return 0;
		else
			return definitionList.size();
	}
	
	public IDObject getDefinition(String defIns){
		if(definitionList.containsKey(defIns)){
			return (IDObject)definitionList.get(defIns);
		}else{
			return null;
		}
	}
	
	
}
