package org.fao.aoscs.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.query.larq.IndexBuilderString;
import com.hp.hpl.jena.query.larq.IndexLARQ;
import com.hp.hpl.jena.query.larq.LARQ;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.sparql.util.StringUtils;

import edu.stanford.smi.protegex.owl.model.OWLModel;

public class TestIndexing {

	public static void main(String args[]) {
		
		OWLModel owlModel = null;
		try 
		{
			
			String dbip = "158.108.33.132";
			String dbport = "3306"; 
			String dbuser = "root";
			String dbpassword = "onmodao";
			//String dbname = "agrovocaos";
			//String dbname = "fullagrovoc_20090423_1421";
			String dbname = "kulibraryrice";
			
			owlModel = getOWLModel("jdbc:mysql://"+dbip+":"+dbport+"/"+dbname+"?requireSSL=false&useUnicode=true&characterEncoding=UTF-8", dbname, dbuser, dbpassword);
			
			testIndex(owlModel);

		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally 
		{ 
			if(owlModel!=null)
				owlModel.dispose();
		}
	}
	
	
	public static void testIndex(OWLModel owlModel)
	{
		Date d = new Date();
		Model model = owlModel.getOntModel();
		
        IndexLARQ index = buildIndex(model) ;
        System.out.println("Total Time taken by indexing: "+((new Date().getTime()-d.getTime())/1000)+" secs.");
        
        // Search for 
        String searchString = "+rice" ;
        
        d = new Date();
        NodeIterator nIter = index.searchModelByIndex(searchString) ;
        int cnt=0;
        for ( ; nIter.hasNext() ; )
        {
          cnt++;
        	// if it's an index storing literals ...
          Literal lit = (Literal)nIter.nextNode() ;
          System.out.println(">>"+lit.getString());
        }
        System.out.println("cnt: "+cnt);
		System.out.println("total Time taken by api: "+((new Date().getTime()-d.getTime())/1000)+" secs.");
		
		d = new Date();
        String queryString = StringUtils.join("\n", new String[]{
            "PREFIX xsd:    <http://www.w3.org/2001/XMLSchema#>" ,
            "PREFIX :       <http://example/>" ,
            "PREFIX pf:     <http://jena.hpl.hp.com/ARQ/property#>",
            "SELECT * {" ,
            "    ?lit pf:textMatch '"+searchString+"'.",
            "}"
        }) ;
        
        performQuery(model, index, queryString) ;
        System.out.println("total Time taken by query: "+((new Date().getTime()-d.getTime())/1000)+" secs.");
        index.close() ;
	}
	
	public static IndexLARQ buildIndex(Model model)
    {
        // ---- Read and index all literal strings.
        IndexBuilderString larqBuilder = new IndexBuilderString() ;
        
        // Index statements as they are added to the model.
        model.register(larqBuilder) ;
        
        // To just build the index, create a model that does not store statements 
        // Model model2 = ModelFactory.createModelForGraph(new GraphSink()) ;
        
        //FileManager.get().readModel(model, datafile);
        
        // ---- Alternatively build the index after the model has been created. 
        larqBuilder.indexStatements(model.listStatements()) ;
        
        // ---- Finish indexing
        larqBuilder.closeForWriting() ;
        model.unregister(larqBuilder) ;
        
        // ---- Create the access index  
        IndexLARQ index = larqBuilder.getIndex() ;
        return index ; 
    }

	public static void performQuery(Model model, IndexLARQ index, String queryString)
    {  
        // Make globally available
        LARQ.setDefaultIndex(index) ;
        
        Query query = QueryFactory.create(queryString) ;
        query.serialize(System.out) ;
        System.out.println();
                                          
        QueryExecution qExec = QueryExecutionFactory.create(query, model) ;
        //LARQ.setDefaultIndex(qExec.getContext(), index) ;
        ResultSetFormatter.out(System.out, qExec.execSelect(), query) ;
        qExec.close() ;
    }
	

	public static OWLModel getOWLModel(String url, String dbname, String username, String password)
	{
		System.out.println("Loading model...");
		Date d = new Date();
		edu.stanford.smi.protegex.owl.database.OWLDatabaseKnowledgeBaseFactory factory = new edu.stanford.smi.protegex.owl.database.OWLDatabaseKnowledgeBaseFactory();         
		Collection<Object> errors = new ArrayList<Object>();
		edu.stanford.smi.protege.model.Project prj = edu.stanford.smi.protege.model.Project.createNewProject(factory, errors);
		edu.stanford.smi.protegex.owl.database.OWLDatabaseKnowledgeBaseFactory.setSources(prj.getSources(), "com.mysql.jdbc.Driver", url, dbname, username, password);
		prj.createDomainKnowledgeBase(factory, errors, true);
		OWLModel owlModel = (OWLModel) prj.getKnowledgeBase();
		System.out.println("Time taken to load model : "+((new Date().getTime()-d.getTime())/1000)+" secs.");
		return owlModel;
	}
	
	/*public static void useIndexing(OWLModel owlModel)
	{
		System.out.println("model: "+owlModel);
		Model model = owlModel.getJenaModel();
		
		 
		//-- Read and index all literal strings.
		IndexBuilderString larqBuilder = new IndexBuilderString() ;
		
		// -- Create an index based on existing statements
		larqBuilder.indexStatements(model.listStatements()) ;
		 
		// -- Finish indexing
		larqBuilder.closeForWriting();
		  
		// -- Create the access index  
		IndexLARQ index = larqBuilder.getIndex() ;
		
		// -- Make globally available
		LARQ.setDefaultIndex(index) ;
		  
		NodeIterator nIter = index.searchModelByIndex("+text") ;
		for ( ; nIter.hasNext() ; )
		{
			// if it's an index storing literals ...
			Literal lit = (Literal)nIter.nextNode() ;
		}
		  
		String queryString = "";
			 
		Query query = QueryFactory.create(queryString);
		QueryExecution qExec = QueryExecutionFactory.create(query, model) ;
		// -- Make available to this query execution only
		//	LARQ.setDefaultIndex(qExec.getContext(), index) ;
  
		try 
		{
			ResultSetFormatter.out(System.out, qExec.execSelect(), query);
		  } 
		finally 
		{ 
			qExec.close() ; 
		} 
	}*/
}
