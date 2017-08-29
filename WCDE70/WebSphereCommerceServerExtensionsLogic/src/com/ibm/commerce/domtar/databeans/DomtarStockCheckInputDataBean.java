package com.ibm.commerce.domtar.databeans;

public class DomtarStockCheckInputDataBean {
	/**
	 * Current assumption is that the BAAN will resolve RRC and Ship-to from country,
	 *  state and city combination and also current date will be taken by BAAN
	 */
	private String customerSoldTo;
	private String country;
	private String state;
	private String city;
	private String serviceOrganisation;
	private String[] items;
	private String[] quanity;
	private String[] quanityUnit;
	private String userId;
	private String currency;
	private boolean isAlternateOffer;
	private String langId;
	private String contactRep;
	
	public String getContactRep() {
		return contactRep;
	}
	public void setContactRep(String contactRep) {
		this.contactRep = contactRep;
	}
	/**
	 * @return the customerSoldTo
	 */
	public String getCustomerSoldTo() {
		return customerSoldTo;
	}
	/**
	 * @param customerSoldTo the customerSoldTo to set
	 */
	public void setCustomerSoldTo(String customerSoldTo) {
		this.customerSoldTo = customerSoldTo;
	}
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
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
	/**
	 * @return the items
	 */
	public String[] getItems() {
		return items;
	}
	/**
	 * @param items the items to set
	 */
	public void setItems(String[] items) {
		this.items = items;
	}
	/**
	 * @return the quanity
	 */
	public String[] getQuanity() {
		return quanity;
	}
	/**
	 * @param quanity the quanity to set
	 */
	public void setQuanity(String[] quanity) {
		this.quanity = quanity;
	}
	/**
	 * @return the quanityUnit
	 */
	public String[] getQuanityUnit() {
		return quanityUnit;
	}
	/**
	 * @param quanityUnit the quanityUnit to set
	 */
	public void setQuanityUnit(String[] quanityUnit) {
		this.quanityUnit = quanityUnit;
	}
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}
	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	/**
	 * @return the isAlternateOffer
	 */
	public boolean isAlternateOffer() {
		return isAlternateOffer;
	}
	/**
	 * @param isAlternateOffer the isAlternateOffer to set
	 */
	public void setAlternateOffer(boolean isAlternateOffer) {
		this.isAlternateOffer = isAlternateOffer;
	}
	/**
	 * @return the landId
	 */
	public String getLangId() {
		return langId;
	}
	/**
	 * @param langId the langId to set
	 */
	public void setLangId(String langId) {
		this.langId = langId;
	}
}
