package org.fao.aoscs.client.module.concept.widgetlib;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.locale.LocaleMessages;
import org.fao.aoscs.client.module.classification.widgetlib.ClassificationDetailTab;
import org.fao.aoscs.client.module.concept.ConceptTemplate;
import org.fao.aoscs.client.module.concept.widgetlib.dialog.AddStructuredProperty;
import org.fao.aoscs.client.module.concept.widgetlib.dialog.AddStructuredProperty.AddStructuredPropertySuccessHandler;
import org.fao.aoscs.client.module.constant.ConceptActionKey;
import org.fao.aoscs.client.module.constant.OWLActionConstants;
import org.fao.aoscs.client.module.constant.OWLStatusConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.module.constant.TreeItemColor;
import org.fao.aoscs.client.module.term.TermDetailTabPanel;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.DialogBoxAOS;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.label.HTMLAOS;
import org.fao.aoscs.client.widgetlib.shared.label.ImageAOS;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabelAOS;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.ConceptTermObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.LanguageCode;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.PermissionObject;
import org.fao.aoscs.domain.TermObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.gen2.table.override.client.FlexTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ConceptStructuredProperty extends ConceptTemplate 
{
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private LocaleMessages messages = (LocaleMessages) GWT.create(LocaleMessages.class);
	
	private AddStructuredProperty addStructuredProperty;
	private EditStructuredProperty editStructuredProperty;
	private DeleteStructuredProperty	deleteStructuredProperty;
	private StructuredPropertyDetail	structuredPropertyDetail;

	private LinkLabelAOS addStructuredPropertyButton; 
	
	public ConceptStructuredProperty(PermissionObject permisstionTable,
			InitializeConceptData initData,
			ConceptDetailTabPanel conceptDetailPanel,
			ClassificationDetailTab classificationDetailPanel) {
		super(permisstionTable, initData, conceptDetailPanel,
				classificationDetailPanel);
	}
	
	private void attachNewImgButton(){
		// create new term button
		ConceptObject concept = getConceptObject();
		functionPanel.clear();
		// check concept status
		addStructuredPropertyButton = new LinkLabelAOS("images/add-grey.gif", "images/add-grey-disabled.gif", constants.conceptAddNewTerm(), constants.conceptAddNewTerm(), permissionTable.contains(OWLActionConstants.TERMCREATE, concept.getStatusID()), new ClickHandler() 
		{
			public void onClick(ClickEvent event) {
				if(addStructuredProperty == null  || !addStructuredProperty.isLoaded)
					addStructuredProperty = new AddStructuredProperty(initData, conceptObject);
				addStructuredProperty.addAddStructuredPropertySuccessHandler(new AddStructuredPropertySuccessHandler(){
					public void onAddStructuredPropertySuccess(ConceptTermObject results) {
						cDetailObj.setConceptTermObject(results);
						ConceptStructuredProperty.this.initData();	
					}
				});
				addStructuredProperty.show();
			}
		});
		this.functionPanel.add(addStructuredPropertyButton);
	}
	
	public void initLayout(){
		this.sayLoading();
		if(cDetailObj!=null && cDetailObj.getConceptTermObject()!=null)
		{
			initData(cDetailObj.getConceptTermObject());
		}
		else
		{
			AsyncCallback<ConceptTermObject> callback = new AsyncCallback<ConceptTermObject>(){
				public void onSuccess(ConceptTermObject result) {
					cDetailObj.setConceptTermObject(result);
					initData(result);
				}
				public void onFailure(Throwable caught) {
					Window.alert(constants.conceptGetTermFail());
				}
			};
		 
			Service.conceptService.getTerm(conceptObject.getName(), MainApp.userOntology, callback);
		}
	}
	
	private void initData(ConceptTermObject ctObj)
	{
		clearPanel();
		attachNewImgButton();
		if(!ctObj.isEmpty()){
			HashMap<String, ArrayList<TermObject>> termList = ctObj.getTermList();

			ArrayList<String> termlanglist = new ArrayList<String>(termList.keySet());
			ArrayList<String> sortedlanglist = new ArrayList<String>();
			for(LanguageCode langCode : MainApp.languageCode)
			{
				String lang = langCode.getLanguageCode().toLowerCase();
				
				if(termlanglist.contains(lang))
				{
					termlanglist.remove(lang);
					if(MainApp.userPreference.isHideNonselectedlanguages())
					{
						if(!MainApp.userSelectedLanguage.contains(lang))
						{
							lang = "";
						}
					}
					if(!lang.equals(""))
						sortedlanglist.add(lang);
				}
			}
			sortedlanglist.addAll(termlanglist);

			FlexTable table = new FlexTable();
			table.setWidget(0, 0, new HTML(constants.conceptLanguage()));
			table.setWidget(0, 1, new HTML(constants.conceptTerm()));
			
			table.getColumnFormatter().setWidth(1, "80%");
			int i=1;
			int count=0;
			for(String language: sortedlanglist){
				table.setWidget(i, 0, new HTML(getFullnameofLanguage(language) + " (" + language.toString() + ")"));
				ArrayList<TermObject> list = (ArrayList<TermObject>) termList.get(language);
				table.setWidget(i, 1, getTermTable(list));						
				i++;
				count += list.size();
			}
			if(conceptObject.getBelongsToModule()==ConceptObject.CONCEPTMODULE) 
				conceptDetailPanel.tabPanel.getTabBar().setTabHTML(0, Convert.replaceSpace( (count)>1? constants.conceptTerms():constants.conceptTerm() ) +"&nbsp;("+(count)+")" );
			if(conceptObject.getBelongsToModule()==ConceptObject.CLASSIFICATIONMODULE) 
				classificationDetailPanel.tab2Panel.getTabBar().setTabHTML(InfoTab.term, Convert.replaceSpace( (count)>1? constants.conceptTerms():constants.conceptTerm() )+"&nbsp;("+(count)+")");
			conceptRootPanel.add(GridStyle.setTableConceptDetailStyleTop(table,"gstFR1","gstFC1","gstR1","gstPanel1",true));
		}else{
			Label sayNo = new Label(constants.conceptNoTerm());
			if(conceptObject.getBelongsToModule()==ConceptObject.CONCEPTMODULE) conceptDetailPanel.tabPanel.getTabBar().setTabHTML(0, Convert.replaceSpace(constants.conceptTerm())+"&nbsp;(0)");
			if(conceptObject.getBelongsToModule()==ConceptObject.CLASSIFICATIONMODULE) classificationDetailPanel.tab2Panel.getTabBar().setTabHTML(0, Convert.replaceSpace(constants.conceptTerm())+"&nbsp;(0)");
			conceptRootPanel.add(sayNo);
			conceptRootPanel.setCellHorizontalAlignment(sayNo, HasHorizontalAlignment.ALIGN_CENTER);
		}
	}
	
	private HTMLTable getTermTable(ArrayList<TermObject> list){
		HTMLTable table = new Grid(list.size(),1);
		for (int i = 0; i < list.size(); i++) {
			TermObject tObj = (TermObject) list.get(i);
			table.setWidget(i, 0, getFunctionButton(tObj));
		}
		table.setWidth("100%");
		return table;
	}
	
	private HorizontalPanel getFunctionButton(final TermObject tObj){
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(2);
		
		// Edit Term
		boolean permission = permissionTable.contains(OWLActionConstants.TERMEDIT, OWLStatusConstants.getOWLStatusID(tObj.getStatus()));
		ImageAOS edit = new ImageAOS(constants.conceptEditTerm(), "images/edit-grey.gif", "images/edit-grey-disabled.gif", permission, new ClickHandler() 
		{
			public void onClick(ClickEvent event) {
				if(editStructuredProperty == null || !editStructuredProperty.isLoaded)
					editStructuredProperty = new EditStructuredProperty(tObj);
				editStructuredProperty.show();					
			}
		});
		hp.add(edit);
		
		// Delete Term
		permission = permissionTable.contains(OWLActionConstants.TERMDELETE, OWLStatusConstants.getOWLStatusID(tObj.getStatus()));
		ImageAOS delete = new ImageAOS(constants.conceptDeleteTerm(), "images/delete-grey.gif", "images/delete-grey-disabled.gif", permission, new ClickHandler(){
			public void onClick(ClickEvent event) {
				if(deleteStructuredProperty == null || !deleteStructuredProperty.isLoaded)
					deleteStructuredProperty = new DeleteStructuredProperty(tObj);
				deleteStructuredProperty.show();
			}
		});
		hp.add(delete);
		
		/*// Move Term
		ImageAOS move = new ImageAOS(constants.conceptMove(), "images/moveconcept-grey.gif", "images/moveconcept-grey.gif", permissionTable.contains(OWLActionConstants.TERMDELETE), new ClickHandler(){
			public void onClick(ClickEvent event) {
				MoveTerm moveTerm;
				if(tObj.isMainLabel())
				{
					moveTerm = new MoveTerm();
				}
				else
				{
					moveTerm = new MoveTerm(tObj);
				}
				moveTerm.show();
			}
		});
		
		if(tObj.getStatus().equals(OWLStatusConstants.DEPRECATED)  || tObj.getStatus().equals(OWLStatusConstants.DELETED) || tObj.getStatus().equals(OWLStatusConstants.PROPOSED_DEPRECATED))
			move.setVisible(false);
		hp.add(move);
		
		*/
		
		final HTMLAOS term = new HTMLAOS();
		term.setHTML(getTermColorByStatus(tObj.getLabel(), tObj.getStatus(), tObj.isMainLabel()), tObj);
		term.setStyleName(Style.Link);
		term.setTitle(tObj.getStatus());
		term.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(structuredPropertyDetail == null || !structuredPropertyDetail.isLoaded)
					structuredPropertyDetail = new StructuredPropertyDetail(conceptObject,(TermObject) term.getValue());
				structuredPropertyDetail.show();
			}
		});
		hp.add(term);
		
		Image wiki = MainApp.aosImageBundle.wikiIcon().createImage();
		wiki.setStyleName("cursor-hand");
		wiki.setTitle(constants.conceptWikipedia());
		wiki.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				 openURL("http://www.wikipedia.org/search-redirect.php?search="+tObj.getLabel()+"&language="+tObj.getLang() , "wikiWindow");
			}
		});
		hp.add(wiki);
		
		return hp;
	}
	
	public static native void openURL(String url , String windowName) /*-{
	   $wnd.open(url,windowName,'');
	}-*/;
	
	private String getTermColorByStatus(String label, String status, boolean isMainLabel){
		String item = new String();
		if(status!=null){
			if(status.equals(OWLStatusConstants.DEPRECATED)){
				item = "<font color=\""+TreeItemColor.COLOR_DEPRECATED+"\"><STRIKE>"+label+"</STRIKE></font>";
			}else if(status.equals(OWLStatusConstants.VALIDATED)){
				item="<font color=\""+TreeItemColor.COLOR_VALIDATED+"\">"+label+"</font>";
			}else if(status.equals(OWLStatusConstants.PUBLISHED)){
				item="<font color=\""+TreeItemColor.COLOR_PUBLISHED+"\">"+label+"</font>";
			}else if(status.equals(OWLStatusConstants.PROPOSED_DEPRECATED)){
				item="<font color=\""+TreeItemColor.COLOR_PROPOSED_DEPRECATED+"\">"+label+"</font>";
			}else if(status.equals(OWLStatusConstants.REVISED)){
				item="<font color=\""+TreeItemColor.COLOR_REVISED+"\">"+label+"</font>";
			}else if(status.equals(OWLStatusConstants.PROPOSED)){
				item="<font color=\""+TreeItemColor.COLOR_PROPOSED+"\">"+label+"</font>";
			}else if(status.equals(OWLStatusConstants.PROPOSED_GUEST)){
				item="<font color=\""+TreeItemColor.COLOR_PROPOSED_GUEST+"\">"+label+"</font>";
			}else if(status.equals(OWLStatusConstants.REVISED_GUEST)){
				item="<font color=\""+TreeItemColor.COLOR_REVISED_GUEST+"\">"+label+"</font>";
			}
		}else{
			item = label;
		}
		if(isMainLabel){
			item = item + "&nbsp;&nbsp;("+constants.conceptPreferred()+")"; 
		}
		return item;
	}
	
	public class EditStructuredProperty extends FormDialogBox implements ClickHandler{
		private TextBox term ;
		private ListBox lang ;
		private CheckBox main ;
		private TermObject tObj ;
		
		public EditStructuredProperty(TermObject tObj){
			super();
			this.tObj = tObj;
			this.setText(constants.conceptEditTerm());
			this.initLayout();
			setWidth("400px");
		}
		
		public void initLayout() {
			lang = new ListBox();
			lang = Convert.makeSelectedLanguageListBox((ArrayList<String[]>)MainApp.getLanguage(),tObj.getLang());
			lang.setWidth("100%");
			
			term = new TextBox();
			term.setText(tObj.getLabel());
			term.setWidth("100%");
			
			main = new CheckBox(constants.conceptPreferredTerm());
			if(tObj.isMainLabel()){
				main.setValue(tObj.isMainLabel());
				main.setEnabled(false);
			}
			
			Grid table = new Grid(2,2);
			table.setWidget(0, 0, new HTML(constants.conceptTerm()));
			table.setWidget(1, 0, new HTML(constants.conceptLanguage()));
			table.setWidget(0, 1, term);
			table.setWidget(1, 1, lang);
			table.setWidth("100%");
			table.getColumnFormatter().setWidth(1, "80%");
			
			VerticalPanel vp = new VerticalPanel();
			vp.add(GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1"));
			vp.add(main);
			vp.setSpacing(0);
			vp.setWidth("100%");
			vp.setCellHorizontalAlignment(main, HasHorizontalAlignment.ALIGN_RIGHT);
			
			addWidget(vp);
		}
		public boolean passCheckInput() {
			boolean pass = false;
			if(lang.getValue((lang.getSelectedIndex())).equals("") ||term.getText().equals("") ){
				pass = false;
			}else{
				pass = true;
			}
			return pass;
		}
		
		public void onSubmit() {
			sayLoading();
			OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.termEdit);
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.termEdit));

			TermObject termObjectNew = new TermObject();
			termObjectNew.setConceptUri(conceptObject.getUri());
			termObjectNew.setConceptName(conceptObject.getName());
			termObjectNew.setUri(tObj.getUri());
			termObjectNew.setName(tObj.getName());
			termObjectNew.setLabel(term.getText());
			termObjectNew.setLang(lang.getValue(lang.getSelectedIndex()));
			termObjectNew.setStatusID(status.getId());
			termObjectNew.setStatus(status.getStatus());
			termObjectNew.setMainLabel(main.getValue());

			Service.conceptService.editTerm(MainApp.userOntology, actionId, status, MainApp.userId, tObj, termObjectNew, conceptObject, new AsyncCallback<ConceptTermObject>(){
				public void onSuccess(ConceptTermObject results){
					cDetailObj.setConceptTermObject(results);
					ConceptStructuredProperty.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.conceptEditTermFail());
				}
			});
		}
		
	}
	
	public class DeleteStructuredProperty extends FormDialogBox implements ClickHandler{
		private TermObject tObj;
		
		public DeleteStructuredProperty(TermObject tObj){
			super(constants.buttonDelete(), constants.buttonCancel());
			this.tObj = tObj;			
			this.setText( constants.conceptDeleteTerm() );
			this.initLayout();
			setWidth("400px");
		}
		
		public void initLayout() {
			Grid table = new Grid(1,2);
			table.setWidget(0,0,getWarningImage());
			table.setWidget(0,1, getMessage(messages.conceptDeleteTermWarning(tObj.getLabel(),tObj.getLang())));
			addWidget(table);
		}
		
		public void onSubmit() {
			sayLoading();
			AsyncCallback<ConceptTermObject> callback = new AsyncCallback<ConceptTermObject>(){
				public void onSuccess(ConceptTermObject results){
					cDetailObj.setConceptTermObject(results);
					ConceptStructuredProperty.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.conceptDeleteTermFail());
				}
			};
			 
			OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.termDelete);
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.termDelete));
		
			Service.conceptService.deleteTerm(MainApp.userOntology,actionId, status, MainApp.userId, tObj, conceptObject, callback);
		}
	
	}
	
	public class StructuredPropertyDetail extends DialogBoxAOS implements ClickHandler{
		private VerticalPanel panel = new VerticalPanel();
		private Button cancel = new Button(constants.buttonClose());
		
		public StructuredPropertyDetail(ConceptObject conceptObject,TermObject termObject){
			String text = termObject.getLabel();
			if(text.length()>100)
				text = text.substring(0,70)+"...";
			this.setHTML(constants.conceptInformationFor()+" <i>"+text+" ("+termObject.getLang()+")</i>&nbsp;["+(termObject.isMainLabel()?constants.conceptPreferredTerm():constants.conceptNonPreferredTerm())+"]");
			TermDetailTabPanel termDetail = new TermDetailTabPanel(permissionTable, initData);
			
			VerticalPanel vp = new VerticalPanel();
			vp.setSize("100%", "100%");
			vp.add(termDetail);
			vp.setSpacing(5);
			
			panel.add(vp);
			panel.setCellHeight(vp, "100%");
			panel.setCellWidth(vp, "100%");
			termDetail.setURI(termObject,conceptObject );

			HorizontalPanel buttonPanel = new HorizontalPanel();
			buttonPanel.setSpacing(5);			
			buttonPanel.add(cancel);
			
			HorizontalPanel hp = new HorizontalPanel();
			hp.setSpacing(0);
			hp.setWidth("100%");
			hp.setStyleName("bottombar");
			hp.add(buttonPanel);			
			hp.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);
									
			panel.add(hp);
			cancel.addClickHandler(this);
			panel.setCellHorizontalAlignment(cancel, HasHorizontalAlignment.ALIGN_RIGHT);
			panel.setSize("100%", "100%");	
			setWidget(panel);
		}
	 
		public void onClick(ClickEvent event) {
			Widget sender = (Widget) event.getSource();
			if(sender.equals(cancel)){
				this.hide();
			}
		}
		
	}

}
