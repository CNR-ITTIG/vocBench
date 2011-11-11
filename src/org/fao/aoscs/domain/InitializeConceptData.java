package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;


public class InitializeConceptData extends LightEntity{

	private static final long serialVersionUID = -882900083093205890L;

	private ArrayList<String[]> source = new ArrayList<String[]>();
	 
	private HashMap<String,String> actionMap = new HashMap<String,String>();

	private HashMap<String,OwlStatus> actionStatus = new HashMap<String,OwlStatus>();
	
	private ArrayList<RelationshipObject> termCodeProperties = new ArrayList<RelationshipObject>();
	
	private ArrayList<RelationshipObject> conceptEditorialAttributes = new ArrayList<RelationshipObject>();
	
	private ArrayList<RelationshipObject> conceptDomainAttributes = new ArrayList<RelationshipObject>();
	
	private ArrayList<RelationshipObject> termEditorialAttributes = new ArrayList<RelationshipObject>();
	
	private ArrayList<RelationshipObject> termDomainAttributes = new ArrayList<RelationshipObject>();
	
	private HashMap<String,String> termCodePropertyType = new HashMap<String,String>();
	
	private ArrayList<TreeObject> conceptTreeObject = new ArrayList<TreeObject>();
	
	private ClassificationObject classificationObject =  new ClassificationObject();
	
	private int belongsToModule;
	
    public static final int CONCEPTMODULE = 0;
	
	public static final int CLASSIFICATIONMODULE = 1;
	
	public HashMap<String, String> getActionMap() {
		return actionMap;
	}
	public void setActionMap(HashMap<String, String> actionMap) {
		this.actionMap = actionMap;
	}
	public HashMap<String, OwlStatus> getActionStatus() {
		return actionStatus;
	}
	public void setActionStatus(HashMap<String, OwlStatus> actionStatus) {
		this.actionStatus = actionStatus;
	}
	public ArrayList<String[]> getSource() {
		return source;
	}
	public void setSource(ArrayList<String[]> source) {
		this.source = source;
	}
	public HashMap<String, String> getTermCodePropertyType() {
		return termCodePropertyType;
	}
	public void setTermCodePropertyType(HashMap<String, String> termCodePropertyType) {
		this.termCodePropertyType = termCodePropertyType;
	}
	public ArrayList<TreeObject> getConceptTreeObject() {
		return conceptTreeObject;
	}
	public void setConceptTreeObject(ArrayList<TreeObject> tree) {
		this.conceptTreeObject = tree;
	}
	public ClassificationObject getClassificationObject() {
		return classificationObject;
	}
	public void setClassificationObject(ClassificationObject classificationObject) {
		this.classificationObject = classificationObject;
	}
	public ArrayList<RelationshipObject> getTermCodeProperties() {
		return termCodeProperties;
	}
	public void setTermCodeProperties(ArrayList<RelationshipObject> termCodeProperties) {
		this.termCodeProperties = termCodeProperties;
	}
	public int getBelongsToModule() {
		return belongsToModule;
	}
	public void setBelongsToModule(int belongsToModule) {
		this.belongsToModule = belongsToModule;
	}
	public void setConceptDomainAttributes(ArrayList<RelationshipObject> conceptDomainAttributes) {
		this.conceptDomainAttributes = conceptDomainAttributes;
	}
	public ArrayList<RelationshipObject> getConceptDomainAttributes() {
		return conceptDomainAttributes;
	}
	public void setConceptEditorialAttributes(
			ArrayList<RelationshipObject> conceptEditorialAttributes) {
		this.conceptEditorialAttributes = conceptEditorialAttributes;
	}
	public ArrayList<RelationshipObject> getConceptEditorialAttributes() {
		return conceptEditorialAttributes;
	}
	public void setTermEditorialAttributes(ArrayList<RelationshipObject> termEditorialAttributes) {
		this.termEditorialAttributes = termEditorialAttributes;
	}
	public ArrayList<RelationshipObject> getTermEditorialAttributes() {
		return termEditorialAttributes;
	}
	public void setTermDomainAttributes(ArrayList<RelationshipObject> termDomainAttributes) {
		this.termDomainAttributes = termDomainAttributes;
	}
	public ArrayList<RelationshipObject> getTermDomainAttributes() {
		return termDomainAttributes;
	}
}
