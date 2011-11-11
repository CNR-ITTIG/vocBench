package org.fao.aoscs.client.module.consistency.widgetlib;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.domain.Consistency;

import com.google.gwt.user.client.ui.Grid;

public class FilterGrid extends Grid{
	private HashMap<String, Consistency> rowValue = new HashMap<String, Consistency>();
	public FilterGrid(){
		super();
	}
	public FilterGrid(int row,int column){
		super(row,column);
	}
	public void setRowValue(int row, Consistency value){
		rowValue.put(Integer.toString(row), value);
	}
	public Consistency getRowValue(String row){
		Consistency value = new Consistency();
		if(rowValue.containsKey(row)){
			value = (Consistency) rowValue.get(row);
		}
		return value;
	}
	
	public void filter(boolean show, String value, String checkbox, ArrayList<String> selectedLanguage, ArrayList<String> selectedStatus, ArrayList<String> selectedDestStatus, ArrayList<String> selectedTermCodeProperty)
	{
		for(int i=1;i<FilterGrid.this.getRowCount();i++)
		{
			Consistency c = (Consistency) FilterGrid.this.getRowValue(Integer.toString(i));
			
			if(checkbox.equals("SHOWSTATUS") && c.getShowStatus()!=null)
			{
				c.setShowStatus(new Boolean(selectedStatus.contains(c.getStatus())));
			}
			else if(checkbox.equals("SHOWDESTSTATUS") && c.getShowDestStatus()!=null)
			{
				c.setShowDestStatus(new Boolean(selectedDestStatus.contains(c.getDestStatus())));
			}
			else if(checkbox.equals("SHOWTERMCODEPROPERTY") && c.getShowTermCodeProperty()!=null)
			{
				c.setShowTermCodeProperty(new Boolean(selectedTermCodeProperty.contains(c.getTermCodeProperty())));
			}
			else if(checkbox.equals("SHOWLANGUAGE") && c.getShowLanguage()!=null)
			{
				c.setShowLanguage(new Boolean(!c.getLanguages().containsAll(selectedLanguage)));
			}
			FilterGrid.this.getRowFormatter().setVisible(i, check(c));
		}
	}
	
	
	public boolean check(Consistency c)
	{
		boolean l = true;
		boolean s = true;
		boolean ds = true;
		boolean tc = true;
		boolean d = true;

		Boolean _l = c.getShowLanguage();
		Boolean _s = c.getShowStatus();
		Boolean _ds = c.getShowDestStatus();
		Boolean _tc = c.getShowTermCodeProperty();
		Boolean _d = c.getShowDate();
		
		if(_l!=null) l = _l.booleanValue();
		if(_s!=null) s = _s.booleanValue();
		if(_ds!=null) ds = _ds.booleanValue();
		if(_tc!=null) tc = _tc.booleanValue();
		if(_d!=null) d = _d.booleanValue();

		if(l && s && ds && tc && d)
			return true;
		else 
			return false;
	}
}
