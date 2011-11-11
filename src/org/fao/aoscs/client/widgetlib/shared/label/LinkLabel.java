package org.fao.aoscs.client.widgetlib.shared.label;


import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

public class LinkLabel extends HorizontalPanel{

	public HorizontalPanel container;
	public HTML label;
	public Image icon; 
	public Spacer spacer = new Spacer("5px","100%");
	public boolean isClickable = true;
	
	public LinkLabel(String image) {
		createLink(image, null, null, null);
	}
	
	public LinkLabel(String image, String toolTip) {
		createLink(image, toolTip, null, null);
	}
	
	public LinkLabel(String image, String toolTip, String labelText){
		createLink(image, toolTip, labelText, null);
	}
	
	public LinkLabel(String image, String toolTip, String  labelText, String linkStyle){
		createLink(image, toolTip, labelText, linkStyle);
	}
	
	public void createLink(String image, String toolTip, String labelText, String linkStyle){
		container = new HorizontalPanel();
		createIcon(image);
		createLabel(labelText);		
		if(toolTip != null)	container.setTitle(toolTip);
		if(isClickable)
			DOM.setStyleAttribute(container.getElement(), "cursor", "pointer");
		else
			DOM.setStyleAttribute(container.getElement(), "cursor", "default");
		DOM.setStyleAttribute(container.getElement(), "margin", "1px");
		this.add(container);
		if(linkStyle != null)	this.setStyleName(linkStyle);
	}
	
	public void createIcon(String image){
		if(image != null && !image.equals("")){
			icon = new Image(image);
			container.add(icon);			
			container.setCellHorizontalAlignment(icon, HasHorizontalAlignment.ALIGN_CENTER);
			container.setCellVerticalAlignment(icon, HasVerticalAlignment.ALIGN_MIDDLE);
		}
	}
	
	public void createLabel(String labelText){
		if(labelText != null){
			label = new HTML(labelText);
			label.setWordWrap(false);
			if(icon != null)	container.add(spacer);
			container.add(label);
			container.setCellHorizontalAlignment(label, HasHorizontalAlignment.ALIGN_CENTER);
			container.setCellVerticalAlignment(label, HasVerticalAlignment.ALIGN_MIDDLE);			
		}
	}
	
	public void setLabelStyle(String style){
		if(label != null){
			label.setStyleName(style);
		}
	}
	
	public void setLabelText(String text){
		if(label == null)
			createLabel(text);
		else
			label.setText(text);				
	}
	
	public void setToolTipText(String text){
		container.setTitle(text);
	}
	
	public void setIcon(String imageUrl){
		if(icon == null)
			createIcon(imageUrl);
		else
			icon.setUrl(imageUrl);		
		
	}
	
	public void setClickable(boolean isClickable) {
		this.isClickable = isClickable;
		if(isClickable)
			DOM.setStyleAttribute(container.getElement(), "cursor", "pointer");
		else
			DOM.setStyleAttribute(container.getElement(), "cursor", "default");		
	}
	
	public void addClickHandler(ClickHandler handler){
		if(icon != null)
			icon.addClickHandler(handler);
		if(label != null)
			label.addClickHandler(handler);
	}
	
	
	public void removeMouseMoveActions(){
		if(label!=null){
			label.addMouseOverHandler(new MouseOverHandler(){
				public void onMouseOver(MouseOverEvent event) {
					DOM.setStyleAttribute(label.getElement(), "textDecoration", "none");
				}
			});
			label.addMouseOutHandler(new MouseOutHandler(){
				public void onMouseOut(MouseOutEvent event) {
					DOM.setStyleAttribute(label.getElement(), "textDecoration", "none");	
				}
			});
		}
		if(icon!=null)
		{
			icon.addMouseOverHandler(new MouseOverHandler(){			
				public void onMouseOver(MouseOverEvent event) {
					DOM.setStyleAttribute(label.getElement(), "textDecoration", "none");
				}
			});
			icon.addMouseOutHandler(new MouseOutHandler(){
				public void onMouseOut(MouseOutEvent event) {
					DOM.setStyleAttribute(label.getElement(), "textDecoration", "none");	
				}
			});
		}
	}
}
