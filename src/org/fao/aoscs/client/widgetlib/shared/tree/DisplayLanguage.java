package org.fao.aoscs.client.widgetlib.shared.tree;

import java.util.ArrayList;

import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.RelationshipTreeObject;
import org.fao.aoscs.domain.SchemeObject;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
import com.google.gwt.widgetideas.client.FastTree;

public class DisplayLanguage {
	
	public static void executeCategory(final FastTree tree, final ArrayList<String> language, final boolean showAlsoNonpreferredTerms, final SchemeObject sObj){
		final ArrayList<ConceptObject> startList = new ArrayList<ConceptObject>();
		Scheduler.get().scheduleDeferred(new Command(){
			public void execute() {
				for (int i = 0; i < tree.getItemCount(); i++) {
					Object obj = tree.getItem(i);
					if (obj instanceof TreeItemAOS) {
						TreeItemAOS item = (TreeItemAOS) obj;
						startList.add((ConceptObject)item.getValue());
						executeCategoryChildItem(item, language, showAlsoNonpreferredTerms, sObj);
					}
				}
				LazyLoadingTree.addTreeItems(tree, startList, showAlsoNonpreferredTerms, sObj);
			}
		});
		
		
	}
	
	public static void executeCategoryChildItem(final TreeItemAOS item, final ArrayList<String> language, final boolean showAlsoNonpreferredTerms, final SchemeObject sObj){
		final ArrayList<ConceptObject> startList = new ArrayList<ConceptObject>();
		Scheduler.get().scheduleDeferred(new Command(){
			public void execute() {
				for (int i = 0; i < item.getChildCount(); i++) {
					Object obj = item.getChild(i);
					if (obj instanceof TreeItemAOS) {
						TreeItemAOS childItem = (TreeItemAOS) obj;
						startList.add((ConceptObject)childItem.getValue());
						executeCategoryChildItem(childItem, language, showAlsoNonpreferredTerms, sObj);
					}
				}
				LazyLoadingTree.addTreeItems(item, startList, showAlsoNonpreferredTerms, sObj);
			}
		});
	}
	
	public static void executeRelation(final FastTree tree, final ArrayList<String> language, final RelationshipTreeObject rtObj){
		final ArrayList<RelationshipObject> startList = new ArrayList<RelationshipObject>();
		Scheduler.get().scheduleDeferred(new Command(){
			public void execute() {
				for (int i = 0; i < tree.getItemCount(); i++) {
					Object obj = tree.getItem(i);
					if (obj instanceof TreeItemAOS) {
						TreeItemAOS item = (TreeItemAOS) obj;
						startList.add((RelationshipObject)item.getValue());
						executeRelationChildItem(item, language, rtObj);
					}
				}
				LazyLoadingTree.addTreeItems(tree, startList, rtObj);
			}
		});
	}
	
	
	
	public static void executeRelationChildItem(final TreeItemAOS item, final ArrayList<String> language, final RelationshipTreeObject rtObj){
		final ArrayList<RelationshipObject> startList = new ArrayList<RelationshipObject>();
		Scheduler.get().scheduleDeferred(new Command(){
			public void execute() {
				for (int i = 0; i < item.getChildCount(); i++) {
					Object obj = item.getChild(i);
					if (obj instanceof TreeItemAOS) {
						TreeItemAOS childItem = (TreeItemAOS) obj;
						startList.add((RelationshipObject)childItem.getValue());
						executeRelationChildItem(childItem, language, rtObj);
					}
				}
				LazyLoadingTree.addTreeItems(item, startList, rtObj);
			}
		});
	}
	
	/*public static void executeRelation(final Tree tree, final ArrayList<String> language, final RelationshipTreeObject rtObj){
		final ArrayList<RelationshipObject> startList = new ArrayList<RelationshipObject>();
		Scheduler.get().scheduleDeferred(new Command(){
			public void execute() {
				for (int i = 0; i < tree.getItemCount(); i++) {
					Object obj = tree.getItem(i);
					if (obj instanceof VtreeItem) {
						VtreeItem item = (VtreeItem) obj;
						startList.add((RelationshipObject)item.getValue());
						executeRelationChildItem(item, language, rtObj);
					}
				}
				LazyLoadingTree.addTreeItems(tree, startList, rtObj);
			}
		});
	}
	
	
	
	public static void executeRelationChildItem(final VtreeItem item, final ArrayList<String> language, final RelationshipTreeObject rtObj){
		final ArrayList<RelationshipObject> startList = new ArrayList<RelationshipObject>();
		Scheduler.get().scheduleDeferred(new Command(){
			public void execute() {
				for (int i = 0; i < item.getChildCount(); i++) {
					Object obj = item.getChild(i);
					if (obj instanceof VtreeItem) {
						VtreeItem childItem = (VtreeItem) obj;
						startList.add((RelationshipObject)childItem.getValue());
						executeRelationChildItem(childItem, language, rtObj);
					}
				}
				LazyLoadingTree.addTreeItems(item, startList, rtObj);
			}
		});
	}*/
	
}
