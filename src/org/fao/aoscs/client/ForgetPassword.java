package org.fao.aoscs.client;

import java.util.ArrayList;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.utility.GridStyle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ForgetPassword extends Composite {
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private VerticalPanel panel = new VerticalPanel();
	private TextBox txtEmail = new TextBox();
	private TextBox txtUsername = new TextBox();
	private PasswordTextBox txtNewpwd = new PasswordTextBox();
	private PasswordTextBox txtcNewpwd = new PasswordTextBox();
	private Button btnChange = new Button(constants.fpChangePassword());
	private Button btnCancel = new Button(constants.buttonCancel());

	public ForgetPassword()
	{
		FlexTable flexpanelmain = new FlexTable();		
		flexpanelmain.setWidget(1, 0, new HTML(constants.fpUserName()));
		flexpanelmain.setWidget(1, 1, txtUsername);
		txtUsername.setSize("200px", "20px");
		flexpanelmain.setWidget(2, 0, new HTML(constants.fpEmail()));
		flexpanelmain.setWidget(2, 1, txtEmail);
		txtEmail.setSize("200px", "20px");
		flexpanelmain.setWidget(3, 0, new HTML(constants.fpNewPassword()));
		flexpanelmain.setWidget(3, 1, txtNewpwd);
		txtNewpwd.setSize("200px", "20px");
		flexpanelmain.setWidget(4, 0, new HTML(constants.fpConfirmPassword()));
		flexpanelmain.setWidget(4, 1, txtcNewpwd);
		txtcNewpwd.setSize("200px", "20px");	
		
		btnChange.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (!txtNewpwd.getText().equals(txtcNewpwd.getText())) {
					Window.alert(constants.fpPasswordMismatch());
				} else {
					if (txtNewpwd.getText().equals("")) {
						Window.alert(constants.fpPasswordEmpty());
					} else {
						if (txtNewpwd.getText().length() < 6) {
							Window.alert(constants.fpPasswordMinChar());
							txtNewpwd.setFocus(true);
						} else {
							AsyncCallback<ArrayList<String[]>> cbkCheckExistUser = new AsyncCallback<ArrayList<String[]>>() {
								public void onSuccess(ArrayList<String[]> tmp) {

									if (tmp.size() == 0) {
										Window.alert(constants.fpNoUser());
									} else {
										final String[] item = (String[]) tmp.get(0);
										AsyncCallback<Object> cbkUpdatepwd = new AsyncCallback<Object>() {
											public void onSuccess(Object result) {
												Main.mailAlert(item[2],
														item[3],
														txtEmail.getText(),
														txtUsername.getText(),
														txtNewpwd.getText());
												Main.gotoLoginScreen();
											}

											public void onFailure(
													Throwable caught) {
												Window.alert(constants.fpChangePasswordFail());
											}
										};
										String sql = "UPDATE users SET password = md5('"
												+ txtNewpwd.getText()
												+ "')"
												+ " WHERE username = '"
												+ txtUsername.getText() + "'";
										Service.queryService.hibernateExecuteSQLUpdate(sql, cbkUpdatepwd);
									}
								}

								public void onFailure(Throwable caught) {
									Window.alert(constants.fpChangePasswordFail());
								}
							};
							String sql = "SELECT username,email,first_name,last_name FROM users "
									+ "WHERE username='"
									+ txtUsername.getText()
									+ "'"
									+ " and email = '"
									+ txtEmail.getText()
									+ "'";
							Service.queryService.execHibernateSQLQuery(sql, cbkCheckExistUser);
						}
					}
				}
			}
		});
		
		btnCancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Main.gotoLoginScreen();
			}
		});
				
		// SAVE and CANCEL
		HorizontalPanel submithp = new HorizontalPanel();
		submithp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		submithp.setSpacing(5);		
		submithp.add(btnChange);
		submithp.add(btnCancel);
		
		HorizontalPanel buttonContainer = new HorizontalPanel();
		buttonContainer.setWidth("100%");
		buttonContainer.add(submithp);
		buttonContainer.setCellHorizontalAlignment(submithp, HasHorizontalAlignment.ALIGN_RIGHT);
		
		HorizontalPanel header = new HorizontalPanel();		
		header.setStyleName("titleLabel");		
		header.add(new HTML(constants.fpChangePassword()));
		
		VerticalPanel content = new VerticalPanel();
		content.setSpacing(10);
		content.add(GridStyle.setTableRowStyle(flexpanelmain, "#F4F4F4", "#E8E8E8", 5));														
		
		VerticalPanel vpMain = new VerticalPanel();
		DOM.setStyleAttribute(vpMain.getElement(), "border", "1px solid #F59131");
		vpMain.add(content);
		
		VerticalPanel everything = new VerticalPanel();		
		everything.setSpacing(5);		
		everything.add(header);
		everything.add(vpMain);
		everything.add(buttonContainer);
		
		
				
		panel.setSize("100%","100%");		
		panel.add(everything);			
		panel.setSpacing(5);
		panel.setCellHorizontalAlignment(everything,HasHorizontalAlignment.ALIGN_CENTER);
		initWidget(panel);
	}
}
