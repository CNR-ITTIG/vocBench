package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class ValidationPermission extends LightEntity  {

	private static final long serialVersionUID = -8817479968777674067L;

	private int id;

	private int usersGroupsId;

	private int action;

	private int status;

	private int newstatus;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUsersGroupsId() {
		return this.usersGroupsId;
	}

	public void setUsersGroupsId(int usersGroupsId) {
		this.usersGroupsId = usersGroupsId;
	}

	public int getAction() {
		return this.action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getNewstatus() {
		return this.newstatus;
	}

	public void setNewstatus(int newstatus) {
		this.newstatus = newstatus;
	}

}
