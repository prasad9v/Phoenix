package com.ibm.commerce.domtar.commands;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.ibm.commerce.command.CommandFactory;
import com.ibm.commerce.command.ControllerCommandImpl;
import com.ibm.commerce.datatype.TypedProperty;
import com.ibm.commerce.domtar.beans.DomtarInventoryHoldItemBean;
import com.ibm.commerce.domtar.databeans.DomtarHoldInputDataBean;
import com.ibm.commerce.domtar.databeans.DomtarHoldOutputDataBean;
import com.ibm.commerce.domtar.helpers.DomtarHelper;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.server.ECConstants;

public class DomtarTriggerHoldControllerCmdImpl extends ControllerCommandImpl
		implements DomtarTriggerHoldControllerCmd {
	
	private static final String M_DOMTARHOLDINFO_PAGE = "DomtarHoldInfoDisplay";
	private static final String CALLING_SYSTEM = "Ecom";
	private static final String SALES_SERVICE_ORG = "2";
	private static final String LANGUAGE = "ENG";
	
	public void performExecute() throws ECException {
		super.performExecute();
		TypedProperty rspProp = new TypedProperty();
		DomtarHoldInputDataBean inventoryHoldInput = populateInventoryHoldInputs(getRequestProperties());
		DomtarHoldOutputDataBean outPutBean =  invokeInventoryHoldWS(inventoryHoldInput);
		rspProp.put("hold_details", outPutBean);
		rspProp.put(ECConstants.EC_VIEWTASKNAME, M_DOMTARHOLDINFO_PAGE);
		setResponseProperties(rspProp);
	}
	/**
	 * Invoke Inventory Hold Web Service
	 * @param holdInputs
	 * @return
	 */
	private DomtarHoldOutputDataBean invokeInventoryHoldWS(DomtarHoldInputDataBean inventoryHoldInput) {
		
		DomtarInvokeInventoryHoldWSTaskCmd invokeInventoryHoldWSCmd = null;
		DomtarHoldOutputDataBean output = new DomtarHoldOutputDataBean();
		try {
			invokeInventoryHoldWSCmd = (DomtarInvokeInventoryHoldWSTaskCmd) CommandFactory.createCommand(
							"com.ibm.commerce.domtar.commands.DomtarInvokeInventoryHoldWSTaskCmd",getStoreId());
			invokeInventoryHoldWSCmd.setCommandContext(getCommandContext());
			invokeInventoryHoldWSCmd.setHoldInput(inventoryHoldInput);
			invokeInventoryHoldWSCmd.execute();
			output = invokeInventoryHoldWSCmd.getHoldResponseOutput();
		} catch (ECException e) {
			e.printStackTrace();
		}
		return output;
	}
	private DomtarHoldInputDataBean populateInventoryHoldInputs(TypedProperty requestProperties) {
		DomtarHoldInputDataBean holdInputs = new DomtarHoldInputDataBean();
		
		String userId = getCommandContext().getUserId().toString();
		
		String vSoldTo = requestProperties.getString("soldTo", "");
		String vHoldInfo =  requestProperties.getString("holdDetails", "");
		String vAdditionalHoldInfo =  requestProperties.getString("additionalHoldDetails", "");
		
		Calendar cal = Calendar.getInstance();
	    Date currentDate = cal.getTime();
	    SimpleDateFormat date_format = new SimpleDateFormat("yyyyMMddHHmmss");
		
		holdInputs.setTrackingId(date_format.format(currentDate));
		holdInputs.setSoldTo(vSoldTo);
		holdInputs.setContact("");
		holdInputs.setCallingSystem(CALLING_SYSTEM);
		holdInputs.setLanguage(LANGUAGE);
		holdInputs.setRequestorName(DomtarHelper.getLogonIdByUserId(userId));
		holdInputs.setServiceOrganisation(SALES_SERVICE_ORG);
		
		/*
		 * HoldInfoArray and AdditionalHoldInfoArray will be in the format 
		 * I2509|10/5/2013|Dallas<<SPACE>>RRC<<SPACE>>2:5|USD90
		 */
		String[] vHoldInfoArray = vHoldInfo.split(",");
		String[] vAdditionalHoldInfoArray =  vAdditionalHoldInfo.split(",");
		HashMap holdInfo = new HashMap();
		String holdId;
		String adlHoldId;
		String[] holdInfoVal = new String[7];
		List<DomtarInventoryHoldItemBean> inventoryHoldItemList = new ArrayList<DomtarInventoryHoldItemBean>();
		String shipTo = "";
		for(int i = 0 ; i < vHoldInfoArray.length ; i++){
			holdId = vHoldInfoArray[i].split(":")[0];
			adlHoldId = vAdditionalHoldInfoArray[i].split(":")[0];
			if(holdId.equals(adlHoldId)){
				DomtarInventoryHoldItemBean holdItemDetails = new DomtarInventoryHoldItemBean();
				//itemcode
				String itemCode = holdId.split("\\|")[0];
				holdItemDetails.setItemCode(itemCode);
				//prodShipdate
				String prodShipdate = holdId.split("\\|")[1];
				holdItemDetails.setProductShipDate(formatProdShipdate(prodShipdate));
				//sourcingLocation
				String sourcingLocation = holdId.split("\\|")[2];
				holdItemDetails.setSourcingLocation(sourcingLocation);
				//Quanity
				String quantity = vHoldInfoArray[i].split(":")[1].split("\\|")[0];
				holdItemDetails.setHoldQuanity(quantity);
				//Quanity Unit
				String quantityUnit = vAdditionalHoldInfoArray[i].split(":")[1].split("\\|")[0];
				holdItemDetails.setHoldQuantityUnit(quantityUnit);
				//Price
				String price = vHoldInfoArray[i].split(":")[1].split("\\|")[1];
				// ship to
				String itemShipTo = vAdditionalHoldInfoArray[i].split(":")[1].split("\\|")[1];
				holdItemDetails.setItemShipTo(itemShipTo);
				if (shipTo.equals("")){
					shipTo = itemShipTo;
				}
				inventoryHoldItemList.add(holdItemDetails);
			}
		}
		
		holdInputs.setShipTo(shipTo);
		holdInputs.setHoldItem(inventoryHoldItemList);

		return holdInputs;
	}
	private String formatProdShipdate(String prodShipDate) {
		DateFormat fromFormat = new SimpleDateFormat("MM/dd/yyyy");
		fromFormat.setLenient(false);
		DateFormat toFormat = new SimpleDateFormat("yyyyMMdd");
		toFormat.setLenient(false);
		String formatedDate = "";
		try {
			Date date = fromFormat.parse(prodShipDate);
			formatedDate = toFormat.format(date);
		} catch (ParseException e) {
			//Ignore
		}
		if(formatedDate.equals("")){
			String day = prodShipDate.substring(3, 5);
			String month = prodShipDate.substring(0, 2);
			String year = prodShipDate.substring(6, prodShipDate.length());
			formatedDate = year+month+day; 
			return formatedDate;
		}
		else{
			return formatedDate;
		}
	}
}
