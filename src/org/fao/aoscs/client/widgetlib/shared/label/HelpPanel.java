package org.fao.aoscs.client.widgetlib.shared.label;


import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class HelpPanel extends HorizontalPanel{

    private DecoratedPopupPanel msg = new DecoratedPopupPanel(true);
    private Image icon;

    public HelpPanel(String message)
    {        
        icon = new Image("images/help.png");
        
        this.add(new Spacer("5px", "5px"));         
        this.add(icon);         
        this.setCellHorizontalAlignment(icon, HasHorizontalAlignment.ALIGN_CENTER);
        this.setCellVerticalAlignment(icon, HasVerticalAlignment.ALIGN_MIDDLE);
        msg.add(new HTML(message));
        
        //Mouse Action Handlers
        icon.addClickHandler(new ClickHandler() 
        {
            public void onClick(ClickEvent event) {
                Widget sender = (Widget) event.getSource();
                msg.setPopupPosition(sender.getAbsoluteLeft(), sender.getAbsoluteTop());
                msg.show();
        }});
        
        DOM.setStyleAttribute(this.getElement(), "cursor", "pointer");
        DOM.setStyleAttribute(this.getElement(), "margin", "1px");
    }
}
