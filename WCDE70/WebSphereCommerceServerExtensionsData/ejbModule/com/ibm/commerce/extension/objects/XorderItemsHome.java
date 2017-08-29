package com.ibm.commerce.extension.objects;

/**
 * Home interface for Enterprise Bean: XorderItems
 */
public interface XorderItemsHome extends javax.ejb.EJBHome {

	/**
	 * Finds an instance using a key for Entity Bean: XorderItems
	 */
	public com.ibm.commerce.extension.objects.XorderItems findByPrimaryKey(
		com.ibm.commerce.extension.objects.XorderItemsKey primaryKey)
		throws javax.ejb.FinderException,
		java.rmi.RemoteException;

	public com.ibm.commerce.extension.objects.XorderItems findfindByCompositeIds(
		java.lang.Integer orderLineItem_Id,
		java.lang.Long orders_Id,
		java.lang.Long orderItems_Id,
		java.lang.String salesOrder_Id)
		throws javax.ejb.FinderException,
		java.rmi.RemoteException;

	public java.util.Enumeration findbysalesOrder_id(
		java.lang.String salesorder_id)
		throws javax.ejb.FinderException,
		java.rmi.RemoteException;

	public java.util.Enumeration findByOrderItemId(java.lang.Long orderItemId)
		throws javax.ejb.FinderException,
		java.rmi.RemoteException;

	/**
	 * @param orderLineItem_Id
	 * @param orders_Id
	 * @param orderItems_Id
	 * @param salesOrder_Id
	 * @param totalWeight
	 * @param weighType
	 * @param hotOrder
	 * @param sourcing_Id
	 * @param ordStatus
	 * @param ordItemStatus
	 * @param estimateDeliveryDate
	 * @param bolIdentifier
	 * @param manifestIdentifier
	 * @param invoiceIdentifier
	 * @param sourceCountry
	 * @param sourceState
	 * @param sourceCity
	 * @param unit
	 * @param includeInTonnage
	 * @return
	 * @throws javax.ejb.CreateException
	 * @throws java.rmi.RemoteException
	 */
	public com.ibm.commerce.extension.objects.XorderItems create(
			java.lang.Integer orderLineItem_Id, java.lang.Long orders_Id,
			java.lang.Long orderItems_Id, java.lang.String salesOrder_Id,
			java.math.BigDecimal totalWeight, java.lang.String weighType,
			java.lang.String hotOrder, java.lang.String sourcing_Id,
			java.lang.String ordStatus, java.lang.Character ordItemStatus,
			java.sql.Timestamp estimateDeliveryDate,
			java.lang.String bolIdentifier,
			java.lang.String manifestIdentifier,
			java.lang.String invoiceIdentifier, java.lang.String sourceCountry,
			java.lang.String sourceState, java.lang.String sourceCity,
			java.lang.String unit, java.lang.String includeInTonnage)
			throws javax.ejb.CreateException, java.rmi.RemoteException;

	/**
	 * @param orderLineItem_Id
	 * @param orders_Id
	 * @param orderItems_Id
	 * @param salesOrder_Id
	 * @return
	 * @throws javax.ejb.CreateException
	 * @throws java.rmi.RemoteException
	 */
	public com.ibm.commerce.extension.objects.XorderItems create(
			java.lang.Integer orderLineItem_Id, java.lang.Long orders_Id,
			java.lang.Long orderItems_Id, java.lang.String salesOrder_Id)
			throws javax.ejb.CreateException, java.rmi.RemoteException;
}
