package org.fao.aoscs.client.locale;

import com.google.gwt.i18n.client.Messages;

public interface LocaleMessages extends Messages {

	//Main
	String mainSignInAs(String username, String groupName);
	
	//LanguageFilter
	String langFilterDeleteAlert(String lang, String langCode);
	String langFilterSameOrderAlert(String order);
	
	//Users
	String userNoRemoveUser(String user);
	String userRemoveUserSuccess(String user);
	String userConfirmRemoveUser(String user);
	
	//Concept	
	String conceptAddNewTerm(String concept);
	String conceptAddNewHint(String concept , String lang);
	String conceptDeleteWarning(String label);
	String conceptRemoveWarning(String label);
	String conceptDeleteTermWarning(String term, String lang);		
	String conceptDefinitionLabelDeleteWarning(String label, String lang);
	String conceptRelationshipDeleteWarning(String label, String conceptLabel);
	String conceptImageTranslationDeleteWarning(String label);
	
	//Relationship
	String relDeleteWarning(String type);
	String relDeleteInvPropertyWarning(String label);
	
	//Term
	String termDeleteCodeWarning(String code);
	String termDeleteValueWarning(String value , String lang);
	String termDeleteRelationshipWarning(String rel , String label1 , String lang1, String label2 , String lang2);
	
	//Scheme
	String schemeNoDescription(String label);
	String schemeNamespaceExists(String label);
	String schemeCategoryDeleteWarning(String label);
	
	// Comment
	String commentPost(String label);
	
}
