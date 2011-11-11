package org.fao.aoscs.client;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.locale.LocaleMessages;
import org.fao.aoscs.client.module.comment.CommentDialog;
import org.fao.aoscs.client.widgetlib.shared.dialog.DialogBoxAOS;
import org.fao.aoscs.domain.UserComments;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CommentDialogBox extends DialogBoxAOS implements ClickHandler{
	
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private static LocaleMessages messages = (LocaleMessages) GWT.create(LocaleMessages.class);
	private VerticalPanel mainpanel = new VerticalPanel();
	private VerticalPanel panel = new VerticalPanel();
	private Button send = new Button(constants.buttonSubmit());
	private Button cancel = new Button(constants.buttonCancel());
	private CommentDialog commentDialog = new CommentDialog();		
	
	public CommentDialogBox(){
		send.addClickHandler(this);
		cancel.addClickHandler(this);
		panel.setSpacing(10);
		panel.add(commentDialog);
		panel.setCellHorizontalAlignment(commentDialog, HasHorizontalAlignment.ALIGN_CENTER);
		this.setHTML(messages.commentPost(History.getToken()));
		panel.setSize("420px", "150px");
		mainpanel.add(panel);
		mainpanel.setCellHorizontalAlignment(panel, HasHorizontalAlignment.ALIGN_CENTER);
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(5);
		buttonPanel.add(send);
		buttonPanel.add(cancel);
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(0);
		hp.setWidth("100%");
		hp.setStyleName("bottombar");
					
		hp.add(buttonPanel);			
		hp.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);
					
		mainpanel.add(hp);					
		mainpanel.setSpacing(0);
		setWidget(mainpanel);
	}
	
	public void onClick(ClickEvent event) {
		Widget sender = (Widget) event.getSource();    
		if(sender.equals(send))
		{
			if(validate())
			{
				UserComments uc = new UserComments();
				uc.setCommentDescription(commentDialog.getComment());
				uc.setModule(History.getToken());
				uc.setName(commentDialog.getName());
				uc.setEmail(commentDialog.getEmail());
				uc.setUserId(MainApp.userId);
				AsyncCallback<String> callback = new AsyncCallback<String>() {
				    public void onSuccess(String success) {
				    	 if(success.equals("true"))
				    	 {
				    		 Window.alert(constants.commentSuccess());
				    	 }
				    	 else
				    		 Window.alert(constants.commentFail());
				    }
				    public void onFailure(Throwable caught) {
				    	Window.alert(constants.commentFail());
				    }
				};
				Service.commentService.sendComment(uc, callback);
				commentDialog.resetForm();
				this.hide();
			}
		}
		else if(sender.equals(cancel))
		{
			commentDialog.resetForm();
			this.hide();	
		}
		
	}
	
	public boolean validate()
	{
		if (commentDialog.getComment().equals("")){
			Window.alert(constants.commentValidate());
			return false;
		}
		else
		{
			return true;
		}
	}


	
}
