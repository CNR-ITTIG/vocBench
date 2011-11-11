package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class LabelObject extends LightEntity{

	private static final long serialVersionUID = -1376408137342151776L;
	
	private String label;
	
	private String language;
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
}
