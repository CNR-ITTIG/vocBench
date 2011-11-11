package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class NewConceptObject extends LightEntity{

	private static final long serialVersionUID = 211939297696871302L;
	
	private int actionId;
	
	private int userId;
	
	private ConceptObject conceptObject;
	
	private TermObject termObject;
	
	private String conceptPosition;
	
	private ConceptObject selectedConceptObject;
	
	public int getActionId() {
		return actionId;
	}
	public void setActionId(int actionId) {
		this.actionId = actionId;
	}
	public ConceptObject getConceptObject() {
		return conceptObject;
	}
	public void setConceptObject(ConceptObject conceptObject) {
		this.conceptObject = conceptObject;
	}
	public String getConceptPosition() {
		return conceptPosition;
	}
	public void setConceptPosition(String conceptPosition) {
		this.conceptPosition = conceptPosition;
	}
	public ConceptObject getSelectedConceptObject() {
		return selectedConceptObject;
	}
	public void setSelectedConceptObject(ConceptObject selectedConceptObject) {
		this.selectedConceptObject = selectedConceptObject;
	}
	public TermObject getTermObject() {
		return termObject;
	}
	public void setTermObject(TermObject termObject) {
		this.termObject = termObject;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
