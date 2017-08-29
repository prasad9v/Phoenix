package com.ibm.commerce.domtar.databeans;

import java.util.List;

import com.ibm.commerce.domtar.beans.DomtarInventoryHoldItemBean;


public class DomtarHoldInputDataBean {

	private String trackingId;
	private String soldTo;
	private String contact;
	private String shipTo;
	private String callingSystem;
	private String language;
	private String requestorName;
	private String serviceOrganisation;
	
	private List <DomtarInventoryHoldItemBean> holdItem;
	
	/**
	 * @return the trackingId
	 */
	public String getTrackingId() {
		return trackingId;
	}
	/**
	 * @param trackingId the TrackingId to set
	 */
	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
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
	 * @return the contactRepId
	 */
	public String getContact() {
		return contact;
	}
	/**
	 * @param contactRepId the contactRepId to set
	 */
	public void setContact(String contact) {
		this.contact = contact;
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
	 * @return the callingSystem
	 */
	public String getCallingSystem() {
		return callingSystem;
	}
	/**
	 * @param callingSystem the callingSystem to set
	 */
	public void setCallingSystem(String callingSystem) {
		this.callingSystem = callingSystem;
	}
	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}
	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	/**
	 * @return the requestorName
	 */
	public String getRequestorName() {
		return requestorName;
	}
	/**
	 * @param requestorName the requestorName to set
	 */
	public void setRequestorName(String requestorName) {
		this.requestorName = requestorName;
	}
	/**
	 * @return the serviceOrganisation
	 */
	public String getServiceOrganisation() {
		return serviceOrganisation;
	}
	/**
	 * @param serviceOrganisation the serviceOrganisation to set
	 */
	public void setServiceOrganisation(String serviceOrganisation) {
		this.serviceOrganisation = serviceOrganisation;
	}
	public List<DomtarInventoryHoldItemBean> getHoldItem() {
		return holdItem;
	}
	public void setHoldItem(List<DomtarInventoryHoldItemBean> holdItem) {
		this.holdItem = holdItem;
	}
	
}
