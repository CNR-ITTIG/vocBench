package org.fao.aoscs.convert;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFReader;

import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.storage.database.DatabaseKnowledgeBaseFactory;
import edu.stanford.smi.protege.util.PropertyList;
import edu.stanford.smi.protegex.owl.database.CreateOWLDatabaseFromFileProjectPlugin;
import edu.stanford.smi.protegex.owl.database.OWLDatabaseKnowledgeBaseFactory;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStore;
import edu.stanford.smi.protegex.owl.writer.rdfxml.rdfwriter.OWLModelAllTripleStoresWriter;
import edu.stanford.smi.protegex.owl.writer.rdfxml.rdfwriter.OWLModelWriter;

public class convertToDatabaseProject {
	private final static String driver = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://localhost:3306/agrovocwb_hindi_v_0_2_1_beta?requireSSL=false&useUnicode=true&characterEncoding=UTF-8";	   
	private static String table = "agrovoc_wb_20100913";
	private static String user = "fao";
	private static String password = "faomimos";
	private static String uri = "file:/D:/workspace/aos_cs_1_v_0_3/AOS/modules0.2/ag_all_hindi3.owl";
	private static int useMethod = 0;

	
	public static void main (String[] args) throws Exception
    {
    	if (args.length < 5) 
			{
		   System.out.println("Argument not sufficients!");
		   System.out.println("Usage:\n convertToDatabase <server address> <database> <owl-file> <table> <username> <password> <use-method>");
		   System.exit(1);
    	}
    	try 
		{
		   url = "jdbc:mysql://"+args[0]+"/"+args[1]+"?requireSSL=false&useUnicode=true&characterEncoding=UTF-8";
   	       uri = "file:/" + args[2];
		   table = args[3];
		   user = args[4];
		   useMethod = Integer.parseInt(args[6]);
		   if (args.length > 5)   password = args[5];

		   //convertToDatabaseProjectStreaming(new URI(uri));
		   Date d = new Date();
		   SimpleDateFormat df = new SimpleDateFormat("yyyymmdd");
		   String owlFile = uri + table + "_" + df.format(d);
		   convertDB2OWLFile(owlFile, useMethod);

    	} 
		catch (Exception e) 
		{
			e.printStackTrace();
    	}
    }
   
    public static void convertToDatabaseProjectWithoutStreaming(OWLModel fileModel) throws Exception {
        System.out.println("In Convert to Database Project");
        List errors = new ArrayList();
        Project fileProject = fileModel.getProject();
        OWLDatabaseKnowledgeBaseFactory factory = new OWLDatabaseKnowledgeBaseFactory();
        PropertyList sources = PropertyList.create(fileProject.getInternalProjectKnowledgeBase());
     
        DatabaseKnowledgeBaseFactory.setSources(sources, driver, url, table, user, password);
        factory.saveKnowledgeBase(fileModel, sources, errors);

        Project dbProject = Project.createNewProject(factory, errors);
        DatabaseKnowledgeBaseFactory.setSources(dbProject.getSources(), driver, url, table, user, password);

        dbProject.createDomainKnowledgeBase(factory, errors, true);
        dbProject.setProjectURI(new URI(uri));
        dbProject.save(errors);
        
        fileModel.dispose();
    }
	   
	public static void convertToDatabaseProjectStreaming(URI uri) throws URISyntaxException {
       CreateOWLDatabaseFromFileProjectPlugin creator = new  CreateOWLDatabaseFromFileProjectPlugin();
       creator.setKnowledgeBaseFactory(new OWLDatabaseKnowledgeBaseFactory());
       creator.setDriver(driver);
       creator.setURL(url);
       creator.setTable(table);
       creator.setUsername(user);
       creator.setPassword(password);
       creator.setOntologyFileURI(uri);
       creator.setUseExistingSources(true);

       Project p = creator.createProject();
       List errors = new ArrayList();
       p.save(errors);
	}

	public static void useOWLModelAllTripleStoresWriter(OWLModel om, String filename) throws Exception 
	{
	    try 
	    {
	    	OWLModelAllTripleStoresWriter omw = new OWLModelAllTripleStoresWriter(om, new URI(filename+"_useOWLModelAllTripleStoresWriter.owl"), false );
	    	omw.write();
	    } 
	    catch (Exception e) 
	    {
	           e.printStackTrace();
	    }
	}
	
	public static void useActiveTripleStore(OWLModel om, String filename) throws Exception 
    {
        try 
        {
            FileWriter writer = new FileWriter(new File(new URI(filename+"_useActiveTripleStore.owl")));
            OWLModelWriter omw = new OWLModelWriter(om, om.getTripleStoreModel().getActiveTripleStore(), writer);
            omw.write();
            writer.flush();
            writer.close();
        } 
        catch (Exception e) 
        {
               e.printStackTrace();
        }
    }
	
	public static void userTopTripleStore(OWLModel om, String filename) throws Exception 
    {
        try 
        {
            FileOutputStream fos = new FileOutputStream(new File(new URI(filename+"_userTopTripleStore.owl")));
            TripleStore ts = om.getTripleStoreModel().getTopTripleStore();
            OutputStreamWriter osw = new OutputStreamWriter(fos); 
            OWLModelWriter writer = new OWLModelWriter(om, ts, osw);
            writer.write();  
        } 
        catch (Exception e) 
        {
               e.printStackTrace();
        }
    }

	public static void convertDB2NTFile(OWLModel om, String filename){
		 try 
        {
			Model m = ModelFactory.createDefaultModel();
			InputStreamReader isr = new InputStreamReader(new FileInputStream(new File(new URI(filename+".owl")))); 
			
			RDFReader r = m.getReader("RDF/XML");
		    r.read(m, isr, "http://aims.fao.org/aos/") ;
			
			//m.read(isr, "N-TRIPLE");
			System.out.println("size: "+m.size());  
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(new File(new URI(filename+".nt")))); 
            m.write(osw, "N-TRIPLE");
        } 
        catch (Exception e) 
        {
               e.printStackTrace();
        }

	}
	
	public static void convertDB2OWLFile(String filename, int useMethod)
	{
		try
		{
		    OWLModel owlModel = getOWLModel();		    
		    Date d = new Date();
		    System.out.println("Starting conversion");
		    
		    switch(useMethod){
		    case 0: 
		        System.out.println("Conversion using useActiveTripleStore");
		        useActiveTripleStore(owlModel, filename);
		        break;
		    case 1: 
		        System.out.println("Conversion using useOWLModelAllTripleStoresWriter");
		        useOWLModelAllTripleStoresWriter(owlModel, filename);
		        break;
		    case 2: 
		        System.out.println("Conversion using userTopTripleStore");
		        userTopTripleStore(owlModel, filename);
		        break;
			case 3: 
		        System.out.println("Conversion for .NT");
		        convertDB2NTFile(owlModel, filename);
		        break;
		    }
			System.out.println((new Date().getTime() - d.getTime())/1000);
		    owlModel.dispose();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static OWLModel getOWLModel(){
		OWLDatabaseKnowledgeBaseFactory factory = new OWLDatabaseKnowledgeBaseFactory();         
		Collection errors = new ArrayList();
		Project prj = Project.createNewProject(factory, errors);
		OWLDatabaseKnowledgeBaseFactory.setSources(prj.getSources(), driver, url, table, user, password);
		prj.createDomainKnowledgeBase(factory, errors, true);
		OWLModel owlModel = (OWLModel) prj.getKnowledgeBase();
		return owlModel;
	}
}





