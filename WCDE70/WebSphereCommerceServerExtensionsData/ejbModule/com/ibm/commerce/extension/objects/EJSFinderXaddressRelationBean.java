package com.ibm.commerce.extension.objects;

/**
 * EJSFinderXaddressRelationBean
 */
public interface EJSFinderXaddressRelationBean {
	/**
	 * findbyBillToId
	 */
	public com.ibm.ejs.persistence.EJSFinder findbyBillToId(java.lang.Long billToId) throws javax.ejb.FinderException, java.rmi.RemoteException;
	/**
	 * findbyRrcId
	 */
	public com.ibm.ejs.persistence.EJSFinder findbyRrcId(java.lang.Long rrcId) throws javax.ejb.FinderException, java.rmi.RemoteException;
}
