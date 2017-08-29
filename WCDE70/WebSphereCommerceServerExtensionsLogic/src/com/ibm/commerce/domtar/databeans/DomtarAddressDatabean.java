package com.ibm.commerce.domtar.databeans;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.ejb.SessionContext;

import com.ibm.commerce.base.helpers.BaseJDBCHelper;
import com.ibm.commerce.beans.SmartDataBean;
import com.ibm.commerce.command.CommandContext;
import com.ibm.commerce.datatype.TypedProperty;
import com.ibm.commerce.exception.ECApplicationException;
import com.ibm.commerce.extension.objects.XaddressRelationAccessBean;
import com.ibm.commerce.foundation.common.util.logging.LoggingHelper;
import com.ibm.commerce.ras.ECMessage;

public class DomtarAddressDatabean extends BaseJDBCHelper implements SmartDataBean {

	private static final String CLASSNAME = DomtarAddressDatabean.class.getName();
	private static final java.util.logging.Logger LOGGER = LoggingHelper.getLogger(DomtarAddressDatabean.class);

	private String soldTo = null;
	private String shipTo = null;
	private String rrc = null;
	private String soldToName = null;
	private String shipToName = null;
	private String rrcName = null;
	private List addressList;
	private String searchType;
	@Override
	public void populate() throws Exception {


		DomtarAddress domtarAddress = null;
		try {
			
			XaddressRelationAccessBean xAddrBean1 = new XaddressRelationAccessBean();
			Enumeration xAddrEnum ;
			if("RRC".equals(searchType)){
				xAddrEnum =  xAddrBean1.findbyRrcId(Long.valueOf(getRrc()));
			}else{
				xAddrEnum =  xAddrBean1.findbyBillToId(Long.valueOf(getSoldTo()));
			}
			addressList = new ArrayList<DomtarAddress>();
			while (xAddrEnum.hasMoreElements()) {
				XaddressRelationAccessBean xAddrBean = (XaddressRelationAccessBean) xAddrEnum.nextElement();
				DomtarAddress domAdd = new DomtarAddress();
				domAdd.setSoldTo(xAddrBean.getBillToId().toString());
				domAdd.setSoldToName(xAddrBean.getBilltoName());
				domAdd.setShipTo(xAddrBean.getShipToId().toString());
				domAdd.setShipToName(xAddrBean.getShiptoName());
				domAdd.setRrc(xAddrBean.getRrcId().toString());
				domAdd.setRrcName(xAddrBean.getRrcName());
				addressList.add(domAdd);
			}
		} catch (Exception e) {
			//LOGGER.info("Error in getting address. "+e.getMessage());
		}
	}
	
	/**
     * Find SoldTo-ShipTo-RRC address mapping by soldTo addressId/
     * 
      * @param soldToAddressId
     * @return  <object>DomtarAddress</object> containing SoldTo-ShipTo-RRC address mapping
     * @throws ECApplicationException
     * @throws SQLException
     */
     public DomtarAddress findSoldToRRCShipToAddressByAddressId(String soldToAddressId)throws ECApplicationException, SQLException {
           final String METHODNAME = "findSoldToRRCShipToAddressByAddressId";

           LOGGER.entering(CLASSNAME, METHODNAME);

           PreparedStatement preparedStatement = null;
           ResultSet resultSet = null;

           StringBuilder queryBuilder = new StringBuilder();
           queryBuilder.append("SELECT BILLTO_ID, BILLTO_NAME, RRC_ID, RRC_NAME, SHIPTO_ID, SHIPTO_NAME FROM XADDRREL WHERE BILLTO_ID = ?");
           LOGGER.info(queryBuilder.toString());
           
           DomtarAddress domtarAddress = null; 
           try {
                 makeConnection();
                 preparedStatement = getPreparedStatement(queryBuilder.toString());
                 preparedStatement.setString(1, soldToAddressId);
                 resultSet = preparedStatement.executeQuery();
                 
                 if (resultSet.next()) {
                       domtarAddress = new DomtarAddress();
                       domtarAddress.setSoldTo(resultSet.getString("BILLTO_ID"));
                       domtarAddress.setSoldToName(resultSet.getString("BILLTO_NAME"));
                       domtarAddress.setShipToName(resultSet.getString("SHIPTO_ID"));
                       domtarAddress.setShipTo(resultSet.getString("SHIPTO_NAME"));
                       domtarAddress.setRrc(resultSet.getString("RRC_ID"));
                       domtarAddress.setRrcName(resultSet.getString("RRC_NAME"));
                       
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

	
	/**
	 * @return the soldTo
	 */
	public String getSoldTo() {
		return soldTo;
	}
	/**
	 * @param soldTo the soldTo to set
	 */
	public void setSoldTo(String soldTo) {
		this.soldTo = soldTo;
	}
	/**
	 * @return the shipTo
	 */
	public String getShipTo() {
		return shipTo;
	}
	/**
	 * @param shipTo the shipTo to set
	 */
	public void setShipTo(String shipTo) {
		this.shipTo = shipTo;
	}
	/**
	 * @return the rrc
	 */
	public String getRrc() {
		return rrc;
	}
	/**
	 * @param rrc the rrc to set
	 */
	public void setRrc(String rrc) {
		this.rrc = rrc;
	}
	public String getSoldToName() {
		return soldToName;
	}
	public void setSoldToName(String soldToName) {
		this.soldToName = soldToName;
	}
	public String getShipToName() {
		return shipToName;
	}
	public void setShipToName(String shipToName) {
		this.shipToName = shipToName;
	}
	public String getRrcName() {
		return rrcName;
	}
	public void setRrcName(String rrcName) {
		this.rrcName = rrcName;
	}

	@Override
	public CommandContext getCommandContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCommandContext(CommandContext commandcontext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TypedProperty getRequestProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRequestProperties(TypedProperty typedproperty)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SessionContext getSessionContext() {
		// TODO Auto-generated method stub
		return null;
	}

	public List getAddressList() {
		return addressList;
	}

	public void setAddressList(List addressList) {
		this.addressList = addressList;
	}

	/**
	 * @return the searchType
	 */
	public String getSearchType() {
		return searchType;
	}

	/**
	 * @param searchType the searchType to set
	 */
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
}
