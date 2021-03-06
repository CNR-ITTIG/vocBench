package org.fao.aoscs.client.module.document.widgetlib;

import java.util.ArrayList;
import java.util.Date;

import net.sf.gilead.pojo.gwt.LightEntity;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.module.validation.widgetlib.Validator;
import org.fao.aoscs.domain.LabelObject;
import org.fao.aoscs.domain.OwlAction;
import org.fao.aoscs.domain.RecentChangeData;
import org.fao.aoscs.domain.RecentChanges;
import org.fao.aoscs.domain.RelationshipObject;
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
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;


public class RecentChangesTable {

	public LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	public static final int NO_TYPE = -1;
	public static final int RELATIONSHIP_TYPE = 0;
	public static final int USER_TYPE = 1;
	public static final int EXPORT_TYPE = 2;
	public static final int VALIDATION_TYPE = 3;
	public static final int GROUP_TYPE = 4;

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
	public CachedTableModel<RecentChanges> getCachedTableModel() 
	{
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
	public DefaultTableDefinition<RecentChanges> getTableDefinition() 
	{
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
	protected FixedWidthFlexTable createHeaderTable() 
	{
		FixedWidthFlexTable headerTable = new FixedWidthFlexTable();

		// Level 1 headers
		FlexCellFormatter formatter = headerTable.getFlexCellFormatter();
		headerTable.setHTML(0, 0, constants.homeConceptTermRelationshipScheme());
		formatter.setHorizontalAlignment(0, 0,HasHorizontalAlignment.ALIGN_CENTER);
		headerTable.setHTML(0, 1, constants.homeChange());
		formatter.setHorizontalAlignment(0, 1,HasHorizontalAlignment.ALIGN_CENTER);
		headerTable.setHTML(0, 2, constants.homeOldValue());
		formatter.setHorizontalAlignment(0, 2,HasHorizontalAlignment.ALIGN_CENTER);
		headerTable.setHTML(0, 3, constants.homeAction());
		formatter.setHorizontalAlignment(0, 3,HasHorizontalAlignment.ALIGN_CENTER);
		headerTable.setHTML(0, 4, constants.homeUser());
		formatter.setHorizontalAlignment(0, 4,HasHorizontalAlignment.ALIGN_CENTER);
		headerTable.setHTML(0, 5, constants.homeDate());
		formatter.setHorizontalAlignment(0, 5,HasHorizontalAlignment.ALIGN_CENTER);

		return headerTable;
	}

	/**
	 * @return the newly created data table.
	 */
	protected FixedWidthGrid createDataTable() {
		FixedWidthGrid dataTable = new FixedWidthGrid();
		return dataTable;
	}

	protected AbstractScrollTable createScrollTable
	(

			FixedWidthFlexTable headerTable, FixedWidthGrid dataTable, int size) {
		// Setup the controller
		tableModel = new RecentChangesTableModel();

		cachedTableModel = new CachedTableModel<RecentChanges>(tableModel);
		cachedTableModel.setPreCachedRowCount(15);
		cachedTableModel.setPostCachedRowCount(15);
		cachedTableModel.setRowCount(size);

		// Create a TableCellRenderer
		TableDefinition<RecentChanges> tableDef = createTableDefinition();

		// Create the scroll table
		pagingScrollTable = new PagingScrollTable<RecentChanges>(cachedTableModel, dataTable, headerTable, tableDef);
		pagingScrollTable.setPageSize(15);
		pagingScrollTable.setEmptyTableWidget(new HTML(constants.homeNoData()));

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

	public void setTable(ArrayList<Users> userList, ArrayList<OwlAction> actionList, int size) 
	{

	    
		this.userList = userList;
		this.actionList = actionList;

		// Initialize the tables
		FixedWidthFlexTable headerTable = createHeaderTable();
		FixedWidthGrid dataTable = createDataTable();
		scrollTable = createScrollTable(headerTable, dataTable, size);
		scrollTable.setWidth(MainApp.getBodyPanelWidth() - 42 + "px");
		scrollTable.setHeight(MainApp.getBodyPanelHeight() - 105 +"px");
		
		// Add the main layout to the page
		layout.setWidth("100%");
		layout.setCellPadding(0);
		layout.setCellSpacing(0);
		layout.setWidget(0, 0, scrollTable); // Add the scroll table to the layout
		
		Window.addResizeHandler(new ResizeHandler()
	    {
	    	public void onResize(ResizeEvent event) 
	    	{
				scrollTable.setWidth(MainApp.getBodyPanelWidth() - 42 + "px");
				scrollTable.setHeight(MainApp.getBodyPanelHeight() - 105 +"px");
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

	private abstract class RecentChangesColumnDefiniton<ColType> extends AbstractColumnDefinition<RecentChanges, ColType> 
	{

		/**
		 * Construct a new {@link RecentChangesColumnDefiniton}.
		 * 
		 */
		public RecentChangesColumnDefiniton() {}

	}

	/**
	 * @return the {@link TableDefinition} with all ColumnDefinitions defined.
	 */
	private TableDefinition<RecentChanges> createTableDefinition() 
	{
		// Create the table definition
		tableDefinition = new DefaultTableDefinition<RecentChanges>();

		// concept/term/relationship
		{
			RecentChangesColumnDefiniton<ArrayList<LightEntity>> columnDef = new RecentChangesColumnDefiniton<ArrayList<LightEntity>>() 
			{
				@Override
				public ArrayList<LightEntity> getCellValue(RecentChanges rowValue) 
				{
					return rowValue.getModifiedObject();
				}			
				@Override
				public void setCellValue(RecentChanges rowValue, ArrayList<LightEntity> cellValue) 
				{
					rowValue.setModifiedObject(cellValue);

				}
			};
			columnDef.setCellRenderer(new CellRenderer<RecentChanges, ArrayList<LightEntity>>() 
					{
				public void renderRowValue(RecentChanges rowValue, ColumnDefinition<RecentChanges, ArrayList<LightEntity>> columnDef,AbstractCellView<RecentChanges> view) 
				{
					view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_DEFAULT);
					view.setStyleName(Style.Link);
					Widget w0 = getTablePanel(0, Style.Link, (ArrayList<LightEntity>) rowValue.getModifiedObject());
					w0.addStyleName("gwt-NoBorder");
					view.setWidget(w0);
				}
					});

			columnDef.setPreferredColumnWidth(300);
			columnDef.setColumnSortable(false);
			tableDefinition.addColumnDefinition(columnDef);
		}    
		// newvalue
		{
			RecentChangesColumnDefiniton<ArrayList<LightEntity>> columnDef = new RecentChangesColumnDefiniton<ArrayList<LightEntity>>() 
			{
				@Override
				public ArrayList<LightEntity> getCellValue(RecentChanges rowValue) {
					return rowValue.getModifiedObject();
				}

				@Override
				public void setCellValue(RecentChanges rowValue, ArrayList<LightEntity> cellValue) {
					rowValue.setModifiedObject((ArrayList<LightEntity>)cellValue);

				}
			};
			columnDef.setCellRenderer(new CellRenderer<RecentChanges, ArrayList<LightEntity>>() 
					{
				public void renderRowValue(RecentChanges rowValue, ColumnDefinition<RecentChanges, ArrayList<LightEntity>> columnDef,AbstractCellView<RecentChanges> view) 
				{
					view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_DEFAULT);
					view.setStyleName(Style.Link);
					Widget w0 = getTablePanel(1, Style.Link, (ArrayList<LightEntity>) rowValue.getModifiedObject());
					w0.addStyleName("gwt-NoBorder");
					view.setWidget(w0);
				}
					});
			columnDef.setPreferredColumnWidth(200);			
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
			columnDef.setPreferredColumnWidth(200);
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
			columnDef.setCellRenderer(new CellRenderer<RecentChanges, Integer>() 
					{
				public void renderRowValue(RecentChanges rowValue, ColumnDefinition<RecentChanges, Integer> columnDef,AbstractCellView<RecentChanges> view) {
					view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					view.setStyleName("gwt-ScrollTable-NoLink");
					if(rowValue.getModifiedActionId() == 72 || rowValue.getModifiedActionId() == 73){
						Object obj =  rowValue.getModifiedObject();
						if(obj!=null)
						{
							if(obj instanceof ArrayList<?>)
							{
								ArrayList<LightEntity> list = (ArrayList<LightEntity>) rowValue.getModifiedObject();
								if(list.size()>0)
								{
									Validation v = (Validation)(list).get(0);
									view.setHTML(Validator.getActionFromID(rowValue.getModifiedActionId(), actionList) + " - " + Validator.getActionFromID(v.getAction(),actionList));
								}
								else
									view.setHTML("&nbsp;");
							}
						}
					}else{
						view.setHTML(Validator.getActionFromID(rowValue.getModifiedActionId(), actionList));						
					}
				}
					});
			columnDef.setPreferredColumnWidth(150);
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
			columnDef.setPreferredColumnWidth(150);
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
			columnDef.setPreferredColumnWidth(130);
			columnDef.setColumnSortable(true);
			tableDefinition.addColumnDefinition(columnDef);
		}

		return tableDefinition;
	}


	private class RecentChangesTableModel extends MutableTableModel<RecentChanges> 
	{

		
		@SuppressWarnings("unchecked")
		public void requestRows(final Request request, final Callback<RecentChanges> callback) 
		{
			// Send RPC request for data
			Service.validationService.requestRecentChangesRows(request, MainApp.userOntology.getOntologyId(), new AsyncCallback<ArrayList<RecentChanges>>() 
					{
				public void onFailure(final Throwable caught) {
					callback.onFailure(new Exception(constants.homeListError()));
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
			else if(obj instanceof RecentChangeData)
			{
				RecentChangeData rcData = (RecentChangeData) obj;
				return LabelFactory.makeLabel(rcData, col==0? LabelFactory.ITEMLABEL : col==1? LabelFactory.ITEMCHANGE : LabelFactory.ITEMOLD);				
			}
		}
		return new HTML("&nbsp;");
	}

	public static String makeRelationshipLabel(RelationshipObject rObj)
	{
		ArrayList<LabelObject> labelList = rObj.getLabelList();
		String labelStr = "";
		for(int i=0;i<labelList.size();i++)
		{
			LabelObject labelObj = (LabelObject) labelList.get(i);
			String lang = (String) labelObj.getLanguage();
			if(MainApp.userSelectedLanguage.contains(lang))
			{
				String label = (String) labelObj.getLabel();
				if(labelStr.equals(""))
					labelStr += " "+label+" ("+lang+")";
				else
					labelStr += ", "+label+" ("+lang+")";
			}
		}
		return labelStr;
	}

	public static HorizontalPanel makeLabel(int type, String text)
	{
		HorizontalPanel panel = new HorizontalPanel();
		panel.add(getImagePanel(type));
		panel.add(getLabelPanel(type, text));

		if(panel.getWidgetCount()<1)
			panel.add(new HTML("&nbsp;"));
		return panel;
	}

	public static Widget getImagePanel(final int type)
	{
		String imgURL = "images/spacer.gif";
		if(type==RecentChangesTable.RELATIONSHIP_TYPE)
		{
			imgURL = "images/relationship-object-logo.gif";
		}
		else if(type==RecentChangesTable.USER_TYPE)
		{
			imgURL = "images/New-users.gif";
		}

		Image image = new Image(imgURL);

		return image;
	}

	public static Widget getLabelPanel(final int type, String text)
	{
		
		LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
		
		if(!text.equals(""))
		{
			HTML label = new HTML(" "+text);
			return label;
		}
		else
		{
			Image image = new Image("images/label-not-found.gif");
			image.setTitle(constants.homeNoTerm());
			return image;
		}
	}

	public String getActionFromID(int id, ArrayList<OwlAction> list)
	{
		String value = "";
		for(int i=0;i<list.size();i++)
		{
			OwlAction os = (OwlAction)list.get(i);
			if(id == os.getId()) value = os.getAction();
		}
		return value;
	}

	public String getActionChildFromID(int id, ArrayList<OwlAction> list)
	{
		String value = "";
		for(int i=0;i<list.size();i++)
		{
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
