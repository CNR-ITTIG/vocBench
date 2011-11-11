package org.fao.aoscs.client.module.concept.service;


import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.domain.ConceptDetailObject;
import org.fao.aoscs.domain.ConceptMappedObject;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.ConceptTermObject;
import org.fao.aoscs.domain.DefinitionObject;
import org.fao.aoscs.domain.HierarchyObject;
import org.fao.aoscs.domain.IDObject;
import org.fao.aoscs.domain.ImageObject;
import org.fao.aoscs.domain.InformationObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.NonFuncObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.RecentChanges;
import org.fao.aoscs.domain.RecentChangesInitObject;
import org.fao.aoscs.domain.RelationObject;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.TermMoveObject;
import org.fao.aoscs.domain.TermObject;
import org.fao.aoscs.domain.TranslationObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.gen2.table.client.TableModelHelper.Request;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("concept")
public interface ConceptService extends RemoteService{

	public ConceptObject addNewConcept(OntologyInfo ontoInfo ,int actionId,int userId,ConceptObject conceptObject,TermObject termObjNew,String position,ConceptObject refConceptObject);
	public ConceptObject getSelectedConcept(OntologyInfo ontoInfo, String className, String parentClassName);
	public ConceptDetailObject getConceptDetail(OntologyInfo ontoInfo, ArrayList<String> langList, String className, String parentClassName);
	public ConceptDetailObject getCategoryDetail(OntologyInfo ontoInfo, ArrayList<String> langList, String className, String parentClassName);
	public InitializeConceptData initData(int group_id, OntologyInfo ontoInfo, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList);
	public void deleteConcept(OntologyInfo ontoInfo ,int actionId,int userId,OwlStatus status,ConceptObject conceptObject);
	public InformationObject getConceptInformation(String cls, OntologyInfo ontoInfo); 
	public HashMap<RelationshipObject, ArrayList<NonFuncObject>> getPropertyValue(String cls, String property, OntologyInfo ontoInfo);
	public ImageObject getConceptImage(String cls, OntologyInfo ontoInfo);
	public DefinitionObject getConceptDefinition(String cls, OntologyInfo ontoInfo);
	public ConceptTermObject getTerm(String cls, OntologyInfo ontoInfo);
	public RelationObject getConceptRelationship(String cls, OntologyInfo ontoInfo);
	public ConceptMappedObject getMappedConcept(String cls, OntologyInfo ontoInfo);
	public RecentChangesInitObject getConceptHistoryInitData(String uri, int ontologyId , int type);
	public ArrayList<RecentChanges> requestConceptHistoryRows(Request request, int ontologyId, String uri , int type);
	public HierarchyObject getConceptHierarchy(OntologyInfo ontologyInfo, String uri, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList); 
	
	public HashMap<String, String> getNamespaces(OntologyInfo ontoInfo);
	public void addNewNamespace(OntologyInfo ontoInfo, String namespacePrefix, String namespace);
	
	public DefinitionObject addDefinition(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject transObj,IDObject ido,ConceptObject conceptObject);
	public DefinitionObject addDefinitionLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject transObj,IDObject ido,ConceptObject conceptObject);
	public DefinitionObject addDefinitionExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject ido,ConceptObject conceptObject);
	public DefinitionObject editDefinitionLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject oldTransObj,TranslationObject newTransObj,ConceptObject conceptObject);
	public DefinitionObject editDefinitionExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject oldIdo,IDObject newIdo,ConceptObject conceptObject);
	public DefinitionObject deleteDefinitionExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject oldIdo,ConceptObject conceptObject);
	public DefinitionObject deleteDefinition(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject ido,ConceptObject conceptObject);
	public DefinitionObject deleteDefinitionLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject oldTransObj,ConceptObject conceptObject);

	public ImageObject addImage(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject transObj,IDObject ido,ConceptObject conceptObject);
	public ImageObject addImageLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject transObj,IDObject ido,ConceptObject conceptObject);
	public ImageObject addImageExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject ido,ConceptObject conceptObject);
	public ImageObject editImageLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject oldTransObj,TranslationObject newTransObj,ConceptObject conceptObject);
	public ImageObject editImageExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject oldIdo,IDObject newIdo,ConceptObject conceptObject);
	public ImageObject deleteImageExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject oldIdo,ConceptObject conceptObject);
	public ImageObject deleteImage(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject ido,ConceptObject conceptObject);
	public ImageObject deleteImageLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject oldTransObj,ConceptObject conceptObject);
	
/*	public void addNonFuncValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,String property,String value,String language,ConceptObject conceptObject);
	public void editNonFuncValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,String property,String oldValue,String oldLanguage,String newValue,String newLanguage,ConceptObject conceptObject);
	public void deleteNonFuncValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,String property,String oldValue,String oldLanguage,ConceptObject conceptObject);
*/
	public HashMap<RelationshipObject, ArrayList<NonFuncObject>> addPropertyValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId, NonFuncObject value,RelationshipObject rObj,ConceptObject conceptObject, String property);
	public HashMap<RelationshipObject, ArrayList<NonFuncObject>> editPropertyValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId, NonFuncObject oldValue,NonFuncObject newValue,RelationshipObject rObj,ConceptObject conceptObject, String property);
	public HashMap<RelationshipObject, ArrayList<NonFuncObject>> deletePropertyValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId, NonFuncObject oldValue,RelationshipObject rObj,ConceptObject conceptObject, String property);

	public RelationObject addNewRelationship(OntologyInfo ontoInfo ,RelationshipObject rObj,String conceptName,String destConceptName,OwlStatus status,int actionId,int userId);
	public RelationObject editRelationship(OntologyInfo ontoInfo ,RelationshipObject rObj,RelationshipObject newRObj,String conceptName,String destConceptName,String newDestConceptName,OwlStatus status,int actionId,int userId);
	public RelationObject deleteRelationship(OntologyInfo ontoInfo ,RelationshipObject rObj,ConceptObject conceptObject,ConceptObject destConceptObj,OwlStatus status,int actionId,int userId);
	
	public ConceptTermObject addTerm(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TermObject newObject,ConceptObject conceptObject);
	public ConceptTermObject editTerm(OntologyInfo ontoInfo ,int actionId, OwlStatus status,int userId,TermObject oldObject,TermObject newObject,ConceptObject conceptObject);
	public ConceptTermObject deleteTerm(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TermObject oldObject,ConceptObject conceptObject);
	public TermMoveObject loadMoveTerm(OntologyInfo ontoInfo, String termURI, String conceptURI);
	public void moveTerm(OntologyInfo ontoInfo ,int actionId, OwlStatus status,int userId, TermObject termObject, TermMoveObject termMoveObject);
	
	public ConceptMappedObject addMappedConcept(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,String destConceptName,String conceptName);
	public ConceptMappedObject deleteMappedConcept(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,ConceptObject destConceptObj,ConceptObject conceptObject);
	
	public void moveConcept(OntologyInfo ontoInfo, String conceptName, String oldParentConceptName, String newParentConceptName, OwlStatus status, int actionId, int userId);
	public void copyConcept(OntologyInfo ontoInfo, String conceptName, String parentConceptName, OwlStatus status, int actionId, int userId);
	public Integer removeConcept(OntologyInfo ontoInfo, String conceptName, String parentConceptName, OwlStatus status, int actionId, int userId);
	
	public static class ConceptServiceUtil{
		private static ConceptServiceAsync instance;
		public static ConceptServiceAsync getInstance()
		{
			if (instance == null) {
				instance = (ConceptServiceAsync) GWT.create(ConceptService.class);
			}
			return instance;
		}
	}
}
