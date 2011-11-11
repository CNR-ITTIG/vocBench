package org.fao.aoscs.client.utility;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

public class TimeConverter {
   private static final int MINUTE = 60;          // 60 seconds
   private static final int HOUR    = 60 * 60;  // 3600 seconds
   //private static final DateTimeFormat sdf = DateTimeFormat.getFormat("dd-MM-yyyy HH:mm:ss");
   private static final DateTimeFormat sdf = DateTimeFormat.getFormat("yyyy-MM-dd");
   public static String ConvertSecsToTime(String timeInSecs)
    {
       double time = Double.parseDouble(timeInSecs);
	   int hrs = (int) (time / HOUR);
       int mn  = (int) ((time - hrs*HOUR) / MINUTE);
       int s   = (int) (time - hrs*HOUR - mn*MINUTE);
       return hrs+":"+mn+":"+s;
    }
   
   public static String ConvertSecsToTime2(String timeInSecs)
   {
      double time = Double.parseDouble(timeInSecs);
	   int hrs = (int) (time / HOUR);
      int mn  = (int) ((time - hrs*HOUR) / MINUTE);
      int s   = (int) (time - hrs*HOUR - mn*MINUTE);
      return hrs+" Hours, "+mn+"Minutes, "+s+" Seconds" ;
   }
   
   
   public static Date ConvertStr2Date(String date){
	   String dateFormat ="yyyy-MM-dd HH:mm:ss";	
	   if(date.length()<11) date += " 00:00:00";
		DateTimeFormat df = DateTimeFormat.getFormat(dateFormat);
	    Date d = df.parse(date);
	    return d;
   }
   
   public static  String formatDate(Date d)
	
	{
		if(d==null)
			return "";
		else
			return sdf.format(d);
	}
   
   public static String formatDate(Date d, String format)
   {
	   DateTimeFormat sdf = DateTimeFormat.getFormat(format);
	   if(d==null)
			return "";
		else
			return sdf.format(d);
   }
}
