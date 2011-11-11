package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

public class InitializeSystemData extends LightEntity{

	private static final long serialVersionUID = -7552794453632777361L;

	private ArrayList<LanguageCode> language = new ArrayList<LanguageCode>();

	private HashMap<String,String> configConstants = new HashMap<String,String>();
	
	private HashMap<String,String> modelConstants = new HashMap<String,String>();

	private HashMap<String,String>  owlStatusConstants = new HashMap<String,String>();
	
	private HashMap<String,Integer>  owlActionConstants = new HashMap<String,Integer>();
	
	private ArrayList<OntologyInfo> ontology = new ArrayList<OntologyInfo>();
	
	private PermissionObject  permissionTable = new PermissionObject();
	
	public PermissionObject getPermissionTable() {
		return permissionTable;
	}
	public void setPermissionTable(PermissionObject permissionTable) {
		this.permissionTable = permissionTable;
	}
	public ArrayList<LanguageCode> getLanguage() {
		return language;
	}
	public void setLanguage(ArrayList<LanguageCode> language) {
		this.language = language;
	}
	public HashMap<String, String> getConfigConstants() {
		return configConstants;
	}
	public void setConfigConstants(HashMap<String, String> configConstants) {
		this.configConstants = configConstants;
	}
	public HashMap<String, String> getModelConstants() {
		return modelConstants;
	}
	public void setModelConstants(HashMap<String, String> modelConstants) {
		this.modelConstants = modelConstants;
	}
	public HashMap<String, String> getOwlStatusConstants() {
		return owlStatusConstants;
	}
	public void setOwlStatusConstants(HashMap<String, String> owlStatusConstants) {
		this.owlStatusConstants = owlStatusConstants;
	}
	public HashMap<String,Integer> getOwlActionConstants() {
		return owlActionConstants;
	}
	public void setOwlActionConstants(HashMap<String, Integer> owlActionConstants) {
		this.owlActionConstants = owlActionConstants;
	}
	public void setOntology(ArrayList<OntologyInfo> ontology) {
		this.ontology = ontology;
	}
	public ArrayList<OntologyInfo> getOntology() {
		return ontology;
	}
}
