package com.ibm.commerce.domtar.databeans;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;

import com.ibm.commerce.beans.InputDataBean;
import com.ibm.commerce.beans.SmartDataBean;
import com.ibm.commerce.command.CommandContext;
import com.ibm.commerce.datatype.TypedProperty;
import com.ibm.commerce.domtar.util.DomtarJDBCHelper;
import com.ibm.commerce.extension.objects.XorderItemsAccessBean;
import com.ibm.commerce.order.objects.OrderItemAccessBean;
import com.ibm.commerce.user.objects.AddressAccessBean;

public class DomtarOrderStatusOrdItemSrchDataBean extends XorderItemsAccessBean
		implements SmartDataBean, InputDataBean {

	public static final String COPYRIGHT = com.ibm.commerce.copyright.IBMCopyright.SHORT_COPYRIGHT;

	private Integer orderLineItemId;
	private Long orderId;
	private Long orderItemsId;
	private String salesOrderId;
	private String ordStatus;
	private String ordItemStatus;
	private char ordItemStat;
	private Timestamp estimateDeliveryDate;
	private String formattedEstDelDate;
	private CommandContext iCommandContext = null;
	private TypedProperty requestProperties;
	private String partNum;
	private String partNumber;
	private String quantity;
	private String address;
	private String customerPO;
	private String orderDate;
	private String ordrDate;
	private String manifest;
	private String invoice;
	private String bol;
	private String pod;
	private Double quanity;
	private Timestamp timeShipped;
	private String formattedTimeShipped;
	private Date fromOrdDate = null;
	private DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.S");
	private Long fromODate = 0L;
	private Timestamp fOrdDate = null;
	private java.lang.String itemStatus;
	private int count = 0;
	private String hotOrder;
	private String weightType;

	private String itemStatE = "E";
	private String itemStatI = "I";
	private String itemStatR = "R";
	private String itemStatC = "C";
	private String itemStatX = "X";

	private java.lang.String itmStatus = "";

	private String ItemStatArray[] = null;

	private List<DomtarOrderStatusOrdItemSrchDataBean> OrderStatusOrdItmList = new ArrayList<DomtarOrderStatusOrdItemSrchDataBean>();
	
	private static final Hashtable<String, String> DOMTAR_ORDER_STATUS = new Hashtable<String, String>();
	
	static {
		DOMTAR_ORDER_STATUS.put("I","Open");
		DOMTAR_ORDER_STATUS.put("R","Shipped");
		DOMTAR_ORDER_STATUS.put("C","Invoiced");
		DOMTAR_ORDER_STATUS.put("X","Cancelled");
	}
	
	private String orderItemStatus;

	public String getOrderItemStatus() {
		return orderItemStatus;
	}

	public void setOrderItemStatus(String orderItemStatus) {
		this.orderItemStatus = orderItemStatus;
	}

	@Override
	public void setRequestProperties(com.ibm.commerce.datatype.TypedProperty requestProperties) throws Exception {
		itemStatus = requestProperties.getString("orderItemStatus", "All");
	}

	public void getOrderItemRecords()

	{
		OrderItemAccessBean orderItemAccessBean = new OrderItemAccessBean();

		OrderStatusOrdItmList = new ArrayList<DomtarOrderStatusOrdItemSrchDataBean>();
		try {
			Enumeration orderitemRecods = orderItemAccessBean
					.findByOrder(new Long(orderId));
			DomtarOrderStatusOrdItemSrchDataBean domtarOrdItemSrchDataBean = null;

			if ("Open".equals(itemStatus)) {
				ItemStatArray = new String[2];
				ItemStatArray[0] = itemStatE;
				ItemStatArray[1] = itemStatI;
			} else if ("Shipped".equals(itemStatus)) {
				itmStatus = itemStatR;
			} else if ("Invoiced".equals(itemStatus)) {
				itmStatus = itemStatC;
			} else if ("Cancelled".equals(itemStatus)) {
				itmStatus = itemStatX;
			} else if ("All".equals(itemStatus)) {
				itmStatus = "All";
			}

			while (orderitemRecods.hasMoreElements()) {
				orderItemAccessBean = (OrderItemAccessBean) orderitemRecods.nextElement();
				
				String shipToAddressId = orderItemAccessBean.getField1();
				AddressAccessBean addressBean = new AddressAccessBean();
				addressBean.setInitKey_AddressId(shipToAddressId);
				
				DomtarJDBCHelper jdbcHelper = new DomtarJDBCHelper();
				String shipToName = "";
				try {
					shipToName = jdbcHelper.findShipToNameByShipToAddressId(addressBean.getNickName());
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (ItemStatArray != null && ItemStatArray.length > 0) {

					if (itemStatE.equals(orderItemAccessBean.getStatus()) || itemStatI.equals(orderItemAccessBean.getStatus())) {

						domtarOrdItemSrchDataBean = new DomtarOrderStatusOrdItemSrchDataBean();
						domtarOrdItemSrchDataBean.setPartNumber(orderItemAccessBean.getPartNumber());
						domtarOrdItemSrchDataBean.setQuantity(new BigDecimal(orderItemAccessBean.getQuantity()).stripTrailingZeros().toPlainString());
						domtarOrdItemSrchDataBean.setAddress(shipToName);

						if (orderItemAccessBean.getRequestedShipDate() != null) {
							formattedTimeShipped = getDateFormatted(orderItemAccessBean.getRequestedShipDate().toString());
							domtarOrdItemSrchDataBean.setFormattedTimeShipped(formattedTimeShipped);
						}
						XorderItemsAccessBean xorderItemsAccessBean = new XorderItemsAccessBean();
						Enumeration xorderitemRecods = xorderItemsAccessBean.findByOrderItemId(orderItemAccessBean.getOrderItemIdInEJBType());

						while (xorderitemRecods.hasMoreElements()) {
							xorderItemsAccessBean = (XorderItemsAccessBean) xorderitemRecods.nextElement();
							domtarOrdItemSrchDataBean.setOrdItemStatus(orderItemAccessBean.getStatus().charAt(0));
							domtarOrdItemSrchDataBean.setOrderItemStatus(DOMTAR_ORDER_STATUS.get(orderItemAccessBean.getStatus()));
							domtarOrdItemSrchDataBean.setHotOrder(xorderItemsAccessBean.getHotOrder());
							domtarOrdItemSrchDataBean.setBol(xorderItemsAccessBean.getBolIdentifier());
							domtarOrdItemSrchDataBean.setWeightType(getUOMFromWeighType(xorderItemsAccessBean.getWeighType()));
							domtarOrdItemSrchDataBean.setManifest(xorderItemsAccessBean.getManifestIdentifier());
							domtarOrdItemSrchDataBean.setInvoice(xorderItemsAccessBean.getInvoiceIdentifier());
							if (xorderItemsAccessBean.getEstimateDeliveryDate() != null) {
								formattedEstDelDate = getDateFormatted(xorderItemsAccessBean.getEstimateDeliveryDate().toString());
								domtarOrdItemSrchDataBean.setFormattedEstDelDate(formattedEstDelDate);
							}
						}
						OrderStatusOrdItmList.add(domtarOrdItemSrchDataBean);
					}
				} else if ("All".equals(itmStatus)) {
					domtarOrdItemSrchDataBean = new DomtarOrderStatusOrdItemSrchDataBean();
					domtarOrdItemSrchDataBean.setPartNumber(orderItemAccessBean.getPartNumber());
					domtarOrdItemSrchDataBean.setQuantity(new BigDecimal(orderItemAccessBean.getQuantity()).stripTrailingZeros().toPlainString());
					domtarOrdItemSrchDataBean.setAddress(shipToName);

					if (orderItemAccessBean.getRequestedShipDate() != null) {
						formattedTimeShipped = getDateFormatted(orderItemAccessBean.getRequestedShipDate().toString());
						domtarOrdItemSrchDataBean.setFormattedTimeShipped(formattedTimeShipped);
					}
					XorderItemsAccessBean xorderItemsAccessBean = new XorderItemsAccessBean();
					Enumeration xorderitemRecods = xorderItemsAccessBean.findByOrderItemId(orderItemAccessBean.getOrderItemIdInEJBType());
					while (xorderitemRecods.hasMoreElements()) {
						xorderItemsAccessBean = (XorderItemsAccessBean) xorderitemRecods.nextElement();
						domtarOrdItemSrchDataBean.setOrdItemStatus(orderItemAccessBean.getStatus().charAt(0));
						domtarOrdItemSrchDataBean.setOrderItemStatus(DOMTAR_ORDER_STATUS.get(orderItemAccessBean.getStatus()));
						domtarOrdItemSrchDataBean.setWeightType(getUOMFromWeighType(xorderItemsAccessBean.getWeighType()));
						domtarOrdItemSrchDataBean.setHotOrder(xorderItemsAccessBean.getHotOrder());
						domtarOrdItemSrchDataBean.setBol(xorderItemsAccessBean.getBolIdentifier());
						if (StringUtils.isEmpty(xorderItemsAccessBean.getManifestIdentifier())) {
							domtarOrdItemSrchDataBean.setManifest("NA");
						} else {
							domtarOrdItemSrchDataBean.setManifest(xorderItemsAccessBean.getManifestIdentifier());
						}
						if (StringUtils.isEmpty(xorderItemsAccessBean.getInvoiceIdentifier())) {
							domtarOrdItemSrchDataBean.setInvoice("NA");
						} else {
							domtarOrdItemSrchDataBean.setInvoice(xorderItemsAccessBean.getInvoiceIdentifier());
						}

						if (xorderItemsAccessBean.getEstimateDeliveryDate() != null) {
							formattedEstDelDate = getDateFormatted(xorderItemsAccessBean.getEstimateDeliveryDate().toString());
							domtarOrdItemSrchDataBean.setFormattedEstDelDate(formattedEstDelDate);
						}
					}
					OrderStatusOrdItmList.add(domtarOrdItemSrchDataBean);
				} else {
					if (orderItemAccessBean.getStatus().equals(itmStatus)) {

						domtarOrdItemSrchDataBean = new DomtarOrderStatusOrdItemSrchDataBean();
						domtarOrdItemSrchDataBean.setPartNumber(orderItemAccessBean.getPartNumber());
						domtarOrdItemSrchDataBean.setQuantity(new BigDecimal(orderItemAccessBean.getQuantity()).stripTrailingZeros().toPlainString());
						domtarOrdItemSrchDataBean.setAddress(shipToName);

						if (orderItemAccessBean.getRequestedShipDate() != null) {
							formattedTimeShipped = getDateFormatted(orderItemAccessBean.getRequestedShipDate().toString());
							domtarOrdItemSrchDataBean.setFormattedTimeShipped(formattedTimeShipped);
						}
						XorderItemsAccessBean xorderItemsAccessBean = new XorderItemsAccessBean();
						Enumeration xorderitemRecods = xorderItemsAccessBean.findByOrderItemId(orderItemAccessBean.getOrderItemIdInEJBType());

						while (xorderitemRecods.hasMoreElements()) {
							xorderItemsAccessBean = (XorderItemsAccessBean) xorderitemRecods.nextElement();
							domtarOrdItemSrchDataBean.setOrdItemStatus(orderItemAccessBean.getStatus().charAt(0));
							domtarOrdItemSrchDataBean.setOrderItemStatus(DOMTAR_ORDER_STATUS.get(orderItemAccessBean.getStatus()));
							domtarOrdItemSrchDataBean.setWeightType(getUOMFromWeighType(xorderItemsAccessBean.getWeighType()));
							domtarOrdItemSrchDataBean.setHotOrder(xorderItemsAccessBean.getHotOrder());
							domtarOrdItemSrchDataBean.setBol(xorderItemsAccessBean.getBolIdentifier());
							domtarOrdItemSrchDataBean.setManifest(xorderItemsAccessBean.getManifestIdentifier());
							domtarOrdItemSrchDataBean.setInvoice(xorderItemsAccessBean.getInvoiceIdentifier());
							if (xorderItemsAccessBean.getEstimateDeliveryDate() != null) {
								formattedEstDelDate = getDateFormatted(xorderItemsAccessBean.getEstimateDeliveryDate().toString());
								domtarOrdItemSrchDataBean.setFormattedEstDelDate(formattedEstDelDate);
							}
						}
						OrderStatusOrdItmList.add(domtarOrdItemSrchDataBean);
					}
				}
			}
		}

		catch (CreateException e) {

			e.printStackTrace();
		} catch (NumberFormatException e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		} catch (NamingException e) {

			e.printStackTrace();
		} catch (FinderException e) {

			e.printStackTrace();
		}

	}
	/*
	 * UOM has been added to WeightType in DB, by appending a **#** after weight type.
	 * So in order to retrieve the value of UOM we need to split it.	 
	 * */
	private String getUOMFromWeighType(String weighType) {
		if(weighType != null){
			String[] uom = weighType.split("\\*\\*#\\*\\*");	
			if(uom.length > 1 && null != uom[1]){
				return uom[1];
			}
		}
		return null;
	}

	public String getHotOrder() {
		return hotOrder;
	}

	public void setHotOrder(String hotOrder) {
		this.hotOrder = hotOrder;
	}
	
	public String getWeightType() {
		return weightType;
	}

	public void setWeightType(String weightType) {
		this.weightType = weightType;
	}

	/**
	 * This method get the date in a format in which it wil be displyed in Order
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

	public List<DomtarOrderStatusOrdItemSrchDataBean> getOrderStatusOrdItmList() {
		return OrderStatusOrdItmList;
	}

	public void setOrderStatusOrdItmList(
			List<DomtarOrderStatusOrdItemSrchDataBean> orderStatusOrdItmList) {
		OrderStatusOrdItmList = orderStatusOrdItmList;
	}

	public java.lang.Integer getOrderLineItemId() {
		return orderLineItemId;
	}

	public void setOrderLineItemId(java.lang.Integer orderLineItemId) {
		this.orderLineItemId = orderLineItemId;
	}

	public java.lang.Long getOrderId() {
		return orderId;
	}

	public void setOrderId(java.lang.Long orderId) {
		this.orderId = orderId;
	}

	public java.lang.Long getOrderItemsId() {
		return orderItemsId;
	}

	public void setOrderItemsId(java.lang.Long orderItemsId) {
		this.orderItemsId = orderItemsId;
	}

	public java.lang.String getSalesOrderId() {
		return salesOrderId;
	}

	public void setSalesOrderId(java.lang.String salesOrderId) {
		this.salesOrderId = salesOrderId;
	}

	public char getOrdItemStat() {
		return ordItemStat;
	}

	public void setOrdItemStat(char ordItemStat) {
		this.ordItemStat = ordItemStat;
	}

	public String getOrdrDate() {
		return ordrDate;
	}

	public void setOrdrDate(String ordrDate) {
		this.ordrDate = ordrDate;
	}

	public java.lang.String getManifest() {
		return manifest;
	}

	public void setManifest(java.lang.String manifest) {
		this.manifest = manifest;
	}

	public java.lang.String getInvoice() {
		return invoice;
	}

	public void setInvoice(java.lang.String invoice) {
		this.invoice = invoice;
	}

	public java.lang.String getBol() {
		return bol;
	}

	public void setBol(java.lang.String bol) {
		this.bol = bol;
	}

	public java.lang.String getPod() {
		return pod;
	}

	public void setPod(java.lang.String pod) {
		this.pod = pod;
	}

	public java.lang.Double getQuanity() {
		return quanity;
	}

	public void setQuanity(java.lang.Double quanity) {
		this.quanity = quanity;
	}

	public java.sql.Timestamp getTimeShipped() {
		return timeShipped;
	}

	public void setTimeShipped(java.sql.Timestamp timeShipped) {
		this.timeShipped = timeShipped;
	}

	public java.lang.String getItemStatus() {
		return itemStatus;
	}

	public void setItemStatus(java.lang.String itemStatus) {
		this.itemStatus = itemStatus;
	}

	public Timestamp getOrderDate() {
		try {
			fromOrdDate = dateFormat.parse(orderDate);
			fromODate = fromOrdDate.getTime();
			fOrdDate = new Timestamp(fromODate);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return fOrdDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
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
		return orderLineItemId;
	}

	/**
	 * @param orderLineItem_Id
	 *            the orderLineItem_Id to set
	 */
	public void setOrderLineItem_Id(java.lang.Integer orderLineItem_Id) {
		this.orderLineItemId = orderLineItem_Id;
	}

	/**
	 * @return the orderItems_Id
	 */
	public java.lang.Long getOrderItems_Id() {
		return orderItemsId;
	}

	/**
	 * @param orderItems_Id
	 *            the orderItems_Id to set
	 */
	public void setOrderItems_Id(java.lang.Long orderItems_Id) {
		this.orderItemsId = orderItems_Id;
	}

	/**
	 * @return the partNumber
	 */
	public java.lang.String getPartNumber() {
		return partNumber;
	}

	/**
	 * @param partNumber
	 *            the partNumber to set
	 */
	public void setPartNumber(java.lang.String partNumber) {
		this.partNumber = partNumber;
	}

	/**
	 * @return the salesOrder_Id
	 */
	public java.lang.String getSalesOrder_Id() {
		return salesOrderId;
	}

	/**
	 * @param salesOrder_Id
	 *            the salesOrder_Id to set
	 */
	public void setSalesOrder_Id(java.lang.String salesOrder_Id) {
		this.salesOrderId = salesOrder_Id;
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
	 * @return the ordItemStatus
	 */
	public Character getOrdItemStatus() {

		return ordItemStat;
	}

	/**
	 * @param ordItemStatus
	 *            the ordItemStatus to set
	 */
	public void setOrdItemStatus(java.lang.Character ordItemStatus) {
		this.ordItemStat = ordItemStatus;
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
	public void populate() throws Exception {
		try {
			getOrderItemRecords();

		} catch (Exception e1) {
			e1.printStackTrace();

		}

	}

	/**
	 * @return the formattedEstDelDate
	 */
	public java.lang.String getFormattedEstDelDate() {
		return formattedEstDelDate;
	}

	/**
	 * @param formattedEstDelDate
	 *            the formattedEstDelDate to set
	 */
	public void setFormattedEstDelDate(java.lang.String formattedEstDelDate) {
		this.formattedEstDelDate = formattedEstDelDate;
	}

	/**
	 * @return the formattedTimeShipped
	 */
	public java.lang.String getFormattedTimeShipped() {
		return formattedTimeShipped;
	}

	/**
	 * @param formattedTimeShipped
	 *            the formattedTimeShipped to set
	 */
	public void setFormattedTimeShipped(java.lang.String formattedTimeShipped) {
		this.formattedTimeShipped = formattedTimeShipped;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(int count) {
		this.count = count;
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

}