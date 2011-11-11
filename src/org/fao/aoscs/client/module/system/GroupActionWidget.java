package org.fao.aoscs.client.module.system;

import java.util.ArrayList;

import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.system.ActionStatusList.ActionStatusListEventHandler;
import org.fao.aoscs.client.module.system.StatusItem.StatusItemDeleteEventHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GroupActionWidget extends HorizontalPanel{

	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	public ListBox actions = new ListBox();
	private ActionStatusList status = new ActionStatusList();
	private String groupId = null;

	public GroupActionWidget() {
		super();
		actions.setVisibleItemCount(16);
		actions.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				if(groupId != null)
					initActionStatusList(groupId, ""+actions.getValue(actions.getSelectedIndex()));
			}
		});
		actions.setWidth("100%");
		add(actions);
		add(status);
		setCellWidth(actions, "60%");
		setCellWidth(status, "40%");
		setCellHorizontalAlignment(actions, HasHorizontalAlignment.ALIGN_LEFT);
		setCellHorizontalAlignment(status, HasHorizontalAlignment.ALIGN_LEFT);
		status.onActionStatusListEventHandler(new ActionStatusListEventHandler(){
			public void onActionStatusListEvent(int param) {
				if(param==1){ // remove action
					initActionList(groupId);
				}
			}
		});
	}
	
	/**
	 * Initialize action ListBox with actions assigned to a group
	 * @param groupId
	 */
	public void initActionList(String groupId){
		this.groupId = groupId;
		status.resetWidget();
		AsyncCallback<ArrayList<String[]>> groupcallback = new AsyncCallback<ArrayList<String[]>>() 
		{
			public void onSuccess(ArrayList<String[]> tmp) 
			{
				actions.clear();
				for(int i=0;i<tmp.size();i++){
					String[] item = (String[]) tmp.get(i);
					String action = item[1].toUpperCase();						
					String child = item[2].toUpperCase();					
					String val = child.length()>0? action +"_"+child : action;
					actions.addItem(val,item[0]);
				}
			}
			public void onFailure(Throwable caught) {
				Window.alert(constants.groupLoadGroupsFail());
			}
		};
		String sqlStr = "SELECT oa.id, oa.action, oa.action_child FROM permission_functionality_map pfm, owl_action oa WHERE pfm.function_id = oa.id AND pfm.group_id = " + groupId + " GROUP BY pfm.function_id";
		Service.queryService.execHibernateSQLQuery(sqlStr, groupcallback);
	}
	
	/**
	 * Get status permissions for a group and action
	 * @param groupId
	 * @param actionId
	 */
	public void initActionStatusList(String groupId, String actionId){
		status.initStatus(groupId, actionId);
	}

	public String getSelectedAction(){
		if(actions.getSelectedIndex() > -1){
			return actions.getValue(actions.getSelectedIndex());
		}
		else
			return null;
	}
}


/**
 * Widget to hold list of status permissions for an action	 
 */
class ActionStatusList extends VerticalPanel{

	public final HandlerManager handlerManager = new HandlerManager(this);
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private String gId;
	private String aId;
	
	public ActionStatusList() {
		super();
		setWidth("100%");			
	}
	
	public void initStatus(String groupId, String actionId){
		gId = groupId;
		aId = actionId;
		resetWidget();
		AsyncCallback<ArrayList<String[]>> groupcallback = new AsyncCallback<ArrayList<String[]>>() 
		{
			public void onSuccess(ArrayList<String[]> tmp) 
			{
				if(tmp.size()>0){
					for(int i=0;i<tmp.size();i++){
						String[] item = (String[]) tmp.get(i);
						int status_id = Integer.parseInt(item[0]);						
						String status = item[1].toUpperCase();
						
						StatusItem sItem = new StatusItem(status, gId, aId, ""+status_id);
						// when the StatusItem is deleted
						sItem.addStatusItemDeleteEventHandler(new StatusItemDeleteEventHandler(){
							public void onStatusItemDeleteEvent() {
								initStatus(gId, aId);
							}
						});
						add(sItem);
						setCellHorizontalAlignment(sItem, HasHorizontalAlignment.ALIGN_LEFT);
					}
					setVisible(true);
				}
				else{
					handlerManager.fireEvent(new ActionStatusListEvent(1));
				}
			}
			public void onFailure(Throwable caught) {
				Window.alert(constants.groupLoadGroupsFail());
			}
		};
		// get unassigned status for given action and group
		String sqlStr = "SELECT os.id, os.status from permission_functionality_map pfm LEFT JOIN owl_status os on pfm.status = os.id where group_id = " + groupId + " and function_id = " + actionId;
		Service.queryService.execHibernateSQLQuery(sqlStr, groupcallback);
	}
	
	/**
	 * Clear all status permissions from widget 
	 */
	public void resetWidget(){
		setVisible(false);
		clear();
	}
	
	public void onActionStatusListEventHandler(ActionStatusListEventHandler handler) {
		handlerManager.addHandler(ActionStatusListEvent.getType(), handler);
	}

	// general purpose event
	static class ActionStatusListEvent extends GwtEvent<ActionStatusListEventHandler> {
		int param;
		private static final Type<ActionStatusListEventHandler> TYPE = new Type<ActionStatusListEventHandler>();
		public ActionStatusListEvent(int param) {
			this.param = param;
		}
		public static Type<ActionStatusListEventHandler> getType() {
			return TYPE;
		}
		@Override
		protected void dispatch(ActionStatusListEventHandler handler) {
			handler.onActionStatusListEvent(param);
		}
		@Override
		public com.google.gwt.event.shared.GwtEvent.Type<ActionStatusListEventHandler> getAssociatedType() {
			return TYPE;
		}
	}
	
	// Button clicked event handler
	public interface ActionStatusListEventHandler extends EventHandler {
		void onActionStatusListEvent(int param);
	}
}

class StatusItem extends HorizontalPanel{

	final protected HandlerManager handlerManager = new HandlerManager(this);
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private HTML text;
	private Image icon = new Image("images/delete-grey.gif");
	
	private String groupId;
	private String actionId;
	private String statusId;
	
	public StatusItem(String label, String gId, String aId, String sId){
		super();
		groupId = gId;
		actionId = aId;
		statusId = sId;
		text = new HTML(label);
		add(icon);
		add(text);
		setWidth("100%");
		setCellWidth(icon, "20px");
		setCellHorizontalAlignment(text, HasHorizontalAlignment.ALIGN_LEFT);
		setCellHorizontalAlignment(icon, HasHorizontalAlignment.ALIGN_LEFT);
		setCellVerticalAlignment(text, HasVerticalAlignment.ALIGN_MIDDLE);
		setCellVerticalAlignment(icon, HasVerticalAlignment.ALIGN_MIDDLE);
		setSpacing(3);
		
		this.icon.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				AsyncCallback<Object> callback = new AsyncCallback<Object>() 
				{
					public void onSuccess(Object result) {
						handlerManager.fireEvent(new StatusItemDeleteEvent());
					}
					public void onFailure(Throwable caught) {
						Window.alert(constants.groupLoadGroupsFail());
					}
				};
				Service.systemService.removeActionFromGroup(groupId, actionId, statusId, callback);			
			}
		});
	}
	
	public void addStatusItemDeleteEventHandler(StatusItemDeleteEventHandler handler){
		handlerManager.addHandler(StatusItemDeleteEvent.getType() ,handler);
	}
	
	static class StatusItemDeleteEvent extends GwtEvent<StatusItemDeleteEventHandler> {
		private static final Type<StatusItemDeleteEventHandler> TYPE = new Type<StatusItemDeleteEventHandler>();
		
		public StatusItemDeleteEvent() {
		}
		public static Type<StatusItemDeleteEventHandler> getType() {
			return TYPE;
		}
		@Override
		protected void dispatch(StatusItemDeleteEventHandler handler) {
			handler.onStatusItemDeleteEvent();
		}
		@Override
		public com.google.gwt.event.shared.GwtEvent.Type<StatusItemDeleteEventHandler> getAssociatedType() {
			return TYPE;
		}
	}

	public interface StatusItemDeleteEventHandler extends EventHandler {
		void onStatusItemDeleteEvent();
	}
}