package org.fao.aoscs.client.module.concept.widgetlib;

import java.util.ArrayList;
import java.util.Iterator;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.classification.widgetlib.ClassificationDetailTab;
import org.fao.aoscs.client.module.concept.ConceptTemplate;
import org.fao.aoscs.client.module.constant.ConceptActionKey;
import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.ConceptBrowser;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.label.LabelAOS;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabel;
import org.fao.aoscs.domain.ConceptMappedObject;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.PermissionObject;
import org.fao.aoscs.domain.TermObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class ConceptMap extends ConceptTemplate{
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private AddMapConcept addMapConcept;
	private DeleteMapConcept deleteMapConcept;
	
	public ConceptMap(PermissionObject permisstionTable,InitializeConceptData initData, ConceptDetailTabPanel conceptDetailPanel, ClassificationDetailTab classificationDetailPanel){
		super(permisstionTable,initData, conceptDetailPanel, classificationDetailPanel);
	}
	
	private void attachNewButton(){
		functionPanel.clear();
		LinkLabel add = new LinkLabel("images/add-grey.gif", constants.conceptAddMappedConcept(), constants.conceptAddMappedConcept());
		add.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(addMapConcept == null || !addMapConcept.isLoaded)
					addMapConcept = new AddMapConcept();
				addMapConcept.show();
			}
		});			
		functionPanel.add(add);
	}
	
	private HorizontalPanel convert2ConceptItem(ConceptObject cObj){
		HorizontalPanel hp = new HorizontalPanel();
		if(cObj.getUri().startsWith(ModelConstants.ONTOLOGYBASENAMESPACE)){
			hp.add(new Image("images/concept_logo.gif"));
		}else{
			hp.add(new Image("images/category_logo.gif"));
		}
		hp.setSpacing(2);
		String label = "";
		if(!cObj.getTerm().isEmpty()){
			Iterator<String> it = cObj.getTerm().keySet().iterator();
			while(it.hasNext()){
				String termIns = (String) it.next();
				TermObject tObj = (TermObject) cObj.getTerm().get(termIns);
				//if(tObj.isMainLabel())
				{
					label = label + tObj.getLabel() + "<b>(</b>"+tObj.getLang()+"<b>)</b>";
				}
			}
			hp.add(new HTML(label));
		}
		return hp;
	}
	
	private HorizontalPanel getFunctionBotton(final ConceptObject cObj){
		HorizontalPanel hp = new HorizontalPanel();
		 
		hp.clear();
		hp.setSpacing(3);
	/*	Image edit = new Image("images/edit-grey.gif");
		edit.setStyleName("cursor-hand");
		edit.setTitle("Edit relationship");
		edit.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				new EditMapConcept(cObj).show();
			}
		});*/
		LinkLabel delete = new LinkLabel("images/delete-grey.gif", constants.conceptDeleteMappedConcept());
		delete.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(deleteMapConcept == null || !deleteMapConcept.isLoaded)
					deleteMapConcept = new DeleteMapConcept(cObj);
				deleteMapConcept.show();
			}
		});			
		//hp.add(edit);
		hp.add(delete);
		//hp.setCellVerticalAlignment(edit, HasVerticalAlignment.ALIGN_MIDDLE);
		hp.setCellVerticalAlignment(delete, HasVerticalAlignment.ALIGN_MIDDLE);
		
		hp.add(convert2ConceptItem(cObj));
		return hp;
	}
	
	public void initLayout(){
		this.sayLoading();
		if(cDetailObj!=null && cDetailObj.getConceptMappedObject()!=null)
		{
			initData(cDetailObj.getConceptMappedObject());
		}
		else
		{
			AsyncCallback<ConceptMappedObject> callback = new AsyncCallback<ConceptMappedObject>(){
			public void onSuccess(ConceptMappedObject result) {
				cDetailObj.setConceptMappedObject(result);
				initData(result);
			}
			public void onFailure(Throwable caught) {
				Window.alert(constants.conceptGetMappedConceptFail());
			}
			};
			Service.conceptService.getMappedConcept(conceptObject.getName(), MainApp.userOntology, callback);
		}
	}
	
	private void initData(ConceptMappedObject cmObj)
	{
		clearPanel();
		attachNewButton(); 
		
		if(!cmObj.isEmpty()){
			ArrayList<ConceptObject> conceptList = cmObj.getConceptListOnly();
			Grid table = new Grid(conceptList.size()+1,1);
			table.setWidget(0, 0, new HTML(constants.conceptMappedConcept()));
			for (int i = 0; i < conceptList.size(); i++) {
				ConceptObject cObj = (ConceptObject) conceptList.get(i);
				table.setWidget(i+1, 0, getFunctionBotton(cObj));
			}
			if(conceptObject.getBelongsToModule()==ConceptObject.CLASSIFICATIONMODULE) classificationDetailPanel.tab2Panel.getTabBar().setTabHTML(InfoTab.conceptmap, Convert.replaceSpace(conceptList.size()>1?constants.conceptMappedConcepts():constants.conceptMappedConcept())+"&nbsp;("+(conceptList.size())+")");
			conceptRootPanel.add(GridStyle.setTableConceptDetailStyleTop(table,"gstFR1","gstFC1","gstR1","gstPanel1",true));
		}else{
			Label sayNo = new Label(constants.conceptNoMappedConcept());
			if(conceptObject.getBelongsToModule()==ConceptObject.CLASSIFICATIONMODULE) classificationDetailPanel.tab2Panel.getTabBar().setTabHTML(InfoTab.conceptmap, Convert.replaceSpace(constants.conceptMappedConcept()+"&nbsp;(0)"));
			conceptRootPanel.add(sayNo);
			conceptRootPanel.setCellHorizontalAlignment(sayNo, HasHorizontalAlignment.ALIGN_CENTER);
		}
	}
	
	private class AddMapConcept extends FormDialogBox{
		private LabelAOS selectedConcept;
		private Image browse;
		
		public AddMapConcept(){
			super(constants.buttonCreate(), constants.buttonCancel());
			setWidth("400px");
			this.setText(constants.conceptAddMappedConcept());
			this.initLayout();
		}

		public void initLayout() {
			selectedConcept = new LabelAOS("--None--");
			
			browse = new Image("images/browseButton3-grey.gif");
			browse.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					final ConceptBrowser cb =((MainApp) RootPanel.get().getWidget(0)).conceptBrowser; 
					cb.showBrowser();
					cb.addSubmitClickHandler(new ClickHandler()
					{
						public void onClick(ClickEvent event) {
							selectedConcept.setText(cb.getSelectedItem(),cb.getTreeObject().getName());
						}					
					});						
				}
			});
			DOM.setStyleAttribute(browse.getElement(), "cursor", "pointer");
			
		
			HorizontalPanel hp = new HorizontalPanel();
			hp.add(selectedConcept);
			hp.add(browse);
			hp.setCellHorizontalAlignment(selectedConcept, HasHorizontalAlignment.ALIGN_LEFT);
			hp.setCellHorizontalAlignment(browse, HasHorizontalAlignment.ALIGN_RIGHT);
			hp.setWidth("100%");
			
			Grid table = new Grid(1,2);
			table.setWidget(0, 0, new HTML(constants.conceptTitle()));
			table.setWidget(0, 1, hp);
			table.setWidth("100%");
			table.getColumnFormatter().setWidth(1, "80%");
			
			addWidget(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));
		}

		public boolean passCheckInput() {
			boolean pass = false;
			if(selectedConcept.getText().equals("--None--")){
				pass = false;
			}else {
				pass = true;
			}
			return pass;
		}

		public void onSubmit() {
			sayLoading();
			String cObj = (String) selectedConcept.getValue();

			AsyncCallback<ConceptMappedObject> callback = new AsyncCallback<ConceptMappedObject>(){
				public void onSuccess(ConceptMappedObject results){
					cDetailObj.setConceptMappedObject(results);
					ConceptMap.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.conceptAddMappedConceptFail());
				}
			};
			
			OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.mappingCreate);
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.mappingCreate));
			
			Service.conceptService.addMappedConcept(MainApp.userOntology, actionId, status, MainApp.userId, cObj, conceptObject.getName(), callback);
		}
	}
	
	private class DeleteMapConcept extends FormDialogBox implements ClickHandler
	{
		private ConceptObject cObj;
		
		public DeleteMapConcept(ConceptObject cObj){
			super(constants.buttonDelete(), constants.buttonCancel());
			this.cObj = cObj;
			this.setText(constants.conceptDeleteMappedConcept());
			setWidth("400px");
			initLayout();
		}
		
		public void initLayout() {
			HTML message = new HTML(constants.conceptDeleteMappedConceptWarning());			
			Grid table = new Grid(1,2);
			table.setWidget(0,0,getWarningImage());
			table.setWidget(0,1, message);
			table.setWidth("100%");			
			addWidget(table);
		}
		
		public void onSubmit() {
			sayLoading();
			AsyncCallback<ConceptMappedObject> callback = new AsyncCallback<ConceptMappedObject>(){
				public void onSuccess(ConceptMappedObject results){
					cDetailObj.setConceptMappedObject(results);
					ConceptMap.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.conceptDeleteMappedConceptFail());
				}
			};
			OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.mappingDelete);
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.mappingDelete));
			
			Service.conceptService.deleteMappedConcept(MainApp.userOntology, actionId, status, MainApp.userId, cObj, conceptObject, callback);
		}
	}
}
