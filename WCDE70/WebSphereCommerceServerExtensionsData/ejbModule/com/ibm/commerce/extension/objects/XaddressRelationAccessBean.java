package com.ibm.commerce.extension.objects;

import javax.rmi.*;
import com.ibm.ivj.ejb.runtime.*;

/**
 * XaddressRelationAccessBean
 * @generated
 */
public class XaddressRelationAccessBean extends AbstractEntityAccessBean
	implements
		com.ibm.commerce.extension.objects.XaddressRelationAccessBeanData {

	/**
	 * @generated
	 */
	private XaddressRelation __ejbRef;

	/**
	 * @generated
	 */
	private Long initKey_billtoId;

	/**
	 * @generated
	 */
	private Long initKey_rrcId;

	/**
	 * @generated
	 */
	private Long initKey_shiptoId;

	/**
	 * getShipToId
	 * @generated
	 */
	public java.lang.Long getShipToId()
		throws java.rmi.RemoteException,
		javax.ejb.CreateException,
		javax.ejb.FinderException,
		javax.naming.NamingException {
		return (((java.lang.Long) __getCache("shipToId")));
	}

	/**
	 * getShiptoName
	 * @generated
	 */
	public java.lang.String getShiptoName()
		throws java.rmi.RemoteException,
		javax.ejb.CreateException,
		javax.ejb.FinderException,
		javax.naming.NamingException {
		return (((java.lang.String) __getCache("shiptoName")));
	}

	/**
	 * setShiptoName
	 * @generated
	 */
	public void setShiptoName(java.lang.String newValue) {
		__setCache("shiptoName", newValue);
	}

	/**
	 * getBillToId
	 * @generated
	 */
	public java.lang.Long getBillToId()
		throws java.rmi.RemoteException,
		javax.ejb.CreateException,
		javax.ejb.FinderException,
		javax.naming.NamingException {
		return (((java.lang.Long) __getCache("billToId")));
	}

	/**
	 * getRrcId
	 * @generated
	 */
	public java.lang.Long getRrcId()
		throws java.rmi.RemoteException,
		javax.ejb.CreateException,
		javax.ejb.FinderException,
		javax.naming.NamingException {
		return (((java.lang.Long) __getCache("rrcId")));
	}

	/**
	 * getRrcName
	 * @generated
	 */
	public java.lang.String getRrcName()
		throws java.rmi.RemoteException,
		javax.ejb.CreateException,
		javax.ejb.FinderException,
		javax.naming.NamingException {
		return (((java.lang.String) __getCache("rrcName")));
	}

	/**
	 * setRrcName
	 * @generated
	 */
	public void setRrcName(java.lang.String newValue) {
		__setCache("rrcName", newValue);
	}

	/**
	 * setInitKey_billtoId
	 * @generated
	 */
	public void setInitKey_billtoId(Long newValue) {
		this.initKey_billtoId = (newValue);
	}

	/**
	 * setInitKey_rrcId
	 * @generated
	 */
	public void setInitKey_rrcId(Long newValue) {
		this.initKey_rrcId = (newValue);
	}

	/**
	 * setInitKey_shiptoId
	 * @generated
	 */
	public void setInitKey_shiptoId(Long newValue) {
		this.initKey_shiptoId = (newValue);
	}

	/**
	 * XaddressRelationAccessBean
	 * @generated
	 */
	public XaddressRelationAccessBean() {
		super();
	}

	/**
	 * XaddressRelationAccessBean
	 * @generated
	 */
	public XaddressRelationAccessBean(javax.ejb.EJBObject o)
		throws java.rmi.RemoteException {
		super(o);
	}

	/**
	 * defaultJNDIName
	 * @generated
	 */
	public String defaultJNDIName() {
		return "ejb/com/ibm/commerce/extension/objects/XaddressRelationHome";
	}

	/**
	 * ejbHome
	 * @generated
	 */
	private com.ibm.commerce.extension.objects.XaddressRelationHome ejbHome()
		throws java.rmi.RemoteException,
		javax.naming.NamingException {
		return (com.ibm.commerce.extension.objects.XaddressRelationHome) PortableRemoteObject
			.narrow(
				getHome(),
				com.ibm.commerce.extension.objects.XaddressRelationHome.class);
	}

	/**
	 * ejbRef
	 * @generated
	 */
	private com.ibm.commerce.extension.objects.XaddressRelation ejbRef()
		throws java.rmi.RemoteException {
		if (ejbRef == null)
			return null;
		if (__ejbRef == null)
			__ejbRef = (com.ibm.commerce.extension.objects.XaddressRelation) PortableRemoteObject
				.narrow(
					ejbRef,
					com.ibm.commerce.extension.objects.XaddressRelation.class);
	
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
	
		com.ibm.commerce.extension.objects.XaddressRelationKey key = keyFromFields(
			initKey_billtoId,
			initKey_rrcId,
			initKey_shiptoId);
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
			com.ibm.commerce.extension.objects.XaddressRelationKey pKey = (com.ibm.commerce.extension.objects.XaddressRelationKey) this
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
	private com.ibm.commerce.extension.objects.XaddressRelationKey keyFromFields(
		Long f0,
		Long f1,
		Long f2) {
		com.ibm.commerce.extension.objects.XaddressRelationKey keyClass = new com.ibm.commerce.extension.objects.XaddressRelationKey();
		keyClass.billtoId = f0;
		keyClass.rrcId = f1;
		keyClass.shiptoId = f2;
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
	 * XaddressRelationAccessBean
	 * @generated
	 */
	public XaddressRelationAccessBean(
		java.lang.Long billtoId,
		java.lang.Long rrcId,
		java.lang.Long shiptoId) throws javax.naming.NamingException,
		javax.ejb.CreateException, java.rmi.RemoteException {
		ejbRef = ejbHome().create(billtoId, rrcId, shiptoId);
	}

	/**
	 * findbyBillToId
	 * @generated
	 */
	public java.util.Enumeration findbyBillToId(java.lang.Long billToId)
		throws javax.naming.NamingException,
		javax.ejb.FinderException,
		java.rmi.RemoteException {
		com.ibm.commerce.extension.objects.XaddressRelationHome localHome = ejbHome();
		java.util.Enumeration ejbs = localHome.findbyBillToId(billToId);
		return (java.util.Enumeration) createAccessBeans(ejbs);
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
	 * getCreatedTime
	 * @generated
	 */
	public java.sql.Timestamp getCreatedTime()
		throws java.rmi.RemoteException,
		javax.ejb.CreateException,
		javax.ejb.FinderException,
		javax.naming.NamingException {
		return (((java.sql.Timestamp) __getCache("createdTime")));
	}

	/**
	 * setCreatedTime
	 * @generated
	 */
	public void setCreatedTime(java.sql.Timestamp newValue) {
		__setCache("createdTime", newValue);
	}

	/**
	 * getMarkForDelete
	 * @generated
	 */
	public java.lang.Integer getMarkForDelete()
		throws java.rmi.RemoteException,
		javax.ejb.CreateException,
		javax.ejb.FinderException,
		javax.naming.NamingException {
		return (((java.lang.Integer) __getCache("markForDelete")));
	}

	/**
	 * setMarkForDelete
	 * @generated
	 */
	public void setMarkForDelete(java.lang.Integer newValue) {
		__setCache("markForDelete", newValue);
	}

	/**
	 * getBilltoName
	 * @generated
	 */
	public java.lang.String getBilltoName()
		throws java.rmi.RemoteException,
		javax.ejb.CreateException,
		javax.ejb.FinderException,
		javax.naming.NamingException {
		return (((java.lang.String) __getCache("billtoName")));
	}

	/**
	 * setBilltoName
	 * @generated
	 */
	public void setBilltoName(java.lang.String newValue) {
		__setCache("billtoName", newValue);
	}

	/**
	 * getLastUpdate
	 * @generated
	 */
	public java.sql.Timestamp getLastUpdate()
		throws java.rmi.RemoteException,
		javax.ejb.CreateException,
		javax.ejb.FinderException,
		javax.naming.NamingException {
		return (((java.sql.Timestamp) __getCache("lastUpdate")));
	}

	/**
	 * setLastUpdate
	 * @generated
	 */
	public void setLastUpdate(java.sql.Timestamp newValue) {
		__setCache("lastUpdate", newValue);
	}

	/**
	 * XaddressRelationAccessBean
	 * @generated
	 */
	public XaddressRelationAccessBean(
		java.lang.Long billtoId,
		java.lang.String billtoName,
		java.lang.Long rrcId,
		java.lang.String rrcName,
		java.lang.Long shiptoId,
		java.lang.String shiptoName,
		java.sql.Timestamp createdTime,
		java.sql.Timestamp lastUpdate,
		java.lang.Integer markForDelete) throws javax.naming.NamingException,
		javax.ejb.CreateException, java.rmi.RemoteException {
		ejbRef = ejbHome().create(
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
		throws javax.naming.NamingException,
		javax.ejb.FinderException,
		java.rmi.RemoteException {
		com.ibm.commerce.extension.objects.XaddressRelationHome localHome = ejbHome();
		java.util.Enumeration ejbs = localHome.findbyRrcId(rrcId);
		return (java.util.Enumeration) createAccessBeans(ejbs);
	}
}
