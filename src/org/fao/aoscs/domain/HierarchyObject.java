package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

public class HierarchyObject extends LightEntity{

	private static final long serialVersionUID = -7867396012767122181L;

	private HashMap<String, ArrayList<TreeObject>> broaderList = new HashMap<String, ArrayList<TreeObject>>();
	
	private TreeObject selectedConcept = new TreeObject();
	
	private ArrayList<TreeObject> narrowerList = new ArrayList<TreeObject>();

	public void setBroaderList(HashMap<String, ArrayList<TreeObject>> broaderList) {
		this.broaderList = broaderList;
	}

	public HashMap<String, ArrayList<TreeObject>> getBroaderList() {
		return broaderList;
	}

	public void setNarrowerList(ArrayList<TreeObject> narrowerList) {
		this.narrowerList = narrowerList;
	}

	public ArrayList<TreeObject> getNarrowerList() {
		return narrowerList;
	}

	public void setSelectedConcept(TreeObject selectedConcept) {
		this.selectedConcept = selectedConcept;
	}

	public TreeObject getSelectedConcept() {
		return selectedConcept;
	} 
	
	
	
}
