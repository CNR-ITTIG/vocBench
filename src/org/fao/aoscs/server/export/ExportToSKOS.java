package org.fao.aoscs.server.export;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.domain.ExportParameterObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.server.protege.ProtegeModelFactory;
import org.fao.aoscs.server.protege.ProtegeWorkbenchUtility;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;

import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;
import edu.stanford.smi.protegex.owl.model.RDFSNames;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLIndividual;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLNamedClass;
import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFSLiteral;

public class ExportToSKOS {

	public static String getExportSKOS(ExportParameterObject exp, OntologyInfo ontoInfo)
    {		
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		String data = getExportSKOS(owlModel, exp);
		return data;
    }
	
	public static String getExportSKOS(OWLModel owlModel, ExportParameterObject exp)
	{
		GenerateSKOS g = new GenerateSKOS();
		String generate = g.generateHead();
		boolean includeChildren = true;
		String className = ModelConstants.CDOMAINCONCEPT;
		OWLNamedClass nameClass = null;
		// IF CLASSIFICATION FILTER
		if(exp.getSchemeURI() != null)
		{
			className = ModelConstants.CCATEGORY;
			nameClass = owlModel.getOWLNamedClass(className);
			
			OWLIndividual schemeIns = owlModel.getOWLIndividual(owlModel.getResourceNameForURI(exp.getSchemeURI()));
			if(schemeIns != null)
			{			
				Collection<?> c = nameClass.getSubclasses(false);
				generate += g.generateConceptScheme(schemeIns.getNamespace(), schemeIns.getNamespacePrefix(), getConceptSchemeLabel(schemeIns), "FAO", "Free to all for non commercial use.", null, null );
				for (Iterator<?> iter = c.iterator(); iter.hasNext();) 
				{
					OWLNamedClass allCls = (OWLNamedClass) iter.next();
					if(!allCls.getName().equals(ModelConstants.CDOMAINCONCEPT))
					{	
						if(allCls.getNamespace().equals(schemeIns.getNamespace()))
						{
							generate += getConcept(owlModel, allCls, true, exp);
						}
						
					}
				}
			}
		}
		else 
		{
			if(exp.getConceptURI() != null)
			{
				className = owlModel.getResourceNameForURI(exp.getConceptURI());
				includeChildren = exp.isIncludeChildren();				
			}
			nameClass = owlModel.getOWLNamedClass(className);
			generate += g.generateConceptScheme();
			generate += getConcept(owlModel, nameClass, includeChildren, exp);
		}
		generate += g.generateEnd();
		return generate;
	}
	
	private static String getConceptSchemeLabel(OWLIndividual schemeIns)
	{
		
		String desc = "";
		for (Iterator<?> iterator = schemeIns.getLabels().iterator(); iterator.hasNext();) 
		{
			Object ob = iterator.next();
	    	if (ob instanceof DefaultRDFSLiteral) 
	    	{
	    		DefaultRDFSLiteral element = (DefaultRDFSLiteral) ob;
	    		if(element.getLanguage().equalsIgnoreCase("en"))
	    		{
	    			desc = element.getString();
	    		}
			}
		}
		return desc;				
	}
	
	/*private static ArrayList<OWLIndividual> getSchemeOf(OWLIndividual ins,OWLProperty belongToScheme)
	{
		ArrayList<OWLIndividual> list = new ArrayList<OWLIndividual>();
		
		Collection<?> c = ins.getPropertyValues(belongToScheme);
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) 
		{
			OWLIndividual element = (OWLIndividual) iter.next();
			list.add(element);
		}
		return list;
	}*/
	
	public static String getConcept(OWLModel owlModel, OWLNamedClass nameClass, boolean includeChildren, ExportParameterObject exp)
	{
		try
		{
			Collection<?> cc = nameClass.getInstances(false);
			String generate ="";
			String conceptURI = nameClass.getURI();
			GenerateSKOS g = new GenerateSKOS();
			String termSKOS = "";
			
			boolean flag = comparedate(owlModel, nameClass, exp);
			if(flag)
			{
				for (Iterator<?> iterator2 = cc.iterator(); iterator2.hasNext();) 
				{
					OWLIndividual insCls = (OWLIndividual) iterator2.next();
					OWLProperty prop =  owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION);
					
					Collection<?> ccc = insCls.getPropertyValues(prop);
					
					for (Iterator<?> iterator3 = ccc.iterator(); iterator3.hasNext();) 
					{
						OWLIndividual termIns = (OWLIndividual) iterator3.next();
						boolean flag2 = checkTermPropertyValue(owlModel, ModelConstants.RTERMEDITORIALDATATYPEPROPERTY, termIns, exp.getTermCodeRepositoryURI(), exp.getStartCode());
						if(flag2)
						{
							RDFSLiteral rrr = termIns.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RISMAINLABEL));
							boolean isMainLabel = Boolean.parseBoolean(rrr.getString());
							Collection<?>  cccc = termIns.getLabels();
							
							for (Iterator<?> iterator4 = cccc.iterator(); iterator4.hasNext();) 
							{
								DefaultRDFSLiteral defaultRDFSLiteral = (DefaultRDFSLiteral) iterator4.next();
								String termLabel = defaultRDFSLiteral.getString();
								String termLang = defaultRDFSLiteral.getLanguage();
								if(isMainLabel){
									termSKOS += g.generateprefLabel(termLang, termLabel);
								}else{
									termSKOS += g.generatealtLabel(termLang, termLabel);
								}
							}
						}
					}
				}
				if(!termSKOS.equals(""))
				{
					generate += g.generateHeadConcept(conceptURI);
					generate += termSKOS;
					generate += getScopeNote(owlModel, nameClass);
					generate += getEditorialNote(owlModel, nameClass);
					generate += getChangeNote(owlModel, nameClass);
					generate += getBroaderConcept(owlModel, nameClass);
					generate += getRelatedConcept(owlModel, nameClass);
					generate += getNarrowerConcept(owlModel, nameClass);
					generate += g.generateTailConcept();
				}
			}
			
			if(includeChildren)
			{
				ArrayList<String> childrenList = getConecptChildren(owlModel, nameClass);
				for(String cl: childrenList)
				{
					OWLNamedClass childClass = owlModel.getOWLNamedClass(cl);
					generate += getConcept(owlModel, childClass, includeChildren, exp);
				}
			}
			return generate;
		}catch (Exception e) 
		{
			e.printStackTrace();
			return "ERROR";
		}
	}
	
	public static ArrayList<OWLObjectProperty> getAllObjectProperties(OWLModel owlModel,OWLObjectProperty rootProp)
	{
		ArrayList<OWLObjectProperty> result = new ArrayList<OWLObjectProperty>();
		RDFProperty subPropertyOf = owlModel.getRDFProperty(RDFSNames.Slot.SUB_PROPERTY_OF);
		Collection<?> propList = owlModel.getRDFResourcesWithPropertyValue(subPropertyOf, rootProp);
		for (Iterator<?> iter = propList.iterator(); iter.hasNext();) {
			OWLObjectProperty prop = (OWLObjectProperty) iter.next();
			result.add(prop);
			getChildObjectProperty(prop, owlModel, result);
		}
		return result;
	}
	
	public static void getChildObjectProperty(OWLObjectProperty rootProp, OWLModel owlModel,ArrayList<OWLObjectProperty> result)
	{
		RDFProperty subClassOfProperty = owlModel.getRDFProperty(RDFSNames.Slot.SUB_PROPERTY_OF);
	    Collection<?> results = owlModel.getRDFResourcesWithPropertyValue(subClassOfProperty, rootProp);
	    for (Iterator<?> iter = results.iterator(); iter.hasNext();) 
	    {
	    	OWLObjectProperty childProp = (OWLObjectProperty) iter.next();
	    	result.add(childProp);
	    	getChildObjectProperty(childProp, owlModel,result);
		}
	    
	}
	
	public static String getScopeNote(OWLModel owlModel, OWLNamedClass nameClass)
	{
		OWLDatatypeProperty hasScopeNote = owlModel.getOWLDatatypeProperty(ModelConstants.RHASSCOPENOTE);
		Collection<?> c = nameClass.getInstances(false);
		String generate ="";
		GenerateSKOS g = new GenerateSKOS();
		for (Iterator<?> iterator2 = c.iterator(); iterator2.hasNext();) 
		{
			OWLIndividual insCls = (OWLIndividual) iterator2.next();
			Collection<?>  coll = insCls.getPropertyValues(hasScopeNote);
			for (Iterator<?> iterator = coll.iterator(); iterator.hasNext();) 
			{
				Object insObj = iterator.next();
				if(insObj instanceof DefaultRDFSLiteral)
				{
					DefaultRDFSLiteral de = (DefaultRDFSLiteral)insObj;
					String lang = de.getLanguage();
					String label = de.getString();
					generate += g.generateScopeNote(lang, label);
				}
			}
		}
		return generate;
	}
	
	public static String getEditorialNote(OWLModel owlModel, OWLNamedClass nameClass)
	{
		OWLDatatypeProperty hasScopeNote = owlModel.getOWLDatatypeProperty(ModelConstants.RHASEDITORIALNOTE);
		Collection<?> c = nameClass.getInstances(false);
		String generate ="";
		GenerateSKOS g = new GenerateSKOS();
		for (Iterator<?> iterator2 = c.iterator(); iterator2.hasNext();) 
		{
			OWLIndividual insCls = (OWLIndividual) iterator2.next();
			Collection<?>  coll = insCls.getPropertyValues(hasScopeNote);
			for (Iterator<?> iterator = coll.iterator(); iterator.hasNext();) 
			{
				Object insObj = iterator.next();
				if(insObj instanceof DefaultRDFSLiteral)
				{
					DefaultRDFSLiteral de = (DefaultRDFSLiteral)insObj;
					String lang = de.getLanguage();
					String label = de.getString();
					generate += g.generateEditorialNote(lang, label);
				}
			}
		}
		return generate;
	}
	
	public static String getChangeNote(OWLModel owlModel,OWLNamedClass nameClass)
	{
		OWLDatatypeProperty hasScopeNote = owlModel.getOWLDatatypeProperty(ModelConstants.RHASUPDATEDDATE);
		Collection<?> c = nameClass.getInstances(false);
		String generate ="";
		GenerateSKOS g = new GenerateSKOS();
		generate += g.generateChangeNoteHead();
		for (Iterator<?> iterator2 = c.iterator(); iterator2.hasNext();) 
		{
			OWLIndividual insCls = (OWLIndividual) iterator2.next();
			Collection<?>  coll = insCls.getPropertyValues(hasScopeNote);
			for (Iterator<?> iterator = coll.iterator(); iterator.hasNext();) 
			{
				Object insObj = iterator.next();
				generate += g.generateDate(insObj.toString());
			}
		}
		generate += g.generateChangeNoteEnd();
		return generate;
	}
	
	public static String getBroaderConcept(OWLModel owlModel, OWLNamedClass nameClass)
	{
		Collection<?> c = nameClass.getNamedSuperclasses(false);
		ArrayList<String> list = new ArrayList<String>();
		for(Iterator<?> it = c.iterator(); it.hasNext();)
		{
			DefaultOWLNamedClass owlName = (DefaultOWLNamedClass) it.next();
			if(!omitBroaderException(owlModel, owlName.getName()))
			{
				list.add(owlName.getURI());
			}
		}
		String generate ="";
		GenerateSKOS g = new GenerateSKOS();
		if(list.size()!=0)
		{
			for(int i=0; i<list.size(); i++)
			{
				generate += g.generateBroader(list.get(i).toString());
			}
		}
		return generate;
	}
	
	public static boolean omitBroaderException(OWLModel owlModel, String className)
	{
		ArrayList<String> broaderException = new ArrayList<String>();		
		
		String owlPrefix = owlModel.getNamespaceManager().getPrefix(ModelConstants.OWLNAMESPACE);
		if(owlPrefix != null)
		{
			broaderException.add(owlPrefix + ":" + ModelConstants.OWLTHING);
		}
		broaderException.add(ModelConstants.CCATEGORY);
		broaderException.add(ModelConstants.CDOMAINCONCEPT);
		if(broaderException.contains(className))
		{
			return true;
		}
		return false;
		
	}
	
	public static String getNarrowerConcept(OWLModel owlModel, OWLNamedClass nameClass)
	{
		Collection<?> c = nameClass.getNamedSubclasses(false);
		ArrayList<String> list = new ArrayList<String>();
		for(Iterator<?> it = c.iterator(); it.hasNext();)
		{
			DefaultOWLNamedClass owlName = (DefaultOWLNamedClass) it.next();
			list.add(owlName.getURI());
		}
		String generate ="";
		GenerateSKOS g = new GenerateSKOS();
		if(list.size()!=0)
		{
			for(int i=0; i<list.size(); i++)
			{
				generate += g.generateNarrower(list.get(i).toString());
			}
		}
		return generate;
	}
	
	public static String getRelatedConcept(OWLModel owlModel, OWLNamedClass nameClass)
	{
		OWLObjectProperty hasRelatedConcept = owlModel.getOWLObjectProperty(ModelConstants.RHASRELATEDCONCEPT);
		Collection<?> propList = getAllObjectProperties(owlModel, hasRelatedConcept);
		Collection<?> cc = nameClass.getInstances(false);
		ArrayList<String> list = new ArrayList<String>();
		for (Iterator<?> iterator2 = cc.iterator(); iterator2.hasNext();) 
		{
			OWLIndividual insCls = (OWLIndividual) iterator2.next();
			for (Iterator<?> iter = propList.iterator(); iter.hasNext();)
			{
				OWLObjectProperty prop = (OWLObjectProperty) iter.next();
				Collection<?>  coll = insCls.getPropertyValues(prop);
				for (Iterator<?> iterator = coll.iterator(); iterator.hasNext();) 
				{
					Object insObj = iterator.next();
					if (insObj instanceof DefaultOWLIndividual) 
					{
						DefaultOWLIndividual conceptIns = (DefaultOWLIndividual) insObj;
						OWLNamedClass destCls = owlModel.getOWLNamedClass(conceptIns.getProtegeType().getName());						
						list.add(destCls.getURI());
					}
				}
			}
		}
		String generate ="";
		GenerateSKOS g = new GenerateSKOS();
		if(list.size()!=0)
		{
			for(int i=0; i<list.size(); i++)
			{
				generate += g.generateRelated(list.get(i).toString());
			}
		}
		return generate;
	}

	// Get children of a concept
	public static ArrayList<String> getConecptChildren(OWLModel owlModel, OWLNamedClass rootCls)
	{
		ArrayList<String> list = new ArrayList<String>();
	    if(rootCls !=null)
	    {
			for(Iterator<?> it = rootCls.getSubclasses(false).iterator(); it.hasNext();) 
			{
				OWLNamedClass cls = (OWLNamedClass) it.next();
				list.add(cls.getName());
			}
	    }
		return list;
	}

	public static Boolean comparedate(OWLModel owlModel, OWLNamedClass nameClass, ExportParameterObject exp)
	{
		String startDate = exp.getStartDate();
        String endDate = exp.getEndDate();
        
		if(startDate == null && endDate == null)
		{
			return true;
		}
		else
		{
			OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, nameClass);
			
	        String datetype = exp.getDateType();
	        String compareDate = null;
	        
	        if(datetype.equalsIgnoreCase("create"))
	        {
	        	Object dateObj = ins.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED));
	        	if(dateObj!=null)
	        	{
	        		compareDate = (String)""+dateObj;
	        	}
	        }else if(datetype.equalsIgnoreCase("update"))
	        {
	        	Object dateObj = ins.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE));
	        	if(dateObj!=null)
	        	{
	        		compareDate = (String) ""+dateObj;
	        	}
	        }
	        if(compareDate != null)
	        {
	    		try 
	    		{
					if(compareDate != null)
					{
						DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
			    		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date compare = df2.parse(compareDate);
						if(startDate != null && endDate != null)
						{
							Date start = df1.parse(startDate);
							Date end = df1.parse(endDate);
							return (compare.equals(start) || compare.equals(end) || (compare.after(start) && compare.before(end)));
						}
						else if(startDate != null)
						{
							Date start = df1.parse(startDate);
							return (compare.equals(start));
						}
						else if(endDate != null)
						{
							Date end = df1.parse(endDate);
							return (compare.equals(end));
						}
						else
						{
							return false;
						}
					}
				} 
	    		catch (ParseException e) 
				{
					e.printStackTrace();
				}
	        }
	        else
	        {
	        	return false;
	        }
		}
		return false;
	}
	
	public static boolean checkTermPropertyValue(OWLModel owlModel, String property, OWLIndividual termInstance, String termProperty, String termPropertyValue)
	{
		OWLProperty nProp = owlModel.getOWLProperty(property);
	    if(termProperty == null || termPropertyValue == null)
	    	return true;
		if(termInstance!=null)
		{
		    for (Iterator<?> itr = nProp.getSubproperties(true).iterator(); itr.hasNext();) 
		    {
		    	OWLProperty prop = (OWLProperty) itr.next();
				Collection<?> labelList = prop.getLabels();
				// get property labels
				boolean go = false;
				
				for (Iterator<?> iterator = labelList.iterator(); iterator.hasNext();) 
				{
					Object obj = iterator.next();
					if (obj instanceof DefaultRDFSLiteral) 
					{
						DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
						String label = element.getString() + " (" + element.getLanguage() + ")";
						if(label.equalsIgnoreCase(termProperty))
						{
							go = true;
						}
						
					}
				}
				if(go)
				{
					// get property values
				    for (Iterator<?> iter = termInstance.getPropertyValues(prop).iterator(); iter.hasNext();) 
				    {
				    	Object obj = iter.next();
				        if (obj instanceof DefaultRDFSLiteral) 
				        {
							DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
							String value = element.getString() + " (" + element.getLanguage()+")";
					        if(termPropertyValue.equalsIgnoreCase(value))
					        {
					        	return true;
					        }
						}
				        else
				        {
				        	if(termPropertyValue.equalsIgnoreCase(""+obj))
				        	{
					        	return true;
				        	}
				        }
				    }
				}
		    }
		}
		return false;
	}
}
