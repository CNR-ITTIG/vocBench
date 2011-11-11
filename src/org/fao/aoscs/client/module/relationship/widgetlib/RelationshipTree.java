package org.fao.aoscs.client.module.relationship.widgetlib;

import java.util.ArrayList;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.locale.LocaleMessages;
import org.fao.aoscs.client.module.classification.widgetlib.OlistBox;
import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.client.module.constant.OWLActionConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.label.LabelAOS;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabel;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabelAOS;
import org.fao.aoscs.client.widgetlib.shared.legend.LegendBar;
import org.fao.aoscs.client.widgetlib.shared.tree.DisplayLanguage;
import org.fao.aoscs.client.widgetlib.shared.tree.LazyLoadingTree;
import org.fao.aoscs.client.widgetlib.shared.tree.SearchTree;
import org.fao.aoscs.client.widgetlib.shared.tree.TreeAOS;
import org.fao.aoscs.client.widgetlib.shared.tree.TreeItemAOS;
import org.fao.aoscs.domain.InitializeRelationshipData;
import org.fao.aoscs.domain.PermissionObject;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.RelationshipTreeObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.FastTree;

public class RelationshipTree extends Composite{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private LocaleMessages messages = (LocaleMessages) GWT.create(LocaleMessages.class);
	private HorizontalPanel rootPanelHp = new HorizontalPanel();
	private HorizontalSplitPanel hSplit = new HorizontalSplitPanel();
	private HorizontalPanel dPanel = new HorizontalPanel();
	private VerticalPanel panel = new VerticalPanel();
	public InitializeRelationshipData initData;
	private OlistBox relationshipTypeListbox;
	private RelationshipObject selectedRelationshipObject;
	private DeckPanel treePanel;
	private Header head;
	private ScrollPanel scObj = new ScrollPanel();
	private ScrollPanel scData = new ScrollPanel();
	private TreeAOS objectTree = new TreeAOS(TreeAOS.TYPE_RELATIONSHIP);
	private TreeAOS dataTypeTree = new TreeAOS(TreeAOS.TYPE_RELATIONSHIP);
	private RelationshipDetailTab detailTab;
	public  PermissionObject permissionTable;
	private CheckBox showURI = new CheckBox(Convert.replaceSpace(constants.relShowUri()), true);
	private LabelAOS uriTb = new LabelAOS();
	private HorizontalPanel getURIPanel = uriPanel();
	private DecoratedPopupPanel allConceptText = new DecoratedPopupPanel(true);
	
	public RelationshipTree(InitializeRelationshipData initData, PermissionObject permissionTable){
		this.initData = initData;
		this.permissionTable = permissionTable;
		detailTab = new RelationshipDetailTab(RelationshipTree.this, permissionTable, initData);
		
		initLayout(initData.getRelationshipTree(),null,null);
        
		hSplit.setSplitPosition("100%");
		hSplit.ensureDebugId("cwHorizontalSplitPanel");
        hSplit.setSplitPosition("100%");
        hSplit.setLeftWidget(panel);
        hSplit.setRightWidget(detailTab);
        
        HorizontalPanel legend = new HorizontalPanel();
        legend.addStyleName("bottombar");
        legend.setSize("100%", "100%");
        legend.add(new LegendBar());

        final VerticalPanel bodyPanel = new VerticalPanel();
        bodyPanel.setSize("100%", "100%");
        bodyPanel.add(hSplit);
        bodyPanel.add(legend);
        bodyPanel.setCellHeight(hSplit, "100%");
        bodyPanel.setCellWidth(hSplit, "100%");
        
        bodyPanel.setSize(MainApp.getBodyPanelWidth()  - 20+"px", MainApp.getBodyPanelHeight() - 30+"px");
        hSplit.setSize("100%", "100%");
        Window.addResizeHandler(new ResizeHandler(){
            public void onResize(ResizeEvent event) {
                bodyPanel.setSize(MainApp.getBodyPanelWidth() - 20+"px", MainApp.getBodyPanelHeight() - 30+"px");               
            }
        });
        
        dPanel.add(bodyPanel);
        dPanel.setStyleName("borderbar");
        
        rootPanelHp.clear();
        rootPanelHp.setSize("100%", "100%");
        rootPanelHp.add(dPanel);
        rootPanelHp.setCellWidth(dPanel, "100%");
        rootPanelHp.setCellHeight(dPanel, "100%");
        rootPanelHp.setSpacing(5);
        rootPanelHp.setSize("100%", "100%");
        rootPanelHp.setCellHorizontalAlignment(dPanel, HasHorizontalAlignment.ALIGN_CENTER);
        rootPanelHp.setCellVerticalAlignment(dPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		initWidget(rootPanelHp);
	}
	
	public void setDisplayLanguage(ArrayList<String> language){
		if(relationshipTypeListbox.getItemCount()>0)
		{
			DisplayLanguage.executeRelation(objectTree, language, (RelationshipTreeObject) relationshipTypeListbox.getObject(relationshipTypeListbox.getSelectedIndex()));
			DisplayLanguage.executeRelation(dataTypeTree, language, (RelationshipTreeObject) relationshipTypeListbox.getObject(relationshipTypeListbox.getSelectedIndex()));
		}
	}
	
	public void showLoading(){
	    hSplit.setSplitPosition("100%");
        detailTab.setVisible(false);
        panel.clear();
		panel.setSize("100%", "100%");
		LoadingDialog sayLoading = new LoadingDialog();
		panel.add(sayLoading);
		panel.setCellHorizontalAlignment(sayLoading, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(sayLoading, HasVerticalAlignment.ALIGN_MIDDLE);
		panel.setCellHeight(sayLoading, "100%");
		panel.setCellWidth(sayLoading, "100%");
	}
	public TreeItemAOS getSelectedItem(){
		ScrollPanel sc = (ScrollPanel) treePanel.getWidget(relationshipTypeListbox.getSelectedIndex());
		FastTree tree=(FastTree) sc.getWidget();
		return (TreeItemAOS) tree.getSelectedItem();
	}
	private class Header extends Composite{
		private VerticalPanel panel = new VerticalPanel();
		private HorizontalPanel btnHp = new HorizontalPanel();
		
		private AddNewRelationship addNewRelationship; 
		private DeleteRelationship deleteRelationship; 
		
		public Header(final OlistBox relationshipListbox,DeckPanel treePanel){
			btnHp.setVisible(false);
			panel.add(makeTopLayer(relationshipListbox, treePanel, btnHp, showURI));
			panel.add(getURIPanel);
			panel.setWidth("100%");
			showFunctionalPanel(false);
			initWidget(panel);
		}
		
		private TreeItemAOS getSelectedItem(OlistBox relationshipListbox,DeckPanel treePanel){
			ScrollPanel sc = (ScrollPanel) treePanel.getWidget(relationshipListbox.getSelectedIndex());
			FastTree tree = (FastTree) sc.getWidget();
			return (TreeItemAOS) tree.getSelectedItem();
		}
		
		private RelationshipObject getParentOfSelectedItem(OlistBox relationshipListbox,DeckPanel treePanel){
			TreeItemAOS item = getSelectedItem(relationshipListbox, treePanel);
			if(item != null ){
				TreeItemAOS parentItem = (TreeItemAOS) item.getParentItem();
				if(parentItem !=null){
					RelationshipObject parent = (RelationshipObject) parentItem.getValue();
					return parent;
				}else{
					RelationshipObject parent = new RelationshipObject();
					if(relationshipListbox.getSelectedIndex()==0){
						parent.setUri(ModelConstants.COMMONBASENAMESPACE+ModelConstants.RWBOBJECTPROPERTY);
						parent.addLabel(ModelConstants.RWBOBJECTPROPERTY, "en");
						parent.setType(RelationshipObject.OBJECT);
						parent.setName(ModelConstants.RWBOBJECTPROPERTY);
					}else{
						parent.setUri(ModelConstants.COMMONBASENAMESPACE+ModelConstants.RWBDATATYPEPROPERTY);
						parent.addLabel(ModelConstants.RWBDATATYPEPROPERTY, "en");
						parent.setType(RelationshipObject.DATATYPE);
						parent.setName(ModelConstants.RWBDATATYPEPROPERTY);
					}
					//parent.setParent("");
					return parent;
				}
			}else{
				return null;
			}
		}
		public void onPropertyTypeListBoxChange(int index){
			treePanel.showWidget(index);
		}
		private HorizontalPanel makeTopLayer(final OlistBox relationshipListbox,final DeckPanel treePanel,HorizontalPanel btnHp,final CheckBox showURI){

			boolean permission = permissionTable.contains(OWLActionConstants.RELATIONSHIPCREATE, -1);
			LinkLabelAOS add = new LinkLabelAOS("images/add-grey.gif", "images/add-grey-disabled.gif", constants.relAddNewRelationship(), permission, new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(addNewRelationship == null || !addNewRelationship.isLoaded)
						addNewRelationship = new AddNewRelationship((RelationshipObject)((TreeItemAOS)getSelectedItem(relationshipListbox,treePanel)).getValue() ,getParentOfSelectedItem(relationshipListbox,treePanel));
					addNewRelationship.show();
				}
			});
			
			permission = permissionTable.contains(OWLActionConstants.RELATIONSHIPDELETE, -1);
			LinkLabelAOS delete = new LinkLabelAOS("images/delete-grey.gif", "images/delete-grey-disabled.gif", constants.relDeleteRelationship(), permission, new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(deleteRelationship == null || !deleteRelationship.isLoaded)
						deleteRelationship = new DeleteRelationship((RelationshipObject)((TreeItemAOS)getSelectedItem(relationshipListbox,treePanel)).getValue());
					deleteRelationship.show();
				}
			});
			

			LinkLabel load = new LinkLabel("images/reload-grey.gif", constants.relReloadRelationship());
			load.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
				    reload();
				    detailTab.reload();
				}
			});
		
			VerticalPanel spacer = new VerticalPanel();
			spacer.add(new HTML("&nbsp"));
			spacer.setWidth("10px");
			
			HorizontalPanel reloadPanel = new HorizontalPanel();
			reloadPanel.setSpacing(3);			
			reloadPanel.add(relationshipListbox);
			reloadPanel.add(spacer);
			reloadPanel.add(load);
			DOM.setStyleAttribute(relationshipListbox.getElement(), "marginLeft", "10px");
			relationshipListbox.addChangeHandler(new ChangeHandler(){
				public void onChange(ChangeEvent event) {
					treePanel.showWidget(relationshipListbox.getSelectedIndex());
					TreeItemAOS vItem = getSelectedItem(relationshipListbox, treePanel);						
					onTreeSelection(vItem);																				
				}
			});			
			reloadPanel.setCellVerticalAlignment(load, HasVerticalAlignment.ALIGN_MIDDLE);
			reloadPanel.setCellVerticalAlignment(relationshipListbox, HasVerticalAlignment.ALIGN_MIDDLE);
			
			
			showURI.setValue(!MainApp.userPreference.isHideUri());			
			showURI.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					getURIPanel.setVisible(showURI.getValue());
					setScrollPanelSize();
				}
			});
						
			HTML name = new HTML(constants.relTitle());
			name.setWordWrap(false);
			name.setStyleName("maintopbartitle");
			
			btnHp.add(add);
			btnHp.add(delete);
			btnHp.add(showURI);
			btnHp.setSpacing(3);

			btnHp.setCellHorizontalAlignment(add, HasHorizontalAlignment.ALIGN_LEFT);
			btnHp.setCellHorizontalAlignment(delete, HasHorizontalAlignment.ALIGN_LEFT);
			btnHp.setCellHorizontalAlignment(showURI, HasHorizontalAlignment.ALIGN_LEFT);

			btnHp.setCellVerticalAlignment(showURI, HasVerticalAlignment.ALIGN_MIDDLE);
			btnHp.setCellVerticalAlignment(add, HasVerticalAlignment.ALIGN_MIDDLE);
			btnHp.setCellVerticalAlignment(delete, HasVerticalAlignment.ALIGN_MIDDLE);
			btnHp.setCellWidth(showURI, "100%");
			
			HorizontalPanel iconPanel = new HorizontalPanel();
			iconPanel.setWidth("100%");
			iconPanel.add(reloadPanel);
			iconPanel.add(btnHp);
			iconPanel.setCellHorizontalAlignment(reloadPanel, HasHorizontalAlignment.ALIGN_LEFT);
			iconPanel.setCellHorizontalAlignment(btnHp, HasHorizontalAlignment.ALIGN_LEFT);
			iconPanel.setCellVerticalAlignment(reloadPanel, HasVerticalAlignment.ALIGN_MIDDLE);
			iconPanel.setCellVerticalAlignment(btnHp, HasVerticalAlignment.ALIGN_MIDDLE);
			iconPanel.setCellWidth(btnHp,"100%");
			
			HorizontalPanel hp = new HorizontalPanel();
			hp.setStyleName("maintopbar");
			hp.setSpacing(3);
			hp.setWidth("100%");
			
			hp.add(name);			
			hp.add(iconPanel);
			hp.setCellVerticalAlignment(name, HasVerticalAlignment.ALIGN_MIDDLE);
			hp.setCellVerticalAlignment(iconPanel, HasVerticalAlignment.ALIGN_MIDDLE);
			hp.setCellHorizontalAlignment(name, HasHorizontalAlignment.ALIGN_LEFT);			
			hp.setCellHorizontalAlignment(iconPanel, HasHorizontalAlignment.ALIGN_LEFT);
			hp.setCellWidth(iconPanel,"100%");
			
			return hp;
		}

		public void showFunctionalPanel(boolean visible){
			btnHp.setVisible(visible);
		}
	}	
	
	private HorizontalPanel uriPanel(){
		uriTb.setWidth("100%");
		uriTb.setWordWrap(true);
		
		HTML label = new HTML("&nbsp;&nbsp;"+constants.relUri()+":&nbsp;");
		label.setStyleName(Style.fontWeightBold);
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(label);
		hp.add(uriTb);
		hp.setWidth("100%");
		hp.setVisible(false);
		hp.setStyleName("showuri");
		hp.setCellWidth(uriTb, "100%");
		hp.setCellHorizontalAlignment(uriTb, HasHorizontalAlignment.ALIGN_LEFT);
		return hp;
	}	
	
	public void setScrollPanelSize()
	{	    
	    setObjScrollPanelSize();
		setDataScrollPanelSize();
	}
	
	public void setObjScrollPanelSize()
	{
		DOM.setStyleAttribute(scObj.getElement(), "backgroundColor","#FFFFFF");
		Scheduler.get().scheduleDeferred(new Command(){

			public void execute() {
				scObj.setHeight(hSplit.getOffsetHeight()-head.getOffsetHeight() +"px");
				Window.addResizeHandler(new ResizeHandler(){
					public void onResize(ResizeEvent event) {
						scObj.setHeight(hSplit.getOffsetHeight()-head.getOffsetHeight()+"px");
					}
				});
			}
	    	
	    });
	}
	
	public void setDataScrollPanelSize()
	{
		DOM.setStyleAttribute(scData.getElement(), "backgroundColor","#FFFFFF");
		Scheduler.get().scheduleDeferred(new Command(){

			public void execute() {
				scData.setHeight(hSplit.getOffsetHeight()-head.getOffsetHeight()+"px");
				Window.addResizeHandler(new ResizeHandler(){
					public void onResize(ResizeEvent event) {
						scData.setHeight(hSplit.getOffsetHeight()-head.getOffsetHeight()+"px");
					}
				});
			}
	    	
	    });
	}
	
	public void initLayout(RelationshipTreeObject[] list, final String targetItem, final String type){
		
	    if(targetItem == null)
        {
            detailTab.setVisible(false);
            hSplit.setSplitPosition("100%");
            getURIPanel.setVisible(false);
        }
	    
		relationshipTypeListbox = new OlistBox();
		treePanel = new DeckPanel();
		treePanel.setSize("100%", "100%");
		
		final RelationshipTreeObject objProp = list[0];
		final RelationshipTreeObject dataTypeProp = list[1];
		
		relationshipTypeListbox.addItem(constants.relObjectProperties(), objProp);
		relationshipTypeListbox.addItem(constants.relDatatypeProperties(), dataTypeProp);
		
        if(type!=null)
        {
            if(type.equals(RelationshipObject.OBJECT))
    		{
    		    loadObjRelationshipTree(objProp, targetItem, type);
    		}
    		else if(type.equals(RelationshipObject.DATATYPE))
    		{
    		    loadDataRelationshipTree(dataTypeProp, targetItem, type);
    		}
        }
		else
		{
		    loadObjRelationshipTree(objProp, targetItem, type);
		    loadDataRelationshipTree(dataTypeProp, targetItem, type);
		}
		treePanel.add(scObj);
		treePanel.add(scData);
		
		head = new Header(relationshipTypeListbox,treePanel);
		
		panel.clear();
        panel.setSize("100%", "100%");
		panel.add(head);
		panel.add(treePanel);
		panel.setCellHeight(treePanel, "100%");
		DOM.setStyleAttribute(treePanel.getElement(), "backgroundColor", "#FFFFFF");
		int index = 0;
		if(type!= null && type.equals(RelationshipObject.DATATYPE))
        {
            index = 1;
        }
        relationshipTypeListbox.setSelectedIndex(index);
		treePanel.showWidget(index);
						
	}
	
	public void loadObjRelationshipTree(final RelationshipTreeObject rtObj, final String targetItemURI, final String type)
	{
		
		LoadingDialog sayLoading = new LoadingDialog();
		VerticalPanel vp = new VerticalPanel();
		vp.add(sayLoading);
		vp.setCellHorizontalAlignment(sayLoading, HasHorizontalAlignment.ALIGN_CENTER);
		vp.setCellVerticalAlignment(sayLoading, HasVerticalAlignment.ALIGN_MIDDLE);
		vp.setCellHeight(sayLoading, "100%");
		vp.setCellWidth(sayLoading, "100%");
		
		scObj = new ScrollPanel();
		scObj.add(vp);
		
		objectTree = new TreeAOS(TreeAOS.TYPE_RELATIONSHIP);
		objectTree.setSize("100%", "100%");
		LazyLoadingTree.addTreeItems(objectTree, rtObj.getRootItem(), rtObj);
		Scheduler.get().scheduleDeferred(new Command(){
		    public void execute() {				
				setObjScrollPanelSize();
				scObj.clear();
				scObj.add(objectTree);
				gotoItem(objectTree, targetItemURI, type);
		    }
		});
	}
	
	public void loadDataRelationshipTree(final RelationshipTreeObject rtObj, final String targetItemURI, final String type)
	{
		LoadingDialog sayLoading = new LoadingDialog();
		VerticalPanel vp = new VerticalPanel();
		vp.add(sayLoading);
		vp.setCellHorizontalAlignment(sayLoading, HasHorizontalAlignment.ALIGN_CENTER);
		vp.setCellVerticalAlignment(sayLoading, HasVerticalAlignment.ALIGN_MIDDLE);
		vp.setCellHeight(sayLoading, "100%");
		vp.setCellWidth(sayLoading, "100%");
		
		scData = new ScrollPanel();
		scData.add(vp);
		dataTypeTree = new TreeAOS(TreeAOS.TYPE_RELATIONSHIP);
		dataTypeTree.setSize("100%", "100%");
		LazyLoadingTree.addTreeItems(dataTypeTree, rtObj.getRootItem(), rtObj);
		Scheduler.get().scheduleDeferred(new Command(){
			public void execute() {				
				setObjScrollPanelSize();
				scData.clear();
				scData.add(dataTypeTree);
				gotoItem(objectTree, targetItemURI, type);
			}
	    });
	}
	
	public void onTreeSelection(TreeItemAOS vItem)
    {
        if(vItem == null)
        {
            hSplit.setSplitPosition("100%");
            detailTab.setVisible(false);
            getURIPanel.setVisible(false);
            uriTb.setText("");
        }
        else
        {
            if(!detailTab.isVisible())  
                hSplit.setSplitPosition("42%");
            setScrollPanelSize();
            detailTab.setVisible(true);
            detailTab.reload();
	        head.showFunctionalPanel(true);
            ((RelationshipTreeObject)relationshipTypeListbox.getObject(relationshipTypeListbox.getSelectedIndex())).setRelationshipSelected(true);
           
            selectedRelationshipObject = (RelationshipObject) vItem.getValue();
            if(vItem.getParentItem()!=null){
                TreeItemAOS parentItem = (TreeItemAOS) vItem.getParentItem();                
                selectedRelationshipObject.setParentObject((RelationshipObject) parentItem.getValue());
            }else{
                RelationshipObject parentObject = new RelationshipObject();
                parentObject.addLabel("Top level relationship", "en");
                parentObject.setType(selectedRelationshipObject.getType());
                selectedRelationshipObject.setParentObject(parentObject);
            }
            vItem.getTree().ensureSelectedItemVisible();
            uriTb.setText(selectedRelationshipObject.getUri());
            detailTab.setURI(selectedRelationshipObject);
            getURIPanel.setVisible(showURI.getValue());
                
            String html = vItem.getHTML();               
            HTML allText = new HTML();
            allText.setHTML(html);              
            allText.addStyleName("cursor-hand");
            allText.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    allConceptText.hide();                      
            }});            
            allConceptText.clear();
            allConceptText.add(allText);
            
            HTML title = new HTML();                
            title.setWidth("100%");
            title.setHTML(html);
            title.setTitle(constants.relShowEntireText());
            title.addStyleName("cursor-hand");
            title.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    Widget sender = (Widget) event.getSource();
                    allConceptText.setPopupPosition(sender.getAbsoluteLeft(), sender.getAbsoluteTop());
                    allConceptText.show();
                }});                    
            detailTab.selectedConceptPanel.clear();
            detailTab.selectedConceptPanel.add(title);
                                            
            DOM.setStyleAttribute(title.getElement(), "height", "18px");
            DOM.setStyleAttribute(title.getElement(), "overflow", "hidden");
            
            detailTab.reload();
            if(relationshipTypeListbox.getSelectedIndex() == 0){
            	detailTab.showInverseProperty(true);
            }else{
            	detailTab.showInverseProperty(false);
            }
            
                      
        }
    }

	public void gotoItem(TreeAOS tree, String targetItemURI, String type)
	{
	    try
		{
			if(targetItemURI!=null && type!=null )
			{
				int index = -1;
				if(type.equals(RelationshipObject.OBJECT))
				{
					index = 0;
				}
				else{
					index = 1;
				}
				relationshipTypeListbox.setSelectedIndex(index);
				head.onPropertyTypeListBoxChange(index);
				for(int i=0; i<tree.getItemCount() ;i++)
				{
					TreeItemAOS vItem = (TreeItemAOS) tree.getItem(i);
					search(tree,targetItemURI,vItem);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void reload(){
        showLoading();
		AsyncCallback<Object> callback = new AsyncCallback<Object>(){
			public void onSuccess(Object result) {
			    initLayout((RelationshipTreeObject[])result,null,null); 
			}
			public void onFailure(Throwable caught) {
				Window.alert(constants.relReloadFail());
			}
		};
		Service.relationshipService.getRelationshipTree(MainApp.userOntology, callback);
	}
	
	/*public void reloadWithTargetItem(final String targetItemURI, final String type){
		AsyncCallback<Object> callback = new AsyncCallback<Object>(){
			public void onSuccess(Object result) {
			    initLayout((RelationshipTreeObject[])result, targetItemURI, type); 
			}
			public void onFailure(Throwable caught) {
				Window.alert(constants.relReloadFail());
			}
		};
		Service.relationshipService.getRelationshipTree(MainApp.userOntology, callback);
	}*/

	public Widget getLoadingPanel()
	{
		LoadingDialog sayLoading = new LoadingDialog();
		VerticalPanel vp = new VerticalPanel();
		vp.add(sayLoading);
		vp.setCellHorizontalAlignment(sayLoading, HasHorizontalAlignment.ALIGN_CENTER);
		vp.setCellVerticalAlignment(sayLoading, HasVerticalAlignment.ALIGN_MIDDLE);
		vp.setCellHeight(sayLoading, "100%");
		vp.setCellWidth(sayLoading, "100%");
		return vp;
	}
	
	private void search(FastTree tree, String targetItemURI, TreeItemAOS item){
		boolean founded = false;
		// do action 
		if(((RelationshipObject)item.getValue()).getUri().equals(targetItemURI)){
		    founded = true;
			tree.setSelectedItem(item);
			item.setState(true);
			tree.ensureSelectedItemVisible();
			onTreeSelection(item);
			// open state
			while(item!=null){
				item = (TreeItemAOS) item.getParentItem();
				if(item!=null){
					item.setState(true);	
				}
			}
		}
		// recursion
		if(!founded){
			for(int i=0;i<item.getChildCount();i++){
				TreeItemAOS childItem = (TreeItemAOS) item.getChild(i);
				if(childItem!=null){
					search(tree,targetItemURI,childItem);
				}
			}
		}
	}
	public class DeleteRelationship extends FormDialogBox implements ClickHandler{
		private RelationshipObject relationshipObject ;
		public DeleteRelationship(RelationshipObject relationshipObject){
			super(constants.buttonDelete(), constants.buttonCancel());
			this.relationshipObject = relationshipObject;
			setWidth("400px");
			setText(constants.relDeleteRelationship());
			initLayout();
		}
		
		public void initLayout() {
			HTML message = new HTML(messages.relDeleteWarning(LazyLoadingTree.getRelationshipLabel(relationshipObject)));			
			Grid table = new Grid(1,2);
			table.setWidget(0, 0,getWarningImage());
			table.setWidget(0, 1, message);
			
			addWidget(table);
		}
	    
	    public void onSubmit() {

			AsyncCallback<Object> callback = new AsyncCallback<Object>(){
				public void onSuccess(Object results){
					//reloadWithTargetItem(relationshipIns, relationshipObject.getType());
					SearchTree.deleteTargetItem(getSelectedTree(relationshipObject.getType()).getSelectedItem());
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.relDeleteReltionshipFail());
				}
			};
			Service.relationshipService.deleteRelationship(relationshipObject, MainApp.userOntology, callback);
	    }
		
	}
	
	private class AddNewRelationship extends FormDialogBox{
	    private TextBox name ;
	    private ListBox language ;
	    private ListBox position ;
	    private RelationshipObject selectRelationshipObject;
	    private RelationshipObject parentObject;
	    
	    public AddNewRelationship(RelationshipObject selectRelationshipObject,RelationshipObject parentObject) {
	    	super();
	    	setWidth("400px");
	    	this.selectRelationshipObject = selectRelationshipObject;
	    	this.parentObject = parentObject;
	    	this.setText(constants.relCreateNewRelationship());
	    	this.initLayout();
	    
	    }
	    public void initLayout() {
	    	name = new TextBox();
	    	name.setWidth("100%");
	    	
	    	language = Convert.makeListBoxWithValue((ArrayList<String[]>)MainApp.getLanguage());
	    	language.setWidth("100%");
	    	
	    	position = new ListBox();
	    	position.setWidth("100%");
	    	position.addItem("--None--","");
	    	position.addItem(constants.relChildRelationship(),"subClass");
	    	position.addItem(constants.relSameRelationship(),"sameLevel");
	    	
	    	Grid table = new Grid(3, 2);
			table.setWidget(0, 0, new HTML(constants.relLabel()));
			table.setWidget(1, 0, new HTML(constants.relLanguage()));
			table.setWidget(2, 0, new HTML(constants.relPosition()));
			table.setWidget(0, 1, name);
			table.setWidget(1, 1, language);
			table.setWidget(2, 1, position);
			table.setWidth("100%");
			table.getColumnFormatter().setWidth(1, "80%");
			
			addWidget(GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1"));
	    }
	    public boolean passCheckInput() {
	    	boolean pass = false;
			if(language.getValue((language.getSelectedIndex())).equals("") || name.getText().length()==0 || position.getValue(position.getSelectedIndex()).equals("")){
				pass = false;
			}else {
				pass = true;
			}
			return pass;
	    }
	    public void onSubmit() {
	    	//showLoading();
	    	final RelationshipObject newProperty = new RelationshipObject();
	    	newProperty.setType(selectRelationshipObject.getType());
	    	newProperty.setParentObject(parentObject);
	    	newProperty.addLabel(name.getText(), language.getValue(language.getSelectedIndex()));
	    	
	    	final String relationshipPosition = position.getValue(position.getSelectedIndex());
	    	
    		AsyncCallback<Object> callback = new AsyncCallback<Object>(){
				public void onSuccess(Object results){
					RelationshipObject rObj = (RelationshipObject) results ;
					//reloadWithTargetItem(rObj.getUri(), newProperty.getType());
					SearchTree.addTargetItem(getSelectedTree(rObj.getType()).getSelectedItem(), rObj, parentObject.getUri(), relationshipPosition);
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.relAddNewReltionshipFail());
				}
			};
			
			
			int actionId = 46; // relationship-create
			Service.relationshipService.addNewRelationship(name.getText(), language.getValue(language.getSelectedIndex()), selectRelationshipObject , parentObject, newProperty, relationshipPosition, MainApp.userOntology, MainApp.userId, actionId ,callback);
	    }
	   
	}
	
	public ScrollPanel getSelectedScrollPanel(String type)
	{
		if(type.equals(RelationshipObject.OBJECT))
		{
			return scObj;
		}
		else if(type.equals(RelationshipObject.DATATYPE))
		{
			return scData;
		}
		else
			return null;
	}
	
	public TreeAOS getSelectedTree(String type)
	{
		if(type.equals(RelationshipObject.OBJECT))
		{
			return objectTree;
		}
		else if(type.equals(RelationshipObject.DATATYPE))
		{
			return dataTypeTree;
		}
		else
			return null;
	}
}
