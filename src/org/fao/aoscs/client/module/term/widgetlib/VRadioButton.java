package org.fao.aoscs.client.module.term.widgetlib;

import com.google.gwt.user.client.ui.RadioButton;

public class VRadioButton extends RadioButton{
	
	private Object object;
	
	public VRadioButton(String name,String label,Object object){
		super(name,label);
		this.object = object;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
}
