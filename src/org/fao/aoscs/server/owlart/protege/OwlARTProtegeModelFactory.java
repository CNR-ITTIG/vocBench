package org.fao.aoscs.server.owlart.protege;

import it.uniroma2.art.owlart.exceptions.VocabularyInitializationException;
import it.uniroma2.art.owlart.models.OWLModel;
import it.uniroma2.art.owlart.models.impl.OWLModelImpl;
import it.uniroma2.art.owlart.protegeimpl.models.BaseRDFModelProtegeImpl;
import it.uniroma2.art.owlart.protegeimpl.models.OWLModelProtegeImpl;
import it.uniroma2.art.owlart.vocabulary.OWL;
import it.uniroma2.art.owlart.vocabulary.RDF;
import it.uniroma2.art.owlart.vocabulary.RDFS;

import java.util.HashMap;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.server.owlart.OWLARTModelFactory;
import org.fao.aoscs.server.protege.ProtegeModelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OwlARTProtegeModelFactory implements OWLARTModelFactory {

	static protected Logger logger = LoggerFactory.getLogger(OwlARTProtegeModelFactory.class);
	static HashMap<String, OWLModel> modelsMap;
	static {
		modelsMap = new HashMap<String, OWLModel>();
	}

	/* 
	 * this method implementation actually does not create a new model from scratch, but wraps the existing
	 * Protege Model which is got from ProtegeModelFactory
	 * 
	 * (non-Javadoc)
	 * @see org.fao.aoscs.server.owlart.OWLARTModelFactory#createOWLModel(org.fao.aoscs.domain.OntologyInfo)
	 */
	public OWLModel createOWLModel(OntologyInfo ontoInfo) {

		ProtegeModelFactory.getOWLModel(ontoInfo); 
		// this gets (or in turn creates) the Protege model, but we just need the project, which is created
		// inside that call
		BaseRDFModelProtegeImpl baseModel = new BaseRDFModelProtegeImpl(ProtegeModelFactory
				.getProject(ontoInfo));
		OWLModelImpl model = new OWLModelProtegeImpl(baseModel);
		try {
			logger.info("initializing RDF vocabularies");
			RDF.Res.initialize(model);
			RDFS.Res.initialize(model);
			OWL.Res.initialize(model);
			logger.info("RDF vocabularies initialized");
			// model.setBaseURI(baseuri);
			return model;
		} catch (VocabularyInitializationException e) {
			throw new RuntimeException("failed to access the Protege DB OWL Model Factory");
		}
	}

	public synchronized OWLModel getOWLModel(OntologyInfo ontoInfo) {
		OWLModel owlModel = modelsMap.get(ontoInfo.getModelID());
		if (owlModel != null) {
			logger.info("model got from modelMap");
			return owlModel;
		} else {
			logger.info("model not present in modelMap, generating it from Inner Protege Model Factory");
			owlModel = createOWLModel(ontoInfo);
			modelsMap.put(ontoInfo.getModelID(), owlModel);
			return owlModel;
		}
	}

	/*
	 * public static OWLModel getOWLModel(OntologyInfo ontoInfo){ OWLDatabaseKnowledgeBaseFactory factory =
	 * new OWLDatabaseKnowledgeBaseFactory(); Collection<Object> errors = new ArrayList<Object>(); Project prj
	 * = Project.createNewProject(factory, errors);
	 * OWLDatabaseKnowledgeBaseFactory.setSources(prj.getSources(), ontoInfo.getDbDriver(),
	 * ontoInfo.getDbUrl(), ontoInfo.getDbTableName(), ontoInfo.getDbUsername(), ontoInfo.getDbPassword());
	 * prj.createDomainKnowledgeBase(factory, errors, true); OWLModel owlModel = (OWLModel)
	 * prj.getKnowledgeBase(); return owlModel; }
	 */
	/*
	 * public static OWLModel getOWLModel(HttpSession session){ OWLModel owlModel = (OWLModel)
	 * session.getAttribute("owlModel"); if(owlModel==null) { UserLogin userLogin = (UserLogin)
	 * session.getAttribute("userloginobj"); OntologyInfo ontoInfo = userLogin.getOntology(); owlModel =
	 * getOWLModel(ontoInfo); session.setAttribute("owlModel", owlModel); System.out.println("Loading Model");
	 * } else System.out.println("Reloading Model"); return owlModel; }
	 * 
	 * 
	 * public static OWLModel loadOWLModel(OntologyInfo ontoInfo){ OWLDatabaseKnowledgeBaseFactory factory =
	 * new OWLDatabaseKnowledgeBaseFactory(); Collection<Object> errors = new ArrayList<Object>(); Project prj
	 * = Project.createNewProject(factory, errors);
	 * OWLDatabaseKnowledgeBaseFactory.setSources(prj.getSources(), ontoInfo.getDbDriver(),
	 * ontoInfo.getDbUrl(), ontoInfo.getDbTableName(), ontoInfo.getDbUsername(), ontoInfo.getDbPassword());
	 * prj.createDomainKnowledgeBase(factory, errors, true); OWLModel owlModel = (OWLModel)
	 * prj.getKnowledgeBase(); return owlModel; }
	 * 
	 * private static HashMap<String, OWLModel> modelList = new HashMap<String, OWLModel>();
	 * 
	 * public static OWLModel getOWLModel(OntologyInfo ontoInfo){ String ontologyId =
	 * Integer.valueOf(ontoInfo.getOntologyId()).toString(); if(!modelList.containsKey(ontologyId)){
	 * System.out.println("Start load the fresh model ("+ontoInfo.getDbTableName()+") ... ");
	 * modelList.put(ontologyId, loadOWLModel(ontoInfo)); return (OWLModel)modelList.get(ontologyId); }else{
	 * System.out.println("Get model("+ontoInfo.getDbTableName()+") from memory ... "); return
	 * (OWLModel)modelList.get(ontologyId); } } public void disposeAll() { Iterator<String> it =
	 * modelList.keySet().iterator(); while (it.hasNext()) { String key = (String)it.next(); OWLModel model =
	 * (OWLModel) modelList.get(key); model.dispose(); } modelList = null; } public void dispose(String
	 * ontologyId) { OWLModel model = (OWLModel) modelList.get(ontologyId); model.dispose();
	 * modelList.remove(ontologyId);
	 * 
	 * System.gc(); System.runFinalization(); System.gc();
	 * 
	 * }
	 */
}
