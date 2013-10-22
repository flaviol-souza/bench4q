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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Country entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="COUNTRY"
    ,schema="DB2ADMIN"
)

public class Country  implements java.io.Serializable {


    // Fields    

     private Integer coId;
     private String coName;
     private Double coExchange;
     private String coCurrency;


    // Constructors

    /** default constructor */
    public Country() {
    }

	/** minimal constructor */
    public Country(Integer coId) {
        this.coId = coId;
    }
    
    /** full constructor */
    public Country(Integer coId, String coName, Double coExchange, String coCurrency) {
        this.coId = coId;
        this.coName = coName;
        this.coExchange = coExchange;
        this.coCurrency = coCurrency;
    }

   
    // Property accessors
    @Id 
    
    @Column(name="CO_ID", unique=true, nullable=false)

    public Integer getCoId() {
        return this.coId;
    }
    
    public void setCoId(Integer coId) {
        this.coId = coId;
    }
    
    @Column(name="CO_NAME", length=50)

    public String getCoName() {
        return this.coName;
    }
    
    public void setCoName(String coName) {
        this.coName = coName;
    }
    
    @Column(name="CO_EXCHANGE", precision=53, scale=0)

    public Double getCoExchange() {
        return this.coExchange;
    }
    
    public void setCoExchange(Double coExchange) {
        this.coExchange = coExchange;
    }
    
    @Column(name="CO_CURRENCY", length=18)

    public String getCoCurrency() {
        return this.coCurrency;
    }
    
    public void setCoCurrency(String coCurrency) {
        this.coCurrency = coCurrency;
    }
   








}