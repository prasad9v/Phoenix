package com.ibm.commerce.domtar.databeans;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
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
import com.ibm.commerce.command.CommandContext;
import com.ibm.commerce.context.entitlement.EntitlementContext;
import com.ibm.commerce.datatype.TypedProperty;
import com.ibm.commerce.domtar.helpers.DomtarHelper;
import com.ibm.commerce.domtar.util.DomtarJDBCHelper;
import com.ibm.commerce.exception.ECSystemException;
import com.ibm.commerce.extension.objects.XorderItemsAccessBean;

public class DomtarOrderStatusOrdSrchDataBean extends XorderItemsAccessBean
implements SmartDataBean, InputDataBean {

	public static final String COPYRIGHT = com.ibm.commerce.copyright.IBMCopyright.SHORT_COPYRIGHT;

	private String orderLineItemId = null;
	private CommandContext iCommandContext = null;
	private TypedProperty requestProperties;
	private Integer orderLineItem_Id;
	private Long orderId;
	private Long orderItemId;
	private String salesOrderId;
	private String ordStatus;
	private String ordStatE = "E";
	private String ordStatI = "I";
	private String ordStatR = "R";
	private String ordStatC = "C";
	private String ordStatX = "X";
	private String itemStatus;
	private String itemStatE = "E";
	private String itemStatI = "I";
	private String itemStatR = "R";
	private String itemStatC = "C";
	private String itemStatX = "X";
	private String sortBy;
	private Timestamp estimateDeliveryDate;
	private String partNum;
	private String quantity;
	private String address;
	private String customerPO;
	private Timestamp timePlaced;
	private int num = 0;
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private DateFormat datFormat = new SimpleDateFormat("MM/dd/yyyy");
	private java.lang.String formatedTimeplaced;
	private String sqlStatement = null;
	private Object[] queryParams = null;
	private List<DomtarOrderStatusOrdSrchDataBean> paginatedOrderStatusOrdList = new ArrayList<DomtarOrderStatusOrdSrchDataBean>();
	private String frmShipDate;
	private String toShipDate;
	private String frmOrdrDate;
	private String toOrdrDate;
	private int beginIndex;
	private int pageSize;
	private int endIndex;
	private long fromODate = 0;
	private long fromShipDate = 0;
	private Timestamp fOrdDate;
	private Timestamp tOrdDate;
	private Timestamp fShipDate;
	private Timestamp tShipDate;
	private List addressList;
	private String soldTo;
	private String shipTo;
	private DateFormat timeStampDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss.SSS");
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
		try {
			populateUserAddressList();
			setNumRecords(getNumberOfRecords());
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	@Override
	public void setRequestProperties(TypedProperty requestProperties)
	throws Exception {
		soldTo = requestProperties.getString("soldTo", "");
		shipTo = requestProperties.getString("shipTo", "");
		ordStatus = requestProperties.getString("ordStatus", "");
		itemStatus = requestProperties.getString("itemStatus", "");
		customerPO = requestProperties.getString("custPo", "");
		salesOrderId = requestProperties.getString("salesOrder", "");
		partNum = requestProperties.getString("partNum", "");
		sortBy = requestProperties.getString("sortBy", "");
		frmShipDate = requestProperties.getString("frmShipDate", "");
		toShipDate = requestProperties.getString("toShipDate", "");
		frmOrdrDate = requestProperties.getString("frmOrdrDate", "");
		toOrdrDate = requestProperties.getString("toOrdrDate", "");
		if (!StringUtils.isEmpty(requestProperties.getString("beginIndex", ""))) {
			beginIndex = Integer.parseInt(requestProperties.getString(
					"beginIndex", ""));
		}

		pageSize = Integer.parseInt(requestProperties.getString("pageSize","10"));

	}

	private List<DomtarOrderStatusOrdSrchDataBean> correctForPageInation(

			Set<DomtarOrderStatusOrdSrchDataBean> orderStatusOrdSet) {
		List<DomtarOrderStatusOrdSrchDataBean> orderStatusOrdList = new ArrayList<DomtarOrderStatusOrdSrchDataBean>(
				orderStatusOrdSet);
		for (int i = 0; i < orderStatusOrdList.size(); i++) {
			if (i >= beginIndex && i < (beginIndex + pageSize)) {
				paginatedOrderStatusOrdList.add(orderStatusOrdList.get(i));
			}
		}
		return paginatedOrderStatusOrdList;
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

	public List getAddressList() {
		return addressList;
	}

	public void setAddressList(List addressList) {
		this.addressList = addressList;
	}

	public List<DomtarOrderStatusOrdSrchDataBean> getPaginatedOrderStatusOrdList() {
		return paginatedOrderStatusOrdList;
	}

	public void setPaginatedOrderStatusOrdList(
			List<DomtarOrderStatusOrdSrchDataBean> paginatedOrderStatusOrdList) {
		this.paginatedOrderStatusOrdList = paginatedOrderStatusOrdList;
	}

	private int getNumberOfRecords() {
		
		HashMap<HashSet<String>, HashSet<String>> addressRelationMap = DomtarHelper.convertUserAddressListToMap(addressList);

		XorderItemsAccessBean xOrderItemsAccessBean = new XorderItemsAccessBean();
		ServerJDBCHelperAccessBean jdbcHelper = new ServerJDBCHelperAccessBean();
		Vector databaseRows = new Vector();

		sqlStatement = " SELECT T2.FIELD3,T2.TIMEPLACED,T1.SALESORDER_ID,T2.STATUS,T1.ORDERS_ID"
			+ " FROM XORDITEMS T1,ORDERS T2, ORDERITEMS T3, ADDRESS T4  "
			+ "WHERE T1.ORDERS_ID=T2.ORDERS_ID AND T1.ORDERITEMS_ID=T3.ORDERITEMS_ID AND T3.FIELD1=T4.ADDRESS_ID  AND  T4.STATUS ='P' ";

		num = 0;

		if (customerPO != null && !StringUtils.isEmpty(customerPO)) {
			sqlStatement += " AND T2.FIELD3 = ? ";
			num += 1;
		}
		if (frmOrdrDate != null && !StringUtils.isEmpty(frmOrdrDate)
				&& toOrdrDate != null && !StringUtils.isEmpty(toOrdrDate)) {
			sqlStatement += " AND T2.TIMEPLACED >= ? AND "
				+ "T2.TIMEPLACED <= ? ";
			num += 2;
		}
		if (ordStatus != null && !StringUtils.isEmpty(ordStatus)) {
			if ("All".equals(ordStatus)) {
				sqlStatement += " AND T2.STATUS IN (?,?,?,?,?)";
				num += 5;
			} else if ("Open".equals(ordStatus)) {
				sqlStatement += " AND T2.STATUS IN (?,?)";
				num += 2;
			} else {
				sqlStatement += " AND T2.STATUS = ? ";
				num += 1;
			}
		}
		if (itemStatus != null && !StringUtils.isEmpty(itemStatus)) {
			if ("All".equals(itemStatus)) {
				sqlStatement += " AND T3.STATUS  IN (?,?,?,?,?)";
				num += 5;
			} else if ("Open".equals(itemStatus)) {
				sqlStatement += " AND T3.STATUS IN (?,?)";
				num += 2;
			} else {
				sqlStatement += " AND T3.STATUS = ? ";
				num += 1;
			}
		}
		if (salesOrderId != null && !StringUtils.isEmpty(salesOrderId)) {
			sqlStatement += " AND T1.SALESORDER_ID = ? ";
			num += 1;
		}
		if (frmShipDate != null && !StringUtils.isEmpty(frmShipDate)
				&& toShipDate != null && !StringUtils.isEmpty(toShipDate)) {
			sqlStatement += " AND T3.REQUESTEDSHIPDATE >= ? "
				+ " AND T3.REQUESTEDSHIPDATE <=  ? ";
			num += 2;
		}
		if (partNum != null && !StringUtils.isEmpty(partNum)) {
			sqlStatement += " AND T3.PARTNUM = ? ";
			num += 1;
		}
		if (shipTo != null && !"".equals(shipTo)) {
			sqlStatement += " AND T4.NICKNAME = ? ";
			num += 1;
		} else {
			sqlStatement += " AND T4.NICKNAME IN (";
			
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
			sqlStatement += " AND T2.FIELD1= ? ";
			num += 1;
		} else {
			sqlStatement += " AND T2.FIELD1 IN (";
			
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

		Object[] queryParams = new Object[num];
		int count = 0;
		if (customerPO != null && !StringUtils.isEmpty(customerPO)) {
			queryParams[count] = getCustomerPO();
			count += 1;
		}
		if (frmOrdrDate != null && !StringUtils.isEmpty(frmOrdrDate)) {
			queryParams[count] = convertStringtoTimestamp(frmOrdrDate
					+ " 00:00:00.000");
			count += 1;
		}
		if (toOrdrDate != null && !StringUtils.isEmpty(toOrdrDate)) {
			queryParams[count] = convertStringtoTimestamp(toOrdrDate
					+ " 23:59:59.000");
			count += 1;
		}
		if (ordStatus != null && !StringUtils.isEmpty(ordStatus)) {
			if ("All".equals(ordStatus)) {
				queryParams[count] = ordStatE;
				count += 1;
				queryParams[count] = ordStatI;
				count += 1;
				queryParams[count] = ordStatR;
				count += 1;
				queryParams[count] = ordStatC;
				count += 1;
				queryParams[count] = ordStatX;
				count += 1;
			} else if ("Open".equals(ordStatus)) {
				queryParams[count] = ordStatE;
				count += 1;
				queryParams[count] = ordStatI;
				count += 1;
			} else if ("Shipped".equals(ordStatus)) {
				queryParams[count] = ordStatR;
				count += 1;
			} else if ("Invoiced".equals(ordStatus)) {
				queryParams[count] = ordStatC;
				count += 1;
			} else {
				queryParams[count] = ordStatX;
				count += 1;
			}
		}

		if (itemStatus != null && !StringUtils.isEmpty(itemStatus)) {
			if ("All".equals(itemStatus)) {
				queryParams[count] = itemStatE;
				count += 1;
				queryParams[count] = itemStatI;
				count += 1;
				queryParams[count] = itemStatR;
				count += 1;
				queryParams[count] = itemStatC;
				count += 1;
				queryParams[count] = itemStatX;
				count += 1;
			} else if ("Open".equals(itemStatus)) {
				queryParams[count] = itemStatE;
				count += 1;
				queryParams[count] = itemStatI;
				count += 1;
			} else if ("Shipped".equals(itemStatus)) {
				queryParams[count] = itemStatR;
				count += 1;
			} else if ("Invoiced".equals(itemStatus)) {
				queryParams[count] = itemStatC;
				count += 1;
			} else {
				queryParams[count] = itemStatX;
				count += 1;
			}
		}
		if (salesOrderId != null && !StringUtils.isEmpty(salesOrderId)) {
			queryParams[count] = getSalesOrderId();
			count += 1;
		}

		if (frmShipDate != null && !StringUtils.isEmpty(frmShipDate)) {
			queryParams[count] = convertStringtoTimestamp(frmShipDate
					+ " 00:00:00.000");
			count += 1;
		}
		if (toShipDate != null && !StringUtils.isEmpty(toShipDate)) {
			queryParams[count] = convertStringtoTimestamp(toShipDate
					+ " 23:59:59.000");
			count += 1;
		}

		if (partNum != null && !StringUtils.isEmpty(partNum)) {
			queryParams[count] = getPartNum();
			count += 1;
		}
		if (shipTo != null && !"".equals(shipTo)) {
			queryParams[count] = getShipTo();
			count += 1;
		}
		if (soldTo != null && !"".equals(soldTo)) {
			queryParams[count] = getSoldTo();
			count += 1;
		}

		if (sortBy != null && !StringUtils.isEmpty(sortBy)){
			if (sortBy.equals("orderDate")) {
				sqlStatement += "  ORDER  BY  T2.TIMEPLACED ASC";
			} else if (sortBy.equals("shipTo")) {
				sqlStatement += "  ORDER  BY  T4.NICKNAME ASC";
			} else if (sortBy.equals("salesOrder")) {
				sqlStatement += "  ORDER  BY  T1.SALESORDER_ID ASC";
			} else if (sortBy.equals("Status")) {
				sqlStatement += "  ORDER  BY  T2.STATUS ASC";
			} else if (sortBy.equals("customerPO")) {
				sqlStatement += "  ORDER  BY  T2.FIELD3 ASC";
			}
		} else {
			sqlStatement += "  ORDER  BY  T2.TIMEPLACED ASC";
		}
		try {
			//System.out.println(sqlStatement);
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

		Set<DomtarOrderStatusOrdSrchDataBean> orderStatusOrdSet = new LinkedHashSet<DomtarOrderStatusOrdSrchDataBean>();
		Set<String> orderIdList = new LinkedHashSet<String>();

		DomtarOrderStatusOrdSrchDataBean domtarOrderStatusOrdSrchDataBean = null;
		for (Object obj : databaseRows) {
			
			setSearchResultExists(true);
			Vector EachRow = (Vector) obj;
			customerPO = (String) EachRow.get(0);
			timePlaced = (Timestamp) EachRow.get(1);
			salesOrderId = (String) EachRow.get(2);
			if(salesOrderId != null){
				salesOrderId = salesOrderId.trim();
			}
			ordStatus = (String) EachRow.get(3);
			orderId = (Long) EachRow.get(4);
			String orderStatus = null;
			if(ordStatI.equals(ordStatus)){
				orderStatus = "Open";
			}else if(ordStatR.equals(ordStatus)){
				orderStatus = "Shipped";
			}else if(ordStatC.equals(ordStatus)){
				orderStatus = "Invoiced";
			}else if(ordStatX.equals(ordStatus)){
				orderStatus = "Cancelled";
			}
				

			if (!orderIdList.contains(orderId.toString())) {
				domtarOrderStatusOrdSrchDataBean = new DomtarOrderStatusOrdSrchDataBean();
				domtarOrderStatusOrdSrchDataBean.setCustomerPO(customerPO);
				domtarOrderStatusOrdSrchDataBean.setFormatedTimeplaced(getDateFormatted(timePlaced.toString()));
				domtarOrderStatusOrdSrchDataBean.setSalesOrderId(salesOrderId);
				domtarOrderStatusOrdSrchDataBean.setOrdStatus(orderStatus);
				domtarOrderStatusOrdSrchDataBean.setOrderId(orderId);
				if (partNum != null && !StringUtils.isEmpty(partNum)) {
					domtarOrderStatusOrdSrchDataBean.setPartNum(partNum);
				}
				orderStatusOrdSet.add(domtarOrderStatusOrdSrchDataBean);
				orderIdList.add(orderId.toString());
			}
		}

		int countRecords = orderStatusOrdSet.size();
		correctForPageInation(orderStatusOrdSet);

		if (countRecords > 0) {
			return countRecords;
		} else {
			return 0;
		}

	}

	/**
	 * This method get the date in a format in which it will be displayed in Order
	 * Status Screen.
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

	public boolean isSearchResultExists() {
		return isSearchResultExists;
	}

	public void setSearchResultExists(boolean isSearchResultExists) {
		this.isSearchResultExists = isSearchResultExists;
	}

	public int getBeginIndex() {
		return beginIndex;
	}

	public void setBeginIndex(int beginIndex) {
		this.beginIndex = beginIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public java.lang.String getFrmShipDate() {
		return frmShipDate;
	}

	public void setFrmShipDate(java.lang.String frmShipDate) {
		this.frmShipDate = frmShipDate;
	}

	public java.lang.String getToShipDate() {
		return toShipDate;
	}

	public void setToShipDate(java.lang.String toShipDate) {
		this.toShipDate = toShipDate;
	}

	public String getFrmOrdrDate() {
		return frmOrdrDate;
	}

	public void setFrmOrdrDate(String frmOrdrDate) {
		this.frmOrdrDate = frmOrdrDate;
	}

	public String getToOrdrDate() {
		return toOrdrDate;
	}

	public void setToOrdrDate(String toOrdrDate) {
		this.toOrdrDate = toOrdrDate;
	}

	public java.lang.String getSalesOrderId() {
		return salesOrderId;
	}

	public void setSalesOrderId(java.lang.String salesOrderId) {
		this.salesOrderId = salesOrderId;
	}

	public java.lang.Long getOrderId() {
		return orderId;
	}

	public void setOrderId(java.lang.Long orderId) {
		this.orderId = orderId;
	}

	public Timestamp getTimePlaced() {
		return timePlaced;
	}

	public void setTimePlaced(Timestamp timePlaced) {
		this.timePlaced = timePlaced;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the quantity
	 */
	public java.lang.String getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(java.lang.String quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the partNum
	 */
	public java.lang.String getPartNum() {
		return partNum;
	}

	/**
	 * @param partNum
	 *            the partNum to set
	 */
	public void setPartNum(java.lang.String partNum) {
		this.partNum = partNum;
	}

	public java.lang.Integer getOrderLineItem_Id() {
		return orderLineItem_Id;
	}

	/**
	 * @param orderLineItem_Id
	 *            the orderLineItem_Id to set
	 */
	public void setOrderLineItem_Id(java.lang.Integer orderLineItem_Id) {
		this.orderLineItem_Id = orderLineItem_Id;
	}

	/**
	 * @return the ordStatus
	 */
	public java.lang.String getOrdStatus() {
		return ordStatus;
	}

	/**
	 * @param ordStatus
	 *            the ordStatus to set
	 */
	public void setOrdStatus(java.lang.String ordStatus) {
		this.ordStatus = ordStatus;
	}

	/**
	 * @return the estimateDeliveryDate
	 */
	public java.sql.Timestamp getEstimateDeliveryDate() {
		return estimateDeliveryDate;
	}

	/**
	 * @param estimateDeliveryDate
	 *            the estimateDeliveryDate to set
	 */
	public void setEstimateDeliveryDate(java.sql.Timestamp estimateDeliveryDate) {
		this.estimateDeliveryDate = estimateDeliveryDate;
	}

	public java.lang.String getFormatedTimeplaced() {
		return formatedTimeplaced;
	}

	public void setFormatedTimeplaced(java.lang.String formatedTimeplaced) {
		this.formatedTimeplaced = formatedTimeplaced;
	}

	@Override
	public CommandContext getCommandContext() {
		return iCommandContext;
	}

	public String getCustomerPO() {
		return customerPO;
	}

	public void setCustomerPO(String customerPO) {
		this.customerPO = customerPO;
	}

	@Override
	public void setCommandContext(
			com.ibm.commerce.command.CommandContext aCommandContext) {
		iCommandContext = aCommandContext;
	}

	@Override
	public TypedProperty getRequestProperties() {
		return requestProperties;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	private Timestamp convertStringtoTimestamp(String pDate) {
		Timestamp vformattedDate = null;
		try {
			Date vParsedDate = timeStampDateFormat.parse(pDate);
			vformattedDate = new Timestamp(vParsedDate.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return vformattedDate;
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
//			MemberAccessBean mAb = new MemberAccessBean();
//			mAb.setInitKey_MemberId(parentMemberId);
//			mAb.refreshCopyHelper();

//			addressList = new ArrayList<DomtarAddress>();
			DomtarJDBCHelper domtarHelper = new DomtarJDBCHelper();
			try {
				addressList = domtarHelper.findSoldToRRCShipToAddressByOrganizationId(parentMemberId);
			} catch (Exception e) {
				e.printStackTrace();
			}
//			if (mAb.getDescendants().length > 0) {
//				for (int i = 0; i < mAb.getDescendants().length; i++) {
//					Long childMemberId = mAb.getDescendants()[i];
//					MemberAccessBean mAb1 = new MemberAccessBean();
//					mAb1.setInitKey_MemberId(childMemberId.toString());
//					mAb1.refreshCopyHelper();
//					if (mAb1.getType().equals("UserBean")) {
//						UserAccessBean userAcBean = new UserAccessBean();
//						userAcBean
//						.setInitKey_MemberId(childMemberId.toString());
//						userAcBean.refreshCopyHelper();
//
//						if ("A".equals(userAcBean.getRegisterType())) {
//							Enumeration enmAddressList = (new AddressAccessBean())
//							.findByMemberId(childMemberId);
//							while (enmAddressList.hasMoreElements()) {
//								AddressAccessBean addressbean = (AddressAccessBean) enmAddressList
//								.nextElement();
//								if ("B".equals(addressbean.getAddressType())) {
//									XaddressRelationAccessBean xAddrBean1 = new XaddressRelationAccessBean();
//									Enumeration xAddrEnum = xAddrBean1
//									.findbyBillToId(Long
//											.valueOf(addressbean
//													.getNickName()));
//									while (xAddrEnum.hasMoreElements()) {
//										XaddressRelationAccessBean xAddrBean = (XaddressRelationAccessBean) xAddrEnum
//										.nextElement();
//										DomtarAddress domAdd = new DomtarAddress();
//										domAdd.setSoldTo(xAddrBean
//												.getBillToId().toString());
//										domAdd.setSoldToName(xAddrBean
//												.getBilltoName());
//										domAdd.setShipTo(xAddrBean
//												.getShipToId().toString());
//										domAdd.setShipToName(xAddrBean
//												.getShiptoName());
//										domAdd.setRrc(xAddrBean.getRrcId()
//												.toString());
//										domAdd.setRrcName(xAddrBean
//												.getRrcName());
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

}
