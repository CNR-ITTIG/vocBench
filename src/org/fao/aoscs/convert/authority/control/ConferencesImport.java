package org.fao.aoscs.convert.authority.control;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.convert.util.ProtegeUtil;

import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;

public class ConferencesImport {
	
	private static final String ADDRESS_FILE="/Sachit/workspace/aos_cs_1_v_1_0_2/csv/20100414-Conferences.csv";
	private static final String ADDRESS_FILE1="/Sachit/workspace/aos_cs_1_v_1_0_2/csv/20100414-ConferenceSeries.csv";
	private static String status = "Published";
	private static String conferenceNamespace = ModelConstants.BASENAMESPACE+"conference"+ModelConstants.NAMESPACESEPARATOR;
	private static String conferenceNamespacePrefix = "conference";
	private static String countryNamespace = ModelConstants.BASENAMESPACE+"country"+ModelConstants.NAMESPACESEPARATOR;
	private static String countryNamespacePrefix = "country";
	private static String geopoliticalNamespace = "http://aims.fao.org/aos/geopolitical.owl#";
	private static String geopoliticalNamespacePrefix = "geo";
	
	private final static String driver = "com.mysql.jdbc.Driver";
	private final static String dbIp = "localhost";
	private final static String dbPort = "3306";
	private final static String dbName = "agrovocwb_ac_conferences_v_1_0";
  	private final static String dbUser = "fao";
  	private final static String dbPassword = "faomimos";
  	private final static String owlURI = "file:/Sachit/workspace/aos_cs_1_v_1_0_2/owl/v1.0/aos.owl";
  	private final static String modelConstantsURI = "org.fao.aoscs.server.ModelConstants";

	public static void main(String args[]) 
	{
		init();
	}
	
	public static void init(){
		try {

			OWLModel owlModel = ProtegeUtil.getOWLModel(driver, dbIp, dbPort, dbName, dbUser, dbPassword, owlURI, modelConstantsURI);

			conferenceNamespace = ModelConstants.BASENAMESPACE+"conference"+ModelConstants.NAMESPACESEPARATOR;
			countryNamespace = ModelConstants.BASENAMESPACE+"country"+ModelConstants.NAMESPACESEPARATOR;
			
			owlModel.getNamespaceManager().setPrefix(conferenceNamespace, conferenceNamespacePrefix);
			owlModel.getNamespaceManager().setPrefix(countryNamespace, countryNamespacePrefix);
			owlModel.getNamespaceManager().setPrefix(geopoliticalNamespace, geopoliticalNamespacePrefix);

			createConferenceConcept(owlModel);
			createConferenceSeriesConcept(owlModel);
			createCountryConcept(owlModel);

			convertConferencesSeries(owlModel, loadConferenceSeriesData(ADDRESS_FILE1));
			convertConferences(owlModel, loadConferenceData(ADDRESS_FILE));
			
			owlModel.dispose();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void convertConferences(OWLModel owlModel, List<Conferences> list)
	{
		int cnt=0;
		for(Conferences conf:list)
		{
			cnt++;
			if(cnt==1) continue;
			//if(cnt>10) break;
			System.out.println("-------------------------------------------------");
			conf.setId(conf.getId().replaceFirst("http://www.fao.org/aims/aos/conference/c_", ""));
			conf.setIsPartOfConferenceSeries(conf.getIsPartOfConferenceSeries().replaceFirst("http://www.fao.org/aims/aos/conference/c_", ""));
			System.out.println(cnt+" - Conf ID: "+conf.getId()+" ParentID: "+conf.getIsPartOfConferenceSeries());		
			
			if(!isEmptyConferences(conf))
			{				
				String status = "Published";
				if(!conf.getObsolete().equals("0"))
					status = "Deprecated";
				
				OWLIndividual ins = ProtegeUtil.createConcept(owlModel, conferenceNamespacePrefix+":c_conference", conferenceNamespacePrefix, conf.getId(), "c_", status); 
				createAllConcept(owlModel, conf, ins, conf.getId(), createLabelSuffix(conf));
				
				if(ins!=null)
				{
					//Create Country concepts
					OWLIndividual countryIns = null;
					if(conf.getIsSpatiallyIncludedInCountryUri()!=null && !conf.getIsSpatiallyIncludedInCountryUri().equals("") && conf.getIsSpatiallyIncludedInCountry()!=null && !conf.getIsSpatiallyIncludedInCountry().equals(""))
					{
						countryIns = createGeopPoliticalConcept(owlModel, conf.getIsSpatiallyIncludedInCountryUri(), conf.getIsSpatiallyIncludedInCountry());
						ProtegeUtil.addConcept2ConceptRelationship(owlModel, ModelConstants.RISSPATIALLYINCLUDEDIN, ins, countryIns);
					}
					
					//Add Number
					ProtegeUtil.addDatatypeProperty(owlModel, ins, ModelConstants.RHASNUMBER, conf.getHasNumber());
		
					//Add Date
					ProtegeUtil.addDatatypeProperty(owlModel, ins, ModelConstants.RHASDATE, conf.getHasDate());
					
					//Add City
					ProtegeUtil.addDatatypeProperty(owlModel, ins, ModelConstants.RISSPATIALLYINCLUDEDINCITY, conf.getIsSpatiallyIncludedInCity());
					
					//Add State
					ProtegeUtil.addDatatypeProperty(owlModel, ins, ModelConstants.RISSPATIALLYINCLUDEDINSTATE, conf.getIsSpatiallyIncludedInState());

					//Add Ispartof Relationship
					createIsPartOfRelationship(owlModel, conf.getId(), conf.getIsPartOfConferenceSeries());
				}
			}
			else
			{
				System.out.println("Empty conferences: "+conf.getId());
			}
		}
	}
	
	public static void convertConferencesSeries(OWLModel owlModel, List<ConferencesSeries> list)
	{
		int cnt=0;
		for(ConferencesSeries confSeries:list)
		{
			cnt++;
			if(cnt==1) continue;
			//if(cnt>10) break;
			System.out.println("-------------------------------------------------");
			confSeries.setUri(confSeries.getUri().replaceFirst("http://www.fao.org/aims/aos/conference/c_", ""));
			System.out.println(cnt+" - Conf Series URI: "+confSeries.getUri());		
			
			if(!isEmptyConferenceSeries(confSeries))
			{				
				String status = "Published";
				if(!confSeries.getObsolete().equals("0"))
					status = "Deprecated";
				
				OWLIndividual ins = ProtegeUtil.createConcept(owlModel, conferenceNamespacePrefix+":c_conferenceseries", conferenceNamespacePrefix, confSeries.getUri(), "c_", status); 
				
				createAllTerm(owlModel, conferenceNamespacePrefix, ins, confSeries.getUri(), confSeries.getLabelAr(), "ar", true, status, null, null, null, "");
				createAllTerm(owlModel, conferenceNamespacePrefix, ins, confSeries.getUri(), confSeries.getLabelEn(), "en", true, status, null, null, confSeries.getAcronymEn(), "");
				createAllTerm(owlModel, conferenceNamespacePrefix, ins, confSeries.getUri(), confSeries.getLabelEnNonPreferredTerms(), "en", false, status, null, null, confSeries.getAcronymEn(), "");
				createAllTerm(owlModel, conferenceNamespacePrefix, ins, confSeries.getUri(), confSeries.getLabelFr(), "fr", true, status, null, null, confSeries.getAcronymFr(), "");
				createAllTerm(owlModel, conferenceNamespacePrefix, ins, confSeries.getUri(), confSeries.getLabelEs(), "es", true, status, null, null, confSeries.getAcronymEs(), "");
				createAllTerm(owlModel, conferenceNamespacePrefix, ins, confSeries.getUri(), confSeries.getLabelZh(), "zh", true, status, null, null, null, "");
				createAllTerm(owlModel, conferenceNamespacePrefix, ins, confSeries.getUri(), confSeries.getLabelRu(), "ru", true, status, null, null, null, "");
				createAllTerm(owlModel, conferenceNamespacePrefix, ins, confSeries.getUri(), confSeries.getLabelIt(), "it", true, status, null, null, null, "");
				createAllTerm(owlModel, conferenceNamespacePrefix, ins, confSeries.getUri(), confSeries.getLabelPt(), "pt", true, status, null, null, null, "");
				createAllTerm(owlModel, conferenceNamespacePrefix, ins, confSeries.getUri(), confSeries.getLabelDe(), "de", true, status, null, null, null, "");
				createAllTerm(owlModel, conferenceNamespacePrefix, ins, confSeries.getUri(), confSeries.getLabelTr(), "tr", true, status, null, null, null, "");
				createAllTerm(owlModel, conferenceNamespacePrefix, ins, confSeries.getUri(), confSeries.getLabelId(), "id", true, status, null, null, null, "");
				createAllTerm(owlModel, conferenceNamespacePrefix, ins, confSeries.getUri(), confSeries.getLabelMk(), "mk", true, status, null, null, null, "");
				createAllTerm(owlModel, conferenceNamespacePrefix, ins, confSeries.getUri(), confSeries.getLabelPl(), "pl", true, status, null, null, null, "");
				
				
			}
			else
			{
				System.out.println("Empty conferences: "+confSeries.getUri());
			}
		}
	}
	
	public static OWLIndividual createAllConcept(OWLModel owlModel, Conferences conf, OWLIndividual ins, String id, String labelSuffix)
	{
		String[] alternativeEn = {conf.getHasSpellingVariantEn1(), conf.getHasSpellingVariantEn2() };
		String[] alternativeFr = {conf.getHasSpellingVariantFr1(), conf.getHasSpellingVariantFr2(), conf.getHasSpellingVariantFr3()};
		String[] alternativeEs = {conf.getHasSpellingVariantEs1(), conf.getHasSpellingVariantEs2(), conf.getHasSpellingVariantEs3(), conf.getHasSpellingVariantEs4()};
		String[] alternativeRu = {conf.getHasSpellingVariantRu()};
		String[] alternativePt = {conf.getHasSpellingVariantPt1(), conf.getHasSpellingVariantPt2()};
		String[] alternativeIt = {conf.getHasSpellingVariantIt()};
		
		createAllTerm(owlModel, conferenceNamespacePrefix, ins, id, conf.getLabelEn(), "en", true, status, null, alternativeEn, conf.getAcronymEn(), labelSuffix);
		createAllTerm(owlModel, conferenceNamespacePrefix, ins, id, conf.getLabelFr(), "fr", true, status, null, alternativeFr, null, labelSuffix);
		createAllTerm(owlModel, conferenceNamespacePrefix, ins, id, conf.getLabelEs(), "es", true, status, null, alternativeEs, null, labelSuffix);
		createAllTerm(owlModel, conferenceNamespacePrefix, ins, id, conf.getLabelAr(), "ar", true, status, null, null, null, labelSuffix);
		createAllTerm(owlModel, conferenceNamespacePrefix, ins, id, conf.getLabelZh(), "zh", true, status, null, null, null, labelSuffix);
		createAllTerm(owlModel, conferenceNamespacePrefix, ins, id, conf.getLabelRu(), "ru", true, status, null, alternativeRu, null, labelSuffix);
		createAllTerm(owlModel, conferenceNamespacePrefix, ins, id, conf.getLabelIt(), "it", true, status, null, alternativeIt, null, labelSuffix);
		createAllTerm(owlModel, conferenceNamespacePrefix, ins, id, conf.getLabelPt(), "pt", true, status, null, alternativePt, null, labelSuffix);
		createAllTerm(owlModel, conferenceNamespacePrefix, ins, id, conf.getLabelDe(), "de", true, status, null, null, null, labelSuffix);
		createAllTerm(owlModel, conferenceNamespacePrefix, ins, id, conf.getLabelTr(), "tr", true, status, null, null, null, labelSuffix);
		createAllTerm(owlModel, conferenceNamespacePrefix, ins, id, conf.getLabelId(), "id", true, status, null, null, null, labelSuffix);
		createAllTerm(owlModel, conferenceNamespacePrefix, ins, id, conf.getLabelMk(), "mk", true, status, null, null, null, labelSuffix);
		createAllTerm(owlModel, conferenceNamespacePrefix, ins, id, conf.getLabelPl(), "pl", true, status, null, null, null, labelSuffix);
		
		return ins;
		
	}
	
	public static void createAllTerm(OWLModel owlModel, String prefix, OWLIndividual ins, String id, String label, String lang, boolean isMainLabel, String status, String code, String[] alternatives, String acronyms, String labelSuffix)
	{
		if(label!=null && !label.equals(""))
		{
			
			OWLIndividual term = ProtegeUtil.getTerm(owlModel, ins, label, lang);
			if(term==null)
			{
				term = createTerm(owlModel, prefix, ins, id, label, lang, isMainLabel, status, code, labelSuffix);
				if(alternatives!=null)
				{
					for(String altTerm:alternatives)
					{
						createAlternativeTerm(owlModel, prefix, ins, term, altTerm, lang, false, status);
					}
				}
				if(acronyms!=null && !acronyms.equals(""))
				{
					createAcronymTerm(owlModel, prefix, ins, term, acronyms, lang, false, status);
				}
			}
		}
	}
	
	public static OWLIndividual createTerm(OWLModel owlModel, String namespacePrefix, OWLIndividual ins, String name, String label, String lang, boolean isMainlabel, String status, String code, String labelSuffix)
	{
		OWLIndividual term = ProtegeUtil.createTerm(owlModel, namespacePrefix, ins, name, label+labelSuffix, lang, isMainlabel, status);
		if(code!=null && !code.equals(""))
			ProtegeUtil.addDatatypeProperty(owlModel, term, ModelConstants.RHASCODEFAOTERM, code);
		return term;
	}

	public static void createAlternativeTerm(OWLModel owlModel, String namespacePrefix, OWLIndividual ins, OWLIndividual preferredTerm, String label, String lang, boolean isMainlabel, String status)
	{
		ProtegeUtil.addSpellingVariation(owlModel, preferredTerm, label, lang, ins);
	}
	
	public static void createAcronymTerm(OWLModel owlModel, String namespacePrefix, OWLIndividual ins, OWLIndividual preferredTerm, String label, String lang, boolean isMainlabel, String status)
	{
		if(label!=null  && !label.equals(""))
		{
			OWLIndividual destTerm = ProtegeUtil.createTerm(owlModel, namespacePrefix, ins, label, label, lang, isMainlabel, status);
			ProtegeUtil.addTermRelationship(owlModel, ModelConstants.RHASACRONYM, preferredTerm, destTerm, ins);
		}
	}
	
	public static void createIsPartOfRelationship(OWLModel owlModel, String insName, String destInsName)
	{
		if(destInsName!=null && !destInsName.equals(""))
		{
			
			OWLIndividual ins = ProtegeUtil.getConceptInstance(owlModel, owlModel.getOWLNamedClass(conferenceNamespacePrefix+":"+"c_"+insName));
			if(ins!=null)
			{
				OWLIndividual destIns = ProtegeUtil.getConceptInstance(owlModel, owlModel.getOWLNamedClass(conferenceNamespacePrefix+":"+"c_"+destInsName));
				if(destIns!=null)
				{
					ProtegeUtil.addConcept2ConceptRelationship(owlModel, ModelConstants.RISPARTOF, ins, destIns);
				}
				else
				{
					System.out.println("Missing ispart destincation"+destIns);
				}
			}
			else
			{
				System.out.println("Missing ispart source"+insName);
			}
		}
	}
	
	public static void createConferenceConcept(OWLModel owlModel){
		String name = "conference";
		String label = "conference";	
		String lang = "en";
		
		OWLIndividual ins = ProtegeUtil.createConcept(owlModel, ModelConstants.CDOMAINCONCEPT, conferenceNamespacePrefix, name, "c_", status);
		ProtegeUtil.createTerm(owlModel, conferenceNamespacePrefix, ins, name, label, lang, true, status);
	}
	
	public static void createConferenceSeriesConcept(OWLModel owlModel){
		String name = "conferenceseries";
		String label = "conference series";	
		String lang = "en";
		
		OWLIndividual ins = ProtegeUtil.createConcept(owlModel, ModelConstants.CDOMAINCONCEPT, conferenceNamespacePrefix, name, "c_", status);
		ProtegeUtil.createTerm(owlModel, conferenceNamespacePrefix, ins, name, label, lang, true, status);
	}
	
	public static void createCountryConcept(OWLModel owlModel){
		String name = "country";
		String label = "country";	
		String lang = "en";
		
		OWLIndividual ins = ProtegeUtil.createConcept(owlModel, ModelConstants.CDOMAINCONCEPT, countryNamespacePrefix, name, "c_", status);
		ProtegeUtil.createTerm(owlModel, countryNamespacePrefix, ins, name, label, lang, true, status);
	}
	
	public static OWLIndividual createGeopPoliticalConcept(OWLModel owlModel, String uri, String label){
		String name = uri.replaceAll("http://www.fao.org/aims/geopolitical.owl#", "");
		String lang = "en";
		OWLNamedClass cls = owlModel.getOWLNamedClass(geopoliticalNamespacePrefix+":"+name);
		OWLIndividual ins = null;
		if(cls==null)
		{
			ins = ProtegeUtil.createConcept(owlModel, countryNamespacePrefix+":c_country", geopoliticalNamespacePrefix, name, "", status);
			
		}
		else
		{
			ins = ProtegeUtil.getConceptInstance(owlModel, cls);
		}
		
		OWLIndividual termIns = ProtegeUtil.getTerm(owlModel, ins, label, lang);
		if(termIns==null)	ProtegeUtil.createTerm(owlModel, geopoliticalNamespacePrefix, ins, name, label, lang, true, status);
		
		return ins;
	}
	
	public static String createLabelSuffix(Conferences conf)
	{
		String labelSuffix = "";
		String labelNumber = "";
		String labelDate = "";
		String labelCity = "";
		String labelState = "";
		String labelCountry = "";
			
		if(conf.getHasNumber()!=null && !conf.getHasNumber().equals(""))
			labelNumber = conf.getHasNumber().trim();
		if(conf.getHasDate()!=null && !conf.getHasDate().equals(""))
			labelDate = conf.getHasDate().trim();
		if(conf.getIsSpatiallyIncludedInCity()!=null && !conf.getIsSpatiallyIncludedInCity().equals(""))
			labelCity = conf.getIsSpatiallyIncludedInCity().trim();
		if(conf.getIsSpatiallyIncludedInState()!=null && !conf.getIsSpatiallyIncludedInState().equals(""))
			labelState = conf.getIsSpatiallyIncludedInState().trim();
		if(conf.getIsSpatiallyIncludedInCountry()!=null && !conf.getIsSpatiallyIncludedInCountry().equals(""))
			labelCountry = conf.getIsSpatiallyIncludedInCountry().trim();
		
		
		if(labelNumber.length()>1)
			labelSuffix += labelNumber;
		if(labelSuffix.length()>1 && (labelDate+labelCity+labelState+labelCountry).length()>1)
			labelSuffix += " : ";
		if(labelDate.length()>1)
			labelSuffix += labelDate;
		if(labelSuffix.length()>1 && labelDate.length()>1 && (labelCity+labelState+labelCountry).length()>1)
			labelSuffix += " : ";
		if(labelCity.length()>1)
			labelSuffix += labelCity;
		if(labelSuffix.length()>1 && labelCity.length()>1 && (labelState+labelCountry).length()>1)
			labelSuffix += ",";
		if(labelSuffix.length()>1 && labelState.length()>1)
			labelSuffix += " ";
		if(labelState.length()>1)
			labelSuffix += "("+labelState+")";
		if(labelSuffix.length()>1 && labelCountry.length()>1)
			labelSuffix += " ";
		if(labelCountry.length()>1)
			labelSuffix += labelCountry;
		if(labelSuffix.length()>1)
			labelSuffix = " ("+labelSuffix+")";
		
		return labelSuffix;
	}
	
	public static boolean isEmptyConferences(Conferences conf)
	{
		return ( conf.getLabelEn().equals("") &&
				conf.getLabelFr().equals("") && 
				conf.getLabelEs().equals("") && 
				conf.getLabelAr().equals("") &&  
				conf.getLabelZh().equals("") &&
				conf.getLabelRu().equals("") && 
				conf.getLabelIt().equals("") && 
				conf.getLabelPt().equals("") && 
				conf.getLabelDe().equals("") && 
				conf.getLabelTr().equals("") && 
				conf.getLabelId().equals("") && 
				conf.getLabelMk().equals("") &&
				conf.getLabelPl().equals("") 
				
		);
	}
	
	public static boolean isEmptyConferenceSeries(ConferencesSeries confSeries)
	{
		return ( confSeries.getLabelEn().equals("") &&
				confSeries.getLabelEnNonPreferredTerms().equals("") && 
				confSeries.getLabelFr().equals("") && 
				confSeries.getLabelEs().equals("") && 
				confSeries.getLabelAr().equals("") &&  
				confSeries.getLabelZh().equals("") &&
				confSeries.getLabelRu().equals("") && 
				confSeries.getLabelIt().equals("") && 
				confSeries.getLabelPt().equals("") && 
				confSeries.getLabelDe().equals("") && 
				confSeries.getLabelTr().equals("") && 
				confSeries.getLabelId().equals("") && 
				confSeries.getLabelMk().equals("") &&
				confSeries.getLabelPl().equals("") 
				
		);
	}
	
	
	@SuppressWarnings("unchecked")
	public static List<Conferences> loadConferenceData(String filename) throws FileNotFoundException
	{
		ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
		strat.setType(Conferences.class);
		//String[] columns = new String[] {"id","faoterm","labelAr","labelEn","labelEs","labelIt","labelFr","labelZh","labelRu","labelPt","labelDe","labelTr","labelId","alternativeEn1","alternativeEn2","alternativeFr1","alternativeFr2","alternativeFr3","alternativeEs1","alternativeEs2","alternativeEs3","alternativeEs4","alternativeRu","number","city","state","alternativeCity1","alternativeCity2","alternativeCity3","country","countryUri","date","alternativeDate1","alternativeDate2","alternativeDate3","alternativeDate4"}; // the fields to bind do in your JavaBean
		//String[] columns = new String[] {"id","faoterm","labelAr","isComponentOfURI1","labelEn","labelEs","labelIt","labelFr","labelZh","labelRu","labelPt","labelDe","labelTr","labelId","alternativeEn1","alternativeEn2","alternativeFr1","alternativeFr2","alternativeFr3","alternativeEs1","alternativeEs2","alternativeEs3","alternativeEs4","alternativeRu","number","date","city","state","country","alternativeCity1","alternativeCity2","alternativeCity3","countryUri","alternativeDate5","alternativeDate1","alternativeDate2","alternativeDate3","alternativeDate4"}; // the fields to bind do in your JavaBean
		String[] columns = new String[] {"id","isPartOfConferenceSeries","labelEn","labelFr","labelEs","labelAr","labelZh","labelRu","labelIt","labelPt","labelDe","labelTr","labelId","labelMk","labelPl","hasSpellingVariantEn1","hasSpellingVariantEn2","hasSpellingVariantFr1","hasSpellingVariantFr2","hasSpellingVariantFr3","hasSpellingVariantEs1","hasSpellingVariantEs2","hasSpellingVariantEs3","hasSpellingVariantEs4","hasSpellingVariantRu","hasSpellingVariantPt1","hasSpellingVariantPt2","hasSpellingVariantIt","hasNumber","hasDate","isSpatiallyIncludedInCity","isSpatiallyIncludedInState","isSpatiallyIncludedInCountry","isSpatiallyIncludedInCountryUri","acronymEn","obsolete"}; // the fields to bind do in your JavaBean
		//String[] columns = new String[] {"URI","isPartOf ConferenceSeries","LABEL_EN","LABEL_FR","LABEL_ES","LABEL_AR","LABEL_ZH","LABEL_RU","LABEL_IT","LABEL_PT","LABEL_DE","LABEL_TR","LABEL_ID","LABEL_MK","LABEL_PL","Has spelling variant_EN_1","Has spelling variant_EN_2","Has spelling variant_FR_1","Has spelling variant_FR_2","Has spelling variant_FR_3","Has spelling variant_ES_1","Has spelling variant_ES_2","Has spelling variant_ES_3","Has spelling variant_ES_4","Has spelling variant_RU","Has spelling variant_PT_1","Has spelling variant_PT_2","Has spelling variant_IT","hasNumber","hasDate","isSpatiallyIncludedIn_City","isSpatiallyIncludedIn_State","isSpatiallyIncludedIn_Country","isSpatiallyIncludedIn_Country_URI","ACRONYM_EN","OBSOLETE"}; // the fields to bind do in your JavaBean
		strat.setColumnMapping(columns);

		CsvToBean csv = new CsvToBean();
		List<Conferences> list = csv.parse(strat, new FileReader(filename));
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public static List<ConferencesSeries> loadConferenceSeriesData(String filename) throws FileNotFoundException
	{
		ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
		strat.setType(ConferencesSeries.class);
		String[] columns = new String[] {"uri","labelAr","labelEn","labelEnNonPreferredTerms","labelFr","labelEs","labelIt","labelZh","labelRu","labelPt","labelDe","labelTr","labelId","labelMk","labelPl","acronymEn","acronymFr","acronymEs","obsolete","faotermCode"}; // the fields to bind do in your JavaBean
		strat.setColumnMapping(columns);
		
		CsvToBean csv = new CsvToBean();
		List<ConferencesSeries> list = csv.parse(strat, new FileReader(filename));
		return list;
	}
}
