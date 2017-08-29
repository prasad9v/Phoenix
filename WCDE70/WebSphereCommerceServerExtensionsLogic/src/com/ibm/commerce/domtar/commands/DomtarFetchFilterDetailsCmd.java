package com.ibm.commerce.domtar.commands;

import java.util.Map;

import org.apache.commons.json.JSONArtifact;

import com.ibm.websphere.command.CacheableCommand;

/**
 * Interface for the command to cache the filter details
 * @author shanawas
 *
 */
public interface DomtarFetchFilterDetailsCmd extends CacheableCommand {
	public String getStoreId();
	public void setStoreId(String storeId);
	public String getCatalogId();
	public void setCatalogId(String catalogId);
	public String getLanguageId();
	public void setLanguageId(String languageId);	
	public String getTradingAgreements();
	public void setTradingAgreements(String tradingAgreements);	
	public JSONArtifact getJSONFilterArtifact();
	public Map getItemDetails();
}
