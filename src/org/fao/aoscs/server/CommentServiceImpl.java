package org.fao.aoscs.server;


import java.util.ArrayList;

import javax.servlet.ServletException;

import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.gwt.GwtConfigurationHelper;
import net.sf.gilead.gwt.PersistentRemoteService;

import org.fao.aoscs.client.module.comment.service.CommentService;
import org.fao.aoscs.domain.UserComments;
import org.fao.aoscs.hibernate.DatabaseUtil;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.server.utility.Date;
import org.fao.aoscs.utility.MailUtil;
import org.hibernate.Session;


public class CommentServiceImpl extends PersistentRemoteService implements CommentService {

	private static final long serialVersionUID = 5376298781734858824L;

	//-------------------------------------------------------------------------
	//
	// Initialization of Remote service : must be done before any server call !
	//
	//-------------------------------------------------------------------------
	@Override
	public void init() throws ServletException
	{
		super.init();
		
	//	Bean Manager initialization
		setBeanManager(GwtConfigurationHelper.initGwtStatelessBeanManager( new HibernateUtil(HibernateUtilities.getSessionFactory())));;
		
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<UserComments> getComments(String module){
		String query = "SELECT * FROM user_comments WHERE module='"+module+"'";
		try
		{
			Session s = HibernateUtilities.currentSession();
			ArrayList<UserComments> list = (ArrayList) s.createSQLQuery(query).addEntity(UserComments.class).list();
			return list;
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new ArrayList<UserComments>();
		}
		finally
		{
			HibernateUtilities.closeSession();
		}
	}
	
	public String sendComment(UserComments uc){
		boolean returnvalue;
		UserComments hibernateUserComments = uc;
		hibernateUserComments.setCommentDate(Date.getROMEDate());
		try {	   	  
			DatabaseUtil.createObject(hibernateUserComments);
			returnvalue = true;
		    } catch(Exception e) {
		    	e.printStackTrace();
		    	returnvalue=false;
		  }	
		  if(returnvalue)
		  {
			  if(!hibernateUserComments.getEmail().equals(""))
			  {
				  String to = hibernateUserComments.getEmail();
				  String subject = "Your feedback for the VocBench Alpha testing";
				  String body = "";
				  if(hibernateUserComments.getName().equals(""))
					  body += "Dear Guest,";
				  else
					  body += "Dear "+hibernateUserComments.getName()+",";
				  body += "\n\nThank you for providing your valuable feedback on our preliminary alpha test version of the new VocBench. You are taking part in the development of an enhanced multi-user multilingual management system for terminology and concepts in the domain of agriculture. We will carefully evaluate your feedback and try to incorporate it into our development. We might contact you in case of further questions.";
				  body += "\n\nWould you like to be updated via email of future developments and releases and stay informed about the ongoing development of this tool? If so, please reply to this email and we will keep you updated in the ongoing development efforts.";
				  body += "\n\nThanks a lot for your interest.";
				  body += "\n\nRegards,";
				  body += "\nThe VocBench team.";
				  MailUtil.sendMail(to, subject, body);
			  }
		  }
		return ""+returnvalue;	
	}
	
}
