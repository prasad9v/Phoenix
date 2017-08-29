package com.ibm.commerce.extension.objects;

import com.ibm.ejs.persistence.*;
import javax.ejb.EntityBean;
import java.sql.*;

/**
 * EJSJDBCPersisterCMPXorderItemsBean_80e4dc6d
 */
public class EJSJDBCPersisterCMPXorderItemsBean_80e4dc6d extends EJSJDBCPersister implements com.ibm.commerce.extension.objects.EJSFinderXorderItemsBean {
	private static final String _createString = "INSERT INTO XORDITEMS (ORDERLINEITEM_ID, ORDERS_ID, ORDERITEMS_ID, SALESORDER_ID, TOTALWEIGHT, WEIGHTYPE, HOTORDER, SOURCING_ID, ORDSTATUS, ORDITEMSTATUS, ESTIMATEDELIVERYDATE, BOLIDENTIFIER, MANIFESTIDENTIFIER, INVOICEIDENTIFIER , OPTCOUNTER) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String _removeString = "DELETE FROM XORDITEMS  WHERE ORDERLINEITEM_ID = ? AND ORDERS_ID = ? AND ORDERITEMS_ID = ? AND SALESORDER_ID = ?";
	private static final String _storeString = "UPDATE XORDITEMS  SET TOTALWEIGHT = ?, WEIGHTYPE = ?, HOTORDER = ?, SOURCING_ID = ?, ORDSTATUS = ?, ORDITEMSTATUS = ?, ESTIMATEDELIVERYDATE = ?, BOLIDENTIFIER = ?, MANIFESTIDENTIFIER = ?, INVOICEIDENTIFIER  = ?, OPTCOUNTER = ? WHERE ORDERLINEITEM_ID = ? AND ORDERS_ID = ? AND ORDERITEMS_ID = ? AND SALESORDER_ID = ?";
	private static final String _loadString = " SELECT T1.ORDERLINEITEM_ID, T1.ORDERS_ID, T1.ORDERITEMS_ID, T1.SALESORDER_ID, T1.TOTALWEIGHT, T1.WEIGHTYPE, T1.HOTORDER, T1.SOURCING_ID, T1.ORDSTATUS, T1.ORDITEMSTATUS, T1.ESTIMATEDELIVERYDATE, T1.BOLIDENTIFIER, T1.MANIFESTIDENTIFIER, T1.INVOICEIDENTIFIER , T1.OPTCOUNTER FROM XORDITEMS  T1 WHERE T1.ORDERLINEITEM_ID = ? AND T1.ORDERS_ID = ? AND T1.ORDERITEMS_ID = ? AND T1.SALESORDER_ID = ?";
	private static final String _loadForUpdateString = _loadString + " FOR UPDATE";
	private static final String[] _predicateColumnNames = {"OPTCOUNTER"};
	private static final boolean[] _predicateMaps = {true};
	private byte[] serObj = null;
	/**
	 * EJSJDBCPersisterCMPXorderItemsBean_80e4dc6d
	 */
	public EJSJDBCPersisterCMPXorderItemsBean_80e4dc6d() throws java.rmi.RemoteException {
		super();	}
	/**
	 * postInit
	 */
	public void postInit() {
	}
	/**
	 * _create
	 */
	public void _create(EntityBean eb) throws Exception {
		Object objectTemp = null;
		XorderItemsBean b = (XorderItemsBean) eb;
		PreparedStatement pstmt;
		Object cacheData[] = new Object[1];
		pstmt = getPreparedStatement(_createString);
		try {
			if (b.orderLineItem_Id == null) {
				pstmt.setNull(1, java.sql.Types.INTEGER);
			}
			else {
				pstmt.setInt(1, b.orderLineItem_Id.intValue());
			}
			if (b.orders_Id == null) {
				pstmt.setNull(2, java.sql.Types.BIGINT);
			}
			else {
				pstmt.setLong(2, b.orders_Id.longValue());
			}
			if (b.orderItems_Id == null) {
				pstmt.setNull(3, java.sql.Types.BIGINT);
			}
			else {
				pstmt.setLong(3, b.orderItems_Id.longValue());
			}
			objectTemp = com.ibm.vap.converters.VapTrimStringConverter.singleton().dataFrom(b.salesOrder_Id);
			if (objectTemp == null) {
				pstmt.setNull(4, java.sql.Types.CHAR);
			}
			else {
				pstmt.setString(4, (java.lang.String)objectTemp);
			}
			if (b.totalWeight == null) {
				pstmt.setNull(5, java.sql.Types.DECIMAL);
			}
			else {
				pstmt.setBigDecimal(5, b.totalWeight);
			}
			objectTemp = com.ibm.vap.converters.VapTrimStringConverter.singleton().dataFrom(b.weighType);
			if (objectTemp == null) {
				pstmt.setNull(6, java.sql.Types.VARCHAR);
			}
			else {
				pstmt.setString(6, (java.lang.String)objectTemp);
			}
			objectTemp = com.ibm.vap.converters.VapTrimStringConverter.singleton().dataFrom(b.hotOrder);
			if (objectTemp == null) {
				pstmt.setNull(7, java.sql.Types.VARCHAR);
			}
			else {
				pstmt.setString(7, (java.lang.String)objectTemp);
			}
			if (b.sourcing_Id == null) {
				pstmt.setNull(8, java.sql.Types.INTEGER);
			}
			else {
				pstmt.setInt(8, b.sourcing_Id.intValue());
			}
			objectTemp = com.ibm.vap.converters.VapTrimStringConverter.singleton().dataFrom(b.ordStatus);
			if (objectTemp == null) {
				pstmt.setNull(9, java.sql.Types.VARCHAR);
			}
			else {
				pstmt.setString(9, (java.lang.String)objectTemp);
			}
			objectTemp = com.ibm.vap.converters.VapStringToCharacterConverter.singleton().dataFrom(b.ordItemStatus);
			if (objectTemp == null) {
				pstmt.setNull(10, java.sql.Types.CHAR);
			}
			else {
				pstmt.setString(10, (java.lang.String)objectTemp);
			}
			if (b.estimateDeliveryDate == null) {
				pstmt.setNull(11, java.sql.Types.TIMESTAMP);
			}
			else {
				pstmt.setTimestamp(11, b.estimateDeliveryDate);
			}
			objectTemp = com.ibm.vap.converters.VapTrimStringConverter.singleton().dataFrom(b.bolIdentifier);
			if (objectTemp == null) {
				pstmt.setNull(12, java.sql.Types.VARCHAR);
			}
			else {
				pstmt.setString(12, (java.lang.String)objectTemp);
			}
			objectTemp = com.ibm.vap.converters.VapTrimStringConverter.singleton().dataFrom(b.manifestIdentifier);
			if (objectTemp == null) {
				pstmt.setNull(13, java.sql.Types.VARCHAR);
			}
			else {
				pstmt.setString(13, (java.lang.String)objectTemp);
			}
			objectTemp = com.ibm.vap.converters.VapTrimStringConverter.singleton().dataFrom(b.invoiceIdentifier);
			if (objectTemp == null) {
				pstmt.setNull(14, java.sql.Types.VARCHAR);
			}
			else {
				pstmt.setString(14, (java.lang.String)objectTemp);
			}
			pstmt.setShort(15, b.optCounter);
			cacheData[0] = new Short(b.optCounter);
			pstmt.executeUpdate();
		}
		finally {
			returnPreparedStatement(pstmt);
		}
		putDataIntoCache(cacheData);
	}
	/**
	 * hydrate
	 */
	public void hydrate(EntityBean eb, Object data, Object pKey) throws Exception {
		Object objectTemp = null;
		XorderItemsBean b = (XorderItemsBean) eb;
		com.ibm.commerce.extension.objects.XorderItemsKey _primaryKey = (com.ibm.commerce.extension.objects.XorderItemsKey)pKey;
		java.sql.ResultSet resultSet = (java.sql.ResultSet) data;
		short tempshort;
		int tempint;

		b.orderLineItem_Id = _primaryKey.orderLineItem_Id;
		b.orders_Id = _primaryKey.orders_Id;
		b.orderItems_Id = _primaryKey.orderItems_Id;
		b.salesOrder_Id = _primaryKey.salesOrder_Id;
		b.totalWeight = resultSet.getBigDecimal(5);
		b.weighType = (java.lang.String)com.ibm.vap.converters.VapTrimStringConverter.singleton().objectFrom(resultSet.getString(6));
		b.hotOrder = (java.lang.String)com.ibm.vap.converters.VapTrimStringConverter.singleton().objectFrom(resultSet.getString(7));
		tempint = resultSet.getInt(8);
		b.sourcing_Id = resultSet.wasNull() ? null : new Integer(tempint);
		b.ordStatus = (java.lang.String)com.ibm.vap.converters.VapTrimStringConverter.singleton().objectFrom(resultSet.getString(9));
		b.ordItemStatus = (java.lang.Character)com.ibm.vap.converters.VapStringToCharacterConverter.singleton().objectFrom(resultSet.getString(10));
		b.estimateDeliveryDate = resultSet.getTimestamp(11);
		b.bolIdentifier = (java.lang.String)com.ibm.vap.converters.VapTrimStringConverter.singleton().objectFrom(resultSet.getString(12));
		b.manifestIdentifier = (java.lang.String)com.ibm.vap.converters.VapTrimStringConverter.singleton().objectFrom(resultSet.getString(13));
		b.invoiceIdentifier = (java.lang.String)com.ibm.vap.converters.VapTrimStringConverter.singleton().objectFrom(resultSet.getString(14));
		b.optCounter = resultSet.getShort(15);
		Object[] cacheData = new Object[1];
		tempshort = resultSet.getShort(15);
		cacheData[0] = resultSet.wasNull() ? null : new Short(tempshort);
		putDataIntoCache(cacheData);
	}
	/**
	 * load
	 */
	public void load(EntityBean eb, Object pKey, boolean forUpdate) throws Exception {
		Object objectTemp = null;
		XorderItemsBean b = (XorderItemsBean) eb;
		com.ibm.commerce.extension.objects.XorderItemsKey _primaryKey = (com.ibm.commerce.extension.objects.XorderItemsKey)pKey;
		PreparedStatement pstmt;
		ResultSet resultSet = null;
		pstmt = getPreparedStatement(_loadString);
		try {
			if (_primaryKey.orderLineItem_Id == null) {
				pstmt.setNull(1, java.sql.Types.INTEGER);
			}
			else {
				pstmt.setInt(1, _primaryKey.orderLineItem_Id.intValue());
			}
			if (_primaryKey.orders_Id == null) {
				pstmt.setNull(2, java.sql.Types.BIGINT);
			}
			else {
				pstmt.setLong(2, _primaryKey.orders_Id.longValue());
			}
			if (_primaryKey.orderItems_Id == null) {
				pstmt.setNull(3, java.sql.Types.BIGINT);
			}
			else {
				pstmt.setLong(3, _primaryKey.orderItems_Id.longValue());
			}
			objectTemp = com.ibm.vap.converters.VapTrimStringConverter.singleton().dataFrom(_primaryKey.salesOrder_Id);
			if (objectTemp == null) {
				pstmt.setNull(4, java.sql.Types.CHAR);
			}
			else {
				pstmt.setString(4, (java.lang.String)objectTemp);
			}
			resultSet = pstmt.executeQuery();
			if (!(resultSet.next())) throw new javax.ejb.ObjectNotFoundException();
			hydrate(eb, resultSet, pKey);
		}
		finally {
			if (resultSet != null) resultSet.close();
			returnPreparedStatement(pstmt);
		}
	}
	/**
	 * refresh
	 */
	public void refresh(EntityBean eb, boolean forUpdate) throws Exception {
		XorderItemsBean b = (XorderItemsBean) eb;
		com.ibm.commerce.extension.objects.XorderItemsKey _primaryKey = new com.ibm.commerce.extension.objects.XorderItemsKey();
		_primaryKey.orderLineItem_Id = b.orderLineItem_Id;
		_primaryKey.orders_Id = b.orders_Id;
		_primaryKey.orderItems_Id = b.orderItems_Id;
		_primaryKey.salesOrder_Id = b.salesOrder_Id;
		load(b, _primaryKey, forUpdate);
	}
	/**
	 * store
	 */
	public void store(EntityBean eb) throws Exception {
		Object objectTemp = null;
		XorderItemsBean b = (XorderItemsBean) eb;
		com.ibm.commerce.extension.objects.XorderItemsKey _primaryKey = new com.ibm.commerce.extension.objects.XorderItemsKey();
		_primaryKey.orderLineItem_Id = b.orderLineItem_Id;
		_primaryKey.orders_Id = b.orders_Id;
		_primaryKey.orderItems_Id = b.orderItems_Id;
		_primaryKey.salesOrder_Id = b.salesOrder_Id;
		PreparedStatement pstmt;
		Object cacheData[] = getDataFromCache();
		RdbRt  aTemplate = new RdbRt(_storeString, _predicateColumnNames, _predicateMaps);
		pstmt = getPreparedStatement(aTemplate.nativeQuery(cacheData));
		try {
			if (_primaryKey.orderLineItem_Id == null) {
				pstmt.setNull(12, java.sql.Types.INTEGER);
			}
			else {
				pstmt.setInt(12, _primaryKey.orderLineItem_Id.intValue());
			}
			if (_primaryKey.orders_Id == null) {
				pstmt.setNull(13, java.sql.Types.BIGINT);
			}
			else {
				pstmt.setLong(13, _primaryKey.orders_Id.longValue());
			}
			if (_primaryKey.orderItems_Id == null) {
				pstmt.setNull(14, java.sql.Types.BIGINT);
			}
			else {
				pstmt.setLong(14, _primaryKey.orderItems_Id.longValue());
			}
			objectTemp = com.ibm.vap.converters.VapTrimStringConverter.singleton().dataFrom(_primaryKey.salesOrder_Id);
			if (objectTemp == null) {
				pstmt.setNull(15, java.sql.Types.CHAR);
			}
			else {
				pstmt.setString(15, (java.lang.String)objectTemp);
			}
			if (b.totalWeight == null) {
				pstmt.setNull(1, java.sql.Types.DECIMAL);
			}
			else {
				pstmt.setBigDecimal(1, b.totalWeight);
			}
			objectTemp = com.ibm.vap.converters.VapTrimStringConverter.singleton().dataFrom(b.weighType);
			if (objectTemp == null) {
				pstmt.setNull(2, java.sql.Types.VARCHAR);
			}
			else {
				pstmt.setString(2, (java.lang.String)objectTemp);
			}
			objectTemp = com.ibm.vap.converters.VapTrimStringConverter.singleton().dataFrom(b.hotOrder);
			if (objectTemp == null) {
				pstmt.setNull(3, java.sql.Types.VARCHAR);
			}
			else {
				pstmt.setString(3, (java.lang.String)objectTemp);
			}
			if (b.sourcing_Id == null) {
				pstmt.setNull(4, java.sql.Types.INTEGER);
			}
			else {
				pstmt.setInt(4, b.sourcing_Id.intValue());
			}
			objectTemp = com.ibm.vap.converters.VapTrimStringConverter.singleton().dataFrom(b.ordStatus);
			if (objectTemp == null) {
				pstmt.setNull(5, java.sql.Types.VARCHAR);
			}
			else {
				pstmt.setString(5, (java.lang.String)objectTemp);
			}
			objectTemp = com.ibm.vap.converters.VapStringToCharacterConverter.singleton().dataFrom(b.ordItemStatus);
			if (objectTemp == null) {
				pstmt.setNull(6, java.sql.Types.CHAR);
			}
			else {
				pstmt.setString(6, (java.lang.String)objectTemp);
			}
			if (b.estimateDeliveryDate == null) {
				pstmt.setNull(7, java.sql.Types.TIMESTAMP);
			}
			else {
				pstmt.setTimestamp(7, b.estimateDeliveryDate);
			}
			objectTemp = com.ibm.vap.converters.VapTrimStringConverter.singleton().dataFrom(b.bolIdentifier);
			if (objectTemp == null) {
				pstmt.setNull(8, java.sql.Types.VARCHAR);
			}
			else {
				pstmt.setString(8, (java.lang.String)objectTemp);
			}
			objectTemp = com.ibm.vap.converters.VapTrimStringConverter.singleton().dataFrom(b.manifestIdentifier);
			if (objectTemp == null) {
				pstmt.setNull(9, java.sql.Types.VARCHAR);
			}
			else {
				pstmt.setString(9, (java.lang.String)objectTemp);
			}
			objectTemp = com.ibm.vap.converters.VapTrimStringConverter.singleton().dataFrom(b.invoiceIdentifier);
			if (objectTemp == null) {
				pstmt.setNull(10, java.sql.Types.VARCHAR);
			}
			else {
				pstmt.setString(10, (java.lang.String)objectTemp);
			}
			pstmt.setShort(11, b.optCounter);
			int inputPos = 15;
			if (cacheData[0] != null) {
				pstmt.setShort(++inputPos, ((Number)cacheData[0]).shortValue());
			}
			if (pstmt.executeUpdate() < 1) {
				throw new OptimisticUpdateFailureException("executeUpdate returned zero rows updated");
			}
		}
		finally {
			returnPreparedStatement(pstmt);
		}
	}
	/**
	 * remove
	 */
	public void remove(EntityBean eb) throws Exception {
		Object objectTemp = null;
		XorderItemsBean b = (XorderItemsBean) eb;
		com.ibm.commerce.extension.objects.XorderItemsKey _primaryKey = new com.ibm.commerce.extension.objects.XorderItemsKey();
		_primaryKey.orderLineItem_Id = b.orderLineItem_Id;
		_primaryKey.orders_Id = b.orders_Id;
		_primaryKey.orderItems_Id = b.orderItems_Id;
		_primaryKey.salesOrder_Id = b.salesOrder_Id;
		PreparedStatement pstmt;
		Object cacheData[] = getDataFromCache();
		RdbRt  aTemplate = new RdbRt(_removeString, _predicateColumnNames, _predicateMaps);
		pstmt = getPreparedStatement(aTemplate.nativeQuery(cacheData));
		try {
			if (_primaryKey.orderLineItem_Id == null) {
				pstmt.setNull(1, java.sql.Types.INTEGER);
			}
			else {
				pstmt.setInt(1, _primaryKey.orderLineItem_Id.intValue());
			}
			if (_primaryKey.orders_Id == null) {
				pstmt.setNull(2, java.sql.Types.BIGINT);
			}
			else {
				pstmt.setLong(2, _primaryKey.orders_Id.longValue());
			}
			if (_primaryKey.orderItems_Id == null) {
				pstmt.setNull(3, java.sql.Types.BIGINT);
			}
			else {
				pstmt.setLong(3, _primaryKey.orderItems_Id.longValue());
			}
			objectTemp = com.ibm.vap.converters.VapTrimStringConverter.singleton().dataFrom(_primaryKey.salesOrder_Id);
			if (objectTemp == null) {
				pstmt.setNull(4, java.sql.Types.CHAR);
			}
			else {
				pstmt.setString(4, (java.lang.String)objectTemp);
			}
			int inputPos = 4;
			if (cacheData[0] != null) {
				pstmt.setShort(++inputPos, ((Number)cacheData[0]).shortValue());
			}
			if (pstmt.executeUpdate() < 1) {
				throw new OptimisticUpdateFailureException("executeUpdate returned zero rows updated");
			}
		}
		finally {
			returnPreparedStatement(pstmt);
		}
	}
	/**
	 * getPrimaryKey
	 */
	public Object getPrimaryKey(Object data) throws Exception {
		com.ibm.commerce.extension.objects.XorderItemsKey key = new com.ibm.commerce.extension.objects.XorderItemsKey();
		java.sql.ResultSet resultSet = (java.sql.ResultSet) data;

		if (resultSet != null) {
			Object objectTemp = null;
			int tempint;
			long templong;

			tempint = resultSet.getInt(1);
			key.orderLineItem_Id = resultSet.wasNull() ? null : new Integer(tempint);
			templong = resultSet.getLong(2);
			key.orders_Id = resultSet.wasNull() ? null : new Long(templong);
			templong = resultSet.getLong(3);
			key.orderItems_Id = resultSet.wasNull() ? null : new Long(templong);
			key.salesOrder_Id = (java.lang.String)com.ibm.vap.converters.VapTrimStringConverter.singleton().objectFrom(resultSet.getString(4));
			return key;
		}
return null;
	}

	/**
	 * RdbRt
	 */
	public class RdbRt {
	public static final String EQUALS = " = ";
	public static final String ISNULL = " IS NULL";
	public static final String MARKER = "?";
	public static final String AND = " AND ";
	private String fStatement;
	private String[] fPredicates = new String[0];
	private boolean[] fPredicateMap = new boolean[0];
	public RdbRt() { }
	public RdbRt(String aStmt) {
		rootStmt(aStmt);
	}
	public RdbRt(String aStmt, String[] anArray, boolean[] aMap) {
		rootStmt(aStmt);
		predicates(anArray);
		predicateMap(aMap);
	}
	public String nativeQuery() {
		StringBuffer aBuffer = new StringBuffer();
		statementOn(aBuffer);
		return aBuffer.toString();
	}
	public String nativeQuery(Object[] values) {
		StringBuffer aBuffer = new StringBuffer();
		statementOn(aBuffer, values);
		return aBuffer.toString();
	}
	public String[] predicates() {
		return fPredicates;
	}
	public void predicates(String[] anArray) {
		fPredicates = anArray;
	}
	public boolean[] predicateMap() {
		return fPredicateMap;
	}
	public void predicateMap(boolean[] anArray) {
		fPredicateMap = anArray;
	}
	public void predicatesOn(StringBuffer aBuffer, Object[] values) {
		int j = predicates().length;
		if (j != values.length)
			throw new RuntimeException("differing number of predicates and values");
		String each;
		for (int i = 0; i < j; i++) {
			if (!fPredicateMap[i]) continue;
				aBuffer.append(AND);
			each = predicates()[i];
			aBuffer.append(each);
			if (values[i] == null)
				aBuffer.append(ISNULL);
			else {
				aBuffer.append(EQUALS);
				aBuffer.append(MARKER);
			}
		}
	}
	public String rootStmt() {
		return fStatement;
	}
	public void rootStmt(String aStmt) {
		fStatement = aStmt;
	}
	public void statementOn(StringBuffer aBuffer) {
		aBuffer.append(rootStmt());
	}
	public void statementOn(StringBuffer aBuffer, Object[] values) {
		aBuffer.append(rootStmt());
		if (predicates() != null && predicates().length != 0) {
			predicatesOn(aBuffer, values);
		}
	}
	}
	/**
	 * supportsFluffOnFind
	 */
	public boolean supportsFluffOnFind() {
		return false;
	}
	/**
	 * findbysalesOrder_id
	 */
	public EJSFinder findbysalesOrder_id(java.lang.String salesorder_id) throws javax.ejb.FinderException, java.rmi.RemoteException {
		ResultSet resultSet = null;
		PreparedStatement pstmt = null;
		try {
			preFind();
			pstmt = getPreparedStatement(" SELECT T1.ORDERLINEITEM_ID, T1.ORDERS_ID, T1.ORDERITEMS_ID, T1.SALESORDER_ID, T1.TOTALWEIGHT, T1.WEIGHTYPE, T1.HOTORDER, T1.SOURCING_ID, T1.ORDSTATUS, T1.ORDITEMSTATUS, T1.ESTIMATEDELIVERYDATE, T1.BOLIDENTIFIER, T1.MANIFESTIDENTIFIER, T1.INVOICEIDENTIFIER , T1.OPTCOUNTER FROM XORDITEMS  T1 WHERE T1.SALESORDER_ID=?");
			if (salesorder_id == null) {
			   pstmt.setNull(1, java.sql.Types.VARCHAR);
			} else {
			   pstmt.setString(1, salesorder_id);
			}
			resultSet = pstmt.executeQuery();
			return new EJSJDBCFinder(resultSet, this, pstmt);
		}
		catch (Exception ex) {
			throw new EJSPersistenceException("find failed:", ex);
		}
	}
	/**
	 * findfindByCompositeIds
	 */
	public com.ibm.commerce.extension.objects.XorderItems findfindByCompositeIds(java.lang.Integer orderLineItem_Id, java.lang.Long orders_Id, java.lang.Long orderItems_Id, java.lang.String salesOrder_Id) throws javax.ejb.FinderException, java.rmi.RemoteException {
		ResultSet resultSet = null;
		PreparedStatement pstmt = null;
		com.ibm.commerce.extension.objects.XorderItems result = null;

		EJSJDBCFinder tmpFinder = null;
		try {
			preFind();
			pstmt = getPreparedStatement(" SELECT T1.ORDERLINEITEM_ID, T1.ORDERS_ID, T1.ORDERITEMS_ID, T1.SALESORDER_ID, T1.TOTALWEIGHT, T1.WEIGHTYPE, T1.HOTORDER, T1.SOURCING_ID, T1.ORDSTATUS, T1.ORDITEMSTATUS, T1.ESTIMATEDELIVERYDATE, T1.BOLIDENTIFIER, T1.MANIFESTIDENTIFIER, T1.INVOICEIDENTIFIER , T1.OPTCOUNTER FROM XORDITEMS  T1 WHERE T1.ORDERLINEITEM_ID = ? & T1.ORDERS_ID = ? & T1.ORDERITEMS_ID = ? & SALESORDER_ID =?");
			pstmt.setObject(1, orderLineItem_Id);
			pstmt.setLong(2, (long)orders_Id.longValue());
			pstmt.setLong(3, (long)orderItems_Id.longValue());
			if (salesOrder_Id == null) {
			   pstmt.setNull(4, java.sql.Types.VARCHAR);
			} else {
			   pstmt.setString(4, salesOrder_Id);
			}
			resultSet = pstmt.executeQuery();
			tmpFinder = new EJSJDBCFinder(resultSet, this, pstmt);
			if (tmpFinder.hasMoreElements()) {
				result = (com.ibm.commerce.extension.objects.XorderItems)tmpFinder.nextElement();
				if (tmpFinder.hasMoreElements())
					throw new javax.ejb.FinderException("Single object finder returned more than one object.");
			}
		}
		catch (javax.ejb.FinderException ex) {
			throw ex;
		}
		catch (Exception ex) {
			throw new EJSPersistenceException("find failed:", ex);
		}
		finally {
			if ( tmpFinder != null ) tmpFinder.close();
		}
		if (result == null) {
			throw new javax.ejb.ObjectNotFoundException();
		}
		return result;
	}
	/**
	 * findByPrimaryKey
	 */
	public com.ibm.commerce.extension.objects.XorderItems findByPrimaryKey(com.ibm.commerce.extension.objects.XorderItemsKey primaryKey) throws java.rmi.RemoteException, javax.ejb.FinderException {
		return (com.ibm.commerce.extension.objects.XorderItems) home.activateBean(primaryKey);
	}
	/**
	 * findByOrderItemId
	 */
	public EJSFinder findByOrderItemId(java.lang.Long orderItemId) throws javax.ejb.FinderException, java.rmi.RemoteException {
		ResultSet resultSet = null;
		PreparedStatement pstmt = null;
		try {
			preFind();
			pstmt = getPreparedStatement(" SELECT T1.ORDERLINEITEM_ID, T1.ORDERS_ID, T1.ORDERITEMS_ID, T1.SALESORDER_ID, T1.TOTALWEIGHT, T1.WEIGHTYPE, T1.HOTORDER, T1.SOURCING_ID, T1.ORDSTATUS, T1.ORDITEMSTATUS, T1.ESTIMATEDELIVERYDATE, T1.BOLIDENTIFIER, T1.MANIFESTIDENTIFIER, T1.INVOICEIDENTIFIER , T1.OPTCOUNTER FROM XORDITEMS  T1 WHERE T1.ORDERITEMS_ID=?");
			pstmt.setLong(1, (long)orderItemId.longValue());
			resultSet = pstmt.executeQuery();
			return new EJSJDBCFinder(resultSet, this, pstmt);
		}
		catch (Exception ex) {
			throw new EJSPersistenceException("find failed:", ex);
		}
	}
}
