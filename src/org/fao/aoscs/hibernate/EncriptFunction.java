package org.fao.aoscs.hibernate;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class EncriptFunction{
	
	public String encriptFunction(String password){
		
	     byte[] theText = null;
	     MessageDigest md = null;
	     String total="";
 
		try {
			theText = password.getBytes( "8859_1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	     
		try {
			md = MessageDigest.getInstance( "MD5" );
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
	     md.update( theText);
	     
	     byte[] digest = md.digest();

	     List<String> list_result = new ArrayList<String>();
    
	     for ( byte b : digest ){	     	  
	    	  
	    	 String result =Integer.toHexString(b & 0xff );
	    	  
	    	  if(result.length()==1){
	    		  result="0"+result;
	    	  }
	    	  list_result.add(result);
	     }
	     
	    Iterator<String> i = list_result.iterator();

	    while (i.hasNext()) {
        	total=total+i.next();
        }   

	return total.trim();
    } 	
}
