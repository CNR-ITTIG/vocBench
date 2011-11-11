package org.fao.aoscs.utility;

import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtil {

	public static void sendMail(String to, String subject, String body)
	{
		ResourceBundle rb = ResourceBundle.getBundle("Config");
		String host = rb.getString("host");
		int port = Integer.parseInt(rb.getString("port"));
		String user = rb.getString("user");
		String password = rb.getString("password");
		String from = rb.getString("from");
		String toAdmin = to;
		if(to.equals("ADMIN"))
			toAdmin = rb.getString("admin");
		sendMessage(host, port, user, password, from, toAdmin, null, null , subject, body);
	}
	public static void sendMail(String to, String cc, String subject, String body)
	{
		ResourceBundle rb = ResourceBundle.getBundle("Config");
		String host = rb.getString("host");
		int port = Integer.parseInt(rb.getString("port"));
		String user = rb.getString("user");
		String password = rb.getString("password");
		String from = rb.getString("from");
		sendMessage(host, port, user, password, from, to, cc, null , subject, body);
	}
	public static void sendMail(String to, String cc, String bcc, String subject, String body)
	{
		ResourceBundle rb = ResourceBundle.getBundle("Config");
		String host = rb.getString("host");
		int port = Integer.parseInt(rb.getString("port"));
		String user = rb.getString("user");
		String password = rb.getString("password");
		String from = rb.getString("from");
		sendMessage(host, port, user, password, from, to, cc, bcc, subject, body);
	}
	
	private static void sendMessage(final String host, final int port, final String user, final String password, String from, String to, String cc, String bcc, String subject, String body)
	{
		try
		{
			Properties props = new Properties();
			props.put("mail.transport.protocol", "smtps");
			props.put("mail.smtps.host", host);
			props.put("mail.smtps.auth", "true");
			props.put("mail.smtps.quitwait", "false");
			
			Session mailConnection = Session.getDefaultInstance(props);
			mailConnection.setDebug(false);
			final Transport transport = mailConnection.getTransport();
			
			final Message message = new MimeMessage(mailConnection);
			message.setContent(body, "text/plain;charset=UTF8");
			message.setFrom(new InternetAddress(from));
			if(to!=null)
				message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
			if(cc!=null)
				message.setRecipients(Message.RecipientType.CC,InternetAddress.parse(cc, false));
			if(bcc!=null)
				message.setRecipients(Message.RecipientType.BCC,InternetAddress.parse(bcc, false));
			message.setSubject(subject);
			
			Runnable r = new Runnable() {
	            public void run() {
	            	try 
	            	{
	            	  	transport.connect(host, port, user, password);     
		      			transport.sendMessage(message,message.getAllRecipients());
		      			transport.close();
	            	}
	            	catch (Exception e) 
	            	{
	            		e.printStackTrace(); 
	            	}
	            } 
	          };
	          Thread t = new Thread(r);
	          t.start();
		}
		catch(Exception e)
		{
			System.out.println("Mail send failed: "+e.getMessage());
		}
	}
	
}

