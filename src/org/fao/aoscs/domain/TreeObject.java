package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class TreeObject extends LightEntity {

	private static final long serialVersionUID = 590936046867486590L;
	
	/*public static final int CONCEPTMODULE = 0;
	public static final int CLASSIFICATIONMODULE = 1;
	public static final int RELATIONSHIPMODULE = 2;
	private int belongsToModule;
	*/
	private String uri;
	private String instance;
	private String name;
	private String status;
	private String nameSpace;
	private String label;
	private boolean hasChild;
	

	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return the hasChild
	 */
	public boolean isHasChild() {
		return hasChild;
	}

	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @param hasChild the hasChild to set
	 */
	public void setHasChild(boolean hasChild) {
		this.hasChild = hasChild;
	}

	/**
	 * @param nameSpace the nameSpace to set
	 */
	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

	/**
	 * @return the nameSpace
	 */
	public String getNameSpace() {
		return nameSpace;
	}

	/**
	 * @param instance the instance to set
	 */
	public void setInstance(String instance) {
		this.instance = instance;
	}

	/**
	 * @return the instance
	 */
	public String getInstance() {
		return instance;
	}

	
	
}
