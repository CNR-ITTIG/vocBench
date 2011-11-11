package org.fao.aoscs.server;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.ServletException;

import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.gwt.GwtConfigurationHelper;
import net.sf.gilead.gwt.PersistentRemoteService;

import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.client.module.search.service.SearchService;
import org.fao.aoscs.client.module.search.widgetlib.AOSSuggestOracleRequest;
import org.fao.aoscs.client.module.search.widgetlib.AOSSuggestOracleResponse;
import org.fao.aoscs.client.module.search.widgetlib.ItemSuggestion;
import org.fao.aoscs.convert.util.ProtegeUtil;
import org.fao.aoscs.domain.AttributesObject;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.InitializeSearchData;
import org.fao.aoscs.domain.NonFuncObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.SearchParameterObject;
import org.fao.aoscs.domain.SearchResultObject;
import org.fao.aoscs.domain.TermObject;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.hibernate.QueryFactory;
import org.fao.aoscs.server.index.IndexingEngineFactory;
import org.fao.aoscs.server.protege.ProtegeModelFactory;
import org.fao.aoscs.server.protege.ProtegeWorkbenchUtility;
import org.fao.aoscs.server.utility.ConceptUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.gen2.table.client.TableModelHelper.ColumnSortInfo;
import com.google.gwt.gen2.table.client.TableModelHelper.ColumnSortList;
import com.google.gwt.gen2.table.client.TableModelHelper.Request;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFSLiteral;

public class SearchServiceImpl extends PersistentRemoteService  implements SearchService {
	
	private static final long serialVersionUID = 8165760079971898449L;
	protected static Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);
	//private ArrayList<String> conceptList = null;
	
	//-------------------------------------------------------------------------
	// Initialization of Remote service : must be done before any server call !
	//-------------------------------------------------------------------------
	@Override
	public void init() throws ServletException
	{
		logger.info("initializing search service");
		super.init();
		
	//	Bean Manager initialization
		logger.info("initializing search service (hybernate code)");
		setBeanManager(GwtConfigurationHelper.initGwtStatelessBeanManager( new HibernateUtil(HibernateUtilities.getSessionFactory())));;		
		logger.info("end search service initialization");
		
	}

	public InitializeSearchData initData(OntologyInfo ontoInfo) {
		logger.info("initializing owlmodel");
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		
		if(ConfigConstants.ISINDEXING)
		{
			//STARRED MOD (Enable line below to enable indexing)
			//OntologyIndexer oi = IndexingEngineFactory.getSearchEngine(ontoInfo);
		}
		
		logger.info("initializing search data");
		InitializeSearchData data = new InitializeSearchData();
		logger.info("search data initialized");
		data.setStatus(getStatus());
		data.setScheme(getScheme(owlModel));
		data.setTermCodeProperties(getAllTermCodeProperties(owlModel));
		data.setConceptDomainAttributes(ConceptUtility.getDatatypeProperties(owlModel, ModelConstants.RCONCEPTDOMAINDATATYPEPROPERTY));
        data.setConceptEditorialAttributes(ConceptUtility.getDatatypeProperties(owlModel, ModelConstants.RCONCEPTEDITORIALDATATYPEPROPERTY));
        data.setTermDomainAttributes(ConceptUtility.getDatatypeProperties(owlModel, ModelConstants.RTERMDOMAINDATATYPEPROPERTY));
        data.setTermEditorialAttributes(ConceptUtility.getDatatypeProperties(owlModel, ModelConstants.RTERMEDITORIALDATATYPEPROPERTY));
        
		///owlModel.dispose();
		return data;
	}
	
	public ArrayList<String[]> getScheme(OWLModel owlModel) {
		logger.debug("inside getScheme(OWLModel owlModel)");
		ArrayList<String[]> list = new ArrayList<String[]>();
		OWLNamedClass cls = owlModel.getOWLNamedClass(ModelConstants.CCLASSIFICATIONSCHEME);
		for (Iterator<OWLIndividual> iter = cls.getInstances(true).iterator(); iter.hasNext();) {
			OWLIndividual element = iter.next();
			String[] tmp = new String[2];
			tmp[1] = element.getURI();
			String tmp_en = "";
			for (Iterator<RDFSLiteral> iterator = element.getLabels().iterator(); iterator.hasNext();) {
				Object obj = iterator.next();
				if (obj instanceof DefaultRDFSLiteral) {
		        	DefaultRDFSLiteral label = (DefaultRDFSLiteral) obj;
		        	if(label.getLanguage().toLowerCase().equals("en"))
		        		tmp_en = label.getString();
		        	tmp[0] = label.getString();
		        }
				else
				{
					tmp[0] = ""+obj;
				}
				if(tmp_en.length()>0) tmp[0]=tmp_en;
			}
			list.add(tmp);
		}
		return list;
	}

	public ArrayList<RelationshipObject> getAllTermCodeProperties(OWLModel owlModel) {
		logger.debug("inside getAllTermCodeProperties(OWLModel owlModel)");
		ArrayList<RelationshipObject> list = new ArrayList<RelationshipObject>();
		OWLDatatypeProperty hasCode = owlModel.getOWLDatatypeProperty(ModelConstants.RHASCODE);
		for (Iterator<DefaultOWLDatatypeProperty> iter = hasCode.getSubproperties(true).iterator(); iter.hasNext();) {
			DefaultOWLDatatypeProperty prop = (DefaultOWLDatatypeProperty) iter.next();
			RelationshipObject rObj = new RelationshipObject();
			rObj.setUri(prop.getURI());
			rObj.setName(prop.getName());
			rObj.setType(RelationshipObject.DATATYPE);
			for (Iterator<Object> iterator = prop.getLabels().iterator(); iterator.hasNext();) {
				Object obj = iterator.next();
				if (obj instanceof DefaultRDFSLiteral) {
					DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
					rObj.addLabel(element.getString(), element.getLanguage());
				}
			}
			list.add(rObj);
		}
		return list;
	}

	private ArrayList<String[]> getStatus() {
		return QueryFactory.getHibernateSQLQuery( "SELECT status, id FROM owl_status ORDER BY id");
	}

	public ArrayList<String[]> getSchemes(OntologyInfo ontoInfo) {
		logger.debug("getting schemes: ");
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		ArrayList<String[]> list = new ArrayList<String[]>();
		OWLNamedClass cls = owlModel.getOWLNamedClass(ModelConstants.CCLASSIFICATIONSCHEME);
		for (Iterator<OWLIndividual> iter = cls.getInstances(true).iterator(); iter.hasNext();) {
			OWLIndividual element = (OWLIndividual) iter.next();
			String[] tmp = new String[2];
			tmp[1] = element.getURI();
			for (Iterator<RDFSLiteral> iterator = element.getLabels().iterator(); iterator.hasNext();) {
				RDFSLiteral label = (RDFSLiteral) iterator.next();
				tmp[0] = label.getString();
			}
			list.add(tmp);
		}
		///owlModel.dispose();;
		logger.debug("schemes got: " + list);
		return list;
	}
	
	private ArrayList<String> getConcepIns(OWLModel owlModel, String clsName, String property, SearchParameterObject searchObj, ArrayList<String> insList)
	{
		OWLNamedClass cls = owlModel.getOWLNamedClass(clsName);
		for (Iterator<OWLIndividual> jt = cls.getInstances(false).iterator(); jt.hasNext();) 
		{
        	OWLIndividual individual = (OWLIndividual) jt.next();
			if(individual!=null)
			{
				boolean chk = false;
				if(property.equals(ModelConstants.RISIMAGEOF))
				{
					chk = checkRDFSLiteral(individual.getComments(), searchObj);
				}
				else
				{
					chk = checkRDFSLiteral(individual.getLabels(), searchObj);
				}
				if(chk)
	        	{
					for (Iterator<OWLIndividual> tr = individual.getPropertyValues(owlModel.getOWLProperty(property)).iterator(); tr.hasNext();) 
					{
						OWLIndividual ind = (OWLIndividual) tr.next();
						if(ind!=null)
						{
							String indName = ind.getName();
							if(!insList.contains(indName))
							{
								insList.add(indName);
							}
						}
					}
				}
			}
		}
		return insList;
	}
	
	public String getSearchResultsSize(SearchParameterObject searchObj, OntologyInfo ontoInfo)
	{
		//conceptList = searchConcept(searchObj, ontoInfo);
		//return ""+conceptList.size();
		return ""+searchConcept(searchObj, ontoInfo).size();
	}
	
	public ArrayList<String> getSearchResults(SearchParameterObject searchObj, OntologyInfo ontoInfo) 
	{
		/*if(conceptList==null)
			conceptList = searchConcept(searchObj, ontoInfo);
		return conceptList;
		*/
		return searchConcept(searchObj, ontoInfo);
	}
	
	private ArrayList<String> doSearchResults(OntologyInfo ontoInfo, SearchParameterObject searchObj, ArrayList<String> insList) 
	{
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		logger.debug("entering getSearchResults:" + searchObj);
		/*System.out.println("searchTerm-------------->> "+ searchObj.getKeyword());
		System.out.println("wildcardOp-------------->> "+ searchObj.getRegex());
		System.out.println("caseSensitive----------->> "+ searchObj.getCaseSensitive());
		System.out.println("includeNotes------------>> "+ searchObj.getIncludeNotes()); 		
		System.out.println("preferedTermOnly-------->> "+ searchObj.isOnlyPreferredTerm());
		
		System.out.println("termCodeRelation-------->> "+ searchObj.getTermCodeRepository());
		System.out.println("termCode---------------->> "+ searchObj.getTermCode());
		
		System.out.println("relationship------------>> "+ searchObj.getRelationship());		
		System.out.println("classificationScheme---->> "+ searchObj.getScheme()); 
		System.out.println("status------------------>> "+ searchObj.getStatus());
		for(String str : searchObj.getSelectedLangauge())
			System.out.println("> "+str);	  
	    Set<String> rel = searchObj.getTermAttribute().keySet();
	    System.out.println("att relation size = " + rel.size());
	    for(String r:rel){
	        ArrayList<NonFuncObject> nf = searchObj.getTermAttribute().get(r);
	        for(NonFuncObject o:nf){
	            System.out.print("value = " + o.getValue());
	            System.out.println(" -- lang = " + o.getLanguage());
	        }
	    }*/
		

		// apply other filter
		insList = applySearchFilter(owlModel, searchObj, insList);
		logger.debug("fsize:"+insList.size());	
		
		ArrayList<String> resultList = new ArrayList<String>();
		for(String ins: insList)
		{
			OWLIndividual individual = owlModel.getOWLIndividual(ins);
			String clsName = individual.getProtegeType().getName();
			if(!resultList.contains(clsName)){
				resultList.add(clsName);
			}
		}
		return resultList;	
	}
	
	private ArrayList<String> applySearchFilter(OWLModel owlModel, SearchParameterObject searchObj, ArrayList<String> list)
	{
		ArrayList<String> insList = new ArrayList<String>(list);
		for (String ins: list) 
		{
        	OWLIndividual individual = owlModel.getOWLIndividual(ins);
        	
        	//check status
        	if(searchObj.getStatus()!=null && !searchObj.getStatus().equals("--None--")){
        		
    			String status = ""+individual.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASSTATUS));
    			if(!status.equalsIgnoreCase(searchObj.getStatus()))
    			{
    				insList.remove(individual.getName());
    				continue;
    			}
			}
        	
        	//check relationship
        	if(searchObj.getRelationship() != null ){	        	    
        	    OWLProperty prop = owlModel.getOWLProperty("" + searchObj.getRelationship());
        		if(prop!=null)
				{
				    if(individual.getPropertyValueCount(prop)<1)
				    {
				    	insList.remove(individual.getName());
				    	continue;
				    }
				}
        	}
        	
        	//check scheme
        	if(searchObj.getScheme() != null && !searchObj.getScheme().equals("--None--")){
        		OWLProperty prop = owlModel.getOWLProperty(ModelConstants.RBELONGSTOSCHEME);
				if(prop!=null)
				{
					boolean ch = false;
					for (Iterator<OWLIndividual> tr = individual.getPropertyValues(prop).iterator(); tr.hasNext();) {
						OWLIndividual scheme = tr.next();
						ch = (scheme.getURI()).equals(searchObj.getScheme());
						if(ch) break;
					}
					
					if(!ch)
					{
						insList.remove(individual.getName());
	    				continue;
					}
				}
        	}
        	
			// check concept attributes					
			if(searchObj.getConceptAttribute().size()>0)
			{	
				boolean chkConceptAttribute = false;
			    for(String r: searchObj.getConceptAttribute().keySet())
			    {
			        ArrayList<NonFuncObject> oList = searchObj.getConceptAttribute().get(r);
			        for(NonFuncObject o : oList)
			        {
			            if(checkRDFSLiteral(individual.getPropertyValues(owlModel.getOWLProperty(r)), o.getValue(), o.getLanguage())){
			            	chkConceptAttribute = true;
			            	break;
			            }
			        }
			    }
			    if(!chkConceptAttribute)
			    {
			    	insList.remove(individual.getName());
			    	continue;
			    }
			}
        	
			//check term level
			if(searchObj.isOnlyPreferredTerm() || searchObj.getTermAttribute().size()>0 || searchObj.getTermNote().size()>0 || (searchObj.getTermCode()!=null && searchObj.getTermCodeRepository()!=null && !searchObj.getTermCodeRepository().equals("--None--")))
			{
				boolean isPreferredTerm = false;
				boolean chkTermCode = false;
				boolean chkTermAttr = false;
				
				for(Iterator<OWLIndividual> t = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION)).iterator(); t.hasNext();) 
				{
					
					if(isPreferredTerm && chkTermCode && chkTermAttr)
				    {
				    	break;
				    }
				    else
				    {
						OWLIndividual term = (OWLIndividual) t.next();
						
						//check preferred term
						if(searchObj.isOnlyPreferredTerm()){
	                        isPreferredTerm = (""+term.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RISMAINLABEL))).toLowerCase().equals("true");
		                }
						else
						{
							isPreferredTerm = true;
						}
						
						//check termcode
						if(searchObj.getTermCode()!=null && searchObj.getTermCodeRepository()!=null && !searchObj.getTermCodeRepository().equals("--None--"))
						{
							OWLProperty prop = owlModel.getOWLProperty(searchObj.getTermCodeRepository());
							if(prop!=null)
							{
								String termCode = ""+term.getPropertyValue(prop);
								chkTermCode = termCode.equals(searchObj.getTermCode());
							}
						}
						else
						{
							chkTermCode = true;
						}
						
						// check term attributes
		                if(searchObj.getTermAttribute().size()>0)
		                {   
		                    Set<String> robj = searchObj.getTermAttribute().keySet();
		                    chkTermAttr = false;
		                    for(String r:robj)
		                    {
		                        ArrayList<NonFuncObject> oList = searchObj.getTermAttribute().get(r);
		                        int ocount = oList.size();
		                        for(NonFuncObject o : oList)
		                        {
		                            if(checkRDFSLiteral(term.getPropertyValues(owlModel.getOWLProperty(r)), o.getValue(), o.getLanguage())){
		                            	chkTermAttr = true;
		    			            	break;
		                            }
		                            
		                        }
		                    }
		                }
		                else
						{
		                	chkTermAttr = true;
						}
				    }
	        	}
				if(!(isPreferredTerm && chkTermCode && chkTermAttr))
			    {
					insList.remove(individual.getName());
					continue;
			    }
			}
		}	
		return insList;
	}
	
	private void printSearchParameterObject(SearchParameterObject searchObj)
	{
		System.out.println("searchTerm-------------->> "+ searchObj.getKeyword());
		System.out.println("wildcardOp-------------->> "+ searchObj.getRegex());
		System.out.println("caseSensitive----------->> "+ searchObj.getCaseSensitive());
		System.out.println("includeNotes------------>> "+ searchObj.getIncludeNotes()); 		
		System.out.println("preferedTermOnly-------->> "+ searchObj.isOnlyPreferredTerm());
		
		System.out.println("termCodeRelation-------->> "+ searchObj.getTermCodeRepository());
		System.out.println("termCode---------------->> "+ searchObj.getTermCode());
		
		System.out.println("relationship------------>> "+ searchObj.getRelationship());		
		System.out.println("classificationScheme---->> "+ searchObj.getScheme()); 
		System.out.println("status------------------>> "+ searchObj.getStatus());
		for(String str : searchObj.getSelectedLangauge())
			System.out.println("> "+str);	  
	    Set<String> rel = searchObj.getTermAttribute().keySet();
	    System.out.println("att relation size = " + rel.size());
	    for(String r:rel){
	        ArrayList<NonFuncObject> nf = searchObj.getTermAttribute().get(r);
	        for(NonFuncObject o:nf){
	            System.out.print("value = " + o.getValue());
	            System.out.println(" -- lang = " + o.getLanguage());
	        }
	    }
	}
	
	private ArrayList<String> olddoSearchResults(SearchParameterObject searchObj, OntologyInfo ontoInfo) 
	{
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		logger.debug("entering getSearchResults:" + searchObj);
	    //printSearchParameterObject(searchObj);
	    
		ArrayList<String> resultList = new ArrayList<String>();
		String cDomain = ModelConstants.CDOMAINCONCEPT;
		if(searchObj.getScheme() != null && !searchObj.getScheme().equals("--None--"))
			cDomain = ModelConstants.CCATEGORY;
		
		for (Iterator it = owlModel.getOWLNamedClass(cDomain).getSubclasses(true).iterator(); it.hasNext();) 
		{	
		    OWLNamedClass cls = (OWLNamedClass) it.next();
			for (Iterator<OWLIndividual> jt = cls.getInstances(false).iterator(); jt.hasNext();) 
			{
	        	OWLIndividual individual = (OWLIndividual) jt.next();
	        	boolean chkRel = true;
	        	boolean chkStatus = true;
	        	boolean chkScheme = true;
	        	boolean chkConceptAttr = true;
	        	
	        	if(searchObj.getRelationship() != null ){	        	    
	        	    OWLProperty prop = owlModel.getOWLProperty("" + searchObj.getRelationship());
	        		if(prop!=null)
					{
					    chkRel = individual.getPropertyValueCount(prop)>0;
					}
	        	}
	        	if(searchObj.getStatus()!=null && !searchObj.getStatus().equals("--None--")){
        			String status = ""+individual.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASSTATUS));
        			chkStatus = status.equals(searchObj.getStatus());
				}
	        	if(searchObj.getScheme() != null && !searchObj.getScheme().equals("--None--")){
	        		OWLProperty prop = owlModel.getOWLProperty(ModelConstants.RBELONGSTOSCHEME);
					if(prop!=null)
					{
						boolean ch = false;
						for (Iterator<OWLIndividual> tr = individual.getPropertyValues(prop).iterator(); tr.hasNext();) {
							OWLIndividual scheme = tr.next();
							ch = (scheme.getURI()).equals(searchObj.getScheme());
							if(ch) break;
						}
						if(!ch)	chkScheme =  ch;
					}
	        	}
	        	if(chkRel && chkStatus && chkScheme)
	        	{
	        		//check definition
	        		boolean chkDef = false;
					if(searchObj.getIncludeNotes())
					{
						for (Iterator<OWLIndividual> tr = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASDEFINITION)).iterator(); tr.hasNext();) 
						{
							chkDef = checkRDFSLiteral((tr.next()).getLabels(), searchObj);
							if(chkDef) break;
						}
					}
					//check scope notes
	        		boolean chkScopeNotes = false;
					if(searchObj.getIncludeNotes())
					{
					    chkScopeNotes = checkRDFSLiteral(individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASSCOPENOTE)), searchObj);
					}
					//check editorial notes
					boolean chkEditorialNotes = false;
					if(searchObj.getIncludeNotes())
					{
					    chkEditorialNotes = checkRDFSLiteral(individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASEDITORIALNOTE)), searchObj);
					}
					//check img desc
	        		boolean chkImgDesc = false;
					if(searchObj.getIncludeNotes())
					{
						for (Iterator<OWLIndividual> tr = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASIMAGE)).iterator(); tr.hasNext();) 
						{
							chkImgDesc = checkRDFSLiteral((tr.next()).getComments(), searchObj);
							if(chkImgDesc) break;
						}
					}
					// check concept attributes					
					if(searchObj.getConceptAttribute().size()>0)
					{	
					    int rcount = searchObj.getConceptAttribute().size();
					    Set<String> robj = searchObj.getConceptAttribute().keySet();
					    chkConceptAttr = false;
					    for(String r:robj)
					    {
					        ArrayList<NonFuncObject> oList = searchObj.getConceptAttribute().get(r);
					        int ocount = oList.size();
					        for(NonFuncObject o : oList)
					        {
					            boolean val = checkRDFSLiteral(individual.getPropertyValues(owlModel.getOWLProperty(r)), o.getValue(), o.getLanguage());
					            if(val){
					                ocount--;
					            }
					            else{
					                break;
					            }
					        }
					        if(ocount > 0)
					            break;
					        else
					            rcount--;
					    }
					    if(rcount == 0)
					        chkConceptAttr = true;
					    else
					        chkConceptAttr = false;
					}
					
					//check term level
					boolean chkTermAttr = true;
					for(Iterator<OWLIndividual> t = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION)).iterator(); t.hasNext();) 
					{
					    OWLIndividual term = (OWLIndividual) t.next();
						boolean chkTermCode = true;
						boolean isPreferredTerm = true;
						if(searchObj.isOnlyPreferredTerm()){
		                    if((""+term.getPropertyValue(term.getOWLModel().getOWLProperty(ModelConstants.RISMAINLABEL))).toLowerCase().equals("false"))
		                        isPreferredTerm = false;
		                }
						if(searchObj.getTermCode()!=null && searchObj.getTermCodeRepository()!=null && !searchObj.getTermCodeRepository().equals("--None--"))
						{
							OWLProperty prop = owlModel.getOWLProperty(searchObj.getTermCodeRepository());
							if(prop!=null)
							{
								String termCode = ""+term.getPropertyValue(prop);
								chkTermCode = termCode.equals(searchObj.getTermCode());																					
							}
						}
						// check term attributes                   
	                    if(searchObj.getTermAttribute().size()>0)
	                    {   
	                        int rcount = searchObj.getTermAttribute().size();
	                        Set<String> robj = searchObj.getTermAttribute().keySet();
	                        chkTermAttr = false;
	                        for(String r:robj)
	                        {
	                            ArrayList<NonFuncObject> oList = searchObj.getTermAttribute().get(r);
	                            int ocount = oList.size();
	                            for(NonFuncObject o : oList)
	                            {
	                                boolean val = checkRDFSLiteral(term.getPropertyValues(owlModel.getOWLProperty(r)), o.getValue(), o.getLanguage());
	                                if(val){
	                                    ocount--;
	                                }
	                                else{
	                                    break;
	                                }
	                            }
	                            if(ocount > 0)
	                                break;
	                            else
	                                rcount--;
	                        }
	                        if(rcount == 0)
	                            chkTermAttr = true;
	                        else
	                            chkTermAttr = false;
	                    }
						if(chkConceptAttr && chkTermCode && isPreferredTerm && chkTermAttr)
						{
						    boolean chkTerm = true;
							//search term labels
						    if(!searchObj.getKeyword().equals("")){
						       chkTerm = checkRDFSLiteral(term.getLabels(), searchObj);
						    }						    
						    if(chkTerm || chkDef  || chkScopeNotes || chkEditorialNotes || chkImgDesc)
							{
								if(!resultList.contains(cls.getName())){
									resultList.add(cls.getName());
								}
							}
	        			}
		        	}
	        	}
			}
		}		
		return resultList;	
	}
	
	private ArrayList<String> getConceptInsList(OWLModel owlModel, ArrayList<String> clsList){
		ArrayList<String> insList = new ArrayList<String>();
		for(String clsURI: clsList)
		{
			OWLNamedClass owlCls = owlModel.getOWLNamedClass(owlModel.getResourceNameForURI(clsURI));
			if(owlCls.getInstanceCount(false)>0){
				for (Iterator iter = owlCls.getInstances(false).iterator(); iter.hasNext();) {
					Object obj = iter.next();
					if (obj instanceof OWLIndividual) {
						OWLIndividual individual = (OWLIndividual) obj;
						insList.add(individual.getName());
					}
				}
			}
		}
		return insList;
	}
	
	
	
	private ArrayList<String> searchConcept(SearchParameterObject searchObj, OntologyInfo ontoInfo) {
		logger.debug("getting search results size for search parameter object: " + searchObj);
		//printSearchParameterObject(searchObj);
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		ArrayList<String>conceptList = new ArrayList<String>();
		try {
			if(!ConfigConstants.ISNEWSEARCH)
			{
				conceptList = olddoSearchResults(searchObj, ontoInfo);
			}
			else
			{
				ArrayList<String> insList = new ArrayList<String>();
				if(ConfigConstants.ISINDEXING)
				{
					//STARRED MOD (use plain service and not SearchServiceImplOWLART to get back to previous version)
					if(searchObj.getKeyword().equals("") || searchObj.getKeyword() == null)
			    	{
						
						insList = getConcepIns(owlModel, ModelConstants.CNOUN, ModelConstants.RISLEXICALIZATIONOF, searchObj, insList);
			    	}
					else
					{
						if(searchObj.getRegex().equals(SearchParameterObject.EXACT_MATCH))
						{
							insList = getConcepIns(owlModel, ModelConstants.CNOUN, ModelConstants.RISLEXICALIZATIONOF, searchObj, insList);
							if(searchObj.getIncludeNotes())
							{
								insList = getConcepIns(owlModel, ModelConstants.CDEFINITION, ModelConstants.RISDEFINITIONOF, searchObj, insList);
								insList = getConcepIns(owlModel, ModelConstants.CIMAGE, ModelConstants.RISIMAGEOF, searchObj, insList);
							}
						}
						else
						{
							insList = getConceptInsList(owlModel, SearchServiceImplOWLART.getSearchResults(searchObj, ontoInfo));
						}
					}
				}
				else
				{
					//check terms
					insList = getConcepIns(owlModel, ModelConstants.CNOUN, ModelConstants.RISLEXICALIZATIONOF, searchObj, insList);
					if(searchObj.getIncludeNotes())
					{
						//check definition
						insList = getConcepIns(owlModel, ModelConstants.CDEFINITION, ModelConstants.RISDEFINITIONOF, searchObj, insList);
						//check image
						insList = getConcepIns(owlModel, ModelConstants.CIMAGE, ModelConstants.RISIMAGEOF, searchObj, insList);
					}
					
				}
				conceptList = doSearchResults(ontoInfo, searchObj, insList);
			}
			//owlModel.dispose();  //this was commented in ay case by SACHIT
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			///owlModel.dispose(); //this was commented in ay case by SACHIT
		}
		return conceptList;			
	}
	
	public ArrayList<SearchResultObject> requestSearchResultsRows(Request request, SearchParameterObject searchObj, OntologyInfo ontoInfo) 
	{	
		logger.debug("inside : requestSearchResultsRows(...)");
		ArrayList<SearchResultObject> retList = new ArrayList<SearchResultObject>();
		
		try
		{
			OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
			List<String> list = getSearchResults(searchObj, ontoInfo);
		    logger.debug("list of found classes: " + list);
		    
			// Get the sort info, even though we ignore it
		    ColumnSortList sortList = request.getColumnSortList();
		    
		    if(sortList!=null)
		    {
			    ColumnSortInfo csi = sortList.getPrimaryColumnSortInfo();
			    if(csi!=null)	
			    {
			    	Collections.sort(list, new DateComparator());
			    	if(!csi.isAscending())
			    		Collections.reverse(list);
			    }
		    }
		    
		    int startRow = request.getStartRow();
		    int numRow = request.getNumRows();
		    if(numRow <0) numRow = 0;
		    int endRow = startRow + numRow;
		    if(endRow > list.size()) endRow = list.size();
		    
			
			list = list.subList(startRow, endRow);
			
			retList = getSearchResultObject(owlModel, list, searchObj);
			
			/*for(String clsName: list)
			{
				OWLNamedClass cls = owlModel.getOWLNamedClass(clsName);
				logger.debug("(protege) cls found: " + cls);
				retList.add(getConceptObject(cls, searchObj));
			}
			
			logger.debug("startrow: " + startRow + "\nendrow: " + endRow);
			logger.debug("returned list of classes (those from startrow to endrow): " + retList);*/
			
			///owlModel.dispose();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return retList;
	}
	
	private ArrayList<SearchResultObject> getSearchResultObject(OWLModel owlModel, List<String> conceptList, SearchParameterObject searchObj)
	{
		ArrayList<SearchResultObject> retList = new ArrayList<SearchResultObject>();
		
		//check relationship
    	if(searchObj.getRelationship() != null ){	
    	    OWLObjectProperty prop = owlModel.getOWLObjectProperty(searchObj.getRelationship());
    		if(prop!=null)
			{
    			RelationshipObject rObj = ProtegeWorkbenchUtility.makeObjectRelationshipObject(prop);
    			for(String clsName: conceptList)
    			{
    				SearchResultObject searchResultObject = new SearchResultObject();
    				OWLNamedClass owlCls = owlModel.getOWLNamedClass(clsName);
        			searchResultObject.setRelationshipObject(rObj);
    				searchResultObject.setConceptObject(getConceptObject(owlCls, searchObj));
    				  
	    			for (Iterator<?> itr = ProtegeUtil.getConceptInstance(owlModel, owlCls).getPropertyValues(prop).iterator(); itr.hasNext();) {
	    				OWLIndividual destIndividual = (OWLIndividual) itr.next();
	    				OWLNamedClass owlDestcls = (OWLNamedClass) destIndividual.getProtegeType();
	    				searchResultObject.addDestConceptObject(getConceptObject(owlDestcls, searchObj));
	    			}
	    			retList.add(searchResultObject);  
    			}
	    	}
		}
    	else
    	{
    		for(String clsName: conceptList)
			{
				OWLNamedClass owlCls = owlModel.getOWLNamedClass(clsName);
				SearchResultObject searchResultObject = new SearchResultObject();
				searchResultObject.setConceptObject(getConceptObject(owlCls, searchObj));
				retList.add(searchResultObject);
			}
    	}
		return retList;
	
	}
	
	private boolean checkRDFSLiteral(Collection<Object> c, SearchParameterObject searchObj)
	{	    
		logger.debug("checking RDFS Literal: " + c);
	    for (Iterator<Object> lit = c.iterator(); lit.hasNext();) {
        	Object obj = (Object) lit.next();
        	if (obj instanceof DefaultRDFSLiteral) 
        	{
        	    DefaultRDFSLiteral rdfLiteral = (DefaultRDFSLiteral) obj;
        	    if(searchObj.getSelectedLangauge().contains(rdfLiteral.getLanguage().toLowerCase()))
	        	{
	        	    if(searchcheck(rdfLiteral.getString(), searchObj))
		        	{
		        		return true;
		        	}
	        	}
	        }
        }
		return false;
	}
	
	private boolean checkRDFSLiteral(Collection<Object> c, String keyword, String lang)
    {   
        for (Iterator<Object> lit = c.iterator(); lit.hasNext();)
        {
            Object obj = (Object) lit.next();
            if(lang!=null)
            {
                if (obj instanceof DefaultRDFSLiteral) 
                {
                    DefaultRDFSLiteral rdfLiteral = (DefaultRDFSLiteral) obj;                    
                    if(lang.contains(rdfLiteral.getLanguage().toLowerCase()))
                    {
                        if(rdfLiteral.getString().trim().equals(keyword.trim()))
                            return true;
                    }
                }
            }
            else
            {
               String objString = ""+obj;
               if(objString.trim().equals(keyword.trim()))
                    return true;
            }
        }
        return false;
    }
	
	private boolean checkLang(Collection<Object> c, ArrayList<String> selectedLang)
	{
		logger.debug("checking language");
		for (Iterator<Object> lit = c.iterator(); lit.hasNext();) {
        	Object obj = (Object) lit.next();
        	if (obj instanceof DefaultRDFSLiteral) 
        	{
	        	DefaultRDFSLiteral rdfLiteral = (DefaultRDFSLiteral) obj;
	        	return selectedLang.contains(rdfLiteral.getLanguage().toLowerCase());
	        }
        	
        }
		return false;
	}
	
	private boolean searchcheck(String searchContent, SearchParameterObject searchObj)
	{
	    logger.debug("search checking");
		if(searchObj.getKeyword().equals(""))
    	{
    		return true;
    	}
    	else
    	{
    		if (searchObj.getRegex().equals(SearchParameterObject.EXACT_MATCH)) 
        	{
        		if(searchObj.getCaseSensitive())
        		{
        			if(searchContent.equals(searchObj.getKeyword()))
        				return true;
        			else 
        				return false;
        		}
        		else
        		{
        			if(searchContent.toLowerCase().equals(searchObj.getKeyword().toLowerCase()))
        				return true;
        			else 
        				return false;
    			}
    		} 
    		else if (searchObj.getRegex().equals(SearchParameterObject.START_WITH)) 
    		{
    			if(searchObj.getCaseSensitive())
    			{
    				if(searchContent.startsWith(searchObj.getKeyword()))
    					return true;
    				else 
        				return false;
    			}
        		else
        		{
        			if(searchContent.toLowerCase().startsWith(searchObj.getKeyword().toLowerCase()))
        				return true;
        			else 
        				return false;
    			}
    		} 
    		else if (searchObj.getRegex().equals(SearchParameterObject.CONTAIN)) 
    		{    		    
    			if(searchObj.getCaseSensitive())
    			{
    				if(searchContent.contains(searchObj.getKeyword()))
    					return true;
    				else 
        				return false;
    			}
        		else
        		{
        			if(searchContent.toLowerCase().contains(searchObj.getKeyword().toLowerCase()))
        				return true;
        			else 
        				return false;
        		}
    		}
    		else if (searchObj.getRegex().equals(SearchParameterObject.END_WITH)) 
            {
                if(searchObj.getCaseSensitive())
                {
                    if(searchContent.endsWith(searchObj.getKeyword()))
                        return true;
                    else 
                        return false;
                }
                else
                {
                    if(searchContent.toLowerCase().endsWith(searchObj.getKeyword().toLowerCase()))
                        return true;
                    else 
                        return false;
                }
            }
    		else if (searchObj.getRegex().equals(SearchParameterObject.EXACT_WORD)) 
    		{
    		    String keyword = searchObj.getKeyword();
                String content = searchContent;
                
    		    if(!searchObj.getCaseSensitive())
    		    {
                    keyword = keyword.toLowerCase();
                    content = content.toLowerCase();
                }
    		    if(content.equals(keyword) || content.contains(" "+keyword+" ") || content.endsWith(" "+keyword) || content.startsWith(keyword+" "))
    		        return true; 
    		    else
                    return false;
    		}
    		else
    			return false;
    	}
	}
	
	private Date getDate(String date) 
	{
		if(!date.equals("null"))
		{
			Locale.setDefault(new Locale("en", "US"));
			SimpleDateFormat df;
			if(date.length()<11)
			{
				df = new SimpleDateFormat("yyyy-MM-dd");
			}
			else
				df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				return df.parse(date);
			} catch (ParseException e) {
				return null;
			}
		}
		else
			return null;
	}
	
	private TermObject getTermObject(OWLIndividual term, boolean includeTermCode)
	{
		logger.debug("getting term object: " + term + "with " + ((includeTermCode) ? "" : "no") + " code");
		OWLIndividual individual = (OWLIndividual) term.getPropertyValue(term.getOWLModel().getOWLProperty(ModelConstants.RISLEXICALIZATIONOF));
		OWLNamedClass cls = (OWLNamedClass) individual.getProtegeType();
		
		TermObject termObj = new TermObject();
    	termObj.setConceptUri(cls.getURI());	
    	termObj.setConceptName(cls.getName());
    	termObj.setUri(term.getURI());	
    	termObj.setName(term.getName());

    	for (Iterator<Object> lit = term.getLabels().iterator(); lit.hasNext();) {
        	Object obj = (Object) lit.next();
        	if (obj instanceof DefaultRDFSLiteral) {
	        	DefaultRDFSLiteral rdfLiteral = (DefaultRDFSLiteral) obj;
	        	termObj.setLabel(rdfLiteral.getString());	
	        	termObj.setLang(rdfLiteral.getLanguage());
	        }
    	}
        termObj.setStatus(""+term.getPropertyValue(term.getOWLModel().getOWLProperty(ModelConstants.RHASSTATUS)));
    	termObj.setMainLabel((""+term.getPropertyValue(term.getOWLModel().getOWLProperty(ModelConstants.RISMAINLABEL))).toLowerCase().equals("true"));
    	termObj.setDateCreate(getDate(""+term.getPropertyValue(term.getOWLModel().getOWLProperty(ModelConstants.RHASDATECREATED))));
    	termObj.setDateModified(getDate(""+term.getPropertyValue(term.getOWLModel().getOWLProperty(ModelConstants.RHASUPDATEDDATE))));
		
    	if(includeTermCode)
    	{
    		logger.debug("getting term code");
	    	for (Iterator<OWLDatatypeProperty> itc = term.getOWLModel().getOWLProperty(ModelConstants.RHASCODE).getSubproperties(false).iterator(); itc.hasNext();) 
	    	{
		    	OWLDatatypeProperty termCodeRelation = (OWLDatatypeProperty) itc.next();
		    	if(term.getPropertyValue(termCodeRelation)!=null)
		    	{
			    	AttributesObject tcObj = new AttributesObject();
			    	NonFuncObject nfObj = new NonFuncObject();
			    	nfObj.setValue(""+term.getPropertyValue(termCodeRelation));
					tcObj.setValue(nfObj);
					tcObj.setRelationshipObject(ProtegeWorkbenchUtility.makeDatatypeRelationshipObject(termCodeRelation));
					termObj.addTermCode(tcObj.getRelationshipObject().getUri(), tcObj);
		    	}
	    	}
    	}
    	logger.debug("returning term object: " + termObj);
    	return termObj;
	}
	
	public ConceptObject getConceptObject(OWLNamedClass cls, SearchParameterObject searchObj) 
	{
		logger.debug("getting concept object: " + cls + " with search parameter: " + searchObj);
		ConceptObject cObj = new ConceptObject();
		for (Iterator<OWLIndividual> jt = cls.getInstances(false).iterator(); jt.hasNext();) 
		{
        	OWLIndividual individual = (OWLIndividual) jt.next();
			cObj.setConceptInstance(individual.getURI());
			cObj.setUri(cls.getURI());
			cObj.setName(cls.getName());
			cObj.setBelongsToModule(ConceptObject.CONCEPTMODULE);
			
			if(searchObj.getScheme() != null && !searchObj.getScheme().equals("--None--"))
        	{
				OWLProperty prop = cls.getOWLModel().getOWLProperty(ModelConstants.RBELONGSTOSCHEME);
				if(prop!=null)
				{
					for (Iterator<OWLIndividual> tr = individual.getPropertyValues(prop).iterator(); tr.hasNext();) {
						OWLIndividual scheme = tr.next();
						if(scheme.getURI().equals(searchObj.getScheme()))
						{
							cObj.setBelongsToModule(ConceptObject.CLASSIFICATIONMODULE);
							cObj.setScheme(scheme.getURI());
						}
					}
				}
        	}
			cObj.setStatus(""+individual.getPropertyValue(cls.getOWLModel().getOWLProperty(ModelConstants.RHASSTATUS)));
			cObj.setDateCreate(getDate(""+individual.getPropertyValue(cls.getOWLModel().getOWLProperty(ModelConstants.RHASDATECREATED))));
			cObj.setDateModified(getDate(""+individual.getPropertyValue(cls.getOWLModel().getOWLProperty(ModelConstants.RHASUPDATEDDATE))));
    
            for (Iterator<OWLIndividual> t = individual.getPropertyValues(cls.getOWLModel().getOWLProperty(ModelConstants.RHASLEXICALIZATION)).iterator(); t.hasNext();) 
            {
	            OWLIndividual term = (OWLIndividual)t.next();
	        	//if((""+term.getPropertyValue(term.getOWLModel().getOWLProperty(ModelConstants.RISMAINLABEL))).toLowerCase().equals("true"))  
	            if(checkLang(term.getLabels(), searchObj.getSelectedLangauge()))
	            {	            		
	                cObj.addTerm(term.getURI(), getTermObject(term, false));
	            }
            }
	    }
		logger.debug("returning concept object: " + cObj);
		return cObj;
	}
	
	private class DateComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			Date s1 = null;
			Date s2 = null;
			if(o1 instanceof ConceptObject)
				s1 = ((ConceptObject)o1).getDateModified();
			if(o2 instanceof ConceptObject)
				s2 = ((ConceptObject)o2).getDateModified();

			if(s1 == null && s2 ==null)
				return 0;
			else if(s1 == null)
				return -1;
			else if(s2 ==null)
				return 1;
			else 
				return s1.compareTo(s2);
		}	 
	}

	private ArrayList sort(ArrayList table) {
		logger.debug("sorting: " + table);
		ArrayList sortedLabelsList = new ArrayList();
		ArrayList sortedResultTable = new ArrayList();
		HashMap tableMap = new HashMap();

		for (int i = 0; i < table.size(); i++) {
			ConceptObject cObj = (ConceptObject) table.get(i);
			sortedLabelsList.add(cObj.getDateModified()+cObj.getUri());
			tableMap.put(cObj.getDateModified()+cObj.getUri(), cObj);
		}
		Collections.sort(sortedLabelsList, String.CASE_INSENSITIVE_ORDER);
		for (int i = 0; i < sortedLabelsList.size(); i++) {
			String key = (String) sortedLabelsList.get(i);
			sortedResultTable.add(tableMap.get(key));
		}
		return sortedResultTable;
	}
	
	public SuggestOracle.Response getSuggestions(AOSSuggestOracleRequest req, boolean includeNotes, ArrayList<String> languages, OntologyInfo ontoInfo) {
		logger.debug("inside getSuggestions");
		AOSSuggestOracleResponse resp = new AOSSuggestOracleResponse();
		resp.setTimestamp(req.getTimestamp());
        // Now set the suggestions in the response
		if(ConfigConstants.ISINDEXING)
		{
			//STARRED MOD (just switch the comment on the following two lines to get back to previous version)
	        resp.setSuggestions(SearchServiceImplOWLART.getSuggestionList(ontoInfo, includeNotes, languages, req));
		}
		else
		{
			resp.setSuggestions(getSuggestionList(ontoInfo, languages, req));
		}
        // Send the response back to the client
        return resp;
	}
	
	private List<Suggestion> getSuggestionList(OntologyInfo ontoInfo, ArrayList<String> languages, SuggestOracle.Request req)
	{
		logger.debug("inside getSuggestionList");
		List<Suggestion> suggestions = new ArrayList<Suggestion>(req.getLimit());
		List<String> labelList = new ArrayList<String>();
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass nounCls = owlModel.getOWLNamedClass(ModelConstants.CNOUN);
		if(nounCls!=null)
		{
			for (Iterator iter = nounCls.getInstances(true).iterator(); iter.hasNext();) 
			{
				OWLIndividual termInstance = (OWLIndividual) iter.next();
				for (Iterator iterator = termInstance.getLabels().iterator(); iterator.hasNext();) 
				{
					Object obj = iterator.next();
			    	if (obj instanceof DefaultRDFSLiteral) 
			    	{
			    		DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
			    		String label = element.getString();
			    		String lang = element.getLanguage().toLowerCase();
			    		if(languages.contains(lang) && label.toLowerCase().startsWith(req.getQuery().toLowerCase()))
			    		{
			    			if(!labelList.contains(label))
			    				labelList.add(label);
			    		}
					}
				}
				
				if(labelList.size()>req.getLimit()-1)
				{
					break;
				}
			}
			for (String label: labelList) 
			{
				suggestions.add(new ItemSuggestion(label, label));
			}
		}
		///owlModel.dispose();
		return suggestions;
	}
	
	public Integer indexOntology(OntologyInfo ontoInfo)
	{
		if(ConfigConstants.ISINDEXING)
		{
			//STARRED MOD (Enable below line to index ontology using index button)
			try
			{
				IndexingEngineFactory.removeSearchEngine(ontoInfo);
				return 0; 
				//this method will never be launched again (only offline indexing), anyway I'm leaving it
				//SearchServiceImplOWLART.indexOntologyCNOUNS(ontoInfo);
			}
			catch(Exception e)
			{
				return 0;	
			}
		}
		else
			return -1;

	}
	
	public boolean reloadSearchEngine(OntologyInfo ontoInfo)
	{
		return IndexingEngineFactory.reloadSearchEngine(ontoInfo);
	}
	
	public static void main(String[] args)
	{
		SystemServiceImpl ssImpl= new SystemServiceImpl();
		ssImpl.loadConfigConstants();
		ssImpl.loadModelConstants();
		
		OntologyInfo ontoInfo = new OntologyInfo();
		ontoInfo.setDbDriver("com.mysql.jdbc.Driver");
		ontoInfo.setDbPassword("@grovoc");
		ontoInfo.setDbTableName("agrovoc_wb_20110223");
		ontoInfo.setDbUrl("jdbc:mysql://203.185.67.131:3306/agrovoc_wb_20110223?requireSSL=false&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&autoReconnectForPools=true");
		ontoInfo.setDbUsername("root");
		ontoInfo.setOntologyId(1);
		
		ArrayList<String> selectedLangauge = new ArrayList<String>();
		selectedLangauge.add("en");
		selectedLangauge.add("fr");
		
		
		SearchParameterObject searchObj = new SearchParameterObject();
		searchObj.setKeyword("mains");
		searchObj.setRegex(SearchParameterObject.EXACT_WORD);
		searchObj.setSelectedLangauge(selectedLangauge);
		
				
		SearchServiceImpl ssi = new SearchServiceImpl();
		ssi.searchConcept(searchObj, ontoInfo);
	}

}
