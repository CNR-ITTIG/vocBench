package org.fao.aoscs.domain;

import java.util.Date;
import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

public class TermObject extends LightEntity {

	private static final long serialVersionUID = 7063968525071738465L;

	private String conceptUri;
	
	private String conceptName;
	
	private String uri;
	
	private String name;

	private String label;

	private String lang;
	
	private int statusID;
	
	private String status;
	
	private Date dateCreate;

	private Date dateModified;
	
	private boolean mainLabel;
	
	private HashMap<String,AttributesObject>  termCode = new HashMap<String,AttributesObject> ();
	
	public Date getDateCreate() {
		return dateCreate;
	}

	public void setDateCreate(Date dateCreate) {
		this.dateCreate = dateCreate;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public boolean isMainLabel() {
		return mainLabel;
	}

	public void setMainLabel(boolean mainLabel) {
		this.mainLabel = mainLabel;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getStatusID() {
		return statusID;
	}

	public void setStatusID(int statusID) {
		this.statusID = statusID;
	}
	
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getConceptUri() {
		return conceptUri;
	}

	public void setConceptUri(String conceptUri) {
		this.conceptUri = conceptUri;
	}

	public HashMap<String, AttributesObject> getTermCode() {
		return termCode;
	}

	public void setTermCode(HashMap<String, AttributesObject> termCode) {
		this.termCode = termCode;
	}
	
	public void addTermCode(String repository,AttributesObject tcObj) {
		if(!this.termCode.containsKey(repository)){
			this.termCode.put(repository, tcObj);
		}
	}
	
	public String getName(){
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getConceptName() {
		return conceptName;
	}

	public void setConceptName(String conceptName) {
		this.conceptName = conceptName;
	}
	
}
