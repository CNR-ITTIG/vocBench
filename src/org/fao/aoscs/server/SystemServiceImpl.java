package org.fao.aoscs.server;
   
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletException;

import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.gwt.GwtConfigurationHelper;
import net.sf.gilead.gwt.PersistentRemoteService;
import net.sf.gilead.pojo.gwt.LightEntity;

import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.client.module.constant.OWLActionConstants;
import org.fao.aoscs.client.module.constant.OWLStatusConstants;
import org.fao.aoscs.client.module.system.service.SystemService;
import org.fao.aoscs.domain.InitializeSystemData;
import org.fao.aoscs.domain.InitializeUsersPreferenceData;
import org.fao.aoscs.domain.LanguageCode;
import org.fao.aoscs.domain.LanguageInterface;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.OwlAction;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.PermissionFunctionalityMap;
import org.fao.aoscs.domain.PermissionGroupMap;
import org.fao.aoscs.domain.PermissionGroupMapId;
import org.fao.aoscs.domain.PermissionObject;
import org.fao.aoscs.domain.RecentChangeData;
import org.fao.aoscs.domain.UserLogin;
import org.fao.aoscs.domain.Users;
import org.fao.aoscs.domain.UsersGroups;
import org.fao.aoscs.hibernate.DatabaseUtil;
import org.fao.aoscs.hibernate.EncriptFunction;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.hibernate.QueryFactory;
import org.fao.aoscs.server.utility.Encrypt;
import org.fao.aoscs.utility.MailUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;


public class SystemServiceImpl extends PersistentRemoteService  implements SystemService {


	private static final long serialVersionUID = 6175143371091682520L;

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
	
	public InitializeSystemData initData(UserLogin userloginObject){
		InitializeSystemData data = new InitializeSystemData();
		data.setLanguage(loadLanguage());		
		data.setConfigConstants(loadConfigConstants());
		data.setModelConstants(loadModelConstants());
		data.setOwlStatusConstants(loadOWLStatsConstants());
		data.setOwlActionConstants(loadOWLActionConstants());
		data.setOntology(getOntology(userloginObject.getUserid()));
		data.setPermissionTable(getPermissions(userloginObject.getGroupid()));
		return data;
	}
	
	public  InitializeUsersPreferenceData initSelectPreferenceData(int userID){
		InitializeUsersPreferenceData initUsersPreference = new InitializeUsersPreferenceData();
		initUsersPreference.setUsersPreference(new UsersPreferenceServiceImpl().getUsersPreference(userID));
		initUsersPreference.setUsergroups(getUserGroup(userID));
		initUsersPreference.setOntology(getOntology(""+userID));
		initUsersPreference.setInterfaceLanguage(getInterfaceLang());
		return initUsersPreference;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<LanguageCode> loadLanguage()
	{
		String query = "SELECT * FROM language_code ORDER BY language_order";
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
	
	public HashMap<String, String> getHelpURL()
	{
		HashMap<String, String> mcMap = new HashMap<String, String>();
		ResourceBundle rb = ResourceBundle.getBundle("help");
		Enumeration<String> en = rb.getKeys();
		while(en.hasMoreElements())
		{
			String key = (String) en.nextElement();
			mcMap.put(key, rb.getString(key));
		}
		return mcMap;
	}
	
	public HashMap<String, String> loadOWLStatsConstants()
	{
		String owlStatusConstantsQuery ="SELECT id,status FROM owl_status ORDER BY id";
		ArrayList<String[]> tmpVal = QueryFactory.getHibernateSQLQuery(owlStatusConstantsQuery);
		HashMap<String, String> owlStatusConstants = new HashMap<String, String>();
		for(int i=0;i<tmpVal.size();i++){
			String[] item = (String[]) tmpVal.get(i);
			owlStatusConstants.put(item[0], item[1]);
		}
		OWLStatusConstants.loadOwlStatusConstants(owlStatusConstants);
		return owlStatusConstants;
	}
	
	public HashMap<String, String> loadConfigConstants()
	{
		HashMap<String, String> mcMap = new HashMap<String, String>();
		ResourceBundle rb = ResourceBundle.getBundle("Config");
		Enumeration<String> en = rb.getKeys();
		while(en.hasMoreElements())
		{
			String key = (String) en.nextElement();
			mcMap.put(key, rb.getString(key));
		}
		ConfigConstants.loadConstants(mcMap);
		return mcMap;
	}
	
	public HashMap<String, String> loadModelConstants()
	{
		HashMap<String, String> mcMap = new HashMap<String, String>();
		ResourceBundle rb = ResourceBundle.getBundle("org.fao.aoscs.server.ModelConstants");
		Enumeration<String> en = rb.getKeys();
		while(en.hasMoreElements())
		{
			String key = (String) en.nextElement();
			mcMap.put(key, rb.getString(key));
		}
		ModelConstants.loadConstants(mcMap);
		return mcMap;
	}
	
	public HashMap<String, Integer> loadOWLActionConstants()
	{
		String owlStatusConstantsQuery ="SELECT id,action,action_child FROM owl_action ORDER BY id";
		ArrayList<String[]> tmpVal = QueryFactory.getHibernateSQLQuery(owlStatusConstantsQuery);
		HashMap<String, Integer> owlActionConstants = new HashMap<String, Integer>();
		for(int i=0;i<tmpVal.size();i++){
			String[] item = (String[]) tmpVal.get(i);
			int id = Integer.parseInt(item[0]);
			
			String action = item[1].toUpperCase();
			action = action.replaceAll("-", "");
			
			String child = item[2].toUpperCase();
			child = child.replaceAll("-", "");
			
			String val = child.length()>0? action +"_"+child : action;
			owlActionConstants.put(val, id);
		}
		OWLActionConstants.loadConstants(owlActionConstants);
		return owlActionConstants;
	}
	
	public UserLogin getAuthorize(String loginuser,String loginpassword){
		
		UserLogin userLoginObj = null;	
	
		String query = "SELECT user_id,username,password " +
					 "FROM users " +
					 "WHERE username='"+QueryFactory.escapeSingleQuote(loginuser)+"' AND password='"+new EncriptFunction().encriptFunction(loginpassword)+"' AND status='1'";
	
		
		ArrayList<String[]> tmpVal = QueryFactory.getHibernateSQLQuery(query);
	
		for(int i=0;i<tmpVal.size();i++){
			String[] item = (String[]) tmpVal.get(i);
			userLoginObj = new UserLogin();	
			userLoginObj.setUserid(item[0]);
			userLoginObj.setLoginname(item[1]);
			userLoginObj.setPassword(item[2]);
			userLoginObj.setNoOfGroup(getNoOfGroups(Integer.parseInt(userLoginObj.getUserid())));
			userLoginObj.setUserSelectedLanguage(getUserSelectedLanguageCode(Integer.parseInt(userLoginObj.getUserid())));
		}
		return userLoginObj;
	} 

	public static UserLogin getUserLoginPermission(UserLogin userLoginObj) throws SQLException {

		ArrayList<String> permissionlist = new ArrayList<String>();
		
		String querystring = "SELECT DISTINCTROW pt.permission FROM permission_group_map pgm " +
				"INNER JOIN permissiontype pt ON pt.permission_id = pgm.permission_id " +
				"WHERE pgm.users_groups_id = '" + userLoginObj.getGroupid() + "' order by pt.permission_id";
		
		ArrayList<String[]> tmpVal = QueryFactory.getHibernateSQLQuery(querystring);
		
		for(int i=0;i<tmpVal.size();i++)
		{
			String[] item = (String[]) tmpVal.get(i);
			String pm = item[0];
		    permissionlist.add(pm);
		}
		userLoginObj.setMenu(permissionlist);
   	 
	    return userLoginObj;
	}
	
	public UserLogin getAuthorization(String name, UserLogin userLoginObj){ 
		
		if (userLoginObj.getLoginname()!="" && userLoginObj.getPassword()!=""){
		
			try 
			{
				 	userLoginObj = getUserLoginPermission(userLoginObj);
				 	setSessionValue(name, userLoginObj);
				} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}

		return userLoginObj;
	}
	
	public boolean setSessionValue(String name, LightEntity obj){ 
			boolean chk = false;
	
			try 
			{
				 	if (obj!=null){
						javax.servlet.http.HttpSession session = getThreadLocalRequest().getSession();
						session.setMaxInactiveInterval(216000);
						session.setAttribute(name, obj);
						chk =  true;
					} 
				} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			return chk;
	}

	public void clearSession(){
		javax.servlet.http.HttpSession session = getThreadLocalRequest().getSession();
		if(session!=null)
			session.invalidate();
	}
	
	public UserLogin checkSession(String name){
		
		try
		{
			javax.servlet.http.HttpSession session = getThreadLocalRequest().getSession();
			UserLogin userLoginObj = (UserLogin) session.getAttribute(name);
			return userLoginObj;	
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean isUserExist(String username){ // Waiting for implement with database method
		int cnt = 0;
		try {
			String query ="SELECT count(*) as cnt FROM users WHERE username = '"+QueryFactory.escapeSingleQuote(username)+"'";
			cnt = (Integer)HibernateUtilities.currentSession().createSQLQuery(query).addScalar("cnt", Hibernate.INTEGER).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtilities.closeSession();
		}
		return cnt>0;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Users> getAllUser(){ // Waiting for implement with database method 
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
	
	@SuppressWarnings("unchecked")
	public ArrayList<LanguageInterface> getInterfaceLang(){// Reuse from org.fao.aoscs.db 
		String query = "SELECT * FROM language_interface ORDER BY language_code";
		try
		{
			ArrayList<LanguageInterface> list = (ArrayList<LanguageInterface>)	HibernateUtilities.currentSession().createSQLQuery(query).addEntity(LanguageInterface.class).list();
			return list;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new ArrayList<LanguageInterface>();
		}
		finally
		{
			HibernateUtilities.closeSession();
		}
		
	}
	
	public ArrayList<UsersGroups> getUserGroup(int userid){ // Reuse from org.fao.aoscs.db 

		if (userid!=0)
		{
			String query =  "SELECT users_groups.users_groups_id, users_groups.users_groups_name, users_groups.users_groups_desc " +
							" FROM users_groups_map INNER JOIN users_groups ON users_groups_map.users_group_id = users_groups.users_groups_id " +
							" WHERE (users_groups_map.users_id)='"+ userid+ "'" +
							" ORDER BY users_groups.users_groups_id;";
	
			ArrayList<String[]> list = QueryFactory.getHibernateSQLQuery(query);
			ArrayList<UsersGroups> userGroupsList = new ArrayList<UsersGroups>();
			for(int i=0;i<list.size();i++){
	    		String[] item = (String[]) list.get(i);
	    		UsersGroups userGroups = new UsersGroups();
	    		userGroups.setUsersGroupsId(Integer.parseInt(item[0]));
	    		userGroups.setUsersGroupsName(item[1]);
	    		userGroups.setUsersGroupsDesc(item[2]);
	    		userGroupsList.add(userGroups);
	    	}					
			return userGroupsList;
		} 
		else{
			 return null;
		 }
	}
	
	public String getEncriptText(String password){
		
		
		EncriptFunction encrpt = new EncriptFunction();
		
		String md5 = encrpt.encriptFunction(password); 
		
		return md5;
	}

	public void SendMail(String to, String subject,String body){
		MailUtil.sendMail(to, subject, body);
	}

	public HashMap<String, ArrayList<String[]>> getGroupStatusAssignment(){
		HashMap<String, ArrayList<String[]>> map = new HashMap<String, ArrayList<String[]>>();
		
		String allGroup = "select users_groups_id ,users_groups_name FROM users_groups";
		ArrayList<String[]> groupList = QueryFactory.getHibernateSQLQuery(allGroup);
		
		String allStatus = "SELECT id ,status FROM owl_status ";
		ArrayList<String[]> statusList = QueryFactory.getHibernateSQLQuery(allStatus);
		
		String allAction = "SELECT id ,action,action_child FROM owl_action ";
		ArrayList<String[]> actionList = QueryFactory.getHibernateSQLQuery(allAction);
		
		String statusAction = "SELECT group_id , action_id , status_id FROM status_action_map ";
		ArrayList<String[]> statusActionList = QueryFactory.getHibernateSQLQuery(statusAction);
		
		map.put("groupList",groupList);
		map.put("statusList",statusList);
		map.put("actionList",actionList);
		map.put("statusActionList",statusActionList);
		return map;
	}
	
	public HashMap<String, ArrayList<String[]>> getGroupValidateStatusAssignment(){
		HashMap<String, ArrayList<String[]>> map = new HashMap<String, ArrayList<String[]>>();
		
		String allGroup = "select users_groups_id ,users_groups_name FROM users_groups";
		ArrayList<String[]> groupList = QueryFactory.getHibernateSQLQuery(allGroup);
		
		String allStatus = "SELECT id ,status FROM owl_status ";
		ArrayList<String[]> statusList = QueryFactory.getHibernateSQLQuery(allStatus);
		
		String statusAction = "SELECT users_groups_id , action , status, newstatus FROM validation_permission ";
		ArrayList<String[]> statusActionList = QueryFactory.getHibernateSQLQuery(statusAction);
		
		map.put("groupList",groupList);
		map.put("statusList",statusList);
		map.put("statusActionList",statusActionList);
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<OntologyInfo> getOntology(String userid)
	{
		try {
			String query = "";
			// if VISITOR then load read only ontology
			if(ConfigConstants.ISVISITOR){
				query = "version ='"+ ConfigConstants.VERSION +"' AND ontology_show='2'";
			}
			else{
				query = "version ='"+ ConfigConstants.VERSION +"' AND ontology_show='1'";
			}
			
			query += "AND ontology_id IN ( SELECT ontology_id FROM users_ontology WHERE user_id =  '"+userid +"' and status = 1)"; 
			
			String sqlStr = "SELECT * FROM ontology_info WHERE "+ query +" order by ontology_name";
			
			Session s = HibernateUtilities.currentSession();
			ArrayList<OntologyInfo> list = (ArrayList<OntologyInfo>) s.createSQLQuery(sqlStr).addEntity(OntologyInfo.class).list();
			return list;

		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<OntologyInfo>();
		} finally {
			HibernateUtilities.closeSession();
		}

	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<OntologyInfo> getOntology(int ontology_id)
	{
		try {
			String query = "";
			// if VISITOR then load read only ontology
			if(ConfigConstants.ISVISITOR){
				query = "version ='"+ ConfigConstants.VERSION +"' AND ontology_show='2'";
			}
			else{
				query = "version ='"+ ConfigConstants.VERSION +"' AND ontology_show='1'";
			}
			
			String sqlStr = "SELECT * FROM ontology_info WHERE ontology_id =  '"+ontology_id +"' AND "+ query;
			
			Session s = HibernateUtilities.currentSession();
			ArrayList<OntologyInfo> list = (ArrayList<OntologyInfo>) s.createSQLQuery(sqlStr).addEntity(OntologyInfo.class).list();
			return list;

		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<OntologyInfo>();
		} finally {
			HibernateUtilities.closeSession();
		}

	}
	
	public int getNoOfGroups(int user_id){ 
		String query = "select users_group_id FROM users_groups_map WHERE users_id='"+ user_id +"'" ;
		return QueryFactory.getHibernateSQLQuery(query).size();	
	}
	
	public ArrayList<String> getUserSelectedLanguageCode(int user_id){ 
		
		ArrayList<String> userSelectedLanguage = new ArrayList<String>();
		
		String sql = "SELECT language_code FROM users_language WHERE status='1' AND user_id='"+user_id+"'";
		ArrayList<String[]> resultlist = QueryFactory.getHibernateSQLQuery(sql);
		for(int z=0;z<resultlist.size();z++)
		{
			String str = ((String[])resultlist.get(z))[0].toLowerCase();		
			if(!userSelectedLanguage.contains(str))
				userSelectedLanguage.add(str);
		}
		return userSelectedLanguage;
	}
	
	public ArrayList<String[]> getCountryCodes(){ 
		
		ArrayList<String[]> countryList = new ArrayList<String[]>();
		
		String sql = "SELECT country_name,country_code FROM country_code ORDER BY country_name";
		ArrayList<String[]> resultlist = QueryFactory.getHibernateSQLQuery(sql);
		for(int z=0;z<resultlist.size();z++)
		{
			String[] val = ((String[])resultlist.get(z));
			countryList.add(val);
		}
		return countryList;
	}

	public ArrayList<LanguageCode> addLanguage(LanguageCode languageCode)
	{
		try
		{
			DatabaseUtil.createObject(languageCode);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return loadLanguage();
	}
	
	public ArrayList<LanguageCode> editLanguage(LanguageCode languageCode){
		try
		{
			DatabaseUtil.update(languageCode, true);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return loadLanguage();
	}
	
	public ArrayList<LanguageCode> deleteLanguage(LanguageCode languageCode){
		try
		{
			DatabaseUtil.delete(languageCode);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return loadLanguage();
	}
	
	public ArrayList<LanguageCode> updateLanguages(ArrayList<LanguageCode> languageCodes){
		try
		{
			for(LanguageCode languageCode: languageCodes)
			{
				DatabaseUtil.update(languageCode, true);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return loadLanguage();
	}
	
	public int registerUser(Users user, ArrayList<String> userGroups, ArrayList<String> userLangs, ArrayList<String> usersOntology)
	{			
		
		int id = 0;
		try
    	{	
			RecentChangeData rcData = new RecentChangeData();
			
			ArrayList<LightEntity> obj = new ArrayList<LightEntity>();
			obj.add(user);			
			
			rcData.setObject(obj);			
			rcData.setActionId(43);
			rcData.setModifierId(6);
			
			DatabaseUtil.addRecentChange(rcData, 0);
			try{
				
				ArrayList<String[]> list = QueryFactory.getHibernateSQLQuery("SELECT * from users where username = '" + QueryFactory.escapeSingleQuote(user.getUsername())+ "'");
				
				if(list.size() > 0)
					return 0;
				user.setPassword(Encrypt.MD5(user.getPassword()));
				DatabaseUtil.createObject(user);
				list = QueryFactory.getHibernateSQLQuery("SELECT user_id from users where username = '" + QueryFactory.escapeSingleQuote(user.getUsername()) + "'");
				id = Integer.parseInt(((String[]) list.get(0))[0]);
				
			}catch(Exception e){	
				e.printStackTrace();				
				return -1;
			}			
			if(userGroups.size() > 0){
				ArrayList<String> sqls = new ArrayList<String>();											
				for(int i=0 ; i < userGroups.size() ; i++)
				{
					sqls.add("INSERT INTO users_groups_map(users_id,users_group_id) VALUES (" + id + "," + userGroups.get(i) + ")"); 					
				}
				
				QueryFactory.hibernateExecuteSQLUpdate(sqls);
			}
			if(userLangs.size() > 0){
				ArrayList<String> sqls = new ArrayList<String>();											
				for(int i=0 ; i < userLangs.size() ; i++)
				{
					sqls.add("INSERT INTO users_language(user_id,language_code,status) VALUES(" + id + ",'" + userLangs.get(i) + "', 1)");					 					
				}
				QueryFactory.hibernateExecuteSQLUpdate(sqls);
			}
			if(usersOntology.size() > 0){
				ArrayList<String> sqls = new ArrayList<String>();											
				for(int i=0 ; i < usersOntology.size() ; i++)
				{
					sqls.add("INSERT INTO users_ontology(user_id,ontology_id,status) VALUES(" + id + ",'" + usersOntology.get(i) + "', 1)");					 					
				}
				QueryFactory.hibernateExecuteSQLUpdate(sqls);
			}
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();    		
    		return -1;
    	}
    	    		
    	return 1;
    			
	}
	
	public int addUser(Users user)
	{			
		
		try
    	{	
			RecentChangeData rcData = new RecentChangeData();
			
			ArrayList<LightEntity> obj = new ArrayList<LightEntity>();
			obj.add(user);			
			
			rcData.setObject(obj);			
			rcData.setActionId(43);
			rcData.setModifierId(6);
			
			DatabaseUtil.addRecentChange(rcData, 0);
			try{
				
				ArrayList<String[]> list = QueryFactory.getHibernateSQLQuery("SELECT * from users where username = '" + QueryFactory.escapeSingleQuote(user.getUsername())+ "'");
				
				if(list.size() > 0)
					return 0;
				user.setPassword(Encrypt.MD5(user.getPassword()));
				DatabaseUtil.createObject(user);
				list = QueryFactory.getHibernateSQLQuery("SELECT user_id from users where username = '" + QueryFactory.escapeSingleQuote(user.getUsername())+ "'");
				
			}catch(Exception e){	
				e.printStackTrace();				
				return -1;
			}			
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();    		
    		return -1;
    	}
    	    		
    	return 1;
    			
	}
	
	public void updateUserData(Users user){
		
		String sql = 	"UPDATE users SET " +
						"username='"+QueryFactory.escapeSingleQuote(user.getUsername())+"',"+
						"first_name='"+QueryFactory.escapeSingleQuote(user.getFirstName())+"'," +
						"last_name='"+QueryFactory.escapeSingleQuote(user.getLastName())+"'," +
						"email='"+QueryFactory.escapeSingleQuote(user.getEmail())+"'," +
						"affiliation='"+QueryFactory.escapeSingleQuote(user.getAffiliation())+"'," +
						"contact_address='"+QueryFactory.escapeSingleQuote(user.getContactAddress())+"',"+
						"postal_code='"+QueryFactory.escapeSingleQuote(user.getPostalCode())+"'," +
						"user_url='"+QueryFactory.escapeSingleQuote(user.getUserUrl()) +"',"+
						"country_code='"+user.getCountryCode()+"'," +
						"work_phone='"+user.getWorkPhone()+"',mobile_phone='"+user.getMobilePhone()+"'," +
						"chat_address='"+QueryFactory.escapeSingleQuote(user.getChatAddress())+"'," +
						"comment='"+QueryFactory.escapeSingleQuote(user.getComment())+"'," +
						"status = '"+user.getStatus()+"'";

		if(!user.getPassword().equals("")){ 
			sql = sql+",password = md5(\""+QueryFactory.escapeSingleQuote(user.getPassword())+"\")";
		}
		sql =sql + " WHERE user_id = '"+user.getUserId()+"'";
		
		QueryFactory.hibernateExecuteSQLUpdate(sql);
	}
	
	public void createGroup(String name , String description, int userId)
	{
		UsersGroups g = new UsersGroups();
		g.setUsersGroupsName(name);
		g.setUsersGroupsDesc(description);
		try
    	{	
			RecentChangeData rcData = new RecentChangeData();
			
			ArrayList<LightEntity> obj = new ArrayList<LightEntity>();
			obj.add(g);			
			
			rcData.setObject(obj);			
			rcData.setActionId(65); // group-create
			rcData.setModifierId(userId); 
			
			DatabaseUtil.addRecentChange(rcData, 0);
			DatabaseUtil.createObject(g);
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}				
	}
	
	public void editGroup(int id, String name , String description, String oldDescription, int userId)
	{
		UsersGroups g = new UsersGroups();
		g.setUsersGroupsId(id);
		g.setUsersGroupsName(name);
		g.setUsersGroupsDesc(description);
		
		UsersGroups oldg = new UsersGroups();
		oldg.setUsersGroupsName(name);
		oldg.setUsersGroupsDesc(oldDescription);
		try
		{	
			RecentChangeData rcData = new RecentChangeData();
			
			ArrayList<LightEntity> obj = new ArrayList<LightEntity>();
			obj.add(g);
			
			ArrayList<LightEntity> oldObj = new ArrayList<LightEntity>();
			oldObj.add(oldg);			
			
			rcData.setObject(obj);			
			rcData.setOldObject(oldObj);
			rcData.setActionId(66); // group-edit
			rcData.setModifierId(userId); 
			
			DatabaseUtil.addRecentChange(rcData, 0);
			DatabaseUtil.update(g, true);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}				
	}
	
	public void deleteGroup(int groupId, String name, String description, int userId)
	{
		UsersGroups g = new UsersGroups();
		g.setUsersGroupsId(groupId);
		g.setUsersGroupsName(name);
		g.setUsersGroupsDesc(description);
		try
		{	
			String sql = "DELETE FROM users_groups_map WHERE users_group_id='"+groupId+"'";					
			QueryFactory.hibernateExecuteSQLUpdate(sql);
			
			sql = "DELETE FROM permission_group_map WHERE users_groups_id='"+groupId+"'";					
			QueryFactory.hibernateExecuteSQLUpdate(sql);
   				   										
			ArrayList<LightEntity> obj = new ArrayList<LightEntity>();
			obj.add(g);			
			
			RecentChangeData rcData = new RecentChangeData();
			rcData.setObject(obj);			
			rcData.setActionId(67); // group-delete
			rcData.setModifierId(userId); 
			
			DatabaseUtil.addRecentChange(rcData, 0);
			DatabaseUtil.delete(g);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}				
	}

	public void addGroupPermission(int groupId, int permitId, String groupName, String permitName, int userId) 
	{
		
		
		PermissionGroupMapId pgid = new PermissionGroupMapId();
		pgid.setUsersGroupsId(groupId);
		pgid.setPermissionId(permitId);
		pgid.setGroupName(groupName);
		pgid.setPermitName(permitName);
		
		PermissionGroupMap pg = new PermissionGroupMap();
		pg.setId(pgid);
		
		
		
		try
		{	
			DatabaseUtil.createObject(pg);
			
			RecentChangeData rcData = new RecentChangeData();
			
			ArrayList<LightEntity> obj = new ArrayList<LightEntity>();
			obj.add(pgid);			
			
			rcData.setObject(obj);			
			rcData.setActionId(68); // group-permission-add
			rcData.setModifierId(userId); 
			
			DatabaseUtil.addRecentChange(rcData , 0);
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void removeGroupPermission(int groupId, int permitId, String groupName, String permitName, int userId) 
	{
		
		PermissionGroupMapId pgid = new PermissionGroupMapId();
		pgid.setUsersGroupsId(groupId);
		pgid.setPermissionId(permitId);
		pgid.setGroupName(groupName);
		pgid.setPermitName(permitName);
		
		PermissionGroupMap pg = new PermissionGroupMap();
		pg.setId(pgid);
		

		try
		{	
			DatabaseUtil.delete(pg);
			
			RecentChangeData rcData = new RecentChangeData();
			
			ArrayList<LightEntity> obj = new ArrayList<LightEntity>();
			obj.add(pgid);			
			
			rcData.setObject(obj);			
			rcData.setActionId(69); // group-permission-delete
			rcData.setModifierId(userId); 
			
			
			DatabaseUtil.addRecentChange(rcData , 0);
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void addGroupsToUser(String userId, ArrayList<String> groupIds){
		String query = "";
		
		for(String groupId : groupIds){
			if(query.length() > 0){
				query += ", ";
			}
			query +=  "("+userId+","+groupId+")";
		}
		query = "INSERT INTO users_groups_map(users_id,users_group_id) VALUES " + query + ";";
		QueryFactory.hibernateExecuteSQLUpdate(query);
	}
	
	public void addLanguagesToUser(String userId, ArrayList<String> languages){
		String query = "";
		
		for(String language : languages){
			if(query.length() > 0){
				query += ", ";
			}
			query +=  "("+userId+",'"+language+"',1)";
		}
		query = "INSERT INTO users_language(user_id,language_code,status) VALUES " + query + ";";
		QueryFactory.hibernateExecuteSQLUpdate(query);
	}
	
	public void addOntologiesToUser(String userId, ArrayList<String> ontologyIds){
		
		for(String ontologyId : ontologyIds){
			String sql = "SELECT count(user_id) from users_ontology WHERE user_id= "+userId+" AND ontology_id="+ontologyId;
			ArrayList<String[]> ret = QueryFactory.getHibernateSQLQuery(sql);
			
			if(ret.size() > 0){
				String[] countArray = (String[])(ret.get(0));
				if(countArray.length > 0){
					try{
						int count = Integer.parseInt(countArray[0]);
						if(count > 0){
							sql = "UPDATE users_ontology SET status = 1 WHERE user_id="+userId+" AND ontology_id="+ontologyId;
							
						}else{
							sql = "INSERT INTO users_ontology VALUES ("+userId+","+ontologyId+",1)";
						}
						QueryFactory.hibernateExecuteSQLUpdate(sql);
							
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}			
		}
	}
	
	public void addUserToGroup(int groupId, int userId, String groupName, String userName, int modifierId)
	{
		String sql = "INSERT INTO users_groups_map(users_id,users_group_id) VALUES('"+userId+"','"+ groupId +"')";
		
		UsersGroups ug = new UsersGroups();
		ug.setUsersGroupsId(groupId);
		ug.setUsersGroupsName(groupName);
		
		
		Users u = new Users();
		u.setUsername(userName);
		try
		{
			QueryFactory.hibernateExecuteSQLUpdate(sql);
			
			RecentChangeData rcData = new RecentChangeData();
			
			ArrayList<LightEntity> obj = new ArrayList<LightEntity>();
			obj.add(ug);
			
			ArrayList<LightEntity> newUser = new ArrayList<LightEntity>();
			newUser.add(u);			
			
			rcData.setObject(obj);			
			rcData.setNewObject(newUser);			
			rcData.setActionId(70); // group-member-add
			rcData.setModifierId(modifierId); 
			
			DatabaseUtil.addRecentChange(rcData , 0);
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void removeUserFromGroup(int groupId, ArrayList<Integer> selectedUsers, String groupName, String userName, int modifierId)
	{
		
		String where = "";
		
		for(int i=0; i< selectedUsers.size(); i++){
			if(i>0){
				where += " OR ";
			}
			where += "users_id = " + selectedUsers.get(i);
		}
		
		String sql = "DELETE FROM users_groups_map WHERE (" + where +  ") AND users_group_id='"+groupId+"'";
		
		UsersGroups ug = new UsersGroups();
		ug.setUsersGroupsId(groupId);
		ug.setUsersGroupsName(groupName);
		
		
		Users u = new Users();
		u.setUsername(userName);
		try
		{
			QueryFactory.hibernateExecuteSQLUpdate(sql);
			
			RecentChangeData rcData = new RecentChangeData();
			
			ArrayList<LightEntity> obj = new ArrayList<LightEntity>();
			obj.add(ug);
			
			ArrayList<LightEntity> objUser = new ArrayList<LightEntity>();
			objUser.add(u);			
			
			rcData.setObject(obj);			
			rcData.setOldObject(objUser);			
			rcData.setActionId(71); // group-member-delete
			rcData.setModifierId(modifierId); 
			
			DatabaseUtil.addRecentChange(rcData , 0);
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void addActionToGroup(ArrayList<PermissionFunctionalityMap> map)
	{
		String sql = "INSERT INTO permission_functionality_map VALUES ";
		try
		{
			Iterator<?> itr = map.iterator();
			int i=0;
			while(itr.hasNext()){
				PermissionFunctionalityMap item = (PermissionFunctionalityMap)itr.next();
				sql += i==0? "" : " , ";
				sql += "('"+item.getFunctionId()+"', '"+item.getGroupId()+"', '"+item.getStatus()+"') ";
				i++;
			}
			sql += ";";
			QueryFactory.hibernateExecuteSQLUpdate(sql);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void removeActionsFromGroup(int groupId,  ArrayList<Integer> selectedActions)
	{
		String where = "";
		
		for(int i=0; i< selectedActions.size(); i++){
			if(i>0){
				where += " OR ";
			}
			where += "function_id = " + selectedActions.get(i);
		}
		String sql = "DELETE FROM permission_functionality_map WHERE (" + where + ") AND group_id ='"+groupId+"'";
		try
		{
			QueryFactory.hibernateExecuteSQLUpdate(sql);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void removeActionFromGroup(String groupId, String actionId, String statusId)
	{
		
		String sql = "DELETE FROM permission_functionality_map WHERE group_id ="+groupId+" AND function_id = "+ actionId + " AND status = " + statusId;
		try
		{
			QueryFactory.hibernateExecuteSQLUpdate(sql);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private PermissionObject getPermissions(String groupId)
	{
		PermissionObject permissions = new PermissionObject();
		String query = "SELECT function_id,group_id,status FROM permission_functionality_map WHERE group_id = '" + groupId + "'";
		try
		{
			ArrayList<String[]> resultlist = QueryFactory.getHibernateSQLQuery(query);
			for(int z=0;z<resultlist.size();z++)
			{
				int fId = Integer.parseInt(((String[])resultlist.get(z))[0]);
				int gId = Integer.parseInt(((String[])resultlist.get(z))[1]);
				int sId = Integer.parseInt(((String[])resultlist.get(z))[2]);
				PermissionFunctionalityMap map = new PermissionFunctionalityMap();
				map.setFunctionId(fId);
				map.setGroupId(gId);
				map.setStatus(sId);
				permissions.add(map);
			}
			return permissions;
		}
		catch(Exception e){
			e.printStackTrace();
			return new PermissionObject();
		}
		finally
		{
			HibernateUtilities.closeSession();
		}
	}
	
	/**
	 * Get actions not assigned to user group. 
	 * @param groupId	
	 * @return List of action as IDs
	 */
	public ArrayList<OwlAction> getUnassignedActions(String groupId)
	{
		ArrayList<OwlAction> owlActions = new ArrayList<OwlAction>();
		try{
			// get total status
			String query = "SELECT COUNT(id) FROM owl_status where id != 99";
			ArrayList<String[]> resultlist = QueryFactory.getHibernateSQLQuery(query);
			int statusCount = Integer.parseInt(resultlist.get(0)[0]);
			
			// get actions which have not been assigned to all status
			query = "SELECT owl_action.id, owl_action.action, owl_action.action_child FROM " + 
					"owl_action RIGHT JOIN " + 
					"(" +
						"SELECT tb1.function_id as id FROM " +
						"(" +
							"SELECT count(function_id) as c,function_id from permission_functionality_map where group_id = "+groupId+" group by function_id" +
						")tb1 " + 
						"WHERE tb1.c < " +statusCount+
					")tb2 " + 
					"ON owl_action.id = tb2.id";
			
			resultlist = QueryFactory.getHibernateSQLQuery(query);
			for(int z=0;z<resultlist.size();z++)
			{
				String[] item = (String[]) resultlist.get(z);
				OwlAction oa = new OwlAction();
				oa.setId(Integer.parseInt(item[0]));
				oa.setAction(item[1].toUpperCase());
				oa.setActionChild(item[2].toUpperCase());
				if(!owlActions.contains(oa))
					owlActions.add(oa);
			}
			
			// get actions not assigned to any status			
			query = "SELECT tb2.id, tb2.action, tb2.action_child FROM " +
					"(" +
						"SELECT * FROM owl_action LEFT JOIN " +
						"(" +
							"SELECT function_id from permission_functionality_map where group_id = "+ groupId +" GROUP By function_id" +
						")tb1 " +
						"ON tb1.function_id = owl_action.id" +
					")tb2 WHERE tb2.function_id IS NULL";
			
			resultlist = QueryFactory.getHibernateSQLQuery(query);
			for(int z=0;z<resultlist.size();z++)
			{
				String[] item = (String[]) resultlist.get(z);
				OwlAction oa = new OwlAction();
				oa.setId(Integer.parseInt(item[0]));
				oa.setAction(item[1].toUpperCase());
				oa.setActionChild(item[2].toUpperCase());
				if(!owlActions.contains(oa))
					owlActions.add(oa);
			}
			return owlActions;
		}
		catch(Exception e){
			e.printStackTrace();
			return new ArrayList<OwlAction>();
		}
		finally{
			HibernateUtilities.closeSession();
		}
	}

	/**
	 * Get list of status not assigned to a action in a user group. 
	 * @param groupId	
	 * @param actionId
	 * @return
	 */
	public ArrayList<OwlStatus> getUnassignedActionStatus(String groupId, String actionId){
		ArrayList<OwlStatus> statusList = new ArrayList<OwlStatus>();
		
		String query = 	"SELECT tb2.id, tb2.status FROM " +
						"( " +
							"SELECT owl_status.id, owl_status.status, tb1.function_id FROM owl_status LEFT JOIN " +
							"( " +
								"SELECT * from permission_functionality_map where group_id = "+groupId+" AND function_id = "+ actionId + 
							") tb1 " +
							"ON tb1.status = owl_status.id WHERE owl_status.id != 99 " +
						")tb2 WHERE tb2.function_id IS NULL";
		try{
			
			ArrayList<String[]> resultlist = QueryFactory.getHibernateSQLQuery(query);
			for(int z=0;z<resultlist.size();z++){
				OwlStatus os = new OwlStatus();
				String[] item = (String[]) resultlist.get(z);
				os.setId(Integer.parseInt(item[0]));
				os.setStatus(item[1]);
				statusList.add(os);
			}
			return statusList;
		}
		catch(Exception e){
			e.printStackTrace();
			return new ArrayList<OwlStatus>();
		}
		finally{
			HibernateUtilities.closeSession();
		}
	}

}

