package org.fao.aoscs.client.module.logging.service;

import com.google.gwt.gen2.table.client.TableModelHelper.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoggingServiceAsync<T> {
	public void startLog(String token, String ID, AsyncCallback callback);
	public void endLog(String token, AsyncCallback callback);
	public void viewLog(AsyncCallback callback);
	public void getLogInfo(AsyncCallback callback);
	public void requestUsersVisitsRows(Request request, AsyncCallback callback);
}
