package org.fao.aoscs.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;

import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.gwt.GwtConfigurationHelper;
import net.sf.gilead.gwt.PersistentRemoteService;

import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.client.module.validation.service.ValidationService;
import org.fao.aoscs.domain.AttributesObject;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.IDObject;
import org.fao.aoscs.domain.NonFuncObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.OwlAction;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.RecentChanges;
import org.fao.aoscs.domain.RecentChangesInitObject;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.SchemeObject;
import org.fao.aoscs.domain.TermObject;
import org.fao.aoscs.domain.TranslationObject;
import org.fao.aoscs.domain.Users;
import org.fao.aoscs.domain.Validation;
import org.fao.aoscs.domain.ValidationFilter;
import org.fao.aoscs.domain.ValidationInitObject;
import org.fao.aoscs.domain.ValidationPermission;
import org.fao.aoscs.hibernate.DatabaseUtil;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.server.index.IndexingEngineFactory;
import org.fao.aoscs.server.protege.ProtegeModelFactory;
import org.fao.aoscs.server.protege.ProtegeWorkbenchUtility;
import org.hibernate.Session;

import com.google.gwt.gen2.table.client.TableModelHelper.ColumnSortInfo;
import com.google.gwt.gen2.table.client.TableModelHelper.ColumnSortList;
import com.google.gwt.gen2.table.client.TableModelHelper.Request;

import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;
import edu.stanford.smi.protegex.owl.model.RDFSNames;
import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFSLiteral;

public class ValidationServiceImpl extends PersistentRemoteService implements ValidationService{
	
	private static final long serialVersionUID = 2404029514881638242L;
	OWLModel owlModel;
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	

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
	
	public  ValidationInitObject getInitData(ValidationFilter vFilter)
	{
		vFilter.setSelectedUserList(getAllUsers());
		vFilter.setSelectedStatusList(getStatus());
		vFilter.setSelectedActionList(getAction());
		
		ValidationInitObject vInitObj = new ValidationInitObject();
		vInitObj.setPermissions(getPermission(vFilter.getGroupID()));
		vInitObj.setUser(vFilter.getSelectedUserList());
		vInitObj.setStatus(vFilter.getSelectedStatusList());
		vInitObj.setAction(vFilter.getSelectedActionList());
		vInitObj.setValidationSize(getValidatesize(vFilter));
		return vInitObj;
	}

	public ArrayList<ValidationPermission> getPermission(int groupID)
	{
		try {
			String query =  "select * from validation_permission where " +
							"users_groups_id='" + groupID + "' and newstatus!=0 ";
			Session s = HibernateUtilities.currentSession();
			List<ValidationPermission> list = s.createSQLQuery(query).addEntity(ValidationPermission.class).list();
			return (ArrayList<ValidationPermission>) list;

		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<ValidationPermission>();
		} finally {
			HibernateUtilities.closeSession();
		}
	}
	
	public ArrayList<Users> getAllUsers()
	{
		try {
			String query = "SELECT * FROM users ORDER BY username;";
			Session s = HibernateUtilities.currentSession();
			List<Users> list = s.createSQLQuery(query).addEntity(Users.class).list();
			return (ArrayList<Users>) list;

		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Users>();
		} finally {
			HibernateUtilities.closeSession();
		}
	}
	
	public ArrayList<OwlStatus> getStatus()
	{
		try {
			String query = "SELECT * FROM owl_status";
			Session s = HibernateUtilities.currentSession();
			List<OwlStatus> list = s.createSQLQuery(query).addEntity(OwlStatus.class).list();
			return (ArrayList<OwlStatus>) list;

		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<OwlStatus>();
		} finally {
			HibernateUtilities.closeSession();
		}
	}
	
	public ArrayList<OwlAction> getAction()
	{
		try {
			String query = "SELECT * FROM owl_action";
			Session s = HibernateUtilities.currentSession();
			List<OwlAction> list = s.createSQLQuery(query).addEntity(OwlAction.class).list();
			return (ArrayList<OwlAction>) list;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<OwlAction>();
		} finally {
			HibernateUtilities.closeSession();
		}
	}
	
	public ArrayList<OwlAction> getOtherAction()
	{
		try {
			String query = "SELECT * FROM other_action";
			Session s = HibernateUtilities.currentSession();
			List<OwlAction> list = s.createSQLQuery(query).addEntity(OwlAction.class).list();
			return (ArrayList<OwlAction>) list;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<OwlAction>();
		} finally {
			HibernateUtilities.closeSession();
		}
	}
	
	public String convertArrayToString(ArrayList<?> list)
	{
		String str = "";
		for(int i=0;i<list.size();i++)
		{
			String tmp = "";
			Object obj = list.get(i);
			if (obj instanceof OwlAction) {
				tmp  = ""+((OwlAction) obj).getId();
			}
			else if (obj instanceof OwlStatus) {
				tmp  = ""+((OwlStatus) obj).getId();
			}
			else if (obj instanceof Users) {
				tmp  = ""+((Users) obj).getUserId();
			}
			else if (obj instanceof String) {
				tmp  = ""+obj;
			}
			if(str.equals(""))
				str = "'"+tmp+"'";
			else
				str += ", '"+tmp+"'";
		}
		return str;
	}
	
	public String getQuery(ValidationFilter vFilter)
	{
		//SELECT distinct COLUMN_NAME FROM information_schema.`COLUMNS` C WHERE table_name = 'recent_changes' and ORDINAL_POSITION = '1'
	    //String query = "select * from recent_changes where ontology_id = "+ontologyId+" ORDER BY "+orderBy+" LIMIT "+numRow+" OFFSET "+startRow;
	    /*String query = "select rc.*, oa.action, u.username from " +
	    				"recent_changes rc, owl_action oa, users u " +
					   "where " +
					   "rc.ontology_id = "+ontologyId+" " +
					   "and rc.modifier_id = u.user_id " +
					   "and rc.modified_action = oa.id " +
					   "order by "+orderBy+" LIMIT "+numRow+" OFFSET "+startRow;
	    */
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    String query = "SELECT {v.*}, oa.action, os.status, o.username, u.username " 
			+  "from validation v " 
			+  "INNER JOIN validation_permission vp  on vp.status = v.status "
			+  "INNER JOIN users o on v.owner_id = o.user_id "
			+  "INNER JOIN users u on v.modifier_id = u.user_id "
			+  "INNER JOIN owl_action oa on v.action = oa.id "
			+  "INNER JOIN owl_status os on v.status = os.id "
			+  "WHERE  ";
	    	    
	    if(vFilter.getSelectedStatusList().size()>0)
			query += "v.status IN ("+convertArrayToString(vFilter.getSelectedStatusList())+") and ";
		if(vFilter.getSelectedUserList().size()>0)
			query += "v.modifier_id IN ("+convertArrayToString(vFilter.getSelectedUserList())+") and ";
		if(vFilter.getSelectedActionList().size()>0)
			query += "v.action IN ("+convertArrayToString(vFilter.getSelectedActionList())+") and ";
		if(vFilter.getFromDate()!=null && vFilter.getToDate()!=null)
			query += "v.date_modified BETWEEN '"+sdf.format(vFilter.getFromDate())+" 00:00:00' AND '"+sdf.format(vFilter.getToDate())+" 23:59:59'  and ";
		
		query += "v.is_validate!=:isValidate and "
			  +  "vp.users_groups_id=:groupid and "
			  +  "v.ontology_id=:ontologyid and "
			  +  "vp.newstatus!=:newstatus " + "group by v.id ";

		return query;
	}

	@SuppressWarnings("unchecked")
	public int getValidatesize(ValidationFilter vFilter)
	{

		String query = getQuery(vFilter);	
		try {
			Session s = HibernateUtilities.currentSession();
			List<Validation> list = s.createSQLQuery(query).addEntity("v",
					Validation.class).setParameter("groupid", vFilter.getGroupID()).setParameter("ontologyid", vFilter.getOntoInfo().getOntologyId())
					.setParameter("newstatus", 0).setParameter("isValidate", 1).list();
			//checked during viewing the validationlist skipped while calculating the size
			//List<Validation> list2 = checkNullValidation((ArrayList<Validation>) list, ProtegeModelFactory.getOWLModel(vFilter.getOntoInfo()), vFilter);
			return list.size();

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}finally {
			HibernateUtilities.closeSession();
		}
	}
	
	public ArrayList<Validation> setValidation(ArrayList<Validation> list)
	{
		ArrayList<Validation> vList = new ArrayList<Validation>();
		for(int i=0;i<list.size();i++)
		{
			Validation v = list.get(i);
			v.setConceptObject((ConceptObject) DatabaseUtil.getObject(v.getConcept()));
			v.setConcept(null);
			v.setTermObject((TermObject) DatabaseUtil.getObject(v.getTerm()));
			v.setTerm(null);
			v.setOldRelationshipObject((RelationshipObject) DatabaseUtil.getObject(v.getOldRelationship()));
			v.setOldRelationship(null);
			v.setNewRelationshipObject((RelationshipObject) DatabaseUtil.getObject(v.getNewRelationship()));
			v.setNewRelationship(null);
			v.setOldObject(DatabaseUtil.getObjectWrappedInArray(v.getOldValue()));
			v.setOldValue(null);
			v.setNewObject(DatabaseUtil.getObjectWrappedInArray(v.getNewValue()));
			v.setNewValue(null);
			vList.add(v);
		}
		return vList;
	}
	
	public ArrayList<Validation> checkNullValidation(ArrayList<Validation> list, OWLModel owlModel, ValidationFilter vFilter)
	{
		ArrayList<Validation> vList = new ArrayList<Validation>();
		//ArrayList<String> concepts = new ArrayList<String>();
		//ArrayList<String> terms = new ArrayList<String>();
		
		for(int i=0;i<list.size();i++)
		{
			Validation v = list.get(i);
			if(v.getConcept() != null)
			{	
				// Check concept
				ConceptObject cobj = (ConceptObject) DatabaseUtil.getObject(v.getConcept());
				if(cobj != null)
				{
					OWLNamedClass cls = owlModel.getOWLNamedClass(cobj.getName());
					if(cls != null)
					{
						// Check term
						if(v.getTerm() != null)
						{
							TermObject tobj = (TermObject) DatabaseUtil.getObject(v.getTerm());
							if(tobj != null)
							{
								OWLIndividual term = owlModel.getOWLIndividual(tobj.getName());
								//OWLIndividual concept = (OWLIndividual) term.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RISLEXICALIZATIONOF));
								//System.out.println(cobj.getUri()+" === "+concept.getProtegeType().getURI());
								
								if(term != null)
								{
									vList.add(v);
								}
								else
								{
									//if(!terms.contains(tobj.getName()))
									{
										//terms.add(""+v.getId());
										v.setIsValidate(new Boolean(true));
										DatabaseUtil.update(v, false);
									}
								}
							}
						}
						else
						{
							vList.add(v);
						}
					}
					else
					{
						//if(!concepts.contains(cobj.getName()))
						{
							//concepts.add(""+v.getId());
							v.setIsValidate(new Boolean(true));
							DatabaseUtil.update(v, false);
						}
					}
				}
			}	

			// Delete stray concepts and terms
			//deleteConceptFromValidation(vFilter, concepts);
			//deleteTermFromValidation(vFilter, terms);
		}
		return vList;
	}
	
	public ArrayList requestValidationRows(com.google.gwt.gen2.table.client.TableModelHelper.Request request, ValidationFilter vFilter)
	{
		HashMap<String, String> col = new HashMap<String, String>();

	   col.put("3", "oa.action");
	   col.put("5", "o.username");
	   col.put("6", "u.username");
	   col.put("7", "v.date_create");
	   col.put("8", "v.date_modified");
	   col.put("9", "os.status");
	   String orderBy = " v.date_modified asc ";
	   
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
	    
	   String query = getQuery(vFilter);
	   query += "order by "+orderBy+" LIMIT "+numRow+" OFFSET "+startRow;
	   
		    
	   try 
	   {
			Session s = HibernateUtilities.currentSession();
			List<Validation> list = s.createSQLQuery(query).addEntity("v",
					Validation.class).setParameter("groupid", vFilter.getGroupID()).setParameter("ontologyid", vFilter.getOntoInfo().getOntologyId())
					.setParameter("newstatus", 0).setParameter("isValidate", 1).list();
			
			ArrayList<Validation> list2 = new ArrayList<Validation>();
			list2 = checkNullValidation((ArrayList<Validation>) list, ProtegeModelFactory.getOWLModel(vFilter.getOntoInfo()), vFilter);
			return setValidation(list2);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new ArrayList<Validation>();
		}
		finally 
		{
			HibernateUtilities.closeSession();
		}

	}
	
	public int updateValidateQueue(List<Validation> validationList, ValidationFilter vFilter)
	{
		Iterator<Validation> itr = validationList.iterator();
		while (itr.hasNext()) {
			Validation v = (Validation) itr.next();
			try
			{
				v.setConcept(DatabaseUtil.setObject(v.getConceptObject()));
			    v.setTerm(DatabaseUtil.setObject(v.getTermObject()));
			    v.setOldRelationship(DatabaseUtil.setObject(v.getOldRelationshipObject()));
			    v.setNewRelationship(DatabaseUtil.setObject(v.getNewRelationshipObject()));
				v.setOldValue(DatabaseUtil.setObjectWrappedInArray(v.getOldObject()));
				v.setNewValue(DatabaseUtil.setObjectWrappedInArray(v.getNewObject()));
				updateTriple(v, vFilter);
				DatabaseUtil.update(v, true);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return getValidatesize(vFilter);
	}

	/*public void deleteConceptFromValidation(ValidationFilter vFilter, ArrayList<String> conceptName)
	{
		String query = getQuery(vFilter);
		try {
			Session s = HibernateUtilities.currentSession();
			List<Validation> vList = s.createSQLQuery(query).addEntity("v",
					Validation.class).setParameter("groupid", vFilter.getGroupID()).setParameter("ontologyid", vFilter.getOntoInfo().getOntologyId())
					.setParameter("newstatus", 0).setParameter("isValidate", 1).list();
			
			for(int i=0;i<vList.size();i++)
			{
				Validation v = vList.get(i);
				if(v!=null )
				{
					ConceptObject cObj = (ConceptObject)DatabaseUtil.getObject(v.getConcept());
					if(cObj!=null)
					{
						if(conceptName.contains(cObj.getName()))
						{
							v.setIsValidate(new Boolean(true));
							DatabaseUtil.update(v, false);
						}
					}
				}
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			HibernateUtilities.closeSession();
		}
	}
	
	public void deleteTermFromValidation(ValidationFilter vFilter, ArrayList<String> termName)
	{
		String query = getQuery(vFilter);
		try {
			Session s = HibernateUtilities.currentSession();
			List<Validation> vList = s.createSQLQuery(query).addEntity("v",
					Validation.class).setParameter("groupid", vFilter.getGroupID()).setParameter("ontologyid", vFilter.getOntoInfo().getOntologyId())
					.setParameter("newstatus", 0).setParameter("isValidate", 1).list();
			for(int i=0;i<vList.size();i++)
			{
				Validation v = vList.get(i);
				if(v!=null )
				{
					TermObject tObj = (TermObject) DatabaseUtil.getObject(v.getTerm());
					if(tObj!=null)
					{
						if(termName.contains(tObj.getName()))
						{
							v.setIsValidate(new Boolean(true));
							DatabaseUtil.update(v, false);
						}
					}
				}
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			HibernateUtilities.closeSession();
		}
	}*/
	
	public void updateTriple(Validation val, ValidationFilter vFilter)
	{
		OntologyInfo ontoInfo = vFilter.getOntoInfo();
		owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		switch (val.getAction()) {

		 case 1:     //     "concept-create"
			 ConceptObject cObj = (ConceptObject) val.getNewObject().get(0);
			 if(val.getIsAccept())
			 {
				 updateConcept(cObj.getName(), val.getStatusLabel());
				 HashMap<String, TermObject> tObjs = cObj.getTerm();
				 TermObject tObj = new TermObject();
				 if(tObjs != null)
				 {
					 Iterator<String> itr = tObjs.keySet().iterator();
					 while(itr.hasNext())
					 {
						 tObj = (TermObject)tObjs.get((String)itr.next());
						 updateTerm(cObj.getName(), tObj, val.getStatusLabel());
					 }
				 }
				 
			 }
			 else
			 {
				 if(val.getConceptObject().getBelongsToModule()==ConceptObject.CONCEPTMODULE)
				 {
					 deleteConcept(cObj.getName(), ontoInfo, vFilter);
				 }
				 else if(val.getConceptObject().getBelongsToModule()==ConceptObject.CLASSIFICATIONMODULE)
				 {
					if(cObj.getUri().startsWith(ModelConstants.ONTOLOGYBASENAMESPACE))
					{
						 deleteConcept(cObj.getName(), ontoInfo, vFilter);
					}
					else
					{
						deleteExistingCategory(cObj.getName(), cObj.getScheme());
					}
				 }
			 }
			 break;
	
		 case 2:     //     "concept-delete"
			 
			 cObj = (ConceptObject) val.getOldObject().get(0);
			 updateConcept(cObj.getName(), val.getStatusLabel());
			 if(val.getIsAccept())
			 {
				 updateTermwhileConcept(cObj.getName(), val.getStatusLabel());
				 deleteConcept(cObj.getName(), val.getStatusLabel());
			 }
			 break;
	
		 case 3:     //     "concept-relationship-create"
			 if(!val.getIsAccept())
			 {
				 deleteRelationship(val);
			 }
			 break;
	
		 case 4:     //     "concept-relationship-edit"
			 if(!val.getIsAccept())
			 {
				 deleteRelationship(val);
				 addRelationship(val);
			 }
			 break;
	
		 case 5:     //     "concept-relationship-delete"
			 if(!val.getIsAccept())
			 {
				 addRelationship(val);
			 }
			 break;
	
		 case 6:     //     "term-create"
			 if(val.getIsAccept())
			 {
				 updateTerm(val.getConceptObject().getName(), ((TermObject)val.getNewObject().get(0)), val.getStatusLabel());
			 }
			 else
				 deleteTerm(val.getConceptObject().getName(), ((TermObject)val.getNewObject().get(0)).getUri(), ontoInfo, vFilter);	
			 break;
	
		 case 7:     //     "term-edit"
			 if(val.getIsAccept())
				 updateTerm(val.getConceptObject().getName(), ((TermObject)val.getNewObject().get(0)), val.getStatusLabel());
			 else
				 revertTerm(val, ontoInfo);
			 break;
	
		 case 8:     //     "term-delete"
			 updateTerm(val.getConceptObject().getName(), ((TermObject)val.getOldObject().get(0)), val.getStatusLabel());
			 break;
	
		 case 9:     //     "term-relationship-add"
			 if(!val.getIsAccept())
			 {
				 deleteTermRelationship(val);
			 }
			 break;
	
		 case 10:     //     "term-relationship-edit"
			 if(!val.getIsAccept())
			 {
				 deleteTermRelationship(val);
				 addTermRelationship(val);
			 }
			 break;
	
		 case 11:     //     "term-relationship-delete"
			 if(!val.getIsAccept())
			 {
				 addTermRelationship(val);
			 }
			 break;
	
		 case 12:     //     "term-note-create"
			 if(!val.getIsAccept())
			 {
				 deleteNonFunctionalTerm(val);
			 }
			 break;
	
		 case 13:     //     "term-note-edit"
			 if(!val.getIsAccept())
			 {
				 deleteNonFunctionalTerm(val);
				 addNonFunctionalTerm(val);
			 }
			 break;
	
		 case 14:     //     "term-note-delete"
			 if(!val.getIsAccept())
			 {
				 addNonFunctionalTerm(val);
			 }
			 break;
	
		 case 15:     //     "term-attribute-create"
			 if(!val.getIsAccept())
			 {
				 deleteNonFunctionalTerm(val);
			 }
			 break;
	
		 case 16:     //     "term-attribute-edit"
			 if(!val.getIsAccept())
			 {
				 deleteNonFunctionalTerm(val);
				 addNonFunctionalTerm(val);
			 }
			 break;
	
		 case 17:     //     "term-attribute-delete"
			 if(!val.getIsAccept())
			 {
				 addNonFunctionalTerm(val);
			 }
			 break;
	
		 case 18:     //     "note-create"
			 if(!val.getIsAccept())
				 deleteNonFunctional(val, ontoInfo);
			 break;
	
		 case 19:     //     "note-edit"
			 if(!val.getIsAccept())
			 {
				 deleteNonFunctional(val, ontoInfo);
				 addNonFunctional(val, ontoInfo);
			 }
			 break;
	
		 case 20:     //     "note-delete"
			 if(!val.getIsAccept())
				 addNonFunctional(val, ontoInfo);
			 break;
	
		 case 21:     //     "definition-create"
			 if(!val.getIsAccept())
				deleteDefinition(val, ontoInfo);	
			 break;
	
		 case 22:     //     "definition-translation-edit"
			 if(!val.getIsAccept())
			 {
				 deleteDefinitionTranslation(val, ontoInfo);
				 addDefinitionTranslation(val, ontoInfo);
			 }
			 break;
	
		 case 23:     //     "definition-delete"
			 if(!val.getIsAccept())
				 unDeleteDefinition(val, ontoInfo);	
			 break;
		 
		 case 24:     //     "image-create"
			 if(!val.getIsAccept())
			 {
				 deleteImage(val, ontoInfo);
			 }
			 break;
		 
		 case 25:     //     "image-translation-edit"
			 if(!val.getIsAccept())
			 {
				 deleteImageTranslation(val, ontoInfo);
				 addImageTranslation(val, ontoInfo);
			 }
			 break;
		 
		 case 26:     //     "image-delete"
			 if(!val.getIsAccept())
			 {
				 undeleteImage(val, ontoInfo);
			 }
			 break;
			 
		 case 27:     //     "image-translation-create"
			 if(!val.getIsAccept())
			 {
				 deleteImageTranslation(val, ontoInfo);
			 }
			 break;
			 
		 case 28:     //     "image-translation-delete"
			 if(!val.getIsAccept())
			 {
				 addImageTranslation(val, ontoInfo);
			 }
			 break;
			 
		 case 29:     //     "definition-translation-create"
			 if(!val.getIsAccept())
			 {
				 deleteDefinitionTranslation(val, ontoInfo);
			 }
			 break;
			 
		 case 30:     //     "definition-translation-delete"
			 if(!val.getIsAccept())
			 {
				 addDefinitionTranslation(val, ontoInfo);
			 }
			 break;
			 
		 case 31:     //     "ext-source-create"
			 if(!val.getIsAccept())
			 {
				 deleteDefinitionSource(val);
			 }
			 break;
			 
		 case 32:     //     "ext-source-edit"
			 if(!val.getIsAccept())
			 {
				 addDefinitionSource(val);
			 }
			 break;
			 
		 case 33:     //     "ext-source-delete"
			 if(!val.getIsAccept())
			 {
				 addDefinitionSource(val);
			 }
			 break;
			 
		 case 34:     //     "image-source-create"
			 if(!val.getIsAccept())
			 {
				 deleteImageSource(val);
			 }
			 break;
			 
		 case 35:     //     "image-source-edit"
			 if(!val.getIsAccept())
			 {
				 addImageSource(val);
			 }
			 break;
			 
		 case 36:     //     "image-source-delete"
			 if(!val.getIsAccept())
			 {
				 addImageSource(val);
			 }
			 break;
		
		 case 37:     //     "attribute-create"
			 if(!val.getIsAccept())
				 deleteNonFunctional(val, ontoInfo);
			 break;
	
		 case 38:     //     "attribute-edit"
			 if(!val.getIsAccept())
			 {
				 deleteNonFunctional(val, ontoInfo);
				 addNonFunctional(val, ontoInfo);
			 }
			 break;
	
		 case 39:     //     "attribute-delete"
			 if(!val.getIsAccept())
				 addNonFunctional(val, ontoInfo);
			 break;
	
		 case 40:     //     "scheme-create"
			 if(!val.getIsAccept())
				 deleteScheme(val);
			 break;
			 
		 case 41:     //     ""mapping-create"
			 if(!val.getIsAccept())
				 deleteSchemeMapping(val);
			 break;
			 
		 case 42:     //     "mapping-delete"
			 if(!val.getIsAccept())
				 addSchemeMapping(val);
			 break;
			 
		 default: 
	      	break;
	   }
		///owlModel.dispose();
	}
	
	public void updateConcept(String conceptName, String status)
	{
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptName);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		individual.setPropertyValue(cls.getOWLModel().getOWLProperty(ModelConstants.RHASSTATUS), status);
		individual.setPropertyValue(cls.getOWLModel().getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
	}
	
	public void updateTermwhileConcept(String conceptName, String status)
	{
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptName);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		individual.setPropertyValue(cls.getOWLModel().getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
		for (Iterator<OWLIndividual> t = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION)).iterator(); t.hasNext();) {
            OWLIndividual term = (OWLIndividual)t.next();
			term.setPropertyValue(cls.getOWLModel().getOWLProperty(ModelConstants.RHASSTATUS), status);
	        term.setPropertyValue(cls.getOWLModel().getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
		}
	}
	
	public void updateTerm(String conceptName, TermObject tempObj, String status)
	{
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptName);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLIndividual term = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, individual, cls.getOWLModel().getOWLProperty(ModelConstants.RHASLEXICALIZATION), tempObj.getUri());
		term.setPropertyValue(cls.getOWLModel().getOWLProperty(ModelConstants.RISMAINLABEL), tempObj.isMainLabel());
		term.setPropertyValue(cls.getOWLModel().getOWLProperty(ModelConstants.RHASSTATUS), status);
        term.setPropertyValue(cls.getOWLModel().getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
        individual.setPropertyValue(cls.getOWLModel().getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());

        if(tempObj.isMainLabel()){
			// Set mainlabel(false) to the other term
			Collection co = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION));
			for (Iterator iter = co.iterator(); iter.hasNext();) {
				Object obj = iter.next();
				if (obj instanceof OWLIndividual) {
					OWLIndividual termIns = (OWLIndividual) obj;
					
					Object mainLabel = termIns.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RISMAINLABEL));
					boolean isMainLabel = false;
					if(mainLabel != null){
						isMainLabel = Boolean.valueOf(mainLabel.toString());
					}
					
					String lang = "";
					for (Iterator iterator = termIns.getLabels().iterator(); iterator.hasNext();) {
						Object obj1 = iterator.next();
				    	if (obj1 instanceof DefaultRDFSLiteral) {
				    		DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj1;
				    		lang = element.getLanguage();
						}
					}
					
					if(!termIns.getURI().equals(term.getURI()) && isMainLabel && lang.toLowerCase().equals(tempObj.getLang().toLowerCase())){
						termIns.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RISMAINLABEL), false);
					}
				}
			}
		}
	}
	
	public void revertTerm(Validation val, OntologyInfo ontoInfo)
	{
		TermObject tObj = (TermObject)val.getOldObject().get(0);
		TermObject OldTObj = (TermObject)val.getNewObject().get(0);
		OWLNamedClass cls = owlModel.getOWLNamedClass(val.getConceptObject().getName());
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLIndividual term = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, individual, cls.getOWLModel().getOWLProperty(ModelConstants.RHASLEXICALIZATION), tObj.getUri());
		term.removeLabel(OldTObj.getLabel(), OldTObj.getLang());
		term.addLabel(tObj.getLabel(), tObj.getLang());
		term.setPropertyValue(cls.getOWLModel().getOWLProperty(ModelConstants.RISMAINLABEL), tObj.isMainLabel());
		term.setPropertyValue(cls.getOWLModel().getOWLProperty(ModelConstants.RHASSTATUS), val.getStatusLabel());
        term.setPropertyValue(cls.getOWLModel().getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
        individual.setPropertyValue(cls.getOWLModel().getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
        
        if(ConfigConstants.ISINDEXING)
        {
	      //update index
        	IndexingEngineFactory.updateIndex(ontoInfo, term.getURI(), tObj.getLang(), SearchServiceImplOWLART.c_nounInstancesIndexCategory);
        	IndexingEngineFactory.updateIndex(ontoInfo, term.getURI(), OldTObj.getLang(), SearchServiceImplOWLART.c_nounInstancesIndexCategory);
        }
        
	}
	
	public void deleteExistingCategory(String conceptName, String scheme)
	{	
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(conceptName));
		OWLProperty hasCat = owlModel.getOWLProperty(ModelConstants.RHASCATEGORY);
		OWLIndividual schemeIndividual = owlModel.getOWLIndividual(ProtegeWorkbenchUtility.getNameFromURI(owlModel, scheme));
		for (Iterator<OWLIndividual> lit = schemeIndividual.getPropertyValues(hasCat).iterator(); lit.hasNext();) {
			OWLIndividual cIndividual = lit.next();
			if(cIndividual.getURI().equals(individual.getURI()))
				schemeIndividual.removePropertyValue(hasCat, cIndividual);
        }	
	}
	
	public void deleteConcept(String conceptName, OntologyInfo ontoInfo, ValidationFilter vFilter)
	{
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptName);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		
		if(ConfigConstants.ISINDEXING)
		{
			//update index
			for (Iterator iter2 = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION)).iterator(); iter2.hasNext();) 
			{
				OWLIndividual termInstance = (OWLIndividual) iter2.next();
				IndexingEngineFactory.deleteIndex(ontoInfo, termInstance.getURI(), SearchServiceImplOWLART.c_nounInstancesIndexCategory);
			}
		}
		
		individual.delete();
		cls.delete();
		
		// remove all related validations
		ArrayList<String> concepts = new ArrayList<String>();
		concepts.add(conceptName);
		//checked during viewing the validationlist
		//deleteConceptFromValidation(vFilter, concepts);
	}

	public void deleteTerm(String conceptName, String termURI, OntologyInfo ontoInfo, ValidationFilter vFilter)
	{

		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptName);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLIndividual term = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, individual, cls.getOWLModel().getOWLProperty(ModelConstants.RHASLEXICALIZATION), termURI);

		if(ConfigConstants.ISINDEXING)
		{
			//update index
			IndexingEngineFactory.deleteIndex(ontoInfo, term.getURI(), SearchServiceImplOWLART.c_nounInstancesIndexCategory);
		}
		String termName = term.getName();
		term.delete();
		// Remove all validations related to this term
		ArrayList<String> terms = new ArrayList<String>();
		terms.add(termName);
		//checked during viewing the validationlist
		//deleteTermFromValidation(vFilter, terms);
	}
	
	
	public void deleteConcept(String conceptName, String status){

		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptName);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		individual.setPropertyValue(cls.getOWLModel().getOWLProperty(ModelConstants.RHASSTATUS), status);
		individual.setPropertyValue(cls.getOWLModel().getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
	}
	
	public void unDeleteDefinition(Validation val, OntologyInfo ontoInfo)
	{
		IDObject defObject = (IDObject) val.getOldObject().get(0);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(val.getConceptObject().getName()));
		OWLIndividual defIns = owlModel.getOWLNamedClass(ModelConstants.CDEFINITION).createOWLIndividual(defObject.getIDName());
		ArrayList<TranslationObject> stObjects = defObject.getIDTranslationList();
		for(int i=0;i<stObjects.size();i++)
		{
			TranslationObject transObj = (TranslationObject)stObjects.get(i);
			defIns.addLabel(transObj.getLabel(), transObj.getLang());
			if(ConfigConstants.ISINDEXING)
			{
				//update index
				IndexingEngineFactory.updateIndex(ontoInfo, defIns.getURI(), transObj.getLang(), SearchServiceImplOWLART.index_definition);
			}
		}

		if(defObject.getIDSource()!=null)	defIns.addPropertyValue(owlModel.getOWLProperty(ModelConstants.RTAKENFROMSOURCE), defObject.getIDSource());
		if(defObject.getIDSourceURL()!=null)	defIns.addPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASSOURCELINK), defObject.getIDSourceURL());
		
		defIns.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED), df.format(defObject.getIDDateCreate()));
		defIns.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
		
		individual.addPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDEFINITION), defIns);
		individual.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());	
		
		
	}
	
	public void deleteDefinition(Validation val, OntologyInfo ontoInfo)
	{
		IDObject defObject = (IDObject) val.getNewObject().get(0);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(val.getConceptObject().getName()));
		OWLIndividual defIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, individual, owlModel.getOWLProperty(ModelConstants.RHASDEFINITION), defObject.getIDUri());
		
		if(ConfigConstants.ISINDEXING)
		{
			//update index
			IndexingEngineFactory.deleteIndex(ontoInfo, defIns.getURI(), SearchServiceImplOWLART.index_definition);
		}
		defIns.delete();
		individual.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());	
	}
	
	public void addDefinitionTranslation(Validation val, OntologyInfo ontoInfo)
	{
		TranslationObject transObj = (TranslationObject) val.getOldObject().get(0);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(val.getConceptObject().getName()));
		OWLIndividual defIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, individual, owlModel.getOWLProperty(ModelConstants.RHASDEFINITION), transObj.getUri());
		defIns.addLabel(transObj.getLabel(), transObj.getLang());
		defIns.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
		individual.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
		
		if(ConfigConstants.ISINDEXING)
		{
			//update index
			IndexingEngineFactory.updateIndex(ontoInfo, defIns.getURI(), transObj.getLang(), SearchServiceImplOWLART.index_definition);
		}
	}
	
	public void deleteDefinitionTranslation(Validation val, OntologyInfo ontoInfo)
	{
		TranslationObject transObj = (TranslationObject) val.getNewObject().get(0);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(val.getConceptObject().getName()));
		OWLIndividual defIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, individual, owlModel.getOWLProperty(ModelConstants.RHASDEFINITION), transObj.getUri());
		defIns.removeLabel(transObj.getLabel(), transObj.getLang());
		defIns.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
		individual.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());	
		
		if(ConfigConstants.ISINDEXING)
		{
			//update index
			IndexingEngineFactory.updateIndex(ontoInfo, defIns.getURI(), transObj.getLang(), SearchServiceImplOWLART.index_definition);
		}
	}
	
	public void addDefinitionSource(Validation val)
	{
		IDObject defObject = (IDObject) val.getOldObject().get(0);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(val.getConceptObject().getName()));
		OWLIndividual defIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, individual, owlModel.getOWLProperty(ModelConstants.RHASDEFINITION), defObject.getIDUri());
		defIns.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RTAKENFROMSOURCE), defObject.getIDSource());
		defIns.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASSOURCELINK), defObject.getIDSourceURL());
		defIns.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
		individual.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());	
	}
	
	public void deleteDefinitionSource(Validation val)
	{
		IDObject defObject = (IDObject) val.getNewObject().get(0);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(val.getConceptObject().getName()));
		OWLIndividual defIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, individual, owlModel.getOWLProperty(ModelConstants.RHASDEFINITION), defObject.getIDUri());
		defIns.removePropertyValue(owlModel.getOWLProperty(ModelConstants.RTAKENFROMSOURCE), defObject.getIDSource());
		defIns.removePropertyValue(owlModel.getOWLProperty(ModelConstants.RHASSOURCELINK), defObject.getIDSourceURL());
		defIns.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
		individual.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());	
	}
	
	public void deleteImage(Validation val, OntologyInfo ontoInfo)
	{
		IDObject imgObject = (IDObject) val.getNewObject().get(0);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(val.getConceptObject().getName()));
		OWLIndividual imgIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, individual, owlModel.getOWLProperty(ModelConstants.RHASIMAGE), imgObject.getIDUri());
		
		if(ConfigConstants.ISINDEXING)
		{
			//update index
			IndexingEngineFactory.deleteIndex(ontoInfo, imgIns.getURI(), SearchServiceImplOWLART.index_ImgDescription);
		}
		
		imgIns.delete();
		individual.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());	
	}
	
	public void undeleteImage(Validation val, OntologyInfo ontoInfo)
	{
		IDObject imgObject = (IDObject)val.getOldObject().get(0);
		OWLNamedClass cls = owlModel.getOWLNamedClass(val.getConceptObject().getName());
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
		OWLNamedClass imgCls = owlModel.getOWLNamedClass(ModelConstants.CIMAGE);

		
		OWLIndividual imgIns = imgCls.createOWLIndividual(imgObject.getIDName());
		ArrayList<TranslationObject> stObjects = imgObject.getIDTranslationList();
		for(int i=0;i<stObjects.size();i++)
		{
			TranslationObject transObj = (TranslationObject)stObjects.get(i);
			imgIns.addLabel(transObj.getLabel(), transObj.getLang());
			imgIns.addPropertyValue(owlModel.getRDFProperty(RDFSNames.Slot.COMMENT), owlModel.createRDFSLiteral(transObj.getDescription(), transObj.getLang()));
			
			if(ConfigConstants.ISINDEXING)
			{
				//update index
				IndexingEngineFactory.updateCommentIndex(ontoInfo, imgIns.getURI(), transObj.getLang(), SearchServiceImplOWLART.index_ImgDescription);
			}
			
		}
		
		if(imgObject.getIDSource()!=null)	imgIns.addPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASIMAGESOURCE), imgObject.getIDSource());
		if(imgObject.getIDSource()!=null)	imgIns.addPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASIMAGELINK), imgObject.getIDSourceURL());
		imgIns.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED), df.format(imgObject.getIDDateCreate()));
		imgIns.setPropertyValue(cls.getOWLModel().getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
		
		individual.addPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASIMAGE), imgIns);
		individual.setPropertyValue(cls.getOWLModel().getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());	
	}
	
	public void addImageTranslation(Validation val, OntologyInfo ontoInfo)
	{
		TranslationObject transObj = (TranslationObject) val.getOldObject().get(0);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(val.getConceptObject().getName()));
		OWLIndividual imgIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, individual, owlModel.getOWLProperty(ModelConstants.RHASIMAGE), transObj.getUri());
		imgIns.addLabel(transObj.getLabel(), transObj.getLang());
		imgIns.addPropertyValue(owlModel.getRDFProperty(RDFSNames.Slot.COMMENT), owlModel.createRDFSLiteral(transObj.getDescription(), transObj.getLang()));
		imgIns.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
		individual.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());	
		
		if(ConfigConstants.ISINDEXING)
		{
			//update index
			IndexingEngineFactory.updateCommentIndex(ontoInfo, imgIns.getURI(), transObj.getLang(), SearchServiceImplOWLART.index_ImgDescription);
		}
	}
	
	public void deleteImageTranslation(Validation val, OntologyInfo ontoInfo)
	{
		TranslationObject transObj = (TranslationObject) val.getNewObject().get(0);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(val.getConceptObject().getName()));
		OWLIndividual imgIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, individual, owlModel.getOWLProperty(ModelConstants.RHASIMAGE), transObj.getUri());
		imgIns.removeLabel(transObj.getLabel(), transObj.getLang());
		imgIns.removePropertyValue(owlModel.getRDFProperty(RDFSNames.Slot.COMMENT), owlModel.createRDFSLiteral(transObj.getDescription(), transObj.getLang()));
		imgIns.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
		individual.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());	
		
		if(ConfigConstants.ISINDEXING)
		{
			//update index
			IndexingEngineFactory.updateCommentIndex(ontoInfo, imgIns.getURI(), transObj.getLang(), SearchServiceImplOWLART.index_ImgDescription);
		}
	}
	
	public void addImageSource(Validation val)
	{
		IDObject imgObject = (IDObject) val.getOldObject().get(0);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(val.getConceptObject().getName()));
		OWLIndividual imgIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, individual, owlModel.getOWLProperty(ModelConstants.RHASIMAGE), imgObject.getIDUri());
		imgIns.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASIMAGESOURCE), imgObject.getIDSource());
		imgIns.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASIMAGELINK), imgObject.getIDSourceURL());
		imgIns.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
		individual.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());	
	
	}
	
	public void deleteImageSource(Validation val)
	{
		
		IDObject imgObject = (IDObject) val.getNewObject().get(0);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(val.getConceptObject().getName()));
		OWLIndividual imgIns = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, individual, owlModel.getOWLProperty(ModelConstants.RHASIMAGE), imgObject.getIDUri());
		imgIns.removePropertyValue(owlModel.getOWLProperty(ModelConstants.RHASIMAGESOURCE), imgObject.getIDSource());
		imgIns.removePropertyValue(owlModel.getOWLProperty(ModelConstants.RHASIMAGELINK), imgObject.getIDSourceURL());
		imgIns.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
		individual.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());	
	
	}
	
	public void addNonFunctional(Validation val, OntologyInfo ontoInfo) 
	{
		AttributesObject attObj = (AttributesObject) val.getOldObject().get(0);
		if(attObj!=null)
		{
			NonFuncObject nfObj = attObj.getValue();
			OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(val.getConceptObject().getName()));
			OWLProperty property = owlModel.getOWLProperty(attObj.getRelationshipObject().getName());
			if(nfObj.getLanguage()!=null && !nfObj.getLanguage().equals(""))
				individual.addPropertyValue(property, owlModel.createRDFSLiteral(nfObj.getValue(), nfObj.getLanguage()));
			else
				individual.addPropertyValue(property, nfObj.getValue());
			individual.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
			
			if(ConfigConstants.ISINDEXING)
			{
				//update index
				if(attObj.getRelationshipObject().getName().equals(ModelConstants.RHASSCOPENOTE))
					IndexingEngineFactory.updateIndex(ontoInfo, individual.getURI(), property.getURI(), nfObj.getLanguage(), SearchServiceImplOWLART.index_scopeNotes);
			}
		}
	}
	
	public void deleteNonFunctional(Validation val, OntologyInfo ontoInfo) 
	{
		AttributesObject attObj = (AttributesObject) val.getNewObject().get(0);
		if(attObj!=null)
		{
			OWLProperty property = owlModel.getOWLProperty(attObj.getRelationshipObject().getName());
			if(attObj!=null)
			{
				NonFuncObject nfObj = attObj.getValue();
				OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(val.getConceptObject().getName()));
				if(nfObj.getLanguage()!=null && !nfObj.getLanguage().equals(""))
				{
					for (Iterator iter = individual.getPropertyValues(property).iterator(); iter.hasNext();) {
						RDFSLiteral element = (RDFSLiteral) iter.next();
						if(element.getString().equals(nfObj.getValue()) && element.getLanguage().equals(nfObj.getLanguage())){
							individual.removePropertyValue(property, element);
						}
					}
				}
				else
				{
					individual.removePropertyValue(property, nfObj.getValue());
				}
				individual.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
				
				if(ConfigConstants.ISINDEXING)
				{
					//update index
					if(property.getName().equals(ModelConstants.RHASSCOPENOTE))
						IndexingEngineFactory.updateIndex(ontoInfo, individual.getURI(), property.getURI(), nfObj.getLanguage(), SearchServiceImplOWLART.index_scopeNotes);
				}
			}
			
			
		}
	
	}
	
	public void addRelationship(Validation val)
	{
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(val.getConceptObject().getName()));
		OWLIndividual destindividual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(((ConceptObject)val.getOldObject().get(0)).getName()));
		individual.addPropertyValue(owlModel.getOWLObjectProperty(val.getOldRelationshipObject().getName()), destindividual);
		individual.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());		
	}
	
	public void deleteRelationship(Validation val)
	{
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(val.getConceptObject().getName()));
		OWLIndividual destindividual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(((ConceptObject)val.getNewObject().get(0)).getName()));
		individual.removePropertyValue(owlModel.getOWLObjectProperty(val.getNewRelationshipObject().getName()), destindividual);
		individual.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());		
	}
	
	public void addTermRelationship(Validation val)
	{
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel,owlModel.getOWLNamedClass(val.getConceptObject().getName()) );
		OWLIndividual destindividual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(((TermObject)val.getOldObject().get(0)).getConceptName()));

		OWLIndividual term = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, individual, individual.getOWLModel().getOWLProperty(ModelConstants.RHASLEXICALIZATION), val.getTermObject().getUri());
		OWLIndividual destTerm = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, destindividual, destindividual.getOWLModel().getOWLProperty(ModelConstants.RHASLEXICALIZATION), ((TermObject)val.getOldObject().get(0)).getUri());

		term.addPropertyValue(owlModel.getOWLObjectProperty(val.getOldRelationshipObject().getName()), destTerm);
		term.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
		destTerm.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
		individual.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());		
		destindividual.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());			
	}
	
	public void deleteTermRelationship(Validation val)
	{
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel,owlModel.getOWLNamedClass(val.getConceptObject().getName()) );
		OWLIndividual destindividual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(((TermObject)val.getNewObject().get(0)).getConceptName()));

		OWLIndividual term = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, individual, individual.getOWLModel().getOWLProperty(ModelConstants.RHASLEXICALIZATION), val.getTermObject().getUri());
		OWLIndividual destTerm = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, destindividual, destindividual.getOWLModel().getOWLProperty(ModelConstants.RHASLEXICALIZATION), ((TermObject)val.getNewObject().get(0)).getUri());

		term.removePropertyValue(owlModel.getOWLObjectProperty(val.getNewRelationshipObject().getName()), destTerm);
		term.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
		destTerm.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
		individual.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());		
		destindividual.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());	
	}
	
	public void addNonFunctionalTerm(Validation val) 
	{
		AttributesObject attObj = (AttributesObject) val.getOldObject().get(0);
		if(attObj!=null)
		{
			NonFuncObject nfObj = attObj.getValue();
			OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(val.getConceptObject().getName()));
			OWLIndividual term = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, individual, individual.getOWLModel().getOWLProperty(ModelConstants.RHASLEXICALIZATION), val.getTermObject().getUri());
			
			if(nfObj.getLanguage()!=null && !nfObj.getLanguage().equals(""))
				term.addPropertyValue(owlModel.getOWLProperty(attObj.getRelationshipObject().getName()), owlModel.createRDFSLiteral(nfObj.getValue(), nfObj.getLanguage()));
			else
				term.addPropertyValue(owlModel.getOWLProperty(attObj.getRelationshipObject().getName()), nfObj.getValue());
			
			term.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
			individual.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());		
		}
	}
	
	public void deleteNonFunctionalTerm(Validation val) 
	{
		
		AttributesObject attObj = (AttributesObject) val.getNewObject().get(0);
		if(attObj!=null)
		{
			OWLProperty property = owlModel.getOWLProperty(attObj.getRelationshipObject().getName());
			if(attObj!=null)
			{
				NonFuncObject nfObj = attObj.getValue();

				OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(val.getConceptObject().getName()));
				OWLIndividual term = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, individual, individual.getOWLModel().getOWLProperty(ModelConstants.RHASLEXICALIZATION), val.getTermObject().getUri());
				
				if(nfObj.getLanguage()!=null && !nfObj.getLanguage().equals(""))
				{
					for (Iterator iter = individual.getPropertyValues(property).iterator(); iter.hasNext();) {
						RDFSLiteral element = (RDFSLiteral) iter.next();
						if(element.getString().equals(nfObj.getValue()) && element.getLanguage().equals(nfObj.getLanguage())){
							term.removePropertyValue(property, element);
						}
					}
				}
				else
				{
					term.removePropertyValue(property, nfObj.getValue());
				}

				term.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
				individual.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
			}
		}
	}
	
	/*public void addTermCodes(Validation val)
	{
		TermCodesObject tcObject = (TermCodesObject) val.getOldObject().get(0);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(val.getConceptObject().getName()));
		OWLIndividual term = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, individual, individual.getOWLModel().getOWLProperty(ModelConstants.RHASLEXICALIZATION), val.getTermObject().getUri());
		term.addPropertyValue(owlModel.getOWLProperty(tcObject.getRepository().getName()), tcObject.getCode());
		term.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
		individual.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
	}
	
	public void editTermCodes(Validation val)
	{
		TermCodesObject tcObject = (TermCodesObject) val.getOldObject().get(0);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(val.getConceptObject().getName()));
		OWLIndividual term = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, individual, individual.getOWLModel().getOWLProperty(ModelConstants.RHASLEXICALIZATION), val.getTermObject().getUri());
		term.setPropertyValue(owlModel.getOWLProperty(tcObject.getRepository().getName()), tcObject.getCode());
		term.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
		individual.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
	}
	
	public void deleteTermCodes(Validation val)
	{
		TermCodesObject tcObject = (TermCodesObject) val.getNewObject().get(0);
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(val.getConceptObject().getName()));
		OWLIndividual term = ProtegeWorkbenchUtility.getConceptPropertyValue(owlModel, individual, individual.getOWLModel().getOWLProperty(ModelConstants.RHASLEXICALIZATION), val.getTermObject().getUri());
		term.removePropertyValue(owlModel.getOWLProperty(tcObject.getRepository().getName()), tcObject.getCode());
		term.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
		individual.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());
	}*/
	
	public void deleteScheme(Validation val)
	{
		SchemeObject sObj = (SchemeObject) val.getNewObject().get(0);
		
		for (Iterator it = owlModel.getOWLNamedClass(ModelConstants.CCATEGORY).getSubclasses(true).iterator(); it.hasNext();) 
		{		    		    
			OWLNamedClass cls = (OWLNamedClass) it.next();			
			for (Iterator<OWLIndividual> jt = cls.getInstances(false).iterator(); jt.hasNext();) 
			{
				OWLIndividual individual = (OWLIndividual) jt.next();
	        	Object obj = individual.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RBELONGSTOSCHEME));
	        	if(obj instanceof OWLIndividual)
	        	{
	        		OWLIndividual scheme = (OWLIndividual) obj;
		        	if(scheme.getName().equals(sObj.getSchemeName()))
		        	{
		        		individual.delete();
		        		cls.delete();
		        	}
	        	}
    			
			}
		}
		
		for (Iterator iter = owlModel.getOWLNamedClass(ModelConstants.CCLASSIFICATIONSCHEME).getInstances(true).iterator(); iter.hasNext();) {
			OWLIndividual schemeInsa = (OWLIndividual) iter.next();
			if(schemeInsa.getName().equals(sObj.getSchemeName()))
			{
				schemeInsa.delete();
				schemeInsa = null;
			}
			
		}
		
		OWLObjectProperty isSubOf = owlModel.getOWLObjectProperty(sObj.getRIsSub());
		if(isSubOf!=null)
		{
			isSubOf.delete();
		}

		
		OWLObjectProperty hasSub = owlModel.getOWLObjectProperty(sObj.getRHasSub());
		if(isSubOf!=null)
		{
			hasSub.delete();
		}
		
		if(owlModel.getNamespaceManager().getPrefix(sObj.getNamespace())!=null)
			owlModel.getNamespaceManager().removePrefix(sObj.getNameSpaceCatagoryPrefix());

	}
	
	public void addSchemeMapping(Validation val)
	{
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(((ConceptObject) val.getConceptObject()).getName()));
		OWLIndividual destIndividual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(((ConceptObject) val.getOldObject().get(0)).getName()));
		individual.addPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASMAPPEDDOMAINCONCEPT), destIndividual);
		individual.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());		
	}
	
	public void deleteSchemeMapping(Validation val)
	{
		OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(((ConceptObject) val.getConceptObject()).getName()));
		OWLIndividual destIndividual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(((ConceptObject) val.getNewObject().get(0)).getName()));
		individual.removePropertyValue(owlModel.getOWLProperty(ModelConstants.RHASMAPPEDDOMAINCONCEPT), destIndividual);
		individual.setPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE), org.fao.aoscs.server.utility.Date.getDateTime());		
	}
	
	public RecentChangesInitObject getRecentChangesInitData(int ontologyId){
		RecentChangesInitObject rcio = new RecentChangesInitObject();
		rcio.setActions(getAction());
		rcio.setUsers(getAllUsers());
		rcio.setSize(getRecentChangesSize(ontologyId));
		return rcio;
	}

	public int getRecentChangesSize(int ontologyId)
	{
		String query = "select * from recent_changes where ontology_id = "+ontologyId+" OR ontology_id = 0";
		try 
		{
			ArrayList<RecentChanges> list = (ArrayList<RecentChanges>) HibernateUtilities.currentSession().createSQLQuery(query).addEntity("rc",RecentChanges.class).list();
			return list.size();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return 0;
		}
		finally 
		{
			HibernateUtilities.closeSession();
		}
	}
	
	public ArrayList<RecentChanges> getRecentChangesData(int ontologyId, int modifiedId)
	{
		String query = "select * from recent_changes where (ontology_id = '"+ontologyId+"' OR ontology_id = 0) and modified_id = '"+modifiedId+"'";
		try 
		{
			ArrayList<RecentChanges> list = (ArrayList<RecentChanges>) HibernateUtilities.currentSession().createSQLQuery(query).addEntity("rc",RecentChanges.class).list();
			return setRecentChanges(list);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return new ArrayList<RecentChanges>();
		}
		finally 
		{
			HibernateUtilities.closeSession();
		}
	}
	
	public ArrayList<RecentChanges> getRecentChangesData(int ontologyId)
	{
		String query = "select rc.* from recent_changes rc where (ontology_id = '"+ontologyId+"' OR ontology_id = 0)";
		try 
		{
			ArrayList<RecentChanges> list = (ArrayList<RecentChanges>) HibernateUtilities.currentSession().createSQLQuery(query).addEntity("rc",RecentChanges.class).list();
			return setRecentChanges(list);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return new ArrayList<RecentChanges>();
		}
		finally 
		{
			HibernateUtilities.closeSession();
		}
	}
	
	public ArrayList<RecentChanges> setRecentChanges(ArrayList<RecentChanges> list)
	{
		ArrayList<RecentChanges> rcList = new ArrayList<RecentChanges>();
		for(int i=0;i<list.size();i++)
		{
			RecentChanges rc = list.get(i);
			rc.setModifiedObject(DatabaseUtil.getObjectWrappedInArray(rc.getObject()));
			rc.setObject(null);
			rcList.add(rc);
		}
		return rcList;
	}
	  
	  public ArrayList<RecentChanges> requestRecentChangesRows(Request request, int ontologyId)
	{
		   HashMap<String, String> col = new HashMap<String, String>();
		   col.put("1", "rc.modified_id");
		   col.put("2", "rc.modified_object");
		   col.put("3", "oa.action");
		   col.put("4", "u.username");
		   col.put("5", "rc.modified_date");
		   col.put("6", "rc.ontology_id");

		   
		   String orderBy = " rc.modified_date desc ";
		  // Get the sort info, even though we ignore it
		    ColumnSortList sortList = request.getColumnSortList();
		    if(sortList!=null)
		    {
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
		    
		    String query = "select rc.*, oa.action, u.username from " +
							"recent_changes rc, owl_action oa, users u " +
							"where " +
							"( rc.ontology_id = "+ontologyId+" OR rc.ontology_id = 0) " +
							"and rc.modifier_id = u.user_id " +
							"and rc.modified_action = oa.id " +
							"order by "+orderBy+" LIMIT "+numRow+" OFFSET "+startRow;			    
		    try 
			{
				ArrayList<RecentChanges> list = (ArrayList<RecentChanges>) HibernateUtilities.currentSession().createSQLQuery(query).addEntity("rc",RecentChanges.class).list();				
				return setRecentChanges(list);
			}
			catch (Exception ex) 
			{
				ex.printStackTrace();
				return new ArrayList<RecentChanges>();
			}
			finally 
			{
				HibernateUtilities.closeSession();
			}

		  }
	  	  	  

	  
	  
}
