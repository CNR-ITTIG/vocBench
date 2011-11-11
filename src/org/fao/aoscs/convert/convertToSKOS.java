package org.fao.aoscs.convert;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.domain.ExportParameterObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.server.export.ExportSKOS;
import org.fao.aoscs.server.export.ExportToSQL;

public class convertToSKOS {

public static void main(String args[]) {
		
		try 
		{
			ModelConstants.loadConstants(convertToSKOS.loadModelConstants());
			
			System.out.println("Starting...");
			
			OntologyInfo ontoInfo = new OntologyInfo();
			ontoInfo.setDbDriver("com.mysql.jdbc.Driver");
			ontoInfo.setDbUrl("jdbc:mysql://158.108.33.132:3306/agrovocwb_en_it_v_0_3_beta?requireSSL=false&useUnicode=true&characterEncoding=UTF-8");			
			ontoInfo.setDbTableName("agrovocwb_en_it_v_0_3_beta");
			ontoInfo.setDbUsername("root");
			ontoInfo.setDbPassword("onmodao");
			
			String conceptURI = "http://www.fao.org/aims/aos/agrovoc#c_992";
			ExportParameterObject exp = new ExportParameterObject();
			exp.setConceptURI(conceptURI);
			exp.setFormat("SKOS");
			String str = convertToSKOS.getExportData(exp, ontoInfo);
			try 
			{
		        BufferedWriter out = new BufferedWriter(new FileWriter("d:/agrovocwv_en_it.rdf"));
		        out.write(str);
		        out.close();
		    } 
			catch (IOException e) 
			{
		    	e.printStackTrace();
		    }
		    System.out.println("Completed...");
			
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

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
	
	public static String getExportData(ExportParameterObject exp,OntologyInfo ontoInfo)
	{
		// sub vocaburary,concept,format,date(create,update),termcode,scheme
		String concepturi = "";
		String subvocab = "";
		String expformat = "";
		String datetype = "";
		String datestart = "";
		String dateend = "";
		
		String termcode = "";
		String startcode = "";
		String scheme = "";
		ArrayList<String> explang = new ArrayList<String>();
		
		if(!exp.isConceptURI()) concepturi = exp.getConceptURI(); 
		if(!exp.isDateTypeEmpty()) datetype = exp.getDateType(); else datetype = "create";
		if(!exp.isStartDateEmpty()) datestart = exp.getStartDate();
		if(!exp.isEndDateEmpty()) dateend = exp.getEndDate();
		
		if(!exp.isFormatEmpty())expformat  = exp.getExportFormat() ;
		if(!exp.isSchemeURIEmpty()) scheme = exp.getSchemeURI() ;
		if(!exp.isTermCodeEmpty()){
			termcode = exp.getTermCodeRepositoryURI();	
			startcode = exp.getStartCode() ;
		}
		
		if(datetype!= null) datetype = "";
		if(datestart!= null) datestart = "";
		if(dateend!= null) dateend = "";
		
		String data = "No Export Data ";
		if(!exp.isLangListEmpty()){
			explang = exp.getExpLanguage();
		} 
	
		ArrayList<String> criterialist = new ArrayList<String>();
		criterialist.add(concepturi);
		criterialist.add(datetype);
		criterialist.add(datestart);
		criterialist.add(dateend);
		criterialist.add(scheme);
		criterialist.add(subvocab);
		criterialist.add(termcode);
		criterialist.add(startcode);
		if(explang.size()==0)
		{
			criterialist.add(null);	
		}
		else
		{
			criterialist.addAll(explang);	
		}
	
		if(expformat.equals("SKOS")) 
		{
			data = ExportSKOS.ExportToSKOS(ontoInfo,criterialist);
		}
		if(expformat.equals("RDBMS_SQL_FORMAT")) 
		{
			data = ExportToSQL.getExportSQL(ontoInfo,criterialist);
		}
		
		return data;
	}
}
