package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class ConsistencyObject extends LightEntity {

	private static final long serialVersionUID = -7151832265729517039L;

	private String label;

	private String width;
	
	private String alignment;
	
	private int type;
	
	private int objectType;
	
	public static int Label = 1;
	
	public static int DestLabel = 2;
	
	public static int RelationLabel = 3;

	public static int CDate = 4;
	
	public static int MDate = 5;
	
	public static int Status = 6;
	
	public static int DestStatus = 7;
	
	public static int TermConcept = 8;
	
	public static int TermCode = 9;
	
	public static int TermCodeProperty = 10;
	
	public static int ConceptWithImgOnEachItem = 11;
	
	public static int TermWithImgOnEachItem = 12;
	
	public static int TermBundledInDestConcept = 13;
	
	public static int ConceptAndTerm = 14;
	
	public static int StatusList = 15;
	
	public static int TermBundledInDestConceptIgnoreLangFilter = 16;
	
	public static int CONCEPT = 1;
	
	public static int TERM = 2;
	
	public static int RELATIONSHIP = 3;

	public String getAlignment() {
		return alignment;
	}

	public void setAlignment(String alignment) {
		this.alignment = alignment;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public int getObjectType() {
		return objectType;
	}

	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}
	

	
}
