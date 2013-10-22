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
 * ShoppingCartLineId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
public class ShoppingCartLineId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4714804402903517633L;
	// Fields
	private Integer sclScId;
	private Integer sclIId;

	// Constructors

	/** default constructor */
	public ShoppingCartLineId() {
	}

	/** full constructor */
	public ShoppingCartLineId(Integer sclScId, Integer sclIId) {
		this.sclScId = sclScId;
		this.sclIId = sclIId;
	}

	// Property accessors

	@Column(name = "SCL_SC_ID", nullable = false)
	public Integer getSclScId() {
		return this.sclScId;
	}

	public void setSclScId(Integer sclScId) {
		this.sclScId = sclScId;
	}

	@Column(name = "SCL_I_ID", nullable = false)
	public Integer getSclIId() {
		return this.sclIId;
	}

	public void setSclIId(Integer sclIId) {
		this.sclIId = sclIId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ShoppingCartLineId))
			return false;
		ShoppingCartLineId castOther = (ShoppingCartLineId) other;

		return ((this.getSclScId() == castOther.getSclScId()) || (this
				.getSclScId() != null
				&& castOther.getSclScId() != null && this.getSclScId().equals(
				castOther.getSclScId())))
				&& ((this.getSclIId() == castOther.getSclIId()) || (this
						.getSclIId() != null
						&& castOther.getSclIId() != null && this.getSclIId()
						.equals(castOther.getSclIId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getSclScId() == null ? 0 : this.getSclScId().hashCode());
		result = 37 * result
				+ (getSclIId() == null ? 0 : this.getSclIId().hashCode());
		return result;
	}

}