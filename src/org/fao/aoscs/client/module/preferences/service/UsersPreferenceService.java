package org.fao.aoscs.client.module.preferences.service;

import java.util.ArrayList;

import org.fao.aoscs.domain.InitializeUsersPreferenceData;
import org.fao.aoscs.domain.Users;
import org.fao.aoscs.domain.UsersLanguage;
import org.fao.aoscs.domain.UsersOntology;
import org.fao.aoscs.domain.UsersPreference;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("usersPreference")
public interface UsersPreferenceService extends RemoteService{
	
	public InitializeUsersPreferenceData getInitData(int userID);
	public Users getUser(int userID);
	public Users updateUsers(Users users , boolean isPasswordChange);
	public UsersPreference getUsersPreference(int userID);
	public UsersPreference addUsersPreference(UsersPreference UsersPreference);
	public UsersPreference updateUsersPreference(UsersPreference UsersPreference);
	public ArrayList<UsersLanguage> getUsersLanguage(int userID);
	public ArrayList<UsersLanguage> updateUsersLanguage(int userID, ArrayList<String> langlist);
	public ArrayList<String[]> getPendingOntology(int userID);
	public ArrayList<String[]> getNonAssignedOntology(int userID);
	public void addUsersOntology(ArrayList<UsersOntology> userOntology);
	
	public static class UserPreferenceServiceUtil{
		private static UsersPreferenceServiceAsync instance;
		public static UsersPreferenceServiceAsync getInstance()
		{
			if (instance == null) {
				instance = (UsersPreferenceServiceAsync) GWT.create(UsersPreferenceService.class);
			}
			return instance;
		}
    }
}
