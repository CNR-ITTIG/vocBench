package org.fao.aoscs.client.widgetlib.shared.tree.service;

import java.util.ArrayList;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.SubVocabObject;
import org.fao.aoscs.domain.TreeObject;
import org.fao.aoscs.domain.TreePathObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("tree")
public interface TreeService extends RemoteService {
	public ArrayList<TreeObject> getTreeObject(String rootNode, OntologyInfo ontoInfo, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList);
	public TreePathObject getTreePath(String targetItem, String rootConcept, OntologyInfo ontoInfo, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList);
	public SubVocabObject getSubVocabInfo(String GeoRootNode, String SciRootNode, OntologyInfo ontoInfo, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList);

	public static class TreeServiceUtil{
		private static TreeServiceAsync instance;
		public static TreeServiceAsync getInstance(){
			if (instance == null) {
				instance = (TreeServiceAsync) GWT.create(TreeService.class);
			}
			return instance;
      }
    }
}