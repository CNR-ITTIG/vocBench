package org.fao.aoscs.client.widgetlib.shared.label;

import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

public class MenuBarAOS extends HorizontalPanel{

	MenuBar menuBar = new MenuBar();
	MenuBar menu = new MenuBar(true);
	
	public MenuBarAOS() {
		super();
											
		menuBar.setAutoOpen(false);
		menuBar.setAnimationEnabled(true);
		menu.setWidth("100%");		
		
		this.add(menuBar);
		this.setCellVerticalAlignment(menuBar, HasVerticalAlignment.ALIGN_MIDDLE);
		this.setCellHorizontalAlignment(menuBar, HasHorizontalAlignment.ALIGN_LEFT);				
	}	
	
	public void addMenuItem(String labelText, boolean asHTML, boolean isEnabled, Command cmd)
	{
		MenuItem menuItem = new MenuItem(Convert.replaceSpace(labelText), true, cmd);
		if(!isEnabled)
		{
			DOM.setStyleAttribute(menuItem.getElement(), "color", "#CCC");
			DOM.setStyleAttribute(menuItem.getElement(),"cursor", "default");	
			DOM.setStyleAttribute(menuItem.getElement(),"textShadow", "1px 1px 1px #000000");	
		}
		menu.addItem(menuItem);
	}
	
	public void addMenuItem(String labelText, boolean asHTML, Command cmd, String desc)
	{
	    String link = "<span alt='"+desc+"' title='"+desc+"' width='100%' height='100%'>"+Convert.replaceSpace(labelText)+"</span>";
	    MenuItem mItem = new MenuItem(link , true , cmd);
	    menu.addItem(mItem);	    
	}
	
	public void setMainLabel(String labelText){	
		MenuItem main = new MenuItem(getMainLabelHTML(Convert.replaceSpace(labelText)), true, menu);		
		menuBar.addItem(main);
		menuBar.setSize("100%", "100%");
	}
	
	public String getMainLabelHTML(String labelText)
	{
		HTML html = new HTML(Convert.replaceSpace(labelText));
		html.setWordWrap(false);
		Image drop = new Image("images/dropdown.png");
		HorizontalPanel label = new HorizontalPanel();		
		label.add(html);
		label.add(new Spacer("5px","100%"));		
		label.add(drop);
		label.setCellHorizontalAlignment(drop, HasHorizontalAlignment.ALIGN_RIGHT);
		label.setWidth("100%");
		
		for(int a=0 ; a < label.getWidgetCount(); a++){
			label.setCellVerticalAlignment(label.getWidget(a), HasVerticalAlignment.ALIGN_MIDDLE);
		}
		
		HorizontalPanel labelC = new HorizontalPanel();
		labelC.setWidth("100%");
		labelC.add(label);
		labelC.setCellWidth(label, "100%");
		
		return labelC.getElement().getInnerHTML();
	}
}
