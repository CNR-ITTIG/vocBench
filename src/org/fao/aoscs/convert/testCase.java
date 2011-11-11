package org.fao.aoscs.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.DomainRangeDatatypeObject;
import org.fao.aoscs.domain.ExportParameterObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.SchemeObject;
import org.fao.aoscs.domain.SearchParameterObject;
import org.fao.aoscs.domain.TermObject;
import org.fao.aoscs.server.SearchServiceImpl;
import org.fao.aoscs.server.export.ExportSQL;
import org.fao.aoscs.server.protege.ProtegeModelFactory;
import org.fao.aoscs.server.protege.ProtegeWorkbenchUtility;

import edu.stanford.smi.protegex.owl.model.AbstractTask;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSDatatype;
import edu.stanford.smi.protegex.owl.model.RDFSNames;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLDataRange;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFSLiteral;

public class testCase {
	
	public static HashMap<String, String> loadModelConstants()
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
	
	public static OntologyInfo getOntoInfo()
	{
	    loadModelConstants();
		OntologyInfo ontoInfo = new OntologyInfo();
		ontoInfo.setDbDriver("com.mysql.jdbc.Driver");
		
		//ontoInfo.setDbUrl("jdbc:mysql://158.108.33.132:3306/agrovoc_wb_21102009?requireSSL=false&useUnicode=true&characterEncoding=UTF-8");			
		//ontoInfo.setDbTableName("agrovoc_wb_21102009");
		//ontoInfo.setOntologyName("AGROVOC_WB_SMALL");

		/*ontoInfo.setDbUrl("jdbc:mysql://158.108.33.132:3306/agrovoc_wb_02092009?requireSSL=false&useUnicode=true&characterEncoding=UTF-8");
		ontoInfo.setDbTableName("agrovoc_wb_02092009");
		ontoInfo.setOntologyName("AGROVOC_WB_SMALL");*/
		
		ontoInfo.setDbUrl("jdbc:mysql://localhost:3306/agrovoc_wb_small?requireSSL=false&useUnicode=true&characterEncoding=UTF-8");
		ontoInfo.setDbTableName("agrovoc_wb_small");
		//ontoInfo.setOntologyName("fullagrovoc_20090904_1421");
		
		ontoInfo.setDbUsername("fao");
		ontoInfo.setDbPassword("faomimos");
		
		return ontoInfo;
	}
	
	public static void main(String args[]) {
		
		/*try {
			testCase.testSearch(getOntoInfo());
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		String q = "";
		for(int h=1; h<=8; h++){
			for(int i=1; i<=75; i++){
				for(int j=1; j<=8; j++){
					if(h == 8)
						h = 12;
					if(j != 6 && j != 6 )
						q += "INSERT INTO permission_functionality_map VALUES ("+i+", "+h+", "+j+"); \n";
				}
			}
		}
		System.out.println(q);
			
	}
	
	@SuppressWarnings("unused")
	private void runTask(OWLModel owlModel) throws Exception 
	{
		AbstractTask task = new AbstractTask("Importing ", false, owlModel.getTaskManager()) 
		{
			public void runTask() throws Exception {
			
			}
		};
		owlModel.getTaskManager().run(task);
	}
	
	public static void testDB(OWLModel owlModel)
	{
		try 
		{
			
			System.out.println("-----------------------------------------------");
			System.out.println("Starting...");
			
			for (Iterator<?> it = owlModel.getOWLNamedClass(ModelConstants.CDOMAINCONCEPT).getSubclasses(true).iterator(); it.hasNext();) 
	        {                        
				OWLNamedClass cls = (OWLNamedClass) it.next();     
		   	 	//System.out.println("cls: "+cls.getURI());
		   	 	OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		   	 	if(individual!=null)
		   	 	{
		   	 	//System.out.println("individual: "+individual.getName());
		   	 	}
		   	 	
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void test()
	{
		try 
		{
	   	    //testExport(getOntoInfo());
			OWLModel owlModel = ProtegeModelFactory.getOWLModel(getOntoInfo());
			//System.out.println("owlModel: "+owlModel);
			//System.out.println("namespace: "+owlModel.getNamespaceManager().getNamespaceForPrefix("agrovoc"));
			//System.out.println("namespace: "+owlModel.getNamespaceManager().getDefaultNamespace());
	   	 	
			
			//for (Iterator<?> it = owlModel.getOWLNamedClass(ModelConstants.CDOMAINCONCEPT).getSubclasses(true).iterator(); it.hasNext();) 
	        {                       
	            //OWLNamedClass cls = (OWLNamedClass) it.next();   
	            OWLNamedClass cls = owlModel.getOWLNamedClass("agrovoc:c_118");   
		   	 	//System.out.println("cls: "+cls.getURI());
		   	 	
		   	 	OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		   	 	if(individual!=null)
		   	 	{
					/*for (Iterator<OWLIndividual> jt = individual.getPropertyValues(owlModel.getOWLProperty("isInfluencedBy")).iterator(); jt.hasNext();) 
		            {
		                
						OWLIndividual dest = jt.next();
						//if(individual.getURI().equals(dest.getURI()))
							System.out.println("Same Concept: "+individual.getURI()+" :  "+dest.getURI());

						OWLProperty hasProperty = owlModel.getOWLProperty("hasType");
						for (Iterator<OWLIndividual> invjt = dest.getPropertyValues(hasProperty).iterator(); invjt.hasNext();) 
			            {
							OWLIndividual inverse = invjt.next();
							System.out.println("Inverse Concept: "+dest.getURI()+" :  "+inverse.getURI()+"  :  "+individual.getURI());
			            }
		            }*/
					
					OWLProperty hasLexicalization = owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION);
	                
	                Collection<?> c =individual.getPropertyValues(hasLexicalization);
	                {
	                    for (Iterator<?> iterm = c.iterator(); iterm.hasNext();) 
	                    { 
	                        OWLIndividual termInstance = (OWLIndividual) iterm.next();
	                        if(termInstance!=null)
	        		   	 	{
	                        	for (Iterator<?> jtr = termInstance.getPossibleRDFProperties().iterator(); jtr.hasNext();) 
	        		            {
	        		                
	        						Object obj = jtr.next();
	        						System.out.println("Same Term: "+termInstance.getURI()+" :  "+obj);
	        		            }
	                        	for (Iterator<?> jtr = termInstance.getPropertyValues(owlModel.getOWLProperty("sameAs")).iterator(); jtr.hasNext();) 
	        		            {
	        		                
	        						OWLIndividual destTerm = (OWLIndividual) jtr.next();
	        						//if(termInstance.getURI().equals(destTerm.getURI()))
	        						System.out.println("               Term: "+termInstance.getURI()+" :  "+destTerm.getURI());
	        		            }
	                        	
	                        	/*for (Iterator<?> iterator = termInstance.getLabels().iterator(); iterator.hasNext();) 
	                            {
	                                Object obj = iterator.next();
	                                if (obj instanceof DefaultRDFSLiteral) {
	                                    DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
	                                    System.out.println(element.getString()+" ("+element.getLanguage()+")");                                                                     
	                                }
	                            }*/
	        		   	 	}
	                    }
	                } 
					
		   	 	}
		   	 	
	        }
	   	    //deleteScheme(ProtegeModelFactory.getOWLModel(getOntoInfo()));
	   	    //searchAll();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void checkLanguageLowerCase()
	{
	    ModelConstants.loadConstants(loadModelConstants());
        OntologyInfo oi = getOntoInfo();
        OWLModel owlModel = ProtegeModelFactory.getOWLModel(oi);
        for (Iterator<?> it = owlModel.getOWLNamedClass(ModelConstants.CDOMAINCONCEPT).getSubclasses(true).iterator(); it.hasNext();) 
        {                       
            OWLNamedClass cls = (OWLNamedClass) it.next();          
            for (Iterator<?> jt = cls.getInstances(false).iterator(); jt.hasNext();) 
            {
                OWLIndividual individual = (OWLIndividual) jt.next();
                OWLProperty hasLexicalization = owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION);
                
                Collection<?> c =individual.getPropertyValues(hasLexicalization);
                {
                    for (Iterator<?> iterm = c.iterator(); iterm.hasNext();) 
                    { 
                        OWLIndividual termInstance = (OWLIndividual) iterm.next();
                        for (Iterator<?> iterator = termInstance.getLabels().iterator(); iterator.hasNext();) 
                        {
                            Object obj = iterator.next();
                            if (obj instanceof DefaultRDFSLiteral) {
                                DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
                                System.out.println(element.getString()+" ("+element.getLanguage()+")");                                                                     
                            }
                        }
                        getProperty(owlModel, termInstance, ModelConstants.RHASSPELLINGVARIANT);
                    }
                }   
                OWLProperty hasDefinition = owlModel.getOWLProperty(ModelConstants.RHASDEFINITION);
                c =individual.getPropertyValues(hasDefinition);
                System.out.println(individual.getName() + " -def- " + c.size());
                {
                    for (Iterator<?> iterm = c.iterator(); iterm.hasNext();) 
                    { 
                        
                        OWLIndividual termInstance = (OWLIndividual) iterm.next();
                        for (Iterator<?> iterator = termInstance.getLabels().iterator(); iterator.hasNext();) 
                        {
                            Object obj = (Object) iterator.next();
                            if (obj instanceof DefaultRDFSLiteral) {
                                DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
                                System.out.println("definition: " + element.getString()+" ("+element.getLanguage()+")");                                                                     
                            }
                        }
                    }
                } 
                getProperty(owlModel, individual, ModelConstants.RHASSCOPENOTE);
                getProperty(owlModel, individual, ModelConstants.RHASEDITORIALNOTE);
            }
        }
	}
	
	public static void getProperty(OWLModel owlModel, OWLIndividual individual, String property){
	    Collection<?> c =individual.getPropertyValues(owlModel.getOWLProperty(property));
        System.out.println(individual.getName() + " - "+property+" - " + c.size());
        {
            for (Iterator<?> iterator = c.iterator(); iterator.hasNext();) 
            { 
                Object obj = (Object) iterator.next();
                if (obj instanceof DefaultRDFSLiteral) {
                    DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
                    System.out.println("scopenote: " + element.getString()+" ("+element.getLanguage()+")");                                                                     
                }
            }
        }
	}
	
	public static void deleteScheme(OWLModel owlModel)
	{
		
		SchemeObject sObj = new SchemeObject();
		sObj.setNamespace("http://aims.fao.org/aos/v01/Test2/");
		sObj.setSchemeName("test2:i_Test2");
		sObj.setSchemeInstance("http://aims.fao.org/aos/v01/Test2/i_Test2");
		sObj.setNameSpaceCatagoryPrefix("test2");
		sObj.setRHasSub("hasTest2SubCategory");
		sObj.setRIsSub("isTest2SubCategoryOf");
		
		OWLIndividual schemeIndividual = owlModel.getOWLIndividual(sObj.getSchemeName());
		
		printll(owlModel, schemeIndividual);
		
		for (Iterator<?> it = owlModel.getOWLNamedClass(ModelConstants.CCATEGORY).getSubclasses(true).iterator(); it.hasNext();) 
		{		    		    
			OWLNamedClass cls = (OWLNamedClass) it.next();			
			for (Iterator<?> jt = cls.getInstances(false).iterator(); jt.hasNext();) 
			{
				OWLIndividual individual = (OWLIndividual) jt.next();
	        	Object obj = individual.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RBELONGSTOSCHEME));
	        	if(obj instanceof OWLIndividual)
	        	{
	        		OWLIndividual scheme = (OWLIndividual) obj;
		        	if(scheme.getName().equals(schemeIndividual.getName()))
		        	{
		        		individual.delete();
		        		cls.delete();
		        	}
	        	}
    			
			}
		}
			if(schemeIndividual!=null)
				schemeIndividual.delete();
			
			System.out.println("isSubOf: "+sObj.getRIsSub());
			OWLObjectProperty isSubOf = owlModel.getOWLObjectProperty(sObj.getRIsSub());
			System.out.println("isSubOf: "+isSubOf);
			if(isSubOf!=null)
			{
				System.out.println("isSubOfname: "+isSubOf.getName());
				isSubOf.delete();
			}
			
			System.out.println("hasSub: "+sObj.getRHasSub());
			OWLObjectProperty hasSub = owlModel.getOWLObjectProperty(sObj.getRHasSub());
			System.out.println("hasSub: "+hasSub);
			if(isSubOf!=null)
			{
				System.out.println("hasSubname: "+hasSub.getName());
				hasSub.delete();
			}
			
			System.out.println("prefix: "+owlModel.getNamespaceManager().getPrefix(sObj.getNamespace()));
			if(owlModel.getNamespaceManager().getPrefix(sObj.getNamespace())!=null)
				owlModel.getNamespaceManager().removePrefix(sObj.getNameSpaceCatagoryPrefix());
			
			
			for (Iterator<?> iter = owlModel.getOWLNamedClass(ModelConstants.CCLASSIFICATIONSCHEME).getInstances(true).iterator(); iter.hasNext();) {
				OWLIndividual schemeIns = (OWLIndividual) iter.next();
				System.out.println("schemeIns: "+schemeIns.getName());
			}
			
			
			printll(owlModel, schemeIndividual);
			
			
		
		
		/*owlModel.getNamespaceManager().removePrefix(sObj.getNameSpaceCatagoryPrefix());
		schemeIndividual.delete();
		
		OWLObjectProperty isSubOf = owlModel.getOWLObjectProperty(sObj.getRIsSub());
		isSubOf.delete();
		
		OWLObjectProperty hasSub = owlModel.getOWLObjectProperty(sObj.getRHasSub());
		hasSub.delete();
		
		*individual: i_123
individual: agrovoc:i_test
individual: agrovoc:i_1256639963893
individual: agrovoc:i_1256640034606
individual: agrovoc:i_1256640139553
individual: i_1256701781350
individual: i_1256704927859
individual: test2:i_1256705446640
scheme: test2:i_Test2
trueindividual: test2:i_1256705446640
		*/

	}
	
	public static void printll(OWLModel owlModel, OWLIndividual schemeIndividual)
	{
		System.out.println("printttttttttttttttttttttttt: "+schemeIndividual);
		for (Iterator<?> it = owlModel.getOWLNamedClass(ModelConstants.CCATEGORY).getSubclasses(true).iterator(); it.hasNext();) 
		{		    		    
			OWLNamedClass cls = (OWLNamedClass) it.next();			
			for (Iterator<?> jt = cls.getInstances(false).iterator(); jt.hasNext();) 
			{
				OWLIndividual individual = (OWLIndividual) jt.next();
	        	System.out.println("individual: "+individual.getName());
	        	Object obj = individual.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RBELONGSTOSCHEME));
	        	if(obj instanceof OWLIndividual)
	        	{
	        		OWLIndividual scheme = (OWLIndividual) obj;

	        		if(schemeIndividual!=null)
	        		{
	        			System.out.println("scheme: "+scheme.getName()+"  =  "+schemeIndividual.getName());
			        	if(scheme.getName().equals(schemeIndividual.getName()))
			        	{
			        		System.out.println("trueindividual: "+individual.getName());
			        		//individual.delete();
			        		//cls.delete();
			        	}
	        		}
	        	}
	        	
			}
		}
	}
	
	@SuppressWarnings("unused")
	private static HashMap<String, ArrayList<String>> getAllTermList(OWLModel owlModel)
	{
		HashMap<String, ArrayList<String>> hMap = new HashMap<String, ArrayList<String>>();
		OWLNamedClass nounCls = owlModel.getOWLNamedClass(ModelConstants.CNOUN);
		if(nounCls!=null)
		{
			for (Iterator<?> iter = nounCls.getInstances(true).iterator(); iter.hasNext();) 
			{
				OWLIndividual termInstance = (OWLIndividual) iter.next();
				for (Iterator<?> iterator = termInstance.getLabels().iterator(); iterator.hasNext();) 
				{
					Object obj = iterator.next();
			    	if (obj instanceof DefaultRDFSLiteral) 
			    	{
			    		DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
			    		String label = element.getString();
			    		String lang = element.getLanguage();
			    		
			    		if(hMap.containsKey(lang))
			    		{
			    			ArrayList<String> termList = hMap.get(lang);
			    			termList.add(label);
			    		}
			    		else
			    		{
			    			ArrayList<String> termList = new ArrayList<String>();
			    			termList.add(label);
			    			hMap.put(lang, termList);
			    		}
			    		
			    		System.out.println(label+" = "+lang);
					}
				}
			}
		}
		return hMap;
	}
	
	public static DomainRangeDatatypeObject getRangeValues(RDFProperty property){
		DomainRangeDatatypeObject data = new DomainRangeDatatypeObject();
		String datatype = null;
		ArrayList<String> list = new ArrayList<String>();		
		Collection<?> c = property.getRanges(true);
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) 
		{
			Object obj = (Object) iter.next();
			// only datatype no values
			if (obj instanceof RDFSDatatype)
			{
				RDFSDatatype type = (RDFSDatatype) obj;				
				datatype = type.getBrowserText();				
			}
			// has values also
			else if (obj instanceof DefaultOWLDataRange)
			{
				DefaultOWLDataRange type = (DefaultOWLDataRange) obj;				
				List<?> rdfList = type.getOneOfValues();
				rdfList = type.getOneOfValueLiterals();
				int i = 0;
				for (Iterator<?> itrr = rdfList.iterator(); itrr.hasNext();) {
					DefaultRDFSLiteral ind = (DefaultRDFSLiteral) itrr.next();					
					if(i==0){													
						datatype = ind.getDatatype().getBrowserText();
					}
					i++;
					list.add(ind.toString());
				}				
			}
		}
		data.setRangeDataType( datatype == null? "any" : datatype);
		data.setRangeValue(list);
		return data;
	}
	
	public static void getDatatypeProperties(OWLModel owlModel, String property){
		OWLDatatypeProperty cdomainProperty = owlModel.getOWLDatatypeProperty(property);
		for (Iterator<?> iter = cdomainProperty.getSubproperties(true).iterator(); iter.hasNext();) {
			DefaultOWLDatatypeProperty prop = (DefaultOWLDatatypeProperty) iter.next();
			RelationshipObject rObj = new RelationshipObject();
			rObj.setUri(prop.getURI());
			rObj.setName(prop.getName());
			rObj.setType(RelationshipObject.DATATYPE);
			DomainRangeDatatypeObject data = getRangeValues(prop);
			
			System.out.println("--------------");
			System.out.println("porp: "+prop.getName());
			System.out.println("isfunc: "+prop.isFunctional());
			System.out.println("is range: "+prop.isRangeDefined());
			System.out.println("datatype: "+data.getRangeDataType());
			System.out.println("datatype: "+data.getRangeValue());
			
		}
	}
	
	public static void getChildDataTypeProperty(OWLDatatypeProperty rootProp, OWLModel owlModel){
		
		RDFProperty subClassOfProperty = owlModel.getRDFProperty(RDFSNames.Slot.SUB_PROPERTY_OF);
	    Collection<?> results = owlModel.getRDFResourcesWithPropertyValue(subClassOfProperty, rootProp);
	    
	    for (Iterator<?> iter = results.iterator(); iter.hasNext();) {
	    	OWLDatatypeProperty childProp = (OWLDatatypeProperty) iter.next();
	    	System.out.println("childProp: "+childProp.getURI());
			getChildDataTypeProperty(childProp, owlModel);
		}
	}
	
    public static void testExport(OntologyInfo ontoInfo){
        ExportParameterObject exp = new ExportParameterObject();        
        exp.setFormat("RDBMS_SQL_FORMAT");
        System.out.println("exporting....");
        System.out.println(ExportSQL.getExportSQL(exp, ontoInfo));
        System.out.println("exporting done");
    }
    
	public static void testSearch(OntologyInfo ontoInfo){

		SearchParameterObject searchObj = new SearchParameterObject();
        ArrayList<String> sl = new ArrayList<String>();
        sl.add("en");
        searchObj.setSelectedLangauge(sl);
        searchObj.setKeyword("rrr");        
        searchObj.setRegex(SearchParameterObject.EXACT_WORD);
        searchObj.setCaseSensitive(true);
        searchObj.setIncludeNotes(false);
        searchObj.setOnlyPreferredTerm(false);
        
        /*searchTerm-------------->> Acquired
		wildcardOp-------------->> EXACT_WORD
		caseSensitive----------->> false
		includeNotes------------>> false
		preferedTermOnly-------->> false
		termCodeRelation-------->> null
		termCode---------------->> null
		relationship------------>> null
		classificationScheme---->> null
		status------------------>> null
        
        searchTerm-------------->> Acquired
        wildcardOp-------------->> EXACT_WORD
        caseSensitive----------->> false
        includeNotes------------>> false
        preferedTermOnly-------->> false
        termCodeRelation-------->> null
        termCode---------------->> null
        relationship------------>> null
        classificationScheme---->> null
        status------------------>> null
        > en
        > fr
        > hi
        */
        //searchObj.setRelationship("is author of");
        
        SearchServiceImpl search = new SearchServiceImpl();
        
        Date d = new Date();
        System.out.println("============New Search==============");
        ArrayList<String> result =  search.getSearchResults(searchObj, ontoInfo);
        System.out.println("result size = " + result.size()+" in "+ ((new Date().getTime()-d.getTime())/1000)+ " secs");
        printSearchResult(ontoInfo, result, searchObj, search);
        
       /* d = new Date();
        System.out.println("============Old Search=============="); 
        result =  search.doSearchResults1(searchObj,owlModel);
        System.out.println("result size = " + result.size()+" in "+ ((new Date().getTime()-d.getTime())/1000)+ " secs");
        printSearchResult(owlModel, result, searchObj, search);
        */
    }
	
	public static void printSearchResult(OntologyInfo ontoInfo, ArrayList<String> result, SearchParameterObject searchObj, SearchServiceImpl search)
	{
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		int i =0;
        for(String r : result)
        {            
            ConceptObject co = search.getConceptObject(owlModel.getOWLNamedClass(r), searchObj);            
            //System.out.println("getConceptInstance = " + co.getConceptInstance());         
            //System.out.println("name      = " + co.getName());
            //System.out.println("uri       = " + co.getUri());
            HashMap<String, TermObject> tol = co.getTerm();
            String labels = "";
            for( String s: tol.keySet() ){
                labels +=  tol.get(s).getLabel()+", ";
            }
            System.out.println("["+ i +"] term ("+r+")      = " + labels);
            i++;
        }
        owlModel.dispose();
	}
	
	
public static void dumpHere(){
    
    /*
    OWLNamedClass cls = owlModel.getOWLNamedClass("agrovoc:c_1254389707212");
    //OWLNamedClass parentCls = owlModel.getOWLNamedClass(parentConceptName);
    int cnt = cls.getSuperclassCount()-1;
    System.out.println("cnt: "+cnt);
    if(cnt>1)
    {
        //cls.removeSuperclass(parentCls);
        for (Iterator<?> iterator = cls.getSuperclasses(false).iterator(); iterator.hasNext();) 
        {
            OWLNamedClass cl = (OWLNamedClass) iterator.next();
            System.out.println("cl: "+cl.getName());
        }
    }
    
    
    String squery = ""+
    "PREFIX agrovoc: <"+ModelConstants.AGROVOCBASENAMESPACE+"> " +
    "SELECT ?subject  ?object  " +
    "WHERE { " +
    "?subject agrovoc:hasLexicalization ?object }"; 

    
    com.hp.hpl.jena.query.Query query = QueryFactory.create(squery);
    QueryExecution qe = QueryExecutionFactory.create(query);
    ResultSet results = (ResultSet) qe.execSelect();
    ResultSetFormatter.out(out, results, query);
    out.close(); 
    qe.close();
    
    
    squery = " Select DISTINCT ?blank7 ?blank8 ?blank9 ?blank0  WHERE "+
                    "{ "+
                    "{ ?blank7 <http://www.w3.org/2000/01/rdf-schema#label> ?blank8 . } "+
                    "{ ?blank7 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?blank9 . } "+
                    "{ ?blank0 <http://www.fao.org/aims/aos/agrovoc#hasLexicalization> ?blank7 . } "+
                    "{ ?blank0 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.fao.org/aims/aos/agrovoc#c_3699> . }  }";
    
    squery = "Select DISTINCT ?blank7 ?blank5 ?blank0 ?blank11  WHERE "+
                    "{   "+
                    "{ ?blank5 <http://www.w3.org/2000/01/rdf-schema#label> ?blank7 . } "+
                    "{ ?blank0 <http://www.fao.org/aims/aos/agrovoc#hasLexicalization> ?blank5 . } "+
                    "{ ?blank0 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?blank11 . }  }";
    System.out.println("Starting...");
    Date d = new Date();
    QueryResults qr = owlModel.executeSPARQLQuery(squery);
    while(qr.hasNext())
    {
        Map<?, ?> map = qr.next();
        System.out.println(map.size());
        for(Object obj:map.keySet())
        {
            System.out.println(obj+" = "+map.get(obj));
        }
    }
    System.out.println("Time taken: "+((new Date().getTime()-d.getTime())/1000)+" secs.");
    System.out.println("Ended...");*/
    //getAllTermList(owlModel);

    
    
    /*NamespaceManager nameSpaceManager = owlModel.getNamespaceManager();
    System.out.println("Default Namespace: "+nameSpaceManager.getDefaultNamespace());
    System.out.println("Agrovoc Namespace Prefix: "+owlModel.getNamespaceManager().getPrefix(ModelConstants.AGROVOCBASENAMESPACE));
     for (Iterator<String> iter = nameSpaceManager.getPrefixes().iterator(); iter.hasNext();) {
         String prefix = iter.next();
        System.out.println("prefix: "+prefix+" -- "+nameSpaceManager.getNamespaceForPrefix(prefix));
     }*/
//testCase.testExport(testCase.getOntoInfo());
}

	
}
