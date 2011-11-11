package org.fao.aoscs.client.widgetlib.Main;

import org.fao.aoscs.client.Main;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * Generates browser compatibility warning message
 *
 */
public class BrowserCompatibilityInfo  extends HorizontalPanel{
	
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private Image warnIcon;
	private HTML warnText;
	
	private LinkLabel labelSafari;
	private LinkLabel labelChrome;
	private LinkLabel labelFirefox;
	
	public BrowserCompatibilityInfo() {
		super();
		
		// Add compatible browsers;
		labelSafari = new LinkLabel("images/browser-safari.png", "Safari", "Safari");
		labelChrome= new LinkLabel("images/browser-chrome.png", "Google Chrome", "Google Chrome");
		labelFirefox = new LinkLabel("images/browser-firefox.png", "Mozilla Firefox", "Mozilla Firefox");
		
		labelSafari.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent arg0) {
				Main.openURL("http://www.apple.com/safari/download/" , "_blank");
			}
		});
		
		labelChrome.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent arg0) {
				Main.openURL("http://www.google.com/chrome/intl/en/landing_chrome.html" , "_blank");
			}
		});
		
		labelFirefox.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent arg0) {
				Main.openURL("http://www.mozilla.com/en-US/firefox/" , "_blank");
			}
		});
	
		warnIcon = new Image("images/warning-small.png");
		warnText = new HTML(constants.mainBrowserWarn());
		HorizontalPanel browsers = addBrowsers();
		
		add(warnIcon);
		add(warnText);
		add(browsers);
		setCellHorizontalAlignment(warnIcon, HasHorizontalAlignment.ALIGN_CENTER);
		setCellHorizontalAlignment(warnText, HasHorizontalAlignment.ALIGN_CENTER);
		setCellHorizontalAlignment(browsers, HasHorizontalAlignment.ALIGN_CENTER);
		setCellVerticalAlignment(warnIcon, HasVerticalAlignment.ALIGN_MIDDLE);
		setCellVerticalAlignment(warnText, HasVerticalAlignment.ALIGN_MIDDLE);
		setCellVerticalAlignment(browsers, HasVerticalAlignment.ALIGN_MIDDLE);
		setSpacing(5);
		setStyleName("browserCompatibilityInfo-container");
		
	}
	
	private HorizontalPanel addBrowsers(){
		HorizontalPanel bs = new HorizontalPanel();
		bs.add(labelSafari);
		bs.add(new HTML("&nbsp;&nbsp;"));
		bs.add(labelChrome);
		bs.add(new HTML("&nbsp;&nbsp;"));
		bs.add(labelFirefox);		
		return bs;
	}

		
	
}
