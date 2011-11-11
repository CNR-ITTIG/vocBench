package org.fao.aoscs.client.module.search.service;

import java.util.ArrayList;

import org.fao.aoscs.client.module.search.widgetlib.AOSSuggestOracleRequest;
import org.fao.aoscs.domain.InitializeSearchData;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.SearchParameterObject;
import org.fao.aoscs.domain.SearchResultObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.ui.SuggestOracle;

@RemoteServiceRelativePath("search")
public interface SearchService extends RemoteService{
 
	public InitializeSearchData initData(OntologyInfo ontoInfo);
	public ArrayList<String[]> getSchemes(OntologyInfo ontoInfo);
	public String getSearchResultsSize (SearchParameterObject searchObj, OntologyInfo ontoInfo);
	public ArrayList<SearchResultObject> requestSearchResultsRows (com.google.gwt.gen2.table.client.TableModelHelper.Request request, SearchParameterObject searchObj, OntologyInfo ontoInfo); 
	public SuggestOracle.Response getSuggestions(AOSSuggestOracleRequest req, boolean includeNotes, ArrayList<String> languages, OntologyInfo ontoInfo);
	public Integer indexOntology(OntologyInfo ontoInfo);
	public boolean reloadSearchEngine(OntologyInfo ontoInfo);
	
	public static class SearchServiceUtil{
		private static SearchServiceAsync instance;
		public static SearchServiceAsync getInstance()
		{
			if (instance == null) {
				instance = (SearchServiceAsync) GWT.create(SearchService.class);
			}
			return instance;
		}
    }
}

