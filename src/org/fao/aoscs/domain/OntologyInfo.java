package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class OntologyInfo extends LightEntity {

	private static final long serialVersionUID = -6562087502264561963L;

	private int ontologyId;

	private String ontologyName;
	
	private String ontologyDescription;
	
	private String dbUrl;
	
	private String dbDriver;
	
	private String dbTableName;
	
	private String dbUsername;
	
	private String dbPassword;
	
	private int ontologyShow;
	
	private String version;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getOntologyShow() {
		return ontologyShow;
	}

	public void setOntologyShow(int ontologyShow) {
		this.ontologyShow = ontologyShow;
	}

	public int getOntologyId() {
		return ontologyId;
	}

	public void setOntologyId(int ontologyId) {
		this.ontologyId = ontologyId;
	}

	public String getOntologyName() {
		return ontologyName.trim();
	}

	public void setOntologyName(String ontologyName) {
		this.ontologyName = ontologyName.trim();
	}

	public String getOntologyDescription() {
		return ontologyDescription;
	}

	public void setOntologyDescription(String ontologyDescription) {
		this.ontologyDescription = ontologyDescription;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getDbDriver() {
		return dbDriver;
	}

	public void setDbDriver(String dbDriver) {
		this.dbDriver = dbDriver;
	}

	public String getDbUsername() {
		return dbUsername;
	}

	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public void setDbTableName(String dbTableName) {
		this.dbTableName = dbTableName;
	}

	public String getDbTableName() {
		return dbTableName;
	}
	
	public String getModelID() {
		return getOntologyId()+"_"+getDbTableName();
	}
	
}
