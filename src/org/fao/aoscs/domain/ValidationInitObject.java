package org.fao.aoscs.domain;

import java.util.ArrayList;

import net.sf.gilead.pojo.gwt.LightEntity;

public class ValidationInitObject extends LightEntity {

	private static final long serialVersionUID = -8359384092953365824L;
	
	private ArrayList<OwlStatus> status;
	
	private ArrayList<OwlAction> action;
	
	private ArrayList<Users>  user;
	
	private ArrayList<ValidationPermission>  permissions;
	
	private int  validationSize;
	
	public ArrayList<OwlStatus> getStatus() {
		return status;
	}
	public void setStatus(ArrayList<OwlStatus> status) {
		this.status = status;
	}
	public ArrayList<OwlAction> getAction() {
		return action;
	}
	public void setAction(ArrayList<OwlAction> action) {
		this.action = action;
	}
	public ArrayList<Users> getUser() {
		return user;
	}
	public void setUser(ArrayList<Users> user) {
		this.user = user;
	}
	public ArrayList<ValidationPermission> getPermissions() {
		return permissions;
	}
	public void setPermissions(ArrayList<ValidationPermission> permissions) {
		this.permissions = permissions;
	}
	public int getValidationSize() {
		return validationSize;
	}
	public void setValidationSize(int validationSize) {
		this.validationSize = validationSize;
	}

	
}
