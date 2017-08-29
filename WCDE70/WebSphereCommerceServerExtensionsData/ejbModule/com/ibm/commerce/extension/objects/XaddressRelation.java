package com.ibm.commerce.extension.objects;

/**
 * Remote interface for Enterprise Bean: XaddressRelation
 */
public interface XaddressRelation extends com.ibm.ivj.ejb.runtime.CopyHelper, javax.ejb.EJBObject, com.ibm.commerce.security.Protectable {


	/**
	 * @return
	 * @throws java.rmi.RemoteException
	 */
	public java.lang.Long getBillToId() throws java.rmi.RemoteException;

	/**
	 * @return
	 * @throws java.rmi.RemoteException
	 */
	public java.lang.Long getRrcId() throws java.rmi.RemoteException;

	/**
	 * @return
	 * @throws java.rmi.RemoteException
	 */
	public java.lang.Long getShipToId() throws java.rmi.RemoteException;
}
