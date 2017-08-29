package com.ibm.commerce.domtar.commands;
import org.apache.commons.json.JSONArtifact;
import com.ibm.commerce.command.ControllerCommandImpl;
import com.ibm.commerce.datatype.TypedProperty;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.exception.ExceptionHandler;


public class DomtarAjaxFilterpopulateControllerCmdImpl extends
		ControllerCommandImpl implements DomtarAjaxFilterpopulateControllerCmd {
	
	private static final String M_JSON_OBJ = "JsonObj";
	
	public void performExecute() throws ECException {
		super.performExecute();
		long vEntryPoint = System.currentTimeMillis();
		String vStoreId = getRequestProperties().getString("storeId","");
		String vCatalogId = getRequestProperties().getString("catalogId","");
		String vLanguageId = getRequestProperties().getString("langId","");
		
		DomtarFetchFilterDetailsCmd fetchFilterDetailsCmd = new DomtarFetchFilterDetailsCmdImpl();
		fetchFilterDetailsCmd.setStoreId(vStoreId);
		fetchFilterDetailsCmd.setCatalogId(vCatalogId);
		fetchFilterDetailsCmd.setLanguageId(vLanguageId);
		fetchFilterDetailsCmd.setTradingAgreements(getCommandContext().getCurrentTradingAgreementIdsAsString());
				
		TypedProperty rspProp = new TypedProperty();	
		try {
			fetchFilterDetailsCmd.execute();
			JSONArtifact jsonFilterArtifact = fetchFilterDetailsCmd.getJSONFilterArtifact();
			if(jsonFilterArtifact != null){
				rspProp.put(M_JSON_OBJ, jsonFilterArtifact);
			}
		} catch (Exception e) {
			ExceptionHandler.convertToECException(e);
		} 	
		setResponseProperties(rspProp);
		long vExitPoint = System.currentTimeMillis();
		long TotCallDiff = (vExitPoint-vEntryPoint);
		System.out.println("Time for total Call is >>>>>>"+TotCallDiff+" ms");
	}
	
}
