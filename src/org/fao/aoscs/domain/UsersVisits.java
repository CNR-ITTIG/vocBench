package org.fao.aoscs.domain;

import java.util.Date;

import net.sf.gilead.pojo.gwt.LightEntity;

public class UsersVisits extends LightEntity {
	
	private static final long serialVersionUID = 1L;

	private int visitId;

	private String token;

	private String ipAddress;

	private String countryCode;

	private Date logInTime;

	private Date lastVisitTime;

	private Date totalLogInTime;

	private String userId;
	
	private String countryName;
	
	private String userName;

	public UsersVisits() {
		super();
	}

	public UsersVisits(String token, String ipAddress,
			String countryCode, Date logInTime, Date lastVisitTime,
			Date totalLogInTime, String userId) {
		super();
		//this.visitId = visitId;
		this.token = token;
		this.ipAddress = ipAddress;
		this.countryCode = countryCode;
		this.logInTime = logInTime;
		this.lastVisitTime = lastVisitTime;
		this.totalLogInTime = totalLogInTime;
		this.userId = userId;
	}

	public int getVisitId() {
		return visitId;
	}

	public void setVisitId(int visitId) {
		this.visitId = visitId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public Date getLogInTime() {
		return logInTime;
	}

	public void setLogInTime(Date logInTime) {
		this.logInTime = logInTime;
	}

	public Date getLastVisitTime() {
		return lastVisitTime;
	}

	public void setLastVisitTime(Date lastVisitTime) {
		this.lastVisitTime = lastVisitTime;
	}

	public Date getTotalLogInTime() {
		return totalLogInTime;
	}

	public void setTotalLogInTime(Date totalLogInTime) {
		this.totalLogInTime = totalLogInTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	
}
