package org.fao.aoscs.client.widgetlib.shared.label;


import org.fao.aoscs.client.module.constant.ConfigConstants;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;

public class LinkLabelAOS extends LinkLabel{
	
	String imageDisabled = "";
	String linkStyle = "link-label-aos";
	String linkStyleDisabled = "link-label-aos-disabled";
	
	public LinkLabelAOS(String image, String imageDisabled, boolean permission, ClickHandler handler) 
	{
		super(image);
		this.imageDisabled = imageDisabled;
		checkPermission(permission, handler);
	}

	public LinkLabelAOS(String image, String imageDisabled, String toolTip, String labelText, String linkStyle, boolean permission, ClickHandler handler) {
		super(image, toolTip, labelText, linkStyle);
		this.imageDisabled = imageDisabled;
		this.linkStyle = linkStyle;
		checkPermission(permission, handler);
	}

	public LinkLabelAOS(String image, String imageDisabled, String toolTip, String labelText, boolean permission, ClickHandler handler) {
		super(image, toolTip, labelText);
		this.imageDisabled = imageDisabled;
		checkPermission(permission, handler);
	}

	public LinkLabelAOS(String image, String imageDisabled, String toolTip, boolean permission, ClickHandler handler) {
		super(image, toolTip);
		this.imageDisabled = imageDisabled;
		checkPermission(permission, handler);
	}
	
	public void checkPermission(boolean permission, ClickHandler handler){
		if(ConfigConstants.PERMISSIONCHECK)
		{
			if(permission){
				addClickHandler(handler);
			}else{
				if(ConfigConstants.PERMISSIONHIDE){
					setVisible(false);
				}else if(ConfigConstants.PERMISSIONDISABLE){
					if(imageDisabled != null && imageDisabled.length() > 0){
						if(icon != null){
							icon.setUrl(this.imageDisabled);
							DOM.setStyleAttribute(icon.getElement(), "cursor", "default");
							icon.setTitle("");
						}
						DOM.setStyleAttribute(container.getElement(), "cursor", "default");
						this.setTitle("");
						this.setLabelStyle(linkStyleDisabled);
						this.removeMouseMoveActions();
					}
				}
			}
		}else{
			addClickHandler(handler);
		}
	}
	
}
