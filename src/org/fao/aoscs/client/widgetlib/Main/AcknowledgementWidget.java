package org.fao.aoscs.client.widgetlib.Main;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.module.constant.Style;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AcknowledgementWidget extends HorizontalPanel{
		
	private Image img;
	
	public AcknowledgementWidget(String image, String imageTitle, String title ,  String subTitle, final String url) 
	{	
		if(image != null)
		{
			img = new Image(image);
			img.setStyleName(Style.Link);
			img.setTitle(imageTitle);
			if(url != null)
			{
				img.addClickHandler(new ClickHandler(){
					public void onClick(ClickEvent event) {
						MainApp.openURL(url);
					}
				});
			}
			this.add(img);
			this.add(new HTML("&nbsp;&nbsp;&nbsp;"));
		}
		HTML text = new HTML(title);
		text.setStyleName("faoname");
		HTML subText = new HTML(subTitle);
		subText.setStyleName("faologan");
		
		VerticalPanel texts = new VerticalPanel();
		texts.add(text);
		texts.add(subText);
				
		this.add(texts);
		this.setCellVerticalAlignment(texts, HasVerticalAlignment.ALIGN_TOP);
	}
	
	public void setImageSize(String width, String height){
		img.setSize(width, height);
	}
}

