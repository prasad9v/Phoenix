package com.ibm.commerce.domtar.commands;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commerce.command.CommandFactory;
import com.ibm.commerce.command.ControllerCommandImpl;
import com.ibm.commerce.datatype.TypedProperty;
import com.ibm.commerce.domtar.helpers.DomtarHelper;
import com.ibm.commerce.domtar.server.DomtarServerHelper;
import com.ibm.commerce.exception.ECApplicationException;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.exception.ECSystemException;
import com.ibm.commerce.foundation.common.util.logging.LoggingHelper;
import com.ibm.commerce.messaging.commands.SendMsgCmd;
import com.ibm.commerce.ras.ECMessage;
import com.ibm.commerce.server.ECConstants;
import com.ibm.commerce.user.objects.AddressAccessBean;
/**
 * Controller command used for downloading, printing and emailing customer documents
 * 
 */
public class DomtarProcessDocumentsCmdImpl extends ControllerCommandImpl
		implements DomtarProcessDocumentsCmd {
	
	private static final String CLASS_NAME = DomtarProcessDocumentsCmdImpl.class.getName();
	private static final Logger LOGGER = LoggingHelper.getLogger(DomtarProcessDocumentsCmdImpl.class);	
	private static final String DOWNLOAD = "download";
	private static final String PRINT = "print";
	private static final String EMAIL = "email";
	
	private static final String ORDER_ACKNOWLEDGEMENT = "OrderAcknowledgement";
	private static final String PACKING_LIST = "PackingList";
	private static final String INVOICE = "Invoice";
	
	private static final int BUFSIZE = 8192;
	private boolean isSharePointServiceDown = false;
	
	private static final String MESSAGE_TYPE_NAME = "SendCustomerDocuments";
	private static final String DOMTAR_EMAIL_ADDRESS = "DomtarOnDemand@domtar.com";
	private static final String EMAIL_SUBJECT = "Your requested Document";
	
	private String action;
	//documentId has the format documentType__documentNumber;
	private String documentId;
	
	

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public void performExecute() throws ECException {
		
		String vMethodName = "performExecute";
		super.performExecute();

		String [] docDetailArray = documentId.split("__");
		String documentType = docDetailArray[0];
		if (documentType.equals("OrdAck")){
			documentType = ORDER_ACKNOWLEDGEMENT;
		}else if (documentType.equals("Manifest")) {
			documentType = PACKING_LIST;
		}else if (documentType.equals("Invoice")){
			documentType = INVOICE;
		}
		String documentNumber = docDetailArray[1];
		String locale = getCommandContext().getLocale().toString();
		
		DomtarInvokeSharePointWSTaskCmd invokeSharePointWSCmd = null;		
		HttpServletResponse response = (HttpServletResponse) getCommandContext().getResponse();
		
		byte[] documentBytes = null;
		
		if(DomtarServerHelper.isDevelopmentEnvironment()){
			documentBytes = getDocumentBytesFromStub();
		} else{			
			try {
				String userId = getCommandContext().getUserId().toString();
				invokeSharePointWSCmd = (DomtarInvokeSharePointWSTaskCmd) CommandFactory.createCommand(
			         "com.ibm.commerce.domtar.commands.DomtarInvokeSharePointWSTaskCmd", getStoreId());
				invokeSharePointWSCmd.setCommandContext(getCommandContext());
				invokeSharePointWSCmd.setDocumentType(documentType);
				invokeSharePointWSCmd.setDocumentNumber(documentNumber);
				invokeSharePointWSCmd.setRequester(DomtarHelper.getLogonIdByUserId(userId));
				invokeSharePointWSCmd.setLanguage("en-US");
				invokeSharePointWSCmd.setChannel("ENCORE");
				invokeSharePointWSCmd.execute();
				documentBytes = invokeSharePointWSCmd.getDocumentAsByteArray();	
			} catch (ECException ex) {
				LOGGER.info("Error in contacting Sharepoint. "+ ex.getMessage());
				isSharePointServiceDown = true;
				ex.printStackTrace();
			}			
		}
		if(documentBytes == null || documentBytes.length == 0){
			isSharePointServiceDown = true;	
	    }
		TypedProperty rspProp = new TypedProperty();
		String mimetype =  "application/pdf";
		String fileName = documentType+"_"+documentNumber+".pdf";
        
		if(getAction().equals(DOWNLOAD) || getAction().equals(PRINT)){
			String responseMsg = "Success";
			if(!isSharePointServiceDown){
				try {
					// sets response content type
			        if (mimetype == null) {
			            mimetype = "application/octet-stream";
			        }
			        response.setContentType(mimetype);
			        response.setContentLength(documentBytes.length);
			        
			        //sets HTTP header
			        if(DOWNLOAD.equals(getAction())){
			        	response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");			        	
			        }else{
			        	response.setHeader("Content-Disposition", "inline");
			        	response.setHeader("Pragma", "no-cache");
			        	response.setHeader("Cache-control", "private");
			        	response.setDateHeader("Expires", 0);
			        }
			        
			        rspProp.put(ECConstants.EC_RAWDOCUMENT, documentBytes);	       
					
				} catch (Exception e) {
					LOGGER.info("Error in download and print . "+ e.getMessage());
					responseMsg = "Failure";
					rspProp.put(ECConstants.EC_RAWDOCUMENT, responseMsg.getBytes());
				}
			} else {
				if(documentBytes == null || documentBytes.length == 0){
					responseMsg = "Failure";
					rspProp.put(ECConstants.EC_RAWDOCUMENT, responseMsg.getBytes());
				} else{
					responseMsg = "SharePointDown";
					rspProp.put(ECConstants.EC_RAWDOCUMENT, responseMsg.getBytes());
				}
			} 
			rspProp.put(ECConstants.EC_VIEWTASKNAME,ECConstants.EC_GENERIC_DIRECTVIEW);
			getCommandContext().setResponse(response);			
		}else if(getAction().equals(EMAIL)){
			String responseMessage = "Success";
			if(!isSharePointServiceDown){
				Long userId = getCommandContext().getUserId();
				String userEmailAddr="";
				try {
					Enumeration<AddressAccessBean> enmAddressList = (new AddressAccessBean()).findByMemberId(userId);
					if(enmAddressList.hasMoreElements()) {
						AddressAccessBean addressbean = (AddressAccessBean)enmAddressList.nextElement();
						System.out.println("AddressType: " + addressbean.getEmail1());
						userEmailAddr = addressbean.getEmail1();
					}
					
					String msgTypeName = "SendCustomerDocuments";
	                
	                SendMsgCmd cmdSendMsg = (SendMsgCmd)CommandFactory.createCommand(SendMsgCmd.NAME, getStoreId());
	                cmdSendMsg.setMsgType(MESSAGE_TYPE_NAME);
	                cmdSendMsg.setStoreID(getStoreId());
	                
	                String CustomerDocNotifyMsg = new String("Hi,\n\nPlease find the requested " +
	                		"document attached.\n\nThanks,\nDomtar Support");
	                cmdSendMsg.setContent(null, "-1", CustomerDocNotifyMsg.getBytes());
	                cmdSendMsg.addContentPart(documentBytes, fileName, mimetype);
	                
	                cmdSendMsg.setConfigData("subject", EMAIL_SUBJECT);
	                cmdSendMsg.setConfigData("sender",DOMTAR_EMAIL_ADDRESS);
	                cmdSendMsg.setConfigData("recipient", userEmailAddr);
	                //cmdSendMsg.setConfigData("contentType", "text/html");
	                    
	                cmdSendMsg.sendImmediate(); 
	                cmdSendMsg.setCommandContext(getCommandContext());
	                cmdSendMsg.execute();
					
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (FinderException e) {
					e.printStackTrace();
				} catch (NamingException e) {
					e.printStackTrace();
				} catch (CreateException e) {
					e.printStackTrace();
				} catch(ECException e){
					responseMessage = "Failure";
					e.printStackTrace();
					LOGGER.info("Notification not sent because exception was thrown from Messaging. "+ e.getMessage());
	            }
			}else {
				if(documentBytes == null || documentBytes.length == 0){
					responseMessage = "Failure";					
				} else{
					responseMessage = "SharePointDown";					
				}
			}

			response.setContentType("text/html");		
			rspProp.put(ECConstants.EC_RAWDOCUMENT,responseMessage.getBytes());
			rspProp.put(ECConstants.EC_VIEWTASKNAME,ECConstants.EC_GENERIC_DIRECTVIEW);
			getCommandContext().setResponse(response);
		}
		setResponseProperties(rspProp);
	}

	private byte[] getDocumentBytesFromStub() {
		String filePath = "C:\\pdf-test.pdf";
		File file = new File(filePath);		
		String fileName = file.getName();
		
		byte[] byteBuffer = new byte[BUFSIZE];
        BufferedInputStream bufferedInputStream;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int start = 0;
        int offset = -1;
		try {
			bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
			while ((offset = bufferedInputStream.read(byteBuffer, start, BUFSIZE)) != -1)
				byteArrayOutputStream.write(byteBuffer, start, offset);
			
			bufferedInputStream.close();
		    byteArrayOutputStream.flush();
		    byteBuffer = byteArrayOutputStream.toByteArray();
		    byteArrayOutputStream.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return byteBuffer;
	}
	
	@Override
	public void setRequestProperties(TypedProperty reqProperties)
			throws ECException {
		setAction(reqProperties.getString("action", ""));
		setDocumentId(reqProperties.getString("documentId", ""));
		super.setRequestProperties(reqProperties);
	}

	@Override
	public void validateParameters() throws ECException {
		if(documentId == null || documentId.equals("")){
			throw new ECSystemException(ECMessage._ERR_BAD_PARMS,this.getClass().getName(), "validateParameters");
		}
	}

}
