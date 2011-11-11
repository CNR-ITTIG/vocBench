package org.fao.aoscs.client.module.document.widgetlib;

import java.util.HashMap;

import org.fao.aoscs.domain.RecentChanges;

import com.google.gwt.user.client.ui.Grid;

public class FilterGrid extends Grid{
	private HashMap<String, RecentChanges> rowValue = new HashMap<String, RecentChanges>();
	public FilterGrid(){
		super();
	}
	public FilterGrid(int row,int column){
		super(row,column);
	}
	public void setRowValue(int row, RecentChanges value){
		rowValue.put(Integer.toString(row), value);
	}
	public RecentChanges getRowValue(String row){
		RecentChanges value = new RecentChanges();
		if(rowValue.containsKey(row)){
			value = (RecentChanges) rowValue.get(row);
		}
		return value;
	}
}
