package org.fao.aoscs.client.module.statistic.service;

import org.fao.aoscs.domain.InitializeStatisticalData;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.StatisticalData;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("statistics")
public interface StatisticsService extends RemoteService{
	
	public InitializeStatisticalData getInitializeStatisticalData(OntologyInfo ontoInfo);
	public StatisticalData getUserStat();
	public StatisticalData getStatPerLang();
	public StatisticalData getStatPerStatus(OntologyInfo ontoInfo);
	public StatisticalData StatPerUser(OntologyInfo ontoInfo);
	public StatisticalData getActionPerUser(OntologyInfo ontoInfo);
	public StatisticalData getStatPerRelationship(OntologyInfo ontoInfo);
	public StatisticalData getExportStat(OntologyInfo ontoInfo);

	public static class StatisticsServiceUtil{
		private static StatisticsServiceAsync instance;
		public static StatisticsServiceAsync getInstance()
		{
			if (instance == null) {
				instance = (StatisticsServiceAsync) GWT.create(StatisticsService.class);
			}
			return instance;
		}
    }
}

