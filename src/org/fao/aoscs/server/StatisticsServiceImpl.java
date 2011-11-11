package org.fao.aoscs.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;

import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.gwt.GwtConfigurationHelper;
import net.sf.gilead.gwt.PersistentRemoteService;

import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.client.module.constant.OWLStatusConstants;
import org.fao.aoscs.client.module.statistic.service.StatisticsService;
import org.fao.aoscs.domain.ClassObject;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.ExportParameterObject;
import org.fao.aoscs.domain.InitializeStatisticalData;
import org.fao.aoscs.domain.LanguageCode;
import org.fao.aoscs.domain.ObjectPerUserStat;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.RecentChangeData;
import org.fao.aoscs.domain.RecentChanges;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.RelationshipTreeObject;
import org.fao.aoscs.domain.StatisticalData;
import org.fao.aoscs.domain.TermObject;
import org.fao.aoscs.domain.Users;
import org.fao.aoscs.domain.Validation;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.hibernate.QueryFactory;
import org.fao.aoscs.server.protege.ProtegeModelFactory;
import org.fao.aoscs.server.protege.ProtegeWorkbenchUtility;
import org.hibernate.Session;

import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;


public class StatisticsServiceImpl extends PersistentRemoteService implements StatisticsService{
	
	private static final long serialVersionUID = -7625187085018959830L;
	
	public static final ArrayList<Integer> TERM_CREATE_ACTION = new ArrayList<Integer>(Arrays.asList(new Integer[] {6}));
	public static final ArrayList<Integer> TERM_EDIT_ACTION = new ArrayList<Integer>(Arrays.asList(new Integer[] {7}));
	public static final ArrayList<Integer> TERM_DELETE_ACTION = new ArrayList<Integer>(Arrays.asList(new Integer[] {8}));
	
	public static final ArrayList<Integer> CONCEPT_CREATE_ACTION = new ArrayList<Integer>(Arrays.asList(new Integer[] {1}));
	public static final ArrayList<Integer> CONCEPT_EDIT_ACTION = new ArrayList<Integer>(Arrays.asList(new Integer[] {3,4,5,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39}));
	public static final ArrayList<Integer> CONCEPT_DELETE_ACTION = new ArrayList<Integer>(Arrays.asList(new Integer[] {2}));
	
	public static final int TERM_CREATE = 0; 
	public static final int TERM_EDIT = 1; 
	public static final int TERM_DELETE = 2; 
	public static final int CONCEPT_CREATE = 3; 
	public static final int CONCEPT_EDIT = 4;
	public static final int CONCEPT_DELETE = 5; 
	
	

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
	
	public InitializeStatisticalData getInitializeStatisticalData(OntologyInfo ontoInfo)
	{
		InitializeStatisticalData initData = new InitializeStatisticalData();
		try
		{
			OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
			ArrayList<RelationshipObject> relList = getRelationships(owlModel);
			initData.setStatusList(getStatus());
			initData.setUserList(getUsers());
			initData.setLanguageList(getLanguage());
			initData.setRelationshipList(relList);
			///owlModel.dispose();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return initData;
	}
	
	public StatisticalData getUserStat()
	{
		StatisticalData statData = new StatisticalData();
		statData.setCountNumberOfUser(countNumberOfUser());
		statData.setCheckWhoLastConnected(checkWhoLastConnected());
		return statData;
	}
	
	public StatisticalData getStatPerLang()
	{
		StatisticalData statData = new StatisticalData();
		statData.setCountNumberOfUsersPerLanguage(countNumberOfUsersPerLanguage());
		return statData;
	}
	
	public StatisticalData getStatPerStatus(OntologyInfo ontoInfo)
	{
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		StatisticalData statData = new StatisticalData();
		statData.setCountNumberOfTermPerStatus(countNumberOfTermPerStatus(owlModel));
		statData.setCountNumberOfConceptPerStatus(countNumberOfConceptPerStatus(owlModel));
		///owlModel.dispose();
		return statData;
	}
	
	public StatisticalData StatPerUser(OntologyInfo ontoInfo)
	{
		StatisticalData statData = new StatisticalData();
		
		statData.setCountNumberOfTermCreatedByUser(countUserAction(ontoInfo.getOntologyId() , TERM_CREATE ));
		statData.setCountNumberOfTermEditedByUser(countUserAction(ontoInfo.getOntologyId() , TERM_EDIT ));
		statData.setCountNumberOfTermDeletedByUser(countUserAction(ontoInfo.getOntologyId() , TERM_DELETE ));
		
		statData.setCountNumberOfConceptCreatedByUser(countUserAction(ontoInfo.getOntologyId() , CONCEPT_CREATE ));
		statData.setCountNumberOfConceptEditedByUser(countUserAction(ontoInfo.getOntologyId() , CONCEPT_EDIT ));
		statData.setCountNumberOfConceptDeletedByUser(countUserAction(ontoInfo.getOntologyId() , CONCEPT_DELETE ));
		
		return statData;
	}
	
	
	public StatisticalData getActionPerUser(OntologyInfo ontoInfo)
	{
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		StatisticalData statData = new StatisticalData();
		statData.setCountNumberOfRelationshipsPerUsers(countNumberOfRelationshipsPerUsers(ontoInfo.getOntologyId(), owlModel));
		statData.setCheckNumberOfLastModificationPerUser(checkNumberOfLastModificationPerUser());
		statData.setCheckNumberOfConnectionPerUser(checkNumberOfConnectionPerUser());
		///owlModel.dispose();
		return statData;
	}
	
	public StatisticalData getStatPerRelationship(OntologyInfo ontoInfo)
	{
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		ArrayList<RelationshipObject> relList = getRelationships(owlModel);
		StatisticalData statData = new StatisticalData();
		statData.setCountNumberOfConceptPerRelationship(countNumberOfConceptPerRelationship(relList, owlModel));
		statData.setListTheDomainAndRangeForRelationship(listTheDomainAndRangeForRelationship(relList, owlModel));
		///owlModel.dispose();
		return statData;
	}
	
	public StatisticalData getExportStat(OntologyInfo ontoInfo)
	{
		StatisticalData statData = new StatisticalData();
		statData.setCountNumberOfExports(countNumberOfExports(ontoInfo.getOntologyId()));
		return statData;
	}
	
	public HashMap<String, Integer> countNumberOfUsersPerLanguage()
	{
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		try 
		{
			String query = "SELECT LCASE(language_code), count(user_id)  FROM  users_language WHERE status='1' GROUP BY language_code ORDER BY language_code";
			ArrayList<String[]> list = QueryFactory.getHibernateSQLQuery(query);
			for(int i=0;i<list.size();i++)
			{
				String[] str = list.get(i);
				try
				{
					int cnt = str[1]==null?0:Integer.parseInt(str[1]);
					map.put(str[0], cnt);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
			}
			return map;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return map;
		} 
		finally 
		{
			HibernateUtilities.closeSession();
		}
	}
	
	public int countNumberOfUser(){
		try 
		{
			String query = "SELECT * FROM users ORDER BY username;";
			return HibernateUtilities.currentSession().createSQLQuery(query).addEntity(Users.class).list().size();
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
	
	public ObjectPerUserStat countUserAction(int ontologyId , int action_id){
		
		HashMap<Integer, Integer> cntProposedMap = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> cntValidatedMap = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> cntPublishedMap = new HashMap<Integer, Integer>();
		
		ArrayList<Users> userList =  getUsers();
		for (int i = 0; i < userList.size(); i++) 
		{
			Users u = (Users) userList.get(i);
			cntProposedMap.put(u.getUserId(), 0);
			cntValidatedMap.put(u.getUserId(), 0);
			cntPublishedMap.put(u.getUserId(), 0);
		}
		
		ArrayList<RecentChanges> list = getRecentChangesData(ontologyId, action_id); // action id for term-create = 6;
		
		for (int i = 0; i < list.size(); i++) 
		{
			RecentChanges c = (RecentChanges) list.get(i);
			int userid = c.getModifierId();
			
			try
			{
				Object obj = c.getModifiedObject().get(0);
				if(obj instanceof Validation)
				{
					Validation val = (Validation) obj;
					if(val!=null)
					{
							
    					if( (action_id == TERM_CREATE || action_id == TERM_EDIT || action_id == TERM_DELETE ))
    					{
    						TermObject tObj = val.getTermObject();
    						if(tObj != null)
    						{
    						    String status = tObj.getStatus();
                                if(status != null && !status.isEmpty())
                                {
    								if(status.equals(OWLStatusConstants.PROPOSED))
    								{
    									int tmp = cntProposedMap.get(userid);
    									tmp++;
    									cntProposedMap.remove(userid);
    									cntProposedMap.put(userid, tmp);
    								}
    								else if(status.equals(OWLStatusConstants.VALIDATED))
    								{
    									int tmp = cntValidatedMap.get(userid);
    									tmp++;
    									cntValidatedMap.remove(userid);
    									cntValidatedMap.put(userid, tmp);
    								}
    								else if(status.equals(OWLStatusConstants.PUBLISHED))
    								{
    									int tmp = cntPublishedMap.get(userid);
    									tmp++;
    									cntPublishedMap.remove(userid);
    									cntPublishedMap.put(userid, tmp);
    								}
                                }
    						}
    					}else if(action_id == CONCEPT_CREATE || action_id == CONCEPT_EDIT || action_id == CONCEPT_DELETE )
    					{
    						ConceptObject cObj = val.getConceptObject();
    						if(cObj != null)
    						{	
    						    String status = cObj.getStatus();
    						    if(status != null && !status.isEmpty())
    						    {
    						        if(status.equals(OWLStatusConstants.PROPOSED))
    								{
    									int tmp = cntProposedMap.get(userid);
    									tmp++;
    									cntProposedMap.remove(userid);
    									cntProposedMap.put(userid, tmp);
    								}
    								else if(status.equals(OWLStatusConstants.VALIDATED))
    								{
    									int tmp = cntValidatedMap.get(userid);
    									tmp++;
    									cntValidatedMap.remove(userid);
    									cntValidatedMap.put(userid, tmp);
    								}
    								else if(status.equals(OWLStatusConstants.PUBLISHED))
    								{
    									int tmp = cntPublishedMap.get(userid);
    									tmp++;
    									cntPublishedMap.remove(userid);
    									cntPublishedMap.put(userid, tmp);
    								}
    						    }
    					    }
    					}
					}
				}
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		ObjectPerUserStat map = new ObjectPerUserStat();
		map.setCountProposed(cntProposedMap);
		map.setCountValidated(cntValidatedMap);
		map.setCountPublished(cntPublishedMap);
		return map;
	}
	

	
	public HashMap<String, Integer> countNumberOfTermPerStatus(OWLModel owlModel){
		
		HashMap<String, Integer> termPerStatus = new HashMap<String, Integer>();
		ArrayList<OwlStatus> statusList = getStatus();
		for(Iterator<OwlStatus> itr = statusList.iterator(); itr.hasNext();)
		{
		
			OwlStatus status = itr.next();
			if(!status.getStatus().equals("deleted"))
			{
				OWLNamedClass nameClass = owlModel.getOWLNamedClass(ModelConstants.CDOMAINCONCEPT);
				int count =0;
				for (Iterator<?> iterator = nameClass.getSubclasses(true).iterator(); iterator.hasNext();) {
					OWLNamedClass subNameClass = (OWLNamedClass) iterator.next();
					ConceptObject cObj = ProtegeWorkbenchUtility.makeConceptObject(owlModel, subNameClass);
					HashMap<String, TermObject> hashMap = cObj.getTerm();
					Iterator<String> i = hashMap.keySet().iterator();
					while (i.hasNext()) {
						String key = i.next().toString();
						TermObject termObject = hashMap.get(key);
						String termStatus = termObject.getStatus();
						if (status.getStatus().equals(termStatus)){
							count++;
						}
					}
				}
				termPerStatus.put(status.getStatus(), count);
			}
			
		}
		return termPerStatus;
		
	}
	
	public HashMap<String, Integer> countNumberOfConceptPerStatus(OWLModel owlModel){
		
			HashMap<String, Integer> conceptPerStatus = new HashMap<String, Integer>();
			ArrayList<OwlStatus> statusList = getStatus();
			for(Iterator<OwlStatus> itr = statusList.iterator(); itr.hasNext();)
			{
			
				OwlStatus status = itr.next();
				if(!status.getStatus().equals("deleted"))
				{
					OWLNamedClass nameClass = owlModel.getOWLNamedClass(ModelConstants.CDOMAINCONCEPT);
					int count =0;
					for (Iterator<?> iterator = nameClass.getSubclasses(true).iterator(); iterator.hasNext();) {
						OWLNamedClass subNameClass = (OWLNamedClass) iterator.next();
						ConceptObject cObj = ProtegeWorkbenchUtility.makeConceptObject(owlModel, subNameClass);
						String conceptStatus = cObj.getStatus();
						if (status.getStatus().equals(conceptStatus)){
							count++;
						}
					}
					conceptPerStatus.put(status.getStatus(), count);
				}
			}
		return conceptPerStatus;
	}
	
	public HashMap<Integer, Integer> countNumberOfRelationshipsPerUsers(int ontologyId, OWLModel owlModel){
		
		HashMap<Integer, Integer> relPerUser = new HashMap<Integer, Integer>();
		ArrayList<String[]> list = new ArrayList<String[]>();
        try 
        {
            String query = "select count(*),modifier_id from recent_changes where ontology_id = '"+ontologyId+"' AND modified_action = '3' Group by modifier_id";
            list = QueryFactory.getHibernateSQLQuery(query);
            if(!list.isEmpty())
            {
                for(String[] rc : list){
                    relPerUser.put(Integer.parseInt(rc[1]), Integer.parseInt(rc[0]));
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
		
		return relPerUser;
	}
	
	
	public HashMap<Integer, Integer> checkNumberOfConnectionPerUser(){
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		try 
		{
			
			String query = "SELECT user_id, count(*) FROM users_visits GROUP BY user_id ORDER BY 2 DESC";
			ArrayList<String[]> list = QueryFactory.getHibernateSQLQuery(query);
			for(int i=0;i<list.size();i++)
			{
				String[] str = list.get(i);
				int cnt = str[1]==null?0:Integer.parseInt(str[1]);
				map.put(Integer.parseInt(str[0]), cnt);
			}
			
			return map;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return map;
		} 
		finally 
		{
			HibernateUtilities.closeSession();
		}
	}
	
	public String checkWhoLastConnected(){
		// TODO re write a code to check who is last connected
		String username = "";
		try 
		{
			String query = "SELECT users.username FROM users WHERE user_id = ( SELECT user_id FROM users_visits HAVING max( `visit_id` ) )";
			ArrayList<String[]> list = QueryFactory.getHibernateSQLQuery(query);
			if(list.size()>0)
			{
				String[] str = list.get(0);
				if(str.length>0)
					username = str[0];
			}
			return username;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return username;
		} 
		finally 
		{
			HibernateUtilities.closeSession();
			
		}
	}
	
	
	public HashMap<Integer, String> checkNumberOfLastModificationPerUser(){
		
		HashMap<Integer, String> lmperuser = new HashMap<Integer, String>();
		try 
		{
			String query = "select rc1.modifier_id, a.action, a.action_child " +
					"from recent_changes AS rc1 " +
					"inner join owl_action AS a " +
					"ON rc1.modified_action = a.id " +
					"JOIN " +
					"(SELECT max(modified_id) modified_id, modifier_id " +
					"FROM recent_changes GROUP BY modifier_id) AS rc2 " +
					"ON rc1.modified_id = rc2.modified_id";
			ArrayList<String[]> list = QueryFactory.getHibernateSQLQuery(query);
			for(int i=0;i<list.size();i++)
			{
				String[] str = list.get(i);
				String action = str[1];
				action += (str[2]==null || str[2].equals(""))?"":"-"+str[2];
				lmperuser.put(Integer.parseInt(str[0]), action);
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
		return lmperuser;
	}
	
	public HashMap<String, Integer> countNumberOfConceptPerRelationship(ArrayList<RelationshipObject> relList, OWLModel owlModel){
		
		HashMap<String, Integer> conceptPerRelationship = new HashMap<String, Integer>();
		for(Iterator<RelationshipObject> itr = relList.iterator(); itr.hasNext();)
		{
			RelationshipObject relationship = itr.next();
			conceptPerRelationship.put(relationship.getUri(), 0);
		}
		for (Iterator<?> it = owlModel.getOWLNamedClass(ModelConstants.CDOMAINCONCEPT).getSubclasses(true).iterator(); it.hasNext();){
			OWLNamedClass subNameClass = (OWLNamedClass) it.next();
			OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, subNameClass);
			if(individual!=null)
			{
				for(Iterator<RelationshipObject> itr = relList.iterator(); itr.hasNext();)
				{
					RelationshipObject relationship = itr.next();
					OWLObjectProperty prop = owlModel.getOWLObjectProperty(relationship.getName());
					int propcount = individual.getPropertyValueCount(prop);
					if(propcount>0)
					{
						int cnt = conceptPerRelationship.get(relationship.getUri());
						conceptPerRelationship.remove(relationship.getUri());
						cnt++;
						conceptPerRelationship.put(relationship.getUri(), cnt);
					}
				}
			}
		}
		return conceptPerRelationship;
		
	}
	/*
	public HashMap<RelationshipObject, Integer> countNumberOfConceptPerRelationship(ArrayList<RelationshipObject> relList, OntologyInfo ontoInfo){
		
		HashMap<RelationshipObject, Integer> conceptPerRelationship = new HashMap<RelationshipObject, Integer>();
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		for(Iterator<RelationshipObject> itr = relList.iterator(); itr.hasNext();)
		{
			RelationshipObject relationship = itr.next();
			OWLNamedClass nameClass = owlModel.getOWLNamedClass(ModelConstants.CDOMAINCONCEPT);
			int count = 0;
			for (Iterator iterator = nameClass.getSubclasses(true).iterator(); iterator.hasNext();){
				OWLNamedClass subNameClass = (OWLNamedClass) iterator.next();
				ConceptObject cObj = ProtegeWorkbenchUtility.makeConceptObject(owlModel, subNameClass);
				String cls = cObj.getName();
				ConceptServiceImpl cImpl = new  ConceptServiceImpl();
				RelationObject rObj = cImpl.getConceptRelationship(cls,ontoInfo);
				HashMap<RelationshipObject, ArrayList<ConceptObject>> hashMap = rObj.getResult();
				Iterator i = hashMap.keySet().iterator();
				while (i.hasNext()) {
					RelationshipObject relationshipObject = (RelationshipObject) i.next();
					String relationShipName = relationshipObject.getName();
					if(relationship.getName().equals(relationShipName)){
						count++;
					}
				}
			}
			conceptPerRelationship.put(relationship, count);
		}
		return conceptPerRelationship;
		
	}
	*/
	public HashMap<String, HashMap<String, ArrayList<ClassObject>>> listTheDomainAndRangeForRelationship (ArrayList<RelationshipObject> relList, OWLModel owlModel){
		HashMap<String, HashMap<String, ArrayList<ClassObject>>> listDomainAndRangePerRelationship = new HashMap<String, HashMap<String, ArrayList<ClassObject>>>();
		RelationshipServiceImpl rsImpl = new RelationshipServiceImpl();
		for(Iterator<RelationshipObject> itr = relList.iterator(); itr.hasNext();)
		{
			RelationshipObject relationship = itr.next();
			HashMap<String, ArrayList<ClassObject>> map = new HashMap<String, ArrayList<ClassObject>>();
			map.put("domain", rsImpl.getDomain(relationship.getName(), owlModel));
			map.put("range", rsImpl.getRange(relationship.getName(), owlModel));
			listDomainAndRangePerRelationship.put(relationship.getUri(), map);
		}
		return listDomainAndRangePerRelationship;
		
	}
	
	public HashMap<String,Integer> countNumberOfExports(int ontologyId)
	{
		HashMap<String,Integer> exportStat = new HashMap<String,Integer>(); 
		ArrayList<RecentChanges> list = new ArrayList<RecentChanges>();
				
		String query = "select rc.* from recent_changes rc where ontology_id = '"+ontologyId+"' AND modified_action = '74' ";
		try 
		{			
			list = (ArrayList<RecentChanges>) HibernateUtilities.currentSession().createSQLQuery(query).addEntity("rc",RecentChanges.class).list();
			list = new ValidationServiceImpl().setRecentChanges(list);
			
			if(!list.isEmpty())
			{
				for(int i=0 ; i<list.size() ; i++)
				{
					RecentChanges rc = list.get(i);					
					Object obj = rc.getModifiedObject().get(0);
					if(obj instanceof RecentChangeData)						
					{
						RecentChangeData rcd = (RecentChangeData)rc.getModifiedObject().get(0);						
						if(rcd.getObject().get(0)!=null)
						{
							ExportParameterObject exObj = (ExportParameterObject)(rcd.getObject().get(0));
							if(exportStat.size()>0)
							{
								if(exportStat.containsKey(exObj.getExportFormat())){
									int val = exportStat.get(exObj.getExportFormat()) + 1;
									exportStat.put(exObj.getExportFormat(), val);						
								}else
									exportStat.put(exObj.getExportFormat(), 1);						
							}else
								exportStat.put(exObj.getExportFormat(), 1);
						}
					}	
				}							
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
		finally 
		{
			HibernateUtilities.closeSession();
		}
			
		return exportStat;
	}
	
	public ArrayList<OwlStatus> getStatus() {
		try {
			String query = "SELECT * FROM owl_status";
			Session s = HibernateUtilities.currentSession();
			List<OwlStatus> list = s.createSQLQuery(query).addEntity(OwlStatus.class).list();
			List<OwlStatus> statuslist = list;
			for(int i =0; i<list.size();i++)
			{
				OwlStatus status = list.get(i);
				if(status.getStatus().equals("deleted"))
				{
					statuslist.remove(i);
				}
			}
			return (ArrayList<OwlStatus>) statuslist;

		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<OwlStatus>();
		} finally {
			HibernateUtilities.closeSession();
		}
	}
	
	public ArrayList<LanguageCode> getLanguage()
	{
		String query = "SELECT * FROM language_code ORDER BY language_code";
		try
		{
			return (ArrayList<LanguageCode>)	HibernateUtilities.currentSession().createSQLQuery(query).addEntity(LanguageCode.class).list();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new ArrayList<LanguageCode>();
		}
		finally
		{
			HibernateUtilities.closeSession();
		}
	}
	
	public ArrayList<Users> getUsers()
	{
		String query = "SELECT * FROM users";
		try
		{
			return (ArrayList<Users>)	HibernateUtilities.currentSession().createSQLQuery(query).addEntity(Users.class).list();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new ArrayList<Users>();
		}
		finally
		{
			HibernateUtilities.closeSession();
		}
	}
	
	public ArrayList<RelationshipObject> getRelationships(OWLModel owlModel)
	{
		ArrayList<RelationshipObject> relationshipList = new ArrayList<RelationshipObject>();
		RelationshipServiceImpl rs = new RelationshipServiceImpl();
		RelationshipTreeObject relTreeObj = rs.getObjectPropertyTree(owlModel, ModelConstants.RHASRELATEDCONCEPT, true);
		HashMap<String,RelationshipObject> relList =  relTreeObj.getRelationshipList();
		for(Iterator<String> itr = relList.keySet().iterator(); itr.hasNext();)
		{
			RelationshipObject relationship = relList.get(itr.next());
			relationshipList.add(relationship);
		}
		return relationshipList;
	}
	
	public ArrayList<RecentChanges> getRecentChangesData(int ontologyId, int action_id)
	{
		String query = "select * from recent_changes where ontology_id = "+ontologyId;
		ArrayList<Integer> actions = new ArrayList<Integer>();
		     if(action_id == TERM_CREATE)  actions = TERM_CREATE_ACTION;
		else if(action_id == TERM_EDIT)    actions = TERM_EDIT_ACTION;
		else if(action_id == TERM_DELETE)  actions = TERM_DELETE_ACTION;
		else if(action_id == CONCEPT_CREATE)  actions = CONCEPT_CREATE_ACTION;
		else if(action_id == CONCEPT_EDIT)    actions = CONCEPT_EDIT_ACTION;
		else if(action_id == CONCEPT_DELETE)  actions = CONCEPT_DELETE_ACTION;
		     
		if(actions.size() > 0)
		{
		    String temp = "";
		    for(int i=0; i<actions.size(); i++)
		    {
		        temp += "modified_action='"+actions.get(i)+"'";
		        if(i<actions.size()-1) temp += " OR ";
		    }
		    temp = "(" + temp + ")";
			//query = "select * from recent_changes where ontology_id = "+ontologyId+" and modified_action='"+action+"'";
			query = "select * from recent_changes where ontology_id = "+ontologyId+" and "+temp;
		}
		try 
		{
			ArrayList<RecentChanges> list = (ArrayList<RecentChanges>) HibernateUtilities.currentSession().createSQLQuery(query).addEntity("rc",RecentChanges.class).list();
			return new ValidationServiceImpl().setRecentChanges(list);
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
}