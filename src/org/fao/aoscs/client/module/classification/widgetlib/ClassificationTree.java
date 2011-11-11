package org.fao.aoscs.client.module.classification.widgetlib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.locale.LocaleMessages;
import org.fao.aoscs.client.module.constant.ConceptActionKey;
import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.ConceptBrowser;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.label.LabelAOS;
import org.fao.aoscs.client.widgetlib.shared.legend.LegendBar;
import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;
import org.fao.aoscs.client.widgetlib.shared.tree.DisplayLanguage;
import org.fao.aoscs.client.widgetlib.shared.tree.LazyLoadingTree;
import org.fao.aoscs.client.widgetlib.shared.tree.TreeAOS;
import org.fao.aoscs.client.widgetlib.shared.tree.TreeItemAOS;
import org.fao.aoscs.domain.ClassificationObject;
import org.fao.aoscs.domain.ConceptDetailObject;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.PermissionObject;
import org.fao.aoscs.domain.SchemeObject;
import org.fao.aoscs.domain.TermObject;

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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.FastTree;

public class ClassificationTree extends Composite {
    private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
    private LocaleMessages messages = (LocaleMessages) GWT.create(LocaleMessages.class);
    private HorizontalPanel panelHp = new HorizontalPanel();
    private HorizontalSplitPanel hSplit = new HorizontalSplitPanel();
    private HorizontalPanel dPanel = new HorizontalPanel();
    private VerticalPanel panel = new VerticalPanel();
    private OlistBox schemeListBox;
    private Header head;
    private Footer foot;
    private ClassificationDetailTab detailPanel;
    private InitializeConceptData initData;
    public DeckPanel treePanel;
    private ConceptObject selectedConceptObject;
    private LabelAOS uriTb = new LabelAOS();
    public CheckBox showAlsoNonpreferredTerms = new CheckBox();
    public HTML showAlsoNonpreferredTermsHTML = new HTML("&nbsp;"
            + constants.schemeShowNonPreferredTermsAlso());
    private CheckBox showURI = new CheckBox();
    public HTML showURIHTML = new HTML("&nbsp;" + constants.schemeShowUri());
    private HorizontalPanel getURIPanel = uriPanel();
    private DecoratedPopupPanel allConceptText = new DecoratedPopupPanel(true);

    public ClassificationTree(PermissionObject permissionTable,
            InitializeConceptData initData, String schemeURI, String targetItem)
    {
        this.initData = initData;
        this.detailPanel = new ClassificationDetailTab(permissionTable, initData);
        this.detailPanel.setVisible(false);
        ClassificationObject clsObj = (ClassificationObject) initData.getClassificationObject();
        initLayout(clsObj, 0, null, targetItem, schemeURI);
        hSplit.ensureDebugId("cwHorizontalSplitPanel");
        hSplit.setSplitPosition("100%");
        hSplit.setLeftWidget(panel);
        hSplit.setRightWidget(detailPanel);

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

        bodyPanel.setSize(MainApp.getBodyPanelWidth() - 20 + "px", MainApp.getBodyPanelHeight()
                - 30 + "px");
        Window.addResizeHandler(new ResizeHandler() {
            public void onResize(ResizeEvent event)
            {
                bodyPanel.setSize(MainApp.getBodyPanelWidth() - 20 + "px", MainApp.getBodyPanelHeight()
                        - 30 + "px");
                hSplit.setSize("100%", "100%");
                if (detailPanel.isVisible())
                    hSplit.setSplitPosition("42%");
                else
                    hSplit.setSplitPosition("100%");
            }
        });

        dPanel.add(bodyPanel);
        dPanel.setStyleName("borderbar");

        panelHp.add(dPanel);
        panelHp.setCellWidth(dPanel, "100%");
        panelHp.setCellHeight(dPanel, "100%");
        panelHp.setSpacing(5);
        panelHp.setSize("100%", "100%");
        panelHp.setCellHorizontalAlignment(dPanel, HasHorizontalAlignment.ALIGN_CENTER);
        panelHp.setCellVerticalAlignment(dPanel, HasVerticalAlignment.ALIGN_MIDDLE);
        initWidget(panelHp);
    }

    private void initLayout(ClassificationObject clsObj, int startSchemeIndex,
            String schemeLabel, String targetItem, String schemeURI)
    {
        HashMap<String, String> schemeIndex = new HashMap<String, String>();
        schemeListBox = new OlistBox();
        treePanel = new DeckPanel();
        treePanel.setSize("100%", "100%");

        panel.clear();
        panel.setSize("100%", "100%");

        ArrayList<String> schemeList = sortHashMapKey(clsObj.getSchemeList());
        for (int i = 0; i < schemeList.size(); i++)
        {
            String schemeInstance = (String) schemeList.get(i);
            SchemeObject sObj = (SchemeObject) clsObj.getSchemeList().get(schemeInstance);
            schemeIndex.put(sObj.getSchemeLabel(), String.valueOf(i));

            if (sObj.isConceptObjectEmpty())
            {
                schemeListBox.addItem(sObj.getSchemeLabel(), sObj);
                VerticalPanel vp = new VerticalPanel();
                vp.add(new HTML(constants.schemeNoCategory()));
                vp.setSize("100%", "100%");
                vp.setCellHorizontalAlignment(vp.getWidget(0), HasHorizontalAlignment.ALIGN_CENTER);
                vp.setCellVerticalAlignment(vp.getWidget(0), HasVerticalAlignment.ALIGN_MIDDLE);
                ScrollPanel sc = new ScrollPanel();
                setScrollPanelSize(sc);
                sc.add(vp);
                treePanel.add(sc);
            }
            else
            {
                schemeListBox.addItem(sObj.getSchemeLabel(), sObj);
                loadCategoryTree(sObj);
            }
        }
        foot = new Footer();
        head = new Header(schemeListBox, treePanel, foot, clsObj);
        panel.add(head);
        panel.add(treePanel);
        panel.add(foot);
        panel.setCellHeight(treePanel, "100%");
        DOM.setStyleAttribute(treePanel.getElement(), "backgroundColor", "#FFFFFF");

        if (schemeURI != null)
        {
            startSchemeIndex = findSchemeListBoxIndex(schemeURI);
            if (startSchemeIndex == -1)
                startSchemeIndex = 0;
        }
        else if (schemeLabel != null)
        {
            startSchemeIndex = Integer.parseInt((String) schemeIndex.get(schemeLabel));
            if (startSchemeIndex == -1)
                startSchemeIndex = 0;
        }

        if (treePanel.getWidgetCount() > 0)
            treePanel.showWidget(startSchemeIndex);
        schemeListBox.setSelectedIndex(startSchemeIndex);
        head.onSchemeListBoxChange(startSchemeIndex);
        try
        {

            SchemeObject sObj = null;
            if (schemeListBox.getSelectedIndex() != -1)
                sObj = (SchemeObject) schemeListBox.getObject(schemeListBox.getSelectedIndex());
            if (sObj != null)
            {
                if (sObj.hasConcept())
                {
                    head.showStartAddCategory(false);
                }
                else
                {
                    head.showStartAddCategory(true);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if (targetItem != null && schemeURI != null)
        {
            gotoItem(schemeURI, targetItem);
        }

    }

    public void setDisplayLanguage(ArrayList<String> language, boolean showAlsoNonpreferredTerms)
    {
        for (int i = 0; i < treePanel.getWidgetCount(); i++)
        {
            Object obj = treePanel.getWidget(i);
            if (obj instanceof ScrollPanel)
            {
                ScrollPanel sc = (ScrollPanel) obj;
                if (sc.getWidget() instanceof FastTree)
                {
                    FastTree tree = (FastTree) sc.getWidget();
                    SchemeObject sObj = (SchemeObject)	schemeListBox.getObject(i);
                    DisplayLanguage.executeCategory(tree, language, showAlsoNonpreferredTerms, sObj);
                }
            }
        }
    }

    private TreeItemAOS getSelectedItem(OlistBox schemeListbox,
            DeckPanel treePanel)
    {
        TreeItemAOS item = null;
        if (treePanel.getWidget(treePanel.getVisibleWidget()) instanceof ScrollPanel)
        {
            ScrollPanel sc = (ScrollPanel) treePanel.getWidget(treePanel.getVisibleWidget());
            if (sc.getWidget() instanceof FastTree)
            {
                FastTree tree = (FastTree) sc.getWidget();
                item = (TreeItemAOS) tree.getSelectedItem();
            }
        }
        return item;

    }

    private ConceptObject getParentOfSelectedConcept()
    {
        ConceptObject pcObj = null;
        if (treePanel.getWidget(treePanel.getVisibleWidget()) instanceof ScrollPanel)
        {
            ScrollPanel sc = (ScrollPanel) treePanel.getWidget(treePanel.getVisibleWidget());
            if (sc.getWidget() instanceof FastTree)
            {
            	FastTree tree = (FastTree) sc.getWidget();
                TreeItemAOS item = (TreeItemAOS) tree.getSelectedItem();
                TreeItemAOS parent = (TreeItemAOS) item.getParentItem();
                if (parent != null)
                {
                    pcObj = (ConceptObject) parent.getValue();
                }
            }
        }
        return pcObj;
    }

    public void showLoading()
    {
        hSplit.setSplitPosition("100%");
        detailPanel.setVisible(false);
        panel.clear();
        panel.setSize("100%", "100%");
        LoadingDialog sayLoading = new LoadingDialog();
        panel.add(sayLoading);
        panel.setCellHorizontalAlignment(sayLoading, HasHorizontalAlignment.ALIGN_CENTER);
        panel.setCellVerticalAlignment(sayLoading, HasVerticalAlignment.ALIGN_MIDDLE);
        panel.setCellHeight(sayLoading, "100%");
        panel.setCellWidth(sayLoading, "100%");
    }

    public void onTreeSelection(final TreeItemAOS vItem)
    {
    	if (vItem == null)
        {
            hSplit.setSplitPosition("100%");
            detailPanel.setVisible(false);
            getURIPanel.setVisible(false);
            uriTb.setText("");
        }
        else
        {
    		head.showFunctionalPanel(true);
        	if(!detailPanel.isVisible())
                hSplit.setSplitPosition("42%");
            detailPanel.setVisible(true);            
            getURIPanel.setVisible(showURI.getValue());

            ((SchemeObject) schemeListBox.getObject(schemeListBox.getSelectedIndex())).setConceptSelected(true);
            
            selectedConceptObject = (ConceptObject) vItem.getValue();
            if (vItem.getParentItem() != null)
            {
            	TreeItemAOS parentItem = (TreeItemAOS) vItem.getParentItem();
                selectedConceptObject.setParentObject((ConceptObject) parentItem.getValue());
            }
            else
            {
                
            	TermObject termObject = new TermObject();
				termObject.setLabel("Top level concept");
				termObject.setUri(ModelConstants.COMMONBASENAMESPACE+"i_en_domain_concept");
				termObject.setName("i_en_domain_concept");
				termObject.setLang("en");
				termObject.setConceptUri(ModelConstants.COMMONBASENAMESPACE+ModelConstants.CCATEGORY);
				termObject.setConceptName(ModelConstants.CCATEGORY);
				
                ConceptObject parentObject = new ConceptObject();
                parentObject.setUri(ModelConstants.COMMONBASENAMESPACE+ ModelConstants.CCATEGORY);
                parentObject.addTerm(termObject.getUri(), termObject);
                parentObject.setName(ModelConstants.CCATEGORY);
                parentObject.setBelongsToModule(ConceptObject.CLASSIFICATIONMODULE);
                selectedConceptObject.setParentObject(parentObject);

            }

			uriTb.setText(selectedConceptObject.getUri());
	        detailPanel.resetTab();
			detailPanel.clearData();
			AsyncCallback<Object> callback = new AsyncCallback<Object>(){
				public void onSuccess(Object result){
					final ConceptDetailObject cDetailObj = (ConceptDetailObject) result;
					selectedConceptObject = cDetailObj.getConceptObject();
					vItem.getTree().ensureSelectedItemVisible();
					uriTb.setText(selectedConceptObject.getUri());
					detailPanel.initData(cDetailObj);
					Scheduler.get().scheduleDeferred(new Command() {
			            public void execute()
			            {  
			            	detailPanel.loadTab(cDetailObj);
			            }
			        });
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.conceptLoadFail());
				}
			};
		 
			Service.conceptService.getCategoryDetail(MainApp.userOntology, MainApp.userSelectedLanguage, selectedConceptObject.getName(), selectedConceptObject.getParentObject().getName(), callback);
	        

            String html = vItem.getHTML();
            //html = html.substring(0, html.indexOf("title=")) + " "+ html.substring(html.indexOf(" ", html.indexOf("title=")));

            HTML allText = new HTML();
            allText.setHTML(html);
            allText.addStyleName("cursor-hand");
            allText.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event)
                {
                    allConceptText.hide();
                }
            });

            allConceptText.clear();
            allConceptText.add(allText);

            HTML title = new HTML();
            title.setWidth("100%");
            title.setHTML(html);
            title.setTitle(constants.schemeShowEntireText());
            title.addStyleName("cursor-hand");
            title.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event)
                {
                    Widget sender = (Widget) event.getSource();
                    allConceptText.setPopupPosition(sender.getAbsoluteLeft(), sender.getAbsoluteTop());
                    allConceptText.show();
                }
            });
            detailPanel.selectedConceptPanel.clear();
            detailPanel.selectedConceptPanel.add(title);

            DOM.setStyleAttribute(title.getElement(), "height", "18px");
            DOM.setStyleAttribute(title.getElement(), "overflow", "hidden");
        }
        setScrollPanelSize((ScrollPanel) treePanel.getWidget(treePanel.getVisibleWidget()));
    }

    private void loadCategoryTree(final SchemeObject sObj)
    {

        LoadingDialog sayLoading = new LoadingDialog();
        VerticalPanel vp = new VerticalPanel();
        vp.setSize("100%", "100%");
        vp.add(sayLoading);
        vp.setCellHorizontalAlignment(sayLoading, HasHorizontalAlignment.ALIGN_CENTER);
        vp.setCellVerticalAlignment(sayLoading, HasVerticalAlignment.ALIGN_MIDDLE);
        vp.setCellHeight(sayLoading, "100%");
        vp.setCellWidth(sayLoading, "100%");

        final ScrollPanel sc = new ScrollPanel();
        setScrollPanelSize(sc);
        sc.add(vp);
        treePanel.add(sc);

        final TreeAOS tree = new TreeAOS(TreeAOS.TYPE_CATEGORY);
        tree.setSize("100%", "100%");
        Scheduler.get().scheduleDeferred(new Command(){
            public void execute()
            {
                LazyLoadingTree.addTreeItems(tree, sObj.getRootItem(), showAlsoNonpreferredTerms.getValue(), sObj);
                sc.clear();
                sc.add(tree);
            }
        });
    }

    private HorizontalPanel uriPanel()
    {
        uriTb.setWordWrap(true);

        HTML label = new HTML("&nbsp;&nbsp;" + constants.schemeUri()
                + ":&nbsp;");
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

    public void setScrollPanelSize(final ScrollPanel sc)
    {
        DOM.setStyleAttribute(sc.getElement(), "backgroundColor", "#FFFFFF");
        Scheduler.get().scheduleDeferred(new Command(){

            public void execute()
            {
                sc.setHeight(hSplit.getOffsetHeight() - head.getOffsetHeight()
                        - foot.getOffsetHeight() + "px");
                Window.addResizeHandler(new ResizeHandler() {
                    public void onResize(ResizeEvent event)
                    {
                        sc.setHeight(hSplit.getOffsetHeight()
                                - head.getOffsetHeight()
                                - foot.getOffsetHeight() + "px");
                        if (detailPanel.isVisible())
                            hSplit.setSplitPosition("42%");
                        else
                            hSplit.setSplitPosition("100%");
                    }
                });
            }

        });
    }

    private class Header extends Composite {
        private VerticalPanel panel = new VerticalPanel();

        private HorizontalPanel btnHp = new HorizontalPanel();
        private Footer foot;
        private Image addStart = new Image("images/add-grey.gif");;

        private AddFirstCategory addFirstCategory;
        private AddScheme addScheme;
        private EditScheme editScheme;
        private DeleteScheme deleteScheme;

        private AddCategory addCategory;
        private DeleteCategory deleteCategory;

        public Header(final OlistBox schemeListBox, DeckPanel treePanel,
                Footer foot, ClassificationObject csObj)
        {
            this.foot = foot;

            addStart.setStyleName(Style.Link);
            addStart.setVisible(false);
            addStart.setTitle(constants.schemeAddNewCategory());
            addStart.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event)
                {
                    if (addFirstCategory == null || !addFirstCategory.isLoaded)
                        addFirstCategory = new AddFirstCategory((SchemeObject) schemeListBox.getObject(schemeListBox.getSelectedIndex()));
                    addFirstCategory.show();
                }
            });

            btnHp.setVisible(false);
            panel.add(makeTopLayer(addStart, btnHp, showURI));

            panel.add(addFunctionalButton(schemeListBox, treePanel, csObj));
            panel.add(getURIPanel);
            panel.setWidth("100%");
            initWidget(panel);
        }

        public void onSchemeListBoxChange(int index)
        {
            if (treePanel.getWidgetCount() > 0)
                treePanel.showWidget(index);
            SchemeObject sObj = null;
            if (schemeListBox.getItemCount() > 0)
                sObj = (SchemeObject) schemeListBox.getObject(index);
            if (sObj != null)
            {
                if (sObj.getDescription() == null)
                {
                    foot.setDescription(messages.schemeNoDescription(sObj.getSchemeLabel()));
                }
                else
                {
                    foot.setDescription(sObj.getDescription());
                }

                if (sObj.hasConcept())
                {
                    head.showFunctionalPanel(sObj.isConceptSelected());
                }
            }
        }

        private Grid addFunctionalButton(final OlistBox schemeListBox,
                final DeckPanel treePanel, final ClassificationObject csObj)
        {

            Image add = new Image("images/add-grey.gif");
            add.setStyleName(Style.Link);
            add.setTitle(constants.schemeNew());
            add.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event)
                {
                    if (addScheme == null || !addScheme.isLoaded)
                        addScheme = new AddScheme(csObj);
                    addScheme.show();
                }
            });

            Image edit = new Image("images/edit-grey.gif");
            edit.setStyleName(Style.Link);
            edit.setTitle(constants.schemeEdit());
            edit.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event)
                {
                    if (editScheme == null || !editScheme.isLoaded)
                        editScheme = new EditScheme((SchemeObject) schemeListBox.getObject(schemeListBox.getSelectedIndex()));
                    editScheme.show();
                }
            });

            Image delete = new Image("images/delete-grey.gif");
            delete.setStyleName(Style.Link);
            delete.setTitle(constants.schemeDelete());
            delete.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event)
                {
                    if (deleteScheme == null || !deleteScheme.isLoaded)
                        deleteScheme = new DeleteScheme((SchemeObject) schemeListBox.getObject(schemeListBox.getSelectedIndex()));
                    deleteScheme.show();
                }
            });

            HorizontalPanel btnHp = new HorizontalPanel();
            btnHp.add(add);
            btnHp.add(edit);
            btnHp.add(delete);
            btnHp.setSpacing(3);

            schemeListBox.addChangeHandler(new ChangeHandler() {
                public void onChange(ChangeEvent event)
                {
                    treePanel.showWidget(schemeListBox.getSelectedIndex());
                    onTreeSelection(getSelectedItem(schemeListBox, treePanel));
                    SchemeObject sObj = (SchemeObject) schemeListBox.getObject(schemeListBox.getSelectedIndex());
                    if (sObj.getDescription() == null)
                    {
                        foot.setDescription(messages.schemeNoDescription(sObj.getSchemeLabel()));
                    }
                    else
                    {
                        foot.setDescription(sObj.getDescription());
                    }
                    if (sObj.hasConcept())
                    {
                        head.showFunctionalPanel(sObj.isConceptSelected());
                        head.showStartAddCategory(false);
                    }
                    else
                    {
                        head.showStartAddCategory(true);
                        head.showFunctionalPanel(false);
                    }

                }
            });
            schemeListBox.setWidth("100%");
            DOM.setStyleAttribute(schemeListBox.getElement(), "backgroundColor", "white");

            Grid table = new Grid(1, 2);
            table.setWidget(0, 0, schemeListBox);
            table.setWidget(0, 1, btnHp);
            table.getColumnFormatter().setWidth(1, "30px");
            table.setWidth("100%");
            table.setStyleName("bottombar");
            return table;
        }

        private HorizontalPanel makeTopLayer(Image addStart,
                HorizontalPanel btnHp, final CheckBox showURI)
        {
        	showAlsoNonpreferredTerms.setValue(!MainApp.userPreference.isHideNonpreferred());
        	showAlsoNonpreferredTerms.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event)
                {
                    ClassificationTree.this.setDisplayLanguage((ArrayList<String>) MainApp.userSelectedLanguage, showAlsoNonpreferredTerms.getValue());
                }
            });
            showAlsoNonpreferredTermsHTML.setWordWrap(false);
            showAlsoNonpreferredTermsHTML.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event)
                {
                    showAlsoNonpreferredTerms.setValue(!showAlsoNonpreferredTerms.getValue());
                    ClassificationTree.this.setDisplayLanguage((ArrayList<String>) MainApp.userSelectedLanguage, showAlsoNonpreferredTerms.getValue());
                }
            });

            Image add = new Image("images/add-grey.gif");
            add.setStyleName(Style.Link);
            add.setTitle(constants.schemeAddNewCategory());
            add.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event)
                {
                    if (addCategory == null || !addCategory.isLoaded)
                        addCategory = new AddCategory(selectedConceptObject, (SchemeObject) schemeListBox.getObject(schemeListBox.getSelectedIndex()));
                    addCategory.show();
                }
            });

            Image delete = new Image("images/delete-grey.gif");
            delete.setStyleName(Style.Link);
            delete.setTitle(constants.schemeDeleteCategory());
            delete.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event)
                {
                    if (deleteCategory == null || !deleteCategory.isLoaded)
                        deleteCategory = new DeleteCategory(selectedConceptObject, (SchemeObject) schemeListBox.getObject(schemeListBox.getSelectedIndex()));
                    deleteCategory.show();
                }
            });

            Image load = new Image("images/reload-grey.gif");
            load.setStyleName(Style.Link);
            load.setTitle(constants.schemeReload());
            load.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event)
                {
                    reload(schemeListBox.getSelectedIndex(), null);
                }
            });

            Label name = new Label(constants.schemeTitle());
            name.setStyleName("maintopbartitle");

            HorizontalPanel reloadPanel = new HorizontalPanel();
            reloadPanel.setSpacing(3);
            reloadPanel.add(load);

            showURI.setValue(!MainApp.userPreference.isHideUri());
            showURI.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event)
                {
                    getURIPanel.setVisible(showURI.getValue());
                    setScrollPanelSize((ScrollPanel) treePanel.getWidget(treePanel.getVisibleWidget()));
                }
            });
            showURIHTML.setWordWrap(false);
            showURIHTML.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event)
                {
                    showURI.setValue(!showURI.getValue());
                    getURIPanel.setVisible(showURI.getValue());
                    setScrollPanelSize((ScrollPanel) treePanel.getWidget(treePanel.getVisibleWidget()));
                }
            });

            Spacer sp = new Spacer("100%", "100%");
            HorizontalPanel checkPanel = new HorizontalPanel();
            checkPanel.setSpacing(3);
            checkPanel.setSize("100%", "100%");
            checkPanel.add(showURI);
            checkPanel.add(showURIHTML);
            checkPanel.setCellWidth(showURI, "100%");
            checkPanel.setCellVerticalAlignment(showURIHTML, HasVerticalAlignment.ALIGN_MIDDLE);

            btnHp.setSpacing(3);
            btnHp.add(add);
            btnHp.add(delete);
            btnHp.add(checkPanel);
            btnHp.add(sp);

            btnHp.setCellHorizontalAlignment(add, HasHorizontalAlignment.ALIGN_LEFT);
            btnHp.setCellHorizontalAlignment(delete, HasHorizontalAlignment.ALIGN_LEFT);
            btnHp.setCellHorizontalAlignment(showURI, HasHorizontalAlignment.ALIGN_LEFT);

            btnHp.setCellVerticalAlignment(add, HasVerticalAlignment.ALIGN_MIDDLE);
            btnHp.setCellVerticalAlignment(delete, HasVerticalAlignment.ALIGN_MIDDLE);
            btnHp.setCellVerticalAlignment(showURI, HasVerticalAlignment.ALIGN_MIDDLE);
            btnHp.setCellWidth(sp, "100%");

            HorizontalPanel iconPanel = new HorizontalPanel();
            iconPanel.setWidth("100%");
            iconPanel.add(reloadPanel);
            iconPanel.add(addStart);
            iconPanel.add(btnHp);
            iconPanel.add(showAlsoNonpreferredTerms);
            iconPanel.add(showAlsoNonpreferredTermsHTML);
            iconPanel.setCellHorizontalAlignment(reloadPanel, HasHorizontalAlignment.ALIGN_LEFT);
            iconPanel.setCellHorizontalAlignment(addStart, HasHorizontalAlignment.ALIGN_LEFT);
            iconPanel.setCellHorizontalAlignment(btnHp, HasHorizontalAlignment.ALIGN_LEFT);
            iconPanel.setCellVerticalAlignment(reloadPanel, HasVerticalAlignment.ALIGN_MIDDLE);
            iconPanel.setCellVerticalAlignment(addStart, HasVerticalAlignment.ALIGN_MIDDLE);
            iconPanel.setCellVerticalAlignment(btnHp, HasVerticalAlignment.ALIGN_MIDDLE);
            iconPanel.setCellHorizontalAlignment(showAlsoNonpreferredTerms, HasHorizontalAlignment.ALIGN_LEFT);
            iconPanel.setCellVerticalAlignment(showAlsoNonpreferredTerms, HasVerticalAlignment.ALIGN_MIDDLE);
            iconPanel.setCellHorizontalAlignment(showAlsoNonpreferredTermsHTML, HasHorizontalAlignment.ALIGN_LEFT);
            iconPanel.setCellVerticalAlignment(showAlsoNonpreferredTermsHTML, HasVerticalAlignment.ALIGN_MIDDLE);
            iconPanel.setCellWidth(showAlsoNonpreferredTermsHTML, "100%");

            HorizontalPanel hp = new HorizontalPanel();
            hp.setStyleName("maintopbar");
            hp.setSpacing(3);
            hp.setWidth("100%");

            hp.add(name);
            hp.add(iconPanel);
           
            hp.setCellHorizontalAlignment(name, HasHorizontalAlignment.ALIGN_LEFT);
            hp.setCellVerticalAlignment(name, HasVerticalAlignment.ALIGN_MIDDLE);
            hp.setCellHorizontalAlignment(iconPanel, HasHorizontalAlignment.ALIGN_LEFT);
            hp.setCellVerticalAlignment(iconPanel, HasVerticalAlignment.ALIGN_MIDDLE);            
            hp.setCellWidth(iconPanel, "100%");

            return hp;
        }

        public void showFunctionalPanel(boolean visible)
        {
            btnHp.setVisible(visible);
        }

        public void showStartAddCategory(boolean visible)
        {
            addStart.setVisible(visible);
        }
    }

    private ArrayList<String> sortHashMapKey(HashMap<String, SchemeObject> map)
    {
        ArrayList<String> list = new ArrayList<String>();
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext())
        {
            String key = (String) it.next();
            SchemeObject sObj = (SchemeObject) map.get(key);
            list.add((String) sObj.getSchemeLabel() + "##" + key);
        }
        Collections.sort(list, String.CASE_INSENSITIVE_ORDER);

        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++)
        {
            result.add(((String) list.get(i)).split("##")[1]);
        }
        return result;
    }

    private class Footer extends Composite {

        private TextArea desc = new TextArea();

        public Footer()
        {

            desc.setReadOnly(true);
            desc.setVisibleLines(3);
            desc.setWidth("100%");
            desc.setVisible(false);
            SchemeObject sObj = null;
            if (schemeListBox.getSelectedIndex() != -1)
                sObj = (SchemeObject) schemeListBox.getObject(schemeListBox.getSelectedIndex());
            if (sObj != null)
            {
                if (sObj.getDescription() == null)
                {
                    desc.setText(messages.schemeNoDescription(sObj.getSchemeLabel()));
                }
                else
                {
                    desc.setText(sObj.getDescription());
                }
            }
            // DOM.setStyleAttribute(desc.getElement(), "background",
            // "#ffffff url('images/bg_headergradient.png') repeat-x bottom left");
            desc.addStyleName("statusbar");
            final CheckBox cb = new CheckBox(constants.schemeShowDescription());
            cb.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event)
                {
                    desc.setVisible(cb.getValue());
                    setScrollPanelSize((ScrollPanel) treePanel.getWidget(treePanel.getVisibleWidget()));
                }
            });

            HorizontalPanel footer = new HorizontalPanel();
            footer.add(cb);
            footer.setWidth("100%");
            footer.addStyleName("bottombar");
            VerticalPanel panel = new VerticalPanel();
            panel.add(desc);
            panel.add(footer);
            panel.setSize("100%", "100%");

            initWidget(panel);

        }

        public void setDescription(String text)
        {
            this.desc.setText(text);
        }
    }

    public void reload(final int startSchemeIndex, final String schemeLabel)
    {
        showLoading();
        AsyncCallback<Object> callback = new AsyncCallback<Object>() {
            public void onSuccess(Object results)
            {
                ClassificationObject clsObj = (ClassificationObject) results;
                initLayout(clsObj, startSchemeIndex, schemeLabel, null, null);
            }

            public void onFailure(Throwable caught)
            {
                Window.alert(constants.schemeReloadTreeFail());
            }
        };

        Service.classificationService.getCategoryTree(MainApp.userOntology, callback);
    }

    public void reloadWithTargetItem(final int startSchemeIndex,
            final String schemeURI, final String targetItemURI)
    {
        AsyncCallback<Object> callback = new AsyncCallback<Object>() {
            public void onSuccess(Object results)
            {
                ClassificationObject clsObj = (ClassificationObject) results;
                initLayout(clsObj, startSchemeIndex, null, targetItemURI, schemeURI);
                detailPanel.reload();
            }

            public void onFailure(Throwable caught)
            {
                Window.alert(constants.schemeReloadTreeFail());
            }
        };

        Service.classificationService.getCategoryTree(MainApp.userOntology, callback);
    }

    private int findSchemeListBoxIndex(String schemeURI)
    {
        int index = -1;
        for (int i = 0; i < schemeListBox.getItemCount(); i++)
        {
            SchemeObject sObj = (SchemeObject) schemeListBox.getObject(i);
            if (sObj.getSchemeInstance().equals(schemeURI))
            {
                index = i;
                break;
            }
        }
        return index;
    }

    private int findSchemeListBoxIndexAndCheckhasConcept(String schemeURI)
    {
        int index = -1;
        for (int i = 0; i < schemeListBox.getItemCount(); i++)
        {
            SchemeObject sObj = (SchemeObject) schemeListBox.getObject(i);
            if (sObj.getSchemeInstance().equals(schemeURI) && sObj.hasConcept())
            {
                index = i;
                break;
            }
        }
        return index;
    }

    public void gotoItem(String schemeURI, String targetItemURI)
    {
        int index = findSchemeListBoxIndexAndCheckhasConcept(schemeURI);
        if (index != -1)
        {
            schemeListBox.setSelectedIndex(index);
            head.onSchemeListBoxChange(index);
            Object obj = treePanel.getWidget(index);
            if (obj instanceof ScrollPanel)
            {
                ScrollPanel sc = (ScrollPanel) obj;
                if (sc.getWidget() instanceof FastTree)
                {
                    FastTree tree = (FastTree) sc.getWidget();
                    for (int i = 0; i < tree.getItemCount(); i++)
                    {
                    	TreeItemAOS vItem = (TreeItemAOS) tree.getItem(i);
                        search(tree, targetItemURI, vItem);
                    }
                }
            }
        }
    }

    private void search(FastTree tree, String targetItemURI, TreeItemAOS item)
    {
        boolean founded = false;
        // do action
        if (((ConceptObject) item.getValue()).getUri().equals(targetItemURI))
        {
            founded = true;
            tree.setSelectedItem(item);
            item.setState(true);
            tree.ensureSelectedItemVisible();
            // open state
            while (item != null)
            {
                item = (TreeItemAOS) item.getParentItem();
                if (item != null)
                {
                    item.setState(true);
                }
            }
        }
        // recursion
        if (!founded)
        {
            for (int i = 0; i < item.getChildCount(); i++)
            {
            	TreeItemAOS childItem = (TreeItemAOS) item.getChild(i);
                if (childItem != null)
                {
                    search(tree, targetItemURI, childItem);
                }
            }
        }
    }

    public class AddScheme extends FormDialogBox {

        private TextBox name;
        private TextBox namespace;
        private TextArea description;
        private ClassificationObject csObj;

        public AddScheme(ClassificationObject csObj)
        {
            super(constants.buttonAdd(), constants.buttonCancel());
            this.csObj = csObj;
            this.setText(constants.schemeCreateNew());
            setWidth("400px");
            findNameSpace(" ");
            this.initLayout();
        }

        private boolean findNameSpace(String namespace)
        {
            HashMap<String, SchemeObject> list = csObj.getSchemeList();
            if (list.containsKey(namespace))
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        public void initLayout()
        {
            name = new TextBox();
            name.setWidth("100%");

            namespace = new TextBox();
            namespace.setWidth("100%");

            description = new TextArea();
            description.setWidth("100%");

            Grid table = new Grid(3, 2);
            table.setWidget(0, 0, new HTML(constants.schemeName()));
            table.setWidget(1, 0, new HTML(constants.schemeNamespace()));
            table.setWidget(2, 0, new HTML(constants.schemeDescription()));
            table.setWidget(0, 1, name);
            table.setWidget(1, 1, namespace);
            table.setWidget(2, 1, description);
            table.setWidth("100%");
            table.getColumnFormatter().setWidth(1, "80%");

            addWidget(GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1"));
        }

        public boolean passCheckInput()
        {
            boolean pass = false;
            if (name.getText().length() == 0
                    || namespace.getText().length() == 0
                    || description.getText().length() == 0)
            {
                pass = false;
            }
            else if (findNameSpace(ModelConstants.BASENAMESPACE
                    + namespace.getText()))
            {
                Window.alert(messages.schemeNamespaceExists(namespace.getText().replaceAll(" ", "")));
                pass = false;
            }
            else
            {
                pass = true;
            }
            return pass;
        }

        public void onSubmit()
        {
            showLoading();

            final SchemeObject sObj = new SchemeObject();
            sObj.setDescription(description.getText());
            String ns = namespace.getText();
            /*String[] wordList = namespace.getText().split(" ");
            for (int i = 0; i < wordList.length; i++)
            {
                if (wordList[i].length() > 0)
                {
                    ns += wordList[i].substring(0, 1).toUpperCase()
                            + wordList[i].substring(1);
                }
            }*/
            sObj.setNamespace(ModelConstants.BASENAMESPACE + ns + ModelConstants.NAMESPACESEPARATOR);
            sObj.setNameSpaceCatagoryPrefix(ns.toLowerCase());
            sObj.setSchemeName(sObj.getNameSpaceCatagoryPrefix() + ":" + "i_"+ ns);
            sObj.setSchemeInstance(sObj.getNamespace() + "i_" + ns);
            sObj.setSchemeLabel(name.getText());
            sObj.setRHasSub(ModelConstants.RHAS + ns+ ModelConstants.SUBCATEGORY);
            sObj.setRIsSub(ModelConstants.RIS + ns+ ModelConstants.SUBCATEGORYOF);

            AsyncCallback<Object> callback = new AsyncCallback<Object>() {
                public void onSuccess(Object results)
                {
                    reload(0, sObj.getSchemeLabel());
                    ModuleManager.resetValidation();
                }

                public void onFailure(Throwable caught)
                {
                    Window.alert(constants.schemeAddFail());
                }
            };

            OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.schemeCreate);
            int actionId = Integer.parseInt((String) initData.getActionMap().get(ConceptActionKey.schemeCreate));

            Service.classificationService.addNewScheme(MainApp.userOntology, actionId, MainApp.userId, status, sObj, callback);
        }
    }

    public class EditScheme extends FormDialogBox {
        private TextBox name;
        private TextArea description;
        private SchemeObject sObj;

        public EditScheme(SchemeObject sObj)
        {
            super();
            this.sObj = sObj;
            this.setText(constants.schemeEdit());
            setWidth("400px");
            this.initLayout();
        }

        public void initLayout()
        {
            name = new TextBox();
            name.setWidth("100%");
            name.setText(sObj.getSchemeLabel());

            description = new TextArea();
            description.setWidth("100%");
            description.setText(sObj.getDescription());

            Grid table = new Grid(2, 2);
            table.setWidget(0, 0, new HTML(constants.schemeName()));
            table.setWidget(1, 0, new HTML(constants.schemeDescription()));
            table.setWidget(0, 1, name);
            table.setWidget(1, 1, description);
            table.setWidth("100%");
            table.getColumnFormatter().setWidth(1, "80%");

            addWidget(GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1"));
        }

        public boolean passCheckInput()
        {
            boolean pass = false;
            if (name.getText().length() == 0
                    || description.getText().length() == 0)
            {
                pass = false;
            }
            else
            {
                pass = true;
            }
            return pass;
        }

        public void onSubmit()
        {
            showLoading();

            SchemeObject sObjNew = new SchemeObject();
            sObjNew.setDescription(description.getText());
            sObjNew.setSchemeLabel(name.getText());

            AsyncCallback<Object> callback = new AsyncCallback<Object>() {
                public void onSuccess(Object results)
                {
                    reload(schemeListBox.getSelectedIndex(), null);
                }

                public void onFailure(Throwable caught)
                {
                    Window.alert(constants.schemeEditFail());
                }
            };
            OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.schemeCreate);
            int actionId = Integer.parseInt((String) initData.getActionMap().get(ConceptActionKey.schemeCreate));

            Service.classificationService.editScheme(MainApp.userOntology, actionId, MainApp.userId, status, sObj, sObjNew, callback);
        }
    }

    public class DeleteScheme extends FormDialogBox {
        private SchemeObject sObj;

        public DeleteScheme(SchemeObject sObj)
        {
            super(constants.buttonDelete(), constants.buttonCancel());
            this.sObj = sObj;
            setWidth("400px");
            this.setText(constants.schemeDelete());
            this.initLayout();
        }

        public void initLayout()
        {
            HTML message = new HTML(constants.schemeDeleteWarning());
            Grid table = new Grid(1, 2);
            table.setWidget(0, 0, getWarningImage());
            table.setWidget(0, 1, message);

            addWidget(table);
        }

        public void onSubmit()
        {
            showLoading();

            AsyncCallback<Object> callback = new AsyncCallback<Object>() {
                public void onSuccess(Object results)
                {
                    reload(0, null);
                }

                public void onFailure(Throwable caught)
                {
                    Window.alert(constants.schemeDeleteFail());
                }
            };
            OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.schemeCreate);
            int actionId = Integer.parseInt((String) initData.getActionMap().get(ConceptActionKey.schemeCreate));

            Service.classificationService.deleteScheme(MainApp.userOntology, actionId, MainApp.userId, status, sObj, callback);
        }
    }

    public class AddCategory extends FormDialogBox {
        private DecoratedTabPanel tp;
        private TextBox name;
        private ListBox language;
        private ListBox position1;
        private ListBox position2;
        private LabelAOS selectedConcept;
        private Image browse;
        private ConceptObject refConcept;
        private SchemeObject schemeObject;

        public AddCategory(ConceptObject refConcept, SchemeObject schemeObject)
        {
            super(constants.buttonCreate(), constants.buttonCancel(), false);
            this.schemeObject = schemeObject;
            this.refConcept = refConcept;
            this.setText(constants.schemeAddNewCategory());
            setWidth("400px");
            this.initLayout();
        }

        public void initLayout()
        {
            VerticalPanel vp1 = new VerticalPanel();
            vp1.add(getCreateFromScratch());
            vp1.setSize("100%", "300px");

            VerticalPanel vp2 = new VerticalPanel();
            vp2.add(getCreateBySelectConcept());
            vp2.setSize("100%", "300px");
            vp2.setCellVerticalAlignment(vp2.getWidget(0), HasVerticalAlignment.ALIGN_TOP);

            tp = new DecoratedTabPanel();
            tp.setAnimationEnabled(true);
            tp.add(vp1, constants.schemeCreateFromScratch());
            tp.add(vp2, constants.schemeSelectExisting());
            tp.setWidth("100%");
            tp.selectTab(0);

            addWidget(tp);
        }

        private VerticalPanel getCreateFromScratch()
        {
            name = new TextBox();
            name.setWidth("100%");

            language = Convert.makeListBoxWithValue((ArrayList<String[]>) MainApp.getLanguage());
            language.setWidth("100%");

            position1 = new ListBox();
            position1.addItem("--None--", "");
            position1.addItem(constants.schemeChildItem(), "child");
            position1.addItem(constants.schemeSameItem(), "sameLevel");
            position1.setWidth("100%");

            Grid table = new Grid(3, 2);
            table.setWidget(0, 0, new HTML(constants.schemeName()));
            table.setWidget(1, 0, new HTML(constants.schemeLanguage()));
            table.setWidget(2, 0, new HTML(constants.schemePosition()));
            table.setWidget(0, 1, name);
            table.setWidget(1, 1, language);
            table.setWidget(2, 1, position1);
            table.setWidth("100%");
            table.getColumnFormatter().setWidth(1, "80%");
            final Button submit = new Button(constants.buttonSubmit());
            submit.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event)
                {

                    if (language.getValue((language.getSelectedIndex())).equals("")
                            || language.getValue(language.getSelectedIndex()).equals("--None--")
                            || name.getText().length() == 0
                            || position1.getValue(position1.getSelectedIndex()).equals(""))
                    {
                        Window.alert(constants.schemeCompleteInfo());
                    }
                    else
                    {
                        AddCategory.this.hide();
                        showLoading();
                        submit.setEnabled(false);
                        TermObject termObject = new TermObject();
                        termObject.setLabel(name.getText());
                        termObject.setLang(language.getValue(language.getSelectedIndex()));
                        termObject.setUri("temp");
                        termObject.setName("temp");
                        termObject.setMainLabel(true);

                        ConceptObject concept = new ConceptObject();

                        String pos = position1.getValue(position1.getSelectedIndex());

                        AsyncCallback<Object> callback = new AsyncCallback<Object>() {
                            public void onSuccess(Object results)
                            {
                                String targetItemURI = (String) results;
                                reloadWithTargetItem(schemeListBox.getSelectedIndex(), schemeObject.getSchemeInstance(), targetItemURI);
                                ModuleManager.resetValidation();
                            }

                            public void onFailure(Throwable caught)
                            {
                                Window.alert(constants.schemeAddCategoryFromScratchFail());
                            }
                        };
                        ConceptObject parentObj = getParentOfSelectedConcept();
                        if (!pos.equals("child"))
                        {
                        	if(parentObj != null)
                        	{
                        		refConcept = parentObj;
                        	}
                        	else
                        	{
                        		ConceptObject cReferenceObj = new ConceptObject();
                        		cReferenceObj.setName(ModelConstants.CCATEGORY);
                    	    	refConcept = cReferenceObj;
                        	}
                            pos = "child";
                        }
                        OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptCreate);
                        int actionId = Integer.parseInt((String) initData.getActionMap().get(ConceptActionKey.conceptCreate));
                        Service.classificationService.addNewCategory(MainApp.userOntology, actionId, MainApp.userId, status, refConcept, pos, termObject, concept, schemeObject, callback);
                    }

                }
            });
            Button cancel = new Button(constants.buttonCancel());
            cancel.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event)
                {
                    AddCategory.this.hide();
                }
            });

            HorizontalPanel buttonPanel = new HorizontalPanel();
            buttonPanel.setSpacing(5);
            buttonPanel.add(submit);
            buttonPanel.add(cancel);

            HorizontalPanel hp = new HorizontalPanel();
            hp.setSpacing(0);
            hp.setWidth("100%");
            hp.setStyleName("bottombar");
            hp.add(buttonPanel);
            hp.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);

            VerticalPanel v = new VerticalPanel();
            v.setSpacing(5);
            v.setWidth("100%");
            v.add(GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1"));

            VerticalPanel vp = new VerticalPanel();
            vp.add(v);
            vp.add(hp);
            vp.setWidth("100%");
            vp.addStyleName("borderbar");

            return vp;
        }

        private VerticalPanel getCreateBySelectConcept()
        {
            selectedConcept = new LabelAOS("--None--");

            browse = new Image("images/browseButton3-grey.gif");
            browse.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event)
                {
                	final ConceptBrowser cb =((MainApp) RootPanel.get().getWidget(0)).conceptBrowser; 
    				cb.showBrowser();
    				cb.addSubmitClickHandler(new ClickHandler()
    				{
    					public void onClick(ClickEvent event) {
    						selectedConcept.setText(cb.getSelectedItem(),cb.getTreeObject().getName());
    					}					
    				});	
                }
            });
            DOM.setStyleAttribute(browse.getElement(), "cursor", "pointer");

            HorizontalPanel hp = new HorizontalPanel();
            hp.add(selectedConcept);
            hp.add(browse);
            hp.setCellHorizontalAlignment(selectedConcept, HasHorizontalAlignment.ALIGN_LEFT);
            hp.setCellHorizontalAlignment(browse, HasHorizontalAlignment.ALIGN_RIGHT);
            hp.setWidth("100%");

            position2 = new ListBox();
            position2.addItem("--None--", "");
            position2.addItem(constants.schemeChildItem(), "child");
            position2.addItem(constants.schemeSameItem(), "sameLevel");
            position2.setWidth("100%");

            Grid table = new Grid(2, 2);
            table.setWidget(0, 0, new HTML(constants.schemeConcept()));
            table.setWidget(1, 0, new HTML(constants.schemePosition()));
            table.setWidget(0, 1, hp);
            table.setWidget(1, 1, position2);
            table.setWidth("100%");
            table.getColumnFormatter().setWidth(1, "80%");

            final Button submit = new Button(constants.buttonSubmit());
            submit.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event)
                {
                    if (selectedConcept.getText().length() == 0
                            || selectedConcept.getText().equals("--None--")
                            || position2.getValue(position2.getSelectedIndex()).equals(""))
                    {
                        Window.alert(constants.schemeCompleteInfo());
                    }
                    else
                    {
                        AddCategory.this.hide();
                        showLoading();
                        submit.setEnabled(false);

                        String pos = position2.getValue(position2.getSelectedIndex());
                        String conceptName = (String) selectedConcept.getValue();
                        

                        AsyncCallback<Object> callback = new AsyncCallback<Object>() {
                            public void onSuccess(Object results)
                            {
                                String targetItemURI = (String) results;
                                reloadWithTargetItem(schemeListBox.getSelectedIndex(), schemeObject.getSchemeInstance(), targetItemURI);
                                ModuleManager.resetValidation();
                            }

                            public void onFailure(Throwable caught)
                            {
                                Window.alert(constants.schemeAddCategoryFromScratchFail());
                            }
                        };
                        ConceptObject parentObj = getParentOfSelectedConcept();
                        if (!pos.equals("child"))
                        {
                        	if(parentObj != null)
                        	{
                        		refConcept = parentObj;
                        	}
                        	else
                        	{
                        		ConceptObject cReferenceObj = new ConceptObject();
                        		cReferenceObj.setName(ModelConstants.CCATEGORY);
                    	    	refConcept = cReferenceObj;
                        	}
                            pos = "child";
                        }
                        OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptCreate);
                        int actionId = Integer.parseInt((String) initData.getActionMap().get(ConceptActionKey.conceptCreate));
                        Service.classificationService.makeLinkToConcept(MainApp.userOntology, MainApp.userId, actionId, status, refConcept, pos, conceptName, schemeObject, callback);
                    }
                }
            });
            Button cancel = new Button(constants.buttonCancel());
            cancel.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event)
                {
                    AddCategory.this.hide();
                }
            });

            HorizontalPanel buttonPanel = new HorizontalPanel();
            buttonPanel.setSpacing(5);
            buttonPanel.add(submit);
            buttonPanel.add(cancel);

            HorizontalPanel hp2 = new HorizontalPanel();
            hp2.setSpacing(0);
            hp2.setWidth("100%");
            hp2.setStyleName("bottombar");
            hp2.add(buttonPanel);
            hp2.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);

            VerticalPanel v = new VerticalPanel();
            v.setSpacing(5);
            v.setWidth("100%");
            v.add(GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1"));

            VerticalPanel vp = new VerticalPanel();
            vp.add(v);
            vp.add(hp2);
            vp.setWidth("100%");
            vp.addStyleName("borderbar");

            return vp;
        }
    }

    public class AddFirstCategory extends FormDialogBox {
        private DecoratedTabPanel tp;
        private TextBox name;
        private ListBox language;
        private LabelAOS selectedConcept;
        private Image browse;
        private SchemeObject schemeObject;

        public AddFirstCategory(SchemeObject schemeObject)
        {
            super(constants.buttonAdd(), constants.buttonCancel(), false);
            this.schemeObject = schemeObject;
            this.setText(constants.schemeAddNewCategory());
            setWidth("400px");
            this.initLayout();
        }

        public void initLayout()
        {
            VerticalPanel vp1 = new VerticalPanel();
            vp1.add(getCreateFromScratch());
            vp1.setSize("100%", "300px");

            VerticalPanel vp2 = new VerticalPanel();
            vp2.add(getCreateBySelectConcept());
            vp2.setSize("100%", "300px");
            vp2.setCellVerticalAlignment(vp2.getWidget(0), HasVerticalAlignment.ALIGN_TOP);

            tp = new DecoratedTabPanel();
            tp.setAnimationEnabled(true);
            tp.add(vp1, constants.schemeCreateFromScratch());
            tp.add(vp2, constants.schemeCreateByConcept());
            tp.setWidth("100%");
            tp.selectTab(0);

            addWidget(tp);
        }

        private VerticalPanel getCreateFromScratch()
        {
            name = new TextBox();
            name.setWidth("100%");

            language = Convert.makeListBoxWithValue((ArrayList<String[]>) MainApp.getLanguage());
            language.setWidth("100%");

            Grid table = new Grid(2, 2);
            table.setWidget(0, 0, new HTML(constants.schemeName()));
            table.setWidget(1, 0, new HTML(constants.schemeLanguage()));
            table.setWidget(0, 1, name);
            table.setWidget(1, 1, language);
            table.setWidth("100%");
            table.getColumnFormatter().setWidth(1, "80%");
            final Button submit = new Button(constants.buttonSubmit());
            submit.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event)
                {

                    if (language.getValue((language.getSelectedIndex())).equals("")
                            || language.getValue(language.getSelectedIndex()).equals("--None--")
                            || name.getText().length() == 0)
                    {
                        Window.alert(constants.schemeCompleteInfo());
                    }
                    else
                    {
                        AddFirstCategory.this.hide();
                        showLoading();
                        submit.setEnabled(false);
                        TermObject termObject = new TermObject();
                        termObject.setLabel(name.getText());
                        termObject.setLang(language.getValue(language.getSelectedIndex()));
                        termObject.setUri("temp");
                        termObject.setName("temp");
                        termObject.setMainLabel(true);

                        ConceptObject concept = new ConceptObject();

                        AsyncCallback<Object> callback = new AsyncCallback<Object>() {
                            public void onSuccess(Object results)
                            {
                                String targetItemURI = (String) results;
                                reloadWithTargetItem(schemeListBox.getSelectedIndex(), schemeObject.getSchemeInstance(), targetItemURI);
                                ModuleManager.resetValidation();
                            }

                            public void onFailure(Throwable caught)
                            {
                                Window.alert(constants.schemeAddCategoryFromScratchFail());
                            }
                        };

                        OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptCreate);
                        int actionId = Integer.parseInt((String) initData.getActionMap().get(ConceptActionKey.conceptCreate));

                        Service.classificationService.addFirstNewCategory(MainApp.userOntology, actionId, MainApp.userId, status, termObject, concept, schemeObject, callback);
                    }
                }
            });
            Button cancel = new Button(constants.buttonCancel());
            cancel.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event)
                {
                    AddFirstCategory.this.hide();
                }
            });

            HorizontalPanel buttonPanel = new HorizontalPanel();
            buttonPanel.setSpacing(5);
            buttonPanel.add(submit);
            buttonPanel.add(cancel);

            HorizontalPanel hp = new HorizontalPanel();
            hp.setSpacing(0);
            hp.setWidth("100%");
            hp.setStyleName("bottombar");
            hp.add(buttonPanel);
            hp.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);

            VerticalPanel v = new VerticalPanel();
            v.setSpacing(5);
            v.setWidth("100%");
            v.add(GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1"));

            VerticalPanel vp = new VerticalPanel();
            vp.add(v);
            vp.add(hp);
            vp.addStyleName("borderbar");
            vp.setWidth("100%");

            return vp;
        }

        private VerticalPanel getCreateBySelectConcept()
        {
            selectedConcept = new LabelAOS("--None--");

            browse = new Image("images/browseButton3-grey.gif");
            browse.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event)
                {
                	final ConceptBrowser cb =((MainApp) RootPanel.get().getWidget(0)).conceptBrowser; 
    				cb.showBrowser();
    				cb.addSubmitClickHandler(new ClickHandler()
    				{
    					public void onClick(ClickEvent event) {
    						selectedConcept.setText(cb.getSelectedItem(),cb.getTreeObject().getName());
    					}					
    				});	
                }
            });
            DOM.setStyleAttribute(browse.getElement(), "cursor", "pointer");

            HorizontalPanel hp = new HorizontalPanel();
            hp.add(selectedConcept);
            hp.add(browse);
            hp.setCellHorizontalAlignment(selectedConcept, HasHorizontalAlignment.ALIGN_LEFT);
            hp.setCellHorizontalAlignment(browse, HasHorizontalAlignment.ALIGN_RIGHT);
            hp.setWidth("100%");

            Grid table = new Grid(1, 2);
            table.setWidget(0, 0, new HTML(constants.schemeConcept()));
            table.setWidget(0, 1, hp);
            table.setWidth("100%");
            table.getColumnFormatter().setWidth(1, "80%");

            final Button submit = new Button(constants.buttonSubmit());
            submit.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event)
                {
                    if (selectedConcept.getText().length() == 0
                            || selectedConcept.getText().equals("--None--"))
                    {
                        Window.alert(constants.schemeCompleteInfo());
                    }
                    else
                    {
                        AddFirstCategory.this.hide();
                        showLoading();
                        submit.setEnabled(false);

                        String conceptName = (String) selectedConcept.getValue();
                        AsyncCallback<Object> callback = new AsyncCallback<Object>() {
                            public void onSuccess(Object results)
                            {
                                String targetItemURI = (String) results;
                                reloadWithTargetItem(schemeListBox.getSelectedIndex(), schemeObject.getSchemeInstance(), targetItemURI);
                                ModuleManager.resetValidation();
                            }

                            public void onFailure(Throwable caught)
                            {
                                Window.alert(constants.schemeAddCategoryFromScratchFail());
                            }
                        };
                        OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptCreate);
                        int actionId = Integer.parseInt((String) initData.getActionMap().get(ConceptActionKey.conceptCreate));

                        Service.classificationService.makeLinkToFirstConcept(MainApp.userOntology, actionId, MainApp.userId, status, conceptName, schemeObject, callback);
                    }
                }
            });
            Button cancel = new Button(constants.buttonCancel());
            cancel.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event)
                {
                    AddFirstCategory.this.hide();
                }
            });

            HorizontalPanel buttonPanel = new HorizontalPanel();
            buttonPanel.setSpacing(5);
            buttonPanel.add(submit);
            buttonPanel.add(cancel);

            HorizontalPanel hp2 = new HorizontalPanel();
            hp2.setSpacing(0);
            hp2.setWidth("100%");
            hp2.setStyleName("bottombar");
            hp2.add(buttonPanel);
            hp2.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);

            VerticalPanel v = new VerticalPanel();
            v.setSpacing(5);
            v.setWidth("100%");
            v.add(GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1"));

            VerticalPanel vp = new VerticalPanel();
            vp.add(v);
            vp.add(hp2);
            vp.setWidth("100%");
            vp.addStyleName("borderbar");
            
            return vp;
        }
    }

    public class DeleteCategory extends FormDialogBox {
        private SchemeObject schemeObject;
        private ConceptObject cObj;

        public DeleteCategory(ConceptObject cObj,SchemeObject schemeObject)
        {
            super(constants.buttonDelete(), constants.buttonCancel());
            this.cObj = cObj;
            this.schemeObject = schemeObject;
            setWidth("400px");
            this.setText(constants.schemeDeleteCategory());
            this.initLayout();
        }

        public void initLayout()
        {
            HTML message = new HTML(messages.schemeCategoryDeleteWarning(getConceptText(cObj)));

            Grid table = new Grid(1, 2);
            table.setWidget(0, 0, getWarningImage());
            table.setWidget(0, 1, message);

            addWidget(table);
        }
        
        public String getConceptText(ConceptObject cObj){
			String text = "";
			HashMap<String, TermObject> map = cObj.getTerm();
			Iterator<String> it = map.keySet().iterator();
			while(it.hasNext()){
				String termIns = (String) it.next();
				TermObject tObj = (TermObject) map.get(termIns);
				if(text.equals(""))
					text +=  tObj.getLabel()+" ("+tObj.getLang()+")";	
				else
					text += ", "+tObj.getLabel()+" ("+tObj.getLang()+")";
			}
			return text;
		}
        
        public void onSubmit()
        {
            showLoading();
            AsyncCallback<Object> callback = new AsyncCallback<Object>() {
                public void onSuccess(Object results)
                {
                    reloadWithTargetItem(0, schemeObject.getSchemeInstance(), cObj.getUri());
                    ModuleManager.resetValidation();
                }

                public void onFailure(Throwable caught)
                {
                    Window.alert(constants.schemeDeleteCategoryFail());
                }
            };

            OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptDelete);
            int actionId = Integer.parseInt((String) initData.getActionMap().get(ConceptActionKey.conceptDelete));

            Service.classificationService.deleteCategory(MainApp.userOntology, actionId, MainApp.userId, status, cObj, schemeObject, callback);
        }
    }

}
