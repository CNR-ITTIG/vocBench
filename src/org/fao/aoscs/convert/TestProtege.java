package org.fao.aoscs.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.fao.aoscs.client.module.constant.ModelConstants;

import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protegex.owl.database.OWLDatabaseKnowledgeBaseFactory;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSNames;
import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFSLiteral;

public class TestProtege {
	
	private final static String driver = "com.mysql.jdbc.Driver";
	private final static String table = "oldprotege";
	private final static String url = "jdbc:mysql://vivaldi.cpe.ku.ac.th:3306/"+table+"?requireSSL=false&useUnicode=true&characterEncoding=UTF-8";
	private final static String user = "root";
	private final static String password = "onmodao";

	public HashMap<String, String> loadModelConstants()
	{
		HashMap<String, String> mcMap = new HashMap<String, String>();
		ResourceBundle rb = ResourceBundle.getBundle("org.fao.aoscs.server.ModelConstants");
		Enumeration<String> en = rb.getKeys();
		while(en.hasMoreElements())
		{
			String key = (String) en.nextElement();
			mcMap.put(key, rb.getString(key));
		}
		ModelConstants.loadConstants(mcMap);
		return mcMap;

	}
	
	public static void main(String args[]) {
		
	try 
		{
		TestProtege testProtege = new TestProtege();
		ModelConstants.loadConstants(testProtege.loadModelConstants());
		
		Date d = new Date();
		System.out.println("Starting to get data from mysql ");
		OWLModel owlModel = testProtege.getOWLModel();
		System.out.println("Time to load owl model : "+(new Date().getTime()-d.getTime())/1000+" secs");
		
		String rootNode = ModelConstants.CDOMAINCONCEPT;
		testProtege.getConceptTree(owlModel, rootNode);
		
		/*String rootProp = ModelConstants.RWBOBJECTPROPERTY;
		rootProp= "hasRelatedTerm";
		testProtege.getObjectPropertyTree(owlModel, owlModel.getOWLObjectProperty(rootProp));*/

		System.out.println("Time to load owl model : "+(new Date().getTime()-d.getTime())/1000+" secs");
		owlModel.dispose();
		System.out.println("Time to get data from mysql : "+(new Date().getTime()-d.getTime())/1000+" secs");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public OWLModel getOWLModel(){
		OWLDatabaseKnowledgeBaseFactory factory = new OWLDatabaseKnowledgeBaseFactory();         
		Collection<Object> errors = new ArrayList<Object>();
		Project prj = Project.createNewProject(factory, errors);
		OWLDatabaseKnowledgeBaseFactory.setSources(prj.getSources(), driver, url, table, user, password);
		prj.createDomainKnowledgeBase(factory, errors, true);
		OWLModel owlModel = (OWLModel) prj.getKnowledgeBase();
		return owlModel;
	}
	
	public void getConceptTree(OWLModel owlModel, String rootNode){
		OWLNamedClass rootCls = owlModel.getOWLNamedClass(rootNode);
		printOWLNamedClassInfo(rootCls);
	    if(rootCls !=null){
			for(Iterator<?> it = rootCls.getSubclasses(true).iterator(); it.hasNext();) 
			{
				OWLNamedClass cls = (OWLNamedClass) it.next();
				OWLIndividual instance = getConceptInstance(owlModel, cls);
				printOWLIndividualInfo(instance);
				getTermInfo(owlModel, instance);
			}
			
	    }	
	}
	
	public void getTermInfo(OWLModel owlModel, OWLIndividual instance)
	{
		for (Iterator<?> iter2 = instance.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION)).iterator(); iter2.hasNext();) 
		{
			OWLIndividual termInstance = (OWLIndividual) iter2.next();
			for (Iterator<?> iterator = termInstance.getLabels().iterator(); iterator.hasNext();) {
				Object obj = iterator.next();
		    	if (obj instanceof DefaultRDFSLiteral) {
		    		DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
		    		String label = element.getString();
		    		String lang = element.getLanguage().toLowerCase();
		    		System.out.println(label+" ("+lang+")");
		    	}
			}
		}
	}
	
	public OWLIndividual getConceptInstance(OWLModel owlModel,OWLNamedClass owlCls){
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
	
	public void getObjectPropertyTree(OWLModel owlModel, OWLObjectProperty rootProp)
	{
		RDFProperty subClassOfProperty = owlModel.getRDFProperty(RDFSNames.Slot.SUB_PROPERTY_OF);
		for(Iterator<?> iter = owlModel.getRDFResourcesWithPropertyValue(subClassOfProperty, rootProp).iterator(); iter.hasNext();) 
		{
			OWLObjectProperty prop = (OWLObjectProperty) iter.next();
			printOWLObjectPropertyInfo(prop);
			getObjectPropertyTree(owlModel, prop);			
		}		

	}
	
	public void printOWLNamedClassInfo(OWLNamedClass rootCls)
	{
		/*System.out.println("---------------------------------------");
		System.out.println("uri: "+rootCls.getURI());
		System.out.println("name: "+rootCls.getName());
		System.out.println("browser text: "+rootCls.getBrowserText());
		System.out.println("namespace: "+rootCls.getNamespace());
		System.out.println("namespaceprefix: "+rootCls.getNamespacePrefix());
		System.out.println("---------------------------------------");*/
	}
	
	public void printOWLIndividualInfo(OWLIndividual ind)
	{
		/*System.out.println("---------------------------------------");
		System.out.println("uri: "+ind.getURI());
		System.out.println("name: "+ind.getName());
		System.out.println("browser text: "+ind.getBrowserText());
		System.out.println("namespace: "+ind.getNamespace());
		System.out.println("namespaceprefix: "+ind.getNamespacePrefix());
		System.out.println("---------------------------------------");*/
	}
	
	public void printOWLObjectPropertyInfo(OWLObjectProperty prop)
	{
		/*System.out.println("---------------------------------------");
		System.out.println("uri: "+prop.getURI());
		System.out.println("name: "+prop.getName());
		System.out.println("browser text: "+prop.getBrowserText());
		System.out.println("namespace: "+prop.getNamespace());
		System.out.println("namespaceprefix: "+prop.getNamespacePrefix());
		System.out.println("---------------------------------------");*/
	}
}
