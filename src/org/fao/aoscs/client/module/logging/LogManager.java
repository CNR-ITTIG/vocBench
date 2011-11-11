package org.fao.aoscs.client.module.logging;

import org.fao.aoscs.client.Main;
import org.fao.aoscs.client.Service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class LogManager {
	
	public static String token = "";
	public void startLog(final String user)
	{
        AsyncCallback<Object> callback = new AsyncCallback<Object>() {
		    public void onSuccess(Object sessionresult) {
		    	token = (String) sessionresult;
		    }
		    public void onFailure(Throwable caught) {
		       	//Window.alert(constants.logStartFail());
		    }
		 };
		 Service.loggingService.startLog(token, user, callback); 
	}
	
	public void endLog()
	{
		AsyncCallback<Object> callback = new AsyncCallback<Object>() {
		    public void onSuccess(Object sessionresult) {
		    	token = (String) sessionresult;
		    	logOut();
		    	
		    }
		    public void onFailure(Throwable caught) {
		       //	Window.alert(constants.logEndFail());
		    }
		 };
		 Service.loggingService.endLog(token, callback); 
	}
	
	public void logOut()
	{
		AsyncCallback<Object> callback = new AsyncCallback<Object>() {
			public void onSuccess(Object result){
				Main.signOut();
			}
			public void onFailure(Throwable caught){
				//Window.alert(constants.logSignoutFail());
			}
		};
		Service.systemService.clearSession(callback);
	}
	
	
}
