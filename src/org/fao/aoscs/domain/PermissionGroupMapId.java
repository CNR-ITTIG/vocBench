package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class PermissionGroupMapId extends LightEntity{

	private static final long serialVersionUID = 3234462045613305169L;
	private int usersGroupsId;
	private int permissionId;
	private String groupName;
	private String permitName;

	public PermissionGroupMapId() {
	}

	public PermissionGroupMapId(int usersGroupsId, int permissionId) {
		this.usersGroupsId = usersGroupsId;
		this.permissionId = permissionId;
	}

	public int getUsersGroupsId() {
		return this.usersGroupsId;
	}

	public void setUsersGroupsId(int usersGroupsId) {
		this.usersGroupsId = usersGroupsId;
	}

	public int getPermissionId() {
		return this.permissionId;
	}

	public void setPermissionId(int permissionId) {
		this.permissionId = permissionId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof PermissionGroupMapId))
			return false;
		PermissionGroupMapId castOther = (PermissionGroupMapId) other;

		return (this.getUsersGroupsId() == castOther.getUsersGroupsId())
				&& (this.getPermissionId() == castOther.getPermissionId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getUsersGroupsId();
		result = 37 * result + this.getPermissionId();
		return result;
	}

	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param permitName the permitName to set
	 */
	public void setPermitName(String permitName) {
		this.permitName = permitName;
	}

	/**
	 * @return the permitName
	 */
	public String getPermitName() {
		return permitName;
	}

}
