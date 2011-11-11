package org.fao.aoscs.server.export;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.LabelObject;
import org.fao.aoscs.domain.RelationObject;

public class EConceptObject {
	private ArrayList<String> scheme = new ArrayList<String>();
	private String conceptURI;
	private String conceptName;
	private String conceptIns;
	private ArrayList<LabelObject> scopeNoteList = new ArrayList<LabelObject>();
	private HashMap<String,ETermObject> term = new HashMap<String,ETermObject>();
	private String status;
	private ConceptObject parentConceptObject = new ConceptObject();
	private String createDate;
	private String updateDate;
	private String namespace;
	private HashMap<String,String> child = new HashMap<String,String>();
	private RelationObject conceptRelation = new RelationObject();
	
	public HashMap<String,String> getChild() {
		return child;
	}
	public void addChild(String childURI) {
		if(!child.containsKey(childURI)){
			child.put(childURI, childURI);
		}
	}
	public String getConceptIns() {
		return conceptIns;
	}
	public void setConceptIns(String conceptIns) {
		this.conceptIns = conceptIns;
	}
	
	public String getConceptURI() {
		return conceptURI;
	}
	public void setConceptURI(String conceptURI) {
		this.conceptURI = conceptURI;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public ArrayList<String> getScheme() {
		return scheme;
	}
	public void setScheme(ArrayList<String> scheme) {
		this.scheme = scheme;
	}
		
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public HashMap<String,ETermObject> getTerm() {
		return term;
	}
	
	public ETermObject getTermObject(String termURI){
		if(term.containsKey(termURI)){
			return (ETermObject) term.get(termURI);
		}else{
			return null;
		}	
	}
/*	public ETermObject getTermObject(String termURI){
		if(termList.containsKey(termURI)){
			return (ETermObject) termList.get(termURI);
		}else{
			return null;
		}	
	}	*/
	public void addTerm(ETermObject tObj) {
		if(!term.containsKey(tObj.getUri())){
			term.put(tObj.getUri(), tObj);
		}
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public boolean hasTerm(String termURI){
		return term.containsKey(termURI);
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public RelationObject getConcetpRelation() {
		return conceptRelation;
	}
	public void setConcetpRelation(RelationObject concetpRelation) {
		this.conceptRelation = concetpRelation;
	}
	public ConceptObject getParentConceptObject() {
		return parentConceptObject;
	}
	public void setParentConceptObject(ConceptObject parentConceptObject) {
		this.parentConceptObject = parentConceptObject;
	}
	public ArrayList<LabelObject> getScopeNoteList() {
		return scopeNoteList;
	}
	public void addScopeNoteList(LabelObject scopeNote) {
		this.scopeNoteList.add(scopeNote);
	}
    public void setConceptName(String conceptName)
    {
        this.conceptName = conceptName;
    }
    public String getConceptName()
    {
        return conceptName;
    }
	
}
