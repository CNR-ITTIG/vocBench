package org.fao.aoscs.client.widgetlib.shared.dialog;

import java.util.ArrayList;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.classification.widgetlib.OlistBox;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.domain.DomainRangeDatatypeObject;
import org.fao.aoscs.domain.InitializeSearchData;
import org.fao.aoscs.domain.NonFuncObject;
import org.fao.aoscs.domain.RelationshipObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;

public class AddValue extends FormDialogBox implements ClickHandler{ 
    
    private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
    private String property ;
    private TextArea value;
    private ListBox language;
    private OlistBox relationship;
    private ListBox values;
    
    public RelationshipObject rObj = new RelationshipObject();
    private ArrayList<String> list = null;
    private String type = "";
    private InitializeSearchData initData;
    public NonFuncObject nonFuncObj;
    public static String CONCEPTDOMAINEDITORIALDATATYPEPROPERTY = "C";
	public static String TERMDOMAINEDITORIALDATATYPEPROPERTY = "T";

    public AddValue(InitializeSearchData initData, String property){
        super(constants.buttonAdd(), constants.buttonCancel());
        this.initData = initData;
        String label = constants.conceptAddAttributes();
        this.setText(label);
        setWidth("400px");
        this.property = property;
        this.initLayout();        
    }
    
    public AddValue(InitializeSearchData initData, String property, String title){
        super(constants.buttonAdd(), constants.buttonCancel());
        this.initData = initData;
        String label = title;
        this.setText(label);
        setWidth("400px");
        this.property = property;
        this.initLayout();        
    }
    
    public void initLayout() {
        value = new TextArea();
        value.setVisibleLines(3);
        value.setWidth("100%");
        
        language = new ListBox();
        language = Convert.makeListBoxWithValue((ArrayList<String[]>)MainApp.getLanguage());
        language.setWidth("100%");
        
        values = new ListBox();
        values.setWidth("100%");
        
        if(property.equals(CONCEPTDOMAINEDITORIALDATATYPEPROPERTY)){
        	 ArrayList<RelationshipObject> list = new ArrayList<RelationshipObject>();
        	 list.addAll(initData.getConceptEditorialAttributes());
        	 list.addAll(initData.getConceptDomainAttributes());
            relationship = Convert.makeOListBoxWithValue(list);
        }
        else if(property.equals(TERMDOMAINEDITORIALDATATYPEPROPERTY)){
        	 ArrayList<RelationshipObject> list = new ArrayList<RelationshipObject>();
        	 list.addAll(initData.getTermEditorialAttributes());
        	 list.addAll(initData.getTermDomainAttributes());
        	 relationship = Convert.makeOListBoxWithValue(list);
        }
        
        /*if(property.equals(ModelConstants.RCONCEPTEDITORIALDATATYPEPROPERTY)){
            relationship = Convert.makeOListBoxWithValue(initData.getConceptEditorialAttributes());
        }
        else if(property.equals(ModelConstants.RCONCEPTDOMAINDATATYPEPROPERTY)){
            relationship = Convert.makeOListBoxWithValue(initData.getConceptDomainAttributes());
        }
        else if(property.equals(ModelConstants.RTERMEDITORIALDATATYPEPROPERTY)){
            relationship = Convert.makeOListBoxWithValue(initData.getTermEditorialAttributes());
        }
        else if(property.equals(ModelConstants.RTERMDOMAINDATATYPEPROPERTY)){
            relationship = Convert.makeOListBoxWithValue(initData.getTermDomainAttributes());
        }*/
      
        relationship.setWidth("100%");
        
        final FlexTable table = new FlexTable();
        table.setWidth("100%");
        table.getColumnFormatter().setWidth(1, "80%");
        table.setWidget(0, 0, new HTML(constants.conceptRelationship()));
        table.setWidget(0, 1, relationship);
        
        
        relationship.addChangeHandler(new ChangeHandler()
        {
            public void onChange(ChangeEvent event) {
                while(table.getRowCount()>1)
                {
                    table.removeRow(table.getRowCount()-1);
                }
                type = "";
                list = null;

                rObj = (RelationshipObject) relationship.getObject(relationship.getSelectedIndex());
                
                if(rObj.getName()!=null)
                {
                    if(rObj.getDomainRangeDatatypeObject()!=null)
                    {
                        DomainRangeDatatypeObject drObj = rObj.getDomainRangeDatatypeObject();
                        type = drObj.getRangeDataType();
                        list = drObj.getRangeValue();
                    }
                    
                    //check value or values
                    if(list!=null && list.size()>0)
                    {
                        values = Convert.makeListBoxSingleValueWithSelectedValue(list, "");
                        table.setWidget(1, 0, new HTML(constants.conceptValue()));
                        table.setWidget(1, 1, values);
                    }
                    else 
                    {
                        table.setWidget(1, 0, new HTML(constants.conceptValue()));
                        table.setWidget(1, 1, value);
                        // check language
                        if(!rObj.isFunctional() && type.equalsIgnoreCase("string"))
                        {
                            table.setWidget(2, 0, new HTML(constants.conceptLanguage()));
                            table.setWidget(2, 1, language);
                        }
                    }
                    GridStyle.updateTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1");
                }
            }
            
        });
        addWidget(GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1"));
    }
    public boolean passCheckInput() {
        if(relationship.getValue(relationship.getSelectedIndex()).equals("--None--")    || relationship.getValue(relationship.getSelectedIndex()).equals(""))
        {
            return false;
        }
        else
        {
            //check value or values
            if(list!=null && list.size()>0)
            {
                if(values.getValue(values.getSelectedIndex()).equals("--None--") || values.getValue(values.getSelectedIndex()).equals(""))
                {
                    return false;
                }
            }
            else 
            {
                if(value.getText().length()==0)
                {
                    return false;
                }
                // check language
                if(rObj.getName()!=null && !rObj.isFunctional() && type.equalsIgnoreCase("string"))
                {
                    if(language.getValue(language.getSelectedIndex()).equals("--None--") || language.getValue(language.getSelectedIndex()).equals(""))
                    {
                        return false;
                    }
                }
            }
            
        }
        return true;
    }
    public void onSubmit() {
        
      //sayLoading();
        
        nonFuncObj = new NonFuncObject();
        //check value or values
        if(list!=null && list.size()>0)
        {
            nonFuncObj.setValue(values.getValue(values.getSelectedIndex()));
            nonFuncObj.setLanguage(null);            
        }
        else 
        {
            nonFuncObj.setValue(value.getText());
            // check language
            if(rObj.getName()!=null && !rObj.isFunctional() && type.equalsIgnoreCase("string"))
            {
                nonFuncObj.setLanguage(language.getValue(language.getSelectedIndex()));
            }
        }        
    }
    
}
