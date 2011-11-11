package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class OwlAction extends LightEntity {

	private static final long serialVersionUID = -5885872018831880758L;

	private int id;

	private String action;

	private String actionChild;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getActionChild() {
		return this.actionChild;
	}

	public void setActionChild(String actionChild) {
		this.actionChild = actionChild;
	}

}
