package org.fao.aoscs.client.widgetlib.shared.panel;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ButtonbarWidget extends Composite {

	public ButtonbarWidget(Widget leftWidget, HorizontalPanel buttonGroup){
		init(leftWidget, buttonGroup, "100%");
	}
	
	public ButtonbarWidget(Widget leftWidget, HorizontalPanel buttonGroup, String width){
		init(leftWidget, buttonGroup, width);
	}
	
	public void init(Widget leftWidget, HorizontalPanel buttonGroup, String width){
		
		HorizontalPanel buttonBar = new HorizontalPanel();
		buttonBar.setStyleName("bottombarwidget");
		buttonBar.setSize(width, "100%");
		
		if(leftWidget!=null)
		{
			buttonBar.add(leftWidget);
			buttonBar.setCellHorizontalAlignment(leftWidget, HasHorizontalAlignment.ALIGN_LEFT);
			buttonBar.setCellVerticalAlignment(leftWidget, HasVerticalAlignment.ALIGN_MIDDLE);
			buttonBar.setCellWidth(leftWidget, "100%");
		}
		
		if(buttonGroup!=null)
		{
			buttonGroup.setSpacing(3);
			VerticalPanel buttonPanel = new VerticalPanel();			
			buttonPanel.setWidth("100%");
			buttonPanel.add(buttonGroup);
			buttonPanel.setCellHorizontalAlignment(buttonGroup,  HasHorizontalAlignment.ALIGN_RIGHT);
			buttonBar.add(buttonPanel);
			buttonBar.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);
			buttonBar.setCellVerticalAlignment(buttonPanel, HasVerticalAlignment.ALIGN_MIDDLE);
			if(leftWidget==null)
			{
				buttonBar.setCellWidth(buttonGroup, "100%");
			}
		}
		initWidget(buttonBar);
	}
}
