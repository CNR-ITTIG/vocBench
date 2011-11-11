package org.fao.aoscs.convert.authority.control;

public class Conferences implements java.io.Serializable {

	private static final long serialVersionUID = -3183494823560809173L;
	/*private String id;
	private String faoterm;
	private String labelAr;
	private String isComponentOfURI1;
	private String labelEn;
	private String labelEs;
	private String labelIt;
	private String labelFr;
	private String labelZh;
	private String labelRu;
	private String labelPt;
	private String labelDe;
	private String labelTr;
	private String labelId;
	private String alternativeEn1;
	private String alternativeEn2;
	private String alternativeFr1;
	private String alternativeFr2;
	private String alternativeFr3;
	private String alternativeEs1;
	private String alternativeEs2;
	private String alternativeEs3;
	private String alternativeEs4;
	private String alternativeRu;
	private String number;
	private String city;
	private String state;
	private String alternativeCity1;
	private String alternativeCity2;
	private String alternativeCity3;
	private String country;
	private String countryUri;
	private String date;
	private String alternativeDate1;
	private String alternativeDate2;
	private String alternativeDate3;
	private String alternativeDate4;
	private String alternativeDate5;
	*/
	
	private String id;
	private String isPartOfConferenceSeries;
	private String labelEn;
	private String labelFr;
	private String labelEs;
	private String labelAr;
	private String labelZh;
	private String labelRu;
	private String labelIt;
	private String labelPt;
	private String labelDe;
	private String labelTr;
	private String labelId;
	private String labelMk;
	private String labelPl;
	private String hasSpellingVariantEn1;
	private String hasSpellingVariantEn2;
	private String hasSpellingVariantFr1;
	private String hasSpellingVariantFr2;
	private String hasSpellingVariantFr3;
	private String hasSpellingVariantEs1;
	private String hasSpellingVariantEs2;
	private String hasSpellingVariantEs3;
	private String hasSpellingVariantEs4;
	private String hasSpellingVariantRu;
	private String hasSpellingVariantPt1;
	private String hasSpellingVariantPt2;
	private String hasSpellingVariantIt;
	private String hasNumber;
	private String hasDate;
	private String isSpatiallyIncludedInCity;
	private String isSpatiallyIncludedInState;
	private String isSpatiallyIncludedInCountry;
	private String isSpatiallyIncludedInCountryUri;
	private String acronymEn;
	private String obsolete;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIsPartOfConferenceSeries() {
		return isPartOfConferenceSeries;
	}
	public void setIsPartOfConferenceSeries(String isPartOfConferenceSeries) {
		this.isPartOfConferenceSeries = isPartOfConferenceSeries;
	}
	public String getLabelEn() {
		return labelEn;
	}
	public void setLabelEn(String labelEn) {
		this.labelEn = labelEn;
	}
	public String getLabelFr() {
		return labelFr;
	}
	public void setLabelFr(String labelFr) {
		this.labelFr = labelFr;
	}
	public String getLabelEs() {
		return labelEs;
	}
	public void setLabelEs(String labelEs) {
		this.labelEs = labelEs;
	}
	public String getLabelAr() {
		return labelAr;
	}
	public void setLabelAr(String labelAr) {
		this.labelAr = labelAr;
	}
	public String getLabelZh() {
		return labelZh;
	}
	public void setLabelZh(String labelZh) {
		this.labelZh = labelZh;
	}
	public String getLabelRu() {
		return labelRu;
	}
	public void setLabelRu(String labelRu) {
		this.labelRu = labelRu;
	}
	public String getLabelIt() {
		return labelIt;
	}
	public void setLabelIt(String labelIt) {
		this.labelIt = labelIt;
	}
	public String getLabelPt() {
		return labelPt;
	}
	public void setLabelPt(String labelPt) {
		this.labelPt = labelPt;
	}
	public String getLabelDe() {
		return labelDe;
	}
	public void setLabelDe(String labelDe) {
		this.labelDe = labelDe;
	}
	public String getLabelTr() {
		return labelTr;
	}
	public void setLabelTr(String labelTr) {
		this.labelTr = labelTr;
	}
	public String getLabelId() {
		return labelId;
	}
	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}
	public String getLabelMk() {
		return labelMk;
	}
	public void setLabelMk(String labelMk) {
		this.labelMk = labelMk;
	}
	public String getLabelPl() {
		return labelPl;
	}
	public void setLabelPl(String labelPl) {
		this.labelPl = labelPl;
	}
	public String getHasSpellingVariantEn1() {
		return hasSpellingVariantEn1;
	}
	public void setHasSpellingVariantEn1(String hasSpellingVariantEn1) {
		this.hasSpellingVariantEn1 = hasSpellingVariantEn1;
	}
	public String getHasSpellingVariantEn2() {
		return hasSpellingVariantEn2;
	}
	public void setHasSpellingVariantEn2(String hasSpellingVariantEn2) {
		this.hasSpellingVariantEn2 = hasSpellingVariantEn2;
	}
	public String getHasSpellingVariantFr1() {
		return hasSpellingVariantFr1;
	}
	public void setHasSpellingVariantFr1(String hasSpellingVariantFr1) {
		this.hasSpellingVariantFr1 = hasSpellingVariantFr1;
	}
	public String getHasSpellingVariantFr2() {
		return hasSpellingVariantFr2;
	}
	public void setHasSpellingVariantFr2(String hasSpellingVariantFr2) {
		this.hasSpellingVariantFr2 = hasSpellingVariantFr2;
	}
	public String getHasSpellingVariantFr3() {
		return hasSpellingVariantFr3;
	}
	public void setHasSpellingVariantFr3(String hasSpellingVariantFr3) {
		this.hasSpellingVariantFr3 = hasSpellingVariantFr3;
	}
	public String getHasSpellingVariantEs1() {
		return hasSpellingVariantEs1;
	}
	public void setHasSpellingVariantEs1(String hasSpellingVariantEs1) {
		this.hasSpellingVariantEs1 = hasSpellingVariantEs1;
	}
	public String getHasSpellingVariantEs2() {
		return hasSpellingVariantEs2;
	}
	public void setHasSpellingVariantEs2(String hasSpellingVariantEs2) {
		this.hasSpellingVariantEs2 = hasSpellingVariantEs2;
	}
	public String getHasSpellingVariantEs3() {
		return hasSpellingVariantEs3;
	}
	public void setHasSpellingVariantEs3(String hasSpellingVariantEs3) {
		this.hasSpellingVariantEs3 = hasSpellingVariantEs3;
	}
	public String getHasSpellingVariantEs4() {
		return hasSpellingVariantEs4;
	}
	public void setHasSpellingVariantEs4(String hasSpellingVariantEs4) {
		this.hasSpellingVariantEs4 = hasSpellingVariantEs4;
	}
	public String getHasSpellingVariantRu() {
		return hasSpellingVariantRu;
	}
	public void setHasSpellingVariantRu(String hasSpellingVariantRu) {
		this.hasSpellingVariantRu = hasSpellingVariantRu;
	}
	public String getHasSpellingVariantPt1() {
		return hasSpellingVariantPt1;
	}
	public void setHasSpellingVariantPt1(String hasSpellingVariantPt1) {
		this.hasSpellingVariantPt1 = hasSpellingVariantPt1;
	}
	public String getHasSpellingVariantPt2() {
		return hasSpellingVariantPt2;
	}
	public void setHasSpellingVariantPt2(String hasSpellingVariantPt2) {
		this.hasSpellingVariantPt2 = hasSpellingVariantPt2;
	}
	public String getHasSpellingVariantIt() {
		return hasSpellingVariantIt;
	}
	public void setHasSpellingVariantIt(String hasSpellingVariantIt) {
		this.hasSpellingVariantIt = hasSpellingVariantIt;
	}
	public String getHasNumber() {
		return hasNumber;
	}
	public void setHasNumber(String hasNumber) {
		this.hasNumber = hasNumber;
	}
	public String getHasDate() {
		return hasDate;
	}
	public void setHasDate(String hasDate) {
		this.hasDate = hasDate;
	}
	public String getIsSpatiallyIncludedInCity() {
		return isSpatiallyIncludedInCity;
	}
	public void setIsSpatiallyIncludedInCity(String isSpatiallyIncludedInCity) {
		this.isSpatiallyIncludedInCity = isSpatiallyIncludedInCity;
	}
	public String getIsSpatiallyIncludedInState() {
		return isSpatiallyIncludedInState;
	}
	public void setIsSpatiallyIncludedInState(String isSpatiallyIncludedInState) {
		this.isSpatiallyIncludedInState = isSpatiallyIncludedInState;
	}
	public String getIsSpatiallyIncludedInCountry() {
		return isSpatiallyIncludedInCountry;
	}
	public void setIsSpatiallyIncludedInCountry(String isSpatiallyIncludedInCountry) {
		this.isSpatiallyIncludedInCountry = isSpatiallyIncludedInCountry;
	}
	public String getIsSpatiallyIncludedInCountryUri() {
		return isSpatiallyIncludedInCountryUri;
	}
	public void setIsSpatiallyIncludedInCountryUri(
			String isSpatiallyIncludedInCountryUri) {
		this.isSpatiallyIncludedInCountryUri = isSpatiallyIncludedInCountryUri;
	}
	public String getAcronymEn() {
		return acronymEn;
	}
	public void setAcronymEn(String acronymEn) {
		this.acronymEn = acronymEn;
	}
	public String getObsolete() {
		return obsolete;
	}
	public void setObsolete(String obsolete) {
		this.obsolete = obsolete;
	}

	

}
