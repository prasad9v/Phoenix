package com.ibm.commerce.domtar.commands;

import java.util.Enumeration;

import org.apache.commons.json.JSONArray;
import org.apache.commons.json.JSONException;
import org.apache.commons.json.JSONObject;

import com.ibm.commerce.command.CommandFactory;
import com.ibm.commerce.command.ControllerCommandImpl;
import com.ibm.commerce.datatype.TypedProperty;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.messaging.commands.SendMsgCmd;
import com.ibm.commerce.ras.ECTrace;
import com.ibm.commerce.ras.ECTraceIdentifiers;
import com.ibm.commerce.user.objects.AddressAccessBean;

public class DomtarEmailPODControllerCmdImpl extends
		ControllerCommandImpl implements DomtarEmailPODControllerCmd {
	
	public static final String CLASS_NAME = "com.ibm.commerce.domtar.commands.DomtarEmailPODControllerCmdImpl";
	
	private static final String MESSAGE_TYPE_NAME = "PODEmailNotify";
	private static final String DOMTAR_EMAIL_ADDRESS = "DomtarOnDemand@domtar.com";
	private static final String POD_EMAIL_SUBJECT = "Proof Of Delivery Request";
	private boolean isEmailSuccessful = true;
	private String page;
	
	
	@Override
	public void performExecute() throws ECException {
		
		super.performExecute();
		responseProperties = new TypedProperty();
		sendEmail();
		String vResponseMsg;
		if (isEmailSuccessful){
			vResponseMsg = "EMAIL_SEND";
			
		}else {
			vResponseMsg = "EMAIL_NOT_SEND";
		}
		responseProperties.put("POD_RESPONSE", vResponseMsg);
		setResponseProperties(responseProperties);
	}

	private void sendEmail() throws ECException {

        final String methodName = "sendEmail";
        ECTrace.entry(ECTraceIdentifiers.COMPONENT_EXTERN, CLASS_NAME, methodName);
        try {
        	Long userId = getCommandContext().getUserId();
    			
            String userEmailAddr="user@domtarondemand.com";
   			Enumeration<AddressAccessBean> enmAddressList = (new AddressAccessBean()).findByMemberId(userId);
   			if(enmAddressList.hasMoreElements()) {
   				AddressAccessBean addressbean = (AddressAccessBean)enmAddressList.nextElement();
   				userEmailAddr = addressbean.getEmail1();
   			}
            SendMsgCmd cmdSendMsg = (SendMsgCmd)CommandFactory.createCommand(SendMsgCmd.NAME, getStoreId());
            cmdSendMsg.setMsgType(MESSAGE_TYPE_NAME);
            cmdSendMsg.setStoreID(getStoreId());
               
            cmdSendMsg.setConfigData("subject", POD_EMAIL_SUBJECT);
            cmdSendMsg.setConfigData("sender",userEmailAddr);
            cmdSendMsg.setConfigData("recipient", DOMTAR_EMAIL_ADDRESS);
            //cmdSendMsg.setConfigData("contentType", "text/html");
                    
            TypedProperty msgParms = new TypedProperty();
            String msgContent = generateEmailContent(getRequestProperties().getString("podRequest",""));
//            msgParms.put("podRequests",msgContent);
//            cmdSendMsg.compose("PODEmailNotifyView", getCommandContext(), msgParms);
            cmdSendMsg.setContent(null, "-1", msgContent.getBytes());
            cmdSendMsg.sendTransacted(); 
            cmdSendMsg.setCommandContext(getCommandContext());
            cmdSendMsg.execute();
        }
        catch(ECException e){
        	isEmailSuccessful = false;
            ECTrace.trace(ECTraceIdentifiers.COMPONENT_EXTERN, CLASS_NAME, methodName, "Notification not sent because exception was thrown from Messaging.");
        }
        catch(Exception e){
        	isEmailSuccessful = false;
            ECTrace.trace(ECTraceIdentifiers.COMPONENT_EXTERN, CLASS_NAME, methodName, "Notification not sent because exception was thrown.");
        }
        
        ECTrace.exit(ECTraceIdentifiers.COMPONENT_EXTERN, CLASS_NAME, methodName); 
		
	}

	private String generateEmailContent(String podRequest) throws JSONException {
		if("".equals(podRequest)){
			return podRequest;
		}
		
		StringBuffer emailContent = new StringBuffer("POD Requests\n");
		emailContent.append("======================\n");
		JSONObject json = new JSONObject(podRequest);
		JSONArray podRequests = (JSONArray) json.get("PODRequests");
		
		String bol = null;
		String bolShipId = null;
		String bolCustomerPO = null;
		String itemNumber = null;
		String itemStatus = null;
		String carrierName = null;
		String deliveryDate = null;
		String customerName = null;
		String customerCity = null;
		String consigneeName = null;
		String consigneeCity = null;
		String consigneeState = null;
		String customerSalesRep = null;
		
		for (Object object : podRequests) {
			JSONObject jsonobj = (JSONObject) object;
			bol = (String) jsonobj.get("bol");
			bolShipId = (String) jsonobj.get("bolShipId");
			bolCustomerPO = (String) jsonobj.get("bolCustomerPO");
			itemNumber = (String) jsonobj.get("itemNumber");
			if (page.equals("orderStatus")){
				itemStatus = (String) jsonobj.get("itemStatus");
			}else{
				carrierName = (String) jsonobj.get("carrierName");
				deliveryDate = (String) jsonobj.get("deliveryDate");
				customerName = (String) jsonobj.get("customerName");
				customerCity = (String) jsonobj.get("customerCity");
				consigneeName = (String) jsonobj.get("consigneeName");
				consigneeCity = (String) jsonobj.get("consigneeCity");
				consigneeState = (String) jsonobj.get("consigneeState");
				customerSalesRep = (String) jsonobj.get("customerSalesRep");
			}			
			
			if(bol!=null){
				emailContent.append("bol").append(" = ").append(bol).append("\n");
			}
			if(bolShipId!=null){
				emailContent.append("bolShipId").append(" = ").append(bolShipId).append("\n");
			}
			if(bolCustomerPO!=null){
				emailContent.append("bolCustomerPO").append(" = ").append(bolCustomerPO).append("\n");
			}
			if(itemNumber!=null){
				emailContent.append("itemNumber").append(" = ").append(itemNumber).append("\n");
			}
			if(itemStatus!=null){
				emailContent.append("itemStatus").append(" = ").append(itemStatus).append("\n");
			}
			if(carrierName!=null){
				emailContent.append("carrierName").append(" = ").append(carrierName).append("\n");
			}
			if(deliveryDate!=null){
				emailContent.append("deliveryDate").append(" = ").append(deliveryDate).append("\n");
			}
			if(customerName!=null){
				emailContent.append("customerName").append(" = ").append(customerName).append("\n");
			}
			if(customerCity!=null){
				emailContent.append("customerCity").append(" = ").append(customerCity).append("\n");
			}
			if(consigneeName!=null){
				emailContent.append("consigneeName").append(" = ").append(consigneeName).append("\n");
			}
			if(consigneeCity!=null){
				emailContent.append("consigneeCity").append(" = ").append(consigneeCity).append("\n");
			}
			if(consigneeState!=null){
				emailContent.append("consigneeState").append(" = ").append(consigneeState).append("\n");
			}
			if(customerSalesRep!=null){
				emailContent.append("customerSalesRep").append(" = ").append(customerSalesRep).append("\n");
			}
			emailContent.append("\n\n");
		}
		
		return emailContent.toString();
	}

	@Override
	public TypedProperty getRequestProperties() {
		return super.getRequestProperties();
		
	}

	@Override
	public void setRequestProperties(TypedProperty pReqProperties)
			throws ECException {
		super.setRequestProperties(pReqProperties);
		page = getRequestProperties().getString("page","");
	}
}
