package com.ibm.commerce.domtar.commands;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import au.com.bytecode.opencsv.CSVReader;

import com.ibm.commerce.command.CommandFactory;
import com.ibm.commerce.command.ControllerCommandImpl;
import com.ibm.commerce.datatype.TypedProperty;
import com.ibm.commerce.domtar.server.DomtarServerHelper;
import com.ibm.commerce.domtar.util.DomtarDataload;
import com.ibm.commerce.domtar.util.DomtarJDBCHelper;
import com.ibm.commerce.domtar.util.DomtarOrder;
import com.ibm.commerce.domtar.util.DomtarOrderItem;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.foundation.common.util.logging.LoggingHelper;
import com.ibm.commerce.order.commands.SalesOrderPreprocessCmd;
import com.ibm.commerce.server.TransactionManager;

public class DomtarOrderCreateCmdImpl extends ControllerCommandImpl  implements  DomtarOrderCreateCmd {

	private static final String CLASSNAME = DomtarOrderCreateCmdImpl.class.getName();
	private static final Logger LOGGER = LoggingHelper.getLogger(DomtarOrderCreateCmdImpl.class);
	
	private static final String[] backendSystemArray = {"Baan","SAP"};
	
	private static final String orderCSVPath ="//home//wcuser//order//";
	
	private static final String orderCSVPathInDev ="E://IBM//Stub//";

	//http://localhost/webapp/wcs/stores/servlet/DomtarOrderCreateCmd?storeId=10201&catalogId=10651
	public void performExecute() throws ECException {
		
		long t1 = System.currentTimeMillis();
		
//		String filename = "//tmp//orderUpload//Stub//DomtarOrder.csv";
//		
//		if(DomtarServerHelper.isDevelopmentEnvironment()){
//			filename = "E://IBM//Stub//DomtarOrder.csv";
//		}
		String filePath = orderCSVPath;
		if(DomtarServerHelper.isDevelopmentEnvironment()){
			filePath = orderCSVPathInDev;
		}
		
		DomtarDataload dataload = new DomtarDataload();
		String[] fileToBeProcessed = null;

		for (String backendSystem : backendSystemArray) {
			String lastUploadedFileName = dataload.getLastFileSuccessfullyUploaded(backendSystem,filePath);
			List<String> sortedFileName = dataload.getSortedCSVToBeUploadedFileList(backendSystem,filePath);
			
			int sortedFileSize = sortedFileName.size();
			
			// Check if there is any file to process.
			if (sortedFileSize != 0) {
				
				// Check if all the files are already processed.
				if (!sortedFileName.get(sortedFileSize -1).equals(lastUploadedFileName)) {
					String[] files = new String[sortedFileSize];
					files = sortedFileName.toArray(files);
					
					//While running initially lastUploadedFileName is null.
					if(lastUploadedFileName == null){
						fileToBeProcessed = new String[files.length];
						System.arraycopy(files, 0, fileToBeProcessed, 0, fileToBeProcessed.length);
					}else{
						for (int i = 0; i < files.length; i++) {
							if (lastUploadedFileName.equals(files[i])) {
								// Get the list of file to be processed.
								fileToBeProcessed = new String[files.length - (i+1)]; 
								System.arraycopy(files, (i+1), fileToBeProcessed, 0, (files.length - (i+1)));
								
								break;
							}
						}
					}					
					if (null != fileToBeProcessed) {
						for (String fileName : fileToBeProcessed) {
							LOGGER.info("Processing of file : "+fileName+" started.");							
							
							//Start processing the Domtar csv file.
							processDomtarSalesOrder(filePath+fileName);
							
							// Append to successfully uploaded list.
							dataload.appendToSuccessfullyUploadedList(backendSystem, fileName,filePath);
							LOGGER.info("Processing of file : "+fileName+" completed.");
						}
					}
					
				} else {
					LOGGER.info("There are no new "+backendSystem+" files available for processing.");
				}
			} else {
				LOGGER.info("No "+backendSystem+" files are available for processing.");
			}
		}
		 
		long t2 = System.currentTimeMillis();
		LOGGER.info("Time taken to process the orders is : " + (t2-t1));
	}

	private void processDomtarSalesOrder(String filename) throws ECException {
		HashMap iterationmap = new HashMap();

		String customerId = this.requestProperties.getString("customerId");
		String contractId = this.requestProperties.getString("contractId");
		
		try {
			CSVReader reader = new CSVReader(new FileReader(filename));
			List<?> content = reader.readAll();
			
			Map<DomtarOrder, ArrayList<DomtarOrderItem>> orders = getDomatarSalesOrderList(content);
			
			for (Map.Entry<DomtarOrder, ArrayList<DomtarOrderItem>> order : orders.entrySet()) {
				DomtarOrder domtarOrder = order.getKey();
				
				String domtarOrderId = domtarOrder.getOrderId();
				TypedProperty orderProps = new TypedProperty();
				
				orderProps.put("customerPO", domtarOrder.getCustomerPO());
				orderProps.put("customerId", customerId);
				
				orderProps.put("orderDate", domtarOrder.getOrderDate());
				orderProps.put("ormOrderId", domtarOrderId);
				
				String soldToAddressId = null;
				try {
					DomtarJDBCHelper domtarHelper = new DomtarJDBCHelper();
					soldToAddressId = domtarHelper.findAddressByNickName(domtarOrder.getSoldTo());
				} catch (Exception e) {
					LOGGER.info("Error in getting soldTo address for sales order : " + domtarOrderId + " with soldTo "+ domtarOrder.getSoldTo() +". Skiping the order.");
					// Since order can not be processed without soldTo address, leaving 
					// this order and continuing with next order.
					continue;
				}
				if(soldToAddressId == null){
					LOGGER.info("Error in getting soldTo address for sales order : " + domtarOrderId + " with soldTo "+ domtarOrder.getSoldTo() +". Skiping the order.");
					// Since order can not be processed without soldTo address, leaving 
					// this order and continuing with next order.
					continue;
				}
				orderProps.put("soldTo", domtarOrder.getSoldTo());				
				
				orderProps.put("storeId", this.getStoreId());
				orderProps.put("langId", "-1");
				orderProps.put("Locale", "en_US");
				orderProps.put("URL", "NoURL");
				orderProps.put("ReUrl", "NoReURL");

				String orderStatus = domtarOrder.getOrderStatus();
				orderProps.put("orderStatus", orderStatus);
				
				LOGGER.info("Domtar OrderId: " + domtarOrderId + " orderStatus: " + orderStatus);
				
				// Get WCS orderId if it is not open order.
				String orderId = null;
				try {
					DomtarJDBCHelper domtarOrderJDBC = new DomtarJDBCHelper();
					orderId = domtarOrderJDBC.findOrderByOrmOrderId(domtarOrderId);
					if (null != orderId) {
						orderProps.put("orderId", orderId);
						LOGGER.info("WCS OrderId: " + orderId);
					}
				} catch (SQLException e) {
					LOGGER.info("Error in getting WCS orderId for sales order "+domtarOrderId+"." + e.getMessage());
					
					// This order can not be processed because of the error in getting WCS orderId. 
					// Continue with next order.
					continue;
				}
				if (orderId == null && DomtarSalesOrderPreprocessCmdImpl.ORDER_STATUS_CHANGED.equals(orderStatus)) {
					/*
					 * When encore receives the sales order for the first time with orderstatus as 'Changed',
					 * then process the order. 
					 */
					orderProps.put("orderStatus", DomtarSalesOrderPreprocessCmdImpl.ORDER_STATUS_OPEN);
					orderStatus = DomtarSalesOrderPreprocessCmdImpl.ORDER_STATUS_OPEN;
				}
				if(orderId == null && !DomtarSalesOrderPreprocessCmdImpl.ORDER_STATUS_OPEN.equals(orderStatus)){
					/*
					 * This order can not be processed because of the error in getting WCS orderId. 
					 * An order should be in open status. Continue with next order.
					 */
					LOGGER.info("Domtar Order "+domtarOrderId+" with status as "+orderStatus+" does not exist in WCS database. Skipping this order....");
					continue;
				}

				if (null != orderId && DomtarSalesOrderPreprocessCmdImpl.ORDER_STATUS_OPEN.equals(orderStatus)) {
					// WCS order Id exists. Update the orderStatus to changed, if it is an open order.
					orderProps.put("orderStatus", DomtarSalesOrderPreprocessCmdImpl.ORDER_STATUS_CHANGED);
					orderStatus = DomtarSalesOrderPreprocessCmdImpl.ORDER_STATUS_CHANGED;
				}
				
				int i = 1;
				for(DomtarOrderItem orderItem : order.getValue()) {
					String orderItemId = orderItem.getOrderItemId();
					String itemCode = orderItem.getItemCode();
					String quantity = orderItem.getQuantity();
					
					if (null == orderItemId || null == itemCode || null == quantity) {
						LOGGER.info("There is not enough information to added this item to the cart.");
						continue;
					}
					String totalWeight = orderItem.getTotalWeight();
					String weightType = orderItem.getWeightType();
					String plannedShipDate = orderItem.getPlannedShipDate();
					String hotOrder = orderItem.getHotOrder();
					String orderItemStatus = orderItem.getItemStatus();
					String shipTo = orderItem.getShipTo();
					String sourcingId = orderItem.getSourcingId();
					
					String shipDate = orderItem.getShipDate();
					
					String unit = orderItem.getUnit();
					String itemStatus = orderItem.getItemStatus();
					String deliveryDate = orderItem.getDeliveryDate();
					String estimateDeliveryDate = orderItem.getEstimateDeliveryDate();
					String bolIdentifier = orderItem.getBolIdentifier();
					String manifestIdentifier = orderItem.getManifestIdentifier();
					String invoiceIdentifier = orderItem.getInvoiceIdentifier();
					String itemPrice = orderItem.getItemPrice();
					String sourceCountry = orderItem.getCountry();
					String sourceState = orderItem.getState();
					String sourceCity = orderItem.getCity();
					String includeInTonnage = orderItem.getIncludeInTonnage();
					
					/*
					 * Get WCS orderItemId.
					 * A line item can be uniquely identified by the following three columns in csv.
					 * DomtarSalesOrder + OrderLineId + bolIdentifier.
					 * So appending the OrderLineId + bolIdentifier to query the DB along with WCS orderId
					 * to fetch WCS OrderItemId.
					 */
					String uniqueIdentifier = orderItemId;
					if(bolIdentifier != null){
						uniqueIdentifier = orderItemId + bolIdentifier;
					}
					orderProps.put("uniqueIdentifier_" + i, uniqueIdentifier);
					orderProps.put("field2_" + i, uniqueIdentifier);
					if (!DomtarSalesOrderPreprocessCmdImpl.ORDER_STATUS_OPEN.equals(orderStatus)
							&& !DomtarSalesOrderPreprocessCmdImpl.ORDER_STATUS_DELETED.equals(orderStatus)) {
						
						DomtarJDBCHelper domtarOrderJDBC = new DomtarJDBCHelper(); 
						try {
							String wcsOrderItemId = domtarOrderJDBC.findOrderItemIdByOrderIdAndDomtarItemCode(orderId,uniqueIdentifier);
							orderProps.put("orderItemId_" + i, wcsOrderItemId);
							LOGGER.info("WCS OrderItemId: " + wcsOrderItemId);
						} catch(Exception e) {
							// Ignore the error while getting orderItemId as this may be a new item 
							// added to existing order (with order status changed to 'changed') 
							LOGGER.info("Error in getting orderItemId for " + orderItemId);
						}
					}
					
					orderProps.put("orderLineItemId_" + i, orderItemId);
					orderProps.put("salesItemId_" + i, domtarOrderId);
					
					orderProps.put("partNumber_" + i, itemCode);
					orderProps.put("UOM_" + i, "C62");
					//orderProps.put("shipModeId_" + i, orderItem.getShipModeId());
					orderProps.put("quantity_" + i, quantity);

					String shipToAddressId = null;
					try {
						DomtarJDBCHelper domtarHelper = new DomtarJDBCHelper();
						shipToAddressId = domtarHelper.findAddressByNickName(orderItem.getShipTo());
					} catch (Exception e) {
						LOGGER.info("Error in getting shipTo address for " + orderItemId);

						// ShipTo address can not be found for the orderItem, use dummy address: 
						// TODO: Get default shipTo addressId
						shipToAddressId = "";
					}
					if(shipToAddressId == null){
						LOGGER.info("Unable to retrieve shipTo address from encore DB for order " + domtarOrderId +" with orderItemId "+orderItemId +". Setting shipToAddressId as empty.");

						// ShipTo address can not be found for the orderItem, use dummy address: 
						// TODO: Get default shipTo addressId
						shipToAddressId = "";
					}
					orderProps.put("shipTo_" + i, shipToAddressId);
					
					if (null != plannedShipDate) 
						orderProps.put("plannedShipDate_" + i, plannedShipDate);
					if (null != shipDate) 
						orderProps.put("shipDate_" + i, shipDate);	
					if (null != deliveryDate) 
						orderProps.put("deliveryDate_" + i, deliveryDate);	
					if (null != totalWeight) 
						orderProps.put("totalWeight_" + i, totalWeight);	
					if (null != weightType) 
						orderProps.put("weightType_" + i, weightType);	
					if (null != hotOrder)
						orderProps.put("hotOrder_" + i, hotOrder);
					if (null != sourcingId)
						orderProps.put("sourcingId_" + i, sourcingId);
					if (null != unit) 
						orderProps.put("unit_" + i, unit);
					if (null != itemStatus) 
						orderProps.put("itemStatus_" + i, itemStatus);
					if (null != estimateDeliveryDate)
						orderProps.put("estimateDeliveryDate_" + i, estimateDeliveryDate);
					if (null != bolIdentifier)
						orderProps.put("bolIdentifier_" + i, bolIdentifier);
					if (null != manifestIdentifier)
						orderProps.put("manifestIdentifier_" + i, manifestIdentifier);
					if (null != invoiceIdentifier)
						orderProps.put("invoiceIdentifier_" + i, invoiceIdentifier);
					if (null != itemPrice)
						orderProps.put("itemPrice_" + i, itemPrice);
					if (null != sourceCountry)
						orderProps.put("sourceCountry_" + i, sourceCountry);
					if (null != sourceState)
						orderProps.put("sourceState_" + i, sourceState);
					if (null != sourceCity)
						orderProps.put("sourceCity_" + i, sourceCity);
					if (null != includeInTonnage)
						orderProps.put("includeInTonnage_" + i, includeInTonnage);
					
					orderProps.put("contractId_" + i, contractId);
					
					i++;
				} // for(DomtarOrderItem orderItem ...
				
				// Get Number of items in the order.
				// orderProps.put("orderItemCount_" + domtarOrderId, --i);
				
				try {
					SalesOrderPreprocessCmd cmd = (SalesOrderPreprocessCmd) CommandFactory.createCommand(
							SalesOrderPreprocessCmd.NAME, this.commandContext.getStoreId());
					cmd.setCommandContext(this.commandContext);
					cmd.setRequestProperties(orderProps);
					cmd.execute();
				} catch (Exception e) {
					LOGGER.info("Error in placing Domtar order " + domtarOrderId + " with soldTo "+ domtarOrder.getSoldTo() +". " + e.getMessage());
					TransactionManager.rollback();
				} finally {
					try {
						TransactionManager.commit();
					} catch(Exception e) {
						LOGGER.info("Error in commiting the transaction after placing the order " + domtarOrderId + ". " + e.getMessage());
					}
				}
				TransactionManager.begin();
				
			}// for (Map.Entry<DomtarOrder...
			
		} catch (Exception e) {
			LOGGER.info("Error in reading/placing domtar order. " + e.getMessage());
		}
		
	}

	/**
	 * Get Domtar sales order list.
	 * 
	 * @param content content in CSV file.
	 * @return <object>Map</object> of orders and orderItems.
	 */
	private Map<DomtarOrder, ArrayList<DomtarOrderItem>> getDomatarSalesOrderList(List <?> content) {
		Map<DomtarOrder, ArrayList<DomtarOrderItem>> csvOrders = new LinkedHashMap<DomtarOrder, ArrayList<DomtarOrderItem>>();
		for (Object object : content) {
			String[] column = (String[]) object;
			
			// Do not process the header of CSV file.
			if ("DomtarSalesOrder".equals(column[0]) || StringUtils.isEmpty(column[0])) {
				continue;
			}
			
			// Get order data.
			String orderId = column[0];
			String orderDate = column[1];
			String soldTo = column[2];
			String customerPO = column[3];
			String orderStatus = column[4];
			
			DomtarOrder domtarOrder = new DomtarOrder();
			domtarOrder.setOrderId(orderId);
			domtarOrder.setOrderDate(orderDate);
			domtarOrder.setSoldTo(soldTo);
			domtarOrder.setCustomerPO(customerPO);
			domtarOrder.setOrderStatus(orderStatus);
			
			// Get order item data
			String orderItemId = column[5];
			String totalWeight = column[6];
			String weightType = column[7];
			String plannedShipDate = column[8];
			String hotOrder = column[9];
			String orderItemStatus = column[10];
			String shipTo = column[11];
			String sourcingId = column[12];
			String itemCode = column[13];
			String shipDate = column[14];
			String quantity = column[15];
			String unit = column[16];
			String itemStatus = column[17];
			String deliveryDate = column[18];
			String estimateDeliveryDate = column[19];
			String bolIdentifier = column[20];
			String manifestIdentifier = column[21];
			String invoiceIdentifier = column[22];
			String itemPrice = column[23];
			String sourceCountry = column[24];
			String sourceState = column[25];
			String sourceCity = column[26];
			String includeInTonnage = column[27];
			
			DomtarOrderItem domtarOrderItem = new DomtarOrderItem();
			domtarOrderItem.setOrderItemId(orderItemId);
			domtarOrderItem.setTotalWeight(totalWeight);
			domtarOrderItem.setWeightType(weightType);
			domtarOrderItem.setPlannedShipDate(plannedShipDate);
			domtarOrderItem.setHotOrder(hotOrder);
			domtarOrderItem.setOrderItemStatus(orderItemStatus);
			domtarOrderItem.setShipTo(shipTo);
			domtarOrderItem.setSourcingId(sourcingId);
			domtarOrderItem.setItemCode(itemCode);
			domtarOrderItem.setShipDate(shipDate);
			domtarOrderItem.setQuantity(quantity);
			domtarOrderItem.setUnit(unit);
			domtarOrderItem.setItemStatus(itemStatus);
			domtarOrderItem.setDeliveryDate(deliveryDate);
			domtarOrderItem.setEstimateDeliveryDate(estimateDeliveryDate);
			domtarOrderItem.setBolIdentifier(bolIdentifier);
			domtarOrderItem.setManifestIdentifier(manifestIdentifier);
			domtarOrderItem.setInvoiceIdentifier(invoiceIdentifier);
			domtarOrderItem.setItemPrice(itemPrice);
			domtarOrderItem.setCountry(sourceCountry);
			domtarOrderItem.setState(sourceState);
			domtarOrderItem.setCity(sourceCity);
			domtarOrderItem.setIncludeInTonnage(includeInTonnage);
			
			// Add an orderitem to order if order already exits else add new order.
			if (csvOrders.containsKey(domtarOrder)) {
				ArrayList<DomtarOrderItem> csvOrderItems = csvOrders.get(domtarOrder);
				csvOrderItems.add(domtarOrderItem);
				csvOrders.put(domtarOrder, csvOrderItems);
			} else {
				ArrayList<DomtarOrderItem> csvOrderItems = new ArrayList<DomtarOrderItem>();
				csvOrderItems.add(domtarOrderItem);
				csvOrders.put(domtarOrder, csvOrderItems);
			}
      	}
		
		return csvOrders;
	}
}