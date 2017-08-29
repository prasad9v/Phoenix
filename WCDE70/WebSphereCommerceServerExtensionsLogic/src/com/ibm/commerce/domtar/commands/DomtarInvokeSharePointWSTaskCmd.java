package com.ibm.commerce.domtar.commands;

import com.ibm.commerce.command.TaskCommand;

public interface DomtarInvokeSharePointWSTaskCmd extends TaskCommand {

	static final String defaultCommandClassName=
		"com.ibm.commerce.domtar.commands.DomtarInvokeSharePointWSTaskCmdImpl";

	public void setDocumentType(String documentType);
	public void setDocumentNumber(String documentNumber);
	public void setRequester(String requester);
	public void setLanguage(String language);
	public void setChannel(String channel);

	public String getDocumentType();
	public String getDocumentNumber();
	public String getRequester();
	public String getLanguage();
	public String getChannel();

	public byte[] getDocumentAsByteArray();
}
