package org.fao.aoscs.client.widgetlib.shared.label;

import java.util.HashMap;

import com.google.gwt.user.client.ui.Label;

public class LabelAOS extends Label{
	private Object value = null;
	private HashMap<Object, Object> map = new HashMap<Object, Object>();
	public LabelAOS(){
		super();
	}
	public LabelAOS(String text){
		super(text);
	}
	public LabelAOS(String text,boolean b){
		super(text,b);
	}
	public LabelAOS(String text,Object value){
		super(text);
		this.value = value;
	}
	public LabelAOS(String text,Object value,HashMap<Object, Object> map){
		super(text);
		this.value = value;
		this.map = map;
	}
	public void setText(String text,Object value){
		this.setText(text);
		this.value = value;
	}
	public Object getValue(){
		return value;
	}
	public HashMap<Object, Object> getMapValue(){
		return map;
	}
}
