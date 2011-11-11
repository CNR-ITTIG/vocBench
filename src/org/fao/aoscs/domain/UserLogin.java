package org.fao.aoscs.domain;

import java.util.ArrayList;

import net.sf.gilead.pojo.gwt.LightEntity;


public class UserLogin extends LightEntity {

	private static final long serialVersionUID = -6279380414785448390L;

	private int noOfGroup = 0;
	
	private ArrayList<String> userSelectedLanguage = new ArrayList<String>();
	
	private String  userid;
	
	private String  groupid;
	
	private String  groupname;
	
	private String  loginname;
	
	private String password;
	
	private String  language;
	
	private OntologyInfo  ontology;
	
	private ArrayList<String> menu = new ArrayList<String>();

	private UsersPreference usersPreference = new UsersPreference();
	
	/**
	 * @return the userid
	 */
	public String getUserid() {
		return userid;
	}

	/**
	 * @return the groupid
	 */
	public String getGroupid() {
		return groupid;
	}

	/**
	 * @return the loginname
	 */
	public String getLoginname() {
		return loginname;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @return the ontology
	 */
	public OntologyInfo getOntology() {
		return ontology;
	}

	/**
	 * @return the menu
	 */
	public ArrayList<String> getMenu() {
		return menu;
	}


	/**
	 * @param userid the userid to set
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}

	/**
	 * @param groupid the groupid to set
	 */
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	/**
	 * @param loginname the loginname to set
	 */
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @param ontology the ontology to set
	 */
	public void setOntology(OntologyInfo ontology) {
		this.ontology = ontology;
	}

	/**
	 * @param menu the menu to set
	 */
	public void setMenu(ArrayList<String> menu) {
		this.menu = menu;
	}

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
	 * @return the noOfGroup
	 */
	public int getNoOfGroup() {
		return noOfGroup;
	}

	/**
	 * @param noOfGroup the noOfGroup to set
	 */
	public void setNoOfGroup(int noOfGroup) {
		this.noOfGroup = noOfGroup;
	}

	/**
	 * @param userSelectedLanguage the userSelectedLanguage to set
	 */
	public void setUserSelectedLanguage(ArrayList<String> userSelectedLanguage) {
		this.userSelectedLanguage = userSelectedLanguage;
	}

	/**
	 * @return the userSelectedLanguage
	 */
	public ArrayList<String> getUserSelectedLanguage() {
		return userSelectedLanguage;
	}

	/**
	 * @param groupname the groupname to set
	 */
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	/**
	 * @return the groupname
	 */
	public String getGroupname() {
		return groupname;
	}

	
}
