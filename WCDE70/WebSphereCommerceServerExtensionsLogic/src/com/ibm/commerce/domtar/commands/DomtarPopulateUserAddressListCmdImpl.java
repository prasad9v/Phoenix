package com.ibm.commerce.domtar.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.json.JSON;
import org.apache.commons.json.JSONArtifact;

import com.ibm.commerce.domtar.databeans.DomtarAddress;
import com.ibm.commerce.domtar.util.DomtarJDBCHelper;
import com.ibm.commerce.exception.ExceptionHandler;
import com.ibm.websphere.command.CacheableCommandImpl;

public class DomtarPopulateUserAddressListCmdImpl extends CacheableCommandImpl
		implements DomtarPopulateUserAddressListCmd {
	
	private String storeId;
	private String languageId;
	private String organizationId;
	private List<DomtarAddress> userAddressList;
	private JSONArtifact addressRelationJsonArtifact;
	
	

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
	 * @return the userAddressList
	 */
	public List<DomtarAddress> getUserAddressList() {
		return userAddressList;
	}

	/**
	 * @param userAddressList the userAddressList to set
	 */
	public void setUserAddressList(List<DomtarAddress> userAddressList) {
		this.userAddressList = userAddressList;
	}

	/**
	 * @return the addressRelationJsonArtifact
	 */
	public JSONArtifact getAddressRelationJsonArtifact() {
		return addressRelationJsonArtifact;
	}

	/**
	 * @param addressRelationJsonArtifact the addressRelationJsonArtifact to set
	 */
	public void setAddressRelationJsonArtifact(
			JSONArtifact addressRelationJsonArtifact) {
		this.addressRelationJsonArtifact = addressRelationJsonArtifact;
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
		
		List addressList = new ArrayList<DomtarAddress>();
		StringBuffer addressJSON = new StringBuffer("{ \"xAddrRel\" : [") ;
		try {

//			MemberAccessBean mAb = new MemberAccessBean();
//			mAb.setInitKey_MemberId(parentMemberId);
//			mAb.refreshCopyHelper();

			int addressCount=0;
			
			
			DomtarJDBCHelper domtarHelper = new DomtarJDBCHelper();
			try {
				addressList = domtarHelper.findSoldToRRCShipToAddressByOrganizationId(getOrganizationId());
				//display drop down in sorted order
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
					addressJSON.append("\"ShipToName\":\""+domtarAddress.getShipToName()+"\"");
					//addressJSON.append("\"rrc\":\""+domtarAddress.getRrc()+"\",");
					//addressJSON.append("\"rrcName\":\""+domtarAddress.getRrcName()+"\"");
					addressJSON.append("}");
					
					addressCount++;
				}
				setUserAddressList(addressList);
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
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		addressJSON.append("]}");

		try {
			if(!addressJSON.toString().equals("")){
				setAddressRelationJsonArtifact(JSON.parse(addressJSON.toString()));
			}
		} catch (Exception e) {
			ExceptionHandler.convertToECException(e);
		} 		
		long vExitPoint = System.currentTimeMillis();
		long TotCallDiff = (vExitPoint-vEntryPoint);
		System.out.println("Time taken for creating address relation JSON is >>>>>>"+TotCallDiff+" ms");
	}

}
