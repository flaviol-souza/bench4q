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

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * OrderLine entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="ORDER_LINE"
    ,schema="DB2ADMIN"
)

public class OrderLine  implements java.io.Serializable {


    // Fields    

     private OrderLineId id;
     private Integer olIId;
     private Integer olQty;
     private Double olDiscount;
     private String olComments;


    // Constructors

    /** default constructor */
    public OrderLine() {
    }

	/** minimal constructor */
    public OrderLine(OrderLineId id) {
        this.id = id;
    }
    
    /** full constructor */
    public OrderLine(OrderLineId id, Integer olIId, Integer olQty, Double olDiscount, String olComments) {
        this.id = id;
        this.olIId = olIId;
        this.olQty = olQty;
        this.olDiscount = olDiscount;
        this.olComments = olComments;
    }

   
    // Property accessors
    @EmbeddedId
    
    @AttributeOverrides( {
        @AttributeOverride(name="olId", column=@Column(name="OL_ID", nullable=false) ), 
        @AttributeOverride(name="olOId", column=@Column(name="OL_O_ID", nullable=false) ) } )

    public OrderLineId getId() {
        return this.id;
    }
    
    public void setId(OrderLineId id) {
        this.id = id;
    }
    
    @Column(name="OL_I_ID")

    public Integer getOlIId() {
        return this.olIId;
    }
    
    public void setOlIId(Integer olIId) {
        this.olIId = olIId;
    }
    
    @Column(name="OL_QTY")

    public Integer getOlQty() {
        return this.olQty;
    }
    
    public void setOlQty(Integer olQty) {
        this.olQty = olQty;
    }
    
    @Column(name="OL_DISCOUNT", precision=53, scale=0)

    public Double getOlDiscount() {
        return this.olDiscount;
    }
    
    public void setOlDiscount(Double olDiscount) {
        this.olDiscount = olDiscount;
    }
    
    @Column(name="OL_COMMENTS", length=110)

    public String getOlComments() {
        return this.olComments;
    }
    
    public void setOlComments(String olComments) {
        this.olComments = olComments;
    }
   








}