package org.fao.aoscs.client.module.classification.service;

import org.fao.aoscs.domain.ClassificationObject;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.SchemeObject;
import org.fao.aoscs.domain.TermObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("classification")
public interface ClassificationService extends RemoteService{
	
	public InitializeConceptData initData(int group_id, OntologyInfo ontoInfo);
	public ConceptObject getSelectedCategory(OntologyInfo ontoInfo, String className, String parentClassName);
	public ClassificationObject getCategoryTree(OntologyInfo ontoInfo);
	
	public String addFirstNewCategory(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,TermObject termObject,ConceptObject concept,SchemeObject schemeObject);
	public String addNewCategory(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,ConceptObject refConcept,String position,TermObject termObject,ConceptObject concept,SchemeObject schemeObject);
	public String makeLinkToFirstConcept(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,String conceptName,SchemeObject schemeObject);
	public String makeLinkToConcept(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,ConceptObject refConcept,String position,String conceptName,SchemeObject schemeObject);
	
	public void deleteCategory(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,ConceptObject conceptObject,SchemeObject schemeObject);
	
	public void addNewScheme(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,SchemeObject sObj);
	public void editScheme(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,SchemeObject oldSObj,SchemeObject newSObj);
	public void deleteScheme(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,SchemeObject oldSObj);
	
	
	public static class ClassificationServiceUtil{
		private static ClassificationServiceAsync instance;
		public static ClassificationServiceAsync getInstance()
		{
			if (instance == null) {
				instance = (ClassificationServiceAsync) GWT.create(ClassificationService.class);
			}
			return instance;
		}
	}
}
