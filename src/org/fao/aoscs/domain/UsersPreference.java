package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class UsersPreference extends LightEntity {

	private static final long serialVersionUID = -8108116808910595189L;

	private int userId;
	private int ontologyId;
	private String frequency;
	private String initialPage;
	private boolean hideUri = true;
	private boolean hideNonpreferred = true;
	private boolean hideDeprecated;
	private boolean hideNonselectedlanguages = true;
	private String languageCodeInterface;

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getOntologyId() {
		return this.ontologyId;
	}

	public void setOntologyId(int ontologyId) {
		this.ontologyId = ontologyId;
	}

	public String getFrequency() {
		return this.frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getInitialPage() {
		return this.initialPage;
	}

	public void setInitialPage(String initialPage) {
		this.initialPage = initialPage;
	}

	public boolean isHideUri() {
		return this.hideUri;
	}

	public void setHideUri(boolean hideUri) {
		this.hideUri = hideUri;
	}

	public boolean isHideDeprecated() {
		return this.hideDeprecated;
	}

	public void setHideDeprecated(boolean hideDeprecated) {
		this.hideDeprecated = hideDeprecated;
	}

	public String getLanguageCodeInterface() {
		return this.languageCodeInterface;
	}

	public void setLanguageCodeInterface(String languageCodeInterface) {
		this.languageCodeInterface = languageCodeInterface;
	}

	public void setHideNonpreferred(boolean hideNonpreferred) {
		this.hideNonpreferred = hideNonpreferred;
	}

	public boolean isHideNonpreferred() {
		return hideNonpreferred;
	}

	public void setHideNonselectedlanguages(boolean hideNonselectedlanguages) {
		this.hideNonselectedlanguages = hideNonselectedlanguages;
	}

	public boolean isHideNonselectedlanguages() {
		return hideNonselectedlanguages;
	}

}
