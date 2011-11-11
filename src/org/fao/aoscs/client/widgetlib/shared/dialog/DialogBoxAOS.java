package org.fao.aoscs.client.widgetlib.shared.dialog;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;

public class DialogBoxAOS extends DialogBox implements ClickHandler{
	
	public boolean isLoaded = false;
	
	public DialogBoxAOS(){
		super(false, true);
		this.setGlassEnabled(true);
	}

	@Override
	public void setText(String text)
	{
		if(text.length()>200)
			text = text.substring(0,200)+"...";
		super.setText(text);
	}
	public void onClick(ClickEvent event) {}

	public void show()
	{
		isLoaded = true;
		DialogBoxAOS.this.setVisible(false);
		DialogBoxAOS.super.show();
		Scheduler.get().scheduleDeferred(new Command() {
			public void execute()
			{  
				int left =  Window.getScrollLeft()+ ((Window.getClientWidth( ) - DialogBoxAOS.this.getOffsetWidth( )) / 2); 
				int top =  Window.getScrollTop()+ ((Window.getClientHeight( ) - DialogBoxAOS.this.getOffsetHeight( )) / 2);  
				DialogBoxAOS.this.setPopupPosition(left, top);
				DialogBoxAOS.this.setVisible(true);
			}
        });
		
	}
	@Override
	public void hide()
	{
		isLoaded = false;
		super.hide();
	}
}
