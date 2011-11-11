package org.fao.aoscs.client.module.logging.service;

import java.util.ArrayList;
import java.util.List;

import org.fao.aoscs.domain.UsersVisits;

import com.google.gwt.core.client.GWT;
import com.google.gwt.gen2.table.client.TableModelHelper.Request;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("logging")
public interface LoggingService extends RemoteService{
	public String startLog(String token, String ID);
	public void endLog(String token);
	public List<String[]> viewLog();
	public List<String> getLogInfo();
	public ArrayList<UsersVisits> requestUsersVisitsRows(Request request);
		
	public static class LoggingServiceUtil{
		private static LoggingServiceAsync instance;
		public static LoggingServiceAsync getInstance()
		{
			if (instance == null) {
				instance = (LoggingServiceAsync) GWT.create(LoggingService.class);
			}
			return instance;
		}
    }
}
