package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class UsersLanguage extends LightEntity {

	private static final long serialVersionUID = -3352424085409921072L;
	
	private UsersLanguageId id;

	public UsersLanguage() {
	}

	public UsersLanguage(UsersLanguageId id) {
		this.id = id;
	}

	public UsersLanguageId getId() {
		return this.id;
	}

	public void setId(UsersLanguageId id) {
		this.id = id;
	}

}
