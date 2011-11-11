package org.fao.aoscs.domain;

import java.util.ArrayList;

import net.sf.gilead.pojo.gwt.LightEntity;


public class InitializeUsersPreferenceData extends LightEntity{

	private static final long serialVersionUID = 4093734094405098622L;
	
	private UsersPreference usersPreference = new UsersPreference();
	private Users usersInfo = new Users();
	private ArrayList<OntologyInfo> ontology = new ArrayList<OntologyInfo>();
	private ArrayList<LanguageInterface> interfaceLanguage = new ArrayList<LanguageInterface>();
	private ArrayList<UsersGroups> usergroups = new ArrayList<UsersGroups>();
	private ArrayList<UsersLanguage> userLanguage = new ArrayList<UsersLanguage>();
	
	/**
	 * @param usersPreference the usersPreference to set
	 */
	public void setUsersPreference(UsersPreference usersPreference) {
		this.usersPreference = usersPreference;
	}

	/**
	 * @return the usersPreference
	 */
	public UsersPreference getUsersPreference() {
		return usersPreference;
	}



	/**
	 * @return the usersInfo
	 */
	public Users getUsersInfo() {
		return usersInfo;
	}



	/**
	 * @param usersInfo the usersInfo to set
	 */
	public void setUsersInfo(Users usersInfo) {
		this.usersInfo = usersInfo;
	}



	/**
	 * @return the ontology
	 */
	public ArrayList<OntologyInfo> getOntology() {
		return ontology;
	}



	/**
	 * @param ontology the ontology to set
	 */
	public void setOntology(ArrayList<OntologyInfo> ontology) {
		this.ontology = ontology;
	}



	/**
	 * @return the interfaceLanguage
	 */
	public ArrayList<LanguageInterface> getInterfaceLanguage() {
		return interfaceLanguage;
	}



	/**
	 * @param interfaceLanguage the interfaceLanguage to set
	 */
	public void setInterfaceLanguage(ArrayList<LanguageInterface> interfaceLanguage) {
		this.interfaceLanguage = interfaceLanguage;
	}

	/**
	 * @param usergroups the usergroups to set
	 */
	public void setUsergroups(ArrayList<UsersGroups> usergroups) {
		this.usergroups = usergroups;
	}

	/**
	 * @return the usergroups
	 */
	public ArrayList<UsersGroups> getUsergroups() {
		return usergroups;
	}

	/**
	 * @param userLanguage the userLanguage to set
	 */
	public void setUserLanguage(ArrayList<UsersLanguage> userLanguage) {
		this.userLanguage = userLanguage;
	}

	/**
	 * @return the userLanguage
	 */
	public ArrayList<UsersLanguage> getUserLanguage() {
		return userLanguage;
	}
	
	
	
}
