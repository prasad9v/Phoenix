package com.ibm.commerce.domtar.commands;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.json.JSON;
import org.apache.commons.json.JSONArtifact;

import com.ibm.commerce.base.objects.ServerJDBCHelperAccessBean;
import com.ibm.commerce.domtar.databeans.DomtarAddress;
import com.ibm.commerce.domtar.util.DomtarJDBCHelper;
import com.ibm.commerce.exception.ECApplicationException;
import com.ibm.commerce.exception.ExceptionHandler;
import com.ibm.commerce.ras.ECMessage;
import com.ibm.websphere.command.CacheableCommandImpl;

public class DomtarPopulateRRCDetailsForStockCheckCmdImpl extends
		CacheableCommandImpl implements
		DomtarPopulateRRCDetailsForStockCheckCmd {
	
	private JSONArtifact rrcAddressJsonArtifact;
	private String storeId;
	private String languageId;
	private String organizationId;
		

	/**
	 * @return the storeId
	 */
	public String getStoreId() {
		return storeId;
	}

	/**
	 * @param storeId the storeId to set
	 */
	public void setStoreId(String storeId) {
		this.storeId = storeId;
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

	/**
	 * @return the organizationId
	 */
	public String getOrganizationId() {
		return organizationId;
	}

	/**
	 * @param organizationId the organizationId to set
	 */
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	/**
	 * @return the rrcAddressJsonArtifact
	 */
	public JSONArtifact getRrcAddressJsonArtifact() {
		return rrcAddressJsonArtifact;
	}

	/**
	 * @param rrcAddressJsonArtifact the rrcAddressJsonArtifact to set
	 */
	public void setRrcAddressJsonArtifact(JSONArtifact rrcAddressJsonArtifact) {
		this.rrcAddressJsonArtifact = rrcAddressJsonArtifact;
	}

	@Override
	public boolean isReadyToCallExecute() {
		if(getOrganizationId() != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void performExecute() throws Exception {
		long vEntryPoint = System.currentTimeMillis();		
		String vJsonObj = generateRRCAddressJSONObj(getOrganizationId());

		try {
			if(!vJsonObj.equals("")){
				setRrcAddressJsonArtifact(JSON.parse(vJsonObj));
			}
		} catch (Exception e) {
			ExceptionHandler.convertToECException(e);
		} 		
		long vExitPoint = System.currentTimeMillis();
		long TotCallDiff = (vExitPoint-vEntryPoint);
		System.out.println("Time taken for creating RRC address JSON is >>>>>>"+TotCallDiff+" ms");
	}

	private String generateRRCAddressJSONObj(String organizationId) throws ECApplicationException {

		String strMethodName = "populateRRCDetailsForStcockCheck";
		StringBuffer jsononCountry = new StringBuffer();
		try {

			HashMap CountryMap = new HashMap();
			HashMap StateMap = new HashMap();
			HashMap CityMap = new HashMap();
			HashMap RRCMap = new HashMap();
			HashMap rrcMap = new HashMap();
			HashMap rrcSoldToRelMap = new HashMap();
			HashMap soldToShipToRelMap = new HashMap();
//			MemberDataBean vMemeberDataBean = new MemberDataBean();
//			vMemeberDataBean.setDataBeanKeyMemberId(parentMemberId);
//			DataBeanManager.activate(vMemeberDataBean);
			
			ArrayList<String> soldToList = new ArrayList<String>();
			
			StringBuffer queryBuilder= new StringBuffer();
			queryBuilder.append("SELECT A.ADDRESSTYPE,A.NICKNAME,A.COUNTRY,A.STATE,A.CITY FROM MBRREL MBR,USERS U,ADDRESS A ");
			queryBuilder.append("WHERE MBR.DESCENDANT_ID=U.USERS_ID AND U.REGISTERTYPE='A' AND A.STATUS='P' ");
			queryBuilder.append("AND A.ADDRESSTYPE IN ('SB','B') AND (A.ISPRIMARY IS NULL OR A.ISPRIMARY <> 1) AND U.USERS_ID=A.MEMBER_ID AND MBR.ANCESTOR_ID = ?");
			//LOGGER.info("ParentMemberId : "+parentMemberId+" : "+queryBuilder.toString());
			
			ServerJDBCHelperAccessBean jdbcHelper = new ServerJDBCHelperAccessBean();	
			Vector databaseRows = new Vector();
			Object[] queryParams = new Object[1];
			queryParams[0] = organizationId;

			databaseRows = jdbcHelper.executeParameterizedQuery(queryBuilder.toString(),queryParams);
			if(databaseRows != null && databaseRows.size()>0){
				for (Object obj : databaseRows) {
					Vector dbRow = (Vector) obj;
					String addressType;
					String nickName;
					String country;
					String state;
					String city;
					try{
						addressType = ((String) dbRow.get(0)).trim();
						nickName = ((String) dbRow.get(1)).trim();
						country = ((String) dbRow.get(2)).trim();
						state = ((String) dbRow.get(3)).trim();
						city = ((String) dbRow.get(4)).trim();
					}catch (Exception e) {
						/*
						 * if any of country, state and city is null, Ignore that row and continue with
						 * the rest.
						 */
						continue;
					}
					
					if("SB".equals(addressType)){
						boolean isRrcANumber = false;
						try {
							NumberFormat.getInstance().parse(nickName);
							isRrcANumber = true;
						} catch (ParseException e) {
							isRrcANumber = false;
						}
						if (isRrcANumber) {
							rrcMap.put(nickName, "");
							if (!CountryMap.containsKey(country)) {
								RRCMap = new HashMap();
								CityMap = new HashMap();
								StateMap = new HashMap();
								RRCMap.put(nickName, "");
								String cityKey = city;
								CityMap.put(cityKey,RRCMap);
								StateMap.put(state,CityMap);
								CountryMap.put(country,StateMap);
							} else {
								StateMap = (HashMap) CountryMap.get(country);
								if (StateMap.containsKey(state)) {
									CityMap = (HashMap) StateMap.get(state);
									if (CityMap.containsKey(city)) {
										RRCMap = (HashMap) CityMap.get(city);
										RRCMap.put(nickName, "");
										CityMap.put(city, RRCMap);
									}else{
										RRCMap = new HashMap();
										RRCMap.put(nickName, "");
										String cityKey = city;
										CityMap.put(cityKey, RRCMap);
										StateMap.put(state, CityMap);
									}
										
								} else {
									RRCMap = new HashMap();
									CityMap = new HashMap();
									RRCMap.put(nickName, "");
									String cityKey = city;
									CityMap.put(cityKey,RRCMap);
									StateMap.put(state,CityMap);
								}
								CountryMap.put(country,StateMap);
							}
						}
					}else if("B".equals(addressType)){
						soldToList.add(nickName);
					}
				}

			

//			if (vMemeberDataBean.getDescendants() != null
//					&& vMemeberDataBean.getDescendants().length > 0) {
//				for (Long cmBean : vMemeberDataBean.getDescendants()) {
//					MemberDataBean vChildMemeberDataBean = new MemberDataBean();
//					vChildMemeberDataBean.setDataBeanKeyMemberId(cmBean
//							.toString());
//					DataBeanManager.activate(vChildMemeberDataBean);
//					if ("UserBean".equals(vChildMemeberDataBean.getType())) {
//						UserAccessBean userAcBean = new UserAccessBean();
//						userAcBean.setInitKey_MemberId(cmBean.toString());
//						userAcBean.refreshCopyHelper();
//						if ("A".equals(userAcBean.getRegisterType())) {
//							Enumeration enmAddressList = (new AddressAccessBean())
//									.findByMemberId(cmBean);
//							while (enmAddressList.hasMoreElements()) {
//								AddressAccessBean addressbean = (AddressAccessBean) enmAddressList
//										.nextElement();
//								if ("SB".equals(addressbean.getAddressType()) && !addressbean.getPrimary().equals("1")) {
//									boolean isRrcANumber = false;
//									try {
//										NumberFormat.getInstance().parse(addressbean.getNickName());
//										isRrcANumber = true;
//									} catch (ParseException e) {
//										isRrcANumber = false;
//									}
//									if (isRrcANumber) {
//										rrcMap.put(addressbean.getNickName(), "");
//										if (!CountryMap.containsKey(addressbean.getCountry())) {
//											RRCMap = new HashMap();
//											CityMap = new HashMap();
//											StateMap = new HashMap();
//											RRCMap.put(addressbean.getNickName(), "");
//											String cityKey = addressbean.getCity();
//											CityMap.put(cityKey,RRCMap);
//											StateMap.put(addressbean.getState(),CityMap);
//											CountryMap.put(addressbean.getCountry(),StateMap);
//										} else {
//											StateMap = (HashMap) CountryMap.get(addressbean.getCountry());
//											if (StateMap.containsKey(addressbean.getState())) {
//												CityMap = (HashMap) StateMap.get(addressbean.getState());
//												if (CityMap.containsKey(addressbean.getCity())) {
//													RRCMap = (HashMap) CityMap.get(addressbean.getCity());
//													RRCMap.put(addressbean.getNickName(), "");
//													CityMap.put(addressbean.getCity(), RRCMap);
//												}else{
//													RRCMap = new HashMap();
//													RRCMap.put(addressbean.getNickName(), "");
//													String cityKey = addressbean.getCity();
//													CityMap.put(cityKey, RRCMap);
//													StateMap.put(addressbean.getState(), CityMap);
//												}
//													
//											} else {
//												RRCMap = new HashMap();
//												CityMap = new HashMap();
//												RRCMap.put(addressbean.getNickName(), "");
//												String cityKey = addressbean.getCity();
//												CityMap.put(cityKey,RRCMap);
//												StateMap.put(addressbean.getState(),CityMap);
//											}
//											CountryMap.put(addressbean.getCountry(),StateMap);
//										}
//									}
//								}else if("B".equals(addressbean.getAddressType())){
//									soldToList.add(addressbean.getNickName());
//								}
//							}
//						}
//					}
//				}

				Iterator<Map.Entry<String, String>> entries = rrcMap.entrySet()
						.iterator();
				while (entries.hasNext()) {
					Map.Entry<String, String> entry = entries.next();
					String rrc = entry.getKey();
					boolean isRrcANumber = false;
					try {
						NumberFormat.getInstance().parse(rrc);
						isRrcANumber = true;
					} catch (ParseException e) {
						isRrcANumber = false;
					}
					if (isRrcANumber) {
//						DomtarAddressDatabean tempDomtardatabean = new DomtarAddressDatabean();
//						tempDomtardatabean.setRrc(rrc);
//						tempDomtardatabean.setSearchType("RRC");
//						DataBeanManager.activate(tempDomtardatabean);
//						List<DomtarAddress> addressList = new ArrayList<DomtarAddress>();
//						addressList = tempDomtardatabean.getAddressList();
						DomtarJDBCHelper domtarHelper = new DomtarJDBCHelper();
						List<DomtarAddress> addressList = domtarHelper.findAddressRelationByRrcAddressId(rrc, soldToList);
						String rrcIdentifier = "";
						String soldToIdentifier = "";
						String shipToIdentifier = "";
						if (addressList != null) {
							for (int i = 0; i < addressList.size(); i++) {
								DomtarAddress soldtobean = addressList.get(i);
								rrcIdentifier = soldtobean.getRrc() + "|-:-|"
										+ soldtobean.getRrcName();
								soldToIdentifier = soldtobean.getSoldTo()
										+ "|-:-|" + soldtobean.getSoldToName();
								shipToIdentifier = soldtobean.getShipTo()
										+ "|-:-|" + soldtobean.getShipToName();
								rrcMap.put(rrc, rrcIdentifier);
								if (rrcSoldToRelMap.containsKey(rrcIdentifier)) {
									if ((rrcSoldToRelMap.get(rrcIdentifier)
											.toString())
											.indexOf(soldToIdentifier) == -1) {
										String soldToDetails = rrcSoldToRelMap
												.get(rrcIdentifier)
												+ "#!#" + soldToIdentifier;
										rrcSoldToRelMap.put(rrcIdentifier,
												soldToDetails);
									}
								} else {
									rrcSoldToRelMap.put(rrcIdentifier,
											soldToIdentifier);
								}

								if (soldToShipToRelMap
										.containsKey(soldToIdentifier)) {
									if ((soldToShipToRelMap
											.get(soldToIdentifier).toString())
											.indexOf(shipToIdentifier) == -1) {
										String shipToDetails = soldToShipToRelMap
												.get(soldToIdentifier)
												+ "#!#" + shipToIdentifier;
										soldToShipToRelMap
												.put(soldToIdentifier,
														shipToDetails);
									}
								} else {
									soldToShipToRelMap.put(soldToIdentifier,
											shipToIdentifier);
								}

							}
						}
					}
				}

				jsononCountry.append("{");
				Iterator<Map.Entry<String, HashMap>> countries = CountryMap
						.entrySet().iterator();
				while (countries.hasNext()) {
					Map.Entry<String, HashMap> countryEntry = countries.next();
					jsononCountry.append("\"").append(countryEntry.getKey())
							.append("\":{");
					Iterator<Map.Entry<String, HashMap>> states = countryEntry
							.getValue().entrySet().iterator();
					while (states.hasNext()) {
						Map.Entry<String, HashMap> stateEntry = states.next();
						jsononCountry.append("\"").append(stateEntry.getKey())
								.append("\":{");
						Iterator<Map.Entry<String, HashMap>> cities = stateEntry
								.getValue().entrySet().iterator();
						while (cities.hasNext()) {
							Map.Entry<String, HashMap> cityEntry = cities.next();
							jsononCountry.append("\"").append(cityEntry.getKey())
								.append("\":{");
							Iterator<Map.Entry<String, String>> rrcs = cityEntry
								.getValue().entrySet().iterator();
							while (rrcs.hasNext()) {
								Map.Entry<String, String> rrcEntry = rrcs.next();
								jsononCountry.append("\"").append(rrcMap.get(rrcEntry.getKey()))
								.append("\":{");
								if (rrcMap.containsKey(rrcEntry.getKey())) {
									String temRrcIdentifier = rrcMap.get(
											rrcEntry.getKey()).toString();
									if (rrcSoldToRelMap
											.containsKey(temRrcIdentifier)) {
										String soldToDetails = rrcSoldToRelMap.get(
												temRrcIdentifier).toString();
										if (soldToDetails != null) {
											String[] soldtoDetailsArr = soldToDetails.split("#!#");
											for (int j = 0; j < soldtoDetailsArr.length; j++) {
												jsononCountry.append("\"").append(
														soldtoDetailsArr[j])
														.append("\":{");
												if (soldToShipToRelMap
														.containsKey(soldtoDetailsArr[j])) {
													String shipToDetails = soldToShipToRelMap
															.get(soldtoDetailsArr[j]).toString();
													String[] shiptoDetailsArr = shipToDetails.split("#!#");
													for (int k = 0; k < shiptoDetailsArr.length; k++) {
														jsononCountry
																.append("\"")
																.append(shiptoDetailsArr[k])
																.append("\": \"true\"");
														if ((k + 1) < shiptoDetailsArr.length) {
															jsononCountry.append(",");
														}
													}
													//SOLD TO
													jsononCountry.append("}");
													if ((j + 1) < soldtoDetailsArr.length) {
														jsononCountry.append(",");
													}
												} 
											}
										}
									}
									//RRC Closed
									jsononCountry.append("}");
									if (rrcs.hasNext()) {
										jsononCountry.append(",");
									}
								}
							}

							
							//City closed
							jsononCountry.append("}");
							
							if (cities.hasNext()) {
								jsononCountry.append(",");
							}
							
							
						}
						//State closed
						jsononCountry.append("}");
						if (states.hasNext()) {
							jsononCountry.append(",");
						}
					}
					//Country closed
					jsononCountry.append("}");
					if (countries.hasNext()) {
						jsononCountry.append(",");
					}
				}
				jsononCountry.append("}");

			}
		} catch (Exception e) {
			//e.printStackTrace();
			throw new ECApplicationException(ECMessage._ERR_BAD_CMD_PARAMETER,
			 this.getClass().getName(), strMethodName,"");
		}
		return jsononCountry.toString();
	}

}
