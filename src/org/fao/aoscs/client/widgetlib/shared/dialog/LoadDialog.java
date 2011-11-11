package org.fao.aoscs.client.widgetlib.shared.dialog;

import com.google.gwt.user.client.ui.PopupPanel;

public class LoadDialog extends PopupPanel {

	public LoadDialog() {
		super(false, true);
		setGlassEnabled(true);
		setWidget(new LoadingDialog());
		setWidth("200px");
		setStyleName("");
		center();
		hide();
	}
}
