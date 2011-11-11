package org.fao.aoscs.client.widgetlib.Main;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.ConfigConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Header extends VerticalPanel{
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);

	public Header() {
		super();
		HTML title = new HTML(constants.mainPageTitle());
		title.setStyleName("header-title");
		
		HTML version = new HTML(constants.mainVersionAllCaps()+ "&nbsp;" + ConfigConstants.DISPLAYVERSION);
		version.setStyleName("header-version");
		
		HorizontalPanel headerTitle = new HorizontalPanel();				 
		headerTitle.add(title);
		headerTitle.add(version);
		headerTitle.setCellVerticalAlignment(title , HasVerticalAlignment.ALIGN_MIDDLE);
		headerTitle.setCellVerticalAlignment(title , HasVerticalAlignment.ALIGN_TOP);
						 		 
		this.setStyleName("header-main");
		this.add(headerTitle);
	}
}
