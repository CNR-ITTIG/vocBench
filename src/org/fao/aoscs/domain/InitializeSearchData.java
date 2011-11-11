package org.fao.aoscs.domain;

import java.util.ArrayList;

import net.sf.gilead.pojo.gwt.LightEntity;

public class InitializeSearchData extends LightEntity{

	private static final long serialVersionUID = -5393206370644964163L;

	private ArrayList<String[]> status = new ArrayList<String[]>();

	private ArrayList<RelationshipObject> termCodeProperties = new ArrayList<RelationshipObject>();

	private ArrayList<String[]> scheme = new ArrayList<String[]>();
	
	private ArrayList<ConceptObject> conceptTree = new ArrayList<ConceptObject>();
	
   private ArrayList<RelationshipObject> conceptEditorialAttributes = new ArrayList<RelationshipObject>();
    
    private ArrayList<RelationshipObject> conceptDomainAttributes = new ArrayList<RelationshipObject>();
    
    private ArrayList<RelationshipObject> termEditorialAttributes = new ArrayList<RelationshipObject>();
    
    private ArrayList<RelationshipObject> termDomainAttributes = new ArrayList<RelationshipObject>();

	/**
	 * @return the status
	 */
	public ArrayList<String[]> getStatus() {
		return status;
	}

	/**
	 * @return the termCodeProperties
	 */
	public ArrayList<RelationshipObject> getTermCodeProperties() {
		return termCodeProperties;
	}

	/**
	 * @return the scheme
	 */
	public ArrayList<String[]> getScheme() {
		return scheme;
	}

	/**
	 * @return the conceptTree
	 */
	public ArrayList<ConceptObject> getConceptTree() {
		return conceptTree;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(ArrayList<String[]> status) {
		this.status = status;
	}

	/**
	 * @param termCodeProperties the termCodeProperties to set
	 */
	public void setTermCodeProperties(
			ArrayList<RelationshipObject> termCodeProperties) {
		this.termCodeProperties = termCodeProperties;
	}

	/**
	 * @param scheme the scheme to set
	 */
	public void setScheme(ArrayList<String[]> scheme) {
		this.scheme = scheme;
	}
	
	/**
	 * @param conceptTree the conceptTree to set
	 */
	public void setConceptTree(ArrayList<ConceptObject> conceptTree) {
		this.conceptTree = conceptTree;
	}

    /**
     * @param conceptEditorialAttributes the conceptEditorialAttributes to set
     */
    public void setConceptEditorialAttributes(
            ArrayList<RelationshipObject> conceptEditorialAttributes)
    {
        this.conceptEditorialAttributes = conceptEditorialAttributes;
    }

    /**
     * @return the conceptEditorialAttributes
     */
    public ArrayList<RelationshipObject> getConceptEditorialAttributes()
    {
        return conceptEditorialAttributes;
    }

    public ArrayList<RelationshipObject> getConceptDomainAttributes()
    {
        return conceptDomainAttributes;
    }

    public void setConceptDomainAttributes(
            ArrayList<RelationshipObject> conceptDomainAttributes)
    {
        this.conceptDomainAttributes = conceptDomainAttributes;
    }

    public ArrayList<RelationshipObject> getTermEditorialAttributes()
    {
        return termEditorialAttributes;
    }

    public void setTermEditorialAttributes(
            ArrayList<RelationshipObject> termEditorialAttributes)
    {
        this.termEditorialAttributes = termEditorialAttributes;
    }

    public ArrayList<RelationshipObject> getTermDomainAttributes()
    {
        return termDomainAttributes;
    }

    public void setTermDomainAttributes(
            ArrayList<RelationshipObject> termDomainAttributes)
    {
        this.termDomainAttributes = termDomainAttributes;
    }	
}
