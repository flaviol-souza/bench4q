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
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * CcXacts entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="CC_XACTS"
    ,schema="DB2ADMIN"
)

public class CcXacts  implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = -7755925479090707461L;
	private Integer cxOId;
     private String cxType;
     private String cxNum;
     private String cxName;
     private Date cxExpire;
     private String cxAuthId;
     private Double cxXactAmt;
     private Date cxXactDate;
     private Integer cxCoId;


    // Constructors

    /** default constructor */
    public CcXacts() {
    }

	/** minimal constructor */
    public CcXacts(Integer cxOId) {
        this.cxOId = cxOId;
    }
    
    /** full constructor */
    public CcXacts(Integer cxOId, String cxType, String cxNum, String cxName, Date cxExpire, String cxAuthId, Double cxXactAmt, Date cxXactDate, Integer cxCoId) {
        this.cxOId = cxOId;
        this.cxType = cxType;
        this.cxNum = cxNum;
        this.cxName = cxName;
        this.cxExpire = cxExpire;
        this.cxAuthId = cxAuthId;
        this.cxXactAmt = cxXactAmt;
        this.cxXactDate = cxXactDate;
        this.cxCoId = cxCoId;
    }

   
    // Property accessors
    @Id 
    
    @Column(name="CX_O_ID", unique=true, nullable=false)

    public Integer getCxOId() {
        return this.cxOId;
    }
    
    public void setCxOId(Integer cxOId) {
        this.cxOId = cxOId;
    }
    
    @Column(name="CX_TYPE", length=10)

    public String getCxType() {
        return this.cxType;
    }
    
    public void setCxType(String cxType) {
        this.cxType = cxType;
    }
    
    @Column(name="CX_NUM", length=20)

    public String getCxNum() {
        return this.cxNum;
    }
    
    public void setCxNum(String cxNum) {
        this.cxNum = cxNum;
    }
    
    @Column(name="CX_NAME", length=30)

    public String getCxName() {
        return this.cxName;
    }
    
    public void setCxName(String cxName) {
        this.cxName = cxName;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="CX_EXPIRE", length=10)

    public Date getCxExpire() {
        return this.cxExpire;
    }
    
    public void setCxExpire(Date cxExpire) {
        this.cxExpire = cxExpire;
    }
    
    @Column(name="CX_AUTH_ID", length=15)

    public String getCxAuthId() {
        return this.cxAuthId;
    }
    
    public void setCxAuthId(String cxAuthId) {
        this.cxAuthId = cxAuthId;
    }
    
    @Column(name="CX_XACT_AMT", precision=53, scale=0)

    public Double getCxXactAmt() {
        return this.cxXactAmt;
    }
    
    public void setCxXactAmt(Double cxXactAmt) {
        this.cxXactAmt = cxXactAmt;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="CX_XACT_DATE", length=10)

    public Date getCxXactDate() {
        return this.cxXactDate;
    }
    
    public void setCxXactDate(Date cxXactDate) {
        this.cxXactDate = cxXactDate;
    }
    
    @Column(name="CX_CO_ID")

    public Integer getCxCoId() {
        return this.cxCoId;
    }
    
    public void setCxCoId(Integer cxCoId) {
        this.cxCoId = cxCoId;
    }
   








}