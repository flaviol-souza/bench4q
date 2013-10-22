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
 * ShoppingCartLine entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="SHOPPING_CART_LINE"
    ,schema="DB2ADMIN"
)

public class ShoppingCartLine  implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = 2199888554244944090L;
	private ShoppingCartLineId id;
     private Integer sclQty;


    // Constructors

    /** default constructor */
    public ShoppingCartLine() {
    }

	/** minimal constructor */
    public ShoppingCartLine(ShoppingCartLineId id) {
        this.id = id;
    }
    
    /** full constructor */
    public ShoppingCartLine(ShoppingCartLineId id, Integer sclQty) {
        this.id = id;
        this.sclQty = sclQty;
    }

   
    // Property accessors
    @EmbeddedId    
    @AttributeOverrides( {
        @AttributeOverride(name="sclScId", column=@Column(name="SCL_SC_ID", nullable=false) ), 
        @AttributeOverride(name="sclIId", column=@Column(name="SCL_I_ID", nullable=false) ) } )
    public ShoppingCartLineId getId() {
        return this.id;
    }
    
    public void setId(ShoppingCartLineId id) {
        this.id = id;
    }
    
    @Column(name="SCL_QTY")
    public Integer getSclQty() {
        return this.sclQty;
    }
    
    public void setSclQty(Integer sclQty) {
        this.sclQty = sclQty;
    }
}