package org.fao.aoscs.client.module.authoringtool;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ImportOntology extends Composite{
	private VerticalPanel vpanel = new VerticalPanel();

	public ImportOntology() {
		vpanel.add(new HTML("Import Ontology - Under Construction"));
		vpanel.setSize("100%","100%");
		vpanel.setSpacing(5);
		initWidget(vpanel);
	}
}

