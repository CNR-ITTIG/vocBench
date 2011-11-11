package org.fao.aoscs.client.widgetlib.Main;

import org.fao.aoscs.client.locale.LocaleConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Partner extends VerticalPanel{
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);

	public Partner() {
		super();
		 		 
		HTML ackTitle = new HTML(constants.mainPageTitle()+":");
		DOM.setStyleAttribute(ackTitle.getElement(), "paddingTop", "10px");
		ackTitle.setStyleName("ack-title");
		 
		AcknowledgementWidget ackFao   = new AcknowledgementWidget("images/logo_fao.jpg","Food and Agriculture Organization of the United Nations","Food and Agriculture Organization of the United Nations","for a world without hunger","http://www.fao.org");
		AcknowledgementWidget ackKu    = new AcknowledgementWidget("images/logo_ku.jpg","Kasetsart University","Kasetsart University","Bangkok, Thailand","http://www.ku.ac.th");
		AcknowledgementWidget ackAgris = new AcknowledgementWidget("images/logo_agris.jpg","Thai National Agris Center","Thai National Agris Center","Bangkok, Thailand","http://thaiagris.lib.ku.ac.th/");
		AcknowledgementWidget ackMimos 	= new AcknowledgementWidget("images/mimos_logo_big.jpg","MIMOS Berhad","MIMOS Berhad","Kuala Lumpur, Malaysia","http://www.mimos.my/");
		
		HTML contrib = new HTML(constants.mainAck1());
		contrib.setStyleName("ackLabel");
		contrib.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
				 
		HTML ack = new HTML(constants.mainAck2());
		ack.setStyleName("ackLabel");
		ack.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		 
		AcknowledgementWidget ackIcrisat = new AcknowledgementWidget("images/logo_icrisat.png","ICRISAT","ICRISAT","International Crops Research Institute for the Semi-Arid Tropics","http://www.icrisat.org" );
		AcknowledgementWidget ackProf = new AcknowledgementWidget(null,null,"Prof. Dagobert Soergel","UNIVERSITY OF MARYLAND",null);		
				
		VerticalPanel vp = new VerticalPanel();
		vp.setSpacing(10);
		DOM.setStyleAttribute(vp.getElement(), "paddingLeft", "60px");	
		vp.add(ackTitle);
		vp.add(ackFao);			
		vp.add(ackKu);
		vp.add(ackAgris);
		vp.add(ackMimos);
		vp.add(ack);		
		vp.add(ackIcrisat);
		vp.add(contrib);				
		vp.add(ackProf);		
		DOM.setStyleAttribute(ackFao.getElement(), "paddingLeft", "10px");
		DOM.setStyleAttribute(ackKu.getElement(), "paddingLeft", "10px");
		DOM.setStyleAttribute(ackAgris.getElement(), "paddingLeft", "10px");
		DOM.setStyleAttribute(ackMimos.getElement(), "paddingLeft", "10px");
		DOM.setStyleAttribute(ackIcrisat.getElement(), "paddingLeft", "10px");
		DOM.setStyleAttribute(ackProf.getElement(), "paddingLeft", "10px");
		this.add(vp);		
	}	
}
