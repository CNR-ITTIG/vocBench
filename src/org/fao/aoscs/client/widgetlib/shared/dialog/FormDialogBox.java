package org.fao.aoscs.client.widgetlib.shared.dialog;

import org.fao.aoscs.client.locale.LocaleConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class FormDialogBox extends DialogBoxAOS implements ClickHandler{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	public VerticalPanel panel = new VerticalPanel();
	public Button submit = new Button(constants.buttonSubmit());
	public Button loop = new Button(constants.buttonSubmit());	
	public Button cancel = new Button(constants.buttonCancel());
	private Widget leftBottomWidget = null;
	public boolean withSubmitCancelButton = true;
	public boolean withLoopButton = false;
	
	public FormDialogBox(){
		setWidget(panel);
	}
	public FormDialogBox(String submitButtonLabel){
		this.submit.setText(submitButtonLabel);
		this.cancel.setVisible(false);
		setWidget(panel);
	}
	public FormDialogBox(boolean withSubmitCancelButton){
		this.withSubmitCancelButton = withSubmitCancelButton;
		setWidget(panel);
	}
	public FormDialogBox(String submitButtonLabel, String cancelButtonLabel, boolean withSubmitCancelButton){
		this.submit.setText(submitButtonLabel);
		this.cancel.setText(cancelButtonLabel);
		this.withSubmitCancelButton = withSubmitCancelButton;
		setWidget(panel);
	}
	public FormDialogBox(String submitButtonLabel, String cancelButtonLabel){
		this.submit.setText(submitButtonLabel);
		this.cancel.setText(cancelButtonLabel);
		setWidget(panel);
	}
	public FormDialogBox(String submitButtonLabel, String cancelButtonLabel, String loopButtonLabel){
		this.submit.setText(submitButtonLabel);
		this.loop.setText(loopButtonLabel);
		this.cancel.setText(cancelButtonLabel);
		this.withLoopButton = true;
		setWidget(panel);
	}
	public FormDialogBox(String submitButtonLabel, String cancelButtonLabel, String loopButtonLabel, boolean withSubmitCancelButton)
	{
		this.submit.setText(submitButtonLabel);
		this.loop.setText(loopButtonLabel);
		this.cancel.setText(cancelButtonLabel);
		this.withSubmitCancelButton = withSubmitCancelButton;
		this.withLoopButton = true;
		setWidget(panel);
	}
	public void initLayout(){
	
	}
	public void setWidth(String width){
		panel.setWidth(width);
	}
	public void setHeight(String height){
		panel.setHeight(height);
	}
	public void setLeftBottomWidget(Widget leftBottomWidget){
		this.leftBottomWidget = leftBottomWidget;
	}
	public void addWidget(Widget obj, boolean noSpacing)
	{
		HorizontalPanel tablePanel = new HorizontalPanel();		
		tablePanel.setSpacing(noSpacing? 0 : 10);
		tablePanel.add(obj);
//		tablePanel.setStyleName("dialogMessage");
		tablePanel.setWidth("100%");
		addWidget(tablePanel);	
	}
	
	public void clearWidget(){
		panel.clear();
	}
	
	public void addWidget(Widget obj){
		HorizontalPanel tablePanel = new HorizontalPanel();
//		tablePanel.setStyleName("dialogMessage");
		tablePanel.setSpacing(10);
		tablePanel.add(obj);
		tablePanel.setWidth("100%");
		addWidget(tablePanel);
			
	}
	private void addWidget(HorizontalPanel tablePanel){
	    tablePanel.setStyleName("dialogMessage");
		panel.add(tablePanel);
		panel.setCellHeight(tablePanel, "100%");
		panel.setCellWidth(tablePanel, "100%");	
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(0);
		hp.setWidth("100%");
				
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(5);
		buttonPanel.add(submit);
		if(withLoopButton)
		{
			buttonPanel.add(loop);
		}
		buttonPanel.add(cancel);
				
		hp.add(buttonPanel);
		hp.setVisible(withSubmitCancelButton);
		hp.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);

		submit.addClickHandler(this);
		if(withLoopButton)
		{
			loop.addClickHandler(this);
		}
		cancel.addClickHandler(this);
		
		
		HorizontalPanel bottomPanel = new HorizontalPanel();
		bottomPanel.setWidth("100%");
		if(leftBottomWidget!=null)
		{
			bottomPanel.add(leftBottomWidget);
			bottomPanel.setCellHorizontalAlignment(leftBottomWidget, HasHorizontalAlignment.ALIGN_LEFT);
			bottomPanel.setCellVerticalAlignment(leftBottomWidget, HasVerticalAlignment.ALIGN_MIDDLE);
		}
		bottomPanel.add(hp);
		bottomPanel.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_RIGHT);
		bottomPanel.setStyleName("bottombar");
		
				
		panel.add(bottomPanel);
		panel.setCellHorizontalAlignment(bottomPanel, HasHorizontalAlignment.ALIGN_RIGHT);		
	}
	
	public void show()
	{		
		if(!isLoaded)
		{
			super.show();			
			submit.setEnabled(true);
			if(this.withLoopButton)
			{
				loop.setEnabled(true);
			}
		}
	}
	
	public void onClick(ClickEvent event) 
	{
		Widget sender = (Widget) event.getSource();
		if(sender.equals(submit) || sender.equals(loop))
		{
			if(passCheckInput())
			{
				if(passCheckUri())
				{
					this.hide();
					if(sender.equals(submit))
					{
						submit.setEnabled(false);
						onSubmit();
					}
					else if(sender.equals(loop))
					{
						loop.setEnabled(false);
						onLoopSubmit();
					}
				}
				else
				{
					Window.alert(constants.conceptCorrectURIPrefix());
				}
			}
			else
			{
				Window.alert(constants.conceptCompleteInfo()); 
			}
		}else if(sender.equals(cancel))
		{
			this.hide();			
		}
		onButtonClicked(sender);
	}
	public boolean passCheckInput(){
		return true;
	}
	public boolean passCheckUri(){
		return true;
	}
	
	public void hide(){
		super.hide();		
	}
	
	public void onSubmit(){
		
	}
	
	public void onLoopSubmit(){
		
	}
	public void onButtonClicked(Widget sender){
		
	}
	public Image getWarningImage(){
		return new Image("images/Warning-new.png");
	}
	public HTML getMessage(String message){	    
	    HTML msg = new HTML(message);
        msg.setStyleName("dialogMessage");
	    return msg;	    
	}
	
	
}
