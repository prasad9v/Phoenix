package com.ibm.commerce.domtar.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.json.JSONArtifact;

import com.ibm.commerce.attachment.beans.AttachmentDataBean;
import com.ibm.commerce.beans.DataBeanManager;
import com.ibm.commerce.catalog.beans.AttributeValueDataBean;
import com.ibm.commerce.catalog.beans.CatalogEntryDataBean;
import com.ibm.commerce.command.ControllerCommandImpl;
import com.ibm.commerce.datatype.TypedProperty;
import com.ibm.commerce.domtar.databeans.DomtarStockCheckInputDataBean;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.exception.ExceptionHandler;
import com.ibm.commerce.search.beans.CatEntrySearchListDataBean;
import com.ibm.commerce.server.ECConstants;

public class DomtarAttributeSearchControllerCmdImpl extends
		ControllerCommandImpl implements
		DomtarAttributeSearchControllerCmd {
	private String vLangId;
	private String vAttribute1;
	private String vAttributevalue1;
	private String vAttributeValueType1;
	private String vAttribute2;
	private String vAttributevalue2;
	private String vAttributeValueType2;
	private String vAttribute3;
	private String vAttributevalue3;
	private String vAttributeValueType3;
	private String vAttribute4;
	private String vAttributevalue4;
	private String vAttributeValueType4;
	private String vAttribute5;
	private String vAttributevalue5;
	private String vAttributeValueType5;
	private String vAttribute6;
	private String vAttributevalue6;
	private String vAttributeValueType6;
	private String vAttribute7;
	private String vAttributevalue7;
	private String vAttributeValueType7;
	private String vAttribute8;
	private String vAttributevalue8;
	private String vAttributeValueType8;
	private String vAttribute9;
	private String vAttributevalue9;
	private String vAttributeValueType9;
	private String vAttribute10;
	private String vAttributevalue10;
	private String vAttributeValueType10;
	private String vAttribute11;
	private String vAttributevalue11;
	private String vAttributeValueType11;
	private String vAttribute12;
	private String vAttributevalue12;
	private String vAttributeValueType12;
	private String vAttribute13;
	private String vAttributevalue13;
	private String vAttributeValueType13;
	private String vAttribute14;
	private String vAttributevalue14;
	private String vAttributeValueType14;
	private String vCatalogID;
	private String vStoreId;
	private boolean vIsAttibute;
	private boolean vDisplycontent;
	private int vPagesize;
	private int vBeginIndex;
	private String vCountry;
	private String vState;
	private String vCity;
	private String vSoldTo;
	private String vShipto;
	private String vHoldDetails;
	private boolean vItemNotExist = false;
	private String vNonExistingItem="";
	private String vFromPage = "";
	Map itemDetails = new HashMap<String, List>();	
	
	
	
	private static final String M_SPLATTRIBFILTER_PAGE = "DomtarStockCheckAttributeFilterListPageDisplay";
	private static final String M_SPLATTRIBFILTER_CATALOG_PAGE= "DomtarProductCatalogListPageDisplay";
	public void performExecute() throws ECException {
		long vEntryPoint = System.currentTimeMillis();
		super.performExecute();
		String strMethodName ="performExecute";
		TypedProperty rspProp = new TypedProperty();
		DomtarStockCheckInputDataBean inputBean = null;			
		loadParameters(getRequestProperties());
		List<CatalogEntryDataBean> vResultLists = new ArrayList<CatalogEntryDataBean>();
		vResultLists = loadResults();
		rspProp.put("totalRecordCount", vResultLists.size());
		String vJsonObj = genereateJsonObj(vResultLists);
		rspProp.put("jsonObj",vJsonObj);
		rspProp.put("resultList", correctForPageInation(vResultLists));
		rspProp.put("itemNotExist",vItemNotExist);
		rspProp.put("nonExistingItem", vNonExistingItem);
		if("STOCKCHECK".equals(getRequestProperties().getString("fromPage", ""))){
			rspProp.put(ECConstants.EC_VIEWTASKNAME, M_SPLATTRIBFILTER_PAGE);
		}else{
			rspProp.put(ECConstants.EC_VIEWTASKNAME, M_SPLATTRIBFILTER_CATALOG_PAGE);
		}
		loadResponseParameters(rspProp);
		setResponseProperties(rspProp);
		long vExitPoint = System.currentTimeMillis();
		long TotCallDiff = (vExitPoint-vEntryPoint);
		System.out.println("AFTER MQT");
		System.out.println("Time for total loading filtered products is >>>>>>"+TotCallDiff+" ms");
	}
	private String genereateJsonObj(List<CatalogEntryDataBean> resultLists) {
		StringBuffer vJsonString = new StringBuffer(1000);
		try{
		vJsonString.append("{");
		int resultCount = resultLists.size();
		int loopCount = 0 ; 
		for(CatalogEntryDataBean vBean : resultLists){
			String vPartNumber = vBean.getItemDataBean().getPartNumber();
			vPartNumber = vPartNumber.toUpperCase();
			String vAttribute = "";
			String vAttributeValue  = "";
			boolean isFeatures = false;
			boolean isCommonUse = false;
			boolean isCertificates = false;
			boolean isGuarentee = false;
			vJsonString.append("\"").append(vPartNumber).append("\":{");
			vJsonString.append("\"Attribute-Details\":{");
			AttributeValueDataBean[] vAttrBeanArr = vBean.getItemDataBean().getAttributeValueDataBeans();
			if(vAttrBeanArr.length != 0){
				int attribCount = 0;
				for (AttributeValueDataBean vAttrBean : vAttrBeanArr) {	
					vAttribute = vAttrBean.getAttribute().getName().trim();
					if(vAttribute.equals("Features")){
						isFeatures = true;
					}else if(vAttribute.equals("CommonUse")){
						 isCommonUse = true;
					}else if(vAttribute.equals("Certificates")){
						isCertificates = true;
					}else if(vAttribute.equals("Guarantee")){
						isGuarentee = true;
					}
					vAttributeValue = vAttrBean.getName().trim();
					vJsonString.append("\"").append(vAttribute).append("\":{\"").append(vAttributeValue).append("\":\"")
					.append(vAttributeValue).append("|:-:|").append(vPartNumber).append("\"}");
					attribCount++;
					if(attribCount < vAttrBeanArr.length){
						vJsonString.append(",");
					}
				}
			}			
			vJsonString.append("}");
						
			String ParentCatentryId = vBean.getParentCatalogEntryIds()[0];
			CatalogEntryDataBean catEntDb = new CatalogEntryDataBean();
			catEntDb.setCatalogEntryID(ParentCatentryId);
			catEntDb.refreshCopyHelper();
			CatEntrySearchListDataBean parentCatentrySearchBean = new CatEntrySearchListDataBean();
			parentCatentrySearchBean.setIsProduct(true);
			parentCatentrySearchBean.setCatalogId(vCatalogID);
			parentCatentrySearchBean.setSku(catEntDb.getPartNumber());
			parentCatentrySearchBean.setSkuOperator(parentCatentrySearchBean.OPERATOR_EQUAL);
			DataBeanManager.activate(parentCatentrySearchBean);	
			CatalogEntryDataBean[] catalogEntityBeans = (CatalogEntryDataBean[]) parentCatentrySearchBean
			.getResultList();	
			if(catalogEntityBeans != null && catalogEntityBeans.length > 0){
				vJsonString.append(",\"ProductDetails\":{");
				for(CatalogEntryDataBean vProductBean : catalogEntityBeans){
					for(AttachmentDataBean attchmentBean :vProductBean.getAllAttachments()){
						if(attchmentBean.getFileName().startsWith("PRODUCTDETAILS")){
							vJsonString.append("\"").append(attchmentBean.getObjectPath()).append(attchmentBean.getPath()).append("\":\"").append(vPartNumber).append("\"");	
							break;
						}
					}
				}
				vJsonString.append("}");
				
			}else{
				vJsonString.append(",\"ProductDetails\":{\"").append("\":\"").append(vPartNumber).append("\"}");			
				vJsonString.append("}");
			}
			/*
			 * Get the associated tabbed content such as Features, CommonUse, Certifications and
			 * Guarantee for an item from the Product.
			 */
			if(catalogEntityBeans != null && catalogEntityBeans.length > 0){
				CatalogEntryDataBean vProductBean = catalogEntityBeans[0];
				if("PRODUCTCATALOG".equals(vFromPage) && (isFeatures || isCommonUse || isCertificates || isGuarentee) ){
					int validAttachmentLength =0;
					for(AttachmentDataBean atBean :vProductBean.getAllAttachments()){
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
					if(validAttachmentLength > 0){
						vJsonString.append(",\"AttachmentDetails\":{");
					}
					boolean checkForShowingComma = false;
					for(AttachmentDataBean atBean :vProductBean.getAllAttachments()){
						
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
					if(validAttachmentLength > 0){
						vJsonString.append("}");
					}				
				}
			}
			vJsonString.append(",\"Name\":{\"").append(vBean.getItemDataBean().getDescription().getName().replaceAll("\"", "\\\\\"")).append("\":\"").append(vBean.getItemDataBean().getDescription().getName().replaceAll("\"", "\\\\\"")).append("|:-:|").append(vPartNumber).append("\"}");		
			if("PRODUCTCATALOG".equals(vFromPage) ){
				vJsonString.append(",\"ThumbNail\":{\"").append(vBean.getItemDataBean().getDescription().getThumbNail()).append("\":\"").append(vPartNumber).append("\"}");			
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
			loopCount++; 
			if(loopCount < resultCount){
				vJsonString.append("},");
			}else{
				vJsonString.append("}");
			}
			
			
			
		}
		vJsonString.append("}");
	
		
		} catch (Exception e) {
				ExceptionHandler.convertToECException(e);
			}
		//System.out.println("vJsonString.toString()>"+vJsonString.toString());
		return vJsonString.toString();
	}
	private String escapeAllHTML(String longDescription) {
		if (longDescription == null){
			return null;
		}
		String outPut ="";
		outPut = longDescription.replaceAll("\"", "\\\\\"");
		outPut = outPut.replaceAll("\n", "");
		outPut = outPut.replaceAll("\\/>", ">");
		outPut = outPut.replaceAll("  >", ">");
		outPut = outPut.replaceAll(" >", ">");
		return outPut;
	}
	private void loadResponseParameters(TypedProperty rspProp) {
		  rspProp.put("langId", vLangId);
		  if (vAttribute1 != null) {
			rspProp.put("attribute1", vAttribute1);
			rspProp.put("attributevalue1", vAttributevalue1);
			rspProp.put("attributeValueType1", vAttributeValueType1);
		}
		if (vAttribute2 != null) {
			rspProp.put("attribute2", vAttribute2);
			rspProp.put("attributevalue2", vAttributevalue2);
			rspProp.put("attributeValueType2", vAttributeValueType2);
		}
		if (vAttribute3 != null) {
			rspProp.put("attribute3", vAttribute3);
			rspProp.put("attributevalue3", vAttributevalue3);
			rspProp.put("attributeValueType3", vAttributeValueType3);
		}
		if (vAttribute4 != null) {
			rspProp.put("attribute4", vAttribute4);
			rspProp.put("attributevalue4", vAttributevalue4);
			rspProp.put("attributeValueType4", vAttributeValueType4);
		}
		if (vAttribute5 != null) {
			rspProp.put("attribute5", vAttribute5);
			rspProp.put("attributevalue5", vAttributevalue5);
			rspProp.put("attributeValueType5", vAttributeValueType5);
		}
		if (vAttribute6 != null) {
			rspProp.put("attribute6", vAttribute6);
			rspProp.put("attributevalue6", vAttributevalue6);
			rspProp.put("attributeValueType6", vAttributeValueType6);
		}
		if (vAttribute7 != null) {
			rspProp.put("attribute7", vAttribute7);
			rspProp.put("attributevalue7", vAttributevalue7);
			rspProp.put("attributeValueType7", vAttributeValueType7);
		}
		if (vAttribute8 != null) {
			rspProp.put("attribute8", vAttribute8);
			rspProp.put("attributevalue8", vAttributevalue8);
			rspProp.put("attributeValueType8", vAttributeValueType8);
		}
		if (vAttribute9 != null) {
			rspProp.put("attribute9", vAttribute9);
			rspProp.put("attributevalue9", vAttributevalue9);
			rspProp.put("attributeValueType9", vAttributeValueType9);
		}
		if (vAttribute10 != null) {
			rspProp.put("attribute10", vAttribute10);
			rspProp.put("attributevalue10", vAttributevalue10);
			rspProp.put("attributeValueType10", vAttributeValueType10);
		}
		if (vAttribute11 != null) {
			rspProp.put("attribute11", vAttribute11);
			rspProp.put("attributevalue11", vAttributevalue11);
			rspProp.put("attributeValueType11", vAttributeValueType11);
		}
		if (vAttribute12 != null) {
			rspProp.put("attribute12", vAttribute12);
			rspProp.put("attributevalue12", vAttributevalue12);
			rspProp.put("attributeValueType12", vAttributeValueType12);
		}
		if (vAttribute13 != null) {
			rspProp.put("attribute13", vAttribute13);
			rspProp.put("attributevalue13", vAttributevalue13);
			rspProp.put("attributeValueType13", vAttributeValueType13);
		}
		if (vAttribute14 != null) {
			rspProp.put("attribute14", vAttribute14);
			rspProp.put("attributevalue14", vAttributevalue14);
			rspProp.put("attributeValueType14", vAttributeValueType14);
		}
		  
		  rspProp.put("catalogID", vCatalogID);
		  rspProp.put("storeId", vStoreId);
		  rspProp.put("isAttibute", vIsAttibute);
		  rspProp.put("displycontent", vDisplycontent);
		  rspProp.put("pagesize", vPagesize);
		  rspProp.put("beginIndex", vBeginIndex);
		  if(vCountry!=null){
			  rspProp.put("country", vCountry);
		  }
		  if(vState!=null){
			  rspProp.put("state", vState); 
		  }
		  if(vCity!=null){
			  rspProp.put("city", vCity); 
		  }
		  if(vSoldTo!=null){
			  rspProp.put("soldTo", vSoldTo);
		  }
		  if(vSoldTo!=null){
			  rspProp.put("shipto", vShipto);
		  }
		  if(vSoldTo!=null){
			  rspProp.put("holdDetails", vHoldDetails);
		  }
		 
		
	}
	private Object correctForPageInation(List<CatalogEntryDataBean> pResultLists) {		
			List<CatalogEntryDataBean> vItemSearchLists = new ArrayList<CatalogEntryDataBean>();
			for(int i = 0 ; i < pResultLists.size() ; i++){
				if(i >= vBeginIndex && i <(vBeginIndex+vPagesize) ){
					vItemSearchLists.add(pResultLists.get(i));
				}
			}
			return vItemSearchLists;
	}
	
	
	/**
	 * Description : This method will get the "itemDetails" map from cache and check for the 
	 * selected attributes and return the list of CatalogEntryDataBeans.
	 * @return
	 * @throws ECException
	 */
	private List<CatalogEntryDataBean> loadResults() throws ECException {
		
		List<CatalogEntryDataBean> vResultList = new ArrayList<CatalogEntryDataBean>();		
		
		//Invoke DomtarFetchFilterDetailsCmd to get the "itemDetails" map. The "itemDetails" will get from cache.
		DomtarFetchFilterDetailsCmd fetchFilterDetailsCmd = new DomtarFetchFilterDetailsCmdImpl();
		fetchFilterDetailsCmd.setStoreId(vStoreId);
		fetchFilterDetailsCmd.setCatalogId(vCatalogID);
		fetchFilterDetailsCmd.setLanguageId(vLangId);
		fetchFilterDetailsCmd.setTradingAgreements(getCommandContext().getCurrentTradingAgreementIdsAsString());
		
		try {
			fetchFilterDetailsCmd.execute();
			 itemDetails = fetchFilterDetailsCmd.getItemDetails();
		} catch (Exception e) {
			ExceptionHandler.convertToECException(e);
		} 	
				
		String catentry_Id = "";
		Iterator iter = null;
		if (itemDetails != null && itemDetails.size() > 0)
			iter = itemDetails.keySet().iterator(); 
		
		while(iter != null && iter.hasNext()) {
		
			catentry_Id = (String)iter.next();

			Map attributes = new HashMap<String, String>();		
			attributes = (Map)((List)itemDetails.get(catentry_Id)).get(6);//Get the attribute map for the catentryId.
			
			/**
			 * Check whether the selected attribute is present in hashmap, if exists then check for the next 
			 * attribute and so on. If the selected attribute is not present in the map then continue for 
			 * next catentryId and so on. Finally add the catentryId to a list.  
			 **/			
		 	if(vAttributevalue1 != null)   {
		 		String attributeValue = (String)attributes.get(vAttribute1);
		 		if(attributeValue == null || !attributeValue.equals(vAttributevalue1))
		 			continue;
			}
			if(vAttributevalue2 != null){
				String attributeValue = (String)attributes.get(vAttribute2);
		 		if(attributeValue == null || !attributeValue.equals(vAttributevalue2))
		 			continue;
			}
			if(vAttributevalue3 != null){
				String attributeValue = (String)attributes.get(vAttribute3);
		 		if(attributeValue == null || !attributeValue.equals(vAttributevalue3))
		 			continue;
			}
			if(vAttributevalue4 != null){
				String attributeValue = (String)attributes.get(vAttribute4);
		 		if(attributeValue == null || !attributeValue.equals(vAttributevalue4))
		 			continue;
			}
			if(vAttributevalue5 != null){
				String attributeValue = (String)attributes.get(vAttribute5);
		 		if(attributeValue == null || !attributeValue.equals(vAttributevalue5))
		 			continue;
			}
			if(vAttributevalue6 != null){
				String attributeValue = (String)attributes.get(vAttribute6);
		 		if(attributeValue == null || !attributeValue.equals(vAttributevalue6))
		 			continue;
			}
			if(vAttributevalue7 != null){
				String attributeValue = (String)attributes.get(vAttribute7);
		 		if(attributeValue == null || !attributeValue.equals(vAttributevalue7))
		 			continue;
			}
			if(vAttributevalue8 != null){
				String attributeValue = (String)attributes.get(vAttribute8);
		 		if(attributeValue == null || !attributeValue.equals(vAttributevalue8))
		 			continue;
			}
			if(vAttributevalue9 != null){
				String attributeValue = (String)attributes.get(vAttribute9);
		 		if(attributeValue == null || !attributeValue.equals(vAttributevalue9))
		 			continue;
			}
			if(vAttributevalue10 != null){
				String attributeValue = (String)attributes.get(vAttribute10);
		 		if(attributeValue == null || !attributeValue.equals(vAttributevalue10))
		 			continue;
			}
			if(vAttributevalue11 != null){
				String attributeValue = (String)attributes.get(vAttribute11);
		 		if(attributeValue == null || !attributeValue.equals(vAttributevalue11))
		 			continue;
			}
			if(vAttributevalue12 != null){
				String attributeValue = (String)attributes.get(vAttribute12);
		 		if(attributeValue == null || !attributeValue.equals(vAttributevalue12))
		 			continue;
			}	
			if(vAttributevalue13 != null){
				String attributeValue = (String)attributes.get(vAttribute13);
		 		if(attributeValue == null || !attributeValue.equals(vAttributevalue13))
		 			continue;
			}	
			if(vAttributevalue14 != null){
				String attributeValue = (String)attributes.get(vAttribute14);
		 		if(attributeValue == null || !attributeValue.equals(vAttributevalue14))
		 			continue;
			}	
		
			try {
				CatalogEntryDataBean catalogEntryDataBean = new CatalogEntryDataBean();
				catalogEntryDataBean.setInitKey_catalogEntryReferenceNumber(catentry_Id);
				DataBeanManager.activate(catalogEntryDataBean);
				vResultList.add(catalogEntryDataBean);
			} catch (Exception e){
				ExceptionHandler.convertToECException(e);
			}
		}
		
		if(vResultList.size() == 0){
			vItemNotExist = true;	
			vNonExistingItem = "Search with the selected attribute will not give any results";
		}		
		return vResultList;
	}
	private void loadParameters(TypedProperty requestProperties) {
		 vLangId = requestProperties.getString("langId", null);
		 vAttribute1= requestProperties.getString("attribute1", null);
		 vAttributevalue1= requestProperties.getString("attributevalue1", null);
		 vAttributeValueType1= requestProperties.getString("attributeValueType1", null);
		 vAttribute2= requestProperties.getString("attribute2", null);
		 vAttributevalue2= requestProperties.getString("attributevalue2", null);
		 vAttributeValueType2= requestProperties.getString("attributeValueType2", null);
		 vAttribute3= requestProperties.getString("attribute3", null);
		 vAttributevalue3= requestProperties.getString("attributevalue3", null);
		 vAttributeValueType3= requestProperties.getString("attributeValueType3", null);
		 vAttribute4= requestProperties.getString("attribute4", null);
		 vAttributevalue4= requestProperties.getString("attributevalue4", null);
		 vAttributeValueType4= requestProperties.getString("attributeValueType4", null);
		 vAttribute5= requestProperties.getString("attribute5", null);
		 vAttributevalue5= requestProperties.getString("attributevalue5", null);
		 vAttributeValueType5= requestProperties.getString("attributeValueType5", null);
		 vAttribute6= requestProperties.getString("attribute6", null);
		 vAttributevalue6= requestProperties.getString("attributevalue6", null);
		 vAttributeValueType6= requestProperties.getString("attributeValueType6", null);
		 vAttribute7= requestProperties.getString("attribute7", null);
		 vAttributevalue7= requestProperties.getString("attributevalue7", null);
		 vAttributeValueType7= requestProperties.getString("attributeValueType7", null);
		 vAttribute8= requestProperties.getString("attribute8", null);
		 vAttributevalue8= requestProperties.getString("attributevalue8", null);
		 vAttributeValueType8= requestProperties.getString("attributeValueType8", null);
		 vAttribute9= requestProperties.getString("attribute9", null);
		 vAttributevalue9= requestProperties.getString("attributevalue9", null);
		 vAttributeValueType9= requestProperties.getString("attributeValueType9", null);
		 vAttribute10= requestProperties.getString("attribute10", null);
		 vAttributevalue10= requestProperties.getString("attributevalue10", null);
		 vAttributeValueType10= requestProperties.getString("attributeValueType10", null);
		 vAttribute11= requestProperties.getString("attribute11", null);
		 vAttributevalue11= requestProperties.getString("attributevalue11", null);
		 vAttributeValueType11= requestProperties.getString("attributeValueType11", null);
		 vAttribute12= requestProperties.getString("attribute12", null);
		 vAttributevalue12= requestProperties.getString("attributevalue12", null);
		 vAttributeValueType12= requestProperties.getString("attributeValueType12", null);
		 vAttribute13= requestProperties.getString("attribute13", null);
		 vAttributevalue13= requestProperties.getString("attributevalue13", null);
		 vAttributeValueType13= requestProperties.getString("attributeValueType13", null);
		 vAttribute14= requestProperties.getString("attribute14", null);
		 vAttributevalue14= requestProperties.getString("attributevalue14", null);
		 vAttributeValueType14= requestProperties.getString("attributeValueType14", null);
		 vCatalogID= requestProperties.getString("catalogID", null);
		 vStoreId= requestProperties.getString("storeId", null);
		 vIsAttibute= requestProperties.getBoolean("isAttibute", false);
		 vDisplycontent= requestProperties.getBoolean("displycontent", false);
		 vPagesize= requestProperties.getIntValue("pagesize", 0);
		 vBeginIndex= requestProperties.getIntValue("beginIndex", 0);
		 vCountry= requestProperties.getString("country", null);
		 vState= requestProperties.getString("state", null);
		 vCity= requestProperties.getString("city", null);
		 vSoldTo= requestProperties.getString("soldTo", null);
		 vShipto= requestProperties.getString("shipto", "");
		 vHoldDetails= requestProperties.getString("holdDetails", "");
		 vFromPage = getRequestProperties().getString("fromPage", "PRODUCTCATALOG");
		
	}

}
