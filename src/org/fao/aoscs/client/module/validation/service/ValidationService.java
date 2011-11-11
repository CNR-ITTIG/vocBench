package org.fao.aoscs.client.module.validation.service;

import java.util.ArrayList;
import java.util.List;

import org.fao.aoscs.domain.OwlAction;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.RecentChanges;
import org.fao.aoscs.domain.RecentChangesInitObject;
import org.fao.aoscs.domain.Users;
import org.fao.aoscs.domain.Validation;
import org.fao.aoscs.domain.ValidationFilter;
import org.fao.aoscs.domain.ValidationInitObject;
import org.fao.aoscs.domain.ValidationPermission;

import com.google.gwt.core.client.GWT;
import com.google.gwt.gen2.table.client.TableModelHelper.Request;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("validation")
public interface ValidationService extends RemoteService{

	public ValidationInitObject getInitData(ValidationFilter vFilter);
 
	public ArrayList<ValidationPermission> getPermission(int groupID);

	public ArrayList<Users> getAllUsers();

	public ArrayList<OwlStatus> getStatus();

	public ArrayList<OwlAction> getAction();
	
	public ArrayList<OwlAction> getOtherAction();

	public int getValidatesize(ValidationFilter vFilter);

	public int updateValidateQueue(List<Validation> value, ValidationFilter vFilter);
 
	public ArrayList<Validation> requestValidationRows(Request request, ValidationFilter vFilter);
	
	public RecentChangesInitObject getRecentChangesInitData(int ontologyID);

	public int getRecentChangesSize(int ontologyID);
	
	public ArrayList<RecentChanges> getRecentChangesData(int ontologyID);

	public ArrayList<RecentChanges> requestRecentChangesRows(Request request, int ontologyID);
	
	
	
	public static class ValidationServiceUtil{
		private static ValidationServiceAsync instance;
		public static ValidationServiceAsync getInstance()
		{
			if (instance == null) {
				instance = (ValidationServiceAsync) GWT.create(ValidationService.class);
			}
			return instance;
		}
    }
}