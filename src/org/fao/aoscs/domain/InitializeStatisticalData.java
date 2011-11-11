package org.fao.aoscs.domain;

import java.util.ArrayList;

import net.sf.gilead.pojo.gwt.LightEntity;

public class InitializeStatisticalData extends LightEntity{

	private static final long serialVersionUID = -6113433824886483639L;

	private ArrayList<OwlStatus> statusList = new ArrayList<OwlStatus>();
	private ArrayList<Users> userList = new ArrayList<Users> ();
	private ArrayList<LanguageCode> languageList = new ArrayList<LanguageCode>();
	private ArrayList<RelationshipObject> relationshipList = new ArrayList<RelationshipObject>();

	
	/**
	 * @return the statusList
	 */
	public ArrayList<OwlStatus> getStatusList() {
		return statusList;
	}
	/**
	 * @return the userList
	 */
	public ArrayList<Users> getUserList() {
		return userList;
	}
	/**
	 * @return the languageList
	 */
	public ArrayList<LanguageCode> getLanguageList() {
		return languageList;
	}
	/**
	 * @return the relationshipList
	 */
	public ArrayList<RelationshipObject> getRelationshipList() {
		return relationshipList;
	}
	/**
	 * @param statusList the statusList to set
	 */
	public void setStatusList(ArrayList<OwlStatus> statusList) {
		this.statusList = statusList;
	}
	/**
	 * @param userList the userList to set
	 */
	public void setUserList(ArrayList<Users> userList) {
		this.userList = userList;
	}
	/**
	 * @param languageList the languageList to set
	 */
	public void setLanguageList(ArrayList<LanguageCode> languageList) {
		this.languageList = languageList;
	}
	/**
	 * @param relationshipList the relationshipList to set
	 */
	public void setRelationshipList(ArrayList<RelationshipObject> relationshipList) {
		this.relationshipList = relationshipList;
	}

	
}
