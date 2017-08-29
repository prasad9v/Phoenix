package com.ibm.commerce.extension.objects;

import com.ibm.ejs.persistence.*;
import javax.ejb.EntityBean;
import java.sql.*;

/**
 * EJSJDBCPersisterCMPXaddressRelationBean_0c064b7f
 */
public class EJSJDBCPersisterCMPXaddressRelationBean_0c064b7f extends EJSJDBCPersister implements com.ibm.commerce.extension.objects.EJSFinderXaddressRelationBean {
	private static final String _createString = "INSERT INTO XADDRREL (BILLTO_ID, RRC_ID, SHIPTO_ID, OPTCOUNTER, RRC_NAME, SHIPTO_NAME, BILLTO_NAME, TIMEPCREATE, LASTUPDATE, MARKFORDELETE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String _removeString = "DELETE FROM XADDRREL  WHERE BILLTO_ID = ? AND RRC_ID = ? AND SHIPTO_ID = ?";
	private static final String _storeString = "UPDATE XADDRREL  SET OPTCOUNTER = ?, RRC_NAME = ?, SHIPTO_NAME = ?, BILLTO_NAME = ?, TIMEPCREATE = ?, LASTUPDATE = ?, MARKFORDELETE = ? WHERE BILLTO_ID = ? AND RRC_ID = ? AND SHIPTO_ID = ?";
	private static final String _loadString = " SELECT T1.OPTCOUNTER, T1.BILLTO_ID, T1.RRC_NAME, T1.RRC_ID, T1.SHIPTO_NAME, T1.SHIPTO_ID, T1.BILLTO_NAME, T1.TIMEPCREATE, T1.LASTUPDATE, T1.MARKFORDELETE FROM XADDRREL  T1 WHERE T1.BILLTO_ID = ? AND T1.RRC_ID = ? AND T1.SHIPTO_ID = ?";
	private static final String _loadForUpdateString = _loadString + " FOR UPDATE";
	private static final String[] _predicateColumnNames = {"OPTCOUNTER"};
	private static final boolean[] _predicateMaps = {true};
	private byte[] serObj = null;
	/**
	 * EJSJDBCPersisterCMPXaddressRelationBean_0c064b7f
	 */
	public EJSJDBCPersisterCMPXaddressRelationBean_0c064b7f() throws java.rmi.RemoteException {
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
		XaddressRelationBean b = (XaddressRelationBean) eb;
		PreparedStatement pstmt;
		Object cacheData[] = new Object[1];
		pstmt = getPreparedStatement(_createString);
		try {
			pstmt.setShort(4, b.optCounter);
			cacheData[0] = new Short(b.optCounter);
			if (b.billtoId == null) {
				pstmt.setNull(1, java.sql.Types.BIGINT);
			}
			else {
				pstmt.setLong(1, b.billtoId.longValue());
			}
			objectTemp = com.ibm.vap.converters.VapTrimStringConverter.singleton().dataFrom(b.rrcName);
			if (objectTemp == null) {
				pstmt.setNull(5, java.sql.Types.VARCHAR);
			}
			else {
				pstmt.setString(5, (java.lang.String)objectTemp);
			}
			if (b.rrcId == null) {
				pstmt.setNull(2, java.sql.Types.BIGINT);
			}
			else {
				pstmt.setLong(2, b.rrcId.longValue());
			}
			objectTemp = com.ibm.vap.converters.VapTrimStringConverter.singleton().dataFrom(b.shiptoName);
			if (objectTemp == null) {
				pstmt.setNull(6, java.sql.Types.VARCHAR);
			}
			else {
				pstmt.setString(6, (java.lang.String)objectTemp);
			}
			if (b.shiptoId == null) {
				pstmt.setNull(3, java.sql.Types.BIGINT);
			}
			else {
				pstmt.setLong(3, b.shiptoId.longValue());
			}
			objectTemp = com.ibm.vap.converters.VapTrimStringConverter.singleton().dataFrom(b.billtoName);
			if (objectTemp == null) {
				pstmt.setNull(7, java.sql.Types.VARCHAR);
			}
			else {
				pstmt.setString(7, (java.lang.String)objectTemp);
			}
			if (b.createdTime == null) {
				pstmt.setNull(8, java.sql.Types.TIMESTAMP);
			}
			else {
				pstmt.setTimestamp(8, b.createdTime);
			}
			if (b.lastUpdate == null) {
				pstmt.setNull(9, java.sql.Types.TIMESTAMP);
			}
			else {
				pstmt.setTimestamp(9, b.lastUpdate);
			}
			if (b.markForDelete == null) {
				pstmt.setNull(10, java.sql.Types.INTEGER);
			}
			else {
				pstmt.setInt(10, b.markForDelete.intValue());
			}
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
		XaddressRelationBean b = (XaddressRelationBean) eb;
		com.ibm.commerce.extension.objects.XaddressRelationKey _primaryKey = (com.ibm.commerce.extension.objects.XaddressRelationKey)pKey;
		java.sql.ResultSet resultSet = (java.sql.ResultSet) data;
		short tempshort;
		int tempint;

		b.billtoId = _primaryKey.billtoId;
		b.rrcId = _primaryKey.rrcId;
		b.shiptoId = _primaryKey.shiptoId;
		b.optCounter = resultSet.getShort(1);
		b.rrcName = (java.lang.String)com.ibm.vap.converters.VapTrimStringConverter.singleton().objectFrom(resultSet.getString(3));
		b.shiptoName = (java.lang.String)com.ibm.vap.converters.VapTrimStringConverter.singleton().objectFrom(resultSet.getString(5));
		b.billtoName = (java.lang.String)com.ibm.vap.converters.VapTrimStringConverter.singleton().objectFrom(resultSet.getString(7));
		b.createdTime = resultSet.getTimestamp(8);
		b.lastUpdate = resultSet.getTimestamp(9);
		tempint = resultSet.getInt(10);
		b.markForDelete = resultSet.wasNull() ? null : new Integer(tempint);
		Object[] cacheData = new Object[1];
		tempshort = resultSet.getShort(1);
		cacheData[0] = resultSet.wasNull() ? null : new Short(tempshort);
		putDataIntoCache(cacheData);
	}
	/**
	 * load
	 */
	public void load(EntityBean eb, Object pKey, boolean forUpdate) throws Exception {
		Object objectTemp = null;
		XaddressRelationBean b = (XaddressRelationBean) eb;
		com.ibm.commerce.extension.objects.XaddressRelationKey _primaryKey = (com.ibm.commerce.extension.objects.XaddressRelationKey)pKey;
		PreparedStatement pstmt;
		ResultSet resultSet = null;
		pstmt = getPreparedStatement(_loadString);
		try {
			if (_primaryKey.billtoId == null) {
				pstmt.setNull(1, java.sql.Types.BIGINT);
			}
			else {
				pstmt.setLong(1, _primaryKey.billtoId.longValue());
			}
			if (_primaryKey.rrcId == null) {
				pstmt.setNull(2, java.sql.Types.BIGINT);
			}
			else {
				pstmt.setLong(2, _primaryKey.rrcId.longValue());
			}
			if (_primaryKey.shiptoId == null) {
				pstmt.setNull(3, java.sql.Types.BIGINT);
			}
			else {
				pstmt.setLong(3, _primaryKey.shiptoId.longValue());
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
		XaddressRelationBean b = (XaddressRelationBean) eb;
		com.ibm.commerce.extension.objects.XaddressRelationKey _primaryKey = new com.ibm.commerce.extension.objects.XaddressRelationKey();
		_primaryKey.billtoId = b.billtoId;
		_primaryKey.rrcId = b.rrcId;
		_primaryKey.shiptoId = b.shiptoId;
		load(b, _primaryKey, forUpdate);
	}
	/**
	 * store
	 */
	public void store(EntityBean eb) throws Exception {
		Object objectTemp = null;
		XaddressRelationBean b = (XaddressRelationBean) eb;
		com.ibm.commerce.extension.objects.XaddressRelationKey _primaryKey = new com.ibm.commerce.extension.objects.XaddressRelationKey();
		_primaryKey.billtoId = b.billtoId;
		_primaryKey.rrcId = b.rrcId;
		_primaryKey.shiptoId = b.shiptoId;
		PreparedStatement pstmt;
		Object cacheData[] = getDataFromCache();
		RdbRt  aTemplate = new RdbRt(_storeString, _predicateColumnNames, _predicateMaps);
		pstmt = getPreparedStatement(aTemplate.nativeQuery(cacheData));
		try {
			if (_primaryKey.billtoId == null) {
				pstmt.setNull(8, java.sql.Types.BIGINT);
			}
			else {
				pstmt.setLong(8, _primaryKey.billtoId.longValue());
			}
			if (_primaryKey.rrcId == null) {
				pstmt.setNull(9, java.sql.Types.BIGINT);
			}
			else {
				pstmt.setLong(9, _primaryKey.rrcId.longValue());
			}
			if (_primaryKey.shiptoId == null) {
				pstmt.setNull(10, java.sql.Types.BIGINT);
			}
			else {
				pstmt.setLong(10, _primaryKey.shiptoId.longValue());
			}
			pstmt.setShort(1, b.optCounter);
			objectTemp = com.ibm.vap.converters.VapTrimStringConverter.singleton().dataFrom(b.rrcName);
			if (objectTemp == null) {
				pstmt.setNull(2, java.sql.Types.VARCHAR);
			}
			else {
				pstmt.setString(2, (java.lang.String)objectTemp);
			}
			objectTemp = com.ibm.vap.converters.VapTrimStringConverter.singleton().dataFrom(b.shiptoName);
			if (objectTemp == null) {
				pstmt.setNull(3, java.sql.Types.VARCHAR);
			}
			else {
				pstmt.setString(3, (java.lang.String)objectTemp);
			}
			objectTemp = com.ibm.vap.converters.VapTrimStringConverter.singleton().dataFrom(b.billtoName);
			if (objectTemp == null) {
				pstmt.setNull(4, java.sql.Types.VARCHAR);
			}
			else {
				pstmt.setString(4, (java.lang.String)objectTemp);
			}
			if (b.createdTime == null) {
				pstmt.setNull(5, java.sql.Types.TIMESTAMP);
			}
			else {
				pstmt.setTimestamp(5, b.createdTime);
			}
			if (b.lastUpdate == null) {
				pstmt.setNull(6, java.sql.Types.TIMESTAMP);
			}
			else {
				pstmt.setTimestamp(6, b.lastUpdate);
			}
			if (b.markForDelete == null) {
				pstmt.setNull(7, java.sql.Types.INTEGER);
			}
			else {
				pstmt.setInt(7, b.markForDelete.intValue());
			}
			int inputPos = 10;
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
		XaddressRelationBean b = (XaddressRelationBean) eb;
		com.ibm.commerce.extension.objects.XaddressRelationKey _primaryKey = new com.ibm.commerce.extension.objects.XaddressRelationKey();
		_primaryKey.billtoId = b.billtoId;
		_primaryKey.rrcId = b.rrcId;
		_primaryKey.shiptoId = b.shiptoId;
		PreparedStatement pstmt;
		Object cacheData[] = getDataFromCache();
		RdbRt  aTemplate = new RdbRt(_removeString, _predicateColumnNames, _predicateMaps);
		pstmt = getPreparedStatement(aTemplate.nativeQuery(cacheData));
		try {
			if (_primaryKey.billtoId == null) {
				pstmt.setNull(1, java.sql.Types.BIGINT);
			}
			else {
				pstmt.setLong(1, _primaryKey.billtoId.longValue());
			}
			if (_primaryKey.rrcId == null) {
				pstmt.setNull(2, java.sql.Types.BIGINT);
			}
			else {
				pstmt.setLong(2, _primaryKey.rrcId.longValue());
			}
			if (_primaryKey.shiptoId == null) {
				pstmt.setNull(3, java.sql.Types.BIGINT);
			}
			else {
				pstmt.setLong(3, _primaryKey.shiptoId.longValue());
			}
			int inputPos = 3;
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
		com.ibm.commerce.extension.objects.XaddressRelationKey key = new com.ibm.commerce.extension.objects.XaddressRelationKey();
		java.sql.ResultSet resultSet = (java.sql.ResultSet) data;

		if (resultSet != null) {
			Object objectTemp = null;
			long templong;

			templong = resultSet.getLong(2);
			key.billtoId = resultSet.wasNull() ? null : new Long(templong);
			templong = resultSet.getLong(4);
			key.rrcId = resultSet.wasNull() ? null : new Long(templong);
			templong = resultSet.getLong(6);
			key.shiptoId = resultSet.wasNull() ? null : new Long(templong);
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
	 * findByPrimaryKey
	 */
	public com.ibm.commerce.extension.objects.XaddressRelation findByPrimaryKey(com.ibm.commerce.extension.objects.XaddressRelationKey primaryKey) throws java.rmi.RemoteException, javax.ejb.FinderException {
		return (com.ibm.commerce.extension.objects.XaddressRelation) home.activateBean(primaryKey);
	}
	/**
	 * findbyBillToId
	 */
	public EJSFinder findbyBillToId(java.lang.Long billToId) throws javax.ejb.FinderException, java.rmi.RemoteException {
		ResultSet resultSet = null;
		PreparedStatement pstmt = null;
		try {
			preFind();
			pstmt = getPreparedStatement(" SELECT T1.OPTCOUNTER, T1.BILLTO_ID, T1.RRC_NAME, T1.RRC_ID, T1.SHIPTO_NAME, T1.SHIPTO_ID, T1.BILLTO_NAME, T1.TIMEPCREATE, T1.LASTUPDATE, T1.MARKFORDELETE FROM XADDRREL  T1 WHERE T1.BILLTO_ID = ?");
			pstmt.setLong(1, (long)billToId.longValue());
			resultSet = pstmt.executeQuery();
			return new EJSJDBCFinder(resultSet, this, pstmt);
		}
		catch (Exception ex) {
			throw new EJSPersistenceException("find failed:", ex);
		}
	}
	/**
	 * findbyRrcId
	 */
	public EJSFinder findbyRrcId(java.lang.Long rrcId) throws javax.ejb.FinderException, java.rmi.RemoteException {
		ResultSet resultSet = null;
		PreparedStatement pstmt = null;
		try {
			preFind();
			pstmt = getPreparedStatement(" SELECT T1.OPTCOUNTER, T1.BILLTO_ID, T1.RRC_NAME, T1.RRC_ID, T1.SHIPTO_NAME, T1.SHIPTO_ID, T1.BILLTO_NAME, T1.TIMEPCREATE, T1.LASTUPDATE, T1.MARKFORDELETE FROM XADDRREL  T1 WHERE T1.RRC_ID = ?");
			pstmt.setLong(1, (long)rrcId.longValue());
			resultSet = pstmt.executeQuery();
			return new EJSJDBCFinder(resultSet, this, pstmt);
		}
		catch (Exception ex) {
			throw new EJSPersistenceException("find failed:", ex);
		}
	}
}
