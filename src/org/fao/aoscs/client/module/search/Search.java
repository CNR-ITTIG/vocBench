package org.fao.aoscs.client.module.search;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.module.search.service.SearchService.SearchServiceUtil;
import org.fao.aoscs.client.module.search.widgetlib.SearchOption;
import org.fao.aoscs.client.utility.DefaultCallback;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.domain.InitializeSearchData;
import org.fao.aoscs.domain.SearchParameterObject;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Search extends Composite{
	
	private VerticalPanel panel = new VerticalPanel();
	
	public SearchOption optionTable;
	
	public Search(final String show, final SearchParameterObject searchObj)
	{	
		LoadingDialog loadingDialog = new LoadingDialog();
		panel.clear();
		panel.setSize("100%", "100%");
	    panel.add(loadingDialog);
	    panel.setCellHorizontalAlignment(loadingDialog, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(loadingDialog, HasVerticalAlignment.ALIGN_MIDDLE);
		
		DefaultCallback callback = new DefaultCallback() {
			public void onSuccess(Object result) {
				InitializeSearchData initData = (InitializeSearchData) result;
				optionTable = new SearchOption(initData, show, searchObj);				
				panel.clear();
				panel.setSize("100%", "100%");
				panel.add(optionTable);
				panel.setCellHorizontalAlignment(optionTable, HasHorizontalAlignment.ALIGN_CENTER);
				panel.setCellVerticalAlignment(optionTable, HasVerticalAlignment.ALIGN_TOP);
			}
			public void onFailure(Throwable caught) {
				super.onFailure(caught);
			}
		};
		SearchServiceUtil.getInstance().initData(MainApp.userOntology, callback);
	
		initWidget(panel);
	}
	
}