package org.fao.aoscs.client.widgetlib.Main;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.MenuLabel;

import com.google.gwt.user.client.ui.HorizontalPanel;

public class ToolBarContainer extends HorizontalPanel{
	private MainApp mainAPP;

	public ToolBarContainer(MainApp app) {
		super();				
		mainAPP = app;
	}
	
	public void addMenu(String label, String title, final String module){
		this.add(new MenuLabel(label, title, module, mainAPP));
	}
	
	public void disableMenu(String label, String title, final String module){
		MenuLabel menuLabel = new MenuLabel(label, title, module, mainAPP);
		menuLabel.disable();
		this.add(menuLabel);
	}
	
	public void activate(String module){
		deactivate();
		for(int i=0 ; i < getWidgetCount(); i++){			
			if(((MenuLabel)getWidget(i)).module.equals(module)){
				((MenuLabel)getWidget(i)).activate();
				break;
			}
		}
	}
	
	public void deactivate(){
		for(int i=0 ; i < getWidgetCount(); i++){
			if(((MenuLabel)getWidget(i)).active){
				((MenuLabel)getWidget(i)).deactivate();
			}
		}
	}	
}
