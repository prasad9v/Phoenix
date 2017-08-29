package com.ibm.commerce.domtar.commands;

import com.ibm.commerce.command.CommandFactory;
import com.ibm.commerce.command.ControllerCommandImpl;
import com.ibm.commerce.datatype.TypedProperty;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.messaging.commands.SendMsgCmd;
import com.ibm.commerce.ras.ECTrace;
import com.ibm.commerce.ras.ECTraceIdentifiers;
import com.ibm.commerce.server.ECConstants;

public class DomtarSendHelpQueryEmailCmdImpl extends ControllerCommandImpl
		implements DomtarSendHelpQueryEmailCmd {
	
	public static final String CLASS_NAME = "com.ibm.commerce.domtar.commands.DomtarSendHelpQueryEmailCmdImpl";
	//Set the default from page as Help page
	private String fromPage = "domtarHelp";
	private boolean helpEmailSuccessful = true;
	
	/**
	 * @return the fromPage
	 */
	public String getFromPage() {
		return fromPage;
	}

	/**
	 * @param fromPage the fromPage to set
	 */
	public void setFromPage(String fromPage) {
		this.fromPage = fromPage;
	}

	/* (non-Javadoc)
	 * @see com.ibm.commerce.command.ControllerCommandImpl#setRequestProperties(com.ibm.commerce.datatype.TypedProperty)
	 */
	@Override
	public void setRequestProperties(TypedProperty reqProperties)
			throws ECException {
		String domtarRep = reqProperties.getString("domtarRep","domtarHelp");
		setFromPage(domtarRep);
		super.setRequestProperties(reqProperties);
	}
	
	public void performExecute() throws ECException {
		
		super.performExecute();
		TypedProperty rspProp = new TypedProperty();		
		sendHelpEmail();
		rspProp.put("helpEmailSuccessful", helpEmailSuccessful);
		if(getFromPage().equals("domtarRep")){
			rspProp.put(ECConstants.EC_VIEWTASKNAME, "DomtarRepDisplay");
		}else{
			rspProp.put(ECConstants.EC_VIEWTASKNAME, "DomtarHelpDisplay");
		}		
		setResponseProperties(rspProp);
	}
	
	private void sendHelpEmail() throws ECException {
		 final String methodName = "sendHelpEmail";
		 
		 try {		
			 String msgTypeName = "sendHelpEmail";
			 SendMsgCmd cmdSendMsg = (SendMsgCmd)CommandFactory.createCommand(SendMsgCmd.NAME, getStoreId());
	         cmdSendMsg.setMsgType(msgTypeName);
	         cmdSendMsg.setStoreID(getStoreId());
	         
	         StringBuffer helpContent = new StringBuffer();
	         
	         if(getFromPage().equals("domtarRep")){
	        	 cmdSendMsg.setConfigData("subject", getRequestProperties().getString("subject",""));
	             cmdSendMsg.setConfigData("sender",getRequestProperties().getString("usermail",""));
	             cmdSendMsg.setConfigData("recipient", getRequestProperties().getString("repmail",""));
	             
	             helpContent.append("Hi,\n\nPlease find my queries/concerns/comments below:\n");
	             helpContent.append("Comments\n").append(getRequestProperties().getString("comment","")).append("\n");
	             helpContent.append("\n\nThanks");
	         }else{
	        	 cmdSendMsg.setConfigData("subject", "Help Query");
	             cmdSendMsg.setConfigData("sender",getRequestProperties().getString("emailAddress",""));
	             cmdSendMsg.setConfigData("recipient", "DomtarOnDemand@domtar.com");
	             
	             helpContent.append("Help Query:\n\n");
	             helpContent.append("category").append(" = ").append(getRequestProperties().getString("category","")).append("\n");
	             helpContent.append("name").append(" = ").append(getRequestProperties().getString("name","")).append("\n");
	             helpContent.append("emailId").append(" = ").append(getRequestProperties().getString("emailAddress","")).append("\n");
	             helpContent.append("comment").append(" = ").append(getRequestProperties().getString("comment","")).append("\n");
	             helpContent.append("\n\nThanks.\n");
	             helpContent.append(getRequestProperties().getString("name",""));
	         }
	         
	         //cmdSendMsg.setConfigData("contentType", "text/html");
	              
	         String helpQueryContent = helpContent.toString();
	         cmdSendMsg.setContent(null, "-1", helpQueryContent.getBytes());
	         cmdSendMsg.sendTransacted();
	         cmdSendMsg.setCommandContext(getCommandContext());
	         cmdSendMsg.execute();
		 } catch (Exception e) {
			 helpEmailSuccessful = false;
			 ECTrace.trace(ECTraceIdentifiers.COMPONENT_EXTERN, CLASS_NAME, methodName, "helpmail not sent because exception was thrown.");
		 }
		
	}

}
