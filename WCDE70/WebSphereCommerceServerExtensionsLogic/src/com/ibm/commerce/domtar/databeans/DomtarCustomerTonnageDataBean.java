package com.ibm.commerce.domtar.databeans;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;

import com.ibm.commerce.base.objects.ServerJDBCHelperAccessBean;
import com.ibm.commerce.beans.InputDataBean;
import com.ibm.commerce.beans.SmartDataBean;
import com.ibm.commerce.catalog.objects.AttributeAccessBean;
import com.ibm.commerce.catalog.objects.AttributeValueAccessBean;
import com.ibm.commerce.catalog.objects.CatalogEntryDescriptionAccessBean;
import com.ibm.commerce.command.CommandContext;
import com.ibm.commerce.context.entitlement.EntitlementContext;
import com.ibm.commerce.datatype.TypedProperty;
import com.ibm.commerce.domtar.helpers.DomtarHelper;
import com.ibm.commerce.domtar.util.DomtarJDBCHelper;
import com.ibm.commerce.exception.ECSystemException;
import com.ibm.commerce.extension.objects.XorderItemsAccessBean;
import com.ibm.commerce.order.objects.OrderAccessBean;
import com.ibm.commerce.user.objects.OrganizationAccessBean;

//TimestampHelper

public class DomtarCustomerTonnageDataBean extends XorderItemsAccessBean
		implements SmartDataBean, InputDataBean {
	
	private static final String DEFAULT_SORTING = "shipDate";
	private static final String SORT_BY_SALESORDER = "salesOrder";
	private static final String SORT_BY_CUSTOMERPO = "customerPO";
	private String sorting;

	String frmShipDate = null;
	String toShipDate = null;
	String country = null;
	String state = null;
	String city = null;
	String shipTo = null;
	String soldTo = null;
	String customer = null;
	String PlannedShipDate = null;
	String customerPO = null;
	String domtarSalesOrdNum = null;
	String itemDesc = null;
	String itemCode = null;
	String[] atrributes = null;
	Double quantity = null;
	String hotOrder=null;
	Timestamp requestedShipDate = null;
	BigDecimal totalWeight = null;
	Timestamp frmShipDateFormatted = null;
	Timestamp toShipDateFormatted = null;
	String sqlStatement = null;
	LinkedHashMap<String, List> customerTonnageMap = new LinkedHashMap<String, List>();
	List<Double> totalTonnageForPeriodList = new ArrayList<Double>();
	List<String> reqShipDateList = new ArrayList<String>();
	Set<String> uniqueReqShipDateSet = new HashSet<String>(reqShipDateList);
	List<CustomerTonnageBean> customerTonnageList = new ArrayList<CustomerTonnageBean>();
	private List<DomtarAddress> addressList;
	HashMap<HashSet<String>, HashSet<String>> addressRelationMap = new HashMap<HashSet<String>, HashSet<String>>();
	private int num = 0;
	private CommandContext commandContext = null;
	private DateFormat dateFormat = new SimpleDateFormat(
			"MM/dd/yyyy hh:mm:ss.SSS");
	private String customerName = null;

	double totalTonnageForPeriod = 0;
	double totalTonnageForPage = 0;
	int beginIndex = 0;
	int pageSize = 3;
	private boolean isSearchResultExists = false;
	
	private int numRecords = 0;
	

	/**
	 * @return the numRecords
	 */
	public int getNumRecords() {
		return numRecords;
	}

	/**
	 * @param numRecords the numRecords to set
	 */
	public void setNumRecords(int numRecords) {
		this.numRecords = numRecords;
	}

	@Override
	public void populate() throws Exception {
		long t1 = System.currentTimeMillis();
		populateUserAddressList();
		long t2 = System.currentTimeMillis();
		System.out.println("Time taken to populate UserAddressList is : " + (t2-t1));
		setNumRecords(getNumberofRecords());
		getResultRecords();
	}

	@Override
	/*
	 * * This method sets the request properties for the data bean
	 */
	public void setRequestProperties(TypedProperty requestProperties)
			throws Exception {

		frmShipDate = requestProperties.getString("frmshipdate", "");
		toShipDate = requestProperties.getString("toshipdate", "");
		country = requestProperties.getString("country", "");
		state = requestProperties.getString("state", "");
		city = requestProperties.getString("city", "");
		shipTo = requestProperties.getString("shipTo", "");
		soldTo = requestProperties.getString("soldTo", "");
		beginIndex = new Integer(requestProperties.getString("beginIndex", "0")).intValue();
		setSorting(requestProperties.getString("sortBy", DEFAULT_SORTING));
	}

	/**
	 * This method gives us the number of records which will be dispalyed in
	 * Customer Tonnage Screen
	 */

	private int getNumberofRecords() {

		// getFormattedDate();

		String sqlStatement = "SELECT T2.REQUESTEDSHIPDATE FROM  "
				+ " ORDERS T1, ORDERITEMS T2, ADDRESS T3, XORDITEMS T4  "
				+ "WHERE T1.ORDERS_ID=T2.ORDERS_ID  AND T3.ADDRESS_ID=T2.FIELD1 " +
						"AND T2.ORDERITEMS_ID=T4.ORDERITEMS_ID " +
						"AND T3.STATUS ='P' AND T2.STATUS = 'I' AND T4.INCLUDEINTONNAGE='Y'";
		num = 0;
		if (!StringUtils.isEmpty(frmShipDate)
				&& !StringUtils.isEmpty(toShipDate)) {
			sqlStatement += "  AND T2.REQUESTEDSHIPDATE   >= ? AND T2.REQUESTEDSHIPDATE     <= ? ";
			num += 2;
		}

		if (shipTo != null && !"".equals(shipTo)) {
			sqlStatement += " AND T3.NICKNAME = ? ";
			num += 1;
		} else {
			sqlStatement += " AND T3.NICKNAME IN (";
			
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
			
//			for (Iterator iterator = addressList.iterator(); iterator.hasNext();) {
//				DomtarAddress object = (DomtarAddress) iterator.next();
//				sqlStatement += "'" + object.getShipTo() + "'";
//				if (iterator.hasNext()) {
//					sqlStatement += ", ";
//				}
//			}
			sqlStatement += ")";
		}

		if (soldTo != null && !"".equals(soldTo)) {
			sqlStatement += " AND T1.FIELD1= ? ";
			num += 1;

		} else {
			sqlStatement += " AND T1.FIELD1 IN (";
			
			
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
//			for (Iterator iterator = addressList.iterator(); iterator.hasNext();) {
//				DomtarAddress object = (DomtarAddress) iterator.next();
//				sqlStatement += object.getSoldTo();
//				if (iterator.hasNext()) {
//					sqlStatement += ", ";
//				}
//			}
			sqlStatement += ")";
		}
		
		if(country != null  && !"".equals(country)) {
			sqlStatement +=" AND T4.SOURCECOUNTRY = ? " ;
			num+=1;
		}
		if(state != null  && !"".equals(state)) {
			sqlStatement +=" AND T4.SOURCESTATE = ? " ;
			num+=1;
		}
		if(city != null  && !"".equals(city)) {
			sqlStatement +=" AND T4.SOURCECITY = ? " ;
			num+=1;
		}
		
		if(sorting.equals(SORT_BY_SALESORDER)){
			sqlStatement +=" ORDER BY T4.SALESORDER_ID ASC" ;
		}else if (sorting.equals(SORT_BY_CUSTOMERPO)) {
			sqlStatement +=" ORDER BY T1.FIELD3 ASC" ;
		}else {
			sqlStatement += " ORDER BY  T2.REQUESTEDSHIPDATE ASC";
		}		
		
		//System.out.println(sqlStatement);

		Integer numRecords = null;
		Timestamp[] timeStampArray = new Timestamp[num];

		Object[] queryParams = new Object[num];
		int paramCount = 0;
		if (frmShipDate != null && !StringUtils.isEmpty(frmShipDate)) {
			frmShipDateFormatted = convertStringtoTimestamp(frmShipDate
					+ " 00:00:00.000");
			queryParams[paramCount] = frmShipDateFormatted;
			paramCount += 1;
		}
		if (toShipDate != null && !StringUtils.isEmpty(toShipDate)) {
			toShipDateFormatted = convertStringtoTimestamp(toShipDate
					+ " 23:59:59.000");
			queryParams[paramCount] = toShipDateFormatted;
			paramCount += 1;
		}
		if (shipTo != null && !"".equals(shipTo)) {
			queryParams[paramCount] = getShipTo();
			paramCount += 1;
		}
		if (soldTo != null && !"".equals(soldTo)) {
			queryParams[paramCount] = getSoldTo();
			paramCount += 1;
		}
		if(country != null  && !"".equals(country)) {
			queryParams[paramCount] = getCountry();
			paramCount += 1;
		}
		if(state != null  && !"".equals(state)) {
			queryParams[paramCount] = getState();
			paramCount += 1;
		}
		if(city != null  && !"".equals(city)) {
			queryParams[paramCount] = getCity();
			paramCount += 1;
		}

		Vector databaseRows = new Vector();
		ServerJDBCHelperAccessBean jdbcHelper = new ServerJDBCHelperAccessBean();
		try {
			databaseRows = jdbcHelper.executeParameterizedQuery(sqlStatement,
					queryParams);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (Object obj : databaseRows) {

			Vector EachRow = (Vector) obj;

			Timestamp Date = (Timestamp) EachRow.get(0);

			if (Date!= null && !reqShipDateList.contains(Date)) {

				reqShipDateList.add(Date.toString());
			}

		}

		uniqueReqShipDateSet = new HashSet<String>(reqShipDateList);

		if (uniqueReqShipDateSet != null && uniqueReqShipDateSet.size() > 0) {
			return uniqueReqShipDateSet.size();
		} else {
			return 0;
		}
	}

	private Timestamp convertStringtoTimestamp(String pDate) {
		Timestamp vformattedDate = null;
		try {
			Date vParsedDate = dateFormat.parse(pDate);
			vformattedDate = new Timestamp(vParsedDate.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return vformattedDate;
	}

	/**
	 * This method get the date in a format in which it wil be displyed in
	 * Customer Tonnage Screen.
	 */
	public String getDateFormatted(String date) {
		String formattedDate = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date dateStr = formatter.parse(date);
			formattedDate = formatter.format(dateStr);
			Date dateFormated = formatter.parse(formattedDate);
			formatter = new SimpleDateFormat("MM/dd/yyyy");
			formattedDate = formatter.format(dateFormated);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return formattedDate;

	}

	/**
	 *This method query the database and get us the results.
	 */

	public void getResultRecords() {

		try {

			String requestShipDate = null;
			String[] shipDateArray = uniqueReqShipDateSet
					.toArray(new String[uniqueReqShipDateSet.size()]);
			Arrays.sort(shipDateArray);

			for (int count = 0; count < shipDateArray.length; count++) {
				String reqShipDate = shipDateArray[count].toString();

				String sqlStatement = "SELECT T2.ORDERITEMS_ID,T2.CATENTRY_ID,T2.QUANTITY,T2.PARTNUM,T2.REQUESTEDSHIPDATE  FROM  "
						+ " ORDERS T1, ORDERITEMS T2, ADDRESS T3, XORDITEMS T4  "
						+ "WHERE T1.ORDERS_ID=T2.ORDERS_ID  AND T3.ADDRESS_ID=T2.FIELD1 " +
								"AND T2.ORDERITEMS_ID=T4.ORDERITEMS_ID  AND  T3.STATUS ='P' " +
								"AND T2.STATUS = 'I' AND T4.INCLUDEINTONNAGE='Y'";

				num = 0;
				if (!StringUtils.isEmpty(frmShipDate)
						&& !StringUtils.isEmpty(toShipDate)) {
					sqlStatement += "  AND T2.REQUESTEDSHIPDATE  = ? ";
					num += 1;
				}

				if (shipTo != null && !"".equals(shipTo)) {
					sqlStatement += " AND T3.NICKNAME = ? ";
					num += 1;
				} else {
					sqlStatement += " AND T3.NICKNAME IN (";
					
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
					
//					for (Iterator iterator = addressList.iterator(); iterator
//							.hasNext();) {
//						DomtarAddress object = (DomtarAddress) iterator.next();
//						sqlStatement += "'" + object.getShipTo() + "'";
//						if (iterator.hasNext()) {
//							sqlStatement += ", ";
//						}
//					}
					sqlStatement += ")";
				}

				if (soldTo != null && !"".equals(soldTo)) {
					sqlStatement += " AND T1.FIELD1= ? ";
					num += 1;

				} else {
					sqlStatement += " AND T1.FIELD1 IN (";
					
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
					
//					for (Iterator iterator = addressList.iterator(); iterator
//							.hasNext();) {
//						DomtarAddress object = (DomtarAddress) iterator.next();
//						sqlStatement += object.getSoldTo();
//						if (iterator.hasNext()) {
//							sqlStatement += ", ";
//						}
//					}
					sqlStatement += ")";
				}
				if(country != null  && !"".equals(country)) {
					sqlStatement +=" AND T4.SOURCECOUNTRY = ? " ;
					num+=1;
				}
				if(state != null  && !"".equals(state)) {
					sqlStatement +=" AND T4.SOURCESTATE = ? " ;
					num+=1;
				}
				if(city != null  && !"".equals(city)) {
					sqlStatement +=" AND T4.SOURCECITY = ? " ;
					num+=1;
				}

				if(sorting.equals(SORT_BY_SALESORDER)){
					sqlStatement +=" ORDER BY T4.SALESORDER_ID ASC" ;
				}else if (sorting.equals(SORT_BY_CUSTOMERPO)) {
					sqlStatement +=" ORDER BY T1.FIELD3 ASC" ;
				}else {
					sqlStatement += " ORDER BY  T2.REQUESTEDSHIPDATE ASC";
				}

				Object[] queryParams = new Object[num];
				int paramCount = 0;
				if (reqShipDate != null && !StringUtils.isEmpty(reqShipDate)) {
					queryParams[paramCount] = reqShipDate;
					paramCount += 1;
				}
				if (shipTo != null && !"".equals(shipTo)) {
					queryParams[paramCount] = getShipTo();
					paramCount += 1;
				}
				if (soldTo != null && !"".equals(soldTo)) {
					queryParams[paramCount] = getSoldTo();
					paramCount += 1;
				}
				if(country != null  && !"".equals(country)) {
					queryParams[paramCount] = getCountry();
					paramCount += 1;
				}
				if(state != null  && !"".equals(state)) {
					queryParams[paramCount] = getState();
					paramCount += 1;
				}
				if(city != null  && !"".equals(city)) {
					queryParams[paramCount] = getCity();
					paramCount += 1;
				}

				ServerJDBCHelperAccessBean jdbcHelper = new ServerJDBCHelperAccessBean();
				Vector databaseRows = new Vector();
				Long orderItemId = null;
				Long catentryId = null;
				Long orderId = null;

				List<CustomerTonnageBean> customerTonnageList = new ArrayList<CustomerTonnageBean>();
				databaseRows = jdbcHelper.executeParameterizedQuery(sqlStatement, queryParams);
				for (Object obj : databaseRows) {
					setSearchResultExists(true);
					CustomerTonnageBean customerTonnageBean = new CustomerTonnageBean();
					Vector EachRow = (Vector) obj;
					orderItemId = (Long) EachRow.get(0);
					catentryId = (Long) EachRow.get(1);
					quantity = (Double) EachRow.get(2);
					itemCode = (String) EachRow.get(3);
					requestedShipDate = (Timestamp) EachRow.get(4);

					CatalogEntryDescriptionAccessBean catalogEntryDescriptionAccessBean = new CatalogEntryDescriptionAccessBean();
					catalogEntryDescriptionAccessBean.setInitKey_catalogEntryReferenceNumber(catentryId.toString());
					catalogEntryDescriptionAccessBean.setInitKey_language_id(Integer.toString(-1));
					catalogEntryDescriptionAccessBean.refreshCopyHelper();
					itemDesc = catalogEntryDescriptionAccessBean.getShortDescription();

					AttributeAccessBean attributeAccessBean = new AttributeAccessBean();
					AttributeValueAccessBean[] attributeStringValueAccessBean = null;
					Enumeration enumeration = attributeAccessBean.findByCatalogEntryId(new Long(catentryId));
					while (enumeration.hasMoreElements()) {
						attributeAccessBean = (AttributeAccessBean) enumeration.nextElement();
						attributeAccessBean.getAttributeReferenceNumber();
						attributeStringValueAccessBean = attributeAccessBean.getAttributeValues();
						for (int iCount = 0; iCount < attributeStringValueAccessBean.length; iCount++) {
							AttributeValueAccessBean attrval = attributeStringValueAccessBean[iCount];

						}
					}
					String weightType=null;
					XorderItemsAccessBean xOrderItemsAccessBean = new XorderItemsAccessBean();
					Enumeration orderItemEnum = xOrderItemsAccessBean.findByOrderItemId(new Long(orderItemId));
					while (orderItemEnum.hasMoreElements()) {
						xOrderItemsAccessBean = (XorderItemsAccessBean) orderItemEnum.nextElement();
						domtarSalesOrdNum = xOrderItemsAccessBean.getSalesOrderId();
						totalWeight = xOrderItemsAccessBean.getTotalWeight();
						orderId = xOrderItemsAccessBean.getOrdersId();
						OrderAccessBean oderAccessBean = new OrderAccessBean();
						oderAccessBean.setInitKey_orderId(orderId.toString());
						oderAccessBean.refreshCopyHelper();
						customerPO = oderAccessBean.getField3();
						weightType = xOrderItemsAccessBean.getWeighType();
						hotOrder=xOrderItemsAccessBean.getHotOrder();
					}

					customerTonnageBean.setItemCode(itemCode);
					customerTonnageBean.setItemDesc(itemDesc);
					customerTonnageBean.setCustomerPo(customerPO);
					customerTonnageBean.setDomtarSalesOrdNum(domtarSalesOrdNum);
					if (totalWeight != null) {
						customerTonnageBean.setTotalWeight(totalWeight.toString());
					}
					if (hotOrder != null) {
						customerTonnageBean.setHotOrder(hotOrder.toString());
					}
					if (quantity != null) {
						customerTonnageBean.setQuantity(quantity.toString());
					}
					if(weightType != null){
						String[] uom = weightType.split("\\*\\*#\\*\\*");	
						if(uom.length > 1 && null != uom[1]){
							customerTonnageBean.setWeightType(uom[1]);
						}
					}
					if (orderId != null) {
						customerTonnageBean.setOrderId(orderId.toString());
					}
					if (reqShipDate != null) {
						requestShipDate = getDateFormatted(reqShipDate.toString());
						customerTonnageBean.setRequestedShipDate(requestShipDate);
					}

					if (count >= new Integer(beginIndex).intValue()
							&& count < (new Integer(beginIndex).intValue() + pageSize) && totalWeight != null) {
						totalTonnageForPeriod += totalWeight.doubleValue();

					}

					customerTonnageList.add(customerTonnageBean);
				}

				totalTonnageForPeriodList.add(totalTonnageForPeriod);
				customerTonnageMap.put(requestShipDate, customerTonnageList);
			}

		}

		catch (RemoteException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}

	}

	private void populateUserAddressList() {

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
			
			//addressList = new ArrayList<DomtarAddress>();
			
			String userMemberId = commandcontext.getUser().getParentMemberId();
			OrganizationAccessBean organizationAccessBean = new OrganizationAccessBean();
			organizationAccessBean.setInitKey_MemberId(userMemberId);
			organizationAccessBean.refreshCopyHelper();
			customerName = organizationAccessBean.getAdministratorLastName();
			
			DomtarJDBCHelper domtarHelper = new DomtarJDBCHelper();
			try {
				addressList = domtarHelper.findSoldToRRCShipToAddressByOrganizationId(parentMemberId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
//			HashMap<String, List<String>> addressRelationMap = new HashMap<String, List<String>>();
//			addressRelationMap = DomtarHelper.convertUserAddressListToMap(addressList);
			
			
			addressRelationMap = DomtarHelper.convertUserAddressListToMap(addressList);
//			MemberAccessBean mAb = new MemberAccessBean();
//			mAb.setInitKey_MemberId(parentMemberId);
//			mAb.refreshCopyHelper();			
//
//			// System.out.println("Parent  : " + parentMemberId
//			// +"  "+mAb.getType());
//			
//			if (mAb.getDescendants().length > 0) {
//				for (int i = 0; i < mAb.getDescendants().length; i++) {
//					Long childMemberId = mAb.getDescendants()[i];
//					MemberAccessBean mAb1 = new MemberAccessBean();
//					mAb1.setInitKey_MemberId(childMemberId.toString());
//					mAb1.refreshCopyHelper();
//					if (mAb1.getType().equals("UserBean")) {
//						UserAccessBean userAcBean = new UserAccessBean();
//						// System.out.println("Setting registerType1: " +
//						// childMemberId);
//						userAcBean.setInitKey_MemberId(childMemberId.toString());
//						userAcBean.refreshCopyHelper();
//
//						if ("A".equals(userAcBean.getRegisterType())) {
//							Enumeration enmAddressList = (new AddressAccessBean())
//									.findByMemberId(childMemberId);
//							while (enmAddressList.hasMoreElements()) {
//								AddressAccessBean addressbean = (AddressAccessBean) enmAddressList
//										.nextElement();
//								// System.out.println("AddressType: " +
//								// addressbean.getAddressType());
//								if ("B".equals(addressbean.getAddressType())) {
//									XaddressRelationAccessBean xAddrBean1 = new XaddressRelationAccessBean();
//									Enumeration xAddrEnum = xAddrBean1
//											.findbyBillToId(Long
//													.valueOf(addressbean
//															.getNickName()));
//									while (xAddrEnum.hasMoreElements()) {
//										XaddressRelationAccessBean xAddrBean = (XaddressRelationAccessBean) xAddrEnum
//												.nextElement();
//										DomtarAddress domAdd = new DomtarAddress();
//										domAdd.setSoldTo(xAddrBean.getBillToId().toString());
//										domAdd.setSoldToName(xAddrBean.getBilltoName());
//										domAdd.setShipTo(xAddrBean.getShipToId().toString());
//										domAdd.setShipToName(xAddrBean.getShiptoName());
//										domAdd.setRrc(xAddrBean.getRrcId().toString());
//										domAdd.setRrcName(xAddrBean.getRrcName());
//										// System.out.println(xAddrBean.getBillToId()+" "+xAddrBean.getBilltoName()+" "+xAddrBean.getShipToId()+" "+xAddrBean.getShiptoName()+" "+xAddrBean.getRrcId()+" "+xAddrBean.getRrcName());
//										addressList.add(domAdd);
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

	}

	public boolean isSearchResultExists() {
		return isSearchResultExists;
	}

	public void setSearchResultExists(boolean isSearchResultExists) {
		this.isSearchResultExists = isSearchResultExists;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public List getAddressList() {
		return addressList;
	}

	public void setAddressList(List addressList) {
		this.addressList = addressList;
	}

	public LinkedHashMap<String, List> getCustomerTonnageMap() {
		return customerTonnageMap;
	}

	public void setCustomerTonnageMap(
			LinkedHashMap<String, List> customerTonnageMap) {
		this.customerTonnageMap = customerTonnageMap;
	}

	/**
	 * This method is getting called when we invoke the databean
	 */

	public int getBeginIndex() {
		return beginIndex;
	}

	public void setBeginIndex(int beginIndex) {
		this.beginIndex = beginIndex;
	}

	public double getTotalTonnageForPeriod() {
		return totalTonnageForPeriod;
	}

	public void setTotalTonnageForPeriod(double totalTonnageForPeriod) {
		this.totalTonnageForPeriod = totalTonnageForPeriod;
	}

	public void setTotalTonnageForPage(double totalTonnageForPage) {
		this.totalTonnageForPage = totalTonnageForPage;
	}

	public Timestamp getRequestedShipDate() {
		return requestedShipDate;
	}

	public void setRequestedShipDate(Timestamp requestedShipDate) {
		this.requestedShipDate = requestedShipDate;
	}

	public List<CustomerTonnageBean> getCustomerTonnageList() {
		return customerTonnageList;
	}

	public void setCustomerTonnageList(
			List<CustomerTonnageBean> customerTonnageList) {
		this.customerTonnageList = customerTonnageList;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getShipTo() {
		return shipTo;
	}

	public void setShipTo(String shipTo) {
		this.shipTo = shipTo;
	}

	public String getSoldTo() {
		return soldTo;
	}

	public void setSoldTo(String soldTo) {
		this.soldTo = soldTo;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getPlannedShipDate() {
		return PlannedShipDate;
	}

	public void setPlannedShipDate(String plannedShipDate) {
		PlannedShipDate = plannedShipDate;
	}

	public String getDomtarSalesOrdNum() {
		return domtarSalesOrdNum;
	}

	public void setDomtarSalesOrdNum(String domtarSalesOrdNum) {
		this.domtarSalesOrdNum = domtarSalesOrdNum;
	}

	public String getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String[] getAtrributes() {
		return atrributes;
	}

	public void setAtrributes(String[] atrributes) {
		this.atrributes = atrributes;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public void setTotalWeight(BigDecimal totalWeight) {
		this.totalWeight = totalWeight;
	}

	public String getCustomerPO() {
		return customerPO;
	}

	public void setCustomerPO(String customerPO) {
		this.customerPO = customerPO;
	}

	public BigDecimal getTotalWeight() {
		return totalWeight;
	}

	public String getFrmShipdate() {
		return frmShipDate;
	}

	public void setFrmShipdate(String frmShipdate) {
		this.frmShipDate = frmShipdate;
	}

	public String getToShipdate() {
		return toShipDate;
	}

	public void setToShipdate(String toShipdate) {
		this.toShipDate = toShipdate;
	}
	
	public String getSorting() {
		return sorting;
	}

	public void setSorting(String sorting) {
		this.sorting = sorting;
	}
	
	@Override
	public TypedProperty getRequestProperties() {
		return null;
	}

	@Override
	public CommandContext getCommandContext() {

		return commandContext;
	}

	@Override
	public void setCommandContext(CommandContext arg0) {

		commandContext = arg0;

	}

	/**
	 * @return the hotOrder
	 */
	public String getHotOrder() {
		return hotOrder;
	}

	/**
	 * @param hotOrder the hotOrder to set
	 */
	public void setHotOrder(String hotOrder) {
		this.hotOrder = hotOrder;
	}

}
