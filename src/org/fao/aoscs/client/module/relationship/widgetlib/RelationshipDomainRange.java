package org.fao.aoscs.client.module.relationship.widgetlib;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.OWLActionConstants;
import org.fao.aoscs.client.module.relationship.RelationshipTemplate;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabelAOS;
import org.fao.aoscs.client.widgetlib.shared.tree.TreeAOS;
import org.fao.aoscs.client.widgetlib.shared.tree.TreeItemAOS;
import org.fao.aoscs.domain.ClassObject;
import org.fao.aoscs.domain.DomainRangeDatatypeObject;
import org.fao.aoscs.domain.InitializeRelationshipData;
import org.fao.aoscs.domain.PermissionObject;
import org.fao.aoscs.domain.RelationshipObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class RelationshipDomainRange extends RelationshipTemplate{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private DeleteDomainRange deleteDomainRange;
	private AddNewDomainRange addNewDomainRange;
	private EditRange editRange;
	private AddRangeValue addRangeValue;

	public RelationshipDomainRange(PermissionObject permissionTable, InitializeRelationshipData initData, RelationshipDetailTab detailPanel) {
		super(permissionTable, initData, detailPanel);
	}
	
    private int getTableSize(ArrayList<ClassObject> domain,ArrayList<ClassObject> range){
    	if(domain.size()>range.size()){
    		return domain.size();
    	}else{
    		return range.size();
    	}
    }
    
    private HorizontalPanel getFunctionButton(final ClassObject clObj,final boolean typeDomain){
		HorizontalPanel hp = new HorizontalPanel();
		
		hp.setSpacing(2);
		String tooltip = null;
		boolean permission = false;
		if(clObj.getType().toLowerCase().equals("domain")){
			tooltip = constants.relDeleteDomain();
			permission = permissionTable.contains(OWLActionConstants.RELATIONSHIPEDIT_DOMAINDELETE, -1);
		}
		else if(clObj.getType().toLowerCase().equals("range")){
			tooltip = constants.relDeleteRange();
			permission = permissionTable.contains(OWLActionConstants.RELATIONSHIPEDIT_RANGEDELETE, -1);
		}
		
		LinkLabelAOS delete = new LinkLabelAOS("images/delete-grey.gif", "images/delete-grey-disabled.gif", null, permission, new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(deleteDomainRange == null || !deleteDomainRange.isLoaded)
					deleteDomainRange = new DeleteDomainRange(clObj,typeDomain,relationshipObject);
				deleteDomainRange.show();
			}
		});
		delete.setToolTipText(tooltip);
		hp.add(delete);
		
		hp.add(new HTML(clObj.getLabel()));
		
		return hp;
	}
    
    private HorizontalPanel getDomainAddButton(){
    	LinkLabelAOS add = new LinkLabelAOS("images/add-grey.gif", "images/add-grey-disabled.gif", constants.relAddNewDomain(), permissionTable.contains(OWLActionConstants.RELATIONSHIPEDIT_DOMAINCREATE, -1), new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(addNewDomainRange == null || !addNewDomainRange.isLoaded)
					addNewDomainRange = new AddNewDomainRange();				
				addNewDomainRange.setType(true);
				addNewDomainRange.show();
			}
		});
    	add.setStyleName("cursor-hand");
		return add;
    }
    private HorizontalPanel getRangeAddButton(){
    	LinkLabelAOS add = new LinkLabelAOS("images/add-grey.gif", "images/add-grey-disabled.gif", constants.relAddNewRange(), permissionTable.contains(OWLActionConstants.RELATIONSHIPEDIT_RANGECREATE, -1), new ClickHandler() {
    		public void onClick(ClickEvent event) {
    			if(relationshipObject.getType().equals(RelationshipObject.OBJECT)){
    				if(addNewDomainRange == null || !addNewDomainRange.isLoaded)
    					addNewDomainRange = new AddNewDomainRange();    				
    				addNewDomainRange.setType(false);
    				addNewDomainRange.show();
    			}
    		}
    	});
    	return add;
    }
    private HorizontalPanel getRangeEditButton(boolean edit , final String type , final ListBox data)
    {
    	final ArrayList<String> list = new ArrayList<String>();
    	for(int i=0 ; i<data.getItemCount() ; i++){
    		list.add(data.getItemText(i));
    	}
    	
    	String image = edit?"images/edit-grey.gif" : "images/add-grey.gif";
    	String imageDisabled = edit?"images/edit-grey-disabled.gif" : "images/add-grey-disabled.gif";
    	String text = edit? constants.relEditRange() : constants.relAddRange();
    	boolean permission = edit? permissionTable.contains(OWLActionConstants.RELATIONSHIPEDIT_RANGEEDIT) : permissionTable.contains(OWLActionConstants.RELATIONSHIPEDIT_RANGECREATE, -1);
    	
    	LinkLabelAOS add = new LinkLabelAOS(image, imageDisabled, text, permission, new ClickHandler() {
    		public void onClick(ClickEvent event) {
    			if(editRange == null || !editRange.isLoaded)
    				editRange = new EditRange(type , list);
    			editRange.show();    		
    		}
    	});
    	return add;
    }
	public void initLayout(){		
		if(relationshipObject.getType().equals(RelationshipObject.OBJECT)){
			this.sayLoading();
			HorizontalPanel domainHp = new HorizontalPanel();
			domainHp.setWidth("100%");
			domainHp.add(new HTML(constants.relDomain()));
			DOM.setStyleAttribute(domainHp.getElement(), "color", "#FFF");
			domainHp.add(getDomainAddButton());
			domainHp.setCellHorizontalAlignment(domainHp.getWidget(0), HasHorizontalAlignment.ALIGN_CENTER);
			domainHp.setCellVerticalAlignment(domainHp.getWidget(0), HasVerticalAlignment.ALIGN_MIDDLE);
			domainHp.setCellHorizontalAlignment(domainHp.getWidget(1), HasHorizontalAlignment.ALIGN_RIGHT);
			
			HorizontalPanel rangeHp = new HorizontalPanel();
			rangeHp.setWidth("100%");
			rangeHp.add(new HTML(constants.relRange()));
			DOM.setStyleAttribute(rangeHp.getElement(), "color", "#FFF");		
			rangeHp.add(getRangeAddButton());
			rangeHp.setCellHorizontalAlignment(rangeHp.getWidget(0), HasHorizontalAlignment.ALIGN_CENTER);
			rangeHp.setCellVerticalAlignment(rangeHp.getWidget(0), HasVerticalAlignment.ALIGN_MIDDLE);
			rangeHp.setCellHorizontalAlignment(rangeHp.getWidget(1), HasHorizontalAlignment.ALIGN_RIGHT);
			
			final Grid table = new Grid(1,2);	
			table.getColumnFormatter().setWidth(0, "50%");
			table.setWidget(0, 0, domainHp);
			table.setWidget(0, 1, rangeHp);
			
			AsyncCallback<HashMap<String, ArrayList<ClassObject>>> callback = new AsyncCallback<HashMap<String, ArrayList<ClassObject>>>(){
				public void onSuccess(HashMap<String, ArrayList<ClassObject>> map) 
				{
					clearPanel();
					ArrayList<ClassObject> domainList = (ArrayList<ClassObject>) map.get("domain");
					ArrayList<ClassObject> rangeList = (ArrayList<ClassObject>) map.get("range");
					table.resizeRows(getTableSize(domainList,rangeList)+1);
					if(!domainList.isEmpty() || !rangeList.isEmpty()){
						for (int i = 0; i < domainList.size(); i++) {
							table.setWidget(i+1, 0, getFunctionButton((ClassObject)domainList.get(i),true));
						}
						for (int i = 0; i < rangeList.size(); i++) {
							table.setWidget(i+1, 1,  getFunctionButton((ClassObject)rangeList.get(i),false));
						}						
						table.getColumnFormatter().setWidth(0, "50%");
						relationshipRootPanel.add(GridStyle.setTableConceptDetailStyleTop(table,"gstFR1","gstFC1","gstR1","gstPanel1",true));
					}					 
					else{						
						relationshipRootPanel.add(GridStyle.setTableConceptDetailStyleTop(table,"gstFR1","gstFC1","gstR1","gstPanel1",true));						
					}
					detailPanel.tabPanel.getTabBar().setTabHTML(4, Convert.replaceSpace(
					        (domainList.size()>1?constants.relDomains():constants.relDomain())+"&nbsp;("+(domainList.size())+")&nbsp;&amp;&nbsp;"+
					        (rangeList.size()>1?constants.relRanges():constants.relRange())+"&nbsp;("+(rangeList.size())+")"));
				}
				public void onFailure(Throwable caught) {
					Window.alert(caught.getMessage());
				}
			};
			Service.relationshipService.getDomainRange(relationshipObject.getName(),  MainApp.userOntology, callback);
						
			
		}else
		{
			// -------------------------------------------
			this.sayLoading();
			// DOMAIN
			HorizontalPanel domainHp = new HorizontalPanel();
			domainHp.setWidth("100%");
			DOM.setStyleAttribute(domainHp.getElement(), "color", "#FFF");			
			domainHp.add(new HTML(constants.relDomain()));				
			domainHp.add(getDomainAddButton());
			domainHp.setCellHorizontalAlignment(domainHp.getWidget(0), HasHorizontalAlignment.ALIGN_LEFT);			
			domainHp.setCellVerticalAlignment(domainHp.getWidget(0), HasVerticalAlignment.ALIGN_MIDDLE);
			domainHp.setCellHorizontalAlignment(domainHp.getWidget(1), HasHorizontalAlignment.ALIGN_RIGHT);
			
			final Grid tableDomain = new Grid(1,1);
			tableDomain.getColumnFormatter().setWidth(0, "100%");
			tableDomain.setWidget(0, 0, domainHp);

			// RANGE									
			final Label rangeType = new Label();			
						
			final ListBox rangeListBox = new ListBox();
			rangeListBox.setVisibleItemCount(10);
			rangeListBox.setWidth("100%");
										
			AsyncCallback<Object> callback = new AsyncCallback<Object>(){
				public void onSuccess(Object result) 
				{
					clearPanel();
					DomainRangeDatatypeObject data = (DomainRangeDatatypeObject) result;
					ArrayList<ClassObject> domainList = data.getDomain();
					ArrayList<String> rangeList = data.getRangeValue();
					
					int rowDomain = domainList.size();
					tableDomain.resizeRows(rowDomain+1);
					if(!domainList.isEmpty()){
						for (int i = 0; i < domainList.size(); i++) {
							tableDomain.setWidget(i+1, 0, getFunctionButton((ClassObject)domainList.get(i),true));
						}
					}							
					
					final Grid rangeDetail = new Grid(0,0);													
					rangeDetail.setWidth("100%");
					
					if(data.getRangeDataType() != null){						
						rangeDetail.resize(1, 2);
						rangeDetail.getColumnFormatter().setWidth(0, "10%");
						rangeDetail.setWidget(0, 0, new HTML(constants.relType()));
						rangeDetail.setWidget(0, 1, rangeType);
						String temp = data.getRangeDataType();
						rangeType.setText(temp.substring(0,1).toUpperCase() + temp.substring(1));
					}
					
					if(!rangeList.isEmpty()){						
						rangeDetail.resize(2, 2);											
						rangeDetail.setWidget(1, 0, new HTML(constants.relValues()));
						rangeDetail.setWidget(1, 1, rangeListBox);						
						for(int i = 0 ; i < rangeList.size() ; i++){
							rangeListBox.addItem(rangeList.get(i));
						}
					}
					detailPanel.tabPanel.getTabBar().setTabHTML(3, Convert.replaceSpace(
					        (domainList.size()>1?constants.relDomains():constants.relDomain())+"&nbsp;("+(domainList.size())+")&nbsp;&amp;&nbsp;"+
					        (rangeList.size()>1?constants.relRanges():constants.relRange())+"&nbsp;("+(rangeList.size())+")"));
					HorizontalPanel rangeHp = new HorizontalPanel();
					rangeHp.setWidth("100%");
					DOM.setStyleAttribute(rangeHp.getElement(), "color", "#FFF");					
					rangeHp.add(new HTML(constants.relRange()));				
					rangeHp.add(getRangeEditButton(rangeList.size()>0 ? true : false , rangeType.getText() , rangeListBox));
					rangeHp.setCellHorizontalAlignment(rangeHp.getWidget(0), HasHorizontalAlignment.ALIGN_LEFT);
					rangeHp.setCellVerticalAlignment(rangeHp.getWidget(0), HasVerticalAlignment.ALIGN_MIDDLE);
					rangeHp.setCellHorizontalAlignment(rangeHp.getWidget(1), HasHorizontalAlignment.ALIGN_RIGHT);
					rangeHp.setCellVerticalAlignment(rangeHp.getWidget(1), HasVerticalAlignment.ALIGN_MIDDLE);
					
					final VerticalPanel rangeDetailVP = new VerticalPanel();
					rangeDetailVP.setWidth("100%");
					rangeDetailVP.setSpacing(5);
					rangeDetailVP.add(GridStyle.setTableConceptDetailStyleleft(rangeDetail,"gslRow2", "gslCol2", "gslPanel2"));
					rangeDetailVP.setCellHorizontalAlignment(rangeHp.getWidget(0), HasHorizontalAlignment.ALIGN_CENTER);
					rangeDetailVP.setCellVerticalAlignment(rangeHp.getWidget(0), HasVerticalAlignment.ALIGN_MIDDLE);
					
					
					final Grid tableRange = new Grid(2,1);					
					tableRange.setWidget(0, 0, rangeHp);
					tableRange.setWidget(1, 0, rangeDetailVP);					
					tableRange.getCellFormatter().setVerticalAlignment(1, 0 ,HasVerticalAlignment.ALIGN_MIDDLE);
					tableRange.getCellFormatter().setHorizontalAlignment(1, 0 ,HasHorizontalAlignment.ALIGN_CENTER);
										
					final VerticalPanel hp = new VerticalPanel();
					hp.setWidth("100%");
					hp.setSpacing(5);
					hp.add(GridStyle.setTableConceptDetailStyleTop(tableDomain,"gstFR1","gstFC1","gstR1","gstPanel1",true));
					hp.add(GridStyle.setTableConceptDetailStyleTop(tableRange,"gstFR1","gstFC1","gstR1","gstPanel1",true));
					relationshipRootPanel.add(hp);									
				}
				public void onFailure(Throwable caught) {
					Window.alert(caught.getMessage());
				}
			};			
			Service.relationshipService.getDomainRangeDatatype(relationshipObject.getName(), MainApp.userOntology, callback);			
			// -------------------------------------------
		}
	}	
	
	public class AddNewDomainRange extends FormDialogBox{  
		private TreeAOS tree;
		private boolean typeDomain;
		public AddNewDomainRange(){ 
			super(constants.buttonCreate(), constants.buttonCancel());
			this.setWidth("500px");
			this.setHeight("400px");			
			this.setText(constants.relCreateNewDomainRange());
			this.initLayout();
		}
		
		public void setType(boolean typeDomain){
			this.typeDomain = typeDomain;
		}
		
		private ScrollPanel wrapByScrollPanel(Widget object){
			ScrollPanel sc = new ScrollPanel();
			sc.setSize("500px", "400px");
			sc.add(object);			
			return sc;
		}
		private TreeAOS convert2DomainRangeTree(ArrayList<ClassObject> clsList){
		    tree = new TreeAOS(TreeAOS.TYPE_ClASS);
			for (int i = 0; i < clsList.size(); i++) {
				ClassObject cls = (ClassObject) clsList.get(i);
				TreeItemAOS item = new TreeItemAOS(cls);
				tree.addItem(item);
			}
			return tree;
		}
		
		public void initLayout(){
			
			VerticalPanel treePanel = new VerticalPanel();
			treePanel.setHeight("120px");
			treePanel.setWidth("100%");
			treePanel.add(wrapByScrollPanel(convert2DomainRangeTree(initData.getClassTree())));
			Grid table = new Grid(1,2);
			table.setWidget(0, 0, new HTML(constants.relClass()));			
			table.setWidget(0, 1, treePanel);
			table.getColumnFormatter().setWidth(1, "80%");
			table.setWidth("100%");
			
			addWidget(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));
		}
	
		  
		public boolean passCheckInput() {
			boolean pass = false;
			if(tree.getSelectedItem()==null){
				pass = false;
			}else {
				pass = true;
			}
			return pass;
		}
		public void onSubmit(){
			sayLoading();
			
			AsyncCallback<Object> callback = new AsyncCallback<Object>(){
				public void onSuccess(Object results){
					RelationshipDomainRange.this.setURI(relationshipObject);
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.relAddNewDomainRangeFail());
				}
			};
			
			ClassObject clObj = (ClassObject)((TreeItemAOS)tree.getSelectedItem()).getValue();		
			if(typeDomain){
				Service.relationshipService.addDomain(relationshipObject, clObj.getName(), 59 , MainApp.userId, MainApp.userOntology, callback);
			}else {
				Service.relationshipService.addRange(relationshipObject, clObj.getName(), 61, MainApp.userId, MainApp.userOntology, callback);
			}
		}
		
	}
	
	public class DeleteDomainRange extends FormDialogBox implements ClickHandler
	{
		private RelationshipObject relationshipObject ;
		private ClassObject clObj; 
		private boolean typeDomain;
		public DeleteDomainRange(ClassObject clObj,boolean typeDomain,RelationshipObject relationshipObject ){
			super(constants.buttonDelete(), constants.buttonCancel());
			this.typeDomain = typeDomain;
			this.clObj = clObj;
			this.relationshipObject = relationshipObject;
			
			setWidth("400px");
			setText(constants.relDeleteProperty());
			initLayout();
		}
		
		public void initLayout() {
		    HTML message = new HTML(constants.relDeleteDomainRangeWarning());
			
			Grid table = new Grid(1,2);
			table.setWidget(0, 0,getWarningImage());
			table.setWidget(0, 1, message);
			
			addWidget(table);
		}

	    
	    public void onSubmit() {
	    	DeleteDomainRange.this.hide();
			sayLoading();
			
			AsyncCallback<Object> callback = new AsyncCallback<Object>(){
				public void onSuccess(Object results){
					RelationshipDomainRange.this.setURI(relationshipObject);
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.relDeleteDomainRangeFail());
				}
			};
			if(typeDomain){
				Service.relationshipService.deleteDomain(relationshipObject, clObj.getName(), 60, MainApp.userId, MainApp.userOntology, callback);
			}else{
				Service.relationshipService.deleteRange(relationshipObject, clObj.getName(), 63, MainApp.userId, MainApp.userOntology, callback);
			}						
	    }
		
	}
	
	public class EditRange extends FormDialogBox {  				
		private ListBox rangeDataType = new ListBox();
		private ListBox rangeData = new ListBox(true);
		private String type = new String();
		
		public EditRange(String type , ArrayList<String> data){ 
			super();
			this.type = type;
			for(int i=0 ; i<data.size() ; i++)
	    		rangeData.addItem(data.get(i));
			this.setWidth("300px");
			this.setHeight("300px");			
			this.setText(constants.relEditRange());
			this.initLayout();
		}
		
		public void initLayout(){
			// Range Data Type Select
			rangeDataType.setWidth("100%");
			rangeDataType.addItem("Any", "any");
			rangeDataType.addItem("Boolean", "boolean");
			rangeDataType.addItem("Float", "float");			
			rangeDataType.addItem("Int", "int");
			rangeDataType.addItem("String", "string");			
			rangeDataType.setSelectedIndex(0);
			for(int i=0 ; i<rangeDataType.getItemCount() ; i++){
	    		if( rangeDataType.getValue(i).equalsIgnoreCase(this.type)){
	    			rangeDataType.setSelectedIndex(i);
	    			break;
	    		}
			}
						
			rangeDataType.addChangeHandler(new ChangeHandler(){
				public void onChange(ChangeEvent event){
					rangeData.clear();
				}
			});
			
			rangeData.setVisibleItemCount(12);
			rangeData.setWidth("100%");
			
			HorizontalPanel hp = new HorizontalPanel();			
			hp.add(getAddButton());
			hp.add(getRemoveButton());
			
			VerticalPanel vp = new VerticalPanel();
			vp.setWidth("100%");
			vp.add(rangeData);
			vp.add(hp);
			vp.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_RIGHT);
			
			Grid table = new Grid(2,2);
			table.getColumnFormatter().setWidth(1, "80%");
			table.setWidth("100%");
			
			table.setWidget(0, 0, new HTML(constants.relType()));
			table.setWidget(1, 0, new HTML(constants.relValue()));			
			table.setWidget(0, 1, rangeDataType);						
			table.setWidget(1, 1, vp);
						
			addWidget(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));					
		}
		private HorizontalPanel getAddButton(){
			LinkLabelAOS add = new LinkLabelAOS("images/add-grey.gif", "images/add-grey-disabled.gif", constants.relAddRange(), permissionTable.contains(OWLActionConstants.RELATIONSHIPEDIT_RANGECREATE), new ClickHandler() {
				public void onClick(ClickEvent event) {		
					if(addRangeValue == null || !addRangeValue.isLoaded)
						addRangeValue = new AddRangeValue(rangeDataType.getItemText(rangeDataType.getSelectedIndex()) , rangeData);
					addRangeValue.show();    		
				}
			});
			return add;
		}
		private HorizontalPanel getRemoveButton(){
			LinkLabelAOS add = new LinkLabelAOS("images/delete-grey.gif", "images/delete-grey-disabled.gif", constants.relDeleteRange(), permissionTable.contains(OWLActionConstants.RELATIONSHIPEDIT_RANGEDELETE), new ClickHandler() {
				public void onClick(ClickEvent event) {    		
					for(int i=0 ; i<rangeData.getItemCount() ; i++){
						if(rangeData.isItemSelected(i))
							rangeData.removeItem(i);
					}
				}
			});
			return add;
		}
		
		public void onSubmit(){
			sayLoading();
			
			AsyncCallback<Object> callback = new AsyncCallback<Object>(){
				public void onSuccess(Object results){
					RelationshipDomainRange.this.setURI(relationshipObject);
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.relAddNewDomainRangeFail());
				}
			};
			ArrayList<String> values = new ArrayList<String>();
			for(int i=0 ; i<rangeData.getItemCount() ; i++){
	    		values.add(rangeData.getItemText(i));
			}				
			Service.relationshipService.addRangeValues(relationshipObject, rangeDataType.getItemText(rangeDataType.getSelectedIndex()), values, 64 , MainApp.userId, MainApp.userOntology, callback);			
		}
	}
	
	public class AddRangeValue extends FormDialogBox {  				
		private TextBox valueText = new TextBox();
		private ListBox list;
		public AddRangeValue(String type , ListBox list){ 
			super(constants.buttonCreate(), constants.buttonCancel());
			this.list = list;
			this.setWidth("300px");
			this.setHeight("100px");			
			this.setText(constants.relAddRangeValue());
			this.initLayout();
		}
		
		public void initLayout(){			
			valueText.setWidth("100%");			
			Grid table = new Grid(1,2);
			table.getColumnFormatter().setWidth(1, "80%");
			table.setWidth("100%");			
			table.setWidget(0, 0, new HTML(constants.relValue()));						
			table.setWidget(0, 1, valueText);						
			addWidget(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));					
		}
		
		public void onSubmit(){
			list.addItem(valueText.getText());																	
		}
	}
	
}
