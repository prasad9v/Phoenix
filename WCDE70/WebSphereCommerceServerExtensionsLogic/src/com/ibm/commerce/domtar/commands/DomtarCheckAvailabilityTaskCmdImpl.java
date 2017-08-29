package com.ibm.commerce.domtar.commands;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.soap.SOAPFault;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.commons.json.JSON;
import org.apache.commons.json.JSONArray;
import org.apache.commons.json.JSONException;
import org.apache.commons.json.JSONObject;

import com.ibm.commerce.beans.DataBeanManager;
import com.ibm.commerce.command.TaskCommandImpl;
import com.ibm.commerce.domtar.databeans.DomtarStockCheckInputDataBean;
import com.ibm.commerce.domtar.databeans.DomtarStockCheckOutputDataBean;
import com.ibm.commerce.domtar.helpers.DomtarHelper;
import com.ibm.commerce.domtar.server.DomtarServerHelper;
import com.ibm.commerce.domtar.testdata.DomtarStubData;
import com.ibm.commerce.domtar.util.DomtarJDBCHelper;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.exception.ECSystemException;
import com.ibm.commerce.foundation.common.util.logging.LoggingHelper;
import com.ibm.commerce.ras.ECMessage;
import com.ibm.commerce.search.beans.CatEntrySearchListDataBean;

import domtar.dnet.drv1dvai01.stockitemcheckandpriceservice.AlternateItem;
import domtar.dnet.drv1dvai01.stockitemcheckandpriceservice.IsAlternateItemRequest;
import domtar.dnet.drv1dvai01.stockitemcheckandpriceservice.Item;
import domtar.dnet.drv1dvai01.stockitemcheckandpriceservice.ObjectFactory;
import domtar.dnet.drv1dvai01.stockitemcheckandpriceservice.ResponseItem;
import domtar.dnet.drv1dvai01.stockitemcheckandpriceservice.StockCheckRequest;
import domtar.dnet.drv1dvai01.stockitemcheckandpriceservice.StockCheckResponse;
import domtar.dnet.drv1dvai01.stockitemcheckandpriceservice.StockItemCheckAndPriceInput;
import domtar.dnet.drv1dvai01.stockitemcheckandpriceservice.StockItemCheckAndPriceOutput;
import domtar.dnet.drv1dvai01.stockitemcheckandpriceservice.StockItemCheckAndPricePort;
import domtar.dnet.drv1dvai01.stockitemcheckandpriceservice.StockItemCheckAndPriceServicePortProxy;

public class DomtarCheckAvailabilityTaskCmdImpl extends TaskCommandImpl
		implements DomtarCheckAvailabilityTaskCmd {
	
	private static final Logger LOGGER = LoggingHelper.getLogger(DomtarCheckAvailabilityTaskCmdImpl.class);
	
	private static final String STOCKCHECK_AND_HOLD_USERNAME = "domtar.webmethods.username";
	private static final String STOCKCHECK_AND_HOLD_PASSWORD = "domtar.webmethods.password";
	
	private static final String CALLING_SYSTEM = "Ecom";
	private static final String SALES_SERVICE_ORG = "2";
	private static final String CURRENCY = "USD";
	private static final String LANGUAGE = "ENG";
	private  boolean visTimeOut = false; 
	public String stockCheckWebServiceError;
	public boolean priceMissing = false;	
	
	private DomtarStockCheckInputDataBean vInputDatas;
	private List<DomtarStockCheckOutputDataBean> vOutputDatas = new ArrayList<DomtarStockCheckOutputDataBean>();
	@Override
	public List<DomtarStockCheckOutputDataBean> getOutputDatas() {
		return vOutputDatas;
	}

	@Override
	public void setInputData(DomtarStockCheckInputDataBean pInputDatas) {
		vInputDatas = pInputDatas;
		
	}
	public DomtarCheckAvailabilityTaskCmdImpl() {
		super();
	}
	
	public void performExecute() throws ECException {
		super.performExecute();
		
		if(DomtarServerHelper.isDevelopmentEnvironment())
			vOutputDatas = getStockDetailsFromStub(vInputDatas);
		else
			vOutputDatas = getStockDetails(vInputDatas);		
	}
	

	/*
	 * Retrieves the  stock details by invoking the StockItemCheckAndPrice Web Service
	 */
	private List<DomtarStockCheckOutputDataBean> getStockDetails(
			DomtarStockCheckInputDataBean inputDatas)  throws ECException  {
		
		//Fetch the webmethods username and password from JVM custom properties.
		String userName = System.getProperty(STOCKCHECK_AND_HOLD_USERNAME);
		String password = System.getProperty(STOCKCHECK_AND_HOLD_PASSWORD);
		
		List<DomtarStockCheckOutputDataBean> stockCheckOutPuts = new ArrayList<DomtarStockCheckOutputDataBean>();
		Calendar cal = Calendar.getInstance();
	    Date currentDate = cal.getTime();
	    SimpleDateFormat date_format = new SimpleDateFormat("yyyyMMddHHmm");
	    DomtarJDBCHelper jdbcHelper = new DomtarJDBCHelper();
		try {
			String userId = getCommandContext().getUserId().toString();
			ObjectFactory objectFactory = new ObjectFactory();
			
			StockCheckRequest stockCheckRequest = objectFactory.createStockCheckRequest();
			stockCheckRequest.setTrackingId(objectFactory.createStockCheckRequestTrackingId(date_format.format(currentDate)));
			stockCheckRequest.setSoldTo(inputDatas.getCustomerSoldTo());
			/*
			 * Setting dummy value for contact since it is mandatory in WebMethods[XSD Validation] 
			 * and not mandatory in BAAN.
			 */
			stockCheckRequest.setContact("Dummy");
			stockCheckRequest.setDemandCity(inputDatas.getCity());
			stockCheckRequest.setDemandState(inputDatas.getState());
			stockCheckRequest.setDemandCountry(inputDatas.getCountry());
			stockCheckRequest.setRequestorName(DomtarHelper.getLogonIdByUserId(userId));
			stockCheckRequest.setCurrency(CURRENCY);
			stockCheckRequest.setLanguage(LANGUAGE);
			stockCheckRequest.setSalesServiceOrg(new BigInteger(SALES_SERVICE_ORG));
			if(inputDatas.isAlternateOffer())
				stockCheckRequest.setIsAlternateItemRequest(objectFactory.createStockCheckRequestIsAlternateItemRequest(IsAlternateItemRequest.Y));
			else
				stockCheckRequest.setIsAlternateItemRequest(objectFactory.createStockCheckRequestIsAlternateItemRequest(IsAlternateItemRequest.N));
			stockCheckRequest.setCallingSystem(CALLING_SYSTEM);
			
			for (int i = 0; i < inputDatas.getItems().length; i++) {				
				String itemCode = inputDatas.getItems()[i];	
				if(itemCode != null) {
					Item item = objectFactory.createItem();
					item.setItemCode(itemCode);
					
					if(!inputDatas.isAlternateOffer()){
						String itemQuantity = inputDatas.getQuanity()[i];						
						item.setQuantity(objectFactory.createItemQuantity(new BigInteger(itemQuantity)));						
					}else{
						item.setQuantity(objectFactory.createItemQuantity(new BigInteger("0")));
					}
					String itemQuantityUnit = inputDatas.getQuanityUnit()[i];
					item.setQuantityUnit(objectFactory.createItemQuantityUnit(itemQuantityUnit));
					stockCheckRequest.getItem().add(item);	
				}
			}
			//Log StockCheck Request
			logStockCheckRequest(stockCheckRequest);
			StockItemCheckAndPriceInput stockCheckInput = objectFactory.createStockItemCheckAndPriceInput();
			stockCheckInput.setStockCheckRequest(stockCheckRequest);
			
			
			StockItemCheckAndPriceServicePortProxy stockCheckProxy = new StockItemCheckAndPriceServicePortProxy();
			/*
			 * Set the username and password for Authenticating the web service. Basic Authentication is 
			 * used here.
			 */
			StockItemCheckAndPricePort stockCheckPort = stockCheckProxy._getDescriptor().getProxy();
			((BindingProvider) stockCheckPort).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, userName);
			((BindingProvider) stockCheckPort).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, password);
			
			StockItemCheckAndPriceOutput stockCheckOutput = stockCheckPort.stockItemCheckAndPrice(stockCheckInput);
			StockCheckResponse stockCheckResponse= stockCheckOutput.getStockCheckResponse();
			
			logStockCheckResponse(stockCheckResponse);
			//New
			HashMap<String, String> itemErrorMap = new HashMap<String, String>();
			String callError = null;
			if(stockCheckResponse.getCallError() != null && stockCheckResponse.getCallError().getValue() != null){
				callError = stockCheckResponse.getCallError().getValue();				
			}
			setStockCheckWebServiceError(callError);
			//New
			List<ResponseItem> responseItemList = stockCheckResponse.getItem();
			for (ResponseItem responseItem : responseItemList) {
	
				DomtarStockCheckOutputDataBean outPut = new DomtarStockCheckOutputDataBean();
				String itemError = null;
				String itemCode = responseItem.getItemCode();
				if(responseItem.getItemError() != null && responseItem.getItemError().getValue() != null){
					itemError = responseItem.getItemError().getValue();
					itemErrorMap.put(itemCode, itemError);
				}
				
				outPut.setItemErrorMap(itemErrorMap);
				outPut.setCstHoldQuantity(responseItem.getCustomerHoldQuantity().getValue());
				outPut.setPartNumber(responseItem.getItemCode());
				if(responseItem.getDiscountedPrice() != null && responseItem.getDiscountedPrice().getValue() !=null && responseItem.getDiscountedPrice().getValue() != "0"){
					outPut.setPrice(responseItem.getDiscountedPrice().getValue());
				}else if(responseItem.getListPrice() != null && responseItem.getListPrice() != "0"){
					outPut.setPrice(responseItem.getListPrice());
				} else{
					outPut.setPriceMissing(true);
				}
				setPriceMissing(outPut.isPriceMissing());
				if(responseItem.getDiscountedPriceCurrency() != null && responseItem.getDiscountedPriceCurrency().getValue() !=null ){
					outPut.setPriceCurrency(responseItem.getDiscountedPriceCurrency().getValue());
				}else{
					outPut.setPriceCurrency(responseItem.getListPriceCurrency());
				}
				
				outPut.setProdShipDate(formatShipdate(responseItem.getProdShipDate()));
				outPut.setQuantityAvaialable(responseItem.getQuantityAvailable());
				outPut.setQuantityOnHold(responseItem.getQuantityOnHold());
				outPut.setQuantityUnit(responseItem.getQuantityUnit());
				outPut.setRrcInfo(responseItem.getSourcingLocation());					
				try{
					outPut.setRrcName(jdbcHelper.findRRCNameByRRCId(responseItem.getSourcingLocation()));	
				}catch(Exception e){
					e.printStackTrace();
				}
							
				outPut.setDisclaimer("All Standard Upcharges Apply");
				
				if(responseItem.getAlternateItem() != null){
					List<AlternateItem> alternateItemList = responseItem.getAlternateItem();
					int listSize = alternateItemList.size();
					for(int i=0; i < listSize; i++)
					{
						/*
						 * TODO : Max 2 alternate items per item code is allowed as of now.
						 * Code Needs to be refractored when requirement changes for no: of alternate items
						 */
						AlternateItem alternateItem = alternateItemList.get(i);
						if (alternateItem != null){
							if(i==0 && alternateItem.getAlternateItemCode() != null && alternateItem.getAlternateItemCode().getValue() !=null ){
								outPut.setAlternativeItem1(alternateItem.getAlternateItemCode().getValue());
							}							
							else if(i==1 && alternateItem.getAlternateItemCode() != null && alternateItem.getAlternateItemCode().getValue() !=null){
								outPut.setAlternativeItem2(alternateItem.getAlternateItemCode().getValue());
							}		
						}
					}
				}
				
				
				List<DomtarStockCheckOutputDataBean> replOutputs = new ArrayList<DomtarStockCheckOutputDataBean>();
	
				DomtarStockCheckOutputDataBean replOutput = new DomtarStockCheckOutputDataBean();
				if(responseItem.getReplenishmentCustomerHoldQuantity() != null && responseItem.getReplenishmentCustomerHoldQuantity().getValue() !=null)
					replOutput.setCstHoldQuantity(responseItem.getReplenishmentCustomerHoldQuantity().getValue());
				replOutput.setPartNumber(responseItem.getItemCode());
				if(responseItem.getReplenishmentDiscountedPrice() != null && responseItem.getReplenishmentDiscountedPrice().getValue() !=null )
					replOutput.setPrice(responseItem.getReplenishmentDiscountedPrice().getValue());
				if (responseItem.getReplenishmentDiscountedPriceCurrency() != null && responseItem.getReplenishmentDiscountedPriceCurrency().getValue() !=null)
					replOutput.setPriceCurrency(responseItem.getReplenishmentDiscountedPriceCurrency().getValue());
				if (responseItem.getReplenishmentProdShipDate() !=null && responseItem.getReplenishmentProdShipDate().getValue() != null)
					replOutput.setProdShipDate(formatShipdate(responseItem.getReplenishmentProdShipDate().getValue()));
				if(responseItem.getReplenishmentQuantityAvailable() !=null && responseItem.getReplenishmentQuantityAvailable().getValue() != null)
					replOutput.setQuantityAvaialable(responseItem.getReplenishmentQuantityAvailable().getValue());
				if (responseItem.getReplenishmentQuantityOnHold() != null && responseItem.getReplenishmentQuantityOnHold().getValue() != null)
					replOutput.setQuantityOnHold(responseItem.getReplenishmentQuantityOnHold().getValue());
				replOutput.setQuantityUnit(responseItem.getQuantityUnit());
				if (responseItem.getReplenishmentSourceLocation() != null && responseItem.getReplenishmentSourceLocation().getValue() != null){
					replOutput.setRrcInfo(responseItem.getReplenishmentSourceLocation().getValue());
					try{
						replOutput.setRrcName(jdbcHelper.findRRCNameByRRCId(responseItem.getReplenishmentSourceLocation().getValue()));	
					}catch(Exception e){
						e.printStackTrace();
					}
				}					
				if(!responseItem.getReplenishmentQuantityAvailable().getValue().equals("0")){
					replOutputs.add(replOutput);
				}
					
				outPut.setReplinishmentDetails(replOutputs);
				if(replOutputs.size() > 0){
					outPut.setReplinishmentExist(true);
				}		
				CatEntrySearchListDataBean catEntrySearchDB = new CatEntrySearchListDataBean();
				if(outPut.getAlternativeItem1() != null && !"".equals(outPut.getAlternativeItem1())){
					catEntrySearchDB.setPageSize("1");
					catEntrySearchDB.setCatalogId(getCommandContext().getRequestProperties().getString("catalogID", ""));
					catEntrySearchDB.setIsItem(true);
					catEntrySearchDB.setSku(outPut.getAlternativeItem1());
					catEntrySearchDB.setSkuOperator(catEntrySearchDB.OPERATOR_EQUAL);
					try{
						DataBeanManager.activate(catEntrySearchDB);
						if(Integer.parseInt(catEntrySearchDB.getResultCount()) > 0){
							outPut.setAlternativeOffer1Exist(true);
						}
					}catch (ECException e) {								
						e.printStackTrace();
					}
				}
				if(outPut.getAlternativeItem2() != null && !"".equals(outPut.getAlternativeItem2())){
					catEntrySearchDB.setPageSize("1");
					catEntrySearchDB.setCatalogId(getCommandContext().getRequestProperties().getString("catalogID", ""));
					catEntrySearchDB.setIsItem(true);
					catEntrySearchDB.setSku(outPut.getAlternativeItem2());
					catEntrySearchDB.setSkuOperator(catEntrySearchDB.OPERATOR_EQUAL);
					try{
						DataBeanManager.activate(catEntrySearchDB);
						if(Integer.parseInt(catEntrySearchDB.getResultCount()) > 0){
							outPut.setAlternativeOffer2Exist(true);
						}
					}catch (ECException e) {								
						e.printStackTrace();
					}
				}
				stockCheckOutPuts.add(outPut);
			}
		
		}catch (SOAPFaultException e) {
			visTimeOut = true;
			e.printStackTrace();
			System.out.println("SOAPFaultException Message : "+e.getMessage());
			SOAPFault soapFault= e.getFault();
			System.out.println("getFaultString() :"+soapFault.getFaultString());
			
		}catch (Exception e){			
			visTimeOut = true;
			e.printStackTrace();
		}
		
		return stockCheckOutPuts;
	}
	
	private String formatShipdate(String prodShipDate) {
		DateFormat fromFormat = new SimpleDateFormat("yyyyMMdd");
		fromFormat.setLenient(false);
		DateFormat toFormat = new SimpleDateFormat("MM/dd/yyyy");
		toFormat.setLenient(false);
		String formatedDate = "";
		try {
			Date date = fromFormat.parse(prodShipDate);
			formatedDate = toFormat.format(date);
		} catch (ParseException e) {
			LOGGER.info("Unable to parse date : "+prodShipDate);
		}
		if(formatedDate.equals(""))
			return prodShipDate;
		else
			return formatedDate;
	}

	private void logStockCheckRequest(StockCheckRequest stockCheckRequest) {
		StringBuffer sb = new StringBuffer("<StockCheckRequest>\n");
		sb.append("<TrackingId>"+stockCheckRequest.getTrackingId().getValue()+"</TrackingId>\n");
		sb.append("<SoldTo>"+stockCheckRequest.getSoldTo()+"</SoldTo>\n");
		sb.append("<Contact>"+stockCheckRequest.getContact()+"</Contact>\n");
		sb.append("<DemandCity>"+stockCheckRequest.getDemandCity()+"</DemandCity>\n");
		sb.append("<DemandState>"+stockCheckRequest.getDemandState()+"</DemandState>\n");
		sb.append("<DemandCountry>"+stockCheckRequest.getDemandCountry()+"</DemandCountry>\n");
		sb.append("<RequestorName>"+stockCheckRequest.getRequestorName()+"</RequestorName>\n");
		sb.append("<Currency>"+stockCheckRequest.getCurrency()+"</Currency>\n");	
		sb.append("<Language>"+stockCheckRequest.getLanguage()+"</Language>\n");	
		sb.append("<SalesServiceOrg>"+stockCheckRequest.getSalesServiceOrg().toString()+"</SalesServiceOrg>\n");	
		sb.append("<IsAlternateItemRequest>"+stockCheckRequest.getIsAlternateItemRequest().getValue().value()+"</IsAlternateItemRequest>\n");	
		sb.append("<CallingSystem>"+stockCheckRequest.getCallingSystem()+"</CallingSystem>\n");
		List<Item> responseItemList = stockCheckRequest.getItem();
		for (Iterator iterator = responseItemList.iterator(); iterator.hasNext();) {
			Item item = (Item) iterator.next();
			sb.append("<Item>\n");
			sb.append("<ItemCode>"+item.getItemCode()+"</ItemCode>\n");
			//if(stockCheckRequest.getIsAlternateItemRequest().getValue().value().equals("N")){
				sb.append("<Quantity>"+item.getQuantity().getValue().toString()+"</Quantity>\n");
				sb.append("<QuantityUnit>"+item.getQuantityUnit().getValue()+"</QuantityUnit>\n");
			//}
			sb.append("</Item>\n");
		}
		sb.append("</StockCheckRequest>");
		LOGGER.info("Stock Check Request :\n"+sb.toString());
	}
	
	private void logStockCheckResponse(StockCheckResponse stockCheckResponse) {
		StringBuffer sb = new StringBuffer("<StockCheckResponse>\n");
		sb.append("<TrackingId>"+stockCheckResponse.getTrackingId().getValue()+"</TrackingId>\n");
		if(stockCheckResponse.getCallError() != null && stockCheckResponse.getCallError().getValue() != null){
			sb.append("<CallError>"+stockCheckResponse.getCallError().getValue()+"</CallError>\n");
		}
		List<ResponseItem> responseItemList = stockCheckResponse.getItem();
		for (Iterator iterator = responseItemList.iterator(); iterator.hasNext();) {
			ResponseItem responseItem = (ResponseItem) iterator.next();
			sb.append("<Item>\n");
			sb.append("<ItemCode>"+responseItem.getItemCode()+"</ItemCode>\n");
			if(responseItem.getItemError() != null && responseItem.getItemError().getValue() != null)
				sb.append("<ItemError>"+responseItem.getItemError().getValue()+"</ItemError>\n");
			sb.append("<SourcingLocation>"+responseItem.getSourcingLocation()+"</SourcingLocation>\n");
			sb.append("<ProdShipDate>"+responseItem.getProdShipDate()+"</ProdShipDate>\n");
			sb.append("<PlannedDeliveryDate>"+responseItem.getPlannedDeliveryDate()+"</PlannedDeliveryDate>\n");
			sb.append("<QuantityOnHold>"+responseItem.getQuantityOnHold()+"</QuantityOnHold>\n");
			sb.append("<QuantityUnit>"+responseItem.getQuantityUnit()+"</QuantityUnit>\n");
			sb.append("<QuantityAvailable>"+responseItem.getQuantityAvailable()+"</QuantityAvailable>\n");
			sb.append("<ListPrice>"+responseItem.getListPrice()+"</ListPrice>\n");
			sb.append("<ListPriceCurrency>"+responseItem.getListPriceCurrency()+"</ListPriceCurrency>\n");
			sb.append("<ListPriceUnit>"+responseItem.getListPriceUnit()+"</ListPriceUnit>\n");
			if(responseItem.getDiscountedPrice() != null && responseItem.getDiscountedPrice().getValue() != null)
				sb.append("<DiscountedPrice>"+responseItem.getDiscountedPrice().getValue()+"</DiscountedPrice>\n");
			if(responseItem.getDiscountedPriceCurrency() != null && responseItem.getDiscountedPriceCurrency().getValue() != null)
				sb.append("<DiscountedPriceCurrency>"+responseItem.getDiscountedPriceCurrency().getValue()+"</DiscountedPriceCurrency>\n");
			if(responseItem.getDiscountedPriceUnit() != null && responseItem.getDiscountedPriceUnit().getValue() != null)
				sb.append("<DiscountedPriceUnit>"+responseItem.getDiscountedPriceUnit().getValue()+"</DiscountedPriceUnit>\n");
			if(responseItem.getCustomerHoldQuantity() != null && responseItem.getCustomerHoldQuantity().getValue() != null)
				sb.append("<CustomerHoldQuantity>"+responseItem.getCustomerHoldQuantity().getValue()+"</CustomerHoldQuantity>\n");
			if(responseItem.getReplenishmentSourceLocation() != null && responseItem.getReplenishmentSourceLocation().getValue() != null)
				sb.append("<ReplenishmentSourceLocation>"+responseItem.getReplenishmentSourceLocation().getValue()+"</ReplenishmentSourceLocation>\n");
			if(responseItem.getReplenishmentProdShipDate() != null && responseItem.getReplenishmentProdShipDate().getValue() != null)
				sb.append("<ReplenishmentProdShipDate>"+responseItem.getReplenishmentProdShipDate().getValue()+"</ReplenishmentProdShipDate>\n");
			if(responseItem.getReplenishmentPlannedDeliveryDate() != null && responseItem.getReplenishmentPlannedDeliveryDate().getValue() != null)
				sb.append("<ReplenishmentPlannedDeliveryDate>"+responseItem.getReplenishmentPlannedDeliveryDate().getValue()+"</ReplenishmentPlannedDeliveryDate>\n");
			if(responseItem.getReplenishmentQuantityOnHold() != null && responseItem.getReplenishmentQuantityOnHold().getValue() != null)
				sb.append("<ReplenishmentQuantityOnHold>"+responseItem.getReplenishmentQuantityOnHold().getValue()+"</ReplenishmentQuantityOnHold>\n");
			if(responseItem.getReplenishmentQuantityAvailable() != null && responseItem.getReplenishmentQuantityAvailable().getValue() != null)
				sb.append("<ReplenishmentQuantityAvailable>"+responseItem.getReplenishmentQuantityAvailable().getValue()+"</ReplenishmentQuantityAvailable>\n");
			if(responseItem.getReplenishmentListPrice() != null && responseItem.getReplenishmentListPrice().getValue() != null)
				sb.append("<ReplenishmentListPrice>"+responseItem.getReplenishmentListPrice().getValue()+"</ReplenishmentListPrice>\n");
			if(responseItem.getReplenishmentListPriceCurrency() != null && responseItem.getReplenishmentListPriceCurrency().getValue() != null)
				sb.append("<ReplenishmentListPriceCurrency>"+responseItem.getReplenishmentListPriceCurrency().getValue()+"</ReplenishmentListPriceCurrency>\n");
			if(responseItem.getReplenishmentListPriceUnit() != null && responseItem.getReplenishmentListPriceUnit().getValue() != null)
				sb.append("<ReplenishmentListPriceUnit>"+responseItem.getReplenishmentListPriceUnit().getValue()+"</ReplenishmentListPriceUnit>\n");
			if(responseItem.getReplenishmentDiscountedPrice() != null && responseItem.getReplenishmentDiscountedPrice().getValue() != null)
				sb.append("<ReplenishmentDiscountedPrice>"+responseItem.getReplenishmentDiscountedPrice().getValue()+"</ReplenishmentDiscountedPrice>\n");
			if(responseItem.getReplenishmentDiscountedPriceCurrency() != null && responseItem.getReplenishmentDiscountedPriceCurrency().getValue() != null)
				sb.append("<ReplenishmentDiscountedPriceCurrency>"+responseItem.getReplenishmentDiscountedPriceCurrency().getValue()+"</ReplenishmentDiscountedPriceCurrency>\n");
			if(responseItem.getReplenishmentDiscountedPriceUnit() != null && responseItem.getReplenishmentDiscountedPriceUnit().getValue() != null)
				sb.append("<ReplenishmentDiscountedPriceUnit>"+responseItem.getReplenishmentDiscountedPriceUnit().getValue()+"</ReplenishmentDiscountedPriceUnit>\n");
			if(responseItem.getReplenishmentCustomerHoldQuantity() != null && responseItem.getReplenishmentCustomerHoldQuantity().getValue() != null)
				sb.append("<ReplenishmentCustomerHoldQuantity>"+responseItem.getReplenishmentCustomerHoldQuantity().getValue()+"</ReplenishmentCustomerHoldQuantity>\n");
			if(responseItem.getAlternateItem() != null){
				List<AlternateItem> alternateItemList = responseItem.getAlternateItem();
				for (Iterator iterator2 = alternateItemList.iterator(); iterator2.hasNext();) {
					AlternateItem alternateItem = (AlternateItem) iterator2.next();
					if (alternateItem != null){
						sb.append("<AlternateItem>\n");
						if(alternateItem.getAlternateItemCode() != null && alternateItem.getAlternateItemCode().getValue() != null)
							sb.append("<AlternateItemCode>"+alternateItem.getAlternateItemCode().getValue()+"</AlternateItemCode>\n");
						sb.append("</AlternateItem>\n");
					}
				}
			}			
			sb.append("</Item>\n");
		}
		sb.append("</StockCheckResponse>");
		LOGGER.info("Stock Check Response :\n"+sb.toString());
	}

	private List<DomtarStockCheckOutputDataBean> getStockDetailsFromStub(
			DomtarStockCheckInputDataBean inputDatas)  throws ECException {
		List<DomtarStockCheckOutputDataBean> outPuts = new ArrayList<DomtarStockCheckOutputDataBean>(); 
		try {
			InputStream is = new FileInputStream(DomtarStubData.STOCK_CHECK_DATA_PATH);
			JSONObject json =  (JSONObject)JSON.parse(getStringFromInputStream(is));
			JSONArray itemMainDetails ;
			JSONObject itemDetails ;
			JSONObject replItemDetails ;
			DomtarJDBCHelper jdbcHelper = new DomtarJDBCHelper();	
			
			String[] vItemsError = new String[10];
			HashMap<String, String> itemErrorMap = new HashMap<String, String>();
			for(int i = 0 ; i < vInputDatas.getItems().length ; i++){
				if(vInputDatas.getItems()[i] != null) {
					DomtarStockCheckOutputDataBean outPut = new DomtarStockCheckOutputDataBean();
					if(json.has(vInputDatas.getItems()[i].toUpperCase())){
						itemMainDetails = (JSONArray) json.get(vInputDatas.getItems()[i].toUpperCase());
						itemDetails =itemMainDetails.getJSONObject(0).getJSONObject("ITEMDETAILS");
						replItemDetails =itemMainDetails.getJSONObject(0).getJSONObject("REPLITEMDETAILS");
						outPut.setAlternativeItem1(itemDetails.getString("AlternateItem1"));
						outPut.setAlternativeItem2(itemDetails.getString("AlternateItem2"));
						outPut.setCstHoldQuantity(itemDetails.getString("CustomerHoldQuanity"));
						outPut.setPartNumber(vInputDatas.getItems()[i]);
						if(itemDetails.has("price") && !itemDetails.isNull("price")){
							if(itemDetails.getString("price").equals("0") || itemDetails.getString("price").equals("")){
								outPut.setPriceMissing(true);
							}else{
								outPut.setPrice(itemDetails.getString("price"));
							}
						} else {
							outPut.setPriceMissing(true);
						}		
						setPriceMissing(outPut.isPriceMissing());
						outPut.setPriceCurrency(itemDetails.getString("currency"));
						outPut.setProdShipDate(formatShipdate(itemDetails.getString("SHIPDATE")));
						outPut.setQuantityAvaialable(itemDetails.getString("AvailQuantity"));
						outPut.setQuantityOnHold(itemDetails.getString("HoldQuantity"));
						outPut.setQuantityUnit(itemDetails.getString("QuanityUnit"));
						outPut.setRrcInfo(itemDetails.getString("RRC"));
						/* Modified for 124, to display rrc name instead of rrc id
						 * But in local they have given the name directly				
						 * */
						try{
							outPut.setRrcName(jdbcHelper.findRRCNameByRRCId(replItemDetails.getString("RRC")));	
						}catch(Exception e){
							e.printStackTrace();
						}
						outPut.setDisclaimer("All Standard Upcharges Apply");
						if("i2507".equalsIgnoreCase(vInputDatas.getItems()[i])){
							 
							 itemErrorMap.put(vInputDatas.getItems()[i],"STK101");
							 outPut.setItemErrorMap(itemErrorMap);
						}
						if("i2508".equalsIgnoreCase(vInputDatas.getItems()[i])){
							 
							 itemErrorMap.put(vInputDatas.getItems()[i],"STK102");
							 outPut.setItemErrorMap(itemErrorMap);
						}
						if("i2510".equalsIgnoreCase(vInputDatas.getItems()[i])){
							 
							setStockCheckWebServiceError("STK001");
						}
						List<DomtarStockCheckOutputDataBean> replOutputs = new ArrayList<DomtarStockCheckOutputDataBean>(); 
						if(replItemDetails != null){
							DomtarStockCheckOutputDataBean replOutput = new DomtarStockCheckOutputDataBean();
							replOutput.setCstHoldQuantity(replItemDetails.getString("CustomerHoldQuanity"));
							replOutput.setPartNumber(vInputDatas.getItems()[i]);
							replOutput.setPrice(replItemDetails.getString("price"));
							replOutput.setPriceCurrency(itemDetails.getString("currency"));
							replOutput.setProdShipDate(formatShipdate(replItemDetails.getString("SHIPDATE")));
							replOutput.setQuantityAvaialable(replItemDetails.getString("AvailQuantity"));
							replOutput.setQuantityOnHold(replItemDetails.getString("HoldQuantity"));
							replOutput.setQuantityUnit(replItemDetails.getString("QuanityUnit"));
							replOutput.setRrcInfo(itemDetails.getString("RRC"));
							 /* Modified for 124, to display rrc name instead of rrc id
							 * But in local they have given the name directly
							 * 
							 * */
							
							try{
								replOutput.setRrcName(jdbcHelper.findRRCNameByRRCId(replItemDetails.getString("RRC")));	
							}catch(Exception e){
								e.printStackTrace();
							}
							
							if(!replItemDetails.getString("AvailQuantity").equals("0") && !replItemDetails.getString("AvailQuantity").equals(""))
								replOutputs.add(replOutput);				
						}
						outPut.setReplinishmentDetails(replOutputs);
						if(replOutputs.size() > 0){
							outPut.setReplinishmentExist(true);
						}
						CatEntrySearchListDataBean catEntrySearchDB = new CatEntrySearchListDataBean();
						
						if(outPut.getAlternativeItem1() != null && !"".equals(outPut.getAlternativeItem1())){
							catEntrySearchDB.setPageSize("1");
							catEntrySearchDB.setCatalogId(getCommandContext().getRequestProperties().getString("catalogID", ""));
							catEntrySearchDB.setIsItem(true);
							catEntrySearchDB.setSku(outPut.getAlternativeItem1());
							catEntrySearchDB.setSkuOperator(catEntrySearchDB.OPERATOR_EQUAL);
							try{
								DataBeanManager.activate(catEntrySearchDB);								
								if(Integer.parseInt(catEntrySearchDB.getResultCount()) > 0){
									outPut.setAlternativeOffer1Exist(true);
								}
							}catch (ECException e) {								
								e.printStackTrace();
							}							
						}
						if(outPut.getAlternativeItem2() != null && !"".equals(outPut.getAlternativeItem2())){
							catEntrySearchDB.setPageSize("1");
							catEntrySearchDB.setCatalogId(getCommandContext().getRequestProperties().getString("catalogID", ""));
							catEntrySearchDB.setIsItem(true);
							catEntrySearchDB.setSku(outPut.getAlternativeItem2());
							catEntrySearchDB.setSkuOperator(catEntrySearchDB.OPERATOR_EQUAL);
							try{
								DataBeanManager.activate(catEntrySearchDB);
								if(Integer.parseInt(catEntrySearchDB.getResultCount()) > 0){
									outPut.setAlternativeOffer2Exist(true);
								}
							}catch (ECException e) {								
								e.printStackTrace();
							}	
						}
						outPuts.add(outPut);
					}
				}
			}			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return outPuts;
	}
	// convert InputStream to String
	private static String getStringFromInputStream(InputStream is) {
 
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
 
		String line;
		try {
 
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
 
		return sb.toString();
 
	}
	public void validateParameters() throws ECException {
		if(vInputDatas.getItems() == null || (vInputDatas.getItems()!= null && vInputDatas.getItems().length == 0)){
			throw new ECSystemException(ECMessage._ERR_BAD_PARMS,
				 	this.getClass().getName(), "validateParameters");
		}
	}

	@Override
	public boolean getTimeOut() {
		// TODO Auto-generated method stub
		return visTimeOut;
	}

	@Override
	public void setTimeout(boolean ptimeOut) {
		// TODO Auto-generated method stub
		visTimeOut = ptimeOut;
	}

	/**
	 * @return the stockCheckWebServiceError
	 */
	public String getStockCheckWebServiceError() {
		return stockCheckWebServiceError;
	}

	/**
	 * @param stockCheckWebServiceError the stockCheckWebServiceError to set
	 */
	public void setStockCheckWebServiceError(String stockCheckWebServiceError) {
		this.stockCheckWebServiceError = stockCheckWebServiceError;
	}

	public boolean isPriceMissing() {
		return priceMissing;
	}

	public void setPriceMissing(boolean priceMissing) {
		this.priceMissing = priceMissing;
	}
	
}
