package org.fao.aoscs.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.query.QueryResults;

public class SparqlQuery {

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
	
	public static void main(String args[]) {
		
		String squery = ""; 

		squery = " Select DISTINCT ?blank7 ?blank8 ?blank9 ?blank0  WHERE "+
						"{ "+
						"{ ?blank7 <http://www.w3.org/2000/01/rdf-schema#label> ?blank8 . } "+
						"{ ?blank7 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?blank9 . } "+
						"{ ?blank0 <http://www.fao.org/aims/aos/agrovoc#hasLexicalization> ?blank7 . } "+
						"{ ?blank0 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.fao.org/aims/aos/agrovoc#c_3699> . }  }";
		
		
		
		squery = "Select DISTINCT ?blank7 ?blank5 ?blank0 ?blank11  WHERE "+
						"{	 "+
						"{ ?blank5 <http://www.w3.org/2000/01/rdf-schema#label> ?blank7 . } "+
						"{ ?blank0 <http://www.fao.org/aims/aos/agrovoc#hasLexicalization> ?blank5 . } "+
						"{ ?blank0 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?blank11 . }  }";
		
		squery = "SELECT ?label ?term ?ins ?cls " +
		"WHERE { ?term <http://www.w3.org/2000/01/rdf-schema#label> ?label . " +
		"?ins <http://www.fao.org/aims/aos/agrovoc#hasLexicalization> ?term . " +
		"?ins <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?cls " +
		"FILTER (regex(str( ?label),\"water\",\"i\") ) . }";
		
		squery = " SELECT ?subject ?object  "+
		"WHERE { ?subject <http://www.w3.org/2000/01/rdf-schema#subClassOf> ?object }";

		
		
		
		
		OWLModel owlModel = null;
		try 
		{
			
			String dbip = "158.108.33.132";
			String dbport = "3306"; 
			String dbuser = "root";
			String dbpassword = "onmodao";
			String dbname = "agrovocaos";
			//String dbname = "fullagrovoc_20090423_1421";
			//String dbname = "kulibraryrice";
			
			owlModel = getOWLModel("jdbc:mysql://"+dbip+":"+dbport+"/"+dbname+"?requireSSL=false&useUnicode=true&characterEncoding=UTF-8", dbname, dbuser, dbpassword);
			
			long timeProtege = executeProtege(owlModel, squery);
			System.out.println("total Time taken by Protege: "+timeProtege+" secs.");

			long timeJena = executeJena(owlModel, squery);
			System.out.println("total Time taken by jena: "+timeJena+" secs.");
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
	
	public static long executeProtege(OWLModel owlModel, String queryString)
	{
		System.out.println("Starting Protege...");
		Date d = new Date();
		int cnt=0;
		try
		{
			QueryResults qr = owlModel.executeSPARQLQuery(queryString);
			while(qr.hasNext())
			{
				cnt++;
				Map<?, ?> map = qr.next();
				for(Object obj:map.keySet())
				{
					System.out.println(obj+" = "+map.get(obj));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("cnt: "+cnt);
		long time = ((new Date().getTime()-d.getTime())/1000);
		System.out.println("total Time taken: "+time+" secs.");
		System.out.println("Ended Protege...");
		return time;
	}
	
	public static long executeJena(OWLModel owlModel, String queryString)
	{
		System.out.println("Starting jena...");
		Date d = new Date();
		
		Model model = owlModel.getJenaModel();
		
		Query query = QueryFactory.create(queryString);

		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		int cnt=0;
		try 
		{
			
			ResultSet qr = qe.execSelect();
			List<?> list = qr.getResultVars();
			ResultSetFormatter.out(System.out, qr, query);
		    while (qr.hasNext())
		    {
		    	cnt++;
		    	QuerySolution soln = qr.nextSolution() ;
		    	for(int i=0;i<list.size();i++)
		    	{
		    		Object obj = list.get(i);
		    		RDFNode n = soln.get(""+obj);
		    		System.out.println(obj+" = "+n);
		    		/*// If you need to test the thing returned
		    		if ( n.isLiteral() )
		    		{
		    			((Literal)n).getLexicalForm() ;
		    		}
		    		if ( n.isResource() )
		    		{
		    			Resource r = (Resource)n ;
		    			if ( ! r.isAnon() )
		    			{
			        
		    			}
		    		}
		    		*/
		    	}
		    }
		}
		finally 
		{ 
			qe.close() ; 
			System.out.println("cnt: "+cnt);
		}
		long time = ((new Date().getTime()-d.getTime())/1000);
		System.out.println("total Time taken by jena: "+time+" secs.");
		System.out.println("Ended jena...");

		return time;
		
	}
	
	
}
