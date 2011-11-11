package org.fao.aoscs.client.module.constant;

import java.util.HashMap;

public class ConfigConstants {
	
	public static boolean ISVISITOR;
	public static boolean PERMISSIONCHECK;
	public static boolean PERMISSIONHIDE;
	public static boolean PERMISSIONDISABLE;
	public static boolean SHOWGUESTLOGIN;

	public static String GUESTUSERNAME;
	public static String GUESTPASSWORD;
	public static String VISITORGROUPID;
	public static String VISITORGROUPNAME;
	public static String VERSION;
	public static String DISPLAYVERSION;
	public static String WEBSERVICESINFO;
	public static String VOCBENCHINFO;
	public static String CONTACTUS;
	public static String COPYRIGHTLINK;
	

	public static boolean ISNEWSEARCH;
	public static boolean ISINDEXING;
	public static String INDEXDATASERVERPATH;
	public static int SEARCHTIMEOUT;
	
	public static int CONCEPTNAVIGATIONHISTORYMAXSIZE;
	
		
	public static void loadConstants(HashMap<String, String> cMap)
	{
		ISVISITOR				= cMap.get("ISVISITOR").equalsIgnoreCase("true")? true : false;
		PERMISSIONCHECK  		= cMap.get("PERMISSIONCHECK").equalsIgnoreCase("true")? true : false;
		PERMISSIONHIDE  		= cMap.get("PERMISSIONHIDE").equalsIgnoreCase("true")? true : false;
		PERMISSIONDISABLE  		= cMap.get("PERMISSIONDISABLE").equalsIgnoreCase("true")? true : false;
		SHOWGUESTLOGIN  		= cMap.get("SHOWGUESTLOGIN").equalsIgnoreCase("true")? true : false;
		
		GUESTUSERNAME			= (String)cMap.get("GUESTUSERNAME");
		GUESTPASSWORD			= (String)cMap.get("GUESTPASSWORD");
		VISITORGROUPID			= (String)cMap.get("VISITORGROUPID");
		VISITORGROUPNAME		= (String)cMap.get("VISITORGROUPNAME");
		VERSION					= (String)cMap.get("VERSION");
		DISPLAYVERSION			= (String)cMap.get("DISPLAYVERSION");
		
		WEBSERVICESINFO			= (String)cMap.get("WEBSERVICESINFO");
		VOCBENCHINFO			= (String)cMap.get("VOCBENCHINFO");	
		CONTACTUS				= (String)cMap.get("CONTACTUS");
		COPYRIGHTLINK			= (String)cMap.get("COPYRIGHTLINK");
		
	    ISNEWSEARCH				= Boolean.parseBoolean((String)cMap.get("ISNEWSEARCH"));	
		ISINDEXING				= Boolean.parseBoolean((String)cMap.get("ISINDEXING"));	
		INDEXDATASERVERPATH		= (String)cMap.get("INDEXDATASERVERPATH");
		SEARCHTIMEOUT			= Integer.parseInt(cMap.get("SEARCHTIMEOUT"));
		
		CONCEPTNAVIGATIONHISTORYMAXSIZE			= Integer.parseInt(cMap.get("CONCEPTNAVIGATIONHISTORYMAXSIZE"));
	}
}
