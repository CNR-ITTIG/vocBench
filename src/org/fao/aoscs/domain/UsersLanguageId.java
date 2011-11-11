package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class UsersLanguageId extends LightEntity {

	private static final long serialVersionUID = -1304596806545579838L;
	
	private int userId;
	private String languageCode;
	private Integer status;

	public UsersLanguageId() {
	}

	public UsersLanguageId(int userId, String languageCode) {
		this.userId = userId;
		this.languageCode = languageCode;
	}

	public UsersLanguageId(int userId, String languageCode, Integer status) {
		this.userId = userId;
		this.languageCode = languageCode;
		this.status = status;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getLanguageCode() {
		return this.languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof UsersLanguageId))
			return false;
		UsersLanguageId castOther = (UsersLanguageId) other;

		return (this.getUserId() == castOther.getUserId())
				&& ((this.getLanguageCode() == castOther.getLanguageCode()) || (this
						.getLanguageCode() != null
						&& castOther.getLanguageCode() != null && this
						.getLanguageCode().equals(castOther.getLanguageCode())))
				&& ((this.getStatus() == castOther.getStatus()) || (this
						.getStatus() != null
						&& castOther.getStatus() != null && this.getStatus()
						.equals(castOther.getStatus())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getUserId();
		result = 37
				* result
				+ (getLanguageCode() == null ? 0 : this.getLanguageCode()
						.hashCode());
		result = 37 * result
				+ (getStatus() == null ? 0 : this.getStatus().hashCode());
		return result;
	}

}
