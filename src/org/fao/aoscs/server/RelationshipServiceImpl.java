package org.fao.aoscs.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;

import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.gwt.GwtConfigurationHelper;
import net.sf.gilead.gwt.PersistentRemoteService;
import net.sf.gilead.pojo.gwt.LightEntity;

import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.client.module.constant.OWLProperties;
import org.fao.aoscs.client.module.relationship.service.RelationshipService;
import org.fao.aoscs.domain.ClassObject;
import org.fao.aoscs.domain.DomainRangeDatatypeObject;
import org.fao.aoscs.domain.InitializeRelationshipData;
import org.fao.aoscs.domain.LabelObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.RecentChangeData;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.RelationshipTreeObject;
import org.fao.aoscs.domain.TranslationObject;
import org.fao.aoscs.hibernate.DatabaseUtil;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.server.protege.ProtegeModelFactory;
import org.fao.aoscs.server.utility.ConceptUtility;

import edu.stanford.smi.protegex.owl.model.OWLDataRange;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.RDFSDatatype;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;
import edu.stanford.smi.protegex.owl.model.RDFSNames;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLDataRange;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLNamedClass;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLUnionClass;
import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFSLiteral;
import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFSNamedClass;

public class RelationshipServiceImpl extends PersistentRemoteService  implements RelationshipService{

	private static final long serialVersionUID = -3336920095465817849L;
	
	//-------------------------------------------------------------------------
	//
	// Initialization of Remote service : must be done before any server call !
	//
	//-------------------------------------------------------------------------
	@Override
	public void init() throws ServletException
	{
		super.init();
		
	//	Bean Manager initialization
		setBeanManager(GwtConfigurationHelper.initGwtStatelessBeanManager( new HibernateUtil(HibernateUtilities.getSessionFactory())));;
		
	}
	
	public InitializeRelationshipData initData(int group_id, OntologyInfo ontoInfo){
		InitializeRelationshipData data = new InitializeRelationshipData();
		data.setRelationshipTree(getRelationshipTree(ontoInfo));
		data.setClassTree(getDomainRangeTree(ontoInfo));
		data.setAllDataType(getRDFSDatatype(ontoInfo));
		data.setActionMap(ConceptUtility.getActionMap(group_id));
		return data;
	}
	
	// recursive
	public void getChildObjectProperty(OWLObjectProperty rootProp, OWLModel owlModel,RelationshipTreeObject rtObj, OWLObjectProperty masterProp)
	{
		RDFProperty subClassOfProperty = owlModel.getRDFProperty(RDFSNames.Slot.SUB_PROPERTY_OF);
		for (Iterator<?> iter = owlModel.getRDFResourcesWithPropertyValue(subClassOfProperty, rootProp).iterator(); iter.hasNext();) 
	    {	
	    	OWLObjectProperty childProp = (OWLObjectProperty) iter.next();	    	
            
            
	    	RelationshipObject rObj = new RelationshipObject();
	    	for (Iterator<?> iterator = childProp.getLabels().iterator(); iterator.hasNext();) 
	    	{
	    		Object obj = (Object) iterator.next();
	    		if (obj instanceof DefaultRDFSLiteral) {
					DefaultRDFSLiteral label = (DefaultRDFSLiteral) obj;
					rObj.addLabel(label.getString(), label.getLanguage());
				}else{
					rObj.addLabel(obj.toString(), "");			
				}
			}
	    		    	
			rObj.setParent(rootProp.getURI());
												
			rObj.setUri(childProp.getURI());
			rObj.setType(RelationshipObject.OBJECT);
			rObj.setRootItem(false);
			rObj.setName(childProp.getName());
			
			rtObj.addRelationshipList(rObj);
			rtObj.addParentChild(rootProp.getURI(), rObj);
			rtObj.addRelationshipDefinition(rObj.getUri() , getRelationshipComments(rObj.getName(), owlModel));
	    	getChildObjectProperty(childProp, owlModel,rtObj, masterProp);
		}
	}
	
	/*public RelationshipTreeObject getObjectPropertyTree(OWLModel owlModel,String rootNode)
	{
		return getObjectPropertyTree(owlModel, rootNode, false);
	}*/
	
	public RelationshipTreeObject getObjectPropertyTree(OWLModel owlModel,String rootNode, boolean includeSelfRelationship)
	{
	    RelationshipTreeObject rtObj = new RelationshipTreeObject();
	    OWLObjectProperty superProp = null;
		RDFProperty subClassOfProperty = owlModel.getRDFProperty(RDFSNames.Slot.SUB_PROPERTY_OF);
		OWLObjectProperty rootProp = owlModel.getOWLObjectProperty(rootNode);
		
		if(includeSelfRelationship)
		{
			for (Iterator<?> iterator = rootProp.getSuperproperties(false).iterator(); iterator.hasNext();) 
			{
				superProp = (OWLObjectProperty) iterator.next();
				
			}
			rtObj = addObjectPropertyTree(owlModel, rtObj, superProp, rootProp);
		}
		for(Iterator<?> iter = owlModel.getRDFResourcesWithPropertyValue(subClassOfProperty, rootProp).iterator(); iter.hasNext();) 
		{
			OWLObjectProperty prop = (OWLObjectProperty) iter.next();
			rtObj = addObjectPropertyTree(owlModel, rtObj, rootProp, prop);
		}	
		
		return rtObj;
	}
	
	public RelationshipTreeObject addObjectPropertyTree(OWLModel owlModel, RelationshipTreeObject rtObj, OWLObjectProperty rootProp, OWLObjectProperty prop)
	{
		
		RelationshipObject rObj = new RelationshipObject();
		// first level
		for (Iterator<?> iterator = prop.getLabels().iterator(); iterator.hasNext();) 
		{
			DefaultRDFSLiteral label = (DefaultRDFSLiteral) iterator.next();
			rObj.addLabel(label.getString(), label.getLanguage());
		}
		
		if(rootProp!=null)
			rObj.setParent(rootProp.getURI());
		
		rObj.setUri(prop.getURI());
		rObj.setType(RelationshipObject.OBJECT);
		rObj.setRootItem(true);
		rObj.setName(prop.getName());
		
		rtObj.addRelationshipList(rObj);
		if(rootProp!=null)
			rtObj.addParentChild(rootProp.getURI(), rObj);
		rtObj.addRelationshipDefinition(rObj.getUri() , getRelationshipComments(rObj.getName(), owlModel));
		// get lower level
		getChildObjectProperty(prop, owlModel,rtObj , prop);
		return rtObj;
	}
	
	public static String getInversePropertyName(OWLObjectProperty prop)
    {   
	    String name = "";
	    RDFProperty ip = prop.getInverseProperty();
	    if(ip != null)
	        name = ip.getName();
	    return name;
    }
	
	
	
	
	public RelationshipTreeObject getObjectPropertyTree(String rootNode, OntologyInfo ontoInfo, boolean includeSelfRelationship){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		RelationshipTreeObject rtObj = getObjectPropertyTree(owlModel, rootNode, includeSelfRelationship);
		///owlModel.dispose();		
	    return rtObj;
	}
		
	//recursive
	public void getChildDataTypeProperty(OWLDatatypeProperty rootProp, OWLModel owlModel,RelationshipTreeObject rtObj){
		
		RDFProperty subClassOfProperty = owlModel.getRDFProperty(RDFSNames.Slot.SUB_PROPERTY_OF);
	    Collection<?> results = owlModel.getRDFResourcesWithPropertyValue(subClassOfProperty, rootProp);
	    
	    for (Iterator<?> iter = results.iterator(); iter.hasNext();) {
	    	OWLDatatypeProperty childProp = (OWLDatatypeProperty) iter.next();
	    	RelationshipObject rObj = new RelationshipObject();
	    	Collection<?> labelList = childProp.getLabels();
	    	
	    	for (Iterator<?> iterator = labelList.iterator(); iterator.hasNext();) {
	    		Object obj = (Object) iterator.next();
	    		if (obj instanceof DefaultRDFSLiteral) {
					DefaultRDFSLiteral label = (DefaultRDFSLiteral) obj;
					rObj.addLabel(label.getString(), label.getLanguage());
				}else{
					//-out.println(obj);
				}
			}
	    	
	    	rObj.setParent(rootProp.getURI());
			rObj.setUri(childProp.getURI());
			rObj.setType(RelationshipObject.DATATYPE);
			rObj.setRootItem(false);
			rObj.setName(childProp.getName());
			
			rtObj.addRelationshipList(rObj);
			rtObj.addParentChild(rootProp.getURI(), rObj);
			rtObj.addRelationshipDefinition(rObj.getUri() , getRelationshipComments(rObj.getName(), owlModel));
			getChildDataTypeProperty(childProp, owlModel,rtObj);
		}
	}
	
	public RelationshipTreeObject getDataTypePropertyTree(String rootNode, OntologyInfo ontoInfo){
        OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
        RelationshipTreeObject rtObj = getDataTypePropertyTree(owlModel, rootNode);
        ///owlModel.dispose();     
        return rtObj;
    }
	
	public RelationshipTreeObject getDataTypePropertyTree(OWLModel owlModel, String rootNode)
	{
		RelationshipTreeObject rtObj = new RelationshipTreeObject();

		RDFProperty subClassOfProperty = owlModel.getRDFProperty(RDFSNames.Slot.SUB_PROPERTY_OF);
	    OWLDatatypeProperty rootProp = owlModel.getOWLDatatypeProperty(rootNode);
	    Collection<?> results = owlModel.getRDFResourcesWithPropertyValue(subClassOfProperty, rootProp);
	    
	    for (Iterator<?> iter = results.iterator(); iter.hasNext();) {
	    	
	    	OWLDatatypeProperty prop = (OWLDatatypeProperty) iter.next();
	    	RelationshipObject rObj = new RelationshipObject();
	    	Collection<?> labelList = prop.getLabels();
	    	for (Iterator<?> iterator = labelList.iterator(); iterator.hasNext();) {
	    		DefaultRDFSLiteral label = (DefaultRDFSLiteral) iterator.next();
	    		rObj.addLabel(label.getString(), label.getLanguage());
			}
	    	
	    	rObj.setParent(rootProp.getURI());
			rObj.setUri(prop.getURI());
			rObj.setType(RelationshipObject.DATATYPE);
			rObj.setRootItem(true);
			rObj.setName(prop.getName());
			
			rtObj.addRelationshipList(rObj);
			rtObj.addParentChild(rootProp.getURI(), rObj);
			rtObj.addRelationshipDefinition(rObj.getUri() , getRelationshipComments(rObj.getName(), owlModel));
			getChildDataTypeProperty(prop, owlModel,rtObj);
		}
		
	    return rtObj;
	}
	
	
	public RelationshipTreeObject[] getRelationshipTree(OntologyInfo ontoInfo){
		
		RelationshipTreeObject[] list = new RelationshipTreeObject[2];
		list[0] = getObjectPropertyTree(ModelConstants.RWBOBJECTPROPERTY, ontoInfo, false);
		list[1] = getDataTypePropertyTree(ModelConstants.RWBDATATYPEPROPERTY, ontoInfo);
		return list;
	}
	
	public DomainRangeDatatypeObject getDomainRangeDatatype(String propertyURI, OntologyInfo ontoInfo){				
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);		
		DomainRangeDatatypeObject data = getRangeValues(owlModel.getOWLDatatypeProperty(propertyURI));
		data.setDomain(getDomain(propertyURI, owlModel));
		///owlModel.dispose();		
		return data;
	}
	
	public HashMap<String, ArrayList<ClassObject>> getDomainRange(String propertyURI, OntologyInfo ontoInfo){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		HashMap<String, ArrayList<ClassObject>> map = new HashMap<String, ArrayList<ClassObject>>();
		map.put("domain", getDomain(propertyURI, owlModel));
		map.put("range", getRange(propertyURI, owlModel));
		///owlModel.dispose();
		return map;
	}
	
	public ArrayList<ClassObject> getDomainRangeTree(OntologyInfo ontoInfo){
		return convert2DomainRangeTreeObject(ontoInfo); 	
	}
	public ArrayList<ClassObject> getClassItemList(String rootName,OntologyInfo ontoInfo){
		ArrayList<ClassObject> list = new ArrayList<ClassObject>();
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(rootName);
		for (Iterator<?> iter = cls.getSubclasses(false).iterator(); iter.hasNext();) {
			Object obj = (Object) iter.next();
			if (obj instanceof OWLNamedClass) {
				OWLNamedClass childCls = (OWLNamedClass) obj;
				if(!childCls.isSystem()){
					ClassObject cObj = new ClassObject();
					cObj.setUri(childCls.getURI());
					cObj.setLabel(childCls.getName());
					cObj.setName(childCls.getName());
					if(childCls.getSubclassCount()>0){
			    		cObj.setHasChild(true);
			    	}else{
			    		cObj.setHasChild(false);
			    	}
					list.add(cObj);
				}
			}
		}
		///owlModel.dispose();
		return list; 	
	}
	private ArrayList<ClassObject> convert2DomainRangeTreeObject(OntologyInfo ontoInfo){
		ArrayList<ClassObject> list = new ArrayList<ClassObject>();
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLThingClass();
		Collection<?> coll = cls.getSubclasses(false);
		for (Iterator<?> iter = coll.iterator(); iter.hasNext();) {
			Object obj = (Object) iter.next();
			if (obj instanceof OWLNamedClass) {
				OWLNamedClass childCls = (OWLNamedClass) obj;
				if(!childCls.isSystem()){
					ClassObject cObj = new ClassObject();
					cObj.setUri(childCls.getURI());
					cObj.setLabel(childCls.getName());
					cObj.setName(childCls.getName());
					if(childCls.getSubclassCount()>0){
			    		cObj.setHasChild(true);
			    	}else{
			    		cObj.setHasChild(false);
			    	}
					list.add(cObj);
				}
			}
		}
		///owlModel.dispose();
		return list; 	
	}
	
	public void deleteRelationship(RelationshipObject selectedItem, OntologyInfo ontoInfo){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		String type = selectedItem.getType();
		if(type.equalsIgnoreCase(RelationshipObject.OBJECT)){
			OWLObjectProperty refProp = owlModel.getOWLObjectProperty(selectedItem.getName());
			refProp.delete();
		}else{
			OWLDatatypeProperty refProp = owlModel.getOWLDatatypeProperty(selectedItem.getName());
			refProp.delete();
		}
		///owlModel.dispose();
	}
	
	public RelationshipObject addNewRelationship(String label , String language , RelationshipObject selectedItem ,RelationshipObject parentOfSelectedItem,RelationshipObject newProperty,String position, OntologyInfo ontoInfo , int userId , int actionId){
		long timeStamp = new java.util.Date().getTime();
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);

		if(position.equals("sameLevel")){
			selectedItem = parentOfSelectedItem;
		}
		
		if(selectedItem.getType().equalsIgnoreCase(RelationshipObject.OBJECT)){
			OWLObjectProperty refProp = owlModel.getOWLObjectProperty(selectedItem.getName());
			OWLObjectProperty newProp = owlModel.createOWLObjectProperty(label.replaceAll(" ", "")+"_"+timeStamp);
			newProp.addLabel(label, language);
			newProp.addSuperproperty(refProp);
			
			newProperty.setUri(newProp.getURI());
			newProperty.setName(newProp.getName());
			newProperty.setParent(selectedItem.getUri());
			newProperty.setParentObject(selectedItem);
		}else{
			OWLDatatypeProperty refProp = owlModel.getOWLDatatypeProperty(selectedItem.getName());
			OWLDatatypeProperty newProp = owlModel.createOWLDatatypeProperty(label.replaceAll(" ", "")+"_"+timeStamp);
			newProp.addLabel(label, language);
			newProp.addSuperproperty(refProp);
			
			newProperty.setUri(newProp.getURI());
			newProperty.setName(newProp.getName());
			newProperty.setParent(selectedItem.getUri());
			newProperty.setParentObject(selectedItem);
		}		

		ArrayList<LightEntity> obj = new ArrayList<LightEntity>();
		obj.add(newProperty);

		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());		
		///owlModel.dispose();
		
		
		return newProperty;
	}
	
	public ArrayList<LabelObject> getRelationshipLabels(String relationship, OntologyInfo ontoInfo){
		ArrayList<LabelObject> list = new ArrayList<LabelObject>();
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLProperty prop = owlModel.getOWLProperty(relationship);
		Collection<?> c = prop.getLabels();
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			Object obj = iter.next();
	    	if (obj instanceof DefaultRDFSLiteral) {
	    		DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
	    		LabelObject label = new LabelObject();
	    		label.setLabel(element.getString());
	    		label.setLanguage(element.getLanguage());
	    		list.add(label);
			}else{
				LabelObject label = new LabelObject();
	    		label.setLabel(obj.toString());
	    		list.add(label);
			}
		}
		///owlModel.dispose();
		return list;
	}
	
	public ArrayList<LabelObject> getRelationshipComments(String relationship, OWLModel owlModel){
		ArrayList<LabelObject> list = new ArrayList<LabelObject>();
		OWLProperty prop = owlModel.getOWLProperty(relationship);
		Collection<?> c = prop.getComments();
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			Object obj = iter.next();
	    	if (obj instanceof DefaultRDFSLiteral) {
	    		DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
	    		LabelObject label = new LabelObject();
	    		label.setLabel(element.getString());
	    		label.setLanguage(element.getLanguage());
	    		list.add(label);
			}else{
				LabelObject label = new LabelObject();
	    		label.setLabel(obj.toString());
	    		list.add(label);
			}
		}
		return list;
	}
	
	public ArrayList<LabelObject> getRelationshipComments(String relationship, OntologyInfo ontoInfo){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		ArrayList<LabelObject> list = getRelationshipComments(relationship, owlModel);
		///owlModel.dispose();
		return list;
	}
	
	public ArrayList<String> getRelationshipProperties(String relationship, OntologyInfo ontoInfo){
		ArrayList<String> list = new ArrayList<String>();
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLProperty prop = owlModel.getOWLProperty(relationship);
	    Collection<?> c = prop.getProtegeTypes();
	    for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
	    	DefaultRDFSNamedClass element = (DefaultRDFSNamedClass)iter.next(); 
	    	if(!element.getName().equals("owl:DatatypeProperty") && !element.getName().equals("owl:ObjectProperty")){
	    		list.add(element.getName());
			}
	    }
	    ///owlModel.dispose();
		return list;
	}
	public HashMap<String, String> getRDFSDatatype(OntologyInfo ontoInfo){
		HashMap<String, String> map = new HashMap<String, String>();
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		Collection<?> c = owlModel.getRDFSDatatypes();
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			RDFSDatatype type = (RDFSDatatype) iter.next();			
			map.put(type.getBrowserText(), type.getName());
		}
		///owlModel.dispose();
		return map;
	}
	
	public ArrayList<ClassObject> getDomain(String relationship, OWLModel owlModel){
		ArrayList<ClassObject> list = new ArrayList<ClassObject>();
		OWLProperty p =  owlModel.getOWLProperty(relationship);
	    Collection<?> c = p.getDomains(true);
	    for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			Object obj = (Object) iter.next();
			if (obj instanceof DefaultOWLUnionClass) {
				DefaultOWLUnionClass element = (DefaultOWLUnionClass) obj;
				Iterator<?> it =  element.listOperands();
				while (it.hasNext()) {
					DefaultOWLNamedClass cls = (DefaultOWLNamedClass) it.next();
					ClassObject cObj = new ClassObject();
					cObj.setType("DOMAIN");
					cObj.setUri(cls.getURI());
					cObj.setName(cls.getName());
					cObj.setLabel(cls.getName());
					list.add(cObj);
				}
			}else if (obj instanceof DefaultOWLNamedClass) {
				DefaultOWLNamedClass element = (DefaultOWLNamedClass) obj;
				ClassObject cObj = new ClassObject();
				cObj.setType("DOMAIN");
				cObj.setUri(element.getURI());
				cObj.setName(element.getName());
				cObj.setLabel(element.getName());
				list.add(cObj);
			}
		}
	    return list; 
	}
	
	public ArrayList<ClassObject> getRange(String relationship, OWLModel owlModel){
		ArrayList<ClassObject> list = new ArrayList<ClassObject>();
		OWLProperty p =  owlModel.getOWLProperty(relationship);
	    Collection<?> c = p.getRanges(true);
	    for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			Object obj = (Object) iter.next();
			if (obj instanceof DefaultOWLUnionClass) {
				DefaultOWLUnionClass element = (DefaultOWLUnionClass) obj;
				Iterator<?> it =  element.listOperands();
				while (it.hasNext()) {
					DefaultOWLNamedClass cls = (DefaultOWLNamedClass) it.next();
					ClassObject cObj = new ClassObject();
					cObj.setType("RANGE");
					cObj.setUri(cls.getURI());
					cObj.setName(cls.getName());
					cObj.setLabel(cls.getName());
					list.add(cObj);
				}
			}else if (obj instanceof DefaultOWLNamedClass) {
				DefaultOWLNamedClass element = (DefaultOWLNamedClass) obj;
				ClassObject cObj = new ClassObject();
				cObj.setType("RANGE");
				cObj.setUri(element.getURI());
				cObj.setName(element.getName());
				cObj.setLabel(element.getName());
				list.add(cObj);
			}
		}
	    return list; 
	}
	public RelationshipObject getInverseProperty(OntologyInfo ontoInfo,String name){
		if(name!=null)
		{
			OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
			OWLObjectProperty prop = owlModel.getOWLObjectProperty(name);
			RDFProperty insProp = prop.getInverseProperty();
			
			if(insProp!=null){
				RelationshipObject rObj = new RelationshipObject();
				rObj.setUri(insProp.getURI());
				rObj.setName(insProp.getName());
				if (insProp instanceof OWLObjectProperty) {
					rObj.setType(RelationshipObject.OBJECT);
				}else if (insProp instanceof OWLDatatypeProperty){
					rObj.setType(RelationshipObject.DATATYPE);
				}
				
				Collection<?> labelList = insProp.getLabels();
		    	for (Iterator<?> iterator = labelList.iterator(); iterator.hasNext();) {
		    		
		    		Object obj = (Object) iterator.next();
		    		if (obj instanceof DefaultRDFSLiteral) {
						DefaultRDFSLiteral label = (DefaultRDFSLiteral) obj;
						rObj.addLabel(label.getString(), label.getLanguage());
					}else{
						rObj.addLabel(obj.toString(), "");
					}
				}
		    	///owlModel.dispose();
				return rObj;
			}else{
				///owlModel.dispose();
				return null;
			}
		}
		else
			return null;
	}
	
	public void setInverseProperty(int actionId , int userId, OntologyInfo ontoInfo,RelationshipObject rObj, String insName){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLObjectProperty prop = owlModel.getOWLObjectProperty(rObj.getName());
		OWLObjectProperty insProp = owlModel.getOWLObjectProperty(insName);
		prop.setInverseProperty(insProp);
		///owlModel.dispose();
		
		ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject tnew = new TranslationObject();
		tnew.setLabel(insName);				
		ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
		neww.add(tnew);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);
		rcData.setNewObject(neww);		
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());			
	}
	
	public void deleteInverseProperty(int actionId, int userId, OntologyInfo ontoInfo, RelationshipObject rObj)
	{
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLObjectProperty prop = owlModel.getOWLObjectProperty(rObj.getName());
		RDFProperty insProp = prop.getInverseProperty();
		prop.removePropertyValue(owlModel.getOWLInverseOfProperty(), insProp);
		///owlModel.dispose();
		
		ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);				
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());	
	}
	
	public void AddPropertyLabel(RelationshipObject rObj,String label,String language,int actionId , int userId, OntologyInfo ontoInfo){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLProperty p =  owlModel.getOWLProperty(rObj.getName());
		p.addLabel(label, language);				
		///owlModel.dispose();
		
		RecentChangeData rcData = new RecentChangeData();
		
		ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject tnew = new TranslationObject();
		tnew.setLabel(label);
		tnew.setLang(language);		
		ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
		neww.add(tnew);
		
		rcData.setObject(obj);
		rcData.setNewObject(neww);		
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());
	}
	
	public void AddPropertyComment(RelationshipObject rObj,String comment,String language, int actionId, int userId, OntologyInfo ontoInfo){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLProperty p =  owlModel.getOWLProperty(rObj.getName());
		RDFSLiteral commentValue = owlModel.createRDFSLiteral(comment, language);
		RDFProperty commentProp = owlModel.getRDFProperty(RDFSNames.Slot.COMMENT);
		p.addPropertyValue(commentProp, commentValue);
		///owlModel.dispose();
		
		ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject tnew = new TranslationObject();
		tnew.setLabel(comment);
		tnew.setLang(language);		
		ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
		neww.add(tnew);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);
		rcData.setNewObject(neww);		
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());		
	}
	
	public void EditPropertyLabel(RelationshipObject rObj,String oldLabel,String oldLanguage,String newLabel,String newLanguage, int actionId, int userId, OntologyInfo ontoInfo){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLProperty p =  owlModel.getOWLProperty(rObj.getName());
		Collection<?> c = p.getLabels();
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			DefaultRDFSLiteral element = (DefaultRDFSLiteral) iter.next();
			if (oldLabel.equals(element.getString()) && oldLanguage.equals(element.getLanguage())) {
				p.removeLabel(oldLabel, oldLanguage);
				p.addLabel(newLabel, newLanguage);
			}
		}
		///owlModel.dispose();
		
		ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject tnew = new TranslationObject();
		tnew.setLabel(newLabel);
		tnew.setLang(newLanguage);		
		ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
		neww.add(tnew);
		
		TranslationObject told = new TranslationObject();
		told.setLabel(oldLabel);
		told.setLang(oldLanguage);		
		ArrayList<LightEntity> old = new ArrayList<LightEntity>();
		old.add(told);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);
		rcData.setNewObject(neww);		
		rcData.setOldObject(old);		
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());	
	}
	public void DeletePropertyLabel(RelationshipObject rObj,String oldLabel,String oldLanguage, int actionId, int userId, OntologyInfo ontoInfo){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLProperty p =  owlModel.getOWLProperty(rObj.getName());
		Collection<?> c = p.getLabels();
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			DefaultRDFSLiteral element = (DefaultRDFSLiteral) iter.next();
			if (oldLabel.equals(element.getString()) && oldLanguage.equals(element.getLanguage())) {
				p.removeLabel(oldLabel, oldLanguage);
			}
		}
		///owlModel.dispose();
		
		ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject told = new TranslationObject();
		told.setLabel(oldLabel);
		told.setLang(oldLanguage);		
		ArrayList<LightEntity> old = new ArrayList<LightEntity>();
		old.add(told);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);			
		rcData.setOldObject(old);		
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());	
	}
	public void EditPropertyComment(RelationshipObject rObj,String oldComment,String oldLanguage,String newComment,String newLanguage, int actionId, int userId, OntologyInfo ontoInfo){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLProperty p =  owlModel.getOWLProperty(rObj.getName());
		RDFSLiteral oldCommentValue = owlModel.createRDFSLiteral(oldComment, oldLanguage);
		RDFSLiteral newCommentValue = owlModel.createRDFSLiteral(newComment, newLanguage);
		RDFProperty commentProp = owlModel.getRDFProperty(RDFSNames.Slot.COMMENT);
		
		Collection<?> c = p.getComments();
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			Object obj = iter.next();
			if (obj instanceof RDFSLiteral) {
				RDFSLiteral element = (RDFSLiteral) obj;
				if (oldComment.equals(element.getString()) && oldLanguage.equals(element.getLanguage())) {
					p.removePropertyValue(commentProp, oldCommentValue);
					p.addPropertyValue(commentProp, newCommentValue);
				}
			}
		}
		///owlModel.dispose();
		
		ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject told = new TranslationObject();
		told.setLabel(oldComment);
		told.setLang(oldLanguage);		
		ArrayList<LightEntity> old = new ArrayList<LightEntity>();
		old.add(told);
		
		TranslationObject tnew = new TranslationObject();
		tnew.setLabel(newComment);
		tnew.setLang(newLanguage);		
		ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
		neww.add(tnew);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);			
		rcData.setOldObject(old);		
		rcData.setNewObject(neww);		
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());	
	}
	
	public void DeletePropertyComment(RelationshipObject rObj, String oldComment,String oldLanguage, int actionId, int userId, OntologyInfo ontoInfo){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLProperty p =  owlModel.getOWLProperty(rObj.getName());
		
		RDFSLiteral oldCommentValue = owlModel.createRDFSLiteral(oldComment, oldLanguage);
		RDFProperty commentProp = owlModel.getRDFProperty(RDFSNames.Slot.COMMENT);
		
		for (Iterator<?> iter = p.getComments().iterator(); iter.hasNext();) {
			Object obj = iter.next();
			if (obj instanceof RDFSLiteral) {
				RDFSLiteral element = (RDFSLiteral) obj;
				if (oldComment.equals(element.getString()) && oldLanguage.equals(element.getLanguage())) {
					p.removePropertyValue(commentProp, oldCommentValue);
				}
			}
		}
		///owlModel.dispose();
		
		ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject told = new TranslationObject();
		told.setLabel(oldComment);
		told.setLang(oldLanguage);		
		ArrayList<LightEntity> old = new ArrayList<LightEntity>();
		old.add(told);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);			
		rcData.setOldObject(old);						
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());
	}
	
	public void addProperty(RelationshipObject rObj,String OWLproperties, int actionId, int userId, OntologyInfo ontoInfo){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		if(OWLproperties.equals(OWLProperties.FUNCTIONAL)){
			OWLProperty p =  owlModel.getOWLProperty(rObj.getName());
			p.setFunctional(true);
		}else if(OWLproperties.equals(OWLProperties.INVERSEFUNCTIONAL)){
			OWLProperty p =  owlModel.getOWLProperty(rObj.getName());
			p.setInverseFunctional(true);
		}else if(OWLproperties.equals(OWLProperties.SYMMETRIC)){
			OWLObjectProperty p = owlModel.getOWLObjectProperty(rObj.getName());
			p.setSymmetric(true);
		}else if(OWLproperties.equals(OWLProperties.TRANSITIVE)){
			OWLObjectProperty p = owlModel.getOWLObjectProperty(rObj.getName());
			p.setTransitive(true);
		}
		///owlModel.dispose();
		
		ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject tnew = new TranslationObject();
		tnew.setLabel(OWLproperties);			
		ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
		neww.add(tnew);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);			
		rcData.setNewObject(neww);						
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());
	}
	
    public void deleteProperty(RelationshipObject rObj, String OWLproperties, int actionId, int userId, OntologyInfo ontoInfo){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);

		if(OWLproperties.equals(OWLProperties.FUNCTIONAL)){
			OWLProperty p =  owlModel.getOWLProperty(rObj.getName());
			p.setFunctional(false);
		}else if(OWLproperties.equals(OWLProperties.INVERSEFUNCTIONAL)){
			OWLProperty p =  owlModel.getOWLProperty(rObj.getName());
			p.setInverseFunctional(false);
		}else if(OWLproperties.equals(OWLProperties.SYMMETRIC)){
			OWLObjectProperty p = owlModel.getOWLObjectProperty(rObj.getName());
			p.setSymmetric(false);
		}else if(OWLproperties.equals(OWLProperties.TRANSITIVE)){
			OWLObjectProperty p = owlModel.getOWLObjectProperty(rObj.getName());
			p.setTransitive(false);
		}
		///owlModel.dispose();
		
		ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject told = new TranslationObject();
		told.setLabel(OWLproperties);			
		ArrayList<LightEntity> old = new ArrayList<LightEntity>();
		old.add(told);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);			
		rcData.setOldObject(old);						
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());
	}
    
    public void addDomain(RelationshipObject rObj, String cls, int actionId, int userId, OntologyInfo ontoInfo){
    	OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
    	OWLProperty p =  owlModel.getOWLProperty(rObj.getName());
    	RDFSClass rdfCls = owlModel.getRDFSNamedClass(cls);
    	p.addUnionDomainClass(rdfCls);
    	///owlModel.dispose();
    	
    	ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject tnew = new TranslationObject();
		tnew.setLabel(cls);			
		ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
		neww.add(tnew);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);			
		rcData.setNewObject(neww);						
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());
    }
    
    public void deleteDomain(RelationshipObject rObj, String cls,  int actionId, int userId, OntologyInfo ontoInfo){
    	OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
    	OWLProperty p =  owlModel.getOWLProperty(rObj.getName());    	
    	RDFSClass rdfCls = owlModel.getRDFSNamedClass(cls);    	
    	p.removeUnionDomainClass(rdfCls);
    	///owlModel.dispose();
    	
    	ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject tnew = new TranslationObject();
		tnew.setLabel(cls);			
		ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
		neww.add(tnew);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);			
		rcData.setOldObject(neww);						
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());
    }
    public void addRange(RelationshipObject rObj, String cls, int actionId, int userId, OntologyInfo ontoInfo){
    	OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
    	OWLObjectProperty p =  owlModel.getOWLObjectProperty(rObj.getName());
    	RDFSClass rdfCls = owlModel.getRDFSNamedClass(cls);
    	p.addUnionRangeClass(rdfCls);
    	///owlModel.dispose();
    	
    	ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject tnew = new TranslationObject();
		tnew.setLabel(cls);			
		ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
		neww.add(tnew);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);			
		rcData.setNewObject(neww);						
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());
    }
    
    public void deleteRange(RelationshipObject rObj, String cls, int actionId, int userId, OntologyInfo ontoInfo){
    	OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
    	OWLObjectProperty p =  owlModel.getOWLObjectProperty(rObj.getName());
    	RDFSClass rdfCls = owlModel.getRDFSNamedClass(cls);
    	p.removeUnionRangeClass(rdfCls);
    	///owlModel.dispose();
    	
    	ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject tnew = new TranslationObject();
		tnew.setLabel(cls);			
		ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
		neww.add(tnew);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);			
		rcData.setOldObject(neww);						
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());
    }
    
    public void setDataTypeRange(String relationship,String type, OntologyInfo ontoInfo){
    	OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
    	OWLDatatypeProperty p  =  owlModel.getOWLDatatypeProperty(relationship);
    	RDFSDatatype dataType = owlModel.getRDFSDatatypeByName(type);
    	p.setRange(dataType);
    	///owlModel.dispose();
    }
    public void addRangeValues(RelationshipObject rObj, String type, ArrayList<String> values , int actionId, int userId, OntologyInfo ontoInfo){    	
    	OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);    	
    	RDFProperty p  =  owlModel.getOWLDatatypeProperty(rObj.getName());
    	RDFSDatatype datatype = owlModel.getXSDstring();
    	
    	if(type.equals("Any"))		datatype = null;
    	if(type.equals("Boolean"))	datatype = owlModel.getXSDboolean();    	    	
    	if(type.equals("Float"))	datatype = owlModel.getXSDfloat();    	    	
    	if(type.equals("Int"))		datatype = owlModel.getXSDint();    	    	
    	if(type.equals("String"))	datatype = owlModel.getXSDstring();
    	
    	setRange(p, datatype);
    	String vals = "";
        if((values != null) && (values.size() > 0))
        {	
        	Iterator<String> ite = values.iterator();
        	int i=0;
        	while(ite.hasNext())
        	{        	
        		String temp = ite.next();
	        	setRangeValue(p , datatype, temp);
	        	vals += i>0? "; " : "";
	        	vals += temp;
	        	i++;	        	
        	}		
        }
        ///owlModel.dispose();
        
        ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject tnew = new TranslationObject();
		tnew.setLabel(type);
		tnew.setDescription(vals);
		ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
		neww.add(tnew);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);			
		rcData.setNewObject(neww);						
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());
    }
    
    // Set range datatype
	public static void setRange(RDFProperty property, RDFSDatatype datatype)
	{
        RDFResource range = property.getRange();
        if (range == null || !range.equals(datatype)) {
            if (range instanceof RDFSDatatype && range.isAnonymous()) {
                range.delete();
            }
            property.setRange(datatype);
        }
	}
	// Set range datatype and values
	public static void setRangeValue(RDFProperty property, RDFSDatatype datatype, String newValue) {
		if (datatype == null) {
			return;
	    }
	    	
	    if (newValue != null) {
            newValue = newValue.trim();
            if (newValue.length() > 0) {
                OWLModel owlModel = property.getOWLModel();
                OWLDataRange newDataRange = null;
                RDFSLiteral newLiteral = owlModel.createRDFSLiteral(newValue, datatype);
                if (property.getRange() instanceof OWLDataRange) {
                    OWLDataRange dataRange = (OWLDataRange) property.getRange();
                    List<?> values = dataRange.getOneOfValueLiterals();
                    RDFSLiteral[] newLiterals = new RDFSLiteral[values.size() + 1];
                    Iterator<?> it = values.iterator();
                    for (int i = 0; it.hasNext(); i++) {
                        RDFSLiteral oldValue = (RDFSLiteral) it.next();
                        if (newLiteral.equals(oldValue)) {
                            return;
                        }
                        newLiterals[i] = oldValue;
                    }
                    newLiterals[newLiterals.length - 1] = newLiteral;
                    newDataRange = owlModel.createOWLDataRange(newLiterals);
                }
                else {
                	newDataRange = owlModel.createOWLDataRange(new RDFSLiteral[]{
                            newLiteral
                    });
                }
                property.setRange(newDataRange);
            }
	    }
	}	
	//Get range values
	public static DomainRangeDatatypeObject getRangeValues(RDFProperty property){
		DomainRangeDatatypeObject data = new DomainRangeDatatypeObject();
		String datatype = null;
		ArrayList<String> list = new ArrayList<String>();		
		Collection<?> c = property.getRanges(true);
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) 
		{
			Object obj = (Object) iter.next();
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
				int i = 0;
				for (Iterator<?> itrr = rdfList.iterator(); itrr.hasNext();) {
					DefaultRDFSLiteral ind = (DefaultRDFSLiteral) itrr.next();					
					if(i==0){													
						datatype = ind.getDatatype().getBrowserText();
					}
					i++;
					list.add(ind.toString());
				}				
			}
		}
		data.setRangeDataType( datatype == null? "any" : datatype);
		data.setRangeValue(list);			
		return data;
	}

}