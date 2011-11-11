package org.fao.aoscs.client.widgetlib.shared.panel;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Spacer extends VerticalPanel {

    public HTML innerText;

    public Spacer(String width, String height)
    {
        super();
        createSpacer(width, height, null);
    }

    public Spacer(String width, String height, String text)
    {
        super();
        createSpacer(width, height, text);
    }

    public void createSpacer(String width, String height, String text)
    {
        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("100%");
        vp.setHeight("100%");
        if (text != null)
        {
            innerText = new HTML(text); 
            vp.add(innerText);
            vp.setCellHorizontalAlignment(vp.getWidget(0), HasHorizontalAlignment.ALIGN_CENTER);
            vp.setCellVerticalAlignment(vp.getWidget(0), HasVerticalAlignment.ALIGN_MIDDLE);
        }
        this.add(vp);
        this.setCellWidth(vp, width);
        this.setCellHeight(vp, height);
        this.setHeight(height);
        this.setWidth(width);
        this.setCellHorizontalAlignment(vp, HasHorizontalAlignment.ALIGN_CENTER);
        this.setCellVerticalAlignment(vp, HasVerticalAlignment.ALIGN_MIDDLE);
    }
}
