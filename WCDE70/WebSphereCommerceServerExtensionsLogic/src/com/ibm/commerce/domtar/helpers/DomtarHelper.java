package com.ibm.commerce.domtar.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

import com.ibm.commerce.domtar.databeans.DomtarAddress;
import com.ibm.commerce.foundation.common.util.logging.LoggingHelper;
import com.ibm.commerce.user.objects.UserRegistryAccessBean;

public class DomtarHelper {
	
	private static final Logger LOGGER = LoggingHelper.getLogger(DomtarHelper.class);
	
	public static String getLogonIdByUserId(String userId){
		
		UserRegistryAccessBean userRegistry = new UserRegistryAccessBean();
		userRegistry.setInitKey_UserId(userId);
		String requestorLogonId="";
		try {
			requestorLogonId = userRegistry.getLogonId();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("Error in getting LogonId for User "+userId+". Error is : "+ e.getMessage());
		}

		return requestorLogonId;
	}

	public static HashMap<HashSet<String>, HashSet<String>> convertUserAddressListToMap(
			List<DomtarAddress> addressList) {
		HashMap<HashSet<String>, HashSet<String>> addressRelationMap = new HashMap<HashSet<String>, HashSet<String>>();
		if (addressList.size()==0){
			return addressRelationMap;
		}
		HashSet<String> soldToSet = new HashSet<String>();
		HashSet<String> shipToSet = new HashSet<String>();
		for (DomtarAddress domtarAddress : addressList) {
			soldToSet.add(domtarAddress.getSoldTo());
			shipToSet.add(domtarAddress.getShipTo());			
		}
		addressRelationMap.put(soldToSet, shipToSet);
		return addressRelationMap;
	}

//	public static HashMap<String, List<String>> convertUserAddressListToMap(
//			List<DomtarAddress> addressList) {
//		HashMap<String, List<String>> addressRelationMap = new HashMap<String, List<String>>();
//		if (addressList == null){
//			return addressRelationMap;
//		}
//		List<String> shipToList = null;
//		for (DomtarAddress domtarAddress : addressList) {
//			String soldTo = domtarAddress.getSoldTo();
//			String shipTo = domtarAddress.getShipTo();
//			if(addressRelationMap.containsKey(soldTo)){
//				shipToList = addressRelationMap.get(soldTo);
//				shipToList.add(shipTo);
//				addressRelationMap.put(soldTo, shipToList);
//			}else{
//				shipToList = new ArrayList<String>();
//				shipToList.add(shipTo);
//				addressRelationMap.put(soldTo, shipToList);				
//			}
//		}
//		return addressRelationMap;
//	}

}
