package org.fao.aoscs.domain;

import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

public class ObjectPerUserStat extends LightEntity {
	
	private static final long serialVersionUID = 1L;
	
	HashMap<Integer , Integer> countProposed = new HashMap<Integer , Integer>();
	HashMap<Integer , Integer> countValidated = new HashMap<Integer , Integer>();
	HashMap<Integer , Integer> countPublished = new HashMap<Integer , Integer>();
	
	public HashMap<Integer, Integer> getCountProposed() {
		return countProposed;
	}
	public void setCountProposed(HashMap<Integer, Integer> countProposed) {
		this.countProposed = countProposed;
	}
	public HashMap<Integer, Integer> getCountValidated() {
		return countValidated;
	}
	public void setCountValidated(HashMap<Integer, Integer> countValidated) {
		this.countValidated = countValidated;
	}
	public HashMap<Integer, Integer> getCountPublished() {
		return countPublished;
	}
	public void setCountPublished(HashMap<Integer, Integer> countPublished) {
		this.countPublished = countPublished;
	}

	
		
}
