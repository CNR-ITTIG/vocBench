package org.fao.aoscs.client.widgetlib.shared.dialog;

import java.util.ArrayList;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.concept.widgetlib.InfoTab;
import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.client.module.search.widgetlib.ResultPanel;
import org.fao.aoscs.client.module.search.widgetlib.SuggestBoxAOS;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;
import org.fao.aoscs.client.widgetlib.shared.tree.ConceptTreeAOS;
import org.fao.aoscs.client.widgetlib.shared.tree.TreeAOS;
import org.fao.aoscs.client.widgetlib.shared.tree.TreeItemAOS;
import org.fao.aoscs.domain.SearchParameterObject;
import org.fao.aoscs.domain.TreeObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ConceptBrowser extends FormDialogBox {

	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private LoadingDialog ld = new LoadingDialog();
	private ConceptTreeAOS conceptTree = null;	
	private ArrayList<TreeObject> ctObj;
	
	private SuggestBoxAOSWB searchText = new SuggestBoxAOSWB(new MultiWordSuggestOracle());
	
	private SearchParameterObject searchObj = new SearchParameterObject();

	private ResultPanel resultPanel = null;
	private ScrollPanel sc = new ScrollPanel();
	private DeckPanel modulePanel = new DeckPanel();
	private HorizontalPanel linkPanel = new HorizontalPanel();
		
	public ConceptBrowser() 
	{
		super();		
		setText(constants.conceptBrowser());
		sc.setSize("700px", "400px");
		
		modulePanel.setSize("700px", "400px");
		modulePanel.add(sc);
		selectWidget(true);
		
		VerticalPanel bodyPanel = new VerticalPanel();
		bodyPanel.setSize("700px", "400px");	
		bodyPanel.add(getSearch());
		bodyPanel.add(modulePanel);
		addWidget(bodyPanel,true);			
	}
	
	private void selectWidget(boolean showConceptTree)
	{
		//submit.setVisible(showConceptTree);
		linkPanel.setSize("100%", "100%");
		linkPanel.clear();
		linkPanel.add(new Spacer("30px", "100%"));
		HorizontalPanel hp = new HorizontalPanel();
		if(showConceptTree)
		{
			hp = getLeftBottomWidgetConcept();
			modulePanel.showWidget(modulePanel.getWidgetIndex(sc));
		}
		else
		{
			hp = getLeftBottomWidgetSearch();
			modulePanel.showWidget(modulePanel.getWidgetIndex(resultPanel));
		}
		Spacer spacer = new Spacer("100%", "100%");
		linkPanel.add(spacer);
		linkPanel.add(hp);
		linkPanel.add(new Spacer("15px", "100%"));
		linkPanel.setCellWidth(spacer, "100%");
		linkPanel.setCellVerticalAlignment(hp, HasVerticalAlignment.ALIGN_MIDDLE);
		linkPanel.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_RIGHT);
        
	}
	private HorizontalPanel getLeftBottomWidgetSearch()
	{
		
		HTML viewTree = new HTML(constants.buttonViewConceptTree());
		viewTree.setTitle(constants.buttonViewConceptTree());
		viewTree.setWordWrap(false);
		viewTree.setStyleName("quick-link");
		viewTree.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				selectWidget(true);
			}
		});
		
		HorizontalPanel leftBottomWidget = new HorizontalPanel();
		leftBottomWidget.add(new Spacer("15px", "100%"));
		leftBottomWidget.add(viewTree);
		return leftBottomWidget;
	}
	
	private HorizontalPanel getLeftBottomWidgetConcept()
	{
		Image reload = new Image("images/reload-grey.gif");
		reload.setTitle(constants.conceptReload());
		reload.setStyleName("quick-link");
		reload.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				sc.clear();
				sc.add(ld);
				reload();
			}
		});
		
		HTML reloadText = new HTML(constants.conceptReload());
		reloadText.setTitle(constants.conceptReload());
		reloadText.setWordWrap(false);
		reloadText.setStyleName("quick-link");
		reloadText.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				sc.clear();
				sc.add(ld);
				reload();
			}
		});
		
		HorizontalPanel leftBottomWidget = new HorizontalPanel();
		leftBottomWidget.add(new Spacer("15px", "100%"));
		leftBottomWidget.add(reload);
		leftBottomWidget.add(new Spacer("5px", "100%"));
		leftBottomWidget.add(reloadText);
		
		if(resultPanel!=null)
		{
			HTML viewSearchResult = new HTML(constants.buttonViewSearchResult());
			viewSearchResult.setTitle(constants.buttonViewSearchResult());
			viewSearchResult.setWordWrap(false);
			viewSearchResult.setStyleName("quick-link");
			viewSearchResult.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					selectWidget(false);
				}
			});
			
			
			leftBottomWidget.add(new Spacer("5px", "100%"));
			leftBottomWidget.add(new Spacer("5px", "100%", "|"));
			leftBottomWidget.add(new Spacer("5px", "100%"));
			leftBottomWidget.add(viewSearchResult);
		}
		
		return leftBottomWidget;
	}
	
	@SuppressWarnings("unchecked")
	public void reload()
	{
		AsyncCallback<ArrayList<TreeObject>> callback = new AsyncCallback<ArrayList<TreeObject>>(){
			public void onSuccess(ArrayList<TreeObject> result){
				ctObj = result;
				conceptTree = new ConceptTreeAOS(ctObj, TreeAOS.TYPE_CONCEPT_BROWSER);
				conceptTree.setSize("99%", "99%");
				sc.clear();	
				sc.setSize("700px", "400px");
				sc.add(conceptTree);					
			}
			public void onFailure(Throwable caught){
				Window.alert(constants.conceptLoadFail());
			}
		};
		Service.treeService.getTreeObject(ModelConstants.CDOMAINCONCEPT, MainApp.userOntology, !MainApp.userPreference.isHideNonpreferred(), MainApp.userPreference.isHideDeprecated(), MainApp.userSelectedLanguage,callback);
	}

	public void showBrowser() 
	{		
		sc.clear();
		sc.setSize("700px", "400px");
		sc.add(ld);
		show();
		if(conceptTree == null)
		{			
			reload();			
		}
		else{
			sc.clear();
			sc.setSize("700px", "400px");
			sc.add(conceptTree);	
		}
			
	}
	
	public HandlerRegistration addSubmitClickHandler(ClickHandler handler) {
		return this.submit.addClickHandler(handler);
	}

	
	public String getSelectedItem(){
		TreeItemAOS item = (TreeItemAOS) conceptTree.tree.getSelectedItem();
		return item.getText();
		//TreeObject cObj =  (TreeObject) item.getValue();
		//return cObj.getLabel();				
	}
	
	public TreeObject getTreeObject(){
		TreeItemAOS item = (TreeItemAOS) conceptTree.tree.getSelectedItem();
		TreeObject treeObj =  (TreeObject) item.getValue();
		return treeObj;
	}
	
private class SuggestBoxAOSWB extends SuggestBoxAOS {
		
		public SuggestBoxAOSWB(MultiWordSuggestOracle oracle){
			super(oracle);
		}
		
		public void onSubmit() {
			
			doSearch();
		}
	}
	
	private HorizontalPanel getSearch()
    {
		searchText.setWidth("200px");
		
		Button btn = new Button(constants.buttonSearch());
        btn.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                doSearch();
            }
        });
        HorizontalPanel simpleSearch = new HorizontalPanel();
        simpleSearch.setWidth("100%");
        simpleSearch.add(searchText);
        simpleSearch.add(new Spacer("5px", "100%"));
        simpleSearch.add(btn);
        simpleSearch.setCellVerticalAlignment(searchText, HasVerticalAlignment.ALIGN_MIDDLE);
        simpleSearch.setCellVerticalAlignment(btn, HasVerticalAlignment.ALIGN_MIDDLE);

        HorizontalPanel hp = new HorizontalPanel();
        hp.setStyleName("maintopbar");
        hp.setSize("100%", "40px");
        hp.add(simpleSearch);
        hp.add(linkPanel);
        hp.setCellWidth(linkPanel, "100%");
        DOM.setStyleAttribute(hp.getElement(), "paddingRight", "10px");
        DOM.setStyleAttribute(hp.getElement(), "paddingLeft", "10px");
        hp.setCellVerticalAlignment(simpleSearch, HasVerticalAlignment.ALIGN_MIDDLE);
        return hp;
    }

    private void doSearch()
    {
        searchObj.setRegex(SearchParameterObject.CONTAIN);
        searchObj.clearSelectedLanguage();
        for(String lang : MainApp.languageDict.keySet())
        {
            searchObj.addSelectedLanguage(lang.toLowerCase());
        }

        if (searchText.getText().length() > 0)
        {
            searchObj.setKeyword(searchText.getText());
        }
        else
            searchObj.setKeyword("");
        
        resultPanel = new ResultPanel();
        resultPanel.setSize("700px", "400px");
        
        modulePanel.add(resultPanel);
        selectWidget(false);
        
        resultPanel.search(searchObj, ModuleManager.MODULE_CONCEPT_BROWSER);
    }
    
    public void gotoItem(String targetItem){
    	conceptTree.gotoItem(targetItem,  ModelConstants.CDOMAINCONCEPT, InfoTab.term, false, MainApp.userPreference.isHideDeprecated(), MainApp.userSelectedLanguage);
    	selectWidget(false);
	}
}