package com.ibm.commerce.domtar.commands;

import com.ibm.commerce.command.ControllerCommand;

public interface DomtarOrderCreateCmd  extends ControllerCommand{
	
	String defaultCommandClassName = "com.ibm.commerce.domtar.commands.DomtarOrderCreateCmdImpl";

}
