package org.fao.aoscs.server.utility;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class Date {
	
	/**Get current date based on ROME time*/
	public static String getDate(){
		//set locale and timezone
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"));
		Locale.setDefault(new Locale("en", "US"));  
		// set format
		String DATE_FORMAT = "yyyy-MM-dd";
		java.text.SimpleDateFormat sdf =  new java.text.SimpleDateFormat(DATE_FORMAT);
		//sdf.setTimeZone(TimeZone.getTimeZone("Europe/Rome"));     
		
		return sdf.format(cal.getTime());
	}
	
	/**Get current date & time based on ROME time*/
	public static String getDateTime(){
		//set locale and timezone
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"));
		Locale.setDefault(new Locale("en", "US"));  
		// set format
		String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
		java.text.SimpleDateFormat sdf =  new java.text.SimpleDateFormat(DATE_FORMAT);
		//sdf.setTimeZone(TimeZone.getTimeZone("Europe/Rome"));
		return sdf.format(cal.getTime());
	}
	
	/**Get current date based on ROME time*/
	public static java.util.Date getROMEDate(){
		//set locale and timezone
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"));
		Locale.setDefault(new Locale("en", "US"));  
		//TimeZone.setDefault(TimeZone.getTimeZone("Europe/Rome"));
		return cal.getTime();
		
	}
}
