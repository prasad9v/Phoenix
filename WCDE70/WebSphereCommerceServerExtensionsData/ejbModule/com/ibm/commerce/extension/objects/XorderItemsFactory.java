package com.ibm.commerce.extension.objects;

import com.ibm.etools.ejb.client.runtime.*;

/**
 * XorderItemsFactory
 * @generated
 */
public class XorderItemsFactory extends AbstractEJBFactory {

	/**
	 * XorderItemsFactory
	 * @generated
	 */
	public XorderItemsFactory() {
		super();
	}

	/**
	 * _acquireXorderItemsHome
	 * @generated
	 */
	protected com.ibm.commerce.extension.objects.XorderItemsHome _acquireXorderItemsHome()
		throws java.rmi.RemoteException {
		return (com.ibm.commerce.extension.objects.XorderItemsHome) _acquireEJBHome();
	}

	/**
	 * acquireXorderItemsHome
	 * @generated
	 */
	public com.ibm.commerce.extension.objects.XorderItemsHome acquireXorderItemsHome()
		throws javax.naming.NamingException {
		return (com.ibm.commerce.extension.objects.XorderItemsHome) acquireEJBHome();
	}

	/**
	 * getDefaultJNDIName
	 * @generated
	 */
	public String getDefaultJNDIName() {
		return "ejb/com/ibm/commerce/extension/objects/XorderItemsHome";
	}

	/**
	 * getHomeInterface
	 * @generated
	 */
	protected Class getHomeInterface() {
		return com.ibm.commerce.extension.objects.XorderItemsHome.class;
	}

	/**
	 * resetXorderItemsHome
	 * @generated
	 */
	public void resetXorderItemsHome() {
		resetEJBHome();
	}

	/**
	 * setXorderItemsHome
	 * @generated
	 */
	public void setXorderItemsHome(
		com.ibm.commerce.extension.objects.XorderItemsHome home) {
		setEJBHome(home);
	}

	/**
	 * findfindByCompositeIds
	 * @generated
	 */
	public com.ibm.commerce.extension.objects.XorderItems findfindByCompositeIds(
		java.lang.Integer orderLineItem_Id,
		java.lang.Long orders_Id,
		java.lang.Long orderItems_Id,
		java.lang.String salesOrder_Id)
		throws javax.ejb.FinderException,
		java.rmi.RemoteException {
		return _acquireXorderItemsHome().findfindByCompositeIds(
			orderLineItem_Id,
			orders_Id,
			orderItems_Id,
			salesOrder_Id);
	}

	/**
	 * create
	 * @generated
	 */
	public com.ibm.commerce.extension.objects.XorderItems create(
		java.lang.Integer orderLineItem_Id,
		java.lang.Long orders_Id,
		java.lang.Long orderItems_Id,
		java.lang.String salesOrder_Id)
		throws javax.ejb.CreateException,
		java.rmi.RemoteException {
		return _acquireXorderItemsHome().create(
			orderLineItem_Id,
			orders_Id,
			orderItems_Id,
			salesOrder_Id);
	}

	/**
	 * findByPrimaryKey
	 * @generated
	 */
	public com.ibm.commerce.extension.objects.XorderItems findByPrimaryKey(
		com.ibm.commerce.extension.objects.XorderItemsKey primaryKey)
		throws javax.ejb.FinderException,
		java.rmi.RemoteException {
		return _acquireXorderItemsHome().findByPrimaryKey(primaryKey);
	}

	/**
	 * findbysalesOrder_id
	 * @generated
	 */
	public java.util.Enumeration findbysalesOrder_id(
		java.lang.String salesorder_id)
		throws javax.ejb.FinderException,
		java.rmi.RemoteException {
		return _acquireXorderItemsHome().findbysalesOrder_id(salesorder_id);
	}

	/**
	 * findByOrderItemId
	 * @generated
	 */
	public java.util.Enumeration findByOrderItemId(java.lang.Long orderItemId)
		throws javax.ejb.FinderException,
		java.rmi.RemoteException {
		return _acquireXorderItemsHome().findByOrderItemId(orderItemId);
	}

	/**
	 * create
	 * @generated
	 */
	public com.ibm.commerce.extension.objects.XorderItems create(
		java.lang.Integer orderLineItem_Id,
		java.lang.Long orders_Id,
		java.lang.Long orderItems_Id,
		java.lang.String salesOrder_Id,
		java.math.BigDecimal totalWeight,
		java.lang.String weighType,
		java.lang.String hotOrder,
		java.lang.String sourcing_Id,
		java.lang.String ordStatus,
		java.lang.Character ordItemStatus,
		java.sql.Timestamp estimateDeliveryDate,
		java.lang.String bolIdentifier,
		java.lang.String manifestIdentifier,
		java.lang.String invoiceIdentifier,
		java.lang.String sourceCountry,
		java.lang.String sourceState,
		java.lang.String sourceCity,
		java.lang.String unit,
		java.lang.String includeInTonnage)
		throws javax.ejb.CreateException,
		java.rmi.RemoteException {
		return _acquireXorderItemsHome().create(
			orderLineItem_Id,
			orders_Id,
			orderItems_Id,
			salesOrder_Id,
			totalWeight,
			weighType,
			hotOrder,
			sourcing_Id,
			ordStatus,
			ordItemStatus,
			estimateDeliveryDate,
			bolIdentifier,
			manifestIdentifier,
			invoiceIdentifier,
			sourceCountry,
			sourceState,
			sourceCity,
			unit,
			includeInTonnage);
	}
}
