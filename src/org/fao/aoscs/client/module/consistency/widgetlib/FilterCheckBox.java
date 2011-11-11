package org.fao.aoscs.client.module.consistency.widgetlib;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class FilterCheckBox extends Composite{
	private CheckBox cb = new CheckBox();
	private String label;
	private String value;	
	private HorizontalPanel panel = new HorizontalPanel();
	private ArrayList<FilterCheckBox> childList = new ArrayList<FilterCheckBox>();
	private FilterCheckBox parentCheckBox; 
	private String checkbox;
	
	public FilterCheckBox(String label, final String value, final FilterGrid table, final String checkbox, final ArrayList<String> selectedLanguage, final ArrayList<String> selectedStatus, final ArrayList<String> selectedDestStatus, final ArrayList<String> selectedTermCodeProperty){
		this.value = value;
		this.checkbox = checkbox;
		panel.add(cb);
		cb.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				FilterCheckBox.this.setCheck(cb.getValue(), table, selectedLanguage, selectedStatus, selectedDestStatus, selectedTermCodeProperty);
			}
		});
		panel.add(new HTML(label));
		initWidget(panel);
	}
	
	public boolean setCheck(boolean chk, final FilterGrid table, final ArrayList<String> selectedLanguage, final ArrayList<String> selectedStatus, final ArrayList<String> selectedDestStatus, final ArrayList<String> selectedTermCodeProperty)
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
		table.filter(chk, FilterCheckBox.this.getValue(), checkbox, selectedLanguage, selectedStatus, selectedDestStatus, selectedTermCodeProperty);		
		// check siblings
		FilterCheckBox parent = getParentCheckBox();		
		boolean chkSe = chk;
		if(parent!=null)
		{
			chkSe = true;			
			ArrayList<FilterCheckBox> siblingList = parent.childList;
			
			if(!chk) 
			{
				chkSe = false;
			}
			else
			{
				if(!siblingList.isEmpty())
				{
					for(int i=0;i<siblingList.size();i++)
					{
						FilterCheckBox item = (FilterCheckBox) siblingList.get(i);
						
						if(!item.isCheck())
						{
							chkSe = false;
							break;
						}
					}
				}
			}
			parent.getCb().setValue(chkSe);
		}
		return chkSe;		
	}
	public void addChildList(FilterCheckBox item){
		childList.add(item);
	}
	public boolean isCheck(){
		return cb.getValue();
	}
	public ArrayList<FilterCheckBox> getChildList(){
		return childList;
	}
	public void setChildList(ArrayList<FilterCheckBox> childList) {
		this.childList = childList;
	}
	public String getValue(){
		return value;
	}
	public void setValue(String value)
	{
		this.value=value;
	}
	public String getCheckbox() {
		return checkbox;
	}
	public void setCheckbox(String checkbox) {
		this.checkbox = checkbox;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public FilterCheckBox getParentCheckBox() {
		return parentCheckBox;
	}
	public void setParentCheckBox(FilterCheckBox parentCheckBox) {
		this.parentCheckBox = parentCheckBox;
	}
	public CheckBox getCb() {
		return cb;
	}
	public void setCb(CheckBox cb) {
		this.cb = cb;
	}
 
}
