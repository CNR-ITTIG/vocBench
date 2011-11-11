package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.Date;

import net.sf.gilead.pojo.gwt.LightEntity;

public class Consistency extends LightEntity {

	private static final long serialVersionUID = -2825850755810298540L;

	private ConceptObject concept =  new ConceptObject();

	private TermObject term =  new TermObject();

	private RelationshipObject relationship =  new RelationshipObject();
	
	private ConceptObject destConcept =  new ConceptObject();
	
	private String status;
	
	private String destStatus;

	private Date dateCreate;

	private Date dateModified;
	
	private String termCode;
	
	private String termCodeProperty;
	
	private ArrayList<String> languages = new ArrayList<String>();
	
	private Boolean showStatus;
	
	private Boolean showDestStatus;
	
	private Boolean showTermCodeProperty;
	
	private Boolean showDate;
	
	private Boolean showLanguage;
	
	public ConceptObject getConcept() {
		return concept;
	}

	public void setConcept(ConceptObject concept) {
		this.concept = concept;
	}

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

	public ConceptObject getDestConcept() {
		return destConcept;
	}

	public void setDestConcept(ConceptObject destConcept) {
		this.destConcept = destConcept;
	}

	public String getDestStatus() {
		return destStatus;
	}

	public void setDestStatus(String destStatus) {
		this.destStatus = destStatus;
	}

	public RelationshipObject getRelationship() {
		return relationship;
	}

	public void setRelationship(RelationshipObject relationship) {
		this.relationship = relationship;
	}

	public Boolean getShowDate() {
		return showDate;
	}

	public void setShowDate(Boolean showDate) {
		this.showDate = showDate;
	}

	public Boolean getShowStatus() {
		return showStatus;
	}

	public void setShowStatus(Boolean showStatus) {
		this.showStatus = showStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public TermObject getTerm() {
		return term;
	}

	public void setTerm(TermObject term) {
		this.term = term;
	}

	public String getTermCode() {
		return termCode;
	}

	public void setTermCode(String termCode) {
		this.termCode = termCode;
	}

	public String getTermCodeProperty() {
		return termCodeProperty;
	}

	public void setTermCodeProperty(String termCodeProperty) {
		this.termCodeProperty = termCodeProperty;
	}

	public Boolean getShowDestStatus() {
		return showDestStatus;
	}

	public void setShowDestStatus(Boolean showDestStatus) {
		this.showDestStatus = showDestStatus;
	}

	public Boolean getShowTermCodeProperty() {
		return showTermCodeProperty;
	}

	public void setShowTermCodeProperty(Boolean showTermCodeProperty) {
		this.showTermCodeProperty = showTermCodeProperty;
	}

	public Boolean getShowLanguage() {
		return showLanguage;
	}

	public void setShowLanguage(Boolean showLanguage) {
		this.showLanguage = showLanguage;
	}

	public ArrayList<String> getLanguages() {
		return languages;
	}

	public void setLanguages(ArrayList<String> languages) {
		this.languages = languages;
	}
	
	public void addLanguages(String lang) {
		if(!this.languages.contains(lang)){
			this.languages.add(lang);
		}
	}
	
}
