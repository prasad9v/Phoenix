package com.ibm.commerce.domtar.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.ibm.commerce.attachment.beans.AttachmentDataBean;
import com.ibm.commerce.beans.DataBeanManager;
import com.ibm.commerce.catalog.beans.AttributeValueDataBean;
import com.ibm.commerce.catalog.beans.CatalogEntryDataBean;
import com.ibm.commerce.command.CommandContext;
import com.ibm.commerce.command.CommandFactory;
import com.ibm.commerce.command.ControllerCommandImpl;
import com.ibm.commerce.context.entitlement.EntitlementContext;
import com.ibm.commerce.datatype.TypedProperty;
import com.ibm.commerce.domtar.databeans.DomtarItemSearchDetailsDataBean;
import com.ibm.commerce.domtar.databeans.DomtarStockCheckInputDataBean;
import com.ibm.commerce.domtar.databeans.DomtarStockCheckOutputDataBean;
import com.ibm.commerce.exception.ECApplicationException;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.exception.ExceptionHandler;
import com.ibm.commerce.foundation.common.util.logging.LoggingHelper;
import com.ibm.commerce.ras.ECMessage;
import com.ibm.commerce.search.beans.AdvancedCatEntrySearchListDataBean;
import com.ibm.commerce.search.beans.CatEntrySearchListDataBean;
import com.ibm.commerce.server.ECConstants;
import com.ibm.commerce.user.objects.OrganizationAccessBean;
import com.ibm.websphere.command.CommandException;

public class DomtarStockCheckItemSearchControllerCmdImpl extends
		ControllerCommandImpl implements
		DomtarStockCheckItemSearchControllerCmd {
	
	private static final Logger LOGGER = LoggingHelper.getLogger(DomtarStockCheckItemSearchControllerCmdImpl.class);
	
	private static final String M_STOCKDETAILSLIST_PAGE = "DomtarStockCheckItemFilterListPageDisplay";
	private String[] items = new String[10];
	private String[] Quantities = new String[10];
	private String[] QuantityUnits = new String[10];
	private String vCountry;
	private String vState;
	private String vCity;
	private String vSoldTo;
	private String vShipTo;
	private String vServiceOrganisation;
	private String vContactrep;
	private String vStoreID;
	private String vCatalogID;
	private String vUserID;
	private String vLangId;
	private String vHoldDetails;
	private boolean vitemNotExist = false;
	private boolean vTimeout=false;
	private boolean vShowContactRep = false;
	private String vNonExistingItem="";
	private int pageSize = 0;
	private int beginIndex = 0;
	private String vDisplayDetailsContent;
	private String vFromPage = "";
	private int  vTotalCount = 0;
	private boolean vPriceMissing = false;
	private int vMissingPriceItems = 0;
	private String vError = "";

	private HashMap<String, String> vitemErrorCodes = new HashMap<String, String>();

	public void performExecute() throws ECException {
		super.performExecute();
		String strMethodName ="performExecute";
		try {
		TypedProperty rspProp = new TypedProperty();
		loadParameters(items,Quantities,getRequestProperties());
		
		// Results will be fetched
		String vJsonObj = loadDetailsForItemSearch();	
		rspProp.put("isTimeOut", vTimeout);
		rspProp.put("non_existing_item", vNonExistingItem);
		rspProp.put("isItemNotExist", vitemNotExist);
		rspProp.put("isAlternateOffer", false);
		rspProp.put("country", vCountry);
		rspProp.put("state", vState);
		rspProp.put("city", vCity);
		rspProp.put("soldTo", vSoldTo);
		rspProp.put("shipTo", vShipTo);
		rspProp.put("holdDetails", vHoldDetails);
		rspProp.put("serviceOrganisation", vServiceOrganisation);
		rspProp.put("contactrep", vContactrep);
		rspProp.put("storeID", vStoreID);
		rspProp.put("catalogID", vCatalogID);
		rspProp.put("userId", vUserID);
		rspProp.put("langId", vLangId);
		rspProp.put("pageSize", pageSize);
		rspProp.put("beginIndex", beginIndex);
		rspProp.put("displayDetailsContent", vDisplayDetailsContent);
		rspProp.put("jsonObj", vJsonObj);
		rspProp.put("totalCount", vTotalCount);
		rspProp.put("priceMissing", vPriceMissing);
		rspProp.put("itemErrorCodes", vitemErrorCodes);
		rspProp.put("webServiceError", vError);
		
		rspProp.put(ECConstants.EC_VIEWTASKNAME, M_STOCKDETAILSLIST_PAGE);
		setResponseProperties(rspProp);
		} catch (CommandException e) {
			throw new ECApplicationException(ECMessage._ERR_BAD_CMD_PARAMETER, this.getClass().getName(), strMethodName,"");
		}
	}
	private List<DomtarItemSearchDetailsDataBean> correctForPageInation(
			List<DomtarItemSearchDetailsDataBean> itemSearchLists) {
		List<DomtarItemSearchDetailsDataBean> vItemSearchLists = new ArrayList<DomtarItemSearchDetailsDataBean>();
		for(int i = 0 ; i < itemSearchLists.size() ; i++){
			if(i >= beginIndex && i <(beginIndex+pageSize) ){
				vItemSearchLists.add(itemSearchLists.get(i));
			}
		}
		
		return vItemSearchLists;
	}
	
	private  String loadDetailsForItemSearch() throws CommandException {
		StringBuffer vJsonString = new StringBuffer(1000);
		
		
		try {
		DomtarItemSearchDetailsDataBean lineItem = null;
		DomtarStockCheckInputDataBean inputBean = null;
		int counter = 0;
		int errorCount = 0;
		int totalCount = 0;
		String userQuantity = null;
		boolean[] itemPresent = new boolean[10];
		Arrays.fill(itemPresent, Boolean.TRUE);
		boolean itemInEncore = true;
		List<CatEntrySearchListDataBean> catEntrySearchDBList = new ArrayList<CatEntrySearchListDataBean>();

		inputBean = mapValuesToInputBean(items,Quantities, QuantityUnits);
		/* Check in DB before calling BAAN to make sure those item codes are not present in
		 * Encore 
		 * */
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				CatEntrySearchListDataBean catEntrySearchDB = new CatEntrySearchListDataBean();
				TypedProperty rqProp = new TypedProperty();
				catEntrySearchDB.setPageSize("1");
				catEntrySearchDB.setResultType("1");
				catEntrySearchDB.setCatalogId(vCatalogID);
				catEntrySearchDB.setIsItem(true);
				catEntrySearchDB.setSku(items[i]);
				catEntrySearchDB.setSkuOperator(catEntrySearchDB.OPERATOR_EQUAL);
				// To be added
				// advancedCatEntrySearchDB.setField1("1");
				DataBeanManager.activate(catEntrySearchDB);
				catEntrySearchDBList.add(catEntrySearchDB);
				CatalogEntryDataBean[] catalogEntityBeans = (CatalogEntryDataBean[]) catEntrySearchDB
							.getResultList();
				
				if (catalogEntityBeans == null) {
					itemPresent[i] = false;
					AdvancedCatEntrySearchListDataBean advanceCatEntrySearchDB = new AdvancedCatEntrySearchListDataBean();
					advanceCatEntrySearchDB.setPageSize("1");
					advanceCatEntrySearchDB.setSku(items[i]);
					advanceCatEntrySearchDB.setSkuOperator(catEntrySearchDB.OPERATOR_EQUAL);
					advanceCatEntrySearchDB.setPublished("0");
					advanceCatEntrySearchDB.setPublishedOperator(catEntrySearchDB.OPERATOR_EQUAL);
					//To be added
					//advancedCatEntrySearchDB.setField1("1");
					DataBeanManager.activate(advanceCatEntrySearchDB);			
					CatalogEntryDataBean[] outerCatalogBean = (CatalogEntryDataBean[]) advanceCatEntrySearchDB
							.getResultList();	
					if(outerCatalogBean!=null && outerCatalogBean.length > 0){
						vShowContactRep = true;
					}
					vitemNotExist = true;
					if("".equals(vNonExistingItem)){
						vNonExistingItem = items[i]+":Invalid Item Code";
					}else{
						vNonExistingItem = vNonExistingItem+"<br/>"+items[i]+":Invalid Item Code";
					}					
					errorCount++;
				}
			}
		}
		for(int j = 0; j < items.length; j++){
			if(itemPresent[j] == false){
				itemInEncore = false;
				break;
			}
		}
		if(itemInEncore){
			//get stock details only if all the items are present in DB.
			DomtarCheckAvailabilityTaskCmd vChkAvailabilityCmd;
			vChkAvailabilityCmd = (DomtarCheckAvailabilityTaskCmd) CommandFactory
					.createCommand(
							"com.ibm.commerce.domtar.commands.DomtarCheckAvailabilityTaskCmd",
							getStoreId());
			vChkAvailabilityCmd.setInputData(inputBean);
			vChkAvailabilityCmd.setCommandContext(getCommandContext());
			vChkAvailabilityCmd.performExecute();
			if(vChkAvailabilityCmd.getTimeOut()){
				vTimeout = true;
			}
			if(vChkAvailabilityCmd.getStockCheckWebServiceError() != null){
				 vError = vChkAvailabilityCmd.getStockCheckWebServiceError();
			 }
			
			
			
			vJsonString.append("{");
			for (int i = 0; i < items.length; i++) {
				if (items[i] != null) {
					userQuantity = Quantities[i];					
					CatalogEntryDataBean[] catalogEntityBeans = (CatalogEntryDataBean[]) catEntrySearchDBList.get(i).getResultList();
					if (catalogEntityBeans != null
							&& catalogEntityBeans.length > 0) {						
						String vAttribute = "";
						String vAttributeValue = "";
						boolean isFeatures = false;
						boolean isCommonUse = false;
						boolean isCertificates = false;
						boolean isGuarentee = false;
						lineItem = new DomtarItemSearchDetailsDataBean();
						

						lineItem.setItemDetails(catalogEntityBeans[0]);
						String vPartNumber = lineItem.getItemDetails()
								.getPartNumber();
						if (counter > 0) {
							vJsonString.append(",");
						} 						
						vJsonString.append("\"").append(vPartNumber).append(
							"\":{");
						
						vJsonString.append("\"Attribute-Details\":{");
						AttributeValueDataBean[] vAttrBeanArr = lineItem
								.getItemDetails().getItemDataBean()
								.getAttributeValueDataBeans();
						if (vAttrBeanArr.length != 0) {
							int attribCount = 0;
							for (AttributeValueDataBean vAttrBean : vAttrBeanArr) {

								vAttribute = vAttrBean.getAttribute().getName().trim();

								if (vAttribute.equals("Features")) {
									isFeatures = true;
								} else if (vAttribute.equals("CommonUse")) {
									isCommonUse = true;
								} else if (vAttribute.equals("Certificates")) {
									isCertificates = true;
								} else if (vAttribute.equals("Guarantee")) {
									isGuarentee = true;
								}
								vAttributeValue = vAttrBean.getName().trim();
								vJsonString.append("\"").append(vAttribute)
										.append("\":{\"").append(
												vAttributeValue)
										.append("\":\"")
										.append(vAttributeValue)
										.append("|:-:|").append(vPartNumber)
										.append("\"}");
								attribCount++;
								if (attribCount < vAttrBeanArr.length) {
									vJsonString.append(",");
								}
							}
						}
						vJsonString.append("}");
						if ("PRODUCTCATALOG".equals(vFromPage)
								&& (isFeatures || isCommonUse || isCertificates || isGuarentee)) {
							AttachmentDataBean[] vAttBean = lineItem.getItemDetails().getItemDataBean().getAllAttachments();
							int validAttachmentLength =0;
							for(AttachmentDataBean atBean : vAttBean){
								if("Features".equalsIgnoreCase(atBean.getFileName())){
									validAttachmentLength++;
								}
								if("CommonUse".equalsIgnoreCase(atBean.getFileName())){
									validAttachmentLength++;
								}
								if("Certificates".equalsIgnoreCase(atBean.getFileName())){ 
									validAttachmentLength++;
								}
								if("Guarantee".equalsIgnoreCase(atBean.getFileName())){
									validAttachmentLength++;
								}
							}
							int attchMnetCount = 0;
							if (validAttachmentLength > 0) {
								vJsonString.append(",\"AttachmentDetails\":{");
							}
							boolean checkForShowingComma = false;
							for (AttachmentDataBean atBean : vAttBean) {
								
								if("Features".equalsIgnoreCase(atBean.getFileName())){
									attchMnetCount++;
									if(isFeatures){
										vJsonString.append("\"Features\":\"").append(escapeAllHTML(atBean.getLongDescription())).append("\"");
									}else{
										vJsonString.append("\"Features\":\"\"");
									}
									checkForShowingComma = true;
								}
								if("CommonUse".equalsIgnoreCase(atBean.getFileName())){
									attchMnetCount++;
									if(isCommonUse){
										vJsonString.append("\"CommonUse\":\"").append(escapeAllHTML(atBean.getLongDescription())).append("\"");
									}else{
										vJsonString.append("\"CommonUse\":\"\"");
									}
									checkForShowingComma = true;
								}
								if("Certificates".equalsIgnoreCase(atBean.getFileName())){ 
									attchMnetCount++;
									if(isCertificates){
										vJsonString.append("\"Certificates\":\"").append(escapeAllHTML(atBean.getLongDescription())).append("\"");
									}else{
										vJsonString.append("\"Certificates\":\"\"");
									}
									checkForShowingComma = true;
								}
								if("Guarantee".equalsIgnoreCase(atBean.getFileName())){
									attchMnetCount++;
									if(isGuarentee){
										vJsonString.append("\"Guarantee\":\"").append(escapeAllHTML(atBean.getLongDescription())).append("\"");
									}else{
										vJsonString.append("\"Guarantee\":\"\"");
									}
									checkForShowingComma = true;
								}
								
								if(attchMnetCount  < validAttachmentLength && checkForShowingComma){
									vJsonString.append(",");
								}
								checkForShowingComma = false;
							}
							if (validAttachmentLength > 0) {
								vJsonString.append("}");
							}
						}
						String ParentCatentryId = lineItem.getItemDetails()
								.getParentCatalogEntryIds()[0];
						CatalogEntryDataBean catEntDb = new CatalogEntryDataBean();
						catEntDb.setCatalogEntryID(ParentCatentryId);
						catEntDb.refreshCopyHelper();
						CatEntrySearchListDataBean parentCtaenbtrySearchBean = new CatEntrySearchListDataBean();
						parentCtaenbtrySearchBean.setIsProduct(true);
						parentCtaenbtrySearchBean.setCatalogId(vCatalogID);
						parentCtaenbtrySearchBean.setSku(catEntDb.getPartNumber());
						parentCtaenbtrySearchBean.setSkuOperator(parentCtaenbtrySearchBean.OPERATOR_EQUAL);
						DataBeanManager.activate(parentCtaenbtrySearchBean);
						CatalogEntryDataBean[] parentCatalogEntityBeans = (CatalogEntryDataBean[]) parentCtaenbtrySearchBean
								.getResultList();
						if (parentCatalogEntityBeans != null && parentCatalogEntityBeans.length > 0) {
							vJsonString.append(",\"ProductDetails\":{");
							for (CatalogEntryDataBean vProductBean : parentCatalogEntityBeans) {
								for (AttachmentDataBean attchmentBean : vProductBean.getAllAttachments()) {
									if (attchmentBean.getFileName().startsWith("PRODUCTDETAILS")) {
										vJsonString
												.append("\"")
												.append(attchmentBean.getObjectPath())
												.append(attchmentBean.getPath())
												.append("\":\"")
												.append(vPartNumber)
												.append("\"");
										break;
									}
								}
							}
							vJsonString.append("}");

						} else {
							vJsonString.append(",\"ProductDetails\":{\"")
									.append("\":\"").append(vPartNumber)
									.append("\"}");
							vJsonString.append("}");
						}
						vJsonString.append(",\"Name\":{\"").append(
								lineItem.getItemDetails().getItemDataBean()
										.getDescription().getName().replaceAll("\"", "\\\\\"")).append(
								"\":\"").append(
								lineItem.getItemDetails().getItemDataBean()
										.getDescription().getName().replaceAll("\"", "\\\\\"")).append(
								"|:-:|").append(vPartNumber).append("\"}");
						if ("PRODUCTCATALOG".equals(vFromPage)) {
							vJsonString.append(",\"ThumbNail\":{\"").append(
									lineItem.getItemDetails().getItemDataBean()
											.getDescription().getThumbNail())
									.append("\":\"").append(vPartNumber)
									.append("\"}");
						}
						if(!"PRODUCTCATALOG".equals(vFromPage) ){
							vJsonString.append(",\"Country\":{\"").append(vCountry).append("\":\"").append(vPartNumber).append("\"}");	
							vJsonString.append(",\"State\":{\"").append(vState).append("\":\"").append(vPartNumber).append("\"}");
							vJsonString.append(",\"City\":{\"").append(vCity).append("\":\"").append(vPartNumber).append("\"}");
							vJsonString.append(",\"SoldTo\":{\"").append(vSoldTo).append("\":\"").append(vPartNumber).append("\"}");
							if("".equals(vHoldDetails)){
								vJsonString.append(",\"HoldDeatils\":{\"").append(vHoldDetails).append("\":\"").append(vPartNumber).append("\"}");
							}else{
								vJsonString.append(",\"HoldDeatils\":{}");
							}
						}
						vJsonString.append(",\"Partnumber\":{\"").append(vPartNumber).append("\":\"").append(vPartNumber).append("\"}");
						vJsonString.append(",\"UserInputQuantity\":{\"").append(userQuantity).append("\":\"").append(vPartNumber).append("\"}");
						

					//	lineItem.setStockCheckDetails(vChkAvailabilityCmd.getOutputDatas());
						int stockchklength = vChkAvailabilityCmd.getOutputDatas().size();
						if(stockchklength > 0) {
							vJsonString.append(",\"StockCheck-Details\":[{");
						}
						int count =0;
						
						for(DomtarStockCheckOutputDataBean vStockItem : vChkAvailabilityCmd.getOutputDatas()){
							if(vStockItem.getPartNumber().equals(items[i])) {
							vJsonString.append("\"AlternateItem1\":\"").append(vStockItem.getAlternativeItem1()).append("\"");
							
							vJsonString.append(",\"AlternateItem2\":\"").append(vStockItem.getAlternativeItem2()).append("\"");
							vJsonString.append(",\"CustomerHoldQuanity\":\"").append(vStockItem.getCstHoldQuantity()).append("\"");
							vJsonString.append(",\"partNumber\":\"").append(vStockItem.getPartNumber()).append("\"");
							vJsonString.append(",\"price\":\"").append(vStockItem.getPrice()).append("\"");
							vJsonString.append(",\"currency\":\"").append(vStockItem.getPriceCurrency()).append("\"");
							vJsonString.append(",\"SHIPDATE\":\"").append(vStockItem.getProdShipDate()).append("\"");
							vJsonString.append(",\"AvailQuantity\":\"").append(vStockItem.getQuantityAvaialable()).append("\"");
							vJsonString.append(",\"HoldQuantity\":\"").append(vStockItem.getQuantityOnHold()).append("\"");
							vJsonString.append(",\"QuanityUnit\":\"").append(vStockItem.getQuantityUnit()).append("\"");
							vJsonString.append(",\"RRC\":\"").append(vStockItem.getRrcInfo()).append("\"");
							vJsonString.append(",\"RRCName\":\"").append(vStockItem.getRrcName()).append("\"");
							vJsonString.append(",\"disclaimer\":\"").append(vStockItem.getDisclaimer()).append("\"");
							vJsonString.append(",\"ReplinishmentExist\":\"").append(vStockItem.isReplinishmentExist()).append("\"");
							vJsonString.append(",\"AlternativeOffer1Exist\":\"").append(vStockItem.isAlternativeOffer1Exist()).append("\"");
							vJsonString.append(",\"AlternativeOffer2Exist\":\"").append(vStockItem.isAlternativeOffer2Exist()).append("\"");
							if(vStockItem.isPriceMissing()){
								vMissingPriceItems++;
							}
							if(vStockItem.getItemErrorMap() != null){							
								vitemErrorCodes =  vStockItem.getItemErrorMap();
							}
							List<DomtarStockCheckOutputDataBean> vRplDetail = vStockItem.getReplinishmentDetails();
							int rplLength = vRplDetail.size();
							if(rplLength >0) {
								vJsonString.append(",\"Replenishment-Details\":{");
							}
							for(DomtarStockCheckOutputDataBean vRplItem : vRplDetail){
								vJsonString.append("\"CustomerHoldQuanity\":\"").append(vRplItem.getCstHoldQuantity()).append("\"");
								vJsonString.append(",\"partNumber\":\"").append(vRplItem.getPartNumber()).append("\"");
								vJsonString.append(",\"price\":\"").append(vRplItem.getPrice()).append("\"");
								vJsonString.append(",\"currency\":\"").append(vRplItem.getPriceCurrency()).append("\"");
								vJsonString.append(",\"SHIPDATE\":\"").append(vRplItem.getProdShipDate()).append("\"");
								vJsonString.append(",\"AvailQuantity\":\"").append(vRplItem.getQuantityAvaialable()).append("\"");
								vJsonString.append(",\"HoldQuantity\":\"").append(vRplItem.getQuantityOnHold()).append("\"");
								vJsonString.append(",\"QuanityUnit\":\"").append(vRplItem.getQuantityUnit()).append("\"");
								vJsonString.append(",\"RRC\":\"").append(vRplItem.getRrcInfo()).append("\"");	
								vJsonString.append(",\"RRCName\":\"").append(vStockItem.getRrcName()).append("\"");
							}
							
							if(rplLength >0) {
								vJsonString.append("}");
							}
							count++;
							if(count < stockchklength){
								vJsonString.append("}");
							} else {
								vJsonString.append("}");
							}
						}
						}
													
							vJsonString.append("]}");
							counter++;
							System.out.println("counter"+counter);
							
							totalCount++;
						
					} else{
						vitemNotExist = true;	
						if("".equals(vNonExistingItem)){
							vNonExistingItem = items[i]+":Invalid Item Code";
						}else{
							vNonExistingItem = vNonExistingItem+"<br/>"+items[i]+":Invalid Item Code";
						}					
						errorCount++;
					}
					
				}

				

			}
		}
		
		if(vMissingPriceItems > 0){
			vPriceMissing = true;
		}
		vTotalCount = totalCount;
		vJsonString.append("}");		
//		System.out.println("vJsonString.toString()>"+vJsonString.toString());
		}catch (Exception e) {
			
			ExceptionHandler.convertToECException(e);
		}
		
		return vJsonString.toString();
	
	}
	
	private String escapeAllHTML(String longDescription) {
		String outPut ="";
		outPut = longDescription.replaceAll("\"", "\\\\\"");
		outPut = outPut.replaceAll("\n", "");
		outPut = outPut.replaceAll("\\/>", ">");
		outPut = outPut.replaceAll("  >", ">");
		outPut = outPut.replaceAll(" >", ">");
		return outPut;
	}
	
	private DomtarStockCheckInputDataBean mapValuesToInputBean(String[] item, String[] qnty, String[] qntyUnit) {
		DomtarStockCheckInputDataBean inputBean = new DomtarStockCheckInputDataBean();
		
		inputBean.setLangId(vLangId);		
		inputBean.setAlternateOffer(false);
		inputBean.setQuanity(qnty);
		inputBean.setQuanityUnit(qntyUnit);
		inputBean.setItems(item);
		inputBean.setCity(vCity);
		inputBean.setCountry(vCountry);
		inputBean.setState(vState);
		inputBean.setCustomerSoldTo(vSoldTo);
		inputBean.setServiceOrganisation(vServiceOrganisation);
		inputBean.setUserId(vUserID);
		inputBean.setContactRep(vContactrep);
		return inputBean;
	}
	private void loadParameters(String[] items2, String[] quantities2,
			TypedProperty requestProperties) {
		int count = 10 ;
		String tempItemcode="";
		String tempQuantity="";
		String tempQuantityUnit="";
		int counter =0;   
		for(int i = 1 ; i <= count ; i++){
			tempItemcode = requestProperties.getString("itemcode"+i, "");
			tempQuantity = requestProperties.getString("quantity"+i, "");
			tempQuantityUnit= requestProperties.getString("quantityUnit"+i, "");
			if(!"".equals(tempItemcode) && "".equals(tempQuantityUnit)){
				CatEntrySearchListDataBean catEntrySearchDB = new CatEntrySearchListDataBean();
				catEntrySearchDB.setPageSize("1");
				catEntrySearchDB.setCatalogId(requestProperties.getString("catalogID", ""));
				catEntrySearchDB.setIsItem(true);
				catEntrySearchDB.setSku(tempItemcode);
				catEntrySearchDB.setSkuOperator(catEntrySearchDB.OPERATOR_EQUAL);
				try {
					DataBeanManager.activate(catEntrySearchDB);
					CatalogEntryDataBean[] CatalogEntryDataBeans = catEntrySearchDB.getResultList();
					if(CatalogEntryDataBeans != null){
						for (CatalogEntryDataBean catalogEntryDataBean : CatalogEntryDataBeans) {
							AttributeValueDataBean[] AttributeValueDataBeans = catalogEntryDataBean.getItemDataBean().getAttributeValueDataBeans();
							for (AttributeValueDataBean attributeValueDataBean : AttributeValueDataBeans) {
								String attribute = attributeValueDataBean.getAttribute().getName();
								if(attribute.equalsIgnoreCase("InventoryUnitOfMeasurement")){
									tempQuantityUnit = attributeValueDataBean.getName();
									break;
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					LOGGER.info("Error in getting the quantityUnitOfMeasurement from WCS DB. "+e.getMessage());
				} 
			}			
			
			if(tempItemcode != ""){
				items[counter] = tempItemcode;
				Quantities[counter] = tempQuantity;
				QuantityUnits[counter] = tempQuantityUnit;
			}
			tempItemcode = "";
			tempQuantity = "";
			tempQuantityUnit="";
			counter++;
		}	
		vCountry = requestProperties.getString("country", "");
		vState = requestProperties.getString("state", "");
		vCity = requestProperties.getString("city", "");
		vSoldTo = requestProperties.getString("soldTo", "");
		vServiceOrganisation = requestProperties.getString("serviceOrganisation", "");
		vContactrep = requestProperties.getString("contactrep", "");
		vStoreID = requestProperties.getString("storeID", "");
		vCatalogID = requestProperties.getString("catalogID", "");
		vUserID = requestProperties.getString("userId", "");
		vLangId = requestProperties.getString("langId", "");
		pageSize = requestProperties.getIntValue("pageSize", 0);
		beginIndex = requestProperties.getIntValue("beginIndex", 0);
		vDisplayDetailsContent = requestProperties.getString("displayDetailsContent", "false");
		vShipTo =  requestProperties.getString("shipTo", "");
		vHoldDetails =  requestProperties.getString("holdDetails", "");
	}
	
	
}
