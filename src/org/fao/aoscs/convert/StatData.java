package org.fao.aoscs.convert;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.TermObject;
import org.fao.aoscs.hibernate.DatabaseUtil;

public class StatData {
	
	private static String jdbcUrl = "jdbc:mysql://vivaldi.cpe.ku.ac.th:3306/administrator_newdevelopment?requireSSL=false&useUnicode=true&characterEncoding=UTF-8";
    private static String jdbcDriver = "com.mysql.jdbc.Driver";
    private static String user = "root";
    private static String password = "onmodao";
    private static Connection conn = null;

	public static void main(String args[]){
		//StatData.getConceptStat("d:\\concept.csv");
		StatData.getTermStat("d:\\term.csv");
	}
	
	public static void getConceptStat(String filename)
	{
		String query = "";
		query = "SELECT rc.concept as val, rc.action, a.action, a.action_child, " +
				"rc.modifier_id, u.username, u.first_name, u.last_name, " +
				"u.country_code, c.country_name, rc.ontology_id, " +
				"o.ontology_name, rc.date_modified FROM validation rc " +
				"INNER JOIN owl_action a on a.id = rc.action " +
				"INNER JOIN users u on u.user_id = rc.modifier_id " +
				"INNER JOIN ontology_info o on o.ontology_id = rc.ontology_id " +
				"INNER JOIN country_code c on u.country_code = c.country_code " +
				"WHERE rc.date_modified BETWEEN '2008-11-17' and '2008-12-12' " +
				"AND rc.ontology_id >=22 " +
				"ORDER BY o.ontology_name, date_modified"; 
		ArrayList<String[]> sqlresult = new ArrayList<String[]>();		
		Statement statement = null;
		ResultSet result = null;
		try 
		{
			
			statement = getConnection().createStatement();   
			result = statement.executeQuery(query);	
			System.out.println(query);
			
			result.last();
			int i = result.getRow();
			System.out.println("Count: "+i);
			result.first();
			
			while(result.next())
			{
				String[] tmp = new String[13];
				tmp[0] = getConceptBlobValue(result.getBlob("val"));
				tmp[1] = result.getString(2);
				tmp[2] = result.getString(3);
				tmp[3] = result.getString(4);
				tmp[4] = result.getString(5);
				tmp[5] = result.getString(6);
				tmp[6] = result.getString(7);
				tmp[7] = result.getString(8);
				tmp[8] = result.getString(9);
				tmp[9] = result.getString(10);
				tmp[10] = result.getString(11);
				tmp[11] = result.getString(12);
				tmp[12] = result.getString(13);
				sqlresult.add(tmp);
		
			}
			statement.close();
		} 
		catch(SQLException e) 
		{
			e.printStackTrace();
		}	
		finally
		{
			close();
		}
		getStat(sqlresult, filename);
	}
	
	public static void getTermStat(String filename)
	{
		String query = "";
		query = "SELECT rc.concept as val, rc.old_value as old , rc.new_value as new, rc.action, a.action, a.action_child, " +
				"rc.modifier_id, u.username, u.first_name, u.last_name, " +
				"u.country_code, c.country_name, rc.ontology_id, " +
				"o.ontology_name, rc.date_modified FROM validation rc " +
				"INNER JOIN owl_action a on a.id = rc.action " +
				"INNER JOIN users u on u.user_id = rc.modifier_id " +
				"INNER JOIN ontology_info o on o.ontology_id = rc.ontology_id " +
				"INNER JOIN country_code c on u.country_code = c.country_code " +
				"WHERE rc.date_modified BETWEEN '2008-11-17' and '2008-12-12' " +
				"AND rc.ontology_id >=22 " +
				"AND rc.action BETWEEN '6' AND '8' " +
				"ORDER BY o.ontology_name, date_modified"; 
		
		ArrayList<String[]> sqlresult = new ArrayList<String[]>();		
		Statement statement = null;
		ResultSet result = null;
		try 
		{
			
			statement = getConnection().createStatement();   
			result = statement.executeQuery(query);	
			System.out.println(query);
			
			result.last();
			int i = result.getRow();
			System.out.println("Count: "+i);
			result.first();
			
			while(result.next())
			{
				String[] tmp = new String[14];
				tmp[0] = ""; 
				
				tmp[0] = getConceptBlobValue(result.getBlob("val")); 
				tmp[1] = getTermBlobValue(result.getBlob("old"), result.getBlob("new")); 
				tmp[2] = result.getString(4);
				tmp[3] = result.getString(5);
				tmp[4] = result.getString(6);
				tmp[5] = result.getString(7);
				tmp[6] = result.getString(8);
				tmp[7] = result.getString(9);
				tmp[8] = result.getString(10);
				tmp[9] = result.getString(11);
				tmp[10] = result.getString(12);
				tmp[11] = result.getString(13);
				tmp[12] = result.getString(14);
				tmp[13] = result.getString(15);
				
				sqlresult.add(tmp);
		
			}
			statement.close();
		} 
		catch(SQLException e) 
		{
			e.printStackTrace();
		}	
		finally
		{
			close();
		}
		getStat(sqlresult, filename);
	}
	
	public static String getConceptBlobValue(Blob blob)
	{
		String str = "";
		byte [] data = toByteArray(blob);
		if(data!=null)
		{
			Object obj = DatabaseUtil.getObject(data);
			if(obj!=null)
			{
				if(obj instanceof ConceptObject)
				{
					ConceptObject cObj = (ConceptObject) obj;
					str = cObj.getUri(); 
				}
			}
		}
		return str;
	}
	
	public static String getTermBlobValue(Blob oblob, Blob nblob)
	{
		String str = "";
		Blob blob = null;
		if(oblob == null)
			blob = nblob;
		else
			blob = oblob;
		byte [] data = toByteArray(blob);
		if(data!=null)
		{
			Object obj = DatabaseUtil.getObject(data);
			if(obj!=null)
			{
				if(obj instanceof TermObject)
				{
					TermObject tObj = (TermObject) obj;
					str = tObj.getLabel()+" ("+tObj.getLang()+")";
				}
			}
		}
		return str;
	}
	
	public static void getStat(ArrayList<String[]> list, String filename)
	{
		try 
		{
			String str = "";
			for(int i=0;i<list.size();i++)
			{
				String[] rows = list.get(i);
				String row = "";
				for(int j=0;j<rows.length;j++)
				{
					String val = rows[j].replace(',', ' ');
					if(j==0)
						row += val;
					else
						row += ", "+val;
				}
				str += row +"\n";
			}
			writeToFile(str, filename);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		
	}
	
	public static byte[] toByteArray(Blob fromBlob) 
	{
		  ByteArrayOutputStream baos = new ByteArrayOutputStream();
		  try 
		  {
			  byte[] buf = new byte[4000];
			  InputStream is = fromBlob.getBinaryStream();
			  try 
			  {
			   for (;;) {
			    int dataSize = is.read(buf);

			    if (dataSize == -1)
			     break;
			    baos.write(buf, 0, dataSize);
			   }
			  }
			  finally 
			  {
			   if (is != null) {
			    try {
			     is.close();
			    } catch (IOException ex) {
			    }
			   }
			  }
			  
			  return baos.toByteArray();
		  } 
		  catch (Exception e) 
		  {
		   throw new RuntimeException(e);
		  } 
		  finally 
		  {
			   if (baos != null) {
			    try 
			    {
			     baos.close();
			    } 
			    catch (IOException ex) 
			    {
			    }
			  
			}
	}
	}
	
	public static void writeToFile(String str, String filename)
	{
		File f = new File(filename);
	    FileOutputStream fop;
		try {
			fop = new FileOutputStream(f);
			 if(f.exists())
			    {
			    	
			        fop.write(str.getBytes());
			        fop.flush();
					fop.close();
					System.out.println("The data has been written");
			        
			    }
		         else
		        	 System.out.println("This file is not exist");
		} catch (Exception e) {
			e.printStackTrace();
		}

	   
	}
	
	public static Connection getConnection(){
          try
          {
              Class.forName (jdbcDriver).newInstance ();
              conn = (Connection) DriverManager.getConnection (jdbcUrl, user, password);
             // System.out.println ("Database connection established");
          }
          catch (Exception e)
          {
              System.err.println ("Cannot connect to database server");
          }
          return conn;
	}

	public static void close(){
		 if (conn != null)
         {
             try
             {
                 conn.close ();
              //   System.out.println ("Database connection terminated");
             }
             catch (Exception e) { /* ignore close errors */ }
         }

	}

	
	/*
	public String getObject(Object obj)
	{
		String str = "";
		if(obj instanceof ConceptObject)
		{
			ConceptObject cObj = (ConceptObject) obj;
			str = cObj.getUri();
		}
		else if(obj instanceof TermObject)
		{
			TermObject tObj = (TermObject) obj;
			str = tObj.getLabel();
		}
		else if(obj instanceof SchemeObject)
		{
			SchemeObject sObj = (SchemeObject) obj;
			sObj.getc
			hp.add(makeLabel(sObj.getSchemeLabel()+checkNullValueInParenthesis("en"), sObj.getDescription(), sObj.getSchemeInstance(), sObj.getSchemeInstance(), style, isAddAction, tab, objectType,  belongsToModule));
		}
		else if(obj instanceof SpellingVariantObject)
		{
			SpellingVariantObject spObj = (SpellingVariantObject) obj;
			spObj.get
			hp.add(makeLabelOnly(spObj.getLabel()+checkNullValueInParenthesis(spObj.getLang()), spObj.getUri(), v.getTermObject().getConceptUri(), v.getConceptObject().getScheme(), style, isAddAction, tab, objectType, isSource,  belongsToModule));
		}
		else if(obj instanceof TermCodesObject)
		{
			TermCodesObject tcObj = (TermCodesObject) obj;
			hp.add(makeLabelOnly(tcObj.getRepository()+checkNullValueInParenthesis(tcObj.getCode()), tcObj.getUri(), v.getTermObject().getConceptUri(), v.getConceptObject().getScheme(), style, isAddAction, tab, objectType, isSource,  belongsToModule));
		}
		else if(obj instanceof ScopeNoteObject)
		{
			ScopeNoteObject snObj = (ScopeNoteObject) obj;
			hp.add(makeLabelOnly(snObj.getLabel()+checkNullValueInParenthesis(snObj.getLang()), snObj.getUri(), v.getConceptObject().getUri(), v.getConceptObject().getScheme(), style, isAddAction, tab, objectType, isSource,  belongsToModule));
		}
		else if(obj instanceof ChangeHistoryObject)
		{
			ChangeHistoryObject chObj = (ChangeHistoryObject) obj;
			hp.add(makeLabelOnly(chObj.getLabel()+checkNullValueInParenthesis(chObj.getLang()), chObj.getUri(), v.getConceptObject().getUri(), v.getConceptObject().getScheme(), style, isAddAction, tab, objectType, isSource,  belongsToModule));
		}
		else if(obj instanceof IDObject)
		{
			IDObject idObj = (IDObject) obj;
			if(idObj.getIDType()==IDObject.DEFINITION)
			{
				if(isSource)
					hp.add(makeLabelOnly(idObj.getIDSource(), idObj.getIDSourceURL(), idObj.getIDSourceURL(), v.getConceptObject().getScheme(), style, isAddAction, tab, objectType, isSource,  belongsToModule));
				else
				{
					ArrayList<TranslationObject> trObjects = idObj.getIDTranslationList();
					String label = "";
					for(int i=0;i<trObjects.size();i++)
					{
						if(!label.equals("")) label += ", ";
						label += ((TranslationObject)trObjects.get(i)).getLabel()+checkNullValueInParenthesis(((TranslationObject)trObjects.get(i)).getLang());
					}
					hp.add(makeLabelOnly(label, idObj.getIDUri(), v.getConceptObject().getUri(), v.getConceptObject().getScheme(), style, isAddAction, tab, objectType, isSource,  belongsToModule));
				}
			}
			else if(idObj.getIDType()==IDObject.IMAGE)
			{
				if(isSource)
					hp.add(makeLabelOnly(idObj.getIDSource(), idObj.getIDSourceURL(), idObj.getIDSourceURL(), v.getConceptObject().getScheme(), style, isAddAction, tab, objectType, isSource,  belongsToModule));
				else
				{
					ArrayList<TranslationObject> trObjects = idObj.getIDTranslationList();
					String label = "";
					for(int i=0;i<trObjects.size();i++)
					{
						if(!label.equals("")) label += ", ";
						label += ((TranslationObject)trObjects.get(i)).getLabel()+checkNullValueInParenthesis(((TranslationObject)trObjects.get(i)).getLang());
					}
					hp.add(makeLabelOnly(label, idObj.getIDUri(), v.getConceptObject().getUri(), v.getConceptObject().getScheme(), style, isAddAction, tab, objectType, isSource,  belongsToModule));
				}
			}
		}
		else if(obj instanceof TranslationObject)
		{
			TranslationObject trObj = (TranslationObject) obj;
			if(trObj.getType()==TranslationObject.DEFINITIONTRANSLATION)
			{
				hp.add(makeLabelOnly(trObj.getLabel()+checkNullValueInParenthesis(trObj.getLang()), trObj.getUri(), v.getConceptObject().getUri(), v.getConceptObject().getScheme(), style, isAddAction, tab, objectType, isSource,  belongsToModule));	
			}
			else if(trObj.getType()==TranslationObject.IMAGETRANSLATION)
			{
				hp.add(makeLabelOnly(trObj.getLabel()+checkNullValueInParenthesis(trObj.getLang()), trObj.getUri(), v.getConceptObject().getUri(), v.getConceptObject().getScheme(), style, isAddAction, tab, objectType, isSource,  belongsToModule));
			}
		}
		return str;
	}
	*/

}
