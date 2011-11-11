package org.fao.aoscs.hibernate;

import java.util.ResourceBundle;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;

public class HibernateUtilities 
{
	
	//----
	// Constants
	//----
	/**
	 * The stateful configuration file
	 */
	private static final String CONFIGURATION_FILE = "hibernate.cfg.xml";
	public static SessionFactory sessionFactory;
	public static Configuration hbConfig;
	public static final ThreadLocal<Session> session = new ThreadLocal<Session>();
	
	private static String dbUrl;
	private static String dbUsername; 
	private static String dbPassword; 
	
    static {
        try {
        	// Get database properties from config file
        	ResourceBundle rb = ResourceBundle.getBundle("Config");
    		dbUrl = rb.getString("dbConnectionUrl");
    		dbUsername = rb.getString("dbUsername");
    		dbPassword = rb.getString("dbPassword");
    		
        	hbConfig = new Configuration();
        	hbConfig.setProperty("hibernate.connection.url", dbUrl);
        	hbConfig.setProperty("hibernate.connection.username", dbUsername);
        	hbConfig.setProperty("hibernate.connection.password", dbPassword);
        	hbConfig.configure(CONFIGURATION_FILE);
        	
        	// Create the SessionFactory from hibernate.cfg.xml
        	sessionFactory = hbConfig.buildSessionFactory();
        	//PersistentBeanManager.getInstance().setSessionFactory(sessionFactory);
        	
        	
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory()
	{
		if (sessionFactory == null)
		{
			try {
				hbConfig = new Configuration();
	        	hbConfig.setProperty("hibernate.connection.url", dbUrl);
	        	hbConfig.setProperty("hibernate.connection.username", dbUsername);
	        	hbConfig.setProperty("hibernate.connection.password", dbPassword);
	        	hbConfig.configure(CONFIGURATION_FILE);
	        	
				// Create the SessionFactory from hibernate.cfg.xml				
	        	sessionFactory = hbConfig.buildSessionFactory();
			} 
			catch (Throwable ex) 
			{
				// Make sure you log the exception, as it might be swallowed
				ex.printStackTrace();
				throw new ExceptionInInitializerError(ex);
			}
		}
		return sessionFactory;
	}

    public static Session currentSession() throws HibernateException {
        Session s = (Session) session.get();
        // Open a new Session, if this Thread has none yet
        if (s == null) {
            s = getSessionFactory().openSession();
            session.set(s);
        }
        return s;
    }

    public static void closeSession() throws HibernateException {
        Session s = (Session) session.get();
        session.set(null);
        if (s != null)
            s.close();
    }

}
