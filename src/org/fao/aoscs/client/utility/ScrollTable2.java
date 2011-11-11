package org.fao.aoscs.client.utility;

import java.util.ArrayList;

import com.google.gwt.gen2.table.client.FixedWidthFlexTable;
import com.google.gwt.gen2.table.client.FixedWidthGrid;
import com.google.gwt.gen2.table.client.ScrollTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;

public class ScrollTable2 extends ScrollTable{
			
	public ScrollTable2() {		
		super(new FixedWidthGrid(), new FixedWidthFlexTable());		
		getDataTable().setCellSpacing(0);
		getDataTable().setCellPadding(0);
		getHeaderTable().setCellSpacing(0);
		getHeaderTable().setCellPadding(0);		
		getDataTable().resize(1, 1);				
	}
	
	public ScrollTable2(int rows , int columns) {		
		super(new FixedWidthGrid(), new FixedWidthFlexTable());		
		getDataTable().setCellSpacing(0);
		getDataTable().setCellPadding(0);
		getHeaderTable().setCellSpacing(0);
		getHeaderTable().setCellPadding(0);		
		getDataTable().resize(rows, columns);				
	}

	public void addHeader(ArrayList<String> headerTitle){
		getHeaderTable().setCellSpacing(0);
	    getHeaderTable().setCellPadding(0);
		com.google.gwt.gen2.table.override.client.FlexTable.FlexCellFormatter formatter = getHeaderTable().getFlexCellFormatter();
		for(int i = 0 ; i < headerTitle.size() ; i++){
			getHeaderTable().setHTML(0, i, headerTitle.get(i));
			formatter.setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_CENTER);
		}
	}
	
	public void addHeader(String title, int col){
		getHeaderTable().setHTML(0, col, title);
		getHeaderTable().getFlexCellFormatter().setHorizontalAlignment(0, col, HasHorizontalAlignment.ALIGN_CENTER);		
	}
	
	public void resizeDataTable(int row, int col){
		getDataTable().resize(row, col);
	}
	
	public void addDataRow(ArrayList<Widget> dataRow , int row){
		for(int i=0 ; i < dataRow.size() ; i++){
			getDataTable().setWidget(row, i , dataRow.get(i));
			getDataTable().getCellFormatter().addStyleName(row,i,"gwt-NoBorder");
		}
	}
	
	public void addDataRow(Widget data, int row , int col){
		getDataTable().setWidget(row, col , data);
		getDataTable().getCellFormatter().addStyleName(row,col,"gwt-NoBorder");		
	}
	
	public void setHeaderColumnFormatting(int column , int width , HorizontalAlignmentConstant align){		
		getHeaderTable().getFlexCellFormatter().setHorizontalAlignment(0, column, align);
		getHeaderTable().setColumnWidth( column, width);		
	}		
	
	public void setDataColumnFormatting(int column , int width , HorizontalAlignmentConstant align){
		for(int i=0 ; i< getDataTable().getRowCount(); i++)
			getDataTable().getCellFormatter().setHorizontalAlignment(i, column, align);		
		getDataTable().setColumnWidth( column, width);		
	}		
}
