package org.fao.aoscs.client.module.system.service;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

import org.fao.aoscs.domain.InitializeSystemData;
import org.fao.aoscs.domain.InitializeUsersPreferenceData;
import org.fao.aoscs.domain.LanguageCode;
import org.fao.aoscs.domain.LanguageInterface;
import org.fao.aoscs.domain.OwlAction;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.PermissionFunctionalityMap;
import org.fao.aoscs.domain.UserLogin;
import org.fao.aoscs.domain.Users;
import org.fao.aoscs.domain.UsersGroups;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("sys")
public interface SystemService extends RemoteService {

	public InitializeSystemData initData(UserLogin userLoginObj);
	public InitializeUsersPreferenceData initSelectPreferenceData(int userID);
	public UserLogin getAuthorize(String loginuser,String loginpassword);
	public UserLogin getAuthorization(String name, UserLogin  userLoginObj);	
	public boolean setSessionValue(String name, LightEntity obj);
	public ArrayList<Users> getAllUser(); 	
	public boolean isUserExist(String username); 	
	public ArrayList<LanguageInterface> getInterfaceLang();	
	public ArrayList<UsersGroups> getUserGroup(int userid);
	public String getEncriptText(String password);
	public UserLogin checkSession(String name);	
	public HashMap<String,ArrayList<String[]>> getGroupStatusAssignment();
	public HashMap<String,ArrayList<String[]>> getGroupValidateStatusAssignment();
	public void clearSession();
	public void SendMail(String to, String subject,String body);
	public ArrayList<String> getUserSelectedLanguageCode(int userID);
	public ArrayList<String[]> getCountryCodes();
	public ArrayList<LanguageCode> addLanguage(LanguageCode languageCode);
	public ArrayList<LanguageCode> editLanguage(LanguageCode languageCode);
	public ArrayList<LanguageCode> deleteLanguage(LanguageCode languageCode);
	public ArrayList<LanguageCode> updateLanguages(ArrayList<LanguageCode> languageCodes);
	public int registerUser(Users user, ArrayList<String> userGroups, ArrayList<String> userLangs, ArrayList<String> userOntology);
	public int addUser(Users user);
	public void updateUserData(Users user);
	public void createGroup(String name , String description, int userId);
	public void editGroup(int groupId, String name , String description, String oldDescription, int userId);
	public void deleteGroup(int groupId, String name, String description, int userId);
	public void addGroupPermission(int groupId, int permitId, String groupName, String permitName, int userId);
	public void removeGroupPermission(int groupId, int permitId, String groupName, String permitName, int userId);
	public void addGroupsToUser(String userId, ArrayList<String> groupIds);
	public void addLanguagesToUser(String userId, ArrayList<String> languages);
	public void addOntologiesToUser(String userId, ArrayList<String> ontologyIds);
	public void addUserToGroup(int groupId, int userId, String groupName, String userName, int modifierId);
	public void removeUserFromGroup(int groupId, ArrayList<Integer> selectedUsers, String groupName, String userName, int modifierId);
	public void addActionToGroup(ArrayList<PermissionFunctionalityMap> map);
	public void removeActionsFromGroup(int groupId, ArrayList<Integer> selectedActions);
	public void removeActionFromGroup(String groupId, String actionId, String statusId);
	public ArrayList<OwlAction> getUnassignedActions(String groupId);
	public ArrayList<OwlStatus> getUnassignedActionStatus(String groupId, String actionId);
	
	public HashMap<String, String> getHelpURL();
	public HashMap<String, String> loadConfigConstants();
	
	public static class SystemServiceUtil{
		private static SystemServiceAsync instance;
		public static SystemServiceAsync getInstance(){
			if (instance == null) {
				instance = (SystemServiceAsync) GWT.create(SystemService.class);
			}
			return instance;
      }
    }
   }