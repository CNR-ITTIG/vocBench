package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.Date;

import net.sf.gilead.pojo.gwt.LightEntity;

public class Validation extends LightEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9024445936408520771L;

	private int id;

	private ConceptObject conceptObject;
	
	private byte[] concept;
	
	private TermObject termObject;

	private byte[] term;

	private int ownerId;

	private int modifierId;

	private int action;
	
	private RelationshipObject oldRelationshipObject;

	private byte[] oldRelationship;
	
	private ArrayList<LightEntity> oldObject;

	private byte[] oldValue;
	
	private RelationshipObject newRelationshipObject;

	private byte[] newRelationship;
	
	private ArrayList<LightEntity> newObject;

	private byte[] newValue;
	
	private int oldStatus;
	
	private int status;
	
	private String statusLabel;
	
	private String oldStatusLabel;

	private Date dateCreate;

	private Date dateModified;

	private String note;

	private int validatorId;

	private Boolean isValidate = new Boolean(false);
	
	private int ontologyId;
	
	private int statusColumn;
	
	private int noteColumn;
	
	private Boolean showUser;
	
	private Boolean showStatus;
	
	private Boolean showAction;
	
	private Boolean showDate;
	
	private Boolean isAccept;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte[] getConcept() {
		return this.concept;
	}

	public void setConcept(byte[] concept) {
		this.concept = concept;
	}

	public byte[] getTerm() {
		return this.term;
	}

	public void setTerm(byte[] term) {
		this.term = term;
	}

	public int getOwnerId() {
		return this.ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public int getModifierId() {
		return this.modifierId;
	}

	public void setModifierId(int modifierId) {
		this.modifierId = modifierId;
	}

	public int getAction() {
		return this.action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public byte[] getOldRelationship() {
		return this.oldRelationship;
	}

	public void setOldRelationship(byte[] oldRelationship) {
		this.oldRelationship = oldRelationship;
	}

	public byte[] getOldValue() {
		return this.oldValue;
	}

	public void setOldValue(byte[] oldValue) {
		this.oldValue = oldValue;
	}

	public byte[] getNewRelationship() {
		return this.newRelationship;
	}

	public void setNewRelationship(byte[] newRelationship) {
		this.newRelationship = newRelationship;
	}

	public byte[] getNewValue() {
		return this.newValue;
	}

	public void setNewValue(byte[] newValue) {
		this.newValue = newValue;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getValidatorId() {
		return this.validatorId;
	}

	public void setValidatorId(int validatorId) {
		this.validatorId = validatorId;
	}

	public Boolean getIsValidate() {
		return this.isValidate;
	}

	public void setIsValidate(Boolean isValidate) {
		this.isValidate = isValidate;
	}

	public ConceptObject getConceptObject() {
		return conceptObject;
	}

	public void setConceptObject(ConceptObject conceptObject) {
		this.conceptObject = conceptObject;
	}

	public Boolean getIsAccept() {
		return isAccept;
	}

	public void setIsAccept(Boolean isAccept) {
		this.isAccept = isAccept;
	}

	public ArrayList<LightEntity> getNewObject() {
		return newObject;
	}

	public void setNewObject(ArrayList<LightEntity> newObject) {
		this.newObject = newObject;
	}

	public RelationshipObject getNewRelationshipObject() {
		return newRelationshipObject;
	}

	public void setNewRelationshipObject(RelationshipObject newRelationshipObject) {
		this.newRelationshipObject = newRelationshipObject;
	}

	public int getNoteColumn() {
		return noteColumn;
	}

	public void setNoteColumn(int noteColumn) {
		this.noteColumn = noteColumn;
	}

	public ArrayList<LightEntity> getOldObject() {
		return oldObject;
	}

	public void setOldObject(ArrayList<LightEntity> oldObject) {
		this.oldObject = oldObject;
	}

	public RelationshipObject getOldRelationshipObject() {
		return oldRelationshipObject;
	}

	public void setOldRelationshipObject(RelationshipObject oldRelationshipObject) {
		this.oldRelationshipObject = oldRelationshipObject;
	}

	public Boolean getShowAction() {
		return showAction;
	}

	public void setShowAction(Boolean showAction) {
		this.showAction = showAction;
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

	public Boolean getShowUser() {
		return showUser;
	}

	public void setShowUser(Boolean showUser) {
		this.showUser = showUser;
	}

	public int getStatusColumn() {
		return statusColumn;
	}

	public void setStatusColumn(int statusColumn) {
		this.statusColumn = statusColumn;
	}

	public TermObject getTermObject() {
		return termObject;
	}

	public void setTermObject(TermObject termObject) {
		this.termObject = termObject;
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

	public int getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(int oldStatus) {
		this.oldStatus = oldStatus;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getOldStatusLabel() {
		return oldStatusLabel;
	}

	public void setOldStatusLabel(String oldStatusLabel) {
		this.oldStatusLabel = oldStatusLabel;
	}

	public String getStatusLabel() {
		return statusLabel;
	}

	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}

	public int getOntologyId() {
		return ontologyId;
	}

	public void setOntologyId(int ontologyId) {
		this.ontologyId = ontologyId;
	}

}
