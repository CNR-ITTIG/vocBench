package org.fao.aoscs.client.module.classification.widgetlib;

import java.util.HashMap;

import com.google.gwt.user.client.ui.ListBox;

public class OlistBox extends ListBox{
	private HashMap<String, Object> list = new HashMap<String, Object>();
	
	public OlistBox(){
		super();
	}
	
	public OlistBox(boolean isMultipleSelect) {
	    super(isMultipleSelect);
	  }
	
	public void addItem(String item, Object obj) {
		super.addItem(item);				
		list.put(item , obj);
	}
	
	public Object getObject(int index){
		if(list.containsKey(getItemText(index))){
			return list.get(getItemText(index));
		}else{
			return null;
		}
	}
	
	public boolean hasItem(String item)
	{		
		if(list.containsKey(item)){			
			return true;
		}else{
			return false;
		}
	}
	
	public void removeItem(int index)
	{		
		String val = getItemText(index);
		list.remove(val);
		super.removeItem(index);
	}
	
}
