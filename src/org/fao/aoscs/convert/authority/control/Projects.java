package org.fao.aoscs.convert.authority.control;

public class Projects implements java.io.Serializable {

	private static final long serialVersionUID = 5672556349331374125L;
	private String id;
	private String projectCode;
	private String labelEn;
	private String labelFr;
	private String labelEs;
	private String labelPt;
	private String isSpatiallyIncludedIn;
	private String isSpatiallyIncludedInUri;
	private String hasSpellingVariantEn;
	private String hasSpellingVariantFr;
	private String hasSpellingVariantEs;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProjectCode() {
		return this.projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getLabelEn() {
		return this.labelEn;
	}

	public void setLabelEn(String labelEn) {
		this.labelEn = labelEn;
	}

	public String getLabelFr() {
		return this.labelFr;
	}

	public void setLabelFr(String labelFr) {
		this.labelFr = labelFr;
	}

	public String getLabelEs() {
		return this.labelEs;
	}

	public void setLabelEs(String labelEs) {
		this.labelEs = labelEs;
	}

	public String getLabelPt() {
		return this.labelPt;
	}

	public void setLabelPt(String labelPt) {
		this.labelPt = labelPt;
	}

	public String getIsSpatiallyIncludedIn() {
		return this.isSpatiallyIncludedIn;
	}

	public void setIsSpatiallyIncludedIn(String isSpatiallyIncludedIn) {
		this.isSpatiallyIncludedIn = isSpatiallyIncludedIn;
	}

	public String getIsSpatiallyIncludedInUri() {
		return this.isSpatiallyIncludedInUri;
	}

	public void setIsSpatiallyIncludedInUri(String isSpatiallyIncludedInUri) {
		this.isSpatiallyIncludedInUri = isSpatiallyIncludedInUri;
	}
	
	public String getHasSpellingVariantEn() {
		return hasSpellingVariantEn;
	}

	public String getHasSpellingVariantFr() {
		return hasSpellingVariantFr;
	}

	public String getHasSpellingVariantEs() {
		return hasSpellingVariantEs;
	}

	public void setHasSpellingVariantEn(String hasSpellingVariantEn) {
		this.hasSpellingVariantEn = hasSpellingVariantEn;
	}

	public void setHasSpellingVariantFr(String hasSpellingVariantFr) {
		this.hasSpellingVariantFr = hasSpellingVariantFr;
	}

	public void setHasSpellingVariantEs(String hasSpellingVariantEs) {
		this.hasSpellingVariantEs = hasSpellingVariantEs;
	}
}
