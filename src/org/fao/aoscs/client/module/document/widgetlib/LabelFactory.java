package org.fao.aoscs.client.module.document.widgetlib;

import java.util.ArrayList;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.domain.ExportParameterObject;
import org.fao.aoscs.domain.LabelObject;
import org.fao.aoscs.domain.PermissionGroupMapId;
import org.fao.aoscs.domain.RecentChangeData;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.TranslationObject;
import org.fao.aoscs.domain.Users;
import org.fao.aoscs.domain.UsersGroups;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class LabelFactory {
	
	static final int ITEMLABEL = 0;
	static final int ITEMCHANGE = 1;
	static final int ITEMOLD = 2;
	
	public static Widget makeLabel(RecentChangeData rc, int returnType)
	{
		Users u = null;
		RelationshipObject r = null;
		UsersGroups g  = null;		
		PermissionGroupMapId pgm = null;		
		switch(rc.getActionId())
		{
			case 43: // user-add				
				if(returnType == ITEMLABEL){
					u = (Users)(rc.getObject().get(0));
					return makeLabel(u.getFirstName()+" "+u.getLastName()+" ("+u.getUsername()+")" , RecentChangesTable.USER_TYPE);
				}
				if(returnType == ITEMCHANGE){					
					return new HTML("&nbsp;");
				}				
				if(returnType == ITEMOLD){					
					return new HTML("&nbsp;");
				}				
				break;
			case 44: // user-edit				
				if(returnType == ITEMLABEL)
					u = (Users)(rc.getObject().get(0));
				else if(returnType == ITEMCHANGE)	
					u = (Users)(rc.getObject().get(0));					
				else if(returnType == ITEMOLD)	
					u = (Users)(rc.getOldObject().get(0));					
				return makeLabel(u.getFirstName()+" "+u.getLastName()+" ("+u.getUsername()+")" , RecentChangesTable.USER_TYPE);
				
			case 46: //relationship-create				
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesTable.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesTable.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMOLD)
					return new HTML("&nbsp;");										
				
				
			case 47: //relationship-delete
				if(returnType == ITEMLABEL)
					return makeLabel(makeRelationshipLabel(r) , RecentChangesTable.RELATIONSHIP_TYPE);								
				else if(returnType == ITEMCHANGE)
					return new HTML("&nbsp;");										
				else if(returnType == ITEMOLD)
					return new HTML("&nbsp;");										
				
			
			case 48: //relationship-edit-label-create				
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesTable.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					TranslationObject to = (TranslationObject)(rc.getNewObject().get(0));
					return makeLabel( to.getLabel() + " (" + to.getLang() + ")" , RecentChangesTable.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMOLD){
					return new HTML("&nbsp;");
				}
			case 49: //relationship-edit-label-edit				
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesTable.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					TranslationObject to = (TranslationObject)(rc.getNewObject().get(0));
					return makeLabel( to.getLabel() + " (" + to.getLang() + ")" , RecentChangesTable.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMOLD){
					TranslationObject to = (TranslationObject)(rc.getOldObject().get(0));
					return makeLabel( to.getLabel() + " (" + to.getLang() + ")" , RecentChangesTable.RELATIONSHIP_TYPE);
				}	
			case 50: //relationship-edit-label-delete				
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesTable.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					return new HTML("&nbsp;");
				}
				else if(returnType == ITEMOLD){
					TranslationObject to = (TranslationObject)(rc.getOldObject().get(0));
					return makeLabel( to.getLabel() + " (" + to.getLang() + ")" , RecentChangesTable.RELATIONSHIP_TYPE);
				}	
			case 51: //relationship-edit-definition-create				
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesTable.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					TranslationObject to = (TranslationObject)(rc.getNewObject().get(0));
					return makeLabel( to.getLabel() + " (" + to.getLang() + ")" , RecentChangesTable.NO_TYPE);
				}
				else if(returnType == ITEMOLD){
					return new HTML("&nbsp;");
				}
			case 52: //relationship-edit-definition-edit				
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesTable.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					TranslationObject to = (TranslationObject)(rc.getNewObject().get(0));
					return makeLabel( to.getLabel() + " (" + to.getLang() + ")" , RecentChangesTable.NO_TYPE);
				}
				else if(returnType == ITEMOLD){
					TranslationObject to = (TranslationObject)(rc.getOldObject().get(0));
					return makeLabel( to.getLabel() + " (" + to.getLang() + ")" , RecentChangesTable.NO_TYPE);
				}	
			case 53: //relationship-edit-definition-delete				
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesTable.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					return new HTML("&nbsp;");
				}
				else if(returnType == ITEMOLD){
					TranslationObject to = (TranslationObject)(rc.getOldObject().get(0));
					return makeLabel( to.getLabel() + " (" + to.getLang() + ")" , RecentChangesTable.NO_TYPE);
				}	
			case 54: //relationship-edit-property-create
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesTable.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					TranslationObject to = (TranslationObject)(rc.getNewObject().get(0));
					return makeLabel( to.getLabel() , RecentChangesTable.NO_TYPE);					
				}
				else if(returnType == ITEMOLD){
					return new HTML("&nbsp;");
				}	
			case 55: //relationship-edit-property-delete
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesTable.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					return new HTML("&nbsp;");
				}
				else if(returnType == ITEMOLD){
					TranslationObject to = (TranslationObject)(rc.getOldObject().get(0));
					return makeLabel( to.getLabel() , RecentChangesTable.NO_TYPE);					
				}	
			case 56: //relationship-edit-inverse-property-create
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesTable.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					TranslationObject to = (TranslationObject)(rc.getNewObject().get(0));
					return makeLabel( to.getLabel() , RecentChangesTable.NO_TYPE);					
				}
				else if(returnType == ITEMOLD){
					return new HTML("&nbsp;");
				}	
			case 57: //relationship-edit-inverse-property-edit
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesTable.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					TranslationObject to = (TranslationObject)(rc.getNewObject().get(0));
					return makeLabel( to.getLabel() , RecentChangesTable.NO_TYPE);					
				}
				else if(returnType == ITEMOLD){
					return new HTML("&nbsp;");					
				}	
			case 58: //relationship-edit-inverse-property-delete
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesTable.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					return new HTML("&nbsp;");										
				}
				else if(returnType == ITEMOLD){
					return new HTML("&nbsp;");					
				}
			case 59: //relationship-edit-domain-create
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesTable.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					TranslationObject to = (TranslationObject)(rc.getNewObject().get(0));
					return makeLabel( to.getLabel() , RecentChangesTable.NO_TYPE);
				}
				else if(returnType == ITEMOLD){
					return new HTML("&nbsp;");					
				}
			case 60: //relationship-edit-domain-delete
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesTable.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					return new HTML("&nbsp;");					
				}
				else if(returnType == ITEMOLD){
					TranslationObject to = (TranslationObject)(rc.getOldObject().get(0));
					return makeLabel( to.getLabel() , RecentChangesTable.NO_TYPE);					
				}	
			case 61: //relationship-edit-range-create
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesTable.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					TranslationObject to = (TranslationObject)(rc.getNewObject().get(0));
					return makeLabel( to.getLabel() , RecentChangesTable.NO_TYPE);					
				}
				else if(returnType == ITEMOLD){
					return new HTML("&nbsp;");					
				}	
			case 62: //relationship-edit-range-edit
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesTable.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					TranslationObject to = (TranslationObject)(rc.getNewObject().get(0));
					return makeLabel( to.getLabel() , RecentChangesTable.NO_TYPE);
				}
				else if(returnType == ITEMOLD){
					TranslationObject to = (TranslationObject)(rc.getOldObject().get(0));
					return makeLabel( to.getLabel() , RecentChangesTable.NO_TYPE);					
				}	
			case 63: //relationship-edit-range-delete
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesTable.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					return new HTML("&nbsp;");
				}
				else if(returnType == ITEMOLD){
					TranslationObject to = (TranslationObject)(rc.getOldObject().get(0));
					return makeLabel( to.getLabel() , RecentChangesTable.NO_TYPE);					
				}	
			case 64: //relationship-edit-range-value-add
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesTable.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					TranslationObject to = (TranslationObject)(rc.getNewObject().get(0));
					return makeLabel( to.getLabel() + ": " + to.getDescription() , RecentChangesTable.NO_TYPE);
				}
				else if(returnType == ITEMOLD){
					return new HTML("&nbsp;");
										
				}
			case 65: //group-create
				g  = (UsersGroups)(rc.getObject().get(0));
				if(returnType == ITEMLABEL){										
					return makeLabel( g.getUsersGroupsName() , RecentChangesTable.GROUP_TYPE );
				}
				else if(returnType == ITEMCHANGE){
					return makeLabel( "Description: " + g.getUsersGroupsDesc() , RecentChangesTable.NO_TYPE);
				}
				else if(returnType == ITEMOLD){
					return new HTML("&nbsp;");
				}	
			case 66: //group-edit
				g  = (UsersGroups)(rc.getObject().get(0));
				if(returnType == ITEMLABEL){										
					return makeLabel( g.getUsersGroupsName() , RecentChangesTable.GROUP_TYPE );
				}
				else if(returnType == ITEMCHANGE){
					return makeLabel( "Description: " + g.getUsersGroupsDesc() , RecentChangesTable.NO_TYPE);
				}
				else if(returnType == ITEMOLD){
					g  = (UsersGroups)(rc.getOldObject().get(0));
					return makeLabel( "Description: " + g.getUsersGroupsDesc() , RecentChangesTable.NO_TYPE);									
				}	
			case 67: //group-delete
				g  = (UsersGroups)(rc.getObject().get(0));
				if(returnType == ITEMLABEL){										
					return makeLabel( g.getUsersGroupsName() , RecentChangesTable.GROUP_TYPE );
				}
				else if(returnType == ITEMCHANGE){
					return new HTML("&nbsp;");
				}
				else if(returnType == ITEMOLD){
					return new HTML("&nbsp;");								
				}
				
			case 68: //group-permission-add
				pgm  = (PermissionGroupMapId)(rc.getObject().get(0));
				if(returnType == ITEMLABEL){										
					return makeLabel( pgm.getGroupName() , RecentChangesTable.GROUP_TYPE );
				}
				else if(returnType == ITEMCHANGE){
					return makeLabel( pgm.getPermitName() , RecentChangesTable.NO_TYPE);
				}
				else if(returnType == ITEMOLD){
					return new HTML("&nbsp;");									
				}
				
			case 69: //group-permission-delete
				pgm  = (PermissionGroupMapId)(rc.getObject().get(0));
				if(returnType == ITEMLABEL){										
					return makeLabel( pgm.getGroupName() , RecentChangesTable.GROUP_TYPE );
				}
				else if(returnType == ITEMCHANGE){
					return new HTML("&nbsp;");
				}
				else if(returnType == ITEMOLD){
					return makeLabel( pgm.getPermitName() , RecentChangesTable.NO_TYPE);									
				}	
			case 70: //group-member-add
				g  = (UsersGroups)(rc.getObject().get(0));
				u = (Users)(rc.getNewObject().get(0));
				if(returnType == ITEMLABEL){										
					return makeLabel( g.getUsersGroupsName() , RecentChangesTable.GROUP_TYPE );
				}
				else if(returnType == ITEMCHANGE){
					return makeLabel( u.getUsername() , RecentChangesTable.NO_TYPE);
				}
				else if(returnType == ITEMOLD){
					return new HTML("&nbsp;");									
				}	
				
			case 71: //group-member-delete
				g  = (UsersGroups)(rc.getObject().get(0));
				u = (Users)(rc.getOldObject().get(0));
				if(returnType == ITEMLABEL){										
					return makeLabel( g.getUsersGroupsName() , RecentChangesTable.GROUP_TYPE );
				}
				else if(returnType == ITEMCHANGE){
					return new HTML("&nbsp;");									
				}
				else if(returnType == ITEMOLD){
					return makeLabel( u.getUsername() , RecentChangesTable.NO_TYPE);
				}
				
			case 74: //export
				ExportParameterObject export  = (ExportParameterObject)(rc.getObject().get(0));
				if(returnType == ITEMLABEL){										
					return makeLabel( export.getExportFormat() , RecentChangesTable.EXPORT_TYPE );
				}
				else if(returnType == ITEMCHANGE){
					return new HTML("&nbsp;");									
				}
				else if(returnType == ITEMOLD){
					return new HTML("&nbsp;");
				}	
		}
		return new HTML("&nbsp;");
	}
	
	private static Widget makeLabel(String label, int objType){
		String imgURL = "images/spacer.gif";
		switch(objType){
		case RecentChangesTable.NO_TYPE:
			imgURL = "images/spacer.gif";
			break;
		case RecentChangesTable.USER_TYPE:
			imgURL = "images/New-users.gif";
			break;
		case RecentChangesTable.RELATIONSHIP_TYPE:
			imgURL = "images/relationship-object-logo.gif";
			break;
		case RecentChangesTable.GROUP_TYPE:
			imgURL = "images/usericon.gif";
			break;
		case RecentChangesTable.EXPORT_TYPE:
			imgURL = "images/export_small.gif";
			break;	
		}
						
		HTML txt = new HTML(" "+label);
		HorizontalPanel panel = new HorizontalPanel();
		panel.add(new Image(imgURL));
		panel.add(new HTML("&nbsp;&nbsp;"));
		panel.add(txt);
		return panel;			
	}
	
	private static String makeRelationshipLabel(RelationshipObject rObj)
	{
		ArrayList<LabelObject> labelList = rObj.getLabelList();
		String labelStr = "";
		for(int i=0;i<labelList.size();i++)
		{
			LabelObject labelObj = (LabelObject) labelList.get(i);
			String lang = (String) labelObj.getLanguage();
			if(MainApp.userSelectedLanguage.contains(lang))
			{
				String label = (String) labelObj.getLabel();
				if(labelStr.equals(""))
					labelStr += " "+label+" ("+lang+")";
				else
					labelStr += ", "+label+" ("+lang+")";
			}
		}
		return labelStr;
	}
}
