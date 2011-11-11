package org.fao.aoscs.server.index;


import it.uniroma2.art.ont_indexer.OntologyIndexer;
import it.uniroma2.art.ont_indexer.index.IndexManager;
import it.uniroma2.art.ont_indexer.search.OntIndexSearcher;
import it.uniroma2.art.owlart.exceptions.ModelAccessException;
import it.uniroma2.art.owlart.model.ARTURIResource;
import it.uniroma2.art.owlart.protegeimpl.factory.ARTModelFactoryProtegeImpl;
import it.uniroma2.art.owlart.protegeimpl.models.OWLModelProtegeImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;

/**
 * This class accesses MySQL-based AGROVOC ontology. It can be used to produce the CUSTOM indexes which are
 * created from instances of C_NOUN, according to the particular multilingual knowledge organization of first
 * OWL version of AGROVOC ontology<br/>
 * In addition to this, this class contains explicit Protege API code, which is used to manipulate results
 * which are returned by the OntologyIndexer in terms of OWL ART API.
 * 
 * 
 * @author Armando Stellato <stellato@info.uniroma2.it>
 * 
 */
public class AGROVOC_UPDATE_Test {

	public final static String c_nounInstancesIndexCategory = "c_nounInstances";
	public final static String index_scopeNotes = "scopeNotes";
	public final static String index_ImgDescription = "imgDescription";
	public final static String index_definition = "definition";

	protected static Logger logger = LoggerFactory.getLogger(AGROVOC_UPDATE_Test.class);

	protected static String genericAIMSBaseNS = "http://aims.fao.org/aos/";
	// used for all AGROVOC specific domain concepts
	protected static String agrovocBaseNS = "http://aims.fao.org/aos/agrovoc/";
	// application concepts used by CONCEPT SERVER 1.0
	protected static String commonAIMBaseNS = "http://aims.fao.org/aos/common/";

	protected static String agrovoc_small_DB = "agrovoc_wb_small";
	protected static String agrovoc_full_DB = "agrovoc_wb_new";

	protected static String dbusername = "fao";
	protected static String dbpwd = "faomimos";

	protected static OntologyIndexer oi;
	protected static OWLModelProtegeImpl model;
	protected static OWLModel protOWLModel;

	public static void main(String[] args) {
		try {

			String chosenDB = agrovoc_small_DB;

			ARTModelFactoryProtegeImpl fact = new ARTModelFactoryProtegeImpl();

			model = (OWLModelProtegeImpl) fact.loadOWLModel("com.mysql.jdbc.Driver",
					"jdbc:mysql://127.0.0.1:3306/" + chosenDB
							+ "?requireSSL=false&useUnicode=true&characterEncoding=UTF-8", chosenDB,
					dbusername, dbpwd);
			
			protOWLModel = model.getProtegeOWLModel();

			String indexLocation = "/Sachit/workspace/data/1_agrovoc_wb_small/index";

			ArrayList<String> langs = new ArrayList<String>();
			langs.add("en");
			langs.add("it");
			ArrayList<String> URILangs = new ArrayList<String>();

			System.out.println("langs have been specified: " + langs);

			oi = new OntologyIndexer(model, indexLocation, langs, URILangs);

			ArrayList<String> indexes = new ArrayList<String>();
			indexes.add(c_nounInstancesIndexCategory);
			indexes.add(index_scopeNotes);
			indexes.add(index_ImgDescription);
			indexes.add(index_definition);

			oi.initializeCustomIndexWriters(indexes);

			oi.createOntIndexSearcher();

			//testUpdateProperty();
			testCONTAINSSearch();
			

			/*
			 * just a check for actual namespaces of available classes ARTURIResource owlCls =
			 * model.createURIResource(OWL.NAMESPACE + "Class"); ARTResourceIterator testIt =
			 * model.listInstances(owlCls, false); while (testIt.streamOpen()) {
			 * System.out.println(testIt.getNext().asURIResource().getURI()); }
			 */

			// INDEX BUILDING (decomment this to redo indexing)

			/*
			 * System.out.println("building index at: " + indexLocation); ARTURIResource cnoun =
			 * model.createURIResource(commonAIMBaseNS + "c_noun"); ARTURIResourceIterator it = new
			 * URIResourceIteratorFilteringResourceIterator(model.listInstances( cnoun, false));
			 * oi.buildCustomIndex(customIndexName, it, true); logger.info("INDEX BUILT");
			 */

		} catch (Exception e) {
			e.printStackTrace(System.err);
			logger.error(e.getMessage());

			System.err.flush();
			System.err.println("error");

			e.printStackTrace();
		}
	}

	public static void searchVanilla() {

		System.out.println("ontsearcher armed");

		Iterator<ARTURIResource> retrievedResources;
		Iterator<String> retrievedLabels;

		oi.setTHRESHOLD(0.7);

		retrievedLabels = oi.findLabelsForIndex(c_nounInstancesIndexCategory, "vanilla", "en",
				OntIndexSearcher.SIMILARITY_STRATEGY).iterator();
		while (retrievedLabels.hasNext())
			System.out.println(retrievedLabels.next());

		retrievedResources = oi.findResourcesForIndex(c_nounInstancesIndexCategory, "vanilla", "en",
				OntIndexSearcher.SIMILARITY_STRATEGY).iterator();
		while (retrievedResources.hasNext())
			System.out.println(retrievedResources.next());
	}

	@SuppressWarnings("deprecation")
	public static void testUpdateLabel() throws IOException, ModelAccessException {
		System.err.println("\n\n\nNOW TESTING ADDITION OF CONCEPT TO INDEX\n\n\n");

		Iterator<ARTURIResource> retrievedResources;
		Iterator<String> retrievedLabels;

		IndexManager idxMgr = oi.getIndexManager();

		String newTermString = "newTerm";

		OWLNamedClass nounCls = protOWLModel.getOWLNamedClass("c_noun");
		OWLIndividual newTermProt = (OWLIndividual) protOWLModel.getInstance(newTermString);
		if (newTermProt == null) {
			newTermProt = (OWLIndividual) nounCls.createInstance(newTermString);
		}
		newTermProt.addLabel("nuovo termine", "it");

		System.out.println("newTermProt URI: " + newTermProt.getURI());

		ARTURIResource newTerm = model.createURIResource(newTermProt.getURI());
		// model.addLabel(newTerm, "nuovo termine", "it");

		idxMgr.addNewResourceToIndex(newTerm, c_nounInstancesIndexCategory);
		idxMgr.updateLangIndexForResource(newTerm, c_nounInstancesIndexCategory, "it");

		retrievedLabels = oi.findLabelsForIndex(c_nounInstancesIndexCategory, "termine", "it",
				OntIndexSearcher.SIMILARITY_STRATEGY).iterator();
		while (retrievedLabels.hasNext())
			System.out.println(retrievedLabels.next());

		retrievedResources = oi.findResourcesForIndex(c_nounInstancesIndexCategory, "termine", "it",
				OntIndexSearcher.SIMILARITY_STRATEGY).iterator();
		while (retrievedResources.hasNext())
			System.out.println(retrievedResources.next());

		idxMgr.deleteResourceFromIndexes(newTerm, c_nounInstancesIndexCategory);

		retrievedLabels = oi.findLabelsForIndex(c_nounInstancesIndexCategory, "termine", "it",
				OntIndexSearcher.SIMILARITY_STRATEGY).iterator();
		while (retrievedLabels.hasNext())
			System.out.println(retrievedLabels.next());
	}

	@SuppressWarnings("deprecation")
	public static void testUpdateProperty() throws IOException, ModelAccessException {
		System.err.println("\n\n\nNOW TESTING ADDITION OF CONCEPT TO INDEX\n\n\n");

		Iterator<ARTURIResource> retrievedResources;
		Iterator<String> retrievedLabels;

		IndexManager idxMgr = oi.getIndexManager();

		String newConceptString = "i_xyz123456";

		
		OWLNamedClass domainConceptCls = protOWLModel.getOWLNamedClass("c_domain_concept");
		OWLIndividual newConceptInst = (OWLIndividual) protOWLModel.getInstance(newConceptString);
		if (newConceptInst == null) {
			newConceptInst = (OWLIndividual) domainConceptCls.createInstance(newConceptString);
		}

		OWLDatatypeProperty hasScopeNote = protOWLModel.getOWLDatatypeProperty("hasScopeNote");
		newConceptInst.addPropertyValue(hasScopeNote, protOWLModel.createRDFSLiteralOrString(
				"this is a scope note", "en"));

		System.out.println("newTermProt URI: " + newConceptInst.getURI());

		ARTURIResource newConceptOWLART = model.createURIResource(newConceptInst.getURI());
		ARTURIResource hasScopeNoteOWLART = model.createURIResource(hasScopeNote.getURI());
		// model.addLabel(newTerm, "nuovo termine", "it");

		idxMgr.addNewResourceToIndex(newConceptOWLART, index_scopeNotes);
		idxMgr.updateLangIndexForResource(newConceptOWLART, hasScopeNoteOWLART, index_scopeNotes, "en");

		retrievedLabels = oi.findLabelsForIndex(index_scopeNotes, "scope", "en",
				OntIndexSearcher.SIMILARITY_STRATEGY).iterator();
		while (retrievedLabels.hasNext())
			System.out.println(retrievedLabels.next());

		retrievedResources = oi.findResourcesForIndex(index_scopeNotes, "scope", "en",
				OntIndexSearcher.SIMILARITY_STRATEGY).iterator();
		while (retrievedResources.hasNext())
			System.out.println(retrievedResources.next());

		/*
		idxMgr.deleteResourceFromIndexes(newTerm, c_nounInstancesIndexCategory);

		retrievedLabels = oi.findLabelsForIndex(c_nounInstancesIndexCategory, "termine", "it",
				OntIndexSearcher.SIMILARITY_STRATEGY).iterator();
		while (retrievedLabels.hasNext())
			System.out.println(retrievedLabels.next());
		*/
	}
	
	public static void testCONTAINSSearch() throws IOException, ModelAccessException {
		System.err.println("\n\n\nNOW TESTING CONTAINS\n\n\n");
		
		Iterator<String> retrievedLabels;


		retrievedLabels = oi.findLabelsForIndex(c_nounInstancesIndexCategory, "*acquired*", "en",
				OntIndexSearcher.EXACT_STRATEGY).iterator();
		while (retrievedLabels.hasNext())
			System.out.println(retrievedLabels.next());


		/*
		idxMgr.deleteResourceFromIndexes(newTerm, c_nounInstancesIndexCategory);

		retrievedLabels = oi.findLabelsForIndex(c_nounInstancesIndexCategory, "termine", "it",
				OntIndexSearcher.SIMILARITY_STRATEGY).iterator();
		while (retrievedLabels.hasNext())
			System.out.println(retrievedLabels.next());
		*/
	}
		
		

	

}

