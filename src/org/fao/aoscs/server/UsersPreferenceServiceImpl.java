package org.fao.aoscs.server;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletException;

import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.gwt.GwtConfigurationHelper;
import net.sf.gilead.gwt.PersistentRemoteService;

import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.module.preferences.service.UsersPreferenceService;
import org.fao.aoscs.domain.InitializeUsersPreferenceData;
import org.fao.aoscs.domain.Users;
import org.fao.aoscs.domain.UsersLanguage;
import org.fao.aoscs.domain.UsersOntology;
import org.fao.aoscs.domain.UsersPreference;
import org.fao.aoscs.hibernate.DatabaseUtil;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.hibernate.QueryFactory;
import org.fao.aoscs.server.utility.Encrypt;

public class UsersPreferenceServiceImpl extends PersistentRemoteService implements UsersPreferenceService{
	
	private static final long serialVersionUID = -891506457037442726L;
	
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

	public  InitializeUsersPreferenceData getInitData(int userID){
		InitializeUsersPreferenceData initUsersPreference = new InitializeUsersPreferenceData();
		initUsersPreference.setUsersPreference(getUsersPreference(userID));
		initUsersPreference.setUsersInfo(getUser(userID));
		initUsersPreference.setUserLanguage(getUsersLanguage(userID));
		initUsersPreference.setOntology(new SystemServiceImpl().getOntology(""+userID));
		initUsersPreference.setInterfaceLanguage(new SystemServiceImpl().getInterfaceLang());
		return initUsersPreference;
	}
	
	public Users getUser(int userID) {
		Users user = new Users();
		try 
		{
			String query = "SELECT * FROM users where user_id='" + userID + "'";
			for (Iterator<?> iter = HibernateUtilities.currentSession().createSQLQuery(query).addEntity(Users.class).list().iterator(); iter.hasNext();) {
				user = (Users) iter.next();
			}
			return user;

		} catch (Exception e) {
			e.printStackTrace();
			return user;
		} finally {
			HibernateUtilities.closeSession();
		}
	}
	
	
	
	public Users updateUsers(Users users , boolean isPasswordChange) {
		try
		{
			if(isPasswordChange)
			{
				users.setPassword(Encrypt.MD5(users.getPassword()));
				DatabaseUtil.update(users, true);
			}
			else
			{
				String query = "update users set email = '"+users.getEmail()+"' where user_id='" + users.getUserId() + "'";
				QueryFactory.hibernateExecuteSQLUpdate(query);
			}
			return users;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<UsersLanguage> getUsersLanguage(int userID)
	{
		String query = "SELECT * FROM users_language where user_id='" + userID + "'";
		return (ArrayList<UsersLanguage>) HibernateUtilities.currentSession().createSQLQuery(query).addEntity(UsersLanguage.class).list();
	}
	
	public ArrayList<String[]> getNonAssignedOntology(int userID)
	{
		String query = "";
		
		// if VISITOR then load read only ontology
		if(ConfigConstants.ISVISITOR){
			query = " version ='"+ ConfigConstants.VERSION +"' AND ontology_show='2' ";
		}
		else{
			query = " version ='"+ ConfigConstants.VERSION +"' AND ontology_show='1' ";
		}
		
		query += " AND ontology_id NOT IN(SELECT ontology_id FROM users_ontology WHERE user_id='"+userID+"')"; 
		
		query = "SELECT ontology_name,ontology_id FROM ontology_info WHERE " + query;
		
		return QueryFactory.getHibernateSQLQuery(query);
		
	}
	
	public ArrayList<String[]> getPendingOntology(int userID)
	{
		String query = "";
		
		// if VISITOR then load read only ontology
		if(ConfigConstants.ISVISITOR){
			query = " version ='"+ ConfigConstants.VERSION +"' AND ontology_show='2'";
		}
		else{
			query = " version ='"+ ConfigConstants.VERSION +"' AND ontology_show='1'";
		}
		
		query += " AND ontology_id IN(SELECT ontology_id FROM users_ontology WHERE user_id='"+userID+"' AND status=0)"; 
		
		query = "SELECT ontology_name,ontology_id FROM ontology_info WHERE "+query;
		
		return QueryFactory.getHibernateSQLQuery(query);
		
	}
	
	public void addUsersOntology(ArrayList<UsersOntology> usersOntologyList)
	{
		for(UsersOntology usersOntology: usersOntologyList)
		{
			try
			{
				DatabaseUtil.createObject(usersOntology);				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public ArrayList<UsersLanguage> updateUsersLanguage(int userID, ArrayList<String> langList)
	{
		String query = "delete from users_language where user_id='" + userID + "'";
		QueryFactory.hibernateExecuteSQLUpdate(query);
		
		try
		{
			if(langList.size()>0)
			{
				String query1 = "";
				for(String lang : langList)
				{	
					if(query1.length()>0)	query1 += ", ";
					query1 += "("+userID+",'"+lang+"', 1)";
				}
				
				query1 = "INSERT INTO users_language VALUES "+query1+";";
				
				QueryFactory.hibernateExecuteSQLUpdate(query1);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
			
		
		return getUsersLanguage(userID);
	}
	
	public UsersPreference getUsersPreference(int userID) {
		UsersPreference userPreference = new UsersPreference();
		try {
			String query =  "select * from users_preference where user_id='" + userID + "'";
			for (Iterator<?> iter = HibernateUtilities.currentSession().createSQLQuery(query).addEntity(UsersPreference.class).list().iterator(); iter.hasNext();) {
				userPreference = (UsersPreference) iter.next();
			}
			return userPreference;

		} catch (Exception e) {
			e.printStackTrace();
			return userPreference;
		} finally {
			HibernateUtilities.closeSession();
		}
	}
	
	public UsersPreference addUsersPreference(UsersPreference usersPreference) {
		try
		{
			DatabaseUtil.createObject(usersPreference);
			return usersPreference;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public UsersPreference updateUsersPreference(UsersPreference usersPreference) {
		try
		{				
			DatabaseUtil.update(usersPreference, true);
			return usersPreference;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

}
