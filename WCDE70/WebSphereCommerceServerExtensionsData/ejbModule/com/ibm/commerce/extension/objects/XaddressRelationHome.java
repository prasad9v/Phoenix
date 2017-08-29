package com.ibm.commerce.extension.objects;

/**
 * Home interface for Enterprise Bean: XaddressRelation
 */
public interface XaddressRelationHome extends javax.ejb.EJBHome {

	/**
	 * Creates an instance from a key for Entity Bean: XaddressRelation
	 */
	public com.ibm.commerce.extension.objects.XaddressRelation create(
		java.lang.Long billtoId,
		java.lang.Long rrcId,
		java.lang.Long shiptoId)
		throws javax.ejb.CreateException,
		java.rmi.RemoteException;

	/**
	 * Finds an instance using a key for Entity Bean: XaddressRelation
	 */
	public com.ibm.commerce.extension.objects.XaddressRelation findByPrimaryKey(
		com.ibm.commerce.extension.objects.XaddressRelationKey primaryKey)
		throws javax.ejb.FinderException,
		java.rmi.RemoteException;

	public java.util.Enumeration findbyBillToId(java.lang.Long billToId)
		throws javax.ejb.FinderException,
		java.rmi.RemoteException;

	/**
	 * @param billtoId
	 * @param billtoName
	 * @param rrcId
	 * @param rrcName
	 * @param shiptoId
	 * @param shiptoName
	 * @param createdTime
	 * @param lastUpdate
	 * @param markForDelete
	 * @return
	 * @throws javax.ejb.CreateException
	 * @throws java.rmi.RemoteException
	 */
	public com.ibm.commerce.extension.objects.XaddressRelation create(
			java.lang.Long billtoId, java.lang.String billtoName,
			java.lang.Long rrcId, java.lang.String rrcName,
			java.lang.Long shiptoId, java.lang.String shiptoName,
			java.sql.Timestamp createdTime, java.sql.Timestamp lastUpdate,
			java.lang.Integer markForDelete) throws javax.ejb.CreateException,
			java.rmi.RemoteException;

	public java.util.Enumeration findbyRrcId(java.lang.Long rrcId)
		throws javax.ejb.FinderException,
		java.rmi.RemoteException;
}
