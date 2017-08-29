package com.ibm.commerce.domtar.commands;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.logging.Logger;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;

import com.ibm.commerce.datatype.TypedProperty;
import com.ibm.commerce.exception.ECApplicationException;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.exception.ECSystemException;
import com.ibm.commerce.extension.objects.XorderItemsAccessBean;
import com.ibm.commerce.foundation.common.util.logging.LoggingHelper;
import com.ibm.commerce.order.commands.SalesOrderPreprocessCmdImpl;
import com.ibm.commerce.order.objects.OrderAccessBean;
import com.ibm.commerce.order.objects.OrderItemAccessBean;
import com.ibm.commerce.order.ras.WcOrderMessage;
import com.ibm.commerce.order.utils.OrderLockingHelper;
import com.ibm.commerce.ras.ECMessage;
import com.ibm.commerce.ras.ECMessageHelper;

public class DomtarSalesOrderPreprocessCmdImpl extends SalesOrderPreprocessCmdImpl {
	
	private static final String CLASSNAME = DomtarSalesOrderPreprocessCmdImpl.class.getName();
	private static final Logger LOGGER = LoggingHelper.getLogger(DomtarSalesOrderPreprocessCmdImpl.class);

	private static final Hashtable<String, String> DOMTAR_ORDER_STATUS = new Hashtable<String, String>(3);

	public static final String ORDER_STATUS_OPEN = "open";
	public static final String ORDER_STATUS_CHANGED = "changed";
	public static final String ORDER_STATUS_SHIPPED = "shipped";
	public static final String ORDER_STATUS_INVOICED = "invoiced";
	public static final String ORDER_STATUS_DELETED = "deleted";
	
	private static final String ORDER_STATUS = "orderStatus";
	private static final String UNCLOCKED_ORDER_FLAG = "0";
	private static final String CANCELED_ORDER_STATUS = "X";
	private static final String MERCHANT_ORDER_ID = "ormOrderId"; 
	
	static {
		DOMTAR_ORDER_STATUS.put("open", "I");
		DOMTAR_ORDER_STATUS.put("changed", "I");
		DOMTAR_ORDER_STATUS.put("shipped", "R");
		DOMTAR_ORDER_STATUS.put("invoiced", "C");
		DOMTAR_ORDER_STATUS.put("deleted", "X");
	}
	
	@Override
	public void performExecute() throws ECException {
		
		TypedProperty requestProps = this.getRequestProperties();
		//LOGGER.info("DomtarSalesOrderPreprocessCmdImpl.RequestProperties: \n" + requestProps);
		
		String merchantOrderId = requestProps.getString(MERCHANT_ORDER_ID, null);
		String orderStatus = requestProps.getString(ORDER_STATUS, null);
		
		String soldTo = requestProps.getString("soldTo", null);
		String orderDate = requestProps.getString("orderDate", null);
		String customerPO = requestProps.getString("customerPO", null);
		String orderId = requestProps.getString("orderId", null);
		String ormOrderId = requestProps.getString("ormOrderId", null);
		
		// Unlock the order before update
		if (ORDER_STATUS_CHANGED.equals(orderStatus) || ORDER_STATUS_SHIPPED.equals(orderStatus) || ORDER_STATUS_INVOICED.equals(orderStatus)) {
			try {
				OrderAccessBean oac = new OrderAccessBean(); 
				oac.setInitKey_orderId(orderId);
				oac.refreshCopyHelper();
				oac.setLock(UNCLOCKED_ORDER_FLAG);
				oac.commitCopyHelper();
				
				LOGGER.info("OrderId : " + orderId + " unlocked.");
				
			} catch (Exception e) {
				LOGGER.info("Error in unlocking order: " + orderId + ". "+ e.getMessage());
                throw new ECApplicationException(WcOrderMessage._ERR_ORDER_IS_NOT_LOCKED, getClass().getName(), 
                		"performExecute", ECMessageHelper.generateMsgParms(String.valueOf(orderId)));
			}
		}

		// Cancel order by changing the status to 'X'
		if (ORDER_STATUS_DELETED.equals(orderStatus)) {
			try {
				OrderAccessBean orderAcBean = new OrderAccessBean(); 
				orderAcBean.setInitKey_orderId(orderId);
				orderAcBean.refreshCopyHelper();
				orderAcBean.setLock(UNCLOCKED_ORDER_FLAG);
				orderAcBean.setStatus(CANCELED_ORDER_STATUS);
				orderAcBean.setLastUpdate(new Timestamp(System.currentTimeMillis()));
				orderAcBean.commitCopyHelper();
				
				OrderItemAccessBean abOrderItems[] = orderAcBean.getOrderItems();
				for (int i = 0; i < abOrderItems.length; i++) {
					abOrderItems[i].setStatus(CANCELED_ORDER_STATUS);
					abOrderItems[i].commitCopyHelper();
				}
				
			} catch (Exception e) {
				LOGGER.info("Error in canceling order: " + orderId + ". "+ e.getMessage());
			}
		} else {
			
			// Get lock on order before update if it is not an open order
			if (null != orderId) {
				OrderLockingHelper helper = new OrderLockingHelper();
				helper.lock(new Long(orderId), true);
			}

			super.performExecute();
			
			//LOGGER.info("DomtarSalesOrderPreprocessCmdImpl.ResponseProperties: \n" + this.responseProperties);
			updateOrder(soldTo, orderDate, customerPO, merchantOrderId, orderStatus, orderId);
			
			// Since the orderitem price is updated, the price of the order needs to be updated.
			setStatus("E");
			callOrderCalculate();
			clearCaches();
			callOrderPrepare();
			setOrderStatus();
			
		} //if
	}
	
	/**
	 * This method is used to update ORDERS, ORDERITEMS and XORDERITEMS tables. 
	 * 
	 * @param soldTo			SoldTo address
	 * @param orderDate			Order place date
	 * @param customerPO		Customer PO number
	 * @param merchantOrderId	Domtar orderId
	 * @param orderStatus		Order status
	 */
	private void updateOrder(String soldTo, String orderDate, String customerPO, String merchantOrderId, String orderStatus, String encoreOrderId) {
		
		String orderId = null;
		if (null != this.responseProperties) {
			orderId = this.responseProperties.getString("orderId", encoreOrderId);	
		} else {
			orderId = encoreOrderId;
		}
		
		if (null != orderId) {
			try {
				// Update the ORDERS fields
		    	OrderAccessBean orderAccessBean = new OrderAccessBean();
				orderAccessBean.setInitKey_orderId(orderId);
				orderAccessBean.refreshCopyHelper();
				orderAccessBean.setField1(soldTo);
		     	orderAccessBean.setPlaceOrderTime(orderDate);
				orderAccessBean.setField3(customerPO);
				orderAccessBean.setMerchantOrderId(merchantOrderId);
				orderAccessBean.setStatus(DOMTAR_ORDER_STATUS.get(this.requestProperties.getString("orderStatus")));	
				orderAccessBean.commitCopyHelper();
				
				// Update the order item fields
				OrderItemAccessBean[] orderItems = orderAccessBean.getOrderItems();
				int itemsCount = orderItems.length;
				int j = 1;
				for (OrderItemAccessBean orderItem : orderItems) {
					
					//Check whether we are updating additional fields to the correct orderitem
					for (int i = 1; i <= itemsCount; i++) {
						String field2 = orderItem.getField2();
						String uniqueIdentifier = requestProperties.getString("uniqueIdentifier_" + i);
						
						if(field2.equals(uniqueIdentifier)){
							//We are OK to update the additional fields.
							String shipTo = requestProperties.getString("shipTo_" + i, null);
							String shipDate = requestProperties.getString("shipDate_" + i, null);
							if("".equals(shipDate)){
								shipDate = null;
							}
							String deliveryDate = requestProperties.getString("deliveryDate_" + i, null);
							if("".equals(deliveryDate)){
								deliveryDate = null;
							}
							String plannedShipDate = requestProperties.getString("plannedShipDate_" + i, null);
							if("".equals(plannedShipDate)){
								plannedShipDate = null;
							}
							String itemPrice = requestProperties.getString("itemPrice_" + i, null);
							
							String orderItemId = orderItem.getOrderItemId();
							
							//Update the shipTo id
							if(!"".equals(shipTo)){
								orderItem.setField1(shipTo);
							}
							
							//Update Field2 with uniqueIdentifier(orderLineItemId+bolIdentifier)
							//orderItem.setField2(requestProperties.getString("uniqueIdentifier_" + i));
							orderItem.setTimeReleased((null != shipDate? Timestamp.valueOf(shipDate): null));
							orderItem.setLastUpdate((null != deliveryDate? Timestamp.valueOf(deliveryDate): null));
							orderItem.setRequestedShipDate((null != plannedShipDate? Timestamp.valueOf(plannedShipDate): null));
							
							// Update the item price with Domtar specific item price.
							if (null != itemPrice) {
								orderItem.setPrice(itemPrice);
								orderItem.setPrepareFlags(2);
							}
							
							orderItem.setStatus(DOMTAR_ORDER_STATUS.get(this.requestProperties.getString("itemStatus_" + i)));	
							orderItem.commitCopyHelper();
							
							// Add/Update XORDITEMS table
							String totalWeight = requestProperties.getString("totalWeight_" + i, null);

							String estimateDeliveryDate = requestProperties.getString("estimateDeliveryDate_" + i, null);
							if("".equals(estimateDeliveryDate)){
								estimateDeliveryDate = null;
							}
							String itemStatus = requestProperties.getString("itemStatus_" + i, null);
							String sourceCountry = requestProperties.getString("sourceCountry_" + i, null);
							
							String sourceState = requestProperties.getString("sourceState_" + i, null);
							
							String sourceCity = requestProperties.getString("sourceCity_" + i, null);
							String includeInTonnage = requestProperties.getString("includeInTonnage_" + i, null);
							/*
							 * TODO: Since there was no unit column in xorditems table earlier, we had appended unit with
							 * weight type. On 16th Jan 2014 a new column unit is added to xorditems table. Once the unit
							 * column have values, then change the logic to read from the unit column.
							 */
							
							if (ORDER_STATUS_OPEN.equals(orderStatus)) {
								XorderItemsAccessBean xOrderItemsAccessBean = new XorderItemsAccessBean(
									new Integer(requestProperties.getString("orderLineItemId_" + i)),
									new Long(orderId), new Long(orderItemId),
									requestProperties.getString("salesItemId_" + i), 
									(null != totalWeight? new java.math.BigDecimal(totalWeight): null),
									requestProperties.getString("weightType_" + i, null)+"**#**"+
									(requestProperties.getString("unit_" + i, null)), 
									requestProperties.getString("hotOrder_" + i, null),
									requestProperties.getString("sourcingId_" + i, null),
									orderStatus.substring(0, 2), (StringUtils.isEmpty(itemStatus)? 'I' : itemStatus.charAt(0)), 
									(null != estimateDeliveryDate? Timestamp.valueOf(estimateDeliveryDate): null),
									requestProperties.getString("bolIdentifier_" + i, null), 
									requestProperties.getString("manifestIdentifier_" + i, null),
									requestProperties.getString("invoiceIdentifier_" + i, null),sourceCountry,
									sourceState,sourceCity,requestProperties.getString("unit_" + i, null),includeInTonnage);
							} else {
								try {
									XorderItemsAccessBean xOrderItemsAccessBean = new XorderItemsAccessBean();
									xOrderItemsAccessBean.setInitKey_orderItems_Id(new Long(orderItemId));
									xOrderItemsAccessBean.setInitKey_orderLineItem_Id(new Integer(requestProperties.getString("orderLineItemId_" + i)));
									xOrderItemsAccessBean.setInitKey_orders_Id(new Long(orderId));
									xOrderItemsAccessBean.setInitKey_salesOrder_Id(requestProperties.getString("salesItemId_" + i));
									xOrderItemsAccessBean.refreshCopyHelper();
									
									xOrderItemsAccessBean.setHotOrder(requestProperties.getString("hotOrder_" + i, null));
									xOrderItemsAccessBean.setEstimateDeliveryDate((null != estimateDeliveryDate? Timestamp.valueOf(estimateDeliveryDate): null));
									xOrderItemsAccessBean.setInvoiceIdentifier(requestProperties.getString("invoiceIdentifier_" + i, null));
									xOrderItemsAccessBean.setBolIdentifier(requestProperties.getString("bolIdentifier_" + i, null));
									xOrderItemsAccessBean.setManifestIdentifier(requestProperties.getString("manifestIdentifier_" + i, null));
									xOrderItemsAccessBean.setIncludeInTonnage(includeInTonnage);
									xOrderItemsAccessBean.commitCopyHelper();
								} catch(Exception e) {
									
									LOGGER.info("Error in updating XORDERITEMS table, trying to add new record...");
									// Try to add a record in XORDERITEMS table if there is any error in update because this may 
									// be his may be a new item added to existing order (with order status changed to 'changed')  
									XorderItemsAccessBean xOrderItemsAccessBean = new XorderItemsAccessBean(
										new Integer(requestProperties.getString("orderLineItemId_" + i)),
										new Long(orderId), new Long(orderItemId),
										requestProperties.getString("salesItemId_" + i), 
										(null != totalWeight? new java.math.BigDecimal(totalWeight): null),
										requestProperties.getString("weightType_" + i, null)+"**#**"+
										(requestProperties.getString("unit_" + i, null)),  
										requestProperties.getString("hotOrder_" + i, null),
										requestProperties.getString("sourcingId_" + i, null),
										orderStatus.substring(0, 2), (StringUtils.isEmpty(itemStatus)? 'I' : itemStatus.charAt(0)), 
										(null != estimateDeliveryDate? Timestamp.valueOf(estimateDeliveryDate): null),
										requestProperties.getString("bolIdentifier_" + i, null), 
										requestProperties.getString("manifestIdentifier_" + i, null),
										requestProperties.getString("invoiceIdentifier_" + i, null),sourceCountry,
										sourceState,sourceCity,requestProperties.getString("unit_" + i, null),includeInTonnage);
									
									LOGGER.info("Adding a record to XORDERITEMS table is successful...");
								}
							}
							break;
						}
					}				
					
					j++;
				}
			} catch (Exception e) {
				LOGGER.info("Error in adding custom detail to an order " + orderId + ". \n" + e.getMessage());
			} 
		}// if
	}

	/**
	 * Set order and orderitem status.
	 * 
	 * @throws ECException
	 */
	private void setOrderStatus() throws ECException {
		String strMethod = "setOrderStatus";
		try {
			OrderAccessBean abOrder = new OrderAccessBean();
			abOrder.setInitKey_orderId(getOrderId().toString());
			abOrder.refreshCopyHelper();
			abOrder.setStatus(DOMTAR_ORDER_STATUS.get(this.requestProperties.getString("orderStatus")));
			abOrder.commitCopyHelper();
			OrderItemAccessBean abOrderItems[] = abOrder.getOrderItems();
			int orderItemCount = abOrderItems.length;
			for (int i = 0; i < abOrderItems.length; i++) {
				//Check whether we are updating correct orderitem status
				for (int j = 1; j <= orderItemCount; j++) {
					String field2 = abOrderItems[i].getField2();
					String uniqueIdentifier = requestProperties.getString("uniqueIdentifier_" + j);
					
					if(field2.equals(uniqueIdentifier)){
						//We are OK to update the orderitem status.
						abOrderItems[i].setStatus(DOMTAR_ORDER_STATUS.get(this.requestProperties.getString("itemStatus_" + j)));
						abOrderItems[i].commitCopyHelper();
						break;
					}
				}
			}

		} catch (CreateException e) {
			throw new ECSystemException(ECMessage._ERR_CREATE_EXCEPTION, getClass().getName(), 
					"setStatus", new Object[] { e.toString() }, e);
		} catch (FinderException e) {
			throw new ECSystemException(ECMessage._ERR_FINDER_EXCEPTION, getClass().getName(), 
					"setStatus", new Object[] { e.toString() }, e);
		} catch (NamingException e) {
			throw new ECSystemException(ECMessage._ERR_NAMING_EXCEPTION, getClass().getName(), 
					"setStatus", new Object[] { e.toString() }, e);
		} catch (RemoteException e) {
			throw new ECSystemException(ECMessage._ERR_REMOTE_EXCEPTION, getClass().getName(), 
					"setStatus", new Object[] { e.toString() }, e);
		}
	}
}