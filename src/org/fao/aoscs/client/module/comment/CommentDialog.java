package org.fao.aoscs.client.module.comment;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.utility.GridStyle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;


public class CommentDialog extends Composite{
	
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private VerticalPanel panel = new VerticalPanel();
	HTML htmltext = new HTML();
	private static TextArea txtcomments = new TextArea();
	private static TextBox txtname = new TextBox();
	private static TextBox txtemail = new TextBox();
	
	public CommentDialog(){
		txtcomments.setSize("300", "100");
		txtname.setWidth("300");
		txtemail.setWidth("300");
		final Grid ft_comment = new Grid(3,2);
		ft_comment.setWidget(0,0, new HTML(constants.commentComments()));
		ft_comment.setWidget(0,1, txtcomments);
		ft_comment.setWidget(1,0, new HTML(constants.commentName()+" ("+constants.commentOptional()+")"));
		ft_comment.setWidget(1,1, txtname);
		ft_comment.setWidget(2,0, new HTML(constants.commentEmail()+" ("+constants.commentOptional()+")"));
		ft_comment.setWidget(2,1, txtemail);		
		panel.add(GridStyle.setTableConceptDetailStyleleft(ft_comment, "gslRow1" , "gslCol1", "gslPanel1"));
		initWidget(panel);
	}
	
	public void resetForm()
	{
		txtcomments.setText("");
		txtname.setText("");
		txtemail.setText("");
	}
	public String getComment()
	{
		return txtcomments.getText();
	}
	public String getName()
	{
		return txtname.getText();
	}
	public String getEmail()
	{
		return txtemail.getText();
	}
}
