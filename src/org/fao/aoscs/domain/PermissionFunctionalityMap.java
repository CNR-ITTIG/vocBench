package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class PermissionFunctionalityMap extends LightEntity{

	private static final long serialVersionUID = -7082908760040590937L;

	private int functionId;
	private int groupId;
	private int status;

	public PermissionFunctionalityMap() {
	}

	public int getFunctionId() {
		return functionId;
	}

	public void setFunctionId(int functionId) {
		this.functionId = functionId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}