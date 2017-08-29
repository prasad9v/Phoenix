package com.ibm.commerce.domtar.databeans;

import java.util.HashMap;
import java.util.List;

/**
 * @author dona
 *
 */
public class DomtarStockCheckOutputDataBean {
	private String partNumber;
	private String rrcInfo;
	private String prodShipDate;
	private String quantityOnHold;
	private String quantityUnit;
	private String quantityAvaialable;
	private String price;
	private String priceCurrency;
	private String cstHoldQuantity;
	private List<DomtarStockCheckOutputDataBean> replinishmentDetails;
	private String alternativeItem1;
	private String alternativeItem2;
	private String[] itemErrorCodes;
	private String[] callErrorCodes;
	private String disclaimer;
	private boolean isReplinishmentExist = false;
	private boolean isAlternativeOffer1Exist = false;
	private boolean isAlternativeOffer2Exist = false;
	private boolean isPriceMissing = false;
	private HashMap<String, String> itemErrorMap;
	private String stockCheckWebServiceCallError;
	private String rrcName;
	/**
	 * @return the partNumber
	 */
	public String getPartNumber() {
		return partNumber;
	}
	/**
	 * @param partNumber the partNumber to set
	 */
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	/**
	 * @return the rrcInfo
	 */
	public String getRrcInfo() {
		return rrcInfo;
	}
	/**
	 * @param rrcInfo the rrcInfo to set
	 */
	public void setRrcInfo(String rrcInfo) {
		this.rrcInfo = rrcInfo;
	}
	/**
	 * @return the prodShipDate
	 */
	public String getProdShipDate() {
		return prodShipDate;
	}
	/**
	 * @param prodShipDate the prodShipDate to set
	 */
	public void setProdShipDate(String prodShipDate) {
		this.prodShipDate = prodShipDate;
	}
	/**
	 * @return the quantityOnHold
	 */
	public String getQuantityOnHold() {
		return quantityOnHold;
	}
	/**
	 * @param quantityOnHold the quantityOnHold to set
	 */
	public void setQuantityOnHold(String quantityOnHold) {
		this.quantityOnHold = quantityOnHold;
	}
	/**
	 * @return the quantityUnit
	 */
	public String getQuantityUnit() {
		return quantityUnit;
	}
	/**
	 * @param quantityUnit the quantityUnit to set
	 */
	public void setQuantityUnit(String quantityUnit) {
		this.quantityUnit = quantityUnit;
	}
	/**
	 * @return the quantityAvaialable
	 */
	public String getQuantityAvaialable() {
		return quantityAvaialable;
	}
	/**
	 * @param quantityAvaialable the quantityAvaialable to set
	 */
	public void setQuantityAvaialable(String quantityAvaialable) {
		this.quantityAvaialable = quantityAvaialable;
	}
	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
	}
	/**
	 * @return the priceCurrency
	 */
	public String getPriceCurrency() {
		return priceCurrency;
	}
	/**
	 * @param priceCurrency the priceCurrency to set
	 */
	public void setPriceCurrency(String priceCurrency) {
		this.priceCurrency = priceCurrency;
	}
	/**
	 * @return the cstHoldQuantity
	 */
	public String getCstHoldQuantity() {
		return cstHoldQuantity;
	}
	/**
	 * @param cstHoldQuantity the cstHoldQuantity to set
	 */
	public void setCstHoldQuantity(String cstHoldQuantity) {
		this.cstHoldQuantity = cstHoldQuantity;
	}
	/**
	 * @return the replinishmentDetails
	 */
	public List<DomtarStockCheckOutputDataBean> getReplinishmentDetails() {
		return replinishmentDetails;
	}
	/**
	 * @param replinishmentDetails the replinishmentDetails to set
	 */
	public void setReplinishmentDetails(
			List<DomtarStockCheckOutputDataBean> replinishmentDetails) {
		this.replinishmentDetails = replinishmentDetails;
	}
	/**
	 * @return the alternativeItem1
	 */
	public String getAlternativeItem1() {
		return alternativeItem1;
	}
	/**
	 * @param alternativeItem1 the alternativeItem1 to set
	 */
	public void setAlternativeItem1(String alternativeItem1) {
		this.alternativeItem1 = alternativeItem1;
	}
	/**
	 * @return the alternativeItem2
	 */
	public String getAlternativeItem2() {
		return alternativeItem2;
	}
	/**
	 * @param alternativeItem2 the alternativeItem2 to set
	 */
	public void setAlternativeItem2(String alternativeItem2) {
		this.alternativeItem2 = alternativeItem2;
	}
	/**
	 * @return the itemErrorCodes
	 */
	public String[] getItemErrorCodes() {
		return itemErrorCodes;
	}
	/**
	 * @param itemErrorCodes the itemErrorCodes to set
	 */
	public void setItemErrorCodes(String[] itemErrorCodes) {
		this.itemErrorCodes = itemErrorCodes;
	}
	/**
	 * @return the callErrorCodes
	 */
	public String[] getCallErrorCodes() {
		return callErrorCodes;
	}
	/**
	 * @param callErrorCodes the callErrorCodes to set
	 */
	public void setCallErrorCodes(String[] callErrorCodes) {
		this.callErrorCodes = callErrorCodes;
	}
	/**
	 * @return the isReplinishmentExist
	 */
	public boolean isReplinishmentExist() {
		return isReplinishmentExist;
	}
	/**
	 * @param isReplinishmentExist the isReplinishmentExist to set
	 */
	public void setReplinishmentExist(boolean isReplinishmentExist) {
		this.isReplinishmentExist = isReplinishmentExist;
	}
	/**
	 * @return the isAlternativeOffer1Exist
	 */
	public boolean isAlternativeOffer1Exist() {
		return isAlternativeOffer1Exist;
	}
	/**
	 * @param isAlternativeOffer1Exist the isAlternativeOffer1Exist to set
	 */
	public void setAlternativeOffer1Exist(boolean isAlternativeOffer1Exist) {
		this.isAlternativeOffer1Exist = isAlternativeOffer1Exist;
	}
	/**
	 * @return the isAlternativeOffer2Exist
	 */
	public boolean isAlternativeOffer2Exist() {
		return isAlternativeOffer2Exist;
	}
	/**
	 * @param isAlternativeOffer2Exist the isAlternativeOffer2Exist to set
	 */
	public void setAlternativeOffer2Exist(boolean isAlternativeOffer2Exist) {
		this.isAlternativeOffer2Exist = isAlternativeOffer2Exist;
	}
	/**
	 * @return the disclaimer
	 */
	public String getDisclaimer() {
		return disclaimer;
	}
	/**
	 * @param disclaimer the disclaimer to set
	 */
	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}
	/**
	 * @return if price is Missing from Baan
	 */
	public boolean isPriceMissing() {
		return isPriceMissing;
	}
	
	public void setPriceMissing(boolean isPriceMissing) {
		this.isPriceMissing = isPriceMissing;
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
	 * @return the stockCheckWebServiceCallError
	 */
	public String getStockCheckWebServiceCallError() {
		return stockCheckWebServiceCallError;
	}
	/**
	 * @param stockCheckWebServiceCallError the stockCheckWebServiceCallError to set
	 */
	public void setStockCheckWebServiceCallError(String stockCheckWebServiceCallError) {
		this.stockCheckWebServiceCallError = stockCheckWebServiceCallError;
	}
	public String getRrcName() {
		return rrcName;
	}
	public void setRrcName(String rrcName) {
		this.rrcName = rrcName;
	}
}
