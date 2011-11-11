package org.fao.aoscs.client.module.validation.widgetlib;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.widgetlib.shared.dialog.DialogBoxAOS;
import org.fao.aoscs.client.widgetlib.shared.tree.TreeAOS;
import org.fao.aoscs.domain.OwlAction;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.Users;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.widgetideas.client.FastTreeItem;

public class ValidatorFilter extends DialogBoxAOS implements ClickHandler{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	public Validator val;
	public VerticalPanel statusPanel   = new VerticalPanel();
	public VerticalPanel userPanel  = new VerticalPanel();
	public VerticalPanel actionPanel = new VerticalPanel();
	public VerticalPanel datePanel = new VerticalPanel();
	private VerticalPanel panel = new VerticalPanel();
	private Button submit = new Button(constants.buttonSubmit());
	private Button cancel = new Button(constants.buttonCancel());
		
	private DateBox startDateBox = new DateBox();	
	private DateBox endDateBox = new DateBox();	
	
	public ValidatorFilter(Validator val){
		this.val = val;
		this.setSize("800px", "400px");
		this.setText(constants.valFilterVal());
		submit.addClickHandler(this);
		cancel.addClickHandler(this);
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(0);
		hp.setWidth("100%");
		hp.setStyleName("bottombar");
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(5);
		buttonPanel.add(submit);
		buttonPanel.add(cancel);
		
		hp.add(buttonPanel);
		hp.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		
		panel.setSize("100%", "100%");
		panel.setSpacing(0);
		panel.add(makeFilterPanel());
		panel.add(hp);
		panel.setCellHorizontalAlignment(hp,HasHorizontalAlignment.ALIGN_RIGHT);
		setWidget(panel);
	}
	
	public void onClick(ClickEvent event) {
		Widget sender = (Widget) event.getSource();
		if(sender.equals(cancel)){
			this.hide();
		}
		else if(sender.equals(submit))
		{
			if(filterDate(startDateBox.getTextBox().getText(), endDateBox.getTextBox().getText()))
			{
				val.reLoad();
				this.hide();
			}
		}
	}
	
	public Grid makeFilterPanel()
	{
		makeUserPanel();
		makeStatusPanel();	
		makeActionPanel();
		makeDatePanel();
		
		
		VerticalPanel vp = new VerticalPanel();
		vp.setSize("100%", "100%");
		vp.add(statusPanel);
		vp.setCellWidth(statusPanel, "100%");
		vp.setCellHeight(statusPanel, "100%");
		vp.add(new HTML("<br>"));
		vp.add(datePanel);
		
		Grid subpanel = new Grid(1,3);
		
		subpanel.setSize("100%", "100%");
		subpanel.setCellSpacing(10);
		subpanel.setWidget(0,0, userPanel);
		subpanel.setWidget(0,1, actionPanel);
		subpanel.setWidget(0,2, vp);
		
		subpanel.getCellFormatter().setWidth(0, 0, "25%");
		subpanel.getCellFormatter().setWidth(0, 1, "45%");
		subpanel.getCellFormatter().setWidth(0, 2, "30%");
		
		subpanel.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
		subpanel.getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
		subpanel.getCellFormatter().setVerticalAlignment(0, 2, HasVerticalAlignment.ALIGN_TOP);
		
		return subpanel;
		
	}
	
	public void makeUserPanel(){
		final CheckBox all = new CheckBox(constants.valAll());
		
		ArrayList<Users> user = val.getUserList();
		userPanel.clear();
		userPanel.setStyleName(Style.filter_panel_border);
		userPanel.addStyleName(Style.filter_panel_background);
		userPanel.setSize("100%", "417px");
		
		Label lb = new Label(constants.valFilterUser());
		lb.setWidth("100%");
		lb.setStyleName(Style.filter_title_background);		
		
		final ArrayList<FilterCheckBox> itemContainer = new ArrayList<FilterCheckBox>();
		VerticalPanel vp = new VerticalPanel();
		for(int i=0;i<user.size();i++){
			Users usr = (Users) user.get(i);
			//if(vTable.currentUserList.contains(new Integer(usr.getUserId())))
			{
				final FilterCheckBox item = new FilterCheckBox(val.getVFilter().getSelectedUserList(), new ArrayList<FilterCheckBox>(),usr.getUsername(),""+usr.getUserId(), "SHOWUSER");
				final CheckBox cb = item.getCb();
				cb.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						item.setCheck(cb.getValue());
						boolean chk = true;
						if(cb.getValue())
						{							 
							for(int i=0;i<itemContainer.size();i++){							
								FilterCheckBox tmp = (FilterCheckBox) itemContainer.get(i);
								if(!tmp.isCheck()){
									chk = false;
									break;
								}
							}
						}
						else {
							chk = false;
						}
						all.setValue(chk);
					}
				});
				item.setCheck(true);
				itemContainer.add(item);
				vp.add(item);
			}
		}
		ScrollPanel sc = new ScrollPanel();
		sc.setSize("100%", "417px");
		sc.setStyleName(Style.filter_scroll_background);
		sc.add(vp);
		
		all.setWidth("100%");		
		all.addStyleName(Style.font_11);		
		all.setValue(true);
		all.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(all.getValue()){
					for(int i=0;i<itemContainer.size();i++){
						FilterCheckBox tmp = (FilterCheckBox) itemContainer.get(i);
						tmp.setCheck(true);
					}
				}else{
					for(int i=0;i<itemContainer.size();i++){
						FilterCheckBox tmp = (FilterCheckBox) itemContainer.get(i);
						tmp.setCheck(false);
					}
				}
			}
		});
		
		HorizontalPanel bottomPanel = new HorizontalPanel();
		bottomPanel.setSpacing(3);
		bottomPanel.add(all);		
		bottomPanel.setCellVerticalAlignment(all, HasVerticalAlignment.ALIGN_MIDDLE);
		bottomPanel.setCellHorizontalAlignment(all, HasHorizontalAlignment.ALIGN_RIGHT);
		
		HorizontalPanel lPanel = new HorizontalPanel();
		lPanel.setStyleName("bottombar");
		lPanel.setSize("100%","100%");		
		lPanel.add(bottomPanel);
		lPanel.setCellVerticalAlignment(bottomPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		lPanel.setCellHorizontalAlignment(bottomPanel, HasHorizontalAlignment.ALIGN_LEFT);
				
		userPanel.add(lb);
		userPanel.setCellHorizontalAlignment(lb, HasHorizontalAlignment.ALIGN_CENTER);
		userPanel.add(sc);
		userPanel.setCellWidth(sc, "100%");
		userPanel.setCellHeight(sc, "100%");
		userPanel.add(lPanel);
		
	}
	
	public void makeStatusPanel(){
		final CheckBox all = new CheckBox(constants.valAll());
		
		ArrayList<OwlStatus> status = val.getStatusList();
		statusPanel.clear();
		statusPanel.setStyleName(Style.filter_panel_border);
		statusPanel.addStyleName(Style.filter_panel_background);
		statusPanel.setSize("100%", "100%");
		
		Label lb = new Label(constants.valFilterStatus());
		lb.setWidth("100%");
		lb.setStyleName(Style.filter_title_background);		
		
		final ArrayList<FilterCheckBox> itemContainer = new ArrayList<FilterCheckBox>();
		VerticalPanel vp = new VerticalPanel();
		for(int i=0;i<status.size();i++){
			OwlStatus os = (OwlStatus)status.get(i);
			if(val.getAcceptvalidationList().containsKey(new Integer(os.getId())) || val.getRejectvalidationList().containsKey(new Integer(os.getId())))
			{
				final FilterCheckBox item = new FilterCheckBox(val.getVFilter().getSelectedStatusList(), new ArrayList<FilterCheckBox>(),os.getStatus(),""+os.getId(), "SHOWSTATUS");
				final CheckBox cb = item.getCb();
				cb.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						item.setCheck(cb.getValue());
						boolean chk = true;
						if(cb.getValue())
						{							 
							for(int i=0;i<itemContainer.size();i++){							
								FilterCheckBox tmp = (FilterCheckBox) itemContainer.get(i);
								if(!tmp.isCheck()){
									chk = false;
									break;
								}
							}
						}
						else {
							chk = false;
						}
						all.setValue(chk);					
					}
				});
				item.setCheck(true);
				itemContainer.add(item);
				vp.add(item);
			}
		}
		ScrollPanel sc = new ScrollPanel();
		sc.setStyleName(Style.filter_scroll_background);
		sc.setSize("100%", "235px");
		sc.add(vp);
		
		all.setWidth("100%");		
		all.addStyleName(Style.font_11);		
		all.setValue(true);
		all.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(all.getValue()){
					for(int i=0;i<itemContainer.size();i++){
						FilterCheckBox tmp = (FilterCheckBox) itemContainer.get(i);
						tmp.setCheck(true);
					}
				}else{
					for(int i=0;i<itemContainer.size();i++){
						FilterCheckBox tmp = (FilterCheckBox) itemContainer.get(i);
						tmp.setCheck(false);
					}
				}
			}
		});
		
		HorizontalPanel bottomPanel = new HorizontalPanel();
		bottomPanel.setSpacing(3);
		bottomPanel.add(all);		
		bottomPanel.setCellVerticalAlignment(all, HasVerticalAlignment.ALIGN_MIDDLE);
		bottomPanel.setCellHorizontalAlignment(all, HasHorizontalAlignment.ALIGN_RIGHT);
		
		HorizontalPanel lPanel = new HorizontalPanel();
		lPanel.setStyleName("bottombar");
		lPanel.setSize("100%","100%");		
		lPanel.add(bottomPanel);
		lPanel.setCellVerticalAlignment(bottomPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		lPanel.setCellHorizontalAlignment(bottomPanel, HasHorizontalAlignment.ALIGN_LEFT);
		
		
		statusPanel.add(lb);
		statusPanel.setCellHorizontalAlignment(lb, HasHorizontalAlignment.ALIGN_CENTER);
		statusPanel.add(sc);
		statusPanel.setCellWidth(sc, "100%");
		statusPanel.setCellHeight(sc, "100%");
		statusPanel.add(lPanel);
		
		
	}

	public void makeActionPanel(){
		final CheckBox all = new CheckBox(constants.valAll()); 
		
		ArrayList<OwlAction> action = val.getActionList();
		actionPanel.clear();		
		actionPanel.setStyleName(Style.filter_panel_border);		
		actionPanel.addStyleName(Style.filter_panel_background);
		actionPanel.setSize("100%", "417px");
		
		Label lb = new Label(constants.valFilterAction());
		lb.setWidth("100%");							
		lb.setStyleName(Style.filter_title_background);		
		
		final ArrayList<FilterCheckBox> itemContainer = new ArrayList<FilterCheckBox>();
	    TreeAOS t = new TreeAOS(TreeAOS.TYPE_VALIDATON_FILTER);
	    HashMap<String, String> hasChildAction = new HashMap<String, String>();
		for(int i=0;i<action.size();i++){
			OwlAction act = (OwlAction) action.get(i);
			if(act.getActionChild()==null || act.getActionChild().equals(""))
			{
				final FilterCheckBox item = new FilterCheckBox(val.getVFilter().getSelectedActionList(), new ArrayList<FilterCheckBox>(),act.getAction(),""+act.getId(), "SHOWACTION");
				final CheckBox cb = item.getCb();
				cb.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						item.setCheck(cb.getValue());
						boolean chk = true;
						if(cb.getValue())
						{							 
							for(int i=0;i<itemContainer.size();i++){							
								FilterCheckBox tmp = (FilterCheckBox) itemContainer.get(i);
								if(!tmp.isCheck()){
									chk = false;
									break;
								}
							}
						}
						else {
							chk = false;
						}
						all.setValue(chk);	
					}
				});
				item.setCheck(true);
				itemContainer.add(item);
				FastTreeItem treeItem = new FastTreeItem(item);
			    t.addItem(treeItem);
			}
			else
			{
				FastTreeItem treeItem;
				if(hasChildAction.containsKey(act.getAction()))
				{
					treeItem = t.getItem(Integer.parseInt((String)hasChildAction.get(act.getAction())));
				}
				else
				{
					final FilterCheckBox item = new FilterCheckBox(val.getVFilter().getSelectedActionList(), new ArrayList<FilterCheckBox>(),act.getAction(),"", "SHOWACTION");
					final CheckBox cb = item.getCb();
					cb.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							item.setCheck(cb.getValue());
							boolean chk = true;
							if(cb.getValue())
							{							 
								for(int i=0;i<itemContainer.size();i++){							
									FilterCheckBox tmp = (FilterCheckBox) itemContainer.get(i);
									if(!tmp.isCheck()){
										chk = false;
										break;
									}
								}
							}
							else {
								chk = false;
							}
							all.setValue(chk);	
						}
					});
					item.setCheck(true);
					itemContainer.add(item);
					treeItem = new FastTreeItem(item);
				    t.addItem(treeItem);
				    int index = -1;
				    for(int ii=0;ii<t.getItemCount();ii++)
				    {
				    	if(t.getItem(ii).equals(treeItem))
				    	{
				    		index = ii;
				    		break;
				    	}
				    		
				    }
				    hasChildAction.put(act.getAction(),""+index);
				}
			    
			    final FilterCheckBox childItem = new FilterCheckBox(val.getVFilter().getSelectedActionList(), new ArrayList<FilterCheckBox>(),act.getActionChild(),""+act.getId(), "SHOWACTION");
			    childItem.getCb().addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						all.setValue(childItem.setCheck(childItem.getCb().getValue()));
					}
				});
			    childItem.setCheck(true);
			    childItem.setParentCheckBox((FilterCheckBox)treeItem.getWidget());
				itemContainer.add(childItem);
				FastTreeItem childTreeItem = new FastTreeItem(childItem);
				treeItem.addItem(childTreeItem);
				treeItem.setState(true);
				((FilterCheckBox)treeItem.getWidget()).addChildList(childItem);
			}
		}
		ScrollPanel sc = new ScrollPanel();
		
		sc.setStyleName(Style.filter_scroll_background);
		sc.setSize("100%", "417px");
		sc.add(t);
		
		all.setWidth("100%");		
		all.addStyleName(Style.font_11);				
		all.setValue(true);
		all.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(all.getValue()){
					for(int i=0;i<itemContainer.size();i++){
						FilterCheckBox tmp = (FilterCheckBox) itemContainer.get(i);
						tmp.setCheck(true);
					}
				}else{
					for(int i=0;i<itemContainer.size();i++){
						FilterCheckBox tmp = (FilterCheckBox) itemContainer.get(i);
						tmp.setCheck(false);
					}
				}
			}
		});
		
		HorizontalPanel bottomPanel = new HorizontalPanel();
		bottomPanel.setSpacing(3);
		bottomPanel.add(all);		
		bottomPanel.setCellVerticalAlignment(all, HasVerticalAlignment.ALIGN_MIDDLE);
		bottomPanel.setCellHorizontalAlignment(all, HasHorizontalAlignment.ALIGN_RIGHT);
		
		HorizontalPanel lPanel = new HorizontalPanel();
		lPanel.setStyleName("bottombar");
		lPanel.setSize("100%","100%");		
		lPanel.add(bottomPanel);
		lPanel.setCellVerticalAlignment(bottomPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		lPanel.setCellHorizontalAlignment(bottomPanel, HasHorizontalAlignment.ALIGN_LEFT);
				
		actionPanel.add(lb);
		actionPanel.setCellHorizontalAlignment(lb, HasHorizontalAlignment.ALIGN_CENTER);
		actionPanel.add(sc);
		actionPanel.setCellWidth(sc, "100%");
		actionPanel.setCellHeight(sc, "100%");
		actionPanel.add(lPanel);
	}
	
	public void makeDatePanel(){
		datePanel.clear();
		datePanel.setStyleName(Style.filter_panel_border);
		datePanel.addStyleName(Style.filter_panel_background);
		datePanel.setSize("100%", "100%");
		
		Label lb = new Label(constants.valFilterDate());
		lb.setWidth("100%");
		lb.setStyleName(Style.filter_title_background);
		
		Image clear = new Image("images/clear-grey.gif");
		clear.setStyleName(Style.Link);
		clear.addStyleName("image-link");
		clear.addStyleName(Style.font_11);
		//clear.addStyleName(Style.background_color_lighterRed_validation);
		clear.setTitle(constants.valClearDateTitle());
		
		Label clearTxt = new Label(" "+constants.valClearDate()+" ");
		clearTxt.setStyleName(Style.Link);
		//clearTxt.addStyleName(Style.background_color_lighterRed_validation);
		clearTxt.addStyleName(Style.colorBlack);
		clearTxt.addStyleName(Style.font_11);
		clearTxt.setTitle(constants.valClearDateTitle());
		
		Label fromDate = new Label(constants.valFrom());
		Label toDate = new Label(constants.valTo());
		
		final CheckBox today = new CheckBox(constants.valToday());
		final CheckBox week = new CheckBox(constants.valThisWeek());
		final CheckBox month = new CheckBox(constants.valThisMonth());
	
			
		today.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(today.getValue())
				{
					week.setValue(false);
					month.setValue(false);
										
					Date sDate = Convert.getBeginDay();					 					
					Date eDate = Convert.getEndDay();
				
					//checkDate(sDate, eDate);
					startDateBox.setValue(sDate);
					endDateBox.setValue(eDate);					
				}
				else
				{
					startDateBox.getTextBox().setText("");
					endDateBox.getTextBox().setText("");
				}
			}
		});
		
		week.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(week.getValue())
				{
					today.setValue(false);
					month.setValue(false);
					
					Date sDate = Convert.getBeginWeek();					 					
					Date eDate = Convert.getEndWeek();
					
					//checkDate(sDate, eDate);
					startDateBox.setValue(sDate);
					endDateBox.setValue(eDate);
				}
				else
				{
					startDateBox.getTextBox().setText("");
					endDateBox.getTextBox().setText("");
				}
			}
		});
		
		month.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(month.getValue())
				{
					today.setValue(false);
					week.setValue(false);					
					Date sDate = Convert.getBeginMonth();					 					
					Date eDate = Convert.getEndMonth();					
					//checkDate(sDate, eDate);
					startDateBox.setValue(sDate);
					endDateBox.setValue(eDate);
				}
				else
				{
					startDateBox.getTextBox().setText("");
					endDateBox.getTextBox().setText("");
				}
			}
		});
		
		
		clear.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				today.setValue(false);
				week.setValue(false);
				month.setValue(false);
				startDateBox.getTextBox().setText("");
				endDateBox.getTextBox().setText("");
				val.getVFilter().setFromDate(null);
				val.getVFilter().setToDate(null);
			}
		});
		
		clearTxt.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				today.setValue(false);
				week.setValue(false);
				month.setValue(false);
				startDateBox.getTextBox().setText("");
				endDateBox.getTextBox().setText("");
				val.getVFilter().setFromDate(null);
				val.getVFilter().setToDate(null);
			}
		});
		
		VerticalPanel checkPanel = new VerticalPanel();
		checkPanel.setWidth("100%");
		checkPanel.add(today);
		checkPanel.add(week);
		checkPanel.add(month);
		startDateBox.setFormat((new DateBox.DefaultFormat (DateTimeFormat.getFormat ("dd/MM/yyyy"))));
		endDateBox.setFormat((new DateBox.DefaultFormat (DateTimeFormat.getFormat ("dd/MM/yyyy"))));
		
		Grid gd = new Grid(2,3);
		gd.setCellSpacing(5);
		gd.setWidget(0,0,fromDate);
		gd.setWidget(0,1,startDateBox);
		gd.setWidget(0,2,new HTML("<font size=1>"+constants.valDateFormat()+"</font>"));
		gd.setWidget(1,0,toDate);		
		gd.setWidget(1,1,endDateBox);
		gd.setWidget(1,2,new HTML("<font size=1>"+constants.valDateFormat()+"</font>"));
		
		
		HorizontalPanel bottomPanel = new HorizontalPanel();
		bottomPanel.setSpacing(2);
		bottomPanel.add(clear);
		bottomPanel.add(clearTxt);
		bottomPanel.setCellVerticalAlignment(clear, HasVerticalAlignment.ALIGN_MIDDLE);
		bottomPanel.setCellHorizontalAlignment(clear, HasHorizontalAlignment.ALIGN_RIGHT);
		bottomPanel.setCellVerticalAlignment(clearTxt, HasVerticalAlignment.ALIGN_MIDDLE);
		bottomPanel.setCellHorizontalAlignment(clearTxt, HasHorizontalAlignment.ALIGN_RIGHT);

		VerticalPanel vp = new VerticalPanel();
		vp.setStyleName(Style.filter_scroll_background);
		vp.setSize("100%","100%");
		vp.add(checkPanel);
		vp.add(gd);
		vp.setCellVerticalAlignment(checkPanel, HasVerticalAlignment.ALIGN_TOP);
		vp.setCellVerticalAlignment(checkPanel, HasVerticalAlignment.ALIGN_TOP);
		
		HorizontalPanel lPanel = new HorizontalPanel();
		lPanel.setStyleName("bottombar");
		lPanel.setSize("100%","100%");
		lPanel.add(bottomPanel);
		lPanel.setCellVerticalAlignment(bottomPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		lPanel.setCellHorizontalAlignment(bottomPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		
		datePanel.add(lb);
		datePanel.setCellHorizontalAlignment(lb, HasHorizontalAlignment.ALIGN_CENTER);
		datePanel.setCellVerticalAlignment(lb, HasVerticalAlignment.ALIGN_TOP);
		datePanel.add(vp);
		datePanel.setCellVerticalAlignment(vp, HasVerticalAlignment.ALIGN_TOP);
		datePanel.add(lPanel);
		datePanel.setCellVerticalAlignment(lPanel, HasVerticalAlignment.ALIGN_BOTTOM);
		
	}
	
	public boolean filterDate(String from, String to)
	{
		if(from.length()>0 || to.length()>0)
		{
			if(from.length()<10 && to.length()<10)
			{
				Window.alert(constants.valDateInvalid());
				return false;
			}
			else if(from.length()<10)
			{
				Window.alert(constants.valDateStartInvalid());
				return false;
			}
			else if (to.length()<10)
			{
				Window.alert(constants.valDateEndInvalid());
				return false;
			}
			else
			{
				try{
				
					Date startDate = DateTimeFormat.getFormat("dd/MM/yyyy").parse(from);
					Date endDate = DateTimeFormat.getFormat("dd/MM/yyyy hh:mm:ss").parse(from+" 23:59:59");
					if(endDate.before(startDate))
					{
						Window.alert(constants.valDateLaterMax());
						return false;
					}
					else
					{
						val.getVFilter().setFromDate(startDate);
						val.getVFilter().setToDate(endDate);
						return true;
					}
				}
				catch(Exception e)
				{
					Window.alert(constants.valDateInvalid());
					return false;
				}
			}
		}
		else
			return true;
	}
	

}
