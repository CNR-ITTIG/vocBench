package org.fao.aoscs.client.module.consistency.service;

import java.util.List;

import org.fao.aoscs.domain.OntologyInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ConsistencyServiceAsync<T> {
	public void getInitData(OntologyInfo ontoInfo, AsyncCallback callback);
	public void getConsistencyQueue(int selection, OntologyInfo ontoInfo, AsyncCallback callback);
	public void updateConsistencyQueue(List value, int selection, OntologyInfo ontoInfo,AsyncCallback callback);
}
