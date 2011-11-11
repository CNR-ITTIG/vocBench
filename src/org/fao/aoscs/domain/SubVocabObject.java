package org.fao.aoscs.domain;

import java.util.ArrayList;

import net.sf.gilead.pojo.gwt.LightEntity;

public class SubVocabObject extends LightEntity{
	
	private static final long serialVersionUID = 8366923358034825128L;
	
	private ArrayList<TreeObject> geoConcept = new ArrayList<TreeObject>();
	private ArrayList<TreeObject> sciConcept = new ArrayList<TreeObject>();
	
	/**
	 * @param geoConcept the geoConcept to set
	 */
	public void setGeoConcept(ArrayList<TreeObject> geoConcept) {
		this.geoConcept = geoConcept;
	}
	/**
	 * @return the geoConcept
	 */
	public ArrayList<TreeObject> getGeoConcept() {
		return geoConcept;
	}
	/**
	 * @param sciConcept the sciConcept to set
	 */
	public void setSciConcept(ArrayList<TreeObject> sciConcept) {
		this.sciConcept = sciConcept;
	}
	/**
	 * @return the sciConcept
	 */
	public ArrayList<TreeObject> getSciConcept() {
		return sciConcept;
	}
		
}
