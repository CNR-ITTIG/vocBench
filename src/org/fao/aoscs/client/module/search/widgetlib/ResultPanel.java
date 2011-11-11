package org.fao.aoscs.client.module.search.widgetlib;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.panel.BodyPanel;
import org.fao.aoscs.domain.SearchParameterObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ResultPanel extends Composite{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private VerticalPanel panel = new VerticalPanel();
	private SearchTable searchTable = new SearchTable();
	private int type = -1;
	
	public ResultPanel(){
		panel.setSize("100%", "100%");
		initWidget(panel);
	}

	public void clearPanel(){
		panel.clear();
	}
	
	public SearchTable getSearchTable()
	{
		return searchTable;
	}
	
	public void search(final SearchParameterObject searchObj){
       	ModuleManager.getMainApp().addSearchLastResultPanel();
		search(searchObj, ModuleManager.MODULE_SEARCH);
	}
	
	public void search(final SearchParameterObject searchObj, int type1){
		this.type = type1;
		clearPanel();	
		// Get result from server 
		LoadingDialog hp = new LoadingDialog(constants.searchSearching());
		panel.setSize("100%", "100%");
		panel.add(hp);
		panel.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(hp, HasVerticalAlignment.ALIGN_MIDDLE);
		
		AsyncCallback<Object> callback = new AsyncCallback<Object>()
		{
			public void onSuccess(Object result)
			{
				clearPanel();
				int resultSize = Integer.parseInt((String) result);

				searchTable = new SearchTable();
				searchTable.setSearchTable(searchObj, resultSize, type);
				//searchTable.getLayout().setHeight("100%");
				
				VerticalPanel bodyPanel = new VerticalPanel();	
				bodyPanel.add(searchTable.getLayout());	
				bodyPanel.setSpacing(10);
				bodyPanel.setCellHeight(searchTable.getLayout(), "100%");
				bodyPanel.setCellWidth(searchTable.getLayout(), "100%");
				bodyPanel.setSize("100%", "100%");
				bodyPanel.setCellVerticalAlignment(searchTable.getLayout(), HasVerticalAlignment.ALIGN_TOP);
				bodyPanel.setCellHorizontalAlignment(searchTable.getLayout(), HasHorizontalAlignment.ALIGN_CENTER);
				
				BodyPanel vvp = null;
				if(type==ModuleManager.MODULE_CONCEPT_BROWSER)
				{
					vvp = new BodyPanel(constants.searchResults() , bodyPanel , null, 700, 400);
				}
				else
				{
					vvp = new BodyPanel(constants.searchResults() , bodyPanel , null);
				}
				
				panel.clear();
				panel.add(vvp);
				panel.setCellHeight(vvp, "100%");
				panel.setCellWidth(vvp, "100%");
				panel.setCellHorizontalAlignment(vvp, HasHorizontalAlignment.ALIGN_CENTER);
				panel.setCellVerticalAlignment(vvp, HasVerticalAlignment.ALIGN_MIDDLE);

			}
			public void onFailure(Throwable caught){
				Window.alert(constants.searchResultSizeFail());
			}
		};
		Service.searchSerice.getSearchResultsSize(searchObj, MainApp.userOntology, callback);
	}
	
	public void filterByLanguage(){
		searchTable.filterByLanguage();
	}
	
}
