package org.fao.aoscs.client.widgetlib.shared.label;

import java.util.ArrayList;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.module.concept.widgetlib.InfoTab;
import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.client.module.constant.OWLStatusConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.module.constant.TreeItemColor;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.HierarchyObject;
import org.fao.aoscs.domain.TreeObject;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

public class HierarchyAOS extends Composite{
	
	private ConceptObject conceptObject = new ConceptObject();
	VerticalPanel mainPanel = new VerticalPanel();
	HierarchyObject hObj =  new HierarchyObject();
	
	public HierarchyAOS(HierarchyObject hObj, ConceptObject conceptObject){
		this.conceptObject = conceptObject;
		this.hObj = hObj;
		loadHierarchy();
		this.initWidget(mainPanel);
	}
	
	private void addBroader(HPanelAOS parentHPanel)
	{
		addBroader(parentHPanel, 0);
	}
	
	private void addBroader(HPanelAOS parentHPanel, int test)
	{
		ArrayList<TreeObject> list = hObj.getBroaderList().get(parentHPanel.getTreeObject().getUri());
		if(list!=null)
		{
			for(final TreeObject tObj: list)
			{
				boolean addButton = tObj.isHasChild() && list.size()>1;
				HPanelAOS hPanel = createHPanel(tObj, parentHPanel.getStep(), addButton);
				parentHPanel.setChildPanel(hPanel);
			}
			if(parentHPanel.getChildPanel().getWidgetCount()<2)
			{
				for(int i =0;i< parentHPanel.getChildPanel().getWidgetCount();i++)
				{
					addBroader((HPanelAOS)parentHPanel.getChildPanel().getWidget(i), test);
				}
			}
			
		}
	}
	
	private void addNarrower(ArrayList<TreeObject> narrower)
	{
		for(TreeObject tObj: narrower)
		{
			mainPanel.add(createNarrowerLabelPanel(tObj));
		}
	}
	
	private void loadHierarchy()
	{
		HPanelAOS hPanel = new HPanelAOS(hObj.getSelectedConcept(), 0);
		hPanel.setLabelPanel(createLabelPanel(hPanel, false));
		addBroader(hPanel);
		mainPanel.add(hPanel);
		addNarrower(hObj.getNarrowerList());
	}
	
	private HPanelAOS createHPanel(final TreeObject tObj, final int parentStep, boolean addButton)
	{
		int step = parentStep+1;
		final HPanelAOS hPanel = new HPanelAOS(tObj, step);
		HorizontalPanel hp = createLabelPanel(hPanel, addButton);
		hPanel.setLabelPanel(hp);
		return hPanel;
	}
	
	private HorizontalPanel createLabelPanel(HPanelAOS hPanel, boolean addButton)
	{
		HorizontalPanel hp = new HorizontalPanel();
		
		TreeObject tObj = hPanel.getTreeObject();
		int step = hPanel.getStep();
		
		for(int i=0;i<step;i++)
		{
			hp.add(getSpacer(25));
		}
		if(addButton)
		{
			hp.add(getAddButton(hPanel, tObj.getUri()));
			hp.add(getSpacer(14));
		}
		else
		{
			if(step>0)
				hp.add(getSpacer(31));
			else
				hp.add(getSpacer(1));
		}
		HTML label = getLabel(tObj);
		
		if(step==0)
		{
			DOM.setStyleAttribute(label.getElement(), "backgroundColor", "#93C2F1");
			DOM.setStyleAttribute(label.getElement(), "height", "17px");
		}
		hp.add(label);
		
		
		return hp;
	}
	
	private HTML getLabel(final TreeObject tObj)
	{
		HTML label = new HTML(convert2Widget(tObj));
		DOM.setStyleAttribute(label.getElement(), "height", "18px");
		DOM.setStyleAttribute(label.getElement(), "overflow", "hidden");
		label.setTitle(tObj.getLabel());
		label.setStyleName(Style.Link);
		label.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event) {
				ModuleManager.selectModule(tObj.getUri(), conceptObject.getScheme(), true, InfoTab.hierarchy, conceptObject.getBelongsToModule());
			}
			
		});
		return label;
	}
	
	private Spacer getSpacer(int width)
	{
		Spacer s = new Spacer(""+width, "25");
		return s;
	}
	
	private HorizontalPanel createNarrowerLabelPanel(TreeObject tObj)
	{
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(getSpacer(25));
		hp.add(getSpacer(25));
		hp.add(getLabel(tObj));
		return hp;
	}
	
	private Image getAddButton(final HPanelAOS hPanel, final String uri)
	{
			
		final Image img = new Image("images/tree-plus.png");
		img.addClickHandler(new ClickHandler() {

			@SuppressWarnings("unchecked")
			public void onClick(ClickEvent event) {
				AsyncCallback<HierarchyObject> callback = new AsyncCallback<HierarchyObject>(){
					public void onSuccess(HierarchyObject results) {
						
						if(img.getUrl().endsWith("images/tree-plus.png"))
						{
							img.setUrl("images/tree-minus.png");
							addBroader(hPanel, hPanel.getStep());
						}
						else
						{
							img.setUrl("images/tree-plus.png");
							hPanel.getChildPanel().clear();
						}
					}
					public void onFailure(Throwable caught) {
						Window.alert(MainApp.constants.conceptHierarchyLoadFail());
					}
				};
				
				Service.conceptService.getConceptHierarchy(MainApp.userOntology, uri, false, false, MainApp.userSelectedLanguage, callback);
				
			}
			
		});
		return img;
	}
	
	public static String convert2Widget(TreeObject tObj){
		String label = tObj.getLabel();
		if(label.startsWith("###EMPTY###"))
			label = "";
		
		if(label.length()==0){
			label = "<img align='top' src='images/label-not-found.gif'>";
		}
		else
		{
			label = getColorForTreeItem(tObj.getStatus(),label).getHTML();
		}
		if(tObj.getUri().startsWith(ModelConstants.ONTOLOGYBASENAMESPACE)){
			label = "<img align='top' src='images/concept_logo.gif'>&nbsp;<span align='middle'>" + label;
		}
		else{
			label = "<img align='top' src='images/category_logo.gif'>&nbsp;<span align='middle'>" + label+"</span>";
		}
		
		return label;
	}
	
	private static HTML getColorForTreeItem(String status,String label){
		HTML item = new HTML();
		if(status!=null){
			if(status.equals(OWLStatusConstants.DEPRECATED)){
				item.setHTML("<font color=\""+TreeItemColor.COLOR_DEPRECATED+"\">"+"<STRIKE>"+label+"</STRIKE>"+"</font>");
			}else if(status.equals(OWLStatusConstants.VALIDATED)){
				item.setHTML("<font color=\""+TreeItemColor.COLOR_VALIDATED+"\">"+label+"</font>");
			}else if(status.equals(OWLStatusConstants.PUBLISHED)){
				item.setHTML("<font color=\""+TreeItemColor.COLOR_PUBLISHED+"\">"+label+"</font>");
			}else if(status.equals(OWLStatusConstants.PROPOSED_DEPRECATED)){
				item.setHTML("<font color=\""+TreeItemColor.COLOR_PROPOSED_DEPRECATED+"\">"+label+"</font>");
			}else if(status.equals(OWLStatusConstants.REVISED)){
				item.setHTML("<font color=\""+TreeItemColor.COLOR_REVISED+"\">"+label+"</font>");
			}else if(status.equals(OWLStatusConstants.PROPOSED)){
				item.setHTML("<font color=\""+TreeItemColor.COLOR_PROPOSED+"\">"+label+"</font>");
			}else if(status.equals(OWLStatusConstants.PROPOSED_GUEST)){
				item.setHTML("<font color=\""+TreeItemColor.COLOR_PROPOSED_GUEST+"\">"+label+"</font>");
			}else if(status.equals(OWLStatusConstants.REVISED_GUEST)){
				item.setHTML("<font color=\""+TreeItemColor.COLOR_REVISED_GUEST+"\">"+label+"</font>");
			}
		}else{
			item.setHTML(label);
		}
		item.setWordWrap(true);
		if(status != null)	item.setTitle(status);
		return item;
	}
		
}
