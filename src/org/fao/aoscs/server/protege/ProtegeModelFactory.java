package org.fao.aoscs.server.protege;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.domain.OntologyInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protegex.owl.database.OWLDatabaseKnowledgeBaseFactory;
import edu.stanford.smi.protegex.owl.model.OWLModel;

public class ProtegeModelFactory {
	
	static protected Logger logger = LoggerFactory.getLogger(ProtegeModelFactory.class);
	static HashMap<String, OWLModel> modelsMap;
	static HashMap<String, Project> projectsMap;
	static {
		modelsMap = new HashMap<String, OWLModel>();
		projectsMap = new HashMap<String, Project>();
	}
	
	public static OWLModel createOWLModel(OntologyInfo ontoInfo){
		logger.info("creating Protege OWLModel: " + ontoInfo.getModelID());
		OWLDatabaseKnowledgeBaseFactory factory = new OWLDatabaseKnowledgeBaseFactory();         
		Collection<Object> errors = new ArrayList<Object>();
		Project prj = Project.createNewProject(factory, errors);
		projectsMap.put(ontoInfo.getModelID(), prj);
		OWLDatabaseKnowledgeBaseFactory.setSources(prj.getSources(), ontoInfo.getDbDriver(), ontoInfo.getDbUrl(), ontoInfo.getDbTableName(), ontoInfo.getDbUsername(), ontoInfo.getDbPassword());
		prj.createDomainKnowledgeBase(factory, errors, true);
		OWLModel owlModel = (OWLModel) prj.getKnowledgeBase();
		modelsMap.put(ontoInfo.getModelID(), owlModel);
		return owlModel;
	}
	
	public static synchronized OWLModel getOWLModel(OntologyInfo ontoInfo) {
		OWLModel owlModel = modelsMap.get(ontoInfo.getModelID());
		if (owlModel!=null) {
			logger.info("model " + ontoInfo.getModelID() + " got from modelMap");
			try {
				// check if connection is alive
				owlModel.getNamespaceManager().setPrefix(ModelConstants.ONTOLOGYBASENAMESPACE, ModelConstants.ONTOLOGYBASENAMESPACEPREFIX);
			} catch(Exception e){}
		} else {
			logger.info("model " + ontoInfo.getModelID() + " not present in modelMap, generating it from DB");
			owlModel = createOWLModel(ontoInfo);				
		}
		return owlModel;
	}
	
	public static Project getProject(OntologyInfo ontoInfo) {
		return projectsMap.get(ontoInfo.getModelID());
	}
	

	/*public static OWLModel getOWLModel(OntologyInfo ontoInfo){
		OWLDatabaseKnowledgeBaseFactory factory = new OWLDatabaseKnowledgeBaseFactory();         
		Collection<Object> errors = new ArrayList<Object>();
		Project prj = Project.createNewProject(factory, errors);
		OWLDatabaseKnowledgeBaseFactory.setSources(prj.getSources(), ontoInfo.getDbDriver(), ontoInfo.getDbUrl(), ontoInfo.getDbTableName(), ontoInfo.getDbUsername(), ontoInfo.getDbPassword());
		prj.createDomainKnowledgeBase(factory, errors, true);
		OWLModel owlModel = (OWLModel) prj.getKnowledgeBase();
		return owlModel;
	}*/
	/*public static OWLModel getOWLModel(HttpSession session){
		OWLModel owlModel = (OWLModel) session.getAttribute("owlModel");
		if(owlModel==null)
		{
			UserLogin userLogin = (UserLogin) session.getAttribute("userloginobj");
			OntologyInfo ontoInfo = userLogin.getOntology();
			owlModel = getOWLModel(ontoInfo);
			session.setAttribute("owlModel", owlModel);
			System.out.println("Loading Model");
		}
		else
			System.out.println("Reloading Model");
		return owlModel;
	}
	
	
	public static OWLModel loadOWLModel(OntologyInfo ontoInfo){
		OWLDatabaseKnowledgeBaseFactory factory = new OWLDatabaseKnowledgeBaseFactory();         
		Collection<Object> errors = new ArrayList<Object>();
		Project prj = Project.createNewProject(factory, errors);
		OWLDatabaseKnowledgeBaseFactory.setSources(prj.getSources(), ontoInfo.getDbDriver(), ontoInfo.getDbUrl(), ontoInfo.getDbTableName(), ontoInfo.getDbUsername(), ontoInfo.getDbPassword());
		prj.createDomainKnowledgeBase(factory, errors, true);
		OWLModel owlModel = (OWLModel) prj.getKnowledgeBase();
		return owlModel;
	}
	
	private static HashMap<String, OWLModel> modelList = new HashMap<String, OWLModel>();
	 
	public static OWLModel getOWLModel(OntologyInfo ontoInfo){
		String ontologyId = Integer.valueOf(ontoInfo.getOntologyId()).toString();
		if(!modelList.containsKey(ontologyId)){
			System.out.println("Start load the fresh model ("+ontoInfo.getDbTableName()+") ... ");
			modelList.put(ontologyId, loadOWLModel(ontoInfo));
			return (OWLModel)modelList.get(ontologyId);
		}else{
			System.out.println("Get model("+ontoInfo.getDbTableName()+") from memory ... ");
			return (OWLModel)modelList.get(ontologyId);
		}
	}
	public void disposeAll() {
		Iterator<String> it = modelList.keySet().iterator();
		while (it.hasNext()) {
			String key = (String)it.next();
			OWLModel model = (OWLModel) modelList.get(key);
			model.dispose();
		}
		modelList = null;
	}
	public void dispose(String ontologyId) {
		OWLModel model = (OWLModel) modelList.get(ontologyId);
		model.dispose();
		modelList.remove(ontologyId);
		
		System.gc();
		System.runFinalization();
		System.gc();

	}

	*/
}
