package org.fao.aoscs.server;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;

import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.gwt.GwtConfigurationHelper;
import net.sf.gilead.gwt.PersistentRemoteService;

import org.fao.aoscs.client.module.consistency.service.ConsistencyService;
import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.client.module.constant.OWLStatusConstants;
import org.fao.aoscs.domain.AttributesObject;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.Consistency;
import org.fao.aoscs.domain.ConsistencyInitObject;
import org.fao.aoscs.domain.NonFuncObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.TermObject;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.hibernate.QueryFactory;
import org.fao.aoscs.server.protege.ProtegeModelFactory;
import org.fao.aoscs.server.protege.ProtegeWorkbenchUtility;

import edu.stanford.smi.protegex.owl.model.OWLDataRange;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFSLiteral;

public class ConsistencyServiceImpl extends PersistentRemoteService implements ConsistencyService{
	
	private static final long serialVersionUID = 288904786686164268L;

	//-------------------------------------------------------------------------
	//
	// Initialization of Remote service : must be done before any server call !
	//
	//-------------------------------------------------------------------------
	@Override
	public void init() throws ServletException
	{
		super.init();
		
	//	Bean Manager initialization
		setBeanManager(GwtConfigurationHelper.initGwtStatelessBeanManager( new HibernateUtil(HibernateUtilities.getSessionFactory())));;
		
	}
	
	public ConsistencyInitObject getInitData(OntologyInfo ontoInfo) {
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		ConsistencyInitObject cio = new ConsistencyInitObject();
		cio.setStatus(getStatus(owlModel));
		cio.setTermCode(getTermCodeProperty(owlModel));
		//cio.setLanguage(getLanguages());
		///owlModel.dispose();
		return cio;
	}
	
	public ArrayList<String> getStatus(OWLModel owlModel) {
		ArrayList<String> statusList = new ArrayList<String>();
		for (Iterator<DefaultRDFSLiteral> it = ((OWLDataRange)owlModel.getOWLProperty(ModelConstants.RHASSTATUS).getRange()).getOneOfValueLiterals().iterator(); it.hasNext();) {
	    	statusList.add(""+it.next());
		}
		return statusList;
	}
	
	public HashMap<String, Consistency> getConsistencyQueue(int selection, OntologyInfo ontoInfo) {
		HashMap<String, Consistency> result = new HashMap<String, Consistency>();
		try 
		{
			OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
			switch(selection)
			{
				case 1:
					result = case1(owlModel);
					break;
				case 2:
					result = case2(owlModel);
					break;
				case 3:
					result = case3(owlModel);
					break;
				case 4:
					result = case4(owlModel);
					break;
				case 5:
					result = case5(owlModel);
					break;
				case 6:
					result = case6(owlModel);
					break;
				case 7:
					result = case7(owlModel);
					break;
				case 8:
					result = case8(owlModel);
					break;
				case 9:
					result = case9(owlModel);
					break;
				case 10:
					result = case10(owlModel);
					break;
				case 11:
					result = case11(owlModel);
					break;
				case 12:
					result = case12(owlModel);
					break;
				case 13:
					result = case13(owlModel);
					break;
				case 14:
					result = case14(owlModel);
					break;
				case 15:
					result = case15(owlModel);
					break;
				case 16:
					result = case16(owlModel);
					break;
				case 17:
					result = case17(owlModel);
					break;
				case 18:
					result = case18(owlModel);
					break;
				case 19:
					result = case19(owlModel);
					break;
				default:
					break;
			}
			///owlModel.dispose();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return result;
	}
	
	public HashMap<String, Consistency> updateConsistencyQueue(List consistencyList, int selection, OntologyInfo ontoInfo) {
		Iterator itr = consistencyList.iterator();
		while (itr.hasNext()) {
			Consistency c = (Consistency) itr.next();
			updateTriple(ontoInfo, c, selection);
		}
		return getConsistencyQueue(selection, ontoInfo);
	}
	
	public void updateTriple(OntologyInfo ontoInfo, Consistency c, int selection)
	{
		switch (selection) {

		case 4: 
			OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
			if(c.getTerm().getUri()==null)
			{
				updateConcept(owlModel, c.getConcept().getName(), c.getStatus());
			}
			else
			{
				updateTerm(owlModel, c.getTerm().getConceptName(), c.getTerm().getUri(), c.getStatus());
			}
			///owlModel.dispose();
		}
	}
	
	public ArrayList<String> getTermCodeProperty(OWLModel owlModel)
	{
		ArrayList<String> tlist = new ArrayList<String>();
		for (Iterator<DefaultOWLDatatypeProperty> it = owlModel.getOWLProperty(ModelConstants.RHASCODE).getSubproperties(false).iterator(); it.hasNext();) {
			DefaultOWLDatatypeProperty prop = (DefaultOWLDatatypeProperty) it.next();
			for (Iterator<DefaultRDFSLiteral> lit = prop.getLabels().iterator(); lit.hasNext();) 
			{
				DefaultRDFSLiteral rdfLiteral = (DefaultRDFSLiteral) lit.next();
				tlist.add(rdfLiteral.getString());
			}
		}
		return tlist;
	}
	
	public ArrayList<String[]> getOwlStatus()
	{
		String query = "SELECT status FROM owl_status";
		return QueryFactory.getHibernateSQLQuery(query);
	}
	
	public ArrayList<String> getLanguageCodes()
	{
		String languageQuery ="SELECT language_code FROM language_code ORDER BY language_code;";
		ArrayList<String> language = new ArrayList<String>();
		ArrayList tmp = QueryFactory.getHibernateSQLQuery(languageQuery);
		for(int i=0;i<tmp.size();i++){
			String[] item = (String[]) tmp.get(i);
			language.add(item[0].toLowerCase());
		}
		return language;
	}
	
	public ArrayList<String[]> getLanguages()
	{
		String languageQuery ="SELECT language_code,language_note FROM language_code ORDER BY language_code;";
		return QueryFactory.getHibernateSQLQuery(languageQuery);
	}
	
	public void updateConcept(OWLModel owlModel, String conceptName, String status)
	{
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptName);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		individual.setPropertyValue(cls.getOWLModel().getOWLProperty(ModelConstants.RHASSTATUS), status);
		individual.setPropertyValue(cls.getOWLModel().getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
	}
	
	public void updateTerm(OWLModel owlModel, String conceptName, String termURI, String status)
	{
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptName);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLIndividual term = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, individual, cls.getOWLModel().getOWLProperty(ModelConstants.RHASLEXICALIZATION), termURI);
        term.setPropertyValue(cls.getOWLModel().getOWLProperty(ModelConstants.RHASSTATUS), status);
        term.setPropertyValue(cls.getOWLModel().getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
        individual.setPropertyValue(cls.getOWLModel().getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
	}
	
	public Date getDate(String date) 
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
	
	public TermObject getTermObject(OWLIndividual term, boolean includeTermCode)
	{
		
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
	    	for (Iterator<OWLDatatypeProperty> itc = term.getOWLModel().getOWLProperty(ModelConstants.RHASCODE).getSubproperties(false).iterator(); itc.hasNext();) 
	    	{
		    	OWLDatatypeProperty termCodeRelation = (OWLDatatypeProperty) itc.next();
		    	if(term.getPropertyValue(termCodeRelation)!=null)
		    	{
			    	AttributesObject tcObj = new AttributesObject();
			    	NonFuncObject nfObject = new NonFuncObject();
			    	nfObject.setValue(""+term.getPropertyValue(termCodeRelation));
					tcObj.setValue(nfObject);
					tcObj.setRelationshipObject(ProtegeWorkbenchUtility.makeDatatypeRelationshipObject(termCodeRelation));
					termObj.addTermCode(tcObj.getRelationshipObject().getUri(), tcObj);
		    	}
	    	}
    	}
    	return termObj;
	}
	
	public ConceptObject getConceptObject(OWLNamedClass cls) 
	{
		ConceptObject cObj = new ConceptObject();
		for (Iterator<OWLIndividual> jt = cls.getInstances(false).iterator(); jt.hasNext();) 
		{
        	OWLIndividual individual = (OWLIndividual) jt.next();
			cObj.setConceptInstance(individual.getURI());
			cObj.setUri(cls.getURI());
			cObj.setName(cls.getName());
			cObj.setStatus(""+individual.getPropertyValue(cls.getOWLModel().getOWLProperty(ModelConstants.RHASSTATUS)));
			cObj.setDateCreate(getDate(""+individual.getPropertyValue(cls.getOWLModel().getOWLProperty(ModelConstants.RHASDATECREATED))));
			cObj.setDateModified(getDate(""+individual.getPropertyValue(cls.getOWLModel().getOWLProperty(ModelConstants.RHASUPDATEDDATE))));
    
            for (Iterator<OWLIndividual> t = individual.getPropertyValues(cls.getOWLModel().getOWLProperty(ModelConstants.RHASLEXICALIZATION)).iterator(); t.hasNext();) 
            {
	            OWLIndividual term = (OWLIndividual)t.next();
	        	if((""+term.getPropertyValue(term.getOWLModel().getOWLProperty(ModelConstants.RISMAINLABEL))).toLowerCase().equals("true"))
	            	cObj.addTerm(term.getURI(), getTermObject(term, false));
            }
	    }
		return cObj;
	}
	
	public RelationshipObject getRelationshipObject(DefaultOWLObjectProperty rel)
	{
		RelationshipObject rObj = new RelationshipObject();
    	rObj.setUri(rel.getURI());
    	rObj.setName(rel.getName());
    	for (Iterator<Object> iterator = rel.getLabels().iterator(); iterator.hasNext();) {
    		Object obj = (Object) iterator.next();
    		if (obj instanceof DefaultRDFSLiteral) {
				DefaultRDFSLiteral label = (DefaultRDFSLiteral) obj;
				rObj.addLabel(label.getString(), label.getLanguage());
			}
		}
    	return rObj;
	}
	
	public HashMap<String, Consistency> case1(OWLModel owlModel) throws ParseException
	{
		HashMap<String, Consistency> result = new HashMap<String, Consistency>();
	    for (Iterator it = owlModel.getOWLNamedClass(ModelConstants.CDOMAINCONCEPT).getSubclasses(true).iterator(); it.hasNext();) {
	    	OWLNamedClass cls = (OWLNamedClass) it.next();
			for (Iterator<OWLIndividual> jt = cls.getInstances(false).iterator(); jt.hasNext();) {
	        	
	        	OWLIndividual individual = (OWLIndividual) jt.next();
	        	String status = ""+individual.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASSTATUS));
	        	if(status.equals("validated") || status.equals("published"))
	            {	
		        	Date cDate = getDate(""+individual.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED)));
					Date uDate = getDate(""+individual.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE)));

					Consistency slot = new Consistency();
					ConceptObject cObj = new ConceptObject();
					cObj.setUri(cls.getURI());
					cObj.setStatus(status);
					cObj.setDateCreate(cDate);
					cObj.setDateModified(uDate);
					HashMap<String, ArrayList<TermObject>> ht = new HashMap<String, ArrayList<TermObject>>();
					Iterator<OWLIndividual> itr = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION)).iterator();
		            for (Iterator<OWLIndividual> t = itr; t.hasNext();) {
			            OWLIndividual term = (OWLIndividual)t.next();
			        	TermObject tObj = (TermObject) getTermObject(term, false);
			        	String lang = tObj.getLang();
			        	if(ht.containsKey(lang))
						{
			        		if(tObj.isMainLabel())
			        		{
			        			ht.get(lang).add(tObj);
			        		}
			        		cObj.addTerm(tObj.getUri(), tObj);
						}
						else
						{
							ArrayList<TermObject> termList = new ArrayList<TermObject>();
							if(tObj.isMainLabel())	
							{
								termList.add(tObj);	
							}
							cObj.addTerm(tObj.getUri(), tObj);
							ht.put(lang, termList);
						}
		            }

		            for (Iterator<String> t = ht.keySet().iterator(); t.hasNext();) 
					{
						String lang = (String) t.next();
						ArrayList<TermObject> termlist = (ArrayList<TermObject>) ht.get(lang);
						if(termlist.size() != 1)
						{
							slot.setConcept(cObj);
				            slot.setStatus(status);
				            slot.setDateCreate(cDate);
				            slot.setDateModified(uDate);
				            result.put(cls.getURI(), slot);
							break;
						}
					}
	            }
	        }
	    }
	    return result;
	}
	
	public HashMap<String, Consistency> case2(OWLModel owlModel) throws ParseException
	{
		HashMap<String, ArrayList<TermObject>> ht = new HashMap<String, ArrayList<TermObject>>();
		for (Iterator it = owlModel.getOWLNamedClass(ModelConstants.CDOMAINCONCEPT).getSubclasses(true).iterator(); it.hasNext();) 
		{
	    	OWLNamedClass cls = (OWLNamedClass) it.next();
			for (Iterator<OWLIndividual> jt = cls.getInstances(false).iterator(); jt.hasNext();) 
			{
	        	OWLIndividual individual = (OWLIndividual) jt.next();
	        	String status = ""+individual.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASSTATUS));
	        	if(status.equals("validated") || status.equals("published"))
	            {	
		            for (Iterator<OWLIndividual> t = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION)).iterator(); t.hasNext();) {
			        	TermObject tObj = (TermObject) getTermObject(t.next(), false);
			        	if(tObj.isMainLabel())
						{
			        		String lang = tObj.getLang();
			        		if(ht.containsKey(lang))
							{
				        		if(tObj.isMainLabel())	ht.get(lang).add(tObj);
							}
							else
							{
								ArrayList<TermObject> termList = new ArrayList<TermObject>();
								termList.add(tObj);	
								ht.put(lang, termList);
							}
						}
		            }
	            }
	        }
	    }
		HashMap<String, ArrayList<TermObject>> hTable = new HashMap<String, ArrayList<TermObject>>();
		for (Iterator<String> t = ht.keySet().iterator(); t.hasNext();) 
    	{
    		ArrayList<TermObject> termlist = (ArrayList<TermObject>) ht.get(t.next());
    		if(termlist!=null)
    		{
    			for(int j=0;j<termlist.size();j++)
    			{
    				TermObject to = (TermObject) termlist.get(j);
    				if(to!=null)
    				{
    					String label = to.getLabel();
    					if(hTable.containsKey(label))
    					{
    						hTable.get(label).add(to);
    					}
    					else
    					{
    						ArrayList<TermObject> t1List = new ArrayList<TermObject>();
    						t1List.add(to);
    						hTable.put(label, t1List);
    					}
    				}
    			}
    		}
    	}
		HashMap<String, Consistency> result = new HashMap<String, Consistency>();
        int cnt = 0;
        for (Iterator<String> iit = hTable.keySet().iterator(); iit.hasNext();) 
		{
			ArrayList<TermObject> termlist = (ArrayList<TermObject>) hTable.get(iit.next());
			if(termlist!=null)
			{
				if(termlist.size() != 1)
				{
					Consistency slot = new Consistency();
					ConceptObject cObj = new ConceptObject();
					cObj.setUri(ModelConstants.COMMONBASENAMESPACE+cnt);
					for(int i=0;i<termlist.size();i++)
					{
						TermObject tObj = (TermObject)termlist.get(i);
						tObj.setStatus(""+ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(tObj.getConceptName())).getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASSTATUS)));
						cObj.addTerm(tObj.getUri(), tObj);
					}
					slot.setConcept(cObj);
		            result.put(ModelConstants.COMMONBASENAMESPACE+cnt, slot);
		            cnt++;
				}
			}
		}
	    return result;
	}
	public HashMap<String, Consistency> case3(OWLModel owlModel) throws ParseException
	{
		HashMap<String, Consistency> result = new HashMap<String, Consistency>();
	    for (Iterator<OWLNamedClass> it = owlModel.getOWLNamedClass(ModelConstants.CDOMAINCONCEPT).getSubclasses(true).iterator(); it.hasNext();) {
			for (Iterator<OWLIndividual> jt = it.next().getInstances(false).iterator(); jt.hasNext();) {
	            for (Iterator<OWLIndividual> t = jt.next().getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION)).iterator(); t.hasNext();) {
		            OWLIndividual term = (OWLIndividual)t.next();
		        	if(term.getPropertyValue(term.getOWLModel().getOWLProperty(ModelConstants.RISLEXICALIZATIONOF))==null)
		        	{
		        		Consistency slot = new Consistency();
		        		TermObject tObj = getTermObject(term, false);
		            	slot.setTerm(tObj);
			            slot.setStatus(tObj.getStatus());
			            slot.setDateCreate(tObj.getDateCreate());
			            slot.setDateModified(tObj.getDateModified());
			            result.put(tObj.getUri(), slot);
		        	}
	            }   
	        }
	    }
	    return result;
	}
	public HashMap<String, Consistency> case4(OWLModel owlModel) throws ParseException
	{
		HashMap<String, Consistency> result = new HashMap<String, Consistency>();
		HashMap<String, Consistency> resultTerm = new HashMap<String, Consistency>();
		for (Iterator<OWLNamedClass> it = owlModel.getOWLNamedClass(ModelConstants.CDOMAINCONCEPT).getSubclasses(true).iterator(); it.hasNext();) 
		{
			OWLNamedClass cls = it.next();
			for (Iterator<OWLIndividual> jt = cls.getInstances(false).iterator(); jt.hasNext();) 
			{
				OWLIndividual individual = (OWLIndividual)jt.next();
				String status = ""+individual.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASSTATUS));
				ConceptObject cObj = new ConceptObject();
				for (Iterator<OWLIndividual> t = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION)).iterator(); t.hasNext();) {
		            OWLIndividual term = (OWLIndividual)t.next();
		            TermObject tObj = getTermObject(term, false);
		        	if((""+term.getPropertyValue(term.getOWLModel().getOWLProperty(ModelConstants.RISMAINLABEL))).toLowerCase().equals("true"))
		            	cObj.addTerm(term.getURI(), tObj);
		        	String st = tObj.getStatus();
		        	if(st.equals("")||st.equals("null")||st==null)
		        	{
		        		Consistency slt = new Consistency();
		        		slt.setTerm(tObj);
		        		slt.setStatus(tObj.getStatus());
		        		slt.setDateCreate(tObj.getDateCreate());
		        		slt.setDateModified(tObj.getDateModified());
		        		resultTerm.put(tObj.getUri(), slt);
		        	}
	            }
	            
	            if(status.equals("")||status.equals("null")||status==null)
	            {
	            	Date cDate = getDate(""+individual.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED)));
	            	Date uDate = getDate(""+individual.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE)));
	            	cObj.setUri(cls.getURI()); 
					cObj.setStatus(status);
					cObj.setDateCreate(cDate);
					cObj.setDateModified(uDate);
					
					Consistency slot = new Consistency();
					slot.setConcept(cObj);
		            slot.setStatus(status);
		            slot.setDateCreate(cDate);
		            slot.setDateModified(uDate);
		            result.put(cls.getURI(), slot);
	            }
	        }
	    }

		for (Iterator<String> itr = resultTerm.keySet().iterator(); itr.hasNext();) 
		{
			String key = itr.next();
			result.put(key, resultTerm.get(key));
		}
	    return result;
	}
	
	public HashMap<String, Consistency> case5(OWLModel owlModel) throws ParseException
	{
		HashMap<String, Consistency> result = new HashMap<String, Consistency>();
		for (Iterator<OWLNamedClass> it = owlModel.getOWLNamedClass(ModelConstants.CDOMAINCONCEPT).getSubclasses(true).iterator(); it.hasNext();) 
		{
			OWLNamedClass cls = it.next();
			for (Iterator<OWLIndividual> jt = cls.getInstances(false).iterator(); jt.hasNext();) 
			{
				OWLIndividual instance = (OWLIndividual)jt.next();
				String st = ""+instance.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASSTATUS));
				if(st.equals("deprecated"))
				{
					for (Iterator<OWLNamedClass> itn = instance.getProtegeType().getSubclasses(false).iterator(); itn.hasNext();) 
					{
						OWLNamedClass subcls = itn.next();
						for (Iterator<OWLIndividual> jtn = subcls.getInstances(false).iterator(); jtn.hasNext();) 
						{
							OWLIndividual individual = (OWLIndividual)jtn.next();
							ConceptObject cObj = new ConceptObject();
							Date cDate = getDate(""+individual.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED)));
			            	Date uDate = getDate(""+individual.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE)));
			            	String status = ""+individual.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASSTATUS));
			            	cObj.setUri(subcls.getURI()); 
							cObj.setStatus(status);
							cObj.setDateCreate(cDate);
							cObj.setDateModified(uDate);
							
							for (Iterator<OWLIndividual> t = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION)).iterator(); t.hasNext();) {
					            OWLIndividual term = (OWLIndividual)t.next();
					        	if((""+term.getPropertyValue(term.getOWLModel().getOWLProperty(ModelConstants.RISMAINLABEL))).toLowerCase().equals("true"))
					            	cObj.addTerm(term.getURI(), getTermObject(term, false));
				        	}
							
							Consistency slot = new Consistency();
							slot.setConcept(cObj);
				            slot.setStatus(status);
				            slot.setDateCreate(cDate);
				            slot.setDateModified(uDate);
				            result.put(subcls.getURI(), slot);	
				         }
					}
				}
	        }
	    }
	    return result;
	}
	
	public HashMap<String, Consistency> case6(OWLModel owlModel) throws ParseException
	{
		HashMap<String, Consistency> result = new HashMap<String, Consistency>();
		for (Iterator<OWLNamedClass> it = owlModel.getOWLNamedClass(ModelConstants.CDOMAINCONCEPT).getSubclasses(false).iterator(); it.hasNext();) {
			OWLNamedClass cls = it.next();
			if(cls.getSubclassCount()<1) {
				int cnt = 0;
				for (Iterator<OWLIndividual> jt = cls.getInstances(false).iterator(); jt.hasNext();) {
					OWLIndividual individual = (OWLIndividual)jt.next();
					for (Iterator<DefaultOWLObjectProperty> itr = owlModel.getOWLProperty(ModelConstants.RHASRELATEDCONCEPT).getSubproperties(false).iterator(); itr.hasNext();) {
		        		if(individual.getPropertyValueCount(owlModel.getOWLProperty(itr.next().getName()))>0)	cnt++;		
		        	}

					for (Iterator<OWLIndividual> t = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION)).iterator(); t.hasNext();) {
						OWLIndividual term = (OWLIndividual)t.next();
						for (Iterator<DefaultOWLObjectProperty> itr = owlModel.getOWLProperty(ModelConstants.RHASRELATEDTERM).getSubproperties(false).iterator(); itr.hasNext();) {
			        		if(term.getPropertyValueCount(owlModel.getOWLProperty(itr.next().getName()))>0)	cnt++;	
			        	}
		        	}
		        }
				if(cnt==0) {
					ConceptObject conceptObj = getConceptObject(cls);
					Consistency slot = new Consistency();
					slot.setConcept(conceptObj);
		            slot.setStatus(conceptObj.getStatus());
		            slot.setDateCreate(conceptObj.getDateCreate());
		            slot.setDateModified(conceptObj.getDateModified());
		            result.put(cls.getURI(), slot);	
				}
			}
	    }
		return result;
	}
	
	public HashMap<String, Consistency> case7(OWLModel owlModel) throws ParseException
	{
		HashMap<String, Consistency> result = new HashMap<String, Consistency>();
		for (Iterator<OWLNamedClass> it = owlModel.getOWLNamedClass(ModelConstants.CDOMAINCONCEPT).getSubclasses(true).iterator(); it.hasNext();) 
		{
			OWLNamedClass cls = it.next();
			for (Iterator<OWLIndividual> jt = cls.getInstances(false).iterator(); jt.hasNext();) 
			{
				OWLIndividual individual = (OWLIndividual)jt.next();
				for (Iterator<DefaultOWLObjectProperty> itr = owlModel.getOWLProperty(ModelConstants.RHASRELATEDCONCEPT).getSubproperties(false).iterator(); itr.hasNext();) {	
					DefaultOWLObjectProperty rel = itr.next();
					OWLIndividual destindividual = (OWLIndividual) individual.getPropertyValue(individual.getOWLModel().getOWLProperty(rel.getName()));
					if(destindividual!=null)
					{
						OWLNamedClass destcls = (OWLNamedClass) destindividual.getProtegeType();
	        			if(cls.getSuperclasses(true).contains(destcls) || cls.getSubclasses(true).contains(destcls))
	        			{
	        				ConceptObject conceptObj = getConceptObject(cls);
	        				ConceptObject destconceptObj = getConceptObject(destcls);
	        				Consistency slot = new Consistency();
	        				slot.setConcept(conceptObj);
	        	            slot.setStatus(conceptObj.getStatus());
	        	            slot.setRelationship(getRelationshipObject(rel));
	        	            slot.setDestConcept(destconceptObj);
	        	            slot.setDestStatus(destconceptObj.getStatus());
	        	            result.put(cls.getURI(), slot);
	        			}
					}
	        	}	
	        }	
	    }
	    return result;
	}
	
	public HashMap<String, Consistency> case8(OWLModel owlModel) throws ParseException
	{
		HashMap<String, Consistency> result = new HashMap<String, Consistency>();
		for (Iterator<OWLNamedClass> it = owlModel.getOWLNamedClass(ModelConstants.CDOMAINCONCEPT).getSubclasses(true).iterator(); it.hasNext();) 
		{
			OWLNamedClass cls = it.next();
			for (Iterator<OWLIndividual> jt = cls.getInstances(false).iterator(); jt.hasNext();) 
			{
				OWLIndividual individual = (OWLIndividual)jt.next();
				for (Iterator<DefaultOWLObjectProperty> itr = owlModel.getOWLProperty(ModelConstants.RHASRELATEDCONCEPT).getSubproperties(false).iterator(); itr.hasNext();) {	
					DefaultOWLObjectProperty rel = itr.next();
					if(!rel.isSymmetric())
					{
						OWLIndividual destindividual = (OWLIndividual) individual.getPropertyValue(rel);
						if(destindividual!=null)
						{
							OWLIndividual orgindividual = (OWLIndividual) destindividual.getPropertyValue(rel);
							if(orgindividual!=null)
							{
								if(individual.getURI().equals(orgindividual.getURI()))
								{
			        				ConceptObject conceptObj = getConceptObject(cls);
			        				ConceptObject destconceptObj = getConceptObject((OWLNamedClass) destindividual.getProtegeType());
			        				Consistency slot = new Consistency();
			        				slot.setConcept(conceptObj);
			        	            slot.setStatus(conceptObj.getStatus());
			        	            slot.setRelationship(getRelationshipObject(rel));
			        	            slot.setDestConcept(destconceptObj);
			        	            slot.setDestStatus(destconceptObj.getStatus());
			        	            result.put(cls.getURI(), slot);
								}
							}
						}
					}
	        	}	
	        }	
	    }
		return result;
	}
	
	public HashMap<String, Consistency> case9(OWLModel owlModel) throws ParseException
	{
		HashMap<String, Consistency> result = new HashMap<String, Consistency>();
		for (Iterator<OWLNamedClass> it = owlModel.getOWLNamedClass(ModelConstants.CDOMAINCONCEPT).getSubclasses(true).iterator(); it.hasNext();) 
		{
			OWLNamedClass cls = it.next();
			for (Iterator<OWLIndividual> jt = cls.getInstances(false).iterator(); jt.hasNext();) 
			{
				OWLIndividual individual = (OWLIndividual)jt.next();
				for (Iterator<DefaultOWLObjectProperty> itr = owlModel.getOWLProperty(ModelConstants.RHASRELATEDCONCEPT).getSubproperties(false).iterator(); itr.hasNext();) {	
					DefaultOWLObjectProperty rel = itr.next();
					if(!rel.isSymmetric())
					{
						OWLIndividual destindividual = (OWLIndividual) individual.getPropertyValue(rel);
						if(destindividual!=null && rel.getInverseProperty()!=null)
						{
							OWLIndividual orgindividual = (OWLIndividual) destindividual.getPropertyValue(rel.getInverseProperty());
							if(orgindividual==null || !individual.getURI().equals(orgindividual.getURI()))
							{
		        				ConceptObject conceptObj = getConceptObject(cls);
		        				ConceptObject destconceptObj = getConceptObject((OWLNamedClass) destindividual.getProtegeType());
		        				Consistency slot = new Consistency();
		        				slot.setConcept(conceptObj);
		        	            slot.setStatus(conceptObj.getStatus());
		        	            slot.setRelationship(getRelationshipObject(rel));
		        	            slot.setDestConcept(destconceptObj);
		        	            slot.setDestStatus(destconceptObj.getStatus());
		        	            result.put(cls.getURI(), slot);
							}
						}
					}
	        	}	
	        }	
	    }
	    return result;
	}
	
	public HashMap<String, Consistency> case10(OWLModel owlModel) throws ParseException
	{
		HashMap<String, Consistency> result = new HashMap<String, Consistency>();
		for (Iterator<OWLNamedClass> it = owlModel.getOWLNamedClass(ModelConstants.CDOMAINCONCEPT).getSubclasses(true).iterator(); it.hasNext();) 
		{
			OWLNamedClass cls = it.next();
			for (Iterator<OWLIndividual> jt = cls.getInstances(false).iterator(); jt.hasNext();) 
			{
				OWLIndividual individual = (OWLIndividual)jt.next();
				String status = ""+individual.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASSTATUS));
	        	if(!status.equals(OWLStatusConstants.DEPRECATED))
				{	
	        		Collection<OWLIndividual> c = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION));
	        		int cnt = 0;
	        		for (Iterator<OWLIndividual> t = c.iterator(); t.hasNext();) {
						OWLIndividual term = (OWLIndividual)t.next();
						String tstatus = ""+term.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASSTATUS));
			        	if(tstatus.equals(OWLStatusConstants.DEPRECATED) || tstatus.equals(OWLStatusConstants.PROPOSED_DEPRECATED)) cnt++;
		        	}
					
					if(c.size() > 0 && cnt == c.size())
					{
						ConceptObject conceptObj = getConceptObject(cls);
        				Consistency slot = new Consistency();
        				slot.setConcept(conceptObj);
        	            slot.setStatus(conceptObj.getStatus());
        	            slot.setDateCreate(conceptObj.getDateCreate());
        	            slot.setDateModified(conceptObj.getDateModified());
        	            result.put(cls.getURI(), slot);
					}
	        	}	
	        }	
	    }
		return result;
	}
	
	public HashMap<String, Consistency> case11(OWLModel owlModel) throws ParseException
	{
		HashMap<String, Consistency> result = new HashMap<String, Consistency>();
		for (Iterator<OWLNamedClass> it = owlModel.getOWLNamedClass(ModelConstants.CDOMAINCONCEPT).getSubclasses(true).iterator(); it.hasNext();) 
		{
			OWLNamedClass cls = it.next();
			for (Iterator<OWLIndividual> jt = cls.getInstances(false).iterator(); jt.hasNext();) 
			{
				OWLIndividual individual = (OWLIndividual)jt.next();
				{	
					for (Iterator<OWLIndividual> t = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION)).iterator(); t.hasNext();) {
						OWLIndividual term = (OWLIndividual)t.next();
						int cnt = 0;
						for (Iterator<DefaultOWLObjectProperty> itr = owlModel.getOWLProperty(ModelConstants.RHASRELATEDTERM).getSubproperties(false).iterator(); itr.hasNext();) {
			        		if(term.getPropertyValueCount(owlModel.getOWLProperty(itr.next().getName()))>0)	cnt++;	
			        	}
						if(cnt<1)
						{
	        				Consistency slot = new Consistency();
	        				ConceptObject conceptObj = getConceptObject(cls);
	        				TermObject termObj = getTermObject(term, false);
	        				slot.setConcept(conceptObj);
	        				slot.setTerm(termObj);
	        	            slot.setStatus(termObj.getStatus());
	        	            slot.setDateCreate(termObj.getDateCreate());
	        	            slot.setDateModified(termObj.getDateModified());
	        	            result.put(cls.getURI(), slot);
						}
		        	}
					
					
	        	}	
	        }	
	    }return result;
	}
	
	public HashMap<String, Consistency> case12(OWLModel owlModel) throws ParseException
	{
		HashMap<String, Consistency> result = new HashMap<String, Consistency>();
		HashMap<ArrayList<String>, ArrayList<OWLIndividual>> temp = new HashMap<ArrayList<String>, ArrayList<OWLIndividual>>();
		for (Iterator itn = owlModel.getOWLNamedClass(ModelConstants.CDOMAINCONCEPT).getSubclasses(true).iterator(); itn.hasNext();) {
	    	OWLNamedClass cls = (OWLNamedClass) itn.next();
			for (Iterator<OWLIndividual> jt = cls.getInstances(false).iterator(); jt.hasNext();) 
			{
	        	OWLIndividual individual = (OWLIndividual) jt.next();
	        	for (Iterator<OWLIndividual> t = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION)).iterator(); t.hasNext();) 
	        	{
		            OWLIndividual term = (OWLIndividual)t.next();
		        	for (Iterator itc = owlModel.getOWLProperty(ModelConstants.RHASCODE).getSubproperties(false).iterator(); itc.hasNext();) 
		        	{
		    	    	OWLDatatypeProperty termCodeRelation = (OWLDatatypeProperty) itc.next();
		    	    	if(term.getPropertyValue(termCodeRelation)!=null)
						{
		    	    		AttributesObject tcObj = new AttributesObject();
		    	    		NonFuncObject nfObj = new NonFuncObject();
		    	    		nfObj.setValue(""+term.getPropertyValue(termCodeRelation));
							tcObj.setValue(nfObj);
							tcObj.setRelationshipObject(ProtegeWorkbenchUtility.makeDatatypeRelationshipObject(termCodeRelation));
		    	    		ArrayList<String> key = new ArrayList<String>();
		    	    		key.add(nfObj.getValue());
		    	    		key.add(tcObj.getRelationshipObject().getUri());
		    	    		
		    	    		if(temp.containsKey(key))
							{
								temp.get(key).add(term);
							}
							else
							{
								ArrayList<OWLIndividual> tlist = new ArrayList<OWLIndividual>();
								tlist.add(term);
								temp.put(key, tlist);
							}
						}
		        	}
	        	}
			}
		}
		for (Iterator<ArrayList<String>> it = temp.keySet().iterator(); it.hasNext();) 
		{
			ArrayList<String> tcObj = it.next();
			ArrayList<OWLIndividual> tclist = temp.get(tcObj);
			
			if(tclist.size()>1)
			{
				Consistency slot = new Consistency();
		        ConceptObject co = new ConceptObject();
				HashMap<String, TermObject> to = new HashMap<String, TermObject>();
				for (Iterator<OWLIndividual> t = tclist.iterator(); t.hasNext();) 
				{
			        OWLIndividual term = (OWLIndividual)t.next();
			        TermObject tObj = getTermObject(term, true);
					to.put(tObj.getUri(), tObj);
					
	            }
				co.setTerm(to);
				slot.setConcept(co);
				slot.setTermCode(tcObj.get(0));
				slot.setTermCodeProperty(tcObj.get(1));
	            result.put(tcObj.get(0)+tcObj.get(1)+tcObj.get(2), slot);
			}
		}
		return result;
	}
	
	public HashMap<String, Consistency> case13(OWLModel owlModel) throws ParseException
	{
		HashMap<String, Consistency> result = new HashMap<String, Consistency>();
	    for (Iterator it = owlModel.getOWLNamedClass(ModelConstants.CDOMAINCONCEPT).getSubclasses(true).iterator(); it.hasNext();) {
	    	OWLNamedClass cls = (OWLNamedClass) it.next();
			for (Iterator<OWLIndividual> jt = cls.getInstances(false).iterator(); jt.hasNext();) {
	        	
	        	OWLIndividual individual = (OWLIndividual) jt.next();
	        	String status = ""+individual.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASSTATUS));
	        	if(status.equals(OWLStatusConstants.PROPOSED_DEPRECATED) || status.equals(OWLStatusConstants.DEPRECATED))
	            {	
		        	Date cDate = getDate(""+individual.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED)));
					Date uDate = getDate(""+individual.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE)));
					
					Consistency slot = new Consistency();
					ConceptObject cObj = new ConceptObject();
					cObj.setUri(cls.getURI());
					cObj.setStatus(status);
					cObj.setDateCreate(cDate);
					cObj.setDateModified(uDate);
		    
		            for (Iterator<OWLIndividual> t = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION)).iterator(); t.hasNext();) {
			            OWLIndividual term = (OWLIndividual)t.next();
			        	if((""+term.getPropertyValue(term.getOWLModel().getOWLProperty(ModelConstants.RISMAINLABEL))).toLowerCase().equals("true"))
			            	cObj.addTerm(term.getURI(), getTermObject(term, false));
		            }
		            
		            slot.setConcept(cObj);
		            slot.setStatus(status);
		            slot.setDateCreate(cDate);
		            slot.setDateModified(uDate);
		            result.put(cls.getURI(), slot);
	            }
	        }
	    }
	    return result;
	}
	
	public HashMap<String, Consistency> case14(OWLModel owlModel) 
	{
		HashMap<String, Consistency> result = new HashMap<String, Consistency>();
		ArrayList relConceptList = new ArrayList();
		for (Iterator<DefaultOWLObjectProperty> it = owlModel.getOWLProperty(ModelConstants.RHASRELATEDCONCEPT).getSubproperties(false).iterator(); it.hasNext();) 
		{
			DefaultOWLObjectProperty rel = it.next();
			relConceptList.add(rel);

		}
		ArrayList relTermList = new ArrayList();
		for (Iterator<DefaultOWLObjectProperty> it = owlModel.getOWLProperty(ModelConstants.RHASRELATEDTERM).getSubproperties(false).iterator(); it.hasNext();) 
		{
			DefaultOWLObjectProperty rel = it.next();
			relTermList.add(rel);

		}

		for (Iterator it = owlModel.getOWLNamedClass(ModelConstants.CDOMAINCONCEPT).getSubclasses(true).iterator(); it.hasNext();) {
	    	OWLNamedClass cls = (OWLNamedClass) it.next();
			for (Iterator<OWLIndividual> jt = cls.getInstances(false).iterator(); jt.hasNext();) {
	        	OWLIndividual individual = (OWLIndividual) jt.next();
	        	ArrayList cList = (ArrayList) relConceptList.clone();
	        	for (Iterator<DefaultOWLObjectProperty> itr = cList.iterator(); itr.hasNext();) {
	        		DefaultOWLObjectProperty rel = itr.next();
	        		if(individual.getPropertyValueCount(owlModel.getOWLProperty(rel.getName()))>0)
	        				relConceptList.remove(rel);
	        	}
	        	
	        	for (Iterator<OWLIndividual> t = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION)).iterator(); t.hasNext();) {
		            OWLIndividual term = (OWLIndividual)t.next();
		            ArrayList rList = (ArrayList) relTermList.clone();
		            for (Iterator<DefaultOWLObjectProperty> itr = rList.iterator(); itr.hasNext();) {
		            	DefaultOWLObjectProperty rel = itr.next();
		        		if(term.getPropertyValueCount(owlModel.getOWLProperty(rel.getName()))>0)
		        		{
		        				relTermList.remove(rel);
		        		}
		        	}
	            }
	        	
			}
		}
		
		for (Iterator<DefaultOWLObjectProperty> itr = relConceptList.iterator(); itr.hasNext();) {
    		RelationshipObject rObj = getRelationshipObject(itr.next());
			Consistency slot = new Consistency();
			slot.setRelationship(rObj);
			result.put(rObj.getUri(), slot);
    		
    	}
		for (Iterator<DefaultOWLObjectProperty> itr = relTermList.iterator(); itr.hasNext();) {
    		DefaultOWLObjectProperty rel = itr.next();
    		RelationshipObject rObj = new RelationshipObject();
	    	rObj.setUri(rel.getURI());
	    	for (Iterator iterator = rel.getLabels().iterator(); iterator.hasNext();) {
	    		Object obj = (Object) iterator.next();
	    		if (obj instanceof DefaultRDFSLiteral) {
					DefaultRDFSLiteral label = (DefaultRDFSLiteral) obj;
					rObj.addLabel(label.getString(), label.getLanguage());
				}
			}
			Consistency slot = new Consistency();
			slot.setRelationship(rObj);
			result.put(rObj.getUri(), slot);
    	}
		return result;
	}
	
	public HashMap<String, Consistency> case15(OWLModel owlModel) throws ParseException
	{
		HashMap<String, Consistency> result = new HashMap<String, Consistency>();
	    for (Iterator it = owlModel.getOWLNamedClass(ModelConstants.CDOMAINCONCEPT).getSubclasses(true).iterator(); it.hasNext();) {
	    	OWLNamedClass cls = (OWLNamedClass) it.next();
			for (Iterator<OWLIndividual> jt = cls.getInstances(false).iterator(); jt.hasNext();) {
	        	
	        	OWLIndividual individual = (OWLIndividual) jt.next();
	        	String status = ""+individual.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASSTATUS));
	        	if(status.equals("validated") || status.equals("published"))
	            {	
		        	Date cDate = getDate(""+individual.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED)));
					Date uDate = getDate(""+individual.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE)));
					
					if(individual.getPropertyValueCount(owlModel.getOWLProperty(ModelConstants.RHASDEFINITION))<1)
					{
						Consistency slot = new Consistency();
						ConceptObject cObj = new ConceptObject();
						cObj.setUri(cls.getURI());
						cObj.setStatus(status);
						cObj.setDateCreate(cDate);
						cObj.setDateModified(uDate);
			    
			            for (Iterator<OWLIndividual> t = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION)).iterator(); t.hasNext();) {
				            OWLIndividual term = (OWLIndividual)t.next();
				        	if((""+term.getPropertyValue(term.getOWLModel().getOWLProperty(ModelConstants.RISMAINLABEL))).toLowerCase().equals("true"))
				            	cObj.addTerm(term.getURI(), getTermObject(term, false));
		
			            }
			            
			            slot.setConcept(cObj);
			            slot.setStatus(status);
			            slot.setDateCreate(cDate);
			            slot.setDateModified(uDate);
			            result.put(cls.getURI(), slot);
					}
	            }
	        }
	    }
	    return result;
	}
	
	public HashMap<String, Consistency> case16(OWLModel owlModel) throws ParseException
	{
		HashMap<String, Consistency> result = new HashMap<String, Consistency>();
		Hashtable<String, ArrayList<TermObject>> ht = new Hashtable<String, ArrayList<TermObject>>();
	    for (Iterator it = owlModel.getOWLNamedClass(ModelConstants.CDOMAINCONCEPT).getSubclasses(true).iterator(); it.hasNext();) {
	    	OWLNamedClass cls = (OWLNamedClass) it.next();
			for (Iterator<OWLIndividual> jt = cls.getInstances(false).iterator(); jt.hasNext();) {
	        	
	        	OWLIndividual individual = (OWLIndividual) jt.next();
	        	String status = ""+individual.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASSTATUS));
	            Date cDate = getDate(""+individual.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED)));
	            Date uDate = getDate(""+individual.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE)));
				
				Consistency slot = new Consistency();
	        	
				ConceptObject cObj = new ConceptObject();
				cObj.setUri(cls.getURI());
				cObj.setStatus(status);
				cObj.setDateCreate(cDate);
				cObj.setDateModified(uDate);

	        	for (Iterator<OWLIndividual> t = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION)).iterator(); t.hasNext();) 
	        	{
		            OWLIndividual term = (OWLIndividual)t.next();
		            if((""+term.getPropertyValue(term.getOWLModel().getOWLProperty(ModelConstants.RISMAINLABEL))).toLowerCase().equals("true"))
		            	cObj.addTerm(term.getURI(), getTermObject(term, false));
	        	}
	        	
	        	ConceptObject dObj = new ConceptObject();
				dObj.setUri(cls.getURI());
				dObj.setStatus(status);
				dObj.setDateCreate(cDate);
				dObj.setDateModified(uDate);
				boolean chk = false;
				for (Iterator<OWLIndividual> t = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION)).iterator(); t.hasNext();) {
		            OWLIndividual term = (OWLIndividual)t.next();
		            TermObject tObj = getTermObject(term, false);
		            dObj.addTerm(term.getURI(), tObj );
		            String label = tObj.getLabel();
					if(label!=null)
					{
						if(ht.containsKey(label))
						{
							ht.get(label).add(tObj);
						}
						else
						{
							ArrayList<TermObject> termObj = new ArrayList<TermObject>();
							termObj.add(tObj);
							ht.put(label, termObj);
						}
					}
	            }
	            slot.setConcept(cObj);
	            slot.setDestConcept(dObj);
	            slot.setStatus(status);
	            slot.setDateCreate(cDate);
	            slot.setDateModified(uDate);
	            result.put(cls.getURI(), slot);
	        }
	    }

		HashMap<String, HashMap<String, TermObject>> conceptMap = new HashMap<String, HashMap<String, TermObject>>();
		for (Iterator<String> it = ht.keySet().iterator(); it.hasNext();)
		{
			ArrayList<TermObject> termList = (ArrayList<TermObject>)ht.get((String)it.next());
			if(termList.size()>1)
			{
				for (Iterator<TermObject> i = termList.iterator(); i.hasNext();)
				{
					TermObject to = (TermObject) i.next();
					String key = to.getConceptUri();
					if(conceptMap.containsKey(key))
						conceptMap.get(key).put(to.getUri(), to);
					else
					{
						HashMap<String, TermObject> ctermList = new HashMap<String, TermObject>();
						ctermList.put(to.getUri(), to);
						conceptMap.put(key, ctermList);
					}
				}
			}
		}

		HashMap<String, Consistency> dummy = (HashMap<String, Consistency>) result.clone();
		for (Iterator<String> i = dummy.keySet().iterator(); i.hasNext();)
		{
			String key = (String) i.next();
			if(conceptMap.containsKey(key))
			{
				result.get(key).getDestConcept().setTerm(conceptMap.get(key));
			}
			else
				result.remove(key);
		}

		return result;
	}
	
	public HashMap<String, Consistency> case17(OWLModel owlModel) throws ParseException
	{
		HashMap<String, Consistency> result = new HashMap<String, Consistency>();
		for (Iterator itn = owlModel.getOWLNamedClass(ModelConstants.CDOMAINCONCEPT).getSubclasses(true).iterator(); itn.hasNext();) {
	    	OWLNamedClass cls = (OWLNamedClass) itn.next();
			for (Iterator<OWLIndividual> jt = cls.getInstances(false).iterator(); jt.hasNext();) 
			{
	        	OWLIndividual individual = (OWLIndividual) jt.next();
	        	String status = ""+individual.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASSTATUS));
	            Date cDate = getDate(""+individual.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED)));
				Date uDate = getDate(""+individual.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE)));

				ConceptObject cObj = new ConceptObject();
				cObj.setUri(cls.getURI());
				cObj.setStatus(status);
				cObj.setDateCreate(cDate);
				cObj.setDateModified(uDate);

	        	for (Iterator<OWLIndividual> t = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION)).iterator(); t.hasNext();) 
	        	{
		            OWLIndividual term = (OWLIndividual)t.next();
		            if((""+term.getPropertyValue(term.getOWLModel().getOWLProperty(ModelConstants.RISMAINLABEL))).toLowerCase().equals("true"))
		            	cObj.addTerm(term.getURI(), getTermObject(term, false));
	        	}
	        	
	        	ConceptObject dObj = new ConceptObject();
				dObj.setUri(cls.getURI());
				dObj.setStatus(status);
				dObj.setDateCreate(cDate);
				dObj.setDateModified(uDate);
				boolean chk = false;
				for (Iterator<OWLIndividual> t = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION)).iterator(); t.hasNext();) {
		            OWLIndividual term = (OWLIndividual)t.next();
		            Object code = term.getPropertyValue(term.getOWLModel().getOWLProperty(ModelConstants.RHASCODEAGROVOC));
		            if(code==null)
		            {
		            	dObj.addTerm(term.getURI(), getTermObject(term, false));
		            	chk = true;
		            }
	            }
				if(chk)	
				{
					Consistency slot = new Consistency();
					slot.setConcept(cObj);
		            slot.setDestConcept(dObj);
		            slot.setStatus(status);
		            slot.setDateCreate(cDate);
		            slot.setDateModified(uDate);
		            result.put(cls.getURI(), slot);	
				}
			}
		}
		return result;
	}

	
	public HashMap<String, Consistency> case18(OWLModel owlModel) throws ParseException
	{
		HashMap<String, ArrayList<OWLIndividual>> temp = new HashMap<String, ArrayList<OWLIndividual>>();
		for (Iterator itn = owlModel.getOWLNamedClass(ModelConstants.CDOMAINCONCEPT).getSubclasses(true).iterator(); itn.hasNext();) {
	    	OWLNamedClass cls = (OWLNamedClass) itn.next();
			for (Iterator<OWLIndividual> jt = cls.getInstances(false).iterator(); jt.hasNext();) 
			{
	        	OWLIndividual individual = (OWLIndividual) jt.next();
	        	for (Iterator<OWLIndividual> t = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION)).iterator(); t.hasNext();) 
	        	{
		            OWLIndividual term = (OWLIndividual)t.next();
		        	for (Iterator itc = owlModel.getOWLProperty(ModelConstants.RHASCODE).getSubproperties(false).iterator(); itc.hasNext();) 
		        	{
		    	    	OWLDatatypeProperty termCodeRelation = (OWLDatatypeProperty) itc.next();
						if(term.getPropertyValue(termCodeRelation)!=null)
						{
							String key = termCodeRelation.getURI()+term.getPropertyValue(termCodeRelation);
							ArrayList<OWLIndividual> tlist = new ArrayList<OWLIndividual>();
							if(temp.containsKey(key))
							{
								temp.get(key).add(term);
							}
							else
							{
								tlist.add(term);
								temp.put(key, tlist);
							}
						}
		        	}
	        	}
			}
		}

		HashMap<OWLIndividual, ArrayList<OWLIndividual>> conceptMap = new HashMap<OWLIndividual, ArrayList<OWLIndividual>>();
		ArrayList<OWLIndividual> tlist;
		for (Iterator<String> it = temp.keySet().iterator(); it.hasNext();) 
		{
			ArrayList<OWLIndividual> tclist = temp.get((String)it.next());
			if(tclist.size()>1)
			{
				
				for (Iterator<OWLIndividual> t = tclist.iterator(); t.hasNext();) 
				{
			        OWLIndividual term = (OWLIndividual)t.next();
			        OWLIndividual individual = (OWLIndividual) term.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RISLEXICALIZATIONOF));
			        
			        if(conceptMap.containsKey(individual))
					{
			        	tlist = conceptMap.get(individual);
			        	tlist.add(term);
					}
					else
					{
						tlist = new ArrayList<OWLIndividual>();
						tlist.add(term);
					}
			        conceptMap.put(individual, tlist);
	            }
			}
		}
		
		HashMap<String, Consistency> result = new HashMap<String, Consistency>();
		for (Iterator<OWLIndividual> iit = conceptMap.keySet().iterator(); iit.hasNext();) 
		{
			OWLIndividual individual = (OWLIndividual)iit.next();
			OWLNamedClass cls = (OWLNamedClass) individual.getProtegeType();

			String status = ""+individual.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASSTATUS));
            Date cDate = getDate(""+individual.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED)));
			Date uDate = getDate(""+individual.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE)));

			
			ConceptObject cObj = new ConceptObject();
			cObj.setUri(cls.getURI());
			cObj.setStatus(status);
			cObj.setDateCreate(cDate);
			cObj.setDateModified(uDate);
			
			for (Iterator<OWLIndividual> t = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION)).iterator(); t.hasNext();) {
	            OWLIndividual term = (OWLIndividual)t.next();
	            if((""+term.getPropertyValue(term.getOWLModel().getOWLProperty(ModelConstants.RISMAINLABEL))).toLowerCase().equals("true"))
	            	cObj.addTerm(term.getURI(), getTermObject(term, true));
            }
			
			ArrayList<OWLIndividual> tList = conceptMap.get(individual);
			ConceptObject dObj = new ConceptObject();
			dObj.setUri(cls.getURI());
			dObj.setStatus(status);
			dObj.setDateCreate(cDate);
			dObj.setDateModified(uDate);
			
			for (Iterator<OWLIndividual> lit = tList.iterator(); lit.hasNext();) 
			{
				OWLIndividual term = (OWLIndividual)lit.next();
				dObj.addTerm(term.getURI(), getTermObject(term, true));
			}

			Consistency slot = new Consistency();
			slot.setConcept(cObj);
            slot.setDestConcept(dObj);
            slot.setStatus(status);
            slot.setDateCreate(cDate);
            slot.setDateModified(uDate);
            result.put(cls.getURI(), slot);			
		}
		return result;
	}
	
	public HashMap<String, Consistency> case19(OWLModel owlModel) throws ParseException
	{
		HashMap<String, Consistency> result = new HashMap<String, Consistency>();
	    for (Iterator it = owlModel.getOWLNamedClass(ModelConstants.CDOMAINCONCEPT).getSubclasses(true).iterator(); it.hasNext();) {
	    	OWLNamedClass cls = (OWLNamedClass) it.next();
			for (Iterator<OWLIndividual> jt = cls.getInstances(false).iterator(); jt.hasNext();) {
	        	
	        	OWLIndividual individual = (OWLIndividual) jt.next();
	        	String status = ""+individual.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASSTATUS));
	            Date cDate = getDate(""+individual.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED)));
				Date uDate = getDate(""+individual.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE)));
				
				Consistency slot = new Consistency();
	        	
				ConceptObject cObj = new ConceptObject();
				cObj.setUri(cls.getURI());
				cObj.setStatus(status);
				cObj.setDateCreate(cDate);
				cObj.setDateModified(uDate);
	        	
	            for (Iterator<OWLIndividual> t = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION)).iterator(); t.hasNext();) {
		            OWLIndividual term = (OWLIndividual)t.next();
		        	if((""+term.getPropertyValue(term.getOWLModel().getOWLProperty(ModelConstants.RISMAINLABEL))).toLowerCase().equals("true"))
		            	cObj.addTerm(term.getURI(), getTermObject(term, false));
		        	for (Iterator<Object> lit = term.getLabels().iterator(); lit.hasNext();) {
		            	Object obj = (Object) lit.next();
		            	if (obj instanceof DefaultRDFSLiteral) {
		    	        	DefaultRDFSLiteral rdfLiteral = (DefaultRDFSLiteral) obj;
		    	        	slot.addLanguages(rdfLiteral.getLanguage());
		    	        }
		        	}
	            }

	            slot.setConcept(cObj);
	            slot.setStatus(status);
	            slot.setDateCreate(cDate);
	            slot.setDateModified(uDate);
	            result.put(cls.getURI(), slot);
	        }
	    }
	    return result;
	}
	
	
	
	
}
