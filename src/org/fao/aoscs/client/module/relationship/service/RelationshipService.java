package org.fao.aoscs.client.module.relationship.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.domain.ClassObject;
import org.fao.aoscs.domain.DomainRangeDatatypeObject;
import org.fao.aoscs.domain.InitializeRelationshipData;
import org.fao.aoscs.domain.LabelObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.RelationshipTreeObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("relationships")
public interface RelationshipService extends RemoteService{
	public InitializeRelationshipData initData(int group_id, OntologyInfo ontoInfo);
	public HashMap<String,java.util.ArrayList<ClassObject>> getDomainRange(String propertyURI, OntologyInfo ontoInfo);
	public DomainRangeDatatypeObject getDomainRangeDatatype(String propertyURI, OntologyInfo ontoInfo);
	public RelationshipTreeObject[] getRelationshipTree(OntologyInfo ontoInfo);
	public RelationshipObject addNewRelationship(String label , String language , RelationshipObject selectedItem ,RelationshipObject parentOfSelectedItem,RelationshipObject newProperty,String position, OntologyInfo ontoInfo, int userId, int actionId);
	public ArrayList<LabelObject> getRelationshipLabels(String relationship, OntologyInfo ontoInfo);
	public ArrayList<LabelObject> getRelationshipComments(String relationship, OntologyInfo ontoInfo);
	public ArrayList<String>  getRelationshipProperties(String relationship, OntologyInfo ontoInfo);
	public RelationshipObject getInverseProperty(OntologyInfo ontoInfo,String name);
	public void AddPropertyLabel(RelationshipObject rObj, String label,String language,int actionId, int userId, OntologyInfo ontoInfo);
	public void AddPropertyComment(RelationshipObject rObj, String comment,String language, int actionId, int userId, OntologyInfo ontoInfo);
	public void EditPropertyLabel(RelationshipObject rObj, String oldLabel,String oldLanguage,String newLabel,String newLanguage, int actionId, int userId, OntologyInfo ontoInfo);
	public void DeletePropertyLabel(RelationshipObject rObj, String oldLabel,String oldLanguage, int actionId, int userId, OntologyInfo ontoInfo);
	public void EditPropertyComment(RelationshipObject rObj, String oldComment,String oldLanguage,String newComment,String newLanguage, int actionId, int userId, OntologyInfo ontoInfo);
	public void DeletePropertyComment(RelationshipObject rObj, String oldComment,String oldLanguage, int actionId, int userId, OntologyInfo ontoInfo);
	public void addProperty(RelationshipObject rObj, String OWLproperties, int actionId, int userId, OntologyInfo ontoInfo);
	public void deleteProperty(RelationshipObject rObj, String OWLproperties, int actionId, int userId, OntologyInfo ontoInfo);
	public void addDomain(RelationshipObject rObj, String cls, int actionId, int userId, OntologyInfo ontoInfo);
	public void deleteDomain(RelationshipObject rObj, String cls, int actionId, int userId, OntologyInfo ontoInfo);
	public void addRange(RelationshipObject rObj, String cls, int actionId, int userId, OntologyInfo ontoInfo);
	public void deleteRange(RelationshipObject rObj, String cls, int actionId, int userId, OntologyInfo ontoInfo);
	public void deleteRelationship(RelationshipObject selectedItem, OntologyInfo ontoInfo);
	public RelationshipTreeObject getObjectPropertyTree(String rootNode, OntologyInfo ontoInfo, boolean includeSelfRelationship);
	public ArrayList<ClassObject> getClassItemList(String rootName,OntologyInfo ontoInfo);
	public void setInverseProperty(int actionId, int userId, OntologyInfo ontoInfo,RelationshipObject rObj, String insName);
	public void deleteInverseProperty(int actionId, int userId, OntologyInfo ontoInfo,RelationshipObject rObj);
	public void setDataTypeRange(String relationship, String type, OntologyInfo ontoInfo);
	public void addRangeValues(RelationshipObject rObj, String type, ArrayList<String> values , int actionId, int userId, OntologyInfo ontoInfo);
	
	public static class RelationshipServiceUtil{
		private static RelationshipServiceAsync instance;
		public static RelationshipServiceAsync getInstance()
		{
			if (instance == null) {
				instance = (RelationshipServiceAsync) GWT.create(RelationshipService.class);
			}
			return instance;
		}
    }
}
