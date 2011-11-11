package org.fao.aoscs.client.widgetlib.shared.dialog;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.locale.LocaleMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class FlexDialogBox extends DialogBoxAOS implements ClickHandler{
	
	public final HandlerManager handlerManager = new HandlerManager(this);
	protected static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	protected static LocaleMessages messages = (LocaleMessages) GWT.create(LocaleMessages.class);
	public VerticalPanel dialogContainer = new VerticalPanel();
	public Button submit = new Button(constants.buttonSubmit());
	public Button loop = new Button(constants.buttonSubmit());
	public Button cancel = new Button(constants.buttonCancel());
	protected boolean doLoop = false;
	private HorizontalPanel bottomPanel = new HorizontalPanel();
	
	public FlexDialogBox(){
		setWidget(dialogContainer);
	}
	
	public FlexDialogBox(String submitButtonLabel){
		this.submit.setText(submitButtonLabel);
		this.cancel.setVisible(false);
		this.loop.setVisible(false);
		setWidget(dialogContainer);
	}
	
	public FlexDialogBox(String submitButtonLabel, String cancelButtonLabel){
		this.submit.setText(submitButtonLabel);
		this.cancel.setText(cancelButtonLabel);
		this.loop.setVisible(false);
		setWidget(dialogContainer);
	}
	
	public FlexDialogBox(String submitButtonLabel, String cancelButtonLabel, String loopButtonLabel){
		this.submit.setText(submitButtonLabel);
		this.cancel.setText(cancelButtonLabel);
		this.loop.setText(loopButtonLabel);
		setWidget(dialogContainer);
	}
	
	public void initLayout(){
	
	}
	
	public void setWidth(String width){
		dialogContainer.setWidth(width);
	}
	
	public void setHeight(String height){
		dialogContainer.setHeight(height);
	}
	
	public void showLoading(){
		VerticalPanel cpanel = new VerticalPanel();
		cpanel.setSize(dialogContainer.getOffsetWidth()+"px", (dialogContainer.getOffsetHeight())+"px");
		LoadingDialog load = new LoadingDialog();
		cpanel.add(load);
		cpanel.setCellHorizontalAlignment(load, HasHorizontalAlignment.ALIGN_CENTER);
		cpanel.setCellVerticalAlignment(load, HasVerticalAlignment.ALIGN_MIDDLE);
		cpanel.setCellHeight(load, "100%");
		cpanel.setCellWidth(load, "100%");
    	setWidget(cpanel);
	}
    
	public void showTable(){
		setWidget(dialogContainer);	
	}
	
	public void addWidget(Widget obj, boolean noSpacing)
	{
		HorizontalPanel tablePanel = new HorizontalPanel();		
		tablePanel.setSpacing(noSpacing? 0 : 10);
		tablePanel.add(obj);
		tablePanel.setWidth("100%");
		addWidget(tablePanel);	
	}
	
	public void addWidget(Widget obj){
		HorizontalPanel tablePanel = new HorizontalPanel();
		tablePanel.setSpacing(10);
		tablePanel.add(obj);
		tablePanel.setWidth("100%");
		addWidget(tablePanel);
	}
	
	private void addWidget(HorizontalPanel tablePanel){
	    tablePanel.setStyleName("dialogMessage");
		dialogContainer.add(tablePanel);
		dialogContainer.setCellHeight(tablePanel, "100%");
		dialogContainer.setCellWidth(tablePanel, "100%");			
		bottomPanel = createBottomPanel();
		dialogContainer.add(bottomPanel);
		dialogContainer.setCellHorizontalAlignment(bottomPanel, HasHorizontalAlignment.ALIGN_RIGHT);
	}
	
	public HorizontalPanel createBottomPanel(){
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(5);
		buttonPanel.add(submit);
		buttonPanel.add(loop);
		buttonPanel.add(cancel);

		submit.addClickHandler(this);
		loop.addClickHandler(this);
		cancel.addClickHandler(this);
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(0);
		hp.setWidth("100%");
		hp.add(buttonPanel);
		hp.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);

		HorizontalPanel bottomPanel = new HorizontalPanel();
		bottomPanel.setWidth("100%");
		bottomPanel.add(hp);
		bottomPanel.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_RIGHT);
		bottomPanel.setStyleName("bottombar");				
		return bottomPanel;			
	}
	
	public void show(){		
		if(!isLoaded)
		{
			super.show();			
			submit.setEnabled(true);
			loop.setEnabled(true);
			cancel.setEnabled(true);
		}
	}
	
	public void hide()
	{		
		super.hide();			
	}
	
	public void onClick(ClickEvent event) 
	{
		Widget sender = (Widget) event.getSource();
		if(sender.equals(submit)){
			if(passCheckInput()){
				handlerManager.fireEvent(new FlexDialogSubmitClickedEvent(event));
				onSubmit();
			}
			else{
				Window.alert(constants.conceptCompleteInfo()); 
			}
		}
		else if(sender.equals(loop))
		{
			if(passCheckInput()){
				handlerManager.fireEvent(new FlexDialogLoopClickedEvent(event));
				onLoopSubmit();
			}
			else{
				Window.alert(constants.conceptCompleteInfo()); 
			}
		}
		else if(sender.equals(cancel)){
			handlerManager.fireEvent(new FlexDialogCancelClickedEvent(event));
			this.onCancel();			
		}
	}
	
	public boolean passCheckInput(){
		return true;
	}
	
	public void onSubmit(){
		
	}
	
	public void onLoopSubmit(){
		
	}
	
	public void onCancel(){
		super.hide();		
	}
	
	public void addFlexDialogClickedHandler(FlexDialogClickedHandler handler) {
		handlerManager.addHandler(FlexDialogSubmitClickedEvent.getType(),handler);
		handlerManager.addHandler(FlexDialogLoopClickedEvent.getType(),handler);
		handlerManager.addHandler(FlexDialogCancelClickedEvent.getType(),handler);
	}
	
	// Submit button clicked event
	public static class FlexDialogSubmitClickedEvent extends GwtEvent<FlexDialogClickedHandler> {
		ClickEvent event;
		private static final Type<FlexDialogClickedHandler> TYPE = new Type<FlexDialogClickedHandler>();
		public FlexDialogSubmitClickedEvent(ClickEvent event) {
			this.event = event;
		}
		public static Type<FlexDialogClickedHandler> getType() {
			return TYPE;
		}
		@Override
		protected void dispatch(FlexDialogClickedHandler handler) {
			handler.onFlexDialogSubmitClicked(event);
		}
		@Override
		public com.google.gwt.event.shared.GwtEvent.Type<FlexDialogClickedHandler> getAssociatedType() {
			return TYPE;
		}
	}
	
	// Loop button clicked event
	static class FlexDialogLoopClickedEvent extends GwtEvent<FlexDialogClickedHandler> {
		ClickEvent event;
		private static final Type<FlexDialogClickedHandler> TYPE = new Type<FlexDialogClickedHandler>();
		public FlexDialogLoopClickedEvent(ClickEvent event) {
			this.event = event;
		}
		public static Type<FlexDialogClickedHandler> getType() {
			return TYPE;
		}
		@Override
		protected void dispatch(FlexDialogClickedHandler handler) {
			handler.onFlexDialogLoopClicked(event);
		}
		@Override
		public com.google.gwt.event.shared.GwtEvent.Type<FlexDialogClickedHandler> getAssociatedType() {
			return TYPE;
		}
	}
	
	// Cancel button clicked event
	static class FlexDialogCancelClickedEvent extends GwtEvent<FlexDialogClickedHandler> {
		ClickEvent event;
		private static final Type<FlexDialogClickedHandler> TYPE = new Type<FlexDialogClickedHandler>();
		public FlexDialogCancelClickedEvent(ClickEvent event) {
			this.event = event;
		}
		public static Type<FlexDialogClickedHandler> getType() {
			return TYPE;
		}
		@Override
		protected void dispatch(FlexDialogClickedHandler handler) {
			handler.onFlexDialogCancelClicked(event);
		}
		@Override
		public com.google.gwt.event.shared.GwtEvent.Type<FlexDialogClickedHandler> getAssociatedType() {
			return TYPE;
		}
	}
	
	// // Button clicked event handler
	public interface FlexDialogClickedHandler extends EventHandler {
		void onFlexDialogSubmitClicked(ClickEvent event);
		void onFlexDialogLoopClicked(ClickEvent event);
		void onFlexDialogCancelClicked(ClickEvent event);
	}
}


