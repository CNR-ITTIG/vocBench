package org.fao.aoscs.server.index;

import it.uniroma2.art.ont_indexer.OntologyIndexer;
import it.uniroma2.art.owlart.exceptions.ModelAccessException;
import it.uniroma2.art.owlart.exceptions.ModelCreationException;
import it.uniroma2.art.owlart.model.ARTURIResource;
import it.uniroma2.art.owlart.models.impl.URIResourceIteratorFilteringResourceIterator;
import it.uniroma2.art.owlart.navigation.ARTURIResourceIterator;
import it.uniroma2.art.owlart.protegeimpl.factory.ARTModelFactoryProtegeImpl;
import it.uniroma2.art.owlart.protegeimpl.models.OWLModelProtegeImpl;
import it.uniroma2.art.owlart.vocabulary.RDFS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.domain.LanguageCode;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.server.SearchServiceImplOWLART;
import org.fao.aoscs.server.SystemServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OfflineIndexer {

	protected static Logger logger = LoggerFactory.getLogger(IndexingEngineFactory.class);

	OntologyIndexer oi;
	OWLModelProtegeImpl owlArtModel;

	/**
	 * since there is no online OntoInfo model here available, you have to write the modelID yourself with
	 * this offline method. modelID is equal to: getOntologyId()+"_"+getDbTableName();.<br/>
	 * For example: 8_agrovoc_wb_new<br/>
	 * 
	 * 
	 * @param modelIDString
	 * @throws ModelCreationException
	 */
	public OfflineIndexer(OntologyInfo ontoInfo, String indexFolderPath) throws ModelCreationException {

		logger
				.info("necessary reinitialization of some ConceptServer data structures for offline processing");
		loadConfigConstants();
		loadModelConstants();

		logger.info("initializing model");
		ARTModelFactoryProtegeImpl fact = new ARTModelFactoryProtegeImpl();
		owlArtModel = (OWLModelProtegeImpl) fact.loadOWLModel(ontoInfo.getDbDriver(), ontoInfo.getDbUrl(),
				ontoInfo.getDbTableName(), ontoInfo.getDbUsername(), ontoInfo.getDbPassword());

		logger.info("initializing indexing engine");

		String indexLocation = indexFolderPath + "/data/" + ontoInfo.getModelID() + "/index";
		// no need to delete. Just delete manually if you have to recreate the index from scratch
		// better to save the index if you just want to access it
		// IndexingEngineFactory.recursiveDelete(new File(indexLocation));
		System.out.println("#### index location = " + indexLocation);

		ArrayList<String> URILangs = new ArrayList<String>();
		URILangs.add("en");
		ArrayList<String> langs = new ArrayList<String>();
		ArrayList<LanguageCode> lngscodes = new SystemServiceImpl().loadLanguage();
		for (LanguageCode langcode : lngscodes) {
			langs.add(langcode.getLanguageCode().toLowerCase());
		}
		logger.debug("working directory is: " + System.getProperty("user.dir"));
		logger.debug("indexed languages are: " + langs);
		try {
			oi = new OntologyIndexer(owlArtModel, indexLocation, langs, URILangs);
			logger.debug("initializing AGROVOC Concept Server index writers");

			ArrayList<String> indexes = new ArrayList<String>();
			indexes.add(SearchServiceImplOWLART.c_nounInstancesIndexCategory);
			indexes.add(SearchServiceImplOWLART.index_definition);
			indexes.add(SearchServiceImplOWLART.index_ImgDescription);
			indexes.add(SearchServiceImplOWLART.index_scopeNotes);

			oi.initializeCustomIndexWriters(indexes);
			logger.debug("index writers initialized");
			oi.createOntIndexSearcher();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("exception: " + e);
			oi = null;
		}
		logger.debug("onto indexer ready");
	}

	public int indexOntologyCNOUNS() {
		logger.debug("building index: " + SearchServiceImplOWLART.c_nounInstancesIndexCategory);
		ARTURIResource cnoun = owlArtModel.createURIResource(ModelConstants.COMMONBASENAMESPACE
				+ ModelConstants.CNOUN);
		ARTURIResourceIterator it;
		try {
			logger.info("Start building index...");
			it = new URIResourceIteratorFilteringResourceIterator(owlArtModel.listInstances(cnoun, false));
			oi.buildCustomIndex(SearchServiceImplOWLART.c_nounInstancesIndexCategory, it, true);
			it.close();
			logger.info("End building index...");
			return 1;
		} catch (ModelAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return 0;
		}

	}

	/**
	 * gets all the instances of c_definition and indexes all their labels
	 * 
	 * @return
	 */
	public int indexOntologyHasDefinition() {
		String indexName = SearchServiceImplOWLART.index_definition;
		logger.debug("building index: " + indexName);
		ARTURIResource cdefinition = owlArtModel.createURIResource(ModelConstants.COMMONBASENAMESPACE
				+ ModelConstants.CDEFINITION);

		ARTURIResourceIterator it;
		try {
			logger.info("Start building index...");
			it = new URIResourceIteratorFilteringResourceIterator(owlArtModel.listInstances(cdefinition,
					false));
			oi.buildCustomIndex(indexName, it, true);
			it.close();
			logger.info("End building index...");
			return 1;
		} catch (ModelAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return 0;
		}

	}

	/**
	 * retrieves all instances of cImage and indexes the values associated to their hasImageDescription
	 * property
	 * 
	 * @return
	 */
	public int indexOntologyScopeNotes() {
		String indexName = SearchServiceImplOWLART.index_scopeNotes;
		logger.debug("building index: " + indexName);
		ARTURIResource cCategory = owlArtModel.createURIResource(ModelConstants.COMMONBASENAMESPACE
				+ ModelConstants.CCATEGORY);

		ARTURIResource hasScopeNoteProp = owlArtModel.createURIResource(ModelConstants.COMMONBASENAMESPACE
				+ ModelConstants.RHASSCOPENOTE);

		ARTURIResourceIterator it;
		try {
			logger.info("Start building index...");
			// inference is set to true here, since each instance is direct instance of one of the subclasses
			// of c_category
			it = new URIResourceIteratorFilteringResourceIterator(owlArtModel.listInstances(cCategory, true));
			oi.buildCustomIndex(indexName, it, hasScopeNoteProp, true);
			it.close();
			logger.info("End building index...");
			return 1;
		} catch (ModelAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return 0;
		}

	}

	/**
	 * retrieves all instances of c_image and indexes the values associated to their hasImageDescription
	 * property
	 * 
	 * @return
	 */
	public int indexOntologyImgDescription() {
		String indexName = SearchServiceImplOWLART.index_ImgDescription;
		logger.debug("building index: " + indexName);
		ARTURIResource cImage = owlArtModel.createURIResource(ModelConstants.COMMONBASENAMESPACE
				+ ModelConstants.CIMAGE);	
		//ARTURIResource imageDescrProp = owlArtModel.createURIResource(ModelConstants.COMMONBASENAMESPACE + ModelConstants.RHASIMAGEDESCRIPTION);
		ARTURIResourceIterator it;
		try {
			logger.info("Start building index...");
			it = new URIResourceIteratorFilteringResourceIterator(owlArtModel.listInstances(cImage, false));		
			oi.buildCustomIndex(indexName, it, RDFS.Res.COMMENT, true);
			it.close();
			logger.info("End building index...");
			return 1;
		} catch (ModelAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return 0;
		}

	}

	public void testSearchResources(int searchStrategy, String indexName, String word, String lang) {
		Iterator<ARTURIResource> retrievedResources;
		retrievedResources = oi.findResourcesForIndex(indexName, word, lang, searchStrategy).iterator();
		while (retrievedResources.hasNext()) {
			ARTURIResource foundURIResource = retrievedResources.next();
			System.out.println("--------------");
			System.out.println("found uriResource for cname: " + foundURIResource);
			String resName = owlArtModel.getProtegeOWLModel()
					.getResourceNameForURI(foundURIResource.getURI());
			System.out.println("found ProtegeName: " + resName);
			System.out.println("found protege individual: "
					+ owlArtModel.getProtegeOWLModel().getOWLIndividual(resName));
			System.out.println("--------------");
		}
	}

	public void testSearchLabels(int searchStrategy, String indexName, String word, String lang) {
		Iterator<String> retrievedLabels;
		retrievedLabels = oi.findLabelsForIndex(indexName, word, lang, searchStrategy).iterator();
		while (retrievedLabels.hasNext())
			System.out.println(retrievedLabels.next());
	}

	/**
	 * borrowed from SystemServiceImpl
	 * 
	 * @return
	 */
	private static HashMap<String, String> loadConfigConstants() {
		HashMap<String, String> mcMap = new HashMap<String, String>();
		ResourceBundle rb = ResourceBundle.getBundle("Config");
		Enumeration<String> en = rb.getKeys();
		while (en.hasMoreElements()) {
			String key = (String) en.nextElement();
			mcMap.put(key, rb.getString(key));
		}
		ConfigConstants.loadConstants(mcMap);
		return mcMap;
	}

	/**
	 * borrowed from SystemServiceImpl
	 * 
	 * @return
	 */
	public static HashMap<String, String> loadModelConstants() {
		HashMap<String, String> mcMap = new HashMap<String, String>();
		ResourceBundle rb = ResourceBundle.getBundle("org.fao.aoscs.server.ModelConstants");
		Enumeration<String> en = rb.getKeys();
		while (en.hasMoreElements()) {
			String key = (String) en.nextElement();
			mcMap.put(key, rb.getString(key));
		}
		ModelConstants.loadConstants(mcMap);
		return mcMap;
	}

	public static OntologyInfo getOntoInfo(int ontologyId) {
		logger.info("necessary reinitialization of some ConceptServer data structures");
		loadConfigConstants();
		loadModelConstants();
		
		OntologyInfo ontoInfo = new OntologyInfo();
		SystemServiceImpl ssi = new SystemServiceImpl();
		ArrayList<OntologyInfo> ontologyList = ssi.getOntology(ontologyId);
		for (OntologyInfo o : ontologyList) {
			if (o.getOntologyId() == ontologyId) {
				ontoInfo = o;
				break;
			}
		}
		return ontoInfo;
	}

	public static void main(String[] args) {
		try {
			String indexFolderPath = null;
			int ontologyId = 0;
			int cnoun = 0;
			int definition = 0;
			int imgDesc = 0;
			int scopeNotes = 0;
			if (args.length != 6) {
				System.out.println("Usage:\n OfflineIndexer <index-folder-path> <ontology-id> <cnouns> <definition> <image-desciption> <scope-notes> \n" +
					    " index-folder-path : path of folder where index files will be stored \n" +
					    " ontology-id: ontology model to consider. Taken from ontology_info \n" +
					    " cnouns/definition/image-description/scope-notes: if 0: do not consider; if >0: consider for indexing \n");
				
				logger.info("Usage:\n OfflineIndexer <index-folder-path> <ontology-id> <cnouns> <definition> <image-desciption> <scope-notes> \n" +
						    " index-folder-path : path of folder where index files will be stored \n" +
						    " ontology-id: ontology model to consider. Taken from ontology_info \n" +
						    " cnouns/definition/image-description/scope-notes: if 0: do not consider; if >0: consider for indexing \n");
				System.exit(0);
			} else {

				indexFolderPath = args[0];
				ontologyId = Integer.parseInt(args[1]);
				cnoun = Integer.parseInt(args[2]);
				definition = Integer.parseInt(args[3]);
				imgDesc = Integer.parseInt(args[4]);
				scopeNotes = Integer.parseInt(args[5]);
		
				OfflineIndexer offInd = new OfflineIndexer(getOntoInfo(ontologyId), indexFolderPath);

				// comment each single line if you already prepared the index and just want to test
				// retrieve-methods
				if(cnoun>0)
				{
					offInd.indexOntologyCNOUNS();
				}
				if(definition>0)
				{
					offInd.indexOntologyHasDefinition();
				}
				if(imgDesc>0)
				{
					offInd.indexOntologyImgDescription();
				}
				if(scopeNotes>0)
				{
					offInd.indexOntologyScopeNotes();
				}
				
				/*offInd.testSearchLabels(OntIndexSearcher.SIMILARITY_STRATEGY,SearchServiceImplOWLART.index_ImgDescription, "img*", "en");
				offInd.testSearchResources(OntIndexSearcher.SIMILARITY_STRATEGY, SearchServiceImplOWLART.index_ImgDescription, "img*", "en");
				offInd.testSearchResources(OntIndexSearcher.SIMILARITY_STRATEGY, SearchServiceImplOWLART.index_definition, "de*", "en");
				offInd.testSearchResources(OntIndexSearcher.SIMILARITY_STRATEGY, SearchServiceImplOWLART.index_scopeNotes, "sc*", "en");*/
				
				/*offInd.testSearchLabels(OntIndexSearcher.EXACT_STRATEGY,
						SearchServiceImplOWLART.c_nounInstancesIndexCategory, "Afric*", "en");
				offInd.testSearchResources(OntIndexSearcher.SIMILARITY_STRATEGY,
						SearchServiceImplOWLART.c_nounInstancesIndexCategory, "Afric*", "en");
				
				offInd.testSearchLabels(OntIndexSearcher.EXACT_STRATEGY,
						SearchServiceImplOWLART.index_scopeNotes, "Add*", "it");
				offInd.testSearchResources(OntIndexSearcher.EXACT_STRATEGY,
						SearchServiceImplOWLART.index_scopeNotes, "Add*", "it");*/
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
