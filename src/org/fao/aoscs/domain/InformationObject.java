package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class InformationObject extends LightEntity{

	private static final long serialVersionUID = 7716230820211339961L;
		
	public String createDate= "";
	
	public String updateDate= "";
	
	public String status= "";
	
	public int type = 0;
	
	public static int CONCEPT_TYPE = 1;
	
	public static int TERM_TYPE = 2;
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public RecentChangesInitObject recentChangesInitObject = new RecentChangesInitObject();
	
	public RecentChangesInitObject getRecentChangesInitObject() {
		return recentChangesInitObject;
	}
	public void setRecentChangesInitObject(
			RecentChangesInitObject recentChangesInitObject) {
		this.recentChangesInitObject = recentChangesInitObject;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
