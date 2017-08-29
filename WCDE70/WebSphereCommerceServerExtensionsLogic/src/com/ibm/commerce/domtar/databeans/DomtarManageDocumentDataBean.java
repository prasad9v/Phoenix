package com.ibm.commerce.domtar.databeans;

import java.net.URLDecoder;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;
import java.util.Map.Entry;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import com.ibm.commerce.base.objects.ServerJDBCHelperAccessBean;
import com.ibm.commerce.beans.InputDataBean;
import com.ibm.commerce.beans.SmartDataBean;
import com.ibm.commerce.beans.SmartDataBeanImpl;
import com.ibm.commerce.command.CommandContext;
import com.ibm.commerce.context.entitlement.EntitlementContext;
import com.ibm.commerce.datatype.TypedProperty;
import com.ibm.commerce.domtar.commands.DomtarPopulateUserAddressListCmd;
import com.ibm.commerce.domtar.commands.DomtarPopulateUserAddressListCmdImpl;
import com.ibm.commerce.domtar.helpers.DomtarHelper;

public class DomtarManageDocumentDataBean extends SmartDataBeanImpl implements SmartDataBean, InputDataBean {

	public static final String COPYRIGHT = com.ibm.commerce.copyright.IBMCopyright.SHORT_COPYRIGHT;

	private static final String DEFAULT_SORTING = "orderDate";
	private static final String SORT_BY_ORDERDATE = "orderDate";
	private static final String SORT_BY_SALESORDER = "salesOrder";
	private static final String SORT_BY_CUSTOMERPO = "customerPO";
	
	private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss.SSS");
	
	private TypedProperty requestProperties;
	
	private static final int ITEMS_PER_PAGE = 10;
	private String sorting;
	
	private String orderDate;
	private boolean isSearchResultExists = false;

	private String partNumber;
	private String invoiceDate;
	
	private HashMap resultList;

	private int num = 0;
	
	private String soldTo;
	private String shipTo;
	private String itemCode;
	//private String shipId;
	private String customerPO;
	private String manifest;
	private String salesOrderId;
	private String invoice;
	private String fromOrderDate;
	private String toOrderDate;
	private String fromInvoiceDate;
	private String toInvoiceDate;
	
	private String vStoreId;
	private String vLangId;
	
	/**
	 * @return the vStoreId
	 */
	public String getStoreId() {
		return vStoreId;
	}
	/**
	 * @param storeId the vStoreId to set
	 */
	public void setStoreId(String storeId) {
		vStoreId = storeId;
	}
	/**
	 * @return the vLangId
	 */
	public String getLangId() {
		return vLangId;
	}
	/**
	 * @param langId the vLangId to set
	 */
	public void setLangId(String langId) {
		vLangId = langId;
	}

	private int pageSize;
	
	private int beginIndex = 0;
	
	private int totalRecords =0;
	
	private List addressList;
	
	public int getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}
	public int getBeginIndex() {
		return beginIndex;
	}
	public void setBeginIndex(int beginIndex) {
		this.beginIndex = beginIndex;
	}
	public String getPartNumber() {
		return partNumber;
	}
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getSorting() {
		return sorting;
	}
	public void setSorting(String sorting) {
		this.sorting = sorting;
	}

	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public boolean isSearchResultExists() {
		return isSearchResultExists;
	}
	public void setSearchResultExists(boolean isSearchResultExists) {
		this.isSearchResultExists = isSearchResultExists;
	}

	public HashMap getResultList() {
		return resultList;
	}
	public void setResultList(HashMap resultList) {
		this.resultList = resultList;
	}

	public String getSoldTo() {
		return soldTo;
	}
	public void setSoldTo(String soldTo) {
		this.soldTo = soldTo;
	}
	public String getShipTo() {
		return shipTo;
	}
	public void setShipTo(String shipTo) {
		this.shipTo = shipTo;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
//	public String getShipId() {
//		return shipId;
//	}
//	public void setShipId(String shipId) {
//		this.shipId = shipId;
//	}
	public String getCustomerPO() {
		return customerPO;
	}
	public void setCustomerPO(String custPo) {
		this.customerPO = custPo;
	}
	public String getManifest() {
		return manifest;
	}
	public void setManifest(String manifest) {
		this.manifest = manifest;
	}
	public String getSalesOrderId() {
		return salesOrderId;
	}
	public void setSalesOrderId(String salesOrder) {
		this.salesOrderId = salesOrder;
	}
	public String getInvoice() {
		return invoice;
	}
	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
	public String getFromOrderDate() {
		return fromOrderDate;
	}
	public void setFromOrderDate(String fromOrderDate) {
		this.fromOrderDate = fromOrderDate;
	}
	public String getToOrderDate() {
		return toOrderDate;
	}
	public void setToOrderDate(String toOrderDate) {
		this.toOrderDate = toOrderDate;
	}
	public String getFromInvoiceDate() {
		return fromInvoiceDate;
	}
	public void setFromInvoiceDate(String fromInvoiceDate) {
		this.fromInvoiceDate = fromInvoiceDate;
	}
	public String getToInvoiceDate() {
		return toInvoiceDate;
	}
	public void setToInvoiceDate(String toInvoiceDate) {
		this.toInvoiceDate = toInvoiceDate;
	}

	@Override
	public void populate()  {	
		try {
			
			populateUserAddressList();
			getManagedDocRecords();
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	@Override
	public void setRequestProperties(com.ibm.commerce.datatype.TypedProperty requestProperties) throws Exception {
		setStoreId(requestProperties.getString("storeId", ""));
		setLangId(requestProperties.getString("langId", "-1"));
		setSoldTo(requestProperties.getString("soldTo", ""));
		setShipTo(requestProperties.getString("shipTo", ""));
		setItemCode(requestProperties.getString("itemCode", ""));
		//setShipId(requestProperties.getString("shipId", ""));
		setCustomerPO(requestProperties.getString("custPo", ""));
		setManifest(requestProperties.getString("manifest",""));
		setSalesOrderId(requestProperties.getString("salesOrder",""));
		setInvoice(requestProperties.getString("invoice", ""));
		setFromOrderDate(decodeDate(requestProperties.getString("fromOrderDate", "")));
		setToOrderDate(decodeDate(requestProperties.getString("toOrderDate", "")));
		setFromInvoiceDate(decodeDate(requestProperties.getString("fromInvoiceDate", "")));
		setToInvoiceDate(decodeDate(requestProperties.getString("toInvoiceDate", "")));
		
		setSorting(requestProperties.getString("sortBy", DEFAULT_SORTING));
		beginIndex = new Integer(requestProperties.getString("beginIndex", "0")).intValue();
		pageSize = Integer.parseInt(requestProperties.getString("pageSize","10"));

	}
	private String decodeDate(String pDate) {
		if(pDate != "")
			return URLDecoder.decode(pDate);
		else
			return pDate;
	}

	
	private void populateUserAddressList() {
		
		try {
			CommandContext commandcontext = getCommandContext();
			String parentMemberId = null;
				
			EntitlementContext ctx = (EntitlementContext)commandcontext.getContext("com.ibm.commerce.context.entitlement.EntitlementContext");
			if(ctx != null) {
				parentMemberId = ctx.getActiveOrganizationId().toString();
			} else {
				parentMemberId = commandcontext.getUser().getParentMemberId();
			}
			
			/*
			 * Call the cached command to fetch the addressList
			 */
			DomtarPopulateUserAddressListCmd populateAddressRelationCmd = null;

			populateAddressRelationCmd = new DomtarPopulateUserAddressListCmdImpl();
			populateAddressRelationCmd.setStoreId(vStoreId);
			populateAddressRelationCmd.setLanguageId(vLangId);
			populateAddressRelationCmd.setOrganizationId(parentMemberId);				
			populateAddressRelationCmd.execute();
				
			//get the addressList from cache
			addressList = populateAddressRelationCmd.getUserAddressList();
			
//			DomtarJDBCHelper domtarHelper = new DomtarJDBCHelper();
//			try {
//				addressList = domtarHelper.findSoldToRRCShipToAddressByOrganizationId(parentMemberId);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			MemberAccessBean mAb = new MemberAccessBean();
//			mAb.setInitKey_MemberId(parentMemberId);
//			mAb.refreshCopyHelper();
			//System.out.println("Parent  : " + parentMemberId +"  "+mAb.getType());
//			addressList = new ArrayList<DomtarAddress>();		
//			if(mAb.getDescendants().length>0){
//				for (int i = 0; i < mAb.getDescendants().length; i++) {
//					Long childMemberId = mAb.getDescendants()[i];
//					MemberAccessBean mAb1 = new MemberAccessBean();
//					mAb1.setInitKey_MemberId(childMemberId.toString());
//					mAb1.refreshCopyHelper();
//					if(mAb1.getType().equals("UserBean")){
//						UserAccessBean userAcBean = new UserAccessBean();
//						//System.out.println("Setting registerType1: " + childMemberId);
//						userAcBean.setInitKey_MemberId(childMemberId.toString());
//						userAcBean.refreshCopyHelper();
//	
//						if ("A".equals(userAcBean.getRegisterType())) {
//							Enumeration<AddressAccessBean> enmAddressList = (new AddressAccessBean()).findByMemberId(childMemberId);
//							while(enmAddressList.hasMoreElements()) {
//								AddressAccessBean addressbean = (AddressAccessBean)enmAddressList.nextElement();
//								System.out.println("AddressType: " + addressbean.getAddressType());
//								if ("B".equals(addressbean.getAddressType())) {
//									XaddressRelationAccessBean xAddrBean1 = new XaddressRelationAccessBean();
//									Enumeration xAddrEnum = xAddrBean1.findbyBillToId(Long.valueOf(addressbean.getNickName()));
//									while (xAddrEnum.hasMoreElements()) {
//										XaddressRelationAccessBean xAddrBean = (XaddressRelationAccessBean) xAddrEnum.nextElement();
//										DomtarAddress domAdd = new DomtarAddress();
//										domAdd.setSoldTo(xAddrBean.getBillToId().toString());
//										domAdd.setSoldToName(xAddrBean.getBilltoName());
//										domAdd.setShipTo(xAddrBean.getShipToId().toString());
//										domAdd.setShipToName(xAddrBean.getShiptoName());
//										domAdd.setRrc(xAddrBean.getRrcId().toString());
//										domAdd.setRrcName(xAddrBean.getRrcName());
//										System.out.println(xAddrBean.getBillToId()+" "+xAddrBean.getBilltoName()+" "+xAddrBean.getShipToId()+" "+xAddrBean.getShiptoName()+" "+xAddrBean.getRrcId()+" "+xAddrBean.getRrcName());
//										addressList.add(domAdd);
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
		
	}
	public void getManagedDocRecords() throws ParseException	{
		
		HashMap<HashSet<String>, HashSet<String>> addressRelationMap = DomtarHelper.convertUserAddressListToMap(addressList);
		
		String vKey;
		List<DomtarManageDocumentDataBean> manageDocumentList =null;
		ServerJDBCHelperAccessBean jdbcHelper = new ServerJDBCHelperAccessBean();	
		Vector databaseRows = new Vector();
		
		String sqlStatement = "SELECT T2.FIELD3,T1.SALESORDER_ID,T2.TIMEPLACED,T3.FIELD1,T3.PARTNUM," +
				"T1.MANIFESTIDENTIFIER,T1.INVOICEIDENTIFIER,T3.TIMERELEASED,T2.FIELD1,T4.NICKNAME  " +
				"FROM XORDITEMS T1,ORDERS T2, ORDERITEMS T3, ADDRESS T4 " +
				"WHERE T1.ORDERS_ID=T2.ORDERS_ID AND T1.ORDERITEMS_ID=T3.ORDERITEMS_ID AND T3.FIELD1=T4.ADDRESS_ID AND T4.STATUS ='P' ";
		
		num=0;
		
		if (customerPO != null && !"".equals(customerPO)) {
			sqlStatement +=" AND T2.FIELD3 = ? ";
			num +=1;
		}
		if(fromOrderDate != null && !fromOrderDate.equals("")){
			sqlStatement +=" AND T2.TIMEPLACED >= ? ";
			num+=1;
		}
		if(toOrderDate != null && !toOrderDate.equals("")){
			sqlStatement +=" AND T2.TIMEPLACED <= ? ";
			num+=1;
		}
		if(fromInvoiceDate!= null && !fromInvoiceDate.equals("")){
			sqlStatement +=" AND T3.TIMERELEASED >= ? ";
			num+=1;
		}
		if(toInvoiceDate != null && !toInvoiceDate.equals("")){
			sqlStatement +=" AND T3.TIMERELEASED <= ? ";
			num+=1;
		}
		
		if(salesOrderId != null && !"".equals(salesOrderId)){
			sqlStatement +=" AND T1.SALESORDER_ID = ? ";
			num+=1;
		}
		
		if(manifest != null && !"".equals(manifest)) {
			sqlStatement +=" AND T1.MANIFESTIDENTIFIER = ? " ;
			num+=1;
		}
		
		if(invoice != null  && !"".equals(invoice)) {
			sqlStatement +=" AND T1.INVOICEIDENTIFIER = ? " ;
			num+=1;
		}
		if(itemCode != null  && !"".equals(itemCode)) {
			sqlStatement +=" AND T3.PARTNUM = ? " ;
			num+=1;
		}
//		if(shipId != null  && !"".equals(shipId)) {
//			sqlStatement +=" AND T3.FIELD1 = ? " ;
//			num+=1;
//		}
		if(shipTo != null  && !"".equals(shipTo)) {
			sqlStatement +=" AND T4.NICKNAME = ? " ;
			num+=1;
		}else{
			sqlStatement +=" AND T4.NICKNAME IN (" ;
			
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
//				sqlStatement +="'"+object.getShipTo()+"'" ;
//				if(iterator.hasNext()){
//					sqlStatement +=", " ;
//				}
//			}
			sqlStatement +=")" ;
		}
		if(soldTo != null  && !"".equals(soldTo)) {
			sqlStatement +=" AND T2.FIELD1= ? " ;
			num+=1;
		}else{
			sqlStatement +=" AND T2.FIELD1 IN (" ;
			
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
//				sqlStatement +=object.getSoldTo() ;
//				if(iterator.hasNext()){
//					sqlStatement +=", " ;
//				}
//			}
			sqlStatement +=")" ;
		}
		
		if(sorting.equals(SORT_BY_SALESORDER)){
			sqlStatement +=" ORDER BY T1.SALESORDER_ID ASC" ;
		}else if (sorting.equals(SORT_BY_CUSTOMERPO)) {
			sqlStatement +=" ORDER BY T2.FIELD3 ASC" ;
		}else {
			sqlStatement +=" ORDER BY T2.TIMEPLACED ASC" ;
		}
		
		Object[] queryParams = new Object[num];
		int count =0;
		if (customerPO != null && !"".equals(customerPO)) {
			queryParams[count] = getCustomerPO();
			count+=1;
		}
		if(fromOrderDate != null && !fromOrderDate.equals("")){			
			queryParams[count] = convertStringtoTimestamp(fromOrderDate+" 00:00:00.000");
			count+=1;
		}
		if(toOrderDate != null && !toOrderDate.equals("")){
			queryParams[count] =convertStringtoTimestamp(toOrderDate+" 23:59:59.000");
			count+=1;
		}
		if(fromInvoiceDate!= null && !fromInvoiceDate.equals("")){			
			queryParams[count] = convertStringtoTimestamp(fromInvoiceDate+" 00:00:00.000");
			count+=1;
		}
		if(toInvoiceDate != null && !toInvoiceDate.equals("")){
			queryParams[count] =convertStringtoTimestamp(toInvoiceDate+" 23:59:59.000");
			count+=1;
		}
	
		if(salesOrderId != null&& !"".equals(salesOrderId)){
			queryParams[count] =getSalesOrderId();
			count+=1;
		}
		
		if(manifest != null && !"".equals(manifest)) {
			queryParams[count] =getManifest();
			count+=1;
		}
		
		if(invoice != null&& !"".equals(invoice)) {
			queryParams[count] =getInvoice();
			count+=1;
		}
		if(itemCode != null  && !"".equals(itemCode)) {
			queryParams[count] =getItemCode();
			count+=1;
		}
//		if(shipId != null  && !"".equals(shipId)) {
//			queryParams[count] =getShipId();
//			count+=1;
//		}
		if(shipTo != null  && !"".equals(shipTo)) {
			queryParams[count] =getShipTo();
			count+=1;
		}
		if(soldTo != null  && !"".equals(soldTo)) {
			queryParams[count] =getSoldTo();
			count+=1;
		}
		try	{
			//System.out.println("SQL Statement is "+sqlStatement);
			long vEntryPoint = System.currentTimeMillis();
			databaseRows = jdbcHelper.executeParameterizedQuery(sqlStatement,queryParams);
			totalRecords =databaseRows.size();
	                   
			DomtarManageDocumentDataBean vPopulateBean = null;
			resultList = new LinkedHashMap<String, List<DomtarManageDocumentDataBean>>();
			int i=0;
			for (Object obj : databaseRows) {
				if (i >= beginIndex) {
					if(i < (beginIndex + pageSize)){
						setSearchResultExists(true);

						Vector EachRow = (Vector) obj;
						vPopulateBean = new DomtarManageDocumentDataBean();
						customerPO = (String) EachRow.get(0); 
						if(customerPO == null){
							customerPO ="";
						}
						salesOrderId = (String)EachRow.get(1);
						Timestamp vOrderDate = (Timestamp) EachRow.get(2);
						orderDate = formatDateForDisplay(vOrderDate,"MM/dd/yyyy");
						
						//shipId =EachRow.get(3).toString();
						partNumber=(String)EachRow.get(4);
						
						manifest=(String)EachRow.get(5);
						if(manifest == null || manifest.equals("")){
							manifest = "NA";
						}
						invoice=(String)EachRow.get(6);
						if(invoice == null || invoice.equals("")){
							invoice = "NA";
						}

						Timestamp vInvoiceDate = (Timestamp) EachRow.get(7);
						invoiceDate = formatDateForDisplay(vInvoiceDate,"MM/dd/yyyy");
						
						//soldTo=EachRow.get(8).toString();
						//shipTo=(String) EachRow.get(9);
						String soldToId=EachRow.get(8).toString();
						String shipToId=(String) EachRow.get(9);
						
						for (Iterator iterator = addressList.iterator(); iterator.hasNext();) {
							DomtarAddress addObj = (DomtarAddress) iterator.next();
							if(addObj.getShipTo().equals(shipToId)){
								shipTo = addObj.getShipToName();
							}
							if(addObj.getSoldTo().equals(soldToId)){
								soldTo = addObj.getSoldToName();
							}
						}
						
						vPopulateBean.setCustomerPO(customerPO.trim());
				    	vPopulateBean.setSalesOrderId(salesOrderId.trim());
				    	vPopulateBean.setOrderDate(orderDate);
				    	//vPopulateBean.setShipId(shipId);
				    	vPopulateBean.setPartNumber(partNumber);
				    	vPopulateBean.setManifest(manifest);
				    	vPopulateBean.setInvoice(invoice);
				    	vPopulateBean.setInvoiceDate(invoiceDate);
				    	vPopulateBean.setShipTo(shipTo);
				    	vPopulateBean.setSoldTo(soldTo);
						
								
						vKey = customerPO+"_"+salesOrderId+"_"+orderDate;
						if(resultList != null && resultList.containsKey(vKey)){
							manageDocumentList = (List<DomtarManageDocumentDataBean>) resultList.get(vKey);
				    	}else{
				    		manageDocumentList = new ArrayList<DomtarManageDocumentDataBean>();
				    	}
						
						manageDocumentList.add(vPopulateBean);
						resultList.put(vKey, manageDocumentList);
					}else{
						// Break the loop if we have 10 line items in the resultList Map
						break;
					}
				}
				i++;
			}
			long vExitPoint = System.currentTimeMillis();
			long TotCallDiff = (vExitPoint-vEntryPoint);
			System.out.println("Time for creating resultList in ManagedDocument >>>>>> "+TotCallDiff+" ms");

		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		
	}
	
	private Timestamp convertStringtoTimestamp(String pDate) {
		Timestamp vformattedDate=null;
		try {			
			Date vParsedDate = dateFormat.parse(pDate);
			vformattedDate = new Timestamp(vParsedDate.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return vformattedDate;
	}
	
	/**
     * @param pTimeStamp
     * @param pFormat
     * @return
     */
    private String formatDateForDisplay(Timestamp pTimeStamp, String pFormat) {
    	
    	if(pTimeStamp == null){
    		return null;
    	}    		
 
        DateFormat vDateFormat = new SimpleDateFormat(pFormat);
        //DateFormat vFormat = DateFormat.getDateInstance(DateFormat.LONG);
        Date vDate = new Date(pTimeStamp.getTime());

        return vDateFormat.format(vDate);
    }

}
