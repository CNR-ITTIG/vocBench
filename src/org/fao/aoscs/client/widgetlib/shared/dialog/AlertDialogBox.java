package org.fao.aoscs.client.widgetlib.shared.dialog;

import org.fao.aoscs.client.locale.LocaleConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class AlertDialogBox extends FormDialogBox implements ClickHandler{
	
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	private ScrollPanel panel = new ScrollPanel();
	
	public AlertDialogBox(String dialogMsg, String AlertMsg){
		super(constants.buttonClose());
		this.setText(dialogMsg);
		panel.setWidth("400px");
		panel.add(getWarningWidget(AlertMsg));
		addWidget(panel);
	}
	
	public Widget getWarningWidget(String msg)
	{
		Grid table = new Grid(1,2);
		table.setWidget(0,0, getWarningImage());
		table.setWidget(0,1, getMessage(msg));
		return table;
	}
	
}
