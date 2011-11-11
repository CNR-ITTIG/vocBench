package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class UsersOntologyId  extends LightEntity {


	private static final long serialVersionUID = -680940906349910951L;
	private int userId;
    private int ontologyId;


    public UsersOntologyId() {
    }

    public UsersOntologyId(int userId, int ontologyId) {
        this.userId = userId;
        this.ontologyId = ontologyId;
    }
    
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
   



   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof UsersOntologyId) ) return false;
		 UsersOntologyId castOther = ( UsersOntologyId ) other; 
         
		 return (this.getUserId()==castOther.getUserId())
 && (this.getOntologyId()==castOther.getOntologyId());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getUserId();
         result = 37 * result + this.getOntologyId();
         return result;
   }   





}
