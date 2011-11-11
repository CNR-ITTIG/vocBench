package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class UsersOntology  extends LightEntity {

	private static final long serialVersionUID = -4677975626422538901L;
	private UsersOntologyId id;
    private int status;

    public UsersOntology() {
    }
    
    public UsersOntology(UsersOntologyId id, int status) {
        this.id = id;
        this.status = status;
    }
    
   
    public UsersOntologyId getId() {
        return this.id;
    }
    
    public void setId(UsersOntologyId id) {
        this.id = id;
    }

    public int getStatus() {
        return this.status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
   








}
