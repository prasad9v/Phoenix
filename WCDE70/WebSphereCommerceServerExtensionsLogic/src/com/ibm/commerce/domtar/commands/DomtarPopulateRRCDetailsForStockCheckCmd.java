/**
 * 
 */
package com.ibm.commerce.domtar.commands;

import org.apache.commons.json.JSONArtifact;

import com.ibm.websphere.command.CacheableCommand;

public interface DomtarPopulateRRCDetailsForStockCheckCmd extends
		CacheableCommand {
	public String getStoreId();
	public void setStoreId(String storeId);
	public String getLanguageId();
	public void setLanguageId(String languageId);
	public String getOrganizationId();
	public void setOrganizationId(String organizationId);
	public JSONArtifact getRrcAddressJsonArtifact();
}
