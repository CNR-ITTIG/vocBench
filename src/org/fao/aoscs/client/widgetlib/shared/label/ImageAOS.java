package org.fao.aoscs.client.widgetlib.shared.label;

import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.module.constant.Style;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Image;
 
public class ImageAOS extends Image{
	
	private String text;
	private String image;
	private String disabledImage;
	private HandlerRegistration handlerRegistration = null;
	private ClickHandler handler;
	
	public ImageAOS(String text, String image, String disabledImage, boolean permission, ClickHandler handler) 
	{		
		super(image);
		this.text = text;
		this.image = image;
		this.disabledImage = disabledImage;
		this.handler = handler;
		if(!ConfigConstants.PERMISSIONCHECK)
		{
			setStyleName(Style.Link);
			setUrl(image);
			setTitle(text);
			DOM.setStyleAttribute(getElement(), "cursor", "pointer");
			if(handlerRegistration == null)
				handlerRegistration = addClickHandler(handler);
		}
		else{
			setEnable(permission);
		}	
		
	}
	
	public void setEnable(boolean permission){
		if(permission)
        {
			setStyleName(Style.Link);
			setUrl(image);
			setTitle(text);
			DOM.setStyleAttribute(getElement(), "cursor", "pointer");
			if(handlerRegistration == null)
				handlerRegistration = addClickHandler(this.handler);
        }
		else{
			if(ConfigConstants.PERMISSIONHIDE){
				setVisible(false);
			}else if(ConfigConstants.PERMISSIONDISABLE){
				setUrl(disabledImage);
				DOM.setStyleAttribute(getElement(), "cursor", "default");
				if(handlerRegistration != null){
					handlerRegistration.removeHandler();
					handlerRegistration = null;
				}
			}
		}
	}	
}

