package com.ibm.commerce.extension.objects;

import com.ibm.etools.ejb.client.runtime.*;

/**
 * XaddressRelationFactory
 * @generated
 */
public class XaddressRelationFactory extends AbstractEJBFactory {

	/**
	 * XaddressRelationFactory
	 * @generated
	 */
	public XaddressRelationFactory() {
		super();
	}

	/**
	 * _acquireXaddressRelationHome
	 * @generated
	 */
	protected com.ibm.commerce.extension.objects.XaddressRelationHome _acquireXaddressRelationHome()
		throws java.rmi.RemoteException {
		return (com.ibm.commerce.extension.objects.XaddressRelationHome) _acquireEJBHome();
	}

	/**
	 * acquireXaddressRelationHome
	 * @generated
	 */
	public com.ibm.commerce.extension.objects.XaddressRelationHome acquireXaddressRelationHome()
		throws javax.naming.NamingException {
		return (com.ibm.commerce.extension.objects.XaddressRelationHome) acquireEJBHome();
	}

	/**
	 * getDefaultJNDIName
	 * @generated
	 */
	public String getDefaultJNDIName() {
		return "ejb/com/ibm/commerce/extension/objects/XaddressRelationHome";
	}

	/**
	 * getHomeInterface
	 * @generated
	 */
	protected Class getHomeInterface() {
		return com.ibm.commerce.extension.objects.XaddressRelationHome.class;
	}

	/**
	 * resetXaddressRelationHome
	 * @generated
	 */
	public void resetXaddressRelationHome() {
		resetEJBHome();
	}

	/**
	 * setXaddressRelationHome
	 * @generated
	 */
	public void setXaddressRelationHome(
		com.ibm.commerce.extension.objects.XaddressRelationHome home) {
		setEJBHome(home);
	}

	/**
	 * findByPrimaryKey
	 * @generated
	 */
	public com.ibm.commerce.extension.objects.XaddressRelation findByPrimaryKey(
		com.ibm.commerce.extension.objects.XaddressRelationKey primaryKey)
		throws javax.ejb.FinderException,
		java.rmi.RemoteException {
		return _acquireXaddressRelationHome().findByPrimaryKey(primaryKey);
	}

	/**
	 * create
	 * @generated
	 */
	public com.ibm.commerce.extension.objects.XaddressRelation create(
		java.lang.Long billtoId,
		java.lang.Long rrcId,
		java.lang.Long shiptoId)
		throws javax.ejb.CreateException,
		java.rmi.RemoteException {
		return _acquireXaddressRelationHome().create(billtoId, rrcId, shiptoId);
	}

	/**
	 * findbyBillToId
	 * @generated
	 */
	public java.util.Enumeration findbyBillToId(java.lang.Long billToId)
		throws javax.ejb.FinderException,
		java.rmi.RemoteException {
		return _acquireXaddressRelationHome().findbyBillToId(billToId);
	}

	/**
	 * create
	 * @generated
	 */
	public com.ibm.commerce.extension.objects.XaddressRelation create(
		java.lang.Long billtoId,
		java.lang.String billtoName,
		java.lang.Long rrcId,
		java.lang.String rrcName,
		java.lang.Long shiptoId,
		java.lang.String shiptoName,
		java.sql.Timestamp createdTime,
		java.sql.Timestamp lastUpdate,
		java.lang.Integer markForDelete)
		throws javax.ejb.CreateException,
		java.rmi.RemoteException {
		return _acquireXaddressRelationHome().create(
			billtoId,
			billtoName,
			rrcId,
			rrcName,
			shiptoId,
			shiptoName,
			createdTime,
			lastUpdate,
			markForDelete);
	}

	/**
	 * findbyRrcId
	 * @generated
	 */
	public java.util.Enumeration findbyRrcId(java.lang.Long rrcId)
		throws javax.ejb.FinderException,
		java.rmi.RemoteException {
		return _acquireXaddressRelationHome().findbyRrcId(rrcId);
	}
}
