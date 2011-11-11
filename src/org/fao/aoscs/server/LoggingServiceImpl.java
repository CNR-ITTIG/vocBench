package org.fao.aoscs.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;

import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.gwt.GwtConfigurationHelper;
import net.sf.gilead.gwt.PersistentRemoteService;

import org.fao.aoscs.client.module.logging.service.LoggingService;
import org.fao.aoscs.domain.UsersVisits;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.hibernate.QueryFactory;
import org.hibernate.Hibernate;
import org.hibernate.Query;

import com.google.gwt.gen2.table.client.TableModelHelper.ColumnSortInfo;
import com.google.gwt.gen2.table.client.TableModelHelper.ColumnSortList;
import com.google.gwt.gen2.table.client.TableModelHelper.Request;

public class LoggingServiceImpl extends PersistentRemoteService  implements LoggingService{

	private static final long serialVersionUID = 4993831639315721413L;
	
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

	public String startLog(String token, String ID){
		String ipadd = this.getThreadLocalRequest().getRemoteAddr();
		String tok = this.getThreadLocalRequest().getSession().getId();
		if( !ipadd.equals("127.0.0.1")){			
			if(tok.equals(token)){
				endLog(token);
				return tok;
			}
			try {					
				String sql = "INSERT INTO users_visits (token, ipaddress, country_code, logintime, lastvisittime, totallogintime, user_id) " +
				"VALUES('"+tok+"','"+ipadd+"','"+getCCFromIP(ipadd)+"', NOW(),NOW(),'00:00:00','"+ID+"')";				
				QueryFactory.hibernateExecuteSQLUpdate(sql);						
			} catch (Exception e) {		
				e.printStackTrace();
			}
		}
		return tok;	
	}
	
	public void endLog(String token){
		String ipadd = this.getThreadLocalRequest().getRemoteAddr();
		if( !ipadd.equals("127.0.0.1")){
			String sql = "UPDATE users_visits SET lastvisittime = NOW(), totallogintime = TIMEDIFF(NOW(), logintime) WHERE token = '"+token+"'";
			QueryFactory.hibernateExecuteSQLUpdate(sql);				
		}
	}
	
	public List<String[]> viewLog(){
		String query = "SELECT ipaddress, country_code.country_name, logintime, lastvisittime, totallogintime, users.username FROM users_visits ";
		query += " INNER  JOIN country_code ON country_code.country_code = users_visits.country_code ";
		query += " INNER  JOIN users ON users.user_id = users_visits.user_id order by lastvisittime desc LIMIT 0 , 30";
		return QueryFactory.getHibernateSQLQuery(query);
	}
	
	public List<String> getLogInfo(){
	
		String query1 = "select count(distinct(ipaddress)) as val from users_visits";
		query1 += " INNER  JOIN country_code ON country_code.country_code = users_visits.country_code ";
		query1 += " INNER  JOIN users ON users.user_id = users_visits.user_id";

		String query2 = "select Sum(Time_to_Sec(totallogintime)) as val from users_visits";
		query2 += " INNER  JOIN country_code ON country_code.country_code = users_visits.country_code ";
		query2 += " INNER  JOIN users ON users.user_id = users_visits.user_id";	
		
		String query3 = "select count(ipaddress) as val from users_visits";
		query3 += " INNER  JOIN country_code ON country_code.country_code = users_visits.country_code ";
		query3 += " INNER  JOIN users ON users.user_id = users_visits.user_id";
		
		List<String> list = new ArrayList<String>();		
		try{						
			Query q = HibernateUtilities.currentSession().createSQLQuery(query1).addScalar("val", Hibernate.STRING);
			list.add((String)q.uniqueResult());
			
			q = HibernateUtilities.currentSession().createSQLQuery(query2).addScalar("val", Hibernate.STRING);
			list.add((String)q.uniqueResult());

			q = HibernateUtilities.currentSession().createSQLQuery(query3).addScalar("val", Hibernate.STRING);
			list.add((String)q.uniqueResult());
			
			HibernateUtilities.closeSession();
		}catch(Exception e){
			e.printStackTrace();
		}		
		return list; 
	}	
	
	public String getCCFromIP(String ipaddress)
	{		
		//String query = "SELECT country_code FROM ip NATURAL JOIN country_code WHERE inet_aton('"+ipaddress+"') BETWEEN start AND end";
		String query = "SELECT country_code.country_code FROM country_code LEFT JOIN ip ON country_code.country_id = ip.ci WHERE inet_aton('"+ipaddress+"') BETWEEN ip.start AND ip.end";
		List<String[]> list = QueryFactory.getHibernateSQLQuery(query);		
		return list.size()==0? "" : list.get(0)[0];		
	}
	
	public long getLongFromIP(String pIPAddress)
	{
		String[] quads = pIPAddress.split( "\\." );
	    if( 4 != quads.length ) {
	      return 0;
	    }

	    long ipnum 
	      = (long)16777216*Long.parseLong(quads[0]) 
	      + (long)65536*Long.parseLong(quads[1])  
	      + (long)256*Long.parseLong(quads[2]) 
	      + (long)1*Long.parseLong(quads[3]) 
	      ;
	    
	    return ipnum;

	}
	
	  public ArrayList<UsersVisits> requestUsersVisitsRows(Request request) {
		  
		  HashMap<String, String> col = new HashMap<String, String>();

		   col.put("0", "users_visits.ipAddress");
		   col.put("1", "country_name");
		   col.put("2", "users_visits.logintime");
		   col.put("3", "users_visits.lastvisittime");
		   col.put("4", "users_visits.totallogintime");
		   col.put("5", "user_name");
		   String orderBy = " users_visits.lastvisittime desc ";
		   
		  // Get the sort info, even though we ignore it
		    ColumnSortList sortList = request.getColumnSortList();
		    if(sortList!=null)
		    {
			    sortList.getPrimaryColumn();
			    sortList.isPrimaryAscending();
			    ColumnSortInfo csi = sortList.getPrimaryColumnSortInfo();
			    
			    if(csi!=null)	
			    {
			    	if(csi.isAscending())
			    		orderBy = col.get(""+csi.getColumn()) + " ASC ";
			    	else
			    		orderBy = col.get(""+csi.getColumn()) + " DESC ";
			    }
		    }
		  	int startRow = request.getStartRow();
		    int numRow = request.getNumRows();
		    if(numRow <0) numRow=0;		    		  
		    
		    String query = "SELECT users_visits.* , country_code.country_name as country_name, users.username as user_name FROM users_visits ";
			query += " INNER  JOIN country_code ON country_code.country_code = users_visits.country_code ";
			query += " INNER  JOIN users ON users.user_id = users_visits.user_id order by "+orderBy+" LIMIT "+numRow+" OFFSET "+startRow;
		    		    
			ArrayList<UsersVisits> list = (ArrayList<UsersVisits>) HibernateUtilities.currentSession().createSQLQuery(query).addEntity("rc",UsersVisits.class).list();
			return list;					    		  
	  }
}
