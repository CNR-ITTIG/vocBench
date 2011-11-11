package org.fao.aoscs.client.widgetlib.shared.panel;

import org.fao.aoscs.client.MainApp;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class BodyPanel extends HorizontalPanel{
	
	public BodyPanel(String title, Widget bodyWidget, HorizontalPanel rightTopWidget, final int width, final int height){
		init(title, null, bodyWidget, rightTopWidget, width, height);
	}
	
	public BodyPanel(String title, Widget bodyWidget, HorizontalPanel rightTopWidget){
		init(title, null, bodyWidget, rightTopWidget, MainApp.getBodyPanelWidth(), MainApp.getBodyPanelHeight());
	}
	
	public BodyPanel(String title, HorizontalPanel leftTopWidget, Widget bodyWidget, HorizontalPanel rightTopWidget){
		init(title, leftTopWidget, bodyWidget, rightTopWidget, MainApp.getBodyPanelWidth(), MainApp.getBodyPanelHeight());
	}
	
	public void init(String title, HorizontalPanel leftTopWidget, Widget bodyWidget, HorizontalPanel rightTopWidget, final int width, final int height)
	{
		HTML titlebox = new HTML(title);
		titlebox.setWordWrap(false);
		titlebox.setStyleName("maintopbartitle");
		titlebox.setWidth("100%");
		

		final HorizontalPanel langBar = new HorizontalPanel();
		langBar.setStyleName("maintopbar");
		langBar.setWidth("100%");
		langBar.add(titlebox);
		langBar.setCellHorizontalAlignment(titlebox, HasHorizontalAlignment.ALIGN_LEFT);
		langBar.setCellVerticalAlignment(titlebox, HasVerticalAlignment.ALIGN_MIDDLE);
		
		langBar.add(new Spacer("15px", "100%"));
		
		if(leftTopWidget!=null)
		{
			leftTopWidget.setSpacing(5);
			VerticalPanel buttonPanel = new VerticalPanel();			
			buttonPanel.setWidth("100%");
			buttonPanel.add(new Spacer("50px", "100%"));
			buttonPanel.add(leftTopWidget);
			buttonPanel.setCellHorizontalAlignment(rightTopWidget,  HasHorizontalAlignment.ALIGN_LEFT);
			langBar.add(buttonPanel);
			langBar.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_LEFT);
			langBar.setCellVerticalAlignment(buttonPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		}
		
		Spacer spacer = new Spacer("100%", "100%");
		langBar.add(spacer);
		langBar.setCellWidth(spacer, "100%");
		
		if(rightTopWidget!=null)
		{
			rightTopWidget.setSpacing(3);
			VerticalPanel buttonPanel = new VerticalPanel();			
			buttonPanel.setWidth("100%");
			buttonPanel.add(rightTopWidget);
			buttonPanel.setCellHorizontalAlignment(rightTopWidget,  HasHorizontalAlignment.ALIGN_RIGHT);
			langBar.add(buttonPanel);
			langBar.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);
			langBar.setCellVerticalAlignment(buttonPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		}
		
		
		
		bodyWidget.setWidth("100%");
		
		final ScrollPanel sp = new ScrollPanel();
		sp.add(bodyWidget);	
		sp.setSize(width-20 +"px", height-50-langBar.getOffsetHeight() +"px");
	    Window.addResizeHandler(new ResizeHandler()
	    {
	    	public void onResize(ResizeEvent event) {
	    		sp.setSize(width-20 +"px", height-50-langBar.getOffsetHeight() +"px");
			}
	    });

		VerticalPanel bodyPanel = new VerticalPanel();
		bodyPanel.setSize("100%", "100%");
		bodyPanel.add(langBar);
		bodyPanel.add(sp);	
		bodyPanel.setStyleName("borderbar");
		
		final VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setSpacing(10);					  
		mainPanel.add(bodyPanel);
		mainPanel.setCellHorizontalAlignment(bodyPanel, HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.setCellVerticalAlignment(bodyPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		mainPanel.setCellWidth(bodyPanel, "100%");
		mainPanel.setCellHeight(bodyPanel, "100%");
		mainPanel.setSize("100%","100%");
		
		this.add(mainPanel);
	}
}

