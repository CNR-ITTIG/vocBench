package org.fao.aoscs.client.module.system;

import java.util.ArrayList;
import java.util.Iterator;

import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.widgetlib.shared.dialog.FlexDialogBox;
import org.fao.aoscs.domain.OwlAction;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.PermissionFunctionalityMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AddGroupActionsDialog extends FlexDialogBox implements ClickHandler {
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	private VerticalPanel container = new VerticalPanel();
	
	private HorizontalPanel addActionContainer = new HorizontalPanel();
	private ListBox actionList = new ListBox();
	private VerticalPanel statusList = new VerticalPanel();
	private String groupId;
	public String actionId;
	private RadioButton selectall = new RadioButton("allOpt" , constants.buttonSelectAll());
	private RadioButton clearall = new RadioButton("allOpt" , constants.buttonClearAll());
	
	public AddGroupActionsDialog(final String gid, String aid) {
		super(constants.buttonAdd(), constants.buttonCancel(), constants.buttonAddAgain());
		this.groupId = gid;
		setText(constants.groupAddAction());

		// create UI for action-status input
		createActionInput();
		createStatusInput();
		
		container.setCellHorizontalAlignment(actionList, HasHorizontalAlignment.ALIGN_LEFT);
		container.setCellHorizontalAlignment(statusList, HasHorizontalAlignment.ALIGN_LEFT);
		container.setSpacing(10);
		container.setWidth("100%");
	
		addWidget(container);
		
		// action list select handler
		actionList.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				ListBox b = (ListBox)event.getSource();
				final String action = b.getValue(b.getSelectedIndex());
				if(action.equals("-1"))
					statusList.clear();
				else
					initStatusList(action, groupId);
			}
		});
		initActionList(this.groupId, aid);
	}
	
	public HorizontalPanel createButtonPanel(){
		HorizontalPanel hp = super.createBottomPanel();
		
		HorizontalPanel radioPanel = new HorizontalPanel();
		radioPanel.add(selectall);
		radioPanel.add(clearall);
		
		hp.insert(radioPanel, 0);
		hp.setCellVerticalAlignment(radioPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		
		selectall.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				int wCount = statusList.getWidgetCount();
				for(int i=0; i<wCount; i++){
					CheckBox cb = (CheckBox)statusList.getWidget(i);							
					cb.setValue(true);
				}
			}
		});
		
		clearall.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {			
				int wCount = statusList.getWidgetCount();
				for(int i=0; i<wCount; i++){
					CheckBox cb = (CheckBox)statusList.getWidget(i);							
					cb.setValue(false);
				}
			}
			
		});
		return hp;
	}
	
	private void createActionInput(){
		addActionContainer.add(actionList);
		addActionContainer.add(statusList);
		addActionContainer.setSpacing(3);
		addActionContainer.setCellVerticalAlignment(actionList, HasVerticalAlignment.ALIGN_MIDDLE);
		addActionContainer.setCellVerticalAlignment(statusList, HasVerticalAlignment.ALIGN_MIDDLE);
		DOM.setStyleAttribute(addActionContainer.getElement(), "border", "1px solid #BBCDF3");
		DOM.setStyleAttribute(addActionContainer.getElement(), "backgroundColor", "#EEEEEE");
		addActionContainer.setWidth("300px");
		container.add(addActionContainer);
	}
	
	private void createStatusInput(){
		container.add(statusList);
	}
	
	private void initActionList(final String groupId, final String aid){
		AsyncCallback<ArrayList<OwlAction>> callback = new AsyncCallback<ArrayList<OwlAction>>() {	
			public void onSuccess(ArrayList<OwlAction> tmp) {
			    Iterator<OwlAction> it = tmp.iterator();
			    actionList.addItem("--"+constants.buttonSelect()+"--", "-1");
			    int selectedIndex = 0;
			    int index = 1;
				while(it.hasNext()){
					OwlAction oa = (OwlAction)it.next();
					String label = oa.getAction() + (oa.getActionChild().length()>0? "-"+oa.getActionChild() : "");
					actionList.addItem(label, ""+oa.getId());
					if(aid != null){
						if(aid.equals(""+oa.getId()))
							selectedIndex = index;
					}
					index++;
				}
				center();
				actionList.setSelectedIndex(selectedIndex);
				if(selectedIndex > 0)
					initStatusList(aid, groupId);
			}

			public void onFailure(Throwable caught) {
				Window.alert(constants.groupUserListFail());
			}
		};
		
		Service.systemService.getUnassignedActions(groupId, callback);
	}
	
	private void initStatusList(final String action, String groupId){
		AsyncCallback<ArrayList<OwlStatus>> callback = new AsyncCallback<ArrayList<OwlStatus>>() {	
			public void onSuccess(ArrayList<OwlStatus> tmp) {
				Iterator<OwlStatus> it = tmp.iterator();
				statusList.clear();
				while(it.hasNext()){
					OwlStatus os = (OwlStatus)it.next();
					String label = os.getStatus();
					CheckBox cb = new CheckBox(label);
					cb.setFormValue(""+os.getId());
					cb.addClickHandler(new ClickHandler(){
						public void onClick(ClickEvent arg0) {
							if(((CheckBox)arg0.getSource()).getValue())
								clearall.setValue(false);
							else
								selectall.setValue(false);
						}
					});
					statusList.add(cb);
				}
				center();
			}
			public void onFailure(Throwable caught) {
				Window.alert(constants.groupUserListFail());
			}
		};
		Service.systemService.getUnassignedActionStatus(groupId, action, callback);
	}

	public boolean passCheckInput(){
		boolean ret = false;
		int wCount = statusList.getWidgetCount();
		for(int i=0; i<wCount-1; i++){
			CheckBox cb = (CheckBox)statusList.getWidget(i);
			if(cb.getValue())
				ret = true;
		}
		
		if(ret && actionList.getSelectedIndex() > 0)
			ret = true;
		else 
			ret = false;
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public void onSubmit(){
		int wCount = statusList.getWidgetCount();
		ArrayList<PermissionFunctionalityMap> map = new ArrayList<PermissionFunctionalityMap>();
		actionId = actionList.getValue(actionList.getSelectedIndex());
		for(int i=0; i<wCount-1; i++){
			CheckBox cb = (CheckBox)statusList.getWidget(i);
			if(cb.getValue()){
				PermissionFunctionalityMap item = new PermissionFunctionalityMap();
				item.setFunctionId(Integer.parseInt(actionId));
				item.setGroupId(Integer.parseInt(this.groupId));
				item.setStatus(Integer.parseInt(cb.getFormValue()));
				map.add(item);
			}
		}
		
		AsyncCallback<Object> callback = new AsyncCallback<Object>(){	
			public void onSuccess(Object result){
				if(doLoop){
					doLoop = false;
					resetDialog();
					show();
				}
				else
					hide();
				handlerManager.fireEvent(new FlexDialogSubmitClickedEvent(null));
			}
			public void onFailure(Throwable caught) {
				Window.alert(constants.groupUserListFail());
			}
		};
		Service.systemService.addActionToGroup(map, callback);
	}
	
	public void onLoopSubmit(){
		doLoop = true;
		onSubmit();
	}
	
	public void resetDialog(){
		initActionList(this.groupId, "");
		statusList.clear();
		loop.setEnabled(true);
	}
}