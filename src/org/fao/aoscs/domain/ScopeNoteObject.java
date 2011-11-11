package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class ScopeNoteObject extends LightEntity {

	private static final long serialVersionUID = 6343377703769990473L;

	private String uri;

	private String label;

	private String lang;
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	
	
}
