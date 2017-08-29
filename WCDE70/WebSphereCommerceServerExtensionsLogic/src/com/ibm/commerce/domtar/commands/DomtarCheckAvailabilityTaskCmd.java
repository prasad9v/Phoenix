package com.ibm.commerce.domtar.commands;

import java.util.List;

import com.ibm.commerce.command.TaskCommand;

public interface DomtarCheckAvailabilityTaskCmd extends TaskCommand {
	static final String defaultCommandClassName=
		"com.ibm.commerce.domtar.commands.DomtarCheckAvailabilityTaskCmdImpl";
	/**
	 * 
	 * @param pInputDatas
	 * tO SET INPUT DATA TO DO STOCKCHECK
	 */
	public void setInputData(com.ibm.commerce.domtar.databeans.DomtarStockCheckInputDataBean pInputDatas);
	public List<com.ibm.commerce.domtar.databeans.DomtarStockCheckOutputDataBean> getOutputDatas();
	public void setTimeout(boolean pTimeOut);
	public boolean getTimeOut();
	public String getStockCheckWebServiceError();
	public boolean isPriceMissing();
}
