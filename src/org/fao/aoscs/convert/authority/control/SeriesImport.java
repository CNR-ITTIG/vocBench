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

public class SeriesImport {
	
	private final static String driver = "com.mysql.jdbc.Driver";
	private final static String dbIp = "localhost";
	private final static String dbPort = "3306";
	private final static String dbName = "agrovocwb_ac_series_v_1_0";
	private final static String dbUser = "fao";
  	private final static String dbPassword = "faomimos";
  	private final static String owlURI = "file:/Sachit/workspace/aos_cs_1_v_1_0_2/owl/v1.0/aos.owl";
  	private final static String modelConstantsURI = "org.fao.aoscs.server.ModelConstants";
	private static final String ADDRESS_FILE="/Sachit/workspace/aos_cs_1_v_1_0_2/csv/20100506_Series.csv";
	
	private static String status = "Published";
	private static String seriesNamespace = ModelConstants.BASENAMESPACE+"series"+ModelConstants.NAMESPACESEPARATOR;
	private static String seriesNamespacePrefix = "series";
	private static String countryNamespace = ModelConstants.BASENAMESPACE+"country"+ModelConstants.NAMESPACESEPARATOR;
	private static String countryNamespacePrefix = "country";
	private static String geopoliticalNamespace = "http://aims.fao.org/aos/geopolitical/";
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

			seriesNamespace = ModelConstants.BASENAMESPACE+"series"+ModelConstants.NAMESPACESEPARATOR;
			countryNamespace = ModelConstants.BASENAMESPACE+"country"+ModelConstants.NAMESPACESEPARATOR;
			corporateNamespace = ModelConstants.BASENAMESPACE+"corporate"+ModelConstants.NAMESPACESEPARATOR;

			owlModel.getNamespaceManager().setPrefix(seriesNamespace, seriesNamespacePrefix);
			owlModel.getNamespaceManager().setPrefix(countryNamespace, countryNamespacePrefix);
			owlModel.getNamespaceManager().setPrefix(geopoliticalNamespace, geopoliticalNamespacePrefix);
			owlModel.getNamespaceManager().setPrefix(corporateNamespace, corporateNamespacePrefix);

			//ImportUtil.printNameSpaces(owlModel);
			
			createSeriesConcept(owlModel);
			createCountryConcept(owlModel);
			createCorporateConcept(owlModel);
			
			List<Series> list = loadSeriesData(ADDRESS_FILE);
			convert(owlModel, list);
			addC2CRelations(owlModel, list);
			
			owlModel.dispose();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	
	public static void createAllTerm(OWLModel owlModel, String prefix, OWLIndividual ins, ArrayList<OWLIndividual> termList, String id, String label, String lang, boolean isMainLabel, String status, String code, String[] alternatives, String acronyms)
	{
		if(label!=null && !label.equals(""))
		{
			OWLIndividual term = createPreferredTerm(owlModel, prefix, ins, id, label, lang, isMainLabel, status, code);
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
	
	public static void convert(OWLModel owlModel, List<Series> list)
	{
		int cnt=0;
		for(Series series:list)
		{
			cnt++;
			if(cnt==1) continue;
			//if(cnt>100) break;
			System.out.println("-------------------------------------------------");
			String id = series.getId().replaceFirst("http://aims.fao.org/aos/series/c_", "");
			series.setId(id);
			System.out.println("Series URI: "+series.getId());

			if(!isEmpty(series))
			{
				String status = "Published";
				if(!series.getObsolete().equals("0"))
					status = "Deprecated";
				
				OWLIndividual ins = ProtegeUtil.createConcept(owlModel, seriesNamespacePrefix+":c_series", seriesNamespacePrefix, series.getId(), "c_", status);
				ArrayList<OWLIndividual> termList = new ArrayList<OWLIndividual>();  
				
				
				String[] alternativeEn = {series.getAlternativeEn1(),series.getAlternativeEn2(), series.getAlternativeEn3()};
				String[] alternativeFr = {series.getAlternativeFr1(), series.getAlternativeFr2()};
				String[] alternativeEs = {series.getAlternativeEs1(), series.getAlternativeEs2()};
				String[] alternativeAr = {series.getAlternativeAr1()};
				String[] alternativeZh = {series.getAlternativeZh1()};
				String[] alternativeRu = {series.getAlternativeRu1(), series.getAlternativeRu2()};
				String[] alternativePl = {series.getAlternativePl1()};
				String[] alternativeNl = {series.getAlternativeNl1()};
				String[] alternativeIt = {series.getAlternativeIt1()};
				String[] alternativeDe = {series.getAlternativeDe1()};
				String[] alternativePt = {series.getAlternativePt1()};
				String[] alternativeSv = {series.getAlternativeSv1()};
				String[] alternativeVi = {series.getAlternativeVi1()};
				String[] alternativeFi = {series.getAlternativeFi1()};
				String[] alternativeSr = {series.getAlternativeSr1()};
				
				
				createAllTerm(owlModel, seriesNamespacePrefix, ins, termList, series.getId(), series.getLabelEn(), "en", true, status, null, alternativeEn, null);
				createAllTerm(owlModel, seriesNamespacePrefix, ins, termList, series.getId(), series.getLabelEnNonPreferred(), "en", false, status, null, null, null);
				createAllTerm(owlModel, seriesNamespacePrefix, ins, termList, series.getId(), series.getLabelEs(), "es", true, status, null, alternativeEs, null);
				createAllTerm(owlModel, seriesNamespacePrefix, ins, termList, series.getId(), series.getLabelFr(), "fr", true, status, null, alternativeFr, null);
				createAllTerm(owlModel, seriesNamespacePrefix, ins, termList, series.getId(), series.getLabelAr(), "ar", true, status, null, alternativeAr, null);
				createAllTerm(owlModel, seriesNamespacePrefix, ins, termList, series.getId(), series.getLabelZh(), "zh", true, status, null, alternativeZh, null);
				createAllTerm(owlModel, seriesNamespacePrefix, ins, termList, series.getId(), series.getLabelRu(), "ru", true, status, null, alternativeRu, null);
				createAllTerm(owlModel, seriesNamespacePrefix, ins, termList, series.getId(), series.getLabelPl(), "pl", true, status, null, alternativePl, null);
				createAllTerm(owlModel, seriesNamespacePrefix, ins, termList, series.getId(), series.getLabelNl(), "nl", true, status, null, alternativeNl, null);
				createAllTerm(owlModel, seriesNamespacePrefix, ins, termList, series.getId(), series.getLabelIt(), "it", true, status, null, alternativeIt, null);
				createAllTerm(owlModel, seriesNamespacePrefix, ins, termList, series.getId(), series.getLabelDe(), "de", true, status, null, alternativeDe, null);
				createAllTerm(owlModel, seriesNamespacePrefix, ins, termList, series.getId(), series.getLabelPt(), "pt", true, status, null, alternativePt, null);
				createAllTerm(owlModel, seriesNamespacePrefix, ins, termList, series.getId(), series.getLabelSv(), "sv", true, status, null, alternativeSv, null);
				createAllTerm(owlModel, seriesNamespacePrefix, ins, termList, series.getId(), series.getLabelVi(), "vi", true, status, null, alternativeVi, null);
				createAllTerm(owlModel, seriesNamespacePrefix, ins, termList, series.getId(), series.getLabelFi(), "fi", true, status, null, alternativeFi, null);
				createAllTerm(owlModel, seriesNamespacePrefix, ins, termList, series.getId(), series.getLabelSr(), "sr", true, status, null, alternativeSr, null);
				
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
				if(series.getIsSpatiallyIncludedInCountry1Uri()!=null && !series.getIsSpatiallyIncludedInCountry1Uri().equals("") && series.getIsSpatiallyIncludedInCountry1()!=null && !series.getIsSpatiallyIncludedInCountry1().equals(""))
				{
					countryIns = createGeopPoliticalConcept(owlModel, series.getIsSpatiallyIncludedInCountry1Uri(), series.getIsSpatiallyIncludedInCountry1());
					ProtegeUtil.addConcept2ConceptRelationship(owlModel, ModelConstants.RISSPATIALLYINCLUDEDIN, ins, countryIns);
				}
				if(series.getIsSpatiallyIncludedInCountry2Uri()!=null && !series.getIsSpatiallyIncludedInCountry2Uri().equals("") && series.getIsSpatiallyIncludedInCountry2()!=null && !series.getIsSpatiallyIncludedInCountry2().equals(""))
				{
					countryIns = createGeopPoliticalConcept(owlModel, series.getIsSpatiallyIncludedInCountry2Uri(), series.getIsSpatiallyIncludedInCountry2());
					ProtegeUtil.addConcept2ConceptRelationship(owlModel, ModelConstants.RISSPATIALLYINCLUDEDIN, ins, countryIns);
				}
				
				// Add relationship isPublishedBy between the concepts
				createIsPublishedByRelationship(owlModel, series.getIsPublishedBy1Uri().replaceFirst("http://aims.fao.org/aos/corporatebody/c_", ""), series.getIsPublishedBy1(), ins);
				createIsPublishedByRelationship(owlModel, series.getIsPublishedBy2Uri().replaceFirst("http://aims.fao.org/aos/corporatebody/c_", ""), series.getIsPublishedBy2(), ins);
				createIsPublishedByRelationship(owlModel, series.getIsPublishedBy3Uri().replaceFirst("http://aims.fao.org/aos/corporatebody/c_", ""), series.getIsPublishedBy3(), ins);
				
				//Add Scope Note
				ProtegeUtil.addDatatypeProperty(owlModel, ins, ModelConstants.RHASSCOPENOTE, series.getScopeNote1En(), "en");
				ProtegeUtil.addDatatypeProperty(owlModel, ins, ModelConstants.RHASSCOPENOTE, series.getScopeNote2En(), "en");
				
				//Add ISSN
				ProtegeUtil.addDatatypeProperty(owlModel, ins, ModelConstants.RHASISSN, series.getIssn());
				
				//Add Hold By
				ProtegeUtil.addDatatypeProperty(owlModel, ins, ModelConstants.RISHOLDBY, series.getHoldingLibrary());
				
				//Add Call Number
				ProtegeUtil.addDatatypeProperty(owlModel, ins, ModelConstants.RHASCALLNUMBER, series.getCallNumber());
			}
			else
			{
				System.out.println("Empty series: "+series.getId());
			}
		}
	}
	
	public static OWLIndividual createPreferredTerm(OWLModel owlModel, String namespacePrefix, OWLIndividual ins, String name, String label, String lang, boolean isMainlabel, String status, String code)
	{
		OWLIndividual term = ProtegeUtil.createTerm(owlModel, namespacePrefix, ins, name, label, lang, isMainlabel, status);
		ProtegeUtil.addDatatypeProperty(owlModel, term, ModelConstants.RHASCODEAGROVOC, code);
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
	
	public static void addC2CRelations(OWLModel owlModel, List<Series> list)
	{
		int cnt=0;
		for(Series series:list)
		{
			cnt++;
			if(cnt==1) continue;
			System.out.println("---------------Relationships----------------------------------");
			String id = series.getId().replaceFirst("http://aims.fao.org/aos/series/c_", "");
			series.setId(id);
			System.out.println("Series URI: "+series.getId());
			
			if(!isEmpty(series))
			{
				OWLIndividual ins = owlModel.getOWLIndividual(seriesNamespacePrefix+":"+"i_"+series.getId());
		
				//Add OTHER LANGUAGE EDITION
				createConcept2ConceptRelationship(owlModel, ModelConstants.RISOTHERLANGUAGEEDITIONOF, ins, getClassNameFromURI(series.getHasTranslation1(), "http://aims.fao.org/aos/series/", seriesNamespacePrefix));
				createConcept2ConceptRelationship(owlModel, ModelConstants.RISOTHERLANGUAGEEDITIONOF, ins, getClassNameFromURI(series.getHasTranslation2(), "http://aims.fao.org/aos/series/", seriesNamespacePrefix));
				createConcept2ConceptRelationship(owlModel, ModelConstants.RISOTHERLANGUAGEEDITIONOF, ins, getClassNameFromURI(series.getHasTranslation3(), "http://aims.fao.org/aos/series/", seriesNamespacePrefix));
				createConcept2ConceptRelationship(owlModel, ModelConstants.RISOTHERLANGUAGEEDITIONOF, ins, getClassNameFromURI(series.getHasTranslation4(), "http://aims.fao.org/aos/series/", seriesNamespacePrefix));
				createConcept2ConceptRelationship(owlModel, ModelConstants.RISOTHERLANGUAGEEDITIONOF, ins, getClassNameFromURI(series.getHasTranslation5(), "http://aims.fao.org/aos/series/", seriesNamespacePrefix));
		
				// Add Follows
				createConcept2ConceptRelationship(owlModel, ModelConstants.RPRECEDES, ins, getClassNameFromURI(series.getIsFollowedBy(), "http://aims.fao.org/aos/series/", seriesNamespacePrefix));
				
				//Add Precedes
				createConcept2ConceptRelationship(owlModel, ModelConstants.RFOLLOWS, ins, getClassNameFromURI(series.getFollows(), "http://aims.fao.org/aos/series/", seriesNamespacePrefix));
			}
			else
			{
				System.out.println("Empty series: "+series.getId());
			}
		}
		
	}
	
	public static void createSeriesConcept(OWLModel owlModel){
		String name = "series";
		String label = "series";	
		String lang = "en";
		
		OWLIndividual ins = ProtegeUtil.createConcept(owlModel, ModelConstants.CDOMAINCONCEPT, seriesNamespacePrefix, name, "c_", status);
		ProtegeUtil.createTerm(owlModel, seriesNamespacePrefix, ins, name, label, lang, true, status);
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
		if(termIns==null)	ProtegeUtil.createTerm(owlModel, corporateNamespacePrefix, ins, name, label, lang, true, status);
		
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
	
	public static boolean isEmpty(Series series)
	{	
		return( series.getLabelEn().equals("") &&
				series.getLabelEnNonPreferred().equals("") &&
				series.getLabelFr().equals("") &&
				series.getLabelEs().equals("") &&
				series.getLabelAr().equals("") &&
				series.getLabelZh().equals("") &&
				series.getLabelRu().equals("") &&
				series.getLabelPl().equals("") &&
				series.getLabelNl().equals("") &&
				series.getLabelIt().equals("") &&
				series.getLabelDe().equals("") &&
				series.getLabelPt().equals("") &&
				series.getLabelSv().equals("") &&
				series.getLabelVi().equals("") &&
				series.getLabelFi().equals("") &&
				series.getLabelSr().equals("")
		);	
		
	}
	
	@SuppressWarnings("unchecked")
	public static List<Series> loadSeriesData(String filename) throws FileNotFoundException
	{
		ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
		strat.setType(Series.class);
		//String[] columns = new String[] {"id","labelEn","labelFr","alternativeFr1","alternativeFr2","labelEs","labelAr","labelZh","labelRu","labelIt","labelDe","labelPt","labelSv","labelVi","labelFi","labelSr","alternativeEn1","alternativeEn2","alternativeEn3","alternativeEs1","alternativeEs2","alternativeZh1","alternativeAr1","alternativeRu1","alternativeRu2","alternativeIt1","alternativeDe1","alternativePt1","alternativeSv1","alternativeVi1","alternativeFi1","alternativeSr1","isSpatiallyIncludedIn1","isSpatiallyIncludedIn1Uri","isSpatiallyIncludedIn2","isSpatiallyIncludedIn2Uri","isProducedBy1","isProducedBy1Uri","isProducedBy2","isProducedBy2Uri","isProducedBy3","isProducedBy3Uri"}; // the fields to bind do in your JavaBean
		String[] columns = new String[] {"id","labelEn","labelEnNonPreferred","labelFr","labelEs","labelAr","labelZh","labelRu","labelPl","labelNl","labelIt","labelDe","labelPt","labelSv","labelVi","labelFi","labelSr","alternativeEn1","alternativeEn2","alternativeEn3","alternativeFr1","alternativeFr2","alternativeEs1","alternativeEs2","alternativeAr1","alternativeZh1","alternativeRu1","alternativeRu2","alternativePl1","alternativeNl1","alternativeIt1","alternativeDe1","alternativePt1","alternativeSv1","alternativeVi1","alternativeFi1","alternativeSr1", "issn","holdingLibrary","callNumber","isSpatiallyIncludedInCountry1","isSpatiallyIncludedInCountry1Uri","isSpatiallyIncludedInCountry2","isSpatiallyIncludedInCountry2Uri","hasTranslation1","hasTranslation2","hasTranslation3","hasTranslation4","hasTranslation5","isFollowedBy","follows","isPublishedBy1","isPublishedBy1Uri","isPublishedBy2","isPublishedBy2Uri","isPublishedBy3","isPublishedBy3Uri","scopeNote1En","scopeNote2En","obsolete"};	// the fields to bind do in your JavaBean
		strat.setColumnMapping(columns);

		CsvToBean csv = new CsvToBean();
		List list = csv.parse(strat, new FileReader(filename));

		return list;
	}
}
