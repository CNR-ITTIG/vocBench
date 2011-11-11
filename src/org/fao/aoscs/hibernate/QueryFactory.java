package org.fao.aoscs.hibernate;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class QueryFactory {
	
	@SuppressWarnings("unchecked")
	public static ArrayList<String[]> getHibernateSQLQuery(String query){
		ArrayList<String[]> result = new ArrayList<String[]>();
		try 
		{
			Session s = HibernateUtilities.currentSession();
			Iterator<Object> itr = s.createSQLQuery(query).list().iterator();
			while(itr.hasNext())
			{
				Object o = itr.next(); 
				if(o instanceof Object[])
				{
					if(o!=null)
					{
						Object[] row = (Object[]) o;
						String[] tmp = new String[row.length];
						for(int i=0;i<row.length;i++)
						{
							if(row[i]!=null)	
								tmp[i] = ""+row[i];
							else
								tmp[i] = "";
						}
						result.add(tmp);	
					}
					
				}
				else
				{
					if(o != null)
					{
						String[] tmp = new String[]{o.toString()};
						result.add(tmp);
					}
				}	
			}
			s.flush();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			HibernateUtilities.closeSession();
		}
		return result;
	}
	
	public static void hibernateExecuteSQLUpdate(ArrayList<String> commandList){
		try 
		{
			
			Session s = HibernateUtilities.currentSession();		
			Transaction tx= s.beginTransaction();
			for(int i=0;i<commandList.size();i++){
				s.createSQLQuery((String)commandList.get(i)).executeUpdate();
			}
			tx.commit();
			s.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			HibernateUtilities.closeSession();
		}
	}
	
	public static int hibernateExecuteSQLUpdate(String sql){
		int result = 0;
		try 
		{
			Session s = HibernateUtilities.currentSession();		
			Transaction tx= s.beginTransaction();
			result = s.createSQLQuery(sql).executeUpdate();								
			tx.commit();
			s.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			HibernateUtilities.closeSession();
		}
		return result;
	}

	public static String escapeSingleQuote(String value){
		return value.replace("'", "''");
	}
}
