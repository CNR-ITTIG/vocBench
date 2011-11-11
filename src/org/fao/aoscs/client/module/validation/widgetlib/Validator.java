package org.fao.aoscs.client.module.validation.widgetlib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import net.sf.gilead.pojo.gwt.LightEntity;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.module.concept.widgetlib.InfoTab;
import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.module.validation.ValidationTemplate;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.tree.LazyLoadingTree;
import org.fao.aoscs.domain.AttributesObject;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.IDObject;
import org.fao.aoscs.domain.LabelObject;
import org.fao.aoscs.domain.NonFuncObject;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.SchemeObject;
import org.fao.aoscs.domain.TermObject;
import org.fao.aoscs.domain.TranslationObject;
import org.fao.aoscs.domain.Validation;
import org.fao.aoscs.domain.ValidationPermission;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class Validator extends ValidationTemplate {

	public void init(int size)
	{
		initLoadingTable();
		if(valFilter == null)
			valFilter = new ValidatorFilter(this);
		initLayout(size);
	}

	public void initLayout(int size)
	{
		validatorPanel.clear();
		validatorPanel.setSize("100%","100%");
	    
		makeTitlePanel();
		initTable(size);
		makeValidationPanel();
		
		validatorPanel.add(titlePanel);
		validatorPanel.add(tablePanel);
		validatorPanel.add(validationFooterPanel);
		validatorPanel.setCellWidth(tablePanel, "100%");
		validatorPanel.setCellHeight(tablePanel, "100%");
		
		
	}

	public void initTable(int size)
	{
		vTable = new ValidationTable();
		vTable.setTable(getVFilter(), getStatusList(), getUserList(), getActionList(), getAcceptvalidationList(), getRejectvalidationList(), size);
	    tablePanel.clear();
	    tablePanel.setSize("100%", "100%");
	    tablePanel.add(vTable.getLayout());
	    tablePanel.setCellHeight(vTable.getLayout(), "100%");
	    tablePanel.setCellWidth(vTable.getLayout(), "100%");
	}

	public static String checkNullValueInParenthesis(String obj)
	{
		if(obj==null || obj.length()<1)
			return "";
		else 
			return " ("+obj+")";
	}

	public void managePermission(ArrayList<ValidationPermission> list)
	{
		Iterator<ValidationPermission> itr = list.iterator();
		while(itr.hasNext())
		{
			ValidationPermission vp = (ValidationPermission) itr.next();
			if(vp.getAction()==1)
			{
				addAcceptvalidationList(new Integer(vp.getStatus()), new Integer(vp.getNewstatus()));	
			}
			else  if(vp.getAction()==2)
			{
				addRejectvalidationList(new Integer(vp.getStatus()), new Integer(vp.getNewstatus()));
			}
		}
	}

	public void filterByLanguage(){
		for(int i=0;i<vTable.getDataTable().getRowCount();i++){
			Validation v = (Validation)vTable.getPagingScrollTable().getRowValue(i);
			for(int j=1;j<=3;j++)
			{
				Widget w = getLabelPanel(j, v, Style.Link);
				w.addStyleName("gwt-NoBorder");
				vTable.getDataTable().setWidget(i, j, w);
			}
		}
		
	}
	public static Widget makeUsers(String user, final String id, String style)
	{
		Label label = new Label(user);
		label.setStyleName(style);
		label.setTitle(user);
		label.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadUser(id);
			}
		});
		return label;
	}
	public static Widget getImagePanel(final int type, final String conceptURI, final String link, final String schemeURI, String style, final boolean isAddAction, final int tab, final int belongsToModule)
	{
		String imgURL = "images/spacer.gif";
		if(type==1)
		{
			if(conceptURI!=null)
			{
				if(conceptURI.startsWith(ModelConstants.ONTOLOGYBASENAMESPACE))
				{
					imgURL = "images/concept_logo.gif";
				}
				else
				{
					imgURL = "images/category_logo.gif";
				}
			}
			else
			{
				imgURL = "images/concept_logo.gif";
			}
		}
		else if(type==2){
			imgURL = "images/term-logo.gif";
		}
		else if(type==3){
			imgURL = "images/relationship-object-logo.gif";
		}
		else if(type==4){
			imgURL = "images/scheme-object-logo.gif";
		}
		else
			imgURL = "images/spacer.gif";
		Image image = new Image(imgURL);
		if(link!= null)
		{
			if(!link.equals(ModelConstants.COMMONBASENAMESPACE+ModelConstants.CDOMAINCONCEPT) && !link.equals(ModelConstants.CDOMAINCONCEPT) && !link.equals(ModelConstants.COMMONBASENAMESPACE+ModelConstants.CCATEGORY) && !link.equals(ModelConstants.CCATEGORY))
			{
				image.setStyleName(style);
				image.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						ModuleManager.gotoItem(link, schemeURI, isAddAction, tab, belongsToModule, type);
					}
				});
			}
		}
		return image;
	}
	public static Widget getLabelPanel(final int type, String text, String title, final String link, final String schemeURI, String style, final boolean isAddAction, final int tab, final int belongsToModule)
	{
		if(!text.equals(""))
		{
			HTML label = new HTML(" "+text);
			if(link!=null)
			{
				if(!link.equals(ModelConstants.COMMONBASENAMESPACE+ModelConstants.CDOMAINCONCEPT) && !link.equals(ModelConstants.CDOMAINCONCEPT) && !link.equals(ModelConstants.COMMONBASENAMESPACE+ModelConstants.CCATEGORY) && !link.equals(ModelConstants.CCATEGORY))
				{
					label.setStyleName(style);
					label.setTitle(title);
					label.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							ModuleManager.gotoItem(link, schemeURI, isAddAction, tab, belongsToModule, type);
						}
					});
				}
			}
			return label;
		}
		else
		{
			Image image = new Image("images/label-not-found.gif");
			image.setTitle(MainApp.constants.homeNoTerm());
			image.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					ModuleManager.gotoItem(link, schemeURI, isAddAction, tab, belongsToModule, type);
				}
			});
			return image;
		}
	}
	public static Widget getExtLinkLabelPanel(String text, String title, String link, String style)
	{
		if(!link.startsWith("http://"))
			link = "http://"+link;
		HTML label = new HTML("<a href=\""+link+"\" target=\"_blank\">"+text+"</a>");
			label.setStyleName(style);
			label.setTitle(title);
			return label;
	}
	public static HorizontalPanel makeConceptLabel(HashMap<String, TermObject> tObjList, String schemeURI, String style, final boolean isAddAction, final int tab, final int objectType, final int belongsToModule)
	{
		HorizontalPanel panel = new HorizontalPanel();
		if(tObjList!=null)
		{
			Iterator<String> itr = tObjList.keySet().iterator();
			while(itr.hasNext())
			{
				String key = (String)itr.next();
				TermObject tObj = (TermObject) tObjList.get(key);
				String language = tObj.getLang();
				if(MainApp.userSelectedLanguage.contains(language))
				{
					String newconceptLabel = "";
					if(panel.getWidgetCount()==0)
					{
						panel.add(getImagePanel(objectType, tObj.getConceptUri(), tObj.getConceptUri(), schemeURI, style, isAddAction, tab, belongsToModule));
						panel.add(new HTML("&nbsp;&nbsp;"));
					}
					else
					{
						newconceptLabel += ",";
					}
					newconceptLabel += tObj.getLabel()+" ("+tObj.getLang()+")";
					panel.add(getLabelPanel(objectType, newconceptLabel ,tObj.getConceptUri(), tObj.getConceptUri(), schemeURI, style, isAddAction, tab, belongsToModule));
				}
			}
			if(panel.getWidgetCount()<1)
			{
				panel.add(getImagePanel(objectType, "", null, schemeURI, style, isAddAction, tab, belongsToModule));
				panel.add(new HTML("&nbsp;&nbsp;"));				
				panel.add(getLabelPanel(objectType, "" ,"Not avaliable in selected language. ", null, schemeURI, style, isAddAction, tab, belongsToModule));
			}
		}
		else
			panel.add(new HTML("&nbsp;"));
		return panel;
	}
	public static HorizontalPanel makeTermLabel(TermObject tObjList, String schemeURI, String style, final boolean isAddAction, final int tab, final int objectType, int belongsToModule)
	{
		HorizontalPanel panel = new HorizontalPanel();
		if(tObjList!=null)
		{
			TermObject tObj = tObjList;
			String language = tObj.getLang();
			String newconceptLabel = "";
			if(MainApp.userSelectedLanguage.contains(language))
			{
				newconceptLabel = tObj.getLabel()+" ("+tObj.getLang()+")";
			}
			panel.add(getImagePanel(objectType, tObj.getConceptUri(), tObj.getConceptUri(), schemeURI, style, isAddAction, tab, belongsToModule));
			panel.add(new HTML("&nbsp;&nbsp"));
			panel.add(getLabelPanel(objectType, newconceptLabel ,tObj.getConceptUri(), tObj.getConceptUri(), schemeURI, style, isAddAction, tab, belongsToModule));
		}
		else
			panel.add(new HTML("&nbsp;"));
		return panel;
	}
	public static HorizontalPanel makeLabel(String text, String title, String link , String schemeURI, String style, final boolean isAddAction, final int tab, final int objectType, int belongsToModule)
	{
		HorizontalPanel panel = new HorizontalPanel();
		panel.add(getImagePanel(objectType, title, link, schemeURI, style, isAddAction, tab, belongsToModule));
		panel.add(new HTML("&nbsp;&nbsp"));
		panel.add(getLabelPanel(objectType, text ,title, link, schemeURI, style, isAddAction, tab, belongsToModule));
		return panel;
	}
	public static HorizontalPanel makeLabelOnly(String text, String title, String link , String schemeURI, String style, final boolean isAddAction, final int tab, final int objectType, boolean isSource, int belongsToModule)
	{
		HorizontalPanel panel = new HorizontalPanel();
		if(isSource)
			panel.add(getExtLinkLabelPanel(text ,title, link, style));
		else
			panel.add(getLabelPanel(objectType, text ,title, link, schemeURI, style, isAddAction, tab, belongsToModule));
		return panel;
	}
	public static String makeRelationshipLabel(RelationshipObject rObj)
	{
		ArrayList<LabelObject> labelList = rObj.getLabelList();
		String labelStr = "";
		for(int i=0;i<labelList.size();i++)
		{
			LabelObject labelObj = (LabelObject) labelList.get(i);
			String lang = labelObj.getLanguage();
			
			if(MainApp.userSelectedLanguage.contains(lang))
			{
				String label = labelObj.getLabel();
				if(labelStr.equals(""))
					labelStr += " "+label+" ("+lang+")";
				else
					labelStr += ", "+label+" ("+lang+")";
			}
		}
		return labelStr;
	}
	public static Widget makeLabelWithRelation(Validation v, String style, final boolean isAddAction, final int tab, final int objectType, boolean isOld, boolean isSource)
	{
		int belongsToModule = v.getConceptObject().getBelongsToModule();
		HorizontalPanel hp = new HorizontalPanel();
		ArrayList<LightEntity> list = new ArrayList<LightEntity>();
		if(isOld)
		{
			if(v.getOldRelationshipObject()!=null)
				hp.add(makeLabel(makeRelationshipLabel(v.getOldRelationshipObject()), v.getOldRelationshipObject().getUri(), v.getConceptObject().getUri(), v.getConceptObject().getScheme(), style, isAddAction, tab, 3, belongsToModule));
			if(v.getOldObject()!=null)
			{
				list = (ArrayList<LightEntity>) v.getOldObject();
				
			}
		}
		else
		{
			if(v.getNewRelationshipObject()!=null)
				hp.add(makeLabel(makeRelationshipLabel(v.getNewRelationshipObject()), v.getNewRelationshipObject().getUri(), v.getConceptObject().getUri(), v.getConceptObject().getScheme(), style, isAddAction, tab, 3, belongsToModule));
			if(v.getNewObject()!=null)
			{
				list = (ArrayList<LightEntity>) v.getNewObject();
			}
		}
		if(list.size()>0)
		{
			Object obj = list.get(0);
			if(obj instanceof ConceptObject)
			{
				ConceptObject cObj = (ConceptObject) obj;
				hp.add(makeConceptLabel((HashMap<String, TermObject>) cObj.getTerm(), cObj.getScheme(), style, isAddAction, tab, objectType, belongsToModule));
			}
			else if(obj instanceof TermObject)
			{
				TermObject tObj = (TermObject) obj;
				hp.add(makeLabel(tObj.getLabel()+checkNullValueInParenthesis(tObj.getLang()), tObj.getUri(), tObj.getConceptUri(), v.getConceptObject().getScheme(), style, isAddAction, tab, objectType,  belongsToModule));
			}
			else if(obj instanceof SchemeObject)
			{
				SchemeObject sObj = (SchemeObject) obj;
				hp.add(makeLabel(sObj.getSchemeLabel()+checkNullValueInParenthesis("en"), sObj.getDescription(), sObj.getSchemeInstance(), sObj.getSchemeInstance(), style, isAddAction, tab, objectType,  belongsToModule));
			}
			else if(obj instanceof AttributesObject)
			{
				AttributesObject aObj = (AttributesObject) obj;
				RelationshipObject rObj = aObj.getRelationshipObject();
				NonFuncObject nfObj = aObj.getValue();
				String label = "";
				if(rObj!=null)
					label += LazyLoadingTree.getRelationshipLabel(rObj, MainApp.constants.mainLocale())+" : "; 
				if(nfObj!=null)
					label += nfObj.getValue() + checkNullValueInParenthesis(nfObj.getLanguage());
				hp.add(makeLabelOnly(label, v.getConceptObject().getUri(), v.getConceptObject().getUri(), v.getConceptObject().getScheme(), style, isAddAction, tab, objectType, isSource,  belongsToModule));
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
		}
		if(hp.getWidgetCount()<1)
			hp.add(new HTML("&nbsp;"));
		return hp;
	}
	public static Widget getLabelPanel(int col, Validation v, String style)
	{

		switch (v.getAction()) {

		 case 1:     //     "concept-create"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.term, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.term, 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.term, 1, true, false);
			 break;
	
		 case 2:     //     "concept-delete"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, false, InfoTab.term, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, false, InfoTab.term, 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, false, InfoTab.term, 1, true, false);
			 break;
	
		 case 3:     //     "concept-relationship-create"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.relationship, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.relationship, 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.relationship, 1, true, false);
			 break;
	
		 case 4:     //     "concept-relationship-edit"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.relationship, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.relationship, 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.relationship, 1, true, false);
			 break;
	
		 case 5:     //     "concept-relationship-delete"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.relationship, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.relationship, 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.relationship, 1, true, false);
			 break;
	
		 case 6:     //     "term-create"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.term, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.term, 2, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.term, 2, true, false);	
			 break;
	
		 case 7:     //     "term-edit"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.term, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.term, 2, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.term, 2, true, false);
			 break;
	
		 case 8:     //     "term-delete"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.term, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.term, 2, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.term, 2, true, false);
			 break;
	
		 case 9:     //     "term-relationship-add"
			 if(col==0) return makeTermLabel(v.getTermObject(), v.getConceptObject().getScheme(),style, true, InfoTab.term, 2, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.term, 2, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.term, 2, true, false);
			 break;
	
		 case 10:     //     "term-relationship-edit"
			 if(col==0) return makeTermLabel(v.getTermObject(), v.getConceptObject().getScheme(),style, true, InfoTab.term, 2, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.term, 2, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.term, 2, true, false);
			 break;
	
		 case 11:     //     "term-relationship-delete"
			 if(col==0) return makeTermLabel(v.getTermObject(), v.getConceptObject().getScheme(),style, true, InfoTab.term, 2, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.term, 2, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.term, 2, true, false);
			 break;
	
		 case 12:     //     "term-note-create"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.term, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.term, 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.term, 1, true, false);
			 break;
	
		 case 13:     //     "term-note-edit"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.term, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.term, 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.term, 1, true, false);
			 break;
	
		 case 14:     //     "term-note-delete"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.term, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.term, 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.term, 1, true, false);
			 break;
	
		 case 15:     //     "term-attribute-create"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.term, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.term, 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.term, 1, true, false);
			 break;
	
		 case 16:     //     "term-attribute-edit"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.term, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.term, 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.term, 1, true, false);
			 break;
	
		 case 17:     //     "term-attribute-delete"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.term, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.term, 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.term, 1, true, false);
			 break;
	
		 case 18:     //     "concept-edit-note-create"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.note, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.note, 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.note, 1, true, false);
			 break;
	
		 case 19:     //     "concept-edit-note-edit"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.note, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.note, 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.note, 1, true, false);
			 break;
	
		 case 20:     //     "concept-edit-note-delete"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.note, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.note, 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.note, 1, true, false);
			 break;
	
		 case 21:     //     "concept-edit-definition-create"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.definition, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.definition, 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.definition, 1, true, false);
			 break;
	
		 case 22:     //     "concept-edit-definition-translation-edit"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.definition, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.definition, 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.definition, 1, true, false);
			 break;
	
		 case 23:     //     "concept-edit-definition-delete"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.definition, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.definition, 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.definition, 1, true, false);
			 break;
		 
		 case 24:     //     "concept-edit-image-create"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.image, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.image, 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.image, 1, true, false);
			 break;
		 
		 case 25:     //     "concept-edit-image-edit"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.image, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.image, 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.image, 1, true, false);
			 break;
		 
		 case 26:     //     "concept-edit-image-delete"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.image, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.image, 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.image, 1, true, false);
			 break;
			 
		 case 27:     //     "concept-edit-image-translation-create"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.image, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.image, 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.image, 1, true, false);
			 break;
			 
		 case 28:     //     "concept-edit-image-translation-delete"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.image, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.image, 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.image, 1, true, false);
			 break;
			 
		 case 29:     //     "concept-edit-definition-translation-create"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.definition, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.definition, 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.definition, 1, true, false);
			 break;
			 
		 case 30:     //     "concept-edit-definition-translation-delete"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.definition, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.definition, 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.definition, 1, true, false);
			 break;
			 
		 case 31:     //     "concept-edit-ext-source-create"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.definition, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.definition, 1, false, true);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.definition, 1, true, true);
			 break;
			 
		 case 32:     //     "concept-edit-ext-source-edit"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.definition, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.definition, 1, false, true);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.definition, 1, true, true);
			 break;
			 
		 case 33:     //     "concept-edit-ext-source-delete"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.definition, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.definition, 1, false, true);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.definition, 1, true, true);
			 break;
			 
		 case 34:     //     "concept-edit-image-source-create"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.image, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.image, 1, false, true);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.image, 1, true, true);
			 break;
			 
		 case 35:     //     "concept-edit-image-source-edit"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.image, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.image, 1, false, true);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.image, 1, true, true);
			 break;
			 
		 case 36:     //     "concept-edit-image-source-delete"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.image, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.image, 1, false, true);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.image, 1, true, true);
			 break;
			 
		 case 37:     //     "concept-edit-attribute-create"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.note, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.note, 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.note, 1, true, false);
			 break;
	
		 case 38:     //     "concept-edit-attribute-edit"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.note, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.note, 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.note, 1, true, false);
			 break;
	
		 case 39:     //     "concept-edit-attribute-delete"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.note, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.note, 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.note, 1, true, false);
			 break;
		 case 40:     //     "scheme-create"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.note, 4, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.note, 4, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.note, 4, true, false);
			 break;
		 case 41:     //     ""mapping-create"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.note, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.note, 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.note, 1, true, false);
			 break;
		 case 42:     //     "mapping-delete"
			 if(col==0) return makeConceptLabel((HashMap<String, TermObject>) v.getConceptObject().getTerm(), v.getConceptObject().getScheme(), style, true, InfoTab.note, 1, v.getConceptObject().getBelongsToModule());
			 if(col==1) return makeLabelWithRelation(v, style, true, InfoTab.note, 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, InfoTab.note, 1, true, false);
			 break;
		 default: 
			 GWT.log("Invalid Action.", null);	
		 	break;
	    }
		return new HTML("");
	}

}
