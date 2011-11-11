package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;


public class LanguageCode extends LightEntity{

	private static final long serialVersionUID = -2943412398251920853L;
	
	private String languageCode;
	private String languageNote;
	private String localLanguage;
	private int languageOrder;

	public String getLanguageCode() {
		return this.languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public String getLanguageNote() {
		return this.languageNote;
	}

	public void setLanguageNote(String languageNote) {
		this.languageNote = languageNote;
	}

	public String getLocalLanguage() {
		return this.localLanguage;
	}

	public void setLocalLanguage(String localLanguage) {
		this.localLanguage = localLanguage;
	}

	public void setLanguageOrder(int languageOrder) {
		this.languageOrder = languageOrder;
	}

	public int getLanguageOrder() {
		return languageOrder;
	}

}
