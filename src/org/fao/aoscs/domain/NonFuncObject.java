package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class NonFuncObject extends LightEntity{

	private static final long serialVersionUID = 8729655341206372475L;
	
	private String value;
	
	private String language;
	
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
