package org.fao.aoscs.client.module.validation.widgetlib;

import java.util.ArrayList;

import org.fao.aoscs.client.module.constant.Style;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class FilterCheckBox extends Composite{
	private CheckBox cb = new CheckBox();
	private String label;
	private String value;	
	private HorizontalPanel panel = new HorizontalPanel();
	private ArrayList<FilterCheckBox> childList = new ArrayList<FilterCheckBox>();
	private FilterCheckBox parentCheckBox; 
	private ArrayList selectedList;
	private String checkbox;
		
	public FilterCheckBox(ArrayList arrayList, ArrayList<FilterCheckBox> list, final String label, final String value, final String checkbox)
	{
		this.label = label;
		this.value = value;
		this.childList = list;
		this.checkbox = checkbox;
		this.selectedList = arrayList;
		panel.add(cb);
		Label lab = new Label(label);
		if(checkbox.equals("SHOWUSER"))
		{
			lab = (Label)Validator.makeUsers(label,value,Style.Link);
		}
		panel.add(lab);
		initWidget(panel);
	}
	public boolean setCheck(boolean check)
	{
		cb.setValue(check);
		if(check)
		{
			if(!selectedList.contains(value))
				selectedList.add(value);
		}
		else
		{
			if(selectedList.contains(value))
				selectedList.remove(value);
		}
		if(!childList.isEmpty())
		{
			for(int i=0;i<childList.size();i++)
			{
				FilterCheckBox item = (FilterCheckBox) childList.get(i);
				item.setCheck(check);
				
			}
		}		
		// check siblings
		FilterCheckBox parent = getParentCheckBox();		
		boolean chkSe = check;
		if(parent!=null)
		{
			chkSe = true;			
			ArrayList<FilterCheckBox> siblingList = parent.childList;
			
			if(!check) 
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
