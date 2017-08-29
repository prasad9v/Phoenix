package com.ibm.commerce.extension.objects;

import javax.rmi.*;
import com.ibm.ivj.ejb.runtime.*;

/**
 * XorderItemsAccessBean
 * @generated
 */
public class XorderItemsAccessBean extends AbstractEntityAccessBean
	implements
		com.ibm.commerce.extension.objects.XorderItemsAccessBeanData {

	/**
	 * @generated
	 */
	private XorderItems __ejbRef;

	/**
	 * @generated
	 */
	private Integer initKey_orderLineItem_Id;

	/**
	 * @generated
	 */
	private Long initKey_orders_Id;

	/**
	 * @generated
	 */
	private Long initKey_orderItems_Id;

	/**
	 * @generated
	 */
	private String initKey_salesOrder_Id;

	/**
	 * getOrderLineItemId
	 * @generated
	 */
	public java.lang.Integer getOrderLineItemId()
		throws java.rmi.RemoteException,
		javax.ejb.CreateException,
		javax.ejb.FinderException,
		javax.naming.NamingException {
		return (((java.lang.Integer) __getCache("orderLineItemId")));
	}

	/**
	 * getOrdersId
	 * @generated
	 */
	public java.lang.Long getOrdersId()
		throws java.rmi.RemoteException,
		javax.ejb.CreateException,
		javax.ejb.FinderException,
		javax.naming.NamingException {
		return (((java.lang.Long) __getCache("ordersId")));
	}

	/**
	 * getHotOrder
	 * @generated
	 */
	public java.lang.String getHotOrder()
		throws java.rmi.RemoteException,
		javax.ejb.CreateException,
		javax.ejb.FinderException,
		javax.naming.NamingException {
		return (((java.lang.String) __getCache("hotOrder")));
	}

	/**
	 * setHotOrder
	 * @generated
	 */
	public void setHotOrder(java.lang.String newValue) {
		__setCache("hotOrder", newValue);
	}

	/**
	 * getInvoiceIdentifier
	 * @generated
	 */
	public java.lang.String getInvoiceIdentifier()
		throws java.rmi.RemoteException,
		javax.ejb.CreateException,
		javax.ejb.FinderException,
		javax.naming.NamingException {
		return (((java.lang.String) __getCache("invoiceIdentifier")));
	}

	/**
	 * setInvoiceIdentifier
	 * @generated
	 */
	public void setInvoiceIdentifier(java.lang.String newValue) {
		__setCache("invoiceIdentifier", newValue);
	}

	/**
	 * getWeighType
	 * @generated
	 */
	public java.lang.String getWeighType()
		throws java.rmi.RemoteException,
		javax.ejb.CreateException,
		javax.ejb.FinderException,
		javax.naming.NamingException {
		return (((java.lang.String) __getCache("weighType")));
	}

	/**
	 * setWeighType
	 * @generated
	 */
	public void setWeighType(java.lang.String newValue) {
		__setCache("weighType", newValue);
	}

	/**
	 * getManifestIdentifier
	 * @generated
	 */
	public java.lang.String getManifestIdentifier()
		throws java.rmi.RemoteException,
		javax.ejb.CreateException,
		javax.ejb.FinderException,
		javax.naming.NamingException {
		return (((java.lang.String) __getCache("manifestIdentifier")));
	}

	/**
	 * setManifestIdentifier
	 * @generated
	 */
	public void setManifestIdentifier(java.lang.String newValue) {
		__setCache("manifestIdentifier", newValue);
	}

	/**
	 * getBolIdentifier
	 * @generated
	 */
	public java.lang.String getBolIdentifier()
		throws java.rmi.RemoteException,
		javax.ejb.CreateException,
		javax.ejb.FinderException,
		javax.naming.NamingException {
		return (((java.lang.String) __getCache("bolIdentifier")));
	}

	/**
	 * setBolIdentifier
	 * @generated
	 */
	public void setBolIdentifier(java.lang.String newValue) {
		__setCache("bolIdentifier", newValue);
	}

	/**
	 * getOptCounter
	 * @generated
	 */
	public short getOptCounter()
		throws java.rmi.RemoteException,
		javax.ejb.CreateException,
		javax.ejb.FinderException,
		javax.naming.NamingException {
		return (((Short) __getCache("optCounter")).shortValue());
	}

	/**
	 * setOptCounter
	 * @generated
	 */
	public void setOptCounter(short newValue) {
		__setCache("optCounter", new Short(newValue));
	}

	/**
	 * getEstimateDeliveryDate
	 * @generated
	 */
	public java.sql.Timestamp getEstimateDeliveryDate()
		throws java.rmi.RemoteException,
		javax.ejb.CreateException,
		javax.ejb.FinderException,
		javax.naming.NamingException {
		return (((java.sql.Timestamp) __getCache("estimateDeliveryDate")));
	}

	/**
	 * setEstimateDeliveryDate
	 * @generated
	 */
	public void setEstimateDeliveryDate(java.sql.Timestamp newValue) {
		__setCache("estimateDeliveryDate", newValue);
	}

	/**
	 * getTotalWeight
	 * @generated
	 */
	public java.math.BigDecimal getTotalWeight()
		throws java.rmi.RemoteException,
		javax.ejb.CreateException,
		javax.ejb.FinderException,
		javax.naming.NamingException {
		return (((java.math.BigDecimal) __getCache("totalWeight")));
	}

	/**
	 * setTotalWeight
	 * @generated
	 */
	public void setTotalWeight(java.math.BigDecimal newValue) {
		__setCache("totalWeight", newValue);
	}

	/**
	 * getOrderItemsId
	 * @generated
	 */
	public java.lang.Long getOrderItemsId()
		throws java.rmi.RemoteException,
		javax.ejb.CreateException,
		javax.ejb.FinderException,
		javax.naming.NamingException {
		return (((java.lang.Long) __getCache("orderItemsId")));
	}

	/**
	 * getOrdItemStatus
	 * @generated
	 */
	public java.lang.Character getOrdItemStatus()
		throws java.rmi.RemoteException,
		javax.ejb.CreateException,
		javax.ejb.FinderException,
		javax.naming.NamingException {
		return (((java.lang.Character) __getCache("ordItemStatus")));
	}

	/**
	 * setOrdItemStatus
	 * @generated
	 */
	public void setOrdItemStatus(java.lang.Character newValue) {
		__setCache("ordItemStatus", newValue);
	}

	/**
	 * getOrdStatus
	 * @generated
	 */
	public java.lang.String getOrdStatus()
		throws java.rmi.RemoteException,
		javax.ejb.CreateException,
		javax.ejb.FinderException,
		javax.naming.NamingException {
		return (((java.lang.String) __getCache("ordStatus")));
	}

	/**
	 * setOrdStatus
	 * @generated
	 */
	public void setOrdStatus(java.lang.String newValue) {
		__setCache("ordStatus", newValue);
	}

	/**
	 * getSalesOrderId
	 * @generated
	 */
	public java.lang.String getSalesOrderId()
		throws java.rmi.RemoteException,
		javax.ejb.CreateException,
		javax.ejb.FinderException,
		javax.naming.NamingException {
		return (((java.lang.String) __getCache("salesOrderId")));
	}

	/**
	 * setInitKey_orderLineItem_Id
	 * @generated
	 */
	public void setInitKey_orderLineItem_Id(Integer newValue) {
		this.initKey_orderLineItem_Id = (newValue);
	}

	/**
	 * setInitKey_orders_Id
	 * @generated
	 */
	public void setInitKey_orders_Id(Long newValue) {
		this.initKey_orders_Id = (newValue);
	}

	/**
	 * setInitKey_orderItems_Id
	 * @generated
	 */
	public void setInitKey_orderItems_Id(Long newValue) {
		this.initKey_orderItems_Id = (newValue);
	}

	/**
	 * setInitKey_salesOrder_Id
	 * @generated
	 */
	public void setInitKey_salesOrder_Id(String newValue) {
		this.initKey_salesOrder_Id = (newValue);
	}

	/**
	 * XorderItemsAccessBean
	 * @generated
	 */
	public XorderItemsAccessBean() {
		super();
	}

	/**
	 * XorderItemsAccessBean
	 * @generated
	 */
	public XorderItemsAccessBean(javax.ejb.EJBObject o)
		throws java.rmi.RemoteException {
		super(o);
	}

	/**
	 * defaultJNDIName
	 * @generated
	 */
	public String defaultJNDIName() {
		return "ejb/com/ibm/commerce/extension/objects/XorderItemsHome";
	}

	/**
	 * ejbHome
	 * @generated
	 */
	private com.ibm.commerce.extension.objects.XorderItemsHome ejbHome()
		throws java.rmi.RemoteException,
		javax.naming.NamingException {
		return (com.ibm.commerce.extension.objects.XorderItemsHome) PortableRemoteObject
			.narrow(
				getHome(),
				com.ibm.commerce.extension.objects.XorderItemsHome.class);
	}

	/**
	 * ejbRef
	 * @generated
	 */
	private com.ibm.commerce.extension.objects.XorderItems ejbRef()
		throws java.rmi.RemoteException {
		if (ejbRef == null)
			return null;
		if (__ejbRef == null)
			__ejbRef = (com.ibm.commerce.extension.objects.XorderItems) PortableRemoteObject
				.narrow(
					ejbRef,
					com.ibm.commerce.extension.objects.XorderItems.class);
	
		return __ejbRef;
	}

	/**
	 * instantiateEJB
	 * @generated
	 */
	protected void instantiateEJB()
		throws javax.naming.NamingException,
		javax.ejb.FinderException,
		java.rmi.RemoteException {
		if (ejbRef() != null)
			return;
	
		com.ibm.commerce.extension.objects.XorderItemsKey key = keyFromFields(
			initKey_orderLineItem_Id,
			initKey_orders_Id,
			initKey_orderItems_Id,
			initKey_salesOrder_Id);
		ejbRef = ejbHome().findByPrimaryKey(key);
	}

	/**
	 * instantiateEJBByPrimaryKey
	 * @generated
	 */
	protected boolean instantiateEJBByPrimaryKey()
		throws javax.ejb.CreateException,
		java.rmi.RemoteException,
		javax.naming.NamingException {
		boolean result = false;
	
		if (ejbRef() != null)
			return true;
	
		try {
			com.ibm.commerce.extension.objects.XorderItemsKey pKey = (com.ibm.commerce.extension.objects.XorderItemsKey) this
				.__getKey();
			if (pKey != null) {
				ejbRef = ejbHome().findByPrimaryKey(pKey);
				result = true;
			}
		} catch (javax.ejb.FinderException e) {
		}
		return result;
	}

	/**
	 * keyFromFields
	 * @generated
	 */
	private com.ibm.commerce.extension.objects.XorderItemsKey keyFromFields(
		Integer f0,
		Long f1,
		Long f2,
		String f3) {
		com.ibm.commerce.extension.objects.XorderItemsKey keyClass = new com.ibm.commerce.extension.objects.XorderItemsKey();
		keyClass.orderLineItem_Id = f0;
		keyClass.orders_Id = f1;
		keyClass.orderItems_Id = f2;
		keyClass.salesOrder_Id = f3;
		return keyClass;
	}

	/**
	 * refreshCopyHelper
	 * @generated
	 */
	public void refreshCopyHelper()
		throws java.rmi.RemoteException,
		javax.ejb.CreateException,
		javax.ejb.FinderException,
		javax.naming.NamingException {
		refreshCopyHelper(ejbRef());
	}

	/**
	 * commitCopyHelper
	 * @generated
	 */
	public void commitCopyHelper()
		throws java.rmi.RemoteException,
		javax.ejb.CreateException,
		javax.ejb.FinderException,
		javax.naming.NamingException {
		commitCopyHelper(ejbRef());
	}

	/**
	 * findbysalesOrder_id
	 * @generated
	 */
	public java.util.Enumeration findbysalesOrder_id(
		java.lang.String salesorder_id)
		throws javax.naming.NamingException,
		javax.ejb.FinderException,
		java.rmi.RemoteException {
		com.ibm.commerce.extension.objects.XorderItemsHome localHome = ejbHome();
		java.util.Enumeration ejbs = localHome
			.findbysalesOrder_id(salesorder_id);
		return (java.util.Enumeration) createAccessBeans(ejbs);
	}

	/**
	 * findfindByCompositeIds
	 * @generated
	 */
	public XorderItemsAccessBean findfindByCompositeIds(
		java.lang.Integer orderLineItem_Id,
		java.lang.Long orders_Id,
		java.lang.Long orderItems_Id,
		java.lang.String salesOrder_Id)
		throws javax.naming.NamingException,
		javax.ejb.FinderException,
		java.rmi.RemoteException {
		com.ibm.commerce.extension.objects.XorderItemsHome localHome = ejbHome();
		com.ibm.commerce.extension.objects.XorderItems ejbs = localHome
			.findfindByCompositeIds(
				orderLineItem_Id,
				orders_Id,
				orderItems_Id,
				salesOrder_Id);
		return (XorderItemsAccessBean) createAccessBeans(ejbs);
	}

	/**
	 * XorderItemsAccessBean
	 * @generated
	 */
	public XorderItemsAccessBean(
		java.lang.Integer orderLineItem_Id,
		java.lang.Long orders_Id,
		java.lang.Long orderItems_Id,
		java.lang.String salesOrder_Id) throws javax.naming.NamingException,
		javax.ejb.CreateException, java.rmi.RemoteException {
		ejbRef = ejbHome().create(
			orderLineItem_Id,
			orders_Id,
			orderItems_Id,
			salesOrder_Id);
	}

	/**
	 * fulfills
	 * @generated
	 */
	public boolean fulfills(java.lang.Long arg0, java.lang.String arg1)
		throws javax.naming.NamingException,
		javax.ejb.FinderException,
		java.lang.Exception,
		java.rmi.RemoteException {
		instantiateEJB();
		return ejbRef().fulfills(arg0, arg1);
	}

	/**
	 * getOwner
	 * @generated
	 */
	public java.lang.Long getOwner()
		throws javax.naming.NamingException,
		javax.ejb.FinderException,
		java.lang.Exception,
		java.rmi.RemoteException {
		instantiateEJB();
		return ejbRef().getOwner();
	}

	/**
	 * findByOrderItemId
	 * @generated
	 */
	public java.util.Enumeration findByOrderItemId(java.lang.Long orderItemId)
		throws javax.naming.NamingException,
		javax.ejb.FinderException,
		java.rmi.RemoteException {
		com.ibm.commerce.extension.objects.XorderItemsHome localHome = ejbHome();
		java.util.Enumeration ejbs = localHome.findByOrderItemId(orderItemId);
		return (java.util.Enumeration) createAccessBeans(ejbs);
	}

	/**
	 * getSourceCity
	 * @generated
	 */
	public java.lang.String getSourceCity()
		throws java.rmi.RemoteException,
		javax.ejb.CreateException,
		javax.ejb.FinderException,
		javax.naming.NamingException {
		return (((java.lang.String) __getCache("sourceCity")));
	}

	/**
	 * setSourceCity
	 * @generated
	 */
	public void setSourceCity(java.lang.String newValue) {
		__setCache("sourceCity", newValue);
	}

	/**
	 * getSourceState
	 * @generated
	 */
	public java.lang.String getSourceState()
		throws java.rmi.RemoteException,
		javax.ejb.CreateException,
		javax.ejb.FinderException,
		javax.naming.NamingException {
		return (((java.lang.String) __getCache("sourceState")));
	}

	/**
	 * setSourceState
	 * @generated
	 */
	public void setSourceState(java.lang.String newValue) {
		__setCache("sourceState", newValue);
	}

	/**
	 * getSourceCountry
	 * @generated
	 */
	public java.lang.String getSourceCountry()
		throws java.rmi.RemoteException,
		javax.ejb.CreateException,
		javax.ejb.FinderException,
		javax.naming.NamingException {
		return (((java.lang.String) __getCache("sourceCountry")));
	}

	/**
	 * setSourceCountry
	 * @generated
	 */
	public void setSourceCountry(java.lang.String newValue) {
		__setCache("sourceCountry", newValue);
	}

	/**
	 * getUnit
	 * @generated
	 */
	public java.lang.String getUnit()
		throws java.rmi.RemoteException,
		javax.ejb.CreateException,
		javax.ejb.FinderException,
		javax.naming.NamingException {
		return (((java.lang.String) __getCache("unit")));
	}

	/**
	 * setUnit
	 * @generated
	 */
	public void setUnit(java.lang.String newValue) {
		__setCache("unit", newValue);
	}

	/**
	 * getIncludeInTonnage
	 * @generated
	 */
	public java.lang.String getIncludeInTonnage()
		throws java.rmi.RemoteException,
		javax.ejb.CreateException,
		javax.ejb.FinderException,
		javax.naming.NamingException {
		return (((java.lang.String) __getCache("includeInTonnage")));
	}

	/**
	 * setIncludeInTonnage
	 * @generated
	 */
	public void setIncludeInTonnage(java.lang.String newValue) {
		__setCache("includeInTonnage", newValue);
	}

	/**
	 * XorderItemsAccessBean
	 * @generated
	 */
	public XorderItemsAccessBean(
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
		java.lang.String includeInTonnage) throws javax.naming.NamingException,
		javax.ejb.CreateException, java.rmi.RemoteException {
		ejbRef = ejbHome().create(
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

	/**
	 * getSourcing_Id
	 * @generated
	 */
	public java.lang.String getSourcing_Id()
		throws java.rmi.RemoteException,
		javax.ejb.CreateException,
		javax.ejb.FinderException,
		javax.naming.NamingException {
		return (((java.lang.String) __getCache("sourcing_Id")));
	}

	/**
	 * setSourcing_Id
	 * @generated
	 */
	public void setSourcing_Id(java.lang.String newValue) {
		__setCache("sourcing_Id", newValue);
	}
}
