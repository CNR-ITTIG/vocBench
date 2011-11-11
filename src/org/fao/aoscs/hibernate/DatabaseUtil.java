package org.fao.aoscs.hibernate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;

import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.RecentChangeData;
import org.fao.aoscs.domain.RecentChanges;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.Validation;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DatabaseUtil {
	
	//create new object
	 public static Object createObject(Object obj)
	 {
	    	
		 	if (obj instanceof Validation) {
				Validation v = (Validation) obj;
				v.setConceptObject((ConceptObject)DatabaseUtil.getObject(v.getConcept()));
				if(v.getOldValue()!=null){
					ArrayList list = new ArrayList();
					list.add(DatabaseUtil.getObject(v.getOldValue()));
					v.setOldObject(list);
				}
				if(v.getNewValue()!=null){
					ArrayList list = new ArrayList();
					list.add(DatabaseUtil.getObject(v.getNewValue()));
					v.setNewObject(list);
				}
				if(v.getOldRelationship()!=null){
					v.setOldRelationshipObject((RelationshipObject)DatabaseUtil.getObject(v.getOldRelationship()));
				}
				if(v.getNewRelationship()!=null){
					v.setNewRelationshipObject((RelationshipObject)DatabaseUtil.getObject(v.getNewRelationship()));
					
				}
				RecentChanges rc = new RecentChanges();
				rc.setObject(DatabaseUtil.setObject(v));
				rc.setModifiedActionId(v.getAction());
				rc.setModifierId(v.getModifierId());
				rc.setModifiedDate(v.getDateModified());
				rc.setOntologyId(v.getOntologyId());
				DatabaseUtil.createObject(rc);
			}
		 	
		 	try
	    	{
		 		Session session = HibernateUtilities.currentSession();
		 		Transaction tx= session.beginTransaction();    		
		 		session.save(obj);		 				 		
		 		tx.commit();		 		
		 		session.flush();		 		
	    	}
	    	catch(Exception ex)
	    	{
	    		ex.printStackTrace();	    		
	    	}
	    	finally
	    	{
	    		HibernateUtilities.closeSession();
	    	}
	    	return obj;
	 }	
	 
	 public static void addRecentChange(RecentChangeData rcData, int ontologyId)
	 {		 				 
		 RecentChanges rc = new RecentChanges();
		 rc.setObject(DatabaseUtil.setObject(rcData));
		 rc.setModifiedActionId(rcData.getActionId());
		 rc.setModifierId(rcData.getModifierId()); 
		 rc.setModifiedDate(new Date());				
		 rc.setOntologyId(ontologyId);
		 DatabaseUtil.createObject(rc);		 
	 }
	 
//  update  all object
    public static void update(Object obj, boolean addToRecentChanges)
    {
    	if (obj instanceof Validation)
    	{
			Validation v = (Validation) obj;
			RecentChanges rc = new RecentChanges();
			
			byte[] b = DatabaseUtil.setObject(v);
			if(addToRecentChanges)
			{
				rc.setObject(b);
				
				if(v.getIsValidate())
				{
					if(v.getIsAccept())
						rc.setModifiedActionId(72); // validation-accepted
					else
						rc.setModifiedActionId(73); // validation-rejected
				}
				else
				{
					rc.setModifiedActionId(v.getAction());
				}
				
				rc.setModifierId(v.getModifierId());
				rc.setModifiedDate(v.getDateModified());
				rc.setOntologyId(v.getOntologyId());
				DatabaseUtil.createObject(rc);
    		}
		}
    	try
    	{    		    		
    		Session session = HibernateUtilities.currentSession();
    		Transaction tx = session.beginTransaction();
    		session.update(obj);
    		tx.commit();
    		session.flush();
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	finally 
    	{
    		HibernateUtilities.closeSession();
        }
    }
    
//  update  all object
    public static void delete(Object obj)
    {
    	if (obj instanceof Validation) {
			Validation v = (Validation) obj;
			RecentChanges rc = new RecentChanges();
			rc.setObject(DatabaseUtil.setObject(v));
			rc.setModifiedActionId(v.getAction());
			rc.setModifierId(v.getModifierId());
			rc.setModifiedDate(v.getDateModified());
			rc.setOntologyId(v.getOntologyId());
			DatabaseUtil.createObject(rc);
		}
    	try
    	{
    		Session session = HibernateUtilities.currentSession();
    		Transaction tx = session.beginTransaction();
    		session.delete(obj);
    		tx.commit();
    		session.flush();
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	finally 
    	{
    		HibernateUtilities.closeSession();
        }
    }
    
    public static byte[] setObject(Object obj)
    {
    	try
    	{
    		if(obj!=null)
    		{
	    		byte[] bytes ;
	    		ByteArrayOutputStream out = new ByteArrayOutputStream() ;
			    ObjectOutputStream objOut = new ObjectOutputStream(out) ;
			    objOut.writeObject(obj);
			    objOut.flush() ;
			    bytes = out.toByteArray() ;
			    objOut.close() ;
			    return bytes;
    		}
    		else
    			return null;
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		return null;
    	}
    	
    }
    
    public static byte[] setObjectWrappedInArray(ArrayList obj)
    {
    	try
    	{
			if(obj!=null && obj.size()>0)
			{
	    		byte[] bytes ;
	    		ByteArrayOutputStream out = new ByteArrayOutputStream() ;
			    ObjectOutputStream objOut = new ObjectOutputStream(out) ;
			    objOut.writeObject(obj.get(0));
			    objOut.flush() ;
			    bytes = out.toByteArray() ;
			    objOut.close() ;
			    return bytes;
			}
			else
				return null;
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		return null;
    	}
    	
    }
    
    public static Object getObject(byte[] bytes)
    {
    	try
    	{
            if(bytes!=null)
            {
	    		ByteArrayInputStream bytesInput = new ByteArrayInputStream(bytes);
	            ObjectInputStream objInput;
	            objInput = new ObjectInputStream(bytesInput);
	            Object obj = objInput.readObject();
			    return obj;
            }
            else return null;
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		return null;
    	}
    }
    
    public static ArrayList getObjectWrappedInArray(byte[] bytes)
    {
    	try
    	{
    		 if(bytes!=null)
             {
    			 ByteArrayInputStream bytesInput = new ByteArrayInputStream(bytes);
	            ObjectInputStream objInput;
	            objInput = new ObjectInputStream(bytesInput);
	            Object obj = objInput.readObject();
	            ArrayList list = new ArrayList();
	            list.add(obj);
			    return list;
             }
    		 else
    			 return new ArrayList();
    	}
    	catch(Exception e)
    	{
    		return new ArrayList();
    	}
    }
    
	

}
