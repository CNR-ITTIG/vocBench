package org.fao.aoscs.convert.authority.control;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.convert.util.ProtegeUtil;

import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;

public class JournalsImport {
	
	private final static String driver = "com.mysql.jdbc.Driver";
	private final static String dbIp = "localhost";
	private final static String dbPort = "3306";
	private final static String dbName = "agrovocwb_ac_journals_v_1_0";
	private final static String dbUser = "fao";
  	private final static String dbPassword = "faomimos";
  	private final static String owlURI = "file:/Sachit/workspace/aos_cs_1_v_1_0_2/owl/v1.0/aos.owl";
  	private final static String modelConstantsURI = "org.fao.aoscs.server.ModelConstants";
	private static final String ADDRESS_FILE="/Sachit/workspace/aos_cs_1_v_1_0_2/csv/20100414_Journals.csv";
	
	private static String status = "Published";
	private static String journalNamespace = ModelConstants.BASENAMESPACE+"journal"+ModelConstants.NAMESPACESEPARATOR;
	private static String journalNamespacePrefix = "journal";
	private static String countryNamespace = ModelConstants.BASENAMESPACE+"country"+ModelConstants.NAMESPACESEPARATOR;
	private static String countryNamespacePrefix = "country";
	private static String geopoliticalNamespace = "http://aims.fao.org/aos/geopolitical.owl#";
	private static String geopoliticalNamespacePrefix = "geo";
	private static String corporateNamespace = ModelConstants.BASENAMESPACE+"corporate"+ModelConstants.NAMESPACESEPARATOR;
	private static String corporateNamespacePrefix = "corporate";


	public static void main(String args[]) 
	{
		init();
	}
	
	public static void init(){
		try {

			OWLModel owlModel = ProtegeUtil.getOWLModel(driver, dbIp, dbPort, dbName, dbUser, dbPassword, owlURI, modelConstantsURI);

			journalNamespace = ModelConstants.BASENAMESPACE+"journal"+ModelConstants.NAMESPACESEPARATOR;
			countryNamespace = ModelConstants.BASENAMESPACE+"country"+ModelConstants.NAMESPACESEPARATOR;
			corporateNamespace = ModelConstants.BASENAMESPACE+"corporate"+ModelConstants.NAMESPACESEPARATOR;

			owlModel.getNamespaceManager().setPrefix(journalNamespace, journalNamespacePrefix);
			owlModel.getNamespaceManager().setPrefix(countryNamespace, countryNamespacePrefix);
			owlModel.getNamespaceManager().setPrefix(geopoliticalNamespace, geopoliticalNamespacePrefix);
			owlModel.getNamespaceManager().setPrefix(corporateNamespace, corporateNamespacePrefix);

			//ImportUtil.printNameSpaces(owlModel);
			
			createJournalConcept(owlModel);
			createCountryConcept(owlModel);
			createCorporateConcept(owlModel);
			
			List<Journals> list = loadJournalsData(ADDRESS_FILE);
			//addISSN(owlModel, list);
			convert(owlModel, list);
			addC2CRelations(owlModel, list);
			
			owlModel.dispose();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void addISSN(OWLModel owlModel, List<Journals> list)
	{
		int cnt=0;
		int cc=0;
		for(Journals journals:list)
		{
			cnt++;
			if(cnt==1) continue;
			System.out.println("---------------Relationships----------------------------------");
			String id = journals.getId().replaceFirst("http://www.fao.org/aims/aos/journal/c_", "");
			journals.setId(id);
			System.out.println("Journals URI: "+journals.getId());
			
			if(!isEmpty(journals))
			{
				OWLIndividual ins = owlModel.getOWLIndividual(journalNamespacePrefix+":"+"i_"+journals.getId());
				if(!journals.getIssn().equals(""))
				{
					cc++;
				System.out.println(cc+" = "+ins.getName()+" - "+journals.getIssn());
				
				//Add ISSN
				ProtegeUtil.addDatatypeProperty(owlModel, ins, ModelConstants.RHASISSN, journals.getIssn());
				}
			}
		}
	}
	
	public static void convert(OWLModel owlModel, List<Journals> list)
	{
		int cnt=0;
		for(Journals journals:list)
		{
			cnt++;
			if(cnt==1) continue;
			//if(cnt>100) break;
			System.out.println("-------------------------------------------------");
			String id = journals.getId().replaceFirst("http://www.fao.org/aims/aos/journal/c_", "");
			journals.setId(id);
			System.out.println("Journals URI: "+journals.getId());
			
			if(!isEmpty(journals))
			{
				
				String status = "Published";
				if(!journals.getObsolete().equals("0"))
					status = "Deprecated";
			
				OWLIndividual ins = ProtegeUtil.createConcept(owlModel, journalNamespacePrefix+":c_journal", journalNamespacePrefix, journals.getId(), "c_", status);
				
				ArrayList<OWLIndividual> termList = new ArrayList<OWLIndividual>();  
				String[] alternativeEn = {journals.getHasSpellingVariantEn1(), journals.getHasSpellingVariantEn2(), journals.getHasSpellingVariantEn3() };
				String[] alternativeFr = {journals.getHasSpellingVariantFr()};
				String[] alternativeEs = {journals.getHasSpellingVariantEs1(), journals.getHasSpellingVariantEs2()};
				String[] alternativeIt = {journals.getHasSpellingVariantIt()};
				String[] alternativeDe = {journals.getHasSpellingVariantDe1(), journals.getHasSpellingVariantDe2()};
				String[] alternativeSk = {journals.getHasSpellingVariantSk()};
				
				createAllTerm(owlModel, journalNamespacePrefix, ins, termList, journals.getId(), journals.getLabelEn(), "en", true, status, null, alternativeEn, null);
				createAllTerm(owlModel, journalNamespacePrefix, ins, termList, journals.getId(), journals.getLabelEnNonPreferredTerm(), "en", false, status, null, null, null);
				createAllTerm(owlModel, journalNamespacePrefix, ins, termList, journals.getId(), journals.getLabelFr(), "fr", true, status, null, alternativeFr, null);
				createAllTerm(owlModel, journalNamespacePrefix, ins, termList, journals.getId(), journals.getLabelEs(), "es", true, status, null, alternativeEs, null);
				createAllTerm(owlModel, journalNamespacePrefix, ins, termList, journals.getId(), journals.getLabelIt(), "it", true, status, null, alternativeIt, null);
				createAllTerm(owlModel, journalNamespacePrefix, ins, termList, journals.getId(), journals.getLabelDe(), "de", true, status, null, alternativeDe, null);
				createAllTerm(owlModel, journalNamespacePrefix, ins, termList, journals.getId(), journals.getLabelSk(), "sk", true, status, null, alternativeSk, null);
				createAllTerm(owlModel, journalNamespacePrefix, ins, termList, journals.getId(), journals.getLabelZh(), "zh", true, status, null, null, null);
	
				/*//Creating HasTranslation relationships among terms
				ArrayList<OWLIndividual> desttermList = (ArrayList<OWLIndividual>) termList.clone();
				if(termList.size()>1)
				{
					for (OWLIndividual term:termList) {
						for (OWLIndividual destTerm:desttermList) {
							if(!term.getURI().equals(destTerm.getURI()))
								ProtegeUtil.addTermRelationship(owlModel, ModelConstants.RHASTRANSLATION, term, destTerm, ins);
						}
						desttermList.remove(term);
					}
				}*/
				
				//Create Country concepts
				OWLIndividual countryIns = null;
				if(journals.getIsSpatiallyIncludedInCountryUri()!=null && !journals.getIsSpatiallyIncludedInCountryUri().equals("") && journals.getIsSpatiallyIncludedInCountry()!=null && !journals.getIsSpatiallyIncludedInCountry().equals(""))
				{
					countryIns = createGeopPoliticalConcept(owlModel, journals.getIsSpatiallyIncludedInCountryUri(), journals.getIsSpatiallyIncludedInCountry());
					ProtegeUtil.addConcept2ConceptRelationship(owlModel, ModelConstants.RISSPATIALLYINCLUDEDIN, ins, countryIns);
				}
				
				
				// Add relationship isPublishedBy between the concepts
				createIsPublishedByRelationship(owlModel, journals.getIsPublishedBy1Uri().replaceFirst("http://www.fao.org/aims/aos/corporatebody/c_", ""), journals.getIsPublishedBy1(), ins);
				createIsPublishedByRelationship(owlModel, journals.getIsPublishedBy2Uri().replaceFirst("http://www.fao.org/aims/aos/corporatebody/c_", ""), journals.getIsPublishedBy2(), ins);
				createIsPublishedByRelationship(owlModel, journals.getIsPublishedBy3Uri().replaceFirst("http://www.fao.org/aims/aos/corporatebody/c_", ""), journals.getIsPublishedBy3(), ins);

				//Add Scope Note
				ProtegeUtil.addDatatypeProperty(owlModel, ins, ModelConstants.RHASSCOPENOTE, journals.getScopeNoteEn(), "en");
				
				//Add ISSN
				ProtegeUtil.addDatatypeProperty(owlModel, ins, ModelConstants.RHASISSN, journals.getIssn());
				
				//Add Hold By
				ProtegeUtil.addDatatypeProperty(owlModel, ins, ModelConstants.RISHOLDBY, journals.getHoldingLibrary());
				
				//Add Call Number
				ProtegeUtil.addDatatypeProperty(owlModel, ins, ModelConstants.RHASCALLNUMBER, journals.getCallNumber());
			}
			else
			{
				System.out.println("Empty journals: "+journals.getId());
			}
		}
	}
	
	
	public static void addC2CRelations(OWLModel owlModel, List<Journals> list)
	{
		int cnt=0;
		for(Journals journals:list)
		{
			cnt++;
			if(cnt==1) continue;
			System.out.println("---------------Relationships----------------------------------");
			String id = journals.getId().replaceFirst("http://www.fao.org/aims/aos/journal/c_", "");
			journals.setId(id);
			System.out.println("Journals URI: "+journals.getId());
			
			if(!isEmpty(journals))
			{
				OWLIndividual ins = owlModel.getOWLIndividual(journalNamespacePrefix+":"+"i_"+journals.getId());
		
				//Add OTHER LANGUAGE EDITION
				createConcept2ConceptRelationship(owlModel, ModelConstants.RISOTHERLANGUAGEEDITIONOF, ins, getClassNameFromURI(journals.getHasTranslation1(), "http://www.fao.org/aims/aos/journal/", journalNamespacePrefix));
				createConcept2ConceptRelationship(owlModel, ModelConstants.RISOTHERLANGUAGEEDITIONOF, ins, getClassNameFromURI(journals.getHasTranslation2(), "http://www.fao.org/aims/aos/journal/", journalNamespacePrefix));
		
				// Add Follows
				createConcept2ConceptRelationship(owlModel, ModelConstants.RPRECEDES, ins, getClassNameFromURI(journals.getIsFollowedBy(), "http://www.fao.org/aims/aos/journal/", journalNamespacePrefix));
				
				//Add Precedes
				createConcept2ConceptRelationship(owlModel, ModelConstants.RFOLLOWS, ins, getClassNameFromURI(journals.getFollows(), "http://www.fao.org/aims/aos/journal/", journalNamespacePrefix));
			}
			else
			{
				System.out.println("Empty journals: "+journals.getId());
			}
		}
		
	}
	
	public static void createAllTerm(OWLModel owlModel, String prefix, OWLIndividual ins, ArrayList<OWLIndividual> termList, String id, String label, String lang, boolean isMainLabel, String status, String code, String[] alternatives, String acronyms)
	{
		
		if(label!=null && !label.equals(""))
		{
			OWLIndividual term = createTerm(owlModel, prefix, ins, id, label.trim(), lang, isMainLabel, status, code);
			termList.add(term);
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
	
	public static OWLIndividual createTerm(OWLModel owlModel, String namespacePrefix, OWLIndividual ins, String name, String label, String lang, boolean isMainlabel, String status, String code)
	{
		OWLIndividual term = ProtegeUtil.createTerm(owlModel, namespacePrefix, ins, name, label, lang, isMainlabel, status);
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
	
	public static void createJournalConcept(OWLModel owlModel){
		String name = "journal";
		String label = "journal";	
		String lang = "en";
		
		OWLIndividual ins = ProtegeUtil.createConcept(owlModel, ModelConstants.CDOMAINCONCEPT, journalNamespacePrefix, name, "c_", status);
		ProtegeUtil.createTerm(owlModel, journalNamespacePrefix, ins, name, label, lang, true, status);
	}
	
	public static void createCountryConcept(OWLModel owlModel){
		String name = "country";
		String label = "country";	
		String lang = "en";
		
		OWLIndividual ins = ProtegeUtil.createConcept(owlModel, ModelConstants.CDOMAINCONCEPT, countryNamespacePrefix, name, "c_", status);
		ProtegeUtil.createTerm(owlModel, countryNamespacePrefix, ins, name, label, lang, true, status);
	}
	
	public static void createCorporateConcept(OWLModel owlModel){
		String name = "corporate";
		String label = "corporate";	
		String lang = "en";
		
		OWLIndividual ins = ProtegeUtil.createConcept(owlModel, ModelConstants.CDOMAINCONCEPT, corporateNamespacePrefix, name, "c_", status);
		ProtegeUtil.createTerm(owlModel, corporateNamespacePrefix, ins, name, label, lang, true, status);
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
	
	public static OWLIndividual createCorporateBodiesConcept(OWLModel owlModel, String name, String label){
		String lang = "en";
		OWLNamedClass cls = owlModel.getOWLNamedClass(corporateNamespacePrefix+":"+"c_"+name);
		OWLIndividual ins = null;
		if(cls==null)
		{
			ins = ProtegeUtil.createConcept(owlModel, corporateNamespacePrefix+":c_corporate", corporateNamespacePrefix, name, "c_", status);
		}
		else
		{
			ins = ProtegeUtil.getConceptInstance(owlModel, cls);
		}
		
		OWLIndividual termIns = ProtegeUtil.getTerm(owlModel, ins, label, lang);
		if(termIns==null)	
		{
			ProtegeUtil.createTerm(owlModel, corporateNamespacePrefix, ins, name, label, lang, true, status);
		}
		
		return ins;
	}
	
	public static void createIsPublishedByRelationship(OWLModel owlModel, String name, String label, OWLIndividual ins)
	{
		if(name!=null && !name.equals(""))
		{
			OWLIndividual destIns = createCorporateBodiesConcept(owlModel, name, label);
			if(destIns!=null)
			{
				ProtegeUtil.addConcept2ConceptRelationship(owlModel, ModelConstants.RISPUBLISHEDBY, ins, destIns);
			}
		}
	}
	
	public static void createConcept2ConceptRelationship(OWLModel owlModel, String property, OWLIndividual ins, String dest)
	{
		if(dest!=null && !dest.equals(""))
		{
			OWLNamedClass owlCls = owlModel.getOWLNamedClass(dest);
			if(owlCls !=null)
			{
				OWLIndividual destIns = ProtegeUtil.getConceptInstance(owlModel, owlCls);
				if(destIns!=null)
				{
					ProtegeUtil.addConcept2ConceptRelationship(owlModel, property, ins, destIns);
				}
				else
				{
					System.out.println("Null Destination: "+dest);
				}
			}
			else
			{
				System.out.println("Null Destination: "+dest);
			}
		}
	}
	
	private static String getClassNameFromURI(String uri, String basename, String prefix)
	{
		
		return uri.replaceFirst(basename, prefix+":");
	}
	
	public static boolean isEmpty(Journals journals)
	{
		return (journals.getLabelEn().equals("") &&
				journals.getLabelEnNonPreferredTerm().equals("") && 
				journals.getLabelFr().equals("") && 
				journals.getLabelEs().equals("") && 
				journals.getLabelIt().equals("") && 
				journals.getLabelDe().equals("") && 
				journals.getLabelSk().equals("") && 
				journals.getLabelZh().equals("") 
		);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Journals> loadJournalsData(String filename) throws FileNotFoundException
	{
		ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
		strat.setType(Journals.class);
		//String[] columns = new String[] {"id","labelEn","labelFr","alternativeFr","labelEs","labelPt","labelIt","labelDe","labelSk","alternativeEn","alternativeEs","alternativePt","alternativeIt","alternativeDe","alternativeSk","isSpatiallyIncludedIn","isSpatiallyIncludedInUri","isProducedBy1","isProducedBy1Uri","isProducedBy2","isProducedBy2Uri","isProducedBy3","isProducedBy3Uri"}; // the fields to bind do in your JavaBean
		String[] columns = new String[] {"id","labelEn","labelEnNonPreferredTerm","labelFr","labelEs","labelIt","labelDe","labelSk","labelSK","labelZh","hasSpellingVariantEn1","hasSpellingVariantEn2","hasSpellingVariantEn3","hasSpellingVariantFr","hasSpellingVariantEs1","hasSpellingVariantEs2","hasSpellingVariantIt","hasSpellingVariantDe1","hasSpellingVariantDe2","hasSpellingVariantSk","issn","holdingLibrary","callNumber","isSpatiallyIncludedInCountry","isSpatiallyIncludedInCountryUri","hasTranslation1","hasTranslation2","isFollowedBy","follows","isPublishedBy1","isPublishedBy1Uri","isPublishedBy2","isPublishedBy2Uri","isPublishedBy3","isPublishedBy3Uri","scopeNoteEn","obsolete","notToBeMigrated"}; // the fields to bind do in your JavaBean
		//String[] columns = new String[] {"URI","LABEL_EN","LABEL_EN_nonPreferredTerm","LABEL_FR","LABEL_ES","LABEL_IT","LABEL_DE","LABEL_SK","LABEL_AR","LABEL_ZH","Has spelling variant_EN_1","Has spelling variant_EN_2","Has spelling variant_EN_3","Has spelling variant_FR","Has spelling variant_ES_1","Has spelling variant_ES_2","Has spelling variant_IT","Has spelling variant_DE_1","Has spelling variant_DE_2","Has spelling variant_SK","ISSN","Holding_Library","Call_Number","isSpatiallyIncludedIn_Country","isSpatiallyIncludedIn_Country_URI","hasTranslation_1","hasTranslation_2","isFollowedBy","Follows","isPublishedBy_1","isPublishedBy_1_URI","isPublishedBy_2","isPublishedBy_2_URI","isPublishedBy_3","isPublishedBy_3_URI","Scope_Note_EN","Obsolete","NotToBeMigrated"
		strat.setColumnMapping(columns);

		CsvToBean csv = new CsvToBean();
		List<Journals> list = csv.parse(strat, new FileReader(filename));

		return list;
	}
}
