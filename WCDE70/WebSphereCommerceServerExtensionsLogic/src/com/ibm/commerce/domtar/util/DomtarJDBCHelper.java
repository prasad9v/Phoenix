/**
 * 
 */
package com.ibm.commerce.domtar.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.SessionContext;

import com.ibm.commerce.base.helpers.BaseJDBCHelper;
import com.ibm.commerce.domtar.databeans.DomtarAddress;
import com.ibm.commerce.domtar.server.DomtarServerHelper;
import com.ibm.commerce.exception.ECApplicationException;
import com.ibm.commerce.foundation.common.util.logging.LoggingHelper;
import com.ibm.commerce.ras.ECMessage;

/**
 * This is the JDBC Helper class for Domtar order.
 * 
 * @author Rakesh
 */
public class DomtarJDBCHelper extends BaseJDBCHelper {

	private static final String CLASSNAME = DomtarJDBCHelper.class.getName();

	private static final java.util.logging.Logger LOGGER = LoggingHelper.getLogger(DomtarJDBCHelper.class);
	
	/*
	 * (non-Javadoc)
	 * @see com.ibm.commerce.base.helpers.BaseJDBCHelper#getSessionContext()
	 */
	@Override
	public SessionContext getSessionContext() {
		return null;
	}

	/**
	 * Find order by ormOrderId.
	 * 
	 * @param ormOrderId
	 * @return orderId
	 * @throws ECApplicationException
	 * @throws SQLException 
	 */
	public String findOrderByOrmOrderId(String ormOrderId) throws ECApplicationException, SQLException {
		
		final String METHODNAME = "findOrderByOrmOrderId";

		LOGGER.entering(CLASSNAME, METHODNAME);

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT ORDERS_ID FROM ORDERS WHERE ORMORDER = ?");
		//LOGGER.info(queryBuilder.toString());
		
		String orderId = null;
		try {
			makeConnection();
			preparedStatement = getPreparedStatement(queryBuilder.toString());
			preparedStatement.setString(1, ormOrderId);
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				orderId = resultSet.getString("ORDERS_ID");
			}
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			throw new ECApplicationException(ECMessage._ERR_ORDER_ACCESS,
					CLASSNAME, METHODNAME, new Object[] { "ormOrderId" });
		} finally {
			closeDBConnectionAndResultSet(resultSet);
		}
		LOGGER.exiting(CLASSNAME, METHODNAME);

		return orderId;
	}
	
	/**
	 * Find orderItemId by WCS orderId and Domtar order item code.
	 * 
	 * @param itemCode
	 * @return OrderItemId
	 * @throws ECApplicationException
	 * @throws SQLException
	 */
	public String findOrderItemIdByOrderIdAndDomtarItemCode(String orderId, String itemCode) throws ECApplicationException, SQLException {
		
		final String METHODNAME = "findOrderItemIdByDomtarItemCode";

		LOGGER.entering(CLASSNAME, METHODNAME);

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT ORDERITEMS_ID FROM ORDERITEMS WHERE ORDERS_ID =? AND FIELD2 = ?");
		//LOGGER.info(queryBuilder.toString());
		
		String orderItemId = null;
		try {
			makeConnection();
			preparedStatement = getPreparedStatement(queryBuilder.toString());
			preparedStatement.setString(1, orderId);
			preparedStatement.setString(2, itemCode);
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				orderItemId = resultSet.getString("ORDERITEMS_ID");
			}
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			throw new ECApplicationException(ECMessage._ERR_ORDER_ACCESS,
					CLASSNAME, METHODNAME, new Object[] { "ORDERITEMS_ID" });
		} finally {
			closeDBConnectionAndResultSet(resultSet);
		}
		
		LOGGER.exiting(CLASSNAME, METHODNAME);

		return orderItemId;
	}
	
	/**
	 * Find addressId by nick name.
	 * 
	 * @param nickName
	 * @return	AddressId
	 * @throws SQLException
	 * @throws ECApplicationException
	 */
	public String findAddressByNickName(String nickName) throws SQLException, ECApplicationException {
		
		final String METHODNAME = "findOrderItemIdByDomtarItemCode";

		LOGGER.entering(CLASSNAME, METHODNAME);

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT ADDRESS_ID FROM ADDRESS WHERE STATUS = 'P' AND NICKNAME = ?");
		//LOGGER.info(queryBuilder.toString());
		
		String addressId = null;
		try {
			makeConnection();
			preparedStatement = getPreparedStatement(queryBuilder.toString());
			preparedStatement.setString(1, nickName);
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				addressId = resultSet.getString("ADDRESS_ID");
			}
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			throw new ECApplicationException(ECMessage._ERR_ORDER_ACCESS,
					CLASSNAME, METHODNAME, new Object[] { "nickName" });
		} finally {
			closeDBConnectionAndResultSet(resultSet);
		}
		
		LOGGER.exiting(CLASSNAME, METHODNAME);

		return addressId;
	}
	
	/**
	 * Find SoldTo-ShipTo-RRC address mapping by soldTo addressId/
	 * 
	 * @param soldToAddressId
	 * @return	<object>DomtarAddress</object> containing SoldTo-ShipTo-RRC address mapping
	 * @throws ECApplicationException
	 * @throws SQLException
	 */
	public DomtarAddress findSoldToRRCShipToAddressByAddressId(String soldToAddressId)throws ECApplicationException, SQLException {
		final String METHODNAME = "findSoldToRRCShipToAddressByAddressId";

		LOGGER.entering(CLASSNAME, METHODNAME);

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT BILLTO_ID, RRC_ID, SHIPTO_ID FROM XADDRREL WHERE BILLTO_ID = ?");
		//LOGGER.info(queryBuilder.toString());
		
		DomtarAddress domtarAddress = null; 
		try {
			makeConnection();
			preparedStatement = getPreparedStatement(queryBuilder.toString());
			preparedStatement.setString(1, soldToAddressId);
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				domtarAddress = new DomtarAddress(resultSet.getString("BILLTO_ID"), 
						resultSet.getString("SHIPTO_ID"),resultSet.getString("RRC_ID"));
			}
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			throw new ECApplicationException(ECMessage._ERR_ADDRENTRY_NOT_FOUND,
					CLASSNAME, METHODNAME, new Object[] { "soldToAddressId" });
		} finally {
			closeDBConnectionAndResultSet(resultSet);
		}
		
		LOGGER.exiting(CLASSNAME, METHODNAME);
		
		return domtarAddress;
	}
	
	/**
	 * Find SoldTo-ShipTo-RRC address mapping by Organization Id/
	 * 
	 * @param organizationId
	 * @return	<object>DomtarAddress</object> containing SoldTo-ShipTo-RRC address mapping
	 * @throws ECApplicationException
	 * @throws SQLException
	 */
	public List findSoldToRRCShipToAddressByOrganizationId(String organizationId) throws ECApplicationException, SQLException {
		final String METHODNAME = "findSoldToRRCShipToAddressByOrganizationId";

		LOGGER.entering(CLASSNAME, METHODNAME);

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<DomtarAddress> addressList = null;

		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT X.BILLTO_ID, X.BILLTO_NAME, X.SHIPTO_ID, X.SHIPTO_NAME, X.RRC_ID, X.RRC_NAME FROM MBRREL MBR,ORGENTITY O,XADDRREL X WHERE ");
		queryBuilder.append("MBR.DESCENDANT_ID=O.ORGENTITY_ID ");
		queryBuilder.append("AND CAST(CAST(X.BILLTO_ID AS CHAR(128)) AS VARCHAR(128)) = O.ORGENTITYNAME ");
		queryBuilder.append("AND MBR.ANCESTOR_ID = ? ");
		queryBuilder.append("UNION ");
		queryBuilder.append("SELECT X.BILLTO_ID, X.BILLTO_NAME, X.SHIPTO_ID, X.SHIPTO_NAME, X.RRC_ID, X.RRC_NAME FROM ORGENTITY O,XADDRREL X WHERE ");
		queryBuilder.append("CAST(CAST(X.BILLTO_ID AS CHAR(128)) AS VARCHAR(128)) = O.ORGENTITYNAME ");
		queryBuilder.append("AND O.ORGENTITY_ID = ?");
		LOGGER.info("organizationId : "+organizationId+" : "+queryBuilder.toString());
		
		DomtarAddress domtarAddress = null; 
		try {
			makeConnection();
			preparedStatement = getPreparedStatement(queryBuilder.toString());
			preparedStatement.setString(1, organizationId);
			preparedStatement.setString(2, organizationId);
			resultSet = preparedStatement.executeQuery();
			addressList = new ArrayList<DomtarAddress>();
			while(resultSet.next()) {
				domtarAddress = new DomtarAddress();
				domtarAddress.setSoldTo(resultSet.getString("BILLTO_ID"));
		        domtarAddress.setSoldToName(resultSet.getString("BILLTO_NAME"));
		        domtarAddress.setShipTo(resultSet.getString("SHIPTO_ID"));
		        domtarAddress.setShipToName(resultSet.getString("SHIPTO_NAME"));	               
		        domtarAddress.setRrc(resultSet.getString("RRC_ID"));
		        domtarAddress.setRrcName(resultSet.getString("RRC_NAME"));
		        addressList.add(domtarAddress);
			}
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			throw new ECApplicationException(ECMessage._ERR_ADDRENTRY_NOT_FOUND,
					CLASSNAME, METHODNAME, new Object[] { "organizationId" });
		} finally {
			closeDBConnectionAndResultSet(resultSet);
		}
		
		LOGGER.exiting(CLASSNAME, METHODNAME);
		
		return addressList;
	}
	
	/**
     * Find SoldTo-ShipTo-RRC address mapping by rrc addressId/
     * 
      * @param rrcAddressId
     * @return  <object>List</object> containing SoldTo-ShipTo-RRC address mapping
     * @throws ECApplicationException
     * @throws SQLException
     */
     public List findAddressRelationByRrcAddressId(String rrcAddressId, ArrayList<String> soldToList)throws ECApplicationException, SQLException {
           final String METHODNAME = "findAddressRelationByRrcAddressId";

           LOGGER.entering(CLASSNAME, METHODNAME);

           PreparedStatement preparedStatement = null;
           ResultSet resultSet = null;
           List<DomtarAddress> addressList = null;

           StringBuilder queryBuilder = new StringBuilder();
           queryBuilder.append("SELECT BILLTO_ID, BILLTO_NAME, RRC_ID, RRC_NAME, SHIPTO_ID, SHIPTO_NAME FROM XADDRREL WHERE RRC_ID = ?");
           
           DomtarAddress domtarAddress = null; 
           try {
        	   makeConnection();
               preparedStatement = getPreparedStatement(queryBuilder.toString());
               preparedStatement.setString(1, rrcAddressId);
               resultSet = preparedStatement.executeQuery();
               addressList = new ArrayList<DomtarAddress>();
               while(resultSet.next()) {
            	   String soldTo = resultSet.getString("BILLTO_ID");
            	   if(soldToList.contains(soldTo)){
		               domtarAddress = new DomtarAddress();
		               domtarAddress.setSoldTo(soldTo);
		               domtarAddress.setSoldToName(resultSet.getString("BILLTO_NAME"));
		               domtarAddress.setShipTo(resultSet.getString("SHIPTO_ID"));
		               domtarAddress.setShipToName(resultSet.getString("SHIPTO_NAME"));	               
		               domtarAddress.setRrc(resultSet.getString("RRC_ID"));
		               domtarAddress.setRrcName(resultSet.getString("RRC_NAME"));
		               addressList.add(domtarAddress);
            	   }
               }
           } catch (Exception e) {
        	   //LOGGER.info("Error in getting address "+rrcAddressId+". "+e.getMessage());
           } finally {
                 closeDBConnectionAndResultSet(resultSet);
           }
           
           LOGGER.exiting(CLASSNAME, METHODNAME);
           
           return addressList;
     }
     
     /**
      * Find SoldTo-ShipTo-RRC address mapping by shipTo addressId/
      * 
       * @param rrcAddressId
      * @return String containing ShipTo_Name
      * @throws ECApplicationException
      * @throws SQLException
      */
      public String findShipToNameByShipToAddressId(String shipToAddressId) throws ECApplicationException, SQLException {
            final String METHODNAME = "findAddressRelationByShipToAddressId";

            LOGGER.entering(CLASSNAME, METHODNAME);

            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;
            String shipToName="";

            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT SHIPTO_NAME FROM XADDRREL WHERE SHIPTO_ID = ?");
            
            DomtarAddress domtarAddress = null; 
            try {
            	makeConnection();
                preparedStatement = getPreparedStatement(queryBuilder.toString());
                preparedStatement.setString(1, shipToAddressId);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                	shipToName = resultSet.getString("SHIPTO_NAME");
                }
            } catch (Exception e) {
         	   //LOGGER.info("Error in getting address "+rrcAddressId+". "+e.getMessage());
            } finally {
                  closeDBConnectionAndResultSet(resultSet);
            }
            
            LOGGER.exiting(CLASSNAME, METHODNAME);
            
            return shipToName;
      }
     /*
      * Find RRC Name of a given RRC ID
      * 
      */
     public String findRRCNameByRRCId(String rrcId) throws ECApplicationException, SQLException {
    	 final String METHODNAME = "findRRCNameByRRCId";

         LOGGER.entering(CLASSNAME, METHODNAME);

         PreparedStatement preparedStatement = null;
         ResultSet resultSet = null;
         String rrcName="";

         StringBuilder queryBuilder = new StringBuilder();
         queryBuilder.append("SELECT DISTINCT RRC_NAME FROM XADDRREL WHERE RRC_ID = ?");
         
         try {
         	makeConnection();
             preparedStatement = getPreparedStatement(queryBuilder.toString());
             preparedStatement.setString(1, rrcId);
             resultSet = preparedStatement.executeQuery();
             if (resultSet.next()) {
            	 rrcName = resultSet.getString("RRC_NAME");
             }
         } catch (Exception e) {
        	 throw new ECApplicationException(ECMessage._ERR_ADDRENTRY_NOT_FOUND,
 					CLASSNAME, METHODNAME, new Object[] { "rrcId" });
         } finally {
               closeDBConnectionAndResultSet(resultSet);
         }
         
         LOGGER.exiting(CLASSNAME, METHODNAME);
         
		 return rrcName;
    	 
     } 
	/**
	 * Close ResultSet, statement and connection if open.
	 * 
	 * @param resultSet 	ResultSet 
	 */
	private void closeDBConnectionAndResultSet(ResultSet resultSet) {
		
		// Close ResultSet if open.
		if (null != resultSet) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				LOGGER.info("Error closing database connection and statement.");
			}
		} // if

		// Close connection and statement if open.
		try {
			closeConnection();
		} catch (SQLException e) {
			LOGGER.info("Error closing database connection and statement.");
		} // try
	}

	public ArrayList<Long> findUserAccountsToBeDeactivated(int numberOfDays) {
		final String METHODNAME = "findUserAccountsToBeDeactivated";
		LOGGER.entering(CLASSNAME, METHODNAME);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String shipToName="";

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT U.USERS_ID FROM USERS U,USERREG UR WHERE U.USERS_ID=UR.USERS_ID AND U.REGISTERTYPE='R' ");
        queryBuilder.append("AND UR.STATUS=1 ");
        if(DomtarServerHelper.isDevelopmentEnvironment()){
        	queryBuilder.append("AND (DAY(CURRENT TIMESTAMP) - DAY(LASTSESSION)) >= ?");
        }else{
        	queryBuilder.append("AND (DAYS(CURRENT TIMESTAMP) - DAYS(U.LASTSESSION)) >= ?");
        }
        
        ArrayList<Long> userIdList = new ArrayList<Long>(); 
        try {
        	makeConnection();
            preparedStatement = getPreparedStatement(queryBuilder.toString());
            preparedStatement.setInt(1, numberOfDays);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
            	userIdList.add(resultSet.getLong("USERS_ID"));
            }
        } catch (Exception e) {
     	   LOGGER.info("Error in getting userIdList. "+e.getMessage());
        } finally {
              closeDBConnectionAndResultSet(resultSet);
        }
        
        LOGGER.exiting(CLASSNAME, METHODNAME);
        
        return userIdList;
	}
	
	/**
	 * Method for getting data from XPRODSUMMARY for a given catentry_id.
	 * Description : This method will return a list with all information related to that catentry_id. Sixth element  
	 * in the list contains a map with all attributes and its values for the given catentry_id.
	 * @param List
	 * @return catentryId
	 * @throws ECApplicationException
	 * @throws SQLException 
	 */
	public List findAttributeByCatentry(String catentry_Id) throws ECApplicationException, SQLException {
		
		final String METHODNAME = "findAttributeByCatentry";  

		LOGGER.entering(CLASSNAME, METHODNAME);

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT CATENTRY_ID,PARTNUMBER,PROD_CATENTRY_ID,PROD_PARTNUMBER,ATTRIBUTE_NAME,ATTRTYPE_ID, " +
				"STRINGVALUE, INTEGERVALUE,FLOATVALUE,SHORTDESCRIPTION,THUMBNAIL FROM XPRODSUMMARY WHERE CATENTRY_ID = ?");
		//LOGGER.info(queryBuilder.toString());
		
		String attributeName = null;				
		String attributeType = null;
		String attributeValue = null;		
		String stringValue  = null;
		String integerValue = null;
		String floatValue= null;
		String catentryId = "";
		String partNumber = "";
		String prodCatentryId = "";
		String prodPartNumber = "";
		String shortDesc =  "";
		String thumbNail =  "";

		Map attributes = new HashMap<String, String>();
		
		List attributeValues = new ArrayList();
		try {
			makeConnection();
			preparedStatement = getPreparedStatement(queryBuilder.toString());
			preparedStatement.setInt(1, Integer.parseInt(catentry_Id));
			resultSet = preparedStatement.executeQuery();
					
			while (resultSet.next()) {				
				if (resultSet.getString("ATTRIBUTE_NAME") == null || resultSet.getString("ATTRIBUTE_NAME").equals("NULL"))
					continue;
				attributeName = resultSet.getString("ATTRIBUTE_NAME").trim();
				attributeType = resultSet.getString("ATTRTYPE_ID").trim();
				stringValue = resultSet.getString("STRINGVALUE");
				integerValue = resultSet.getString("INTEGERVALUE");
				floatValue = resultSet.getString("FLOATVALUE");
				catentryId = resultSet.getString("CATENTRY_ID").trim();
				partNumber = resultSet.getString("PARTNUMBER").trim();
				prodCatentryId = resultSet.getString("PROD_CATENTRY_ID").trim();
				prodPartNumber = resultSet.getString("PROD_PARTNUMBER").trim();
				shortDesc =  resultSet.getString("SHORTDESCRIPTION");
				thumbNail =  resultSet.getString("THUMBNAIL");
				
				if (stringValue != null && !"".equals(stringValue))
					attributeValue = stringValue.trim();
				else if (integerValue != null && !"".equals(integerValue))
					attributeValue = integerValue.trim();
				else if (floatValue != null && !"".equals(floatValue))
					attributeValue = floatValue.trim();
				
				if (shortDesc != null && !"".equals(shortDesc))
					shortDesc = shortDesc.trim();
				else
					shortDesc = "";
				
				if (thumbNail != null && !"".equals(thumbNail))
					thumbNail = thumbNail.trim();
				else
					thumbNail = "";
										
				attributes.put(attributeName, attributeValue); //Put the attributes in the map with attributeName as key 
			}
			attributeValues.add(catentryId);
			attributeValues.add(partNumber);
			attributeValues.add(prodCatentryId);
			attributeValues.add(prodPartNumber);
			attributeValues.add(shortDesc);
			attributeValues.add(shortDesc);
			attributeValues.add(attributes);
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			throw new ECApplicationException(ECMessage._ERR_ACCESS_DB,
					CLASSNAME, METHODNAME, new Object[] { "catentryid" });
		} finally {
			closeDBConnectionAndResultSet(resultSet);
		}
		LOGGER.exiting(CLASSNAME, METHODNAME);

		return attributeValues;
	}
}
