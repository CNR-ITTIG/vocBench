package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class UsersGroups extends LightEntity {

	private static final long serialVersionUID = -896512374042521544L;

	private Integer usersGroupsId;
	
	private String usersGroupsName;
	
	private String usersGroupsDesc;


	public Integer getUsersGroupsId() {
		return this.usersGroupsId;
	}

	public void setUsersGroupsId(Integer usersGroupsId) {
		this.usersGroupsId = usersGroupsId;
	}

	public String getUsersGroupsName() {
		return this.usersGroupsName;
	}

	public void setUsersGroupsName(String usersGroupsName) {
		this.usersGroupsName = usersGroupsName;
	}

	public String getUsersGroupsDesc() {
		return this.usersGroupsDesc;
	}

	public void setUsersGroupsDesc(String usersGroupsDesc) {
		this.usersGroupsDesc = usersGroupsDesc;
	}

}
