package org.fao.aoscs.server;

import it.uniroma2.art.ont_indexer.OntologyIndexer;
import it.uniroma2.art.ont_indexer.search.OntIndexSearcher;
import it.uniroma2.art.owlart.exceptions.ModelAccessException;
import it.uniroma2.art.owlart.model.ARTURIResource;
import it.uniroma2.art.owlart.models.OWLModel;
import it.uniroma2.art.owlart.models.impl.URIResourceIteratorFilteringResourceIterator;
import it.uniroma2.art.owlart.navigation.ARTResourceIterator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.client.module.search.widgetlib.ItemSuggestion;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.SearchParameterObject;
import org.fao.aoscs.server.index.IndexingEngineFactory;
import org.fao.aoscs.server.owlart.ModelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

public class SearchServiceImplOWLART {

	public final static String c_nounInstancesIndexCategory = "c_nounInstances";
	public final static String index_scopeNotes = "scopeNotes";
	public final static String index_ImgDescription = "imgDescription";
	public final static String index_definition = "definition";

	protected static Logger logger = LoggerFactory.getLogger(SearchServiceImplOWLART.class);

	/**
	 * this has then to be implemented according to some specifications which are in the original
	 * getSuggestionList (such as the use of ModelConstants.CNOUN...). <br/>
	 * Please note that maybe this change should be required in the OntoIndexer Module as I'm noticing here
	 * that this problem reflects the particular way the AGROVOC ontology is being organized (such as the fact
	 * that there are instances representing some of the classes in the ontology) At the moment, the search
	 * engine is just able to reply according to standard indexed classes
	 * 
	 * @param ontoInfo
	 * @param languages
	 * @param req
	 * @return
	 */
	public static List<Suggestion> getSuggestionList(OntologyInfo ontoInfo, boolean includeNotes, ArrayList<String> languages,
			SuggestOracle.Request req) {
		logger.debug("inside getSuggestionList");
		HashSet<Suggestion> suggestions = new HashSet<Suggestion>(req.getLimit());
		ArrayList<Suggestion> result = new ArrayList<Suggestion>();

		OntologyIndexer oi = IndexingEngineFactory.getSearchEngine(ontoInfo);
		try {
			oi.createOntIndexSearcher();
			for (String lang : languages) {
				// here I should put the equivalent of STARTSWITH (must look in Lucene...)
				oi.setTHRESHOLD(0.3);
				Collection<String> labels = oi.findLabelsForIndex(c_nounInstancesIndexCategory, req
						.getQuery()
						+ "*", lang, OntIndexSearcher.EXACT_STRATEGY);
				if (includeNotes) 
				{
				labels.addAll(oi.findLabelsForIndex(index_definition, req
						.getQuery()
						+ "*", lang, OntIndexSearcher.EXACT_STRATEGY));
				labels.addAll(oi.findLabelsForIndex(index_ImgDescription, req
						.getQuery()
						+ "*", lang, OntIndexSearcher.EXACT_STRATEGY));
				labels.addAll(oi.findLabelsForIndex(index_scopeNotes, req
						.getQuery()
						+ "*", lang, OntIndexSearcher.EXACT_STRATEGY));
				}
				for (String label : labels)
					suggestions.add(new ItemSuggestion(label, label));

				if (suggestions.size() > req.getLimit() - 1) {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} finally {
			result.addAll(suggestions);
		}
		return result;

	}

	/**
	 * should be finalized!
	 * 
	 * @param searchObj
	 * @param ontoInfo
	 * @return
	 */
	public static ArrayList<String> getSearchResults(SearchParameterObject searchObj, OntologyInfo ontoInfo) {
		OWLModel owlModel = ModelFactory.getOWLModel(ontoInfo);

		logger.debug("entering getSearchResults_STARREDIMPL:" + searchObj);
		HashSet<String> tempList = new HashSet<String>();
		ArrayList<String> resultList = new ArrayList<String>();

		// search strategy is always EXACT, and modifiers can still affect it.
		// SIMILARITY is different, it is more like a fuzzy search, for words which are close to the one
		// you put as keyword; it is not currently available in the workbench, though you may decide to
		// include it
		int searchStrategy = OntIndexSearcher.EXACT_STRATEGY;
		
		if (searchObj.getRegex().equals(SearchParameterObject.FUZZY_SEARCH))
		{
			searchStrategy = OntIndexSearcher.SIMILARITY_STRATEGY;
		}
		
		String queryString = obtainQueryString(searchObj);
		
		OntologyIndexer oi = IndexingEngineFactory.getSearchEngine(ontoInfo);
		try {
			oi.createOntIndexSearcher();
			ArrayList<String> languages = searchObj.getSelectedLangauge();
			for (String lang : languages) {

				// CNOUNS SEARCH

				Collection<ARTURIResource> retrievedResources = oi.findResourcesForIndex(
						c_nounInstancesIndexCategory, queryString, lang, searchStrategy);
				for (ARTURIResource foundCNameURIResource : retrievedResources) {

					ARTURIResource hasLexProp = owlModel.createURIResource(ModelConstants.COMMONBASENAMESPACE
							+ ModelConstants.RHASLEXICALIZATION);

					ARTResourceIterator associatedDomainObjects;
					try {
						associatedDomainObjects = owlModel.listSubjectsOfPredObjPair(hasLexProp,
								foundCNameURIResource, true);
						URIResourceIteratorFilteringResourceIterator urifilt = new URIResourceIteratorFilteringResourceIterator(
								associatedDomainObjects);
						while (urifilt.hasNext()) {
							ARTURIResource domainObject = urifilt.next();
							logger.info("domainObject: " + domainObject);
							// String domainObjectName =
							// owlModel.getProtegeOWLModel().getResourceNameForURI(domainObject.getURI());
							// logger.info("domain object ProtegeName: " + domainObjectName);
							// logger.info("found protege individual: " +
							// owlModel.getProtegeOWLModel().getOWLIndividual(domainObjectName));
							ARTResourceIterator associatedDomainClasses = owlModel.listTypes(domainObject,
									false);
							URIResourceIteratorFilteringResourceIterator classesURIfilt = new URIResourceIteratorFilteringResourceIterator(
									associatedDomainClasses);
							while (classesURIfilt.hasNext()) {
								ARTURIResource domainClass = classesURIfilt.next();
								logger.info("domainClass: " + domainClass);
								// String domainClassName =
								// owlModel.getProtegeOWLModel().getResourceNameForURI(domainClass.getURI());
								// logger.info("domain class ProtegeName: " + domainClassName);
								tempList.add(domainClass.getURI());
							}
						}
					} catch (ModelAccessException e) {
						e.printStackTrace();
					}
				}

				if (searchObj.getIncludeNotes()) {

					// HAS DEFINITION
					// i_concept --> hasDefinition --> i_definition --> label (@language)

					retrievedResources = oi.findResourcesForIndex(index_definition, queryString,
							lang, searchStrategy);
					for (ARTURIResource foundCDefURIResource : retrievedResources) {

						ARTURIResource hasDefProp = owlModel
								.createURIResource(ModelConstants.COMMONBASENAMESPACE
										+ ModelConstants.RHASDEFINITION);

						ARTResourceIterator associatedDomainObjects;
						try {
							// getting the i_concepts having above i_definitions
							associatedDomainObjects = owlModel.listSubjectsOfPredObjPair(hasDefProp,
									foundCDefURIResource, true);
							URIResourceIteratorFilteringResourceIterator urifilt = new URIResourceIteratorFilteringResourceIterator(
									associatedDomainObjects);
							while (urifilt.hasNext()) {
								ARTURIResource domainObject = urifilt.next();
								// logger.debug("domainObject: " + domainObject);
								// getting the c_concepts for each i_concept
								ARTResourceIterator associatedDomainClasses = owlModel.listTypes(
										domainObject, false);
								URIResourceIteratorFilteringResourceIterator classesURIfilt = new URIResourceIteratorFilteringResourceIterator(
										associatedDomainClasses);
								while (classesURIfilt.hasNext()) {
									ARTURIResource domainClass = classesURIfilt.next();
									logger.info("domainClass: " + domainClass);
									tempList.add(domainClass.getURI());
								}
							}
						} catch (ModelAccessException e) {
							e.printStackTrace();
						}
					}

					// SCOPE NOTES
					// i_concept --> hasScopeNote --> textualdata (@language)

					retrievedResources = oi.findResourcesForIndex(index_scopeNotes, queryString,
							lang, searchStrategy);

					for (ARTURIResource foundIConceptURIResource : retrievedResources) {

						ARTResourceIterator cConcepts;
						try {
							cConcepts = owlModel.listTypes(foundIConceptURIResource, false);
							URIResourceIteratorFilteringResourceIterator classesURIfilt = new URIResourceIteratorFilteringResourceIterator(
									cConcepts);
							while (classesURIfilt.hasNext()) {
								ARTURIResource domainClass = classesURIfilt.next();
								logger.info("domainClass: " + domainClass);
								tempList.add(domainClass.getURI());
							}
						} catch (ModelAccessException e) {
							e.printStackTrace();
						}

					}
					
					
					// IMAGE DESCRIPTION
					// i_concept --> hasImage --> i_Image --> hasImageDescription --> textualdata (@language) 
										
					retrievedResources = oi.findResourcesForIndex(index_ImgDescription, queryString,
							lang, searchStrategy);
					for (ARTURIResource foundCDefURIResource : retrievedResources) {

						ARTURIResource hasImageProp = owlModel
								.createURIResource(ModelConstants.COMMONBASENAMESPACE
										+ ModelConstants.RHASIMAGE);

						ARTResourceIterator associatedDomainObjects;
						try {
							// getting the i_concepts having above i_definitions
							associatedDomainObjects = owlModel.listSubjectsOfPredObjPair(hasImageProp,
									foundCDefURIResource, true);
							URIResourceIteratorFilteringResourceIterator urifilt = new URIResourceIteratorFilteringResourceIterator(
									associatedDomainObjects);
							while (urifilt.hasNext()) {
								ARTURIResource domainObject = urifilt.next();
								// logger.debug("domainObject: " + domainObject);
								// getting the c_concepts for each i_concept
								ARTResourceIterator associatedDomainClasses = owlModel.listTypes(
										domainObject, false);
								URIResourceIteratorFilteringResourceIterator classesURIfilt = new URIResourceIteratorFilteringResourceIterator(
										associatedDomainClasses);
								while (classesURIfilt.hasNext()) {
									ARTURIResource domainClass = classesURIfilt.next();
									logger.info("domainClass: " + domainClass);
									tempList.add(domainClass.getURI());
								}
							}
						} catch (ModelAccessException e) {
							e.printStackTrace();
						}
					}
					
					
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} finally {
			resultList.addAll(tempList);
		}

		logger.info("objects found: " + resultList);

		return resultList;
	}

	/**
	 * obtains the Lucene query from the keyword and the WorkBench selected search strategy 
	 * 
	 * @param searchObj
	 * @return
	 */
	public static String obtainQueryString(SearchParameterObject searchObj) {
		String queryString=searchObj.getKeyword().toLowerCase();
		String modifier = searchObj.getRegex();
		if (modifier.equals(SearchParameterObject.EXACT_WORD)) 
			return "\"" + queryString + "\"";
		if (modifier.equals(SearchParameterObject.CONTAIN))
			return "*" + queryString + "*";
		if (modifier.equals(SearchParameterObject.START_WITH))
			return queryString + "*";		
		if (modifier.equals(SearchParameterObject.END_WITH))
			return "*" + queryString;
		return queryString;
	}

	
	
	
}
