package org.fao.aoscs.server.owlart;

import it.uniroma2.art.owlart.models.OWLModel;

import org.fao.aoscs.domain.OntologyInfo;

/**
 * @author Odnamar
 * 
 *         The interface for model factories. Whenever a wrapper for a given triple store technology is added
 *         to the AGROVOC Workbench, a factory implementing this interface has to be provided
 * 
 */
public interface OWLARTModelFactory {

	public OWLModel createOWLModel(OntologyInfo ontoInfo);

	public OWLModel getOWLModel(OntologyInfo ontoInfo);

}
