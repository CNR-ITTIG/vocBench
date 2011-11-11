package org.fao.aoscs.client.module.term.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.ConceptTermObject;
import org.fao.aoscs.domain.InformationObject;
import org.fao.aoscs.domain.NonFuncObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.TermObject;
import org.fao.aoscs.domain.TermRelationshipObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("term")
public interface TermService extends RemoteService{
	public InformationObject getTermInformation(String cls,String termIns, OntologyInfo ontoInfo);
	public TermRelationshipObject getTermRelationship(String cls,String termIns, OntologyInfo ontoInfo);
	public ArrayList<NonFuncObject> getTermNonFunc(String cls,String termIns,String property, OntologyInfo ontoInfo);
	public HashMap<RelationshipObject, ArrayList<NonFuncObject>> getPropertyValue(String cls, String termIns, ArrayList<String> property, OntologyInfo ontoInfo);

	//public TermCodeObject getTermCode(String cls,String termIns, OntologyInfo ontoInfo);
	public HashMap<String, String> getTermCodes(ArrayList<String> terms, OntologyInfo ontoInfo);
	
	public void addTermRelationship(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,RelationshipObject property,TermObject termObj,TermObject destTermObj,ConceptObject conceptObject);
	public void editTermRelationship(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,RelationshipObject oldProperty,RelationshipObject newProperty,TermObject termObj,TermObject oldDestTermObj,TermObject newDestTermObj,ConceptObject conceptObject);
	public void deleteTermRelationship(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,RelationshipObject property,TermObject termObj,TermObject destTermObj,ConceptObject conceptObject);
	
/*	public void addTermNoneFuncValue(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,String propertyURI,String value,String language,TermObject termObject,ConceptObject conceptObject);
	public void editTermNoneFuncValue(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,String propertyURI,String oldValue,String oldLanguage,String newValue,String newLanguage,TermObject termObject,ConceptObject conceptObject);
	public void deleteTermNoneFuncValue(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,String propertyURI,String value,String language,TermObject termObject,ConceptObject conceptObject);
*/
	public void addPropertyValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId, NonFuncObject value,RelationshipObject rObj,TermObject termObject,ConceptObject conceptObject);
	public void editPropertyValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId, NonFuncObject oldValue,NonFuncObject newValue,RelationshipObject rObj,TermObject termObject,ConceptObject conceptObject);
	public void deletePropertyValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId, NonFuncObject oldValue,RelationshipObject rObj,TermObject termObject,ConceptObject conceptObject);

	public void updatePropertyValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId, NonFuncObject oldValue,NonFuncObject newValue,String rObjName,TermObject termObject,ConceptObject conceptObject);
	public ConceptTermObject getConceptTermObject(String cls, OntologyInfo ontoInfo);
	
	/*public void addTermCode(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,TermCodesObject tcObj,TermObject termObject,ConceptObject conceptObject);
	public void editTermCode(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,TermCodesObject oldTcObj,TermCodesObject newTcObj,TermObject termObject,ConceptObject conceptObject);
	public void deleteTermCode(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,TermCodesObject tcObj,TermObject termObject,ConceptObject conceptObject);*/
	
	public static class TermServiceUtil{
		private static TermServiceAsync instance;
		public static TermServiceAsync getInstance()
		{
			if (instance == null) {
				instance = (TermServiceAsync) GWT.create(TermService.class);
			}
			return instance;
		}
    }
}

