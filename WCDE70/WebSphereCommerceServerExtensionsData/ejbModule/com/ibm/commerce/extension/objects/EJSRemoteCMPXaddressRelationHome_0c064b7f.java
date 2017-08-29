package com.ibm.commerce.extension.objects;

import com.ibm.ejs.container.*;

/**
 * EJSRemoteCMPXaddressRelationHome_0c064b7f
 */
public class EJSRemoteCMPXaddressRelationHome_0c064b7f extends EJSWrapper implements com.ibm.commerce.extension.objects.XaddressRelationHome {
	/**
	 * EJSRemoteCMPXaddressRelationHome_0c064b7f
	 */
	public EJSRemoteCMPXaddressRelationHome_0c064b7f() throws java.rmi.RemoteException {
		super();	}
	/**
	 * create
	 */
	public com.ibm.commerce.extension.objects.XaddressRelation create(java.lang.Long billtoId, java.lang.Long rrcId, java.lang.Long shiptoId) throws javax.ejb.CreateException, java.rmi.RemoteException {
		EJSDeployedSupport _EJS_s = container.getEJSDeployedSupport(this);
		Object[] _jacc_parms = null;
		com.ibm.commerce.extension.objects.XaddressRelation _EJS_result = null;
		try {
			if (container.doesJaccNeedsEJBArguments( this ))
			{
				_jacc_parms = new Object[3];
				_jacc_parms[0] = billtoId;
				_jacc_parms[1] = rrcId;
				_jacc_parms[2] = shiptoId;
			}
	com.ibm.commerce.extension.objects.EJSCMPXaddressRelationHomeBean_0c064b7f beanRef = (com.ibm.commerce.extension.objects.EJSCMPXaddressRelationHomeBean_0c064b7f)container.preInvoke(this, 0, _EJS_s, _jacc_parms);
			_EJS_result = beanRef.create(billtoId, rrcId, shiptoId);
		}
		catch (javax.ejb.CreateException ex) {
			_EJS_s.setCheckedException(ex);
			throw ex;
		}
		catch (java.rmi.RemoteException ex) {
			_EJS_s.setUncheckedException(ex);
		}
		catch (Throwable ex) {
			_EJS_s.setUncheckedException(ex);
			throw new java.rmi.RemoteException("bean method raised unchecked exception", ex);
		}

		finally {
			try{
				container.postInvoke(this, 0, _EJS_s);
			} finally {
				container.putEJSDeployedSupport(_EJS_s);
			}
		}
		return _EJS_result;
	}
	/**
	 * create
	 */
	public com.ibm.commerce.extension.objects.XaddressRelation create(java.lang.Long billtoId, java.lang.String billtoName, java.lang.Long rrcId, java.lang.String rrcName, java.lang.Long shiptoId, java.lang.String shiptoName, java.sql.Timestamp createdTime, java.sql.Timestamp lastUpdate, java.lang.Integer markForDelete) throws javax.ejb.CreateException, java.rmi.RemoteException {
		EJSDeployedSupport _EJS_s = container.getEJSDeployedSupport(this);
		Object[] _jacc_parms = null;
		com.ibm.commerce.extension.objects.XaddressRelation _EJS_result = null;
		try {
			if (container.doesJaccNeedsEJBArguments( this ))
			{
				_jacc_parms = new Object[9];
				_jacc_parms[0] = billtoId;
				_jacc_parms[1] = billtoName;
				_jacc_parms[2] = rrcId;
				_jacc_parms[3] = rrcName;
				_jacc_parms[4] = shiptoId;
				_jacc_parms[5] = shiptoName;
				_jacc_parms[6] = createdTime;
				_jacc_parms[7] = lastUpdate;
				_jacc_parms[8] = markForDelete;
			}
	com.ibm.commerce.extension.objects.EJSCMPXaddressRelationHomeBean_0c064b7f beanRef = (com.ibm.commerce.extension.objects.EJSCMPXaddressRelationHomeBean_0c064b7f)container.preInvoke(this, 1, _EJS_s, _jacc_parms);
			_EJS_result = beanRef.create(billtoId, billtoName, rrcId, rrcName, shiptoId, shiptoName, createdTime, lastUpdate, markForDelete);
		}
		catch (javax.ejb.CreateException ex) {
			_EJS_s.setCheckedException(ex);
			throw ex;
		}
		catch (java.rmi.RemoteException ex) {
			_EJS_s.setUncheckedException(ex);
		}
		catch (Throwable ex) {
			_EJS_s.setUncheckedException(ex);
			throw new java.rmi.RemoteException("bean method raised unchecked exception", ex);
		}

		finally {
			try{
				container.postInvoke(this, 1, _EJS_s);
			} finally {
				container.putEJSDeployedSupport(_EJS_s);
			}
		}
		return _EJS_result;
	}
	/**
	 * findByPrimaryKey
	 */
	public com.ibm.commerce.extension.objects.XaddressRelation findByPrimaryKey(com.ibm.commerce.extension.objects.XaddressRelationKey primaryKey) throws javax.ejb.FinderException, java.rmi.RemoteException {
		EJSDeployedSupport _EJS_s = container.getEJSDeployedSupport(this);
		Object[] _jacc_parms = null;
		com.ibm.commerce.extension.objects.XaddressRelation _EJS_result = null;
		try {
			if (container.doesJaccNeedsEJBArguments( this ))
			{
				_jacc_parms = new Object[1];
				_jacc_parms[0] = primaryKey;
			}
	com.ibm.commerce.extension.objects.EJSCMPXaddressRelationHomeBean_0c064b7f beanRef = (com.ibm.commerce.extension.objects.EJSCMPXaddressRelationHomeBean_0c064b7f)container.preInvoke(this, 2, _EJS_s, _jacc_parms);
			_EJS_result = beanRef.findByPrimaryKey(primaryKey);
		}
		catch (javax.ejb.FinderException ex) {
			_EJS_s.setCheckedException(ex);
			throw ex;
		}
		catch (java.rmi.RemoteException ex) {
			_EJS_s.setUncheckedException(ex);
		}
		catch (Throwable ex) {
			_EJS_s.setUncheckedException(ex);
			throw new java.rmi.RemoteException("bean method raised unchecked exception", ex);
		}

		finally {
			try{
				container.postInvoke(this, 2, _EJS_s);
			} finally {
				container.putEJSDeployedSupport(_EJS_s);
			}
		}
		return _EJS_result;
	}
	/**
	 * findbyBillToId
	 */
	public java.util.Enumeration findbyBillToId(java.lang.Long billToId) throws javax.ejb.FinderException, java.rmi.RemoteException {
		EJSDeployedSupport _EJS_s = container.getEJSDeployedSupport(this);
		Object[] _jacc_parms = null;
		java.util.Enumeration _EJS_result = null;
		try {
			if (container.doesJaccNeedsEJBArguments( this ))
			{
				_jacc_parms = new Object[1];
				_jacc_parms[0] = billToId;
			}
	com.ibm.commerce.extension.objects.EJSCMPXaddressRelationHomeBean_0c064b7f beanRef = (com.ibm.commerce.extension.objects.EJSCMPXaddressRelationHomeBean_0c064b7f)container.preInvoke(this, 3, _EJS_s, _jacc_parms);
			_EJS_result = beanRef.findbyBillToId(billToId);
		}
		catch (javax.ejb.FinderException ex) {
			_EJS_s.setCheckedException(ex);
			throw ex;
		}
		catch (java.rmi.RemoteException ex) {
			_EJS_s.setUncheckedException(ex);
		}
		catch (Throwable ex) {
			_EJS_s.setUncheckedException(ex);
			throw new java.rmi.RemoteException("bean method raised unchecked exception", ex);
		}

		finally {
			try{
				container.postInvoke(this, 3, _EJS_s);
			} finally {
				container.putEJSDeployedSupport(_EJS_s);
			}
		}
		return _EJS_result;
	}
	/**
	 * findbyRrcId
	 */
	public java.util.Enumeration findbyRrcId(java.lang.Long rrcId) throws javax.ejb.FinderException, java.rmi.RemoteException {
		EJSDeployedSupport _EJS_s = container.getEJSDeployedSupport(this);
		Object[] _jacc_parms = null;
		java.util.Enumeration _EJS_result = null;
		try {
			if (container.doesJaccNeedsEJBArguments( this ))
			{
				_jacc_parms = new Object[1];
				_jacc_parms[0] = rrcId;
			}
	com.ibm.commerce.extension.objects.EJSCMPXaddressRelationHomeBean_0c064b7f beanRef = (com.ibm.commerce.extension.objects.EJSCMPXaddressRelationHomeBean_0c064b7f)container.preInvoke(this, 4, _EJS_s, _jacc_parms);
			_EJS_result = beanRef.findbyRrcId(rrcId);
		}
		catch (javax.ejb.FinderException ex) {
			_EJS_s.setCheckedException(ex);
			throw ex;
		}
		catch (java.rmi.RemoteException ex) {
			_EJS_s.setUncheckedException(ex);
		}
		catch (Throwable ex) {
			_EJS_s.setUncheckedException(ex);
			throw new java.rmi.RemoteException("bean method raised unchecked exception", ex);
		}

		finally {
			try{
				container.postInvoke(this, 4, _EJS_s);
			} finally {
				container.putEJSDeployedSupport(_EJS_s);
			}
		}
		return _EJS_result;
	}
	/**
	 * getEJBMetaData
	 */
	public javax.ejb.EJBMetaData getEJBMetaData() throws java.rmi.RemoteException {
		EJSDeployedSupport _EJS_s = container.getEJSDeployedSupport(this);
		Object[] _jacc_parms = null;
		javax.ejb.EJBMetaData _EJS_result = null;
		try {
			if (container.doesJaccNeedsEJBArguments( this ))
			{
				_jacc_parms = new Object[0];
			}
	com.ibm.commerce.extension.objects.EJSCMPXaddressRelationHomeBean_0c064b7f beanRef = (com.ibm.commerce.extension.objects.EJSCMPXaddressRelationHomeBean_0c064b7f)container.preInvoke(this, 5, _EJS_s, _jacc_parms);
			_EJS_result = beanRef.getEJBMetaData();
		}
		catch (java.rmi.RemoteException ex) {
			_EJS_s.setUncheckedException(ex);
		}
		catch (Throwable ex) {
			_EJS_s.setUncheckedException(ex);
			throw new java.rmi.RemoteException("bean method raised unchecked exception", ex);
		}

		finally {
			try{
				container.postInvoke(this, 5, _EJS_s);
			} finally {
				container.putEJSDeployedSupport(_EJS_s);
			}
		}
		return _EJS_result;
	}
	/**
	 * getHomeHandle
	 */
	public javax.ejb.HomeHandle getHomeHandle() throws java.rmi.RemoteException {
		EJSDeployedSupport _EJS_s = container.getEJSDeployedSupport(this);
		Object[] _jacc_parms = null;
		javax.ejb.HomeHandle _EJS_result = null;
		try {
			if (container.doesJaccNeedsEJBArguments( this ))
			{
				_jacc_parms = new Object[0];
			}
	com.ibm.commerce.extension.objects.EJSCMPXaddressRelationHomeBean_0c064b7f beanRef = (com.ibm.commerce.extension.objects.EJSCMPXaddressRelationHomeBean_0c064b7f)container.preInvoke(this, 6, _EJS_s, _jacc_parms);
			_EJS_result = beanRef.getHomeHandle();
		}
		catch (java.rmi.RemoteException ex) {
			_EJS_s.setUncheckedException(ex);
		}
		catch (Throwable ex) {
			_EJS_s.setUncheckedException(ex);
			throw new java.rmi.RemoteException("bean method raised unchecked exception", ex);
		}

		finally {
			try{
				container.postInvoke(this, 6, _EJS_s);
			} finally {
				container.putEJSDeployedSupport(_EJS_s);
			}
		}
		return _EJS_result;
	}
	/**
	 * remove
	 */
	public void remove(java.lang.Object primaryKey) throws java.rmi.RemoteException, javax.ejb.RemoveException {
		EJSDeployedSupport _EJS_s = container.getEJSDeployedSupport(this);
		Object[] _jacc_parms = null;
		
		try {
			if (container.doesJaccNeedsEJBArguments( this ))
			{
				_jacc_parms = new Object[1];
				_jacc_parms[0] = primaryKey;
			}
	com.ibm.commerce.extension.objects.EJSCMPXaddressRelationHomeBean_0c064b7f beanRef = (com.ibm.commerce.extension.objects.EJSCMPXaddressRelationHomeBean_0c064b7f)container.preInvoke(this, 7, _EJS_s, _jacc_parms);
			beanRef.remove(primaryKey);
		}
		catch (java.rmi.RemoteException ex) {
			_EJS_s.setUncheckedException(ex);
		}
		catch (javax.ejb.RemoveException ex) {
			_EJS_s.setCheckedException(ex);
			throw ex;
		}
		catch (Throwable ex) {
			_EJS_s.setUncheckedException(ex);
			throw new java.rmi.RemoteException("bean method raised unchecked exception", ex);
		}

		finally {
			try{
				container.postInvoke(this, 7, _EJS_s);
			} finally {
				container.putEJSDeployedSupport(_EJS_s);
			}
		}
		return ;
	}
	/**
	 * remove
	 */
	public void remove(javax.ejb.Handle handle) throws java.rmi.RemoteException, javax.ejb.RemoveException {
		EJSDeployedSupport _EJS_s = container.getEJSDeployedSupport(this);
		Object[] _jacc_parms = null;
		
		try {
			if (container.doesJaccNeedsEJBArguments( this ))
			{
				_jacc_parms = new Object[1];
				_jacc_parms[0] = handle;
			}
	com.ibm.commerce.extension.objects.EJSCMPXaddressRelationHomeBean_0c064b7f beanRef = (com.ibm.commerce.extension.objects.EJSCMPXaddressRelationHomeBean_0c064b7f)container.preInvoke(this, 8, _EJS_s, _jacc_parms);
			beanRef.remove(handle);
		}
		catch (java.rmi.RemoteException ex) {
			_EJS_s.setUncheckedException(ex);
		}
		catch (javax.ejb.RemoveException ex) {
			_EJS_s.setCheckedException(ex);
			throw ex;
		}
		catch (Throwable ex) {
			_EJS_s.setUncheckedException(ex);
			throw new java.rmi.RemoteException("bean method raised unchecked exception", ex);
		}

		finally {
			try{
				container.postInvoke(this, 8, _EJS_s);
			} finally {
				container.putEJSDeployedSupport(_EJS_s);
			}
		}
		return ;
	}
}
