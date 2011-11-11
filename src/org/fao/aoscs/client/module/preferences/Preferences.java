package org.fao.aoscs.client.module.preferences;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.module.preferences.widgetlib.WBPreferences;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;

public class Preferences extends Composite {

	WBPreferences prefWB = new WBPreferences();
	
	public Preferences()
	{
		prefWB.setSize(MainApp.getBodyPanelWidth()+"px", MainApp.getBodyPanelHeight() +"px");
		Window.addResizeHandler(new ResizeHandler()
	    {
	    	public void onResize(ResizeEvent event) {
	    		prefWB.setSize(MainApp.getBodyPanelWidth()+"px", MainApp.getBodyPanelHeight() +"px");
			}
	    });
		
		initWidget(prefWB);
	}
	
	public void savePreference(){
	    prefWB.savePreference();
	}

	public WBPreferences getPrefWB() {
		return prefWB;
	}
	
}
