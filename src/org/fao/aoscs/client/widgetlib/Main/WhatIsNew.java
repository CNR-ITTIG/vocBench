package org.fao.aoscs.client.widgetlib.Main;

import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class WhatIsNew extends VerticalPanel{
	
	private VerticalPanel itemContainer;

	public WhatIsNew() {
		super();
		
		HorizontalPanel whatIsNewTitle = new HorizontalPanel();
		whatIsNewTitle.setStyleName("whatIsNew-title");
		whatIsNewTitle.add(new HTML("What's new"));
		
		itemContainer = new VerticalPanel();		
		itemContainer.setStyleName("whatIsNew-content");		
			
		Spacer space = new Spacer("100%", "100%");
		space.setStyleName("whatIsNew-content");
		
		this.setSize("100%","100%");								
		this.add(whatIsNewTitle);
		this.add(itemContainer);
		this.add(space);
		this.setCellHeight(space, "100%");
		
		this.setCellHeight(whatIsNewTitle, "20px");
		this.setCellWidth(whatIsNewTitle, "100%");
		this.setCellWidth(itemContainer, "100%");
		
	}
	
	public void addNewItem(String sring){		
		itemContainer.add(new HTML("<ul><li>" + sring + "</li></ul>"));
	}
	
	public void addNewItem(HTML html){		
	    itemContainer.add(new HTML("<ul><li>" + html + "</li></ul>"));
	}
		
}
