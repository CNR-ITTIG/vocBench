package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class ClassObject extends LightEntity{
	
	private static final long serialVersionUID = 1245285095550667435L;
	
	private String uri;
	
	private String label;
	
	private String type;
	
	private String name;
	
	private boolean hasChild;
	
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isHasChild() {
		return hasChild;
	}
	public void setHasChild(boolean hasChild) {
		this.hasChild = hasChild;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
