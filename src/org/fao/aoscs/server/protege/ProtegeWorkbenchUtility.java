package org.fao.aoscs.server.protege;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.client.module.constant.OWLStatusConstants;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.TermObject;
import org.fao.aoscs.domain.TreeObject;

import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFSLiteral;

public class ProtegeWorkbenchUtility {
	
	public static Date getDate(String date) 
	{
		if(!date.equals("null"))
		{
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			if(date.length()<11) date += " 00:00:00";
			try
			{
				return df.parse(date);
			}
			catch(Exception e)
			{
				return getDate(date, "EEE MMM d  HH:mm:ss z yyyy") ;
			}
		}
		else
			return null;
	}
	
	public static Date getDate(String date, String format) 
	{
		if(!date.equals("null"))
		{
			SimpleDateFormat df = new SimpleDateFormat(format);
			
			if(date.length()<11) date += " 00:00:00";
			try
			{
				return df.parse(date);
			}
			catch(Exception e)
			{
				return null;
			}
		}
		else
			return null;
	}
	
	public static OWLIndividual getConceptInstance(OWLModel owlModel,OWLNamedClass owlCls){
		OWLIndividual individual = null;
		if(owlCls!=null){
			if(owlCls.getInstanceCount(false)>0){
				for (Iterator<?> iter = owlCls.getInstances(false).iterator(); iter.hasNext();) {
					Object obj = iter.next();
					if (obj instanceof OWLIndividual) {
						individual = (OWLIndividual) obj;
					}
				}
			}
		}
		return individual;
	}
	
	public static OWLIndividual getConceptPropertyValue(OWLModel owlModel,OWLIndividual conceptIns ,OWLProperty prop,String insURI){
		OWLIndividual individual = null;
		for (Iterator<?> iter = conceptIns.getPropertyValues(prop).iterator(); iter.hasNext();) {
			Object obj = iter.next();
				if (obj instanceof OWLIndividual) {
					OWLIndividual indl = (OWLIndividual) obj;
					if(indl.getURI().equals(insURI)){
						individual = indl;
					}
				}
		}
		return individual;
	}
	
	public static TreeObject makeTreeObject(OWLModel owlModel, OWLNamedClass cls, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList){
		TreeObject treeObject = null;
		try
		{
			OWLIndividual instance = getConceptInstance(owlModel, cls);
			
			if(instance != null)
			{
				treeObject = new TreeObject();
				treeObject.setInstance(instance.getURI());
				treeObject.setUri(cls.getURI());
				treeObject.setName(cls.getName());
				treeObject.setNameSpace(instance.getNamespace());
				treeObject.setHasChild(cls.getSubclassCount()>0);
				Object cstatus = instance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASSTATUS));
				if(cstatus!=null)	treeObject.setStatus(cstatus.toString());
				
				ArrayList<String> sortedList = new ArrayList<String>();
				HashMap<String, Boolean> checkMainLabelList = new HashMap<String, Boolean>();
				
				for (Iterator<?> iter2 = instance.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION)).iterator(); iter2.hasNext();) 
				{
					OWLIndividual termInstance = (OWLIndividual) iter2.next();
					
					Object status = termInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASSTATUS));
					
					boolean isMainLabel = false;
					Object prefObj = termInstance.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RISMAINLABEL));
					if(prefObj!=null)
						isMainLabel = Boolean.valueOf(prefObj.toString());
					for (Iterator<?> iterator = termInstance.getLabels().iterator(); iterator.hasNext();) {
						Object obj = iterator.next();
				    	if (obj instanceof DefaultRDFSLiteral) {
				    		DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
				    		String label = element.getString();
				    		String lang = element.getLanguage().toLowerCase();
				    		if(!showAlsoNonpreferredTerms){
								if(isMainLabel && langList.contains(lang) && checkDeprecated(status.toString(), isHideDeprecated) ){
									sortedList.add(lang+"###"+label);
								}			
							}else{
								if(langList.contains(lang) && checkDeprecated(status.toString(), isHideDeprecated)){
									sortedList.add(lang+"###"+label);
									checkMainLabelList.put(lang+"###"+label, isMainLabel);
								}			
							}
				    	}
					}
				}
				Collections.sort(sortedList, String.CASE_INSENSITIVE_ORDER);
				String termLabel = "";
				for (int i = 0; i < sortedList.size(); i++) {
					String str =  (String) sortedList.get(i);
					String[] element = str.split("###");
					String separator = "; ";
					if(i==(sortedList.size()-1))
						separator = "";
					if(element.length==2){
						if(checkMainLabelList.get(str) != null && checkMainLabelList.get(str))
						{
							termLabel = termLabel + "<b>"+ element[1] + " ("+element[0]+")"+separator+"</b>";
						}
						else
						{
							termLabel = termLabel + element[1] + " ("+element[0]+")"+separator;
						}
					}
				}
				if(termLabel.length()==0)
					termLabel = "###EMPTY###"+treeObject.getUri();
				treeObject.setLabel(termLabel);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return treeObject;
	}
	
	public static boolean checkDeprecated(String status, boolean isHideDeprecated)
    {
        boolean chk = true;
        if (isHideDeprecated)
        {
            if (/*
                 * status.equals(OWLStatusConstants.PROPOSED_DEPRECATED_STRINGVALUE
                 * ) ||
                 */status.equals(OWLStatusConstants.DEPRECATED))
                chk = false;
        }
        return chk;
    }
	
	public static ConceptObject makeConceptObject(OWLModel owlModel,OWLNamedClass cls){
		OWLIndividual conceptIns = getConceptInstance(owlModel, cls);

		ConceptObject cObj = new ConceptObject();
		if(conceptIns != null)
		{
			cObj.setConceptInstance(conceptIns.getURI());	
			cObj.setName(cls.getName());
			cObj.setNameSpace(conceptIns.getNamespace());
			cObj.setUri(((OWLNamedClass)conceptIns.getProtegeType()).getURI());
			if(cls.getSubclassCount()>0){
	    		cObj.setHasChild(true);
	    	}else{
	    		cObj.setHasChild(false);
	    	}
	    	
			Object createDate = conceptIns.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED));
			Object updateDate = conceptIns.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE));
			try {
				if(createDate != null && updateDate !=null){
					cObj.setDateCreate(getDate(""+createDate));
					cObj.setDateModified(getDate(""+updateDate));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			Object status = conceptIns.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASSTATUS));
			if(status!=null){
				cObj.setStatus(status.toString());
				cObj.setStatusID(OWLStatusConstants.getOWLStatusID(status.toString()));
			}
			
			for (Iterator<?> iter2 = conceptIns.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION)).iterator(); iter2.hasNext();) {
				OWLIndividual termInstance = (OWLIndividual) iter2.next();
				TermObject termObject = makeTermObject(owlModel, termInstance, cls);
				cObj.addTerm(termObject.getUri(), termObject);
			}
		}	
		return cObj;
	}
	
	
	public static TermObject makeTermObject(OWLModel owlModel,OWLIndividual termInstance,OWLNamedClass owlCls){
		TermObject termObject = new TermObject();
		termObject.setConceptUri(owlCls.getURI());
		termObject.setConceptName(owlCls.getName());
		termObject.setUri(termInstance.getURI());
		termObject.setName(termInstance.getName());
		
		Object createDate = termInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED));
		Object updateDate = termInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE));
		try {
			if(createDate != null && updateDate !=null){
				termObject.setDateCreate(getDate(""+createDate));
				termObject.setDateModified(getDate(""+updateDate));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		Object status = termInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASSTATUS));
		if(status!=null){
			termObject.setStatus(status.toString());
			termObject.setStatusID(OWLStatusConstants.getOWLStatusID(status.toString()));
		}

		Object mainLabel = termInstance.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RISMAINLABEL));
		if(mainLabel != null){
			termObject.setMainLabel(Boolean.valueOf(mainLabel.toString()));
		}
		
		for (Iterator<?> iterator = termInstance.getLabels().iterator(); iterator.hasNext();) {
			Object obj = iterator.next();
	    	if (obj instanceof DefaultRDFSLiteral) {
	    		DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
	    		termObject.setLabel(element.getString());
	    		termObject.setLang(element.getLanguage().toLowerCase());
			}
		}
		return termObject;
	}
	
	public static RelationshipObject makeDatatypeRelationshipObject(OWLDatatypeProperty prop){
		RelationshipObject rObj = new RelationshipObject();
		rObj.setName(prop.getName());
		rObj.setUri(prop.getURI());
		rObj.setType(RelationshipObject.DATATYPE);
		for (Iterator<?> iterator = prop.getLabels().iterator(); iterator.hasNext();) {
			Object obj = iterator.next();
	    	if (obj instanceof DefaultRDFSLiteral) {
	    		DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
	    		rObj.addLabel(element.getString(), element.getLanguage());
			}
		}
		return rObj;
	}
	
	public static RelationshipObject makeObjectRelationshipObject(OWLObjectProperty prop){
		RelationshipObject rObj = new RelationshipObject();
		rObj.setName(prop.getName());
		rObj.setUri(prop.getURI());
		rObj.setType(RelationshipObject.OBJECT);
		for (Iterator<?> iterator = prop.getLabels().iterator(); iterator.hasNext();) {
			Object obj = iterator.next();
	    	if (obj instanceof DefaultRDFSLiteral) {
	    		DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
	    		rObj.addLabel(element.getString(), element.getLanguage());
			}
		}
		return rObj;
	}
	
	public static String getNameFromURI(OWLModel owlModel, String uri)
	{
		String name = "";
		String namespace = owlModel.getNamespaceForURI(uri);
		name = uri.split(namespace)[1];
		if(!namespace.equals(owlModel.getNamespaceManager().getDefaultNamespace()))
		{
			String prefix = owlModel.getNamespaceManager().getPrefix(namespace);
			name = prefix+":"+name;
		}
		return name;
	}
}
