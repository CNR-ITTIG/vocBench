package org.fao.aoscs.client.module.importdata;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ImportData extends Composite{
	private VerticalPanel panel = new VerticalPanel();
	public ImportData(){
		HTML msg = new HTML("<font size=\"5\" face=\"Verdana\">ImportData : Under Construction</font>");
		panel.add(msg);
		panel.setSize("100%", "100%");
		panel.setCellHorizontalAlignment(msg, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(msg, HasVerticalAlignment.ALIGN_MIDDLE);
		initWidget(panel);
	}
}
