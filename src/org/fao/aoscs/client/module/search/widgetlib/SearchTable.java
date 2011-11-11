package org.fao.aoscs.client.module.search.widgetlib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.concept.widgetlib.InfoTab;
import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;
import org.fao.aoscs.client.widgetlib.shared.tree.TreeItemAOS;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.LabelObject;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.SearchParameterObject;
import org.fao.aoscs.domain.SearchResultObject;
import org.fao.aoscs.domain.TermObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.gen2.table.client.AbstractColumnDefinition;
import com.google.gwt.gen2.table.client.AbstractScrollTable;
import com.google.gwt.gen2.table.client.CachedTableModel;
import com.google.gwt.gen2.table.client.CellRenderer;
import com.google.gwt.gen2.table.client.ColumnDefinition;
import com.google.gwt.gen2.table.client.DefaultTableDefinition;
import com.google.gwt.gen2.table.client.FixedWidthFlexTable;
import com.google.gwt.gen2.table.client.FixedWidthGrid;
import com.google.gwt.gen2.table.client.FixedWidthGridBulkRenderer;
import com.google.gwt.gen2.table.client.MutableTableModel;
import com.google.gwt.gen2.table.client.PagingOptions;
import com.google.gwt.gen2.table.client.PagingScrollTable;
import com.google.gwt.gen2.table.client.ScrollTable;
import com.google.gwt.gen2.table.client.TableDefinition;
import com.google.gwt.gen2.table.client.SelectionGrid.SelectionPolicy;
import com.google.gwt.gen2.table.client.TableDefinition.AbstractCellView;
import com.google.gwt.gen2.table.client.TableModelHelper.Request;
import com.google.gwt.gen2.table.client.TableModelHelper.Response;
import com.google.gwt.gen2.table.client.TableModelHelper.SerializableResponse;
import com.google.gwt.gen2.table.event.client.RowSelectionEvent;
import com.google.gwt.gen2.table.event.client.RowSelectionHandler;
import com.google.gwt.gen2.table.event.client.TableEvent.Row;
import com.google.gwt.gen2.table.override.client.FlexTable;
import com.google.gwt.gen2.table.override.client.FlexTable.FlexCellFormatter;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class SearchTable {
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private	SearchParameterObject searchObj = new SearchParameterObject();
	private int type = 0 ;
	private HorizontalPanel pagingPanel = new HorizontalPanel();
	
	private DateTimeFormat sdf = DateTimeFormat.getFormat("dd-MM-yyyy HH:mm:ss");
	
	/**
	   * The {@link CachedTableModel} around the main table model.
	   */
	  private CachedTableModel<SearchResultObject> cachedTableModel = null;
	  
	  /**
	   * The {@link PagingScrollTable}.
	   */
	  private PagingScrollTable<SearchResultObject> pagingScrollTable = null;
	  
	  
	  /**
	   * The {@link DataSourceTableModel}.
	   */
	  private SearchTableModel tableModel = null;

	  /**
	   * The {@link DefaultTableDefinition}.
	   */
	  private DefaultTableDefinition<SearchResultObject> tableDefinition = null;

	  /**
	   * @return the cached table model
	   */
	  public CachedTableModel<SearchResultObject> getCachedTableModel() {
	    return cachedTableModel;
	  }

	 
	  /**
	   * @return the {@link PagingScrollTable}
	   */
	  public PagingScrollTable<SearchResultObject> getPagingScrollTable() {
	    return pagingScrollTable;
	  }

	  /**
	   * @return the table definition of columns
	   */
	  public DefaultTableDefinition<SearchResultObject> getTableDefinition() {
	    return tableDefinition;
	  }

	  /**
	   * @return the table model
	   */
	  public SearchTableModel getTableModel() {
	    return tableModel;
	  }

	  public void insertDataRow(int beforeRow) {
	    getCachedTableModel().insertRow(beforeRow);
	  }

	  /**
	   * The main layout panel.
	   */
	  private FlexTable layout = new FlexTable();

	  /**
	   * The scroll table.
	   */
	  private AbstractScrollTable scrollTable = null;

	  /**
	   * @return the data table.
	   */
	  public FixedWidthGrid getDataTable() {
	    return getScrollTable().getDataTable();
	  }

	  /**
	   * @return the footer table.
	   */
	  public FixedWidthFlexTable getFooterTable() {
	    return getScrollTable().getFooterTable();
	  }

	  /**
	   * @return the header table.
	   */
	  public FixedWidthFlexTable getHeaderTable() {
	    return getScrollTable().getHeaderTable();
	  }

	  /**
	   * @return the scroll table.
	   */
	  public AbstractScrollTable getScrollTable() {
	    return scrollTable;
	  }

	  /**
	   * @return the new header table
	   */
	  protected FixedWidthFlexTable createHeaderTable(SearchParameterObject searchObj) {
	    FixedWidthFlexTable headerTable = new FixedWidthFlexTable();

	    // Level 1 headers
	    FlexCellFormatter formatter = headerTable.getFlexCellFormatter();
	    
		headerTable.setHTML(0, 0, constants.searchConcept());
		formatter.setHorizontalAlignment(0, 0,HasHorizontalAlignment.ALIGN_CENTER);
		
		if(searchObj.getRelationship()!=null)
		{
			headerTable.setHTML(0, 1, constants.relRelationship());
			formatter.setHorizontalAlignment(0, 1,HasHorizontalAlignment.ALIGN_CENTER);
			headerTable.setHTML(0, 2, constants.searchConcept());
			formatter.setHorizontalAlignment(0, 2,HasHorizontalAlignment.ALIGN_CENTER);
		}
		else
		{
			headerTable.setHTML(0, 1, constants.searchDate());
			formatter.setHorizontalAlignment(0, 1,HasHorizontalAlignment.ALIGN_CENTER);
		}

	    return headerTable;
	  }

	  /**
	   * @return the newly created data table.
	   */
	  protected FixedWidthGrid createDataTable() {
	    FixedWidthGrid dataTable = new FixedWidthGrid();
	    if(type==ModuleManager.MODULE_CONCEPT_BROWSER)
	    {
		    dataTable.setSelectionEnabled(true);
		    dataTable.setSelectionPolicy(SelectionPolicy.ONE_ROW);
		    dataTable.addRowSelectionHandler(new RowSelectionHandler()
		    {
				public void onRowSelection(RowSelectionEvent event) {
					int selectedRowIndex = -1;
					for (Row row : event.getSelectedRows()) {
						selectedRowIndex = row.getRowIndex();
					}
					ConceptObject cObj = pagingScrollTable.getRowValue(selectedRowIndex).getConceptObject();
					ModuleManager.gotoItem(cObj.getUri(), cObj.getScheme(), true, InfoTab.term, cObj.getBelongsToModule(), type);					
				}
		    	
		    });
	    }
	    else
	    {
	    	dataTable.setSelectionEnabled(false);
	    }
	    return dataTable;
	  }

	  protected AbstractScrollTable createScrollTable(
	      FixedWidthFlexTable headerTable, FixedWidthGrid dataTable, int size, SearchParameterObject searchObj) {
	    // Setup the controller
	    tableModel = new SearchTableModel();
	    cachedTableModel = new CachedTableModel<SearchResultObject>(tableModel);
	    cachedTableModel.setPreCachedRowCount(50);
	    cachedTableModel.setPostCachedRowCount(50);
	    cachedTableModel.setRowCount(size);
	    	
	    // Create a TableCellRenderer
	    TableDefinition<SearchResultObject> tableDef = createTableDefinition(searchObj);

	    // Create the scroll table
	    pagingScrollTable = new PagingScrollTable<SearchResultObject>(cachedTableModel, dataTable, headerTable, tableDef);
	    pagingScrollTable.setPageSize(25);
	    pagingScrollTable.setEmptyTableWidget(new HTML("<span style='color:red'>"+constants.searchNoResult()+"</span>"));

	    // Setup the bulk renderer
	    FixedWidthGridBulkRenderer<SearchResultObject> bulkRenderer = new FixedWidthGridBulkRenderer<SearchResultObject>(dataTable, pagingScrollTable);
	    pagingScrollTable.setBulkRenderer(bulkRenderer);

	    
	    // Setup the formatting
	    pagingScrollTable.setCellPadding(3);
	    pagingScrollTable.setCellSpacing(0);
	    pagingScrollTable.setResizePolicy(ScrollTable.ResizePolicy.FILL_WIDTH);

	    return pagingScrollTable;
	  }

	  
	  /**
	   * @return the main layout panel
	   */
	  public FlexTable getLayout() {
	    return layout;
	  }

	  /**
	   * Called when the module has finished loading.
	   */
	  protected void onModuleLoaded() {
	      pagingScrollTable.gotoFirstPage();
	  }
	
	public void setSearchTable(SearchParameterObject searchObj, int size, int type1) {
		
		this.searchObj = searchObj;
		type = type1;

		// Initialize the tables
		FixedWidthFlexTable headerTable = createHeaderTable(searchObj);
		FixedWidthGrid dataTable = createDataTable();
		scrollTable = createScrollTable(headerTable, dataTable, size, searchObj);
		
		if(type==ModuleManager.MODULE_SEARCH)
		{
			scrollTable.setWidth(MainApp.getBodyPanelWidth() - 42 + "px");
			scrollTable.setHeight(MainApp.getBodyPanelHeight() - 105 +"px");
			pagingPanel.setSize(MainApp.getBodyPanelWidth() - 42 + "px", MainApp.getBodyPanelHeight() - 105 +"px");
		}
		else if(type==ModuleManager.MODULE_CONCEPT_BROWSER)
		{
			scrollTable.setWidth(700 - 42 + "px");
			scrollTable.setHeight(400 - 110 +"px");
			pagingPanel.setWidth(700 - 42 + "px");
		}
		else if(type==ModuleManager.MODULE_CONCEPT_CHECK_EXIST)
		{
			scrollTable.setWidth(500 - 42 + "px");
			scrollTable.setHeight(300 - 110 +"px");
			pagingPanel.setWidth(500 - 42 + "px");
		}
		
		// Add the main layout to the page
		layout.setWidth("100%");
		layout.setCellPadding(0);
		layout.setCellSpacing(0);
		layout.setWidget(0, 0, scrollTable);		// Add the scroll table to the layout
		
	    Window.addResizeHandler(new ResizeHandler()
	    {
	    	public void onResize(ResizeEvent event) {
	    		
	    		if(type==ModuleManager.MODULE_SEARCH)
	    		{
	    			scrollTable.setWidth(MainApp.getBodyPanelWidth() - 42 + "px");
	    			scrollTable.setHeight(MainApp.getBodyPanelHeight() - 105 +"px");
	    			pagingPanel.setWidth(MainApp.getBodyPanelWidth() - 42 + "px");
	    		}
	    		else if(type==ModuleManager.MODULE_CONCEPT_BROWSER)
	    		{
	    			scrollTable.setWidth(700 - 42 + "px");
	    			scrollTable.setHeight(400 - 110 +"px");
	    			pagingPanel.setWidth(700 - 42 + "px");
	    		}
	    		else if(type==ModuleManager.MODULE_CONCEPT_CHECK_EXIST)
	    		{
	    			scrollTable.setWidth(500 - 42 + "px");
	    			scrollTable.setHeight(300 - 110 +"px");
	    			pagingPanel.setWidth(500 - 42 + "px");
	    		}
				scrollTable.redraw();
			}
	    });
	    
		// Create an paging options
		PagingOptions pagingOptions = new PagingOptions(getPagingScrollTable());
		pagingOptions.setHeight("100%");
		pagingPanel.setHeight("100%");
	    pagingPanel.add(pagingOptions);
	    pagingPanel.setStyleName("gwt-PagingOptionsPanel");
	    pagingPanel.setCellHeight(pagingOptions, "100%");
	    pagingPanel.setCellWidth(pagingOptions, "100%");
		layout.insertRow(1);
		layout.setWidget(1,0, pagingPanel);
		
		// Do any required post processing
		onModuleLoaded();
}
	
	public abstract class SearchColumnDefiniton<ColType> extends AbstractColumnDefinition<SearchResultObject, ColType> {

		/**
		 * Construct a new {@link SearchColumnDefiniton}.
		 * 
		 */
		public SearchColumnDefiniton() {}

	}
	
	 /**
	   * @return the {@link TableDefinition} with all ColumnDefinitions defined.
	   */
	 private TableDefinition<SearchResultObject> createTableDefinition(SearchParameterObject searchObj) {
	    

	    // Create the table definition
	    tableDefinition = new DefaultTableDefinition<SearchResultObject>();
	    

	    // concept
	    {
			SearchColumnDefiniton<Object> columnDef = new SearchColumnDefiniton<Object>() {

				@Override
				public Object getCellValue(SearchResultObject rowValue) {
					return null;
				}

				@Override
				public void setCellValue(SearchResultObject rowValue, Object cellValue) {
				
				}

			};
			columnDef.setCellRenderer(new CellRenderer<SearchResultObject, Object>() {
				public void renderRowValue(SearchResultObject rowValue, ColumnDefinition<SearchResultObject, Object> columnDef,AbstractCellView<SearchResultObject> view) {
					view.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
					view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_DEFAULT);
					Widget w = (Widget) getConceptColumn((ConceptObject)rowValue.getConceptObject(), type);
			    	w.addStyleName("gwt-NoBorder");
			    	view.setWidget(w);
				}
			});
			//columnDef.setMinimumColumnWidth(150);
			//columnDef.setPreferredColumnWidth(150);
			//columnDef.setMaximumColumnWidth(150);
			columnDef.setColumnSortable(false);
			tableDefinition.addColumnDefinition(columnDef);
	    }
	    
	    if(searchObj.getRelationship()!=null)
	    {
		 // relationship
		    {
				SearchColumnDefiniton<Object> columnDef = new SearchColumnDefiniton<Object>() {
	
					@Override
					public Object getCellValue(SearchResultObject rowValue) {
						return null;
					}
	
					@Override
					public void setCellValue(SearchResultObject rowValue, Object cellValue) {
					
					}
	
				};
				columnDef.setCellRenderer(new CellRenderer<SearchResultObject, Object>() {
					public void renderRowValue(SearchResultObject rowValue, ColumnDefinition<SearchResultObject, Object> columnDef,AbstractCellView<SearchResultObject> view) {
						view.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
						view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_DEFAULT);
						Widget w = (Widget) getRelationshipColumn((RelationshipObject)rowValue.getRelationshipObject());
				    	w.addStyleName("gwt-NoBorder");
				    	view.setWidget(w);
					}
				});
				//columnDef.setMinimumColumnWidth(150);
				//columnDef.setPreferredColumnWidth(150);
				//columnDef.setMaximumColumnWidth(150);
				columnDef.setColumnSortable(false);
				tableDefinition.addColumnDefinition(columnDef);
		    }
		    
		 // destinationConcept
		    {
				SearchColumnDefiniton<Object> columnDef = new SearchColumnDefiniton<Object>() {
	
					@Override
					public Object getCellValue(SearchResultObject rowValue) {
						return null;
					}
	
					@Override
					public void setCellValue(SearchResultObject rowValue, Object cellValue) {
					
					}
	
				};
				columnDef.setCellRenderer(new CellRenderer<SearchResultObject, Object>() {
					public void renderRowValue(SearchResultObject rowValue, ColumnDefinition<SearchResultObject, Object> columnDef,AbstractCellView<SearchResultObject> view) {
						view.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
						view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_DEFAULT);
						Widget w = (Widget) getDestConceptColumn((ArrayList<ConceptObject>)rowValue.getDestConceptObject(), type);
				    	w.addStyleName("gwt-NoBorder");
				    	view.setWidget(w);
					}
				});
				//columnDef.setMinimumColumnWidth(150);
				//columnDef.setPreferredColumnWidth(150);
				//columnDef.setMaximumColumnWidth(150);
				columnDef.setColumnSortable(false);
				tableDefinition.addColumnDefinition(columnDef);
		    }
	    }
	    else
	    {
	    
			// Date
			{
				SearchColumnDefiniton<Date> columnDef = new SearchColumnDefiniton<Date>() {
					@Override
					public Date getCellValue(SearchResultObject rowValue) {
						return rowValue.getConceptObject().getDateModified();
					}
				
					@Override
					public void setCellValue(SearchResultObject rowValue, Date cellValue) {
						rowValue.getConceptObject().setDateModified(cellValue);
						
					}
	
				};
				columnDef.setCellRenderer(new CellRenderer<SearchResultObject, Date>() {
					public void renderRowValue(SearchResultObject rowValue, ColumnDefinition<SearchResultObject, Date> columnDef,AbstractCellView<SearchResultObject> view) {
						view.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
						view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
						view.setStyleName("gwt-ScrollTable-NoLink");
						view.setHTML(""+"&nbsp;"+formatDate(rowValue.getConceptObject().getDateModified()));
					}
				});
				columnDef.setMinimumColumnWidth(150);
				columnDef.setPreferredColumnWidth(150);
				columnDef.setMaximumColumnWidth(150);
				columnDef.setColumnSortable(true);
				tableDefinition.addColumnDefinition(columnDef);
		    }
	    }
	    
	    return tableDefinition;
	  }

	 private class SearchTableModel extends MutableTableModel<SearchResultObject> {
			  
		@SuppressWarnings("unchecked")
		public void requestRows(final Request request, final Callback<SearchResultObject> callback) {
			
			// Send RPC request for data
			Service.searchSerice.requestSearchResultsRows(request, searchObj, MainApp.userOntology, new AsyncCallback<ArrayList<SearchResultObject>>() 
					{
				        public void onFailure(final Throwable caught) {
				          callback.onFailure(new Exception(constants.searchListError()));
				        }
	
				        public void onSuccess(ArrayList<SearchResultObject> rList) {
							Response response = new SerializableResponse(rList);
							callback.onRowsReady(request, (Response) response);
				        }
			      });
		}

		@Override
		protected boolean onRowInserted(int beforeRow) {
			return false;
		}

		@Override
		protected boolean onRowRemoved(int row) {
			return false;
		}

		@Override
		protected boolean onSetRowValue(int row, SearchResultObject rowValue) {
			return false;
		}
	}
	
	 public  Widget getConceptLabelPanel(ConceptObject cObj, int type)
	 {
		FlowPanel  labelTab = new FlowPanel ();
		labelTab.setSize("100%", "100%");
		labelTab.ensureDebugId("cwFlowPanel");

		if(cObj.getUri()!=null)
		{
			HashMap<String, TermObject> hm = cObj.getTerm();
			
			ArrayList<String> sortedList = new ArrayList<String>();
			HashMap<String, Boolean> checkMainLabelList = new HashMap<String, Boolean>();
			for (Iterator<TermObject> iterator = hm.values().iterator(); iterator.hasNext();) 
			{
				TermObject tObj = (TermObject) iterator.next();
				if(MainApp.userSelectedLanguage.contains(tObj.getLang().toLowerCase()))
				{
					if(!tObj.isMainLabel()){
						sortedList.add(tObj.getLang().toLowerCase()+"###"+tObj.getLabel());	
					}else{
						sortedList.add(tObj.getLang().toLowerCase()+"###"+tObj.getLabel());	
						checkMainLabelList.put(tObj.getLang()+"###"+tObj.getLabel(), tObj.isMainLabel());
					} 
				}
			}
			Collections.sort(sortedList, String.CASE_INSENSITIVE_ORDER);
			
			for (int i = 0; i < sortedList.size(); i++) {
				String str =  (String) sortedList.get(i);
				String[] element = str.split("###");
				if(element.length==2){
					String label = "";
					if(checkMainLabelList.get(str) != null && checkMainLabelList.get(str))
					{
						if(label.length()==0)
							label = label + "<b>"+ element[1] + " ("+element[0]+")</b>";
						else
							label = label +";&nbsp;"+ "<b>"+ element[1] + " ("+element[0]+")</b>";
						
					}
					else
					{
						if(label.length()==0)
							label = label + element[1] + " ("+element[0]+")";
						else
							label = label +";&nbsp;"+ element[1] + " ("+element[0]+")";
					}
					
					
					labelTab.add(wrapFlow(getLabelPanel(type, TreeItemAOS.getColorForTreeItem(cObj.getStatus(), label).getHTML() ,cObj.getUri(), cObj.getUri(), cObj.getScheme(), Style.Link, true, InfoTab.term, cObj.getBelongsToModule())));
					if(i<(sortedList.size()-1))
					{
						HTML htmlLabel = new HTML(";&nbsp;");
						htmlLabel.setStyleName(Style.Link);
						labelTab.add(wrapFlow(htmlLabel));
					}
				}
			}
			
			if(labelTab.getWidgetCount()<1)
				labelTab.add(getLabelPanel(type, constants.searchLanguageNotFound(), cObj.getUri(), cObj.getUri(), cObj.getScheme(), Style.Link, true, InfoTab.term, cObj.getBelongsToModule()));

		}
		return labelTab;
	}
	 
	 public Widget getConceptColumn(ConceptObject cObj, int type)
	 {
		 String imgURL = "";
		 if(cObj.getUri().startsWith(ModelConstants.ONTOLOGYBASENAMESPACE))
			 imgURL = "images/concept_logo.gif";
		 else 
			 imgURL = "images/category_logo.gif";
		 
		 HorizontalPanel panel = new HorizontalPanel();
			panel.add(new Image(imgURL));
			panel.add(new Spacer("15", "15"));
			panel.add(getConceptLabelPanel(cObj, type));
			return panel;
	 }
	 
	 public Widget getDestConceptColumn(ArrayList<ConceptObject> cObjList, int type)
	 {
		 VerticalPanel panel = new VerticalPanel();
		 panel.setSpacing(5);
		 for(ConceptObject cObj: cObjList)
		 {
			 panel.add(getConceptColumn(cObj, type));
		 }
		return panel;
	 }
	 
	 public  Widget getRelationshipLabelPanel(RelationshipObject rObj)
	 {
		FlowPanel  labelTab = new FlowPanel ();
		labelTab.setSize("100%", "100%");
		labelTab.ensureDebugId("cwFlowPanel");

		if(rObj.getUri()!=null)
		{
			ArrayList<LabelObject> hm = rObj.getLabelList();
			
			ArrayList<String> sortedList = new ArrayList<String>();
			for (Iterator<LabelObject> iterator = hm.iterator(); iterator.hasNext();) 
			{
				LabelObject labelObj = (LabelObject) iterator.next();
				if(MainApp.userSelectedLanguage.contains(labelObj.getLanguage().toLowerCase()))
				{
				     sortedList.add(labelObj.getLanguage().toLowerCase()+"###"+labelObj.getLabel());		
				}
			}
			Collections.sort(sortedList, String.CASE_INSENSITIVE_ORDER);
			
			for (int i = 0; i < sortedList.size(); i++) {
				String str =  (String) sortedList.get(i);
				String[] element = str.split("###");
				if(element.length==2){
					labelTab.add(wrapFlow(new Label(element[1]+" ("+element[0]+")")));
					if(i<(sortedList.size()-1))
					{
						HTML label = new HTML(";&nbsp;");
						label.setStyleName(Style.Link);
						labelTab.add(wrapFlow(label));
					}
				}
			}
		}
		
		if(labelTab.getWidgetCount()<1)
		{
			Image image = new Image("images/not-found.gif");
			image.setTitle(constants.searchLanguageNotFound());
			labelTab.add(image);
		}
		return labelTab;
	}

	 public  Widget getLabelPanel(final int type, String text, String title, final String link, final String schemeURI, String style, final boolean isAddAction, final int tab, final int belongsToModule)
		{
			if(!text.equals(""))
			{
				HTML label = new HTML();
				label.setHTML("&nbsp;"+text);
				if(link!=null)
				{
					if(!link.equals(ModelConstants.CDOMAINCONCEPT))
					{
						label.setStyleName(style);
						label.setTitle(title);
						label.addClickHandler(new ClickHandler() {
							public void onClick(ClickEvent event) {
								onLabelClicked(link, schemeURI, isAddAction, tab, belongsToModule, type);
							}
						});
					}
				}
				return label;
			}
			else
			{
				Image image = new Image("images/label-not-found.gif");
				image.setStyleName(style);
				image.setTitle(constants.homeNoTerm());
				image.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						onLabelClicked(link, schemeURI, isAddAction, tab, belongsToModule, type);
					}
				});
				return image;
			}
		}
	 
	public Widget getRelationshipColumn(RelationshipObject rObj)
	{
		HorizontalPanel panel = new HorizontalPanel();
		panel.add(new Image("images/relationship-object-logo.gif"));
		panel.add(new Spacer("15", "15"));
		panel.add(getRelationshipLabelPanel(rObj));
		return panel;
	}
		
	  public void onLabelClicked(String link, String schemeURI, boolean isAddAction,int tab, int belongsToModule, int type){
		  ModuleManager.gotoItem(link, schemeURI, isAddAction, tab, belongsToModule, type);
	  }
	  
		public static Widget wrapFlow(Widget w)
		{
			  VerticalPanel vp = new VerticalPanel();
			  DOM.setStyleAttribute(vp.getElement(), "float", "left");
			  DOM.setStyleAttribute(vp.getElement(), "display", "inline");
			  vp.add(w);
			  return vp;
		}
		
		public String formatDate(Date d)
		{
			if(d==null)
				return "";
			else
				return sdf.format(d);
		}
		
		public void filterByLanguage(){
			if(getScrollTable() != null)
			{						
				if(getDataTable()!= null)
				{				
					for(int i=0;i<getDataTable().getRowCount();i++){
						ConceptObject cObj = (ConceptObject) getPagingScrollTable().getRowValue(i).getConceptObject();
						Widget w = getConceptColumn(cObj, type);
						w.addStyleName("gwt-NoBorder");
						getDataTable().setWidget(i, 0, w);
						
						if(searchObj.getRelationship()!=null)
						{
							Widget rel = (Widget) getRelationshipColumn((RelationshipObject) getPagingScrollTable().getRowValue(i).getRelationshipObject());
							rel.addStyleName("gwt-NoBorder");
					    	getDataTable().setWidget(i, 1, rel);
					    	
							Widget destConcept = (Widget) getDestConceptColumn((ArrayList<ConceptObject>) getPagingScrollTable().getRowValue(i).getDestConceptObject(), type);
							destConcept.addStyleName("gwt-NoBorder");
					    	getDataTable().setWidget(i, 2, destConcept);
						}
					}
				}
			}
		}
}
