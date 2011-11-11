package org.fao.aoscs.server.index;

import it.uniroma2.art.ont_indexer.OntologyIndexer;
import it.uniroma2.art.ont_indexer.index.IndexManager;
import it.uniroma2.art.owlart.model.ARTURIResource;
import it.uniroma2.art.owlart.models.OWLModel;
import it.uniroma2.art.owlart.vocabulary.RDFS;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.domain.LanguageCode;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.server.SearchServiceImplOWLART;
import org.fao.aoscs.server.SystemServiceImpl;
import org.fao.aoscs.server.owlart.ModelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexingEngineFactory {

	protected static Logger logger = LoggerFactory.getLogger(IndexingEngineFactory.class);
	
	static HashMap<String, OntologyIndexer> searchEngineMap;
	static {
		searchEngineMap = new HashMap<String, OntologyIndexer>();
	}
	
	static OntologyIndexer createSearchEngine(OntologyInfo ontoInfo) {
		logger.info("initializing indexing engine");
		OWLModel owlArtModel = ModelFactory.getOWLModel(ontoInfo);
		String indexLocation = ConfigConstants.INDEXDATASERVERPATH+"/data/" + ontoInfo.getModelID() + "/index";
		ArrayList<String> URILangs = new ArrayList<String>();
		//URILangs.add("en");
		ArrayList<String> langs = new ArrayList<String>();
		ArrayList<LanguageCode> lngscodes = new SystemServiceImpl().loadLanguage();
		for (LanguageCode langcode : lngscodes) {
			langs.add(langcode.getLanguageCode().toLowerCase());
		}
		logger.debug("working directory is: " + System.getProperty("user.dir") );
		logger.debug("indexed languages are: " +  langs);
		OntologyIndexer oi;
		try {
			oi = new OntologyIndexer(owlArtModel, indexLocation, langs, URILangs);
			ArrayList<String> indexes = new ArrayList<String>();
			indexes.add(SearchServiceImplOWLART.c_nounInstancesIndexCategory);
			indexes.add(SearchServiceImplOWLART.index_definition);
			indexes.add(SearchServiceImplOWLART.index_ImgDescription);
			indexes.add(SearchServiceImplOWLART.index_scopeNotes);
			
			logger.debug("initializing AGROVOC Concept Server index writers");
			oi.initializeCustomIndexWriters(indexes);
			logger.debug("index writers initialized");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("exception: " + e);
			oi = null;
		}
		logger.debug("onto indexer ready");
		return oi;
	}
	
	public static synchronized OntologyIndexer getSearchEngine(OntologyInfo ontoInfo) {
		OntologyIndexer oi = searchEngineMap.get("" + ontoInfo.getOntologyId());
		if (oi != null) {
			logger.info("search engine got from search engine Map");
			return oi;
		} else {
			logger.info("search engine  not present in search engine Map, generating it from owlModel");
			oi = createSearchEngine(ontoInfo);
			searchEngineMap.put("" + ontoInfo.getOntologyId(), oi);
			return oi;
		}
	}
	
	public static synchronized boolean reloadSearchEngine(OntologyInfo ontoInfo) {
		try
		{
			searchEngineMap.remove("" + ontoInfo.getOntologyId());
			OntologyIndexer oi = createSearchEngine(ontoInfo);
			searchEngineMap.put("" + ontoInfo.getOntologyId(), oi);
			logger.info("search engine reloaded in search engine Map");
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	public static synchronized void removeSearchEngine(OntologyInfo ontoInfo) {
		OntologyIndexer oi = IndexingEngineFactory.getSearchEngine(ontoInfo);
		if (oi != null) {
			try
			{
				searchEngineMap.remove("" + ontoInfo.getOntologyId());
				recursiveDelete(new File(oi.getIndexManager().getBaseIndexDir()));
				logger.info("remove search engine from search engine Map");
			}
			catch(Exception e)
			{
				e.printStackTrace();
				logger.info("remove search engine from search engine Map failed");
			}
		} 
		else {
			logger.info("search engine  not present in search engine Map, generating it from owlModel");
		}
	}
	
	public static void recursiveDelete (File dirPath) {
		try{
			if(dirPath.exists()){
			    String [] ls = dirPath.list ();
			    for (int idx = 0; idx < ls.length; idx++) 
			    {
			      File file = new File (dirPath, ls [idx]);
			      if (file.isDirectory ())
			        recursiveDelete (file);
			      file.delete ();
			    }
			    dirPath.delete();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void updateIndex(OntologyInfo ontoInfo, String uri, String lang, String idxIdentifier) 
	{
		try
		{
			OWLModel owlModel = ModelFactory.getOWLModel(ontoInfo);
			OntologyIndexer oi = IndexingEngineFactory.getSearchEngine(ontoInfo);
			
			//This gets the index manager from the OntologyIndexer object		
			IndexManager idxMgr = oi.getIndexManager();
		
			ARTURIResource newResource = owlModel.createURIResource(uri);
									
			//This row tells the indexing engine to add a new resource to the index
			idxMgr.addNewResourceToIndex(newResource, idxIdentifier);
			
			//This row updates the lang eg. index for the above resource. Use it whenever you add/remove labels to any existing concept (you do not need to specify the label info for the indexer, it just updates the info by getting them from the ontology)
			idxMgr.updateLangIndexForResource(newResource, idxIdentifier, lang.toLowerCase());
					
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}
	
	public static void updateIndex(OntologyInfo ontoInfo, String uri, String relURI, String lang, String idxIdentifier) 
	{
		try
		{
			OWLModel owlModel = ModelFactory.getOWLModel(ontoInfo);
			OntologyIndexer oi = IndexingEngineFactory.getSearchEngine(ontoInfo);
			
			//This gets the index manager from the OntologyIndexer object		
			IndexManager idxMgr = oi.getIndexManager();
			
			ARTURIResource newResourceOWLART = owlModel.createURIResource(uri);
			ARTURIResource newRelOWLART = owlModel.createURIResource(relURI);
			
			//This row tells the indexing engine to add a new resource to the index
			idxMgr.addNewResourceToIndex(newResourceOWLART, idxIdentifier);
			
			//This row updates the lang eg. index for the above resource. Use it whenever you add/remove labels to any existing concept (you do not need to specify the label info for the indexer, it just updates the info by getting them from the ontology)
			idxMgr.updateLangIndexForResource(newResourceOWLART, newRelOWLART, idxIdentifier, lang.toLowerCase());
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static void updateCommentIndex(OntologyInfo ontoInfo, String uri, String lang, String idxIdentifier) 
	{
		try
		{
			OWLModel owlModel = ModelFactory.getOWLModel(ontoInfo);
			OntologyIndexer oi = IndexingEngineFactory.getSearchEngine(ontoInfo);
			
			//This gets the index manager from the OntologyIndexer object		
			IndexManager idxMgr = oi.getIndexManager();
		
			ARTURIResource newResourceOWLART = owlModel.createURIResource(uri);
									
			//This row tells the indexing engine to add a new resource to the index
			idxMgr.addNewResourceToIndex(newResourceOWLART, idxIdentifier);

			//This row updates the lang eg. index for the above resource. Use it whenever you add/remove labels to any existing concept (you do not need to specify the label info for the indexer, it just updates the info by getting them from the ontology)
			idxMgr.updateLangIndexForResource(newResourceOWLART, RDFS.Res.COMMENT, idxIdentifier, lang.toLowerCase());
			
			
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}
	
	public static void deleteIndex(OntologyInfo ontoInfo, String uri, String idxIdentifier) 
	{
	
		try
		{
			OWLModel owlModel = ModelFactory.getOWLModel(ontoInfo);
			OntologyIndexer oi = IndexingEngineFactory.getSearchEngine(ontoInfo);
			
			//This gets the index manager from the OntologyIndexer object		
			IndexManager idxMgr = oi.getIndexManager();
		
			ARTURIResource newResources = owlModel.createURIResource(uri);
						
			//This single row deletes a resource from all indexes (i.e., if a resource is deleted, all indexes, both the resource index and all language indexes) are being cleaned of the info associated to that resource, so you just need to write this single row
			idxMgr.deleteResourceFromIndexes(newResources, idxIdentifier);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	
	}
}
