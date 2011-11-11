package org.fao.aoscs.convert.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.server.utility.Date;

import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protegex.owl.database.CreateOWLDatabaseFromFileProjectPlugin;
import edu.stanford.smi.protegex.owl.database.OWLDatabaseKnowledgeBaseFactory;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;
import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFSLiteral;

public class ProtegeUtil {

	private static void createDB(String driver, String dbIp, String dbPort, String dbName, String dbUser, String dbPassword)
	{
		try 
		{
			ArrayList<String> sqlCommand = new ArrayList<String>();
			sqlCommand.add("DROP DATABASE  IF EXISTS `"+dbName+"`;");
			sqlCommand.add("CREATE DATABASE `"+dbName+"` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci");
			String dbUrl = "jdbc:mysql://"+dbIp+":"+dbPort+"/mysql?requireSSL=false&useUnicode=true&characterEncoding=UTF-8";
			executeBatchSQL(sqlCommand, driver, dbUrl, dbUser, dbPassword);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	
	public static void executeBatchSQL(ArrayList<String> sqlCommand, String driver, String dbUrl, String dbUser, String dbPassword)
	{
		Connection connection = null;
		Statement statement = null;

		try 
		{
			Class.forName(driver).newInstance();
			connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
			statement = connection.createStatement();
			for (String sql : sqlCommand) 
			{
				statement.execute(sql);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			if (statement != null) 
			{
				try 
				{
					statement.close();
				} 
				catch (SQLException e) 
				{
				} // nothing we can do
			}
			if (connection != null) 
			{
				try 
				{
					connection.close();
				} 
				catch (SQLException e) 
				{
				} // nothing we can do
			}
		}
	}
	
	
	private static void convertToDatabaseProjectStreaming(URI owlURI, String driver, String dbUrl, String dbName, String dbUser, String dbPassword) throws URISyntaxException {
		CreateOWLDatabaseFromFileProjectPlugin creator = new  CreateOWLDatabaseFromFileProjectPlugin();
		creator.setKnowledgeBaseFactory(new OWLDatabaseKnowledgeBaseFactory());
		creator.setDriver(driver);
		creator.setURL(dbUrl);
		creator.setTable(dbName);
		creator.setUsername(dbUser);
		creator.setPassword(dbPassword);
		creator.setOntologyFileURI(owlURI);
		creator.setUseExistingSources(true);
		
		Project p = creator.createProject();
		List<Object> errors = new ArrayList<Object>();
		p.save(errors);
	}
	
	private static OWLModel createOWLModel(String driver, String dbUrl, String dbName, String dbUser, String dbPassword){
		OWLDatabaseKnowledgeBaseFactory factory = new OWLDatabaseKnowledgeBaseFactory();         
		Collection<Object> errors = new ArrayList<Object>();
		Project prj = Project.createNewProject(factory, errors);
		OWLDatabaseKnowledgeBaseFactory.setSources(prj.getSources(), driver, dbUrl, dbName, dbUser, dbPassword);
		prj.createDomainKnowledgeBase(factory, errors, true);
		OWLModel owlModel = (OWLModel) prj.getKnowledgeBase();
		return owlModel;
	}
	
	public static OWLModel getOWLModel(String driver, String dbIp, String dbPort, String dbName, String dbUser, String dbPassword, String owlURI, String modelConstantsURI)
	{
		try 
		{
			String dbUrl = "jdbc:mysql://"+dbIp+":"+dbPort+"/"+dbName+"?requireSSL=false&useUnicode=true&characterEncoding=UTF-8";
			createDB(driver, dbIp, dbPort, dbName, dbUser, dbPassword);
			convertToDatabaseProjectStreaming(new URI(owlURI), driver, dbUrl, dbName, dbUser, dbPassword);
			ModelConstants.loadConstants(loadModelConstants(modelConstantsURI));
			OWLModel owlModel = createOWLModel(driver, dbUrl, dbName, dbUser, dbPassword);
			return owlModel;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
		
	public static HashMap<String, String> loadModelConstants(String modelConstantsURI)
	{
		HashMap<String, String> mcMap = new HashMap<String, String>();
		ResourceBundle rb = ResourceBundle.getBundle(modelConstantsURI);
		Enumeration<String> en = rb.getKeys();
		while(en.hasMoreElements())
		{
			String key = (String) en.nextElement();
			mcMap.put(key, rb.getString(key));
		}
		ModelConstants.loadConstants(mcMap);
		return mcMap;

	}
	
	/*public static void printNameSpaces(OWLModel owlModel)
	{
		NamespaceManager nameSpaceManager = owlModel.getNamespaceManager();
		System.out.println("Default Namespace: "+nameSpaceManager.getDefaultNamespace());
		System.out.println("Agrovoc Namespace Prefix: "+owlModel.getNamespaceManager().getPrefix(ModelConstants.AGROVOCBASENAMESPACE));
		 for (Iterator<String> iter = nameSpaceManager.getPrefixes().iterator(); iter.hasNext();) {
			 String prefix = iter.next();
		   System.out.println("prefix: "+prefix+" -- "+nameSpaceManager.getNamespaceForPrefix(prefix));
		 }
	}*/
	
	public static OWLIndividual createConcept(OWLModel owlModel, String parentName, String namespacePrefix, String name, String namePrefix, String status){
		
		OWLProperty createDate = owlModel.getOWLProperty(ModelConstants.RHASDATECREATED);
		OWLProperty statusProp = owlModel.getOWLProperty(ModelConstants.RHASSTATUS);

		//OWLNamedClass parentCls = owlModel.getOWLNamedClass(parentName);
		
		OWLNamedClass parentCls = null;
		parentCls = owlModel.getOWLNamedClass(parentName);
		if(parentCls==null)
		{
			parentCls = owlModel.createOWLNamedClass(parentName);
		}
		
		OWLNamedClass cls = null;
		cls = owlModel.getOWLNamedClass(namespacePrefix+":"+namePrefix+name);
		if(cls==null)
		{
			cls = owlModel.createOWLNamedClass(namespacePrefix+":"+namePrefix+name);
			cls.addSuperclass(parentCls);
		}
		
		OWLIndividual ins = null;
		ins = getConceptInstance(owlModel, cls);
		if(ins==null)
		{
			ins = (OWLIndividual) cls.createInstance(namespacePrefix+":"+"i_"+name);
			ins.setPropertyValue(createDate, getDateTime());
			ins.setPropertyValue(statusProp, status);
			setInstanceUpdateDate(owlModel, ins);
		}
		
		return ins;
		
	}
	
	public static OWLIndividual createTerm(OWLModel owlModel, String namespacePrefix, OWLIndividual ins, String name, String label, String lang, boolean isMainlabel, String status){

		OWLProperty lexicon = owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION);
		OWLProperty mainlabel = owlModel.getOWLProperty(ModelConstants.RISMAINLABEL);
		OWLProperty createDate = owlModel.getOWLProperty(ModelConstants.RHASDATECREATED);
		OWLProperty statusProp = owlModel.getOWLProperty(ModelConstants.RHASSTATUS);
		OWLNamedClass nounCls = owlModel.getOWLNamedClass(ModelConstants.CNOUN);
		
		OWLIndividual term =  (OWLIndividual) nounCls.createInstance(namespacePrefix+":"+"i_"+lang.toLowerCase()+"_"+name+"_"+new java.util.Date().getTime());
		term.setPropertyValue(mainlabel, isMainlabel);
		term.addLabel(label, lang);
		term.setPropertyValue(createDate, getDateTime());
		term.setPropertyValue(statusProp, status);
		setInstanceUpdateDate(owlModel, term);
		ins.addPropertyValue(lexicon, term);			
		setInstanceUpdateDate(owlModel, ins);
		
		return term;
	}

	public static OWLIndividual createDefinition(OWLModel owlModel, String namespacePrefix, OWLIndividual ins, String label, String lang){
		long timeStamp = new java.util.Date().getTime();
		
		String clsNamePrefix = namespacePrefix+":";
		
		OWLNamedClass defCls = owlModel.getOWLNamedClass(ModelConstants.CDEFINITION);
		OWLProperty hasDef = owlModel.getOWLProperty(ModelConstants.RHASDEFINITION);
		OWLProperty createDate = owlModel.getOWLProperty(ModelConstants.RHASDATECREATED);
		
		OWLIndividual defIns = defCls.createOWLIndividual(clsNamePrefix+"i_"+"def_"+timeStamp);
		defIns.addPropertyValue(createDate, Date.getDateTime());
				
		defIns.addLabel(label, lang);
		setInstanceUpdateDate(owlModel, defIns);
		ins.addPropertyValue(hasDef, defIns);		
		return defIns;		
	}
	
	public static OWLIndividual createDefinitionWithDate(OWLModel owlModel, String namespacePrefix, OWLIndividual ins, String label, String lang, String cdate){
		long timeStamp = new java.util.Date().getTime();
		
		String clsNamePrefix = namespacePrefix+":";
		
		OWLNamedClass defCls = owlModel.getOWLNamedClass(ModelConstants.CDEFINITION);
		OWLProperty hasDef = owlModel.getOWLProperty(ModelConstants.RHASDEFINITION);
		OWLProperty createDate = owlModel.getOWLProperty(ModelConstants.RHASDATECREATED);
		OWLProperty updateDate = owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE);		
		
		OWLIndividual defIns = defCls.createOWLIndividual(clsNamePrefix+"i_"+"def_"+timeStamp);
		defIns.addPropertyValue(createDate, "2008-02-18 12:00:00");
		defIns.addPropertyValue(updateDate, "2008-02-19 22:00:00");
		defIns.addLabel(label, lang);
		
		ins.addPropertyValue(hasDef, defIns);			
		return defIns;		
	}
	
	private static void setInstanceUpdateDate(OWLModel owlModel,OWLIndividual ins){
		OWLProperty updateDate = owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE);
		ins.setPropertyValue(updateDate, getDateTime());
	}
	
	@SuppressWarnings("unchecked")
	public static OWLIndividual getConceptInstance(OWLModel owlModel,OWLNamedClass owlCls){
		OWLIndividual individual = null;
		if(owlCls!=null){
			if(owlCls.getInstanceCount(false)>0){
				for (Iterator iter = owlCls.getInstances(false).iterator(); iter.hasNext();) {
					Object obj = iter.next();
					if (obj instanceof OWLIndividual) {
						individual = (OWLIndividual) obj;
					}
				}
			}
		}
		return individual;
	}
	
	@SuppressWarnings("unchecked")
	public static OWLIndividual getTerm(OWLModel owlModel, OWLIndividual conceptIns, String label, String lang){
		OWLProperty lexicon = owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION);
		for (Iterator iter = conceptIns.getPropertyValues(lexicon).iterator(); iter.hasNext();) {
			Object obj = iter.next();
				if (obj instanceof OWLIndividual) {
					OWLIndividual termInstance = (OWLIndividual) obj;
					for (Iterator iterator = termInstance.getLabels().iterator(); iterator.hasNext();) {
						Object ob = iterator.next();
				    	if (ob instanceof DefaultRDFSLiteral) {
				    		DefaultRDFSLiteral element = (DefaultRDFSLiteral) ob;
				    		if(element.getString().equals(label) && element.getLanguage().equalsIgnoreCase(lang))
				    		{
				    			return termInstance;
				    		}
						}
					}
				}
		}
		return null;
	}
	
	public static void addTermRelationship(OWLModel owlModel, String property, OWLIndividual termIns, OWLIndividual destTermIns, OWLIndividual ins){

		OWLProperty owlProperty = owlModel.getOWLProperty(property);
		termIns.addPropertyValue(owlProperty, destTermIns);
		setInstanceUpdateDate(owlModel, termIns);
		setInstanceUpdateDate(owlModel, ins);
	
	}
	
	public static void addSpellingVariation(OWLModel owlModel, OWLIndividual termIns, String label, String lang, OWLIndividual ins){
		//Create Spelling Variation
		if(label!=null  && !label.equals(""))
		{
			OWLProperty owlProperty = owlModel.getOWLProperty(ModelConstants.RHASSPELLINGVARIANT);
			RDFSLiteral val = owlModel.createRDFSLiteral(label, lang);
			termIns.addPropertyValue(owlProperty, val);
			setInstanceUpdateDate(owlModel, termIns);
			setInstanceUpdateDate(owlModel, ins);
		}
	
	}
	
	public static void addDatatypeProperty(OWLModel owlModel, OWLIndividual ins, String property, String val){

		if(val!=null && !val.equals(""))
		{
			OWLProperty codeProperty = owlModel.getOWLProperty(property);
			ins.addPropertyValue(codeProperty, val);
		}
	}
	
	public static void addDatatypeProperty(OWLModel owlModel, OWLIndividual ins, String property, String label, String lang){

		if(label!=null && !label.equals("") && lang!=null && !lang.equals(""))
		{
			OWLProperty codeProperty = owlModel.getOWLProperty(property);
			RDFSLiteral val = owlModel.createRDFSLiteral(label, lang);
			ins.addPropertyValue(codeProperty, val);
		}
	}
	
	public static void addConcept2ConceptRelationship(OWLModel owlModel, String property, OWLIndividual conceptIns, OWLIndividual destConceptIns){

		if(destConceptIns!=null)
		{
			OWLProperty owlProperty = owlModel.getOWLProperty(property);
			if(owlProperty!=null)
			{
			conceptIns.addPropertyValue(owlProperty, destConceptIns);
			setInstanceUpdateDate(owlModel, conceptIns);
			}
			else
			{
				System.out.println("No property available: "+ property);
				
			}
		}
	}
	
	public static String getDateTime(){
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"));
		Locale.setDefault(new Locale("en", "US"));  
		String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
		java.text.SimpleDateFormat sdf =  new java.text.SimpleDateFormat(DATE_FORMAT);
		return sdf.format(cal.getTime());
	}
}
