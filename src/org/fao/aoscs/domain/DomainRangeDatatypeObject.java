package org.fao.aoscs.domain;

import java.util.ArrayList;

import net.sf.gilead.pojo.gwt.LightEntity;

public class DomainRangeDatatypeObject extends LightEntity{

	private static final long serialVersionUID = 2188011892922624003L;

	private ArrayList<ClassObject> domain;
	
	private String rangeDataType;
	
	private ArrayList<String> rangeValue;

	public ArrayList<ClassObject> getDomain() {
		return domain;
	}

	public void setDomain(ArrayList<ClassObject> domain) {
		this.domain = domain;
	}

	public String getRangeDataType() {
		return rangeDataType;
	}

	public void setRangeDataType(String rangeDataType) {
		this.rangeDataType = rangeDataType;
	}

	public ArrayList<String> getRangeValue() {
		return rangeValue;
	}

	public void setRangeValue(ArrayList<String> rangeValue) {
		this.rangeValue = rangeValue;
	}
	
}
