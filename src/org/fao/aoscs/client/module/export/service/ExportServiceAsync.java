package org.fao.aoscs.client.module.export.service;

import org.fao.aoscs.domain.ExportParameterObject;
import org.fao.aoscs.domain.OntologyInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ExportServiceAsync<T> {
	public void initData(OntologyInfo ontoInfo, AsyncCallback callback);
	public void getExportData(ExportParameterObject exp,OntologyInfo ontoInfo, AsyncCallback callback);
	public void getExportDataAndFilename(ExportParameterObject exp,OntologyInfo ontoInfo, AsyncCallback callback);
	public void markExportAsRecentChange(ExportParameterObject exp,int userId, int actionId, OntologyInfo ontoInfo, AsyncCallback callback);
}
