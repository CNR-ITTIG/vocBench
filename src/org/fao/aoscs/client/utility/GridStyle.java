package org.fao.aoscs.client.utility;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


public class GridStyle {

	public static VerticalPanel setTableConceptDetailStyleTop(com.google.gwt.gen2.table.override.client.FlexTable table, String styleFirstRow, String styleFirstCol , String styleRest ,String style ,boolean wordwrap)
	{
		VerticalPanel panel = new VerticalPanel();
		
		table.setBorderWidth(0);
		table.setCellPadding(1);
		table.setCellSpacing(1);
		
		if(table.getRowCount()>0)
		{
			for (int i = 0; i < table.getCellCount(0); i++) 
			{
				table.getCellFormatter().setStyleName(0,i,styleFirstRow);		
				table.getCellFormatter().setWordWrap(0, i, wordwrap);
				table.getCellFormatter().setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_CENTER);			
				for (int j = 1; j < table.getRowCount(); j++) {
					table.getCellFormatter().setStyleName(j,0, styleFirstCol);
					table.getCellFormatter().setStyleName(j,i, styleRest);				
					table.getCellFormatter().setWordWrap(j, i, wordwrap);
				}
			}	
		}
		panel.add(table);
		table.setWidth("100%");
		panel.setWidth("100%");
		panel.setStyleName(style);
	
		return panel;
	}
	
	public static VerticalPanel setTableConceptDetailStyleTop(Grid table, String styleFirstRow, String styleFirstCol , String styleRest ,String style ,boolean wordwrap)
	{
		VerticalPanel panel = new VerticalPanel();
		
		table.setBorderWidth(0);
		table.setCellPadding(1);
		table.setCellSpacing(1);
		
		for (int i = 0; i < table.getColumnCount(); i++) 
		{
			table.getCellFormatter().setStyleName(0,i,styleFirstRow);		
			table.getCellFormatter().setWordWrap(0, i, wordwrap);
			table.getCellFormatter().setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_CENTER);			
			for (int j = 1; j < table.getRowCount(); j++) {
				table.getCellFormatter().setStyleName(j,0, styleFirstCol);
				table.getCellFormatter().setStyleName(j,i, styleRest);				
				table.getCellFormatter().setWordWrap(j, i, wordwrap);
			}
		}		
		panel.add(table);
		table.setWidth("100%");
		panel.setWidth("100%");
		panel.setStyleName(style);
		/*DOM.setStyleAttribute(panel.getElement(), "background", borderColor);*/		
		return panel;
	}

	public static ScrollPanel setTableConceptDetailStyleTopScrollPanel(Grid table,String color,String borderColor,boolean wordwrap)
	{
		ScrollPanel panel = new ScrollPanel();
		panel.clear();
		panel.setSize("100%", "100%");
		table.setBorderWidth(0);
		table.setCellPadding(1);
		table.setCellSpacing(1);
		
		for (int i = 0; i < table.getColumnCount(); i++) 
		{
			DOM.setStyleAttribute(table.getCellFormatter().getElement(0, i),"backgroundColor", color);
			table.getCellFormatter().setWordWrap(0, i, wordwrap);
			table.getCellFormatter().setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_CENTER);
			DOM.setStyleAttribute(table.getCellFormatter().getElement(0, i), "paddingLeft", "8px");
			for (int j = 1; j < table.getRowCount(); j++) {
				DOM.setStyleAttribute(table.getCellFormatter().getElement(j, i),"backgroundColor", "white");
				DOM.setStyleAttribute(table.getCellFormatter().getElement(j, i), "paddingLeft", "8px");
				table.getCellFormatter().setWordWrap(j, i, wordwrap);
			}
		}
		
		panel.add(table);
		table.setWidth("100%");
		panel.setHeight("100%");
		DOM.setStyleAttribute(panel.getElement(), "backgroundColor", borderColor);
		
		return panel;
	}
	public static VerticalPanel setTableConceptDetailStyleleft(Grid table, String styleFirstCol, String styleRest, String style )
	{
		VerticalPanel panel = new VerticalPanel();
		
		table.setBorderWidth(0);
		table.setCellPadding(1);
		table.setCellSpacing(1);
		
		for (int i = 0; i < table.getRowCount(); i++) 
		{
			table.getCellFormatter().addStyleName(i, 0, styleFirstCol);									
			table.getCellFormatter().setWordWrap(i, 0, false);
			for (int j = 1; j < table.getColumnCount(); j++) 
			{
				table.getCellFormatter().setStyleName(i, j, styleRest);				
				table.getCellFormatter().setWordWrap(i, j, false);
			}
		} 
		panel.add(table);
		panel.setWidth("100%");
		panel.setStyleName(style);
		return panel;
	}
	
	public static void updateTableConceptDetailStyleleft(Grid table, String styleFirstCol, String styleRest, String style )
	{
		table.setBorderWidth(0);
		table.setCellPadding(1);
		table.setCellSpacing(1);
		
		for (int i = 0; i < table.getRowCount(); i++) 
		{
			table.getCellFormatter().addStyleName(i, 0, styleFirstCol);									
			table.getCellFormatter().setWordWrap(i, 0, false);
			for (int j = 1; j < table.getColumnCount(); j++) 
			{
				table.getCellFormatter().setStyleName(i, j, styleRest);				
				table.getCellFormatter().setWordWrap(i, j, false);
			}
		} 
	}
	
	public static VerticalPanel setTableConceptDetailStyleleft(FlexTable table, String styleFirstCol, String styleRest, String style )
	{
		VerticalPanel panel = new VerticalPanel();
		
		table.setBorderWidth(0);
		table.setCellPadding(1);
		table.setCellSpacing(1);
		
		for (int i = 0; i < table.getRowCount(); i++) 
		{
			table.getCellFormatter().addStyleName(i, 0, styleFirstCol);									
			table.getCellFormatter().setWordWrap(i, 0, false);
			for (int j = 1; j < table.getCellCount(i); j++) 
			{
				table.getCellFormatter().setStyleName(i, j, styleRest);				
				table.getCellFormatter().setWordWrap(i, j, false);
			}
		} 
		panel.add(table);
		panel.setWidth("100%");
		panel.setStyleName(style);
		return panel;
	}
	public static void updateTableConceptDetailStyleleft(FlexTable table, String styleFirstCol, String styleRest, String style )
	{
		table.setBorderWidth(0);
		table.setCellPadding(1);
		table.setCellSpacing(1);
		
		for (int i = 0; i < table.getRowCount(); i++) 
		{
			table.getCellFormatter().addStyleName(i, 0, styleFirstCol);									
			table.getCellFormatter().setWordWrap(i, 0, false);
			for (int j = 1; j < table.getCellCount(i); j++) 
			{
				table.getCellFormatter().setStyleName(i, j, styleRest);				
				table.getCellFormatter().setWordWrap(i, j, false);
			}
		} 
	}
	public static VerticalPanel setTableConceptDetailStyleleft4Col(FlexTable table,String color, String alternateColor, String borderColor){
		VerticalPanel panel = new VerticalPanel();
		
		table.setBorderWidth(0);
		table.setCellPadding(1);
		table.setCellSpacing(1);
		
		for (int i = 0; i < table.getRowCount(); i++) {
			for (int j = 0; j < table.getCellCount(i); j++) {
				if((j%2)==0)
				{
					DOM.setStyleAttribute(table.getCellFormatter().getElement(i, j),"backgroundColor", color);
				}
				else
				{
					DOM.setStyleAttribute(table.getCellFormatter().getElement(i, j),"backgroundColor", alternateColor);
				}
				DOM.setStyleAttribute(table.getCellFormatter().getElement(i, j), "padding", "3px");
				table.getCellFormatter().setWordWrap(i, j, false);
			}
		}
		
		panel.add(table);
		panel.setWidth("100%");
		DOM.setStyleAttribute(panel.getElement(), "backgroundColor", borderColor);
		
		return panel;
	}
	public static VerticalPanel setTableRowStyle(FlexTable table,String color, String borderColor, int spacing)
	{
		table.setWidth("100%");
		table.setBorderWidth(0);
		table.setCellPadding(spacing);
		table.setCellSpacing(spacing);
		
		for (int i = 0; i < table.getRowCount(); i++) {
			for (int j = 0; j < table.getCellCount(i); j++) {
				DOM.setStyleAttribute(table.getCellFormatter().getElement(i, j),"backgroundColor", color);
				DOM.setStyleAttribute(table.getCellFormatter().getElement(i, j), "border", "1px solid "+borderColor);
				DOM.setStyleAttribute(table.getCellFormatter().getElement(i, j), "padding", "3px");
				table.getCellFormatter().setWordWrap(i, j, false);
			}
		}
		VerticalPanel panel = new VerticalPanel();
		panel.add(table);
		panel.setWidth("100%");
		return panel;
	}

	
}
