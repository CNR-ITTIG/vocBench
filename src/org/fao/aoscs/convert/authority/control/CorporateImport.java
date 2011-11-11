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

public class CorporateImport {
	
	private final static String driver = "com.mysql.jdbc.Driver";
	private final static String dbIp = "localhost";
	private final static String dbPort = "3306";
	private final static String dbName = "agrovocwb_ac_corporate_bodies_v_1_0";
	private final static String dbUser = "fao";
  	private final static String dbPassword = "faomimos";
  	private final static String owlURI = "file:/Sachit/workspace/aos_cs_1_v_1_0_2/owl/v1.0/aos.owl";
  	private final static String modelConstantsURI = "org.fao.aoscs.server.ModelConstants";
	private static final String ADDRESS_FILE="/Sachit/workspace/aos_cs_1_v_1_0_2/csv/20100407-CorporateBodies.csv";
	
	private static String status = "Published";
	private static String corporateNamespace = ModelConstants.BASENAMESPACE+"corporate"+ModelConstants.NAMESPACESEPARATOR;
	private static String corporateNamespacePrefix = "corporate";
	private static String countryNamespace = ModelConstants.BASENAMESPACE+"country"+ModelConstants.NAMESPACESEPARATOR;
	private static String countryNamespacePrefix = "country";
	private static String geopoliticalNamespace = "http://aims.fao.org/aos/geopolitical.owl#";
	private static String geopoliticalNamespacePrefix = "geo";

	public static void main(String args[]) 
	{
		init();
	}
	
	public static void init(){
		try {

			OWLModel owlModel = ProtegeUtil.getOWLModel(driver, dbIp, dbPort, dbName, dbUser, dbPassword, owlURI, modelConstantsURI);

			corporateNamespace = ModelConstants.BASENAMESPACE+"corporate"+ModelConstants.NAMESPACESEPARATOR;
			countryNamespace = ModelConstants.BASENAMESPACE+"country"+ModelConstants.NAMESPACESEPARATOR;
			owlModel.getNamespaceManager().setPrefix(corporateNamespace, corporateNamespacePrefix);
			owlModel.getNamespaceManager().setPrefix(countryNamespace, countryNamespacePrefix);
			owlModel.getNamespaceManager().setPrefix(geopoliticalNamespace, geopoliticalNamespacePrefix);

			createCorporateConcept(owlModel);
			createCountryConcept(owlModel);
			
			convert(owlModel, loadCorporateBodiesData(ADDRESS_FILE));
			
			owlModel.dispose();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void convert(OWLModel owlModel, List<Corporate> list)
	{
		int cnt=0;
		for(Corporate corporate:list)
		{
			cnt++;
			if(cnt==1) continue;
			//if(cnt>100) break;
			System.out.println("-------------------------------------------------");
			String id = corporate.getUri().replaceFirst("http://aims.fao.org/aos/corporatebody/c_", "");
			corporate.setUri(id);
			System.out.println("Corporate URI: "+corporate.getUri());

			if(!isEmpty(corporate))
			{
				String status = "Published";
				if(!corporate.getObsolete().equals("0"))
					status = "Deprecated";
				
				OWLIndividual ins = ProtegeUtil.createConcept(owlModel, corporateNamespacePrefix+":c_corporate", corporateNamespacePrefix, corporate.getUri(), "c_", status);
				
				ArrayList<OWLIndividual> termList = new ArrayList<OWLIndividual>();  
				String[] alternativeEn = {corporate.getAlternativeEn1(),corporate.getAlternativeEn2(),corporate.getAlternativeEn3(),corporate.getAlternativeEn4(),corporate.getAlternativeEn5()};
				String[] alternativeFr = {corporate.getAlternativeFr1(),corporate.getAlternativeFr2(),corporate.getAlternativeFr3(),corporate.getAlternativeFr4(),corporate.getAlternativeFr5()};
				String[] alternativeEs = {corporate.getAlternativeEs1(), corporate.getAlternativeEs2(),corporate.getAlternativeEs3(),corporate.getAlternativeEs4(),corporate.getAlternativeEs5(),corporate.getAlternativeEs6(),corporate.getAlternativeEs7(),corporate.getAlternativeEs8(),corporate.getAlternativeEs9(),corporate.getAlternativeEs10(),corporate.getAlternativeEs11(),corporate.getAlternativeEs12(),corporate.getAlternativeEs13(),corporate.getAlternativeEs14(),corporate.getAlternativeEs15(),corporate.getAlternativeEs16()};
				String[] alternativeAr = {corporate.getAlternativeAr()};
				String[] alternativeZh = {corporate.getAlternativeZh()};
				String[] alternativeRu = {corporate.getAlternativeRu()};
				String[] alternativeIt = {corporate.getAlternativeIt1(), corporate.getAlternativeIt2(),corporate.getAlternativeIt3(),corporate.getAlternativeIt4()};
				String[] alternativeBn = {corporate.getAlternativeBn()};
				String[] alternativeBs = {corporate.getAlternativeBs()};
				String[] alternativeCa = {corporate.getAlternativeCa()};
				String[] alternativeCs = {corporate.getAlternativeCs()};
				String[] alternativeDa = {corporate.getAlternativeDa()};
				String[] alternativeDe = {corporate.getAlternativeDe1(),corporate.getAlternativeDe2()};
				String[] alternativeFj = {corporate.getAlternativeFj()};
				String[] alternativeHr = {corporate.getAlternativeHr()};
				String[] alternativeHu = {corporate.getAlternativeHu()};
				String[] alternativeId = {corporate.getAlternativeId()};
				String[] alternativeLv = {corporate.getAlternativeLv()};
				String[] alternativeMk = {corporate.getAlternativeMk()};
				String[] alternativeMl = {corporate.getAlternativeMl()};
				String[] alternativeMs = {corporate.getAlternativeMs()};
				String[] alternativeNl = {corporate.getAlternativeNl()};
				String[] alternativeNo = {corporate.getAlternativeNo()};
				String[] alternativePl = {corporate.getAlternativePl()};
				String[] alternativePo = {corporate.getAlternativePo()};
				String[] alternativePt = {corporate.getAlternativePt1()};
				String[] alternativeRo = {corporate.getAlternativeRo()};
				String[] alternativeSk = {corporate.getAlternativeSk()};
				String[] alternativeSl = {corporate.getAlternativeSl()};
				String[] alternativeSr = {corporate.getAlternativeSr()};
				String[] alternativeSv = {corporate.getAlternativeSv()};
				String[] alternativeTr = {corporate.getAlternativeTr()};
				String[] alternativeUk = {corporate.getAlternativeUk()};
				
				createAllTerm(owlModel, corporateNamespacePrefix, ins, termList, corporate.getUri(), corporate.getLabelEn(), "en",  true, status, corporate.getFaotermCode(), alternativeEn, corporate.getAcronymEn());
				createAllTerm(owlModel, corporateNamespacePrefix, ins, termList, corporate.getUri(), corporate.getLabelEnNonPreferredTerm(), "en",  false, status, null, null, null);
				createAllTerm(owlModel, corporateNamespacePrefix, ins, termList, corporate.getUri(), corporate.getLabelFr(), "fr",  true, status, corporate.getFaotermCode(), alternativeFr, corporate.getAcronymFr());
				createAllTerm(owlModel, corporateNamespacePrefix, ins, termList, corporate.getUri(), corporate.getLabelEs(), "es",  true, status, corporate.getFaotermCode(), alternativeEs, corporate.getAcronymEs());
				createAllTerm(owlModel, corporateNamespacePrefix, ins, termList, corporate.getUri(), corporate.getLabelAr(), "ar",  true, status, corporate.getFaotermCode(), alternativeAr, corporate.getAcronymAr());
				createAllTerm(owlModel, corporateNamespacePrefix, ins, termList, corporate.getUri(), corporate.getLabelZh(), "zh",  true, status, corporate.getFaotermCode(), alternativeZh, corporate.getAcronymZh());
				createAllTerm(owlModel, corporateNamespacePrefix, ins, termList, corporate.getUri(), corporate.getLabelRu(), "ru",  true, status, corporate.getFaotermCode(), alternativeRu, corporate.getAcronymRu());
				createAllTerm(owlModel, corporateNamespacePrefix, ins, termList, corporate.getUri(), corporate.getLabelIt(), "it",  true, status, corporate.getFaotermCode(), alternativeIt, corporate.getAcronymIt());
				createAllTerm(owlModel, corporateNamespacePrefix, ins, termList, corporate.getUri(), corporate.getLabelBn(), "bn",  true, status, corporate.getFaotermCode(), alternativeBn, null);
				createAllTerm(owlModel, corporateNamespacePrefix, ins, termList, corporate.getUri(), corporate.getLabelBs(), "bs",  true, status, corporate.getFaotermCode(), alternativeBs, null);
				createAllTerm(owlModel, corporateNamespacePrefix, ins, termList, corporate.getUri(), corporate.getLabelCa(), "ca",  true, status, corporate.getFaotermCode(), alternativeCa, null);
				createAllTerm(owlModel, corporateNamespacePrefix, ins, termList, corporate.getUri(), corporate.getLabelCs(), "cs",  true, status, corporate.getFaotermCode(), alternativeCs, null);				
				createAllTerm(owlModel, corporateNamespacePrefix, ins, termList, corporate.getUri(), corporate.getLabelDa(), "da",  true, status, corporate.getFaotermCode(), alternativeDa, null);
				createAllTerm(owlModel, corporateNamespacePrefix, ins, termList, corporate.getUri(), corporate.getLabelDe(), "de",  true, status, corporate.getFaotermCode(), alternativeDe, null);
				createAllTerm(owlModel, corporateNamespacePrefix, ins, termList, corporate.getUri(), corporate.getLabelFj(), "fj",  true, status, corporate.getFaotermCode(), alternativeFj, null);
				createAllTerm(owlModel, corporateNamespacePrefix, ins, termList, corporate.getUri(), corporate.getLabelHr(), "hr",  true, status, corporate.getFaotermCode(), alternativeHr, null);
				createAllTerm(owlModel, corporateNamespacePrefix, ins, termList, corporate.getUri(), corporate.getLabelHu(), "hu",  true, status, corporate.getFaotermCode(), alternativeHu, null);
				createAllTerm(owlModel, corporateNamespacePrefix, ins, termList, corporate.getUri(), corporate.getLabelId(), "id",  true, status, corporate.getFaotermCode(), alternativeId, null);
				createAllTerm(owlModel, corporateNamespacePrefix, ins, termList, corporate.getUri(), corporate.getLabelLv(), "lv",  true, status, corporate.getFaotermCode(), alternativeLv, null);
				createAllTerm(owlModel, corporateNamespacePrefix, ins, termList, corporate.getUri(), corporate.getLabelMk(), "mk",  true, status, corporate.getFaotermCode(), alternativeMk, null);
				createAllTerm(owlModel, corporateNamespacePrefix, ins, termList, corporate.getUri(), corporate.getLabelMl(), "ml",  true, status, corporate.getFaotermCode(), alternativeMl, null);
				createAllTerm(owlModel, corporateNamespacePrefix, ins, termList, corporate.getUri(), corporate.getLabelMs(), "ms",  true, status, corporate.getFaotermCode(), alternativeMs, null);
				createAllTerm(owlModel, corporateNamespacePrefix, ins, termList, corporate.getUri(), corporate.getLabelNl(), "nl",  true, status, corporate.getFaotermCode(), alternativeNl, null);
				createAllTerm(owlModel, corporateNamespacePrefix, ins, termList, corporate.getUri(), corporate.getLabelNo(), "no",  true, status, corporate.getFaotermCode(), alternativeNo, null);
				createAllTerm(owlModel, corporateNamespacePrefix, ins, termList, corporate.getUri(), corporate.getLabelPl(), "pl",  true, status, corporate.getFaotermCode(), alternativePl, null);
				createAllTerm(owlModel, corporateNamespacePrefix, ins, termList, corporate.getUri(), corporate.getLabelPo(), "pt",  true, status, corporate.getFaotermCode(), alternativePo, null);
				createAllTerm(owlModel, corporateNamespacePrefix, ins, termList, corporate.getUri(), corporate.getLabelPt(), "pt",  true, status, corporate.getFaotermCode(), alternativePt, null);
				createAllTerm(owlModel, corporateNamespacePrefix, ins, termList, corporate.getUri(), corporate.getLabelRo(), "ro",  true, status, corporate.getFaotermCode(), alternativeRo, null);
				createAllTerm(owlModel, corporateNamespacePrefix, ins, termList, corporate.getUri(), corporate.getLabelSk(), "sk",  true, status, corporate.getFaotermCode(), alternativeSk, null);
				createAllTerm(owlModel, corporateNamespacePrefix, ins, termList, corporate.getUri(), corporate.getLabelSl(), "sl",  true, status, corporate.getFaotermCode(), alternativeSl, null);
				createAllTerm(owlModel, corporateNamespacePrefix, ins, termList, corporate.getUri(), corporate.getLabelSr(), "sr",  true, status, corporate.getFaotermCode(), alternativeSr, null);
				createAllTerm(owlModel, corporateNamespacePrefix, ins, termList, corporate.getUri(), corporate.getLabelSv(), "sv",  true, status, corporate.getFaotermCode(), alternativeSv, null);
				createAllTerm(owlModel, corporateNamespacePrefix, ins, termList, corporate.getUri(), corporate.getLabelTr(), "tr",  true, status, corporate.getFaotermCode(), alternativeTr, null);
				createAllTerm(owlModel, corporateNamespacePrefix, ins, termList, corporate.getUri(), corporate.getLabelUk(), "uk",  true, status, corporate.getFaotermCode(), alternativeUk, null);


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
				if(corporate.getIsSpatiallyIncludedInCountryUri()!=null && !corporate.getIsSpatiallyIncludedInCountryUri().equals("") && corporate.getIsSpatiallyIncludedInCountry()!=null && !corporate.getIsSpatiallyIncludedInCountry().equals(""))
				{
					countryIns = createGeopPoliticalConcept(owlModel, corporate.getIsSpatiallyIncludedInCountryUri(), corporate.getIsSpatiallyIncludedInCountry());
					ProtegeUtil.addConcept2ConceptRelationship(owlModel, ModelConstants.RISSPATIALLYINCLUDEDIN, ins, countryIns);
				}
			
				//Add City
				ProtegeUtil.addDatatypeProperty(owlModel, ins, ModelConstants.RISSPATIALLYINCLUDEDINCITY, corporate.getIsSpatiallyIncludedInCity());
				
				//Add State
				ProtegeUtil.addDatatypeProperty(owlModel, ins, ModelConstants.RISSPATIALLYINCLUDEDINSTATE, corporate.getIsSpatiallyIncludedInState());
	
			
			}
			else
			{
				System.out.println("Empty corporate: "+corporate.getUri());
			}
		}
	
		// Add relationship isPartOf between the concepts
		for(Corporate corporate:list)
		{
			System.out.println("-------------------------------------------------");
			corporate.setUri(corporate.getUri().replaceFirst("http://aims.fao.org/aos/corporatebody/c_", ""));
			corporate.setIsPartOfURI1(corporate.getIsPartOfURI1().replaceFirst("http://aims.fao.org/aos/corporatebody/c_", ""));
			corporate.setIsPartOfURI2(corporate.getIsPartOfURI2().replaceFirst("http://aims.fao.org/aos/corporatebody/c_", ""));
			System.out.println("Corporate URI: "+corporate.getUri());
			
			createIsPartOfRelationship(owlModel, corporate.getUri(), corporate.getIsPartOfURI1());
			createIsPartOfRelationship(owlModel, corporate.getUri(), corporate.getIsPartOfURI1());
		}
		
	}
	
	public static void createAllTerm(OWLModel owlModel, String prefix, OWLIndividual ins, ArrayList<OWLIndividual> termList, String id, String label, String lang, boolean isMainLabel, String status, String code, String[] alternatives, String acronyms)
	{
		if(label!=null && !label.equals(""))
		{
			OWLIndividual term = createTerm(owlModel, prefix, ins, id, label, lang, isMainLabel, status, code);
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
		if(code!=null && !code.equals(""))
		ProtegeUtil.addDatatypeProperty(owlModel, term, ModelConstants.RHASCODEFAOTERM, code);
		return term;
	}

	public static void createAlternativeTerm(OWLModel owlModel, String namespacePrefix, OWLIndividual ins, OWLIndividual preferredTerm, String label, String lang, boolean isMainlabel, String status)
	{
		if(label!=null  && !label.equals(""))
		{
			ProtegeUtil.addSpellingVariation(owlModel, preferredTerm, label, lang, ins);
		}
	}
	
	public static void createAcronymTerm(OWLModel owlModel, String namespacePrefix, OWLIndividual ins, OWLIndividual preferredTerm, String label, String lang, boolean isMainlabel, String status)
	{
		if(label!=null  && !label.equals(""))
		{
			OWLIndividual destTerm = ProtegeUtil.createTerm(owlModel, namespacePrefix, ins, label, label, lang, isMainlabel, status);
			ProtegeUtil.addTermRelationship(owlModel, ModelConstants.RHASACRONYM, preferredTerm, destTerm, ins);
		}
	}
	
	public static void createCorporateConcept(OWLModel owlModel){
		String name = "corporate";
		String label = "corporate";	
		String lang = "en";
		
		OWLIndividual ins = ProtegeUtil.createConcept(owlModel, ModelConstants.CDOMAINCONCEPT, corporateNamespacePrefix, name, "c_", status);
		ProtegeUtil.createTerm(owlModel, corporateNamespacePrefix, ins, name, label, lang, true, status);
	}
	
	public static void createCountryConcept(OWLModel owlModel){
		String name = "country";
		String label = "country";	
		String lang = "en";
		
		OWLIndividual ins = ProtegeUtil.createConcept(owlModel, ModelConstants.CDOMAINCONCEPT, countryNamespacePrefix, name, "c_", status);
		ProtegeUtil.createTerm(owlModel, countryNamespacePrefix, ins, name, label, lang, true, status);
	}
	
	public static OWLIndividual createGeopPoliticalConcept(OWLModel owlModel, String uri, String label){
		String name = uri.replaceAll("http://aims.fao.org/aos/geopolitical.owl#", "");
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
	
	public static void createIsPartOfRelationship(OWLModel owlModel, String insName, String destInsName)
	{
		if(destInsName!=null && !destInsName.equals(""))
		{
			
			OWLIndividual ins = ProtegeUtil.getConceptInstance(owlModel, owlModel.getOWLNamedClass(corporateNamespacePrefix+":"+"c_"+insName));
			if(ins!=null)
			{
				OWLIndividual destIns = ProtegeUtil.getConceptInstance(owlModel, owlModel.getOWLNamedClass(corporateNamespacePrefix+":"+"c_"+destInsName));
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
	
	public static boolean isEmpty(Corporate corporateBodies)
	{
		return (corporateBodies.getLabelEn().equals("") && 
				corporateBodies.getLabelEnNonPreferredTerm().equals("") && 
				corporateBodies.getLabelFr().equals("") && 
				corporateBodies.getLabelEs().equals("") && 
				corporateBodies.getLabelAr().equals("") && 
				corporateBodies.getLabelZh().equals("") && 
				corporateBodies.getLabelRu().equals("") && 
				corporateBodies.getLabelIt().equals("") && 
				corporateBodies.getLabelBn().equals("") && 
				corporateBodies.getLabelBs().equals("") && 
				corporateBodies.getLabelCa().equals("") && 
				corporateBodies.getLabelCs().equals("") && 
				corporateBodies.getLabelDa().equals("") && 
				corporateBodies.getLabelDe().equals("") && 
				corporateBodies.getLabelFj().equals("") && 
				corporateBodies.getLabelHr().equals("") && 
				corporateBodies.getLabelHu().equals("") && 
				corporateBodies.getLabelId().equals("") && 
				corporateBodies.getLabelLv().equals("") && 
				corporateBodies.getLabelMk().equals("") && 
				corporateBodies.getLabelMl().equals("") && 
				corporateBodies.getLabelMs().equals("") && 
				corporateBodies.getLabelNl().equals("") && 
				corporateBodies.getLabelNo().equals("") && 
				corporateBodies.getLabelPl().equals("") && 
				corporateBodies.getLabelPo().equals("") && 
				corporateBodies.getLabelPt().equals("") && 
				corporateBodies.getLabelRo().equals("") && 
				corporateBodies.getLabelSk().equals("") && 
				corporateBodies.getLabelSl().equals("") && 
				corporateBodies.getLabelSr().equals("") && 
				corporateBodies.getLabelSv().equals("") && 
				corporateBodies.getLabelTr().equals("") && 
				corporateBodies.getLabelUk().equals("")
		);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Corporate> loadCorporateBodiesData(String filename) throws FileNotFoundException
	{
		ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
		strat.setType(Corporate.class);
		//String[] columns = new String[] {"uri","labelEn","labelEs","labelFr","labelZh","labelAr","labelRu","labelIt","labelPt","labelRo","labelPl","labelTr","labelNl","labelHu","labelCa","labelId","labelDe","labelSv","labelMs","labelNo","labelUk","labelDa","labelFj","labelSl","labelBs","labelCs","labelHr","labelSk","labelSr","labelMk","labelMl","isComponentOf1","isComponentOfUri1","isComponentOf2","isComponentOfUri2","isSpatiallyIncludedIn","isSpatiallyIncludedInCity","isSpatiallyIncludedInState","isSpatiallyIncludedInUri","faotermCode","obsolete","acronymEn","acronymFr","acronymEs","alternativeEn1","alternativeEn2","alternativeEn3","alternativeEn4","alternativeEn5","alternativeFr1","alternativeFr2","alternativeFr3","alternativeFr4","alternativeEs1","alternativeEs2","alternativeEs3","alternativeEs4","alternativeEs5","alternativeEs6","alternativeEs7","alternativeEs8","alternativeEs9","alternativeEs10","alternativeEs11","alternativeEs12","alternativeEs13","alternativeEs14","alternativeEs15","alternativeIt1","alternativeIt2","alternativePt1","alternativeRo","alternativePl","alternativeTr","alternativeNl","alternativeHu","alternativeCa","alternativeId","alternativeDe","alternativeSv","alternativeMs","alternativeNo","alternativeUk","alternativeDa","alternativeFj","alternativeSl","alternativeBs","alternativeCs","alternativeHr","alternativeSk","alternativeSr","alternativeMk","alternativeMl"}; // the fields to bind do in your JavaBean
		String[] columns = new String[] {"uri","labelEn", "labelEnNonPreferredTerm","labelFr","labelEs","labelAr","labelZh","labelRu","labelIt","labelBn","labelBs","labelCa","labelCs","labelDa","labelDe","labelFj","labelHr","labelHu","labelId","labelLv","labelMk","labelMl","labelMs","labelNl","labelNo","labelPl","labelPo","labelPt","labelRo","labelSk","labelSl","labelSr","labelSv","labelTr","labelUk","acronymEn","acronymFr","acronymEs","acronymAr","acronymZh","acronymRu","acronymIt","obsolete","isPartOf1","isPartOfURI1","isPartOf2","isPartOfURI2","isSpatiallyIncludedInCountry","isSpatiallyIncludedInCountryUri","isSpatiallyIncludedInCity","isSpatiallyIncludedInState","faotermCode","alternativeEn1","alternativeEn2","alternativeEn3","alternativeEn4","alternativeEn5","alternativeFr1","alternativeFr2","alternativeFr3","alternativeFr4","alternativeFr5","alternativeEs1","alternativeEs2","alternativeEs3","alternativeEs4","alternativeEs5","alternativeEs6","alternativeEs7","alternativeEs8","alternativeEs9","alternativeEs10","alternativeEs11","alternativeEs12","alternativeEs13","alternativeEs14","alternativeEs15","alternativeEs16","alternativeAr","alternativeZh","alternativeRu","alternativeIt1","alternativeIt2","alternativeIt3","alternativeIt4","alternativeBn","alternativeBs","alternativeCa","alternativeCs","alternativeDa","alternativeDe1","alternativeDe2","alternativeFj","alternativeHr","alternativeHu","alternativeId","alternativeLv","alternativeMk","alternativeMl","alternativeMs","alternativeNl","alternativeNo","alternativePl","alternativePo","alternativePt1","alternativeRo","alternativeSk","alternativeSl","alternativeSr","alternativeSv","alternativeTr","alternativeUk"}; // the fields to bind do in your JavaBean
		strat.setColumnMapping(columns);

		CsvToBean csv = new CsvToBean();
		List<Corporate> list = csv.parse(strat, new FileReader(filename));

		return list;
	}
}
