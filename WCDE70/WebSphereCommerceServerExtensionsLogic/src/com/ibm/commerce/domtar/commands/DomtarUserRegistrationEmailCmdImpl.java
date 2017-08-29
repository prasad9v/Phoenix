package com.ibm.commerce.domtar.commands;

import java.util.logging.Logger;

import com.ibm.commerce.command.CommandFactory;
import com.ibm.commerce.command.ControllerCommandImpl;
import com.ibm.commerce.datatype.TypedProperty;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.foundation.common.util.logging.LoggingHelper;
import com.ibm.commerce.messaging.commands.SendMsgCmd;
import com.ibm.commerce.ras.ECTrace;
import com.ibm.commerce.ras.ECTraceIdentifiers;
import com.ibm.commerce.server.ECConstants;
/**
 * Controller command used for sending user registration email details to 
 * domtarondemand email address.
 */
public class DomtarUserRegistrationEmailCmdImpl extends ControllerCommandImpl
		implements DomtarUserRegistrationEmailCmd {
	
	public static final String CLASS_NAME = "com.ibm.commerce.domtar.commands.DomtarUserRegistrationEmailCmdImpl";
	private static final Logger LOGGER = LoggingHelper.getLogger(DomtarUserRegistrationEmailCmdImpl.class);	
	private boolean emailSuccessful = true;

	/* (non-Javadoc)
	 * @see com.ibm.commerce.command.AbstractECTargetableCommand#performExecute()
	 */
	@Override
	public void performExecute() throws ECException {
		super.performExecute();
		sendEmail();
		TypedProperty rspProp = new TypedProperty();
		rspProp.put(ECConstants.EC_VIEWTASKNAME,"UserRegistrationAddPendingApprovalView");
		setResponseProperties(rspProp);
	}

	private void sendEmail() {
		final String methodName = "sendEmail";
		try {
			String msgTypeName = "EmailUserRegistrationDetails";
			SendMsgCmd cmdSendMsg = (SendMsgCmd)CommandFactory.createCommand(SendMsgCmd.NAME, getStoreId());
	        cmdSendMsg.setMsgType(msgTypeName);
	        cmdSendMsg.setStoreID(getStoreId());
	         
	        StringBuffer emailContent = new StringBuffer();
	         
	        //Recipient and subject are set in admin console.
	        //cmdSendMsg.setConfigData("subject", "New User Registration Details");
	        cmdSendMsg.setConfigData("sender",getRequestProperties().getString("email1",""));
	        //cmdSendMsg.setConfigData("recipient", "DomtarOnDemand@domtar.com");
	        //cmdSendMsg.setConfigData("recipient", "shanawas.moideen@wipro.com");
	            
	        emailContent.append("New User Registration details:\n\n");
	        emailContent.append("First Name").append(" = ").append(getRequestProperties().getString("firstName",""));
	        emailContent.append("\n");
	        emailContent.append("Last Name").append(" = ").append(getRequestProperties().getString("lastName",""));
	        emailContent.append("\n");
	        emailContent.append("Company/Customer Name").append(" = ").append(getRequestProperties().getString("customerName",""));
	        emailContent.append("\n");
	        emailContent.append("Phone number").append(" = ").append(getRequestProperties().getString("phone1",""));
	        emailContent.append("\n");
	        emailContent.append("E-mail").append(" = ").append(getRequestProperties().getString("email1",""));
	        emailContent.append("\n");
	        emailContent.append("Address Line 1").append(" = ").append(getRequestProperties().getString("address1",""));
	        emailContent.append("\n");
	        emailContent.append("Address Line 2").append(" = ").append(getRequestProperties().getString("address2",""));
	        emailContent.append("\n");
	        emailContent.append("Country/Region").append(" = ").append(getRequestProperties().getString("country",""));
	        emailContent.append("\n");
	        emailContent.append("State/Province").append(" = ").append(getRequestProperties().getString("state",""));
	        emailContent.append("\n");
	        emailContent.append("City").append(" = ").append(getRequestProperties().getString("city",""));
	        emailContent.append("\n");
	        emailContent.append("ZIP code/Postal code").append(" = ").append(getRequestProperties().getString("zipCode",""));
	        emailContent.append("\n");
	        
	        String location = getRequestProperties().getString("location","");
	        if (location.equals("singleSoldTO")){
	        	emailContent.append("Single Sold To").append(" = ").append(getRequestProperties().getString("singleSoldTO",""));
	        	emailContent.append("\n");
	        }else {
	        	emailContent.append("Location").append(" = ").append("All Locations");
	        	emailContent.append("\n");
			}
	        
	        emailContent.append("Approver's Name").append(" = ").append(getRequestProperties().getString("approverName",""));
	        emailContent.append("\n");
	        emailContent.append("Approver's Email").append(" = ").append(getRequestProperties().getString("approverEmail",""));
	        emailContent.append("\n");
	        emailContent.append("Approver's Phone").append(" = ").append(getRequestProperties().getString("approverPhone",""));
	        emailContent.append("\n\nThanks,\n");
	        emailContent.append(getRequestProperties().getString("firstName",""));
	        emailContent.append(" ");
	        emailContent.append(getRequestProperties().getString("lastName",""));
	        
	        LOGGER.info("Email Content : "+emailContent.toString());
	        cmdSendMsg.setContent(null, "-1", emailContent.toString().getBytes());
	        cmdSendMsg.sendTransacted();
	        cmdSendMsg.setCommandContext(getCommandContext());
	        cmdSendMsg.execute();
	    }catch (Exception e) {
	    	emailSuccessful = false;
	    	LOGGER.info("Exception while sending User registration details : "+e.getMessage());	 
	    	ECTrace.trace(ECTraceIdentifiers.COMPONENT_EXTERN, CLASS_NAME, methodName, "User registration email not sent because exception was thrown.");
		}
	}

}
