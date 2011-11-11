package org.fao.aoscs.client.module.search.widgetlib;

import org.fao.aoscs.client.widgetlib.shared.label.LinkLabel;
import org.fao.aoscs.domain.NonFuncObject;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

public class NonFuncObjectEle extends HorizontalPanel {
    
    public NonFuncObject obj;
    public LinkLabel delete = new LinkLabel("images/delete-grey.gif", "Remove");
    
    public NonFuncObjectEle(String rel, NonFuncObject obj)
    {
        super();
        this.obj = obj;
        this.add(delete);
        this.setSpacing(5);
        
        this.add(new Image("images/relationship-datatype-logo.gif"));
        this.add(new HTML(rel+"<b>:</b>"));
        this.add(new HTML(obj.getValue()));
        if(obj.getLanguage()!=null)
            this.add(new HTML("&nbsp;("+obj.getLanguage()+")"));
    }    
}
