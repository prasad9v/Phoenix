package com.ibm.commerce.domtar.commands;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.NamingException;

import com.ibm.commerce.datatype.TypedProperty;
import com.ibm.commerce.exception.ECApplicationException;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.member.syncbeans.UserSyncBean;
import com.ibm.commerce.ras.ECMessage;
import com.ibm.commerce.ras.ECMessageHelper;
import com.ibm.commerce.security.commands.ResetPasswordCmd;
import com.ibm.commerce.security.commands.ResetPasswordCmdImpl;

public class DomtarResetPasswordCmdImpl extends ResetPasswordCmdImpl
implements ResetPasswordCmd{
	
	public void performExecute() throws ECException{
		final String METHODNAME = "performExecute";
		String logonId = getLogonId();
		UserSyncBean isbUser;
		try {
			isbUser = UserSyncBean.findByLogonId(getLogonId());
			/*
			 *  When logging into admin tools, if the password is expired we will be asked to 
			 *  change the password. In this scenario the change password will not work. TO make it work
			 *  ignore challengeAnswer
			 */
			Integer storeId = getStoreId();
			if(storeId !=0){
				String strChallengeAnswer = isbUser.getProperty("challengeAnswer");
				setChallengeAnswer(strChallengeAnswer);
			}
			 
		} catch (RemoteException e) {
			throw new ECApplicationException(ECMessage._ERR_BAD_CMD_PARAMETER,
					this.getClass().getName(), METHODNAME, ""); 
		} catch (CreateException e) {
			throw new ECApplicationException(ECMessage._ERR_BAD_CMD_PARAMETER,
					this.getClass().getName(), METHODNAME, ""); 
		} catch (NamingException e) {
			throw new ECApplicationException(ECMessage._ERR_BAD_CMD_PARAMETER,
					this.getClass().getName(), METHODNAME, ""); 
		} catch (FinderException ex) {
			TypedProperty hshNVPs = new TypedProperty();
            hshNVPs.put("ErrorCode", "2010");
            ECApplicationException expTmp = new ECApplicationException(ECMessage._ERR_BAD_PARMS, getClass().getName(), "METHODNAME", ECMessageHelper.generateMsgParms("logonId"), "ResetPasswordGuestErrorView", hshNVPs);
            throw expTmp;
		}
		
		super.performExecute();
       
	}

}
