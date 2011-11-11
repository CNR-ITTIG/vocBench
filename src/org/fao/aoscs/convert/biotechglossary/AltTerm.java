package org.fao.aoscs.convert.biotechglossary;

public class AltTerm implements java.io.Serializable {
	
	private static final long serialVersionUID = 2511008898738883482L;
	private String idAlternate;
	private String idGlossary;
	private String alternateTerm;
	private String fieldOfApplication;
	private String idLang;
	private String idOriginator;
	private String dataEntryDate;	
	
	
	public String getIdAlternate() {
		return idAlternate;
	}
	public void setIdAlternate(String idAlternate) {
		this.idAlternate = idAlternate;
	}
	public String getIdGlossary() {
		return idGlossary;
	}
	public void setIdGlossary(String idGlossary) {
		this.idGlossary = idGlossary;
	}
	public String getAlternateTerm() {
		return alternateTerm;
	}
	public void setAlternateTerm(String alternateTerm) {
		this.alternateTerm = alternateTerm;
	}
	public String getFieldOfApplication() {
		return fieldOfApplication;
	}
	public void setFieldOfApplication(String fieldOfApplication) {
		this.fieldOfApplication = fieldOfApplication;
	}
	public String getIdLang() {
		return idLang;
	}
	public void setIdLang(String idLang) {
		this.idLang = idLang;
	}
	public String getIdOriginator() {
		return idOriginator;
	}
	public void setIdOriginator(String idOriginator) {
		this.idOriginator = idOriginator;
	}
	public String getDataEntryDate() {
		return dataEntryDate;
	}
	public void setDataEntryDate(String dataEntryDate) {
		this.dataEntryDate = dataEntryDate;
	}
	
	public String getLang(){
		if(this.idLang.equals("10")) return "zh";
		if(this.idLang.equals("1")) return "en";
		if(this.idLang.equals("2")) return "fr";
		if(this.idLang.equals("3")) return "es";
		if(this.idLang.equals("5")) return "ar";
		if(this.idLang.equals("6")) return "it";
		if(this.idLang.equals("7")) return "pt";
		if(this.idLang.equals("8")) return "jp";
		return null;		
	}
	
}
