package org.fao.aoscs.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MenuIcon extends Composite {
	private Image im ;
	private VerticalPanel menuPanel;
	private Label menuLabel;
		
	public MenuIcon(String label, String title, String image, final String module, final MainApp mainAPP)
	{
		menuPanel = new VerticalPanel();
		menuPanel.add(im = new Image());
		menuPanel.add(menuLabel = new Label(label));
		menuPanel.setCellHorizontalAlignment(im, HasHorizontalAlignment.ALIGN_CENTER);
		menuPanel.setCellHorizontalAlignment(menuLabel, HasHorizontalAlignment.ALIGN_CENTER);
		menuPanel.setCellVerticalAlignment(menuLabel, HasVerticalAlignment.ALIGN_BOTTOM);
		menuPanel.setStyleName("menuPanel");
		
		im.setStyleName("menuIcon");
		
		menuLabel.setStyleName("menuLabel");
		
		im.addMouseOverHandler(new MouseOverHandler(){
			public void onMouseOver(MouseOverEvent event) {
				Widget sender = (Widget) event.getSource(); 
				DOM.setStyleAttribute(sender.getParent().getParent().getElement(), "border", "1px ridge #92C1F0");
			}
		});
		im.addMouseOutHandler(new MouseOutHandler(){
			public void onMouseOut(MouseOutEvent event) {
				Widget sender = (Widget) event.getSource(); 
				DOM.setStyleAttribute(sender.getParent().getParent().getElement(), "border", "1px solid white");
			}
		});
		menuLabel.addMouseOverHandler(new MouseOverHandler(){
			public void onMouseOver(MouseOverEvent event) {
				Widget sender = (Widget) event.getSource(); 
				DOM.setStyleAttribute(sender.getParent().getParent().getElement(), "border", "1px ridge #92C1F0");
				
			}
		});
		menuLabel.addMouseOutHandler(new MouseOutHandler(){
			public void onMouseOut(MouseOutEvent event) {
				Widget sender = (Widget) event.getSource(); 
				DOM.setStyleAttribute(sender.getParent().getParent().getElement(), "border", "1px solid white");
			}
		});
		im.setUrl(image);
		im.setTitle(title);
		im.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				mainAPP.goToModule(module);
			}
		});
		
		initWidget(menuPanel);
		
	}
	
	 
	
}
