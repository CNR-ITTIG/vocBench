package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class OwlStatus extends LightEntity {

	private static final long serialVersionUID = -6013279162149886444L;

	private int id;

	private String status;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
