package com.ibm.commerce.extension.objects;

import com.ibm.ejs.container.*;

/**
 * EJSCMPXorderItemsHomeBean_80e4dc6d
 */
public class EJSCMPXorderItemsHomeBean_80e4dc6d extends EJSHome {
	static final long serialVersionUID = 61;
	/**
	 * EJSCMPXorderItemsHomeBean_80e4dc6d
	 */
	public EJSCMPXorderItemsHomeBean_80e4dc6d() throws java.rmi.RemoteException {
		super();	}
	/**
	 * postCreateWrapper
	 */
	public com.ibm.commerce.extension.objects.XorderItems postCreateWrapper(com.ibm.ejs.container.BeanO beanO, Object ejsKey) throws javax.ejb.CreateException, java.rmi.RemoteException {
		return  (com.ibm.commerce.extension.objects.XorderItems) super.postCreate(beanO, ejsKey, true);
	}
	/**
	 * afterPostCreateWrapper
	 */
	public void afterPostCreateWrapper(com.ibm.ejs.container.BeanO beanO, Object ejsKey) throws javax.ejb.CreateException, java.rmi.RemoteException {
		super.afterPostCreate(beanO, ejsKey);
	}
	/**
	 * findbysalesOrder_id
	 */
	public java.util.Enumeration findbysalesOrder_id(java.lang.String salesorder_id) throws javax.ejb.FinderException, java.rmi.RemoteException {
return super.getEnumeration(((com.ibm.commerce.extension.objects.EJSFinderXorderItemsBean)persister).findbysalesOrder_id(salesorder_id));	}
	/**
	 * findfindByCompositeIds
	 */
	public com.ibm.commerce.extension.objects.XorderItems findfindByCompositeIds(java.lang.Integer orderLineItem_Id, java.lang.Long orders_Id, java.lang.Long orderItems_Id, java.lang.String salesOrder_Id) throws javax.ejb.FinderException, java.rmi.RemoteException {
return ((com.ibm.commerce.extension.objects.EJSJDBCPersisterCMPXorderItemsBean_80e4dc6d)persister).findfindByCompositeIds(orderLineItem_Id, orders_Id, orderItems_Id, salesOrder_Id);	}
	/**
	 * create
	 */
	public com.ibm.commerce.extension.objects.XorderItems create(java.lang.Integer orderLineItem_Id, java.lang.Long orders_Id, java.lang.Long orderItems_Id, java.lang.String salesOrder_Id, java.math.BigDecimal totalWeight, java.lang.String weighType, java.lang.String hotOrder, java.lang.Integer sourcing_Id, java.lang.String ordStatus, java.lang.Character ordItemStatus, java.sql.Timestamp estimateDeliveryDate, java.lang.String bolIdentifier, java.lang.String manifestIdentifier, java.lang.String invoiceIdentifier) throws javax.ejb.CreateException, java.rmi.RemoteException {
		BeanO beanO = null;
		com.ibm.commerce.extension.objects.XorderItems _EJS_result = null;
		boolean createFailed = false;
		try {
			beanO = super.createBeanO();
			com.ibm.commerce.extension.objects.XorderItemsBean bean = (com.ibm.commerce.extension.objects.XorderItemsBean) beanO.getEnterpriseBean();
			bean.ejbCreate(orderLineItem_Id, orders_Id, orderItems_Id, salesOrder_Id, totalWeight, weighType, hotOrder, sourcing_Id, ordStatus, ordItemStatus, estimateDeliveryDate, bolIdentifier, manifestIdentifier, invoiceIdentifier);
			Object _primaryKey = keyFromBean(bean);
_EJS_result = postCreateWrapper(beanO, _primaryKey);
			bean.ejbPostCreate(orderLineItem_Id, orders_Id, orderItems_Id, salesOrder_Id, totalWeight, weighType, hotOrder, sourcing_Id, ordStatus, ordItemStatus, estimateDeliveryDate, bolIdentifier, manifestIdentifier, invoiceIdentifier);
			afterPostCreateWrapper(beanO, _primaryKey);
		}
		catch (javax.ejb.CreateException ex) {
			createFailed = true;
			throw ex;
		} catch (java.rmi.RemoteException ex) {
			createFailed = true;
			throw ex;
		} catch (Throwable ex) {
			createFailed = true;
			throw new CreateFailureException(ex);
		} finally {
			if (createFailed) {
				super.createFailure(beanO);
			}
		}
		return _EJS_result;
	}
	/**
	 * create
	 */
	public com.ibm.commerce.extension.objects.XorderItems create(java.lang.Integer orderLineItem_Id, java.lang.Long orders_Id, java.lang.Long orderItems_Id, java.lang.String salesOrder_Id) throws javax.ejb.CreateException, java.rmi.RemoteException {
		BeanO beanO = null;
		com.ibm.commerce.extension.objects.XorderItems _EJS_result = null;
		boolean createFailed = false;
		try {
			beanO = super.createBeanO();
			com.ibm.commerce.extension.objects.XorderItemsBean bean = (com.ibm.commerce.extension.objects.XorderItemsBean) beanO.getEnterpriseBean();
			bean.ejbCreate(orderLineItem_Id, orders_Id, orderItems_Id, salesOrder_Id);
			Object _primaryKey = keyFromBean(bean);
_EJS_result = postCreateWrapper(beanO, _primaryKey);
			bean.ejbPostCreate(orderLineItem_Id, orders_Id, orderItems_Id, salesOrder_Id);
			afterPostCreateWrapper(beanO, _primaryKey);
		}
		catch (javax.ejb.CreateException ex) {
			createFailed = true;
			throw ex;
		} catch (java.rmi.RemoteException ex) {
			createFailed = true;
			throw ex;
		} catch (Throwable ex) {
			createFailed = true;
			throw new CreateFailureException(ex);
		} finally {
			if (createFailed) {
				super.createFailure(beanO);
			}
		}
		return _EJS_result;
	}
	/**
	 * findByPrimaryKey
	 */
	public com.ibm.commerce.extension.objects.XorderItems findByPrimaryKey(com.ibm.commerce.extension.objects.XorderItemsKey primaryKey) throws javax.ejb.FinderException, java.rmi.RemoteException {
		return ((com.ibm.commerce.extension.objects.EJSJDBCPersisterCMPXorderItemsBean_80e4dc6d) persister).findByPrimaryKey(primaryKey);
	}
	/**
	 * findByOrderItemId
	 */
	public java.util.Enumeration findByOrderItemId(java.lang.Long orderItemId) throws javax.ejb.FinderException, java.rmi.RemoteException {
return super.getEnumeration(((com.ibm.commerce.extension.objects.EJSFinderXorderItemsBean)persister).findByOrderItemId(orderItemId));	}
	/**
	 * keyFromBean
	 */
	public Object keyFromBean(javax.ejb.EntityBean generalEJB) {
		com.ibm.commerce.extension.objects.XorderItemsBean tmpEJB = (com.ibm.commerce.extension.objects.XorderItemsBean) generalEJB;
		com.ibm.commerce.extension.objects.XorderItemsKey keyClass = new com.ibm.commerce.extension.objects.XorderItemsKey();
		keyClass.orderLineItem_Id = tmpEJB.orderLineItem_Id;
		keyClass.orders_Id = tmpEJB.orders_Id;
		keyClass.orderItems_Id = tmpEJB.orderItems_Id;
		keyClass.salesOrder_Id = tmpEJB.salesOrder_Id;
		return keyClass;
	}
	/**
	 * keyFromFields
	 */
	public com.ibm.commerce.extension.objects.XorderItemsKey keyFromFields(java.lang.Integer f0, java.lang.Long f1, java.lang.Long f2, java.lang.String f3) {
		com.ibm.commerce.extension.objects.XorderItemsKey keyClass = new com.ibm.commerce.extension.objects.XorderItemsKey();
		keyClass.orderLineItem_Id = f0;
		keyClass.orders_Id = f1;
		keyClass.orderItems_Id = f2;
		keyClass.salesOrder_Id = f3;
		return keyClass;
	}
}
