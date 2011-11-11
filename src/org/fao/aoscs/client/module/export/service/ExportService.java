package org.fao.aoscs.client.module.export.service;
 
import java.util.HashMap;

import org.fao.aoscs.domain.ExportParameterObject;
import org.fao.aoscs.domain.InitializeExportData;
import org.fao.aoscs.domain.OntologyInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("export")
public interface ExportService extends RemoteService{
	
	public InitializeExportData initData(OntologyInfo ontoInfo);
	public String getExportData(ExportParameterObject exp,OntologyInfo ontoInfo);
	public HashMap<String, String> getExportDataAndFilename(ExportParameterObject exp,OntologyInfo ontoInfo);
	public void markExportAsRecentChange(ExportParameterObject exp,int userId, int actionId, OntologyInfo ontoInfo);
	
	public static class ExportServiceUtil{
		private static ExportServiceAsync instance;
		public static ExportServiceAsync getInstance()
		{
			if (instance == null) {
				instance = (ExportServiceAsync) GWT.create(ExportService.class);
			}
			return instance;
		}
    }
}
