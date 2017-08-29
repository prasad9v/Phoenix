package com.ibm.commerce.domtar.commands;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import javax.xml.ws.BindingProvider;

import com.ibm.commerce.command.TaskCommandImpl;
import com.ibm.commerce.domtar.beans.DomtarInventoryHoldItemBean;
import com.ibm.commerce.domtar.databeans.DomtarHoldInputDataBean;
import com.ibm.commerce.domtar.databeans.DomtarHoldOutputDataBean;
import com.ibm.commerce.domtar.server.DomtarServerHelper;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.foundation.common.util.logging.LoggingHelper;

import domtar.dnet.drv1dvai01.inventoryholdservice.FaultPkgWsccommonExternalDocumentsServiceFault;
import domtar.dnet.drv1dvai01.inventoryholdservice.InventoryHoldInput;
import domtar.dnet.drv1dvai01.inventoryholdservice.InventoryHoldOutput;
import domtar.dnet.drv1dvai01.inventoryholdservice.InventoryHoldRequest;
import domtar.dnet.drv1dvai01.inventoryholdservice.InventoryHoldResponse;
import domtar.dnet.drv1dvai01.inventoryholdservice.InventoryHoldServicePortProxy;
import domtar.dnet.drv1dvai01.inventoryholdservice.InventoryHoldWSDPortType;
import domtar.dnet.drv1dvai01.inventoryholdservice.Item;
import domtar.dnet.drv1dvai01.inventoryholdservice.Item2;
import domtar.dnet.drv1dvai01.inventoryholdservice.ObjectFactory;

public class DomtarInvokeInventoryHoldWSTaskCmdImpl extends TaskCommandImpl 
		implements DomtarInvokeInventoryHoldWSTaskCmd {
	
	private static final Logger LOGGER = LoggingHelper.getLogger(DomtarInvokeInventoryHoldWSTaskCmdImpl.class);
	
	private static final String STOCKCHECK_AND_HOLD_USERNAME = "domtar.webmethods.username";
	private static final String STOCKCHECK_AND_HOLD_PASSWORD = "domtar.webmethods.password";
	
	private DomtarHoldInputDataBean vHoldInputData;
	private DomtarHoldOutputDataBean vHoldOutputData;

	@Override
	public void performExecute() throws ECException {
		super.performExecute();
		
		if(DomtarServerHelper.isDevelopmentEnvironment())
			vHoldOutputData = getHoldDetailsFromStub();
		else
			vHoldOutputData = invokeInventoryHoldWebService();
	}

	private DomtarHoldOutputDataBean invokeInventoryHoldWebService() {
		//Log Inventory Hold Request
		logInventoryHoldRequest(vHoldInputData);
		
		//Fetch the webmethods username and password from JVM custom properties.
		String userName = System.getProperty(STOCKCHECK_AND_HOLD_USERNAME);
		String password = System.getProperty(STOCKCHECK_AND_HOLD_PASSWORD);
		
		ObjectFactory objectFactory = new ObjectFactory();
		InventoryHoldRequest invHoldRequest = objectFactory.createInventoryHoldRequest();
		invHoldRequest.setTrackingId(vHoldInputData.getTrackingId());
		invHoldRequest.setSoldTo(vHoldInputData.getSoldTo());
		invHoldRequest.setContact(vHoldInputData.getContact());
		invHoldRequest.setShipTo(vHoldInputData.getShipTo());
		invHoldRequest.setCallingSystem(vHoldInputData.getCallingSystem());
		invHoldRequest.setLanguage(objectFactory.createInventoryHoldRequestLanguage(vHoldInputData.getLanguage()));
		invHoldRequest.setRequestorName(vHoldInputData.getRequestorName());
		invHoldRequest.setSalesServiceOrg(vHoldInputData.getServiceOrganisation());
		
		List <DomtarInventoryHoldItemBean> invHoldItemBeanList = vHoldInputData.getHoldItem();
		for (DomtarInventoryHoldItemBean invHoldItemBean : invHoldItemBeanList) {
			Item item = objectFactory.createItem();
			item.setItemCode(invHoldItemBean.getItemCode());
			item.setQuantity(invHoldItemBean.getHoldQuanity());
			item.setQuantityUnit(invHoldItemBean.getHoldQuantityUnit());
			item.setProdShipDate(invHoldItemBean.getProductShipDate());
			item.setSourcingLocation(invHoldItemBean.getSourcingLocation());
			item.setItemShipTo(invHoldItemBean.getItemShipTo());
			invHoldRequest.getItem().add(item);
		}
				
		InventoryHoldInput invHoldInput = objectFactory.createInventoryHoldInput();
		invHoldInput.setInventoryHoldRequest(invHoldRequest);
		
		InventoryHoldServicePortProxy invHoldProxy = new InventoryHoldServicePortProxy();
		/*
		 * Set the username and password for Authenticating the web service. Basic Authentication is 
		 * used here.
		 */
		InventoryHoldWSDPortType invHoldPort = invHoldProxy._getDescriptor().getProxy();
		((BindingProvider) invHoldPort).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, userName);
		((BindingProvider) invHoldPort).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, password);
		
		InventoryHoldOutput invHoldOutput = null;
		HashMap<String, String> itemErrorMap = new HashMap<String, String>();
		List<DomtarInventoryHoldItemBean> inventoryHoldItemList = new ArrayList<DomtarInventoryHoldItemBean>();
		DomtarHoldOutputDataBean holdOutputData = new DomtarHoldOutputDataBean();
		
		try {
			invHoldOutput = invHoldPort.inventoryHold(invHoldInput);
			InventoryHoldResponse invHoldResponse = invHoldOutput.getInventoryHoldResponse();
			//Log Inventory Hold Response
			logInventoryHoldResponse(invHoldResponse);
			
			String callError = null;
			if(invHoldResponse.getCallError() != null && invHoldResponse.getCallError().getValue() != null){
				callError = invHoldResponse.getCallError().getValue();
				holdOutputData.setHoldWebServiceCallError(callError);
			}
			holdOutputData.setTrackingId(invHoldResponse.getTrackingId().getValue());
			holdOutputData.setHoldTrackingNumber(invHoldResponse.getHoldTrackingNumber());
			holdOutputData.setHoldDuartion(invHoldResponse.getHoldDuration());
			holdOutputData.setHoldDate(invHoldResponse.getHoldDate());
			holdOutputData.setHoldTime(invHoldResponse.getHoldTime());

			List<Item2> responseItemList = invHoldResponse.getItem();
			for (Item2 responseItem : responseItemList) {
				DomtarInventoryHoldItemBean invHoldItem = new DomtarInventoryHoldItemBean();
				String itemError = null;
				String itemCode = responseItem.getItemCode();
				if(responseItem.getItemError() != null && responseItem.getItemError().getValue() != null){
					itemError = responseItem.getItemError().getValue();
					itemErrorMap.put(itemCode, itemError);
				}							
				String prodShipDate = responseItem.getProdShipDate();
				String quantityOnHold = responseItem.getQuantityOnHold();
				String quantityUnit = responseItem.getQuantityUnit();
				String sourcingLocation = responseItem.getSourcingLocation();
				invHoldItem.setItemCode(itemCode);
				invHoldItem.setProductShipDate(prodShipDate);
				invHoldItem.setHoldQuanity(quantityOnHold);
				invHoldItem.setHoldQuantityUnit(quantityUnit);
				invHoldItem.setSourcingLocation(sourcingLocation);
				inventoryHoldItemList.add(invHoldItem);
			}
			holdOutputData.setItemErrorMap(itemErrorMap);
			holdOutputData.setHoldItem(inventoryHoldItemList);
			
		} catch (FaultPkgWsccommonExternalDocumentsServiceFault e) {
			LOGGER.info("Fault Exception while invoking Inventory Hold Web Service : "+e.getMessage());
			LOGGER.info("Service Fault Message : "+e.getFaultInfo().getMessage());
			e.printStackTrace();
		}catch (Exception e) {
			LOGGER.info("Exception while invoking Inventory Hold Web Service : "+e.getMessage());
			e.printStackTrace();
			holdOutputData.setHoldWebServiceCallError("Inventory Hold Service is not available at the moment. Please try again Later.");
		}
		return holdOutputData;
	}

	private DomtarHoldOutputDataBean getHoldDetailsFromStub() {
		
		logInventoryHoldRequest(vHoldInputData);
		
		DomtarHoldOutputDataBean output = new DomtarHoldOutputDataBean();
		String[] vCallError = new String[5];
		String[] vItemsError = new String[10];		
		
		List <DomtarInventoryHoldItemBean> holdItemBean = vHoldInputData.getHoldItem();
		HashMap<String, String> itemErrorMap = new HashMap<String, String>();
		for(DomtarInventoryHoldItemBean vInput : holdItemBean){
			if("2506".equalsIgnoreCase(vInput.getItemCode())){
				itemErrorMap.put(vInput.getItemCode(), "Cannot hold "+vInput.getItemCode()+" as its hold limit exceeded");
			}
			if("2509".equalsIgnoreCase(vInput.getItemCode())){
				itemErrorMap.put(vInput.getItemCode(), "Cannot hold "+vInput.getItemCode()+" as its temorarary out of stock");
			}
			if("2510".equalsIgnoreCase(vInput.getItemCode())){
				itemErrorMap.put(vInput.getItemCode(), "Cannot hold "+vInput.getItemCode()+" as its currently not to be sold outside");
			}
			if("2514".equalsIgnoreCase(vInput.getItemCode())){
				String callError = "Connection time expired for Call. " +
				"For any emergency, please contact 'Support@domtar.com' or 100101101000 " +
				"for assistance.";
				output.setHoldWebServiceCallError(callError);
			}
		}
		
		
		output.setItemErrorMap(itemErrorMap);		
		
		Random randomGenerator = new Random();
		output.setHoldTrackingNumber(String.valueOf(randomGenerator.nextInt(1000000000)));
		Date vCurrentDate = new Date();
		String pattern = "yyyyMMdd";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		output.setHoldDate(format.format(vCurrentDate));
		output.setHoldDuartion("45");
		output.setHoldTime("10:10:10");
		output.setHoldItem(holdItemBean);
	
		return output;
	}

	private void logInventoryHoldResponse(InventoryHoldResponse invHoldResponse) {
		StringBuffer sb = new StringBuffer("<InventoryHoldResponse>\n");
		sb.append("<TrackingId>"+invHoldResponse.getTrackingId().getValue()+"</TrackingId>\n");
		sb.append("<HoldTrackingNumber>"+invHoldResponse.getHoldTrackingNumber()+"</HoldTrackingNumber>\n");
		sb.append("<HoldDuration>"+invHoldResponse.getHoldDuration()+"</HoldDuration>\n");
		sb.append("<HoldDate>"+invHoldResponse.getHoldDate()+"</HoldDate>\n");
		sb.append("<HoldTime>"+invHoldResponse.getHoldTime()+"</HoldTime>\n");
		if(invHoldResponse.getCallError() != null && invHoldResponse.getCallError().getValue() != null)
			sb.append("<CallError>"+invHoldResponse.getCallError().getValue()+"</CallError>\n");	

		List<Item2> responseItemList = invHoldResponse.getItem();
		for (Item2 responseItem : responseItemList) {
			sb.append("<Item>\n");
			sb.append("<ItemCode>"+responseItem.getItemCode()+"</ItemCode>\n");
			if(responseItem.getItemError() != null && responseItem.getItemError().getValue() != null)
				sb.append("<ItemError>"+responseItem.getItemError().getValue()+"</ItemError>\n");
			sb.append("<HoldQuantity>"+responseItem.getProdShipDate()+"</HoldQuantity>\n");
			sb.append("<HoldQuantityUnit>"+responseItem.getQuantityOnHold()+"</HoldQuantityUnit>\n");
			sb.append("<ProdShipDate>"+responseItem.getQuantityUnit()+"</ProdShipDate>\n");
			sb.append("<SourcingLocation>"+responseItem.getSourcingLocation()+"</SourcingLocation>\n");
			sb.append("</Item>\n");
		}
		
		sb.append("</InventoryHoldResponse>");
		LOGGER.info("Inventory Hold Response : \n"+sb.toString());
	}

	private void logInventoryHoldRequest(DomtarHoldInputDataBean holdInputData) {
		StringBuffer sb = new StringBuffer("<InventoryHoldRequest>\n");
		sb.append("<TrackingId>"+holdInputData.getTrackingId()+"</TrackingId>\n");
		sb.append("<SoldTo>"+holdInputData.getSoldTo()+"</SoldTo>\n");
		sb.append("<Contact>"+holdInputData.getContact()+"</Contact>\n");
		sb.append("<ShipTo>"+holdInputData.getShipTo()+"</ShipTo>\n");
		sb.append("<CallingSystem>"+holdInputData.getCallingSystem()+"</CallingSystem>\n");
		sb.append("<Language>"+holdInputData.getLanguage()+"</Language>\n");	
		sb.append("<RequestorName>"+holdInputData.getRequestorName()+"</RequestorName>\n");
		sb.append("<SalesServiceOrg>"+holdInputData.getServiceOrganisation()+"</SalesServiceOrg>\n");	
		
		List<DomtarInventoryHoldItemBean> invHoldItemBeanList = holdInputData.getHoldItem();
		
		for (DomtarInventoryHoldItemBean invHoldItemBean : invHoldItemBeanList) {
			sb.append("<Item>\n");
			sb.append("<ItemCode>"+invHoldItemBean.getItemCode()+"</ItemCode>\n");
			sb.append("<HoldQuantity>"+invHoldItemBean.getHoldQuanity()+"</HoldQuantity>\n");
			sb.append("<HoldQuantityUnit>"+invHoldItemBean.getHoldQuantityUnit()+"</HoldQuantityUnit>\n");
			sb.append("<ProdShipDate>"+invHoldItemBean.getProductShipDate()+"</ProdShipDate>\n");
			sb.append("<SourcingLocation>"+invHoldItemBean.getSourcingLocation()+"</SourcingLocation>\n");
			sb.append("<itemShipTo>"+invHoldItemBean.getItemShipTo()+"</itemShipTo>\n");
			sb.append("</Item>\n");
		}
		sb.append("</InventoryHoldRequest>");
		LOGGER.info("Inventory Hold Request : \n"+sb.toString());
	}

	@Override
	public void setHoldInput(DomtarHoldInputDataBean holdInput) {
		vHoldInputData = holdInput;		
	}

	@Override
	public DomtarHoldOutputDataBean getHoldResponseOutput() {
		return vHoldOutputData;
		
	}

}
