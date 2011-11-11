package org.fao.aoscs.server;

import java.util.ArrayList;

import javax.servlet.ServletException;

import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.gwt.GwtConfigurationHelper;
import net.sf.gilead.gwt.PersistentRemoteService;

import org.fao.aoscs.client.query.service.QueryService;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.hibernate.QueryFactory;


public class QueryServiceImpl extends PersistentRemoteService implements QueryService{
 
	private static final long serialVersionUID = -8827763623764714650L;
	
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

	public void hibernateExecuteSQLUpdate(ArrayList<String> queryList){
		QueryFactory.hibernateExecuteSQLUpdate(queryList);
	}
	
	public int hibernateExecuteSQLUpdate(String query){
		return QueryFactory.hibernateExecuteSQLUpdate(query);
	}
	
	public ArrayList<String[]> execHibernateSQLQuery(String query){
		return (ArrayList<String[]>)QueryFactory.getHibernateSQLQuery(query);
	}
	
}
