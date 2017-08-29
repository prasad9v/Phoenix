package com.ibm.commerce.domtar.commands;

import java.util.List;

import org.apache.commons.json.JSONArtifact;

import com.ibm.commerce.domtar.databeans.DomtarAddress;
import com.ibm.websphere.command.CacheableCommand;

public interface DomtarPopulateUserAddressListCmd extends CacheableCommand {
	public String getStoreId();
	public void setStoreId(String storeId);
	public String getLanguageId();
	public void setLanguageId(String languageId);
	public String getOrganizationId();
	public void setOrganizationId(String organizationId);
	public List<DomtarAddress> getUserAddressList();
	public JSONArtifact getAddressRelationJsonArtifact();
}
