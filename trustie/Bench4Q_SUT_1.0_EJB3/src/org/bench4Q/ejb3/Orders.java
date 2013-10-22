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

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * Orders entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="ORDERS"
    ,schema="DB2ADMIN"
)

public class Orders  implements java.io.Serializable {


    // Fields    

     private Integer OId;
     private Integer OCId;
     private Date ODate;
     private Double OSubTotal;
     private Double OTax;
     private Double OTotal;
     private String OShipType;
     private Date OShipDate;
     private Integer OBillAddrId;
     private Integer OShipAddrId;
     private String OStatus;


    // Constructors

    /** default constructor */
    public Orders() {
    }

	/** minimal constructor */
    public Orders(Integer OId) {
        this.OId = OId;
    }
    
    /** full constructor */
    public Orders(Integer OId, Integer OCId, Date ODate, Double OSubTotal, Double OTax, Double OTotal, String OShipType, Date OShipDate, Integer OBillAddrId, Integer OShipAddrId, String OStatus) {
        this.OId = OId;
        this.OCId = OCId;
        this.ODate = ODate;
        this.OSubTotal = OSubTotal;
        this.OTax = OTax;
        this.OTotal = OTotal;
        this.OShipType = OShipType;
        this.OShipDate = OShipDate;
        this.OBillAddrId = OBillAddrId;
        this.OShipAddrId = OShipAddrId;
        this.OStatus = OStatus;
    }

   
    // Property accessors
    @Id 
    
    @Column(name="O_ID", unique=true, nullable=false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Integer getOId() {
        return this.OId;
    }
    
    public void setOId(Integer OId) {
        this.OId = OId;
    }
    
    @Column(name="O_C_ID")

    public Integer getOCId() {
        return this.OCId;
    }
    
    public void setOCId(Integer OCId) {
        this.OCId = OCId;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="O_DATE", length=10)

    public Date getODate() {
        return this.ODate;
    }
    
    public void setODate(Date ODate) {
        this.ODate = ODate;
    }
    
    @Column(name="O_SUB_TOTAL", precision=53, scale=0)

    public Double getOSubTotal() {
        return this.OSubTotal;
    }
    
    public void setOSubTotal(Double OSubTotal) {
        this.OSubTotal = OSubTotal;
    }
    
    @Column(name="O_TAX", precision=53, scale=0)

    public Double getOTax() {
        return this.OTax;
    }
    
    public void setOTax(Double OTax) {
        this.OTax = OTax;
    }
    
    @Column(name="O_TOTAL", precision=53, scale=0)

    public Double getOTotal() {
        return this.OTotal;
    }
    
    public void setOTotal(Double OTotal) {
        this.OTotal = OTotal;
    }
    
    @Column(name="O_SHIP_TYPE", length=10)

    public String getOShipType() {
        return this.OShipType;
    }
    
    public void setOShipType(String OShipType) {
        this.OShipType = OShipType;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="O_SHIP_DATE", length=10)

    public Date getOShipDate() {
        return this.OShipDate;
    }
    
    public void setOShipDate(Date OShipDate) {
        this.OShipDate = OShipDate;
    }
    
    @Column(name="O_BILL_ADDR_ID")

    public Integer getOBillAddrId() {
        return this.OBillAddrId;
    }
    
    public void setOBillAddrId(Integer OBillAddrId) {
        this.OBillAddrId = OBillAddrId;
    }
    
    @Column(name="O_SHIP_ADDR_ID")

    public Integer getOShipAddrId() {
        return this.OShipAddrId;
    }
    
    public void setOShipAddrId(Integer OShipAddrId) {
        this.OShipAddrId = OShipAddrId;
    }
    
    @Column(name="O_STATUS", length=15)

    public String getOStatus() {
        return this.OStatus;
    }
    
    public void setOStatus(String OStatus) {
        this.OStatus = OStatus;
    }
   








}