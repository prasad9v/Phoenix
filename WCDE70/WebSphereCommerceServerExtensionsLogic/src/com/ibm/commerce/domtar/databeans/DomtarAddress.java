/**
 * 
 */
package com.ibm.commerce.domtar.databeans;

import java.io.Serializable;

/**
 * @author Rakesh Thawait
 *
 */
public class DomtarAddress 	implements Serializable, Comparable<DomtarAddress>{
	private String soldTo = null;
	private String shipTo = null;
	private String rrc = null;
	private String soldToName = null;
	private String shipToName = null;
	private String rrcName = null;
	
		/**
	 * @param soldTo
	 * @param shipTo
	 * @param rrc
	 */
	public DomtarAddress(String soldTo, String shipTo, String rrc) {
		super();
		this.soldTo = soldTo;
		this.shipTo = shipTo;
		this.rrc = rrc;
	}	
	public DomtarAddress() {
		super();		
	}
	/**
	 * @return the soldTo
	 */
	public String getSoldTo() {
		return soldTo;
	}
	/**
	 * @param soldTo the soldTo to set
	 */
	public void setSoldTo(String soldTo) {
		this.soldTo = soldTo;
	}
	/**
	 * @return the shipTo
	 */
	public String getShipTo() {
		return shipTo;
	}
	/**
	 * @param shipTo the shipTo to set
	 */
	public void setShipTo(String shipTo) {
		this.shipTo = shipTo;
	}
	/**
	 * @return the rrc
	 */
	public String getRrc() {
		return rrc;
	}
	/**
	 * @param rrc the rrc to set
	 */
	public void setRrc(String rrc) {
		this.rrc = rrc;
	}
	public String getSoldToName() {
		return soldToName;
	}
	public void setSoldToName(String soldToName) {
		this.soldToName = soldToName;
	}
	public String getShipToName() {
		return shipToName;
	}
	public void setShipToName(String shipToName) {
		this.shipToName = shipToName;
	}
	public String getRrcName() {
		return rrcName;
	}
	public void setRrcName(String rrcName) {
		this.rrcName = rrcName;
	}
	@Override
	//Sort sold to name as part of sort CR
	public int compareTo(DomtarAddress o) {		
		return this.soldToName.toLowerCase().compareTo(o.getSoldToName().toLowerCase());
	}
}
