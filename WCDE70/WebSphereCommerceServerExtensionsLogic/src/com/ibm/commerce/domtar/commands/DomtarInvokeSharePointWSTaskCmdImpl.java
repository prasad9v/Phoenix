package com.ibm.commerce.domtar.commands;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.MessageContext;

import com.domtar.webservices.DocumentsSoap;
import com.domtar.webservices.DocumentsSoap12Proxy;
import com.ibm.commerce.command.TaskCommandImpl;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.exception.ECSystemException;
import com.ibm.commerce.foundation.common.util.logging.LoggingHelper;
import com.ibm.commerce.ras.ECMessage;

public class DomtarInvokeSharePointWSTaskCmdImpl extends TaskCommandImpl
		implements DomtarInvokeSharePointWSTaskCmd {
	
	private static final Logger LOGGER = LoggingHelper.getLogger(DomtarInvokeSharePointWSTaskCmdImpl.class);
	
	private static final String SHAREPOINT_USERNAME = "domtar.sharepoint.username";
	private static final String SHAREPOINT_PASSWORD = "domtar.sharepoint.password";
	
	private String documentType;
	private String documentNumber;
	private String requester;
	private String language;
	private String channel;
	
	private byte[] fileBytes;
	
	public byte[] getDocumentAsByteArray() {
		return fileBytes;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getRequester() {
		return requester;
	}

	public void setRequester(String requester) {
		this.requester = requester;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	@Override
	public void performExecute() throws ECException {
		super.performExecute();
		
		logSharepointWSRequest();
		
		//Fetch the sharepoint username and password from JVM custom properties.
		String userName = System.getProperty(SHAREPOINT_USERNAME);
		String password = System.getProperty(SHAREPOINT_PASSWORD);
		
		Map<String, List<String>> httpHeaders = new HashMap<String,List<String>>();
		httpHeaders.put("Type", Collections.singletonList(channel));
		httpHeaders.put("PUID", Collections.singletonList(requester));
		
		try {
			DocumentsSoap12Proxy documentSoapProxy = new DocumentsSoap12Proxy();
			DocumentsSoap documentSoap = documentSoapProxy._getDescriptor().getProxy();
			((BindingProvider) documentSoap).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, userName);
			((BindingProvider) documentSoap).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, password);
			((BindingProvider) documentSoap).getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, httpHeaders);
			
			fileBytes = documentSoap.getDocument(documentType, documentNumber, requester, language, channel);

		} catch (WebServiceException e) {
			e.printStackTrace();
			throw new ECSystemException(ECMessage._ERR_FAIL_TO_CONNECT_TO_EXTERNAL_SYSTEM,this.getClass().getName(), "performExecute");
		}
	}

	private void logSharepointWSRequest() {
		StringBuffer sb = new StringBuffer("<SharePointRequest>\n");
		sb.append("<documentType>"+documentType+"</documentType>\n");
		sb.append("<documentNumber>"+documentNumber+"</documentNumber>\n");
		sb.append("<requester>"+requester+"</requester>\n");
		sb.append("<language>"+language+"</language>\n");
		sb.append("<channel>"+channel+"</channel>\n");		
		sb.append("</SharePointRequest>");
		LOGGER.info("Share Point Request : \n"+sb.toString());		
	}

}
