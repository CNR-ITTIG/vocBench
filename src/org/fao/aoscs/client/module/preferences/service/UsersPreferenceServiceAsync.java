package org.fao.aoscs.client.module.preferences.service;

import java.util.ArrayList;

import org.fao.aoscs.domain.Users;
import org.fao.aoscs.domain.UsersOntology;
import org.fao.aoscs.domain.UsersPreference;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UsersPreferenceServiceAsync<T> {
	
	public void getInitData(int userID, AsyncCallback callback);
	public void getUser(int userID, AsyncCallback callback);
	public void updateUsers(Users users, boolean isPasswordChange, AsyncCallback callback);
	public void getUsersPreference(int userID, AsyncCallback callback);
	public void addUsersPreference(UsersPreference UsersPreference, AsyncCallback callback);
	public void updateUsersPreference(UsersPreference UsersPreference, AsyncCallback callback);
	public void getUsersLanguage(int userID, AsyncCallback callback);
	public void updateUsersLanguage(int userID, ArrayList<String> langlist, AsyncCallback callback);
	public void getPendingOntology(int userID, AsyncCallback callback);
	public void getNonAssignedOntology(int userID, AsyncCallback callback);
	public void addUsersOntology(ArrayList<UsersOntology> userOntology, AsyncCallback callback);
}
