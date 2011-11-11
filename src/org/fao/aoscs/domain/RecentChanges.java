package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.Date;

import net.sf.gilead.pojo.gwt.LightEntity;

public class RecentChanges extends LightEntity {

	private static final long serialVersionUID = -2486389716915205119L;

	private int modifiedId;
	
	private ArrayList<LightEntity> modifiedObject = new ArrayList<LightEntity>();
	
	private byte[] object;
	
	private int modifiedActionId;
	
	private int modifierId;
	
	private Date modifiedDate;
	
	private int ontologyId;

	public int getModifiedId() {
		return modifiedId;
	}

	public void setModifiedId(int modifiedId) {
		this.modifiedId = modifiedId;
	}

	public ArrayList<LightEntity> getModifiedObject() {
		return modifiedObject;
	}

	public void setModifiedObject(ArrayList<LightEntity> modifiedObject) {
		this.modifiedObject = modifiedObject;
	}

	public byte[] getObject() {
		return object;
	}

	public void setObject(byte[] object) {
		this.object = object;
	}
	
	public int getModifierId() {
		return modifierId;
	}

	public void setModifierId(int modifierId) {
		this.modifierId = modifierId;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public int getOntologyId() {
		return ontologyId;
	}

	public void setOntologyId(int ontologyId) {
		this.ontologyId = ontologyId;
	}

	public int getModifiedActionId() {
		return modifiedActionId;
	}

	public void setModifiedActionId(int modifiedActionId) {
		this.modifiedActionId = modifiedActionId;
	}

}
