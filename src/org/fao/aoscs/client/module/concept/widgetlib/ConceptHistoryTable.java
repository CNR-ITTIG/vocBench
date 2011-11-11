package org.fao.aoscs.client.module.concept.widgetlib;

import java.util.ArrayList;
import java.util.Date;

import net.sf.gilead.pojo.gwt.LightEntity;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.module.validation.widgetlib.Validator;
import org.fao.aoscs.domain.OwlAction;
import org.fao.aoscs.domain.RecentChanges;
import org.fao.aoscs.domain.Users;
import org.fao.aoscs.domain.Validation;

import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;


public class ConceptHistoryTable {
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private String uri;
	private int type; // 1=concept ; 2 = term
	private	 ArrayList<Users> userList = new ArrayList<Users>();
	
	private	ArrayList<OwlAction> actionList = new ArrayList<OwlAction>(); 
	
	 /**
	   * The {@link CachedTableModel} around the main table model.
	   */
	  private CachedTableModel<RecentChanges> cachedTableModel = null;
	  
	  /**
	   * The {@link PagingScrollTable}.
	   */
	  private PagingScrollTable<RecentChanges> pagingScrollTable = null;
	  
	  
	  /**
	   * The {@link DataSourceTableModel}.
	   */
	  private RecentChangesTableModel tableModel = null;

	  /**
	   * The {@link DefaultTableDefinition}.
	   */
	  private DefaultTableDefinition<RecentChanges> tableDefinition = null;

	  /**
	   * @return the cached table model
	   */
	  public CachedTableModel<RecentChanges> getCachedTableModel() {
	    return cachedTableModel;
	  }

	 
	  /**
	   * @return the {@link PagingScrollTable}
	   */
	  public PagingScrollTable<RecentChanges> getPagingScrollTable() {
	    return pagingScrollTable;
	  }

	  /**
	   * @return the table definition of columns
	   */
	  public DefaultTableDefinition<RecentChanges> getTableDefinition() {
	    return tableDefinition;
	  }

	  /**
	   * @return the table model
	   */
	  public RecentChangesTableModel getTableModel() {
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

	    // Level 1 headers
	    FlexCellFormatter formatter = headerTable.getFlexCellFormatter();	    
		headerTable.setHTML(0, 0, constants.conceptChange());
		formatter.setHorizontalAlignment(0, 0,HasHorizontalAlignment.ALIGN_CENTER);
		headerTable.setHTML(0, 1, constants.conceptOldValue());
		formatter.setHorizontalAlignment(0, 1,HasHorizontalAlignment.ALIGN_CENTER);
		headerTable.setHTML(0, 2, constants.conceptAction());
		formatter.setHorizontalAlignment(0, 2,HasHorizontalAlignment.ALIGN_CENTER);
		headerTable.setHTML(0, 3, constants.conceptUser());
		formatter.setHorizontalAlignment(0, 3,HasHorizontalAlignment.ALIGN_CENTER);
		headerTable.setHTML(0, 4, constants.conceptDate());
		formatter.setHorizontalAlignment(0, 4,HasHorizontalAlignment.ALIGN_CENTER);

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
	    tableModel = new RecentChangesTableModel();

	    cachedTableModel = new CachedTableModel<RecentChanges>(tableModel);
	    cachedTableModel.setPreCachedRowCount(8);
	    cachedTableModel.setPostCachedRowCount(8);
	    cachedTableModel.setRowCount(size);
	    	
	    // Create a TableCellRenderer
	    TableDefinition<RecentChanges> tableDef = createTableDefinition();

	    // Create the scroll table
	    pagingScrollTable = new PagingScrollTable<RecentChanges>(cachedTableModel, dataTable, headerTable, tableDef);
	    pagingScrollTable.setPageSize(15);
	    pagingScrollTable.setEmptyTableWidget(new HTML(constants.conceptNoDataToDisplay()));

	    // Setup the bulk renderer
	    FixedWidthGridBulkRenderer<RecentChanges> bulkRenderer = new FixedWidthGridBulkRenderer<RecentChanges>(dataTable, pagingScrollTable);
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
	
	public void setTable(ArrayList<Users> userList, ArrayList<OwlAction> actionList, int size , String uri , final int type) {
		this.uri = uri;
		this.type = type;
		this.userList = userList;
		this.actionList = actionList;

		// Add the main layout to the page
		layout.setWidth("100%");
		layout.setCellPadding(0);
		layout.setCellSpacing(0);
		
		// Initialize the tables
		{
		  // Create the tables
		  FixedWidthFlexTable headerTable = createHeaderTable();
		  FixedWidthGrid dataTable = createDataTable();
		  scrollTable = createScrollTable(headerTable, dataTable, size);	 
		  if(type==1) 
			  scrollTable.setHeight(MainApp.getBodyPanelHeight() - 280+"px");		  
		  else
			  scrollTable.setHeight(240+"px");
		
		  // Add the scroll table to the layout
		  layout.setWidth("100%");
		  layout.setCellPadding(0);
		  layout.setCellSpacing(0);
		  layout.setWidget(0, 0, scrollTable);
		
		}
		
		// Resize the table on window resize
	    Window.addResizeHandler(new ResizeHandler()
	    {
	    	public void onResize(ResizeEvent event) {
	    		scrollTable.setWidth("99%");
	    		if(type==1) 
	  			  scrollTable.setHeight(MainApp.getBodyPanelHeight() - 280+"px");		  
	  		  else
	  			  scrollTable.setHeight(240+"px");
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
	
	public abstract class RecentChangesColumnDefiniton<ColType> extends AbstractColumnDefinition<RecentChanges, ColType> {

		/**
		 * Construct a new {@link RecentChangesColumnDefiniton}.
		 * 
		 */
		public RecentChangesColumnDefiniton() {}

	}
	
	 /**
	   * @return the {@link TableDefinition} with all ColumnDefinitions defined.
	   */
	  private TableDefinition<RecentChanges> createTableDefinition() {
	    

	    // Create the table definition
	    tableDefinition = new DefaultTableDefinition<RecentChanges>();
	    
	    
	    // Change
	    {
			RecentChangesColumnDefiniton<ArrayList<LightEntity>> columnDef = new RecentChangesColumnDefiniton<ArrayList<LightEntity>>() {
				@Override
				public ArrayList<LightEntity> getCellValue(RecentChanges rowValue) {
					return rowValue.getModifiedObject();
				}
			
				@Override
				public void setCellValue(RecentChanges rowValue, ArrayList<LightEntity> cellValue) {
					rowValue.setModifiedObject((ArrayList<LightEntity>)cellValue);
					
				}
			};
			columnDef.setCellRenderer(new CellRenderer<RecentChanges, ArrayList<LightEntity>>() {
				public void renderRowValue(RecentChanges rowValue, ColumnDefinition<RecentChanges, ArrayList<LightEntity>> columnDef,AbstractCellView<RecentChanges> view) {
					view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_DEFAULT);
					view.setStyleName(Style.Link);
					Widget w0 = getTablePanel(1, Style.Link, (ArrayList<LightEntity>) rowValue.getModifiedObject());
			    	w0.addStyleName("gwt-NoBorder");
					view.setWidget(w0);
				}
			});
//			columnDef.setMinimumColumnWidth(50);
			columnDef.setPreferredColumnWidth(200);
//			columnDef.setMaximumColumnWidth(300);
			columnDef.setColumnSortable(false);
			tableDefinition.addColumnDefinition(columnDef);
	    }
	    
	    // oldvalue
	    {
			RecentChangesColumnDefiniton<ArrayList<LightEntity>> columnDef = new RecentChangesColumnDefiniton<ArrayList<LightEntity>>() {
				@Override
				public ArrayList<LightEntity> getCellValue(RecentChanges rowValue) {
					return rowValue.getModifiedObject();
				}
			
				@Override
				public void setCellValue(RecentChanges rowValue, ArrayList<LightEntity> cellValue) {
					rowValue.setModifiedObject((ArrayList<LightEntity>)cellValue);
					
				}
			};
			columnDef.setCellRenderer(new CellRenderer<RecentChanges, ArrayList<LightEntity>>() {
				public void renderRowValue(RecentChanges rowValue, ColumnDefinition<RecentChanges, ArrayList<LightEntity>> columnDef,AbstractCellView<RecentChanges> view) {
					view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_DEFAULT);
					view.setStyleName(Style.Link);
					Widget w0 = getTablePanel(2, Style.Link, (ArrayList<LightEntity>) rowValue.getModifiedObject());
			    	w0.addStyleName("gwt-NoBorder");
					view.setWidget(w0);
				}
			});
//			columnDef.setMinimumColumnWidth(50);
			columnDef.setPreferredColumnWidth(200);
//			columnDef.setMaximumColumnWidth(300);
			columnDef.setColumnSortable(false);
			tableDefinition.addColumnDefinition(columnDef);
	    }

	    // Action
		{
			RecentChangesColumnDefiniton<Integer> columnDef = new RecentChangesColumnDefiniton<Integer>() {
				@Override
				public Integer getCellValue(RecentChanges rowValue) {
					return rowValue.getModifiedActionId();
				}
			
				@Override
				public void setCellValue(RecentChanges rowValue, Integer cellValue) {
					rowValue.setModifiedActionId(cellValue);
					
				}
			};
			columnDef.setCellRenderer(new CellRenderer<RecentChanges, Integer>() {
				public void renderRowValue(RecentChanges rowValue, ColumnDefinition<RecentChanges, Integer> columnDef,AbstractCellView<RecentChanges> view) {
					view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					view.setStyleName("gwt-ScrollTable-NoLink");
					if(rowValue.getModifiedActionId() == 72 || rowValue.getModifiedActionId() == 73){
						Validation v = (Validation)((ArrayList<LightEntity>) rowValue.getModifiedObject()).get(0);
						view.setHTML(Validator.getActionFromID(rowValue.getModifiedActionId(), actionList) + " - " + Validator.getActionFromID(v.getAction(),actionList));						
					}else{
						view.setHTML(Validator.getActionFromID(rowValue.getModifiedActionId(), actionList));						
					}
				}
			});
//			columnDef.setMinimumColumnWidth(50);
			columnDef.setPreferredColumnWidth(150);
//			columnDef.setMaximumColumnWidth(250);
			columnDef.setColumnSortable(true);
			tableDefinition.addColumnDefinition(columnDef);
	    }
		
		// User
		{
			RecentChangesColumnDefiniton<Integer> columnDef = new RecentChangesColumnDefiniton<Integer>() {
				@Override
				public Integer getCellValue(RecentChanges rowValue) {
					return rowValue.getModifierId();
				}
			
				@Override
				public void setCellValue(RecentChanges rowValue, Integer cellValue) {
					rowValue.setModifierId(cellValue);
					
				}
			};
			columnDef.setCellRenderer(new CellRenderer<RecentChanges, Integer>() {
				public void renderRowValue(RecentChanges rowValue, ColumnDefinition<RecentChanges, Integer> columnDef,AbstractCellView<RecentChanges> view) {
					view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					view.setStyleName("gwt-ScrollTable-NoLink");
					view.setHTML(Validator.getUserNameFromID(rowValue.getModifierId(), userList));
				}
			});
//			columnDef.setMinimumColumnWidth(100);
			columnDef.setPreferredColumnWidth(150);
//			columnDef.setMaximumColumnWidth(250);
			columnDef.setColumnSortable(true);
			tableDefinition.addColumnDefinition(columnDef);
	    }
		
		// Date
		{
			RecentChangesColumnDefiniton<Date> columnDef = new RecentChangesColumnDefiniton<Date>() {
				@Override
				public Date getCellValue(RecentChanges rowValue) {
					return rowValue.getModifiedDate();
				}
			
				@Override
				public void setCellValue(RecentChanges rowValue, Date cellValue) {
					rowValue.setModifiedDate(cellValue);
					
				}

			};
			columnDef.setCellRenderer(new CellRenderer<RecentChanges, Date>() {
				public void renderRowValue(RecentChanges rowValue, ColumnDefinition<RecentChanges, Date> columnDef,AbstractCellView<RecentChanges> view) {
					view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					view.setStyleName("gwt-ScrollTable-NoLink");
					view.setHTML(""+DateTimeFormat.getFormat("dd-MM-yyyy HH:mm:ss").format(rowValue.getModifiedDate()));
				}
			});
//			columnDef.setMinimumColumnWidth(130);
			columnDef.setPreferredColumnWidth(130);
//			columnDef.setMaximumColumnWidth(130);
			columnDef.setColumnSortable(true);
			tableDefinition.addColumnDefinition(columnDef);
	    }
	    
	    return tableDefinition;
	  }

	
	  private class RecentChangesTableModel extends MutableTableModel<RecentChanges> {
			  
		@SuppressWarnings("unchecked")
		public void requestRows(final Request request, final Callback<RecentChanges> callback) {
			
			// Send RPC request for data
			Service.conceptService.requestConceptHistoryRows(request, MainApp.userOntology.getOntologyId(),uri, type ,new AsyncCallback<ArrayList<RecentChanges>>() 
			{
		        public void onFailure(final Throwable caught) {		        	
		          callback.onFailure(new Exception("RPC Failure - while getting history row from " + request.getStartRow()));
		        }
		
		        public void onSuccess(final ArrayList<RecentChanges> rList) {
		    		final Response response = new SerializableResponse(rList);		    		
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
		protected boolean onSetRowValue(int row, RecentChanges rowValue) {
			return false;
		}
	}
	  
	public static Widget getTablePanel(int col, String link, ArrayList<LightEntity> list)
	{
		if(list.size()>0)
		{
			Object obj = list.get(0);
			if(obj instanceof Validation)
			{
				Validation v = (Validation) obj;
				return Validator.getLabelPanel(col, v, Style.Link);
			}
		}
		return new HTML("&nbsp;");
	}

	public String getActionFromID(int id, ArrayList<OwlAction> list)
	{
		String value = "";
		for(int i=0;i<list.size();i++){
			OwlAction os = (OwlAction)list.get(i);
			if(id == os.getId()) value = os.getAction();
		}
		return value;
	}

	public String getActionChildFromID(int id, ArrayList<OwlAction> list)
	{
		String value = "";
		for(int i=0;i<list.size();i++){
			OwlAction os = (OwlAction)list.get(i);
			if(id == os.getId()) value = os.getActionChild();
		}
		return value;
	}

	public String getActionDetailFromID(int id, ArrayList<OwlAction> list)
	{
		String action = getActionFromID(id, list);
		String actiondetail = getActionChildFromID(id, list);
		if(!actiondetail.equals(""))
			return action+" - "+actiondetail;
		else
			return action;
	}

	public String getUserNameFromID(int id, ArrayList<Users> list)
	{
		String value = "";
		for(int i=0;i<list.size();i++){
			Users u = (Users) list.get(i);
			if(id  == u.getUserId()) value = u.getUsername();
		}
		return value;
	}
}
