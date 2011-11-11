package org.fao.aoscs.server.export;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.server.ConceptServiceImpl;
import org.fao.aoscs.server.protege.ProtegeModelFactory;
import org.fao.aoscs.server.protege.ProtegeWorkbenchUtility;

import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFSLiteral;

public class AgrovocData {
	
	private AgrovocObject agObj = new AgrovocObject();
	
	public AgrovocData(OntologyInfo ontoInfo){
		  this.agObj = convert2AgrovocObject(ontoInfo);
	}
	
	public AgrovocObject getAgrovocObject(){
		return agObj;
	}
	
	public ETermObject makeTermObject(OWLModel owlModel,OWLIndividual termInstance,OWLNamedClass owlCls){
		ETermObject termObject = new ETermObject();
		termObject.setUri(termInstance.getURI());
		Object createDate = termInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED));
		Object updateDate = termInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE));
		if(createDate != null && updateDate !=null){
			termObject.setCreateDate(createDate.toString());
			termObject.setUpdateDate(updateDate.toString());
		}
		Object status = termInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASSTATUS));
		if(status!=null){
			termObject.setStatus(status.toString());
		}
		Object mainLabel = termInstance.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RISMAINLABEL));
		if(mainLabel != null){
			termObject.setMainLabel(Boolean.valueOf(mainLabel.toString()));
		}

		Collection<?> termLabelList = termInstance.getLabels();
		for (Iterator<?> iterator = termLabelList.iterator(); iterator.hasNext();) {
			Object obj = iterator.next();
	    	if (obj instanceof DefaultRDFSLiteral) {
	    		DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
	    		termObject.setLabel(element.getString());
	    		termObject.setLanguage(element.getLanguage().toLowerCase());
			}
		}
		OWLProperty hasFishery3AlphaCode = owlModel.getOWLProperty(ModelConstants.RHASFISHERY3ALPHACODE);
		OWLProperty hasTaxonomicCode = owlModel.getOWLProperty(ModelConstants.RHASCODETAXONOMIC);
		OWLProperty hasCodeFaoPa = owlModel.getOWLProperty(ModelConstants.RHASCODEFAOPA);
		OWLProperty hasCodeAgrovoc = owlModel.getOWLProperty(ModelConstants.RHASCODEAGROVOC);
		OWLProperty hasCodeFaoterm = owlModel.getOWLProperty(ModelConstants.RHASCODEFAOTERM);
		OWLProperty hasCodeAsfa = owlModel.getOWLProperty(ModelConstants.RHASCODEASFA);
		OWLProperty hasCodeAsc = owlModel.getOWLProperty(ModelConstants.RHASCODEASC);
		termObject.setCodeAGROVOC(getLiteralValue(owlModel, termInstance, hasCodeAgrovoc));
		if(hasCodeAsc != null)	termObject.setCodeASC(getLiteralValue(owlModel, termInstance, hasCodeAsc));
		if(hasCodeAsfa != null) termObject.setCodeASFA(getLiteralValue(owlModel, termInstance, hasCodeAsfa));
		if(hasCodeFaoPa != null) termObject.setCodeFAOPA(getLiteralValue(owlModel, termInstance, hasCodeFaoPa));
		if(hasCodeFaoterm != null) termObject.setCodeFAOTERM(getLiteralValue(owlModel, termInstance, hasCodeFaoterm));
		if(hasFishery3AlphaCode != null) termObject.setCodeFishery(getLiteralValue(owlModel, termInstance, hasFishery3AlphaCode));
		if(hasTaxonomicCode != null) termObject.setCodeTaxonomic(getLiteralValue(owlModel, termInstance, hasTaxonomicCode));

		return termObject;
	}
	
	public String getLiteralValue(OWLModel owlModel,OWLIndividual ins,OWLProperty prop){
		String literal = null;
		if(ins.getPropertyValueLiteral(prop)!= null){
			literal = ins.getPropertyValueLiteral(prop).toString();
		}
		return literal;
	}

	private ArrayList<String> getSchemeOf(OWLIndividual ins,OWLProperty belongToScheme){
		ArrayList<String> list = new ArrayList<String>();
		
		Collection<?> c = ins.getPropertyValues(belongToScheme);
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			OWLIndividual element = (OWLIndividual) iter.next();
			list.add(element.getURI());
		}
		return list;
	}
	
	private ConceptObject getParentConceptObject(OWLNamedClass cls){
		OWLNamedClass parentCls = null;
		Collection<?> co = cls.getSuperclasses(false);
		for (Iterator<?> iter = co.iterator(); iter.hasNext();) 
		{
			parentCls = (OWLNamedClass) iter.next();
		}
		if(parentCls != null){
			ConceptObject lcObj = null;
			if((parentCls.getOWLModel() != null) && (parentCls != null))
			{
				if(ProtegeWorkbenchUtility.makeConceptObject(parentCls.getOWLModel(),parentCls) != null)
				{
					lcObj = ProtegeWorkbenchUtility.makeConceptObject(parentCls.getOWLModel(),parentCls) ;
				}				
			}

			if(lcObj != null)
			{
				return lcObj ; //ProtegeWorkbenchUtility.makeConceptObject(cls.getOWLModel(),cls);
			}
			else
			{
				return null;
			}
		}else{
			return null;
		} 
	}
	
	
	public AgrovocObject convert2AgrovocObject(OntologyInfo ontoInfo){
		
		AgrovocObject agObj = new AgrovocObject();
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(ModelConstants.CCATEGORY);
		OWLProperty belongToScheme = owlModel.getOWLProperty(ModelConstants.RBELONGSTOSCHEME);
	
		Collection<?> c = cls.getSubclasses(true);
		
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			OWLNamedClass allCls = (OWLNamedClass) iter.next();
			if(!allCls.getURI().equals(ModelConstants.CDOMAINCONCEPT)){
			        
				OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, allCls);
				if(ins != null){
					EConceptObject  cObj = new EConceptObject();
					cObj.setScheme(getSchemeOf(ins, belongToScheme));
					cObj.setConceptURI(allCls.getURI());
					cObj.setConceptIns(ins.getURI());
					cObj.setNamespace(allCls.getNamespace());
					cObj.setParentConceptObject(getParentConceptObject(allCls));
					// === Related
					cObj.setConcetpRelation(new ConceptServiceImpl().getConceptRelationship(owlModel, allCls.getName()));
					
					Collection<?> childList = allCls.getSubclasses(true);
					for (Iterator<?> iterator1 = childList.iterator(); iterator1.hasNext();) {
						OWLNamedClass child = (OWLNamedClass) iterator1.next();
						if(!child.getURI().equals(ModelConstants.CDOMAINCONCEPT)){
							cObj.addChild(child.getURI());
						}
					}
					
					Object createDate = ins.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED));
					Object updateDate = ins.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE));
					
					if(createDate != null && updateDate !=null){
						cObj.setCreateDate(createDate.toString());
						cObj.setUpdateDate(updateDate.toString());
					}
					
					Object status = ins.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASSTATUS));
					if(status!=null){
						cObj.setStatus(status.toString());
					}
					
					Collection<?> lexicon = ins.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION));
					for (Iterator<?> iter2 = lexicon.iterator(); iter2.hasNext();) {
						OWLIndividual termInstance = (OWLIndividual) iter2.next();
						ETermObject termObject = makeTermObject(owlModel, termInstance, cls);
						if(!termObject.equals(null))	cObj.addTerm(termObject);
					}
					agObj.addConceptList(cObj);
				}
			}
		}
		///owlModel.dispose();
		return agObj;
	}
	
}
