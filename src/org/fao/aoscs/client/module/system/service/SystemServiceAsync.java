package org.fao.aoscs.client.module.system.service;

import java.util.ArrayList;

import net.sf.gilead.pojo.gwt.LightEntity;

import org.fao.aoscs.domain.LanguageCode;
import org.fao.aoscs.domain.PermissionFunctionalityMap;
import org.fao.aoscs.domain.UserLogin;
import org.fao.aoscs.domain.Users;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SystemServiceAsync<T> {
	public void initData(UserLogin userLoginObj, AsyncCallback callback);
	public void initSelectPreferenceData(int userID, AsyncCallback callback);
	public void getAuthorize(String loginuser,String loginpassword, AsyncCallback callback);
	public void getAuthorization(String name, UserLogin  userLoginObj, AsyncCallback callback);
	public void setSessionValue(String name, LightEntity obj, AsyncCallback callback);
	public void getAllUser(AsyncCallback callback);
	public void isUserExist(String username, AsyncCallback callback);
	public void getInterfaceLang(AsyncCallback callback);
	public void getUserGroup(int userid, AsyncCallback callback);
	public void getEncriptText(String password, AsyncCallback callback);
	public void checkSession(String name, AsyncCallback callback);
	public void clearSession(AsyncCallback callback);
	public void SendMail(String to, String subject,String body,AsyncCallback callback);
	public void getGroupStatusAssignment(AsyncCallback callback);
	public void getGroupValidateStatusAssignment(AsyncCallback callback);
	public void getUserSelectedLanguageCode(int userID, AsyncCallback callback);
	public void getCountryCodes(AsyncCallback callback);
	public void addLanguage(LanguageCode languageCode, AsyncCallback callback);
	public void editLanguage(LanguageCode languageCode, AsyncCallback callback);
	public void deleteLanguage(LanguageCode languageCode, AsyncCallback callback);
	public void updateLanguages(ArrayList<LanguageCode> languageCodes, AsyncCallback callback);
	public void registerUser(Users user, ArrayList<String> userGroups, ArrayList<String> userLangs, ArrayList<String> userOntology, AsyncCallback callback);
	public void addUser(Users user, AsyncCallback callback);
	public void updateUserData(Users user, AsyncCallback callback);
	public void createGroup(String name , String description, int userId, AsyncCallback callback);
	public void editGroup(int groupId, String name , String description, String oldDescription, int userId, AsyncCallback callback);
	public void deleteGroup(int groupId, String name, String description, int userId, AsyncCallback callback);
	public void addGroupPermission(int groupId, int permitId, String groupName, String permitName, int userId, AsyncCallback callback);
	public void removeGroupPermission(int groupId, int permitId, String groupName, String permitName, int userId, AsyncCallback callback);
	public void addGroupsToUser(String userId, ArrayList<String> groupIds, AsyncCallback callback);
	public void addLanguagesToUser(String userId, ArrayList<String> languages, AsyncCallback callback);
	public void addOntologiesToUser(String userId, ArrayList<String> ontologyIds, AsyncCallback callback);
	public void addUserToGroup(int groupId, int userId, String groupName, String userName, int modifierId, AsyncCallback callback);
	public void removeUserFromGroup(int groupId, ArrayList<Integer> selectedUsers, String groupName, String userName, int modifierId, AsyncCallback callback);
	//public void addActionToGroup(int groupId, int actionId, String groupName, String action, int modifierId, AsyncCallback callback);
	public void addActionToGroup(ArrayList<PermissionFunctionalityMap> map, AsyncCallback callback);
	public void removeActionsFromGroup(int groupId, ArrayList<Integer> selectedActions, AsyncCallback callback);
	public void removeActionFromGroup(String groupId, String actionId, String statusId, AsyncCallback callback);
	public void getUnassignedActions(String groupId, AsyncCallback callback);
	public void getUnassignedActionStatus(String groupId, String actionId, AsyncCallback callback);
	public void getHelpURL(AsyncCallback callback);
	public void loadConfigConstants(AsyncCallback callback);
	
}