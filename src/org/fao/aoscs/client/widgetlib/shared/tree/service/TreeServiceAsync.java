package org.fao.aoscs.client.widgetlib.shared.tree.service;

import java.util.ArrayList;

import org.fao.aoscs.domain.OntologyInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TreeServiceAsync<T> {
	public void getTreeObject(String rootNode, OntologyInfo ontoInfo, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList, AsyncCallback callback);
	public void getTreePath(String targetItem, String rootConcept, OntologyInfo ontoInfo, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList,AsyncCallback callback);
	public void getSubVocabInfo(String GeoRootNode, String SciRootNode, OntologyInfo ontoInfo, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList, AsyncCallback callback);
}
