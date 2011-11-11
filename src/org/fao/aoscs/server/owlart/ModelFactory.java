package org.fao.aoscs.server.owlart;

import it.uniroma2.art.owlart.exceptions.ModelAccessException;
import it.uniroma2.art.owlart.exceptions.ModelCreationException;
import it.uniroma2.art.owlart.exceptions.UnsupportedRDFFormatException;
import it.uniroma2.art.owlart.io.RDFFormat;
import it.uniroma2.art.owlart.model.ARTURIResource;
import it.uniroma2.art.owlart.models.OWLModel;
import it.uniroma2.art.owlart.models.impl.URIResourceIteratorFilteringResourceIterator;
import it.uniroma2.art.owlart.navigation.ARTURIResourceIterator;
import it.uniroma2.art.owlart.protegeimpl.models.OWLModelProtegeImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.server.SystemServiceImpl;
import org.fao.aoscs.server.index.OfflineIndexer;
import org.fao.aoscs.server.owlart.protege.OwlARTProtegeModelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.ontology.OntModel;

/**
 * @author Armando Stellato
 * 
 *         this class is a provider for OWLModel implementations. Currently, it wraps an existing instance of
 *         {@link OWLARTModelFactory} which is hard-coded in its initialization
 * 
 */
public class ModelFactory {

	static protected Logger logger = LoggerFactory.getLogger(ModelFactory.class);

	private static OWLARTModelFactory modelFactory;

	static {
		modelFactory = new OwlARTProtegeModelFactory();
	}

	public static OWLModel createOWLModel(OntologyInfo ontoInfo) {
		return modelFactory.createOWLModel(ontoInfo);
	}

	public synchronized static OWLModel getOWLModel(OntologyInfo ontoInfo) {
		return modelFactory.getOWLModel(ontoInfo);
	}

	/**
	 * utility method for running an offline
	 * 
	 * @param modelIDString
	 * @param chosenDB
	 * @param dbusername
	 * @param dbpwd
	 * @return
	 * @throws ModelCreationException
	 */
	public static OWLModel createOfflineModel(OntologyInfo ontoInfo) throws ModelCreationException {
		logger
				.info("necessary reinitialization of some ConceptServer data structures for offline processing");
		SystemServiceImpl ssImpl = new SystemServiceImpl();
		ConfigConstants.loadConstants(ssImpl.loadConfigConstants());
		ModelConstants.loadConstants(ssImpl.loadModelConstants());

		logger.info("initializing model");
		OWLModel owlModel;
		owlModel = createOWLModel(ontoInfo);

		/*
		 * ARTModelFactoryProtegeImpl fact = new ARTModelFactoryProtegeImpl(); OWLModelProtegeImpl
		 * owlArtModel; owlArtModel = (OWLModelProtegeImpl) fact.loadOWLModel(ontoInfo.getDbDriver(),
		 * ontoInfo.getDbUrl(), ontoInfo.getDbTableName(), ontoInfo.getDbUsername(),
		 * ontoInfo.getDbPassword());
		 */

		return owlModel;
	}

	@SuppressWarnings("unused")
	private static void handleErrors(Collection<?> errors) {
		Iterator<?> i = errors.iterator();
		while (i.hasNext()) {
			System.out.println("Error: " + i.next());
		}
		if (!errors.isEmpty()) {
			System.exit(-1);
		}
	}

	/**
	 * utility main for various offline applications. Please Sachit maintain all the commented code, because
	 * it represents several attempts on outputting data
	 * 
	 * @param args
	 * @throws ModelCreationException
	 * @throws IOException
	 * @throws ModelAccessException
	 * @throws UnsupportedRDFFormatException
	 */
	public static void main(String[] args) throws ModelCreationException, IOException, ModelAccessException,
			UnsupportedRDFFormatException {
		int ontologyId;

		final String writeRDFCommand = "writeRDF";
		final String countInstances = "c_inst";
		final String countSubClasses = "c_subcl";

		HashSet<String> commands = new HashSet<String>();
		commands.add(writeRDFCommand);
		commands.add(countInstances);
		commands.add(countSubClasses);

		if (args.length < 2) {
			String errorMsg = "Usage:\n OfflineModelLoader <ontology-id> <COMMAND>";
			System.out.println(errorMsg);
			logger.info(errorMsg);
			System.exit(0);
		}
		String command = args[1];
		if (!commands.contains(command)) {
			String errorMsg = "command: " + command
					+ " not available; check correct typing; available commands are: " + commands;
			System.out.println(errorMsg);
			logger.info(errorMsg);
			System.exit(0);
		}

		ontologyId = Integer.parseInt(args[0]);
		OntologyInfo ontoInfo = OfflineIndexer.getOntoInfo(ontologyId);
		OWLModel owlModel;
		owlModel = createOfflineModel(ontoInfo);

		if (command.equals(writeRDFCommand)) {
			RDFFormat format;
			if (args.length > 2)
				format = RDFFormat.parseFormat(args[2]);
			else
				format = RDFFormat.NTRIPLES;
			System.out.println("format: " + format);
			
			System.out.println("Starting: " + new Date());

			FileOutputStream fos = new FileOutputStream(new File(ontoInfo.getModelID() + ".nt"));
			PrintWriter pw = new PrintWriter(fos, true);
			OntModel om = ((OWLModelProtegeImpl)owlModel).getProtegeOWLModel().getOntModel();
			om.write(pw, "N-TRIPLE");
			pw.flush();
			pw.close();
			om.close();
			
			System.out.println("Completed: " + new Date());
			System.exit(0);
			
		}

		if (command.equals(countInstances) || command.equals(countSubClasses)) {
			boolean inferred = false;
			String conceptLocalName = ModelConstants.CDOMAINCONCEPT;

			if (args.length > 2)
				inferred = Boolean.parseBoolean(args[2]);

			if (args.length > 3)
				conceptLocalName = args[3];

			ARTURIResource counted_concept = owlModel.createURIResource(ModelConstants.COMMONBASENAMESPACE
					+ conceptLocalName);
			ARTURIResourceIterator it;

			logger.debug("COMMONBASENAMESPACE: " + ModelConstants.COMMONBASENAMESPACE);
			logger.debug("counted_concept: " + counted_concept);
			logger.debug("inferred: " + inferred);

			logger.info("Start counting root domain concepts...");
			int count = 0;
			if (command.equals(countInstances))
				it = new URIResourceIteratorFilteringResourceIterator(owlModel.listInstances(counted_concept,
						inferred));
			else if (command.equals(countSubClasses))
				it = new URIResourceIteratorFilteringResourceIterator(owlModel.listSubClasses(
						counted_concept, inferred));
			else
				throw new IllegalArgumentException("specify the operation for retrieving ontology resources");
			while (it.streamOpen()) {
				it.getNext();
				count++;
			}
			it.close();
			System.out.println("# of root domain concepts in " + ontoInfo.getModelID() + " : " + count);
			logger.info("End counting root domain concepts...");

		}

	}

}
