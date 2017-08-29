package com.ibm.commerce.domtar.commands;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;

import com.ibm.commerce.beans.DataBeanManager;
import com.ibm.commerce.command.ControllerCommandImpl;
import com.ibm.commerce.context.entitlement.EntitlementContext;
import com.ibm.commerce.contract.beans.ContractDataBean;
import com.ibm.commerce.contract.objects.TradingAgreementAccessBean;
import com.ibm.commerce.datatype.TypedProperty;
import com.ibm.commerce.domtar.helpers.DomtarHelper;
import com.ibm.commerce.exception.ECApplicationException;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.ras.ECMessage;
import com.ibm.commerce.server.ECConstants;
import com.ibm.commerce.server.WcsApp;
import com.ibm.commerce.user.beans.ContractListDataBean;
import com.ibm.commerce.user.beans.MemberDataBean;
import com.ibm.commerce.user.beans.OrganizationDataBean;
import com.ibm.commerce.user.beans.RoleDataBean;
import com.ibm.commerce.user.beans.UserDataBean;
import com.ibm.commerce.user.objects.AddressAccessBean;
import com.ibm.commerce.user.objects.OrganizationAccessBean;

public class DomtarLandingPageControllerCmdImpl extends ControllerCommandImpl
		implements DomtarLandingPageControllerCmd {
	
	
	private static final String M_LANDING_PAGE = "DomtarLandingPageDisplay";
	private static final String M_IS_HOMEPAGE = "isHomePage";
	private static final String M_IS_PRODUCTCATALOGPAGE = "isProductCatalogPage";
	private static final String M_IS_STOCKCHECKPAGE = "isStockCheckPage";
	private static final String M_IS_STOCKCHECKPAGE_HOLD = "isStockCheckPageHold";
	private static final String M_IS_CUSTOMERTONNAGE = "isCustomerTonnagePage";
	private static final String M_IS_MANAGEDOCUMENTPAGE = "isManageDocumentPage";
	private static final String M_IS_ORDERSTATUS = "isOrderStatusPage";
	private static final String M_IS_LOGEDINUSER = "isLoggedInUser";
	
	private static final String ROLE_STOCKCHECKPAGE = "DOMTAR_STOCKCHECK";
	private static final String ROLE_STOCKCHECKPAGE_HOLD = "DOMTAR_STOCKCHECK_HOLD";
	private static final String ROLE_CUSTOMERTONNAGE = "DOMTAR_CUSTOMERTONNAGE";
	private static final String ROLE_MANAGEDOCUMENTPAGE = "DOMTAR_MANAGEDOCUMENT";
	private static final String ROLE_ORDERSTATUS = "DOMTAR_ORDERSTATUS";
	private long vLogedInTime = 0 ;

	public void performExecute() throws ECException {
		super.performExecute();
		boolean isLogedInUser = findIfLoginUser();
		TypedProperty rspProp = new TypedProperty();
		rspProp.put(M_IS_HOMEPAGE, true);
		rspProp.put(M_IS_PRODUCTCATALOGPAGE, true);
		if(isLogedInUser){
			String userId = getCommandContext().getUserId().toString();
			/*
			 * logonId is used to pass to google analytics for tracking purpose.
			 */
			String logonId = DomtarHelper.getLogonIdByUserId(userId);
			rspProp.put("UserLogonId", logonId);
			String vLoginMailId = getLoggedInMailId();
			rspProp.put("LoggedInMailId", vLoginMailId);
			String vLogedInUser = getLoggedInUser();
			rspProp.put("LoggedInUser", vLogedInUser);
			String vGetLastLoggedInBefore = getLastLoggedInBefore();
			rspProp.put("LastLoginDetails", vGetLastLoggedInBefore);
			/*
			 * Get the DomtarRep value from Orgentity.Field1 Column.
			 */
			String vDomatrRep = getDomtarRep();
			String domtarRepEmail ="";
			if (!"".equals(vDomatrRep)){
				String domRepEmail[] = vDomatrRep.split("\\|");
				if(domRepEmail.length>=2){
					domtarRepEmail = domRepEmail[1];
				}
			}
			
			rspProp.put("DomtarRepEmail", domtarRepEmail);
			rspProp.put("DomtarRep", vDomatrRep);
			rspProp.put("dTimeout", (getLogoutInterval() - 60000));
			rspProp.put("LoginTime", vLogedInTime);

			if(checkForRole(ROLE_STOCKCHECKPAGE)){
				rspProp.put(M_IS_STOCKCHECKPAGE, true);
			}else{
				rspProp.put(M_IS_STOCKCHECKPAGE, false);
			}
			
			if(checkForRole(ROLE_STOCKCHECKPAGE_HOLD)){
				rspProp.put(M_IS_STOCKCHECKPAGE_HOLD, true);
			}else{
				rspProp.put(M_IS_STOCKCHECKPAGE_HOLD, false);
			}
			
			if(checkForRole(ROLE_CUSTOMERTONNAGE)){
				rspProp.put(M_IS_CUSTOMERTONNAGE, true);
			}else{
				rspProp.put(M_IS_CUSTOMERTONNAGE, false);
			}
			
			
			if(checkForRole(ROLE_MANAGEDOCUMENTPAGE)){
				rspProp.put(M_IS_MANAGEDOCUMENTPAGE, true);
			}else{
				rspProp.put(M_IS_MANAGEDOCUMENTPAGE, false);
			}
			
			if(checkForRole(ROLE_ORDERSTATUS)){
				rspProp.put(M_IS_ORDERSTATUS, true);
			}else{
				rspProp.put(M_IS_ORDERSTATUS, false);
			}
			
			String currentContracts  = getCommandContext().getCurrentTradingAgreementIdsAsString();
			if(currentContracts.contains(";")){
				currentContracts = getContractForTheUser();				
			}
			rspProp.put("contractId", currentContracts);
		}
		String timeStamp = 
	            new SimpleDateFormat("MMM dd, yyyy").format(Calendar.getInstance().getTime());
		
		rspProp.put("CurrentDate", timeStamp);
		
		rspProp.put(M_IS_LOGEDINUSER, isLogedInUser);
		rspProp.put(ECConstants.EC_VIEWTASKNAME, M_LANDING_PAGE);
		setResponseProperties(rspProp);
	}
	
	private String getDomtarRep() {
		 String strMethodName = "getDomtarRep";
		 String vDomtarRep= "";
		 try {
			String activeOrganisationId = getCommandContext().getUser().getParentMemberId();
			OrganizationAccessBean organizationAccessBean = new OrganizationAccessBean();
			organizationAccessBean.setInitKey_MemberId(activeOrganisationId);

			vDomtarRep = organizationAccessBean.getOrgEntityField1();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(vDomtarRep == null){
			return "";
		}
		return vDomtarRep;
	}

	public String getContractForTheUser() throws ECException{
		ContractListDataBean contractBean =  new ContractListDataBean();
		contractBean.setCommandContext(getCommandContext());   
		ContractDataBean oContractBean1 = null;
		ContractDataBean oContractBean2 = null;
        try {
              DataBeanManager.activate(contractBean, getCommandContext());              
              Map contractMap = contractBean.getContracts();
              
              String parentMemberId = null;              
              Iterator contractEntry = contractMap.entrySet().iterator();
              EntitlementContext entitlementContext = (EntitlementContext)getCommandContext().getContext("com.ibm.commerce.context.entitlement.EntitlementContext");              	
              while(contractEntry.hasNext()){
            	  Map.Entry contractPairs = (Map.Entry)contractEntry.next();
              
			        if(entitlementContext != null) {
			            parentMemberId = entitlementContext.getActiveOrganizationId().toString();
			        } else {
			        	parentMemberId = getCommandContext().getUser().getParentMemberId();
			        }
             
			        boolean isParticipant = false;
			 
					com.ibm.commerce.contract.objects.TradingAgreementAccessBean tdrAgree = new com.ibm.commerce.contract.objects.TradingAgreementAccessBean();
					tdrAgree.setInitKey_tradingId(contractPairs.getKey().toString());					
					for (com.ibm.commerce.contract.objects.ParticipantAccessBean partAcBean : tdrAgree.getTradingLevelParticipants()) {
						String participantMemberId = partAcBean.getMemberId();																
						if (parentMemberId.equals(participantMemberId)) {
							isParticipant = true;
							break;
						}
					}
					if(isParticipant){	
						oContractBean1 = new ContractDataBean();
						oContractBean1.setDataBeanKeyReferenceNumber((String)contractPairs.getKey());
						oContractBean1.setCommandContext(getCommandContext());
						oContractBean1.populate();
						if(oContractBean2 == null){
							oContractBean2 = oContractBean1;
						}else if(oContractBean2.getTimeCreatedInEJBType().after(oContractBean1.getTimeCreatedInEJBType())){
							oContractBean1 = oContractBean2;
						}
					}
              }
              
              if(entitlementContext != null && oContractBean1 != null) {
				entitlementContext.setCurrentTradingAgreementIds((String)oContractBean1.getName());
				
				TradingAgreementAccessBean[] oBean = {(new TradingAgreementAccessBean()).findValidTAById(new Long((String)oContractBean1.getDataBeanKeyReferenceNumber()))};
				getCommandContext().setCurrentTradingAgreements(oBean);
				return oContractBean1.getDataBeanKeyReferenceNumber();
              }
        }catch (Exception e) {
        	e.printStackTrace();
        	throw new ECApplicationException(ECMessage._ERR_BAD_CMD_PARAMETER,
					this.getClass().getName(), "performExecute", "");
		}
        return "0";
	}
	
	 private String getLoggedInMailId() throws ECApplicationException {
		 String strMethodName = "getLoggedInMailId";
		 String emailId= "";
		 try {			 
			 String memberId = getCommandContext().getUser().getMemberId();	
			Enumeration enmAddressList = (new AddressAccessBean())
				.findByMemberId(Long.valueOf(memberId));
			while (enmAddressList.hasMoreElements()) {
				AddressAccessBean addressbean = (AddressAccessBean) enmAddressList
				.nextElement();
				if(addressbean.getEmail1() !=null && !"".equals(addressbean.getEmail1().trim())){
					emailId = addressbean.getEmail1();
				}								
			}
			 
		 }catch (Exception e) {
				// e.printStackTrace();
				throw new ECApplicationException(ECMessage._ERR_BAD_CMD_PARAMETER,
						this.getClass().getName(), strMethodName, "");
		}
		return emailId;
	}
//	private String getDomtarRep() throws ECApplicationException {
//		 String strMethodName = "getDomtarRep";
//		 String vDomtarRep= "";
//		 try {
//			
//			 String activeOrganisationId = getCommandContext().getUser()
//				.getParentMemberId();
//			 vDomtarRep = fethParentRepDetails(activeOrganisationId);
//			 do {
//				 MemberDataBean vMemeberDataBean = new MemberDataBean();
//				vMemeberDataBean.setDataBeanKeyMemberId(activeOrganisationId);
//				DataBeanManager.activate(vMemeberDataBean);
//				activeOrganisationId = vMemeberDataBean.getParentMemberId();
//				if(activeOrganisationId != null && !"".equals(activeOrganisationId)){
//					vDomtarRep = fethParentRepDetails(activeOrganisationId);
//				}else{
//					vDomtarRep = " ";
//				}
//			 }while ("".equals(vDomtarRep));
//			 
//			 
//			 
//		 }catch (Exception e) {
//				// e.printStackTrace();
//				throw new ECApplicationException(ECMessage._ERR_BAD_CMD_PARAMETER,
//						this.getClass().getName(), strMethodName, "");
//			}
//		return vDomtarRep.trim();
//	}
	private String fethParentRepDetails(String activeOrganisationId) throws ECApplicationException { 
		String strMethodName = "fethParentRepDetails";
		 boolean isCsr = false;
		 String vCsrDetails = "";
		 try {
		MemberDataBean vMemeberDataBean = new MemberDataBean();
		vMemeberDataBean.setDataBeanKeyMemberId(activeOrganisationId);
		DataBeanManager.activate(vMemeberDataBean);
		if (vMemeberDataBean.getDescendants() != null
				&& vMemeberDataBean.getDescendants().length > 0) {
			for (Long cmBean : vMemeberDataBean.getDescendants()) {
				MemberDataBean vChildMemeberDataBean = new MemberDataBean();
				vChildMemeberDataBean.setDataBeanKeyMemberId(cmBean
						.toString());
				DataBeanManager.activate(vChildMemeberDataBean);
				 Integer[] roles = vChildMemeberDataBean.getRoles();
				 for(int i = 0 ; i < roles.length ; i++){
					 RoleDataBean rdb = new RoleDataBean();
					 rdb.setInitKey_RoleId(roles[i].toString());
					 DataBeanManager.activate(rdb);				 
					 if(rdb  != null ){
						 if("Customer Service Representative".equals(rdb.getName())){
								Enumeration enmAddressList = (new AddressAccessBean())
								.findByMemberId(cmBean);
								while (enmAddressList.hasMoreElements()) {
									AddressAccessBean addressbean = (AddressAccessBean) enmAddressList
									.nextElement();
									if(addressbean.getLastName() !=null && !"".equals(addressbean.getLastName().trim())){
										vCsrDetails = addressbean.getLastName()+" "+addressbean.getFirstName()+" | "+addressbean.getEmail1();
									}								
								}
							 isCsr = true;
							 break;
						 }
					 }
				 }
			}
		}
		 }catch (Exception e) {
				// e.printStackTrace();
				throw new ECApplicationException(ECMessage._ERR_BAD_CMD_PARAMETER,
						this.getClass().getName(), strMethodName, "");
			}
		
		return vCsrDetails;
	}
	private String getLastLoggedInBefore(){
		 String strMethodName = "getLastLoggedInBefore";
		 String lastLoginTime= "";
		 SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
		Date today = Calendar.getInstance().getTime();
		Date logedInDate = null;
		String vTextToReply = "";
		 try {	 
			 lastLoginTime = getCommandContext().getUser().getPreviousLastSession();
			 logedInDate = sdf.parse(lastLoginTime);
			 long diff = sdf.parse(sdf.format(today)).getTime() - sdf.parse(sdf.format(logedInDate)).getTime();
			 long diffDays = diff / (24 * 60 * 60 * 1000);
			 if(diffDays == 0){
				 vTextToReply = "Today";
			 }else if(diffDays == 1){
				 vTextToReply = "Yesterday";
			 }else{
				 vTextToReply = diffDays+" Days Ago";
			 } 
		 }catch (Exception e) {
			 lastLoginTime = "";
			}
		return vTextToReply;
	}
	private String getLoggedInUser()  throws ECApplicationException {
		 String strMethodName = "getLoggedInUser";
		 String displayName= "";
		 try {
			 String memberId = getCommandContext().getUser().getMemberId();	
			 Enumeration enmAddressList = (new AddressAccessBean()).findByMemberId(Long.valueOf(memberId));
			 while (enmAddressList.hasMoreElements()) {
				 AddressAccessBean addressbean = (AddressAccessBean) enmAddressList.nextElement();
				 if(addressbean.getFirstName() !=null && !"".equals(addressbean.getFirstName().trim())){
					 displayName = addressbean.getFirstName();
				 }else if (addressbean.getLastName() !=null && !"".equals(addressbean.getLastName().trim())){
					 displayName = addressbean.getLastName();
				 }
			 }
		 }catch (Exception e) {
				// e.printStackTrace();
				throw new ECApplicationException(ECMessage._ERR_BAD_CMD_PARAMETER,
						this.getClass().getName(), strMethodName, "");
		}
		return displayName;
	}
	private boolean checkForRole(String roleStockcheckpage) throws ECApplicationException {
		 String strMethodName = "checkForRole";
			boolean isValidRole = false;
			
		 try {
			 OrganizationDataBean orgBean = new OrganizationDataBean() ;
			 orgBean.setDataBeanKeyMemberId( getCommandContext().getParentOrg());
			 DataBeanManager.activate(orgBean);
			
			 Integer[] roles = getCommandContext().getUser().getRoles();
			 for(int i = 0 ; i < roles.length ; i++){
				 RoleDataBean rdb = new RoleDataBean();
				 rdb.setInitKey_RoleId(roles[i].toString());
				 DataBeanManager.activate(rdb);
				 if(rdb  != null ){
					 if(roleStockcheckpage.equals(rdb.getName())){
						 isValidRole = true;
						 break;
					 }
				 }
			 }		 
				
			} catch (Exception e) {
				// e.printStackTrace();
				throw new ECApplicationException(ECMessage._ERR_BAD_CMD_PARAMETER,
						this.getClass().getName(), strMethodName, "");
			}
		return isValidRole;
	}
	private boolean findIfLoginUser() throws ECApplicationException {
		String strMethodName = "findIfLoginUser";
		boolean isLogin = false;
		try {
			String vUserType = getCommandContext().getUser().getRegisterType();
			if ("G".equals(vUserType)) {
				isLogin = false;
			} else{
				isLogin = true;
			}
		} catch (Exception e) {
			// e.printStackTrace();
			throw new ECApplicationException(ECMessage._ERR_BAD_CMD_PARAMETER,
					this.getClass().getName(), strMethodName, "");
		}
		return isLogin;
	}
	
	private long getLogoutInterval()throws ECApplicationException {
		String strMethodName = "getLogoutInterval";
        long loginTime = 0;
        long currentTime = 0; 
        long logoutInterval = 0;
        
        UserDataBean userBean =  new UserDataBean();
        userBean.setCommandContext(getCommandContext());
        userBean.setDataBeanKeyMemberId(getCommandContext().getUserId().toString());
        try {
              DataBeanManager.activate(userBean, getCommandContext()); 
              vLogedInTime  = (userBean.getLastSessionInEJBType()).getTime(); 
              GregorianCalendar gc = new GregorianCalendar();
              currentTime = gc.getTimeInMillis();
              logoutInterval = WcsApp.defaultTimeout - (currentTime - vLogedInTime) ;     
        } catch (Exception e) {             
             // e.printStackTrace();
			throw new ECApplicationException(ECMessage._ERR_BAD_CMD_PARAMETER,
					this.getClass().getName(), strMethodName, "");
        }
        return logoutInterval;
  }
	


	public void validateParameters() throws ECException {
		 
	 }

}
