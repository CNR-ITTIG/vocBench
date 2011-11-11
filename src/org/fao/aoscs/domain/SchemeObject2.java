package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class SchemeObject2 extends LightEntity{

	private static final long serialVersionUID = -6755125595202254605L;

	private String label ;
	
	private String description;
	
	private String uri;
	
	private String nameSpace;

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

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}
	
}
