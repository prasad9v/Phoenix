package com.ibm.commerce.extension.objects;

/**
 * Remote interface for Enterprise Bean: XorderItems
 */
public interface XorderItems extends com.ibm.ivj.ejb.runtime.CopyHelper, javax.ejb.EJBObject, com.ibm.commerce.security.Protectable {


	/**
	 * @return
	 * @throws java.rmi.RemoteException
	 */
	public java.lang.Long getOrdersId() throws java.rmi.RemoteException;

	/**
	 * @return
	 * @throws java.rmi.RemoteException
	 */
	public java.lang.Long getOrderItemsId() throws java.rmi.RemoteException;

	/**
	 * @return
	 * @throws java.rmi.RemoteException
	 */
	public java.lang.Integer getOrderLineItemId()
			throws java.rmi.RemoteException;

	/**
	 * @return
	 * @throws java.rmi.RemoteException
	 */
	public java.lang.String getSalesOrderId() throws java.rmi.RemoteException;
}
