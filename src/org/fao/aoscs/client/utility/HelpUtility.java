package org.fao.aoscs.client.utility;

import java.util.HashMap;

import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class HelpUtility {
	
	static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);	
	static HashMap<String, String> helpURL;
	
	/*static {
		if(helpURL==null)
			loadHelp();
	}*/
	
	public static void loadHelp()
	{
		AsyncCallback<HashMap<String, String>> callback = new AsyncCallback<HashMap<String, String>>() {
            public void onSuccess(HashMap<String, String> helpMap)
            {
            	helpURL = helpMap;
            }

            public void onFailure(Throwable caught)
            {
            	Window.alert(constants.helpFail());
            }
        };
        Service.systemService.getHelpURL(callback);
	}
	
	private static void loadHelp(final String key)
	{
		AsyncCallback<HashMap<String, String>> callback = new AsyncCallback<HashMap<String, String>>() {
            public void onSuccess(HashMap<String, String> helpMap)
            {
            	helpURL = helpMap;
            	String url = helpURL.get(key);
    			String path = helpURL.get("PATH");
    			if(path==null || path.equals(""))	path = GWT.getHostPageBaseURL();
    			openURL(path+url);
            }

            public void onFailure(Throwable caught)
            {
            	Window.alert(constants.helpFail());
            }
        };
        Service.systemService.getHelpURL(callback);
	}
	
	public static void openHelp(String token)
	{
		String key = "HELP";

		if(token.equals("Search")){
			key = "SEARCH";
		}
		else if(token.equals("Import")){
			key = "IMPORT";			
		}
		else if(token.equals("Export")){
			key = "EXPORT";			
		}
		else if(token.equals("Validation")){
			key = "VALIDATION";		
		}
		else if(token.equals("Consistency")){
			key = "CONSISTENCY";		
		}
		else if(token.equals("Terms")){
			key = "TERMS";		
		}
		else if(token.equals("Concepts")){
			key = "CONCEPT";			
		}
		else if(token.equals("Classifications")){
			key = "SCHEMES";		
		}
		else if(token.equals("Statistics")){
			key = "STATISTICS";		
		}
		else if(token.equals("Users")){
			key = "USER";		
		}
		else if(token.equals("Groups")){
			key = "GROUP";		
		}
		else if(token.equals("Preferences")){
			key = "PREFERENCES";		
		}
		else if(token.equals("Others_systems")){
			key = "OTHERS";		
		}
		else if(token.equals("Relationships")){
			key = "RELATIONSHIPS";		
		}
		else if(token.equals("Comments")){
			key = "COMMENTS";		
		}
		else if(token.equals("Logs")){
			key = "LOGS";		
		}
		else if(token.equals("Home")){
			key = "ABOUT";		
		}
		else if(token.equals("Login")){
			key = "LOGIN";		
		}
		else
		{
			key = token;
		}
		if(key.equals(""))
			key = "ERROR";
		
		if(helpURL==null)
		{
			loadHelp(key);
		}
		else
		{
			String url = helpURL.get(key);
			String path = helpURL.get("PATH");
			if(path==null || path.equals(""))	path = GWT.getHostPageBaseURL();
			openURL(path+url);
		}
		 
	}
	
	 public static native void openURL(String url) /*-{
     $wnd.open(url,'_help');
 }-*/;
}
