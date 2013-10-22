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
 * Item entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="ITEM"
    ,schema="DB2ADMIN"
)

public class Item  implements java.io.Serializable {


    // Fields    

     private Integer IId;
     private String ITitle;
     private Integer IAId;
     private Date IPubDate;
     private String IPublisher;
     private String ISubject;
     private String IDesc;
     private Integer IRelated1;
     private Integer IRelated2;
     private Integer IRelated3;
     private Integer IRelated4;
     private Integer IRelated5;
     private String IThumbnail;
     private String IImage;
     private Double ISrp;
     private Double ICost;
     private Date IAvail;
     private Integer IStock;
     private String IIsbn;
     private Integer IPage;
     private String IBacking;
     private String IDimensions;


    // Constructors

    /** default constructor */
    public Item() {
    }

	/** minimal constructor */
    public Item(Integer IId) {
        this.IId = IId;
    }
    
    /** full constructor */
    public Item(Integer IId, String ITitle, Integer IAId, Date IPubDate, String IPublisher, String ISubject, String IDesc, Integer IRelated1, Integer IRelated2, Integer IRelated3, Integer IRelated4, Integer IRelated5, String IThumbnail, String IImage, Double ISrp, Double ICost, Date IAvail, Integer IStock, String IIsbn, Integer IPage, String IBacking, String IDimensions) {
        this.IId = IId;
        this.ITitle = ITitle;
        this.IAId = IAId;
        this.IPubDate = IPubDate;
        this.IPublisher = IPublisher;
        this.ISubject = ISubject;
        this.IDesc = IDesc;
        this.IRelated1 = IRelated1;
        this.IRelated2 = IRelated2;
        this.IRelated3 = IRelated3;
        this.IRelated4 = IRelated4;
        this.IRelated5 = IRelated5;
        this.IThumbnail = IThumbnail;
        this.IImage = IImage;
        this.ISrp = ISrp;
        this.ICost = ICost;
        this.IAvail = IAvail;
        this.IStock = IStock;
        this.IIsbn = IIsbn;
        this.IPage = IPage;
        this.IBacking = IBacking;
        this.IDimensions = IDimensions;
    }

   
    // Property accessors
    @Id 
    
    @Column(name="I_ID", unique=true, nullable=false)

    public Integer getIId() {
        return this.IId;
    }
    
    public void setIId(Integer IId) {
        this.IId = IId;
    }
    
    @Column(name="I_TITLE", length=60)

    public String getITitle() {
        return this.ITitle;
    }
    
    public void setITitle(String ITitle) {
        this.ITitle = ITitle;
    }
    
    @Column(name="I_A_ID")

    public Integer getIAId() {
        return this.IAId;
    }
    
    public void setIAId(Integer IAId) {
        this.IAId = IAId;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="I_PUB_DATE", length=10)

    public Date getIPubDate() {
        return this.IPubDate;
    }
    
    public void setIPubDate(Date IPubDate) {
        this.IPubDate = IPubDate;
    }
    
    @Column(name="I_PUBLISHER", length=60)

    public String getIPublisher() {
        return this.IPublisher;
    }
    
    public void setIPublisher(String IPublisher) {
        this.IPublisher = IPublisher;
    }
    
    @Column(name="I_SUBJECT", length=60)

    public String getISubject() {
        return this.ISubject;
    }
    
    public void setISubject(String ISubject) {
        this.ISubject = ISubject;
    }
    
    @Column(name="I_DESC", length=500)

    public String getIDesc() {
        return this.IDesc;
    }
    
    public void setIDesc(String IDesc) {
        this.IDesc = IDesc;
    }
    
    @Column(name="I_RELATED1")

    public Integer getIRelated1() {
        return this.IRelated1;
    }
    
    public void setIRelated1(Integer IRelated1) {
        this.IRelated1 = IRelated1;
    }
    
    @Column(name="I_RELATED2")

    public Integer getIRelated2() {
        return this.IRelated2;
    }
    
    public void setIRelated2(Integer IRelated2) {
        this.IRelated2 = IRelated2;
    }
    
    @Column(name="I_RELATED3")

    public Integer getIRelated3() {
        return this.IRelated3;
    }
    
    public void setIRelated3(Integer IRelated3) {
        this.IRelated3 = IRelated3;
    }
    
    @Column(name="I_RELATED4")

    public Integer getIRelated4() {
        return this.IRelated4;
    }
    
    public void setIRelated4(Integer IRelated4) {
        this.IRelated4 = IRelated4;
    }
    
    @Column(name="I_RELATED5")

    public Integer getIRelated5() {
        return this.IRelated5;
    }
    
    public void setIRelated5(Integer IRelated5) {
        this.IRelated5 = IRelated5;
    }
    
    @Column(name="I_THUMBNAIL", length=40)

    public String getIThumbnail() {
        return this.IThumbnail;
    }
    
    public void setIThumbnail(String IThumbnail) {
        this.IThumbnail = IThumbnail;
    }
    
    @Column(name="I_IMAGE", length=40)

    public String getIImage() {
        return this.IImage;
    }
    
    public void setIImage(String IImage) {
        this.IImage = IImage;
    }
    
    @Column(name="I_SRP", precision=53, scale=0)

    public Double getISrp() {
        return this.ISrp;
    }
    
    public void setISrp(Double ISrp) {
        this.ISrp = ISrp;
    }
    
    @Column(name="I_COST", precision=53, scale=0)

    public Double getICost() {
        return this.ICost;
    }
    
    public void setICost(Double ICost) {
        this.ICost = ICost;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="I_AVAIL", length=10)

    public Date getIAvail() {
        return this.IAvail;
    }
    
    public void setIAvail(Date IAvail) {
        this.IAvail = IAvail;
    }
    
    @Column(name="I_STOCK")

    public Integer getIStock() {
        return this.IStock;
    }
    
    public void setIStock(Integer IStock) {
        this.IStock = IStock;
    }
    
    @Column(name="I_ISBN", length=13)

    public String getIIsbn() {
        return this.IIsbn;
    }
    
    public void setIIsbn(String IIsbn) {
        this.IIsbn = IIsbn;
    }
    
    @Column(name="I_PAGE")

    public Integer getIPage() {
        return this.IPage;
    }
    
    public void setIPage(Integer IPage) {
        this.IPage = IPage;
    }
    
    @Column(name="I_BACKING", length=15)

    public String getIBacking() {
        return this.IBacking;
    }
    
    public void setIBacking(String IBacking) {
        this.IBacking = IBacking;
    }
    
    @Column(name="I_DIMENSIONS", length=25)

    public String getIDimensions() {
        return this.IDimensions;
    }
    
    public void setIDimensions(String IDimensions) {
        this.IDimensions = IDimensions;
    }
   








}