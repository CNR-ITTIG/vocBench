package org.fao.aoscs.client;

import java.util.LinkedHashMap;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.utility.HelpUtility;
import org.fao.aoscs.client.widgetlib.shared.label.MenuBarAOS;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class AboutVocBenchMenu extends HorizontalPanel{
	
	public static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private PartnersDialogBox partnersDialog;
	
	public  AboutVocBenchMenu()
    {
        // FULL MENU ITEM OF USER
    	LinkedHashMap<String, String> user = new LinkedHashMap<String, String>();
        user.put("Help", constants.mainHelp());
        user.put("ContactUs", constants.footerContactUs());
        user.put("Partners", constants.footerPartners());

        MenuBarAOS aboutTab = new MenuBarAOS();
        aboutTab.addMenuItem(constants.mainHelp(), false, true, new Command() {
            public void execute()
            {
            	HelpUtility.openHelp(History.getToken());
            }
        });
        
        aboutTab.addMenuItem(constants.footerContactUs(), false, true, new Command() {
            public void execute()
            {
            	Main.openURL(ConfigConstants.CONTACTUS, "_help");
            }
        });
        
        aboutTab.addMenuItem(constants.footerPartners(), false, true, new Command() {
            public void execute()
            {
            	if (partnersDialog == null || !partnersDialog.isLoaded){
    			    partnersDialog = new PartnersDialogBox();
    			    partnersDialog.setWidth("150px");
    			}
    			partnersDialog.show();
            }
        });
        
        HorizontalPanel aboutPanel = new HorizontalPanel();
        {
            aboutTab.setMainLabel(constants.toolbarHomeTitle());
            aboutPanel.add(aboutTab);
            aboutPanel.setCellVerticalAlignment(aboutTab, HasVerticalAlignment.ALIGN_MIDDLE);
            aboutPanel.setCellHorizontalAlignment(aboutTab, HasHorizontalAlignment.ALIGN_LEFT);
        }
        this.add(aboutPanel);
    }
}
