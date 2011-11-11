package org.fao.aoscs.server.importer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.NonFuncObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.TermObject;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.server.ConceptServiceImpl;
import org.fao.aoscs.server.RelationshipServiceImpl;
import org.fao.aoscs.server.protege.ProtegeModelFactory;
import org.hibernate.SessionFactory;

import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;
import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFSLiteral;

public class SkosImporter
{

	private JenaOWLModel owlModel;

	private OWLNamedClass conceptSchemeClass;
	private OWLNamedClass conceptClass;
	private OWLProperty hasTopComceptProperty;
	private OWLProperty narrowerProperty;
	private OWLProperty broaderProperty;
	private RDFProperty prefLabelProperty;
	private RDFProperty altLabelProperty;
	private RDFProperty scopeNoteProperty;

	private OntologyInfo ontoInfo;

	private ConceptServiceImpl conceptService = new ConceptServiceImpl();

	private int userId;

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		SkosImporter si = new SkosImporter();
		si.start();
	}

	public void start() throws Exception
	{
		SessionFactory sessionFactory = HibernateUtilities.getSessionFactory();
		ontoInfo = getOntoInfo();

		OWLModel vocBenchOwlModel = ProtegeModelFactory.getOWLModel(ontoInfo);

		// String uri =
		// "http://protege.cim3.net/file/pub/ontologies/travel/travel.owl";

		// alternatively, you can specify a local path on your computer
		// for the travel.owl ontology. Example:
		String uri = "file:///D:/workspaces/wsGalileo/Voc1/vocabularies/aslt_1_0.skos.xml";

		initModel(uri);

		Collection<OWLIndividual> conceptSchemes = conceptSchemeClass.getInstances(true);

		for (OWLIndividual conceptScheme : conceptSchemes)
		{
			Collection<OWLIndividual> topConcepts = conceptScheme.getPropertyValues(hasTopComceptProperty);
			System.out.println(topConcepts);
			for (OWLIndividual concept : topConcepts)
			{
				ConceptObject addedTopConcept = addTopConcept(concept);
			}

		}

		int instanceCount = conceptClass.getInstanceCount(true);
		System.out.println(instanceCount + " istanze della classe skos:Concept");

		Collection<OWLIndividual> concepts = conceptClass.getInstances(true);

		for (OWLIndividual concept : concepts)
		{
			System.out.println("-------------------------------------------------------\n" + concept);
			// addTopConcept(concept);

			// Collection<OWLIndividual> narrowerConcepts =
			// concept.getPropertyValues(narrowerProperty);
			// for (OWLIndividual owlIndividual : narrowerConcepts)
			// {
			// System.out.println(owlIndividual);
			// }
			// System.out.println("********************************************************");

			Collection<OWLIndividual> broaderConcepts = concept.getPropertyValues(broaderProperty);
			for (OWLIndividual owlIndividual : broaderConcepts)
			{
				System.out.println(owlIndividual);
			}

			if (broaderConcepts.size() > 1)
			{
				System.out.println("broader: " + broaderConcepts.size());
			}
			// Collection<OWLIndividual> topConcepts =
			// owlIndividual.getPropertyValues(hasTopComceptProperty);
			// System.out.println(topConcepts);

		}

	}

	private ConceptObject addTopConcept(OWLIndividual concept)
	{
		ConceptObject selectedConceptObject = new ConceptObject();
		selectedConceptObject.setName("c_domain_concept");

		return addConcept(concept, selectedConceptObject);
	}

	private ConceptObject addConcept(OWLIndividual concept, ConceptObject parentConcept)
	{
		ConceptObject conceptObject = new ConceptObject();
		conceptObject.setNameSpace("http://www.ittig.cnr.it/skos/aslt#");

		String conceptName = concept.getLocalName();
		conceptObject.setName(conceptName);
		conceptObject.setStatusID(2);
		conceptObject.setStatus("Proposed");
		conceptObject.setBelongsToModule(0);
		conceptObject.setHasChild(false);

		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		ConceptObject addedConcept = conceptService.getConceptObject(owlModel, "ittig:" + conceptName);
		if (addedConcept.getName() == null)
		{
			addedConcept = conceptService.addNewConceptWithoutTerm(ontoInfo, 1, userId, conceptObject, "subClass", parentConcept);
		}

		addTerms(addedConcept, concept.getPropertyValues(prefLabelProperty), true);

		addTerms(addedConcept, concept.getPropertyValues(altLabelProperty), false);

		addPropertyValues(concept, addedConcept, "hasScopeNote", scopeNoteProperty);

		Collection<OWLIndividual> narrowerConcepts = concept.getPropertyValues(narrowerProperty);
		for (OWLIndividual narrowerConcept : narrowerConcepts)
		{
			ConceptObject existingNarrowerConcept = conceptService.getConceptObject(owlModel, "ittig:" + narrowerConcept.getLocalName());

			if (existingNarrowerConcept.getName() == null)
			{
				ConceptObject addedNarrowerConcept = addConcept(narrowerConcept, addedConcept);
			}
			else
			{
				System.out.println("Narrower condiviso: " + addedConcept.getName() + " -> " + narrowerConcept.getLocalName());
			}
		}

		return addedConcept;
	}

	private void addPropertyValues(OWLIndividual concept, ConceptObject addedConcept, String string, RDFProperty scopeNoteProperty2)
	{
		RelationshipObject rObj = getRelationshipObject(string);
		Collection<RDFSLiteral> propertyValues = concept.getPropertyValues(scopeNoteProperty2);
		for (RDFSLiteral scopeNote : propertyValues)
		{
			OwlStatus status = new OwlStatus();
			status.setId(2);
			status.setStatus("Proposed");
			NonFuncObject value = new NonFuncObject();
			value.setValue(scopeNote.getBrowserText());
			value.setLanguage(scopeNote.getLanguage());
//			RelationshipObject rObj = new RelationshipObject();
			conceptService.addPropertyValue(ontoInfo, 18, status, userId, value, rObj, addedConcept, "conceptEditorialDatatypeProperty");
		}
	}

	private RelationshipObject getRelationshipObject(String string)
	{
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLProperty prop = owlModel.getOWLProperty(string);
//		for (Iterator<?> itr = nProp.getSubproperties(true).iterator(); itr.hasNext();)
//		{
//			OWLProperty prop = (OWLProperty) itr.next();
			RelationshipObject rObj = new RelationshipObject();
			rObj.setUri(prop.getURI());
			rObj.setName(prop.getName());
			rObj.setType(RelationshipObject.DATATYPE);
			rObj.setFunctional(prop.isFunctional());
			rObj.setDomainRangeDatatypeObject(RelationshipServiceImpl.getRangeValues(prop));
			Collection<?> labelList = prop.getLabels();
			for (Iterator<?> iterator = labelList.iterator(); iterator.hasNext();)
			{
				Object obj = iterator.next();
				if (obj instanceof DefaultRDFSLiteral)
				{
					DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
					rObj.addLabel(element.getString(), element.getLanguage());
				}
			}
//		}
		return rObj;
	}

	private void addTerms(ConceptObject addedConcept, Collection<RDFSLiteral> altLabels, boolean prefLabel)
	{
		TermObject termObjNew;
		for (RDFSLiteral owlIndividual : altLabels)
		{
			System.out.println(owlIndividual + "@" + owlIndividual.getLanguage());

			termObjNew = createNewTermObject(owlIndividual.getBrowserText(), owlIndividual.getLanguage(), addedConcept.getName(), addedConcept.getUri(),
					prefLabel);
			OwlStatus status = new OwlStatus();
			status.setId(2);
			status.setStatus("Proposed");
			conceptService.addTerm(ontoInfo, 6, status, userId, termObjNew, addedConcept);
		}
	}

	private TermObject createNewTermObject(String browserText, String language, String name, String uri, boolean b)
	{
		TermObject termObjNew = new TermObject();
		termObjNew.setLabel(browserText);
		termObjNew.setLang(language);
		termObjNew.setStatus("Proposed");
		termObjNew.setStatusID(0);
		termObjNew.setConceptName(name);
		termObjNew.setConceptUri(uri);
		termObjNew.setMainLabel(b);
		return termObjNew;
	}

	private void initModel(String uri) throws Exception
	{
		owlModel = ProtegeOWL.createJenaOWLModelFromURI(uri);

		conceptSchemeClass = owlModel.getOWLNamedClass("skos:ConceptScheme");
		conceptClass = owlModel.getOWLNamedClass("skos:Concept");
		hasTopComceptProperty = owlModel.getOWLProperty("skos:hasTopConcept");
		narrowerProperty = owlModel.getOWLProperty("skos:narrower");
		broaderProperty = owlModel.getOWLProperty("skos:broader");
		prefLabelProperty = owlModel.getRDFProperty("skos:prefLabel");
		altLabelProperty = owlModel.getRDFProperty("skos:altLabel");
		scopeNoteProperty = owlModel.getRDFProperty("skos:scopeNote");
	}

	public HashMap<String, String> loadModelConstants()
	{
		HashMap<String, String> mcMap = new HashMap<String, String>();
		ResourceBundle rb = ResourceBundle.getBundle("org.fao.aoscs.server.ModelConstants");
		Enumeration<String> en = rb.getKeys();
		while (en.hasMoreElements())
		{
			String key = (String) en.nextElement();
			mcMap.put(key, rb.getString(key));
		}
		ModelConstants.loadConstants(mcMap);
		return mcMap;

	}

	public OntologyInfo getOntoInfo()
	{
		loadModelConstants();
		OntologyInfo ontoInfo = new OntologyInfo();
		ontoInfo.setDbDriver("com.mysql.jdbc.Driver");

		// ontoInfo.setDbUrl("jdbc:mysql://158.108.33.132:3306/agrovoc_wb_21102009?requireSSL=false&useUnicode=true&characterEncoding=UTF-8");
		// ontoInfo.setDbTableName("agrovoc_wb_21102009");
		// ontoInfo.setOntologyName("AGROVOC_WB_SMALL");

		/*
		 * ontoInfo.setDbUrl("jdbc:mysql://158.108.33.132:3306/agrovoc_wb_02092009?requireSSL=false&useUnicode=true&characterEncoding=UTF-8"
		 * ); ontoInfo.setDbTableName("agrovoc_wb_02092009");
		 * ontoInfo.setOntologyName("AGROVOC_WB_SMALL");
		 */

		ontoInfo.setDbUrl("jdbc:mysql://localhost:3306/agrovoc_wb_20110223_products");
		ontoInfo.setDbTableName("aosittig1");
		// ontoInfo.setOntologyName("fullagrovoc_20090904_1421");

		ontoInfo.setDbUsername("vocbench");
		ontoInfo.setDbPassword("vocbench");

		return ontoInfo;
	}

}
