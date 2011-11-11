package org.fao.aoscs.server.utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.hibernate.QueryFactory;
import org.fao.aoscs.server.RelationshipServiceImpl;

import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFSDatatype;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLDataRange;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFSLiteral;

public class ConceptUtility {

	//TODO pmshrestha - Do not use source list from database. Use model data.
	public static ArrayList<String[]> getSource(){
		String sourceQuery = "SELECT source FROM owl_def_source ORDER BY id";
		ArrayList<String[]> linkSource = QueryFactory.getHibernateSQLQuery(sourceQuery);
		return linkSource;
	}
	
	public static ArrayList<RelationshipObject> getAllTermCodeProperties(OWLModel owlModel){
		ArrayList<RelationshipObject> list = new ArrayList<RelationshipObject>();
		OWLDatatypeProperty hasCode = owlModel.getOWLDatatypeProperty(ModelConstants.RHASCODE);
		for (Iterator<?> iter = hasCode.getSubproperties(true).iterator(); iter.hasNext();) {
			DefaultOWLDatatypeProperty prop = (DefaultOWLDatatypeProperty) iter.next();
			RelationshipObject rObj = new RelationshipObject();
			rObj.setUri(prop.getURI());
			rObj.setName(prop.getName());
			rObj.setType(RelationshipObject.DATATYPE);
			for (Iterator<?> iterator = prop.getLabels().iterator(); iterator.hasNext();) {
				Object obj = iterator.next();
    	    	if (obj instanceof DefaultRDFSLiteral) {
    	    		DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
    	    		rObj.addLabel(element.getString(), element.getLanguage());
    			}
			}
			list.add(rObj);
		}
		return list;
	}
	
	public static ArrayList<RelationshipObject> getDatatypeProperties(OWLModel owlModel, String property){
		ArrayList<RelationshipObject> list = new ArrayList<RelationshipObject>();
		OWLDatatypeProperty cdomainProperty = owlModel.getOWLDatatypeProperty(property);
		for (Iterator<?> iter = cdomainProperty.getSubproperties(true).iterator(); iter.hasNext();) {
			DefaultOWLDatatypeProperty prop = (DefaultOWLDatatypeProperty) iter.next();
			RelationshipObject rObj = new RelationshipObject();
			rObj.setUri(prop.getURI());
			rObj.setName(prop.getName());
			rObj.setType(RelationshipObject.DATATYPE);
			rObj.setFunctional(prop.isFunctional());
			rObj.setDomainRangeDatatypeObject(RelationshipServiceImpl.getRangeValues(prop));
			for (Iterator<?> iterator = prop.getLabels().iterator(); iterator.hasNext();) {
				Object obj = iterator.next();
    	    	if (obj instanceof DefaultRDFSLiteral) {
    	    		DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
    	    		rObj.addLabel(element.getString(), element.getLanguage());
    			}
			}
			list.add(rObj);
		}
		return list;
	}
	
	public static HashMap<String, String> getActionMap(int group_id){
		HashMap<String, String> actionMap = new HashMap<String, String>();

		String actionQuery  =  "SELECT  owl_action.action ,owl_action.action_child, owl_status.status , status_action_map.status_id ,owl_action.id " +
							   "FROM status_action_map " +
							   "INNER JOIN  owl_status ON status_action_map.status_id = owl_status.id  JOIN " +
							   			   "owl_action ON owl_action.id = status_action_map.action_id " +
							   "WHERE status_action_map.group_id = '"+group_id+"' " ;
		ArrayList<String[]> actionStatus = QueryFactory.getHibernateSQLQuery(actionQuery);
		for(int i=0;i < actionStatus.size();i++){
			String[] item = (String[]) actionStatus.get(i);
			String[] item2 = new String[2];
			item2[0] = item[2];
			item2[1] = item[3];
			String key = "";
			if(item[1].length()==0){
				key = item[0];
			}else{
				key = item[0]+"-"+item[1];
			}
				actionMap.put(key, item[4]);
		}
		return actionMap;
	}

	public static HashMap<String, OwlStatus> getActionStatusMap(int group_id){
		HashMap<String, OwlStatus> actionStatusMap = new HashMap<String, OwlStatus>();
		String actionQuery  =  "SELECT  owl_action.action ,owl_action.action_child, owl_status.status , status_action_map.status_id ,owl_action.id " +
		   "FROM status_action_map " +
		   "INNER JOIN  owl_status ON status_action_map.status_id = owl_status.id  JOIN " +
		   			   "owl_action ON owl_action.id = status_action_map.action_id " +
		   "WHERE status_action_map.group_id = '"+group_id+"' " ;
		ArrayList<String[]> actionStatus = QueryFactory.getHibernateSQLQuery(actionQuery);
		for(int i=0;i < actionStatus.size();i++){
			String[] item = (String[]) actionStatus.get(i);
			OwlStatus status = new OwlStatus();
			status.setId(Integer.parseInt(item[3]));
			status.setStatus(item[2]);
			//String[] item2 = new String[2];
			//item2[0] = item[2];
			//item2[1] = item[3];
			String key = "";
			if(item[1].length()==0){
				key = item[0];
			}else{
				key = item[0]+"-"+item[1];
			}
			actionStatusMap.put(key, status);
		}
		return actionStatusMap;
	}
	
	public static HashMap<String,String> getTermCodeType(OWLModel owlModel){
		HashMap<String,String> typeList = new HashMap<String,String>();		
		OWLDatatypeProperty hasCode = owlModel.getOWLDatatypeProperty(ModelConstants.RHASCODE);		
		for (Iterator<?> iter = hasCode.getSubproperties(true).iterator(); iter.hasNext();) {
			
			DefaultOWLDatatypeProperty property = (DefaultOWLDatatypeProperty) iter.next();						
			String datatype = null;
					
			Collection<?> c = property.getRanges(true);
			for (Iterator<?> it = c.iterator(); it.hasNext();) 
			{
				Object obj = (Object) it.next();
				// only datatype no values
				if (obj instanceof RDFSDatatype)
				{
					RDFSDatatype type = (RDFSDatatype) obj;				
					datatype = type.getBrowserText();				
				}
				// has values also
				else if (obj instanceof DefaultOWLDataRange)
				{
					DefaultOWLDataRange type = (DefaultOWLDataRange) obj;				
					List<?> rdfList = type.getOneOfValues();
					rdfList = type.getOneOfValueLiterals();
					if( (rdfList != null ) && (rdfList.size() > 0)){
						datatype = ((DefaultRDFSLiteral)rdfList.get(0)).getDatatype().getBrowserText();						
					}								
				}
			}
			typeList.put(property.getURI(), datatype);
		}
		return typeList;
	}
}
