package com.ibm.commerce.domtar.commands;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.NamingException;

import org.apache.commons.json.JSONArtifact;

import com.ibm.commerce.base.objects.ServerJDBCHelperAccessBean;
import com.ibm.commerce.command.CommandContext;
import com.ibm.commerce.command.ControllerCommandImpl;
import com.ibm.commerce.context.entitlement.EntitlementContext;
import com.ibm.commerce.datatype.TypedProperty;
import com.ibm.commerce.domtar.databeans.DomtarAddress;
import com.ibm.commerce.domtar.helpers.DomtarHelper;
import com.ibm.commerce.domtar.util.DomtarJDBCHelper;
import com.ibm.commerce.exception.ECApplicationException;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.exception.ECSystemException;
import com.ibm.commerce.exception.ExceptionHandler;
import com.ibm.commerce.ras.ECMessage;
import com.ibm.commerce.server.ECConstants;

public class DomtarAjaxPageLoadControllerCmdImpl extends ControllerCommandImpl
		implements DomtarAjaxPageLoadControllerCmd {
	
	private static final String M_HOME_PAGE = "DomtarHomePageDisplay";
	private static final String M_PRODUCTCATALOG_PAGE = "DomtarProductCatalogPageDisplay";
	private static final String M_STOCKCHECK_PAGE = "DomtarStockCheckPageDisplay";
	private static final String M_CUSTOMERTONNAGE_PAGE = "DomtarCustomerTonnage";
	private static final String M_MANAGEDOCUMENT_PAGE = "DomtarManageDocView";
	private static final String M_ORDERSTATUS_PAGE = "DomtarOrderStatusPageDisplay";
	
	private String addressRelationJson;
	
	public void performExecute() throws ECException {
		super.performExecute();
		TypedProperty rspProp = new TypedProperty();		
		String vSelectedPage = getRequestProperties().getString("selectedPage","");
		String vStoreId = getRequestProperties().getString("storeId","");
		String vLangId = getRequestProperties().getString("langId","-1");
		String vCatalogId = getRequestProperties().getString("catalogId","");
		
		//Get the organization of the user as ID
		String organizationId = null;
		try {
			EntitlementContext ctx = (EntitlementContext) getCommandContext()
			.getContext("com.ibm.commerce.context.entitlement.EntitlementContext");
			if (ctx != null) {
				organizationId = ctx.getActiveOrganizationId().toString();
			} else {
				organizationId = getCommandContext().getUser().getParentMemberId();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		/*
		 * Populate the address relation JSON by calling the cached command.
		 */
		DomtarPopulateUserAddressListCmd populateAddressRelationCmd = null;
		JSONArtifact jsonAddressRelArtifact = null;
		List<DomtarAddress> addressList = null;
		if("CUSTOMERTONNAGE".equals(vSelectedPage) || "MANAGEDOCUMENT".equals(vSelectedPage) || "ORDERSTATUS".equals(vSelectedPage)){
			try {
				populateAddressRelationCmd = new DomtarPopulateUserAddressListCmdImpl();
				populateAddressRelationCmd.setStoreId(vStoreId);
				populateAddressRelationCmd.setLanguageId(vLangId);
				populateAddressRelationCmd.setOrganizationId(organizationId);				
				populateAddressRelationCmd.execute();
				
				jsonAddressRelArtifact = populateAddressRelationCmd.getAddressRelationJsonArtifact();
				addressList = populateAddressRelationCmd.getUserAddressList();			
				
			} catch (Exception e) {
				ExceptionHandler.convertToECException(e);
			}
		}		
		
		if("HOME".equals(vSelectedPage)){
			rspProp.put(ECConstants.EC_VIEWTASKNAME, M_HOME_PAGE);
		}else if("PRODUCTCATALOG".equals(vSelectedPage)){	
			rspProp.put(ECConstants.EC_VIEWTASKNAME, M_PRODUCTCATALOG_PAGE);
		}else if("STOCKCHECK".equals(vSelectedPage)){	
			String vItemCodeToRoute = getRequestProperties().getString("route_to_itemCode","");
			if(!"".equals(vItemCodeToRoute)){
				rspProp.put("route_to_itemcode", vItemCodeToRoute);
			}			
			/*
			 * call the cached command to get the rrc json for stockcheck
			 */
			try{
				DomtarPopulateRRCDetailsForStockCheckCmd populateRRCAddressCmd = new DomtarPopulateRRCDetailsForStockCheckCmdImpl();
				populateRRCAddressCmd.setStoreId(vStoreId);
				populateRRCAddressCmd.setLanguageId(vLangId);
				populateRRCAddressCmd.setOrganizationId(organizationId);
				
				populateRRCAddressCmd.execute();
				JSONArtifact jsonFilterArtifact = populateRRCAddressCmd.getRrcAddressJsonArtifact();
				if(jsonFilterArtifact != null){
					rspProp.put("JSONObj", jsonFilterArtifact);
				}
			}catch (Exception e) {
				ExceptionHandler.convertToECException(e);
			} 
//			String vJSONObj = populateRRCDetailsForStockCheck();
//			rspProp.put("JSONObj", vJSONObj);
			
			rspProp.put(ECConstants.EC_VIEWTASKNAME, M_STOCKCHECK_PAGE);
		}else if("CUSTOMERTONNAGE".equals(vSelectedPage)){

			String vJSONObj = createCountryStateCityJson(addressList);
			rspProp.put("countryListJSON", vJSONObj);

			if(jsonAddressRelArtifact != null){
				rspProp.put("xAddrRelJSON", jsonAddressRelArtifact);
			}

//			List addressList = populateUserAddressList();
//			String vJSONObj = createCountryStateCityJson(addressList);
//			
//			rspProp.put("countryListJSON", vJSONObj);
//			rspProp.put("xAddrRelJSON", addressRelationJson);
			rspProp.put(ECConstants.EC_VIEWTASKNAME, M_CUSTOMERTONNAGE_PAGE);
		}else if("MANAGEDOCUMENT".equals(vSelectedPage)){
			
			if(jsonAddressRelArtifact != null){
				rspProp.put("xAddrRelJSON", jsonAddressRelArtifact);
			}
			rspProp.put(ECConstants.EC_VIEWTASKNAME, M_MANAGEDOCUMENT_PAGE);
		}else if("ORDERSTATUS".equals(vSelectedPage)){
			if(jsonAddressRelArtifact != null){
				rspProp.put("xAddrRelJSON", jsonAddressRelArtifact);
			}
			rspProp.put(ECConstants.EC_VIEWTASKNAME, M_ORDERSTATUS_PAGE);
		}
		
		setResponseProperties(rspProp);
	}
	private String createCountryStateCityJson(List addressList) {
		HashMap<HashSet<String>, HashSet<String>> addressRelationMap = DomtarHelper.convertUserAddressListToMap(addressList);
		
		String sqlStatement = "SELECT DISTINCT T4.SOURCECOUNTRY ||'##-##'|| T4.SOURCESTATE ||'##-##'||T4.SOURCECITY " +
				"FROM ORDERS T1, ORDERITEMS T2, ADDRESS T3, XORDITEMS T4 " +
				"WHERE T1.ORDERS_ID=T2.ORDERS_ID AND T1.ORDERS_ID=T4.ORDERS_ID " +
				"AND T3.ADDRESS_ID=T2.FIELD1 AND T3.STATUS ='P' AND T2.STATUS = 'I' AND T4.INCLUDEINTONNAGE='Y' ";
		
		sqlStatement += "AND T3.NICKNAME IN (";
		
		for (Entry<HashSet<String>, HashSet<String>> entry : addressRelationMap.entrySet()) {
			HashSet<String> shipToSet =entry.getValue();
			for (Iterator iterator = shipToSet.iterator(); iterator.hasNext();) {
				String shipToValue = (String) iterator.next();
				sqlStatement += "'" + shipToValue + "'";
				if (iterator.hasNext()) {
					sqlStatement += ", ";
				}
			}
		}
		
//		for (Iterator iterator = addressList.iterator(); iterator.hasNext();) {
//			DomtarAddress object = (DomtarAddress) iterator.next();
//			sqlStatement += "'" + object.getShipTo() + "'";
//			if (iterator.hasNext()) {
//				sqlStatement += ", ";
//			}
//		}
		sqlStatement += ") ";
		
		sqlStatement += "AND T1.FIELD1 IN (";
		
		for (Entry<HashSet<String>, HashSet<String>> entry : addressRelationMap.entrySet()) {
			HashSet<String> soldToSet =entry.getKey();
			for (Iterator iterator = soldToSet.iterator(); iterator.hasNext();) {
				String soldToValue = (String) iterator.next();
				sqlStatement += soldToValue;
				if (iterator.hasNext()) {
					sqlStatement += ", ";
				}
			}
		}
		
		
//		for (Iterator iterator = addressList.iterator(); iterator.hasNext();) {
//			DomtarAddress object = (DomtarAddress) iterator.next();
//			sqlStatement += object.getSoldTo();
//			if (iterator.hasNext()) {
//				sqlStatement += ", ";
//			}
//		}
		sqlStatement += ")";
//		System.out.println("CountryStateCity Query : "+sqlStatement);
		
		Vector databaseRows = new Vector();
		ServerJDBCHelperAccessBean jdbcHelper = new ServerJDBCHelperAccessBean();
		StringBuffer countryJSON = new StringBuffer("{ \"CountryList\" : [") ;
		try {			
			databaseRows = jdbcHelper.executeQuery(sqlStatement);
			
			for (Iterator iterator = databaseRows.iterator(); iterator.hasNext();) {
				Vector dbRow = (Vector) iterator.next();
				String combinedvalues = (String) dbRow.get(0);
				String[] combinedCountryStateCityArray = combinedvalues.split("##-##");
				
				if(combinedCountryStateCityArray.length == 3){
					String Country = combinedCountryStateCityArray[0]; 
					String State = combinedCountryStateCityArray[1]; 
					String City = combinedCountryStateCityArray[2];
					
					countryJSON.append("{");
					countryJSON.append("\"Country\":\""+Country+"\",");
					countryJSON.append("\"State\":\""+State+"\",");
					countryJSON.append("\"City\":\""+City+"\"");
					countryJSON.append("}");
					if(iterator.hasNext()){
						countryJSON.append(",");
					}
				}
			}
			
		} catch (RemoteException e) {

			e.printStackTrace();
		} catch (NamingException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		} catch (CreateException e) {

			e.printStackTrace();
		}
		countryJSON.append("]}");
		return countryJSON.toString();
	}
	private List populateUserAddressList() {		
		List addressList = new ArrayList<DomtarAddress>();
		StringBuffer addressJSON = new StringBuffer("{ \"xAddrRel\" : [") ;
		try {
			CommandContext commandcontext = getCommandContext();
			String parentMemberId = null;

			EntitlementContext ctx = (EntitlementContext) commandcontext
					.getContext("com.ibm.commerce.context.entitlement.EntitlementContext");
			if (ctx != null) {
				parentMemberId = ctx.getActiveOrganizationId().toString();
			} else {
				parentMemberId = commandcontext.getUser().getParentMemberId();
			}
//			MemberAccessBean mAb = new MemberAccessBean();
//			mAb.setInitKey_MemberId(parentMemberId);
//			mAb.refreshCopyHelper();

			
			
			int addressCount=0;
			
			
			DomtarJDBCHelper domtarHelper = new DomtarJDBCHelper();
			try {
				addressList = domtarHelper.findSoldToRRCShipToAddressByOrganizationId(parentMemberId);
				Collections.sort(addressList);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(addressList != null){
				for (Iterator iterator = addressList.iterator(); iterator.hasNext();) {
					DomtarAddress domtarAddress = (DomtarAddress) iterator.next();
					if(addressCount!=0){
						addressJSON.append(",");
					}
					addressJSON.append("{");
					addressJSON.append("\"SoldTo\":\""+domtarAddress.getSoldTo()+"\",");
					addressJSON.append("\"SoldToName\":\""+domtarAddress.getSoldToName()+"\",");
					addressJSON.append("\"ShipTo\":\""+domtarAddress.getShipTo()+"\",");
					addressJSON.append("\"ShipToName\":\""+domtarAddress.getShipToName()+"\",");
					addressJSON.append("\"rrc\":\""+domtarAddress.getRrc()+"\",");
					addressJSON.append("\"rrcName\":\""+domtarAddress.getRrcName()+"\"");
					addressJSON.append("}");
					
					addressCount++;
				}
			}

//			if (mAb.getDescendants().length > 0) {
//				for (int i = 0; i < mAb.getDescendants().length; i++) {
//					Long childMemberId = mAb.getDescendants()[i];
//					MemberAccessBean mAb1 = new MemberAccessBean();
//					mAb1.setInitKey_MemberId(childMemberId.toString());
//					mAb1.refreshCopyHelper();
//					if (mAb1.getType().equals("UserBean")) {
//						UserAccessBean userAcBean = new UserAccessBean();
//						userAcBean.setInitKey_MemberId(childMemberId.toString());
//						userAcBean.refreshCopyHelper();
//
//						if ("A".equals(userAcBean.getRegisterType())) {
//							Enumeration enmAddressList = (new AddressAccessBean())
//									.findByMemberId(childMemberId);
//							while (enmAddressList.hasMoreElements()) {
//								AddressAccessBean addressbean = (AddressAccessBean) enmAddressList
//										.nextElement();
//
//								if ("B".equals(addressbean.getAddressType())) {
//									XaddressRelationAccessBean xAddrBean1 = new XaddressRelationAccessBean();
//									Enumeration xAddrEnum = xAddrBean1
//											.findbyBillToId(Long.valueOf(addressbean.getNickName()));
//									while (xAddrEnum.hasMoreElements()) {
//										
//										if(addressCount!=0){
//											addressJSON.append(",");
//										}
//										XaddressRelationAccessBean xAddrBean = (XaddressRelationAccessBean) xAddrEnum.nextElement();
//										DomtarAddress domAdd = new DomtarAddress();
//										domAdd.setSoldTo(xAddrBean.getBillToId().toString());
//										domAdd.setSoldToName(xAddrBean.getBilltoName());
//										domAdd.setShipTo(xAddrBean.getShipToId().toString());
//										domAdd.setShipToName(xAddrBean.getShiptoName());
//										domAdd.setRrc(xAddrBean.getRrcId().toString());
//										domAdd.setRrcName(xAddrBean.getRrcName());
//										addressList.add(domAdd);
//										
//										addressJSON.append("{");
//										addressJSON.append("\"SoldTo\":\""+xAddrBean.getBillToId().toString()+"\",");
//										addressJSON.append("\"SoldToName\":\""+xAddrBean.getBilltoName()+"\",");
//										addressJSON.append("\"ShipTo\":\""+xAddrBean.getShipToId().toString()+"\",");
//										addressJSON.append("\"ShipToName\":\""+xAddrBean.getShiptoName()+"\",");
//										addressJSON.append("\"rrc\":\""+xAddrBean.getRrcId().toString()+"\",");
//										addressJSON.append("\"rrcName\":\""+xAddrBean.getRrcName()+"\"");
//										addressJSON.append("}");
//										
//										addressCount++;
//									}
//								}
//							}
//						}
//					}
//				}
//			}
			
		} catch (ECSystemException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		addressJSON.append("]}");
		addressRelationJson = addressJSON.toString();
		return addressList;
	}
	 private String populateRRCDetailsForStockCheck()
			throws ECApplicationException {

		String strMethodName = "populateRRCDetailsForStcockCheck";
		StringBuffer jsononCountry = new StringBuffer();
		try {
			String parentMemberId = null;

			EntitlementContext ctx = (EntitlementContext) getCommandContext()
					.getContext(
							"com.ibm.commerce.context.entitlement.EntitlementContext");
			if (ctx != null) {
				parentMemberId = ctx.getActiveOrganizationId().toString();
			} else {
				parentMemberId = getCommandContext().getUser().getParentMemberId();
			}
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
			queryParams[0] = parentMemberId;

			databaseRows = jdbcHelper.executeParameterizedQuery(queryBuilder.toString(),queryParams);
			if(databaseRows != null && databaseRows.size()>0){
				for (Object obj : databaseRows) {
					Vector dbRow = (Vector) obj;
					String addressType = ((String) dbRow.get(0)).trim();
					String nickName = ((String) dbRow.get(1)).trim();
					String country = ((String) dbRow.get(2)).trim();
					String state = ((String) dbRow.get(3)).trim();
					String city = ((String) dbRow.get(4)).trim();
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
								jsononCountry.append("\"").append(rrcEntry.getKey())
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
			throw new
			 ECApplicationException(ECMessage._ERR_BAD_CMD_PARAMETER,
			 this.getClass().getName(), strMethodName,"");
		}
		return jsononCountry.toString();
	}
	public void validateParameters() throws ECException {
		 
	 }
}
