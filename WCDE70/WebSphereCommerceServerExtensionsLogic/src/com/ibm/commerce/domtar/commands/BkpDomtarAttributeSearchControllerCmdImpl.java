package com.ibm.commerce.domtar.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ibm.commerce.beans.DataBeanManager;
import com.ibm.commerce.catalog.beans.CatalogEntryDataBean;
import com.ibm.commerce.command.CommandFactory;
import com.ibm.commerce.command.ControllerCommandImpl;
import com.ibm.commerce.datatype.TypedProperty;
import com.ibm.commerce.domtar.databeans.DomtarItemSearchDetailsDataBean;
import com.ibm.commerce.domtar.databeans.DomtarStockCheckInputDataBean;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.search.beans.CatEntrySearchListDataBean;
import com.ibm.commerce.server.ECConstants;
import com.ibm.disthub2.impl.util.ArrayUtil;

public class BkpDomtarAttributeSearchControllerCmdImpl extends
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
	//FOR SORT
	private String vSortWith = "";
	
	
	
	
	private static final String M_SPLATTRIBFILTER_PAGE = "DomtarStockCheckAttributeFilterListPageDisplay";
	private static final String M_SPLATTRIBFILTER_CATALOG_PAGE= "DomtarProductCatalogListPageDisplay";
	public void performExecute() throws ECException {
		super.performExecute();
		String strMethodName ="performExecute";
		TypedProperty rspProp = new TypedProperty();
		DomtarStockCheckInputDataBean inputBean = null;			
		loadParameters(getRequestProperties());
		List<CatalogEntryDataBean> vResultLists = new ArrayList<CatalogEntryDataBean>();
		vResultLists = loadResults();
		rspProp.put("totalRecordCount", vResultLists.size());
		
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
	private List<CatalogEntryDataBean> loadResults() throws ECException {
		
		List<CatalogEntryDataBean> vResultList = new ArrayList<CatalogEntryDataBean>();		
		CatEntrySearchListDataBean initalCatEntrySearchDB = new CatEntrySearchListDataBean();
		initalCatEntrySearchDB.setPageSize("100000");
		initalCatEntrySearchDB.setResultType("1");
		initalCatEntrySearchDB.setCatalogId(vCatalogID);
		initalCatEntrySearchDB.setIsItem(true);
		if(vAttributevalue1 != null){
			initalCatEntrySearchDB.setAttributeValue1(vAttributevalue1.trim());
			initalCatEntrySearchDB.setAttributeName1(vAttribute1);
			initalCatEntrySearchDB.setAttributeValueType1(vAttributeValueType1);
			initalCatEntrySearchDB.setAttributeValueOperator1(initalCatEntrySearchDB.OPERATOR_EQUAL);
		}
		if(vAttributevalue2 != null){
			initalCatEntrySearchDB.setAttributeValue2(vAttributevalue2.trim());
			initalCatEntrySearchDB.setAttributeName2(vAttribute2);
			initalCatEntrySearchDB.setAttributeValueType2(vAttributeValueType2);
			initalCatEntrySearchDB.setAttributeValueOperator2(initalCatEntrySearchDB.OPERATOR_EQUAL);
		}
		if(vAttributevalue3 != null){
			initalCatEntrySearchDB.setAttributeValue3(vAttributevalue3.trim());
			initalCatEntrySearchDB.setAttributeName3(vAttribute3);
			initalCatEntrySearchDB.setAttributeValueType3(vAttributeValueType3);
			initalCatEntrySearchDB.setAttributeValueOperator3(initalCatEntrySearchDB.OPERATOR_EQUAL);
		}
		if(vAttributevalue4 != null){
			initalCatEntrySearchDB.setAttributeValue4(vAttributevalue4.trim());
			initalCatEntrySearchDB.setAttributeName4(vAttribute4);
			initalCatEntrySearchDB.setAttributeValueType4(vAttributeValueType4);
			initalCatEntrySearchDB.setAttributeValueOperator4(initalCatEntrySearchDB.OPERATOR_EQUAL);
		}
		if(vAttributevalue5 != null){
			initalCatEntrySearchDB.setAttributeValue5(vAttributevalue5.trim());
			initalCatEntrySearchDB.setAttributeName5(vAttribute5);
			initalCatEntrySearchDB.setAttributeValueType5(vAttributeValueType5);
			initalCatEntrySearchDB.setAttributeValueOperator5(initalCatEntrySearchDB.OPERATOR_EQUAL);
		}
		if(vAttributevalue6 != null){
			initalCatEntrySearchDB.setAttributeValue6(vAttributevalue6.trim());
			initalCatEntrySearchDB.setAttributeName6(vAttribute6);
			initalCatEntrySearchDB.setAttributeValueType6(vAttributeValueType6);
			initalCatEntrySearchDB.setAttributeValueOperator6(initalCatEntrySearchDB.OPERATOR_EQUAL);
		}
		if(vAttributevalue7 != null){
			initalCatEntrySearchDB.setAttributeValue7(vAttributevalue7.trim());
			initalCatEntrySearchDB.setAttributeName7(vAttribute7);
			initalCatEntrySearchDB.setAttributeValueType7(vAttributeValueType7.trim());
			initalCatEntrySearchDB.setAttributeValueOperator7(initalCatEntrySearchDB.OPERATOR_EQUAL);
		}
		if(vAttributevalue8 != null){
			initalCatEntrySearchDB.setAttributeValue8(vAttributevalue8.trim());
			initalCatEntrySearchDB.setAttributeName8(vAttribute8);
			initalCatEntrySearchDB.setAttributeValueType8(vAttributeValueType8);
			initalCatEntrySearchDB.setAttributeValueOperator8(initalCatEntrySearchDB.OPERATOR_EQUAL);
		}
		if(vAttributevalue9 != null){
			initalCatEntrySearchDB.setAttributeValue9(vAttributevalue9.trim());
			initalCatEntrySearchDB.setAttributeName9(vAttribute9);
			initalCatEntrySearchDB.setAttributeValueType9(vAttributeValueType9);
			initalCatEntrySearchDB.setAttributeValueOperator9(initalCatEntrySearchDB.OPERATOR_EQUAL);
		}
		if(vAttributevalue10 != null){
			initalCatEntrySearchDB.setAttributeValue10(vAttributevalue10.trim());
			initalCatEntrySearchDB.setAttributeName10(vAttribute10);
			initalCatEntrySearchDB.setAttributeValueType10(vAttributeValueType10);
			initalCatEntrySearchDB.setAttributeValueOperator10(initalCatEntrySearchDB.OPERATOR_EQUAL);
		}
		//To be added
		//advancedCatEntrySearchDB.setField1("1");
		DataBeanManager.activate(initalCatEntrySearchDB);			
		CatalogEntryDataBean[] catalogEntityBeans = (CatalogEntryDataBean[]) initalCatEntrySearchDB
				.getResultList();	
		if(catalogEntityBeans!=null && catalogEntityBeans.length > 0){
			if(!vIsAttibute){
				CatEntrySearchListDataBean indVidualCheck = null;
				for(CatalogEntryDataBean catBean : catalogEntityBeans){
					indVidualCheck = new CatEntrySearchListDataBean();
					indVidualCheck.setPageSize("1");
					indVidualCheck.setResultType("1");
					indVidualCheck.setCatalogId(vCatalogID);
					indVidualCheck.setIsItem(true);
					indVidualCheck.setSku(catBean.getItemDataBean().getPartNumber());
					indVidualCheck.setSkuOperator(indVidualCheck.OPERATOR_EQUAL);
					if(vAttributevalue11 != null){
						indVidualCheck.setAttributeValue1(vAttributevalue11.trim());
						indVidualCheck.setAttributeName1(vAttribute11);
						indVidualCheck.setAttributeValueType1(vAttributeValueType11);
						indVidualCheck.setAttributeValueOperator1(initalCatEntrySearchDB.OPERATOR_EQUAL);
					}
					if(vAttributevalue12 != null){
						indVidualCheck.setAttributeValue2(vAttributevalue12.trim());
						indVidualCheck.setAttributeName2(vAttribute12);
						indVidualCheck.setAttributeValueType2(vAttributeValueType12);
						indVidualCheck.setAttributeValueOperator2(initalCatEntrySearchDB.OPERATOR_EQUAL);
					}
					if(vAttributevalue13 != null){
						indVidualCheck.setAttributeValue3(vAttributevalue13.trim());
						indVidualCheck.setAttributeName3(vAttribute13);
						indVidualCheck.setAttributeValueType3(vAttributeValueType13);
						indVidualCheck.setAttributeValueOperator3(initalCatEntrySearchDB.OPERATOR_EQUAL);
					}
					if(vAttributevalue14 != null){
						indVidualCheck.setAttributeValue4(vAttributevalue14.trim());
						indVidualCheck.setAttributeName4(vAttribute14);
						indVidualCheck.setAttributeValueType4(vAttributeValueType14);
						indVidualCheck.setAttributeValueOperator4(initalCatEntrySearchDB.OPERATOR_EQUAL);
					}
					DataBeanManager.activate(indVidualCheck);			
					CatalogEntryDataBean[] newCatentBean = (CatalogEntryDataBean[]) indVidualCheck
							.getResultList();	
					if(newCatentBean!=null && newCatentBean.length > 0){
						vResultList.add(catalogEntityBeans[0]);
					}
				}
			}else{
				
				vResultList = Arrays.asList(catalogEntityBeans);
				
			}
		}else{
			vItemNotExist = true;	
			vNonExistingItem = "Search with the selected attribute will not give any results";
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
		 vSortWith = requestProperties.getString("sorthWith", "");
	}

}
