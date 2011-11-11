package org.fao.aoscs.client.module.validation.service;

import java.util.List;

import org.fao.aoscs.domain.Validation;
import org.fao.aoscs.domain.ValidationFilter;

import com.google.gwt.gen2.table.client.TableModelHelper.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ValidationServiceAsync<T> {
	public void getInitData(ValidationFilter vFilter, AsyncCallback callback);
	public void getPermission(int groupID, AsyncCallback callback);
	public void getAllUsers(AsyncCallback callback);
	public void getStatus(AsyncCallback callback);
	public void getAction(AsyncCallback callback);
	public void getOtherAction(AsyncCallback callback);
	public void getValidatesize(ValidationFilter vFilter, AsyncCallback callback);
	public void updateValidateQueue(List<Validation> value, ValidationFilter vFilter, AsyncCallback callback);
	public void requestValidationRows(Request request, ValidationFilter vFilter, AsyncCallback callback);
	public void getRecentChangesSize(int ontologyID, AsyncCallback callback);
	public void getRecentChangesData(int ontologyID, AsyncCallback callback);
	public void getRecentChangesInitData(int ontologyID, AsyncCallback callback);
	public void requestRecentChangesRows(Request request, int ontologyID, AsyncCallback callback);

}
