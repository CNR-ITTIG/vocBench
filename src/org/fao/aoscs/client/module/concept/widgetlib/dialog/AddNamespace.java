package org.fao.aoscs.client.module.concept.widgetlib.dialog;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.FlexDialogBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

public class AddNamespace extends FlexDialogBox {

    private TextBox namespacePrefix;
    private TextBox namespace;
    private ListBox namespaceList;
    
    private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	public AddNamespace(ListBox namespaceList)
    {
        super(constants.buttonAdd(), constants.buttonCancel());
        this.setText(constants.conceptAddNamespace());
        this.namespaceList = namespaceList;
        setWidth("400px");
        this.initLayout();
    }

    public void initLayout()
    {
        namespacePrefix = new TextBox();
        namespacePrefix.setWidth("100%");

        namespace = new TextBox();
        namespace.setWidth("100%");

        Grid table = new Grid(2, 2);
        table.setWidget(0, 0, new HTML(constants.conceptNamespacePrefix()));
        table.setWidget(1, 0, new HTML(constants.conceptNamespace()));
        table.setWidget(0, 1, namespacePrefix);
        table.setWidget(1, 1, namespace);
        table.setWidth("100%");
        table.getColumnFormatter().setWidth(1, "80%");

        addWidget(GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1"));
    }

    public boolean passCheckInput()
    {
        boolean pass = false;
        if (namespacePrefix.getText().length() == 0
                || namespace.getText().length() == 0)
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
        AsyncCallback<Object> callback = new AsyncCallback<Object>() {
            public void onSuccess(Object results)
            {
            	namespaceList.addItem(namespace.getValue(), namespacePrefix.getValue());
            	int cnt = namespaceList.getItemCount();
            	if(cnt>0) cnt = cnt-1;
            	namespaceList.setSelectedIndex(cnt);
            	hide();
            }
            public void onFailure(Throwable caught)
            {
                Window.alert(constants.conceptAddNamespaceFail());
            }
        };

        Service.conceptService.addNewNamespace(MainApp.userOntology, namespacePrefix.getValue(), namespace.getValue(), callback);
    }
}
