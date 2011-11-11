package org.fao.aoscs.client.module.consistency.widgetlib;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;


public class FilterCheckBox2 extends Composite{
	private CheckBox cb = new CheckBox();
	private String value = "";	
	private String checkbox = "";

	private HorizontalPanel panel = new HorizontalPanel();
	public FilterCheckBox2(String label, final String value, final FilterGrid table, final String checkbox, final ArrayList<String> selectedLanguage, final ArrayList<String> selectedStatus, final ArrayList<String> selectedDestStatus, final ArrayList<String> selectedTermCodeProperty){
		this.value = value;
		this.checkbox = checkbox;
		panel.add(cb);
		cb.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				FilterCheckBox2.this.setCheck(cb.getValue(), table, selectedLanguage, selectedStatus, selectedDestStatus, selectedTermCodeProperty);
			}
		});
		panel.add(new HTML(label));
		initWidget(panel);
	}
	public void setCheck(boolean chk, final FilterGrid table, final ArrayList<String> selectedLanguage, final ArrayList<String> selectedStatus, final ArrayList<String> selectedDestStatus, final ArrayList<String> selectedTermCodeProperty)
	{
		cb.setValue(chk);
		if(checkbox.equals("SHOWSTATUS"))
		{
			if(chk)
			{
				if(!selectedStatus.contains(value))
				{
					selectedStatus.add(value);
				}
			}
			else
			{
				if(selectedStatus.contains(value))
				{
					selectedStatus.remove(value);
				}
			}
		}
		else if(checkbox.equals("SHOWDESTSTATUS"))
		{
			if(chk)
			{
				if(!selectedDestStatus.contains(value))
				{
					selectedDestStatus.add(value);
				}
			}
			else
			{
				if(selectedDestStatus.contains(value))
				{
					selectedDestStatus.remove(value);
				}
			}
		}
		else if(checkbox.equals("SHOWTERMCODEPROPERTY"))
		{
			if(chk)
			{
				if(!selectedTermCodeProperty.contains(value))
				{
					selectedTermCodeProperty.add(value);
				}
			}
			else
			{
				if(selectedTermCodeProperty.contains(value))
				{
					selectedTermCodeProperty.remove(value);
				}
			}
		}
		else if(checkbox.equals("SHOWLANGUAGE"))
		{
			if(chk)
			{
				if(!selectedLanguage.contains(value))
				{
					selectedLanguage.add(value);
				}
			}
			else
			{
				if(selectedLanguage.contains(value))
				{
					selectedLanguage.remove(value);
				}
			}
		}
		table.filter(chk, FilterCheckBox2.this.getValue(), checkbox, selectedLanguage, selectedStatus, selectedDestStatus, selectedTermCodeProperty);
		
	}
	
	public String getValue(){
		return value;
	}
	public boolean isCheck(){
		return cb.getValue();
	}
 
}
