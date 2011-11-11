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

public class ProjectsImport {
	
	private static final String ADDRESS_FILE="/Sachit/workspace/aos_cs_1_v_1_0_2/csv/20100322-Projects.csv";
	private static String status = "Published";
	private static String projectNamespace = ModelConstants.BASENAMESPACE+"fao_project"+ModelConstants.NAMESPACESEPARATOR;
	private static String projectNamespacePrefix = "project";
	private static String countryNamespace = ModelConstants.BASENAMESPACE+"country"+ModelConstants.NAMESPACESEPARATOR;
	private static String countryNamespacePrefix = "country";
	private static String geopoliticalNamespace = "http://aims.fao.org/aos/geopolitical.owl#";
	private static String geopoliticalNamespacePrefix = "geo";

	private final static String driver = "com.mysql.jdbc.Driver";
	private final static String dbIp = "localhost";
	private final static String dbPort = "3306";
	private final static String dbName = "agrovocwb_ac_projects_v_1_0";
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
			
			projectNamespace = ModelConstants.BASENAMESPACE+"fao_project"+ModelConstants.NAMESPACESEPARATOR;
			countryNamespace = ModelConstants.BASENAMESPACE+"country"+ModelConstants.NAMESPACESEPARATOR;
			
			owlModel.getNamespaceManager().setPrefix(projectNamespace, projectNamespacePrefix);
			owlModel.getNamespaceManager().setPrefix(countryNamespace, countryNamespacePrefix);
			owlModel.getNamespaceManager().setPrefix(geopoliticalNamespace, geopoliticalNamespacePrefix);

			//ImportUtil.printNameSpaces(owlModel);
			
			createProjectConcept(owlModel);
			createCountryConcept(owlModel);
			
			convert(owlModel, loadProjectData(ADDRESS_FILE));
			
			owlModel.dispose();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void convert(OWLModel owlModel, List<Projects> list)
	{
		int cnt=0;
		for(Projects project:list)
		{
			cnt++;
			if(cnt==1) continue;
			//if(cnt>100) break;
			System.out.println("-------------------------------------------------");
			String id = project.getId().replaceFirst("http://www.fao.org/aims/aos/project/c_", "");
			project.setId(id);
			System.out.println("Project ID: "+project.getId());
			
			boolean chk = isEmpty(project);
			if(!chk)
			{
			
				OWLIndividual ins = ProtegeUtil.createConcept(owlModel, projectNamespacePrefix+":c_project", projectNamespacePrefix, project.getId(), "c_", status);
				ArrayList<OWLIndividual> termList = new ArrayList<OWLIndividual>();  
				
				String[] alternativeEn = {project.getHasSpellingVariantEn()};
				String[] alternativeFr = {project.getHasSpellingVariantFr()};
				String[] alternativeEs = {project.getHasSpellingVariantFr()};
				
				createAllTerm(owlModel, projectNamespacePrefix, ins, termList, project.getId(), project.getLabelEn(), "en", status, null, alternativeEn, null);
				createAllTerm(owlModel, projectNamespacePrefix, ins, termList, project.getId(), project.getLabelEs(), "es", status, null, alternativeEs, null);
				createAllTerm(owlModel, projectNamespacePrefix, ins, termList, project.getId(), project.getLabelFr(), "fr", status, null, alternativeFr, null);
				createAllTerm(owlModel, projectNamespacePrefix, ins, termList, project.getId(), project.getLabelPt(), "pt", status, null, null, null);
				
				
				
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
				if(project.getIsSpatiallyIncludedInUri()!=null && !project.getIsSpatiallyIncludedInUri().equals("") && project.getIsSpatiallyIncludedIn()!=null && !project.getIsSpatiallyIncludedIn().equals(""))
				{
					countryIns = createGeopPoliticalConcept(owlModel, project.getIsSpatiallyIncludedInUri(), project.getIsSpatiallyIncludedIn());
					ProtegeUtil.addConcept2ConceptRelationship(owlModel, ModelConstants.RISSPATIALLYINCLUDEDIN, ins, countryIns);
				}
				
				//Add Number
				ProtegeUtil.addDatatypeProperty(owlModel, ins, ModelConstants.RHASNUMBER, project.getProjectCode());
				
			}
			else
			{
				System.out.println("Empty projects: "+project.getId());
			}
		}
	}
	
	public static void createAllTerm(OWLModel owlModel, String prefix, OWLIndividual ins, ArrayList<OWLIndividual> termList, String id, String label, String lang, String status, String code, String[] alternatives, String acronyms)
	{
		if(label!=null && !label.equals(""))
		{
			OWLIndividual term = createPreferredTerm(owlModel, prefix, ins, id, label, lang, true, status, code);
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
	
	public static OWLIndividual createPreferredTerm(OWLModel owlModel, String namespacePrefix, OWLIndividual ins, String name, String label, String lang, boolean isMainlabel, String status, String code)
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
	
	public static void createProjectConcept(OWLModel owlModel){
		String name = "project";
		String label = "project";	
		String lang = "en";
		
		OWLIndividual ins = ProtegeUtil.createConcept(owlModel, ModelConstants.CDOMAINCONCEPT, projectNamespacePrefix, name, "c_", status);
		ProtegeUtil.createTerm(owlModel, projectNamespacePrefix, ins, name, label, lang, true, status);
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
	
	public static boolean isEmpty(Projects project)
	{
		return ( project.getLabelEn().equals("") && 
				project.getLabelEs().equals("") && 
				project.getLabelFr().equals("") && 
				project.getLabelPt().equals("")
		);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Projects> loadProjectData(String filename) throws FileNotFoundException
	{
		ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
		strat.setType(Projects.class);
		//String[] columns = new String[] {"id","projectCode","labelEn","labelFr","labelEs","labelPt","isSpatiallyIncludedIn","isSpatiallyIncludedInUri"}; // the fields to bind do in your JavaBean
		String[] columns = new String[] {"id","projectCode","labelEn","labelFr","labelEs","labelPt","isSpatiallyIncludedIn","isSpatiallyIncludedInUri","hasSpellingVariantEn","hasSpellingVariantFr","hasSpellingVariantEs"}; // the fields to bind do in your JavaBean
		
		strat.setColumnMapping(columns);

		CsvToBean csv = new CsvToBean();
		List<Projects> list = csv.parse(strat, new FileReader(filename));

		return list;
	}
}
