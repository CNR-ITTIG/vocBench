package org.fao.aoscs.client.widgetlib.shared.panel;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class TitleBodyWidget extends HorizontalPanel{
	VerticalPanel masterBox = new VerticalPanel();

	public TitleBodyWidget(String title , Widget bodyWidget , HorizontalPanel buttonGroup, String width, String height){
		init(title ,  bodyWidget ,  buttonGroup,  width,  height);
		this.add(masterBox);
	}
	
	public TitleBodyWidget(String title , Widget bodyWidget , HorizontalPanel buttonGroup, Widget bottomWidget, String width, String height){
		
		init(title ,  bodyWidget ,  buttonGroup,  width,  height);	
		
		if(bottomWidget !=null ) 
		{
			bottomWidget.setWidth(width);
			masterBox.add(bottomWidget);
			masterBox.setCellVerticalAlignment(bottomWidget, HasVerticalAlignment.ALIGN_TOP);
			masterBox.setCellWidth(bottomWidget, "100%");
		}
		this.add(masterBox);
	}
	
	public void init(String title , Widget bodyWidget , HorizontalPanel buttonGroup, String width, String height){
		HTML titlebox = new HTML(title);
		titlebox.setStyleName("titlebartext");
		titlebox.setWidth("100%");
		titlebox.setWordWrap(false);

		HorizontalPanel langBar = new HorizontalPanel();
		langBar.setStyleName("titlebar");
		langBar.setWidth("100%");
		langBar.add(titlebox);
		
		if(buttonGroup!=null)
		{
			buttonGroup.setSpacing(3);
			VerticalPanel buttonPanel = new VerticalPanel();			
			buttonPanel.setWidth("100%");
			buttonPanel.add(buttonGroup);
			buttonPanel.setCellHorizontalAlignment(buttonGroup,  HasHorizontalAlignment.ALIGN_RIGHT);
			langBar.add(buttonPanel);
			langBar.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);
			langBar.setCellVerticalAlignment(buttonPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		}
		langBar.setCellHorizontalAlignment(titlebox, HasHorizontalAlignment.ALIGN_LEFT);
		langBar.setCellVerticalAlignment(titlebox, HasVerticalAlignment.ALIGN_MIDDLE);
		langBar.setCellWidth(titlebox, "100%");
		
		bodyWidget.setWidth("100%");
		
		ScrollPanel sp = new ScrollPanel();
		sp.setWidth(width);
		sp.add(bodyWidget);	
		sp.setStyleName("borderbar");
		
		masterBox.setSize(width, height);
		if(!((title ==null || title.length()<1) && buttonGroup ==null)) masterBox.add(langBar);
		masterBox.add(sp);	
		masterBox.setCellWidth(langBar, "100%");
		masterBox.setCellWidth(sp, "100%");
		masterBox.setCellHeight(sp, "100%");
		masterBox.setCellVerticalAlignment(sp, HasVerticalAlignment.ALIGN_TOP);
	}

}

