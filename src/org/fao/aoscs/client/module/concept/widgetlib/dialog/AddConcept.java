package org.fao.aoscs.client.module.concept.widgetlib.dialog;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.module.concept.widgetlib.InfoTab;
import org.fao.aoscs.client.module.concept.widgetlib.dialog.AddNewEnglishDefinition.AddNewEnglishDefinitionSuccessHandler;
import org.fao.aoscs.client.module.concept.widgetlib.dialog.AddNewTerm.AddNewTermSuccessHandler;
import org.fao.aoscs.client.module.constant.ConceptActionKey;
import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.client.module.constant.OWLActionConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.module.search.widgetlib.SearchTable;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.FlexDialogBox;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabel;
import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;
import org.fao.aoscs.client.widgetlib.shared.tree.ConceptTreeAOS;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.ConceptTermObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.SearchParameterObject;
import org.fao.aoscs.domain.TermObject;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AddConcept extends FlexDialogBox{

	private VerticalPanel container;
	private TextBox name;
    private ListBox language;
    private HorizontalPanel existingConcepts = new HorizontalPanel();
    private CheckBox preferedTerm;
    private Button checkExistance;
    private LinkLabel existStatus;
    private ListBox position;
    private ListBox namespace;
    private HorizontalPanel namespacePanel; 
    private TextBox uri;
    private HorizontalPanel uriPanel;
    private DisclosureWidget advancedOptions;
    private ConceptSearchTable searchTable;
    private AddNewEnglishDefinition addEnglishDefinition;
    private FlexTable table;
    private Image addNamespace;
    private ConceptObject selectedConceptObject;
    private InitializeConceptData initData;
    private ConceptTreeAOS tree;
    public CheckBox showAlsoNonpreferredTerms;
    private boolean isFirstCheckDone = false;
    private boolean isConceptPropertyShown = false;
    
	public String rootConceptURI = ModelConstants.CDOMAINCONCEPT;
	
    public AddConcept(ConceptTreeAOS tree, InitializeConceptData initData, ConceptObject selectedConceptObject, CheckBox showAlsoNonpreferredTerms) {
    	
    	super(constants.buttonCreate(), constants.buttonCancel());
    	this.tree = tree;
    	this.initData = initData;
    	this.selectedConceptObject = selectedConceptObject;
    	this.showAlsoNonpreferredTerms = showAlsoNonpreferredTerms;
    	setWidth("460px");
    	this.setText(constants.conceptCreateNew());
    	this.initLayout();
    }
    
    public void initLayout() {
    	
    	name = new TextBox();
    	name.setWidth("100%");
    	
    	language = Convert.makeListBoxWithValue((ArrayList<String[]>)MainApp.getLanguage());
    	language.setWidth("100%");
    	
    	checkExistance = new Button(constants.conceptCheckAvailability());
    	checkExistance.setWidth("100%");
    	checkExistance.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				doCheckExistance();
			}
    	});
    	
    	existStatus = new LinkLabel(null);
    	existStatus.setClickable(false);
    	existStatus.setIcon("images/warning-small.png");
    	existStatus.setLabelText(constants.conceptConceptExist());
    	
    	position = new ListBox();
    	position.setWidth("100%");
    	if(selectedConceptObject != null){
    	    position.addItem("--None--","");
    	    // remove option to create new "concept as sub-class" of selected concept according to permission table
    	    if(MainApp.permissionTable.contains(OWLActionConstants.CONCEPTCREATE, selectedConceptObject.getStatusID()))
    	    	position.addItem(constants.conceptChildConcept(),"subClass");
            position.addItem(constants.conceptSameLevelConcept(),"sameLevel");
    	}
    	position.addItem(constants.conceptTopLevelConcept(),"topLevel");
    	
    	preferedTerm = new CheckBox();
    	preferedTerm.setText(constants.conceptPreferredTerm());
    	
    	boolean editable = MainApp.groupId == 1 ? true : false;
    	addNamespace = new Image("images/add-grey.gif");
    	addNamespace.setStyleName(Style.Link);
    	addNamespace.setVisible(editable);
    	addNamespace.setTitle(constants.conceptAddNamespace());
    	addNamespace.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                new AddNamespace(namespace).show();
            }
        });
    	namespace = new ListBox();
    	namespace.setWidth("100%");
    	//namespace.addItem("--None--","");
    	
    	final Label uriPrefix = new Label();
    	uriPrefix.setWidth("100%");
    	
    	AsyncCallback<HashMap<String, String>> callback = new AsyncCallback<HashMap<String, String>>() {
            public void onSuccess(HashMap<String, String> results)
            {
            	for(String prefix:results.keySet())
            	{
            		namespace.addItem(results.get(prefix), results.get(prefix));
            		String ns = results.get(prefix);
            		// Automatically select namespace according to ONTOLOGYBASENAMESPACE 
            		if( ns.equals(ModelConstants.ONTOLOGYBASENAMESPACE)){
            			namespace.setItemSelected(namespace.getItemCount()-1, true);
            			uriPrefix.setText(namespace.getValue(namespace.getSelectedIndex()));
            		}
            	}
            }
            public void onFailure(Throwable caught)
            {
                Window.alert(constants.conceptAddNamespaceFail());
            }
        };
        Service.conceptService.getNamespaces(MainApp.userOntology, callback);
    
    	namespacePanel = new HorizontalPanel();
    	namespacePanel.setSize("100%", "100%");
    	namespacePanel.add(namespace);
    	namespacePanel.add(new Spacer("5px","100%"));
    	namespacePanel.add(addNamespace);
    	namespacePanel.setCellWidth(namespace, "100%");
    	namespacePanel.setCellVerticalAlignment(addNamespace, HasVerticalAlignment.ALIGN_MIDDLE);
    	
    	uri = new TextBox();
    	uri.setWidth("100%");
    	    	
    	uriPanel = new HorizontalPanel();
    	uriPanel.setSize("100%", "100%");
    	uriPanel.add(uriPrefix);
    	uriPanel.add(uri);
    	uriPanel.setCellWidth(uri, "100%");
    	uriPanel.setCellVerticalAlignment(uriPrefix, HasVerticalAlignment.ALIGN_MIDDLE);
    	uriPanel.setCellVerticalAlignment(uri, HasVerticalAlignment.ALIGN_MIDDLE);
    	
    	namespace.addChangeHandler(new ChangeHandler()
    	{
			public void onChange(ChangeEvent arg0) {
				uriPrefix.setText(namespace.getValue(namespace.getSelectedIndex()));
			}
    	});
    	
    	// Advanced options
    	FlexTable advancedOptionsTable = new FlexTable();
    	advancedOptionsTable.setWidget(0, 0, new HTML(constants.conceptNamespace()));
    	advancedOptionsTable.setWidget(0, 1, namespacePanel);
		
    	advancedOptionsTable.setWidget(1, 0, new HTML(constants.conceptUri()));
    	advancedOptionsTable.setWidget(1, 1, uriPanel);
    	
    	advancedOptionsTable.setWidth("100%");
    	advancedOptionsTable.getColumnFormatter().setWidth(0, "30%");
    	advancedOptionsTable.getColumnFormatter().setWidth(1, "70%");
		GridStyle.updateTableConceptDetailStyleleft(advancedOptionsTable,"gslRow1", "gslCol1", "gslPanel1");

    	advancedOptions = new DisclosureWidget(constants.conceptShowAdvOptions(), constants.conceptShowAdvOptionsHelp());
    	advancedOptions.setStyleContainer("new-concept-adv-options-show", "new-concept-adv-options-hide");
    	advancedOptions.setWidth("100%");
    	advancedOptions.setContent(advancedOptionsTable);
    	advancedOptions.setStyleName("gslPanel1");
    	
    	// Add Concept Form
    	table = new FlexTable();
    	table.setWidget(0, 0, new HTML(constants.conceptLabel()));
    	table.setWidget(0, 1, name);
    	
		table.setWidget(1, 0, new HTML(constants.conceptLanguage()));
		table.setWidget(1, 1, language);

		table.setWidget(2, 0, existStatus);
		table.setWidget(2, 1, checkExistance);
		table.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_CENTER);
		table.getCellFormatter().setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_CENTER);
		
		table.setWidget(3, 0, existingConcepts);
		table.getFlexCellFormatter().setColSpan(3, 0, 2);
		
		table.setWidget(4, 0, new HTML(constants.conceptPosition()));
		table.setWidget(4, 1, position);
		
		table.setWidget(5, 0, preferedTerm);
		table.getFlexCellFormatter().setColSpan(5, 0, 2);
		table.getCellFormatter().setHorizontalAlignment(5, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		
		table.setWidget(6, 0, advancedOptions);
		table.getFlexCellFormatter().setColSpan(6, 0, 2);
		
		table.setWidth("460px");		
		table.getColumnFormatter().setWidth(0, "30%");
		table.getColumnFormatter().setWidth(1, "70%");
		GridStyle.updateTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1");
		DOM.setStyleAttribute(table.getCellFormatter().getElement(5, 0),"backgroundColor", "#FFF");
		
		hideConceptExistStatus();
		hideExistingConcepts();
		hideConceptProperties();
		center();
		
		// Dialog Container
		container = new VerticalPanel(); 
		container.add(table);    	
		container.setWidth("100%");
		container.setStyleName("gslPanel1");
		addWidget(container);
    }
        
    public boolean passCheckInput() {
    	boolean pass = false;    
    	if(!isFirstCheckDone || !isConceptPropertyShown)
    		pass = true;
    	else if(passCheckLang() && passCheckLabel() && passCheckNamespace() && passCheckPos() && passCheckUri())
			pass = true;
		return pass;
    }
    
    public void show(){
    	super.show();    	
    }
    
    public void onSubmit(){
    	if(!isFirstCheckDone){
			doCheckExistance();
    	}
    	else if(isFirstCheckDone && !isConceptPropertyShown){
    		hideConceptExistStatus();
    		hideExistingConcepts();
    		showConceptProperties();
    		center();
    		isConceptPropertyShown = true;
    		// make term label and language read only
			name.setEnabled(false);
			language.setEnabled(false);
			checkExistance.setVisible(false);
			hideConceptExistStatus();
    	}
    	else{	
			OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptCreate);
			final int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptCreate));
			final TermObject tObj = new TermObject();
	    	tObj.setLabel(name.getText());
	    	tObj.setLang(language.getValue(language.getSelectedIndex()));
	    	tObj.setMainLabel(preferedTerm.getValue());
	    	tObj.setStatus(status.getStatus());
	    	
	    	final ConceptObject cObj = new ConceptObject();
	    	if(!uri.getText().equals(""))	cObj.setName(uri.getText());
	    	cObj.setStatus(status.getStatus());
	    	cObj.setStatusID(status.getId());
	    	cObj.setNameSpace(namespace.getValue(namespace.getSelectedIndex()));
			
			String conceptPosition = position.getValue(position.getSelectedIndex());
			ConceptObject cReferenceObj = new ConceptObject();
	    	if(conceptPosition.equals("topLevel"))
	    	{
	    	    cReferenceObj.setName(ModelConstants.CDOMAINCONCEPT);
	            conceptPosition = "subClass"; 
	    	}
	    	else
	    	{
	    	    cReferenceObj = selectedConceptObject;
	    	}
	    	
	    	final String cpos = conceptPosition;
	    	final ConceptObject cobj = cReferenceObj;
			
	    	final AsyncCallback<Object> callback = new AsyncCallback<Object>(){
				public void onSuccess(Object results){
					hideAddConcept();
					
					final ConceptObject conceptObject = (ConceptObject) results ;
					
					if(!language.getValue(language.getSelectedIndex()).equals("en")){							
			    		Window.alert(messages.conceptAddNewHint(name.getText(),language.getValue(language.getSelectedIndex())));
			    		if(addEnglishDefinition == null || !addEnglishDefinition.isLoaded)
			    			addEnglishDefinition = new AddNewEnglishDefinition(initData, conceptObject);
			    		addEnglishDefinition.addAddNewEnglishDefinitionSuccessHandler(new AddNewEnglishDefinitionSuccessHandler() {
							public void onAddNewEnglishDefinitionSuccess() {
								final AddNewTerm newTerm = new AddNewTerm(initData, conceptObject, name.getText() + " (" + language.getValue(language.getSelectedIndex()) + ")");
								newTerm.setIsLanguageExist(!MainApp.userSelectedLanguage.contains(tObj.getLang().toLowerCase()));
								newTerm.addFlexDialogClickedHandler(new FlexDialogClickedHandler() {
									public void onFlexDialogSubmitClicked(ClickEvent event) {
										newTerm.hide();
										
										tree.gotoItem(conceptObject.getUri(),rootConceptURI, InfoTab.term, showAlsoNonpreferredTerms.getValue(), MainApp.userPreference.isHideDeprecated(), MainApp.userSelectedLanguage);
									}
									public void onFlexDialogLoopClicked(ClickEvent event) {
									}
									public void onFlexDialogCancelClicked(ClickEvent event) {
										newTerm.hide();
										tree.gotoItem(conceptObject.getUri(),rootConceptURI, InfoTab.term, showAlsoNonpreferredTerms.getValue(), MainApp.userPreference.isHideDeprecated(), MainApp.userSelectedLanguage);
									}
								});
								newTerm.show();
							}
						});
			    		addEnglishDefinition.show();
			    	}
					else{
						ModuleManager.resetValidation();
						// Add term
						AddNewTerm newTerm = new AddNewTerm(initData, conceptObject, name.getText() + " (" + language.getValue(language.getSelectedIndex()) + ")");
						newTerm.addAddNewTermSuccessHandler(new AddNewTermSuccessHandler(){
							public void onAddNewTermSuccess(ConceptTermObject results) {
								tree.gotoItem(conceptObject.getUri(),rootConceptURI, InfoTab.term, showAlsoNonpreferredTerms.getValue(), MainApp.userPreference.isHideDeprecated(), MainApp.userSelectedLanguage);
							}
						});
						
						newTerm.addFlexDialogClickedHandler(new FlexDialogClickedHandler(){
							public void onFlexDialogCancelClicked(ClickEvent event) {
								tree.gotoItem(conceptObject.getUri(),rootConceptURI, InfoTab.term, showAlsoNonpreferredTerms.getValue(), MainApp.userPreference.isHideDeprecated(), MainApp.userSelectedLanguage);
							}
							public void onFlexDialogLoopClicked(ClickEvent event) {
							}
							public void onFlexDialogSubmitClicked(ClickEvent event) {								
							}
						});
						newTerm.show();
					}
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.conceptAddFail());
				}
			};
			Service.conceptService.addNewConcept(MainApp.userOntology, actionId, MainApp.userId, cObj, tObj, cpos, cobj, callback);
    	}
    }
    
    public void onCancel(){
    	this.hide();
    }
    
    public void setSubmitLabel(boolean visible, String label){
    	if(visible){
    		this.submit.setVisible(true);
    		if(label != null)
    			this.submit.setText(label);
    	}
    	else
    		this.submit.setVisible(false);
    }
        
    public void setCancelLabel(String label){
    	this.cancel.setText(label);
    }
        
    public void doCheckExistance(){
    	if(passCheckLabel() && passCheckLang())
    	{
    		if(isFirstCheckDone)
    			showLoadingExistingConcepts();
    		else
    			showLoading();
    		submit.setEnabled(false);
			final SearchParameterObject searchObj = new SearchParameterObject();
	        searchObj.setRegex(SearchParameterObject.EXACT_MATCH);
	        searchObj.clearSelectedLanguage();
	        searchObj.setKeyword(name.getText());
	        searchObj.addSelectedLanguage(language.getValue(language.getSelectedIndex()));
	        
	        searchTable = new ConceptSearchTable();
	        searchTable.setDialog(this);
	        searchTable.addItemClickedHandler(new ConceptSearchTableItemClickedHandler(){
				public void onItemClicked(final String link) {
					final ConfirmGoToExistingConcept d = new ConfirmGoToExistingConcept();
					d.addFlexDialogClickedHandler(new FlexDialogClickedHandler(){
						public void onFlexDialogSubmitClicked(ClickEvent event) {
							tree.gotoItem(link,  ModelConstants.CDOMAINCONCEPT, InfoTab.term, false, MainApp.userPreference.isHideDeprecated(), MainApp.userSelectedLanguage);
							d.hide();
							hideAddConcept();
						}
						public void onFlexDialogLoopClicked(ClickEvent event) {
						}
						public void onFlexDialogCancelClicked(ClickEvent event) {
							d.hide();
						}
					});
					d.show();
			}});
	        
			AsyncCallback<Object> callback2 = new AsyncCallback<Object>(){
				public void onSuccess(Object result){
					showTable();
					submit.setEnabled(true);
		    		cancel.setEnabled(true);
					int resultSize = Integer.parseInt((String) result);
					if(resultSize > 0){
						searchTable.setSearchTable(searchObj, resultSize, ModuleManager.MODULE_CONCEPT_CHECK_EXIST);
						showConceptExistStatus();
						showExistingConcepts();
						checkExistance.setVisible(true);
						if(!isFirstCheckDone)
							center();
						isFirstCheckDone = true;
						
					}
					else{
						// make term label and language read only
						name.setEnabled(false);
						language.setEnabled(false);
						checkExistance.setVisible(false);
						hideConceptExistStatus();
						hideExistingConcepts();
						showConceptProperties();
						submit.setVisible(true);
						isFirstCheckDone = true;
						checkExistance.setVisible(false);
						setSubmitLabel(true, constants.buttonCreate());
						center();
					}
				}
				public void onFailure(Throwable caught){
					showTable();
					Window.alert(constants.conceptCheckAvailabilityFail());
				}
			};
			// Check if matching concept label and language exits in current ontology			
			Service.searchSerice.getSearchResultsSize(searchObj, MainApp.userOntology, callback2);
    	}else
    		Window.alert(constants.conceptCompleteInfo());
    }
    
    public void showLoadingExistingConcepts(){
    	VerticalPanel cpanel = new VerticalPanel();
		cpanel.setSize(existingConcepts.getOffsetWidth()+"px", existingConcepts.getOffsetHeight()+"px");
		LoadingDialog load = new LoadingDialog();
		cpanel.add(load);
		cpanel.setCellHorizontalAlignment(load, HasHorizontalAlignment.ALIGN_CENTER);
		cpanel.setCellVerticalAlignment(load, HasVerticalAlignment.ALIGN_MIDDLE);
		cpanel.setCellHeight(load, "100%");
		cpanel.setCellWidth(load, "100%");
    	
    	existingConcepts.clear();
    	existingConcepts.add(cpanel);
    	table.getRowFormatter().setVisible(3, true);
    }
    
    public void showLoading(){
    	VerticalPanel cpanel = new VerticalPanel();
		cpanel.setSize(table.getOffsetWidth()+"px", (table.getOffsetHeight())+"px");
		LoadingDialog load = new LoadingDialog();
		cpanel.add(load);
		cpanel.setCellHorizontalAlignment(load, HasHorizontalAlignment.ALIGN_CENTER);
		cpanel.setCellVerticalAlignment(load, HasVerticalAlignment.ALIGN_MIDDLE);
		cpanel.setCellHeight(load, "100%");
		cpanel.setCellWidth(load, "100%");
		DOM.setStyleAttribute(cpanel.getElement(), "backgroundColor", "#F1F1F1");
    	
		container.clear();
		container.add(cpanel);    	
    }
    
    public void showTable(){
    	container.clear();
		container.add(table);	
	}
    
    public void showExistingConcepts(){
    	submit.setVisible(true);
    	existingConcepts.clear();
    	existingConcepts.add(searchTable.getLayout());
    	table.getRowFormatter().setVisible(3, true);
    }
    
    public void hideExistingConcepts(){    	
    	table.getRowFormatter().setVisible(3, false);
    }
    
    public void showConceptProperties(){
    	table.getRowFormatter().setVisible(4, true);
    	table.getRowFormatter().setVisible(5, true);
    	table.getRowFormatter().setVisible(6, true);
    	isConceptPropertyShown = true;
    }
    
    public void hideConceptProperties(){
    	table.getRowFormatter().setVisible(4, false);
    	table.getRowFormatter().setVisible(5, false);
    	table.getRowFormatter().setVisible(6, false);
    	isConceptPropertyShown = false;
    }
       
    public void showConceptExistStatus(){
    	table.getRowFormatter().setVisible(2, true);
    }
    
    public void hideConceptExistStatus(){
    	table.getRowFormatter().setVisible(2, false);
    }
    
    public boolean passCheckLabel(){
    	return name.getText().length()==0? false:true;
    }
        
    public boolean passCheckLang(){
    	return language.getValue((language.getSelectedIndex())).equals("--None--") || language.getValue((language.getSelectedIndex())).equals("")? false:true;
    }
        
    public boolean passCheckUri(){
    	boolean pass = false;
    	if(uri.getText() != "" || uri.getText() != null)
		{
    		String val = uri.getText();
    		if(val.length() > 0){
    			String v = val.substring(0, 1);
	    		if( v.matches("[a-zA-Z]") )
	    			pass = true;
	    		else
	    			pass = false;
    		}else
    			pass = true;
    	}
		else
			pass = true;
    	return pass;
    }
   
    public boolean passCheckPos(){
    	return position.getValue(position.getSelectedIndex()).equals("")? false : true;
    }
        
    public boolean passCheckNamespace(){
    	return namespace.getValue(namespace.getSelectedIndex()).equals("")? false : true;
    }
    
    public boolean passCheckExistingConceptSelected(){
    	return searchTable.getDataTable().getSelectedRows().size() > 0? true : false;
    }
        
    public void hideAddConcept(){
    	hide();
    }

}

class ConceptSearchTable extends SearchTable{
	
	final private HandlerManager handlerManager = new HandlerManager(this);
	
	public void addItemClickedHandler(ConceptSearchTableItemClickedHandler handler) {
		handlerManager.addHandler(ConceptSearchTableItemClickedEvent.getType(),handler);  
	}
	AddConcept dialog;
	
	public void setDialog(AddConcept dialog){
		this.dialog = dialog;
	}
	public void onLabelClicked(String link, String schemeURI, boolean isAddAction,int tab, int belongsToModule, int type){
		handlerManager.fireEvent(new ConceptSearchTableItemClickedEvent(link));
	}
}

class ConceptSearchTableItemClickedEvent extends GwtEvent<ConceptSearchTableItemClickedHandler> {
	
	String link;
	
	private static final Type<ConceptSearchTableItemClickedHandler> TYPE = new Type<ConceptSearchTableItemClickedHandler>();

	public ConceptSearchTableItemClickedEvent(String link) {
		this.link = link;
	}
	public static Type<ConceptSearchTableItemClickedHandler> getType() {
		return TYPE;
	}
	@Override
	protected void dispatch(ConceptSearchTableItemClickedHandler handler) {
		handler.onItemClicked(link);
	}
	@Override

	public com.google.gwt.event.shared.GwtEvent.Type<ConceptSearchTableItemClickedHandler> getAssociatedType() {
		return TYPE;
	}
}

interface ConceptSearchTableItemClickedHandler extends EventHandler {

	void onItemClicked(String link);
} 

class ConfirmGoToExistingConcept extends FlexDialogBox{
	private HTML msg = new HTML();
	public ConfirmGoToExistingConcept(){
		super("Yes", "Cancel");
		setWidth("400px");
		this.setText(constants.conceptGoToSelectedConceptWarningTitle());
		msg.setHTML(constants.conceptGoToSelectedConceptWarning());
		this.initLayout();
	}
	public void initLayout() {
		Grid table = new Grid(1,2);
		table.setWidget(0, 0, new Image("images/Warning.png"));
		table.setWidget(0, 1, msg);
		table.setWidth("100%");
		addWidget(table);
	}
}