package com.ibm.commerce.domtar.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.json.JSON;
import org.apache.commons.json.JSONArtifact;

import com.ibm.commerce.beans.DataBeanManager;
import com.ibm.commerce.catalog.beans.AttributeValueDataBean;
import com.ibm.commerce.catalog.beans.CatalogEntryDataBean;
import com.ibm.commerce.datatype.TypedProperty;
import com.ibm.commerce.domtar.util.DomtarJDBCHelper;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.exception.ExceptionHandler;
import com.ibm.commerce.search.beans.CatEntrySearchListDataBean;
import com.ibm.websphere.command.CacheableCommandImpl;

public class DomtarFetchFilterDetailsCmdImpl extends CacheableCommandImpl
		implements DomtarFetchFilterDetailsCmd {

	
	private static final String M_BRAND = "Brand";
	private static final String M_BASICTYPE = "BasisType";
	private static final String M_BRIGHTNESS = "Brightness";
	private static final String M_PACKTYPE = "PackType";
	private static final String M_SIZE = "Size";
	private static final String M_FINISH = "Finish";
	private static final String M_OPACITY = "Opacity";
	private static final String M_CERTIFICATIONS = "Certifications";
	private static final String M_RECYCLED = "Recycled";
	private static final String M_COLOR = "Color";
	private static final String M_SMOOTHNESS = "Smoothness";
	private static final String M_BASICWEIGHT = "BasisWeight";
	private JSONArtifact jsonFilterArtifact;
	private String storeId;
	private String catalogId;
	private String languageId;
	private String tradingAgreements;
	Map itemDetails = new HashMap<String, List>();
	Map attributeType = new HashMap<String, String>();
	
	@Override
	public String getTradingAgreements() {
		return tradingAgreements;
	}

	@Override
	public void setTradingAgreements(String tradingAgreements) {
		this.tradingAgreements = tradingAgreements;		
	}	

	/**
	 * @return the jsonFilterArtifact
	 */
	public JSONArtifact getJSONFilterArtifact() {
		return jsonFilterArtifact;
	}

	/**
	 * @param jsonFilterArtifact the jsonFilterArtifact to set
	 */
	public void setJSONFilterArtifact(JSONArtifact jsonFilterArtifact) {
		this.jsonFilterArtifact = jsonFilterArtifact;
	}

	/**
	 * @return the storeId
	 */
	public String getStoreId() {
		return storeId;
	}

	public Map getItemDetails() {
		return itemDetails;
	}

	public void setItemDetails(Map itemDetails) {
		this.itemDetails = itemDetails;
	}

	/**
	 * @param storeId the storeId to set
	 */
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	/**
	 * @return the catalogId
	 */
	public String getCatalogId() {
		return catalogId;
	}

	/**
	 * @param catalogId the catalogId to set
	 */
	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
	}

	/**
	 * @return the languageId
	 */
	public String getLanguageId() {
		return languageId;
	}

	/**
	 * @param languageId the languageId to set
	 */
	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}

	@Override
	public boolean isReadyToCallExecute() {
		if(getTradingAgreements() != null) {
			return true;
		} else {
			return false;
		}
			
	}

	@Override
	public void performExecute() throws Exception {
		
		attributeType.put("Brand", "STRING");                 
		attributeType.put("BasisType", "STRING");
		attributeType.put("Brightness", "STRING");
		attributeType.put("PackType", "STRING");
		attributeType.put("Size", "STRING");
		attributeType.put("Finish", "STRING");
		attributeType.put("Opacity", "STRING");
		attributeType.put("Certifications", "STRING");                 
		attributeType.put("Recycled", "STRING");
		attributeType.put("Color", "STRING");
		attributeType.put("Smoothness", "STRING");
		attributeType.put("BasisWeight", "STRING");
		
		long vEntryPoint = System.currentTimeMillis();		
		long vBeforeJsonCall = System.currentTimeMillis();
		String vJsonObj = genereateJsonObj(getStoreId(),getLanguageId(),getCatalogId());
		long vAfterJsonCall = System.currentTimeMillis();
		long JsonCallDiff = (vAfterJsonCall-vBeforeJsonCall);
		try {
			if(!vJsonObj.equals("")){				
				setJSONFilterArtifact(JSON.parse(vJsonObj));
			}
		} catch (Exception e) {
			ExceptionHandler.convertToECException(e);
		} 		
		long vExitPoint = System.currentTimeMillis();
		long TotCallDiff = (vExitPoint-vEntryPoint);
		System.out.println("Time for total Call is >>>>>>"+TotCallDiff+" ms");

	}
	
	private String genereateJsonObj(String pStoreId, String pLanguageId,
			String pCatalogId) throws ECException {
		long vfetchpoint = System.currentTimeMillis();
	
	CatEntrySearchListDataBean catEntrySearchDB = new CatEntrySearchListDataBean();
		String vJsonObjTOReturn = "";
		try {		
			TypedProperty rqProp = new TypedProperty();
			// rqProp.put("resultType","1");
			catEntrySearchDB.setPageSize("1000000");
			catEntrySearchDB.setResultType("1");
			catEntrySearchDB.setCatalogId(pCatalogId);
			catEntrySearchDB.setIsItem(true);
			
			//To be added
			//advancedCatEntrySearchDB.setField1("1");
			DataBeanManager.activate(catEntrySearchDB);			
			CatalogEntryDataBean[] catalogEntityBeans = (CatalogEntryDataBean[]) catEntrySearchDB
					.getResultList();	
			if(catalogEntityBeans == null){				
				return vJsonObjTOReturn.toString();
			}
			long vfetchpointend = System.currentTimeMillis();
			System.out.println("Time for fetch list is >>>>>>"+(vfetchpointend - vfetchpoint)+" ms");
			long vloadMapStart = System.currentTimeMillis();
			long vIniMapStart = System.currentTimeMillis();
			HashMap vAvailableAttribute = new HashMap();		
			vAvailableAttribute.put(M_BRAND, true);
			vAvailableAttribute.put(M_BASICTYPE, true);
			vAvailableAttribute.put(M_BASICWEIGHT, true);
			vAvailableAttribute.put(M_BRIGHTNESS, true);
			vAvailableAttribute.put(M_PACKTYPE, true);
			vAvailableAttribute.put(M_SIZE, true);
			vAvailableAttribute.put(M_FINISH, true);
			vAvailableAttribute.put(M_OPACITY, true);
			vAvailableAttribute.put(M_CERTIFICATIONS, true);
			vAvailableAttribute.put(M_RECYCLED, true);
			vAvailableAttribute.put(M_COLOR, true);
			vAvailableAttribute.put(M_SMOOTHNESS, true);
			HashMap vBrand = new HashMap();
			HashMap vBasictype = new HashMap();
			HashMap vBrightness = new HashMap();
			HashMap vPacktype = new HashMap();
			HashMap vSize = new HashMap();
			HashMap vFinish = new HashMap();
			HashMap vOpacity = new HashMap();
			HashMap vCertifications = new HashMap();
			HashMap vRecycled = new HashMap();
			HashMap vColor = new HashMap();
			HashMap vSmoothness = new HashMap();
			HashMap vBasisweight = new HashMap();
			StringBuffer vBrandTremString =  new StringBuffer();
			StringBuffer vBasictypeTremString =  new StringBuffer();
			StringBuffer vBrightnessTremString =  new StringBuffer();
			StringBuffer vPacktypeTremString =  new StringBuffer();
			StringBuffer vSizeTremString =  new StringBuffer();
			StringBuffer vFinishTremString =  new StringBuffer();
			StringBuffer vOpacityTremString =  new StringBuffer();
			StringBuffer vCertificationsTremString =  new StringBuffer();
			StringBuffer vRecycledTremString =  new StringBuffer();
			StringBuffer vColorTremString =  new StringBuffer();
			StringBuffer vSmoothnessTremString =  new StringBuffer();
			StringBuffer vWeightTremString =  new StringBuffer();
			StringBuffer vCaliperTremString =  new StringBuffer();
			StringBuffer vBasisweightTremString =  new StringBuffer();
			StringBuffer vBrandTypeString  =  new StringBuffer(0);
			StringBuffer vBasictypeTypeString  =  new StringBuffer(0);
			StringBuffer vBrightnessTypeString  =  new StringBuffer(0);
			StringBuffer vPacktypeTypeString  =  new StringBuffer(0);
			StringBuffer vSizeTypeString  =  new StringBuffer(0);
			StringBuffer vFinishTypeString  =  new StringBuffer(0);
			StringBuffer vOpacityTypeString  =  new StringBuffer(0);
			StringBuffer vCertificationsTypeString  =  new StringBuffer(0);
			StringBuffer vRecycledTypeString  =  new StringBuffer(0);
			StringBuffer vColorTypeString  =  new StringBuffer(0);
			StringBuffer vSmoothnessTypeString  =  new StringBuffer();
			StringBuffer vBasisweightTypeString =  new StringBuffer();
			long vIniMapEnd = System.currentTimeMillis();
			System.out.println("Time for initaiante map is >>>>>>"+(vIniMapEnd - vIniMapStart)+" ms");
			String vAttribute = "";
			String vAttributeValue  = "";
			String vPartNumber = "";
			String vItemId  = "";
			String vAttributeType  = "";
			AttributeValueDataBean[] vAttrBeanArr = null;			
			for (CatalogEntryDataBean vBean : catalogEntityBeans) {
				
			//	long vIt4MapoutStart = System.currentTimeMillis();
				//vAttrBeanArr = vBean.getItemDataBean().getDefiningAttributeValueDataBeans();
				//long vIinitArrEnd = System.currentTimeMillis();
			//	System.out.println("Time for initialising attribute array is >>>>>>"+(vIinitArrEnd - vIt4MapoutStart)+" ms");
				
				//Get the attribute/values from xprodsummary table for the catentryId
				List attributeValues = new ArrayList();
				DomtarJDBCHelper domtarOrderJDBC = new DomtarJDBCHelper(); 
				attributeValues = domtarOrderJDBC.findAttributeByCatentry(vBean.getItemDataBean().getItemID());
					
				
				
				if(attributeValues.size() == 0){					
					continue;
				}
				vPartNumber = vBean.getItemDataBean().getPartNumber();
				vItemId = vBean.getItemDataBean().getItemID();
				long vIt4MapStart = System.currentTimeMillis();
				//for (AttributeValueDataBean vAttrBean : vAttrBeanArr) {
				
				Map attributes = new HashMap<String, String>();
								
				attributes = (Map)attributeValues.get(6);//Sixth element in the arraylist contains a map with attributenames as key.
				itemDetails.put(vBean.getItemDataBean().getItemID(),attributeValues); //Put the arraylist into "itemDetails" map with catentryId as key.
								
				Iterator iter = null;
				if (attributes != null && attributes.size() > 0)
				 iter = attributes.keySet().iterator(); 
				
				while(iter != null && iter.hasNext()) { 			 
					 vAttribute = (String)iter.next(); 
				     vAttributeValue =  (String)attributes.get(vAttribute); 
				     //vAttributeValue = vAttributeValue.replaceAll("\"", "\\\\\"");
					 
					 if(!vAvailableAttribute.containsKey(vAttribute)){
						 continue;
					 }
					 /*
					  * Workaround:
					  * When any of the attribute contains a double quotes,the attribute dropdown
					  * never populates. Workaround is to remove those attributes.
					  * TO-DO: Remove the below code to handle the double quotes.
					  */
					 if (vAttributeValue.indexOf("\"")!=-1){
						 continue;
					 }
					 					 
					 if (M_BRAND.equals(vAttribute)) {
						 if (vBrand.containsKey(vAttributeValue)) {
							//vBrandTremString.setLength(10000);							
							vBrandTremString.append(vBrand.get(vAttributeValue));
							vBrandTremString.append(",\"").append(vItemId).append(
											"\":\"").append(vPartNumber).append("\"");
						 } else {
							 vBrandTremString.append("\"").append(vItemId)
							 .append("\":\"").append(vPartNumber)
							 .append("\"");
						 }
						 vBrand.put(vAttributeValue, vBrandTremString.toString());
						vBrandTremString.setLength(0);
						if (vBrandTypeString.length() == 0) {		
							vAttributeType = (String) attributeType.get(M_BRAND);
							vBrandTypeString.append("\"").append(M_BRAND)
									.append("\":{\"").append(vAttributeType)
									.append("\":\"true\"}");
						}
					} else if (M_BASICTYPE.equals(vAttribute)) {
						if (vBasictype.containsKey(vAttributeValue)) {
							//vBasictypeTremString.setLength(10000);
							vBasictypeTremString.append(vBasictype.get(vAttributeValue));
							vBasictypeTremString.append(
									",\"").append(vItemId).append("\":\"")
									.append(vPartNumber).append("\"");
						} else {
							vBasictypeTremString.append("\"").append(vItemId)
									.append("\":\"").append(vPartNumber)
									.append("\"");
						}
						vBasictype.put(vAttributeValue, vBasictypeTremString.toString());
						vBasictypeTremString.setLength(0);
						if (vBasictypeTypeString.length() == 0) {	
							vAttributeType = (String) attributeType.get(M_BASICTYPE);
							vBasictypeTypeString.append("\"").append(
									M_BASICTYPE).append("\":{\"").append(
									vAttributeType).append("\":\"true\"}");

						}
					} 
					else if (M_BASICWEIGHT.equals(vAttribute)) {
						if (vBasisweight.containsKey(vAttributeValue)) {
						
							vBasisweightTremString.append(vBasisweight.get(vAttributeValue));
							vBasisweightTremString.append(",\"").append(vItemId).append("\":\"").append(vPartNumber).append("\"");
						} else {
							vBasisweightTremString.append("\"").append(vItemId).append("\":\"").append(vPartNumber).append("\"");
						}
						vBasisweight.put(vAttributeValue, vBasisweightTremString.toString());
						vBasisweightTremString.setLength(0);
						if (vBasisweightTypeString.length() == 0) {	
						vAttributeType =  (String) attributeType.get(M_BASICWEIGHT);
						vBasisweightTypeString.append("\"").append(M_BASICWEIGHT).append("\":{\"").append(vAttributeType).append("\":\"true\"}");
						}
					}
					else if (M_BRIGHTNESS.equals(vAttribute)) {
						if (vBrightness.containsKey(vAttributeValue)) {
							//vBrightnessTremString.setLength(10000);
							vBrightnessTremString.append( vBrightness.get(vAttributeValue));
							vBrightnessTremString.append(
									",\"").append(vItemId).append("\":\"")
									.append(vPartNumber).append("\"");
						} else {
							vBrightnessTremString.append("\"").append(vItemId)
									.append("\":\"").append(vPartNumber)
									.append("\"");
						}
						vBrightness.put(vAttributeValue, vBrightnessTremString.toString());
						vBrightnessTremString.setLength(0);
						if (vBrightnessTypeString.length() == 0) {
							vAttributeType = (String) attributeType.get(M_BRIGHTNESS);
							vBrightnessTypeString.append("\"").append(
									M_BRIGHTNESS).append("\":{\"").append(
									vAttributeType).append("\":\"true\"}");
						}
					} else if (M_PACKTYPE.equals(vAttribute)) {
						if (vPacktype.containsKey(vAttributeValue)) {
							//vPacktypeTremString.setLength(10000);
							vPacktypeTremString.append( vPacktype.get(vAttributeValue));
							vPacktypeTremString.append(
									",\"").append(vItemId).append("\":\"")
									.append(vPartNumber).append("\"");
						} else {
							vPacktypeTremString.append("\"").append(vItemId)
									.append("\":\"").append(vPartNumber)
									.append("\"");
						}
						vPacktype.put(vAttributeValue, vPacktypeTremString.toString());
						vPacktypeTremString.setLength(0);
						if (vPacktypeTypeString.length() == 0) {
							vAttributeType = (String) attributeType.get(M_PACKTYPE);
							vPacktypeTypeString.append("\"").append(M_PACKTYPE)
									.append("\":{\"").append(vAttributeType)
									.append("\":\"true\"}");
						}
					} else if (M_SIZE.equals(vAttribute)) {
						if (vSize.containsKey(vAttributeValue)) {
							//vSizeTremString.setLength(10000);
							vSizeTremString .append( vSize.get(vAttributeValue));
							vSizeTremString.append(",\"").append(vItemId).append(
											"\":\"").append(vPartNumber)
									.append("\"");
						} else {
							vSizeTremString.append("\"").append(vItemId)
									.append("\":\"").append(vPartNumber)
									.append("\"");
						}
						vSize.put(vAttributeValue, vSizeTremString.toString());
						vSizeTremString.setLength(0);
						if (vSizeTypeString.length() == 0) {
							vAttributeType =  (String) attributeType.get(M_SIZE);
							vSizeTypeString.append("\"").append(M_SIZE).append(
									"\":{\"").append(vAttributeType).append(
									"\":\"true\"}");
						}
					} else if (M_FINISH.equals(vAttribute)) {
						if (vFinish.containsKey(vAttributeValue)) {
							//vFinishTremString.setLength(10000);
							vFinishTremString.append( vFinish.get(vAttributeValue));
							vFinishTremString.append(",\"")
									.append(vItemId).append("\":\"").append(
											vPartNumber).append("\"");
						} else {
							vFinishTremString.append("\"").append(vItemId)
									.append("\":\"").append(vPartNumber)
									.append("\"");
						}
						vFinish.put(vAttributeValue, vFinishTremString.toString());
						vFinishTremString.setLength(0);
						if (vFinishTypeString.length() == 0) {
							vAttributeType = (String) attributeType.get(M_FINISH);
							vFinishTypeString.append("\"").append(M_FINISH)
									.append("\":{\"").append(vAttributeType)
									.append("\":\"true\"}");
						}
					} else if (M_OPACITY.equals(vAttribute)) {
						if (vOpacity.containsKey(vAttributeValue)) {
							//vOpacityTremString.setLength(10000);
							vOpacityTremString.append( vOpacity.get(vAttributeValue));
							vOpacityTremString.append(",\"").append(vItemId).append(
											"\":\"").append(vPartNumber)
									.append("\"");
						} else {
							vOpacityTremString.append("\"").append(vItemId)
									.append("\":\"").append(vPartNumber)
									.append("\"");
						}
						vOpacity.put(vAttributeValue, vOpacityTremString.toString());
						vOpacityTremString.setLength(0);
						if (vOpacityTypeString.length() == 0) {
							vAttributeType = (String) attributeType.get(M_OPACITY);
							vOpacityTypeString.append("\"").append(M_OPACITY)
									.append("\":{\"").append(vAttributeType)
									.append("\":\"true\"}");
						}
					} else if (M_CERTIFICATIONS.equals(vAttribute)) {
						if (vCertifications.containsKey(vAttributeValue)) {
							//vCertificationsTremString.setLength(10000);
							vCertificationsTremString.append( vCertifications.get(vAttributeValue));
							vCertificationsTremString.append(",\"").append(vItemId).append(
											"\":\"").append(vPartNumber)
									.append("\"");
						} else {
							vCertificationsTremString.append("\"").append(
									vItemId).append("\":\"")
									.append(vPartNumber).append("\"");
						}
						vCertifications.put(vAttributeValue,
								vCertificationsTremString.toString());
						vCertificationsTremString.setLength(0);
						if (vCertificationsTypeString.length() == 0) {
							vAttributeType = (String) attributeType.get(M_CERTIFICATIONS);
							vCertificationsTypeString.append("\"").append(
									M_CERTIFICATIONS).append("\":{\"").append(
									vAttributeType).append("\":\"true\"}");
						}
					} else if (M_RECYCLED.equals(vAttribute)) {
						if (vRecycled.containsKey(vAttributeValue)) {
							//vRecycledTremString.setLength(10000);
							vRecycledTremString.append(vRecycled.get(vAttributeValue));
							vRecycledTremString.append(
									",\"").append(vItemId).append("\":\"")
									.append(vPartNumber).append("\"");
						} else {
							vRecycledTremString.append("\"").append(vItemId)
									.append("\":\"").append(vPartNumber)
									.append("\"");
						}
						vRecycled.put(vAttributeValue, vRecycledTremString.toString());
						vRecycledTremString.setLength(0);
						if (vRecycledTypeString.length() == 0) {
							vAttributeType = (String) attributeType.get(M_RECYCLED);
							vRecycledTypeString.append("\"").append(M_RECYCLED)
									.append("\":{\"").append(vAttributeType)
									.append("\":\"true\"}");
						}
					} else if (M_COLOR.equals(vAttribute)) {
						if (vColor.containsKey(vAttributeValue)) {
							//vColorTremString.setLength(10000);
							vColorTremString.append(vColor.get(vAttributeValue));
							vColorTremString.append(",\"").append(vItemId).append(
											"\":\"").append(vPartNumber)
									.append("\"");
						} else {
							vColorTremString.append("\"").append(vItemId)
									.append("\":\"").append(vPartNumber)
									.append("\"");
						}
						vColor
								.put(vAttributeValue, vColorTremString.toString());
						vColorTremString.setLength(0);
						if (vColorTypeString.length() == 0) {
							vAttributeType = (String) attributeType.get(M_COLOR);
							vColorTypeString.append("\"").append(M_COLOR)
									.append("\":{\"").append(vAttributeType)
									.append("\":\"true\"}");
						}
					} else if (M_SMOOTHNESS.equals(vAttribute)) {
						if (vSmoothness.containsKey(vAttributeValue)) {
							//vSmoothnessTremString.setLength(10000);
							vSmoothnessTremString.append( vSmoothness.get(vAttributeValue));
							vSmoothnessTremString.append(
									",\"").append(vItemId).append("\":\"")
									.append(vPartNumber).append("\"");
						} else {
							vSmoothnessTremString.append("\"").append(vItemId)
									.append("\":\"").append(vPartNumber)
									.append("\"");
						}
						vSmoothness.put(vAttributeValue, vSmoothnessTremString.toString());
						vSmoothnessTremString.setLength(0);
						if (vSmoothnessTypeString.length() == 0) {
							vAttributeType = (String) attributeType.get(M_SMOOTHNESS);
							vSmoothnessTypeString.append("\"").append(
									M_SMOOTHNESS).append("\":{\"").append(
									vAttributeType).append("\":\"true\"}");
						}
					}
				}
				vAttrBeanArr = null;
			//	long vIt4MapEnd = System.currentTimeMillis();
			//	System.out.println("Time for iterate for map is >>>>>>"+(vIt4MapEnd - vIt4MapStart)+" ms");
			//	long vIt4MapoutEnd = System.currentTimeMillis();
			//	System.out.println("Time for outer looping 4 map is #######"+(vIt4MapoutEnd - vIt4MapoutStart)+" ms");
			}
			System.out.println("itemDetails:"+itemDetails.size());
			vBasictypeTremString = null;
			vBasisweightTremString = null;
			vBrightnessTremString = null;
			vPacktypeTremString = null;
			vSizeTremString = null;
			vFinishTremString = null;
			vOpacityTremString = null;
			vCertificationsTremString = null;
			vRecycledTremString = null;
			vColorTremString = null;
			vSmoothnessTremString = null;
			long vloadMapEnd  = System.currentTimeMillis();
			System.out.println("Time for load map is >>>>>>"+(vloadMapEnd - vloadMapStart)+" ms");
			long vloadJsonStart  = System.currentTimeMillis();
			StringBuffer vJsonContent = new StringBuffer();
			vJsonContent.append("{");
			vJsonContent.append("\"").append(M_BRAND).append("\" : [ {");
			Iterator vBrandEntries = vBrand.entrySet().iterator();
			while (vBrandEntries.hasNext()) {
				Map.Entry pairs = (Map.Entry) vBrandEntries.next();
				vJsonContent.append("\"").append(pairs.getKey()).append(
						"\" : {").append(pairs.getValue()).append("}");
				if (vBrandEntries.hasNext()) {
					vJsonContent.append(",");
				}
				vBrandEntries.remove(); // avoids a
										// ConcurrentModificationException
			}
			vJsonContent.append("} ] , \"").append(M_BASICTYPE).append("\" : [ {");
			Iterator vBasictypeEntries = vBasictype.entrySet().iterator();
			while (vBasictypeEntries.hasNext()) {
				Map.Entry pairs = (Map.Entry) vBasictypeEntries.next();
				vJsonContent.append("\"").append(pairs.getKey()).append(
						"\" : {").append(pairs.getValue()).append("}");
				if (vBasictypeEntries.hasNext()) {
					vJsonContent.append(",");
				}
				vBasictypeEntries.remove(); // avoids a
											// ConcurrentModificationException
			}
			
			vJsonContent.append("} ] , \"").append(M_BASICWEIGHT).append("\" : [ {");
			Iterator vBasicweightEntries = vBasisweight.entrySet().iterator();
			while (vBasicweightEntries.hasNext()) {
				Map.Entry pairs = (Map.Entry) vBasicweightEntries.next();
				vJsonContent.append("\"").append(pairs.getKey()).append("\" : {").append(pairs.getValue()).append("}");
				if (vBasicweightEntries.hasNext()) {
					vJsonContent.append(",");
				}
				vBasicweightEntries.remove(); // avoids a
				// ConcurrentModificationException
			}
	
			vJsonContent.append("} ], \"").append(M_BRIGHTNESS).append("\" : [ {");
			Iterator vBrightnessEntries = vBrightness.entrySet().iterator();
			while (vBrightnessEntries.hasNext()) {
				Map.Entry pairs = (Map.Entry) vBrightnessEntries.next();
				vJsonContent.append("\"").append(pairs.getKey()).append(
						"\" : {").append(pairs.getValue()).append("}");
				if (vBrightnessEntries.hasNext()) {
					vJsonContent.append(",");
				}
				vBrightnessEntries.remove(); // avoids a
												// ConcurrentModificationException
			}
			vJsonContent.append("} ] , \"").append(M_PACKTYPE).append("\" : [ {");
			Iterator vPacktypeEntries = vPacktype.entrySet().iterator();
			while (vPacktypeEntries.hasNext()) {
				Map.Entry pairs = (Map.Entry) vPacktypeEntries.next();
				vJsonContent.append("\"").append(pairs.getKey()).append(
						"\" : {").append(pairs.getValue()).append("}");
				if (vPacktypeEntries.hasNext()) {
					vJsonContent.append(",");
				}
				vPacktypeEntries.remove(); // avoids a
											// ConcurrentModificationException
			}
			vJsonContent.append("} ] , \"").append(M_SIZE).append("\" : [ {");
			Iterator vSizeEntries = vSize.entrySet().iterator();
			while (vSizeEntries.hasNext()) {
				Map.Entry pairs = (Map.Entry) vSizeEntries.next();
				vJsonContent.append("\"").append(pairs.getKey()).append(
						"\" : {").append(pairs.getValue()).append("}");
				if (vSizeEntries.hasNext()) {
					vJsonContent.append(",");
				}
				vSizeEntries.remove(); // avoids a
										// ConcurrentModificationException
			}
			vJsonContent.append("} ] , \"").append(M_FINISH).append("\" : [ {");
			Iterator vFinishEntries = vFinish.entrySet().iterator();
			while (vFinishEntries.hasNext()) {
				Map.Entry pairs = (Map.Entry) vFinishEntries.next();
				vJsonContent.append("\"").append(pairs.getKey()).append(
						"\" : {").append(pairs.getValue()).append("}");
				if (vFinishEntries.hasNext()) {
					vJsonContent.append(",");
				}
				vFinishEntries.remove(); // avoids a
											// ConcurrentModificationException
			}
			vJsonContent.append("} ] , \"").append(M_OPACITY).append("\" : [ {");
			Iterator vOpacityEntries = vOpacity.entrySet().iterator();
			while (vOpacityEntries.hasNext()) {
				Map.Entry pairs = (Map.Entry) vOpacityEntries.next();
				vJsonContent.append("\"").append(pairs.getKey()).append(
						"\" : {").append(pairs.getValue()).append("}");
				if (vOpacityEntries.hasNext()) {
					vJsonContent.append(",");
				}
				vOpacityEntries.remove(); // avoids a
											// ConcurrentModificationException
			}
			vJsonContent.append("} ], \"").append(M_CERTIFICATIONS).append(
					"\" : [ {");
			Iterator vCertificationsEntries = vCertifications.entrySet()
					.iterator();
			while (vCertificationsEntries.hasNext()) {
				Map.Entry pairs = (Map.Entry) vCertificationsEntries.next();
				vJsonContent.append("\"").append(pairs.getKey()).append(
						"\" : {").append(pairs.getValue()).append("}");
				if (vCertificationsEntries.hasNext()) {
					vJsonContent.append(",");
				}
				vCertificationsEntries.remove(); // avoids a
													// ConcurrentModificationException
			}
			vJsonContent.append("} ] , \"").append(M_RECYCLED).append("\" : [ {");
			Iterator vRecycledEntries = vRecycled.entrySet().iterator();
			while (vRecycledEntries.hasNext()) {
				Map.Entry pairs = (Map.Entry) vRecycledEntries.next();
				vJsonContent.append("\"").append(pairs.getKey()).append(
						"\" : {").append(pairs.getValue()).append("}");
				if (vRecycledEntries.hasNext()) {
					vJsonContent.append(",");
				}
				vRecycledEntries.remove(); // avoids a
											// ConcurrentModificationException
			}
			vJsonContent.append("} ] , \"").append(M_COLOR).append("\" : [ {");
			Iterator vColorEntries = vColor.entrySet().iterator();
			while (vColorEntries.hasNext()) {
				Map.Entry pairs = (Map.Entry) vColorEntries.next();
				vJsonContent.append("\"").append(pairs.getKey()).append(
						"\" : {").append(pairs.getValue()).append("}");
				if (vColorEntries.hasNext()) {
					vJsonContent.append(",");
				}
				vColorEntries.remove(); // avoids a
										// ConcurrentModificationException
			}
			vJsonContent.append("} ] , \"").append(M_SMOOTHNESS).append("\" : [ {");
			Iterator vSmoothnessEntries = vSmoothness.entrySet().iterator();
			while (vSmoothnessEntries.hasNext()) {
				Map.Entry pairs = (Map.Entry) vSmoothnessEntries.next();
				vJsonContent.append("\"").append(pairs.getKey()).append(
						"\" : {").append(pairs.getValue()).append("}");
				if (vSmoothnessEntries.hasNext()) {
					vJsonContent.append(",");
				}
				vSmoothnessEntries.remove(); // avoids a
												// ConcurrentModificationException
			}
			vJsonContent.append("} ]");
			vJsonContent.append(", \"attributeType\" : [ {");
			if (vBrandTypeString.length() > 0) {
				vJsonContent.append(vBrandTypeString.toString()).append(", ");
			}else{
				vJsonContent.append("\"").append(M_BRAND).append("\" : {} , ");
			}
			
			if (vBasictypeTypeString.length() > 0) {
				vJsonContent.append(vBasictypeTypeString.toString()).append(", ");
			}else{
				vJsonContent.append("\"").append(M_BASICTYPE).append("\" : {} , ");
			}
			
			if (vBasisweightTypeString.length() > 0) {
				vJsonContent.append(vBasisweightTypeString.toString()).append(", ");
			}else{
				vJsonContent.append("\"").append(M_BASICWEIGHT).append("\" : {} , ");
			}
			
			if (vBrightnessTypeString.length() > 0) {
				vJsonContent.append(vBrightnessTypeString.toString()).append(", ");
			}else{
				vJsonContent.append("\"").append(M_BRIGHTNESS).append("\" : {} , ");
			}
			if (vPacktypeTypeString.length() > 0) {
				vJsonContent.append(vPacktypeTypeString.toString()).append(", ");
			}else{
				vJsonContent.append("\"").append(M_PACKTYPE).append("\" : {} , ");
			}
			if (vSizeTypeString.length() > 0) {
				vJsonContent.append(vSizeTypeString.toString()).append(", ");
			}else{
				vJsonContent.append("\"").append(M_SIZE).append("\" : {} , ");
			}
			if (vFinishTypeString.length() > 0) {
				vJsonContent.append(vFinishTypeString.toString()).append(", ");
			}else{
				vJsonContent.append("\"").append(M_FINISH).append("\" : {} , ");
			}
			if (vOpacityTypeString.length() > 0) {
				vJsonContent.append(vOpacityTypeString.toString()).append(", ");
			}else{
				vJsonContent.append("\"").append(M_OPACITY).append("\" : {} , ");
			}
			if (vCertificationsTypeString.length() > 0) {
				vJsonContent.append(vCertificationsTypeString.toString()).append(", ");
			}else{
				vJsonContent.append("\"").append(M_CERTIFICATIONS).append("\" : {} , ");
			}
			if (vRecycledTypeString.length() > 0) {
				vJsonContent.append(vRecycledTypeString.toString()).append(", ");
			}else{
				vJsonContent.append("\"").append(M_RECYCLED).append("\" : {} , ");
			}
			if (vColorTypeString.length() > 0) {
				vJsonContent.append(vColorTypeString.toString()).append(", ");
			}else{
				vJsonContent.append("\"").append(M_COLOR).append("\" : {} , ");
			}
			if (vSmoothnessTypeString.length() > 0) {
				vJsonContent.append(vSmoothnessTypeString.toString());
			}else{
				vJsonContent.append("\"").append(M_SMOOTHNESS).append("\" : {} ");
			}
			vJsonContent.append(" } ] }");		
			vJsonObjTOReturn = vJsonContent.toString();	
			long vloadJsonEnd  = System.currentTimeMillis();
			System.out.println("**** TIME AFTER MQT *****");
			System.out.println("Time for load Json is >>>>>>"+(vloadJsonEnd - vloadJsonStart)+" ms");
		} catch (Exception e) {
			ExceptionHandler.convertToECException(e);
		}
//		System.out.println("Filter Details Json : "+vJsonObjTOReturn);
		return vJsonObjTOReturn;
	}

	
}
