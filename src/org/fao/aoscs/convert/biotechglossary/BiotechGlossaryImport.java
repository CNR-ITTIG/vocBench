package org.fao.aoscs.convert.biotechglossary;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.convert.util.ProtegeUtil;

import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLProperty;

public class BiotechGlossaryImport {
	
	private static String GLOSSARY_ADDRESS_FILE="csv/biotechglossary/GLOSSARY2010-11-16-16-24-49-modified.csv";
	private static String DEFINITION_ADDRESS_FILE="csv/biotechglossary/DEFINITION2010-11-16-16-24-46-modified.csv";
	private static String ALTERNATIVETERMS_ADDRESS_FILE="csv/biotechglossary/ALTERNATE_TERMS2010-11-16-16-24-46-modified.csv";
	private static String status = "Published";
	private static String biotechGlossaryNamespace = ModelConstants.BASENAMESPACE+"biotechglossary"+ModelConstants.NAMESPACESEPARATOR;
	private static String biotechGlossaryNamespacePrefix = "biotechglossary";

	private static String driver = "com.mysql.jdbc.Driver";
	private static String dbIp = "localhost";
	private static String dbPort = "3306";
	private static String dbName = "agrovocwb_biotechglossary_v_0_1";
	private static String dbUser = "fao";
  	private static String dbPassword = "faomimos";
  	private static String owlURI = "file:/Users/prashanta/Dev/Workspace/acsw_35169/owl/v1.0/aos.owl";
  	private static String modelConstantsURI = "org.fao.aoscs.server.ModelConstants";
	
	public static void main(String args[]) 
	{				
		if(args.length > 0){
			dbName 		= args[0];
			dbUser 		= args[1];
			dbPassword 	= args[2];
			owlURI 		= args[3];
			
			GLOSSARY_ADDRESS_FILE = args[4];
			DEFINITION_ADDRESS_FILE = args[5];
			ALTERNATIVETERMS_ADDRESS_FILE = args[6];
		}
		init();
		//readInput();
	}
	
	static void readInput() 
	{
		
		String filename = "/Users/prashanta/Documents/workspace/aos_cs_1_v_1_0_2/csv/biotechglossary/ru.txt";
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(filename));
			String s;
			
			while( (s = in.readLine()) != null) {
				System.out.println(s);
				String UTF8Str = new String(s.getBytes("windows-1251"));
				System.out.println(UTF8Str);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
   	
	public static void init(){
		try {
			OWLModel owlModel = ProtegeUtil.getOWLModel(driver, dbIp, dbPort, dbName, dbUser, dbPassword, owlURI, modelConstantsURI);
			
			biotechGlossaryNamespace = ModelConstants.BASENAMESPACE+"biotechglossary"+ModelConstants.NAMESPACESEPARATOR;			
			owlModel.getNamespaceManager().setPrefix(biotechGlossaryNamespace, biotechGlossaryNamespacePrefix);
			
			createBiotechGlossaryConcept(owlModel);
			
			List<Glossary> glossaryData = loadGlossaryData(GLOSSARY_ADDRESS_FILE);
			List<Definition> definitionData = loadDefinitionData(DEFINITION_ADDRESS_FILE);
			List<AltTerm> altTermData = loadAltTermData(ALTERNATIVETERMS_ADDRESS_FILE);
			
			convert(owlModel, glossaryData, definitionData, altTermData);		
			owlModel.dispose();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void checkDuplicates(List<Glossary> glossaryData){
		ArrayList<Glossary> lg = new ArrayList<Glossary>();
		Iterator<Glossary> itr = glossaryData.iterator();
		while(itr.hasNext()){
			Glossary g = itr.next();
			// case1
			if(g.getTermsE().length() <= 2)
				lg.add(g);
			// case2 and case3
			if(g.getTermsE().contains("%") || g.getTermsE().contains("/")  || g.getTermsE().contains("/"))
				lg.add(g);
		}
		
		Iterator<Glossary> itr2 = lg.iterator();
		while(itr2.hasNext()){
			Glossary g = itr2.next();
			System.out.println(g.getIdGlossary() + " -- " + g.getTermsE());
			System.out.println();
		}
		System.out.println("size = " + lg.size());
	}
	
	public static void convert(OWLModel owlModel, List<Glossary> list, List<Definition> listDefinition, List<AltTerm> listAltTerm)
	{
		int cnt=0;
		int tot = list.size();
		//int tot = 20;
		for(Glossary glossary:list)
		{
			String gid = glossary.getIdGlossary();
			cnt++;
			if(cnt==1) continue;
			if(cnt>tot) break;
			if(!isEmpty(glossary))
			{
				String status = "Published";
				// create concept instance
				OWLIndividual ins = ProtegeUtil.createConcept(owlModel, biotechGlossaryNamespacePrefix+":c_biotechglossary", biotechGlossaryNamespacePrefix, gid, "c_", status);
				// Adjust create date
				ins.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED), formatDate(glossary.getDateEntry()));
				System.out.println("-------------------------------------------------");
				System.out.println("["+cnt+"]Concept Created: "+gid);
				
				// create terms in concept
				createAllTerms(owlModel, biotechGlossaryNamespacePrefix, ins, gid, glossary);
					
				// create definitions				
				for(Definition def : listDefinition)
				{
					if(def.getIdGlossary().equals(gid))
					{
						createDefinition(owlModel, biotechGlossaryNamespacePrefix, ins, def.getDefinitionF(), "fr", def.getDateEntry());
						createDefinition(owlModel, biotechGlossaryNamespacePrefix, ins, def.getDefinitionA(), "ar", def.getDateEntry());
						createDefinition(owlModel, biotechGlossaryNamespacePrefix, ins, def.getDefinitionC(), "zh", def.getDateEntry());
						createDefinition(owlModel, biotechGlossaryNamespacePrefix, ins, def.getDefinitionR(), "ru", def.getDateEntry());
						createDefinition(owlModel, biotechGlossaryNamespacePrefix, ins, def.getDefinitionS(), "es", def.getDateEntry());
						createDefinition(owlModel, biotechGlossaryNamespacePrefix, ins, def.getDefinitionE(), "en", def.getDateEntry());						
					}
				}				
				
				// create alternative terms
				int count = 0;
				for(AltTerm term : listAltTerm)
				{				
					if(term.getIdGlossary().equals(gid))
					{
						if(term.getLang() != null){
							createAllTerm(owlModel, biotechGlossaryNamespacePrefix, ins, gid, term.getAlternateTerm(), term.getLang(), false, status, term.getDataEntryDate());
							count++;
						}
						else 
							System.out.println("Lang null for " + term.getAlternateTerm() + " -- for glossary id - " + term.getIdGlossary());
					}
				}
			}
			else
				System.out.println("Empty glossary: "+glossary.getIdGlossary());
			System.out.println("DONE : " + Round((float)(cnt*100)/tot,2) + "%");
		}
	}
	
	public static float Round(float Rval, int Rpl) {
		  float p = (float)Math.pow(10,Rpl);
		  Rval = Rval * p;
		  float tmp = Math.round(Rval);
		  return (float)tmp/p;
	}
	
	public static void createBiotechGlossaryConcept(OWLModel owlModel){
		String name = "biotechglossary";
		String label = "biotechglossary";	
		String lang = "en";
		
		OWLIndividual ins = ProtegeUtil.createConcept(owlModel, ModelConstants.CDOMAINCONCEPT, biotechGlossaryNamespacePrefix, name, "c_", status);
		ProtegeUtil.createTerm(owlModel, biotechGlossaryNamespacePrefix, ins, name, label, lang, true, status);
	}
	
	public static OWLIndividual createAllTerms(OWLModel owlModel, String prefix, OWLIndividual ins, String id, Glossary glossary)
	{
		createAllTerm(owlModel, prefix, ins, id, glossary.getTermsE(), "en",  true, status, glossary.getDateEntry());
		createAllTerm(owlModel, prefix, ins, id, glossary.getTermsF(), "fr",  true, status, glossary.getDateEntry());
		createAllTerm(owlModel, prefix, ins, id, glossary.getTermsS(), "es",  true, status, glossary.getDateEntry());
		createAllTerm(owlModel, prefix, ins, id, glossary.getTermsA(), "ar",  true, status, glossary.getDateEntry());
		createAllTerm(owlModel, prefix, ins, id, glossary.getTermsC(), "zh",  true, status, glossary.getDateEntry());
		createAllTerm(owlModel, prefix, ins, id, glossary.getTermsR(), "ru",  true, status, glossary.getDateEntry());		
		return ins;		
	}
	
	public static String formatDate(String date){
		Date d = new Date();
		SimpleDateFormat f1 = new SimpleDateFormat("MMM dd, yyyy h:mm a"); //Mar 22, 2005 9:33 AM
		SimpleDateFormat f2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //2011-02-04 01:50:25
		String ret= "";
		try {
			d = f1.parse(date);
			ret = f2.format(d);
		} catch (ParseException e) {
			System.out.println("Problem with date format");
			e.printStackTrace();
		}
		return ret;
	}
	
	public static void createAllTerm(OWLModel owlModel, String prefix, OWLIndividual ins, String id, String label, String lang, boolean isMainLabel, String status, String createDate)
	{
		String cdate = formatDate(createDate);
		if(label!=null && !label.equals(""))
		{
			OWLIndividual term = createTerm(owlModel, prefix, ins, id, label, lang, isMainLabel, status, cdate);
			System.out.println((isMainLabel? "MAIN" : "ALT") + " TERM created: " + label + "("+lang+")" + " -- id: " + id + " -- prefix: " + prefix + " -- uri: " + term.getURI() + " cdate: " + cdate);			
		}
	}
	
	public static OWLIndividual createTerm(OWLModel owlModel, String namespacePrefix, OWLIndividual ins, String name, String label, String lang, boolean isMainlabel, String status, String createDate)
	{
		OWLIndividual term = ProtegeUtil.createTerm(owlModel, namespacePrefix, ins, name, label, lang, isMainlabel, status);
		if(createDate != null){			
			term.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED), createDate);
			OWLProperty updateDate = owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE);
			ins.setPropertyValue(updateDate, createDate);
			term.setPropertyValue(updateDate, createDate);
		}
		return term;
	}
	
	public static void createDefinition(OWLModel owlModel, String prefix, OWLIndividual ins, String label, String lang, String createDate)
	{
		String cdate = formatDate(createDate);
		if(label!=null && !label.equals(""))
		{	
			System.out.println("ADDED DEF: " + label + "("+lang+") -- created on: " + cdate);
			if(createDate != null){
				ProtegeUtil.createDefinitionWithDate(owlModel, prefix, ins, label, lang, cdate);
			}
			else
				ProtegeUtil.createDefinition(owlModel, prefix, ins, label, lang);
		}
	}
		
	public static void createIsPartOfRelationship(OWLModel owlModel, String insName, String destInsName)
	{
		if(destInsName!=null && !destInsName.equals(""))
		{
			
			OWLIndividual ins = ProtegeUtil.getConceptInstance(owlModel, owlModel.getOWLNamedClass(biotechGlossaryNamespacePrefix+":"+"c_"+insName));
			if(ins!=null)
			{
				OWLIndividual destIns = ProtegeUtil.getConceptInstance(owlModel, owlModel.getOWLNamedClass(biotechGlossaryNamespacePrefix+":"+"c_"+destInsName));
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
	
	public static boolean isEmpty(Glossary glossary)
	{
		return (glossary.getTermsA().equals("") && 
				glossary.getTermsC().equals("") && 
				glossary.getTermsE().equals("") && 
				glossary.getTermsF().equals("") && 
				glossary.getTermsR().equals("") && 
				glossary.getTermsS().equals("")
		);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Glossary> loadGlossaryData(String filename) throws FileNotFoundException
	{
		ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();		
		strat.setType(Glossary.class);
		String[] columns = new String[] {"idGlossary", "termsE", "termsF", "termsS", "termsA", "termsC", "idOriginator", "dateEntry", "idLang", "published", "note", "termsR"}; // the fields to bind do in your JavaBean
		strat.setColumnMapping(columns);

		CsvToBean csv = new CsvToBean();
		List<Glossary> list = csv.parse(strat, new FileReader(filename));
		
		System.out.println("Total Glossary items = " + list.size());
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Definition> loadDefinitionData(String filename) throws FileNotFoundException
	{
		ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
		strat.setType(Definition.class);
		String[] columns = new String[] {"idDefinition", "idGlossary", "definitionE", "definitionF",  "definitionS", "definitionA", "definitionC", "referenceE", "referenceF", "referenceS", "referenceA", "dateEntry", "idOriginator", "referenceC", "official", "definitionR",}; // the fields to bind do in your JavaBean
		strat.setColumnMapping(columns);

		CsvToBean csv = new CsvToBean();
		List<Definition> list = csv.parse(strat, new FileReader(filename));
		
		System.out.println("Total Definition items = " + list.size());
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public static List<AltTerm> loadAltTermData(String filename) throws FileNotFoundException
	{
		ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
		strat.setType(AltTerm.class);
		String[] columns = new String[] {"idAlternate", "idGlossary", "alternateTerm", "fieldOfApplication",  "idLang", "idOriginator", "dataEntryDate"}; // the fields to bind do in your JavaBean
		strat.setColumnMapping(columns);

		CsvToBean csv = new CsvToBean();
		List<AltTerm> list = csv.parse(strat, new FileReader(filename));
		
		System.out.println("Total Alt terms = " + list.size());
		return list;
	
	}
}
