package org.fao.aoscs.server.export;

public class ETermObject {
	private String uri;
	private String createDate;
	private String updateDate;
	private String status;
	private String codeFAOPA;
	private String codeASC;
	private String codeASFA;
	private String codeTaxonomic;
	private String codeFAOTERM;
	private String codeFishery ;
	private String codeAGROVOC;
	private String label;
	private String language;
	private boolean mainLabel;
	private boolean isAbbreviationOf;
	private boolean hasAcronymOf;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getCodeAGROVOC() {
		return codeAGROVOC;
	}

	public void setCodeAGROVOC(String codeAGROVOC) {
		if(codeAGROVOC != null){
			this.codeAGROVOC = codeAGROVOC;
		}
	}

	public String getCodeASC() {
		return codeASC;
	}

	public void setCodeASC(String codeASC) {
		if(codeASC != null){
			this.codeASC = codeASC;
		}
	}

	public String getCodeASFA() {
		return codeASFA;
	}

	public void setCodeASFA(String codeASFA) {
		if(codeASFA != null){
			this.codeASFA = codeASFA;
		}
	}

	public String getCodeFAOPA() {
		return codeFAOPA;
	}

	public void setCodeFAOPA(String codeFAOPA) {
		if(codeFAOPA != null){
			this.codeFAOPA = codeFAOPA;
		}
	}

	public String getCodeFAOTERM() {
		return codeFAOTERM;
	}

	public void setCodeFAOTERM(String codeFAOTERM) {
		if(codeFAOTERM != null){
			this.codeFAOTERM = codeFAOTERM;
		}
	}

	public String getCodeFishery() {
		return codeFishery;
	}

	public void setCodeFishery(String codeFishery) {
		if(codeFishery != null){
			this.codeFishery = codeFishery;
		}
	}

	public String getCodeTaxonomic() {
		return codeTaxonomic;
	}

	public void setCodeTaxonomic(String codeTaxonomic) {
		if(codeTaxonomic != null){
			this.codeTaxonomic = codeTaxonomic;
		}
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public boolean isMainLabel() {
		return mainLabel;
	}

	public void setMainLabel(boolean mainLabel) {
		this.mainLabel = mainLabel;
	}

    public void setAbbreviationOf(boolean isAbbreviationOf)
    {
        this.isAbbreviationOf = isAbbreviationOf;
    }

    public boolean isAbbreviationOf()
    {
        return isAbbreviationOf;
    }

    public boolean isHasAcronymOf()
    {
        return hasAcronymOf;
    }

    public void setHasAcronymOf(boolean hasAcronymOf)
    {
        this.hasAcronymOf = hasAcronymOf;
    }
}
