package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class TermCodesObject extends LightEntity {
	
	private static final long serialVersionUID = 8259785468107909198L;

	private RelationshipObject repository;

	private String code;

	/**
	 * @return the serialVersionUID
	 */
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	/**
	 * @return the repository
	 */
	public RelationshipObject getRepository() {
		return repository;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param repository the repository to set
	 */
	public void setRepository(RelationshipObject repository) {
		this.repository = repository;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	

}
