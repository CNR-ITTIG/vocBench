package org.fao.aoscs.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;

import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.gwt.GwtConfigurationHelper;
import net.sf.gilead.gwt.PersistentRemoteService;

import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.client.widgetlib.shared.tree.service.TreeService;
import org.fao.aoscs.domain.NtreeItemObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.SubVocabObject;
import org.fao.aoscs.domain.TreeObject;
import org.fao.aoscs.domain.TreePathObject;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.server.protege.ProtegeModelFactory;
import org.fao.aoscs.server.protege.ProtegeWorkbenchUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSNames;

public class TreeServiceImpl extends PersistentRemoteService  implements TreeService{
	
	private static final long serialVersionUID = -821892565268959754L;
	protected static Logger logger = LoggerFactory.getLogger(TreeServiceImpl.class);

	// -------------------------------------------------------------------------
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
	
	public ArrayList<TreeObject> getTreeObject(String rootNode, OntologyInfo ontoInfo, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList) {
		logger.debug("getTreeObject(" + rootNode + ", " + isHideDeprecated + ", "
				+ langList + ")");
		Date d = new Date();
		logger.debug("Starting to get data from mysql ");
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		logger.debug("Time to load owl model : "+(new Date().getTime()-d.getTime())/1000+" secs");
		ArrayList<TreeObject> treeObjList = getTreeObject(owlModel, rootNode, showAlsoNonpreferredTerms, isHideDeprecated, langList);
		logger.debug("Time to load owl model : "+(new Date().getTime()-d.getTime())/1000+" secs");
		///owlModel.dispose();
		logger.debug("Time to get data from mysql : "+(new Date().getTime()-d.getTime())/1000+" secs");
		return treeObjList;
	}
	
	public ArrayList<TreeObject> getTreeObject(OWLModel owlModel, String rootNode, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList){
		logger.debug("getTreeObject(" + rootNode + ", " + isHideDeprecated + ", "
				+ langList + ")");
		ArrayList<TreeObject> treeObjList = new ArrayList<TreeObject>();
		final HashMap<String, TreeObject> cList = new HashMap<String, TreeObject>();
		final HashMap<String, TreeObject> emptycList = new HashMap<String, TreeObject>();
		OWLNamedClass rootCls = owlModel.getOWLNamedClass(rootNode);
	    if(rootCls !=null){
			for(Iterator it = rootCls.getSubclasses(false).iterator(); it.hasNext();) {
				OWLNamedClass cls = (OWLNamedClass) it.next();
				TreeObject treeObj = ProtegeWorkbenchUtility.makeTreeObject(owlModel, cls, showAlsoNonpreferredTerms, isHideDeprecated, langList);
				if(treeObj!=null)
				{
					if(treeObj.getLabel().startsWith("###EMPTY###"))
						emptycList.put(treeObj.getLabel(), treeObj);
					else
						cList.put(treeObj.getLabel(), treeObj);
				}
			}
			List<String> labelKeys = new ArrayList<String>(cList.keySet()); 
			Collections.sort(labelKeys, String.CASE_INSENSITIVE_ORDER);
			
			for (Iterator<String> itr = labelKeys.iterator(); itr.hasNext();){ 
    			treeObjList.add(cList.get(itr.next()));
            }
			for (Iterator<String> itr = emptycList.keySet().iterator(); itr.hasNext();){ 
    			treeObjList.add(emptycList.get(itr.next()));
            }
		}
	    return treeObjList ;
	}
	
	public  SubVocabObject getSubVocabInfo(String GeoRootNode, String SciRootNode, OntologyInfo ontoInfo, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList)
	{
		logger.debug("getSubVocabInfo(" + GeoRootNode + ", " + SciRootNode + ", " 
				+ isHideDeprecated + ", " + langList + ")");
		SubVocabObject obj = new SubVocabObject();
		try
		{
			OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
			obj.setGeoConcept(getTreeObject(owlModel, GeoRootNode, showAlsoNonpreferredTerms, isHideDeprecated, langList));
			obj.setSciConcept(getTreeObject(owlModel, SciRootNode, showAlsoNonpreferredTerms, isHideDeprecated, langList));
			///owlModel.dispose();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return obj;
	}
	
	private NtreeItemObject createNtreeObject(OWLModel owlModel,OWLNamedClass owlCls, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList){
		logger.debug("createNtreeObject(" + owlCls + ", " + isHideDeprecated
				+ ", " + langList + ")");
		NtreeItemObject ntObj = new NtreeItemObject();
		TreeObject cObj  = ProtegeWorkbenchUtility.makeTreeObject(owlModel, owlCls, showAlsoNonpreferredTerms, isHideDeprecated, langList);
		ntObj.setTreeObject(cObj);
	
		RDFProperty subClassOfProperty = owlModel.getRDFProperty(RDFSNames.Slot.SUB_CLASS_OF);
		Collection subClasses = owlModel.getRDFResourcesWithPropertyValue(subClassOfProperty, owlCls);
		for (Iterator iter = subClasses.iterator(); iter.hasNext();) {
			OWLNamedClass cls = (OWLNamedClass) iter.next();
			TreeObject childObj  =  ProtegeWorkbenchUtility.makeTreeObject(owlModel, cls, showAlsoNonpreferredTerms, isHideDeprecated, langList);
			ntObj.addChild(childObj);
		}
		return ntObj;
	}

	private ArrayList getSuperClassByOrder(OWLModel owlModel, String rootConcept) {
		logger.debug("getSuperClassByOrder(" + rootConcept + ")");
		ArrayList list = new ArrayList();
		//OWLNamedClass cdomain = owlModel.getOWLNamedClass(ModelConstants.CDOMAINCONCEPT);
		OWLNamedClass cdomain = owlModel.getOWLNamedClass(rootConcept);
		Collection coll = cdomain.getSubclasses(false);
		for (Iterator iter = coll.iterator(); iter.hasNext();) {
			OWLNamedClass cls = (OWLNamedClass) iter.next();
			list.add(cls.getURI());
		}
		return list;
	}
	
	public ArrayList loadList(OWLNamedClass dCls, OWLModel owlModel, ArrayList list, String rootConcept, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList)
	{
		logger.debug("loadList(" + dCls + ", " + list + ", " + rootConcept + ", " 
				+ isHideDeprecated + ", " + langList + ")");
		Collection c = dCls.getSuperclasses(false);
		for (Iterator iter = c.iterator(); iter.hasNext();) {
			OWLNamedClass element = (OWLNamedClass) iter.next();
			if(!element.getName().equals(rootConcept) && !element.getName().equals(ModelConstants.CDOMAINCONCEPT) && !element.getName().equals("owl:Thing") && !element.getName().equals(ModelConstants.CCATEGORY)) {
				list.add(createNtreeObject(owlModel, element, showAlsoNonpreferredTerms, isHideDeprecated, langList));
				loadList(element, owlModel, list, rootConcept, showAlsoNonpreferredTerms, isHideDeprecated, langList);	
			}
		}
		return list;
	}
	
	public TreePathObject getTreePath(String targetItem, String rootConcept, OntologyInfo ontoInfo, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList){
		logger.debug("getTreePath(" + targetItem + ", " + rootConcept + ", " + isHideDeprecated + ", " + langList + ")" );
		TreePathObject tpObj = new TreePathObject();
		ArrayList list = new ArrayList();
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass dCls = owlModel.getOWLNamedClass(ProtegeWorkbenchUtility.getNameFromURI(owlModel, targetItem));
		if(dCls!=null){
			list = loadList(dCls, owlModel, list, rootConcept, showAlsoNonpreferredTerms, isHideDeprecated, langList);
			/*Collection c = dCls.getSuperclasses(true);
			for (Iterator iter = c.iterator(); iter.hasNext();) {
				OWLNamedClass element = (OWLNamedClass) iter.next();
				if(!element.getName().equals(ModelConstants.CDOMAINCONCEPT) && !element.getName().equals("owl:Thing") && !element.getName().equals(ModelConstants.CCATEGORY)) {
					list.add(createNtreeObject(owlModel, element));
				
				}
			}*/
			
			if(list.isEmpty()){
				list.add(createNtreeObject(owlModel, dCls, showAlsoNonpreferredTerms, isHideDeprecated, langList));
			}
			
			if(!list.isEmpty()){
				ArrayList rootList = getSuperClassByOrder(owlModel, rootConcept);
				for (int i = 0; i < list.size(); i++) {
					NtreeItemObject nObj = (NtreeItemObject) list.get(i);
					tpObj.addItemList(nObj);
					if(rootList.contains(nObj.getURI())){
						tpObj.setRootItem(nObj);
					}
				}
			}
		}
		///owlModel.dispose();
		return tpObj;
	}

}
