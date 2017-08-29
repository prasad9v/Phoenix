package com.ibm.commerce.domtar.databeans;





public class CustomerTonnageBean {
	
	String customerPo =null;
    String  requestedShipDate =null;
	String  customerPO =null;
	String domtarSalesOrdNum =null;
    String  itemDesc =null;
    String itemCode =null;
    String []atrributes =null;
    String  quantity =null;
    String  totalWeight =null;
    String  orderId =null;
    String  beginIndex =null;
    String  pageNumebr= null;
    String  totalRecords =null;
    String  totalTonnageForPeriod =null;
    String weightType = null;
    String hotOrder = null;
    
    
    /**
	 * @return the weightType
	 */
	public String getWeightType() {
		return weightType;
	}
	/**
	 * @param weightType the weightType to set
	 */
	public void setWeightType(String weightType) {
		this.weightType = weightType;
	}
	public String getTotalTonnageForPeriod() {
		return totalTonnageForPeriod;
	}
	public void setTotalTonnageForPeriod(String totalTonnageForPeriod) {
		this.totalTonnageForPeriod = totalTonnageForPeriod;
	}
	public String getRequestedShipDate() {
		return requestedShipDate;
	}
	public void setRequestedShipDate(String requestedShipDate) {
		this.requestedShipDate = requestedShipDate;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getCustomerPo() {
		return customerPo;
	}
	public void setCustomerPo(String customerPo) {
		this.customerPo = customerPo;
	}
	
	public String getCustomerPO() {
		return customerPO;
	}
	public void setCustomerPO(String customerPO) {
		this.customerPO = customerPO;
	}
	public String getDomtarSalesOrdNum() {
		return domtarSalesOrdNum;
	}
	public void setDomtarSalesOrdNum(String domtarSalesOrdNum) {
		this.domtarSalesOrdNum = domtarSalesOrdNum;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String[] getAtrributes() {
		return atrributes;
	}
	public void setAtrributes(String[] atrributes) {
		this.atrributes = atrributes;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getTotalWeight() {
		return totalWeight;
	}
	public void setTotalWeight(String totalWeight) {
		this.totalWeight = totalWeight;
	}
	/**
	 * @return the hotOrder
	 */
	public String getHotOrder() {
		return hotOrder;
	}
	/**
	 * @param hotOrder the hotOrder to set
	 */
	public void setHotOrder(String hotOrder) {
		this.hotOrder = hotOrder;
	}

    

}
