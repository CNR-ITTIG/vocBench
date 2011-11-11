package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

public class ConceptDetailObject extends LightEntity{

	private static final long serialVersionUID = -3207867800157365870L;
	
	private ConceptObject conceptObject =  new ConceptObject();
	private ConceptTermObject conceptTermObject =  new ConceptTermObject();
	private InformationObject informationObject =  new InformationObject();
	private ImageObject imageObject =  new ImageObject();
	private DefinitionObject definitionObject = new DefinitionObject();
	private HierarchyObject hierarchyObject = new HierarchyObject();
	private RelationObject relationObject =  new RelationObject();
	private ConceptMappedObject conceptMappedObject =  new ConceptMappedObject();
	private HashMap<RelationshipObject, ArrayList<NonFuncObject>> attributeObject = new HashMap<RelationshipObject, ArrayList<NonFuncObject>>();
	private HashMap<RelationshipObject, ArrayList<NonFuncObject>> noteObject = new HashMap<RelationshipObject, ArrayList<NonFuncObject>>();
	
	private int termCount = 0;
	private int definitionCount = 0;
	private int relationCount = 0;
	private int conceptMappedCount = 0;
	private int attributeCount = 0;
	private int noteCount = 0;
	private int imageCount = 0;
	private int historyCount = 0;

	/**
	 * @return the conceptObject
	 */
	public ConceptObject getConceptObject() {
		return conceptObject;
	}
	/**
	 * @return the conceptTermObject
	 */
	public ConceptTermObject getConceptTermObject() {
		return conceptTermObject;
	}
	/**
	 * @return the informationObject
	 */
	public InformationObject getInformationObject() {
		return informationObject;
	}
	/**
	 * @return the imageObject
	 */
	public ImageObject getImageObject() {
		return imageObject;
	}
	/**
	 * @return the definitionObject
	 */
	public DefinitionObject getDefinitionObject() {
		return definitionObject;
	}
	/**
	 * @return the relationObject
	 */
	public RelationObject getRelationObject() {
		return relationObject;
	}
	/**
	 * @return the conceptMappedObject
	 */
	public ConceptMappedObject getConceptMappedObject() {
		return conceptMappedObject;
	}
	/**
	 * @param conceptObject the conceptObject to set
	 */
	public void setConceptObject(ConceptObject conceptObject) {
		this.conceptObject = conceptObject;
	}
	/**
	 * @param conceptTermObject the conceptTermObject to set
	 */
	public void setConceptTermObject(ConceptTermObject conceptTermObject) {
		this.conceptTermObject = conceptTermObject;
	}
	/**
	 * @param informationObject the informationObject to set
	 */
	public void setInformationObject(InformationObject informationObject) {
		this.informationObject = informationObject;
	}
	/**
	 * @param imageObject the imageObject to set
	 */
	public void setImageObject(ImageObject imageObject) {
		this.imageObject = imageObject;
	}
	/**
	 * @param definitionObject the definitionObject to set
	 */
	public void setDefinitionObject(DefinitionObject definitionObject) {
		this.definitionObject = definitionObject;
	}
	/**
	 * @param relationObject the relationObject to set
	 */
	public void setRelationObject(RelationObject relationObject) {
		this.relationObject = relationObject;
	}
	/**
	 * @param conceptMappedObject the conceptMappedObject to set
	 */
	public void setConceptMappedObject(ConceptMappedObject conceptMappedObject) {
		this.conceptMappedObject = conceptMappedObject;
	}
	/**
	 * @param attributeObject the attributeObject to set
	 */
	public void setAttributeObject(HashMap<RelationshipObject, ArrayList<NonFuncObject>> attributeObject) {
		this.attributeObject = attributeObject;
	}
	/**
	 * @return the attributeObject
	 */
	public HashMap<RelationshipObject, ArrayList<NonFuncObject>> getAttributeObject() {
		return attributeObject;
	}
	public void setHierarchyObject(HierarchyObject hierarchyObject) {
		this.hierarchyObject = hierarchyObject;
	}
	public HierarchyObject getHierarchyObject() {
		return hierarchyObject;
	}
	/**
	 * @param noteObject the noteObject to set
	 */
	public void setNoteObject(HashMap<RelationshipObject, ArrayList<NonFuncObject>> noteObject) {
		this.noteObject = noteObject;
	}
	/**
	 * @return the noteObject
	 */
	public HashMap<RelationshipObject, ArrayList<NonFuncObject>> getNoteObject() {
		return noteObject;
	}
	
	public int getTermCount() {
		return termCount;
	}
	public void setTermCount(int termCount) {
		this.termCount = termCount;
	}
	public int getDefinitionCount() {
		return definitionCount;
	}
	public void setDefinitionCount(int definitionCount) {
		this.definitionCount = definitionCount;
	}
	public int getRelationCount() {
		return relationCount;
	}
	public void setRelationCount(int relationCount) {
		this.relationCount = relationCount;
	}
	public int getConceptMappedCount() {
		return conceptMappedCount;
	}
	public void setConceptMappedCount(int conceptMappedCount) {
		this.conceptMappedCount = conceptMappedCount;
	}
	public int getAttributeCount() {
		return attributeCount;
	}
	public void setAttributeCount(int attributeCount) {
		this.attributeCount = attributeCount;
	}
	public int getNoteCount() {
		return noteCount;
	}
	public void setNoteCount(int noteCount) {
		this.noteCount = noteCount;
	}
	public int getImageCount() {
		return imageCount;
	}
	public void setImageCount(int imageCount) {
		this.imageCount = imageCount;
	}
	public void setHistoryCount(int historyCount) {
		this.historyCount = historyCount;
	}
	public int getHistoryCount() {
		return historyCount;
	}
}

