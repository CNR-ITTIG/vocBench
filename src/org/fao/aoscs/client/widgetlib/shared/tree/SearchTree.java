package org.fao.aoscs.client.widgetlib.shared.tree;

import java.util.ArrayList;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.TreeObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.widgetideas.client.FastTree;
import com.google.gwt.widgetideas.client.FastTreeItem;

public class SearchTree {
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	public static void searchNReplace(FastTree tree,TreeItemAOS item,String targetItem)
	{		
		ArrayList<TreeItemAOS> itemSet = new ArrayList<TreeItemAOS>();
		for(int i=0;i<tree.getItemCount();i++){
			TreeItemAOS vItem = (TreeItemAOS) tree.getItem(i);
			itemSet.add(vItem);
		}
		tree.clear();
		
		boolean found = false;
		for(int i=0;i<itemSet.size();i++){
			TreeItemAOS vItem = (TreeItemAOS) itemSet.get(i);
			if(((TreeObject)item.getValue()).getUri().equals(((TreeObject)vItem.getValue()).getUri())){
				itemSet.set(i, item);
				found = true;
			}
		}
	
		if(!found){
			itemSet.add(item);
		}
		
		for(int i=0;i<itemSet.size();i++){
			tree.addItem((TreeItemAOS) itemSet.get(i));
		}
		setSelected(tree,targetItem);
	}
	/**find item in tree */
	public static void setSelected(FastTree tree,String targetItem){
		for(int i=0;i<tree.getItemCount();i++){
			search(tree,targetItem,(TreeItemAOS) tree.getItem(i));
		}
		
		if(tree.getSelectedItem() == null || !((TreeObject)((TreeItemAOS)tree.getSelectedItem()).getValue()).getUri().equals(targetItem)){
			Window.alert(constants.sharedConceptNotFound());
		}
		
	}
//	 recursive function use with setSelected
	public static boolean search(FastTree tree,String targetItem,TreeItemAOS item){
		boolean founded = false;
		// do action 
		if(((TreeObject)item.getValue()).getUri().equals(targetItem)){
			founded = true;
			tree.setSelectedItem(item);
			tree.ensureSelectedItemVisible();
			item.getTree().ensureSelectedItemVisible();
			item.setState(true);
			//item.onSelected();
			// open state
			while(item!=null){
				item = (TreeItemAOS) item.getParentItem();
				if(item!=null){
					item.setState(true);
				}
			}
			return true;
		}
		// recursion
		if(!founded){
			for(int i=0;i<item.getChildCount();i++){
				Object obj = item.getChild(i);
				if (obj instanceof TreeItemAOS) {
					TreeItemAOS childItem = (TreeItemAOS) obj;
					if(childItem!=null){
						boolean chk = search(tree,targetItem,childItem);
						if(chk) return chk;
					}
				}
			}
		}
		return false;
	}
	
	public static void addTargetItem(FastTreeItem selectedItem, Object obj, String parentItemURI, String position, boolean showAlsoNonpreferredTerms){

		if(selectedItem!=null)
		{
			String topLevelItemURI = "";
			TreeItemAOS item = null;
			
			TreeAOS tree = (TreeAOS) selectedItem.getTree();
			if(obj instanceof RelationshipObject)
			{
				RelationshipObject rObj = (RelationshipObject) obj;
				item = new TreeItemAOS(rObj, LazyLoadingTree.getRelationshipLabel(rObj));
				topLevelItemURI = getTopLevelItemURI(rObj.getType());
			}
			else if(obj instanceof TreeObject)
			{
				TreeObject tObj = (TreeObject) obj;
				item = new TreeItemAOS(tObj, showAlsoNonpreferredTerms);
				topLevelItemURI = getTopLevelItemURI(ModelConstants.CDOMAINCONCEPT);
			}
			if(item!=null)
			{
				if(position.equals("sameLevel")){
					if(parentItemURI.equals(topLevelItemURI))
					{
						tree.addItem(item);
						tree.setSelectedItem(item, false);
						item.onSelected();
					}
				}
				else
				{
					tree.setSelectedItem(null);
					selectedItem.addItem(item);
					selectedItem.setState(true, false);
					tree.setSelectedItem(item, false);
					item.onSelected();
				}
			
			}
		}
	}
	
	public static void addTargetItem(FastTreeItem selectedItem, Object obj, String parentItemURI, String position){
		addTargetItem(selectedItem, obj, parentItemURI, position, false);
	}
	
	public static void editTargetItem(FastTreeItem selectedItem, Object obj){
		editTargetItem(selectedItem, obj, false);
	}
	
	public static void editTargetItem(FastTreeItem selectedItem, Object obj, boolean showAlsoNonpreferredTerms){
		TreeItemAOS newSelectedItem = null;
		TreeAOS tree = (TreeAOS) selectedItem.getTree();
		if(obj instanceof RelationshipObject)
		{
			RelationshipObject rObj = (RelationshipObject) obj;
			newSelectedItem = new TreeItemAOS(rObj, LazyLoadingTree.getRelationshipLabel(rObj));
		}
		else if(obj instanceof TreeObject)
		{
			TreeObject tObj = (TreeObject) obj;
			newSelectedItem = new TreeItemAOS(tObj, showAlsoNonpreferredTerms);
		}
		
		if(selectedItem!=null)
		{
			tree.setSelectedItem(null);
			TreeItemAOS parentItem = (TreeItemAOS) selectedItem.getParentItem();
			if(parentItem!=null)
			{
				parentItem.removeItem(selectedItem);
				parentItem.addItem(newSelectedItem);
			}
			else
			{
				tree.removeItem(selectedItem);
				tree.addItem(newSelectedItem);
			}
			selectedItem.setState(true, false);
			tree.setSelectedItem(newSelectedItem);
			newSelectedItem.onSelected();
		}
	}
	
	public static void deleteTargetItem(FastTreeItem fastTreeItem){
	    
		//TreeItemAOS selectedItem = searchTree(tree, itemURI);
		TreeAOS tree = (TreeAOS) fastTreeItem.getTree();
		if(fastTreeItem!=null)
		{
			tree.setSelectedItem(null);
			TreeItemAOS parentItem = (TreeItemAOS) fastTreeItem.getParentItem();
			if(parentItem!=null)
			{
				parentItem.removeItem(fastTreeItem);
				if(parentItem.getChildCount()<1)
				{
					TreeItemAOS grandparentItem = (TreeItemAOS) parentItem.getParentItem();
					if(grandparentItem!=null)
					{
						grandparentItem.removeItem(parentItem);
						grandparentItem.addItem(parentItem);
					}
					else
					{
						tree.removeItem(parentItem);
						tree.addItem(parentItem);
					}

				}
				tree.setSelectedItem(parentItem);
			}
			fastTreeItem.remove();
		}
	}
	
	/*private static TreeItemAOS searchTree(TreeAOS tree, String itemURI)
	{
		TreeItemAOS selectedItem = null;
		for(int i=0; i<tree.getItemCount() ;i++)
		{
			selectedItem = searchItem((TreeItemAOS) tree.getItem(i), itemURI);
			if(selectedItem!=null)	break;
		}
		return selectedItem;
	}
	
	private static TreeItemAOS searchItem(TreeItemAOS item, String itemURI){

		TreeItemAOS selectedItem = null;
		Object obj = item.getValue();
		String itemStr = "";
		if(obj instanceof RelationshipObject)
		{
			RelationshipObject rObj = (RelationshipObject) obj;
			itemStr = rObj.getUri();
		}
		else if(obj instanceof TreeObject)
		{
			TreeObject tObj = (TreeObject) obj;
			itemStr = tObj.getUri();		
		}
		else if(obj instanceof ConceptObject)
		{
			ConceptObject cObj = (ConceptObject) obj;
			itemStr = cObj.getUri();		
		}	
		
		if(itemStr.equals(itemURI)){
			selectedItem = item;
		}
		else
		{
			for(int i=0;i<item.getChildCount();i++){
				selectedItem = searchItem((TreeItemAOS) item.getChild(i), itemURI);
				if(selectedItem!=null)	break;
			}
		}
		return selectedItem;
	}*/
	
	public static String getTopLevelItemURI(String type)
	{
		if(type.equals(RelationshipObject.OBJECT))
		{
			return ModelConstants.COMMONBASENAMESPACE+ModelConstants.RWBOBJECTPROPERTY;
		}
		else if(type.equals(RelationshipObject.OBJECT))
		{
			return ModelConstants.COMMONBASENAMESPACE+ModelConstants.RWBDATATYPEPROPERTY;
		}
		else
			return null;
	}
	
}
