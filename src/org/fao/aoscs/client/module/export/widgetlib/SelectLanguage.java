package org.fao.aoscs.client.module.export.widgetlib;

import java.util.ArrayList;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.DialogBoxAOS;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.domain.LanguageCode;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SelectLanguage extends DialogBoxAOS implements ClickHandler{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private VerticalPanel panel = new VerticalPanel();
	private Button submit = new Button(constants.buttonSubmit());
	private Button close = new Button(constants.buttonCancel());
	private ArrayList<LangCheckBox> langCheckBoxContainer = new ArrayList<LangCheckBox>(); // LangCheckBox is element object of this container
	private RadioButton cbAll = new RadioButton("selectOption" , constants.buttonSelectAll());
	private RadioButton clearAll = new RadioButton("selectOption" , constants.buttonClearAll());
	private ArrayList<String> userSelectedLanguage = new ArrayList<String>();

	public SelectLanguage(){
		this.userSelectedLanguage = ExportOptionTable.userSelectedLanguage;
		this.setText(constants.exportLangFilter());		
		init(MainApp.languageCode);
		setWidget(panel);
	}
	
	public void init(ArrayList<LanguageCode> language)
	{
		panel.clear();
		submit.ensureDebugId("cwBasicButton-normal");
		close.ensureDebugId("cwBasicButton-normal");
	
		final Grid table = new Grid(language.size()+1, 2);
		table.setBorderWidth(1);
		table.setSize("100%", "100%");
		
		table.setWidget(0, 0, new HTML("<b>"+constants.exportLang()+"</b>"));
		table.setWidget(0, 1, new HTML("<b>"+constants.exportCode()+"</b>"));

		table.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		table.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
		
		int counter = 0;
		for(int i=0;i<language.size();i++){
			LanguageCode langaugeCode = (LanguageCode)language.get(i);
			LangCheckBox lcb = new LangCheckBox(langaugeCode);
			for(int j=0;j<userSelectedLanguage.size();j++){
				String code = (String) userSelectedLanguage.get(j);
				if(lcb.getValue().toLowerCase().equals(code.toLowerCase())){
					lcb.setCheck();
					counter++;
				}
			}
			langCheckBoxContainer.add(lcb);
			table.setWidget(i+1, 0, lcb);
			table.setWidget(i+1, 1, new HTML(langaugeCode.getLanguageCode().toLowerCase()));
			table.getCellFormatter().setHorizontalAlignment(i+1, 1, HasHorizontalAlignment.ALIGN_CENTER);
		}
	
		
		// Popup element
		
		Widget w = GridStyle.setTableConceptDetailStyleTopScrollPanel(table, "#e3e8f3", "#BBCDF3", false);
		w.setSize("300", "400");
		
		VerticalPanel vpw = new VerticalPanel();
		vpw.setSpacing(5);
		vpw.add(w);
		
		panel.add(vpw);
		
		clearAll.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				for(int i=0;i<langCheckBoxContainer.size();i++){
					LangCheckBox lcb = (LangCheckBox) langCheckBoxContainer.get(i);
					lcb.setUncheck();
				}				
			}			
		});

		if(counter==language.size()){
			cbAll.setValue(true);
		}
		
		
		cbAll.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(cbAll.getValue()){
					for(int i=0;i<langCheckBoxContainer.size();i++){
						LangCheckBox lcb = (LangCheckBox) langCheckBoxContainer.get(i);
						lcb.setCheck();
					}
				}else{
					for(int i=0;i<langCheckBoxContainer.size();i++){
						LangCheckBox lcb = (LangCheckBox) langCheckBoxContainer.get(i);
						lcb.setUncheck();
					}
				}
			}
		});
		
		HorizontalPanel allHp = new HorizontalPanel();
		allHp.add(cbAll);
		allHp.add(clearAll);
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(5);			
		buttonPanel.add(submit);
		buttonPanel.add(close);
		
		HorizontalPanel hp = new HorizontalPanel();		
		hp.setWidth("100%");
		hp.setStyleName("bottombar");
		
		hp.add(allHp);
		hp.add(buttonPanel);
		
		hp.setCellHorizontalAlignment(allHp, HasHorizontalAlignment.ALIGN_LEFT);
		hp.setCellVerticalAlignment(allHp, HasVerticalAlignment.ALIGN_MIDDLE);
		hp.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);
				
		submit.addClickHandler(this);		
		close.addClickHandler(this);				
		panel.add(hp);				
	}
	
	private class LangCheckBox extends Composite{
		private CheckBox cb = new CheckBox();
		private String value = "";	
		private HorizontalPanel panel = new HorizontalPanel();
		
		public LangCheckBox(LanguageCode languageCode){
			this.value = languageCode.getLanguageCode().toLowerCase();
			panel.add(cb);
			cb.setText(languageCode.getLocalLanguage().toLowerCase());
			cb.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(cb.getValue()){
						cbAll.setValue(true);
						for(int i=0;i<langCheckBoxContainer.size();i++){
							if(!((LangCheckBox)langCheckBoxContainer.get(i)).isCheck()){
								cbAll.setValue(false);
							}
						}
					}else{
						if(cbAll.getValue()){
							cbAll.setValue(false);
						}
					}
				}
			});
			initWidget(panel);
		}
		public void setCheck(){
			cb.setValue(true);
		}
		public void setUncheck(){
			cb.setValue(false);
		}
		public String getValue(){
			return value;
		}
		public boolean isCheck(){
			return cb.getValue();
		}
		
		
	}
	
	public void onClick(ClickEvent event) {
		Widget sender = (Widget) event.getSource();
		if(sender.equals(close)){
			// Clear history data
			if(langCheckBoxContainer.size()>0){
				for(int i=0;i<langCheckBoxContainer.size();i++ ){
					LangCheckBox lcb = (LangCheckBox) langCheckBoxContainer.get(i);
					lcb.setUncheck();
				}
			}
			cbAll.setValue(false);
			if(ExportOptionTable.userSelectedLanguage.size()>0){
				for(int i=0;i<ExportOptionTable.userSelectedLanguage.size();i++){
					String lang = (String) ExportOptionTable.userSelectedLanguage.get(i);
					for(int j=0;j<langCheckBoxContainer.size();j++ ){
						LangCheckBox lcb = (LangCheckBox) langCheckBoxContainer.get(j);
						if(lcb.getValue().equals(lang)){
							lcb.setCheck();
						}
					}
				}
			}
			this.hide();
		}else if(sender.equals(submit)){
			ExportOptionTable.userSelectedLanguage.clear();
			for(int i=0;i<langCheckBoxContainer.size();i++){
				LangCheckBox lcb = (LangCheckBox) langCheckBoxContainer.get(i);
				if(lcb.isCheck()){
					ExportOptionTable.userSelectedLanguage.add(lcb.getValue());
				}
			}
			this.hide();
		}
		
	}

	public void sayLoading(){
		clearPanel();
		LoadingDialog sayLoading = new LoadingDialog();
		panel.setSize("300", "400");
		panel.add(sayLoading);
		panel.setCellHorizontalAlignment(sayLoading, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(sayLoading, HasVerticalAlignment.ALIGN_MIDDLE);
	}
	
	public void clearPanel(){
		panel.clear();
		panel.setSize("100%","100%");
	}

}