package com.ibm.commerce.extension.objects;

/**
 * EJSFinderXorderItemsBean
 */
public interface EJSFinderXorderItemsBean {
	/**
	 * findbysalesOrder_id
	 */
	public com.ibm.ejs.persistence.EJSFinder findbysalesOrder_id(java.lang.String salesorder_id) throws javax.ejb.FinderException, java.rmi.RemoteException;
	/**
	 * findfindByCompositeIds
	 */
	public com.ibm.commerce.extension.objects.XorderItems findfindByCompositeIds(java.lang.Integer orderLineItem_Id, java.lang.Long orders_Id, java.lang.Long orderItems_Id, java.lang.String salesOrder_Id) throws javax.ejb.FinderException, java.rmi.RemoteException;
	/**
	 * findByOrderItemId
	 */
	public com.ibm.ejs.persistence.EJSFinder findByOrderItemId(java.lang.Long orderItemId) throws javax.ejb.FinderException, java.rmi.RemoteException;
}
