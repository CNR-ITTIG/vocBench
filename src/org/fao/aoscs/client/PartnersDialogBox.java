package org.fao.aoscs.client;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.widgetlib.Main.Partner;
import org.fao.aoscs.client.widgetlib.shared.dialog.DialogBoxAOS;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PartnersDialogBox  extends DialogBoxAOS implements ClickHandler{
		
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private VerticalPanel mainpanel = new VerticalPanel();
	private VerticalPanel panel = new VerticalPanel();
	private Button cancel = new Button(constants.buttonClose());
		
	public PartnersDialogBox(){
		
		this.setText(constants.footerPartners());
		panel.add(getPanel());
		panel.setSize("520px", "500px");
		
		HorizontalPanel bottomBar = new HorizontalPanel();
		bottomBar.setSpacing(0);
		bottomBar.setWidth("100%");
		bottomBar.setStyleName("bottombar");
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(5);			
		buttonPanel.add(cancel);
				
		bottomBar.add(buttonPanel);			
		bottomBar.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		
		mainpanel.add(panel);
		mainpanel.add(bottomBar);					
		cancel.addClickHandler(this);
		setWidget(mainpanel);
	}
	
	public void onClick(ClickEvent event) {
		this.hide();
	}
	
	public Widget getPanel()
	{
		Partner p = new Partner();
		VerticalPanel vp = new VerticalPanel();
		vp.setSize("100%", "100%");
		vp.setSpacing(10);
		vp.add(p);
		vp.setCellWidth(p, "100%");
		vp.setCellHeight(p, "100%");
		vp.setCellVerticalAlignment(p, HasVerticalAlignment.ALIGN_TOP);
		
		final ScrollPanel sc = new ScrollPanel();
		sc.setSize("520px", "500px");			
		sc.add(vp);
		return sc;
	}
}
