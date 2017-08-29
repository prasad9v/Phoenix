package com.ibm.commerce.domtar.commands;

import java.util.List;

import org.apache.commons.json.JSONArray;
import org.apache.commons.json.JSONArtifact;
import org.apache.commons.json.JSONObject;

import com.ibm.commerce.command.ControllerCommandImpl;
import com.ibm.commerce.context.entitlement.EntitlementContext;
import com.ibm.commerce.datatype.TypedProperty;
import com.ibm.commerce.domtar.databeans.DomtarAddress;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.exception.ExceptionHandler;

public class DomtarAjaxFetchShipToAddressCmdImpl extends ControllerCommandImpl
		implements DomtarAjaxFetchShipToAddressCmd {
	private static final String M_JSON_OBJ = "JsonObj";

	/* (non-Javadoc)
	 * @see com.ibm.commerce.command.AbstractECTargetableCommand#performExecute()
	 */
	@Override
	public void performExecute() throws ECException {
		
		String vStoreId = getRequestProperties().getString("storeId","");
		String vSoldTo = getRequestProperties().getString("soldTo","");
		String vLangId = getRequestProperties().getString("langId","-1");
		
		//Get the organization of the user as ID
		String organizationId = null;
		try {
			EntitlementContext ctx = (EntitlementContext) getCommandContext()
			.getContext("com.ibm.commerce.context.entitlement.EntitlementContext");
			if (ctx != null) {
				organizationId = ctx.getActiveOrganizationId().toString();
			} else {
				organizationId = getCommandContext().getUser().getParentMemberId();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		/*
		 * Populate the address relation JSON by calling the cached command.
		 */
		DomtarPopulateUserAddressListCmd populateAddressRelationCmd = null;
		JSONArtifact jsonAddressRelArtifact = null;
		List<DomtarAddress> addressList = null;
		JSONObject shipToJSONObj = new JSONObject();
		JSONArray shipToJSONArray = new JSONArray();
		TypedProperty rspProp = new TypedProperty();	
		try {
			populateAddressRelationCmd = new DomtarPopulateUserAddressListCmdImpl();
			populateAddressRelationCmd.setStoreId(vStoreId);
			populateAddressRelationCmd.setLanguageId(vLangId);
			populateAddressRelationCmd.setOrganizationId(organizationId);				
			populateAddressRelationCmd.execute();
			
			//get the address relation json from cache
			jsonAddressRelArtifact = populateAddressRelationCmd.getAddressRelationJsonArtifact();			
			
			JSONObject xAddrRelJSONObj = (JSONObject) jsonAddressRelArtifact;
			JSONArray jsonArray = xAddrRelJSONObj.getJSONArray("xAddrRel");
			for (int i = 0, size = jsonArray.length(); i < size; i++){
		      	JSONObject objectInArray = jsonArray.getJSONObject(i);
		      	String soldToId = objectInArray.getString("SoldTo");
		      	//Filter the shipto based on the selected soldto
		      	if(vSoldTo.equals(soldToId)){
			      	shipToJSONArray.put(objectInArray);
		      	}
		    }
			shipToJSONObj.put("xAddrRel", shipToJSONArray);
		} catch (Exception e) {
			ExceptionHandler.convertToECException(e);
		}
		rspProp.put(M_JSON_OBJ, shipToJSONObj);
		setResponseProperties(rspProp);
	}
}
