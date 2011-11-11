package org.fao.aoscs.client.widgetlib.shared.dialog;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.module.term.widgetlib.VRadioButton;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.label.LabelAOS;
import org.fao.aoscs.domain.ConceptTermObject;
import org.fao.aoscs.domain.LanguageCode;
import org.fao.aoscs.domain.TermObject;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.gen2.table.override.client.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

public class TermBrowser extends FormDialogBox implements ClickHandler {
	private TermObject termObject;
	private LabelAOS selectedTerm;
	private FlexTable table;
	LoadingDialog sayLoading = new LoadingDialog();

	public TermBrowser(TermObject termObject, LabelAOS selectedTerm) {
		super();
		this.termObject = termObject;
		this.selectedTerm = selectedTerm;
		this.setText(MainApp.constants.termBrowser());
		setWidth("400px");
		this.initLayout();
	}

	private Grid getTermTable(ArrayList<TermObject> list) {
	    
	    Grid table = new Grid(list.size(), 1);		    
		if (!list.isEmpty()) 
		{
			for (int i = 0; i < list.size(); i++) 
			{				    
			   TermObject tObj = (TermObject) list.get(i);
				VRadioButton cb = new VRadioButton("term", tObj.getLabel(),tObj);
				HorizontalPanel hp = new HorizontalPanel();
				hp.add(cb);
				if (tObj.isMainLabel()) 
				{
					hp.add(new HTML("&nbsp;(" + tObj.getStatus()+ "&nbsp;[preferred]&nbsp;" + ")&nbsp;"));
				} else 
				{
				    hp.add(new HTML("&nbsp;(" + tObj.getStatus()+ ")&nbsp;"));
				}
				hp.setCellVerticalAlignment(cb,
						HasVerticalAlignment.ALIGN_MIDDLE);
				hp.setCellVerticalAlignment(hp.getWidget(1),
						HasVerticalAlignment.ALIGN_MIDDLE);

				table.setWidget(i, 0, hp);
				if (tObj.getLabel().equals(termObject.getLabel()))
				{
					cb.setEnabled(false);
				}
			}
		}
		table.setWidth("100%");
		return table;
	}

	public void initLayout() {

		this.setWidth("400px");
		this.setHeight("500px");
		this.panel.add(sayLoading);
		AsyncCallback<Object> callback = new AsyncCallback<Object>() {
			public void onSuccess(Object result) {

				TermBrowser.this.panel.remove(sayLoading);
				ConceptTermObject ctObj = (ConceptTermObject) result;

				HashMap<String, ArrayList<TermObject>> termListByLang = ctObj.getTermList();
				//Iterator<String> it = termListByLang.keySet().iterator();
				
				ArrayList<String> termlanglist = new ArrayList<String>(termListByLang.keySet());
				ArrayList<String> sortedlanglist = new ArrayList<String>();
				for(LanguageCode langCode : MainApp.languageCode)
				{
					String lang = langCode.getLanguageCode().toLowerCase();
					
					if(termlanglist.contains(lang))
					{
						termlanglist.remove(lang);
						if(MainApp.userPreference.isHideNonselectedlanguages())
						{
							if(!MainApp.userSelectedLanguage.contains(lang))
							{
								lang = "";
							}
						}
						if(!lang.equals(""))
							sortedlanglist.add(lang);
					}
				}
				sortedlanglist.addAll(termlanglist);
				
				
				table = new FlexTable();
				table.setWidget(0, 0, new HTML(MainApp.constants.termLanguage()));
				table.setWidget(0, 1, new HTML(MainApp.constants.termTerm()));
				int i = 1;
				for(String language: sortedlanglist)
				{
					table.setWidget(i, 0, new HTML(MainApp.getFullnameofLanguage(language) + " (" + language.toString() + ")"));
					table.setWidget(i,1,getTermTable((ArrayList<TermObject>) termListByLang.get(language)));
					i++;
				}
				table.setWidth("100%");
				table.getColumnFormatter().setWidth(1, "80%");

				ScrollPanel sc = new ScrollPanel();
				sc.add(GridStyle.setTableConceptDetailStyleTop(table,"gstFR1","gstFC1","gstR1","gstPanel1",true));
				sc.setHeight("400px");
				sc.setWidth("100%");

				addWidget(sc);
			}

			public void onFailure(Throwable caught) {
				Window.alert(MainApp.constants.termGetTermFail());
			}
		};
		Service.termService.getConceptTermObject(termObject.getConceptName(), MainApp.userOntology, callback);
	}

	public TermObject getSelectedItem() {
		TermObject tObj = new TermObject();
		for (int i = 1; i < table.getRowCount(); i++) {
			Grid list = (Grid) table.getWidget(i, 1);
			for (int j = 0; j < list.getRowCount(); j++) {

				HorizontalPanel hp = (HorizontalPanel) list.getWidget(j, 0);
				VRadioButton cb = (VRadioButton) hp.getWidget(0);

				if (cb.getValue()) {
					tObj = (TermObject) cb.getObject();
				}
			}
		}
		return tObj;
	}

	public void onSubmit() {
		TermObject tObj = getSelectedItem();
		selectedTerm.setText(tObj.getLabel(), tObj);
	}
}
