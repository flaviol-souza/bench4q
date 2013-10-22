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
 * Author entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="AUTHOR"
    ,schema="DB2ADMIN"
)

public class Author  implements java.io.Serializable {


    // Fields    

     private Integer AId;
     private String AFname;
     private String ALname;
     private String AMname;
     private Date ADob;
     private String ABio;


    // Constructors

    /** default constructor */
    public Author() {
    }

	/** minimal constructor */
    public Author(Integer AId) {
        this.AId = AId;
    }
    
    /** full constructor */
    public Author(Integer AId, String AFname, String ALname, String AMname, Date ADob, String ABio) {
        this.AId = AId;
        this.AFname = AFname;
        this.ALname = ALname;
        this.AMname = AMname;
        this.ADob = ADob;
        this.ABio = ABio;
    }

   
    // Property accessors
    @Id 
    
    @Column(name="A_ID", unique=true, nullable=false)

    public Integer getAId() {
        return this.AId;
    }
    
    public void setAId(Integer AId) {
        this.AId = AId;
    }
    
    @Column(name="A_FNAME", length=20)

    public String getAFname() {
        return this.AFname;
    }
    
    public void setAFname(String AFname) {
        this.AFname = AFname;
    }
    
    @Column(name="A_LNAME", length=20)

    public String getALname() {
        return this.ALname;
    }
    
    public void setALname(String ALname) {
        this.ALname = ALname;
    }
    
    @Column(name="A_MNAME", length=20)

    public String getAMname() {
        return this.AMname;
    }
    
    public void setAMname(String AMname) {
        this.AMname = AMname;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="A_DOB", length=10)

    public Date getADob() {
        return this.ADob;
    }
    
    public void setADob(Date ADob) {
        this.ADob = ADob;
    }
    
    @Column(name="A_BIO", length=500)

    public String getABio() {
        return this.ABio;
    }
    
    public void setABio(String ABio) {
        this.ABio = ABio;
    }
   








}