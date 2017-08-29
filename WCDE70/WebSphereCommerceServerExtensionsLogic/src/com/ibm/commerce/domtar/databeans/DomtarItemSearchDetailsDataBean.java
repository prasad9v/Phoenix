package com.ibm.commerce.domtar.databeans;

import java.util.List;

import com.ibm.commerce.catalog.beans.CatalogEntryDataBean;

public class DomtarItemSearchDetailsDataBean {

	private CatalogEntryDataBean itemDetails =null;
	private List<com.ibm.commerce.domtar.databeans.DomtarStockCheckOutputDataBean> stockCheckDetails;
	private int rowNumber = 0;
	/**
	 * @return the itemDetails
	 */
	public CatalogEntryDataBean getItemDetails() {
		return itemDetails;
	}
	/**
	 * @param itemDetails the itemDetails to set
	 */
	public void setItemDetails(CatalogEntryDataBean itemDetails) {
		this.itemDetails = itemDetails;
	}
	/**
	 * @return the stockCheckDetails
	 */
	public List<com.ibm.commerce.domtar.databeans.DomtarStockCheckOutputDataBean> getStockCheckDetails() {
		return stockCheckDetails;
	}
	/**
	 * @param stockCheckDetails the stockCheckDetails to set
	 */
	public void setStockCheckDetails(
			List<com.ibm.commerce.domtar.databeans.DomtarStockCheckOutputDataBean> stockCheckDetails) {
		this.stockCheckDetails = stockCheckDetails;
	}
	/**
	 * @return the rowNumber
	 */
	public int getRowNumber() {
		return rowNumber;
	}
	/**
	 * @param rowNumber the rowNumber to set
	 */
	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}
	
}
