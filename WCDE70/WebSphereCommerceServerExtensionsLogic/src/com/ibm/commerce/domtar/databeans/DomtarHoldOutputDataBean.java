package com.ibm.commerce.domtar.databeans;

import java.util.HashMap;
import java.util.List;

import com.ibm.commerce.domtar.beans.DomtarInventoryHoldItemBean;

public class DomtarHoldOutputDataBean {
	
	private String trackingId;
	private String holdTrackingNumber;
	private String holdDuartion;
	private String holdDate;
	private String holdTime;
	
	private List <DomtarInventoryHoldItemBean> holdItem;
	private HashMap<String, String> itemErrorMap;
	private String holdWebServiceCallError;
	/**
	 * @return the trackingId
	 */
	public String getTrackingId() {
		return trackingId;
	}
	/**
	 * @param trackingId the trackingId to set
	 */
	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}
	/**
	 * @return the holdTrackingNumber
	 */
	public String getHoldTrackingNumber() {
		return holdTrackingNumber;
	}
	/**
	 * @param holdTrackingNumber the holdTrackingNumber to set
	 */
	public void setHoldTrackingNumber(String holdTrackingNumber) {
		this.holdTrackingNumber = holdTrackingNumber;
	}
	/**
	 * @return the holdDuartion
	 */
	public String getHoldDuartion() {
		return holdDuartion;
	}
	/**
	 * @param holdDuartion the holdDuartion to set
	 */
	public void setHoldDuartion(String holdDuartion) {
		this.holdDuartion = holdDuartion;
	}
	/**
	 * @return the holdDate
	 */
	public String getHoldDate() {
		return holdDate;
	}
	/**
	 * @param holdDate the holdDate to set
	 */
	public void setHoldDate(String holdDate) {
		this.holdDate = holdDate;
	}
	/**
	 * @return the holdTime
	 */
	public String getHoldTime() {
		return holdTime;
	}
	/**
	 * @param holdTime the holdTime to set
	 */
	public void setHoldTime(String holdTime) {
		this.holdTime = holdTime;
	}
	/**
	 * @return the holdItem
	 */
	public List<DomtarInventoryHoldItemBean> getHoldItem() {
		return holdItem;
	}
	/**
	 * @param holdItem the holdItem to set
	 */
	public void setHoldItem(List<DomtarInventoryHoldItemBean> holdItem) {
		this.holdItem = holdItem;
	}
	/**
	 * @return the itemErrorMap
	 */
	public HashMap<String, String> getItemErrorMap() {
		return itemErrorMap;
	}
	/**
	 * @param itemErrorMap the itemErrorMap to set
	 */
	public void setItemErrorMap(HashMap<String, String> itemErrorMap) {
		this.itemErrorMap = itemErrorMap;
	}
	/**
	 * @return the holdWebServiceCallError
	 */
	public String getHoldWebServiceCallError() {
		return holdWebServiceCallError;
	}
	/**
	 * @param holdWebServiceCallError the holdWebServiceCallError to set
	 */
	public void setHoldWebServiceCallError(String holdWebServiceCallError) {
		this.holdWebServiceCallError = holdWebServiceCallError;
	}
	
}
