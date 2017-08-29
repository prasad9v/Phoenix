/**
 * 
 */
package com.ibm.commerce.domtar.commands;

import java.sql.Timestamp;

import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.security.commands.LogonCmd;
import com.ibm.commerce.security.commands.LogonCmdImpl;
import com.ibm.commerce.user.objects.UserAccessBean;
import com.ibm.commerce.user.objsrc.UserCache;

/**
 * @author shanawas
 *
 */
public class DomtarLogonCmdImpl extends LogonCmdImpl implements LogonCmd {

	/* (non-Javadoc)
	 * @see com.ibm.commerce.security.commands.LogonCmdImpl#performExecute()
	 */
	@Override
	public void performExecute() throws ECException {
		
		String memberId = null;
		Timestamp lastSession = null;
		try {
			memberId = getUserRegistryObject().getUserId();
			UserAccessBean userBean = UserCache.findByPrimaryKey(memberId);

			//get the lastsession time value for the member
			lastSession = userBean.getLastSessionInEJBType();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Call super to perform the actual login
		super.performExecute();
		
		//Update the prevlastsession column in users table with the lastsession value.
		try {
			UserAccessBean ubean = new UserAccessBean();
			ubean.setInitKey_MemberId(memberId);
			ubean.setPreviousLastSession(lastSession);		
			ubean.commitCopyHelper();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
