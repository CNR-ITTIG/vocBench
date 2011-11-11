package org.fao.aoscs.client.module.export.widgetlib;
 
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.classification.Classification;
import org.fao.aoscs.client.module.classification.widgetlib.OlistBox;
import org.fao.aoscs.client.module.concept.Concept;
import org.fao.aoscs.client.module.consistency.Consistency;
import org.fao.aoscs.client.module.constant.ExportFormat;
import org.fao.aoscs.client.module.constant.OWLActionConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.module.document.About;
import org.fao.aoscs.client.module.relationship.Relationship;
import org.fao.aoscs.client.module.search.Search;
import org.fao.aoscs.client.module.validation.Validation;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.ConceptBrowser;
import org.fao.aoscs.client.widgetlib.shared.dialog.DialogBoxAOS;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.label.LabelAOS;
import org.fao.aoscs.client.widgetlib.shared.panel.BodyPanel;
import org.fao.aoscs.client.widgetlib.shared.panel.ButtonbarWidget;
import org.fao.aoscs.client.widgetlib.shared.panel.TitleBodyWidget;
import org.fao.aoscs.domain.ExportParameterObject;
import org.fao.aoscs.domain.InitializeExportData;
import org.fao.aoscs.domain.RelationshipObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class ExportOptionTable extends Composite{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private VerticalPanel panel = new VerticalPanel();
	private InitializeExportData  initData = new InitializeExportData();
	private HorizontalPanel pExpDetail = new HorizontalPanel();
	public static ArrayList<String> userSelectedLanguage ;
	public DeckPanel modulePanel = new DeckPanel();
	public FlexTable table;
	public Concept concept;
	public Classification classification;
	public Consistency consistency;
	public Validation validation;
	public About about;
	public Search search;
	public Image conceptBrowse; 
	public Relationship relationship;
	private TextArea ta = new TextArea();
	private TextBox txtfilename = new TextBox();
	private Button save = new Button(constants.exportButton());
	private LabelAOS subVocabLabel = new LabelAOS("--None--","");
	private DateBox startDate = new DateBox();		
	private DateBox endDate = new DateBox();
	private LabelAOS conceptLabel = new LabelAOS("--None--","");
	private LabelAOS tconceptLabel = new LabelAOS("--None--","");
	private Image conceptClear;
	private CheckBox conceptChildren; 
	private ListBox format = new ListBox();
	private ListBox scheme = new ListBox();
	private String key = "";
	private ExportParameterObject exp = new ExportParameterObject();
	private TermCodeDialogBox termCodeDialogBox;
	private SelectLanguage selectLanguage;
	
	public ExportOptionTable(InitializeExportData initData){
		this.initData = initData;
		userSelectedLanguage= (ArrayList<String>) MainApp.userSelectedLanguage;
		initLayout();
		initWidget(panel);
	}
	
	
	private void initLayout(){
		
		final VerticalPanel optionPanel = new VerticalPanel();
		optionPanel.setWidth("100%");
		
		table = new FlexTable();
		table.setWidth("100%");

		HTML conceptLab = new HTML(constants.exportConcept());
		HTML exportLab = new HTML(constants.exportFormat());
		HTML schemeLab = new HTML(constants.exportScheme());
		HTML dateLab = new HTML(constants.exportDate());
		HTML termCodeLab = new HTML(constants.exportTermCode());
		
		conceptLab.setWordWrap(false);
		exportLab.setWordWrap(false);
		schemeLab.setWordWrap(false);
		dateLab.setWordWrap(false);
		termCodeLab.setWordWrap(false);
		        
		
        
		table.setWidget(0, 0, exportLab);
		table.setWidget(0, 1, getExportFormat());
        table.setWidget(0, 2, schemeLab);
        table.setWidget(0, 3, getScheme());
        
        
		table.setWidget(1, 0, conceptLab);
		table.setWidget(1, 1, getConcept());
		table.getFlexCellFormatter().setColSpan(1, 1, 3);
		
		table.setWidget(2, 0, dateLab);
		table.setWidget(2, 1, getDatePanel());
        table.setWidget(2, 2, termCodeLab);
        table.setWidget(2, 3, getTermCode());
        
        
		
		

		table.getColumnFormatter().setWidth(0, "10%");
		table.getColumnFormatter().setWidth(1, "40%");
		table.getColumnFormatter().setWidth(2, "10%");
		table.getColumnFormatter().setWidth(3, "40%");
		
		final VerticalPanel exportOption= new VerticalPanel();
		exportOption.setSpacing(10);
		exportOption.setSize("100%", "100%");
		exportOption.add(GridStyle.setTableRowStyle(table, "#F4F4F4", "#E8E8E8", 3));
		
		Button clear = new Button(constants.buttonClear());
		final Button export = new Button(constants.exportPreviewButton());
			
		HorizontalPanel wrapper = new HorizontalPanel();
		wrapper.setSpacing(5);
		wrapper.add(export);
		wrapper.add(clear);
		
		
		final ButtonbarWidget buttonBarPanel = new ButtonbarWidget(null, wrapper);

		ta.setHeight("100%");
		ta.setWidth("100%");
		ta.setReadOnly(true);
		
		pExpDetail.setWidth("100%");
		pExpDetail.setHeight("100%");
		pExpDetail.add(ta);
		pExpDetail.setCellHeight(ta, "100%");
		
		txtfilename.setWidth("300px");
		txtfilename.setEnabled(false);
		
		save.setEnabled(false);
		save.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event) {
				AsyncCallback<Object> callback = new AsyncCallback<Object>(){
					public void onSuccess(Object result){		
						openURL(GWT.getHostPageBaseURL()+"downloadExportData?filename="+txtfilename.getText()+"&key="+key);
					}
					public void onFailure(Throwable caught){
						Window.alert(constants.exportLogFail());							
					}
				};
				Service.exportService.markExportAsRecentChange(exp, MainApp.userId, 74 , MainApp.userOntology, callback);
			}
		});

		HorizontalPanel bottombar = new HorizontalPanel();
		bottombar.setSpacing(5);
		bottombar.add(txtfilename);
		bottombar.add(save);
		
		
		ButtonbarWidget bottomBarPanel = new ButtonbarWidget(null, bottombar);

		final VerticalPanel previewPanel = new VerticalPanel();
		previewPanel.setSize(MainApp.getBodyPanelWidth()- 65 +"px" , MainApp.getBodyPanelHeight() - 265+"px");
	    Window.addResizeHandler(new ResizeHandler()
	    {
	    	public void onResize(ResizeEvent event) {
	    		previewPanel.setSize(MainApp.getBodyPanelWidth()- 65 +"px" , MainApp.getBodyPanelHeight() - 265+"px");
			}
	    });
		previewPanel.add(pExpDetail);
		previewPanel.add(bottomBarPanel);
		previewPanel.setCellHeight(pExpDetail, "100%");
				
		TitleBodyWidget previewBox = new TitleBodyWidget(constants.exportPreview(), previewPanel, null, MainApp.getBodyPanelWidth()- 45 +"px" , "100%");

		HorizontalPanel pwrapper = new HorizontalPanel();
		pwrapper.setSize("100%", "100%");
		pwrapper.setSpacing(10);
		pwrapper.add(previewBox);
		pwrapper.setCellHeight(previewBox, "100%");
		pwrapper.setCellVerticalAlignment(previewBox,HasVerticalAlignment.ALIGN_TOP);
		
		optionPanel.add(exportOption);
		optionPanel.add(buttonBarPanel);
		optionPanel.add(pwrapper);
		optionPanel.setCellHeight(pwrapper, "100%");
		optionPanel.setCellHorizontalAlignment(pwrapper,HasHorizontalAlignment.ALIGN_CENTER);
		optionPanel.setCellVerticalAlignment(exportOption,HasVerticalAlignment.ALIGN_TOP);
		optionPanel.setCellVerticalAlignment(buttonBarPanel,HasVerticalAlignment.ALIGN_TOP);
		optionPanel.setCellVerticalAlignment(pwrapper,HasVerticalAlignment.ALIGN_TOP);
		
		Image img = new Image("images/map-grey.gif");
		final Label lang = new Label(constants.exportSelectLang());
		lang.setSize("150", "100%");
		lang.setStyleName("displayexportLang");
		lang.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		lang.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(selectLanguage == null || !selectLanguage.isLoaded)
					selectLanguage = new SelectLanguage();
				selectLanguage.show();
			}
		});
		HorizontalPanel langPanel = new HorizontalPanel();
		langPanel.setSize("10%", "100%");
		langPanel.add(img);
		langPanel.add(new HTML("&nbsp;"));
		langPanel.add(lang);
		langPanel.add(new HTML("&nbsp;"));
		langPanel.add(new HTML("&nbsp;"));
		langPanel.setCellWidth(lang, "100%");
		langPanel.setCellHeight(lang, "100%");
		langPanel.setCellVerticalAlignment(img, HasVerticalAlignment.ALIGN_MIDDLE);
		langPanel.setCellVerticalAlignment(lang, HasVerticalAlignment.ALIGN_MIDDLE);
		langPanel.setCellHorizontalAlignment(lang, HasHorizontalAlignment.ALIGN_RIGHT);		
		langPanel.setSpacing(1);		
		
		
		BodyPanel mainPanel = new BodyPanel(constants.exportTitle() , optionPanel , langPanel);
		panel.clear();
		panel.add(mainPanel);	      
	    panel.setCellHorizontalAlignment(mainPanel,  HasHorizontalAlignment.ALIGN_CENTER);
	    panel.setCellVerticalAlignment(mainPanel,  HasVerticalAlignment.ALIGN_TOP);
		
		
	
	// =================
		clear.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {	

				subVocabLabel.setText("--None--"); 
				startDate.getTextBox().setText(""); 
				endDate.getTextBox().setText("");
				conceptLabel.setText("--None--");	
				tconceptLabel.setText("--None--");
				
				txtfilename.setText("");
				txtfilename.setEnabled(false);
				save.setEnabled(false);

				ta.setText("");
				if(scheme.getItemCount()>=0)	format.setItemSelected(0,true);
				if(scheme.getItemCount()>=0)	scheme.setItemSelected(0,true);
				
				exp = new ExportParameterObject();
				
				pExpDetail.clear();
			}
		});
		
		export.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				String expformat = exp.getFormat();
				if(expformat == null || expformat.equals(""))
				{
					Window.alert(constants.exportSelectFormat());
				}
				else if(exp.getStartDate() != null || exp.getEndDate() != null)
				{
					if(exp.getStartDate() == null || exp.getEndDate() == null)
					{
						Window.alert(constants.exportSelectDateRange());
					}
				}
				else
				{
					ta.setText("");
					export.setEnabled(false);
					save.setEnabled(false);
					txtfilename.setEnabled(false);
					
					exp.setExpLanguage(userSelectedLanguage);				
					pExpDetail.clear();
					
					LoadingDialog ld = new LoadingDialog(constants.exportLoading());
					pExpDetail.add(ld);
					pExpDetail.setCellHorizontalAlignment(ld, HasHorizontalAlignment.ALIGN_CENTER);
					pExpDetail.setCellVerticalAlignment(ld, HasVerticalAlignment.ALIGN_MIDDLE);
					
					AsyncCallback<HashMap<String, String>> callback = new AsyncCallback<HashMap<String, String>>()
					{
						public void onSuccess(HashMap<String, String> exportData)
						{
						    String data = "";							
							for(Iterator<String> itr = exportData.keySet().iterator(); itr.hasNext();)
							{
								key = (String)itr.next();
								data = exportData.get(key);
								if(data == null || data.equals(""))
									data = constants.exportDataFail();
							}
							pExpDetail.clear();
							pExpDetail.add(ta);		
							ta.setText(data);
							String formattype = format.getValue(format.getSelectedIndex());
							String filename = "export_"+formattype.toLowerCase()+"_"+DateTimeFormat.getFormat("ddMMyyyyhhmmss").format(new Date());
							if(formattype.equals(ExportFormat.SKOS))
								filename += ".rdf";
							else if(formattype.equals(ExportFormat.TBX))
								filename += ".tbx";
							else if(formattype.equals(ExportFormat.OWL_SIMPLE_FORMAT))
								filename += ".owl";
							else if(formattype.equals(ExportFormat.OWL_COMPLETE_FORMAT))
								filename += ".owl";
							else if(formattype.equals(ExportFormat.RDBMS_SQL_FORMAT))
								filename += ".sql";
							
							save.setEnabled(MainApp.permissionTable.contains(OWLActionConstants.EXPORT, -1) && true);
							txtfilename.setEnabled(true);
							txtfilename.setText(filename);
							export.setEnabled(true);
						    
						}
						public void onFailure(Throwable caught){
							Window.alert(constants.exportDataFail());
							save.setEnabled(false);
							txtfilename.setEnabled(false);
							export.setEnabled(true);
							pExpDetail.clear();
							pExpDetail.add(ta);
						}
					};
					Service.exportService.getExportDataAndFilename(exp, MainApp.userOntology, callback);
				}
			}
		});
	}
	
	private ListBox getExportFormat(){
		format.addItem("--None--", "--None--");
		format.addItem(constants.exportSKOS(), ExportFormat.SKOS);
		format.addItem(constants.exportSQL(), ExportFormat.RDBMS_SQL_FORMAT);
		format.setWidth("100%");
		format.addChangeHandler(new ChangeHandler(){
			public void onChange(ChangeEvent event){
				if(!format.getValue((format.getSelectedIndex())).equals("") && !format.getValue(format.getSelectedIndex()).equals("--None--")){
					exp.setFormat(format.getValue(format.getSelectedIndex()));
				}else{
					exp.setFormat(null);
				}
			}
		});
		
		return format;
	}
	private ListBox getScheme(){
		scheme = Convert.makeListBoxWithValue(initData.getScheme());
		scheme.setWidth("100%");
		scheme.addChangeHandler(new ChangeHandler()
		{
			public void onChange(ChangeEvent event)
			{
				if(!scheme.getValue((scheme.getSelectedIndex())).equals("") && !scheme.getValue(scheme.getSelectedIndex()).equals("--None--"))
				{
					exp.setSchemeURI(scheme.getValue(scheme.getSelectedIndex()));
					/*conceptBrowse.setVisible(false); 
					conceptLabel.setVisible(false);
					conceptClear.setVisible(false);
					conceptChildren.setVisible(false);
					*/
					table.getRowFormatter().setVisible(1, false);
				}else
				{
					exp.setSchemeURI(null);
					/*conceptBrowse.setVisible(true);
					conceptLabel.setVisible(true);
					conceptClear.setVisible(true);
					conceptChildren.setVisible(true);
					*/
					table.getRowFormatter().setVisible(1, true);
				}
			}
		});
		return scheme;
	}
	
	private HorizontalPanel getConcept()
	{
		HorizontalPanel conceptHp = new HorizontalPanel();
		conceptHp.add(conceptLabel);
		conceptBrowse = new Image("images/browseButton3-grey.gif");
		conceptBrowse.setStyleName(Style.Link);
		conceptBrowse.addClickHandler(new ClickHandler() 
		{
			public void onClick(ClickEvent event) 
			{
				final ConceptBrowser cb =((MainApp) RootPanel.get().getWidget(0)).conceptBrowser; 
				cb.showBrowser();
				cb.addSubmitClickHandler(new ClickHandler()
				{
					public void onClick(ClickEvent event) 
					{
						conceptLabel.setText(cb.getSelectedItem(),cb.getTreeObject());
						exp.setConceptURI(cb.getTreeObject().getUri());
					}					
				});						
			}
		});
		conceptHp.add(conceptBrowse);
		
		conceptClear = new Image("images/trash-grey.gif");
		conceptClear.setTitle(constants.buttonClear());
		conceptClear.setStyleName(Style.Link);
		conceptClear.addClickHandler(new ClickHandler() 
		{
			public void onClick(ClickEvent event) 
			{
				conceptLabel.setText("");
				exp.setConceptURI(null);
			}
		});
		conceptHp.add(conceptClear);
		
		conceptChildren = new CheckBox(constants.exportIncludeChildren(), true);
		conceptChildren.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event) 
			{
				exp.setIncludeChildren(((CheckBox) event.getSource()).getValue());
			}
		});
		conceptHp.add(conceptChildren);		
		conceptHp.setSpacing(3);
		conceptHp.setWidth("100%");
		conceptHp.setCellHorizontalAlignment(conceptLabel, HasHorizontalAlignment.ALIGN_LEFT);
		conceptHp.setCellHorizontalAlignment(conceptBrowse, HasHorizontalAlignment.ALIGN_RIGHT);
		conceptHp.setCellHorizontalAlignment(conceptChildren, HasHorizontalAlignment.ALIGN_LEFT);
		conceptHp.setCellWidth(conceptLabel, "80%");
		return conceptHp;
	}
	private HorizontalPanel getTermCode(){
		HorizontalPanel conceptHp = new HorizontalPanel();
		//final LabelAOS tconceptLabel = new LabelAOS("--None--","");
		conceptHp.add(tconceptLabel);
		Image conceptBrowse = new Image("images/browseButton3-grey.gif");
		conceptBrowse.setStyleName(Style.Link);
		conceptBrowse.addClickHandler(new ClickHandler() 
		{
			public void onClick(ClickEvent event) 
			{
				if(termCodeDialogBox == null || !termCodeDialogBox.isLoaded)
					termCodeDialogBox = new TermCodeDialogBox(tconceptLabel);
				termCodeDialogBox.show();
			}
		});
		
		Image clear = new Image("images/trash-grey.gif");
		clear.setTitle(constants.buttonClear());
		clear.setStyleName(Style.Link);
		clear.addClickHandler(new ClickHandler() 
		{
			public void onClick(ClickEvent event) 
			{
				tconceptLabel.setText("");
				exp.setTermCodeRepositoryURI(null);
				exp.setStartCode(null);
			}
		});
		
		conceptHp.add(conceptBrowse);
		conceptHp.add(clear);
		conceptHp.setSpacing(3);
		conceptHp.setWidth("100%");
		conceptHp.setCellHorizontalAlignment(conceptBrowse, HasHorizontalAlignment.ALIGN_RIGHT);
		conceptHp.setCellHorizontalAlignment(tconceptLabel, HasHorizontalAlignment.ALIGN_LEFT);
		conceptHp.setCellWidth(tconceptLabel, "100%");
		return conceptHp;
	}
	
	private HorizontalPanel getDatePanel()
	{
		HorizontalPanel panel = new HorizontalPanel();

		startDate.setFormat((new DateBox.DefaultFormat (DateTimeFormat.getFormat ("dd/MM/yyyy"))));
		startDate.addValueChangeHandler(new ValueChangeHandler<Date>()
		{
			public void onValueChange(ValueChangeEvent<Date> event) 
			{
				exp.setStartDate(""+((DateBox)(event.getSource())).getTextBox().getValue());
			}
		});		
		startDate.getTextBox().addValueChangeHandler(new ValueChangeHandler<String>()
		{
			public void onValueChange(ValueChangeEvent<String> event) 
			{
				String value = ((TextBox)(event.getSource())).getValue();
				exp.setStartDate(value.equals("")? null : value);			
			}
		});

		endDate.setFormat((new DateBox.DefaultFormat (DateTimeFormat.getFormat ("dd/MM/yyyy"))));
		endDate.addValueChangeHandler(new ValueChangeHandler<Date>()
		{
			public void onValueChange(ValueChangeEvent<Date> event) 
			{
				exp.setEndDate(((DateBox)(event.getSource())).getTextBox().getValue());
			}
		});
		endDate.getTextBox().addValueChangeHandler(new ValueChangeHandler<String>()
		{
			public void onValueChange(ValueChangeEvent<String> event) 
			{
				String value = ((TextBox)(event.getSource())).getValue();
				exp.setEndDate(value.equals("")? null : value);
			}
			
		});
		
		final ListBox type = new ListBox();
		type.addItem(constants.exportCreate(), "create");
		type.addItem(constants.exportUpdate(), "update");
		type.addChangeHandler(new ChangeHandler()
		{
			public void onChange(ChangeEvent event)
			{
				if(type.getValue(type.getSelectedIndex()).length()>0)
				{
					exp.setDateType(type.getValue(type.getSelectedIndex()));
				}else
				{
					exp.setDateType(null);
				}
			}
		});
		
		Grid table = new Grid(1,5);
		table.setWidget(0, 0, type);
		table.setWidget(0, 1, new HTML(constants.exportFrom()));
		table.setWidget(0, 2, startDate);
		table.setWidget(0, 3, new HTML(constants.exportTo()));
		table.setWidget(0, 4, endDate);
		
		panel.add(table);
		
		return panel;
	}
	
	private class TermCodeDialogBox extends DialogBoxAOS implements ClickHandler{
		
		private VerticalPanel panel = new VerticalPanel();
		private Button submit = new Button(constants.buttonSubmit());
		private Button close = new Button(constants.buttonCancel());
		private OlistBox repository = new OlistBox();
		private TextBox tbCode = new TextBox();
		private LabelAOS termCode = new LabelAOS();
		
		public TermCodeDialogBox(LabelAOS termCode)
		{
			this.termCode = termCode;
			this.setText(constants.exportTermCodeBrowser());
			Grid table = new Grid(2,2);
			table.setWidget(0, 0, new HTML(constants.exportTermCodeType()));
			table.setWidget(1, 0, new HTML(constants.exportTermCode()));
			ArrayList<RelationshipObject> code = initData.getTermCodeProperties();
			repository = Convert.makeOListBoxWithValue(code);
			
			table.setWidget(0, 1, repository);
			table.setWidget(1, 1, tbCode);
			tbCode.setWidth("100%");
			
			submit.addClickHandler(this);
			close.addClickHandler(this);
			
			HorizontalPanel buttonPanel = new HorizontalPanel();
			buttonPanel.setSpacing(5);
			buttonPanel.add(submit);
			buttonPanel.add(close);
			
			HorizontalPanel hp = new HorizontalPanel();
			hp.setSpacing(0);
			hp.setWidth("100%");
			hp.setStyleName("bottombar");
			hp.add(buttonPanel);
			hp.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);
					
			
			VerticalPanel vp = new VerticalPanel();
			vp.setSize("100%", "100%");
			vp.setSpacing(10);
			vp.add(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));
			
			panel.add(vp);
			panel.add(hp);
			panel.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_RIGHT);
			setWidget(panel);
		}
		private void clearHistory(){
			repository.setSelectedIndex(0);
			tbCode.setText("");
		}
		public void onClick(ClickEvent event) 
		{
			Widget sender = (Widget) event.getSource();
			if(sender.equals(submit))
			{
				String selectedValue = repository.getValue(repository.getSelectedIndex());
				String selectedText = repository.getItemText(repository.getSelectedIndex());
				
				if(selectedValue!="" && selectedValue!="--None--" && tbCode.getText().length()>0)
				{
					termCode.setText(selectedText+" : "+tbCode.getText(), selectedValue+" : "+tbCode.getText());
					exp.setTermCodeRepositoryURI(selectedValue);
					exp.setStartCode(tbCode.getText());
					this.hide();
				}
				else
				{
					Window.alert(constants.exportNoData());
				}
				clearHistory();
				this.hide();
			}else if(sender.equals(close)){
				clearHistory();
				this.hide();
			}
		}
	}
	

	public static native void openURL(String url) /*-{
	   $wnd.open(url,'_self','');
	}-*/;
}