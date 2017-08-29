package com.ibm.commerce.domtar.commands;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.naming.NamingException;

import com.ibm.commerce.beans.DataBeanManager;
import com.ibm.commerce.command.CommandContext;
import com.ibm.commerce.common.beans.StoreDataBean;
import com.ibm.commerce.contract.objects.TradingAgreementAccessBean;
import com.ibm.commerce.datatype.TypedProperty;
import com.ibm.commerce.exception.ECApplicationException;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.ras.ECMessage;
import com.ibm.commerce.ras.ECMessageHelper;
import com.ibm.commerce.user.beans.ContractListDataBean;
import com.ibm.commerce.user.objects.MemberGroupAccessBean;
import com.ibm.commerce.user.objects.MemberGroupMemberAccessBean;
import com.ibm.commerce.user.objects.OrganizationAccessBean;
import com.ibm.commerce.usermanagement.commands.PreUserRegistrationAddCmd;
import com.ibm.commerce.usermanagement.commands.PreUserRegistrationAddCmdImpl;

public class DomarPreUserRegistrationAddCmdImpl extends
		PreUserRegistrationAddCmdImpl implements PreUserRegistrationAddCmd {

	@Override
	public void performExecute() throws ECException {		
		
		try {
			TypedProperty requestPros = this.getRequestProperties();
			// Get the memberId of buyer organization
			OrganizationAccessBean organizationAcBean = new OrganizationAccessBean();
			OrganizationAccessBean buyerOrg = organizationAcBean.findByDN(requestPros.getString("parentMember", null));
			String lBuyerOrgId = buyerOrg.getOrganizationId();
			
			
			System.out.println("lBuyerOrgId  -- "+ lBuyerOrgId );
		
			//	TypedProperty reponsePros = this.getResponseProperties();

			String storeId = requestPros.getString("storeId", null);
			boolean isMemberContractParticipant = false;
			
			CommandContext commancontext = this.getCommandContext();
		 	commancontext.setActiveOrganizationId(new Long(lBuyerOrgId));
			
		 	//	ContractDataBean contract = new ContractDataBean();
			
			// Get the list of contracts for the store
			ContractListDataBean contracts = new ContractListDataBean();
			contracts.setCommandContext(commancontext);
			DataBeanManager.activate(contracts);
			HashMap contractHas = contracts.getContracts();
			
			Set contractSet  = contractHas.keySet();
			System.out.println("contractSet size : " + contractSet.size());
			for (Iterator iterator = contractSet.iterator(); iterator.hasNext();) {
				String object = (String) iterator.next();
				System.out.println("Contract Id: " + object);
				
				TradingAgreementAccessBean tdrAgree = new TradingAgreementAccessBean();
				tdrAgree.setInitKey_tradingId(object);
				tdrAgree.refreshCopyHelper();
				
				for (com.ibm.commerce.contract.objects.ParticipantAccessBean partAcBean : tdrAgree.getTradingLevelParticipants()) {
					String participantMemberId = partAcBean.getMemberId();					
					if (lBuyerOrgId.equals(participantMemberId)) {
						isMemberContractParticipant = true;
					}
				}
			} // for			
			if (!isMemberContractParticipant) {
				System.out.println("Organization name "+ requestPros.getString("parentMember", null));
				throw new ECApplicationException(ECMessage._ERR_CMD_INVALID_PARAM,
						this.getClass().getName(), "performExecute", ECMessageHelper.generateMsgParms("contract"));
			}
		
			StoreDataBean storeDB = new StoreDataBean();
			storeDB.setStoreId(requestPros.getString("storeId", null));
			DataBeanManager.activate(storeDB, this.commandContext);

			// Check of Buyer organization is already registered with store
			if (!storeDB.isBuyerOrganizationRegisteredToStore(requestPros.getString("parentMember", null))) {
				String storeOwnerOrgId = storeDB.getMemberId();
				MemberGroupAccessBean memberGrpAcBean = new MemberGroupAccessBean();
				memberGrpAcBean = memberGrpAcBean.findByOwnerName(new Long(storeOwnerOrgId), "RegisteredCustomers");
				if(memberGrpAcBean != null) {					
					// Get the 'RegisteredCustomers' member groupId for the store
					String mbrGrpId = memberGrpAcBean.getMbrGrpId();			
					
					// Add buyer organization to 'RegisteredCustomers' member group
					MemberGroupMemberAccessBean mngrpmnAcBean = new MemberGroupMemberAccessBean(new Long(mbrGrpId), new Long(lBuyerOrgId), "0");
					mngrpmnAcBean.commitCopyHelper();
					
					System.out.println("Buyer Org is added in RegisteredCustomers member group.");
	            }
			}
		} catch (ObjectNotFoundException e) {
			System.err.println(e);
			System.err.println("Error get/adding member group");
		} catch (RemoteException e) {
			System.err.println("Error get/adding member group");
		} catch (CreateException e) {
			System.err.println("Error get/adding member group");			 
		} catch (FinderException e) {
			System.err.println("Error get/adding member group");			
		} catch (NamingException e) {
			System.err.println("Error get/adding member group");
		}
		
		final String methodName = "performExecute";
		super.performExecute();
		
	}
}