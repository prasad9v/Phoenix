package com.ibm.commerce.extension.objects;

import com.ibm.ejs.container.*;

/**
 * EJSRemoteCMPXorderItemsHome_80e4dc6d
 */
public class EJSRemoteCMPXorderItemsHome_80e4dc6d extends EJSWrapper implements com.ibm.commerce.extension.objects.XorderItemsHome {
	/**
	 * EJSRemoteCMPXorderItemsHome_80e4dc6d
	 */
	public EJSRemoteCMPXorderItemsHome_80e4dc6d() throws java.rmi.RemoteException {
		super();	}
	/**
	 * create
	 */
	public com.ibm.commerce.extension.objects.XorderItems create(java.lang.Integer orderLineItem_Id, java.lang.Long orders_Id, java.lang.Long orderItems_Id, java.lang.String salesOrder_Id) throws javax.ejb.CreateException, java.rmi.RemoteException {
		EJSDeployedSupport _EJS_s = container.getEJSDeployedSupport(this);
		Object[] _jacc_parms = null;
		com.ibm.commerce.extension.objects.XorderItems _EJS_result = null;
		try {
			if (container.doesJaccNeedsEJBArguments( this ))
			{
				_jacc_parms = new Object[4];
				_jacc_parms[0] = orderLineItem_Id;
				_jacc_parms[1] = orders_Id;
				_jacc_parms[2] = orderItems_Id;
				_jacc_parms[3] = salesOrder_Id;
			}
	com.ibm.commerce.extension.objects.EJSCMPXorderItemsHomeBean_80e4dc6d beanRef = (com.ibm.commerce.extension.objects.EJSCMPXorderItemsHomeBean_80e4dc6d)container.preInvoke(this, 0, _EJS_s, _jacc_parms);
			_EJS_result = beanRef.create(orderLineItem_Id, orders_Id, orderItems_Id, salesOrder_Id);
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
	public com.ibm.commerce.extension.objects.XorderItems create(java.lang.Integer orderLineItem_Id, java.lang.Long orders_Id, java.lang.Long orderItems_Id, java.lang.String salesOrder_Id, java.math.BigDecimal totalWeight, java.lang.String weighType, java.lang.String hotOrder, java.lang.Integer sourcing_Id, java.lang.String ordStatus, java.lang.Character ordItemStatus, java.sql.Timestamp estimateDeliveryDate, java.lang.String bolIdentifier, java.lang.String manifestIdentifier, java.lang.String invoiceIdentifier) throws javax.ejb.CreateException, java.rmi.RemoteException {
		EJSDeployedSupport _EJS_s = container.getEJSDeployedSupport(this);
		Object[] _jacc_parms = null;
		com.ibm.commerce.extension.objects.XorderItems _EJS_result = null;
		try {
			if (container.doesJaccNeedsEJBArguments( this ))
			{
				_jacc_parms = new Object[14];
				_jacc_parms[0] = orderLineItem_Id;
				_jacc_parms[1] = orders_Id;
				_jacc_parms[2] = orderItems_Id;
				_jacc_parms[3] = salesOrder_Id;
				_jacc_parms[4] = totalWeight;
				_jacc_parms[5] = weighType;
				_jacc_parms[6] = hotOrder;
				_jacc_parms[7] = sourcing_Id;
				_jacc_parms[8] = ordStatus;
				_jacc_parms[9] = ordItemStatus;
				_jacc_parms[10] = estimateDeliveryDate;
				_jacc_parms[11] = bolIdentifier;
				_jacc_parms[12] = manifestIdentifier;
				_jacc_parms[13] = invoiceIdentifier;
			}
	com.ibm.commerce.extension.objects.EJSCMPXorderItemsHomeBean_80e4dc6d beanRef = (com.ibm.commerce.extension.objects.EJSCMPXorderItemsHomeBean_80e4dc6d)container.preInvoke(this, 1, _EJS_s, _jacc_parms);
			_EJS_result = beanRef.create(orderLineItem_Id, orders_Id, orderItems_Id, salesOrder_Id, totalWeight, weighType, hotOrder, sourcing_Id, ordStatus, ordItemStatus, estimateDeliveryDate, bolIdentifier, manifestIdentifier, invoiceIdentifier);
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
	public com.ibm.commerce.extension.objects.XorderItems findByPrimaryKey(com.ibm.commerce.extension.objects.XorderItemsKey primaryKey) throws javax.ejb.FinderException, java.rmi.RemoteException {
		EJSDeployedSupport _EJS_s = container.getEJSDeployedSupport(this);
		Object[] _jacc_parms = null;
		com.ibm.commerce.extension.objects.XorderItems _EJS_result = null;
		try {
			if (container.doesJaccNeedsEJBArguments( this ))
			{
				_jacc_parms = new Object[1];
				_jacc_parms[0] = primaryKey;
			}
	com.ibm.commerce.extension.objects.EJSCMPXorderItemsHomeBean_80e4dc6d beanRef = (com.ibm.commerce.extension.objects.EJSCMPXorderItemsHomeBean_80e4dc6d)container.preInvoke(this, 2, _EJS_s, _jacc_parms);
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
	 * findfindByCompositeIds
	 */
	public com.ibm.commerce.extension.objects.XorderItems findfindByCompositeIds(java.lang.Integer orderLineItem_Id, java.lang.Long orders_Id, java.lang.Long orderItems_Id, java.lang.String salesOrder_Id) throws javax.ejb.FinderException, java.rmi.RemoteException {
		EJSDeployedSupport _EJS_s = container.getEJSDeployedSupport(this);
		Object[] _jacc_parms = null;
		com.ibm.commerce.extension.objects.XorderItems _EJS_result = null;
		try {
			if (container.doesJaccNeedsEJBArguments( this ))
			{
				_jacc_parms = new Object[4];
				_jacc_parms[0] = orderLineItem_Id;
				_jacc_parms[1] = orders_Id;
				_jacc_parms[2] = orderItems_Id;
				_jacc_parms[3] = salesOrder_Id;
			}
	com.ibm.commerce.extension.objects.EJSCMPXorderItemsHomeBean_80e4dc6d beanRef = (com.ibm.commerce.extension.objects.EJSCMPXorderItemsHomeBean_80e4dc6d)container.preInvoke(this, 3, _EJS_s, _jacc_parms);
			_EJS_result = beanRef.findfindByCompositeIds(orderLineItem_Id, orders_Id, orderItems_Id, salesOrder_Id);
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
	 * findByOrderItemId
	 */
	public java.util.Enumeration findByOrderItemId(java.lang.Long orderItemId) throws javax.ejb.FinderException, java.rmi.RemoteException {
		EJSDeployedSupport _EJS_s = container.getEJSDeployedSupport(this);
		Object[] _jacc_parms = null;
		java.util.Enumeration _EJS_result = null;
		try {
			if (container.doesJaccNeedsEJBArguments( this ))
			{
				_jacc_parms = new Object[1];
				_jacc_parms[0] = orderItemId;
			}
	com.ibm.commerce.extension.objects.EJSCMPXorderItemsHomeBean_80e4dc6d beanRef = (com.ibm.commerce.extension.objects.EJSCMPXorderItemsHomeBean_80e4dc6d)container.preInvoke(this, 4, _EJS_s, _jacc_parms);
			_EJS_result = beanRef.findByOrderItemId(orderItemId);
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
	 * findbysalesOrder_id
	 */
	public java.util.Enumeration findbysalesOrder_id(java.lang.String salesorder_id) throws javax.ejb.FinderException, java.rmi.RemoteException {
		EJSDeployedSupport _EJS_s = container.getEJSDeployedSupport(this);
		Object[] _jacc_parms = null;
		java.util.Enumeration _EJS_result = null;
		try {
			if (container.doesJaccNeedsEJBArguments( this ))
			{
				_jacc_parms = new Object[1];
				_jacc_parms[0] = salesorder_id;
			}
	com.ibm.commerce.extension.objects.EJSCMPXorderItemsHomeBean_80e4dc6d beanRef = (com.ibm.commerce.extension.objects.EJSCMPXorderItemsHomeBean_80e4dc6d)container.preInvoke(this, 5, _EJS_s, _jacc_parms);
			_EJS_result = beanRef.findbysalesOrder_id(salesorder_id);
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
				container.postInvoke(this, 5, _EJS_s);
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
	com.ibm.commerce.extension.objects.EJSCMPXorderItemsHomeBean_80e4dc6d beanRef = (com.ibm.commerce.extension.objects.EJSCMPXorderItemsHomeBean_80e4dc6d)container.preInvoke(this, 6, _EJS_s, _jacc_parms);
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
				container.postInvoke(this, 6, _EJS_s);
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
	com.ibm.commerce.extension.objects.EJSCMPXorderItemsHomeBean_80e4dc6d beanRef = (com.ibm.commerce.extension.objects.EJSCMPXorderItemsHomeBean_80e4dc6d)container.preInvoke(this, 7, _EJS_s, _jacc_parms);
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
				container.postInvoke(this, 7, _EJS_s);
			} finally {
				container.putEJSDeployedSupport(_EJS_s);
			}
		}
		return _EJS_result;
	}
	/**
	 * remove
	 */
	public void remove(java.lang.Object obj) throws java.rmi.RemoteException, javax.ejb.RemoveException {
		EJSDeployedSupport _EJS_s = container.getEJSDeployedSupport(this);
		Object[] _jacc_parms = null;
		
		try {
			if (container.doesJaccNeedsEJBArguments( this ))
			{
				_jacc_parms = new Object[1];
				_jacc_parms[0] = obj;
			}
	com.ibm.commerce.extension.objects.EJSCMPXorderItemsHomeBean_80e4dc6d beanRef = (com.ibm.commerce.extension.objects.EJSCMPXorderItemsHomeBean_80e4dc6d)container.preInvoke(this, 8, _EJS_s, _jacc_parms);
			beanRef.remove(obj);
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
	com.ibm.commerce.extension.objects.EJSCMPXorderItemsHomeBean_80e4dc6d beanRef = (com.ibm.commerce.extension.objects.EJSCMPXorderItemsHomeBean_80e4dc6d)container.preInvoke(this, 9, _EJS_s, _jacc_parms);
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
				container.postInvoke(this, 9, _EJS_s);
			} finally {
				container.putEJSDeployedSupport(_EJS_s);
			}
		}
		return ;
	}
}
