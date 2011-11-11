package org.fao.aoscs.client.widgetlib.shared.dialog;

import org.fao.aoscs.client.locale.LocaleConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoadingDialog extends Composite{
		
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	public LoadingDialog(){
		initWidget(getPanel(constants.ldLoading()));
	}
	
	public LoadingDialog(String message){
		initWidget(getPanel(message));
	}
	
	private VerticalPanel getPanel(String message)
	{
		HTML label = new HTML("&nbsp;"+message+"&nbsp;");
		label.setWordWrap(false);
	    
	    Image img = new Image("images/loading.gif");
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.addStyleName("loadingDialog");
	    hp.add(img);
	    hp.add(label);
	    hp.setSpacing(10);
	    hp.setCellHorizontalAlignment(img, HasHorizontalAlignment.ALIGN_RIGHT);
	    hp.setCellVerticalAlignment(img, HasVerticalAlignment.ALIGN_MIDDLE);
	    hp.setCellHorizontalAlignment(label, HasHorizontalAlignment.ALIGN_LEFT);
	    hp.setCellVerticalAlignment(label, HasVerticalAlignment.ALIGN_MIDDLE);
	    VerticalPanel panel = new VerticalPanel();
	    panel.add(hp);
	    panel.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(hp, HasVerticalAlignment.ALIGN_MIDDLE);
		panel.setSize("100%", "100%");
		
		return panel;
	}
}
