package org.fao.aoscs.client.module.consistency.service;

import java.util.HashMap;
import java.util.List;

import org.fao.aoscs.domain.Consistency;
import org.fao.aoscs.domain.ConsistencyInitObject;
import org.fao.aoscs.domain.OntologyInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("consistency")
public interface ConsistencyService extends RemoteService{

	public ConsistencyInitObject getInitData(OntologyInfo ontoInfo);
	public HashMap<String, Consistency> getConsistencyQueue(int selection, OntologyInfo ontoInfo);
	public HashMap<String, Consistency> updateConsistencyQueue(List<Consistency> value, int selection, OntologyInfo ontoInfo);
	
	public static class ConsistencyServiceUtil{
		private static ConsistencyServiceAsync instance;
		public static ConsistencyServiceAsync getInstance()
		{
			if (instance == null) {
				instance = (ConsistencyServiceAsync) GWT.create(ConsistencyService.class);
			}
			return instance;
		}
    }
}
