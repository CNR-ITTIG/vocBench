package org.fao.aoscs.client.widgetlib.shared.label;

import com.google.gwt.user.client.ui.HTML;

public class HTMLAOS extends HTML{
	private Object value = null;
	public HTMLAOS(){
		super();
	}
	public HTMLAOS(String html){
		super(html);
	}
	public HTMLAOS(String html,boolean b){
		super(html,b);
	}
	public HTMLAOS(String html,Object value){
		super(html);
		this.value = value;
	}
	public Object getValue(){
		return value;
	}
	public void setHTML(String html,Object value){
		this.setHTML(html);
		this.value = value;
	}
}
