package org.fao.aoscs.client.widgetlib.shared.legend;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.classification.Classification;
import org.fao.aoscs.client.module.concept.Concept;
import org.fao.aoscs.client.module.constant.OWLStatusConstants;
import org.fao.aoscs.client.module.constant.TreeItemColor;
import org.fao.aoscs.client.module.relationship.Relationship;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabel;
import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class LegendBar extends AbsolutePanel{

	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	private HTML more;
	private Image moreImage;
	
	private SimplePanel legends;
	private SimplePanel legendsBottom;
	
	private boolean isMoreShown = false;
	
	public LegendBar(){
		this.add(createLegends());
		this.add(createLegendsBottom());
		legendsBottom.setVisible(false);
	}
	
	public SimplePanel createLegends(){
		HorizontalPanel moreButton = createMoreButton();
		
		HTML txtlegend = new HTML(constants.legendTitle()+"&nbsp;");
		txtlegend.addStyleName("legendTitle");
		
		HorizontalPanel legend = new HorizontalPanel();		
		legend.setSpacing(10);		
		legend.add(txtlegend);
		legend.add(createLegendLabel(OWLStatusConstants.PROPOSED_GUEST, TreeItemColor.COLOR_PROPOSED_GUEST));
		legend.add(createLegendLabel(OWLStatusConstants.PROPOSED, TreeItemColor.COLOR_PROPOSED));
		legend.add(createLegendLabel(OWLStatusConstants.VALIDATED, TreeItemColor.COLOR_VALIDATED));
		legend.add(createLegendLabel(OWLStatusConstants.PUBLISHED, TreeItemColor.COLOR_PUBLISHED));
		legend.add(createLegendLabel(OWLStatusConstants.REVISED_GUEST, TreeItemColor.COLOR_REVISED_GUEST));
		legend.add(createLegendLabel(OWLStatusConstants.REVISED, TreeItemColor.COLOR_REVISED));
		legend.add(createLegendLabel(OWLStatusConstants.PROPOSED_DEPRECATED, TreeItemColor.COLOR_PROPOSED_DEPRECATED));
		legend.add(createLegendLabel(OWLStatusConstants.DEPRECATED, TreeItemColor.COLOR_DEPRECATED));
		
		HorizontalPanel top = new HorizontalPanel();
		top.setWidth("100%");		
		top.add(legend);
		top.add(moreButton);
		top.add(new HTML("&nbsp;"));
		top.setCellHorizontalAlignment(legend, HasHorizontalAlignment.ALIGN_LEFT);
		top.setCellHorizontalAlignment(moreButton, HasHorizontalAlignment.ALIGN_RIGHT);
		top.setCellVerticalAlignment(legend, HasVerticalAlignment.ALIGN_MIDDLE);
		top.setCellVerticalAlignment(moreButton, HasVerticalAlignment.ALIGN_MIDDLE);
		
		legends = new SimplePanel();
		legends.add(top);
		return legends;
	}
	
	public SimplePanel createLegendsBottom(){
		HTML txtlegend = new HTML("&nbsp;");
		txtlegend.addStyleName("legendTitle");
		
		Spacer s = new Spacer("70px", "100%");
		
		HorizontalPanel moreLegend = new HorizontalPanel();
		moreLegend.setSpacing(10);
		moreLegend.add(txtlegend);
		moreLegend.add( getMoreLegendItem("images/concept_logo.gif" , constants.legendConcept()) );
		moreLegend.add( getMoreLegendItem("images/term-logo.gif" , constants.legendTerm()) );
		moreLegend.add( getMoreLegendItem("images/relationship-object-logo.gif" , constants.legendRelationshipObjectType()) );
		moreLegend.add( getMoreLegendItem("images/relationship-datatype-logo.gif" , constants.legendRelationshipDataType()) );
		moreLegend.add( getMoreLegendItem("images/category_logo.gif" , constants.legendCategory()) );		
		moreLegend.add( getMoreLegendItem("images/scheme-object-logo.gif" , constants.legendClassification()) );
		moreLegend.setCellWidth(txtlegend, "40px");
		
		HorizontalPanel bottom = new HorizontalPanel();
		bottom.setWidth("100%");		
		bottom.add(moreLegend);
		bottom.add(s);
		bottom.add(new HTML("&nbsp;"));
		bottom.setCellHorizontalAlignment(s, HasHorizontalAlignment.ALIGN_RIGHT);
		bottom.setCellHorizontalAlignment(moreLegend, HasHorizontalAlignment.ALIGN_LEFT);
		bottom.setCellVerticalAlignment(moreLegend, HasVerticalAlignment.ALIGN_MIDDLE);
		
		legendsBottom = new SimplePanel();
		legendsBottom.add(bottom);
		return legendsBottom;
	}
	
	public void toggleLegendDisplay(){
		if(isMoreShown){
			legendsBottom.setVisible(false);
			more.setHTML(constants.legendShowMore());
			moreImage.setUrl("images/popen.png");
		}else{
			legendsBottom.setVisible(true);		
			more.setHTML(constants.legendShowLess());
			moreImage.setUrl("images/pclose.png");
		}
		isMoreShown = !isMoreShown;
		setScrollPanel();
	}
	
	public HorizontalPanel createMoreButton(){
		moreImage = new Image("images/popen.png");
		moreImage.addStyleName("cursor-hand");
		moreImage.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				toggleLegendDisplay();
			}});
		
		more = new HTML(constants.legendShowMore());
		more.addStyleName("cursor-hand");
		more.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				toggleLegendDisplay();
			}
		});
		
		HorizontalPanel moreButton = new HorizontalPanel();
		moreButton.add(more);	
		moreButton.add(moreImage);
		moreButton.setSpacing(5);
		moreButton.setCellVerticalAlignment(more, HasVerticalAlignment.ALIGN_MIDDLE);
		moreButton.setCellVerticalAlignment(moreImage, HasVerticalAlignment.ALIGN_MIDDLE);
		return moreButton;
	}
	
	public HorizontalPanel createLegendLabel(String text, String color)
	{
	    String temp = text;
		HorizontalPanel legend = new HorizontalPanel();
		HTML txt = new HTML();
		txt.addStyleName("legend");
		if(text.equals(OWLStatusConstants.DEPRECATED))
			txt.setHTML("<STRIKE>"+temp+"</STRIKE>");
		else
			txt.setHTML(temp);
		HTML box = createLegendBox(color);
		legend.add(box);
		legend.add(new HTML("&nbsp;"));
		legend.add(txt);
		legend.setCellVerticalAlignment(box, HasVerticalAlignment.ALIGN_MIDDLE);
		return legend;
	}
	
	public HTML createLegendBox(String color)
	{
		HTML box = new HTML("&nbsp;");
		box.setSize("10px", "10px");
		DOM.setStyleAttribute(box.getElement(), "fontSize", "1px");
		DOM.setStyleAttribute(box.getElement(), "backgroundColor", color);
		DOM.setStyleAttribute(box.getElement(), "border", "#666666 1 solid");
		return box;
	}

	public HorizontalPanel getMoreLegendItem(String image, String text){
		HorizontalPanel hp = new HorizontalPanel();
		LinkLabel leg = new LinkLabel(image, text, text);
		leg.setClickable(false);
		hp.add(leg);
		hp.setCellVerticalAlignment(leg, HasVerticalAlignment.ALIGN_MIDDLE);
		hp.setCellWidth(leg, "100%");
		return hp;			
	}
	
	public void setScrollPanel()
	{
		Widget w = ModuleManager.getSelectedMainAppWidget();
		if(w instanceof Concept)
		{
			((Concept) w).conceptTree.setScrollPanelSize();
		}
		else if(w instanceof Classification)
		{
			Classification c = (Classification) w;
			int i = c.scheme.treePanel.getVisibleWidget();
			if(i!=-1)
			{
				Widget wid = c.scheme.treePanel.getWidget(i);
				if(wid instanceof ScrollPanel)
				{
					c.scheme.setScrollPanelSize((ScrollPanel) wid);
				}
			}
		}
		else if(w instanceof Relationship)
		{
			((Relationship) w).relationshipTree.setScrollPanelSize();
		}
	}	
}
