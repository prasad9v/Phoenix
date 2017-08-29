package com.ibm.commerce.domtar.commands;

import com.ibm.commerce.datatype.TypedProperty;
import com.ibm.commerce.exception.ECApplicationException;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.ras.ECMessage;
import com.ibm.commerce.usermanagement.commands.UserRegistrationAddCmd;
import com.ibm.commerce.usermanagement.commands.UserRegistrationAddUBFCmdImpl;

public class DomtarUserRegistrationAddCmdImpl extends
UserRegistrationAddUBFCmdImpl implements UserRegistrationAddCmd {
 

	public void performExecute() throws ECException {

		super.performExecute();
	}

	public void setRequestProperties(TypedProperty reqProperties)
			throws ECApplicationException {
		final String METHODNAME = "setRequestProperties";
		super.setRequestProperties(reqProperties);

		String logonId = null;
		try {
			logonId = generateLogonId(reqProperties);
		} catch (ECException e1) {
			throw new ECApplicationException(ECMessage._ERR_BAD_CMD_PARAMETER,
					this.getClass().getName(), METHODNAME, "");
		}
		reqProperties.put("logonId", logonId);

		super.setRequestProperties(reqProperties);
	}
	
	public String generateLogonId(TypedProperty reqProperties) throws ECException
	 { 	
	 		   String logonId = null;
		 	   String id1 = null;
		 	   String id2 = null;
		 	   String id3 = null;
		 	   String lName;
		 	   String fName;	
		 	   int numb = 0;
		 	   
		 	   do
		 	   {
		 	   
		 	   fName = reqProperties.getString(REQUEST_PARAM_FIRSTNAME).substring(0, 1).toUpperCase();
		       lName = reqProperties.getString(REQUEST_PARAM_LASTNAME).toUpperCase();
		       if(lName.length() >= 12 )
			   {
		    	   lName = lName.substring(0, 11);  	    	 
			   }
		       id1 = lName.concat(fName);
		       if(numb == 0)
		       {
		    	   id3=id1;
			   }
		       else
		       {
		       id2 = concatenate(id1,numb,lName,fName);  		     
		       id3 = id2.concat(Integer.toString(numb));
		       }		       
		       numb++;
		       }
			   while(isLogonIdUnique(id3) == false);		       
			return id3;			 
		 }

	public String concatenate(String logonId, int numb, String lastName,String firstName)
	{		
		String logonIdFinal = null;	
		
		if(lastName.length()>=11 && numb<=9 )
		{
			lastName=lastName.substring(0, 10);
			logonIdFinal=lastName.concat(firstName);
		}
		else if(lastName.length()>=10 && numb > 9 )
		{
			lastName=lastName.substring(0, 9);
			logonIdFinal=lastName.concat(firstName);
		}
		else
		{			
			logonIdFinal = logonId;
		}	
		return logonIdFinal;		
	}
}
