package org.fao.aoscs.server;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;

import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.gwt.GwtConfigurationHelper;
import net.sf.gilead.gwt.PersistentRemoteService;
import net.sf.gilead.pojo.gwt.LightEntity;

import org.fao.aoscs.client.module.export.service.ExportService;
import org.fao.aoscs.domain.ExportParameterObject;
import org.fao.aoscs.domain.InitializeExportData;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.RecentChangeData;
import org.fao.aoscs.hibernate.DatabaseUtil;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.server.export.ExportSQL;
import org.fao.aoscs.server.export.ExportToSKOS;
import org.fao.aoscs.server.protege.ProtegeModelFactory;

import edu.stanford.smi.protegex.owl.model.OWLModel;

public class ExportServiceImpl extends PersistentRemoteService implements ExportService {
	
	private static final long serialVersionUID = 4448572872961275047L;
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
	
	public InitializeExportData initData(OntologyInfo ontoInfo){
	    InitializeExportData data = new InitializeExportData();
	    try{
    		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
    		data.setScheme(new SearchServiceImpl().getScheme(owlModel));
    		data.setTermCodeProperties(new SearchServiceImpl().getAllTermCodeProperties(owlModel));
    		///owlModel.dispose();
	    }catch(Exception e){
	        e.printStackTrace();
	    }
		return data;
	}
	
	public String getExportData(ExportParameterObject exp,OntologyInfo ontoInfo)
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
		
	
		String data = null;
		/*System.out.println("serv concepturi = "+concepturi);
		System.out.println("serv datetype = "+datetype);
		System.out.println("serv datestart = "+datestart);
		System.out.println("serv dateend = "+dateend);
		
		System.out.println("serv expformat = "+expformat);
		System.out.println("serv scheme = "+scheme);
		System.out.println("serv subvocab = "+subvocab);
		System.out.println("serv termcode = "+termcode + " startcode = "+startcode);
		
		System.out.println("langlist status = "+exp.isLangListEmpty());*/
		
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
		
		try{
			if(expformat.equals("SKOS")) 
			{
				//data = ExportSKOS.ExportToSKOS(ontoInfo,criterialist);
				data = ExportToSKOS.getExportSKOS(exp, ontoInfo);
			}
			if(expformat.equals("RDBMS_SQL_FORMAT")) 
			{
				//data = ExportToSQL.ExportToSQL(ontoInfo,criterialist);
		        data = ExportSQL.getExportSQL(exp, ontoInfo);    
			}   
	    }
		catch(Exception e)
		{
	        e.printStackTrace();
	        
	    }
		return data;
	}
	
	public HashMap<String, String> getExportDataAndFilename(ExportParameterObject exp, OntologyInfo ontoInfo)
	{
		String data = getExportData(exp, ontoInfo);
		HashMap<String, String> hMap = new HashMap<String, String>();
		String key = "agrovocexport"+this.getThreadLocalRequest().getSession().getId();
		this.getThreadLocalRequest().getSession().setAttribute(key, data);
		hMap.put(key, data);
		return hMap;
	}
	
	public void markExportAsRecentChange(ExportParameterObject exp,int userId, int actionId, OntologyInfo ontoInfo)
	{
		
		RecentChangeData rcData = new RecentChangeData();
		ArrayList<LightEntity> obj = new ArrayList<LightEntity>();
		obj.add(exp);
		rcData.setObject(obj);
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());	
	
	}
}
