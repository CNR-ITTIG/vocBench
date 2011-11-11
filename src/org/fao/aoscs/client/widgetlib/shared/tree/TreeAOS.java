package org.fao.aoscs.client.widgetlib.shared.tree;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.domain.ConceptObject;

import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.widgetideas.client.FastTree;
import com.google.gwt.widgetideas.client.FastTreeItem;

public class TreeAOS extends FastTree{
	
	private int type;
	public static int TYPE_CONCEPT = 1;
	public static int TYPE_CATEGORY = 2;
	public static int TYPE_RELATIONSHIP = 3;
	public static int TYPE_CONCEPT_BROWSER = 4;
	public static int TYPE_RELATIONSHIP_BROWSER = 5;
	public static int TYPE_SUBVOCABULARY_BROWSER = 6;
	public static int TYPE_ClASS = 7;
	public static int TYPE_VALIDATON_FILTER = 8;
	
	public TreeAOS (int type){
		this.type = type;
		FastTree.addDefaultCSS();
		StyleInjector.inject(".gwt-FastTree .selected .treeItemContent{	background-color: #93C2F1}");
	}
	
	public void addItem(FastTreeItem treeitem)
	{
		boolean chk = true;
		if(treeitem instanceof TreeItemAOS) 
		{
			TreeItemAOS vtreeitem = (TreeItemAOS) treeitem;
			Object obj = vtreeitem.getValue();
			if(obj instanceof ConceptObject)
			{
				ConceptObject cObj = (ConceptObject) obj;
				chk = MainApp.checkDeprecated(cObj.getStatus());
			}
		}
		if(chk)	super.addItem(treeitem);
		
	}
	
	
	public void setTreeState(boolean open)
	{
		for(int i=0;i<this.getItemCount();i++)
		{
			FastTreeItem treeItem = this.getItem(i);
			treeItem.setState(open);
			setTreeState(treeItem, open);
			
		}
	}
	
	private void setTreeState(FastTreeItem treeItem, boolean open)
	{
		
		for(int j=0;j<treeItem.getChildCount();j++)
		{
			FastTreeItem childItem = treeItem.getChild(j);
			childItem.setState(open);
			setTreeState(childItem, open);
		}
	}


	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}


	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
}
