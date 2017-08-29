package com.ibm.commerce.domtar.commands;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.ibm.commerce.command.ControllerCommandImpl;
import com.ibm.commerce.datatype.TypedProperty;
import com.ibm.commerce.domtar.util.DomtarJDBCHelper;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.foundation.common.util.logging.LoggingHelper;
import com.ibm.commerce.user.objects.UserRegistryAccessBean;
/**
 * Controller command used for making user account inactive, if unused for more than 90 days(default).
 * Number of days can be controlled by passing the noOfDays as parameter from the scheduler. User inactivity
 * is identified by checking the current timestamp aganist the lastsession COLUMN in USERS table.
 */
public class DomtarDeactivateUserAccountCmdImpl extends ControllerCommandImpl
		implements DomtarDeactivateUserAccountCmd {
	private static final Logger LOGGER = LoggingHelper.getLogger(DomtarDeactivateUserAccountCmdImpl.class);
	
	//Default number of days in which the user account is active
	private static final int NO_OF_DAYS = 90;
	private int numberOfDays;


	/**
	 * @return the numberOfDays
	 */
	public int getNumberOfDays() {
		return numberOfDays;
	}

	/**
	 * @param numberOfDays the numberOfDays to set
	 */
	public void setNumberOfDays(int numberOfDays) {
		this.numberOfDays = numberOfDays;
	}

	@Override
	public void performExecute() throws ECException {
		
		/**
		 * Fetch the list of userIds which needs to be de-activated based on the 
		 * number of days passed as parameter.
		 */
		DomtarJDBCHelper domtarHelper = new DomtarJDBCHelper();
		ArrayList<Long> userIdList = domtarHelper.findUserAccountsToBeDeactivated(getNumberOfDays());
		
		int counter = 0;
		if(userIdList != null && userIdList.size()>0){
			for (Long userId : userIdList) {
				try {
					UserRegistryAccessBean userRegBean = new UserRegistryAccessBean();
					userRegBean.setInitKey_UserId(String.valueOf(userId));
					userRegBean.setStatus("0");
					userRegBean.commitCopyHelper();
					counter++;
				}catch (Exception e) {
					LOGGER.info("Error while disabling user account with userId "+userId+". Error is "+e.getMessage());
				}
			}
		}
		LOGGER.info("User deactive job completed. Total deactivated user count is "+counter+".");
		
	}

	@Override
	public void setRequestProperties(TypedProperty reqProperties)
			throws ECException {
		setNumberOfDays(reqProperties.getIntValue("noOfDays", NO_OF_DAYS));
	}
	
}
