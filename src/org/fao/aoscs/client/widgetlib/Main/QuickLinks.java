package org.fao.aoscs.client.widgetlib.Main;

import org.fao.aoscs.client.AboutVocBenchMenu;
import org.fao.aoscs.client.Main;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.locale.LocaleMessages;
import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabel;
import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class QuickLinks extends HorizontalPanel {
	
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private static LocaleMessages messages = (LocaleMessages) GWT.create(LocaleMessages.class);
	
	HorizontalPanel left= new HorizontalPanel();
	HorizontalPanel right= new HorizontalPanel();
	
	HorizontalPanel loginDetail= new HorizontalPanel();
	HorizontalPanel ontology= new HorizontalPanel();
	HorizontalPanel home = new HorizontalPanel();
	HorizontalPanel statusBar= new HorizontalPanel();
	HorizontalPanel glossary= new HorizontalPanel();
	public HTML adminText;
	
	Spacer space= new Spacer("20px" , "100%");
	
	public QuickLinks(String styleName , boolean minimum)
	{		
		super();
		
		if(styleName != null)
			this.setStyleName(styleName);
		
		final LinkLabel homelabel = new LinkLabel("images/home.png", constants.menuHome(), constants.menuHome(), "quick-link");
		home.add(homelabel);
		
		this.add(left);
		this.add(right);
		this.setCellHorizontalAlignment(left, HasHorizontalAlignment.ALIGN_LEFT);
		this.setCellHorizontalAlignment(right, HasHorizontalAlignment.ALIGN_RIGHT);
		this.setCellVerticalAlignment(left, HasVerticalAlignment.ALIGN_MIDDLE);
		this.setCellVerticalAlignment(right, HasVerticalAlignment.ALIGN_MIDDLE);
		
		left.setHeight("100%");
		right.setHeight("100%");
		
		if(!minimum){
			left.add(new Spacer("20px", "100%"));
			left.add(loginDetail);
			left.add(ontology);		
			left.setCellHorizontalAlignment(loginDetail, HasHorizontalAlignment.ALIGN_CENTER);		
			left.setCellVerticalAlignment(loginDetail, HasVerticalAlignment.ALIGN_MIDDLE);
			left.setCellHeight(loginDetail, "100%");
			
			left.setCellHorizontalAlignment(ontology, HasHorizontalAlignment.ALIGN_CENTER);		
			left.setCellVerticalAlignment(ontology, HasVerticalAlignment.ALIGN_MIDDLE);
			left.setCellHeight(ontology, "100%");
			
			right.add(statusBar);
			right.add(new Spacer("20px", "100%"));
		}
		else{
			AboutVocBenchMenu about = new AboutVocBenchMenu();
			left.add(new Spacer("20px","100%"));
			left.add(home);
			left.add(new Spacer("10px","100%"));
			left.add(new Spacer("1px","100%", "|"));
			left.add(about);
			left.add(space);
			
			left.setCellHorizontalAlignment(home, HasHorizontalAlignment.ALIGN_CENTER);		
			left.setCellVerticalAlignment(home, HasVerticalAlignment.ALIGN_MIDDLE);
			left.setCellHorizontalAlignment(about, HasHorizontalAlignment.ALIGN_CENTER);		
			left.setCellVerticalAlignment(about, HasVerticalAlignment.ALIGN_MIDDLE);
			left.setCellHorizontalAlignment(home, HasHorizontalAlignment.ALIGN_CENTER);		
			left.setCellVerticalAlignment(home, HasVerticalAlignment.ALIGN_MIDDLE);
			left.setCellHeight(home, "100%");
			
			left.setCellWidth(space, "100%");
			
			homelabel.addClickHandler( new ClickHandler()
			{
				public void onClick(ClickEvent event) {
					Main.openURL(ConfigConstants.VOCBENCHINFO , "_blank");			
				}					
			});
		}
		this.setWidth("100%");	
		this.setHeight("100%");	
	}
	
	public void showLoginOptions(String username, String groupName)
	{
		HTML signIn = new HTML(messages.mainSignInAs(username, groupName));				
		signIn.setWordWrap(false);		
		loginDetail.add(signIn);
		loginDetail.setCellHorizontalAlignment(signIn,HasHorizontalAlignment.ALIGN_LEFT);
		loginDetail.setCellVerticalAlignment(signIn,HasVerticalAlignment.ALIGN_BOTTOM);
	}
	
	public void setOntologyPanel(Widget panel)
	{
		ontology.add(panel);	
	}
	
	public void setStatusBar(Widget panel)
	{
		statusBar.add(panel);	
		right.setCellVerticalAlignment(statusBar, HasVerticalAlignment.ALIGN_MIDDLE);
	}
}
