package org.fao.aoscs.server.export;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.fao.aoscs.client.module.constant.OWLStatusConstants;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.RelationObject;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.hibernate.QueryFactory;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;

public class ExportToSQL {
	
	public static String getExportSQL(OntologyInfo ontoInfo,ArrayList<String> criterialist){
		String ExpStr = "";
		
	//	OntologyInfo ontoInfo = new OntologyInfo();
		AgrovocData agDat = new AgrovocData(ontoInfo);
		
		AgrovocObject agObj = new AgrovocObject() ;
		agObj = agDat.getAgrovocObject();
		ExpStr = SetupHeader();
		ExpStr  = ExpStr +GenerateCreateTableScript()+"\n\n";
		ExpStr = ExpStr + expLanguageScript()+"\n\n";
		ExpStr = ExpStr + expMaintenancegroupsScript()+"\n\n";
		ExpStr = ExpStr + expTermStatusScript()+"\n\n";
				
		ExpStr = ExpStr +expTermScript(agObj,criterialist)+"\n\n"; //xxxxxxxxxx
		ExpStr = ExpStr + setupFinalString();
		return ExpStr;
		
	}

	public static String expTermScript(AgrovocObject agObj,ArrayList<String> criterialist)
	{	
		String catscmfield = "INSERT INTO `catschemes` (`schemeid`,`maintenancegroupid`,`scheme`,`languagecode`,`active`) VALUES ";
		String catfield = "INSERT INTO `categories` (`schemeid`,`categoryid`,`languagecode`,`categoryname`,`parentcategoryid`) VALUES ";
		String lnkfield = "INSERT INTO `linktype` (`linktypeid`,`languagecode`,`linkdesc`,`linkabr`,`linkdescription`,`createdate`," +
		"`rlinkcode`,`parentlinktypeid`,`linklevel`) VALUES "; 
		
		int noofcatscm = 0;  	int nooftermlnk = 0; 
		int nooftermcode = 0; 	int nooflnktype = 0;
		//static String ExpStr ;
		String termfield = "INSERT INTO `agrovocterm` (`termcode`,`languagecode`,`termspell`,`statusid`,`createdate`,`frequencyiad`,`frequencycad`,`lastupdate`,`scopeid`,`idowner`,`termsense`,`termoffset`) VALUES"; 

		String mappfield = "INSERT INTO `mapping` (`catcode`,`termcode`,`schemeid`,`parentcategoryid`) VALUES";		
		String scopefield = "INSERT INTO `scope` (`scopeid`,`scopedesc`,`languagecode`,`scopegrpid`) VALUES ";
		String tagtypefield = "INSERT INTO `tagtype` (`tagtypeid`,`tagdesc`,`languagecode`) VALUES ";
		
		String termlnkfield = "INSERT INTO `termlink` (`termcode1`,`termcode2`,`languagecode1`,`languagecode2`," +
		"`linktypeid`,`createdate`,`maintenancegroupid`,`newlinktypeid`,`confirm`,`technique`,`update`," + 
		"`updmaintenancegroupid`) VALUES ";
		String termtagfield = "INSERT INTO `termtag` (`termcode`,`languagecode`,`tagtypeid`,`tagtext`,`createdate`,`lastupdate`) VALUES ";

		
		String termScript = "";
		String termlnkScript = "";
		
		String catscmScript = "";
		String catScript  = "";
		String lnkScript  = "";
		
		String returnstr = "";
		
		String mappScript = mappfield;		
		String scopeScript =  scopefield;
		String tagtypeScript = tagtypefield;
//		termlnkScript = termlnkfield;
		String termtagScript = termtagfield;


//		 criteria from export gui

		String datetype = (String) criterialist.get(1);
		String datestart = (String) criterialist.get(2);
		String dateend = (String) criterialist.get(3);
	
		String schemevar = (String) criterialist.get(4);
		//String subvocab  = (String) criterialist.get(5); 
		String termcode= (String) criterialist.get(6);
		String concepturi = (String) criterialist.get(0);
		
		ArrayList<String> explang = new ArrayList<String>();
		
		if(criterialist.get(8) != null)
		{
			for(int i=8;i<criterialist.size();i++)
			{
				explang.add((String) criterialist.get(i)); 
			}			
		}
		
		HashMap<String, EConceptObject> conceptList = agObj.getConceptList();
		Iterator<String> it = conceptList.keySet().iterator();
		int i =0;
//---- end criteria from export gui 
	
		while(it.hasNext()){
			i=i+1;
			
			String conceptURI = (String)it.next();
			if(concepturi == null) concepturi = "";
			if(conceptURI.equals(concepturi) || concepturi==""){				
				EConceptObject cObj =  agObj.getConceptObject(conceptURI);
	//**********  update create start/end date 
				if(cObj != null) {
	
				// date criteria
					String cupdatedate = cObj.getUpdateDate();
					String ccreatedate  = cObj.getCreateDate();
					//--- check criteria		
					String datecheck ="" ;
					if(datetype.equals("update")){		datecheck = cupdatedate; 	}
					if(datetype.equals("create")){		datecheck = ccreatedate; 	}
					
					try {
						if(comparedate(datecheck,datestart,dateend)){
//------------ catscheme
//	expSchemeScript(EConceptObject cObj,String schemevar,String lcatscmScript,String catscmfield,Integer noofcatscm,String lcatScript,String catfield)
							ArrayList<String> lstscheme = (ArrayList<String>) cObj.getScheme();
							
							if(lstscheme != null){
//------------------ catschemes ---------
								if(catscmScript == null) catscmScript = "";
								for(int k=0;k<lstscheme.size();k++){
									String schemeuri = (String) lstscheme.get(k);
									String schemedesc = "" ; 
									if(schemevar=="") schemevar=null;
									if(schemeuri.equals(schemevar) || schemevar==null){
										if(catscmScript=="") {
											catscmScript =  "--\n" +
															"-- Dumping data for table `catschemes`\n" +
															"-- \n"+catscmfield+"\n"; 
														//	"/*!40000 ALTER TABLE `catschemes` DISABLE KEYS */;" +					
										}
										if((noofcatscm%600)==0)
										{ 
											if(noofcatscm !=0) catscmScript = catscmScript + ";\n" + catscmfield+"\n";		
										}
										if((noofcatscm%600)!= 0) catscmScript = catscmScript + ",\n";
//										INSERT INTO `catschemes` (`schemeid`,`maintenancegroupid`,`scheme`,`languagecode`,`active`) 
										catscmScript = catscmScript+"('"+schemeuri+"','','"+schemedesc+"','EN','y')";
										//--------------- end catschemes -----	
										//--------------- category  ----------
										String categoryname = ""; 
										if(catScript==null) catScript="";
										if(catScript=="") 
										{
											catScript = "-- \n" +
														"-- Dumping data for table `categories`\n" +
														"-- \n"+catfield + "\n";
													//	"/*!40000 ALTER TABLE `categories` DISABLE KEYS */;" +				
										}
										if((noofcatscm%600)==0)
										{ 
											if(noofcatscm !=0) 
												catScript = catScript + ";\n"+catfield+"\n";		
										}
										if((noofcatscm%600)!=0){ catScript = catScript + ",\n"; }
										ConceptObject ecatCobj = (ConceptObject) cObj.getParentConceptObject();
										String parentCObj  = ecatCobj.getUri();
										
										catScript = catScript + " \t('"+schemeuri+"','"+cObj.getConceptURI()+"','EN','"+categoryname+"','"+parentCObj+"')";
										noofcatscm = noofcatscm +1;
									} //if(schemeuri.equals(schemevar)){
								} // for			
							} // if									
			//------------ end cat scheme
//----------- export Relation -------------
							RelationObject rObj = cObj.getConcetpRelation();
							HashMap<RelationshipObject, ArrayList<ConceptObject>> rlst = rObj.getResult();						
							
							if(rlst!=null){
								Iterator<RelationshipObject> itrelation = rlst.keySet().iterator();
								while(itrelation.hasNext()){
									ArrayList<ConceptObject> conlst = (ArrayList<ConceptObject>) rlst.get(itrelation.next());
									for(int k=0;k<conlst.size();k++){					
										ConceptObject rcObj = (ConceptObject) conlst.get(k);
										
										String lnktypeid = rcObj.getConceptInstance() ; 						
										String langcode = "";
										String linkdesc = rcObj.getDisplayedConceptLabel();
										String linkabr = rcObj.getUri() ; 
										String linkdescript = linkdesc;
										
										java.util.Date createdate = rcObj.getDateCreate();
										String rlinkcode = ""; 
										String parentlinktypeid = rcObj.getParentInstance(); //"";
										String linklevel = "";
										if(lnktypeid == null) lnktypeid = "";
										if(lnktypeid != ""){
											if(lnkScript==null) lnkScript = "";
											if(lnkScript=="") {
												lnkScript = "--\n" +
															"-- Dumping data for table `linktype`\n" +
															"-- \n"+lnkfield + "\n"; 
														//	"/*!40000 ALTER TABLE `linktype` DISABLE KEYS */;" +				
											}
											if(nooflnktype!=0)
											{
												if(nooflnktype%600==0)	lnkScript = lnkScript + ";\n"+lnkfield +"\n";
											}	 			
											
											if(nooflnktype%600!=0) lnkScript = lnkScript + ",\n";
											lnkScript = lnkScript +
											 "\t('"+lnktypeid+"','"+langcode +"','"+linkdesc+"','"+linkabr+"'," +
											 "'"+linkdescript+"','"+createdate+"','"+rlinkcode+"','"+parentlinktypeid+"','"+linklevel+"')\n"; 
											nooflnktype =nooflnktype+1;
										} // if
									} // for
								} // while
							}// if
							
//----------- end export Relation -------							

//--------------- export term --------------------
							HashMap<?, ?> termList = cObj.getTerm();
							Iterator<?> itterm = termList.keySet().iterator();	
						//	String termcode1 = "";
						//	String languagecode1 = "";

							while(itterm.hasNext()){
								String termuri = (String) itterm.next();
								if(termcode == null) termcode = "";
								if(termuri.equals(termcode) || termcode==""){
									ETermObject tObj = cObj.getTermObject(termuri); 
							
									if(tObj != null){
										//String turi = tObj.getUri();
										// xxxxxxxxxxxx check language here
										String tlang =tObj.getLanguage();
								
										String tcagrovoc =tObj.getCodeAGROVOC();
										String tcasc =tObj.getCodeASC();
										String tcasfa =tObj.getCodeASFA();
										String tcfaopa =tObj.getCodeFAOPA();
										String tcfaoterm =tObj.getCodeFAOTERM();
										String tcfishery =tObj.getCodeFishery();
										//String tctaxonomic =tObj.getCodeTaxonomic();
										
										String tcreatedate =tObj.getCreateDate();
										String tupdatedate =tObj.getUpdateDate();
										String tstatus =tObj.getStatus();
										String tstatuscode = "";
										String termspell = tObj.getLabel();
										
										if(tcagrovoc == null)	tcagrovoc = tcasc;
										if(tcagrovoc == null)	tcagrovoc = tcasfa;
										if(tcagrovoc == null)	tcagrovoc = tcfaopa;
										if(tcagrovoc == null)	tcagrovoc = tcfaoterm;
										if(tcagrovoc == null)	tcagrovoc = tcfishery;
									//	if(tcagrovoc == null)	tcagrovoc = tctaxonomic;
										
										if(tstatus == null) tstatus = "";
										if(tstatus != ""){
											if(tObj.isMainLabel()){
												if(tstatus.equals(OWLStatusConstants.PROPOSED)) tstatuscode = "10";
												if(tstatus.equals(OWLStatusConstants.VALIDATED)) tstatuscode = "15";
												if(tstatus.equals(OWLStatusConstants.PUBLISHED)) tstatuscode = "20";
												if(tstatuscode.equals(""))	tstatuscode = "50";
											}else{
												if(tstatus.equals(OWLStatusConstants.PROPOSED)) tstatuscode = "70";
												if(tstatus.equals(OWLStatusConstants.VALIDATED)) tstatuscode = "80";
												if(tstatus.equals(OWLStatusConstants.PUBLISHED)) tstatuscode = "90";
												if(tstatuscode.equals(""))	tstatuscode = "60";
											}
											if(tstatus.equals(OWLStatusConstants.PROPOSED_GUEST)) tstatuscode = "100";
											if(tstatus.equals(OWLStatusConstants.PROPOSED_DEPRECATED)) tstatuscode = "120";
										//	temporary,	proposed (trusted), revised, proposed (non trusted),reference	
										}
										if(tcagrovoc == null) tcagrovoc = "";
										if(tcagrovoc != "")
											if(tcagrovoc.equals("36000")) tstatuscode = "110";
										
										String scopeid = "";			String idowner = "";
										String termsense = "NULL";		String termoffset = "NULL";
										if(termScript==null) termScript= "";
										for(int k=0;k<explang.size();k++)
										{
											String selectlang = (String) explang.get(k);
											if(tlang.equals(selectlang)) 
											{
												if(termScript=="") {
													termScript = "--\n" +
																 "-- Dumping data for table `termcode`\n" +
																 "-- \n"+  termfield + "\n"; 
																// "/*!40000 ALTER TABLE `termcode` DISABLE KEYS */;" +									
												}
												if(nooftermcode%600==0) 
												{	
													if(nooftermcode!=0) 
														termScript = termScript + ";\n\n"+ termfield+"\n";
												}
												if(nooftermcode%600 != 0) termScript = termScript + ",\n";							
												// check language tlang xxxxxxxxxxxxxx
//			String termfield = "INSERT INTO `agrovocterm` (`termcode`,`languagecode`,`termspell`,`statusid`,`createdate`,`frequencyiad`,`frequencycad`,`lastupdate`,`scopeid`,`idowner`,`termsense`,`termoffset`) VALUES"; 
//tctaxonomic
												//tcode
												termScript = termScript  +
													"\t('"+tcagrovoc+"','"+tlang+"','"+termspell+"','"+tstatuscode+"','"+tcreatedate+"','0','0','"+
													tupdatedate+"','"+scopeid+"','"+idowner+"','"+termsense+"','"+termoffset+"')" ;
													nooftermcode = nooftermcode + 1;
											}	
										}
								//-------------------- termlink
									//xxxxxxxxxxxxxx check language criteria 										
										String termcode2 = "";
										String languagecode2 = "";
										
										String termcode1 = "";
										String languagecode1 = "";
										
										if(tObj.isMainLabel())
										{
											termcode1 = tcagrovoc; // tObj.getUri();
											languagecode1 = tObj.getLanguage();
										}else{
											termcode2 = tcagrovoc ; //tObj.getUri();
											languagecode2 = tObj.getLanguage();
										}
										String linktypeid = "";
										String createdate = tObj.getCreateDate();
										String maintenancegroupid = "";
										String  newlinktypeid = "NULL";
										String confirm = "NULL";
										String technique = "NULL";
										String update = "NULL";
										String updmaintenancegroupid = "NULL";
										if(termcode1 != ""){
											if(termlnkScript == null) termlnkScript = "";
											for(int m=0;m<explang.size();m++)
											{
												String selectlang = (String) explang.get(m);
												if(tlang.equals(selectlang)) 
												{
													if(termlnkScript=="")
													{ 
														termlnkScript = "--\n" +
																		"-- Dumping data for table `termlink`\n" +
																		"--\n"+ termlnkfield + "\n"; 
																	//	"/*!40000 ALTER TABLE `termlink` DISABLE KEYS */;" +	
													}
													if((nooftermlnk%600)==0){ if(nooftermlnk!=0)	termlnkScript = termlnkScript +";\n"+ termlnkfield+"\n";	}
													if((nooftermlnk%600)!=0) termlnkScript  = termlnkScript + ",\n";
													termlnkScript =termlnkScript + 
													"\t('"+termcode1+"','"+termcode2+"','"+languagecode1+"','"+languagecode2+"'," +
													"'"+linktypeid+"','"+createdate+"','"+maintenancegroupid+"','"+newlinktypeid+"',"+
													"'"+confirm+"','"+technique+"','"+update+"'," + 
													"'"+updmaintenancegroupid+"')";
													nooftermlnk = nooftermlnk +1;		
												}	
											}	
										}				
									} // if(tObj
								} //if(termuri.equals(termcode)){
							} // while */							
//----------------end export term -----------------							
						}
					} catch (ParseException e) {
							e.printStackTrace();
					}
				} // if cObj != null
			} // if(conceptURI.equals(concepturi)){
		}
		if(catScript == null) catScript = "";
		if(catScript!=""){		
			catScript = catScript + ";\n" ;
		//		"/*!40000 ALTER TABLE `categories` ENABLE KEYS */;\n\n";
		}
		if(catscmScript==null) catscmScript = "";
		if(catscmScript!="") {
			catscmScript = catscmScript  + ";\n";
		//	"/*!40000 ALTER TABLE `catschemes` ENABLE KEYS */;\n";
		}
		
		if(lnkScript==null) lnkScript= "";
		if(lnkScript!="") {
			lnkScript = lnkScript + ";\n";
			//	"/*!40000 ALTER TABLE `linktype` ENABLE KEYS */;\n";
		}
		if(termlnkScript==null) termlnkScript= "";
		if(termlnkScript!=""){
			termlnkScript = termlnkScript + ";\n";
		//		"/*!40000 ALTER TABLE `termlink` ENABLE KEYS */;\n";
		}
		if(termScript==null) termScript= "";
		if(termScript!="")		{
			termScript  = termScript + ";\n";
		//	"/*!40000 ALTER TABLE `termcode` ENABLE KEYS */;\n";
		}
		
		mappScript = ""; 
		scopeScript = ""; 
		tagtypeScript = "";
		termtagScript = "";

		returnstr = catscmScript  + "\n"+ catScript+"\n"+ lnkScript + "\n" +
					mappScript+"\n"+ scopeScript+"\n"+ tagtypeScript+"\n"+ 
					termlnkScript+"\n"+termScript +"\n"+termtagScript+"\n";
		return returnstr;
	}
	
	public static String expLanguageScript()
	{
		//final String ExpLangStr = "";
		String sql = "SELECT language_code,language_note,local_language FROM language_code ORDER BY language_code";
		ArrayList<String[]> langlist = QueryFactory.getHibernateSQLQuery(sql);
		
		String groupid="0"; int order =0 ; String createdate = "";
		String lngstr = "";
    	if(langlist.size()>0)
    	{
    		lngstr= "--\n"+
					"-- Dumping data for table `language`\n"+
					"--\n"+
					"INSERT INTO `language` (`languagecode`,`name`,`lnggroupid`,`originalname`,`lngorder`,`createdate`) VALUES \n";		    		
  //  		"/*!40000 ALTER TABLE `language` DISABLE KEYS */;\n+
    	}
    	for(int i=0;i<langlist.size();i++)
    	{
    		String[] item = (String[]) langlist.get(i); 
    		order = order +1;
    		if(i!=0) lngstr = lngstr + ",\n";
    		lngstr = lngstr + "\t('"+item[0] +"','"+item[1]+ "','"+groupid + "','"+item[2]+ "','" + order + "','"+ createdate +  "')";	    	
    	}
    	lngstr = lngstr + ";\n";
 //   	lngstr = lngstr + "/*!40000 ALTER TABLE `language` ENABLE KEYS */;\n";
    	return lngstr ;
		
	}
	public static String expTermStatusScript()
	{
		String retstr = "";
		String sql = "SELECT id,status FROM owl_status ORDER BY id";
		ArrayList<String[]> result = QueryFactory.getHibernateSQLQuery(sql);
		
		if(result.size()>0)
    	{
    		retstr = "--\n"+
					"-- Dumping data for table `termstatus`\n"+
					"--\n"+
					"INSERT INTO `termstatus` (`statusid`,`statusdesc`,`languagecode`) VALUES \n";		  
	//		"/*!40000 ALTER TABLE `termstatus` DISABLE KEYS */;\n"+
    	}
    	for(int i=0;i<result.size();i++)
    	{
    		String[] item = (String[]) result.get(i); 
    		if(i!=0) retstr = retstr + ",\n";
    		retstr = retstr + "\t('"+item[0] +"','"+item[1]+ "','')";	    
    	}
    	retstr = retstr + ";\n";
   // 	retstr = retstr + "/*!40000 ALTER TABLE `termstatus` ENABLE KEYS */;\n";
    	return retstr ;
	}
	
	public static String expMaintenancegroupsScript(){
		String expMaintengroup = "";
		String sql = "select users_groups_name,users_group_id,username,password " +
		" FROM users_groups,users,users_groups_map " +
		" Where users_groups_map.users_group_id = users_groups.users_groups_id " +
		" and users_groups_map.users_id = users.user_id";
		ArrayList<String[]> datalist = QueryFactory.getHibernateSQLQuery(sql);
		
		String tmpstrfld = "--\n"+
				"-- Dumping data for table `maintenancegroups`\n"+
				"--\n"+
				"INSERT INTO `maintenancegroups` (`name`,`maintenancegroupid`,`login`,`password`) VALUES \n";
	//	"/*!40000 ALTER TABLE `maintenancegroups` DISABLE KEYS */;\n"+
 
   // 	if(datalist.size()>0) expMaintengroup = tmpstrfld;
      	if(datalist.size()>0)
	     {
      	 	int k = 1 ;
      	 	expMaintengroup = tmpstrfld;
	    	for(int i=0;i<datalist.size();i++)
	    	{
	    		String[] item = (String[]) datalist.get(i); 
	   			if(k%600==0)  expMaintengroup = expMaintengroup + ";\n" + tmpstrfld;
	    		if(k%600 !=0) {
	    			if(k != 1) expMaintengroup = expMaintengroup + ",\n";
	    		}
	    		expMaintengroup = expMaintengroup + "\t('"+item[0] +"','"+item[1]+ "','"+item[2] + "','"+item[3]+ "')";	
	    		k = k+1;
	    	}
	    	expMaintengroup = expMaintengroup + ";\n";
	      }
    //	expMaintengroup = expMaintengroup + "/*!40000 ALTER TABLE `maintenancegroups` ENABLE KEYS */;\n";
    	
    	return expMaintengroup;
	}

	
	public static Boolean comparedate(String datec,String dates,String datef ) throws ParseException
	{
		Boolean returnvalue = false;
		DateFormat du = new SimpleDateFormat("dd/MM/yyyy"); // datestart = 02/04/2008
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); // dbdate = 2006-09-14
		java.util.Date d1 = null;
		java.util.Date d3 ;
		if(datec==null) datec="";
		if(dates==null) dates = "";
		if(datef==null) datef = "";
			
		if(datec!="") {
			d1 = df.parse(datec);
			if(dates!=""){
				java.util.Date d2 = du.parse(dates);
				if(datef!=""){
					d3 = du.parse(datef);
					if(d1.after(d2) & d1.before(d3))	returnvalue = true;
				}else{
					if(d1.after(d2)) returnvalue = true; 
				}
			}else{ 
				if(datef!=""){ // no date start
					d3 = du.parse(datef);
					if(d1.before(d3))	returnvalue= true;
				}else{ // no date criteria
					returnvalue = true;
				}
			}
		}
		else			// --- if datec is empty
		{
			returnvalue = true; // if datevalue in db empty return export
		}
		return returnvalue;		
		
	}
	public static String SetupHeader(){
		return  "" +
			"-- \n"  +
			"-- SQL format exported from the AGROVOC Concept Server Workbench\n" +
			"-- -------------------------------------------------------------\n" +
			"\n"+
			"/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;\n" +
			"/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;\n" +
			"/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;\n" +
			"/*!40101 SET NAMES utf8 */;\n" +
			"\n"+
			"/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;\n" +
			"/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;\n" +
			"/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;\n" +
			"\n"+
			"\n"+
			"--\n" +
			"-- Create schema agrovoc\n" +
			"--\n" +
			"\n"+
			"-- CREATE DATABASE IF NOT EXISTS agrovoc;\n" +
			"-- USE agrovoc;\n" ;
	}
	public static String GenerateCreateTableScript(){
		return ""+
		"--\n"+
		"-- Definition of table `agrovocterm`\n"+
		"--\n"+
		"\n"+
		"DROP TABLE IF EXISTS `agrovocterm`;\n"+
		"CREATE TABLE `agrovocterm` (\n"+
		"  `termcode` varchar(200) NOT NULL default '0',\n"+
		"  `languagecode` varchar(2) NOT NULL default '',\n"+
		"  `termspell` varchar(170) default NULL,\n"+
		"  `statusid` tinyint(3) unsigned default NULL,\n"+
		"  `createdate` date default NULL,\n"+
		"  `frequencyiad` int(11) default NULL,\n"+
		"  `frequencycad` int(11) default NULL,\n"+
		"  `lastupdate` datetime default NULL,\n"+
		"  `scopeid` varchar(2) default NULL,\n"+
		"  `idowner` tinyint(2) default '10',\n"+
		"  `termsense` tinyint(2) default NULL,\n"+
		"  `termoffset` varchar(8) default NULL,\n"+
		"  PRIMARY KEY  (`termcode`,`languagecode`),\n"+
		"  KEY `termspell` (`termspell`),\n"+
		"  KEY `statusid` (`statusid`)\n"+
		") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='InnoDB free: 70656 kB';\n"+
		"\n"+
		"\n"+
		"--\n"+
		"-- Definition of table `categories`\n"+
		"--\n"+
		"\n"+
		"DROP TABLE IF EXISTS `categories`;\n"+
		"CREATE TABLE `categories` (\n"+
		"  `schemeid` varchar(200) NOT NULL default '',\n"+
		"  `categoryid` varchar(200) NOT NULL default '',\n"+
		"  `languagecode` varchar(2) NOT NULL default '',\n"+
		"  `categoryname` varchar(255) default NULL,\n"+
		"  `parentcategoryid` varchar(10) default NULL,\n"+
		"  PRIMARY KEY  (`categoryid`,`languagecode`,`schemeid`),\n"+
		"  KEY `categoryname` (`categoryname`)\n"+
		") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n"+
		"\n"+
		"\n"+		
		"--\n"+
		"-- Definition of table `catschemes`\n"+
		"--\n"+
		"\n"+
		"DROP TABLE IF EXISTS `catschemes`;\n"+
		"CREATE TABLE `catschemes` (\n"+
		"  `schemeid` varchar(200) NOT NULL default '0',\n"+
		"  `maintenancegroupid` int(11) NOT NULL default '0',\n"+
		"  `scheme` varchar(150) NOT NULL default '',\n"+
		"  `languagecode` varchar(2) NOT NULL default '',\n"+
		"  `active` varchar(1) default NULL\n"+
		"--  PRIMARY KEY  (`schemeid`,`languagecode`),\n"+
		"--  KEY `category` (`schemeid`)\n"+
		") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n"+
		"\n"+
		"\n"+
		"--\n"+
		"-- Definition of table `language`\n"+
		"--\n"+
		"\n"+
		"DROP TABLE IF EXISTS `language`;\n"+
		"CREATE TABLE `language` (\n"+
		"  `languagecode` varchar(2) NOT NULL default '',\n"+
		"  `name` varchar(15) default NULL,\n"+
		"  `lnggroupid` tinyint(3) unsigned default NULL,\n"+
		"  `originalname` varchar(15) default NULL,\n"+
		"  `lngorder` tinyint(4) default '0',\n"+
		"  `createdate` datetime default NULL,\n"+
		"  PRIMARY KEY  (`languagecode`)\n"+
		") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n"+
		"\n"+
		"\n"+
		"--\n"+
		"-- Definition of table `linktype`\n"+
		"--\n"+
		"\n"+
		"DROP TABLE IF EXISTS `linktype`;\n"+
		"CREATE TABLE `linktype` (\n"+
		"  `linktypeid` varchar(200) NOT NULL default '',\n"+
		"  `languagecode` varchar(2) NOT NULL default '',\n"+
		"  `linkdesc` varchar(60) default NULL,\n"+
		"  `linkabr` varchar(60) default NULL,\n"+
		"  `linkdescription` longtext,\n"+
		"  `createdate` datetime default NULL,\n"+
		"  `rlinkcode` int(11) unsigned default NULL,\n"+
		"  `parentlinktypeid` int(11) default NULL,\n"+
		"  `linklevel` varchar(2) default NULL,\n"+
		"  PRIMARY KEY  (`linktypeid`,`languagecode`),\n"+
		"  UNIQUE KEY `linkabr` (`linkabr`,`languagecode`)\n"+
		") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n"+
		"\n"+
		"\n"+
		"--\n"+
		"-- Definition of table `maintenancegroups`\n"+
		"--\n"+
		"\n"+
		"DROP TABLE IF EXISTS `maintenancegroups`;\n"+
		"CREATE TABLE `maintenancegroups` (\n"+
		"  `name` varchar(150) NOT NULL default '',\n"+
		"  `maintenancegroupid` int(11) NOT NULL default '0',\n"+
		"  `login` varchar(30) NOT NULL default '',\n"+
		"  `password` varchar(30) NOT NULL default '',\n"+
		"  PRIMARY KEY  (`name`,`maintenancegroupid`,`login`) \n "+
		") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n"+
		"\n"+
		"\n"+
		"--\n"+
		"-- Definition of table `mapping`\n"+
		"--\n"+
		"\n"+
		"DROP TABLE IF EXISTS `mapping`;\n"+
		"CREATE TABLE `mapping` (\n"+
		"  `catcode` varchar(5) NOT NULL default '0',\n"+
		"  `termcode` int(11) NOT NULL default '0',\n"+
		"  `schemeid` int(11) NOT NULL default '0',\n"+
		"  `parentcategoryid` varchar(10) NOT NULL default '',\n"+
		"  PRIMARY KEY  (`catcode`,`termcode`,`schemeid`,`parentcategoryid`)\n"+
		") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n"+
		"\n"+
		"\n"+
		"--\n"+
		"-- Definition of table `scope`\n"+
		"--\n"+
		"\n"+
		"DROP TABLE IF EXISTS `scope`;\n"+
		"CREATE TABLE `scope` (\n"+
		"  `scopeid` varchar(2) NOT NULL default '',\n"+
		"  `scopedesc` varchar(60) default NULL,\n"+
		"  `languagecode` varchar(2) NOT NULL default '',\n"+
		"  `scopegrpid` tinyint(3) unsigned default NULL,\n"+
		"  PRIMARY KEY  (`scopeid`,`languagecode`)\n"+
		") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n"+
		"\n"+
		"\n"+
		"--\n"+
		"-- Definition of table `tagtype`\n"+
		"--\n"+
		"\n"+
		"DROP TABLE IF EXISTS `tagtype`;\n"+
		"CREATE TABLE `tagtype` (\n"+
		"  `tagtypeid` tinyint(3) unsigned NOT NULL default '0',\n"+
		"  `tagdesc` varchar(60) default NULL,\n"+
		"  `languagecode` varchar(2) NOT NULL default '',\n"+
		"  PRIMARY KEY  (`tagtypeid`,`languagecode`)\n"+
		") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n"+
		"\n"+
		"\n"+
		"--\n"+
		"-- Definition of table `termlink`\n"+
		"--\n"+
		"\n"+
		"DROP TABLE IF EXISTS `termlink`;\n"+
		"CREATE TABLE `termlink` (\n"+
		"  `termcode1` varchar(200) NOT NULL default '0',\n"+
		"  `termcode2` varchar(200) NOT NULL default '0',\n"+
		"  `languagecode1` varchar(2) default NULL,\n"+
		"  `languagecode2` varchar(2) default NULL,\n"+
		"  `linktypeid` int(11) unsigned NOT NULL default '0',\n"+
		"  `createdate` datetime default NULL,\n"+
		"  `maintenancegroupid` int(11) NOT NULL default '0',\n"+
		"  `newlinktypeid` int(11) default NULL,\n"+
		"  `confirm` varchar(1) default NULL,\n"+
		"  `technique` varchar(5) default NULL,\n"+
		"  `update` datetime default NULL,\n"+
		"  `updmaintenancegroupid` int(11) default NULL,\n"+
		"  PRIMARY KEY  (`termcode1`,`termcode2`,`linktypeid`)\n"+
		") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n"+
		"\n"+
		"\n"+
		"--\n"+
		"-- Definition of table `termstatus`\n"+
		"--\n"+
		"\n"+
		"DROP TABLE IF EXISTS `termstatus`;\n"+
		"CREATE TABLE `termstatus` (\n"+
		"  `statusid` tinyint(3) unsigned NOT NULL default '0',\n"+
		"  `statusdesc` varchar(60) default NULL,\n"+
		"  `languagecode` varchar(2) NOT NULL default '',\n"+
		"  PRIMARY KEY  (`statusid`,`languagecode`)\n"+
		") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n"+
		"\n"+
		"\n"+
		"--\n"+
		"-- Definition of table `termtag`\n"+
		"--\n"+
		"\n"+
		"DROP TABLE IF EXISTS `termtag`;\n"+
		"CREATE TABLE `termtag` (\n"+
		"  `termcode` int(11) NOT NULL default '0',\n"+
		"  `languagecode` varchar(2) NOT NULL default '',\n"+
		"  `tagtypeid` tinyint(3) unsigned NOT NULL default '0',\n"+
		"  `tagtext` text,\n"+
		"  `createdate` datetime default NULL,\n"+
		"  `lastupdate` datetime default NULL,\n"+
		"  PRIMARY KEY  (`tagtypeid`,`languagecode`,`termcode`)\n"+
		") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
	}
	
	private static  String setupFinalString(){
		return 
"/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;\n" +
"/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;\n" +
"/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;\n" +
"/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;\n" +
"/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;\n" +
"/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;\n" +
"/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;\n";
	}
}
