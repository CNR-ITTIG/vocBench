package org.fao.aoscs.domain;

import java.util.ArrayList;

import net.sf.gilead.pojo.gwt.LightEntity;

public class ConsistencyInitObject extends LightEntity {

	private static final long serialVersionUID = 8328349455724140622L;
	
	private ArrayList<String> status;
	
	private ArrayList<String> termCode;
	
	public ArrayList<String> getStatus() {
		return status;
	}

	public void setStatus(ArrayList<String> status) {
		this.status = status;
	}

	public ArrayList<String> getTermCode() {
		return termCode;
	}

	public void setTermCode(ArrayList<String> termCode) {
		this.termCode = termCode;
	}
	
	

	
}
