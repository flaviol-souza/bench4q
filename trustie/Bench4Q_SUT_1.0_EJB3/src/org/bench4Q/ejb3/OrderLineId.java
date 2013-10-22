/**
 * =========================================================================
 * 					Bench4Q version 1.0.0
 * =========================================================================
 * 
 * Bench4Q is available on the Internet at http://forge.ow2.org/projects/jaspte
 * You can find latest version there. 
 * 
 * Distributed according to the GNU Lesser General Public Licence. 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by   
 * the Free Software Foundation; either version 2.1 of the License, or any
 * later version.
 * 
 * SEE Copyright.txt FOR FULL COPYRIGHT INFORMATION.
 * 
 * This source code is distributed "as is" in the hope that it will be
 * useful.  It comes with no warranty, and no author or distributor
 * accepts any responsibility for the consequences of its use.
 *
 *
 * This version is a based on the implementation of TPC-W from University of Wisconsin. 
 * This version used some source code of The Grinder.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 *  * Developer(s): Xin Zhu.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 * 
 */
package org.bench4Q.ejb3;

import javax.persistence.Column;
import javax.persistence.Embeddable;


/**
 * OrderLineId entity. @author MyEclipse Persistence Tools
 */
@Embeddable

public class OrderLineId  implements java.io.Serializable {


    // Fields    

     private Integer olId;
     private Integer olOId;


    // Constructors

    /** default constructor */
    public OrderLineId() {
    }

    
    /** full constructor */
    public OrderLineId(Integer olId, Integer olOId) {
        this.olId = olId;
        this.olOId = olOId;
    }

   
    // Property accessors

    @Column(name="OL_ID", nullable=false)

    public Integer getOlId() {
        return this.olId;
    }
    
    public void setOlId(Integer olId) {
        this.olId = olId;
    }

    @Column(name="OL_O_ID", nullable=false)

    public Integer getOlOId() {
        return this.olOId;
    }
    
    public void setOlOId(Integer olOId) {
        this.olOId = olOId;
    }
   



   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof OrderLineId) ) return false;
		 OrderLineId castOther = ( OrderLineId ) other; 
         
		 return ( (this.getOlId()==castOther.getOlId()) || ( this.getOlId()!=null && castOther.getOlId()!=null && this.getOlId().equals(castOther.getOlId()) ) )
 && ( (this.getOlOId()==castOther.getOlOId()) || ( this.getOlOId()!=null && castOther.getOlOId()!=null && this.getOlOId().equals(castOther.getOlOId()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getOlId() == null ? 0 : this.getOlId().hashCode() );
         result = 37 * result + ( getOlOId() == null ? 0 : this.getOlOId().hashCode() );
         return result;
   }   





}