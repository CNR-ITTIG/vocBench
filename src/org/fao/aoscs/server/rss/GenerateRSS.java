package org.fao.aoscs.server.rss;

import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sf.gilead.pojo.gwt.LightEntity;

import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.client.module.constant.OWLActionConstants;
import org.fao.aoscs.domain.AttributesObject;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.InitializeSystemData;
import org.fao.aoscs.domain.LabelObject;
import org.fao.aoscs.domain.NonFuncObject;
import org.fao.aoscs.domain.OwlAction;
import org.fao.aoscs.domain.RecentChangeData;
import org.fao.aoscs.domain.RecentChanges;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.SchemeObject;
import org.fao.aoscs.domain.TermObject;
import org.fao.aoscs.domain.TranslationObject;
import org.fao.aoscs.domain.Users;
import org.fao.aoscs.domain.Validation;
import org.fao.aoscs.server.SystemServiceImpl;
import org.fao.aoscs.server.ValidationServiceImpl;

import com.sun.syndication.feed.module.DCModule;
import com.sun.syndication.feed.module.DCModuleImpl;
import com.sun.syndication.feed.module.DCSubject;
import com.sun.syndication.feed.module.DCSubjectImpl;
import com.sun.syndication.feed.module.SyModule;
import com.sun.syndication.feed.module.SyModuleImpl;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;

public class GenerateRSS {

	public String getRSSText(String feedType, String ontoIogyId, String rcid) {
		String rssString = "";
		SyndFeedOutput output = new SyndFeedOutput();
		try {
			SyndFeed feed = getRSSFeed(feedType, ontoIogyId, rcid);
			if (feed != null) {
				rssString = output.outputString(feed);
			}
		} catch (FeedException e) {
			e.printStackTrace();
		}
		return rssString;
	}
	
	public String checkObject(ArrayList<LightEntity> list){
		
		Object obj = list.get(0);
		if(obj instanceof ConceptObject)
		{
			ConceptObject cObj = (ConceptObject) obj;
			HashMap<String,TermObject> tObjList = cObj.getTerm();
			String value = "";
			if(tObjList!=null)
			{
				Iterator<String> itr = tObjList.keySet().iterator();
				while(itr.hasNext())
				{
					TermObject tObj = (TermObject) tObjList.get((String) itr.next());
					String lang = tObj.getLang();
					String term = tObj.getLabel();
					value += term+"("+lang+")";
				}
				
			}
			return value;
		}
		else if(obj instanceof TermObject)
		{
			TermObject tObj = (TermObject) obj;
			String preferred =  (tObj.isMainLabel())? "Preferred" : "Non-preferred";
			String value = "";
			if (tObj.getLabel()!=null){
				value = tObj.getLabel() +" ("+preferred+") ";
			}
			return value;
		}
		else if(obj instanceof SchemeObject)
		{
			SchemeObject sObj = (SchemeObject) obj;
			String value = "";
			if(sObj.getSchemeLabel()!=null){
				value = sObj.getSchemeLabel();
			}
			return value;
		}
		else if(obj instanceof AttributesObject)
		{
			AttributesObject aObj = (AttributesObject) obj;
			RelationshipObject rObj = aObj.getRelationshipObject();
			NonFuncObject nfObj = aObj.getValue();
			String value = "";
			if(rObj!=null)
				value += makeRelationshipLabel(rObj)+" : "; 
			if(nfObj!=null)
				value += nfObj.getValue() + checkNullValueInParenthesis(nfObj.getLanguage());
			return value;
		}
		else if(obj instanceof TranslationObject)
		{
			TranslationObject trObj = (TranslationObject) obj;
			String value = "";
			if(trObj.getType()==TranslationObject.DEFINITIONTRANSLATION)
			{
				if(trObj.getLabel()!=null){
					value = trObj.getLabel();
				}	
			}
			else if(trObj.getType()==TranslationObject.IMAGETRANSLATION)
			{
				if(trObj.getLabel()!=null){
					value = trObj.getLabel();
				}
			}
			return value;
		}
		return "";
		
	}

	public SyndFeed getRSSFeed(String feedType, String ontoIogyId, String rcid) {

		try {

			SyndFeed feed = new SyndFeedImpl();
			SyModule synmodule = new SyModuleImpl();

			feed.setFeedType(feedType);
			feed.setTitle("VocBench Concept Server Workbench News");
			feed.setLink("index.html");
			feed.setDescription("Recent Changes on VocBench Concept Server Workbench.");
			feed.setLanguage("English");

			synmodule.setUpdateBase(new Date());
			synmodule.setUpdateFrequency(1);
			synmodule.setUpdatePeriod(SyModule.DAILY);

			List<SyModule> feedmodule = new ArrayList<SyModule>();
			feedmodule.add(synmodule);
			feed.setModules(feedmodule);

			ValidationServiceImpl v = new ValidationServiceImpl();
			
			ArrayList<RecentChanges> list = new ArrayList<RecentChanges>();			
			if(rcid.equals("0"))
				list = v.getRecentChangesData(Integer.parseInt(ontoIogyId));
			else
				list = v.getRecentChangesData(Integer.parseInt(ontoIogyId), Integer.parseInt(rcid));
		
			/*ArrayList<RecentChanges> otherList = new ArrayList<RecentChanges>();
			if(rcid.equals("0"))
				otherList = v.getRecentChangesData(0);
			else
				otherList = v.getRecentChangesData(0, Integer.parseInt(rcid));*/
			
			ArrayList<Users> usersList = v.getAllUsers();
			ArrayList<OwlAction> actionList = v.getAction();
			
			HashMap<String, Users> userMap = new HashMap<String, Users>();
			for(int i=0;i<usersList.size();i++)
			{
				Users user = usersList.get(i);
				userMap.put(""+user.getUserId(), user);
			}
			
			HashMap<String, OwlAction> actionMap = new HashMap<String, OwlAction>();
			for(int i=0;i<actionList.size();i++)
			{
				OwlAction action = actionList.get(i);
				actionMap.put(""+action.getId(), action);
			}
			
			List<SyndEntry> entries = new ArrayList<SyndEntry>();
			SyndEntry entry;
			SyndContent description;
			List<DCModule> dcmodulelist;

			// OWL Action Syndication  
			for (int i = 0; i < list.size(); i++) 
			{
				RecentChanges c = (RecentChanges) list.get(i);
				int modifiedId = c.getModifiedId();
				Date modifiedDate = c.getModifiedDate();

				String action = "";
				String creator = "";
				String contributor = "";
				String desc = "";
				ArrayList<String> languages = new ArrayList<String>();
								
				OwlAction owlAction = actionMap.get(""+c.getModifiedActionId());
				if(owlAction != null)
				{
					action = (owlAction.getAction()==null||owlAction.getAction().equals(""))?"":owlAction.getAction();
					action += (owlAction.getActionChild()==null||owlAction.getActionChild().equals(""))?"":"-"+owlAction.getActionChild();
				}
								
				try
				{
					Object obj = c.getModifiedObject().get(0);
					if(obj instanceof Validation)
					{
						Validation val = (Validation) obj;						
						if(val!=null)
						{
							if(c.getModifiedActionId() == 72 || c.getModifiedActionId() == 73){
								OwlAction a = actionMap.get(""+val.getAction());
								action += " - " + a.getAction() + 
										((a.getActionChild() != null && a.getActionChild().length()>0)? "-"+a.getActionChild() : "");
							}
							
							if(val.getOwnerId() !=0)
							{
								Users user = userMap.get(""+val.getOwnerId());
								if(user!=null)
								creator = user.getFirstName()==null?"":user.getFirstName()+user.getLastName()==null?"":" "+user.getLastName()+user.getUsername()==null?"":"("+user.getUsername()+")";
							}
							if(val.getModifierId()!=0)
							{
								Users user = userMap.get(""+val.getModifierId());
								if(user!=null)
								contributor =  user.getFirstName()==null?"":user.getFirstName()+user.getLastName()==null?"":" "+user.getLastName()+user.getUsername()==null?"":"("+user.getUsername()+")";
							}
							
							String conceptImg = "<img src='images/concept_logo.gif' border='0'>";
							String conceptLabel = "";
							
							if(val.getConceptObject()!=null)
							{
								if(val.getConceptObject().getUri()!=null)
								{
									if(!val.getConceptObject().getUri().startsWith(ModelConstants.ONTOLOGYBASENAMESPACE))
										conceptImg = "<img src='images/category_logo.gif' border='0'>";
								}
							}
							
							HashMap<String, TermObject> tObjList = (HashMap<String, TermObject>) val.getConceptObject().getTerm();
							if(tObjList!=null)
							{
								Iterator<String> itr = tObjList.keySet().iterator();
								while(itr.hasNext())
								{
									TermObject tObj = (TermObject) tObjList.get((String) itr.next());
									String lang = tObj.getLang();
									if(!languages.contains(lang))		
										languages.add(lang);
									if(!conceptLabel.equals(""))	
										conceptLabel += ",";
									conceptLabel += tObj.getLabel()+" ("+lang+")";
								}				
							}
							desc = conceptImg+"&nbsp;"+conceptLabel;							
							
						    //--- imm add to get new value and old value
							
							ArrayList<LightEntity> listNew = (ArrayList<LightEntity>) val.getNewObject();
							if (listNew!=null)
							{
								if(listNew.size()>0)
								{
									if(checkObject(listNew)!="")
									{
										desc += "<br><br>Change : "+checkObject(listNew);
									}
								}
							}
							
							ArrayList<LightEntity> listOld = (ArrayList<LightEntity>) val.getOldObject();
							if (listOld!=null)
							{
								if(listOld.size()>0)
								{
									if(checkObject(listOld)!="")
									{
										desc += "<br><br>Old Value : "+checkObject(listOld);
									}
								}
							}
							
							
						}						
					}	
					else if(obj instanceof RecentChangeData)
					{
						RecentChangeData rcObj = (RecentChangeData)c.getModifiedObject().get(0);
												
						if(rcObj.getObject().get(0)!=null)
						{
							OwlAction a = actionMap.get(""+rcObj.getActionId());
							if(a != null)
							{
								action = (a.getAction()==null||a.getAction().equals(""))?"":a.getAction();
								action += (a.getActionChild()==null||a.getActionChild().equals(""))?"":"-"+a.getActionChild();
							}						
							if(rcObj.getOwnerId() !=0)
                            {
                                Users user = userMap.get(""+rcObj.getOwnerId());
                                if(user!=null)
                                    creator = user.getFirstName()==null?"":user.getFirstName()+user.getLastName()==null?"":" "+user.getLastName()+user.getUsername()==null?"":"("+user.getUsername()+")";
                            }
							Users user = userMap.get(""+rcObj.getModifierId());
							desc = "";
							if(user!=null)
								contributor =  user.getFirstName()==null?"":user.getFirstName()+user.getLastName()==null?"":" "+user.getLastName()+user.getUsername()==null?"":"("+user.getUsername()+")";
							desc = LabelRSS.makeLabel(rcObj , LabelRSS.ITEMLABEL);
							desc += LabelRSS.makeLabel(rcObj , LabelRSS.ITEMCHANGE);
							desc += LabelRSS.makeLabel(rcObj , LabelRSS.ITEMOLD);
						}							
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}

				// ----------------

				entry = new SyndEntryImpl();
				entry.setTitle(action);
				entry.setLink("DownloadRSS?type="+feedType+"&ontologyId="+ontoIogyId+"&rcid="+modifiedId);
				entry.setPublishedDate(modifiedDate);
				description = new SyndContentImpl();
				description.setType("text/html");
				description.setValue(desc);
				entry.setDescription(description);

				DCModule dcmodule;
				DCSubject dcsubject = new DCSubjectImpl();
				dcmodule = new DCModuleImpl();
				dcsubject.setValue("the dcsubject");
				dcmodule.setSubject(dcsubject);
				dcmodule.setLanguages(languages);
				dcmodule.setIdentifier("dcIdentifiers");
				dcmodule.setCreator(creator);
				dcmodule.setContributor(contributor);
				dcmodule.setDate(modifiedDate);
				dcmodulelist = new ArrayList<DCModule>();
				dcmodulelist.add(dcmodule);

				entry.setModules(dcmodulelist);
				entries.add(entry);
			}
			
			
			feed.setEntries(entries);
			return feed;

		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println(ex.getMessage());
			return null;
		}

	}
	
	public static String makeRelationshipLabel(RelationshipObject rObj)
	{
		ArrayList<LabelObject> labelList = rObj.getLabelList();
		String labelStr = "";
		for(int i=0;i<labelList.size();i++)
		{
			LabelObject labelObj = (LabelObject) labelList.get(i);
			String lang = labelObj.getLanguage();
			String label = labelObj.getLabel();
			if(labelStr.equals(""))
				labelStr += " "+label+" ("+lang+")";
			else
				labelStr += ", "+label+" ("+lang+")";
		}
		return labelStr;
	}
	
	public static String checkNullValueInParenthesis(String obj)
	{
		if(obj==null || obj.length()<1)
			return "";
		else 
			return " ("+obj+")";
	}

	public static void main(String args[]) {
		try {
			SystemServiceImpl s = new SystemServiceImpl();
			
			InitializeSystemData data = s.initData(null);
			ModelConstants.loadConstants(data.getModelConstants());
			OWLActionConstants.loadConstants(data.getOwlActionConstants());
			
			GenerateRSS cRSS = new GenerateRSS();
			String fileName = "RSS1_0.xml";
			Writer writer = new FileWriter(fileName);
			
			SyndFeedOutput output = new SyndFeedOutput();
			output.output(cRSS.getRSSFeed("rss_2.0", "13", "0"), writer);
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
