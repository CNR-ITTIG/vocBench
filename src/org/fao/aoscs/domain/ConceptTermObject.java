package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

public class ConceptTermObject extends LightEntity{

	private static final long serialVersionUID = 273372581373724572L;
	
	private HashMap<String, ArrayList<TermObject>> termList = new HashMap<String, ArrayList<TermObject>>();

	public HashMap<String, ArrayList<TermObject>> getTermList() {
		return termList;
	}
	
	public void setTermList(HashMap<String, ArrayList<TermObject>> termList) {
		this.termList = termList;
	}

	public void addTermList(String language,TermObject tObj) {
		if(!termList.containsKey(language)){
			ArrayList<TermObject> list = new ArrayList<TermObject>();
			termList.put(language, list);
			list.add(tObj);
		}else{
			((ArrayList<TermObject>)termList.get(language)).add(tObj);
		}
	}
	public boolean isEmpty(){
		return termList.isEmpty();
	}
	
	public int getTermCount(){
		if(termList==null) 
			return 0;
		else
			return termList.size();
	}
}
