package org.fao.aoscs.client.module.concept.service;

import java.util.ArrayList;

import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.IDObject;
import org.fao.aoscs.domain.NonFuncObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.TermMoveObject;
import org.fao.aoscs.domain.TermObject;
import org.fao.aoscs.domain.TranslationObject;

import com.google.gwt.gen2.table.client.TableModelHelper.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ConceptServiceAsync<T> {
	
	public void initData(int group_id, OntologyInfo ontoInfo, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList, AsyncCallback callback);
	public void getSelectedConcept(OntologyInfo ontoInfo, String className, String parentClassName,AsyncCallback callback);
	public void getConceptDetail(OntologyInfo ontoInfo, ArrayList<String> langList, String className, String parentClassName,AsyncCallback callback);
	public void getCategoryDetail(OntologyInfo ontoInfo, ArrayList<String> langList, String className, String parentClassName,AsyncCallback callback);
	public void getConceptInformation(String cls, OntologyInfo ontoInfo,AsyncCallback callback);
	public void getPropertyValue(String cls, String property, OntologyInfo ontoInfo, AsyncCallback callback);
	public void getConceptImage(String cls, OntologyInfo ontoInfo,AsyncCallback callback);
	public void getConceptDefinition(String cls, OntologyInfo ontoInfo,AsyncCallback callback);
	public void getTerm(String cls, OntologyInfo ontoInfo,AsyncCallback callback);
	public void getConceptRelationship(String cls, OntologyInfo ontoInfo,AsyncCallback callback);
	public void getMappedConcept(String cls, OntologyInfo ontoInfo,AsyncCallback callback);
	public void getConceptHistoryInitData(String uri, int ontologyId , int type ,AsyncCallback callback);
	public void requestConceptHistoryRows(Request request, int ontologyId, String uri, int type, AsyncCallback callback);
	public void getConceptHierarchy(OntologyInfo ontologyInfo, String uri, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList, AsyncCallback callback);
	
	public void getNamespaces(OntologyInfo ontoInfo, AsyncCallback callback);
	public void addNewNamespace(OntologyInfo ontoInfo, String namespacePrefix, String namespace, AsyncCallback callback);
	
	public void addNewConcept(OntologyInfo ontoInfo ,int actionId,int userId,ConceptObject conceptObject,TermObject termObjNew,String position, ConceptObject refConceptObject, AsyncCallback callback);
	public void deleteConcept(OntologyInfo ontoInfo ,int actionId,int userId,OwlStatus status,ConceptObject conceptObject,AsyncCallback callback);
	
	public void addDefinition(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject transObj,IDObject ido,ConceptObject conceptObject,AsyncCallback callback);
	public void addDefinitionLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject transObj,IDObject ido,ConceptObject conceptObject,AsyncCallback callback);
	public void addDefinitionExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject ido,ConceptObject conceptObject,AsyncCallback callback);
	public void editDefinitionLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject oldTransObj,TranslationObject newTransObj,ConceptObject conceptObject,AsyncCallback callback);
	public void editDefinitionExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject oldIdo,IDObject newIdo,ConceptObject conceptObject,AsyncCallback callback);
	public void deleteDefinitionExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject oldIdo,ConceptObject conceptObject,AsyncCallback callback);
	public void deleteDefinition(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject ido,ConceptObject conceptObject,AsyncCallback callback);
	public void deleteDefinitionLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject oldTransObj,ConceptObject conceptObject,AsyncCallback callback);


	public void addImage(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject transObj,IDObject ido,ConceptObject conceptObject, AsyncCallback callback);
	public void addImageLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject transObj,IDObject ido,ConceptObject conceptObject,AsyncCallback callback);
	public void addImageExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject ido,ConceptObject conceptObject,AsyncCallback callback);
	public void editImageLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject oldTransObj,TranslationObject newTransObj,ConceptObject conceptObject,AsyncCallback callback);
	public void editImageExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject oldIdo,IDObject newIdo,ConceptObject conceptObject,AsyncCallback callback);
	public void deleteImageExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject oldIdo,ConceptObject conceptObject,AsyncCallback callback);
	public void deleteImage(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject ido,ConceptObject conceptObject,AsyncCallback callback);
	public void deleteImageLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject oldTransObj,ConceptObject conceptObject,AsyncCallback callback);
	
	/*public void addNonFuncValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,String property,String value,String language,ConceptObject conceptObject,AsyncCallback callback);
	public void editNonFuncValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,String property,String oldValue,String oldLanguage,String newValue,String newLanguage,ConceptObject conceptObject,AsyncCallback callback);
	public void deleteNonFuncValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,String property,String oldValue,String oldLanguage,ConceptObject conceptObject,AsyncCallback callback);
*/
	public void addPropertyValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId, NonFuncObject value,RelationshipObject rObj,ConceptObject conceptObject, String property,AsyncCallback callback);
	public void editPropertyValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId, NonFuncObject oldValue,NonFuncObject newValue,RelationshipObject rObj,ConceptObject conceptObject, String property,AsyncCallback callback);
	public void deletePropertyValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId, NonFuncObject oldValue,RelationshipObject rObj,ConceptObject conceptObject, String property,AsyncCallback callback);

	public void addNewRelationship(OntologyInfo ontoInfo ,RelationshipObject rObj,String conceptName,String destConceptName,OwlStatus status,int actionId,int userId, AsyncCallback callback);
	public void editRelationship(OntologyInfo ontoInfo ,RelationshipObject rObj,RelationshipObject newRObj,String conceptName,String destConceptName,String newDestConceptName,OwlStatus status,int actionId,int userId,AsyncCallback callback);
	public void deleteRelationship(OntologyInfo ontoInfo ,RelationshipObject rObj,ConceptObject conceptObject,ConceptObject destConceptObj,OwlStatus status,int actionId,int userId,AsyncCallback callback);

	public void addTerm(OntologyInfo ontoInfo ,int actionId, OwlStatus status,int userId,TermObject newObject,ConceptObject conceptObject, AsyncCallback callback);
	public void editTerm(OntologyInfo ontoInfo ,int actionId, OwlStatus status,int userId,TermObject oldObject,TermObject newObject,ConceptObject conceptObject,AsyncCallback callback);
	public void deleteTerm(OntologyInfo ontoInfo ,int actionId, OwlStatus status,int userId,TermObject oldObject,ConceptObject conceptObject,AsyncCallback callback);
	public void loadMoveTerm(OntologyInfo ontoInfo, String termURI, String conceptURI, AsyncCallback callback);
	public void moveTerm(OntologyInfo ontoInfo ,int actionId, OwlStatus status,int userId,TermObject termObject,TermMoveObject termMoveObject, AsyncCallback callback);
	
	
	public void addMappedConcept(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,String destConceptName,String conceptName,AsyncCallback callback);
	public void deleteMappedConcept(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,ConceptObject destConceptObj,ConceptObject conceptObject,AsyncCallback callback);

	public void moveConcept(OntologyInfo ontoInfo, String conceptName, String oldParentConceptName, String newParentConceptName, OwlStatus status, int actionId, int userId, AsyncCallback callback);
	public void copyConcept(OntologyInfo ontoInfo, String conceptName, String parentConceptName, OwlStatus status, int actionId, int userId, AsyncCallback callback);
	public void removeConcept(OntologyInfo ontoInfo, String conceptName, String parentConceptName, OwlStatus status, int actionId, int userId, AsyncCallback callback);
}
