package org.fao.aoscs.client.module.constant;

import java.util.HashMap;

/**
 * Constants for the OWL Status
 *
 */
public class OWLStatusConstants {

	static HashMap<String, String> OWLStatusConstants = new HashMap<String, String>();
	/**
	 * Constant for OWL Status : proposed (guest)
	 *
	 */
	public static final int PROPOSED_GUEST_ID = 1; 
	public static  String PROPOSED_GUEST = ""; 
	/**
	 * Constant for OWL Status : proposed 
	 *
	 */
	public static final int PROPOSED_ID = 2; 	
	public static  String PROPOSED  = ""; 
	/**
	 * Constant for OWL Status : revised (guest)
	 *
	 */ 
	public static final int REVISED_GUEST_ID = 3; 
	public static  String REVISED_GUEST = ""; 
	/**
	 * Constant for OWL Status : revised
	 *
	 */
	public static final int REVISED_ID = 4; 		
	public static  String REVISED = ""; 
	/**
	 * Constant for OWL Status : proposed deprecated
	 *
	 */
	public static final int PROPOSED_DEPRECATED_ID = 5; 	
	public static  String PROPOSED_DEPRECATED = ""; 
	/**
	 * Constant for OWL Status : deprecated
	 *
	 */
	public static final int DEPRECATED_ID = 6; 		
	public static  String DEPRECATED = ""; 
	/**
	 * Constant for OWL Status : validated
	 *
	 */
	public static final int VALIDATED_ID = 7; 			
	public static  String VALIDATED = ""; 
	/**
	 * Constant for OWL Status : published
	 *
	 */
	public static final int PUBLISHED_ID = 8;  			
	public static  String PUBLISHED = ""; 
	/**
	 * Constant for OWL Status : deleted [Only used in validation module]
	 *
	 */
	public static final int DELETED_ID = 99;				
	public static  String DELETED = ""; 
	
	public static void loadOwlStatusConstants(HashMap<String, String> hMap)
	{
		OWLStatusConstants = hMap;
		PROPOSED_GUEST = (String) OWLStatusConstants.get(""+PROPOSED_GUEST_ID); 
		PROPOSED  = (String) OWLStatusConstants.get(""+PROPOSED_ID);
		REVISED_GUEST = (String) OWLStatusConstants.get(""+REVISED_GUEST_ID);
		REVISED = (String) OWLStatusConstants.get(""+REVISED_ID);
		PROPOSED_DEPRECATED = (String) OWLStatusConstants.get(""+PROPOSED_DEPRECATED_ID);
		DEPRECATED = (String) OWLStatusConstants.get(""+DEPRECATED_ID);		
		VALIDATED = (String) OWLStatusConstants.get(""+VALIDATED_ID);
		PUBLISHED = (String) OWLStatusConstants.get(""+PUBLISHED_ID);	
		DELETED = (String) OWLStatusConstants.get(""+DELETED_ID);

	}
	
	public static int getOWLStatusID(String owlStatus)
	{

		 if(owlStatus.equals(PROPOSED_GUEST))
			 return PROPOSED_GUEST_ID;
		 else if(owlStatus.equals(PROPOSED))   
			 return PROPOSED_ID;			 
		 else if(owlStatus.equals(REVISED_GUEST))   
			 return REVISED_GUEST_ID;			 
		 else if(owlStatus.equals(REVISED))   
			 return REVISED_ID;			 
		 else if(owlStatus.equals(PROPOSED_DEPRECATED))   
			 return PROPOSED_DEPRECATED_ID;			 
		 else if(owlStatus.equals(DEPRECATED))   
			 return DEPRECATED_ID;			 
		 else if(owlStatus.equals(VALIDATED))   
			 return VALIDATED_ID;		 
		 else if(owlStatus.equals(PUBLISHED))   
			 return PUBLISHED_ID;			 
		 else if(owlStatus.equals(DELETED))   
			 return DELETED_ID;
		 else 
			 return 0;

	}
	 
}
