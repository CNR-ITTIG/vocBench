package org.fao.aoscs.client.module.consistency.widgetlib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.concept.widgetlib.InfoTab;
import org.fao.aoscs.client.module.consistency.ConsistencyTemplate;
import org.fao.aoscs.client.module.constant.ConsistencyConstants;
import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.tree.LazyLoadingTree;
import org.fao.aoscs.domain.AttributesObject;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.Consistency;
import org.fao.aoscs.domain.ConsistencyObject;
import org.fao.aoscs.domain.LabelObject;
import org.fao.aoscs.domain.NonFuncObject;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.TermObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ConsistencyCheck extends ConsistencyTemplate {
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	DateTimeFormat sdf = DateTimeFormat.getFormat("dd-MM-yyyy HH:mm:ss");
	
	public void init(HashMap<String, Consistency> list, ArrayList<String> status, ArrayList<String> termCodeProperty, ArrayList<String[]> language, int selection)
	{
		makeCheckPanel(status, termCodeProperty, language);
		initTable(list, selection, status);
		initWidgets(status, termCodeProperty, language);
	}
	
	public void initWidgets(ArrayList<String> status, ArrayList<String> termCodeProperty, ArrayList<String[]> language)
	{
		makeStatusPanel(status);
		makeDestStatusPanel(status);
		makeTermCodePropertyPanel(termCodeProperty);
		makeLanguagePanel(language);
		makeDatePanel();
		
		statusPanel.setVisible(false);
		destStatusPanel.setVisible(false);
		datePanel.setVisible(false);
		termCodePropertyPanel.setVisible(false);
		languagePanel.setVisible(false);
		
		VerticalPanel filterPanel = new VerticalPanel();
		filterPanel.setSize("100%", "100%");
		filterPanel.add(datePanel);
		filterPanel.add(statusPanel);
		filterPanel.add(destStatusPanel);
		filterPanel.add(termCodePropertyPanel);
		filterPanel.add(languagePanel);
		filterPanel.setCellVerticalAlignment(datePanel, HasVerticalAlignment.ALIGN_TOP);
		filterPanel.setCellVerticalAlignment(statusPanel, HasVerticalAlignment.ALIGN_TOP);
		filterPanel.setCellVerticalAlignment(destStatusPanel, HasVerticalAlignment.ALIGN_TOP);
		filterPanel.setCellVerticalAlignment(termCodePropertyPanel, HasVerticalAlignment.ALIGN_TOP);
		filterPanel.setCellVerticalAlignment(languagePanel, HasVerticalAlignment.ALIGN_TOP);
		filterPanel.setCellHorizontalAlignment(datePanel, HasHorizontalAlignment.ALIGN_CENTER);
		filterPanel.setCellHorizontalAlignment(statusPanel, HasHorizontalAlignment.ALIGN_CENTER);
		filterPanel.setCellHorizontalAlignment(destStatusPanel, HasHorizontalAlignment.ALIGN_CENTER);
		filterPanel.setCellHorizontalAlignment(termCodePropertyPanel, HasHorizontalAlignment.ALIGN_CENTER);
		filterPanel.setCellHorizontalAlignment(languagePanel, HasHorizontalAlignment.ALIGN_CENTER);
		
		final HorizontalPanel bodyPanel = new HorizontalPanel();
		bodyPanel.add(dp);
		bodyPanel.setCellWidth(dp, "100%");
		bodyPanel.setCellHeight(dp, "100%");
		bodyPanel.setCellVerticalAlignment(dp, HasVerticalAlignment.ALIGN_TOP);
		bodyPanel.setSize("100%", "100%");
		
		final ScrollPanel scfilter = new ScrollPanel();
		scfilter.add(filterPanel);
		
		scfilter.setSize("300px", MainApp.getBodyPanelHeight() - checkPanel.getOffsetHeight() - 80+"px");
		//bodyPanel.setSize(MainApp.getBodyPanelWidth()-380 +"px", MainApp.getBodyPanelHeight()-315 +"px");
	    Window.addResizeHandler(new ResizeHandler()
	    {
	    	public void onResize(ResizeEvent event) {
	    		//bodyPanel.setSize(MainApp.getBodyPanelWidth()-380 +"px", MainApp.getBodyPanelHeight()-315 +"px");
	    		scfilter.setSize("300px", MainApp.getBodyPanelHeight() - checkPanel.getOffsetHeight() -80+"px");
			}
		});	
		
		subpanel.setSpacing(3);
		subpanel.add(scfilter);
		subpanel.setSize("100%", "100%");
		subpanel.setCellWidth(scfilter, "100%");
		subpanel.setCellHeight(scfilter, "100%");
		subpanel.setVisible(false);
		
		HorizontalPanel mainPanel = new HorizontalPanel();
		mainPanel.setSpacing(10);
		mainPanel.add(bodyPanel);
		mainPanel.setCellWidth(bodyPanel, "100%");
		mainPanel.setCellHeight(bodyPanel, "100%");
		mainPanel.add(subpanel);
		mainPanel.setCellHeight(subpanel, "100%");
		mainPanel.setCellHorizontalAlignment(subpanel, HasHorizontalAlignment.ALIGN_RIGHT);
		mainPanel.setCellVerticalAlignment(subpanel, HasVerticalAlignment.ALIGN_TOP);
		mainPanel.setCellVerticalAlignment(bodyPanel, HasVerticalAlignment.ALIGN_TOP);
		mainPanel.setSize("100%", "100%");
		
		panel.clear();
		panel.setSize("100%", "100%");
		panel.add(checkPanel);
		panel.add(mainPanel);
		panel.setCellWidth(mainPanel, "100%");
		panel.setCellHeight(mainPanel, "100%");
		
		
	}
	
	public void initTable(HashMap<String, Consistency> list, int selection, ArrayList<String> status)
	{
		if(selection>0)
		{
			makeLayout(list, selection, false, false, status);

			VerticalPanel vTable = new VerticalPanel();
			vTable.setWidth("100%");
			vTable.addStyleName(Style.background_color_green_consistency);
			vTable.add(table);
			
			final ScrollPanel sc = (ScrollPanel) dp.getWidget(selection);
			sc.clear();
			sc.add(vTable);
			sc.setSize("100%", MainApp.getBodyPanelHeight() - checkPanel.getOffsetHeight() - 30 +"px");
			Window.addResizeHandler(new ResizeHandler()
		    {
		    	public void onResize(ResizeEvent event) {
		    		sc.setSize("100%", MainApp.getBodyPanelHeight() - checkPanel.getOffsetHeight() - 30 +"px");
				}
		    });
			
		}
		dp.showWidget(selection);
	}
	
	public HorizontalPanel makeLabel(final Consistency c, String style, final boolean isAddAction, final int tab, final int type, int imgType,  boolean dest, boolean imgOnEachItem, boolean langFilter)
	{
		String link = null;
		String imageURI = "images/concept_logo.gif";
		if(1 == imgType)
		{
			imageURI = "images/concept_logo.gif";
		}
		else if(2 == imgType)
		{
			imageURI = "images/term-logo.gif";
		}
		else if(3 == imgType)
		{
			imageURI = "images/relationship-object-logo.gif";
		}
		
		Image image = new Image(imageURI);
		HorizontalPanel ctpanel = new HorizontalPanel();
		if(type==1)
		{
			ConceptObject cObject = new ConceptObject();
			if(dest)
				cObject = c.getDestConcept();
			else
				cObject = c.getConcept();
			link = cObject.getUri();
			HashMap<String, TermObject> tObjList = cObject.getTerm();
			Iterator<String> iter = tObjList.keySet().iterator();
			while(iter.hasNext())
			{
				String key = (String )iter.next();
				TermObject tObj = (TermObject)tObjList.get(key);
				String lang = tObj.getLang();
				boolean languageFilter = MainApp.userSelectedLanguage.contains(lang);
				if(!langFilter)
				{
					languageFilter = true;
					
				}
				if(languageFilter)
				{
					final String uri = tObj.getConceptUri();
					Label label = new Label();
					label.setStyleName(style);
					label.setTitle(uri);
					HorizontalPanel hpCode = new HorizontalPanel();
					if(imgOnEachItem)
					{
						label.setText(" "+tObj.getLabel()+" ("+lang+")"+" ["+tObj.getStatus()+"]");
					}
					else
					{
						HashMap<String, AttributesObject> hMap = tObj.getTermCode();
						if(hMap!=null)
						{
							Iterator<String> itrn = hMap.keySet().iterator();
							while(itrn.hasNext())
							{
								String repositoryURI = (String)itrn.next();
								AttributesObject attributeObj = (AttributesObject)hMap.get(repositoryURI);
								NonFuncObject nfObj = attributeObj.getValue(); 
								String code = nfObj.getValue();
								String repository = LazyLoadingTree.getRelationshipLabel(attributeObj.getRelationshipObject());
								Label labelCode = new Label();
								labelCode.setStyleName(style);
								labelCode.setTitle(repository);
								if(hpCode.getWidgetCount()>0)
									labelCode.setText(", "+code);
								else
									labelCode.setText(code);
								labelCode.addClickHandler(new ClickHandler() {
									public void onClick(ClickEvent event) {
										ModuleManager.gotoConceptItem(uri, isAddAction, tab); 
									}
								});
								hpCode.add(labelCode);
							}
						}
						label.setText(" "+tObj.getLabel()+" ("+lang+")");
					}
					label.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							ModuleManager.gotoConceptItem(uri, isAddAction, tab); 
						}
					});
					
					if(ctpanel.getWidgetCount()>0)
					{
						ctpanel.add(new HTML(",&nbsp;"));
						if(imgOnEachItem)	ctpanel.add(new Image(imageURI));
					}
					ctpanel.add(label);
					if(hpCode.getWidgetCount()>0)	
					{
						ctpanel.add(new HTML("&nbsp;"));
						ctpanel.add(new Label("["));
						ctpanel.add(hpCode);
						ctpanel.add(new Label("]"));
					}
				
				}
			}
		}
		else if(type==2)
		{
			TermObject tObj = c.getTerm();
			String lang = tObj.getLang();
			link = tObj.getConceptUri();
			if(MainApp.userSelectedLanguage.contains(lang))
			{
				final String uri = tObj.getConceptUri();
				Label label = new Label();
				label.setStyleName(style);
				if(imgOnEachItem)
					label.setText(" "+tObj.getLabel()+" ("+lang+")"+" ["+tObj.getStatus()+"]");
				else
					label.setText(" "+tObj.getLabel()+" ("+lang+")");
				label.setTitle(uri);
				label.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						ModuleManager.gotoConceptItem(uri, isAddAction, tab); 
					}
				});
				if(ctpanel.getWidgetCount()>0)
				{
					ctpanel.add(new HTML(",&nbsp;"));
					if(imgOnEachItem)	ctpanel.add(new Image(imageURI));
				}
				ctpanel.add(label);
			}
		}
		else if(type==3)
		{
			RelationshipObject rObj = c.getRelationship();	
			ArrayList<LabelObject> labelList = rObj.getLabelList();
			String labelStr = "";
			for(int i=0;i<labelList.size();i++)
			{
				LabelObject labelObj = (LabelObject) labelList.get(i);
				String lang = (String) labelObj.getLanguage();
				if(MainApp.userSelectedLanguage.contains(lang))
				{
					String lab = (String) labelObj.getLabel();
					labelStr = " "+lab+" ("+lang+")";	
					Label label = new Label();
					label.setText(labelStr);
					label.setTitle(rObj.getUri());
					if(ctpanel.getWidgetCount()>0)
					{
						ctpanel.add(new HTML(",&nbsp;"));
						if(imgOnEachItem)	ctpanel.add(new Image(imageURI));
					}
					ctpanel.add(label);
				}
			}
		}
		
		if(ctpanel.getWidgetCount()==0)
		{
			Image im = new Image("images/not-found.gif");
			if(link!= null)
			{
				if(!link.equals(ModelConstants.COMMONBASENAMESPACE))
				{
					im.setTitle(link);
					final String lURI = link;
					im.setStyleName(style);
					im.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							ModuleManager.gotoItem(lURI, null, isAddAction, tab, ConceptObject.CONCEPTMODULE, type);
						}
					});
				}
			}
			ctpanel.add(im);
		}
		HorizontalPanel panel = new HorizontalPanel();
		panel.add(image);
		panel.add(ctpanel);

		return panel;
	}
	
	public void filterByLanguage(){
		makeLayout(null, listBox.getSelectedIndex(), true, false, null);
	}
	
	public void initLoadingTable(int selection){
		LoadingDialog sayLoading = new LoadingDialog();
		ScrollPanel sc = (ScrollPanel)dp.getWidget(selection);
		sc.setSize("100%", "100%");
		sc.clear();
		sc.add(sayLoading);
		dp.showWidget(selection);
	}
	
	public String formatDate(Date d)
			
	{
		if(d==null)
			return "";
		else
			return sdf.format(d);
	}

	public void makeLayout(HashMap<String, Consistency> row, int selection, boolean filter, boolean loadFilterOnly, ArrayList<String> statusArray)
	{
		
		ConsistencyObject concept = new ConsistencyObject();
		concept.setLabel(constants.conConcept());
		concept.setWidth("");
		concept.setType(ConsistencyObject.Label);
		
		ConsistencyObject destConcept = new ConsistencyObject();
		destConcept.setLabel(constants.conConceptDest());
		destConcept.setWidth("");
		destConcept.setType(ConsistencyObject.DestLabel);
		
		ConsistencyObject termBundledInDestConcept = new ConsistencyObject();
		termBundledInDestConcept.setLabel(constants.conTerm());
		termBundledInDestConcept.setWidth("");
		termBundledInDestConcept.setType(ConsistencyObject.TermBundledInDestConcept);
		
		ConsistencyObject termBundledInDestConceptIgnoreLangFilter = new ConsistencyObject();
		termBundledInDestConceptIgnoreLangFilter.setLabel(constants.conTerm());
		termBundledInDestConceptIgnoreLangFilter.setWidth("");
		termBundledInDestConceptIgnoreLangFilter.setType(ConsistencyObject.TermBundledInDestConceptIgnoreLangFilter);
		
		ConsistencyObject termConcept = new ConsistencyObject();
		termConcept.setLabel(constants.conConcept());
		termConcept.setWidth("");
		termConcept.setType(ConsistencyObject.TermConcept);
		
		ConsistencyObject conceptWithImgOnEachItem = new ConsistencyObject();
		conceptWithImgOnEachItem.setLabel(constants.conConcept());
		conceptWithImgOnEachItem.setWidth("");
		conceptWithImgOnEachItem.setType(ConsistencyObject.ConceptWithImgOnEachItem);
		
		ConsistencyObject termWithImgOnEachItem = new ConsistencyObject();
		termWithImgOnEachItem.setLabel(constants.conTerm());
		termWithImgOnEachItem.setWidth("");
		termWithImgOnEachItem.setType(ConsistencyObject.TermWithImgOnEachItem);
		
		ConsistencyObject conceptAndTerm = new ConsistencyObject();
		conceptAndTerm.setLabel(constants.conConceptTerm());
		conceptAndTerm.setWidth("");
		conceptAndTerm.setType(ConsistencyObject.ConceptAndTerm);
		
		ConsistencyObject term = new ConsistencyObject();
		term.setLabel(constants.conTerm());
		term.setWidth("");
		term.setType(ConsistencyObject.Label);
		
		ConsistencyObject termCode = new ConsistencyObject();
		termCode.setLabel(constants.conTermCode());
		termCode.setWidth("");
		termCode.setType(ConsistencyObject.TermCode);
		
		ConsistencyObject termCodeProperty = new ConsistencyObject();
		termCodeProperty.setLabel(constants.conTermCodeProperty());
		termCodeProperty.setWidth("");
		termCodeProperty.setType(ConsistencyObject.TermCodeProperty);
		
		ConsistencyObject relationship = new ConsistencyObject();
		relationship.setLabel(constants.conRelationship());
		relationship.setWidth("");
		relationship.setType(ConsistencyObject.RelationLabel);
		
		ConsistencyObject cDate = new ConsistencyObject();
		cDate.setLabel(constants.conDateCreated());
		cDate.setWidth("140px");
		cDate.setType(ConsistencyObject.CDate);
		
		ConsistencyObject mDate = new ConsistencyObject();
		mDate.setLabel(constants.conDateModified());
		mDate.setWidth("140px");
		mDate.setType(ConsistencyObject.MDate);
		
		ConsistencyObject status = new ConsistencyObject();
		status.setLabel(constants.conStatus());
		status.setWidth("100px");
		status.setType(ConsistencyObject.Status);
		
		ConsistencyObject statusList = new ConsistencyObject();
		statusList.setLabel(constants.conStatus());
		statusList.setWidth("100px");
		statusList.setType(ConsistencyObject.StatusList);
		
		ConsistencyObject destStatus = new ConsistencyObject();
		destStatus.setLabel(constants.conStatsDest());
		destStatus.setWidth("100px");
		destStatus.setType(ConsistencyObject.DestStatus);
		
		ArrayList<ConsistencyObject> list = new ArrayList<ConsistencyObject>();
		
		clearDate();
		
		switch(selection)
		{
			case 1:
				setVisibleFilter(true, false, true, false, false);
				if(!loadFilterOnly)
				{
					list.add(concept);
					list.add(cDate);
					list.add(mDate);
					list.add(status);
					if(filter)
						filterTable(list);
					else
						populateTable(row, list, selection, statusArray);
				}
				break;
			
			case 2:
				setVisibleFilter(false, false, false, false, false);
				if(!loadFilterOnly)
				{
					list.add(conceptWithImgOnEachItem);
					if(filter)
						filterTable(list);
					else
						populateTable(row, list, selection, statusArray);
				}
				break;
			
			case 3:
				setVisibleFilter(true, false, true, false, false);
				if(!loadFilterOnly)
				{
					list.add(term);
					list.add(cDate);
					list.add(mDate);
					list.add(status);
					if(filter)
						filterTable(list);
					else
						populateTable(row, list, selection, statusArray);
				}
				break;
			
			case 4:
				setVisibleFilter(false, false, true, false, false);
				if(!loadFilterOnly)
				{
					list.add(conceptAndTerm);
					list.add(cDate);
					list.add(mDate);
					list.add(statusList);
					
					if(filter)
						filterTable(list);
					else
						populateTable(row, list, selection, statusArray);
				}
				break;

			case 5:
				setVisibleFilter(true, false, true, false, false);
				if(!loadFilterOnly)
				{
					list.add(concept);
					list.add(cDate);
					list.add(mDate);
					list.add(status);
					
					if(filter)
						filterTable(list);
					else
						populateTable(row, list, selection, statusArray);
				}
				break;

			case 6:
				setVisibleFilter(true, false, true, false, false);
				if(!loadFilterOnly)
				{
					list.add(concept);
					list.add(cDate);
					list.add(mDate);
					list.add(status);
					
					if(filter)
						filterTable(list);
					else
						populateTable(row, list, selection, statusArray);
				}
				break;
			
			case 7:
				setVisibleFilter(true, true, false, false, false);
				if(!loadFilterOnly)
				{
					list.add(concept);
					list.add(status);
					list.add(relationship);
					list.add(destConcept);
					list.add(destStatus);
					
					if(filter)
						filterTable(list);
					else
						populateTable(row, list, selection, statusArray);
				}
				break;
			
			case 8:
				setVisibleFilter(true, true, false, false, false);
				if(!loadFilterOnly)
				{
					list.add(concept);
					list.add(status);
					list.add(relationship);
					list.add(destConcept);
					list.add(destStatus);
					
					if(filter)
						filterTable(list);
					else
						populateTable(row, list, selection, statusArray);
				}
				break;
			
			case 9:
				setVisibleFilter(true, true, false, false, false);
				if(!loadFilterOnly)
				{
					list.add(concept);
					list.add(status);
					list.add(relationship);
					list.add(destConcept);
					list.add(destStatus);
					
					if(filter)
						filterTable(list);
					else
						populateTable(row, list, selection, statusArray);
				}
				break;
			
			case 10:
				setVisibleFilter(true, false, true, false, false);
				if(!loadFilterOnly)
				{
					list.add(concept);
					list.add(cDate);
					list.add(mDate);
					list.add(status);
					
					if(filter)
						filterTable(list);
					else
						populateTable(row, list, selection, statusArray);
				}
				break;
			
			case 11:
				setVisibleFilter(true, false, true, false, false);
				if(!loadFilterOnly)
				{
					list.add(term);
					list.add(termConcept);
					list.add(cDate);
					list.add(mDate);
					list.add(status);
					
					if(filter)
						filterTable(list);
					else
						populateTable(row, list, selection, statusArray);
				}
				break;
			
			case 12:
				setVisibleFilter(false, false, false, true, false);
				if(!loadFilterOnly)
				{
					list.add(termWithImgOnEachItem);
					list.add(termCode);
					list.add(termCodeProperty);
					if(filter)
						filterTable(list);
					else
						populateTable(row, list, selection, statusArray);
				}
				break;
			
			case 13:
				setVisibleFilter(true, false, true, false, false);
				if(!loadFilterOnly)
				{
					list.add(concept);
					list.add(cDate);
					list.add(mDate);
					list.add(status);
					
					if(filter)
						filterTable(list);
					else
						populateTable(row, list, selection, statusArray);
				}
				break;
				
			case 14:
				setVisibleFilter(false, false, false, false, false);
				if(!loadFilterOnly)
				{
					list.add(relationship);
					if(filter)
						filterTable(list);
					else
						populateTable(row, list, selection, statusArray);
				}
				break;
				
			case 15:
				setVisibleFilter(true, false, true, false, false);
				if(!loadFilterOnly)
				{
					list.add(concept);
					list.add(cDate);
					list.add(mDate);
					list.add(status);
					
					if(filter)
						filterTable(list);
					else
						populateTable(row, list, selection, statusArray);
				}
				break;
				
			case 16:
				setVisibleFilter(true, false, true, false, false);
				if(!loadFilterOnly)
				{
					list.add(concept);
					list.add(termBundledInDestConceptIgnoreLangFilter);
					list.add(cDate);
					list.add(mDate);
					list.add(status);
					
					if(filter)
						filterTable(list);
					else
						populateTable(row, list, selection, statusArray);
				}
				break;
				
			case 17:
				setVisibleFilter(true, false, true, false, false);
				if(!loadFilterOnly)
				{
					list.add(termBundledInDestConcept);
					list.add(termConcept);
					list.add(cDate);
					list.add(mDate);
					list.add(status);
					
					if(filter)
						filterTable(list);
					else
						populateTable(row, list, selection, statusArray);
				}
				break;
				
			case 18:
				setVisibleFilter(true, false, true, false, false);
				if(!loadFilterOnly)
				{
					list.add(termBundledInDestConcept);
					list.add(concept);
					list.add(cDate);
					list.add(mDate);
					list.add(status);
					
					if(filter)
						filterTable(list);
					else
						populateTable(row, list, selection, statusArray);
				}
				break;
				
			case 19:
				setVisibleFilter(true, false, true, false, true);
				if(!loadFilterOnly)
				{
					list.add(concept);
					list.add(cDate);
					list.add(mDate);
					list.add(status);
				
					if(filter)
						filterTable(list);
					else
						populateTable(row, list, selection, statusArray);
				}
				break;
			default:
				break;
				
		}
		if(!statusPanel.isVisible() && !destStatusPanel.isVisible()  && !datePanel.isVisible() && !termCodePropertyPanel.isVisible())
		{
			SHPanel.setVisible(false);
			subpanel.setVisible(false);
		}
	}
	
	public void initializeShowValue(Consistency c)
	{
		if(statusPanel.isVisible())	
			c.setShowStatus(new Boolean(true));
		else
			c.setShowStatus(null);
		if(destStatusPanel.isVisible())	
			c.setShowDestStatus(new Boolean(true));
		else
			c.setShowDestStatus(null);
		if(termCodePropertyPanel.isVisible())	
			c.setShowTermCodeProperty(new Boolean(true));
		else
			c.setShowTermCodeProperty(null);
		if(datePanel.isVisible())	
			c.setShowDate(new Boolean(true));
		else
			c.setShowDate(null);
		if(languagePanel.isVisible())	
			c.setShowLanguage(new Boolean(true));
		else
			c.setShowLanguage(null);
	}
	
	public void setVisibleFilter(boolean status, boolean destStatus, boolean date, boolean termCodeProperty, boolean language)
	{
		statusPanel.setVisible(status);
		destStatusPanel.setVisible(destStatus);
		datePanel.setVisible(date);
		termCodePropertyPanel.setVisible(termCodeProperty);
		languagePanel.setVisible(language);
	}
	
	public void filterTable(ArrayList<ConsistencyObject> col)
	{
		int columnSize = col.size();
		for(int i=1;i<table.getRowCount();i++)
		{
			Consistency c = (Consistency)table.getRowValue(Integer.toString(i));
			
			for(int j=0;j<columnSize;j++)
			{
				
				ConsistencyObject co = (ConsistencyObject) col.get(j);
				int type = ConsistencyConstants.getResultType((Integer.parseInt(listBox.getValue(listBox.getSelectedIndex()))));
				if(co.getType()==ConsistencyObject.Label)
					table.setWidget(i,j,  makeLabel(c, "term-Label", true, InfoTab.term, type, type, false, false, true));
				else if(co.getType()==ConsistencyObject.DestLabel)
					table.setWidget(i,j,  makeLabel(c, "term-Label", true, InfoTab.term, type, type, true, false, true));
				else if(co.getType()==ConsistencyObject.TermBundledInDestConcept)
					table.setWidget(i,j,  makeLabel(c, "term-Label", true, InfoTab.term, type, 2, true, false, true));
				else if(co.getType()==ConsistencyObject.TermConcept)
					table.setWidget(i,j,  makeLabel(c, "term-Label", true, InfoTab.term, 1, 1, false, false, true));
				else if(co.getType()==ConsistencyObject.RelationLabel)
					table.setWidget(i,j,  makeLabel(c, "term-Label", true, InfoTab.term, 3, 3, false, false, true));
				else if(co.getType()==ConsistencyObject.ConceptWithImgOnEachItem)
					table.setWidget(i,j,  makeLabel(c, "term-Label", true, InfoTab.term, type, 1, false, true, true));
				else if(co.getType()==ConsistencyObject.TermWithImgOnEachItem)
					table.setWidget(i,j,  makeLabel(c, "term-Label", true, InfoTab.term, type, 2, false, true, true));
				else if(co.getType()==ConsistencyObject.ConceptAndTerm)
				{
					if(c.getTerm().getUri()==null) 
						type = 1;
					else
						type = 2;
					table.setWidget(i,j,  makeLabel(c, "term-Label", true, InfoTab.term, type, type, false, false,true));
				}
			}
			
			initializeShowValue(c);
		}
	}
	
	public void populateTable(HashMap<String, Consistency> row, ArrayList<ConsistencyObject> col, int selection, ArrayList<String> status)
	{
		int columnSize = col.size();
		table = new FilterGrid(row.size()+1,columnSize);
		table.setWidth("100%");

		GridStyle.setTableConceptDetailStyleTop(table,"gstFR1","gstR1","gstR1","gstPanel1",true);
		
		for(int i=0;i<columnSize;i++)
		{
			ConsistencyObject co = (ConsistencyObject) col.get(i);
			table.setWidget(0,i,new HTML("<b>"+co.getLabel()+"</b>"));
			table.getCellFormatter().setWidth(0, i, co.getWidth());
			table.getCellFormatter().setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_CENTER);
		}
		
		ArrayList<String> keys = new ArrayList<String>(); 
		keys.addAll(row.keySet()); 
		Collections.sort(keys, String.CASE_INSENSITIVE_ORDER); 
		Iterator<String> itr = keys.iterator();
		for(int i=0;i<keys.size();i++)
		{
			String key = (String)itr.next();
			final Consistency c = (Consistency)row.get(key);
			initializeShowValue(c);
			table.setRowValue(i+1, c);
			for(int j=0;j<columnSize;j++)
			{
				ConsistencyObject co = (ConsistencyObject) col.get(j);
				int type = ConsistencyConstants.getResultType((Integer.parseInt(listBox.getValue(listBox.getSelectedIndex()))));
				if(co.getType()==ConsistencyObject.Label)
					table.setWidget(i+1,j,  makeLabel(c, "term-Label", true, InfoTab.term, type, type, false, false, true));
				else if(co.getType()==ConsistencyObject.CDate)
				{
					table.setWidget(i+1,j,  new Label(formatDate(c.getDateCreate())));
					table.getCellFormatter().setHorizontalAlignment(i+1, j, HasHorizontalAlignment.ALIGN_CENTER);
				}
				else if(co.getType()==ConsistencyObject.MDate)
				{
					table.setWidget(i+1,j,  new Label(formatDate(c.getDateModified())));
					table.getCellFormatter().setHorizontalAlignment(i+1, j, HasHorizontalAlignment.ALIGN_CENTER);
				}
				else if(co.getType()==ConsistencyObject.Status)
				{
					table.setWidget(i+1,j,  new Label(c.getStatus()));
				}
				else if(co.getType()==ConsistencyObject.StatusList)
				{
					table.setWidget(i+1,j,  makeStatusListBox(status, c));
				}
				else if(co.getType()==ConsistencyObject.DestStatus)
				{
					table.setWidget(i+1,j,  new Label(c.getDestStatus()));
				}
				else if(co.getType()==ConsistencyObject.DestLabel)
				{
					table.setWidget(i+1,j,  makeLabel(c, "term-Label", true, InfoTab.term, type, type, true, false, true));
				}
				else if(co.getType()==ConsistencyObject.TermBundledInDestConcept)
				{
					table.setWidget(i+1,j,  makeLabel(c, "term-Label", true, InfoTab.term, type, 2, true, false, true));
				}
				else if(co.getType()==ConsistencyObject.TermBundledInDestConceptIgnoreLangFilter)
				{
					table.setWidget(i+1,j,  makeLabel(c, "term-Label", true, InfoTab.term, type, 2, true, false, false));
				}
				else if(co.getType()==ConsistencyObject.TermConcept)
				{
					table.setWidget(i+1,j,  makeLabel(c, "term-Label", true, InfoTab.term, 1, 1, false, false, true));
				}
				else if(co.getType()==ConsistencyObject.RelationLabel)
				{
					table.setWidget(i+1,j,  makeLabel(c, "term-Label", true, InfoTab.term, 3, 3, false, false, true));
				}
				else if(co.getType()==ConsistencyObject.ConceptWithImgOnEachItem)
				{
					table.setWidget(i+1,j,  makeLabel(c, "term-Label", true, InfoTab.term, type, type, false, true, true));
				}
				else if(co.getType()==ConsistencyObject.TermWithImgOnEachItem)
				{
					table.setWidget(i+1,j,  makeLabel(c, "term-Label", true, InfoTab.term, type, 2, false, true, true));
				}
				else if(co.getType()==ConsistencyObject.TermCode)
				{
					table.setWidget(i+1,j,  new Label(c.getTermCode()));
				}
				else if(co.getType()==ConsistencyObject.TermCodeProperty)
				{
					table.setWidget(i+1,j,  new Label(c.getTermCodeProperty()));
				}
				else if(co.getType()==ConsistencyObject.ConceptAndTerm)
				{
					if(c.getTerm().getUri()==null) 
						type = 1;
					else
						type = 2;
					table.setWidget(i+1,j,  makeLabel(c, "term-Label", true, InfoTab.term, type, type, false, false, true));
				}
				
			}
		}
	}
	
	public Widget makeStatusListBox(ArrayList<String> statusList, final Consistency c)
	{
		final ListBox listbox = new ListBox();
		listbox.addItem(constants.conNotAvailable(), "");
		for(int i=0;i<statusList.size();i++){
			String statusValue = (String) statusList.get(i);
			listbox.addItem(statusValue);
			if(c.getStatus().equals(statusValue))
				listbox.setSelectedIndex(i);
		}
		listbox.addChangeHandler(new ChangeHandler()
		{
			public void onChange(ChangeEvent event) {
				
				c.setStatus(listbox.getValue(listbox.getSelectedIndex()));
			}
		});
		return listbox;
	}
	
	public void checkConsistency(final int selection, final ArrayList<String> statusList, final ArrayList<String> termCodePropertyList, final ArrayList<String[]> languageList)
	{
		initLoadingTable(selection);
		AsyncCallback<HashMap<String, Consistency>> callback = new AsyncCallback<HashMap<String, Consistency>>() {
			public void onSuccess(HashMap<String, Consistency> list) {
				initTable(list, selection, statusList);
				makeStatusPanel(statusList);
				makeDestStatusPanel(statusList);
				makeTermCodePropertyPanel(termCodePropertyList);
				makeLanguagePanel(languageList);
				subpanel.setVisible(true);
			}
			public void onFailure(Throwable caught) {
				Window.alert(constants.conLoadConDataFail());
			}
		};
		Service.consistencyService.getConsistencyQueue((Integer.parseInt(listBox.getValue(selection))), MainApp.userOntology, callback);
	}
	
	public void update(ArrayList<Consistency> updateValue, final int selection, final ArrayList<String> statusList, final ArrayList<String> termCodePropertyList, final ArrayList<String[]> languageList)
	{
		initLoadingTable(selection);
		AsyncCallback<HashMap<String, Consistency>> callback = new AsyncCallback<HashMap<String, Consistency>>() {
			public void onSuccess(HashMap<String, Consistency> list) {
				initTable(list, selection, statusList);
				makeStatusPanel(statusList);
				makeDestStatusPanel(statusList);
				makeTermCodePropertyPanel(termCodePropertyList);
				makeLanguagePanel(languageList);
				ModuleManager.resetConcept();
			}
			public void onFailure(Throwable caught) {
				Window.alert(constants.conLoadConDataFail());
			}
		};
		Service.consistencyService.updateConsistencyQueue(updateValue, (Integer.parseInt(listBox.getValue(selection))), MainApp.userOntology, callback);
		
	}


	
}