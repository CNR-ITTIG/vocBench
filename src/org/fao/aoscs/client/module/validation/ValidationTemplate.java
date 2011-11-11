package org.fao.aoscs.client.module.validation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.OWLActionConstants;
import org.fao.aoscs.client.module.constant.OWLStatusConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.module.validation.widgetlib.ValidationTable;
import org.fao.aoscs.client.module.validation.widgetlib.ValidatorFilter;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.domain.OwlAction;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.Users;
import org.fao.aoscs.domain.Validation;
import org.fao.aoscs.domain.ValidationFilter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ValidationTemplate extends Composite{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	public VerticalPanel validatorPanel = new VerticalPanel();
	public VerticalPanel tablePanel = new VerticalPanel();
	public HorizontalPanel titlePanel   = new HorizontalPanel();
	public HorizontalPanel validationFooterPanel = new HorizontalPanel();
	public ValidatorFilter valFilter = null;
	
	private ValidationFilter vFilter= new ValidationFilter();
	private ArrayList<OwlStatus> statusList = new ArrayList<OwlStatus>(); 
	private ArrayList<Users> userList = new ArrayList<Users>();
	private ArrayList<OwlAction> actionList = new ArrayList<OwlAction>();
	private HashMap<Integer, Integer> acceptvalidationList = new HashMap<Integer, Integer>();
	private HashMap<Integer, Integer> rejectvalidationList = new HashMap<Integer, Integer>();
	public ValidationTable vTable = null;
	
	public ValidationTemplate(){
		vFilter.setGroupID(MainApp.groupId);
		vFilter.setOntoInfo(MainApp.userOntology);
		
		HorizontalPanel dPanel = new HorizontalPanel();
		dPanel.add(validatorPanel);
		dPanel.setStyleName("borderbar");
		
		VerticalPanel tempPanel = new VerticalPanel();
		tempPanel.setSize("100%", "100%");
		tempPanel.add(dPanel);
		tempPanel.setCellWidth(dPanel, "100%");
		tempPanel.setCellHeight(dPanel, "100%");
		tempPanel.setCellHorizontalAlignment(dPanel, HasHorizontalAlignment.ALIGN_CENTER);
		tempPanel.setCellVerticalAlignment(dPanel, HasVerticalAlignment.ALIGN_MIDDLE);

		
		initWidget(tempPanel);
	}
	
	public static void loadUser(String userID){
		Iterator<?> it = RootPanel.get().iterator();
		while(it.hasNext()){
			Object o = it.next();
			if(o instanceof MainApp)
			{
				MainApp m = (MainApp) o;
				m.loadUser(userID);
			}
			
		}
	}
	
	public void initLoadingTable(){
		int height = tablePanel.getOffsetHeight();
		int width = tablePanel.getOffsetWidth();
		LoadingDialog sayLoading = new LoadingDialog();
		tablePanel.clear();
		tablePanel.setSize(width+"px", height+"px");
		tablePanel.add(sayLoading);
	}
	
	public void init(int object){}
	public void initTable(int object){}
	@SuppressWarnings("unchecked")
	public void update(ArrayList<Validation> updateValue){
		initLoadingTable();
		Service.validationService.updateValidateQueue(updateValue, getVFilter(), new AsyncCallback<Integer>() {
			public void onSuccess(Integer size) {
				try
				{
					ModuleManager.resetConcept();
					ModuleManager.resetClassification();
					//ConceptModule.reloadRecentChanges();
				}
				catch(Exception e)
				{}
				initTable(size);
			}
			public void onFailure(Throwable caught) {
				initTable(0);	
			}
		});
	}
	public void reLoad()
	{
		initLoadingTable();
		Service.validationService.getValidatesize(vFilter, new AsyncCallback<Object>() {
			public void onSuccess(Object result) {
				int size = 0;
				try
				{
					size = (Integer) result;
				}
				catch(Exception e)
				{}
				initTable(size);
			}

			public void onFailure(Throwable caught) {
				initTable(0);
				Window.alert(constants.valLoadValDataSizeFail());
			}
		});
	}
	
	public int getSelectedIndex(ListBox lb, String selectedItem)
	{
		for(int i=0;i<lb.getItemCount();i++){
			if(selectedItem.equals(lb.getItemText(i))){
				return i;
			}
		}
		return -1;
	}
	
	public static String getStatusFromID(int id, ArrayList<OwlStatus> list)
	{
		String value = "";
		for(int i=0;i<list.size();i++){
			OwlStatus os = (OwlStatus)list.get(i);
			if(id == os.getId()) value = os.getStatus();
		}
		return value;
	}
	
	public static String getActionFromID(int id, ArrayList<OwlAction> list)
	{
		String value = "";
		for(int i=0;i<list.size();i++){
			OwlAction os = (OwlAction)list.get(i);
			if(id == os.getId()) 
				value = os.getAction() + ((os.getActionChild() != null && os.getActionChild().length() > 0) ? "-"+os.getActionChild() : "" );
		}
		return value;
	}
	
	public static String getActionChildFromID(int id, ArrayList<OwlAction> list)
	{
		String value = "";
		for(int i=0;i<list.size();i++){
			OwlAction os = (OwlAction)list.get(i);
			if(id == os.getId()) value = "-"+os.getActionChild();
		}
		return value;
	}
	
	public static String getUserNameFromID(int id, ArrayList<Users> list)
	{
		String value = "";
		for(int i=0;i<list.size();i++){
			Users u = (Users) list.get(i);
			if(id  == u.getUserId()) value = u.getUsername();
		}
		return value;
	}
	
	public void checkEnable(CheckBox accept, CheckBox reject, int rowstatus)
	{
		accept.setEnabled(false);
		reject.setEnabled(false);
		if(MainApp.permissionTable.contains(OWLActionConstants.VALIDATIONACCEPTED, -1) && acceptvalidationList.containsKey(new Integer(rowstatus))) 
			accept.setEnabled(true);
		if(MainApp.permissionTable.contains(OWLActionConstants.VALIDATIONREJECTED, -1) && rejectvalidationList.containsKey(new Integer(rowstatus))) 
			reject.setEnabled(true);
	}
	
	public void changeStatus(HashMap<Integer, Integer> list, int row, int column, int oldselectedItem, ArrayList<OwlStatus> statusList)
	{
		if(list.containsKey(new Integer(oldselectedItem)))
		{
			int newselectedItem = ((Integer)list.get(new Integer(oldselectedItem))).intValue();
			vTable.getDataTable().setWidget(row, column, new HTML(""+getStatusFromID(newselectedItem, statusList)));
			vTable.getDataTable().getCellFormatter().addStyleName(row, column, "validate-red");
		}	
	}

	
	public void makeTitlePanel(){
		titlePanel.clear();
		titlePanel.setWidth("100%");
		titlePanel.addStyleName("maintopbar");
		
		Image reload = new Image("images/reload-grey.gif");
		reload.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				reLoad();
			}
		});
		reload.setTitle(constants.valReload());
		reload.setStyleName(Style.Link);
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(5);		
		hp.add(reload);		
		hp.setCellVerticalAlignment(reload, HasVerticalAlignment.ALIGN_MIDDLE);
		hp.setCellHorizontalAlignment(reload, HasHorizontalAlignment.ALIGN_LEFT);
		
		HorizontalPanel SHPanel = new HorizontalPanel();
		Image filterImg = new Image("images/filter-grey.gif");
		Label applyFilter = new Label(constants.valFilterApply());
		applyFilter.setStyleName(Style.Link);
		applyFilter.addStyleName(Style.colorBlack);
		applyFilter.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				valFilter.show();
				}
			});
		SHPanel.setSpacing(5);
		SHPanel.add(filterImg);
		SHPanel.add(applyFilter);

		HTML titleName = new HTML(constants.valTitle());
		titleName.setWordWrap(false);
		titleName.addStyleName("maintopbartitle");
		
		titlePanel.add(titleName);
		titlePanel.setCellWidth(titleName, "50px");
		titlePanel.add(hp);
		titlePanel.add(SHPanel);
		titlePanel.setCellVerticalAlignment(titleName, HasVerticalAlignment.ALIGN_MIDDLE);
		titlePanel.setCellHorizontalAlignment(titleName, HasHorizontalAlignment.ALIGN_LEFT);
		titlePanel.setCellVerticalAlignment(hp, HasVerticalAlignment.ALIGN_MIDDLE);
		titlePanel.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_LEFT);
		titlePanel.setCellVerticalAlignment(SHPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		titlePanel.setCellHorizontalAlignment(SHPanel, HasHorizontalAlignment.ALIGN_RIGHT);
	}
	
	public void makeValidationPanel()
	{
		validationFooterPanel.clear();		
		DOM.setStyleAttribute(validationFooterPanel.getElement(), "background", "#ffffff url('images/bg_headergradient.png') repeat-x bottom left");
		validationFooterPanel.setSize("100%", "100%");
		
		final CheckBox acceptAll = new CheckBox(constants.valAcceptAll());
		final CheckBox rejectAll = new CheckBox(constants.valRejectAll());
		Button doneButton = new Button(constants.valValidate());
		
		doneButton.setEnabled(MainApp.permissionTable.contains(OWLActionConstants.VALIDATIONACCEPTED, -1) || MainApp.permissionTable.contains(OWLActionConstants.VALIDATIONREJECTED, -1));
		acceptAll.setEnabled(MainApp.permissionTable.contains(OWLActionConstants.VALIDATIONACCEPTED, -1));
		rejectAll.setEnabled(MainApp.permissionTable.contains(OWLActionConstants.VALIDATIONREJECTED, -1));
		acceptAll.setWidth("100%");
		rejectAll.setWidth("100%");
		acceptAll.addStyleName(Style.colorBlack);
		rejectAll.addStyleName(Style.colorBlack);
		acceptAll.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Widget sender = (Widget) event.getSource();
				if(((CheckBox)sender).getValue()){
					for(int j=0;j<vTable.getDataTable().getRowCount();j++){
						if(vTable.getDataTable().getRowFormatter().isVisible(j))
						{
							org.fao.aoscs.domain.Validation v = (org.fao.aoscs.domain.Validation)vTable.getPagingScrollTable().getRowValue(j);
							
							if(acceptvalidationList.containsKey(new Integer(v.getStatus())))
							{
								changeStatus(acceptvalidationList, j, v.getStatusColumn(),v.getStatus() ,statusList);
								vTable.getDataTable().getCellFormatter().addStyleName(j, v.getStatusColumn(), "validate-red");
								((CheckBox)((VerticalPanel)vTable.getDataTable().getWidget(j,vTable.getDataTable().getColumnCount()-1)).getWidget(0)).setValue(true);
								((CheckBox)((VerticalPanel)vTable.getDataTable().getWidget(j,vTable.getDataTable().getColumnCount()-1)).getWidget(1)).setEnabled(false);
							}
						}
					}
					rejectAll.setEnabled(false);
				}
				else
				{
					for(int j=0;j<vTable.getDataTable().getRowCount();j++){
						org.fao.aoscs.domain.Validation v = (org.fao.aoscs.domain.Validation)vTable.getPagingScrollTable().getRowValue(j);
						vTable.getDataTable().setWidget(j, v.getStatusColumn(), new HTML(getStatusFromID(v.getStatus(), statusList)));
						vTable.getDataTable().getCellFormatter().removeStyleName(j, v.getStatusColumn(), "validate-red");
						((CheckBox)((VerticalPanel)vTable.getDataTable().getWidget(j,vTable.getDataTable().getColumnCount()-1)).getWidget(0)).setValue(false);
						checkEnable(((CheckBox)((VerticalPanel)vTable.getDataTable().getWidget(j,vTable.getDataTable().getColumnCount()-1)).getWidget(0)), ((CheckBox)((VerticalPanel)vTable.getDataTable().getWidget(j,vTable.getDataTable().getColumnCount()-1)).getWidget(1)), v.getStatus());
					}
					rejectAll.setEnabled(MainApp.permissionTable.contains(OWLActionConstants.VALIDATIONREJECTED, -1) && true);
				}
				
			}
		});
		
		rejectAll.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Widget sender = (Widget) event.getSource();
				if(((CheckBox)sender).getValue()){
					for(int j=0;j<vTable.getDataTable().getRowCount();j++){
						if(vTable.getDataTable().getRowFormatter().isVisible(j))
						{
							org.fao.aoscs.domain.Validation v = (org.fao.aoscs.domain.Validation)vTable.getPagingScrollTable().getRowValue(j);
							if(rejectvalidationList.containsKey(new Integer(v.getStatus())))
							{
								if(v.getStatus()==OWLStatusConstants.VALIDATED_ID)
								{
										if(v.getOldStatus()==0) 
											vTable.getDataTable().setWidget(j, v.getStatusColumn(), new HTML(""+getStatusFromID(OWLStatusConstants.DELETED_ID, statusList)));
										else	
											vTable.getDataTable().setWidget(j, v.getStatusColumn(), new HTML(""+getStatusFromID(v.getOldStatus(), statusList)));
										vTable.getDataTable().getCellFormatter().addStyleName(j, v.getStatusColumn(), "validate-red");
								}
								else if(v.getStatus()==OWLStatusConstants.PROPOSED_DEPRECATED_ID)
								{
									vTable.getDataTable().setWidget(j, v.getStatusColumn(), new HTML(""+getStatusFromID(v.getOldStatus(), statusList)));
									vTable.getDataTable().getCellFormatter().addStyleName(j, v.getStatusColumn(), "validate-red");
								}
								else
								{
									int newselectedItem = ((Integer)rejectvalidationList.get(new Integer(v.getStatus()))).intValue();
									vTable.getDataTable().setWidget(j, v.getStatusColumn(), new HTML(""+getStatusFromID(newselectedItem, statusList)));
									vTable.getDataTable().getCellFormatter().addStyleName(j, v.getStatusColumn(), "validate-red");
									if(newselectedItem==OWLStatusConstants.DELETED_ID)
									{
										TextArea ta = (TextArea) vTable.getDataTable().getWidget(j, v.getNoteColumn());
										ta.setReadOnly(false);
										ta.setFocus(true);
									}
								}
									
								((CheckBox)((VerticalPanel)vTable.getDataTable().getWidget(j,vTable.getDataTable().getColumnCount()-1)).getWidget(1)).setValue(true);
								((CheckBox)((VerticalPanel)vTable.getDataTable().getWidget(j,vTable.getDataTable().getColumnCount()-1)).getWidget(0)).setEnabled(false);
							}
						}
					}
					acceptAll.setEnabled(false);
				}
				else
				{
					for(int j=0;j<vTable.getDataTable().getRowCount();j++){
						org.fao.aoscs.domain.Validation v = (org.fao.aoscs.domain.Validation)vTable.getPagingScrollTable().getRowValue(j);
						TextArea ta = (TextArea) vTable.getDataTable().getWidget(j, v.getNoteColumn());
						ta.setReadOnly(true);
						ta.setText(v.getNote()==null?"":v.getNote());
						vTable.getDataTable().setWidget(j, v.getStatusColumn(), new HTML(getStatusFromID(v.getStatus(), statusList)));
						vTable.getDataTable().getCellFormatter().removeStyleName(j, v.getStatusColumn(), "validate-red");
						((CheckBox)((VerticalPanel)vTable.getDataTable().getWidget(j,vTable.getDataTable().getColumnCount()-1)).getWidget(1)).setValue(false);
						checkEnable(((CheckBox)((VerticalPanel)vTable.getDataTable().getWidget(j,vTable.getDataTable().getColumnCount()-1)).getWidget(0)), ((CheckBox)((VerticalPanel)vTable.getDataTable().getWidget(j,vTable.getDataTable().getColumnCount()-1)).getWidget(1)), v.getStatus());
					}
					acceptAll.setEnabled(MainApp.permissionTable.contains(OWLActionConstants.VALIDATIONACCEPTED, -1) && true);
				}
			}
		});
		
		doneButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ArrayList<Validation> updateValue = new ArrayList<Validation>();
				for(int j=0;j<vTable.getDataTable().getRowCount();j++){
					org.fao.aoscs.domain.Validation v = (org.fao.aoscs.domain.Validation)vTable.getPagingScrollTable().getRowValue(j);
					CheckBox accept = ((CheckBox)((VerticalPanel)vTable.getDataTable().getWidget(j,vTable.getDataTable().getColumnCount()-1)).getWidget(0));
					CheckBox reject = ((CheckBox)((VerticalPanel)vTable.getDataTable().getWidget(j,vTable.getDataTable().getColumnCount()-1)).getWidget(1));
					vTable.getDataTable().getCellFormatter().addStyleName(j, v.getStatusColumn(), "validate-red");
					int oldselectedItem = v.getStatus();
					if(accept.getValue() && !reject.getValue())
					{
						if(acceptvalidationList.containsKey(new Integer(oldselectedItem)))
						{
							Integer newselectedItem = (Integer)acceptvalidationList.get(new Integer(oldselectedItem));
							v.setIsAccept(new Boolean(true));
							v.setStatus(newselectedItem.intValue());
							v.setStatusLabel(getStatusFromID(newselectedItem.intValue(), statusList));
							v.setValidatorId(MainApp.userId);
							
							// check if status is validated, isValidated is set to false so that it's status can be changed to published
							if(v.getStatus()==OWLStatusConstants.VALIDATED_ID)
								v.setIsValidate(new Boolean(false));
							else
								v.setIsValidate(new Boolean(true));
							
							v.setDateModified(new Date());
							updateValue.add(v);
						}	
					}
					else if(reject.getValue() && !accept.getValue())
					{
						if(rejectvalidationList.containsKey(new Integer(oldselectedItem)))
						{
							int newselectedItem = ((Integer)rejectvalidationList.get(new Integer(oldselectedItem))).intValue();
							if(v.getStatus()==OWLStatusConstants.VALIDATED_ID)
							{
								if(v.getOldStatus()==0) 
									newselectedItem = OWLStatusConstants.DELETED_ID;
								else	
									newselectedItem = v.getOldStatus();
							}
							else if(v.getStatus()==OWLStatusConstants.PROPOSED_DEPRECATED_ID)
							{
								newselectedItem = v.getOldStatus();
							}

							v.setIsAccept(new Boolean(false));
							v.setStatus(newselectedItem);
							v.setDateModified(new Date());
							
							v.setStatusLabel(getStatusFromID(newselectedItem, statusList));
							if(v.getStatus()==99) v.setNote(((TextArea)vTable.getDataTable().getWidget(j, v.getNoteColumn())).getText());
							v.setValidatorId(MainApp.userId);
							
							// check if status is validated, isValidated is set to false so that it's status can be changed to published
							if(v.getStatus()==OWLStatusConstants.VALIDATED_ID)
								v.setIsValidate(new Boolean(false));
							else
								v.setIsValidate(new Boolean(true));
							updateValue.add(v);
						}	
					}
				}
				update(updateValue);
				acceptAll.setValue(false);
				rejectAll.setValue(false);
			}
		});
		
		HorizontalPanel hp = new HorizontalPanel();		
		DOM.setStyleAttribute(hp.getElement(), "background", "#ffffff url('images/bg_headergradient.png') repeat-x bottom left");		
		hp.setSpacing(5);
		hp.add(acceptAll);
		hp.add(rejectAll);
		hp.add(doneButton);
		hp.setCellHorizontalAlignment(acceptAll, HasHorizontalAlignment.ALIGN_RIGHT);
		hp.setCellHorizontalAlignment(rejectAll, HasHorizontalAlignment.ALIGN_RIGHT);
		hp.setCellHorizontalAlignment(doneButton, HasHorizontalAlignment.ALIGN_RIGHT);
		hp.setCellVerticalAlignment(acceptAll, HasVerticalAlignment.ALIGN_MIDDLE);
		hp.setCellVerticalAlignment(rejectAll, HasVerticalAlignment.ALIGN_MIDDLE);
		hp.setCellVerticalAlignment(doneButton, HasVerticalAlignment.ALIGN_MIDDLE);
		
		validationFooterPanel.add(hp);
		validationFooterPanel.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_RIGHT);
	}
	
	
	
	

	public ArrayList<OwlStatus> getStatusList() {
		return statusList;
	}

	public void setStatusList(ArrayList<OwlStatus> statusList) {
		this.statusList = statusList;
	}

	public ArrayList<Users> getUserList() {
		return userList;
	}

	public void setUserList(ArrayList<Users> userList) {
		this.userList = userList;
	}

	public ArrayList<OwlAction> getActionList() {
		return actionList;
	}

	public void setActionList(ArrayList<OwlAction> actionList) {
		this.actionList = actionList;
	}

	public HashMap<Integer, Integer> getAcceptvalidationList() {
		return acceptvalidationList;
	}

	public void setAcceptvalidationList(HashMap<Integer, Integer> acceptvalidationList) {
		this.acceptvalidationList = acceptvalidationList;
	}

	public HashMap<Integer, Integer> getRejectvalidationList() {
		return rejectvalidationList;
	}

	public void setRejectvalidationList(HashMap<Integer, Integer> rejectvalidationList) {
		this.rejectvalidationList = rejectvalidationList;
	}
	
	public void addAcceptvalidationList(Integer name, Integer value)
	{
		this.acceptvalidationList.put(name, value);	
	}
	
	public void addRejectvalidationList(Integer name, Integer value)
	{
		this.rejectvalidationList.put(name, value);	
	}

	public ValidationFilter getVFilter() {
		return vFilter;
	}

}

