package org.fao.aoscs.client.module.search.service;

import java.util.ArrayList;

import org.fao.aoscs.client.module.search.widgetlib.AOSSuggestOracleRequest;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.SearchParameterObject;
import com.google.gwt.gen2.table.client.TableModelHelper.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SearchServiceAsync<T> {
	public void initData(OntologyInfo ontoInfo, AsyncCallback callback);
	public void getSchemes(OntologyInfo ontoInfo, AsyncCallback callback);
	public void getSearchResultsSize (SearchParameterObject searchObj, OntologyInfo ontoInfo, AsyncCallback callback);
	public void requestSearchResultsRows (Request request, SearchParameterObject searchObj, OntologyInfo ontoInfo,AsyncCallback callback);
	public void getSuggestions(AOSSuggestOracleRequest req, boolean includeNotes, ArrayList<String> languages, OntologyInfo ontoInfo, AsyncCallback callback);
	public void indexOntology (OntologyInfo ontoInfo, AsyncCallback callback);
	public void reloadSearchEngine (OntologyInfo ontoInfo, AsyncCallback callback);
}
