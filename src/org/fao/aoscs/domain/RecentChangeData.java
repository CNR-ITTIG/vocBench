package org.fao.aoscs.domain;

import java.util.ArrayList;

import net.sf.gilead.pojo.gwt.LightEntity;

public class RecentChangeData extends LightEntity{

	private static final long serialVersionUID = -4156594049096689277L;

	private int id;
	
	private ArrayList<LightEntity> object = new ArrayList<LightEntity>();
	private ArrayList<LightEntity> newObject = new ArrayList<LightEntity>();
	private ArrayList<LightEntity> oldObject = new ArrayList<LightEntity>();
	
	private int actionId;
	
	private int modifierId;
	
	private int ownerId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getActionId() {
		return actionId;
	}

	public void setActionId(int action) {
		this.actionId = action;
	}

	public int getModifierId() {
		return modifierId;
	}

	public void setModifierId(int modifierId) {
		this.modifierId = modifierId;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public void setObject(ArrayList<LightEntity> object) {
		this.object = object;
	}
	
	public ArrayList<LightEntity> getObject() {
		return object;
	}

	public void setNewObject(ArrayList<LightEntity> newObject) {
		this.newObject = newObject;
	}
	
	public ArrayList<LightEntity> getNewObject() {
		return newObject;
	}

	public void setOldObject(ArrayList<LightEntity> oldObject) {
		this.oldObject = oldObject;
	}
	
	public ArrayList<LightEntity> getOldObject() {
		return oldObject;
	}

}
