package org.fao.aoscs.client.module.concept.widgetlib.dialog;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.widgetlib.shared.label.HelpPanel;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabel;
import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class DisclosureWidget extends AbsolutePanel{
	protected static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private SimplePanel headerContainer;
	private SimplePanel container;
	private LinkLabel link;
	private HelpPanel help; 
	private boolean isContainerHidden = true;
	private String headerMessage = "";
	private String helpMessage = "";
	
	private String styleContainerHide = "disclosure-widget-container-hide";
	
	private String styleContainerShow = "disclosure-widget-container-show";
	
	public DisclosureWidget(String headerMessage, String helpMessage) {
		super();
		this.headerMessage = headerMessage;
		this.helpMessage = helpMessage;
		createHeader();
		container = new SimplePanel();
		// hide container - without anim
		container.setStyleName("disclosure-widget-container");
		this.add(container);
		
	}
	
	public void createHeader(){
		HorizontalPanel header = new HorizontalPanel();
		header.setStyleName("disclosure-widget-header");
		link = new LinkLabel("images/popen.png", headerMessage, headerMessage);
		link.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				if(!isContainerHidden){
					hideContainer();
					link.setIcon("images/popen.png");
				}
				else{
					showContainer();
					link.setIcon("images/pclose.png");
				}
			}
		});
		
		header.add(new Spacer("2px","100%"));
		header.add(link);
		help = new HelpPanel(helpMessage);
		header.add(help);
		header.add(new Spacer("2px","100%"));
		header.setCellHorizontalAlignment(link, HasHorizontalAlignment.ALIGN_LEFT);
		header.setCellHorizontalAlignment(help, HasHorizontalAlignment.ALIGN_RIGHT);
		header.setCellVerticalAlignment(link, HasVerticalAlignment.ALIGN_MIDDLE);
		header.setCellVerticalAlignment(help, HasVerticalAlignment.ALIGN_MIDDLE);
		headerContainer = new SimplePanel();
		headerContainer.add(header);
		this.add(headerContainer);
	}
	
	public void setContent(Widget w){
		container.add(w);
	}

	public void showHeader(){
		headerContainer.setVisible(true);
	}
	
	public void hideHeader(){
		headerContainer.setVisible(false);
	}
	
	public void showHelp(){
		help.setVisible(true);
	}
	
	public void hideHelp(){
		help.setVisible(false);
	}
	
	public void showContainer(){
		container.setStyleName(styleContainerShow);
		isContainerHidden = false;
	}
	
	public void hideContainer(){
		container.setStyleName(styleContainerHide);
		isContainerHidden = true;
	}
	
	public void setStyleContainerShow(String styleContainerShow) {
		this.styleContainerShow = styleContainerShow;
	}
	
	public void setStyleContainerHide(String styleContainerHide) {
		this.styleContainerHide = styleContainerHide;
	}
	
	public void setStyleContainer(String styleContainerShow, String styleContainerHide){
		setStyleContainerShow(styleContainerShow);
		setStyleContainerHide(styleContainerHide);
	}
}
