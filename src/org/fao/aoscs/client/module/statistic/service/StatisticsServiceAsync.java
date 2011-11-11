package org.fao.aoscs.client.module.statistic.service;

import org.fao.aoscs.domain.OntologyInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface StatisticsServiceAsync<T> {
	
	public void getInitializeStatisticalData(OntologyInfo ontoInfo, AsyncCallback callback);
	public void getUserStat(AsyncCallback callback);
	public void getStatPerLang(AsyncCallback callback);
	public void getStatPerStatus(OntologyInfo ontoInfo, AsyncCallback callback);
	public void StatPerUser(OntologyInfo ontoInfo, AsyncCallback callback);
	public void getActionPerUser(OntologyInfo ontoInfo, AsyncCallback callback);
	public void getStatPerRelationship(OntologyInfo ontoInfo, AsyncCallback callback);
	public void getExportStat(OntologyInfo ontoInfo, AsyncCallback callback);

}
