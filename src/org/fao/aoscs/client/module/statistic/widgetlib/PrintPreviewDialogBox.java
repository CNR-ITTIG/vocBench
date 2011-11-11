package org.fao.aoscs.client.module.statistic.widgetlib;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.utility.Print;
import org.fao.aoscs.client.widgetlib.shared.dialog.DialogBoxAOS;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PrintPreviewDialogBox extends DialogBoxAOS implements ClickHandler{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private VerticalPanel mainpanel = new VerticalPanel();
	private VerticalPanel panel = new VerticalPanel();
	private Button print = new Button(constants.buttonPrint());
	private Button cancel = new Button(constants.buttonCancel());
	private Widget printWidget = new Widget();
		
	public PrintPreviewDialogBox(){
		this.setText(constants.statPrintPreview());
	}

	public void setPrintWidget(Widget printWidget)
	{
		this.printWidget = printWidget;
	}
	
	public void show()
	{
		if(!isLoaded){
			super.show();			
			init();
		}
	}
	
	public void init()
	{
		ScrollPanel sc = new ScrollPanel();
		sc.add(printWidget);
		sc.setSize("800px", "450px");
		
		HTML html = new HTML("<p style='font-size:20; font-weight:bold' align=center>"+constants.statPrintPreviewTitle()+"</p>");
		panel.add(html);
		panel.add(sc);
		panel.setSpacing(10);
		panel.setCellHorizontalAlignment(panel, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(panel, HasVerticalAlignment.ALIGN_MIDDLE);
		mainpanel.add(panel);
		
		print.addClickHandler(this);
		cancel.addClickHandler(this);

		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(5);
		buttonPanel.add(print);
		buttonPanel.add(cancel);

		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(0);
		hp.setWidth("100%");
		hp.setStyleName("bottombar");
					
		hp.add(buttonPanel);			
		hp.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);
					
		mainpanel.add(hp);					
		mainpanel.setSpacing(0);
		setWidget(mainpanel);
	}
	
	public void onClick(ClickEvent event) {
		Widget sender = (Widget) event.getSource();
		if(sender.equals(print))
		{
			Print.it("<link rel='StyleSheet' type='text/css' href='aos.css'>", printWidget.getElement().getInnerHTML());	
			this.hide();
		}
		else if(sender.equals(cancel))
		{
			this.hide();	
		}
		
	}
	

	
}
