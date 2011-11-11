package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class PermissionGroupMap extends LightEntity{

	private static final long serialVersionUID = -7082908760040590937L;

	private PermissionGroupMapId id;

	public PermissionGroupMap() {
	}

	public PermissionGroupMap(PermissionGroupMapId id) {
		this.id = id;
	}

	public PermissionGroupMapId getId() {
		return this.id;
	}

	public void setId(PermissionGroupMapId id) {
		this.id = id;
	}

}
