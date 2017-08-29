package com.ibm.commerce.extension.objects;

import com.ibm.ejs.container.*;

/**
 * EJSCMPXaddressRelationHomeBean_0c064b7f
 */
public class EJSCMPXaddressRelationHomeBean_0c064b7f extends EJSHome {
	static final long serialVersionUID = 61;
	/**
	 * EJSCMPXaddressRelationHomeBean_0c064b7f
	 */
	public EJSCMPXaddressRelationHomeBean_0c064b7f() throws java.rmi.RemoteException {
		super();	}
	/**
	 * postCreateWrapper
	 */
	public com.ibm.commerce.extension.objects.XaddressRelation postCreateWrapper(com.ibm.ejs.container.BeanO beanO, Object ejsKey) throws javax.ejb.CreateException, java.rmi.RemoteException {
		return  (com.ibm.commerce.extension.objects.XaddressRelation) super.postCreate(beanO, ejsKey, true);
	}
	/**
	 * afterPostCreateWrapper
	 */
	public void afterPostCreateWrapper(com.ibm.ejs.container.BeanO beanO, Object ejsKey) throws javax.ejb.CreateException, java.rmi.RemoteException {
		super.afterPostCreate(beanO, ejsKey);
	}
	/**
	 * create
	 */
	public com.ibm.commerce.extension.objects.XaddressRelation create(java.lang.Long billtoId, java.lang.String billtoName, java.lang.Long rrcId, java.lang.String rrcName, java.lang.Long shiptoId, java.lang.String shiptoName, java.sql.Timestamp createdTime, java.sql.Timestamp lastUpdate, java.lang.Integer markForDelete) throws javax.ejb.CreateException, java.rmi.RemoteException {
		BeanO beanO = null;
		com.ibm.commerce.extension.objects.XaddressRelation _EJS_result = null;
		boolean createFailed = false;
		try {
			beanO = super.createBeanO();
			com.ibm.commerce.extension.objects.XaddressRelationBean bean = (com.ibm.commerce.extension.objects.XaddressRelationBean) beanO.getEnterpriseBean();
			bean.ejbCreate(billtoId, billtoName, rrcId, rrcName, shiptoId, shiptoName, createdTime, lastUpdate, markForDelete);
			Object _primaryKey = keyFromBean(bean);
_EJS_result = postCreateWrapper(beanO, _primaryKey);
			bean.ejbPostCreate(billtoId, billtoName, rrcId, rrcName, shiptoId, shiptoName, createdTime, lastUpdate, markForDelete);
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
	public com.ibm.commerce.extension.objects.XaddressRelation findByPrimaryKey(com.ibm.commerce.extension.objects.XaddressRelationKey primaryKey) throws javax.ejb.FinderException, java.rmi.RemoteException {
		return ((com.ibm.commerce.extension.objects.EJSJDBCPersisterCMPXaddressRelationBean_0c064b7f) persister).findByPrimaryKey(primaryKey);
	}
	/**
	 * create
	 */
	public com.ibm.commerce.extension.objects.XaddressRelation create(java.lang.Long billtoId, java.lang.Long rrcId, java.lang.Long shiptoId) throws javax.ejb.CreateException, java.rmi.RemoteException {
		BeanO beanO = null;
		com.ibm.commerce.extension.objects.XaddressRelation _EJS_result = null;
		boolean createFailed = false;
		try {
			beanO = super.createBeanO();
			com.ibm.commerce.extension.objects.XaddressRelationBean bean = (com.ibm.commerce.extension.objects.XaddressRelationBean) beanO.getEnterpriseBean();
			bean.ejbCreate(billtoId, rrcId, shiptoId);
			Object _primaryKey = keyFromBean(bean);
_EJS_result = postCreateWrapper(beanO, _primaryKey);
			bean.ejbPostCreate(billtoId, rrcId, shiptoId);
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
	 * findbyBillToId
	 */
	public java.util.Enumeration findbyBillToId(java.lang.Long billToId) throws javax.ejb.FinderException, java.rmi.RemoteException {
return super.getEnumeration(((com.ibm.commerce.extension.objects.EJSFinderXaddressRelationBean)persister).findbyBillToId(billToId));	}
	/**
	 * findbyRrcId
	 */
	public java.util.Enumeration findbyRrcId(java.lang.Long rrcId) throws javax.ejb.FinderException, java.rmi.RemoteException {
return super.getEnumeration(((com.ibm.commerce.extension.objects.EJSFinderXaddressRelationBean)persister).findbyRrcId(rrcId));	}
	/**
	 * keyFromBean
	 */
	public Object keyFromBean(javax.ejb.EntityBean generalEJB) {
		com.ibm.commerce.extension.objects.XaddressRelationBean tmpEJB = (com.ibm.commerce.extension.objects.XaddressRelationBean) generalEJB;
		com.ibm.commerce.extension.objects.XaddressRelationKey keyClass = new com.ibm.commerce.extension.objects.XaddressRelationKey();
		keyClass.billtoId = tmpEJB.billtoId;
		keyClass.rrcId = tmpEJB.rrcId;
		keyClass.shiptoId = tmpEJB.shiptoId;
		return keyClass;
	}
	/**
	 * keyFromFields
	 */
	public com.ibm.commerce.extension.objects.XaddressRelationKey keyFromFields(java.lang.Long f0, java.lang.Long f1, java.lang.Long f2) {
		com.ibm.commerce.extension.objects.XaddressRelationKey keyClass = new com.ibm.commerce.extension.objects.XaddressRelationKey();
		keyClass.billtoId = f0;
		keyClass.rrcId = f1;
		keyClass.shiptoId = f2;
		return keyClass;
	}
}
