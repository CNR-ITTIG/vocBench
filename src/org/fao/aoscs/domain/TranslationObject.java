package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class TranslationObject extends LightEntity {

	private static final long serialVersionUID = -1162706800617732404L;

	private String uri;

	private String label;
	
	private String description;

	private String lang;
	
	private int type;
	
	/**
	 * Constant for Source/Translation Object :  1 for Image Translation
	 */
	public static final int IMAGETRANSLATION = 1;
	/**
	 * Constant for Source/Translation Object :  2 for Definition Translation
	 */
	public static final int DEFINITIONTRANSLATION = 2;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
			
}
