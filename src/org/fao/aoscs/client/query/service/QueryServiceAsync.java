package org.fao.aoscs.client.query.service;


import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface QueryServiceAsync<T> {
	public void execHibernateSQLQuery(String query,AsyncCallback callback);
	public void hibernateExecuteSQLUpdate(ArrayList<String> queryList,AsyncCallback callback);
	public void hibernateExecuteSQLUpdate(String query, AsyncCallback callback);
}
