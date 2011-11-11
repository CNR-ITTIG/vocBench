package org.fao.aoscs.client.module.relationship.service;

import java.util.ArrayList;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.RelationshipObject;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RelationshipServiceAsync<T> {
	public void initData(int group_id, OntologyInfo ontoInfo, AsyncCallback callback);
	public void getDomainRange(String propertyURI, OntologyInfo ontoInfo, AsyncCallback callback);
	public void getDomainRangeDatatype(String propertyURI, OntologyInfo ontoInfo, AsyncCallback callback);
	public void getRelationshipTree(OntologyInfo ontoInfo, AsyncCallback callback);
	public void addNewRelationship(String label , String language , RelationshipObject selectedItem ,RelationshipObject parentOfSelectedItem,RelationshipObject newProperty,String position, OntologyInfo ontoInfo, int userId, int actionId, AsyncCallback callback);
	public void getRelationshipLabels(String relationship, OntologyInfo ontoInfo, AsyncCallback callback);
	public void getRelationshipComments(String relationship, OntologyInfo ontoInfo, AsyncCallback callback);
	public void getRelationshipProperties(String relationship, OntologyInfo ontoInfo, AsyncCallback callback);
	public void getInverseProperty(OntologyInfo ontoInfo,String name, AsyncCallback callback);
	public void AddPropertyLabel(RelationshipObject rObj,String label,String language,int actionId , int userId, OntologyInfo ontoInfo, AsyncCallback callback);
	public void AddPropertyComment(RelationshipObject rObj,String comment,String language, int actionId , int userId , OntologyInfo ontoInfo, AsyncCallback callback);
	public void EditPropertyLabel(RelationshipObject rObj, String oldLabel, String oldLanguage, String newLabel, String newLanguage, int actionId, int userId, OntologyInfo ontoInfo, AsyncCallback callback);
	public void DeletePropertyLabel(RelationshipObject rObj, String oldLabel,String oldLanguage, int actionId, int userId, OntologyInfo ontoInfo, AsyncCallback callback);
	public void EditPropertyComment(RelationshipObject rObj,String oldComment,String oldLanguage,String newComment,String newLanguage, int actionId, int userId, OntologyInfo ontoInfo, AsyncCallback callback);
	public void DeletePropertyComment(RelationshipObject rObj,String oldComment,String oldLanguage, int actionId, int userId, OntologyInfo ontoInfo, AsyncCallback callback);
	public void addProperty(RelationshipObject rObj,String OWLproperties, int actionId, int userId, OntologyInfo ontoInfo, AsyncCallback callback);
	public void deleteProperty(RelationshipObject rObj,String OWLproperties, int actionId, int userId, OntologyInfo ontoInfo, AsyncCallback callback);
	public void addDomain(RelationshipObject rObj, String cls, int actionId , int userId, OntologyInfo ontoInfo, AsyncCallback callback);
	public void deleteDomain(RelationshipObject rObj, String cls, int actionId , int userId, OntologyInfo ontoInfo, AsyncCallback callback);
	public void addRange(RelationshipObject rObj, String cls, int actionId, int userId, OntologyInfo ontoInfo, AsyncCallback callback);
	public void deleteRange(RelationshipObject rObj, String cls, int actionId , int userId, OntologyInfo ontoInfo, AsyncCallback callback);
	public void deleteRelationship(RelationshipObject selectedItem, OntologyInfo ontoInfo, AsyncCallback callback);
	public void getObjectPropertyTree(String rootNode, OntologyInfo ontoInfo, boolean includeSelfRelationship, AsyncCallback callback);	
	public void getClassItemList(String rootName,OntologyInfo ontoInfo, AsyncCallback callback);
	public void setInverseProperty(int actionId, int userId, OntologyInfo ontoInfo,RelationshipObject rObj, String insName, AsyncCallback callback);
	public void deleteInverseProperty(int actionId, int userId, OntologyInfo ontoInfo,RelationshipObject rObj, AsyncCallback callback);
	public void setDataTypeRange(String relationship,String type, OntologyInfo ontoInfo, AsyncCallback callback);
	public void addRangeValues(RelationshipObject rObj, String type, ArrayList<String> values , int actionId, int userId, OntologyInfo ontoInfo, AsyncCallback callback);
}
