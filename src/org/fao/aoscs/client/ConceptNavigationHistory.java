package org.fao.aoscs.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.concept.widgetlib.InfoTab;
import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.utility.TimeConverter;
import org.fao.aoscs.client.widgetlib.shared.dialog.DialogBoxAOS;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.tree.TreeItemAOS;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.TermObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ConceptNavigationHistory extends DialogBoxAOS implements ClickHandler{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private VerticalPanel panel = new VerticalPanel();
	private VerticalPanel vp = new VerticalPanel();
	private Button close = new Button(constants.buttonClose());
	
			
	public ConceptNavigationHistory(HashMap<Long, ConceptObject> conceptNavigationHistoryList){
		this.setText(constants.conceptNavigationHistory());		
		init(conceptNavigationHistoryList);
		setWidget(panel);
	}
	
	public void init(final HashMap<Long, ConceptObject> conceptNavigationHistoryList)
	{
		panel.clear();
		vp.clear();
		close.ensureDebugId("cwBasicButton-normal");
		
		VerticalPanel listPanel = new VerticalPanel();
		
		List<Long> labelKeys = new ArrayList<Long>(conceptNavigationHistoryList.keySet()); 
		Collections.sort(labelKeys);
		Collections.reverse(labelKeys);
		int i=labelKeys.size();
		for(long timeStamp: labelKeys)
		{
			
			
			final VerticalPanel conceptPanel = new VerticalPanel();
			conceptPanel.setSize("100%", "100%");
			final ConceptObject cObj = conceptNavigationHistoryList.get(timeStamp);
			
			String dateTime = TimeConverter.formatDate(new Date(timeStamp), "yyyy-MM-dd HH:mm:ss");
			Image img = new Image("images/clock.png");
			HTML dateLabel = new HTML("<b>"+dateTime+"</b>");
			HorizontalPanel hp = new HorizontalPanel();
			hp.setSize("100%", "20px");
			hp.setStyleName("navhistorytopbar");
			hp.add(img);
			hp.add(dateLabel);
			hp.setCellWidth(dateLabel, "100%");
			hp.setCellHorizontalAlignment(img, HasHorizontalAlignment.ALIGN_LEFT);
			hp.setCellHorizontalAlignment(dateLabel, HasHorizontalAlignment.ALIGN_LEFT);
			hp.setCellVerticalAlignment(img, HasVerticalAlignment.ALIGN_MIDDLE);
			hp.setCellVerticalAlignment(dateLabel, HasVerticalAlignment.ALIGN_MIDDLE);
			conceptPanel.add(hp);
			conceptPanel.add(convert2Widget(cObj, makeLabel(cObj, true, MainApp.userSelectedLanguage)));
			conceptPanel.setStyleName("borderbar");
			listPanel.add(conceptPanel);
			i--;
			
		}
		listPanel.setSpacing(10);
		listPanel.setWidth("100%");
		if(listPanel.getWidgetCount()<1)
		{
			Label noConcepts = new Label(constants.conceptNoNavigationHistory());
			listPanel.add(noConcepts);
			listPanel.setCellHorizontalAlignment(noConcepts, HasHorizontalAlignment.ALIGN_CENTER);
			listPanel.setCellVerticalAlignment(noConcepts, HasVerticalAlignment.ALIGN_MIDDLE);
		}
		else
		{
			listPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
			listPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		}
		
		ScrollPanel sc = new ScrollPanel(listPanel);
		sc.setWidth("700px");
		sc.setHeight("400px");
		
		vp.add(sc);

		HorizontalPanel allHp = new HorizontalPanel();
		allHp.setWidth("100%");
		DOM.setStyleAttribute(allHp.getElement(), "background", "url('images/bg_headergradient.png')");
		DOM.setStyleAttribute(allHp.getElement(), "border", "1px solid #BBCDF3");


		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(5);			
		buttonPanel.add(close);
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(0);
		hp.setWidth("100%");
		hp.setStyleName("bottombar");
		
		hp.add(buttonPanel);
		
		hp.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		
		close.addClickHandler(this);
				
		vp.setSpacing(0);
		
		panel.add(vp);
		panel.add(hp);	
		panel.setWidth("100%");
		
	}
	
	public void onClick(ClickEvent event) {
		Widget sender = (Widget) event.getSource();
		if(sender.equals(close)){
			this.hide();
		}
	}
	
	public void sayLoading(){
		clearPanel();
		LoadingDialog sayLoading = new LoadingDialog();
		panel.setSize("300px", "400px");
		panel.add(sayLoading);
		panel.setCellHorizontalAlignment(sayLoading, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(sayLoading, HasVerticalAlignment.ALIGN_MIDDLE);
	}
	
	public void clearPanel(){
		panel.clear();
		panel.setSize("300px", "400px");
	}
	
	private String makeLabel(ConceptObject cObj, boolean showAlsoNonpreferredTerms, ArrayList<String> langList)
	{
		Collection<TermObject>  tObjList = cObj.getTerm().values();
		ArrayList<String> sortedList = new ArrayList<String>();
		HashMap<String, Boolean> checkMainLabelList = new HashMap<String, Boolean>();
		
		for (TermObject termInstance: tObjList) 
		{
			boolean isMainLabel = termInstance.isMainLabel();
			String label = termInstance.getLabel();
			String lang = termInstance.getLang().toLowerCase();
			if(!showAlsoNonpreferredTerms){
				if(isMainLabel && langList.contains(lang) ){
					sortedList.add(lang+"###"+label);
				}			
			}else{
				if(langList.contains(lang)){
					sortedList.add(lang+"###"+label);
					checkMainLabelList.put(lang+"###"+label, isMainLabel);
				}			
			}
	  
		}
		Collections.sort(sortedList, String.CASE_INSENSITIVE_ORDER);
		String termLabel = "";
		for (int i = 0; i < sortedList.size(); i++) {
			String str =  (String) sortedList.get(i);
			String[] element = str.split("###");
			String separator = "; ";
			if(i==(sortedList.size()-1))
				separator = "";
			if(element.length==2){
				if(checkMainLabelList.get(str) != null && checkMainLabelList.get(str))
				{
					termLabel = termLabel + "<b>"+ element[1] + " ("+element[0]+")"+separator+"</b>";
				}
				else
				{
					termLabel = termLabel + element[1] + " ("+element[0]+")"+separator;
				}
			}
		}
		if(termLabel.length()==0)
			termLabel = "###EMPTY###"+cObj.getUri();
		
		return TreeItemAOS.getColorForTreeItem(cObj.getStatus(), termLabel).getHTML();
		
	}
	
	private Widget convert2Widget(final ConceptObject cObj, String label){
		final HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(5);
		if(cObj.getUri().startsWith(ModelConstants.ONTOLOGYBASENAMESPACE)){
			hp.add(MainApp.aosImageBundle.conceptIcon().createImage());
		}else{
			hp.add(MainApp.aosImageBundle.categoryIcon().createImage());
		}
		
		hp.setSpacing(2);
				
		if(label.startsWith("###EMPTY###"))
			label = "";
		if(label.length()==0){
			hp.add(MainApp.aosImageBundle.labelNotFound().createImage());
		}
		else
		{
			final HTML conceptLabel = new HTML(label);
			hp.add(conceptLabel);
			conceptLabel.addMouseOverHandler(new MouseOverHandler(){

				public void onMouseOver(MouseOverEvent event) {
					DOM.setStyleAttribute(hp.getElement(), "backgroundColor", "#93C2F1");
					DOM.setStyleAttribute(conceptLabel.getElement(), "cursor", "pointer");
					
				}
				
			});
			
			conceptLabel.addMouseOutHandler(new MouseOutHandler(){

				public void onMouseOut(MouseOutEvent event) {
					DOM.setStyleAttribute(hp.getElement(), "backgroundColor", "#FFFFFF");
					DOM.setStyleAttribute(conceptLabel.getElement(), "cursor", "auto");
					
				}
				
			});
			
			conceptLabel.addClickHandler(new ClickHandler()
			{

				public void onClick(ClickEvent event) {
					ModuleManager.gotoItem(cObj.getUri(), cObj.getScheme(), true, InfoTab.term, cObj.getBelongsToModule(), ModuleManager.MODULE_CONCEPT);
					ConceptNavigationHistory.this.hide();
					
				}
				
			});
		}
		
		
		
		
		return hp;
	}

}
