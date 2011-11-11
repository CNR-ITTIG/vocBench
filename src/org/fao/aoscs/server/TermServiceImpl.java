package org.fao.aoscs.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;

import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.gwt.GwtConfigurationHelper;
import net.sf.gilead.gwt.PersistentRemoteService;

import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.client.module.term.service.TermService;
import org.fao.aoscs.domain.AttributesObject;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.ConceptTermObject;
import org.fao.aoscs.domain.InformationObject;
import org.fao.aoscs.domain.NonFuncObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.TermObject;
import org.fao.aoscs.domain.TermRelationshipObject;
import org.fao.aoscs.domain.Validation;
import org.fao.aoscs.hibernate.DatabaseUtil;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.server.protege.ProtegeModelFactory;
import org.fao.aoscs.server.protege.ProtegeWorkbenchUtility;
import org.fao.aoscs.server.utility.Date;

import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLIndividual;
import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFSLiteral;

public class TermServiceImpl extends PersistentRemoteService  implements TermService{
	
	private static final long serialVersionUID = 5566419720164329975L;
	
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
	
	private void setInstanceUpdateDate(OWLModel owlModel,OWLIndividual ins){
		OWLProperty updateDate = owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE);
		ins.setPropertyValue(updateDate, Date.getDateTime());
	}
	public InformationObject getTermInformation(String cls,String termIns, OntologyInfo ontoInfo){
		InformationObject infoObj = new InformationObject();
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass owlCls = owlModel.getOWLNamedClass(cls);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlCls);
		Collection<?> lexicon = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION));
		for (Iterator<?> iter = lexicon.iterator(); iter.hasNext();) {
			OWLIndividual termInstance = (OWLIndividual) iter.next();
			if(termInstance.getName().equalsIgnoreCase(termIns)){
				Object createDate = termInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED));
				Object updateDate = termInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE));
				Object status = termInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASSTATUS));
				if(createDate != null){
					infoObj.setCreateDate(createDate.toString());
				}
				if(updateDate!=null){
					infoObj.setUpdateDate(updateDate.toString());
				}
				if(status!=null){
					infoObj.setStatus(status.toString());
				}								
			}
		}
		///owlModel.dispose();
		return infoObj;
	}
	public ConceptTermObject getConceptTermObject(String cls, OntologyInfo ontoInfo){
		ConceptTermObject ctObj = new ConceptTermObject();
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass owlCls = owlModel.getOWLNamedClass(cls);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlCls);
		Collection<?> lexicon = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION));
		for (Iterator<?> iter = lexicon.iterator(); iter.hasNext();) {
			OWLIndividual termInstance = (OWLIndividual) iter.next();
			TermObject termObject = ProtegeWorkbenchUtility.makeTermObject(owlModel, termInstance, owlCls);
			ctObj.addTermList(termObject.getLang(), termObject);
		}
		///owlModel.dispose();
		return ctObj;
	}

	public Collection<OWLObjectProperty> getAllObjectPropperties(OWLModel owlModel,OWLObjectProperty rootProp){
		Collection<OWLObjectProperty> result = new ArrayList<OWLObjectProperty>();
		Collection<?> propList = rootProp.getSubproperties(true);
		for (Iterator<?> iter = propList.iterator(); iter.hasNext();) {
			OWLObjectProperty prop = (OWLObjectProperty) iter.next();
			result.add(prop);
		}
		return result;
	}
	
	public TermRelationshipObject getTermRelationship(String cls,String termIns, OntologyInfo ontoInfo){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		return getTermRelationship(owlModel, cls, termIns);
	}
	
	public TermRelationshipObject getTermRelationship(OWLModel owlModel, String cls, String termIns){
		TermRelationshipObject trObj = new TermRelationshipObject();
		OWLNamedClass owlCls = owlModel.getOWLNamedClass(cls);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlCls);
		OWLObjectProperty hasRelatedTerm = owlModel.getOWLObjectProperty(ModelConstants.RHASRELATEDTERM);
		
		Collection<OWLObjectProperty> propList = getAllObjectPropperties(owlModel, hasRelatedTerm);
		propList.add(hasRelatedTerm);
		
		Collection<?> lexicon = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION));
		for (Iterator<?> iter = lexicon.iterator(); iter.hasNext();) 
		{
			OWLIndividual termInstance = (OWLIndividual) iter.next();
			if(termInstance.getName().equalsIgnoreCase(termIns))
			{
				for (Iterator<?> iter2 = propList.iterator(); iter2.hasNext();) 
				{
					ArrayList<TermObject> termList = new ArrayList<TermObject>();
					
					OWLObjectProperty prop = (OWLObjectProperty) iter2.next();
					
					RelationshipObject rObj = new RelationshipObject();
					rObj.setName(prop.getName());
					rObj.setUri(prop.getURI());
					rObj.setType(RelationshipObject.OBJECT);
					
					Collection<?> labelList = prop.getLabels();
					
					for (Iterator<?> iterator = labelList.iterator(); iterator.hasNext();) 
					{
						Object obj = iterator.next();
		    	    	if (obj instanceof DefaultRDFSLiteral) 
		    	    	{
		    	    		DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
		    	    		rObj.addLabel(element.getString(), element.getLanguage());
		    			}
					}
					
					Collection<?>  coll = termInstance.getPropertyValues(prop);
					
					for (Iterator<?> iterator = coll.iterator(); iterator.hasNext();) 
					{
						Object insObj = iterator.next();
						if (insObj instanceof DefaultOWLIndividual) 
						{
							DefaultOWLIndividual tIns = (DefaultOWLIndividual) insObj;
							TermObject tObj = ProtegeWorkbenchUtility.makeTermObject(owlModel, tIns, owlCls);
							termList.add(tObj);
						}
		    		}

					
					if(!termList.isEmpty()){
						trObj.addResult(rObj, termList);
					}
					
				}
			}
		}
		///owlModel.dispose();	
		return trObj;
	}
	
	public HashMap<RelationshipObject, ArrayList<NonFuncObject>> getPropertyValue(String cls, String termIns, ArrayList<String> prop, OntologyInfo ontoInfo){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		HashMap<RelationshipObject, ArrayList<NonFuncObject>> list = new HashMap<RelationshipObject, ArrayList<NonFuncObject>>();
		for(String property: prop)
		{
			list.putAll(getPropertyValue(owlModel, property, cls, termIns));
		}
		///owlModel.dispose();
		return list;
	}
	
	private HashMap<RelationshipObject, ArrayList<NonFuncObject>> getPropertyValue(OWLModel owlModel, String property, String cls, String termURI){
		HashMap<RelationshipObject, ArrayList<NonFuncObject>> list = new HashMap<RelationshipObject, ArrayList<NonFuncObject>>();
	    OWLNamedClass owlCls = owlModel.getOWLNamedClass(cls);
	    OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlCls);
		OWLIndividual termInstance = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, individual, owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION), termURI);
		OWLProperty nProp = owlModel.getOWLProperty(property);
	    
		if(termInstance!=null)
		{
	    
		    for (Iterator<?> itr = nProp.getSubproperties(true).iterator(); itr.hasNext();) {
		    	OWLProperty prop = (OWLProperty) itr.next();
		    	RelationshipObject rObj = new RelationshipObject();
		    	rObj.setUri(prop.getURI());
				rObj.setName(prop.getName());
				rObj.setType(RelationshipObject.DATATYPE);
				rObj.setFunctional(prop.isFunctional());
				rObj.setDomainRangeDatatypeObject(RelationshipServiceImpl.getRangeValues(prop));
				Collection<?> labelList = prop.getLabels();
				for (Iterator<?> iterator = labelList.iterator(); iterator.hasNext();) 
				{
					Object obj = iterator.next();
					if (obj instanceof DefaultRDFSLiteral) {
						DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
						rObj.addLabel(element.getString(), element.getLanguage());
					}
				}
				ArrayList<NonFuncObject> values = new ArrayList<NonFuncObject>();
			    for (Iterator<?> iter = termInstance.getPropertyValues(prop).iterator(); iter.hasNext();) 
			    {
			        
			    	Object obj = iter.next();
			    	NonFuncObject nonFuncObj = new NonFuncObject();
	
			        if (obj instanceof DefaultRDFSLiteral) {
						DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
				        nonFuncObj.setLanguage(element.getLanguage());
				        nonFuncObj.setValue(element.getString());
					}
			        else
			        {
				        nonFuncObj.setValue(""+obj);
			        }
			        values.add(nonFuncObj);
			    }
			    if(values.size()>0)
			    	list.put(rObj, values);
		    }
		}
	    return list;
	}
	
	
	public ArrayList<NonFuncObject> getTermNonFunc(String cls,String termIns,String property, OntologyInfo ontoInfo){
		ArrayList<NonFuncObject> list = new ArrayList<NonFuncObject>();
		
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass owlCls = owlModel.getOWLNamedClass(cls);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlCls);
		OWLProperty hasStemmedForm = owlModel.getOWLProperty(property);
		
		Collection<?> lexicon = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION));
		for (Iterator<?> iter = lexicon.iterator(); iter.hasNext();) {
			OWLIndividual termInstance = (OWLIndividual) iter.next();
			if(termInstance.getName().equalsIgnoreCase(termIns)){
				Collection<?>  coll = termInstance.getPropertyValues(hasStemmedForm);
				for (Iterator<?> iter2 = coll.iterator(); iter2.hasNext();) {
					RDFSLiteral element = (RDFSLiteral) iter2.next();
					NonFuncObject nObj = new NonFuncObject();
					nObj.setValue(element.getString());
					nObj.setLanguage(element.getLanguage());
					list.add(nObj);
				}
			}
		}
		///owlModel.dispose();
		return list;
	}
	
	public String getTermCodeValue(OWLIndividual termIns,OWLProperty prop){
		String code = null;
		Object obj = termIns.getPropertyValue(prop);
		if (obj instanceof Integer) {
			code = obj.toString();
		}
		else if(obj instanceof String) {
			code = (String) obj;
		}
		return code;
	}
	
	/*public TermCodeObject getTermCode(String cls,String termIns, OntologyInfo ontoInfo){
		TermCodeObject tcObj = new TermCodeObject();
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass owlCls = owlModel.getOWLNamedClass(cls);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlCls);
		OWLDatatypeProperty hasCode = owlModel.getOWLDatatypeProperty(ModelConstants.RHASCODE);
		
		Collection<?> propList = hasCode.getSubproperties(true);
		OWLProperty lexicon = owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION);
		OWLIndividual termInstance = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, individual, lexicon, termIns);
		
		for (Iterator<?> iter2 = propList.iterator(); iter2.hasNext();) {
			DefaultOWLDatatypeProperty prop = (DefaultOWLDatatypeProperty) iter2.next();

			RelationshipObject rObj = new RelationshipObject();
			rObj.setName(prop.getName());
			rObj.setUri(prop.getURI());
			rObj.setType(RelationshipObject.DATATYPE);
			Collection<?> labelList = prop.getLabels();
			for (Iterator<?> iterator = labelList.iterator(); iterator
					.hasNext();) {
				Object obj = iterator.next();
    	    	if (obj instanceof DefaultRDFSLiteral) {
    	    		DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
    	    		rObj.addLabel(element.getString(), element.getLanguage());
    			}
			}
			String code = getTermCodeValue(termInstance, prop);
			if(code!=null){
				AttributeObject tc = new TermCodesObject();
				tc.setCode(code);
				tc.setRepository(rObj);
				tcObj.addResult(rObj, tc);
			}
		}
		///owlModel.dispose();	
		return tcObj;
	}*/
 
	public void addTermRelationship(OntologyInfo ontoInfo, int actionId,OwlStatus status,int userId,RelationshipObject property,TermObject termObj,TermObject destTermObj,ConceptObject conceptObject){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty lexicon = owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION);
		OWLProperty relatedTerm = owlModel.getOWLProperty(property.getName());
		OWLIndividual termIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, ins, lexicon, termObj.getUri());
		OWLIndividual destTermIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, ins, lexicon, destTermObj.getUri());
		termIns.addPropertyValue(relatedTerm, destTermIns);
		setInstanceUpdateDate(owlModel, termIns);
		setInstanceUpdateDate(owlModel, ins);
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setTerm(DatabaseUtil.setObject(termObj));
		v.setTermObject(termObj);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setNewRelationship(DatabaseUtil.setObject(property));
		v.setNewValue(DatabaseUtil.setObject(destTermObj));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(Date.getROMEDate());
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
	}

	public void editTermRelationship(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,RelationshipObject oldProperty,RelationshipObject newProperty,TermObject termObj,TermObject oldDestTermObj,TermObject newDestTermObj,ConceptObject conceptObject){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty lexicon = owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION);
		OWLProperty oldRelatedTerm = owlModel.getOWLProperty(oldProperty.getName());
		OWLProperty newRelatedTerm = owlModel.getOWLProperty(newProperty.getName());
		OWLIndividual termIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, ins, lexicon, termObj.getUri());
		OWLIndividual oldDestTermIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, ins, lexicon, oldDestTermObj.getUri());
		OWLIndividual newDestTermIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, ins, lexicon, newDestTermObj.getUri());
		
		termIns.removePropertyValue(oldRelatedTerm, oldDestTermIns);
		termIns.addPropertyValue(newRelatedTerm, newDestTermIns);
		setInstanceUpdateDate(owlModel, termIns);
		setInstanceUpdateDate(owlModel, ins);
		
	    Validation v = new Validation();
	    v.setConcept(DatabaseUtil.setObject(conceptObject));
	    v.setTerm(DatabaseUtil.setObject(termObj));
	    v.setTermObject(termObj);
	    v.setOwnerId(userId);
	    v.setModifierId(userId);
	    v.setAction(actionId);
	    v.setStatus(status.getId());
	    v.setOldRelationship(DatabaseUtil.setObject(oldProperty));
	    v.setOldValue(DatabaseUtil.setObject(oldDestTermObj));
	    v.setNewRelationship(DatabaseUtil.setObject(newProperty));
	    v.setNewValue(DatabaseUtil.setObject(newDestTermObj));
	    v.setOntologyId(ontoInfo.getOntologyId());
	    v.setDateCreate(ProtegeWorkbenchUtility.getDate(""+termIns.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED))));
		v.setDateModified(Date.getROMEDate());
	    DatabaseUtil.createObject(v);
	    ///owlModel.dispose();
	}
	public void deleteTermRelationship(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,RelationshipObject property,TermObject termObj,TermObject destTermObj,ConceptObject conceptObject){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty lexicon = owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION);
		OWLProperty relatedTerm = owlModel.getOWLProperty(property.getName());
		OWLIndividual termIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, ins, lexicon, termObj.getUri());
		OWLIndividual destTermIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, ins, lexicon, destTermObj.getUri());
		termIns.removePropertyValue(relatedTerm, destTermIns);
		setInstanceUpdateDate(owlModel, termIns);
		setInstanceUpdateDate(owlModel, ins);
		
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setTerm(DatabaseUtil.setObject(termObj));
		v.setTermObject(termObj);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOldRelationship(DatabaseUtil.setObject(property));
		v.setOldValue(DatabaseUtil.setObject(destTermObj));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(ProtegeWorkbenchUtility.getDate(""+termIns.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED))));
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
	}
	/*public void addTermNoneFuncValue(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,String propertyURI,String value,String language,TermObject termObject,ConceptObject conceptObject){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty nonFuncProp = owlModel.getOWLProperty(propertyURI);
		OWLProperty lexicon = owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION);
		OWLIndividual termIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, ins, lexicon, termObject.getUri());
		RDFSLiteral val = owlModel.createRDFSLiteral(value, language);
		termIns.addPropertyValue(nonFuncProp, val);
		setInstanceUpdateDate(owlModel, termIns);
		setInstanceUpdateDate(owlModel, ins);
		
		SpellingVariantObject svObjNew = new SpellingVariantObject();
		svObjNew.setLabel(value);
		svObjNew.setLang(language);
		svObjNew.setUri(termObject.getUri());
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setTerm(DatabaseUtil.setObject(termObject));
		v.setTermObject(termObject);
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setNewValue(DatabaseUtil.setObject(svObjNew));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(Date.getROMEDate());
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
	}
	
	public void addTermCode(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,TermCodesObject tcObj,TermObject termObject,ConceptObject conceptObject){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty lexicon = owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION);
		OWLIndividual termIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, ins, lexicon, termObject.getUri());
		OWLProperty codeProperty = owlModel.getOWLProperty(tcObj.getRepository().getName());
		termIns.addPropertyValue(codeProperty, tcObj.getCode());
		setInstanceUpdateDate(owlModel, termIns);
		setInstanceUpdateDate(owlModel, ins);
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setTerm(DatabaseUtil.setObject(termObject));
		v.setTermObject(termObject);
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setNewValue(DatabaseUtil.setObject(tcObj));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(Date.getROMEDate());
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
	}
	public void editTermCode(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,TermCodesObject oldTcObj,TermCodesObject newTcObj,TermObject termObject,ConceptObject conceptObject){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty lexicon = owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION);
		OWLIndividual termIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, ins, lexicon, termObject.getUri());
		OWLProperty codeProperty = owlModel.getOWLProperty(newTcObj.getRepository().getName());
		termIns.setPropertyValue(codeProperty, newTcObj.getCode());
		setInstanceUpdateDate(owlModel, termIns);
		setInstanceUpdateDate(owlModel, ins);
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setTerm(DatabaseUtil.setObject(termObject));
		v.setTermObject(termObject);
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setOldValue(DatabaseUtil.setObject(oldTcObj));
		v.setNewValue(DatabaseUtil.setObject(newTcObj));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(ProtegeWorkbenchUtility.getDate(""+termIns.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED))));
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
	}

	public void deleteTermCode(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,TermCodesObject tcObj,TermObject termObject,ConceptObject conceptObject){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty lexicon = owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION);
		OWLIndividual termIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, ins, lexicon, termObject.getUri());
		OWLProperty codeProperty = owlModel.getOWLProperty(tcObj.getRepository().getName());
		termIns.removePropertyValue(codeProperty, tcObj.getCode());
		setInstanceUpdateDate(owlModel, termIns);
		setInstanceUpdateDate(owlModel, ins);
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setTerm(DatabaseUtil.setObject(termObject));
		v.setTermObject(termObject);
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setOldValue(DatabaseUtil.setObject(tcObj));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(ProtegeWorkbenchUtility.getDate(""+termIns.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED))));
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
	}

	public void editTermNoneFuncValue(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,String propertyURI,String oldValue,String oldLanguage,String newValue,String newLanguage,TermObject termObject,ConceptObject conceptObject){
		
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty prop = owlModel.getOWLProperty(propertyURI);
		OWLProperty lexicon = owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION);
		OWLIndividual termIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, ins, lexicon, termObject.getUri());
		RDFSLiteral newVal = owlModel.createRDFSLiteral(newValue, newLanguage);
		setInstanceUpdateDate(owlModel, termIns);
		setInstanceUpdateDate(owlModel, ins);
		
		Collection<?> val = termIns.getPropertyValues(prop);
		for (Iterator<?> iter = val.iterator(); iter.hasNext();) {
			RDFSLiteral element = (RDFSLiteral) iter.next();
			if(element.getString().equals(oldValue) && element.getLanguage().equals(oldLanguage)){
				termIns.removePropertyValue(prop, element);
				termIns.addPropertyValue(prop, newVal);
			}
		}
		
		
		SpellingVariantObject svObj = new SpellingVariantObject();
		svObj.setLabel(oldValue);
		svObj.setLang(oldLanguage);
		svObj.setUri(termObject.getUri());
		
		SpellingVariantObject svObjNew = new SpellingVariantObject();
		svObjNew.setLabel(newValue);
		svObjNew.setLang(newLanguage);
		svObjNew.setUri(termObject.getUri());
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setTerm(DatabaseUtil.setObject(termObject));
		v.setTermObject(termObject);
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setOldValue(DatabaseUtil.setObject(svObj));
		v.setNewValue(DatabaseUtil.setObject(svObjNew));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(ProtegeWorkbenchUtility.getDate(""+termIns.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED))));
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
	}
	
	public void deleteTermNoneFuncValue(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,String propertyURI,String value,String language,TermObject termObject,ConceptObject conceptObject){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty prop = owlModel.getOWLProperty(propertyURI);
		OWLProperty lexicon = owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION);
		OWLIndividual termIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, ins, lexicon, termObject.getUri());
		setInstanceUpdateDate(owlModel, termIns);
		setInstanceUpdateDate(owlModel, ins);
		
		Collection<?> val = termIns.getPropertyValues(prop);
		for (Iterator<?> iter = val.iterator(); iter.hasNext();) {
			RDFSLiteral element = (RDFSLiteral) iter.next();
			if(element.getString().equals(value) && element.getLanguage().equals(language)){
				termIns.removePropertyValue(prop, element);
			}
		}
		
		SpellingVariantObject svObj = new SpellingVariantObject();
		svObj.setLabel(value);
		svObj.setLang(language);
		svObj.setUri(termObject.getUri());
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setTerm(DatabaseUtil.setObject(termObject));
		v.setTermObject(termObject);
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setOldValue(DatabaseUtil.setObject(svObj));	
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(ProtegeWorkbenchUtility.getDate(""+termIns.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED))));
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
	}*/
	
	public void deletePropertyValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId, NonFuncObject oldValue, RelationshipObject rObj, TermObject termObject, ConceptObject conceptObject){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty prop = owlModel.getOWLProperty(rObj.getName());
		OWLIndividual termInstance = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, ins, owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION), termObject.getUri());
		
		if(oldValue.getLanguage()!=null && !oldValue.getLanguage().equals(""))
		{
			for (Iterator<?> iter = termInstance.getPropertyValues(prop).iterator(); iter.hasNext();) {
				RDFSLiteral element = (RDFSLiteral) iter.next();
				if(element.getString().equals(oldValue.getValue()) && element.getLanguage().equals(oldValue.getLanguage())){
					termInstance.removePropertyValue(prop, element);
				}
			}
		}
		else
		{
			termInstance.removePropertyValue(prop, oldValue.getValue());
		}
		
		setInstanceUpdateDate(owlModel, ins);
		setInstanceUpdateDate(owlModel, termInstance);
		
		AttributesObject attObj = new AttributesObject();
		attObj.setRelationshipObject(rObj);
		attObj.setValue(oldValue);

		Validation v = new Validation();
		v.setOldValue(DatabaseUtil.setObject(attObj));
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setTerm(DatabaseUtil.setObject(termObject));
		v.setTermObject(termObject);
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(ProtegeWorkbenchUtility.getDate(""+ins.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED))));
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
	}
	
	public void updatePropertyValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,NonFuncObject oldValue, NonFuncObject newValue, String rObjName, TermObject termObject, ConceptObject conceptObject){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		RelationshipObject rObj = ProtegeWorkbenchUtility.makeDatatypeRelationshipObject(owlModel.getOWLDatatypeProperty(rObjName));
		editPropertyValue(ontoInfo ,actionId,status,userId,oldValue, newValue, rObj, termObject, conceptObject);
	}
	
	
	public void editPropertyValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,NonFuncObject oldValue, NonFuncObject newValue, RelationshipObject rObj, TermObject termObject, ConceptObject conceptObject){
		
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty prop = owlModel.getOWLProperty(rObj.getName());
		OWLIndividual termInstance = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, ins, owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION), termObject.getUri());
		if(oldValue.getLanguage()!=null && !oldValue.getLanguage().equals(""))
		{
			for (Iterator<?> iter = termInstance.getPropertyValues(prop).iterator(); iter.hasNext();) {
				RDFSLiteral element = (RDFSLiteral) iter.next();
				if(element.getString().equals(oldValue.getValue()) && element.getLanguage().equals(oldValue.getLanguage())){
					termInstance.removePropertyValue(prop, element);
					termInstance.addPropertyValue(prop, owlModel.createRDFSLiteral(newValue.getValue(), newValue.getLanguage()));
				}
			}
		}
		else
		{
			termInstance.removePropertyValue(prop, oldValue.getValue());
			termInstance.addPropertyValue(prop, newValue.getValue());
		}
		
		setInstanceUpdateDate(owlModel, ins);
		setInstanceUpdateDate(owlModel, termInstance);
		
		AttributesObject oldAttObj = new AttributesObject();
		oldAttObj.setRelationshipObject(rObj);
		oldAttObj.setValue(oldValue);
		
		AttributesObject newAttObj = new AttributesObject();
		newAttObj.setRelationshipObject(rObj);
		newAttObj.setValue(newValue);
		
		Validation v = new Validation();
		v.setOldValue(DatabaseUtil.setObject(oldAttObj));
		v.setNewValue(DatabaseUtil.setObject(newAttObj));
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setTerm(DatabaseUtil.setObject(termObject));
		v.setTermObject(termObject);
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(ProtegeWorkbenchUtility.getDate(""+ins.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED))));
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
	}
	
	public void addPropertyValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId, NonFuncObject value,RelationshipObject rObj, TermObject termObject, ConceptObject conceptObject){
		OWLModel owlModel = null; 
		try
		{
			owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
			OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
			OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
			OWLProperty prop = owlModel.getOWLProperty(rObj.getName());
			OWLIndividual termInstance = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, ins, owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION), termObject.getUri());
			
			
			if(value.getLanguage()!=null && !value.getLanguage().equals(""))
				termInstance.addPropertyValue(prop, owlModel.createRDFSLiteral(value.getValue(), value.getLanguage()));
			else
				termInstance.addPropertyValue(prop, value.getValue());

			setInstanceUpdateDate(owlModel, ins);
			setInstanceUpdateDate(owlModel, termInstance);
			
			AttributesObject attObj = new AttributesObject();
			attObj.setRelationshipObject(rObj);
			attObj.setValue(value);
			
			Validation v = new Validation();
			v.setNewValue(DatabaseUtil.setObject(attObj));
			v.setConcept(DatabaseUtil.setObject(conceptObject));
			v.setTerm(DatabaseUtil.setObject(termObject));
			v.setTermObject(termObject);
			v.setAction(actionId);
			v.setStatus(status.getId());
			v.setOwnerId(userId);
			v.setModifierId(userId);
			v.setOntologyId(ontoInfo.getOntologyId());
			v.setDateCreate(Date.getROMEDate());
			v.setDateModified(Date.getROMEDate());
			DatabaseUtil.createObject(v);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if(owlModel!=null)
		{
			///owlModel.dispose();
		}
		
	}
	
	public HashMap<String, String> getTermCodes(ArrayList<String> terms, OntologyInfo ontoInfo)
	{
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLProperty hasCodeAgrovoc = owlModel.getOWLProperty(ModelConstants.RHASCODEAGROVOC);
		
		HashMap<String, String> termCodeMap = new HashMap<String, String>();
		for(String key: terms)
		{
			OWLIndividual termInstance = owlModel.getOWLIndividual(key);
			termCodeMap.put(key, ""+termInstance.getPropertyValue(hasCodeAgrovoc));
		}
		
		///owlModel.dispose();
		return termCodeMap;
	}
	
}
