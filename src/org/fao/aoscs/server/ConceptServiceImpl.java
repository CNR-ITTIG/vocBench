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

import org.fao.aoscs.client.module.concept.service.ConceptService;
import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.domain.AttributesObject;
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
import org.fao.aoscs.domain.LabelObject;
import org.fao.aoscs.domain.NonFuncObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.RecentChanges;
import org.fao.aoscs.domain.RecentChangesInitObject;
import org.fao.aoscs.domain.RelationObject;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.TermMoveObject;
import org.fao.aoscs.domain.TermObject;
import org.fao.aoscs.domain.TermRelationshipObject;
import org.fao.aoscs.domain.TranslationObject;
import org.fao.aoscs.domain.TreeObject;
import org.fao.aoscs.domain.Validation;
import org.fao.aoscs.hibernate.DatabaseUtil;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.hibernate.QueryFactory;
import org.fao.aoscs.server.index.IndexingEngineFactory;
import org.fao.aoscs.server.protege.ProtegeModelFactory;
import org.fao.aoscs.server.protege.ProtegeWorkbenchUtility;
import org.fao.aoscs.server.utility.ConceptUtility;
import org.fao.aoscs.server.utility.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.gen2.table.client.TableModelHelper.ColumnSortInfo;
import com.google.gwt.gen2.table.client.TableModelHelper.ColumnSortList;
import com.google.gwt.gen2.table.client.TableModelHelper.Request;

import edu.stanford.smi.protegex.owl.model.NamespaceManager;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;
import edu.stanford.smi.protegex.owl.model.RDFSNames;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLIndividual;
import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFSLiteral;

public class ConceptServiceImpl extends PersistentRemoteService implements ConceptService {
	
	private static final long serialVersionUID = -8716000785148203270L;
	protected static Logger logger = LoggerFactory.getLogger(ConceptServiceImpl.class);

	//-------------------------------------------------------------------------
	//
	// Initialization of Remote service : must be done before any server call !
	//
	//-------------------------------------------------------------------------
	@Override
	public void init() throws ServletException
	{
		super.init();
		GWT.log("starting ConceptService initialization", null);
		logger.info("starting ConceptService initialization");

		// Bean Manager initialization
		setBeanManager(GwtConfigurationHelper.initGwtStatelessBeanManager( new HibernateUtil(HibernateUtilities.getSessionFactory())));;
		// End of Bean Manager initialization

		GWT.log("ConceptService initialized", null);
		logger.info("ConceptService initialized");
	}
	
	public InitializeConceptData initData(int group_id, OntologyInfo ontoInfo, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList){
		GWT.log("concept data being initialized", null);
		logger.info("concept data being initialized");
		InitializeConceptData data = new InitializeConceptData();
		try
		{
			OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
			data.setSource(ConceptUtility.getSource());
			data.setTermCodeProperties(ConceptUtility.getAllTermCodeProperties(owlModel));
			data.setConceptDomainAttributes(ConceptUtility.getDatatypeProperties(owlModel, ModelConstants.RCONCEPTDOMAINDATATYPEPROPERTY));
			data.setConceptEditorialAttributes(ConceptUtility.getDatatypeProperties(owlModel, ModelConstants.RCONCEPTEDITORIALDATATYPEPROPERTY));
			data.setTermDomainAttributes(ConceptUtility.getDatatypeProperties(owlModel, ModelConstants.RTERMDOMAINDATATYPEPROPERTY));
			data.setTermEditorialAttributes(ConceptUtility.getDatatypeProperties(owlModel, ModelConstants.RTERMEDITORIALDATATYPEPROPERTY));
			data.setActionMap(ConceptUtility.getActionMap(group_id));
			data.setActionStatus(ConceptUtility.getActionStatusMap(group_id));
			data.setTermCodePropertyType(ConceptUtility.getTermCodeType(owlModel));
			data.setBelongsToModule(InitializeConceptData.CONCEPTMODULE);
			data.setConceptTreeObject(new TreeServiceImpl().getTreeObject(owlModel, ModelConstants.CDOMAINCONCEPT, showAlsoNonpreferredTerms, isHideDeprecated, langList));
			
			///owlModel.dispose();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		GWT.log("concept data initialized: " + data, null);
		logger.info("concept data initialized: " + data);
		return data;
	}
	
	public ConceptDetailObject getConceptDetail(OntologyInfo ontoInfo, ArrayList<String> langList, String className, String parentClassName)
	{
		logger.info("getting details of concept: " + className + ", " + parentClassName);
		ConceptDetailObject cDetailObj = new ConceptDetailObject();
		try {
			logger.info("gettingOWLModel");
			OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
			logger.info("owlModel got");
			ConceptObject cObj = getSelectedConcept(owlModel, className, parentClassName);
			
			cDetailObj.setConceptObject(cObj);
			cDetailObj.setConceptTermObject(getTerm(owlModel, className));
			cDetailObj.setInformationObject(null);
			cDetailObj.setImageObject(null);
			cDetailObj.setDefinitionObject(null);
			cDetailObj.setRelationObject(null);
			cDetailObj.setNoteObject(null);
			cDetailObj.setAttributeObject(null);
			cDetailObj.setHierarchyObject(null);
			
			cDetailObj.setTermCount(getConceptPropertyCount(owlModel, langList, className));
			cDetailObj.setDefinitionCount(getPropertyCount(owlModel, ModelConstants.RHASDEFINITION, className));
			cDetailObj.setNoteCount(getPropertyValueCount(owlModel, ModelConstants.RCONCEPTEDITORIALDATATYPEPROPERTY, className));
			cDetailObj.setAttributeCount(getPropertyValueCount(owlModel, ModelConstants.RCONCEPTDOMAINDATATYPEPROPERTY, className));
			cDetailObj.setRelationCount(getConceptRelationshipCount(owlModel, className));
			cDetailObj.setImageCount(getPropertyCount(owlModel, ModelConstants.RHASIMAGE, className));
			
			int si = -1;
			try
			{
				si = filterHistory( cObj.getUri() , getConceptHistoryData(ontoInfo.getOntologyId() , InformationObject.CONCEPT_TYPE) , InformationObject.CONCEPT_TYPE ).size();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			cDetailObj.setHistoryCount(si);
			///owlModel.dispose();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		logger.info("concept details: " + cDetailObj);
		return cDetailObj;
	}
	
	public ConceptDetailObject getCategoryDetail(OntologyInfo ontoInfo, ArrayList<String> langList, String className, String parentClassName)
	{
		logger.info("getting details of category: " + className + ", " + parentClassName);
		ConceptDetailObject cDetailObj = new ConceptDetailObject();
		try {
			logger.info("gettingOWLModel");
			OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
			logger.info("owlModel got");
			ConceptObject cObj = getSelectedCategory(owlModel, className, parentClassName);
			
			cDetailObj.setConceptObject(cObj);
			cDetailObj.setConceptTermObject(getTerm(owlModel, className));
			cDetailObj.setDefinitionObject(null);
			cDetailObj.setNoteObject(null);
			cDetailObj.setAttributeObject(null);
			cDetailObj.setConceptMappedObject(null);
			cDetailObj.setInformationObject(null);
			
			cDetailObj.setTermCount(getConceptPropertyCount(owlModel, langList, className));
			cDetailObj.setDefinitionCount(getPropertyCount(owlModel, ModelConstants.RHASDEFINITION, className));
			cDetailObj.setNoteCount(getPropertyValueCount(owlModel, ModelConstants.RCONCEPTEDITORIALDATATYPEPROPERTY, className));
			cDetailObj.setAttributeCount(getPropertyValueCount(owlModel, ModelConstants.RCONCEPTDOMAINDATATYPEPROPERTY, className));
			cDetailObj.setConceptMappedCount(getPropertyValueCount(owlModel, ModelConstants.RHASMAPPEDDOMAINCONCEPT, className));
			int si = -1;
			try
			{
				si = filterHistory( cObj.getUri() , getConceptHistoryData(ontoInfo.getOntologyId() , InformationObject.CONCEPT_TYPE) , InformationObject.CONCEPT_TYPE ).size();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			cDetailObj.setHistoryCount(si);
			///owlModel.dispose();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		logger.info("category details: " + cDetailObj);
		return cDetailObj;
	}

	public ConceptObject getSelectedConcept(OntologyInfo ontoInfo, String className, String parentClassName) {
		logger.info("getSelectedConcept(" + ontoInfo + ", " + className + ", " + parentClassName + ")");
		ConceptObject cObj = new ConceptObject();
		try
		{
			OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
			cObj = getSelectedConcept(owlModel, className, parentClassName);
			///owlModel.dispose();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return cObj;
	}

	public ConceptObject getSelectedConcept(OWLModel owlModel, String className, String parentClassName) {
		logger.info("getSelectedConcept(OWLModel, " + className + ", " + parentClassName + "): ");
		ConceptObject cObj = new ConceptObject();
		try
		{
			cObj = getConceptObject(owlModel, className);
			cObj.setBelongsToModule(ConceptObject.CONCEPTMODULE);
			ConceptObject parentObject = getParentConceptObject(owlModel, parentClassName);
			cObj.setParentInstance(parentObject.getConceptInstance());
			cObj.setParentObject(parentObject);
			cObj.setParentURI(parentObject.getUri());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return cObj;
	}
	
	public ConceptObject getSelectedCategory(OWLModel owlModel, String className, String parentClassName) {
		logger.info("getSelectedConcept(OWLModel, " + className + ", " + parentClassName + "): ");
		ConceptObject cObj = new ConceptObject();
		try
		{
			cObj = getConceptObject(owlModel, className);
			cObj.setBelongsToModule(ConceptObject.CLASSIFICATIONMODULE);
			ConceptObject parentObject = getParentCategoryObject(owlModel, parentClassName);
			cObj.setParentInstance(parentObject.getConceptInstance());
			cObj.setParentObject(parentObject);
			cObj.setParentURI(parentObject.getUri());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return cObj;
	}
	
	public ConceptObject getConceptObject(OWLModel owlModel, String className) {
		logger.info("getConceptObject(OWLModel, String): " + className);
		ConceptObject cObj = new ConceptObject();
		try
		{
			OWLNamedClass cls = owlModel.getOWLNamedClass(className);
			cObj = ProtegeWorkbenchUtility.makeConceptObject(owlModel, cls);
			cObj.setBelongsToModule(ConceptObject.CONCEPTMODULE);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return cObj;
	}
	
	private ConceptObject getParentCategoryObject(OWLModel owlModel, String className) {
		logger.info("getParentConceptObject: " + className);
		ConceptObject cObj = new ConceptObject();
		cObj.setBelongsToModule(ConceptObject.CLASSIFICATIONMODULE);
		if(className!=null && !ModelConstants.CDOMAINCONCEPT.equals(className)){
			cObj = getConceptObject(owlModel, className);
		}else{
			if(ModelConstants.CDOMAINCONCEPT.equals(className))
			{
				TermObject termObject = new TermObject();
				termObject.setLabel("Top level concept");
				termObject.setUri(ModelConstants.COMMONBASENAMESPACE+"i_en_domain_concept");
				termObject.setName("i_en_domain_concept");
				termObject.setLang("en");
				termObject.setConceptUri(ModelConstants.COMMONBASENAMESPACE+ModelConstants.CDOMAINCONCEPT);
				termObject.setConceptName(ModelConstants.CDOMAINCONCEPT);
				
				cObj = new ConceptObject();
				cObj.setUri(ModelConstants.COMMONBASENAMESPACE+ModelConstants.CDOMAINCONCEPT);
				cObj.addTerm(termObject.getUri(), termObject);
				cObj.setName(ModelConstants.CDOMAINCONCEPT);
			}
		}
		return cObj;
	}

	private ConceptObject getParentConceptObject(OWLModel owlModel, String className) {
		logger.info("getParentConceptObject: " + className);
		ConceptObject cObj = new ConceptObject();
		cObj.setBelongsToModule(ConceptObject.CONCEPTMODULE);
		if(className!=null && !ModelConstants.CDOMAINCONCEPT.equals(className)){
			cObj = getConceptObject(owlModel, className);
		}else{
			if(ModelConstants.CDOMAINCONCEPT.equals(className))
			{
				TermObject termObject = new TermObject();
				termObject.setLabel("Top level concept");
				termObject.setUri(ModelConstants.COMMONBASENAMESPACE+"i_en_domain_concept");
				termObject.setName("i_en_domain_concept");
				termObject.setLang("en");
				termObject.setConceptUri(ModelConstants.COMMONBASENAMESPACE+ModelConstants.CDOMAINCONCEPT);
				termObject.setConceptName(ModelConstants.CDOMAINCONCEPT);
				
				cObj = new ConceptObject();
				cObj.setUri(ModelConstants.COMMONBASENAMESPACE+ModelConstants.CDOMAINCONCEPT);
				cObj.addTerm(termObject.getUri(), termObject);
				cObj.setName(ModelConstants.CDOMAINCONCEPT);
			}
		}
		return cObj;
	}

	private void setInstanceUpdateDate(OWLModel owlModel, OWLIndividual ins) {
		logger.info("setInstanceUpdateData: " + ins);
		OWLProperty updateDate = owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE);
		ins.setPropertyValue(updateDate, Date.getDateTime());
	}
	
	public HashMap<String, String> getNamespaces(OntologyInfo ontoInfo)
	{
		HashMap<String, String> nameSpaceMap = new HashMap<String, String>();
		try
		{
			OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
			nameSpaceMap = getNamespaces(owlModel);
			///owlModel.dispose();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return nameSpaceMap;
	}
	
	private HashMap<String, String> getNamespaces(OWLModel owlModel)
	{
		HashMap<String, String> nameSpaceMap = new HashMap<String, String>();
		NamespaceManager nameSpaceManager = owlModel.getNamespaceManager();
		for (Iterator<?> iter = nameSpaceManager.getPrefixes().iterator(); iter.hasNext();) 
		{
			String prefix = (String) iter.next();
			String namespace = nameSpaceManager.getNamespaceForPrefix(prefix);
			// filter out namespace having text as www.w3.org
			if( !namespace.contains("www.w3.org"))
				nameSpaceMap.put(prefix, namespace);
		 }
		return nameSpaceMap;
	}
	
	public void addNewNamespace(OntologyInfo ontoInfo, String namespacePrefix, String namespace)
	{
		try
		{
			OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
			addNewNamespace(owlModel, namespacePrefix, namespace);
			///owlModel.dispose();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void addNewNamespace(OWLModel owlModel, String namespacePrefix, String namespace)
	{
		owlModel.getNamespaceManager().setPrefix(namespace, namespacePrefix);
	}
	
	public ConceptObject addNewConcept(OntologyInfo ontoInfo ,int actionId,int userId,ConceptObject conceptObject,TermObject termObject, String conceptPosition,ConceptObject selectedConceptObject){
		try
		{
			long unique = new java.util.Date().getTime();
			OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
			OWLProperty lexicon = owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION);
			OWLProperty mainlabel = owlModel.getOWLProperty(ModelConstants.RISMAINLABEL);
			OWLProperty createDate = owlModel.getOWLProperty(ModelConstants.RHASDATECREATED);
			OWLProperty status = owlModel.getOWLProperty(ModelConstants.RHASSTATUS);
			OWLProperty hasCodeAgrovoc = owlModel.getOWLProperty(ModelConstants.RHASCODEAGROVOC);
			OWLNamedClass nounCls = owlModel.getOWLNamedClass(ModelConstants.CNOUN);
			
			String namespace = conceptObject.getNameSpace();
			String agrovocNamespacePrefix = owlModel.getNamespaceManager().getPrefix(namespace);
			String clsNamePrefix = agrovocNamespacePrefix+":";
			
			
			String className = clsNamePrefix+"c_"+unique;
			String instanceName = clsNamePrefix+"i_"+unique;
			String termName = clsNamePrefix+"i_"+termObject.getLang().toLowerCase()+"_"+unique;
			String conceptName = conceptObject.getName();
			
			if(conceptName!=null && !conceptName.equals(""))
			{
				className = clsNamePrefix+conceptName;
				instanceName = clsNamePrefix+"i_"+conceptName;
				termName = clsNamePrefix+"i_"+termObject.getLang().toLowerCase()+"_"+conceptName;
			}
			
			OWLNamedClass cls = owlModel.createOWLNamedClass(className);
			
			ConceptObject parentObject = null;
			if(selectedConceptObject.getName().equals(ModelConstants.CDOMAINCONCEPT))
			    parentObject = getParentConceptObject(owlModel, selectedConceptObject.getName());
			else 
			    parentObject = selectedConceptObject;
			
			if(conceptPosition.equals("sameLevel")){
				
				if(selectedConceptObject.getParentObject()==null)
				{
					for (Iterator<?> it = owlModel.getOWLNamedClass(selectedConceptObject.getName()).getSuperclasses(false).iterator(); it.hasNext();) 
					{
						OWLNamedClass parentCls = (OWLNamedClass) it.next();
						ConceptObject parentConceptObject = ProtegeWorkbenchUtility.makeConceptObject(owlModel, parentCls);
						selectedConceptObject.setParentObject(parentConceptObject);
						selectedConceptObject.setParentInstance(parentConceptObject.getConceptInstance());
						selectedConceptObject.setParentURI(parentConceptObject.getUri());
					}
				}
				parentObject = selectedConceptObject.getParentObject();
			}
			OWLNamedClass parentCls = owlModel.getOWLNamedClass(parentObject.getName());
			cls.addSuperclass(parentCls);
	
			OWLIndividual term = (OWLIndividual) nounCls.createInstance(termName);
			term.addLabel(termObject.getLabel(), termObject.getLang());
			term.addPropertyValue(mainlabel, termObject.isMainLabel());
			term.addPropertyValue(createDate, Date.getDateTime());
			term.addPropertyValue(status, conceptObject.getStatus());
			term.addPropertyValue(hasCodeAgrovoc, ""+unique);
			setInstanceUpdateDate(owlModel, term);
			 
			OWLIndividual ins = (OWLIndividual) cls.createInstance(instanceName);
			ins.addPropertyValue(lexicon, term);
			ins.addPropertyValue(createDate, Date.getDateTime());
			ins.addPropertyValue(status, conceptObject.getStatus());
			setInstanceUpdateDate(owlModel, ins);
			
			termObject.setUri(term.getURI());
			termObject.setName(term.getName());
			conceptObject.setConceptInstance(ins.getURI());
			conceptObject.setUri(cls.getURI());
			conceptObject.setName(cls.getName());
			termObject.setConceptUri(conceptObject.getUri());
			termObject.setConceptName(conceptObject.getName());
			conceptObject.addTerm(termObject.getUri(), termObject);
			 
			if(ConfigConstants.ISINDEXING)
			{
				//update index
				IndexingEngineFactory.updateIndex(ontoInfo, term.getURI(), termObject.getLang(), SearchServiceImplOWLART.c_nounInstancesIndexCategory);
			}
			
			Validation v = new Validation();
			v.setConcept(DatabaseUtil.setObject(parentObject));
			v.setOwnerId(userId);
			v.setModifierId(userId);
			v.setNewValue(DatabaseUtil.setObject(conceptObject));
			v.setStatus(conceptObject.getStatusID());
			v.setAction(actionId);
			v.setOntologyId(ontoInfo.getOntologyId());
			v.setDateCreate(Date.getROMEDate());
			v.setDateModified(Date.getROMEDate());
			DatabaseUtil.createObject(v);
			///owlModel.dispose();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return conceptObject;
	}
	
	public ConceptObject addNewConceptWithoutTerm(OntologyInfo ontoInfo ,int actionId,int userId,ConceptObject conceptObject, String conceptPosition,ConceptObject selectedConceptObject){
		try
		{
			long unique = new java.util.Date().getTime();
			OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
			OWLProperty lexicon = owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION);
			OWLProperty mainlabel = owlModel.getOWLProperty(ModelConstants.RISMAINLABEL);
			OWLProperty createDate = owlModel.getOWLProperty(ModelConstants.RHASDATECREATED);
			OWLProperty status = owlModel.getOWLProperty(ModelConstants.RHASSTATUS);
			OWLProperty hasCodeAgrovoc = owlModel.getOWLProperty(ModelConstants.RHASCODEAGROVOC);
			OWLNamedClass nounCls = owlModel.getOWLNamedClass(ModelConstants.CNOUN);
			
			String namespace = conceptObject.getNameSpace();
			String agrovocNamespacePrefix = owlModel.getNamespaceManager().getPrefix(namespace);
			String clsNamePrefix = agrovocNamespacePrefix+":";
			
			
			String className = clsNamePrefix+"c_"+unique;
			String instanceName = clsNamePrefix+"i_"+unique;
//			String termName = clsNamePrefix+"i_"+termObject.getLang().toLowerCase()+"_"+unique;
			String conceptName = conceptObject.getName();
			
			if(conceptName!=null && !conceptName.equals(""))
			{
				className = clsNamePrefix+conceptName;
				instanceName = clsNamePrefix+"i_"+conceptName;
//				termName = clsNamePrefix+"i_"+termObject.getLang().toLowerCase()+"_"+conceptName;
			}
			
			OWLNamedClass cls = owlModel.createOWLNamedClass(className);
			
			ConceptObject parentObject = null;
			if(selectedConceptObject.getName().equals(ModelConstants.CDOMAINCONCEPT))
			    parentObject = getParentConceptObject(owlModel, selectedConceptObject.getName());
			else 
			    parentObject = selectedConceptObject;
			
			if(conceptPosition.equals("sameLevel")){
				
				if(selectedConceptObject.getParentObject()==null)
				{
					for (Iterator<?> it = owlModel.getOWLNamedClass(selectedConceptObject.getName()).getSuperclasses(false).iterator(); it.hasNext();) 
					{
						OWLNamedClass parentCls = (OWLNamedClass) it.next();
						ConceptObject parentConceptObject = ProtegeWorkbenchUtility.makeConceptObject(owlModel, parentCls);
						selectedConceptObject.setParentObject(parentConceptObject);
						selectedConceptObject.setParentInstance(parentConceptObject.getConceptInstance());
						selectedConceptObject.setParentURI(parentConceptObject.getUri());
					}
				}
				parentObject = selectedConceptObject.getParentObject();
			}
			OWLNamedClass parentCls = owlModel.getOWLNamedClass(parentObject.getName());
			cls.addSuperclass(parentCls);
	
//			OWLIndividual term = (OWLIndividual) nounCls.createInstance(termName);
//			term.addLabel(termObject.getLabel(), termObject.getLang());
//			term.addPropertyValue(mainlabel, termObject.isMainLabel());
//			term.addPropertyValue(createDate, Date.getDateTime());
//			term.addPropertyValue(status, conceptObject.getStatus());
//			term.addPropertyValue(hasCodeAgrovoc, ""+unique);
//			setInstanceUpdateDate(owlModel, term);
			 
			OWLIndividual ins = (OWLIndividual) cls.createInstance(instanceName);
//			ins.addPropertyValue(lexicon, term);
			ins.addPropertyValue(createDate, Date.getDateTime());
			ins.addPropertyValue(status, conceptObject.getStatus());
			setInstanceUpdateDate(owlModel, ins);
			
//			termObject.setUri(term.getURI());
//			termObject.setName(term.getName());
			conceptObject.setConceptInstance(ins.getURI());
			conceptObject.setUri(cls.getURI());
			conceptObject.setName(cls.getName());
//			termObject.setConceptUri(conceptObject.getUri());
//			termObject.setConceptName(conceptObject.getName());
//			conceptObject.addTerm(termObject.getUri(), termObject);
			 
//			if(ConfigConstants.ISINDEXING)
//			{
//				//update index
//				IndexingEngineFactory.updateIndex(ontoInfo, term.getURI(), termObject.getLang(), SearchServiceImplOWLART.c_nounInstancesIndexCategory);
//			}
			
			Validation v = new Validation();
			v.setConcept(DatabaseUtil.setObject(parentObject));
			v.setOwnerId(userId);
			v.setModifierId(userId);
			v.setNewValue(DatabaseUtil.setObject(conceptObject));
			v.setStatus(conceptObject.getStatusID());
			v.setAction(actionId);
			v.setOntologyId(ontoInfo.getOntologyId());
			v.setDateCreate(Date.getROMEDate());
			v.setDateModified(Date.getROMEDate());
			DatabaseUtil.createObject(v);
			///owlModel.dispose();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return conceptObject;
	}
	
	public RelationObject deleteRelationship(OntologyInfo ontoInfo ,RelationshipObject rObj,ConceptObject conceptObject,ConceptObject destConceptObj,OwlStatus status,int actionId,int userId){
		logger.info("deleteRelationship(" + ontoInfo + ", " + rObj + ", " + conceptObject + ", "
				+ destConceptObj + ", " + status + ", " + actionId + ", " + userId + ")");
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLNamedClass destCls = owlModel.getOWLNamedClass(destConceptObj.getName());
		OWLIndividual destIns = ProtegeWorkbenchUtility.getConceptInstance(owlModel, destCls);
		OWLObjectProperty prop = owlModel.getOWLObjectProperty(rObj.getName());
		ins.removePropertyValue(prop, destIns);
		setInstanceUpdateDate(owlModel, ins);
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setAction(actionId);
		v.setOldRelationship(DatabaseUtil.setObject(rObj));
		v.setOldValue(DatabaseUtil.setObject(destConceptObj));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(ProtegeWorkbenchUtility.getDate(""+ins.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED))));
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
		return getConceptRelationship(owlModel, conceptObject.getName());
	}
	public RelationObject editRelationship(OntologyInfo ontoInfo ,RelationshipObject rObj,RelationshipObject newRObj,String conceptName,String destConceptName,String newDestConceptName,OwlStatus status,int actionId,int userId){
		logger.info("editRelationship(" + ontoInfo + ", " + rObj + ", " + newRObj + ", " + conceptName + ", "
				+ destConceptName + ", " + newDestConceptName + ", " + status + ", " + actionId + ", "
				+ userId + ")");
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptName);
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLNamedClass destCls = owlModel.getOWLNamedClass(destConceptName);
		OWLIndividual destIns = ProtegeWorkbenchUtility.getConceptInstance(owlModel, destCls);
		OWLNamedClass newDestCls = owlModel.getOWLNamedClass(newDestConceptName);
		OWLIndividual newDestIns = ProtegeWorkbenchUtility.getConceptInstance(owlModel, newDestCls);
		OWLObjectProperty prop = owlModel.getOWLObjectProperty(rObj.getName());
		OWLObjectProperty newProp = owlModel.getOWLObjectProperty(newRObj.getName());
		ins.removePropertyValue(prop, destIns);
		ins.addPropertyValue(newProp, newDestIns);
		setInstanceUpdateDate(owlModel, ins);	
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(ProtegeWorkbenchUtility.makeConceptObject(owlModel, cls)));
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setAction(actionId);
		v.setOldRelationship(DatabaseUtil.setObject(rObj));
		v.setOldValue(DatabaseUtil.setObject(ProtegeWorkbenchUtility.makeConceptObject(owlModel, destCls)));
		v.setNewRelationship(DatabaseUtil.setObject(newRObj));
		v.setNewValue(DatabaseUtil.setObject(ProtegeWorkbenchUtility.makeConceptObject(owlModel, newDestCls)));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(ProtegeWorkbenchUtility.getDate(""+ins.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED))));
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
		return getConceptRelationship(owlModel, conceptName);
	}
	public RelationObject addNewRelationship(OntologyInfo ontoInfo, RelationshipObject rObj, String conceptName,String destConceptName, OwlStatus status,int actionId,int userId){

		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptName);
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLNamedClass destCls = owlModel.getOWLNamedClass(destConceptName);
		OWLIndividual destIns = ProtegeWorkbenchUtility.getConceptInstance(owlModel, destCls);
		OWLObjectProperty prop = owlModel.getOWLObjectProperty(rObj.getName());
		ins.addPropertyValue(prop, destIns);
		setInstanceUpdateDate(owlModel, ins);
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(ProtegeWorkbenchUtility.makeConceptObject(owlModel, cls)));
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setAction(actionId);
		v.setNewRelationshipObject(rObj);
		v.setNewRelationship(DatabaseUtil.setObject(rObj));
		v.setNewValue(DatabaseUtil.setObject(ProtegeWorkbenchUtility.makeConceptObject(owlModel, destCls)));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(Date.getROMEDate());
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
		return getConceptRelationship(owlModel, conceptName);
	}
	
	public int getConceptStatus(String status){
		int statusId = 0 ;
		String sqlQuery = "SELECT id FROM owl_status WHERE status='"+status.toLowerCase()+"'";
		ArrayList<String[]> sqlResult = QueryFactory.getHibernateSQLQuery(sqlQuery);
		if(!sqlResult.isEmpty()){
			statusId = Integer.parseInt(((String[])sqlResult.get(0))[0]);
		}
		return statusId;
	}
	
	public void deleteConcept(OntologyInfo ontoInfo ,int actionId,int userId,OwlStatus status,ConceptObject conceptObject){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty statusProp = owlModel.getOWLProperty(ModelConstants.RHASSTATUS);
		ins.setPropertyValue(statusProp, status.getStatus());
		setInstanceUpdateDate(owlModel, ins);
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setOldValue(DatabaseUtil.setObject(conceptObject));
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setAction(actionId);
		v.setOldStatusLabel(conceptObject.getStatus());
		v.setOldStatus(conceptObject.getStatusID());
		v.setStatus(status.getId());
		v.setStatusLabel(status.getStatus());
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(conceptObject.getDateCreate());
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
	}
	
	public DefinitionObject deleteDefinitionLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject oldTransObj,ConceptObject conceptObject){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty hasDef = owlModel.getOWLProperty(ModelConstants.RHASDEFINITION);
		OWLIndividual defIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, ins, hasDef, oldTransObj.getUri());
		defIns.removeLabel(oldTransObj.getLabel(), oldTransObj.getLang());
		setInstanceUpdateDate(owlModel, defIns);
		setInstanceUpdateDate(owlModel, ins);
		
		if(ConfigConstants.ISINDEXING)
		{
			//update index
			IndexingEngineFactory.updateIndex(ontoInfo, defIns.getURI(), oldTransObj.getLang(), SearchServiceImplOWLART.index_definition);
		}
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setAction(actionId);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setOldValue(DatabaseUtil.setObject(oldTransObj));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(ProtegeWorkbenchUtility.getDate(""+defIns.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED))));
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
		return getConceptDefinition(owlModel, conceptObject.getName());
	}
	public DefinitionObject editDefinitionLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject oldTransObj,TranslationObject newTransObj,ConceptObject conceptObject){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty hasDef = owlModel.getOWLProperty(ModelConstants.RHASDEFINITION);
		OWLIndividual defIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, ins, hasDef, oldTransObj.getUri());
		defIns.removeLabel(oldTransObj.getLabel(), oldTransObj.getLang());
		defIns.addLabel(newTransObj.getLabel(),newTransObj.getLang());
		setInstanceUpdateDate(owlModel, defIns);
		setInstanceUpdateDate(owlModel, ins);
		
		if(ConfigConstants.ISINDEXING)
		{
			//update index
			IndexingEngineFactory.updateIndex(ontoInfo, defIns.getURI(), newTransObj.getLang(), SearchServiceImplOWLART.index_definition);
		}

		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setAction(actionId);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setOldValue(DatabaseUtil.setObject(oldTransObj));
		v.setNewValue(DatabaseUtil.setObject(newTransObj));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(ProtegeWorkbenchUtility.getDate(""+defIns.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED))));
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
		return getConceptDefinition(owlModel, conceptObject.getName());
	}
	
	public DefinitionObject deleteDefinitionExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject oldIdo,ConceptObject conceptObject){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty hasDef = owlModel.getOWLProperty(ModelConstants.RHASDEFINITION);
		OWLProperty takenFromSource = owlModel.getOWLProperty(ModelConstants.RTAKENFROMSOURCE);
		OWLProperty hasSourceLink = owlModel.getOWLProperty(ModelConstants.RHASSOURCELINK);
		OWLIndividual defIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, ins, hasDef, oldIdo.getIDUri());
		defIns.removePropertyValue(takenFromSource, oldIdo.getIDSource());
		defIns.removePropertyValue(hasSourceLink, oldIdo.getIDSourceURL());
		setInstanceUpdateDate(owlModel, defIns);
		setInstanceUpdateDate(owlModel, ins);
		
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setAction(actionId);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setOldValue(DatabaseUtil.setObject(oldIdo));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(ProtegeWorkbenchUtility.getDate(""+defIns.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED))));
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
		return getConceptDefinition(owlModel, conceptObject.getName());
	}
	
	public DefinitionObject editDefinitionExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject oldIdo,IDObject newIdo,ConceptObject conceptObject){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty hasDef = owlModel.getOWLProperty(ModelConstants.RHASDEFINITION);
		OWLProperty takenFromSource = owlModel.getOWLProperty(ModelConstants.RTAKENFROMSOURCE);
		OWLProperty hasSourceLink = owlModel.getOWLProperty(ModelConstants.RHASSOURCELINK);
		OWLIndividual defIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, ins, hasDef, oldIdo.getIDUri());
		defIns.setPropertyValue(takenFromSource, newIdo.getIDSource());
		defIns.setPropertyValue(hasSourceLink, newIdo.getIDSourceURL());
		setInstanceUpdateDate(owlModel, defIns);
		setInstanceUpdateDate(owlModel, ins);
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setOldValue(DatabaseUtil.setObject(oldIdo));
		v.setNewValue(DatabaseUtil.setObject(newIdo));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(ProtegeWorkbenchUtility.getDate(""+defIns.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED))));
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
		return getConceptDefinition(owlModel, conceptObject.getName());
	}
	
	public DefinitionObject addDefinitionExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject ido,ConceptObject conceptObject){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty hasDef = owlModel.getOWLProperty(ModelConstants.RHASDEFINITION);
		OWLProperty takenFromSource = owlModel.getOWLProperty(ModelConstants.RTAKENFROMSOURCE);
		OWLProperty hasSourceLink = owlModel.getOWLProperty(ModelConstants.RHASSOURCELINK);
		OWLIndividual defIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, ins, hasDef, ido.getIDUri());
		defIns.addPropertyValue(takenFromSource, ido.getIDSource());
		defIns.addPropertyValue(hasSourceLink, ido.getIDSourceURL());
		setInstanceUpdateDate(owlModel, defIns);
		setInstanceUpdateDate(owlModel, ins);
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setNewValue(DatabaseUtil.setObject(ido));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(Date.getROMEDate());
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
		return getConceptDefinition(owlModel, conceptObject.getName());
	}
	
	public DefinitionObject addDefinitionLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject transObj,IDObject ido,ConceptObject conceptObject){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty hasDef = owlModel.getOWLProperty(ModelConstants.RHASDEFINITION);
		OWLIndividual defIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, ins, hasDef, ido.getIDUri());
		defIns.addLabel(transObj.getLabel(),transObj.getLang());
		setInstanceUpdateDate(owlModel, defIns);
		setInstanceUpdateDate(owlModel, ins);
		
		if(ConfigConstants.ISINDEXING)
		{
			//update index
			IndexingEngineFactory.updateIndex(ontoInfo, defIns.getURI(), transObj.getLang(), SearchServiceImplOWLART.index_definition);
		}
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setNewValue(DatabaseUtil.setObject(transObj));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(Date.getROMEDate());
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
		
		return getConceptDefinition(owlModel, conceptObject.getName());
	}
	
	
	public DefinitionObject addDefinition(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject transObj,IDObject ido,ConceptObject conceptObject){
		
		long timeStamp = new java.util.Date().getTime();
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		
		String namespace = conceptObject.getNameSpace();
		String agrovocNamespacePrefix = owlModel.getNamespaceManager().getPrefix(namespace);
		String clsNamePrefix = agrovocNamespacePrefix+":";
		
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLNamedClass defCls = owlModel.getOWLNamedClass(ModelConstants.CDEFINITION);
		OWLProperty hasDef = owlModel.getOWLProperty(ModelConstants.RHASDEFINITION);
		OWLProperty takenFromSource = owlModel.getOWLProperty(ModelConstants.RTAKENFROMSOURCE);
		OWLProperty hasSourceLink = owlModel.getOWLProperty(ModelConstants.RHASSOURCELINK);
		OWLProperty createDate = owlModel.getOWLProperty(ModelConstants.RHASDATECREATED);
		
		OWLIndividual defIns = defCls.createOWLIndividual(clsNamePrefix+"i_"+"def_"+timeStamp);
		defIns.addLabel(transObj.getLabel(), transObj.getLang());
		defIns.addPropertyValue(createDate, Date.getDateTime());
		defIns.addPropertyValue(takenFromSource, ido.getIDSource());
		defIns.addPropertyValue(hasSourceLink, ido.getIDSourceURL());
		setInstanceUpdateDate(owlModel, defIns);
		
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		ins.addPropertyValue(hasDef, defIns);
		setInstanceUpdateDate(owlModel, ins);
		
		transObj.setUri(defIns.getURI());
		ido.setIDUri(defIns.getURI());
		ido.setIDName(defIns.getName());
		
		if(ConfigConstants.ISINDEXING)
		{
			//update index
			IndexingEngineFactory.updateIndex(ontoInfo, defIns.getURI(), transObj.getLang(), SearchServiceImplOWLART.index_definition);
		}
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setAction(actionId);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setNewValue(DatabaseUtil.setObject(ido));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(Date.getROMEDate());
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
		return getConceptDefinition(owlModel, conceptObject.getName());
	}
	
	public DefinitionObject deleteDefinition(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject ido,ConceptObject conceptObject){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLProperty hasDef = owlModel.getOWLProperty(ModelConstants.RHASDEFINITION);
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLIndividual defIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, ins, hasDef, ido.getIDUri());
		
		
		if(ConfigConstants.ISINDEXING)
		{
			//update index
			IndexingEngineFactory.deleteIndex(ontoInfo, defIns.getURI(), SearchServiceImplOWLART.index_definition);
		}
		
		defIns.delete();
		setInstanceUpdateDate(owlModel, defIns);
		setInstanceUpdateDate(owlModel, ins);

		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOldValue(DatabaseUtil.setObject(ido));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(ido.getIDDateCreate());
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
		return getConceptDefinition(owlModel, conceptObject.getName());
	}
	
	public ImageObject deleteImageLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject oldTransObj,ConceptObject conceptObject){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty hasImg = owlModel.getOWLProperty(ModelConstants.RHASIMAGE);
		OWLIndividual imgIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, ins, hasImg, oldTransObj.getUri());
		RDFSLiteral commentValue = owlModel.createRDFSLiteral(oldTransObj.getDescription(), oldTransObj.getLang());
		RDFProperty comment = owlModel.getRDFProperty(RDFSNames.Slot.COMMENT);
		imgIns.removeLabel(oldTransObj.getLabel(), oldTransObj.getLang());
		imgIns.removePropertyValue(comment, commentValue);
		
		setInstanceUpdateDate(owlModel, imgIns);
		setInstanceUpdateDate(owlModel, ins);
		
		if(ConfigConstants.ISINDEXING)
		{
			//update index
			IndexingEngineFactory.updateCommentIndex(ontoInfo, imgIns.getURI(), oldTransObj.getLang(), SearchServiceImplOWLART.index_ImgDescription);
		}
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setAction(actionId);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setOldValue(DatabaseUtil.setObject(oldTransObj));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(ProtegeWorkbenchUtility.getDate(""+imgIns.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED))));
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
		return getConceptImage(owlModel, conceptObject.getName());
	}
	
	public ImageObject editImageLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject oldTransObj,TranslationObject newTransObj,ConceptObject conceptObject){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty hasImg = owlModel.getOWLProperty(ModelConstants.RHASIMAGE);
		OWLIndividual imgIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, ins, hasImg, oldTransObj.getUri());
		
		RDFSLiteral oldCommentValue = owlModel.createRDFSLiteral(oldTransObj.getDescription(), oldTransObj.getLang());
		RDFSLiteral newCommentValue = owlModel.createRDFSLiteral(newTransObj.getDescription(), newTransObj.getLang());
		RDFProperty comment = owlModel.getRDFProperty(RDFSNames.Slot.COMMENT);
		
		imgIns.removeLabel(oldTransObj.getLabel(), oldTransObj.getLang());
		imgIns.removePropertyValue(comment, oldCommentValue);
		
		imgIns.addLabel(newTransObj.getLabel(), newTransObj.getLang());
		imgIns.addPropertyValue(comment, newCommentValue);
		
		setInstanceUpdateDate(owlModel, imgIns);
		setInstanceUpdateDate(owlModel, ins);
		

		if(ConfigConstants.ISINDEXING)
		{
			//update index
			IndexingEngineFactory.updateIndex(ontoInfo, imgIns.getURI(), newTransObj.getLang(), SearchServiceImplOWLART.index_ImgDescription);
		}
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOldValue(DatabaseUtil.setObject(oldTransObj));
		v.setNewValue(DatabaseUtil.setObject(newTransObj));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(ProtegeWorkbenchUtility.getDate(""+imgIns.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED))));
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
		return getConceptImage(owlModel, conceptObject.getName());
	}
	
	
	public ImageObject deleteImageExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject oldIdo,ConceptObject conceptObject){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty hasImg = owlModel.getOWLProperty(ModelConstants.RHASIMAGE);
		OWLProperty hasImageSource = owlModel.getOWLProperty(ModelConstants.RHASIMAGESOURCE);
		OWLProperty hasImageLink = owlModel.getOWLProperty(ModelConstants.RHASIMAGELINK);
		OWLIndividual imgIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, ins, hasImg, oldIdo.getIDUri());
		imgIns.removePropertyValue(hasImageSource, oldIdo.getIDSource());
		imgIns.removePropertyValue(hasImageLink, oldIdo.getIDSourceURL());
		setInstanceUpdateDate(owlModel, imgIns);
		setInstanceUpdateDate(owlModel, ins);
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setAction(actionId);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setOldValue(DatabaseUtil.setObject(oldIdo));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(ProtegeWorkbenchUtility.getDate(""+imgIns.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED))));
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
		return getConceptImage(owlModel, conceptObject.getName());
	}
	
	public ImageObject editImageExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject oldIdo,IDObject newIdo,ConceptObject conceptObject){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty hasImg = owlModel.getOWLProperty(ModelConstants.RHASIMAGE);
		OWLProperty hasImageSource = owlModel.getOWLProperty(ModelConstants.RHASIMAGESOURCE);
		OWLProperty hasImageLink = owlModel.getOWLProperty(ModelConstants.RHASIMAGELINK);
		OWLIndividual imgIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, ins, hasImg, oldIdo.getIDUri());
		imgIns.setPropertyValue(hasImageSource, newIdo.getIDSource());
		imgIns.setPropertyValue(hasImageLink, newIdo.getIDSourceURL());
		setInstanceUpdateDate(owlModel, imgIns);
		setInstanceUpdateDate(owlModel, ins);
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setAction(actionId);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setOldValue(DatabaseUtil.setObject(oldIdo));
		v.setNewValue(DatabaseUtil.setObject(newIdo));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(ProtegeWorkbenchUtility.getDate(""+imgIns.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED))));
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
		return getConceptImage(owlModel, conceptObject.getName());
	}
	
	public ImageObject addImageExternalSource(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject ido,ConceptObject conceptObject){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty hasImg = owlModel.getOWLProperty(ModelConstants.RHASIMAGE);
		OWLProperty hasImageSource = owlModel.getOWLProperty(ModelConstants.RHASIMAGESOURCE);
		OWLProperty hasImageLink = owlModel.getOWLProperty(ModelConstants.RHASIMAGELINK);
		OWLIndividual imgIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, ins, hasImg, ido.getIDUri());
		imgIns.addPropertyValue(hasImageSource, ido.getIDSource());
		imgIns.addPropertyValue(hasImageLink, ido.getIDSourceURL());
		setInstanceUpdateDate(owlModel, imgIns);
		setInstanceUpdateDate(owlModel, ins);
		
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setNewValue(DatabaseUtil.setObject(ido));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(Date.getROMEDate());
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
		return getConceptImage(owlModel, conceptObject.getName());
	}
	
	public ImageObject addImageLabel(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject transObj,IDObject ido,ConceptObject conceptObject){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty hasImg = owlModel.getOWLProperty(ModelConstants.RHASIMAGE);
		OWLIndividual imgIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, ins, hasImg, ido.getIDUri());
		imgIns.addLabel(transObj.getLabel(),transObj.getLang());
		
		RDFSLiteral commentValue = owlModel.createRDFSLiteral(transObj.getDescription(), transObj.getLang());
		RDFProperty comment = owlModel.getRDFProperty(RDFSNames.Slot.COMMENT);
		imgIns.addPropertyValue(comment, commentValue);
		setInstanceUpdateDate(owlModel, imgIns);
		setInstanceUpdateDate(owlModel, ins);
		
		if(ConfigConstants.ISINDEXING)
		{
			//update index
			IndexingEngineFactory.updateCommentIndex(ontoInfo, imgIns.getURI(), transObj.getLang(), SearchServiceImplOWLART.index_ImgDescription);
		}
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setAction(actionId);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setNewValue(DatabaseUtil.setObject(transObj));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(Date.getROMEDate());
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
		return getConceptImage(owlModel, conceptObject.getName());
		
	}
	
	public ImageObject addImage(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,TranslationObject transObj,IDObject ido,ConceptObject conceptObject){
		long timeStamp = new java.util.Date().getTime();
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		
		String namespace = conceptObject.getNameSpace();
		String agrovocNamespacePrefix = owlModel.getNamespaceManager().getPrefix(namespace);
		String clsNamePrefix = agrovocNamespacePrefix+":";
		
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLNamedClass defCls = owlModel.getOWLNamedClass(ModelConstants.CIMAGE);
		OWLProperty hasImg = owlModel.getOWLProperty(ModelConstants.RHASIMAGE);
		OWLProperty createDate = owlModel.getOWLProperty(ModelConstants.RHASDATECREATED);
		OWLProperty updateDate = owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE);
		OWLProperty hasImageSource = owlModel.getOWLProperty(ModelConstants.RHASIMAGESOURCE);
		OWLProperty hasImageLink = owlModel.getOWLProperty(ModelConstants.RHASIMAGELINK);
		
		OWLIndividual imgIns = defCls.createOWLIndividual(clsNamePrefix+"i_"+"img_"+timeStamp);
		imgIns.addPropertyValue(createDate, Date.getDateTime());
		imgIns.addPropertyValue(updateDate, Date.getDateTime());
		imgIns.addLabel(transObj.getLabel(), transObj.getLang());
		imgIns.addPropertyValue(hasImageSource, ido.getIDSource());
		imgIns.addPropertyValue(hasImageLink, ido.getIDSourceURL());
		
		
		RDFSLiteral commentValue = owlModel.createRDFSLiteral(transObj.getDescription(), transObj.getLang());
		RDFProperty comment = owlModel.getRDFProperty(RDFSNames.Slot.COMMENT);
		
		imgIns.addPropertyValue(comment, commentValue);
		setInstanceUpdateDate(owlModel, imgIns);
		
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		ins.addPropertyValue(hasImg, imgIns);
		setInstanceUpdateDate(owlModel, ins);
		
		transObj.setUri(imgIns.getURI());
		ido.setIDUri(imgIns.getURI());
		ido.setIDName(imgIns.getName());
		
		if(ConfigConstants.ISINDEXING)
		{
			//update index
			IndexingEngineFactory.updateCommentIndex(ontoInfo, imgIns.getURI(), transObj.getLang(), SearchServiceImplOWLART.index_ImgDescription);
		}
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setAction(actionId);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setNewValue(DatabaseUtil.setObject(ido));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(Date.getROMEDate());
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
		return getConceptImage(owlModel, conceptObject.getName());
		
	}
	
	public ImageObject deleteImage(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,IDObject ido,ConceptObject conceptObject){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLProperty hasImg = owlModel.getOWLProperty(ModelConstants.RHASIMAGE);
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLIndividual imgIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, ins, hasImg, ido.getIDUri());
		
		if(ConfigConstants.ISINDEXING)
		{
			//update index
			IndexingEngineFactory.deleteIndex(ontoInfo, imgIns.getURI(), SearchServiceImplOWLART.index_ImgDescription);
		}
		
		imgIns.delete();
		setInstanceUpdateDate(owlModel, imgIns);
		setInstanceUpdateDate(owlModel, ins);
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setAction(actionId);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setOldValue(DatabaseUtil.setObject(ido));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(ido.getIDDateCreate());
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
		return getConceptImage(owlModel, conceptObject.getName());
	}
	
	public void moveTerm(OntologyInfo ontoInfo ,int actionId, OwlStatus status, int userId, TermObject termObject, TermMoveObject termMoveObject){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		
		OWLProperty lexicon = owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION);
		
		OWLNamedClass oldCls = owlModel.getOWLNamedClass(owlModel.getResourceNameForURI(termMoveObject.getOldConceptURI()));
		OWLIndividual oldIns = ProtegeWorkbenchUtility.getConceptInstance(owlModel, oldCls);
		
		OWLNamedClass newCls = owlModel.getOWLNamedClass(owlModel.getResourceNameForURI(termMoveObject.getNewConceptURI()));
		OWLIndividual newIns = ProtegeWorkbenchUtility.getConceptInstance(owlModel, newCls);
		
		
		HashMap<String, TermRelationshipObject> termRelList = termMoveObject.getTermRelList();
		
		for(String termURI: termRelList.keySet())
		{
			OWLIndividual termIns = owlModel.getOWLIndividual(owlModel.getResourceNameForURI(termURI));
			
			oldIns.removePropertyValue(lexicon, termIns);
			setInstanceUpdateDate(owlModel, oldIns);
			
			newIns.addPropertyValue(lexicon, termIns);
			setInstanceUpdateDate(owlModel, newIns);
			
			TermRelationshipObject trObj = termRelList.get(termURI);
			HashMap<RelationshipObject, ArrayList<TermObject>> trlist = trObj.getResult();
			for(RelationshipObject rObj : trlist.keySet())
			{
				OWLProperty relatedTerm = owlModel.getOWLProperty(owlModel.getResourceNameForURI(rObj.getUri()));
				ArrayList<TermObject> tObjList = trlist.get(rObj);
				for(TermObject destTermObj: tObjList)
				{
					OWLIndividual destTermIns = owlModel.getOWLIndividual(owlModel.getResourceNameForURI(destTermObj.getUri()));
					termIns.removePropertyValue(relatedTerm, destTermIns);
				}
			}
		}
		
		///owlModel.dispose();
	}
	
	public TermMoveObject loadMoveTerm(OntologyInfo ontoInfo, String termURI, String conceptURI){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(owlModel.getResourceNameForURI(conceptURI));
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty lexicon = owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION);
		OWLProperty codeAgrovoc = owlModel.getOWLProperty(ModelConstants.RHASCODEAGROVOC);
		OWLIndividual termIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, ins, lexicon, termURI);
		///owlModel.dispose();
		return getAGROVOCCodeTerm(owlModel, cls.getName(), ""+termIns.getPropertyValue(codeAgrovoc));
	}
	
	public ConceptTermObject deleteTerm(OntologyInfo ontoInfo ,int actionId, OwlStatus status,int userId,TermObject oldObject,ConceptObject conceptObject){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty lexicon = owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION);
		OWLProperty statusProp = owlModel.getOWLProperty(ModelConstants.RHASSTATUS);
		OWLIndividual termIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, ins, lexicon, oldObject.getUri());
		termIns.setPropertyValue(statusProp, status.getStatus());
		setInstanceUpdateDate(owlModel, termIns);
		setInstanceUpdateDate(owlModel, ins);
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setOldStatus(getConceptStatus(oldObject.getStatus()));
		v.setDateCreate(Date.getROMEDate());
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOldValue(DatabaseUtil.setObject(oldObject));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(ProtegeWorkbenchUtility.getDate(""+termIns.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED))));
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
		return getTerm(owlModel, conceptObject.getName());
	}
	
	public ConceptTermObject editTerm(OntologyInfo ontoInfo ,int actionId, OwlStatus status, int userId, TermObject oldObject, TermObject newObject, ConceptObject conceptObject){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty lexicon = owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION);
		OWLProperty mainlabel = owlModel.getOWLProperty(ModelConstants.RISMAINLABEL);
		OWLProperty statusProp = owlModel.getOWLProperty(ModelConstants.RHASSTATUS);
		OWLIndividual term = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, ins, lexicon, oldObject.getUri());
		
		if(!newObject.getLabel().equals(oldObject.getLabel()) || !newObject.getLang().equals(oldObject.getLang()))
		{
			term.removeLabel(oldObject.getLabel(), oldObject.getLang());
			term.addLabel(newObject.getLabel(), newObject.getLang());
		}
		
		if(newObject.isMainLabel()!=oldObject.isMainLabel())
			term.setPropertyValue(mainlabel, newObject.isMainLabel());
		term.setPropertyValue(statusProp, status.getStatus());
		
		/*if(newObject.isMainLabel()){
			// Set mainlabel(false) to the other term
			Collection<?> co = ins.getPropertyValues(lexicon);
			for (Iterator<?> iter = co.iterator(); iter.hasNext();) {
				Object obj = iter.next();
				if (obj instanceof OWLIndividual) {
					OWLIndividual termIns = (OWLIndividual) obj;
					TermObject tObj = ProtegeWorkbenchUtility.makeTermObject(owlModel, termIns, cls);
					if(!termIns.getURI().equals(term.getURI()) && tObj.isMainLabel() && tObj.getLang().equals(newObject.getLang())){
						termIns.setPropertyValue(mainlabel, false);
					}
				}
			}
		}*/
		
		setInstanceUpdateDate(owlModel, term);
		setInstanceUpdateDate(owlModel, ins);
		
		if(ConfigConstants.ISINDEXING)
		{
			//update index
			IndexingEngineFactory.updateIndex(ontoInfo, term.getURI(), oldObject.getLang(), SearchServiceImplOWLART.c_nounInstancesIndexCategory);
			IndexingEngineFactory.updateIndex(ontoInfo, term.getURI(), newObject.getLang(), SearchServiceImplOWLART.c_nounInstancesIndexCategory);
		}
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOldStatus(getConceptStatus(oldObject.getStatus()));
		v.setOldValue(DatabaseUtil.setObject(oldObject));
		v.setNewValue(DatabaseUtil.setObject(newObject));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(ProtegeWorkbenchUtility.getDate(""+term.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED))));
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
		return getTerm(owlModel, conceptObject.getName());
		
	}
	
	public ConceptTermObject addTerm(OntologyInfo ontoInfo ,int actionId, OwlStatus status,int userId,TermObject newObject,ConceptObject conceptObject){
		long unique = new java.util.Date().getTime();
		
		ConceptTermObject ctObj = getTerm(conceptObject.getName(), ontoInfo);
		if(!ctObj.isEmpty()){
			HashMap<String, ArrayList<TermObject>> termList = ctObj.getTermList();
			ArrayList<String> termlanglist = new ArrayList<String>(termList.keySet());	
			for(String s : termlanglist){
				ArrayList<TermObject> terms = termList.get(s);
				for(TermObject t : terms)
				{
					if(newObject.getLabel().equals(t.getLabel()) && newObject.getLang().equals(t.getLang()))
					{
						return null;
					}
				}
			}
		}
		
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		String namespace = conceptObject.getNameSpace();
		String agrovocNamespacePrefix = owlModel.getNamespaceManager().getPrefix(namespace);
		String clsNamePrefix = agrovocNamespacePrefix+":";
		
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty lexicon = owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION);
		OWLProperty mainlabel = owlModel.getOWLProperty(ModelConstants.RISMAINLABEL);
		OWLProperty createDate = owlModel.getOWLProperty(ModelConstants.RHASDATECREATED);
		OWLProperty statusProp = owlModel.getOWLProperty(ModelConstants.RHASSTATUS);
		OWLProperty hasCodeAgrovoc = owlModel.getOWLProperty(ModelConstants.RHASCODEAGROVOC);
		
		OWLNamedClass nounCls = owlModel.getOWLNamedClass(ModelConstants.CNOUN);
		OWLIndividual term = (OWLIndividual) nounCls.createInstance(clsNamePrefix+"i_"+newObject.getLang().toLowerCase()+"_"+unique);
		term.addLabel(newObject.getLabel(), newObject.getLang());
		term.addPropertyValue(mainlabel, newObject.isMainLabel());
		term.addPropertyValue(createDate, Date.getDateTime());
		term.addPropertyValue(statusProp, status.getStatus());
		
		String codeAgrovoc = ""+unique;
		if(newObject.isMainLabel())
		{
			for (Iterator<?> iter2 = ins.getPropertyValues(lexicon).iterator(); iter2.hasNext();) {
				OWLIndividual termInstance = (OWLIndividual) iter2.next();
				Object mainLabel = termInstance.getPropertyValue(mainlabel);
				if(mainLabel != null && mainLabel.equals(true)){
					Object agrovocCode = termInstance.getPropertyValue(hasCodeAgrovoc);
					if(agrovocCode!=null && !agrovocCode.equals(""))
					{
						codeAgrovoc = ""+agrovocCode;
						break;
					}
				}
			}
			
		}
		term.addPropertyValue(hasCodeAgrovoc, codeAgrovoc);
		
		/*if(newObject.isMainLabel()){
			// Set mainlabel(false) to the other term
			Collection<?> co = ins.getPropertyValues(lexicon);
			for (Iterator<?> iter = co.iterator(); iter.hasNext();) {
				Object obj = iter.next();
				if (obj instanceof OWLIndividual) {
					OWLIndividual termIns = (OWLIndividual) obj;
					TermObject tObj = ProtegeWorkbenchUtility.makeTermObject(owlModel, termIns, cls);
					if(tObj.isMainLabel() && tObj.getLang().equals(newObject.getLang())){
						termIns.setPropertyValue(mainlabel, false);
					}
				}
			}
		}*/
		
		setInstanceUpdateDate(owlModel, term);
		
		ins.addPropertyValue(lexicon, term);
		setInstanceUpdateDate(owlModel, ins);
		
		newObject.setUri(term.getURI());
		newObject.setName(term.getName());
		newObject.setStatus(status.getStatus());
		newObject.setStatusID(status.getId());

		if(ConfigConstants.ISINDEXING)
		{
			//update index
			IndexingEngineFactory.updateIndex(ontoInfo, term.getURI(), newObject.getLang(), SearchServiceImplOWLART.c_nounInstancesIndexCategory);
		}
		
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setNewValue(DatabaseUtil.setObject(newObject));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(Date.getROMEDate());
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
		return getTerm(owlModel, conceptObject.getName());
	}

	/** Get information tab panel : create date , update date , status */
	public InformationObject getConceptInformation(String cls, OntologyInfo ontoInfo) {
		logger.info("getConceptInformation(" + cls + ", " + ontoInfo + ")");
		InformationObject infoObj = new InformationObject();
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		infoObj = getConceptInformation(owlModel, cls, ontoInfo, 1);
		///owlModel.dispose();
		return infoObj;
	}
	
	private InformationObject getConceptInformation(OWLModel owlModel, String cls, OntologyInfo ontoInfo, int type) {
		InformationObject infoObj = new InformationObject();
		OWLNamedClass owlCls = owlModel.getOWLNamedClass(cls);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlCls);
		OWLProperty cDate = owlModel.getOWLProperty(ModelConstants.RHASDATECREATED);
		OWLProperty uDate = owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE);
		OWLProperty stat = owlModel.getOWLProperty(ModelConstants.RHASSTATUS);
		
		Object createDate = individual.getPropertyValueLiteral(cDate);
		Object updateDate = individual.getPropertyValueLiteral(uDate);
		Object status = individual.getPropertyValueLiteral(stat);
		
		if(createDate != null){
			infoObj.setCreateDate(createDate.toString());
		}
		if(updateDate != null){
			infoObj.setUpdateDate(updateDate.toString());
		}
		if(status != null){
			infoObj.setStatus(status.toString());
		}	
		
		infoObj.setRecentChangesInitObject(getConceptHistoryInitData(cls, ontoInfo.getOntologyId(), type));
		return infoObj;
	}
	
	public HierarchyObject getConceptHierarchy(OntologyInfo ontologyInfo, String uri, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList)
	{
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontologyInfo);
		
		HashMap<String, ArrayList<TreeObject>> broaderList = new HashMap<String, ArrayList<TreeObject>>();
		ArrayList<TreeObject> narrowerList = new ArrayList<TreeObject>();
		
		OWLNamedClass rootCls = owlModel.getOWLNamedClass(owlModel.getResourceNameForURI(uri));
		TreeObject treeObj = ProtegeWorkbenchUtility.makeTreeObject(owlModel, rootCls, showAlsoNonpreferredTerms, isHideDeprecated, langList);
		getBroaderHierarchy(owlModel, rootCls, showAlsoNonpreferredTerms, isHideDeprecated, langList, broaderList); 
		narrowerList = getNarrowerHierarchy(owlModel, rootCls, showAlsoNonpreferredTerms, isHideDeprecated, langList); 

		HierarchyObject hObj = new HierarchyObject();
		hObj.setBroaderList(broaderList);
		hObj.setSelectedConcept(treeObj);
		hObj.setNarrowerList(narrowerList);
		
		return hObj;
	}
	
	public ArrayList<TreeObject> getParentConceptList(OWLNamedClass rootCls, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList)
	{
		ArrayList<TreeObject> list = new ArrayList<TreeObject>();
		for(Iterator<?> it = rootCls.getSuperclasses(false).iterator(); it.hasNext();) {
			OWLNamedClass cls = (OWLNamedClass) it.next();
			TreeObject treeObj = ProtegeWorkbenchUtility.makeTreeObject(rootCls.getOWLModel(), cls, showAlsoNonpreferredTerms, isHideDeprecated, langList);
			if(treeObj!=null)
			{
				treeObj.setHasChild(hasParentConcept(cls));
				list.add(treeObj);
			}
		}
		return list;
	}
	
	
	public void getBroaderHierarchy(OWLModel owlModel, OWLNamedClass rootCls, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList, HashMap<String, ArrayList<TreeObject>> treeObjList){

	    if(rootCls !=null){
	    	ArrayList<TreeObject> list = getParentConceptList(rootCls, showAlsoNonpreferredTerms,  isHideDeprecated, langList);
			treeObjList.put(rootCls.getURI(), list);
			for(TreeObject tObj: list)
			{
				OWLNamedClass cls1 = owlModel.getOWLNamedClass(tObj.getName());
				getBroaderHierarchy(owlModel, cls1, showAlsoNonpreferredTerms, isHideDeprecated, langList, treeObjList);
			}
			
		}
	}
	
	public boolean hasParentConcept(OWLNamedClass cls)
	{
		int cnt = 0;
		for(Iterator<?> it = cls.getSuperclasses(false).iterator(); it.hasNext();) {
			OWLNamedClass superCls = (OWLNamedClass) it.next();
			if(superCls.getName().equals(ModelConstants.CDOMAINCONCEPT))
				return false;
			cnt++;
		}
		
		return cnt>1;
	}
	
	public ArrayList<TreeObject> getNarrowerHierarchy(OWLModel owlModel, OWLNamedClass rootCls, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList){

		ArrayList<TreeObject> list = new ArrayList<TreeObject>();
	    if(rootCls !=null){
			for(Iterator<?> it = rootCls.getSubclasses(false).iterator(); it.hasNext();) {
				OWLNamedClass cls = (OWLNamedClass) it.next();
				TreeObject treeObj = ProtegeWorkbenchUtility.makeTreeObject(owlModel, cls, showAlsoNonpreferredTerms, isHideDeprecated, langList);
				if(treeObj!=null)
				{
					list.add(treeObj);
				}
			}
			
		}
	    return list ;
	}
	
	/*public ArrayList<NonFuncObject> getNonFuncValue(String cls,String prop, OntologyInfo ontoInfo){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		ArrayList<NonFuncObject> list = getNonFuncValue(owlModel, cls, prop);		
		///owlModel.dispose();
		return list;
	}
	
	private ArrayList<NonFuncObject> getNonFuncValue(OWLModel owlModel, String cls,String prop){
	    ArrayList<NonFuncObject> list = new ArrayList<NonFuncObject>();
	    OWLNamedClass owlCls = owlModel.getOWLNamedClass(cls);
	    OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlCls);
	    OWLProperty nProp = owlModel.getOWLProperty(prop);
	    
	    for (Iterator<?> iter = individual.getPropertyValues(nProp).iterator(); iter.hasNext();) {
	        RDFSLiteral element = (RDFSLiteral) iter.next();
	        NonFuncObject nObj = new NonFuncObject();
	        nObj.setValue(element.getString());
	        nObj.setLanguage(element.getLanguage());
	        list.add(nObj);
	    }	    
	    return list;
	}*/
	
	public HashMap<RelationshipObject, ArrayList<NonFuncObject>> getPropertyValue(String cls, String prop, OntologyInfo ontoInfo){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		HashMap<RelationshipObject, ArrayList<NonFuncObject>> list = getPropertyValue(owlModel, prop, cls);		
		///owlModel.dispose();
		return list;
	}
	
	private HashMap<RelationshipObject, ArrayList<NonFuncObject>> getPropertyValue(OWLModel owlModel, String property, String cls){
		HashMap<RelationshipObject, ArrayList<NonFuncObject>> list = new HashMap<RelationshipObject, ArrayList<NonFuncObject>>();
	    OWLNamedClass owlCls = owlModel.getOWLNamedClass(cls);
	    OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlCls);
	    OWLProperty nProp = owlModel.getOWLProperty(property);
	    
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
		    for (Iterator<?> iter = individual.getPropertyValues(prop).iterator(); iter.hasNext();) 
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
	    return list;
	}
	
	private int getPropertyValueCount(OWLModel owlModel, String property, String cls){
		int count = 0;
		OWLNamedClass owlCls = owlModel.getOWLNamedClass(cls);
	    OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlCls);
	    OWLProperty nProp = owlModel.getOWLProperty(property);
	    List<OWLProperty> c = new ArrayList<OWLProperty>(nProp.getSubproperties(true));
	    c.add(nProp);
	    for (Iterator<?> itr = c.iterator(); itr.hasNext();) {
	    	OWLProperty prop = (OWLProperty) itr.next();
		    count += individual.getPropertyValues(prop).size(); 
	    }
	    return count;
	}

	public ImageObject getConceptImage(String cls, OntologyInfo ontoInfo) {
		logger.info("getConceptImage(" + cls + ")");
		ImageObject imgObj = new ImageObject();
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		imgObj = getConceptImage(owlModel, cls);
		///owlModel.dispose();
		return imgObj;
	}
	
	private ImageObject getConceptImage(OWLModel owlModel, String cls){
		
		ImageObject imgObj = new ImageObject();
		OWLNamedClass owlCls = owlModel.getOWLNamedClass(cls);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlCls);
		OWLProperty hasImage = owlModel.getOWLProperty(ModelConstants.RHASIMAGE);
		
		for (Iterator<?> iter = individual.getPropertyValues(hasImage).iterator(); iter.hasNext();) {
			OWLIndividual imgInstance = (OWLIndividual) iter.next();
			
			Object createDate = imgInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED));
			Object updateDate = imgInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE));
			Object source = imgInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASIMAGESOURCE));
			Object link = imgInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASIMAGELINK));
			
			IDObject idObj = new IDObject();
			try {
				if(createDate !=null && updateDate !=null){
					idObj.setIDDateCreate(ProtegeWorkbenchUtility.getDate(""+createDate));
					idObj.setIDDateModified(ProtegeWorkbenchUtility.getDate(""+updateDate));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(source!=null && link!=null){
				idObj.setIDSource(source.toString());
				idObj.setIDSourceURL(link.toString());
			}
			idObj.setIDType(IDObject.IMAGE);
			idObj.setIDUri(imgInstance.getURI());
			idObj.setIDName(imgInstance.getName());
			
			for (Iterator<?> iterator = imgInstance.getLabels().iterator(); iterator.hasNext();) {

				Object obj = iterator.next();
				TranslationObject tObj = new TranslationObject();
    	    	if (obj instanceof DefaultRDFSLiteral) {
    	    		DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
    	    		tObj.setLabel(element.getString());
    	    		tObj.setLang(element.getLanguage());
    	    		tObj.setType(TranslationObject.IMAGETRANSLATION);
    	    		tObj.setUri(imgInstance.getURI());
    	    		idObj.addIDTranslationList(tObj);
    			}   	    	
    	    		
				for (Iterator<?> it = imgInstance.getComments().iterator(); it.hasNext();) {
					Object obj1 = it.next();
			    	if (obj1 instanceof DefaultRDFSLiteral) {
			    		DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj1;
			    		LabelObject label = new LabelObject();
			    		label.setLabel(element.getString());
			    		label.setLanguage(element.getLanguage());
			    		if(tObj.getLang().equals(element.getLanguage())){
			    			tObj.setDescription(element.getString());
			    		}
					}		
				}
			}
			imgObj.addImageList(imgInstance.getURI(), idObj);
		}
		return imgObj;
	}

	public DefinitionObject getConceptDefinition(String cls, OntologyInfo ontoInfo) {
		logger.info("getConceptDefinition(" + cls + ")");
		DefinitionObject dObj = new DefinitionObject();
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		dObj = getConceptDefinition(owlModel, cls);
		///owlModel.dispose();
		return dObj;
	}
	
	private DefinitionObject getConceptDefinition(OWLModel owlModel, String cls){
		DefinitionObject dObj = new DefinitionObject();
		OWLNamedClass owlCls = owlModel.getOWLNamedClass(cls);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlCls);
		
		OWLProperty hasDef = owlModel.getOWLProperty(ModelConstants.RHASDEFINITION);
		
		for (Iterator<?> iter = individual.getPropertyValues(hasDef).iterator(); iter.hasNext();) {
			OWLIndividual defInstance = (OWLIndividual) iter.next();
			
			Object createDate = defInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED));
			Object updateDate = defInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE));
			Object source = defInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RTAKENFROMSOURCE));
			Object link = defInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASSOURCELINK));
			
			IDObject idObj = new IDObject();
			try {
				idObj.setIDDateCreate(ProtegeWorkbenchUtility.getDate(""+createDate));
				idObj.setIDDateModified(ProtegeWorkbenchUtility.getDate(""+updateDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(source!=null && link!=null){
				idObj.setIDSource(source.toString());
				idObj.setIDSourceURL(link.toString());
			}
			idObj.setIDType(IDObject.DEFINITION);
			idObj.setIDUri(defInstance.getURI());
			idObj.setIDName(defInstance.getName());
			
			for (Iterator<?> iterator = defInstance.getLabels().iterator(); iterator.hasNext();) {
				Object obj = iterator.next();
				TranslationObject tObj = new TranslationObject();
    	    	if (obj instanceof DefaultRDFSLiteral) {
    	    		DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
    	    		tObj.setLabel(element.getString());
    	    		tObj.setLang(element.getLanguage());
    	    		tObj.setType(TranslationObject.DEFINITIONTRANSLATION);
    	    		tObj.setUri(defInstance.getURI());
    	    		idObj.addIDTranslationList(tObj);
    			}else{
    				tObj.setLabel(obj.toString());
    				tObj.setUri(defInstance.getURI());
    				idObj.addIDTranslationList(tObj);
    			}
				
			}
			dObj.addDefinitionList(defInstance.getURI(), idObj);
		}
		return dObj;
	}
	
	private int getConceptPropertyCount(OWLModel owlModel, ArrayList<String> langList, String cls){
		int count = 0;
		OWLNamedClass owlCls = owlModel.getOWLNamedClass(cls);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlCls);
		for (Iterator<?> iter = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION)).iterator(); iter.hasNext();) {
			OWLIndividual termInstance = (OWLIndividual) iter.next();
    		for (Iterator<?> iterator = termInstance.getLabels().iterator(); iterator.hasNext();) {
    			Object obj = iterator.next();
    	    	if (obj instanceof DefaultRDFSLiteral) {
    	    		DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
    	    		if(langList.contains(element.getLanguage().toLowerCase()))
    	    			count++;
    			}
			}
		}
		return count;
	}
	
	private int getPropertyCount(OWLModel owlModel, String property, String cls){
		
		OWLNamedClass owlCls = owlModel.getOWLNamedClass(cls);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlCls);
		return individual.getPropertyValues(owlModel.getOWLProperty(property)).size();
	}

	public ConceptTermObject getTerm(String cls, OntologyInfo ontoInfo) {
		logger.info("getTerm(" + cls + ")");
		ConceptTermObject ctObj = new ConceptTermObject();
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		ctObj = getTerm(owlModel, cls);
		///owlModel.dispose();
		return ctObj;
	}
	
	private TermMoveObject getAGROVOCCodeTerm(OWLModel owlModel, String cls, String codeAgrovoc){
		TermMoveObject tmObj = new TermMoveObject();
		OWLNamedClass owlCls = owlModel.getOWLNamedClass(cls);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlCls);
		TermServiceImpl termServiceImpl = new TermServiceImpl();

		for (Iterator<?> iter = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION)).iterator(); iter.hasNext();) {
			OWLIndividual termInstance = (OWLIndividual) iter.next();
			
			String code  = ""+termInstance.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASCODEAGROVOC));
			if(code.equals(codeAgrovoc))
			{
				Object mainLabel = termInstance.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RISMAINLABEL));
				
				TermObject termObject = new TermObject();
				termObject.setUri(termInstance.getURI());
				termObject.setName(termInstance.getName());		
				termObject.setConceptUri(owlCls.getURI());
				termObject.setConceptName(owlCls.getName());
				
				Object createDate = termInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED));
				Object updateDate = termInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE));
				Object status = termInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASSTATUS));
				if(status !=null){
					termObject.setStatus(status.toString());
				}
				if(createDate != null && updateDate != null){
					try {
						termObject.setDateCreate(ProtegeWorkbenchUtility.getDate(""+createDate));
						termObject.setDateModified(ProtegeWorkbenchUtility.getDate(""+updateDate));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				if(mainLabel != null){
					termObject.setMainLabel(Boolean.valueOf(mainLabel.toString()));
				}
	    		Collection<?> labelList = termInstance.getLabels();
	    		for (Iterator<?> iterator = labelList.iterator(); iterator.hasNext();) {
	    			Object obj = iterator.next();
	    	    	if (obj instanceof DefaultRDFSLiteral) {
	    	    		DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
	    	    		termObject.setLabel(element.getString());
	    	    		termObject.setLang(element.getLanguage().toLowerCase());
	    			}
				}
	    		tmObj.addTermList(termObject.getLang(), termObject);
	    		
	    		
	    		tmObj.addTermRelationship(termObject.getUri(), termServiceImpl.getTermRelationship(owlModel, cls, termInstance.getName()));
			}
		}
		return tmObj;
	}
	
	private ConceptTermObject getTerm(OWLModel owlModel, String cls){
		ConceptTermObject ctObj = new ConceptTermObject();
		OWLNamedClass owlCls = owlModel.getOWLNamedClass(cls);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlCls);

		for (Iterator<?> iter = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION)).iterator(); iter.hasNext();) {
			OWLIndividual termInstance = (OWLIndividual) iter.next();
			Object mainLabel = termInstance.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RISMAINLABEL));
			
			TermObject termObject = new TermObject();
			termObject.setUri(termInstance.getURI());
			termObject.setName(termInstance.getName());		
			termObject.setConceptUri(owlCls.getURI());
			termObject.setConceptName(owlCls.getName());
			
			Object createDate = termInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED));
			Object updateDate = termInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE));
			Object status = termInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASSTATUS));
			if(status !=null){
				termObject.setStatus(status.toString());
			}
			if(createDate != null && updateDate != null){
				try {
					termObject.setDateCreate(ProtegeWorkbenchUtility.getDate(""+createDate));
					termObject.setDateModified(ProtegeWorkbenchUtility.getDate(""+updateDate));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if(mainLabel != null){
				termObject.setMainLabel(Boolean.valueOf(mainLabel.toString()));
			}
    		Collection<?> labelList = termInstance.getLabels();
    		for (Iterator<?> iterator = labelList.iterator(); iterator.hasNext();) {
    			Object obj = iterator.next();
    	    	if (obj instanceof DefaultRDFSLiteral) {
    	    		DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
    	    		termObject.setLabel(element.getString());
    	    		termObject.setLang(element.getLanguage().toLowerCase());
    			}
			}
    		ctObj.addTermList(termObject.getLang(), termObject);
		}
		return ctObj;
	}

	public ConceptMappedObject getMappedConcept(String cls, OntologyInfo ontoInfo) {
		logger.info("getMappedConcept(" + cls + ")");
		ConceptMappedObject cMap = new ConceptMappedObject();
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		cMap = getMappedConcept(owlModel, cls);
		///owlModel.dispose();
		return cMap;
	}
	private ConceptMappedObject getMappedConcept(OWLModel owlModel, String cls){
		ConceptMappedObject cMap = new ConceptMappedObject();
		OWLNamedClass owlCls = owlModel.getOWLNamedClass(cls);
		OWLProperty mapProp = owlModel.getOWLProperty(ModelConstants.RHASMAPPEDDOMAINCONCEPT);
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlCls);
		
		Collection<?> co = ins.getPropertyValues(mapProp);
		for (Iterator<?> iter = co.iterator(); iter.hasNext();) {
			Object obj = iter.next();
			if (obj instanceof OWLIndividual) {
				OWLIndividual destConceptIns = (OWLIndividual) obj;
				ConceptObject cObj = ProtegeWorkbenchUtility.makeConceptObject(owlModel, (OWLNamedClass)destConceptIns.getProtegeType());
				cMap.addConceptList(cObj);
			}
		}
		return cMap;
	}

	public Collection<OWLObjectProperty> getAllObjectPropperties(OWLModel owlModel, OWLObjectProperty rootProp) {
		logger.info("getAllObjectPropperties(" + rootProp + ")");
		Collection<OWLObjectProperty> result = new ArrayList<OWLObjectProperty>();
		RDFProperty subPropertyOf = owlModel.getRDFProperty(RDFSNames.Slot.SUB_PROPERTY_OF);
		Collection<?> propList = owlModel.getRDFResourcesWithPropertyValue(subPropertyOf, rootProp);
		for (Iterator<?> iter = propList.iterator(); iter.hasNext();) {
			OWLObjectProperty prop = (OWLObjectProperty) iter.next();
			result.add(prop);
			getChildObjectProperty(prop, owlModel, result);
		}
		return result;
	}
	
	public void getChildObjectProperty(OWLObjectProperty rootProp, OWLModel owlModel, Collection<OWLObjectProperty> result) {
		logger.info("getChildObjectProperty(" + rootProp + ", " + result + ")");
		RDFProperty subClassOfProperty = owlModel.getRDFProperty(RDFSNames.Slot.SUB_PROPERTY_OF);
	    Collection<?> results = owlModel.getRDFResourcesWithPropertyValue(subClassOfProperty, rootProp);
	    
	    for (Iterator<?> iter = results.iterator(); iter.hasNext();) {
	    	OWLObjectProperty childProp = (OWLObjectProperty) iter.next();
	    	result.add(childProp);
	    	getChildObjectProperty(childProp, owlModel,result);
		}
	    
	}
	
	public Collection<String> getAllObjectPropertiesName(OWLModel owlModel, OWLObjectProperty rootProp) {
		logger.info("getAllObjectProppertiesName(" + rootProp + ")");
		Collection<String> result = new ArrayList<String>();
		RDFProperty subPropertyOf = owlModel.getRDFProperty(RDFSNames.Slot.SUB_PROPERTY_OF);
		Collection<?> propList = owlModel.getRDFResourcesWithPropertyValue(subPropertyOf, rootProp);
		for (Iterator<?> iter = propList.iterator(); iter.hasNext();) {
			OWLObjectProperty prop = (OWLObjectProperty) iter.next();
			result.add(prop.getName());
			getChildObjectPropertyName(prop, owlModel, result);
		}
		return result;
	}
	
	public void getChildObjectPropertyName(OWLObjectProperty rootProp, OWLModel owlModel, Collection<String> result) {
		logger.info("getChildObjectPropertyName(" + rootProp + ", " + result + ")");
		RDFProperty subClassOfProperty = owlModel.getRDFProperty(RDFSNames.Slot.SUB_PROPERTY_OF);
	    Collection<?> results = owlModel.getRDFResourcesWithPropertyValue(subClassOfProperty, rootProp);
	    
	    for (Iterator<?> iter = results.iterator(); iter.hasNext();) {
	    	OWLObjectProperty childProp = (OWLObjectProperty) iter.next();
	    	result.add(childProp.getName());
	    	getChildObjectPropertyName(childProp, owlModel,result);
		}
	    
	}
	
	
	public int getConceptRelationshipCount(OWLModel owlModel, String cls) {
		logger.info("getConceptRelationshipCount(" + cls + ")");
		int count = 0;
		OWLNamedClass owlCls = owlModel.getOWLNamedClass(cls);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlCls);
		
		OWLObjectProperty hasRelatedConcept = owlModel.getOWLObjectProperty(ModelConstants.RHASRELATEDCONCEPT);
		
		Collection<String> propList = getAllObjectPropertiesName(owlModel, hasRelatedConcept);
		propList.add(hasRelatedConcept.getName());
	
		for (Iterator<?> iterator3 = individual.getRDFProperties().iterator(); iterator3.hasNext();) {
			Object prop1 = iterator3.next(); 
			if(prop1 instanceof OWLObjectProperty)
			{
				OWLObjectProperty owlProp = (OWLObjectProperty) prop1;
				if(propList.contains(owlProp.getName()))
				{
					count += individual.getPropertyValueCount(owlProp);	
				}
			}
		}
		return count;
	}


	public RelationObject getConceptRelationship(OWLModel owlModel, String cls) {
		logger.info("getConceptRelationship(" + cls + ")");
		RelationObject relationObject = new RelationObject();
		OWLNamedClass owlCls = owlModel.getOWLNamedClass(cls);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlCls);
		
		OWLObjectProperty hasRelatedConcept = owlModel.getOWLObjectProperty(ModelConstants.RHASRELATEDCONCEPT);
		
		Collection<OWLObjectProperty> propList = getAllObjectPropperties(owlModel, hasRelatedConcept);
		propList.add(hasRelatedConcept);
		for (Iterator<?> iter = propList.iterator(); iter.hasNext();)
		{
			ArrayList<ConceptObject> conceptList = new ArrayList<ConceptObject>();
			OWLObjectProperty prop = (OWLObjectProperty) iter.next();
			
			RelationshipObject rObj = new RelationshipObject();
			rObj.setUri(prop.getURI());
			rObj.setName(prop.getName());
			rObj.setType(RelationshipObject.OBJECT);
			Collection<?> labelList = prop.getLabels();
			for (Iterator<?> iterator = labelList.iterator(); iterator.hasNext();) 
			{
				Object obj = iterator.next();
				if (obj instanceof DefaultRDFSLiteral) {
					DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
					rObj.addLabel(element.getString(), element.getLanguage());
				}
			}
			
			Collection<?>  coll = individual.getPropertyValues(prop);
			for (Iterator<?> iterator = coll.iterator(); iterator.hasNext();) {
				Object insObj = iterator.next();
				if (insObj instanceof DefaultOWLIndividual) {
					DefaultOWLIndividual conceptIns = (DefaultOWLIndividual) insObj;
					OWLNamedClass destCls = owlModel.getOWLNamedClass(conceptIns.getProtegeType().getName());
					ConceptObject cObj = ProtegeWorkbenchUtility.makeConceptObject(owlModel, destCls);
					conceptList.add(cObj);
				}
			}
			if(!conceptList.isEmpty())
			{			
				relationObject.addResult(rObj, conceptList);
			}
			
		}
		return relationObject;
	}

	public RelationObject getConceptRelationship(String cls, OntologyInfo ontoInfo) {
		logger.info("getConceptRelationship(" + cls + ")");
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		RelationObject rObj = getConceptRelationship(owlModel,cls);
		///owlModel.dispose();
		return rObj;
	}
	
	/*public void deleteNonFuncValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,String property,String oldValue,String oldLanguage,ConceptObject conceptObject){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty prop = owlModel.getOWLProperty(property);
		setInstanceUpdateDate(owlModel, ins);
		
		Collection<?> val = ins.getPropertyValues(prop);
		for (Iterator<?> iter = val.iterator(); iter.hasNext();) {
			RDFSLiteral element = (RDFSLiteral) iter.next();
			if(element.getString().equals(oldValue) && element.getLanguage().equals(oldLanguage)){
				ins.removePropertyValue(prop, element);
			}
		}
		
		Validation v = new Validation();
		if(property.equals(ModelConstants.RHASSCOPENOTE)){
			ScopeNoteObject spObj = new ScopeNoteObject();
			spObj.setUri(conceptObject.getUri());
			spObj.setLang(oldLanguage);
			spObj.setLabel(oldValue);
			v.setOldValue(DatabaseUtil.setObject(spObj));
		}else if(property.equals(ModelConstants.RHASCHANGEHISTORY)){
			ChangeHistoryObject spObj = new ChangeHistoryObject();
			spObj.setUri(conceptObject.getUri());
			spObj.setLang(oldLanguage);
			spObj.setLabel(oldValue);
			v.setOldValue(DatabaseUtil.setObject(spObj));
		}
		
		v.setConcept(DatabaseUtil.setObject(conceptObject));
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
	
	public void editNonFuncValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,String property,String oldValue,String oldLanguage,String newValue,String newLanguage,ConceptObject conceptObject){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty prop = owlModel.getOWLProperty(property);
		setInstanceUpdateDate(owlModel, ins);
		
		for (Iterator<?> iter = ins.getPropertyValues(prop).iterator(); iter.hasNext();) {
			RDFSLiteral element = (RDFSLiteral) iter.next();
			if(element.getString().equals(oldValue) && element.getLanguage().equals(oldLanguage)){
				RDFSLiteral newVal = owlModel.createRDFSLiteral(newValue, newLanguage);
				ins.removePropertyValue(prop, element);
				ins.addPropertyValue(prop, newVal);
			}
		}
		
		
		Validation v = new Validation();
		if(property.equals(ModelConstants.RHASSCOPENOTE)){
			ScopeNoteObject spObj = new ScopeNoteObject();
			spObj.setUri(conceptObject.getUri());
			spObj.setLabel(oldValue);
			spObj.setLang(oldLanguage);
			v.setOldValue(DatabaseUtil.setObject(spObj));
			
			ScopeNoteObject spObjNew = new ScopeNoteObject();
			spObjNew.setUri(conceptObject.getUri());
			spObjNew.setLabel(newValue);
			spObjNew.setLang(newLanguage);
			v.setNewValue(DatabaseUtil.setObject(spObjNew));
			
		}else if(property.equals(ModelConstants.RHASCHANGEHISTORY)){
			
			ChangeHistoryObject spObj = new ChangeHistoryObject();
			spObj.setUri(conceptObject.getUri());
			spObj.setLabel(oldValue);
			spObj.setLang(oldLanguage);
			v.setOldValue(DatabaseUtil.setObject(spObj));
			
			ChangeHistoryObject spObjNew = new ChangeHistoryObject();
			spObjNew.setUri(conceptObject.getUri());
			spObjNew.setLabel(newValue);
			spObjNew.setLang(newLanguage);
			v.setNewValue(DatabaseUtil.setObject(spObjNew));
		}
		v.setConcept(DatabaseUtil.setObject(conceptObject));
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
	
	public void addNonFuncValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,String property,String value,String language,ConceptObject conceptObject){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty prop = owlModel.getOWLProperty(property);
		RDFSLiteral val = owlModel.createRDFSLiteral(value, language);
		ins.addPropertyValue(prop, val);
		setInstanceUpdateDate(owlModel, ins);
		
		Validation v = new Validation();

		if(property.equals(ModelConstants.RHASSCOPENOTE)){
			ScopeNoteObject spObj = new ScopeNoteObject();
			spObj.setLang(language);
			spObj.setLabel(value);
			spObj.setUri(conceptObject.getUri());
			v.setNewValue(DatabaseUtil.setObject(spObj));
			
		}else if(property.equals(ModelConstants.RHASCHANGEHISTORY)){
			ChangeHistoryObject spObj = new ChangeHistoryObject();
			spObj.setLang(language);
			spObj.setLabel(value);
			spObj.setUri(conceptObject.getUri());
			v.setNewValue(DatabaseUtil.setObject(spObj));
		}
		
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(Date.getROMEDate());
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
		
	}*/
	
	public HashMap<RelationshipObject, ArrayList<NonFuncObject>> deletePropertyValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId, NonFuncObject oldValue, RelationshipObject rObj, ConceptObject conceptObject, String property){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty prop = owlModel.getOWLProperty(rObj.getName());
		setInstanceUpdateDate(owlModel, ins);
		
		
		if(oldValue.getLanguage()!=null && !oldValue.getLanguage().equals(""))
		{
			for (Iterator<?> iter = ins.getPropertyValues(prop).iterator(); iter.hasNext();) {
				RDFSLiteral element = (RDFSLiteral) iter.next();
				if(element.getString().equals(oldValue.getValue()) && element.getLanguage().equals(oldValue.getLanguage())){
					ins.removePropertyValue(prop, element);
				}
			}
		}
		else
		{
			ins.removePropertyValue(prop, oldValue.getValue());
		}
		
		AttributesObject attObj = new AttributesObject();
		attObj.setRelationshipObject(rObj);
		attObj.setValue(oldValue);
		
		if(ConfigConstants.ISINDEXING)
		{
			//update index
			if(prop.getName().equals(ModelConstants.RHASSCOPENOTE))
				IndexingEngineFactory.updateIndex(ontoInfo, ins.getURI(), prop.getURI(), oldValue.getLanguage(), SearchServiceImplOWLART.index_scopeNotes);
		}

		Validation v = new Validation();
		v.setOldValue(DatabaseUtil.setObject(attObj));
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(ProtegeWorkbenchUtility.getDate(""+ins.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED))));
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
		return getPropertyValue(owlModel, property, conceptObject.getName());
	}
	
	public HashMap<RelationshipObject, ArrayList<NonFuncObject>> editPropertyValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,NonFuncObject oldValue, NonFuncObject newValue, RelationshipObject rObj, ConceptObject conceptObject, String property){
		logger.info("editNonFuncValue(" + actionId + ", " + status + ", " + userId + ", "
				+ oldValue + ", " + newValue + ", " + conceptObject
				+ ")");
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty prop = owlModel.getOWLProperty(rObj.getName());
		setInstanceUpdateDate(owlModel, ins);
		
		if(oldValue.getLanguage()!=null && !oldValue.getLanguage().equals(""))
		{
			for (Iterator<?> iter = ins.getPropertyValues(prop).iterator(); iter.hasNext();) {
				RDFSLiteral element = (RDFSLiteral) iter.next();
				if(element.getString().equals(oldValue.getValue()) && element.getLanguage().equals(oldValue.getLanguage())){
					ins.removePropertyValue(prop, element);
					ins.addPropertyValue(prop, owlModel.createRDFSLiteral(newValue.getValue(), newValue.getLanguage()));
				}
			}
		}
		else
		{
			ins.removePropertyValue(prop, oldValue.getValue());
			ins.addPropertyValue(prop, newValue.getValue());
		}
		
		AttributesObject oldAttObj = new AttributesObject();
		oldAttObj.setRelationshipObject(rObj);
		oldAttObj.setValue(oldValue);
		
		AttributesObject newAttObj = new AttributesObject();
		newAttObj.setRelationshipObject(rObj);
		newAttObj.setValue(newValue);
		
		if(ConfigConstants.ISINDEXING)
		{
			//update index
			if(prop.getName().equals(ModelConstants.RHASSCOPENOTE))
				IndexingEngineFactory.updateIndex(ontoInfo, ins.getURI(), prop.getURI(), newValue.getLanguage(), SearchServiceImplOWLART.index_scopeNotes);
		}
		
		Validation v = new Validation();
		v.setOldValue(DatabaseUtil.setObject(oldAttObj));
		v.setNewValue(DatabaseUtil.setObject(newAttObj));
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setAction(actionId);
		v.setStatus(status.getId());
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(ProtegeWorkbenchUtility.getDate(""+ins.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED))));
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
		
		return getPropertyValue(owlModel, property, conceptObject.getName());
	}
	
	public HashMap<RelationshipObject, ArrayList<NonFuncObject>> addPropertyValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId, NonFuncObject value,RelationshipObject rObj,ConceptObject conceptObject, String property){
		OWLModel owlModel = null; 
		try
		{
			logger.info("addNonFuncValue(" + actionId + ", " + status + ", " + userId + ", " 
				+ value + ", " + conceptObject + ")");
			owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
			OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
			OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
			OWLProperty prop = owlModel.getOWLProperty(rObj.getName());
			
			
			if(value.getLanguage()!=null && !value.getLanguage().equals(""))
				ins.addPropertyValue(prop, owlModel.createRDFSLiteral(value.getValue(), value.getLanguage()));
			else
				ins.addPropertyValue(prop, value.getValue());

			setInstanceUpdateDate(owlModel, ins);
			
			AttributesObject attObj = new AttributesObject();
			attObj.setRelationshipObject(rObj);
			attObj.setValue(value);
			
			if(ConfigConstants.ISINDEXING)
			{
				//update index
				if(prop.getName().equals(ModelConstants.RHASSCOPENOTE))
					IndexingEngineFactory.updateIndex(ontoInfo, ins.getURI(), prop.getURI(), value.getLanguage(), SearchServiceImplOWLART.index_scopeNotes);
			}
			
			
			Validation v = new Validation();
			v.setNewValue(DatabaseUtil.setObject(attObj));
			v.setConcept(DatabaseUtil.setObject(conceptObject));
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
		return getPropertyValue(owlModel, property, conceptObject.getName());
	}
	
	public ConceptMappedObject addMappedConcept(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,String destConceptName,String conceptName){
		logger.info("addMappedConcept(" + actionId + ", " + status + ", " + userId + ", " + destConceptName
				+ ", " + conceptName + ")");
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptName);
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty mapProp = owlModel.getOWLProperty(ModelConstants.RHASMAPPEDDOMAINCONCEPT);
		OWLNamedClass destCls = owlModel.getOWLNamedClass(destConceptName);
		OWLIndividual destIns = ProtegeWorkbenchUtility.getConceptInstance(owlModel, destCls);
		ins.addPropertyValue(mapProp, destIns);
		setInstanceUpdateDate(owlModel, ins);
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(ProtegeWorkbenchUtility.makeConceptObject(owlModel, cls)));
		v.setAction(actionId);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setNewValue(DatabaseUtil.setObject(ProtegeWorkbenchUtility.makeConceptObject(owlModel, destCls)));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(Date.getROMEDate());
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
		return getMappedConcept(owlModel, conceptName);
	}
	
	public ConceptMappedObject deleteMappedConcept(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,ConceptObject destConceptObj,ConceptObject conceptObject){
		logger.info("deleteMappedConcept(" + actionId + ", " + status + ", " + userId + ", " + destConceptObj
				+ ", " + conceptObject + ")");
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty mapProp = owlModel.getOWLProperty(ModelConstants.RHASMAPPEDDOMAINCONCEPT);
		OWLNamedClass destCls = owlModel.getOWLNamedClass(destConceptObj.getName());
		OWLIndividual destIns = ProtegeWorkbenchUtility.getConceptInstance(owlModel, destCls);
		ins.removePropertyValue(mapProp, destIns);
		setInstanceUpdateDate(owlModel, ins);
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setAction(actionId);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setOldValue(DatabaseUtil.setObject(destConceptObj));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(ProtegeWorkbenchUtility.getDate(""+ins.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED))));
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
		return getMappedConcept(owlModel, conceptObject.getName());
	}
	
	public void moveConcept(OntologyInfo ontoInfo, String conceptName, String oldParentConceptName, String newParentConceptName, OwlStatus status, int actionId, int userId){
		
		logger.info("moveConcept(" + conceptName + ", " + oldParentConceptName + ", " + newParentConceptName
				+ ", " + status + ", " + actionId + ", " + userId + ")");
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptName);
		OWLNamedClass oldParentCls = owlModel.getOWLNamedClass(oldParentConceptName);
		OWLNamedClass newParentCls = owlModel.getOWLNamedClass(newParentConceptName);
		cls.removeSuperclass(oldParentCls);
		cls.addSuperclass(newParentCls);
		///owlModel.dispose();
	}
	
	public void copyConcept(OntologyInfo ontoInfo, String conceptName, String parentConceptName, OwlStatus status, int actionId, int userId){
		
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptName);
		OWLNamedClass parentCls = owlModel.getOWLNamedClass(parentConceptName);
		cls.addSuperclass(parentCls);
		///owlModel.dispose();
	}
	
	public Integer removeConcept(OntologyInfo ontoInfo, String conceptName, String parentConceptName, OwlStatus status, int actionId, int userId){
		int cnt = 0;
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptName);
		OWLNamedClass parentCls = owlModel.getOWLNamedClass(parentConceptName);
		cnt = cls.getSuperclassCount()-1;
		if(cnt>1)
		{
			cls.removeSuperclass(parentCls);
		}
		///owlModel.dispose();
		return cnt;
	}


	public RecentChangesInitObject getConceptHistoryInitData(String uri, int ontologyId , int type) 
	{
		RecentChangesInitObject rcio = new RecentChangesInitObject();
		rcio.setActions(new ValidationServiceImpl().getAction());
		rcio.setUsers(new ValidationServiceImpl().getAllUsers());
		rcio.setSize(filterHistory( uri , getConceptHistoryData(ontologyId , type) , type ).size());	    
		return rcio;
	}
		
	public ArrayList<RecentChanges> getConceptHistoryData(int ontologyId , int type) // type = 1 - concept ; type = 2 - concept-term
	{
		logger.info("getConceptHistoryData(" + ontologyId + ", " + type + ")");
		String actionCondition = null;
	    if(type == InformationObject.CONCEPT_TYPE){
	    	actionCondition = "rc.modified_action = 1 OR rc.modified_action = 2 OR rc.modified_action = 3 OR rc.modified_action = 4 OR rc.modified_action = 5 OR rc.modified_action = 18 OR rc.modified_action = 19 OR rc.modified_action = 20 OR rc.modified_action = 21 OR rc.modified_action = 22 OR rc.modified_action = 23 OR rc.modified_action = 24 OR rc.modified_action = 25 OR rc.modified_action = 26 OR rc.modified_action = 27 OR rc.modified_action = 28 OR rc.modified_action = 29 OR rc.modified_action = 30 OR rc.modified_action = 31 OR rc.modified_action = 32 OR rc.modified_action = 33 OR rc.modified_action = 34 OR rc.modified_action = 35 OR rc.modified_action = 36 OR rc.modified_action = 37 OR rc.modified_action = 38 OR rc.modified_action = 39  OR rc.modified_action = 72 OR rc.modified_action = 73";
	    }else if(type == InformationObject.TERM_TYPE){
	    	actionCondition = "rc.modified_action = 6 OR rc.modified_action = 7 OR rc.modified_action = 8 OR rc.modified_action = 9 OR rc.modified_action = 10 OR " +
	    				  	"rc.modified_action = 11 OR rc.modified_action = 12 OR rc.modified_action = 13 OR rc.modified_action = 14 OR rc.modified_action = 15 OR rc.modified_action = 16 OR rc.modified_action = 17 OR rc.modified_action = 72 OR rc.modified_action = 73";		    	
	    }
		String query = "select rc.* from recent_changes rc where ontology_id = "+ontologyId + " AND ( " + actionCondition + " )";	

		try 
		{
			ArrayList<RecentChanges> list = (ArrayList<RecentChanges>) HibernateUtilities.currentSession().createSQLQuery(query).addEntity("rc",RecentChanges.class).list();
			ArrayList<RecentChanges> cloneList = new ArrayList<RecentChanges>();
			if(list.size()>0)
			{
				list = new ValidationServiceImpl().setRecentChanges(list);				 				
				for(int i=0; i<list.size();i++)
				{
					if(list.get(i).getModifiedActionId() == 72 || list.get(i).getModifiedActionId() == 73)
					{												
						Object obj = list.get(i).getModifiedObject();
						if(obj!=null)
						{
							ArrayList<LightEntity> aList = list.get(i).getModifiedObject();
							if(aList.size()>0)
							{
								Object o = aList.get(0);
								if(o instanceof Validation)
								{
									Validation v = (Validation) aList.get(0);				
									int action = v.getAction();
									if(type == InformationObject.CONCEPT_TYPE)
									{
										if( action == 1 || action == 2 || action == 3 || action == 4 || action == 5 || action == 18 || action == 19 || action == 20 || action == 21 || action == 22 || action == 23 || action == 24 || action == 25 || action == 26 || action == 27 || action == 28 || action == 29 || action == 30 || action == 31 || action == 32 || action == 33 || action == 34 || action == 35 || action == 36 || action == 37 || action == 38 || action == 39)
											cloneList.add(list.get(i)); 	
									}
									else if(type == InformationObject.TERM_TYPE)
									{
										if( action == 6 || action == 7 || action == 8 || action == 9 || action == 10 || action == 11 || action == 12 || action == 13 || action == 14 || action == 15 || action == 16 || action == 17)
											cloneList.add(list.get(i)); 	
									}
								}
							}
						}
					}else
						cloneList.add(list.get(i));
				}
				
			}		

			return cloneList;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return new ArrayList<RecentChanges>();
		}
		finally 
		{
			HibernateUtilities.closeSession();
		}
	}

	public ArrayList<RecentChanges> filterHistory(String uri, ArrayList<RecentChanges> rcList, int filterBy) {
		logger.info("filterHistory(" + uri + ", " + rcList + ", " + filterBy + ")" );
		ArrayList<RecentChanges> list = new ArrayList<RecentChanges>();
		
		for (int i = 0; i < rcList.size(); i++)			
		{			
			RecentChanges c = (RecentChanges) rcList.get(i);
			try			
			{				
				if(c.getModifiedObject().size() > 0 )
				{					
					Object obj = c.getModifiedObject().get(0);					
					if(obj instanceof Validation)
					{
						Validation val = (Validation) obj;
						
						if(val!=null)
						{	
							if(filterBy == InformationObject.CONCEPT_TYPE){
								ConceptObject cObj =  val.getConceptObject();							
								if (cObj!=null)
								{										
									if(cObj.getUri() != null)
									{
										if(cObj.getUri().equals(uri))								
										{
											list.add(c);									
										}
									}
								}
							}
							else if(filterBy == InformationObject.TERM_TYPE){
								TermObject tObj =  val.getTermObject();							
								if (tObj!=null)
								{										
									if(tObj.getUri() != null)
									{										
										if(tObj.getUri().equals(uri))								
										{
											list.add(c);									
										}
									}
								}
							}
						}
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		
		}		
		return list;
	}
	
	public ArrayList<RecentChanges> requestConceptHistoryRows(Request request, int ontologyId, String uri , int type) {
		logger.info("requestConceptHistoryRows(" + request + ", " + ontologyId + ", " + uri + ", " + type + ")");
		HashMap<String, String> col = new HashMap<String, String>();
		   col.put("1", "rc.modified_id");
		   col.put("2", "rc.modified_object");
		   col.put("3", "oa.action");
		   col.put("4", "u.username");
		   col.put("5", "rc.modified_date");
		   
		   String orderBy = " rc.modified_date desc ";
		  // Get the sort info, even though we ignore it
		    ColumnSortList sortList = request.getColumnSortList();
		    if(sortList!=null)
		    {
			    ColumnSortInfo csi = sortList.getPrimaryColumnSortInfo();
			    if(csi!=null)	
			    {
			    	 if(csi.isAscending())
			    		orderBy = col.get(""+csi.getColumn()) + " ASC ";
			    	else
			    		orderBy = col.get(""+csi.getColumn()) + " DESC ";
			    }
		    }

		    int startRow = request.getStartRow();
		    int numRow = request.getNumRows();
		    if(numRow <0) numRow=0;
		    
		    String actionCondition = null;
		    if(type == 1){
		    	actionCondition = "rc.modified_action = 1 OR rc.modified_action = 2 OR rc.modified_action = 3 OR rc.modified_action = 4 OR rc.modified_action = 5 OR rc.modified_action = 18 OR rc.modified_action = 19 OR rc.modified_action = 20 OR rc.modified_action = 21 OR rc.modified_action = 22 OR rc.modified_action = 23 OR rc.modified_action = 24 OR rc.modified_action = 25 OR rc.modified_action = 26 OR rc.modified_action = 27 OR rc.modified_action = 28 OR rc.modified_action = 29 OR rc.modified_action = 30 OR rc.modified_action = 31 OR rc.modified_action = 32 OR rc.modified_action = 33 OR rc.modified_action = 34 OR rc.modified_action = 35 OR rc.modified_action = 36 OR rc.modified_action = 37 OR rc.modified_action = 38 OR rc.modified_action = 39  OR rc.modified_action = 72 OR rc.modified_action = 73";
		    }else if(type == 2){
		    	actionCondition = "rc.modified_action = 6 OR rc.modified_action = 7 OR rc.modified_action = 8 OR rc.modified_action = 9 OR rc.modified_action = 10 OR " +
		    				  	"rc.modified_action = 11 OR rc.modified_action = 12 OR rc.modified_action = 13 OR rc.modified_action = 14 OR rc.modified_action = 15 OR rc.modified_action = 16 OR rc.modified_action = 17 OR rc.modified_action = 72 OR rc.modified_action = 73";		    	
		    }
		    String query = "select rc.*, oa.action, u.username from " +
							"recent_changes rc, owl_action oa, users u " +
							"where " +
							"rc.ontology_id = "+ontologyId +
						   " and (" + actionCondition + " ) " +
						   " and rc.modifier_id = u.user_id " +
						   " and rc.modified_action = oa.id " +
						   " order by "+orderBy; //" LIMIT "+numRow+" OFFSET "+startRow;
		    		    
		    try 
			{
				ArrayList<RecentChanges> list = (ArrayList<RecentChanges>) HibernateUtilities.currentSession().createSQLQuery(query).addEntity("rc",RecentChanges.class).list();				
				ArrayList<RecentChanges> filteredList = filterHistory(uri, new ValidationServiceImpl().setRecentChanges(list) , type);
				ArrayList<RecentChanges> cloneList = new ArrayList<RecentChanges>(); 
				
				if(list.size()>0)
				{				
					for(int i=0; i<filteredList.size();i++)
					{
						if(filteredList.get(i).getModifiedActionId() == 72 || filteredList.get(i).getModifiedActionId() == 73)
						{												
							Validation v = (Validation) filteredList.get(i).getModifiedObject().get(0);				
							int action = v.getAction();
							if(type == 1)
							{
								if( action == 1 || action == 2 || action == 3 || action == 4 || action == 5 || action == 18 || action == 19 || action == 20 || action == 21 || action == 22 || action == 23 || action == 24 || action == 25 || action == 26 || action == 27 || action == 28 || action == 29 || action == 30 || action == 31 || action == 32 || action == 33 || action == 34 || action == 35 || action == 36 || action == 37 || action == 38 || action == 39)
									cloneList.add(filteredList.get(i)); 	
							}
							else if(type == 2)
							{
								if( action == 6 || action == 7 || action == 8 || action == 9 || action == 10 || action == 11 || action == 12 || action == 13 || action == 14 || action == 15 || action == 16 || action == 17)
									cloneList.add(filteredList.get(i)); 	
							}											
						}else
							cloneList.add(filteredList.get(i));
					}
					
				}
				
				if(numRow <0) numRow = 0;
			    int endRow = startRow + numRow;
			    if(endRow > cloneList.size()) endRow = cloneList.size();			    			    			   
				return new ArrayList<RecentChanges>(cloneList.subList(startRow, endRow)) ;				
											
			}
			catch (Exception ex) 
			{
				ex.printStackTrace();
				return new ArrayList<RecentChanges>();
			}
			finally 
			{
				HibernateUtilities.closeSession();
			}
	  }



}