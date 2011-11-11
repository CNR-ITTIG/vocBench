package org.fao.aoscs.domain;

import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

public class ClassificationObject extends LightEntity{
	
	private static final long serialVersionUID = -3100583661100266688L;
	
	public HashMap<String,SchemeObject> schemeList = new HashMap<String,SchemeObject>();

	public HashMap<String, SchemeObject> getSchemeList() {
		return schemeList;
	}
	
	public void setSchemeList(HashMap<String, SchemeObject> schemeList) {
		this.schemeList = schemeList;
	}

	public SchemeObject getSchemeObject(String schemeInstance){
		if(this.schemeList.containsKey(schemeInstance)){
			return (SchemeObject)this.schemeList.get(schemeInstance);
		}else{
			return null;
		}
	}
	
	public boolean hasScheme(String schemeInstance){
		if(schemeList.containsKey(schemeInstance)){
			return true;
		}else{
			return false;
		}
	}
	
	public void addSchemeList(String schemeInstance,SchemeObject sObj) {
		if(!this.schemeList.containsKey(schemeInstance)){
			this.schemeList.put(schemeInstance, sObj);
		}
	}
	
	public boolean isContainScheme(String schemeURI){
		if(this.schemeList.containsKey(schemeURI)){
			return true;
		}else{
			return false;
		}
	}
	
}
