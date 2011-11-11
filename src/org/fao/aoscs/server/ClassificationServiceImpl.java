package org.fao.aoscs.server;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;

import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.gwt.GwtConfigurationHelper;
import net.sf.gilead.gwt.PersistentRemoteService;

import org.fao.aoscs.client.module.classification.service.ClassificationService;
import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.domain.ClassificationObject;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.SchemeObject;
import org.fao.aoscs.domain.TermObject;
import org.fao.aoscs.domain.Validation;
import org.fao.aoscs.hibernate.DatabaseUtil;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.server.protege.ProtegeModelFactory;
import org.fao.aoscs.server.protege.ProtegeWorkbenchUtility;
import org.fao.aoscs.server.utility.ConceptUtility;
import org.fao.aoscs.server.utility.Date;

import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;

public class ClassificationServiceImpl extends PersistentRemoteService implements ClassificationService{

	private static final long serialVersionUID = -7732410471281708562L;

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
		setBeanManager(GwtConfigurationHelper.initGwtStatelessBeanManager( new HibernateUtil(HibernateUtilities.getSessionFactory())));
	}
	
	public InitializeConceptData initData(int group_id, OntologyInfo ontoInfo){
		InitializeConceptData data = new InitializeConceptData();
		try
		{
			OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
			data.setSource(ConceptUtility.getSource());
			data.setTermCodeProperties(ConceptUtility.getAllTermCodeProperties(owlModel));
			data.setConceptDomainAttributes(ConceptUtility.getDatatypeProperties(owlModel, ModelConstants.RCONCEPTDOMAINDATATYPEPROPERTY));
			data.setConceptEditorialAttributes(ConceptUtility.getDatatypeProperties(owlModel, ModelConstants.RCONCEPTEDITORIALDATATYPEPROPERTY));
			data.setConceptEditorialAttributes(ConceptUtility.getDatatypeProperties(owlModel, ModelConstants.RCONCEPTEDITORIALDATATYPEPROPERTY));
			data.setTermDomainAttributes(ConceptUtility.getDatatypeProperties(owlModel, ModelConstants.RTERMDOMAINDATATYPEPROPERTY));
			data.setActionMap(ConceptUtility.getActionMap(group_id));
			data.setActionStatus(ConceptUtility.getActionStatusMap(group_id));
			data.setTermCodePropertyType(ConceptUtility.getTermCodeType(owlModel));
			data.setBelongsToModule(InitializeConceptData.CLASSIFICATIONMODULE);
			data.setClassificationObject(getCategoryTree(ontoInfo));
			///owlModel.dispose();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return data;
	}
	
	public ConceptObject getSelectedCategory(OntologyInfo ontoInfo, String className, String parentClassName)
	{
		ConceptObject cObj = new ConceptObject();
		try
		{
			OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
			cObj = getCategoryObject(owlModel, className);
			cObj.setBelongsToModule(ConceptObject.CLASSIFICATIONMODULE);
			ConceptObject parentObject = getParentCategoryObject(owlModel, parentClassName);
			cObj.setParentInstance(parentObject.getConceptInstance());
			cObj.setParentObject(parentObject);
			cObj.setParentURI(parentObject.getUri());
			///owlModel.dispose();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return cObj;
	}
	
	public ConceptObject getCategoryObject(OWLModel owlModel, String className)
	{
		ConceptObject cObj = new ConceptObject();
		try
		{
			OWLNamedClass cls = owlModel.getOWLNamedClass(className);
			cObj = ProtegeWorkbenchUtility.makeConceptObject(owlModel, cls);
			cObj.setBelongsToModule(ConceptObject.CLASSIFICATIONMODULE);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return cObj;
	}
	
	private ConceptObject getParentCategoryObject(OWLModel owlModel, String className)
	{
		ConceptObject cObj = new ConceptObject();
		cObj.setBelongsToModule(ConceptObject.CLASSIFICATIONMODULE);
		if(className!=null && !ModelConstants.CCATEGORY.equals(className)){
			cObj = getCategoryObject(owlModel, className);
		}else{
			if(ModelConstants.CCATEGORY.equals(className))
			{
				TermObject termObject = new TermObject();
				termObject.setLabel("Top level concept");
				termObject.setUri(ModelConstants.COMMONBASENAMESPACE+"i_en_domain_concept");
				termObject.setName("i_en_domain_concept");
				termObject.setLang("en");
				termObject.setConceptUri(ModelConstants.COMMONBASENAMESPACE+ModelConstants.CCATEGORY);
				termObject.setConceptName(ModelConstants.CCATEGORY);
				
				cObj = new ConceptObject();
				cObj.setUri(ModelConstants.COMMONBASENAMESPACE+ModelConstants.CCATEGORY);
				cObj.addTerm(termObject.getUri(), termObject);
				cObj.setName(ModelConstants.CCATEGORY);
			}
		}
		return cObj;
	}
	
	public ClassificationObject getCategoryTree(OntologyInfo ontoInfo){
		ClassificationObject clsObj = new ClassificationObject();
		try
		{
			OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
			OWLNamedClass scheme = owlModel.getOWLNamedClass(ModelConstants.CCLASSIFICATIONSCHEME);
			OWLProperty hasCategory = owlModel.getOWLProperty(ModelConstants.RHASCATEGORY);
			
			
			for (Iterator iter = scheme.getInstances(true).iterator(); iter.hasNext();) {
				OWLIndividual schemeIns = (OWLIndividual) iter.next();
				if(!schemeIns.isBeingDeleted())
				{
					SchemeObject sObj = getSchemeObject(schemeIns);
					for (Iterator iterator = schemeIns.getPropertyValues(hasCategory).iterator(); iterator.hasNext();) {
						OWLIndividual categoryIns = (OWLIndividual) iterator.next();
						OWLProperty isSubPropertyOf = owlModel.getOWLProperty(sObj.getRIsSub()); 	
						ConceptObject cObj = ProtegeWorkbenchUtility.makeConceptObject(owlModel, (OWLNamedClass)categoryIns.getProtegeType());		
						cObj.setHasChild(false);
						cObj.setBelongsToModule(ConceptObject.CLASSIFICATIONMODULE);
						cObj.setScheme(schemeIns.getURI());
						OWLIndividual parent = getCategoryParent(isSubPropertyOf, categoryIns);
						if(parent!=null ){
							cObj.setParentInstance(parent.getURI());
							cObj.setRootItem(false);
						}else{
							cObj.setRootItem(true);
						}		
						sObj.addCategoryList(cObj.getConceptInstance(), cObj);
					}
					clsObj.addSchemeList(sObj.getSchemeInstance(), sObj);
				}
			}
			///owlModel.dispose();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return clsObj;
	}
	
	public OWLIndividual getCategoryParent(OWLProperty isSubPropertyOf,OWLIndividual categoryIns){
		OWLIndividual ins  = null;
		for (Iterator iter = categoryIns.getPropertyValues(isSubPropertyOf).iterator(); iter.hasNext();) {
			OWLIndividual parentIns = (OWLIndividual) iter.next();
			ins = parentIns;
		}
		return ins;
	}
	public SchemeObject getSchemeObject(OWLIndividual schemeIns){
		SchemeObject sObj = new SchemeObject();
		sObj.setNamespace(schemeIns.getNamespace());
		sObj.setSchemeName(schemeIns.getName());
		sObj.setSchemeInstance(schemeIns.getURI());
		sObj.setNameSpaceCatagoryPrefix(schemeIns.getNamespacePrefix());
		String schemeLabel =getSchemeLabel(schemeIns);
		String schemeDescription =getSchemeComment(schemeIns);
		if(schemeLabel!=null){
			sObj.setSchemeLabel(schemeLabel);
		}
		if(schemeDescription!=null){
			sObj.setDescription(schemeDescription);
		}
		sObj.setRHasSub(ModelConstants.RHAS+schemeIns.getLocalName().replaceAll("i_", "")+ModelConstants.SUBCATEGORY);
		sObj.setRIsSub(ModelConstants.RIS+schemeIns.getLocalName().replaceAll("i_", "")+ModelConstants.SUBCATEGORYOF);
		return sObj;
	}
	private String getSchemeLabel(OWLIndividual schemeIns){
		String label = null;
		Collection co = schemeIns.getLabels();
		for (Iterator iter = co.iterator(); iter.hasNext();) {
			Object obj = (Object) iter.next();
			if (obj instanceof RDFSLiteral) {
				RDFSLiteral element = (RDFSLiteral) obj;
				if(element.getLanguage().equals("en")){
					label = element.getString();
				}
			}
		}
		return label;
	}
	
	private String getSchemeComment(OWLIndividual schemeIns){
		String comment = null;
		for (Iterator iter = schemeIns.getComments().iterator(); iter.hasNext();) {
			Object obj = (Object) iter.next();
			if (obj instanceof String) {
				String element = (String) obj;
				comment = element;
			}
		}
		return comment;
	}
	
	public void deleteScheme(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,SchemeObject oldSObj){
		try
		{
			OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
			
			for (Iterator it = owlModel.getOWLNamedClass(ModelConstants.CCATEGORY).getSubclasses(true).iterator(); it.hasNext();) 
			{		    		    
				OWLNamedClass cls = (OWLNamedClass) it.next();			
				for (Iterator<OWLIndividual> jt = cls.getInstances(false).iterator(); jt.hasNext();) 
				{
					OWLIndividual individual = (OWLIndividual) jt.next();
		        	Object obj = individual.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RBELONGSTOSCHEME));
		        	if(obj instanceof OWLIndividual)
		        	{
		        		OWLIndividual scheme = (OWLIndividual) obj;
			        	if(scheme.getName().equals(oldSObj.getSchemeName()))
			        	{
			        		individual.delete();
			        		cls.delete();
			        	}
		        	}
	    			
				}
			}
			
			for (Iterator iter = owlModel.getOWLNamedClass(ModelConstants.CCLASSIFICATIONSCHEME).getInstances(true).iterator(); iter.hasNext();) {
				OWLIndividual schemeInsa = (OWLIndividual) iter.next();
				if(schemeInsa.getName().equals(oldSObj.getSchemeName()))
				{
					schemeInsa.delete();
					schemeInsa = null;
				}
				
			}
			
			OWLObjectProperty isSubOf = owlModel.getOWLObjectProperty(oldSObj.getRIsSub());
			if(isSubOf!=null)
			{
				isSubOf.delete();
			}

			
			OWLObjectProperty hasSub = owlModel.getOWLObjectProperty(oldSObj.getRHasSub());
			if(isSubOf!=null)
			{
				hasSub.delete();
			}
			
			if(owlModel.getNamespaceManager().getPrefix(oldSObj.getNamespace())!=null)
				owlModel.getNamespaceManager().removePrefix(oldSObj.getNameSpaceCatagoryPrefix());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		///owlModel.dispose();
	}

	public void editScheme(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,SchemeObject oldSObj,SchemeObject newSObj){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLIndividual schemeIns = owlModel.getOWLIndividual(oldSObj.getSchemeName());
		schemeIns.removeLabel(oldSObj.getSchemeLabel(), "en");
		schemeIns.addLabel(newSObj.getSchemeLabel(), "en");
		schemeIns.setComment(newSObj.getDescription());
		
		TermObject termObj = new TermObject();
		termObj.setLabel(oldSObj.getSchemeLabel());
		termObj.setLang("en");
		termObj.setMainLabel(true);
		
		ConceptObject blankConcept = new ConceptObject();
		blankConcept.addTerm(termObj.getUri(), termObj);
		blankConcept.setBelongsToModule(ConceptObject.CLASSIFICATIONMODULE);
	/*	
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(blankConcept));
		v.setAction(actionId);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setNewValue(DatabaseUtil.setObject(newSObj));
		v.setOldValue(DatabaseUtil.setObject(oldSObj));
		v.setOntologyId(ontologyId);
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);*/
		
		///owlModel.dispose();
	}
	
	public void addNewScheme(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,SchemeObject sObj){
		
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass classification = owlModel.getOWLNamedClass(ModelConstants.CCLASSIFICATIONSCHEME);
		OWLNamedClass cCategory = owlModel.getOWLNamedClass(ModelConstants.CCATEGORY);
		owlModel.getNamespaceManager().setPrefix(sObj.getNamespace(), sObj.getNameSpaceCatagoryPrefix());
		OWLIndividual schemeIns = classification.createOWLIndividual(sObj.getSchemeName());
		schemeIns.addLabel(sObj.getSchemeLabel(), "en");
		schemeIns.addComment(sObj.getDescription());
		//String nameSpaceForCategory = schemeIns.getName().replaceAll("i_", "").toLowerCase();
		//owlModel.getNamespaceManager().setPrefix(ModelConstants.COREBASENAMESPACE+nameSpaceForCategory+"#", nameSpaceForCategory);
		
		OWLObjectProperty hasSubCategory =  owlModel.getOWLObjectProperty(ModelConstants.RHASSUBCATEGORY);
		OWLObjectProperty newHasSub = owlModel.createOWLObjectProperty(sObj.getRHasSub());
		newHasSub.addLabel(ModelConstants.RHAS+" "+sObj.getNameSpaceCatagoryPrefix().toUpperCase()+" "+ModelConstants.SUBCATEGORY, "en");
		newHasSub.addSuperproperty(hasSubCategory);
		newHasSub.setTransitive(true);
		newHasSub.setDomain(cCategory);
		newHasSub.setRange(cCategory);
		
		OWLObjectProperty isSubCategoryOf =  owlModel.getOWLObjectProperty(ModelConstants.RISSUBCATEGORYOF);
		OWLObjectProperty newIsSub = owlModel.createOWLObjectProperty(sObj.getRIsSub());
		newIsSub.addLabel(ModelConstants.RIS+" "+sObj.getNameSpaceCatagoryPrefix().toUpperCase()+" "+ModelConstants.SUBCATEGORYOF, "en");
		newIsSub.addSuperproperty(isSubCategoryOf);
		newIsSub.setTransitive(true);
		newIsSub.setDomain(cCategory);
		newIsSub.setRange(cCategory);
		
		newIsSub.setInverseProperty(newHasSub);
		
		TermObject termObj = new TermObject();
		termObj.setLabel("New Scheme");
		termObj.setLang("en");
		termObj.setMainLabel(true);
		
		ConceptObject blankConcept = new ConceptObject();
		blankConcept.addTerm(termObj.getUri(), termObj);
		blankConcept.setBelongsToModule(ConceptObject.CLASSIFICATIONMODULE);
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(blankConcept));
		v.setAction(actionId);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setNewValue(DatabaseUtil.setObject(sObj));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(Date.getROMEDate());
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
	}
	
	private void setInstanceUpdateDate(OWLModel owlModel,OWLIndividual ins){
		OWLProperty updateDate = owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE);
		ins.setPropertyValue(updateDate, Date.getDateTime());
	}
	
	public String addFirstNewCategory(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,TermObject termObject ,ConceptObject conceptObject,SchemeObject schemeObject){

		long unique = new java.util.Date().getTime();
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLProperty lexicon = owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION);
		OWLProperty mainlabel = owlModel.getOWLProperty(ModelConstants.RISMAINLABEL);
		OWLProperty createDate = owlModel.getOWLProperty(ModelConstants.RHASDATECREATED);
		OWLProperty statusProp = owlModel.getOWLProperty(ModelConstants.RHASSTATUS);
		OWLProperty hasCategory = owlModel.getOWLProperty(ModelConstants.RHASCATEGORY);
		OWLNamedClass nounCls = owlModel.getOWLNamedClass(ModelConstants.CNOUN);
		OWLIndividual schemeIns = owlModel.getOWLIndividual(schemeObject.getSchemeName());
		
		OWLNamedClass cls = owlModel.createOWLNamedClass(schemeObject.getNameSpaceCatagoryPrefix()+":"+"c_"+unique);
		OWLNamedClass cCategory = owlModel.getOWLNamedClass(ModelConstants.CCATEGORY);
		cls.addSuperclass(cCategory);
		
		OWLIndividual term = (OWLIndividual) nounCls.createInstance(schemeObject.getNameSpaceCatagoryPrefix()+":"+"i_"+termObject.getLang().toLowerCase()+"_"+unique);
		term.addLabel(termObject.getLabel(), termObject.getLang());
		term.addPropertyValue(mainlabel, termObject.isMainLabel());
		term.addPropertyValue(createDate, Date.getDateTime());
		term.addPropertyValue(statusProp, status.getStatus());
		setInstanceUpdateDate(owlModel, term);
		 
		OWLIndividual ins = (OWLIndividual) cls.createInstance(schemeObject.getNameSpaceCatagoryPrefix()+":"+"i_"+unique);
		ins.addPropertyValue(lexicon, term);
		ins.addPropertyValue(createDate, Date.getDateTime());
		ins.addPropertyValue(statusProp, status.getStatus());
		setInstanceUpdateDate(owlModel, ins);
		schemeIns.addPropertyValue(hasCategory, ins);
		
		
		conceptObject.setConceptInstance(ins.getURI());
		conceptObject.setUri(cls.getURI());
		conceptObject.setName(cls.getName());
		conceptObject.setStatus(status.getStatus());
		conceptObject.setScheme(schemeIns.getURI());
		termObject.setUri(term.getURI());
		termObject.setName(term.getName());
		termObject.setConceptUri(conceptObject.getUri());
		termObject.setConceptName(conceptObject.getName());
		conceptObject.addTerm(termObject.getUri(), termObject);
		 
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(getCategoryParentObject()));
		v.setAction(actionId);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setNewValue(DatabaseUtil.setObject(conceptObject));
		v.setDateCreate(Date.getROMEDate());
		v.setDateModified(Date.getROMEDate());
		v.setOntologyId(ontoInfo.getOntologyId());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
		return conceptObject.getUri();
	}
	private ConceptObject getCategoryParentObject(){
		TermObject termObject = new TermObject();
		termObject.setLabel("Top level concept");
		termObject.setUri("");
		termObject.setLang("en");
		termObject.setConceptUri(ModelConstants.CCATEGORY);
		termObject.setConceptName(ModelConstants.CCATEGORY);
		
		ConceptObject parentObject = new ConceptObject();
		parentObject.setUri(ModelConstants.COMMONBASENAMESPACE+ModelConstants.CCATEGORY);
		parentObject.addTerm(termObject.getUri(), termObject);
		parentObject.setName(ModelConstants.CCATEGORY);
		parentObject.setBelongsToModule(ConceptObject.CLASSIFICATIONMODULE);
		
		return parentObject;
	}
	public void deleteCategory(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,ConceptObject conceptObject,SchemeObject schemeObject){
		
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
	public String addNewCategory(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,ConceptObject parentObject,String position,TermObject termObject,ConceptObject conceptObject,SchemeObject schemeObject){
		try
		{
			long unique = new java.util.Date().getTime();
			OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
			OWLProperty lexicon = owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION);
			OWLProperty mainlabel = owlModel.getOWLProperty(ModelConstants.RISMAINLABEL);
			OWLProperty createDate = owlModel.getOWLProperty(ModelConstants.RHASDATECREATED);
			OWLProperty statusProp = owlModel.getOWLProperty(ModelConstants.RHASSTATUS);
			OWLProperty hasCategory = owlModel.getOWLProperty(ModelConstants.RHASCATEGORY);
			OWLNamedClass nounCls = owlModel.getOWLNamedClass(ModelConstants.CNOUN);
			
			OWLNamedClass cls = owlModel.createOWLNamedClass(schemeObject.getNameSpaceCatagoryPrefix()+":"+"c_"+unique);
			OWLIndividual schemeIns = owlModel.getOWLIndividual(schemeObject.getSchemeName());
			
			OWLIndividual term = (OWLIndividual) nounCls.createInstance(schemeObject.getNameSpaceCatagoryPrefix()+":"+"i_"+termObject.getLang().toLowerCase()+"_"+unique);
			term.addLabel(termObject.getLabel(), termObject.getLang());
			term.addPropertyValue(mainlabel, termObject.isMainLabel());
			term.addPropertyValue(createDate, Date.getDateTime());
			term.addPropertyValue(statusProp, status.getStatus());
			setInstanceUpdateDate(owlModel, term);
			 
			OWLIndividual ins = (OWLIndividual) cls.createInstance(schemeObject.getNameSpaceCatagoryPrefix()+":"+"i_"+unique);
			ins.addPropertyValue(lexicon, term);
			ins.addPropertyValue(createDate, Date.getDateTime());
			ins.addPropertyValue(statusProp, status.getStatus());
			setInstanceUpdateDate(owlModel, ins);
			schemeIns.addPropertyValue(hasCategory, ins);
			
			
			OWLNamedClass parentCls;
			OWLIndividual parentIns;
			if(!parentObject.getName().endsWith(ModelConstants.CCATEGORY)){
				parentCls = owlModel.getOWLNamedClass(parentObject.getName());
				parentIns = ProtegeWorkbenchUtility.getConceptInstance(owlModel, parentCls);
				OWLProperty isSub = owlModel.getOWLProperty(schemeObject.getRIsSub());
				ins.addPropertyValue(isSub,parentIns);
			}
			else
			{
				parentObject = getCategoryParentObject();
				parentCls = owlModel.getOWLNamedClass(ModelConstants.CCATEGORY);
			}
			cls.addSuperclass(parentCls);
			
			conceptObject.setConceptInstance(ins.getURI());
			conceptObject.setUri(cls.getURI());
			conceptObject.setName(cls.getName());
			conceptObject.setStatus(status.getStatus());
			conceptObject.setScheme(schemeObject.getSchemeInstance());
			termObject.setUri(term.getURI());
			termObject.setName(term.getName());
			termObject.setConceptUri(conceptObject.getUri());
			termObject.setConceptName(conceptObject.getName());
			conceptObject.addTerm(termObject.getUri(), termObject);
			 
			Validation v = new Validation();
			v.setConcept(DatabaseUtil.setObject(parentObject));
			v.setAction(actionId);
			v.setOwnerId(userId);
			v.setModifierId(userId);
			v.setStatus(status.getId());
			v.setNewValue(DatabaseUtil.setObject(conceptObject));
			v.setDateCreate(Date.getROMEDate());
			v.setDateModified(Date.getROMEDate());
			v.setOntologyId(ontoInfo.getOntologyId());
			DatabaseUtil.createObject(v);
			///owlModel.dispose();
			return conceptObject.getUri();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "";
		}
	}
	
	public String makeLinkToFirstConcept(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,String conceptName,SchemeObject schemeObject){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptName);
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty hasCategory = owlModel.getOWLProperty(ModelConstants.RHASCATEGORY);
		OWLIndividual schemeIns = owlModel.getOWLIndividual(schemeObject.getSchemeName());
		schemeIns.addPropertyValue(hasCategory, ins);
		
		ConceptObject conceptObject = ProtegeWorkbenchUtility.makeConceptObject(owlModel, cls);
		conceptObject.setScheme(schemeObject.getSchemeInstance());
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(getCategoryParentObject()));
		v.setAction(actionId);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setNewValue(DatabaseUtil.setObject(conceptObject));
		v.setDateCreate(Date.getROMEDate());
		v.setDateModified(Date.getROMEDate());
		v.setOntologyId(ontoInfo.getOntologyId());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
		return conceptObject.getUri();
	}
	
	public String makeLinkToConcept(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,ConceptObject refConcept,String position,String conceptName,SchemeObject schemeObject){
		
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptName);
		OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLProperty hasCategory = owlModel.getOWLProperty(ModelConstants.RHASCATEGORY);
		OWLIndividual schemeIns = owlModel.getOWLIndividual(schemeObject.getSchemeName());
		ConceptObject parentObject = refConcept;
		if(!position.equals("child")){
			parentObject = refConcept.getParentObject();
		}
		
		schemeIns.addPropertyValue(hasCategory, ins);
		
		OWLNamedClass parentCls = owlModel.getOWLNamedClass(parentObject.getName());
		if(!parentCls.getName().equals(ModelConstants.CCATEGORY)){
			OWLIndividual parentIns = ProtegeWorkbenchUtility.getConceptInstance(owlModel, parentCls);
			OWLProperty hasSub = owlModel.getOWLProperty(schemeObject.getRHasSub());
			OWLProperty isSub = owlModel.getOWLProperty(schemeObject.getRIsSub());
			ins.addPropertyValue(isSub,parentIns);
			parentIns.addPropertyValue(hasSub,ins);
		}
		ConceptObject conceptObject = ProtegeWorkbenchUtility.makeConceptObject(owlModel, cls);
		conceptObject.setScheme(schemeObject.getSchemeInstance());
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(refConcept));
		v.setAction(actionId);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setNewValue(DatabaseUtil.setObject(conceptObject));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(Date.getROMEDate());
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);	
		///owlModel.dispose();
		return conceptObject.getUri();
	}
}
