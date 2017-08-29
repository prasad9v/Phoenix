package com.ibm.commerce.domtar.commands;

import com.ibm.commerce.command.TaskCommand;
import com.ibm.commerce.domtar.databeans.DomtarHoldInputDataBean;
import com.ibm.commerce.domtar.databeans.DomtarHoldOutputDataBean;

public interface DomtarInvokeInventoryHoldWSTaskCmd extends TaskCommand {
	
	static final String defaultCommandClassName=
		"com.ibm.commerce.domtar.commands.DomtarInvokeInventoryHoldWSTaskCmdImpl";
	public void setHoldInput(DomtarHoldInputDataBean holdInput);
	public DomtarHoldOutputDataBean getHoldResponseOutput();
}
