package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.Date;

import net.sf.gilead.pojo.gwt.LightEntity;

public class ValidationFilter extends LightEntity {

	private static final long serialVersionUID = 9030480177168627213L;

	private int groupID = 0;
	
	private OntologyInfo ontoInfo = new OntologyInfo();
	
	private ArrayList<OwlStatus> selectedStatusList = new ArrayList<OwlStatus>(); 

	private ArrayList<Users> selectedUserList = new ArrayList<Users>();

	private ArrayList<OwlAction> selectedActionList = new ArrayList<OwlAction>();
	
	private Date fromDate =  null;
	
	private Date toDate =  null;

	public int getGroupID() {
		return groupID;
	}

	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}

	public OntologyInfo getOntoInfo() {
		return ontoInfo;
	}

	public void setOntoInfo(OntologyInfo ontoInfo) {
		this.ontoInfo = ontoInfo;
	}

	public ArrayList<OwlStatus> getSelectedStatusList() {
		return selectedStatusList;
	}

	public void setSelectedStatusList(ArrayList<OwlStatus> arrayList) {
		this.selectedStatusList = arrayList;
	}
	
	public void addSelectedStatusList(OwlStatus value)
	{
		this.selectedStatusList.add(value);	
	}
	
	public void removeSelectedStatusList(OwlStatus value)
	{
		this.selectedStatusList.remove(value);	
	}
	
	public ArrayList<Users> getSelectedUserList() {
		return selectedUserList;
	}

	public void setSelectedUserList(ArrayList<Users> arrayList) {
		this.selectedUserList = arrayList;
	}

	public void addSelectedUserList(Users value)
	{
		this.selectedUserList.add(value);	
	}
	
	public void removeSelectedUserList(Users value)
	{
		this.selectedUserList.remove(value);	
	}
	
	public ArrayList<OwlAction> getSelectedActionList() {
		return selectedActionList;
	}

	public void setSelectedActionList(ArrayList<OwlAction> arrayList) {
		this.selectedActionList = arrayList;
	}
	
	public void addSelectedActionList(OwlAction value)
	{
		this.selectedActionList.add(value);	
	}
	
	public void removeSelectedActionList(OwlAction value)
	{
		this.selectedActionList.remove(value);	
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
}
