package org.fao.aoscs.server.export;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

public class ERelationshipObject extends LightEntity {

    private static final long serialVersionUID = 1535611152899758428L;

    
    private HashMap<String, String> labelList = new HashMap<String, String>();

	private String uri;
	
	private String name ;
	
	private ArrayList<String> parent;
	
	private boolean rootItem = false;
	
	//private ArrayList<Object> childList = new ArrayList<Object>();
	
	private ERelationshipObject parentObject;
	
	private String type;
	
	private String linkLevel;
	
	private String inversePropertyName;
	
	public static final String OBJECT = "OBJECT PROPERTY";
	
	public static final String DATATYPE = "DATATYPE PROPERTY";
	
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public ArrayList<String> getParent() {
		return parent;
	}

	public void setParent(ArrayList<String> parent) {
		this.parent = parent;
	}

	public HashMap<String, String> getLabelList() {
		return labelList;
	}
	
	public void setLabelList(HashMap<String, String> labelList) {
		this.labelList = labelList;
	}
	
	public String getLabel(String language){
		if(labelList.containsKey(language)){
			return (String) labelList.get(language);
		}else{
			return null;
		}
	}
	
	public boolean hasLabel(String language){
		return labelList.containsKey(language);
	}
	
	public void addLabelList(String label,String language) {
		if(!labelList.containsKey(language)){
			labelList.put(language, label);
		}
	}

	public boolean isRootItem() {
		return rootItem;
	}

	public void setRootItem(boolean rootItem) {
		this.rootItem = rootItem;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ERelationshipObject getParentObject() {
		return parentObject;
	}

	public void setParentObject(ERelationshipObject parentObject) {
		this.parentObject = parentObject;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getLinkLevel()
    {
        return linkLevel;
    }

    public void setLinkLevel(String linkLevel)
    {
        this.linkLevel = linkLevel;
    }

    public String getInversePropertyName()
    {
        return inversePropertyName;
    }

    public void setInversePropertyName(String inversePropertyName)
    {
        this.inversePropertyName = inversePropertyName;
    }
	
}
