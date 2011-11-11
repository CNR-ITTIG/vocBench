package org.fao.aoscs.domain;

import java.util.ArrayList;

import net.sf.gilead.pojo.gwt.LightEntity;

public class RecentChangesInitObject extends LightEntity {

	private static final long serialVersionUID = -2155960541213261144L;

	private ArrayList<Users> users;

	private ArrayList<OwlAction> actions;
	
	private ArrayList<RecentChanges> recentChanges;
	
	private int size;

	public ArrayList<Users> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<Users> arrayList) {
		this.users = arrayList;
	}

	public ArrayList<OwlAction> getActions() {
		return actions;
	}

	public void setActions(ArrayList<OwlAction> arrayList) {
		this.actions = arrayList;
	}

	public ArrayList<RecentChanges> getRecentChanges() {
		return recentChanges;
	}

	public void setRecentChanges(ArrayList<RecentChanges> recentChanges) {
		this.recentChanges = recentChanges;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
}
