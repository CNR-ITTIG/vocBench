package org.fao.aoscs.client.module.validation.widgetlib;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.OWLActionConstants;
import org.fao.aoscs.client.module.constant.OWLStatusConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.module.effects.FlashEffect;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.OwlAction;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.Users;
import org.fao.aoscs.domain.Validation;
import org.fao.aoscs.domain.ValidationFilter;

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
import com.google.gwt.gen2.table.client.TableDefinition.AbstractCellView;
import com.google.gwt.gen2.table.client.TableModelHelper.Request;
import com.google.gwt.gen2.table.client.TableModelHelper.Response;
import com.google.gwt.gen2.table.client.TableModelHelper.SerializableResponse;
import com.google.gwt.gen2.table.override.client.FlexTable;
import com.google.gwt.gen2.table.override.client.FlexTable.FlexCellFormatter;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;



public class ValidationTable {
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private ValidationFilter vFilter = new ValidationFilter();
	private	ArrayList<OwlStatus> statusList = new ArrayList<OwlStatus>(); 
	private	ArrayList<Users> userList = new ArrayList<Users>();
	private	ArrayList<OwlAction> actionList = new ArrayList<OwlAction>(); 
	private	HashMap<Integer, Integer> acceptvalidationList = new HashMap<Integer, Integer>();
	private	HashMap<Integer, Integer> rejectvalidationList = new HashMap<Integer, Integer>();
	
	/**
	   * The {@link CachedTableModel} around the main table model.
	   */
	  private CachedTableModel<Validation> cachedTableModel = null;
	  
	  /**
	   * The {@link PagingScrollTable}.
	   */
	  private PagingScrollTable<Validation> pagingScrollTable = null;
	  
	  
	  /**
	   * The {@link DataSourceTableModel}.
	   */
	  private ValidationTableModel tableModel = null;

	  /**
	   * The {@link DefaultTableDefinition}.
	   */
	  private DefaultTableDefinition<Validation> tableDefinition = null;

	  /**
	   * @return the cached table model
	   */
	  public CachedTableModel<Validation> getCachedTableModel() {
	    return cachedTableModel;
	  }

	 
	  /**
	   * @return the {@link PagingScrollTable}
	   */
	  public PagingScrollTable<Validation> getPagingScrollTable() {
	    return pagingScrollTable;
	  }

	  /**
	   * @return the table definition of columns
	   */
	  public DefaultTableDefinition<Validation> getTableDefinition() {
	    return tableDefinition;
	  }

	  /**
	   * @return the table model
	   */
	  public ValidationTableModel getTableModel() {
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
	  protected FixedWidthFlexTable createHeaderTable() {
	    FixedWidthFlexTable headerTable = new FixedWidthFlexTable();
	    headerTable.setHeight("35");
	    // Level 1 headers
	    FlexCellFormatter formatter = headerTable.getFlexCellFormatter();
	    headerTable.setWidget(0,0,new HTML(constants.valConcept()));
		formatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		headerTable.setWidget(0,1,new HTML(constants.valChange()));
		formatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
		headerTable.setWidget(0,2,new HTML(constants.valOldValue()));
		formatter.setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_CENTER);
		headerTable.setWidget(0,3,new HTML(constants.valAction()));
		formatter.setHorizontalAlignment(0, 3, HasHorizontalAlignment.ALIGN_CENTER);
		headerTable.setWidget(0,4,new HTML(constants.valOwner()));
		formatter.setHorizontalAlignment(0, 4, HasHorizontalAlignment.ALIGN_CENTER);
		headerTable.setWidget(0,5,new HTML(constants.valModifier()));
		formatter.setHorizontalAlignment(0, 5, HasHorizontalAlignment.ALIGN_CENTER);
		headerTable.setWidget(0,6,new HTML(constants.valCreateDate()));
		formatter.setHorizontalAlignment(0, 6, HasHorizontalAlignment.ALIGN_CENTER);
		headerTable.setWidget(0,7,new HTML(constants.valModifiedDate()));
		formatter.setHorizontalAlignment(0, 7, HasHorizontalAlignment.ALIGN_CENTER);
		headerTable.setWidget(0,8,new HTML(constants.valStatus()));
		formatter.setHorizontalAlignment(0, 8, HasHorizontalAlignment.ALIGN_CENTER);
		headerTable.setWidget(0,9,new HTML(constants.valNote()));
		formatter.setHorizontalAlignment(0, 9, HasHorizontalAlignment.ALIGN_CENTER);
		headerTable.setWidget(0,10,new HTML("&nbsp;"));
		formatter.setHorizontalAlignment(0, 10, HasHorizontalAlignment.ALIGN_CENTER);

	    return headerTable;
	  }

	  /**
	   * @return the newly created data table.
	   */
	  protected FixedWidthGrid createDataTable() {
	    FixedWidthGrid dataTable = new FixedWidthGrid();
	    //dataTable.setSelectionPolicy(SelectionPolicy.CHECKBOX);
	    return dataTable;
	  }

	  protected AbstractScrollTable createScrollTable(
	      FixedWidthFlexTable headerTable, FixedWidthGrid dataTable, int size) {
	    // Setup the controller
	    tableModel = new ValidationTableModel();
	    cachedTableModel = new CachedTableModel<Validation>(tableModel);
	    cachedTableModel.setPreCachedRowCount(20);
	    cachedTableModel.setPostCachedRowCount(20);
	    cachedTableModel.setRowCount(size);
	    	
	    // Create a TableCellRenderer
	    TableDefinition<Validation> tableDef = createTableDefinition();

	    // Create the scroll table
	    pagingScrollTable = new PagingScrollTable<Validation>(cachedTableModel, dataTable, headerTable, tableDef);
	    pagingScrollTable.setPageSize(10);
	    pagingScrollTable.setEmptyTableWidget(new HTML(constants.valNoData()));

	    // Setup the bulk renderer
	    FixedWidthGridBulkRenderer<Validation> bulkRenderer = new FixedWidthGridBulkRenderer<Validation>(dataTable, pagingScrollTable);
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
	
	public void setTable(ValidationFilter vFilter, ArrayList<OwlStatus> statusList, ArrayList<Users> userList, ArrayList<OwlAction> actionList, HashMap<Integer, Integer> acceptvalidationList, HashMap<Integer, Integer> rejectvalidationList, int size)
	{
		this.vFilter = vFilter;
		this.statusList = statusList;
		this.userList = userList;
		this.actionList = actionList;
		this.acceptvalidationList = acceptvalidationList;
		this.rejectvalidationList = rejectvalidationList;
		
		// Add the main layout to the page
		layout.setSize("100%", "100%");
		layout.setCellPadding(0);
		layout.setCellSpacing(0);
		
		// Initialize the tables
		FixedWidthFlexTable headerTable = createHeaderTable();
		FixedWidthGrid dataTable = createDataTable();
		scrollTable = createScrollTable(headerTable, dataTable, size);
		scrollTable.setWidth(MainApp.getBodyPanelWidth() - 40+ "px");
		scrollTable.setHeight(MainApp.getBodyPanelHeight() - 105 +"px");
		
		// Add the scroll table to the layout
		layout.setWidget(0, 0, scrollTable);
		Window.addResizeHandler(new ResizeHandler()
	    {
	    	public void onResize(ResizeEvent event) {
				scrollTable.setWidth(MainApp.getBodyPanelWidth() - 40 + "px");
				scrollTable.redraw();
			}
		});
		
		// Create an paging options
		PagingOptions pagingOptions = new PagingOptions(getPagingScrollTable());
		pagingOptions.setHeight("100%");
	    HorizontalPanel hp = new HorizontalPanel();
	    hp.setSize("100%", "100%");
	    hp.add(pagingOptions);
	    hp.setStyleName("gwt-PagingOptionsPanel");
	    hp.setCellHeight(pagingOptions, "100%");
	    hp.setCellWidth(pagingOptions, "100%");
		layout.insertRow(1);
		layout.setWidget(1,0, hp);
		
		// Do any required post processing
		onModuleLoaded();
}
	
	public abstract class ValidationColumnDefiniton<ColType> extends AbstractColumnDefinition<Validation, ColType> {

		/**
		 * Construct a new {@link ValidationColumnDefiniton}.
		 * 
		 */
		public ValidationColumnDefiniton() {}

	}
	
	 /**
	   * @return the {@link TableDefinition} with all ColumnDefinitions defined.
	   */
	  private TableDefinition<Validation> createTableDefinition() {
	    

	    // Create the table definition
	    tableDefinition = new DefaultTableDefinition<Validation>();
	    
	    // concept/term
	    {
			ValidationColumnDefiniton<Object> columnDef = new ValidationColumnDefiniton<Object>() {
				@Override
				public Object getCellValue(Validation rowValue) {
					return rowValue.getConceptObject();
				}
			
				@Override
				public void setCellValue(Validation rowValue, Object cellValue) {
					rowValue.setConceptObject((ConceptObject) cellValue);
					
				}
			};
			columnDef.setCellRenderer(new CellRenderer<Validation, Object>() {
				public void renderRowValue(Validation rowValue, ColumnDefinition<Validation, Object> columnDef,AbstractCellView<Validation> view) {
					view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_DEFAULT);
					view.setStyleName(Style.Link);
					Widget w = Validator.getLabelPanel(0, (Validation) rowValue, Style.Link);
			    	w.addStyleName("gwt-NoBorder");
					view.setWidget(w);
				}
			});
			columnDef.setMinimumColumnWidth(150);
			columnDef.setPreferredColumnWidth(200);
			columnDef.setMaximumColumnWidth(300);
			columnDef.setColumnSortable(false);
			tableDefinition.addColumnDefinition(columnDef);
	    }
	    
	    // newvalue
	    {
			ValidationColumnDefiniton<Object> columnDef = new ValidationColumnDefiniton<Object>() {
				@Override
				public Object getCellValue(Validation rowValue) {
					return rowValue.getNewObject();
				}
			
				@SuppressWarnings("unchecked")
				@Override
				public void setCellValue(Validation rowValue, Object cellValue) {
					rowValue.setNewObject((ArrayList<LightEntity>) cellValue);
					
				}
			};
			columnDef.setCellRenderer(new CellRenderer<Validation, Object>() {
				public void renderRowValue(Validation rowValue, ColumnDefinition<Validation, Object> columnDef,AbstractCellView<Validation> view) {
					view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_DEFAULT);
					view.setStyleName(Style.Link);
					Widget w = Validator.getLabelPanel(1, (Validation) rowValue, Style.Link);
			    	w.addStyleName("gwt-NoBorder");
					view.setWidget(w);
				}
			});
			columnDef.setMinimumColumnWidth(150);
			columnDef.setPreferredColumnWidth(200);
			columnDef.setMaximumColumnWidth(300);
			columnDef.setColumnSortable(false);
			tableDefinition.addColumnDefinition(columnDef);
	    }
	    
	    // oldvalue
	    {
			ValidationColumnDefiniton<Object> columnDef = new ValidationColumnDefiniton<Object>() {
				@Override
				public Object getCellValue(Validation rowValue) {
					return rowValue.getOldObject();
				}
			
				@SuppressWarnings("unchecked")
				@Override
				public void setCellValue(Validation rowValue, Object cellValue) {
					rowValue.setOldObject((ArrayList<LightEntity>)cellValue);
					
				}
			};
			columnDef.setCellRenderer(new CellRenderer<Validation, Object>() {
				public void renderRowValue(Validation rowValue, ColumnDefinition<Validation, Object> columnDef,AbstractCellView<Validation> view) {
					view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_DEFAULT);
					view.setStyleName(Style.Link);
					Widget w = Validator.getLabelPanel(2, (Validation) rowValue, Style.Link);
			    	w.addStyleName("gwt-NoBorder");
					view.setWidget(w);
				}
			});
			columnDef.setMinimumColumnWidth(150);
			columnDef.setPreferredColumnWidth(200);
			columnDef.setMaximumColumnWidth(300);
			columnDef.setColumnSortable(false);
			tableDefinition.addColumnDefinition(columnDef);
	    }

	    // Action
		{
			ValidationColumnDefiniton<Integer> columnDef = new ValidationColumnDefiniton<Integer>() {
				@Override
				public Integer getCellValue(Validation rowValue) {
					return rowValue.getAction();
				}
			
				@Override
				public void setCellValue(Validation rowValue, Integer cellValue) {
					rowValue.setAction(cellValue);
					
				}
			};
			columnDef.setCellRenderer(new CellRenderer<Validation, Integer>() {
				public void renderRowValue(Validation rowValue, ColumnDefinition<Validation, Integer> columnDef,AbstractCellView<Validation> view) {
					view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					view.setStyleName(Style.Link);
					view.setHTML(Validator.getActionFromID(rowValue.getAction(),actionList)+""+Validator.getActionChildFromID(rowValue.getAction(),actionList)+"&nbsp;");
				}
			});
			columnDef.setMinimumColumnWidth(100);
			columnDef.setPreferredColumnWidth(100);
			columnDef.setMaximumColumnWidth(250);
			columnDef.setColumnSortable(true);
			tableDefinition.addColumnDefinition(columnDef);
	    }
		
		// Owner
		{
			ValidationColumnDefiniton<Integer> columnDef = new ValidationColumnDefiniton<Integer>() {
				@Override
				public Integer getCellValue(Validation rowValue) {
					return rowValue.getOwnerId();
				}

				@Override
				public void setCellValue(Validation rowValue, Integer cellValue) {
					rowValue.setOwnerId(cellValue);
				}
			};
			columnDef.setCellRenderer(new CellRenderer<Validation, Integer>() {
				public void renderRowValue(Validation rowValue, ColumnDefinition<Validation, Integer> columnDef,AbstractCellView<Validation> view) {
					view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					view.setStyleName(Style.Link);
					Widget w = Validator.makeUsers(Validator.getUserNameFromID(rowValue.getOwnerId(),userList),""+rowValue.getOwnerId(),Style.Link);
					w.addStyleName("gwt-NoBorder");
					view.setWidget(w);
				}
			});
			columnDef.setMinimumColumnWidth(75);
			columnDef.setPreferredColumnWidth(100);
			columnDef.setMaximumColumnWidth(150);
			columnDef.setColumnSortable(true);
			tableDefinition.addColumnDefinition(columnDef);
	    }
		
		// Modifier
		{
			ValidationColumnDefiniton<Integer> columnDef = new ValidationColumnDefiniton<Integer>() {
				@Override
				public Integer getCellValue(Validation rowValue) {
					return rowValue.getModifierId();
				}

				@Override
				public void setCellValue(Validation rowValue, Integer cellValue) {
					rowValue.setModifierId(cellValue);
				}
			};
			columnDef.setCellRenderer(new CellRenderer<Validation, Integer>() {
				public void renderRowValue(Validation rowValue, ColumnDefinition<Validation, Integer> columnDef,AbstractCellView<Validation> view) {
					view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					view.setStyleName(Style.Link);
					Widget w = Validator.makeUsers(Validator.getUserNameFromID(rowValue.getModifierId(),userList),""+rowValue.getModifierId(),Style.Link);
					w.addStyleName("gwt-NoBorder");
					view.setWidget(w);
				}
			});
			columnDef.setMinimumColumnWidth(75);
			columnDef.setPreferredColumnWidth(100);
			columnDef.setMaximumColumnWidth(150);
			columnDef.setColumnSortable(true);
			tableDefinition.addColumnDefinition(columnDef);
	    }
		
		// CreationDate
		{
			ValidationColumnDefiniton<Date> columnDef = new ValidationColumnDefiniton<Date>() {
				@Override
				public Date getCellValue(Validation rowValue) {
					return rowValue.getDateCreate();
				}

				@Override
				public void setCellValue(Validation rowValue, Date cellValue) {
					rowValue.setDateCreate(cellValue);
				}
			};
			columnDef.setCellRenderer(new CellRenderer<Validation, Date>() {
				public void renderRowValue(Validation rowValue, ColumnDefinition<Validation, Date> columnDef,AbstractCellView<Validation> view) {
					view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					view.setStyleName("gwt-ScrollTable-NoLink");
					VerticalPanel w = new VerticalPanel();
					HTML date = new HTML(DateTimeFormat.getFormat("dd-MM-yyyy").format(rowValue.getDateCreate()));
					HTML time = new HTML(DateTimeFormat.getFormat("HH:mm:ss").format(rowValue.getDateCreate()));
					w.add(date);
					w.add(time);
					w.setCellHorizontalAlignment(date, HasHorizontalAlignment.ALIGN_RIGHT);
					w.setCellHorizontalAlignment(time, HasHorizontalAlignment.ALIGN_RIGHT);
					w.addStyleName("gwt-NoBorder");
					view.setWidget(w);
				}
			});
			columnDef.setMinimumColumnWidth(75);
			columnDef.setPreferredColumnWidth(75);
			columnDef.setMaximumColumnWidth(75);
			columnDef.setColumnSortable(true);
			tableDefinition.addColumnDefinition(columnDef);
	    }
		
		// Modified date
		{
			ValidationColumnDefiniton<Date> columnDef = new ValidationColumnDefiniton<Date>() {
				@Override
				public Date getCellValue(Validation rowValue) {
					return rowValue.getDateModified();
				}

				@Override
				public void setCellValue(Validation rowValue, Date cellValue) {
					rowValue.setDateModified(cellValue);
				}
			};
			columnDef.setCellRenderer(new CellRenderer<Validation, Date>() {
				public void renderRowValue(Validation rowValue, ColumnDefinition<Validation, Date> columnDef,AbstractCellView<Validation> view) {
					view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					view.setStyleName("gwt-ScrollTable-NoLink");
					VerticalPanel w = new VerticalPanel();
					HTML date = new HTML(DateTimeFormat.getFormat("dd-MM-yyyy").format(rowValue.getDateModified()));
					HTML time = new HTML(DateTimeFormat.getFormat("HH:mm:ss").format(rowValue.getDateModified()));
					w.add(date);
					w.add(time);
					w.setCellHorizontalAlignment(date, HasHorizontalAlignment.ALIGN_RIGHT);
					w.setCellHorizontalAlignment(time, HasHorizontalAlignment.ALIGN_RIGHT);
					w.addStyleName("gwt-NoBorder");
					view.setWidget(w);
				}
			});
			columnDef.setMinimumColumnWidth(75);
			columnDef.setPreferredColumnWidth(75);
			columnDef.setMaximumColumnWidth(75);
			columnDef.setColumnSortable(true);
			tableDefinition.addColumnDefinition(columnDef);
	    }
			
		// Status
		{
			ValidationColumnDefiniton<Integer> columnDef = new ValidationColumnDefiniton<Integer>() {
				@Override
				public Integer getCellValue(Validation rowValue) {
					return rowValue.getStatus();
				}
			
				@Override
				public void setCellValue(Validation rowValue, Integer cellValue) {
					rowValue.setStatus(cellValue);
					
				}
			};
			columnDef.setCellRenderer(new CellRenderer<Validation, Integer>() {
				public void renderRowValue(Validation rowValue, ColumnDefinition<Validation, Integer> columnDef,AbstractCellView<Validation> view) {
					view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					view.setStyleName("gwt-ScrollTable-NoLink");
					view.setHTML(""+Validator.getStatusFromID(rowValue.getStatus(),statusList));
				}
			});
			columnDef.setMinimumColumnWidth(75);
			columnDef.setPreferredColumnWidth(75);
			columnDef.setMaximumColumnWidth(100);
			columnDef.setColumnSortable(true);
			tableDefinition.addColumnDefinition(columnDef);
	    }
		
		// Note
		{
			ValidationColumnDefiniton<String> columnDef = new ValidationColumnDefiniton<String>() {
				@Override
				public String getCellValue(Validation rowValue) {
					return rowValue.getNote();
				}

				@Override
				public void setCellValue(Validation rowValue, String cellValue) {
					rowValue.setNote(cellValue);
				}
			};
			columnDef.setCellRenderer(new CellRenderer<Validation, String>() {
				public void renderRowValue(Validation rowValue, ColumnDefinition<Validation, String> columnDef,AbstractCellView<Validation> view) {
					view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					view.setStyleName("gwt-ScrollTable-NoLink");
					TextArea ta = new TextArea();
					ta.setStyleName("validate-TextArea");
					ta.setVisibleLines(2);
					ta.setSize("100%", "50px");
					ta.setReadOnly(true);
					ta.setText(rowValue.getNote()==null?"":rowValue.getNote());
					ta.addStyleName("gwt-NoBorder");
					view.setWidget(ta);
				}
			});
			columnDef.setMinimumColumnWidth(50);
			columnDef.setPreferredColumnWidth(100);
			columnDef.setMaximumColumnWidth(200);
			columnDef.setColumnSortable(false);
			tableDefinition.addColumnDefinition(columnDef);
	    }
		
		// Validation Panel
		{
			ValidationColumnDefiniton<Object> columnDef = new ValidationColumnDefiniton<Object>() {
				@Override
				public Object getCellValue(Validation rowValue) {
					return null;
				}

				@Override
				public void setCellValue(Validation rowValue, Object cellValue) {
				}
			};
			columnDef.setCellRenderer(new CellRenderer<Validation, Object>() {
				public void renderRowValue(Validation rowValue, ColumnDefinition<Validation, Object> columnDef,AbstractCellView<Validation> view) {
					view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					view.setStyleName("gwt-ScrollTable-NoLink");
					Widget w = makeFunctionPanel(rowValue, view.getRowIndex(), statusList);
					w.addStyleName("gwt-NoBorder");
					view.setWidget(w);
				}
			});
			columnDef.setMinimumColumnWidth(60);
			columnDef.setPreferredColumnWidth(60);
			columnDef.setMaximumColumnWidth(60);
			columnDef.setColumnSortable(false);
			tableDefinition.addColumnDefinition(columnDef);
	    }
	    
	    return tableDefinition;
	  }

	
	  private class ValidationTableModel extends MutableTableModel<Validation> {
			  
		@SuppressWarnings("unchecked")
		public void requestRows(final Request request, final Callback<Validation> callback) {
			
			// Send RPC request for data
			Service.validationService.requestValidationRows(request, vFilter, new AsyncCallback<ArrayList<Validation>>() 
			{
		        public void onFailure(Throwable caught) {
		          callback.onFailure(new Exception(constants.valListError()));
		        }
		
		        public void onSuccess(ArrayList<Validation> list) {
		        	ArrayList<Validation> current = new ArrayList<Validation>();
		        	for (int i = 0; i < list.size(); i++) {
		        		Validation v = (Validation) list.get(i);
		        		v.setStatusColumn(8);
		    			v.setNoteColumn(9);
		    			v.setShowUser(new Boolean(true));
		    			v.setShowStatus(new Boolean(true));
		    			v.setShowAction(new Boolean(true));
		    			v.setShowDate(new Boolean(true));
		    			v.setStatusLabel(Validator.getStatusFromID(v.getStatus(),statusList));
						current.add(v);
					}
		        	
		        	Response response = new SerializableResponse(current);
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
		protected boolean onSetRowValue(int row, Validation rowValue) {
			return false;
		}
	}
	
	  public VerticalPanel makeFunctionPanel(final Validation v, final int row, final ArrayList<OwlStatus> statusList)
		{
			final int rowstatus = v.getStatus();
			VerticalPanel vPanel = new VerticalPanel();

			final CheckBox accept = new CheckBox(constants.buttonAccept());
			final CheckBox reject = new CheckBox(constants.buttonReject());

			accept.setTitle(constants.buttonAccept());
			reject.setTitle(constants.buttonReject());
			
			accept.addStyleName("acceptrejectpanel");
			reject.addStyleName("acceptrejectpanel");
			
			accept.setEnabled(MainApp.permissionTable.contains(OWLActionConstants.VALIDATIONACCEPTED, -1));
			reject.setEnabled(MainApp.permissionTable.contains(OWLActionConstants.VALIDATIONREJECTED, -1));
			
			accept.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					Widget sender = (Widget) event.getSource();
					if(((CheckBox)sender).getValue())
					{	
						changeStatus(acceptvalidationList, row, v.getStatusColumn() ,v.getStatus(),statusList);
						reject.setEnabled(false);
					}
					else
					{
						getDataTable().getCellFormatter().removeStyleName(row, v.getStatusColumn(), "validate-red");
						getDataTable().setWidget(row, v.getStatusColumn(), new HTML(Validator.getStatusFromID(v.getStatus(), statusList)));
						checkEnable(accept, reject, rowstatus);
					}

				}
			});
			reject.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					Widget sender = (Widget) event.getSource();
					if(((CheckBox)sender).getValue())
					{
						if(rejectvalidationList.containsKey(new Integer(v.getStatus())))
						{
							if(v.getStatus()==OWLStatusConstants.VALIDATED_ID)
							{
								if(v.getOldStatus()==0) 
									getDataTable().setWidget(row, v.getStatusColumn(), new HTML(""+Validator.getStatusFromID(OWLStatusConstants.DELETED_ID, statusList)));
								else	
									getDataTable().setWidget(row, v.getStatusColumn(), new HTML(""+Validator.getStatusFromID(v.getOldStatus(), statusList)));
								getDataTable().getCellFormatter().addStyleName(row, v.getStatusColumn(), "validate-red");	
							}
							else if(v.getStatus()==OWLStatusConstants.PROPOSED_DEPRECATED_ID)
							{
								getDataTable().setWidget(row, v.getStatusColumn(), new HTML(""+Validator.getStatusFromID(v.getOldStatus(), statusList)));
								getDataTable().getCellFormatter().addStyleName(row, v.getStatusColumn(), "validate-red");
							}
							else
							{
								int newselectedItem = ((Integer)rejectvalidationList.get(new Integer(v.getStatus()))).intValue();
								getDataTable().setWidget(row, v.getStatusColumn(), new HTML(""+Validator.getStatusFromID(newselectedItem, statusList)));
								getDataTable().getCellFormatter().addStyleName(row, v.getStatusColumn(), "validate-red");
								if(newselectedItem==OWLStatusConstants.DELETED_ID)
								{
									TextArea ta = (TextArea) getDataTable().getWidget(row, v.getNoteColumn());
									ta.setReadOnly(false);
									ta.setFocus(true);
									new FlashEffect().widget(ta).go();
								}
							}
						accept.setEnabled(false);
						}
					}
					else
					{
						TextArea ta = (TextArea) getDataTable().getWidget(row, v.getNoteColumn());
						ta.setText(v.getNote()==null?"":v.getNote());
						ta.setReadOnly(true);
						getDataTable().setWidget(row, v.getStatusColumn(), new HTML(Validator.getStatusFromID(v.getStatus(), statusList)));
						getDataTable().getCellFormatter().removeStyleName(row, v.getStatusColumn(), "validate-red");
						checkEnable(accept, reject, rowstatus);
					}
				}
			});
			
			vPanel.setSpacing(1);
			vPanel.add(accept);
			vPanel.add(reject);
			checkEnable(accept, reject, rowstatus);
			return vPanel;
		}
	  
	  public void checkEnable(CheckBox accept, CheckBox reject, int rowstatus)
		{
			accept.setEnabled(false);
			reject.setEnabled(false);			
			if(MainApp.permissionTable.contains(OWLActionConstants.VALIDATIONACCEPTED, -1) && acceptvalidationList.containsKey(new Integer(rowstatus))) 
				accept.setEnabled(true);
			if(MainApp.permissionTable.contains(OWLActionConstants.VALIDATIONREJECTED, -1) && rejectvalidationList.containsKey(new Integer(rowstatus))) 
				reject.setEnabled(true);
		}
		
		public void changeStatus(HashMap<Integer, Integer> list, int row, int column, int oldselectedItem, ArrayList<OwlStatus> statusList)
		{
			if(list.containsKey(new Integer(oldselectedItem)))
			{
				int newselectedItem = ((Integer)list.get(new Integer(oldselectedItem))).intValue();
				getDataTable().setWidget(row, column, new HTML(""+Validator.getStatusFromID(newselectedItem, statusList)));
				getDataTable().getCellFormatter().addStyleName(row, column, "validate-red");
			}	
		}
}
