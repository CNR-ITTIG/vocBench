package org.fao.aoscs.client.module.term.service;

import java.util.ArrayList;

import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.NonFuncObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.TermObject;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TermServiceAsync<T> {
	
	public void getTermInformation(String cls,String termIns, OntologyInfo ontoInfo,AsyncCallback callback);
	public void getTermRelationship(String cls,String termIns, OntologyInfo ontoInfo,AsyncCallback callback);
	public void getTermNonFunc(String cls,String termIns,String property, OntologyInfo ontoInfo,AsyncCallback callback);
	public void getPropertyValue(String cls, String termIns, ArrayList<String> property, OntologyInfo ontoInfo,AsyncCallback callback);
	
	//public void getTermCode(String cls,String termIns, OntologyInfo ontoInfo,AsyncCallback callback);
	public void getTermCodes(ArrayList<String> terms, OntologyInfo ontoInfo,AsyncCallback callback);
	
	public void addTermRelationship(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,RelationshipObject property,TermObject termObj,TermObject destTermObj,ConceptObject conceptObject,AsyncCallback callback);
	public void editTermRelationship(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,RelationshipObject oldProperty,RelationshipObject newProperty,TermObject termObj,TermObject oldDestTermObj,TermObject newDestTermObj,ConceptObject conceptObject,AsyncCallback callback);
	public void deleteTermRelationship(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,RelationshipObject property,TermObject termObj,TermObject destTermObj,ConceptObject conceptObject,AsyncCallback callback);
	
	/*public void addTermNoneFuncValue(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,String propertyURI,String value,String language,TermObject termObject,ConceptObject conceptObject,AsyncCallback callback);
	public void editTermNoneFuncValue(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,String propertyURI,String oldValue,String oldLanguage,String newValue,String newLanguage,TermObject termObject,ConceptObject conceptObject,AsyncCallback callback);
	public void deleteTermNoneFuncValue(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,String propertyURI,String value,String language,TermObject termObject,ConceptObject conceptObject,AsyncCallback callback);
*/
	public void addPropertyValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId, NonFuncObject value,RelationshipObject rObj,TermObject termObject,ConceptObject conceptObject,AsyncCallback callback);
	public void editPropertyValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId, NonFuncObject oldValue,NonFuncObject newValue,RelationshipObject rObj,TermObject termObject,ConceptObject conceptObject,AsyncCallback callback);
	public void deletePropertyValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId, NonFuncObject oldValue,RelationshipObject rObj,TermObject termObject,ConceptObject conceptObject,AsyncCallback callback);

	public void updatePropertyValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId, NonFuncObject oldValue,NonFuncObject newValue,String rObjName,TermObject termObject,ConceptObject conceptObject,AsyncCallback callback);
	public void getConceptTermObject(String cls, OntologyInfo ontoInfo,AsyncCallback callback);
	
	
	/*public void addTermCode(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,TermCodesObject tcObj,TermObject termObject,ConceptObject conceptObject,AsyncCallback callback);
	public void editTermCode(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,TermCodesObject oldTcObj,TermCodesObject newTcObj,TermObject termObject,ConceptObject conceptObject,AsyncCallback callback);
	public void deleteTermCode(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,TermCodesObject tcObj,TermObject termObject,ConceptObject conceptObject,AsyncCallback callback);*/

}
