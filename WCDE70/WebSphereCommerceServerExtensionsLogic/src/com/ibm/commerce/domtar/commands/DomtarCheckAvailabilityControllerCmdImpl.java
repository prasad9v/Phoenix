package com.ibm.commerce.domtar.commands;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.NamingException;

import com.ibm.commerce.beans.DataBeanManager;
import com.ibm.commerce.catalog.beans.AttributeValueDataBean;
import com.ibm.commerce.catalog.beans.CatalogEntryDataBean;
import com.ibm.commerce.command.CommandFactory;
import com.ibm.commerce.command.ControllerCommandImpl;
import com.ibm.commerce.datatype.TypedProperty;
import com.ibm.commerce.domtar.databeans.DomtarStockCheckInputDataBean;
import com.ibm.commerce.exception.ECApplicationException;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.ras.ECMessage;
import com.ibm.commerce.search.beans.CatEntrySearchListDataBean;
import com.ibm.commerce.server.ECConstants;
import com.ibm.websphere.command.CommandException;

public class DomtarCheckAvailabilityControllerCmdImpl extends
		ControllerCommandImpl implements DomtarCheckAvailabilityControllerCmd {
	
	private static final String M_STOCKDETAILS_PAGE = "DomtarStockCheckDetailsDisplay";

	private String vError = "";
	
	public void performExecute() throws ECException {
		super.performExecute();
		String strMethodName ="performExecute";
		TypedProperty rspProp = new TypedProperty();
		DomtarStockCheckInputDataBean inputBean = null;
		try {
			inputBean = mapValueToInputBean(getRequestProperties());
			
			DomtarCheckAvailabilityTaskCmd vChkAvailabilityCmd;
			vChkAvailabilityCmd = (DomtarCheckAvailabilityTaskCmd) CommandFactory.createCommand("com.ibm.commerce.domtar.commands.DomtarCheckAvailabilityTaskCmd", getStoreId());
			vChkAvailabilityCmd.setInputData(inputBean);
			vChkAvailabilityCmd.setCommandContext(getCommandContext());		
			vChkAvailabilityCmd.performExecute();
			
			if(vChkAvailabilityCmd.getStockCheckWebServiceError() != null){
				vError = vChkAvailabilityCmd.getStockCheckWebServiceError();
			} 
			rspProp.put("webServiceError",vError);			
			rspProp.put("isTimeOut", vChkAvailabilityCmd.getTimeOut());
			rspProp.put("isAlternateOffer", inputBean.isAlternateOffer());
			rspProp.put("stockDetails", vChkAvailabilityCmd.getOutputDatas());		
			rspProp.put("soldTo", getRequestProperties().getString("soldTo", ""));
			rspProp.put("shipTo", getRequestProperties().getString("shipTo", ""));
			rspProp.put("holdDetails", getRequestProperties().getString("holdDetails", ""));
			rspProp.put("priceMissing",vChkAvailabilityCmd.isPriceMissing());
			rspProp.put(ECConstants.EC_VIEWTASKNAME, M_STOCKDETAILS_PAGE);
		} catch (CommandException e) {
			throw new ECApplicationException(ECMessage._ERR_BAD_CMD_PARAMETER, this.getClass().getName(), strMethodName,"");
		}
		
		setResponseProperties(rspProp);
	}

	private DomtarStockCheckInputDataBean mapValueToInputBean(
			TypedProperty requestProperties) {
		
		String vItems = requestProperties.getString("partNumber", "");
		String[] vItemsArray = vItems.split(",");
		String itemsInDB = "";
		/* 
		 * In case of Alternate item stock check, identify the quantity unit 
		 * from WCS DB.
		 *  
		 */
		String quantityUnit= "";
		for (String item : vItemsArray) {
			CatEntrySearchListDataBean catEntrySearchDB = new CatEntrySearchListDataBean();
			TypedProperty rqProp = new TypedProperty();
			catEntrySearchDB.setPageSize("1");
			catEntrySearchDB.setCatalogId(requestProperties.getString("catalogID", ""));
			catEntrySearchDB.setIsItem(true);
			catEntrySearchDB.setSku(item);
			catEntrySearchDB.setSkuOperator(catEntrySearchDB.OPERATOR_EQUAL);
			try {
				DataBeanManager.activate(catEntrySearchDB);
				CatalogEntryDataBean[] CatalogEntryDataBeans = catEntrySearchDB.getResultList();
				if(CatalogEntryDataBeans != null){
					if(itemsInDB.equals(""))
						itemsInDB = item;
					else
						itemsInDB = itemsInDB +","+ item;
					for (CatalogEntryDataBean catalogEntryDataBean : CatalogEntryDataBeans) {
						AttributeValueDataBean[] AttributeValueDataBeans = catalogEntryDataBean.getItemDataBean().getAttributeValueDataBeans();
						for (AttributeValueDataBean attributeValueDataBean : AttributeValueDataBeans) {
							String attribute = attributeValueDataBean.getAttribute().getName();
							if(attribute.equalsIgnoreCase("InventoryUnitOfMeasurement")){
								String unitOfMeasurement = attributeValueDataBean.getName();
								if(quantityUnit.equals(""))
									quantityUnit = unitOfMeasurement;
								else
									quantityUnit = quantityUnit +","+unitOfMeasurement;
							}
						}
					}
				} 						
			} catch (ECException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CreateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		DomtarStockCheckInputDataBean inputBean = new DomtarStockCheckInputDataBean();
		inputBean.setLangId(requestProperties.getString("langId", "-1"));		
		if(!itemsInDB.equals(""))
			inputBean.setItems(itemsInDB.split(","));
					
		if("ALTERNATEOFFER".equalsIgnoreCase(requestProperties.getString("reqType", "STOCKCHECK"))){
			inputBean.setAlternateOffer(true);
			inputBean.setQuanityUnit(quantityUnit.split(","));
		}else{
			inputBean.setAlternateOffer(false);
			String vQuanity = requestProperties.getString("quantity", "");
			String vQuanityUnit = requestProperties.getString("quantityUnit", "");
			inputBean.setQuanity(vQuanity.split(","));
			inputBean.setQuanityUnit(vQuanityUnit.split(","));
		}
		
		inputBean.setCity(requestProperties.getString("city", ""));
		inputBean.setCountry(requestProperties.getString("country", ""));
		inputBean.setState(requestProperties.getString("state", ""));
		inputBean.setCustomerSoldTo(requestProperties.getString("soldTo", ""));
		inputBean.setServiceOrganisation(requestProperties.getString("serviceOrganisation", ""));
		inputBean.setUserId(requestProperties.getString("userId", ""));
		inputBean.setContactRep(requestProperties.getString("contactrep", ""));
		return inputBean;
	}
}
