package org.fao.aoscs.domain;

import java.util.ArrayList;

import net.sf.gilead.pojo.gwt.LightEntity;

public class ExportParameterObject extends LightEntity{

	private static final long serialVersionUID = -7032544321200001869L;

	private String format = null;
	
	private String schemeURI = null;
	
	private String conceptURI = null;
	
	private String termCodeRepositoryURI = null;
	
	private String startCode = null;
	
	private String endCode = null;
	
	private String startDate = null;
	
	private String endDate = null;
	
	private String dateType = "Create";
	
//	private ArrayList explang ;
	
	private ArrayList<String> langlist = new ArrayList<String>();
	
	private boolean includeChildren = false;
	
	public boolean isIncludeChildren() {
		return includeChildren;
	}

	public void setIncludeChildren(boolean includeChildren) {
		this.includeChildren = includeChildren;
	}

	public ArrayList<String> getExpLanguage(){
		return this.langlist;
	}
	
	public String getConceptURI(){
		return this.conceptURI;
	}
	
	public String getDateType(){
		return this.dateType;
	}
	
	public String getEndCode(){
		return this.endCode;
	}
	
	public String getEndDate(){
		return this.endDate;
	}
	
	public String getExportFormat(){
		return this.format;
	}
	
	public String getFormat() {
			return format;
		}
	
	public String getSchemeURI(){
		return this.schemeURI;
	}
	
	public String getStartCode(){
		return this.startCode;
	}
	
	public String getStartDate(){
		return this.startDate;
	}
	
	public String getTermCodeRepositoryURI(){
		return this.termCodeRepositoryURI;
	}
	
	public boolean isConceptURI(){
		if(this.conceptURI == null){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isDateEmpty(){
		if(this.dateType == null || this.startDate == null || this.endDate == null){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isDateTypeEmpty(){
		if(this.startDate == null){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isEndDateEmpty(){
			if(this.endDate == null){
				return true;
			}else{
				return false;
			}
		}

	public boolean isFormatEmpty(){
		if(this.format == null){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isSchemeURIEmpty(){
		if(this.schemeURI == null){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isStartDateEmpty(){
		if(this.startDate == null){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isTermCodeEmpty(){
		//if(this.termCodeRepositoryURI == null || this.startCode == null || this.endCode == null){
		if(this.termCodeRepositoryURI == null || this.startCode == null ){
				
			return true;
		}else{
			return false;
		}
	}

	public boolean isLangListEmpty(){
		if(this.langlist.size() == 0){
			return true;
		}else{
			return false;
		}
	}

	public void setExpLanguage(ArrayList <String> langlist){
		this.langlist = langlist ;
	}
	
	public void setConceptURI(String conceptURI){
		this.conceptURI = conceptURI;
	}
	
	public void setDateType(String dateType){
		this.dateType = dateType;
	}
	
	public void setEndCode(String endCode){
		this.endCode = endCode;
	}
	
	public void setEndDate(String endDate){
		this.endDate = endDate;
	}

	public void setExportFormat(String format){
		this.format = format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public void setSchemeURI(String schemeURI){
		this.schemeURI = schemeURI;
	}
	 public void setStartCode(String startCode){
		this.startCode = startCode;
	}
	 
	 public void setStartDate(String startDate){
		this.startDate = startDate;
	}

	public void setTermCodeRepositoryURI(String termCodeRepositoryURI){
		this.termCodeRepositoryURI = termCodeRepositoryURI;
	}
}
