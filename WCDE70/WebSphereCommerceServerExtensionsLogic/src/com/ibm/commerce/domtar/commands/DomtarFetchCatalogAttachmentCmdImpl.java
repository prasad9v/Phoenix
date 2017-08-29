package com.ibm.commerce.domtar.commands;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commerce.command.ControllerCommandImpl;
import com.ibm.commerce.datatype.TypedProperty;
import com.ibm.commerce.exception.ECApplicationException;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.ras.ECMessage;
import com.ibm.commerce.server.ECConstants;

public class DomtarFetchCatalogAttachmentCmdImpl extends ControllerCommandImpl
		implements DomtarFetchCatalogAttachmentCmd {
	public void performExecute() throws ECException {
		super.performExecute();
		String strMethodName = "performExecute";
		String fileName = "";
		String attachmentPath = getRequestProperties().getString(
				"attachemntPath", "");
		BufferedReader br = null;
		TypedProperty rspProp = new TypedProperty();
		rspProp = new TypedProperty();
		try {
			HttpServletRequest req = ((com.ibm.commerce.webcontroller.HttpControllerRequestObject) getCommandContext()
					.getRequest()).getHttpRequest();
			StringBuffer vHostNameBuffer = req.getRequestURL(); 
			String vHostName = vHostNameBuffer.toString().split("webapp")[0];
			vHostName = vHostName.substring(0, (vHostName.length() - 1));
			String vCompleteUrl = vHostName + attachmentPath;
			HttpServletResponse response = (HttpServletResponse) getCommandContext()
					.getResponse();
			response.setContentType("application/pdf");
			if (!"".equals(attachmentPath)) {
				String[] urlSplit = attachmentPath.split("/");
				fileName = urlSplit[urlSplit.length - 1];
				URL vPdfPath = new URL(vCompleteUrl);
				InputStream in = vPdfPath.openStream();
				BufferedInputStream bin = new BufferedInputStream(in);
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				int c;
				while ((c = bin.read()) > -1) {
					bout.write(c);
				}
				bin.close();
				byte[] buf = bout.toByteArray();
				rspProp.put(ECConstants.EC_RAWDOCUMENT, buf);
			} else {
				rspProp.put(ECConstants.EC_RAWDOCUMENT, new byte[0]);
			}
			response.setHeader("content-disposition", "attachment; filename="
					+ fileName);
			getCommandContext().setResponse(response);
		} catch (Exception e) {
			throw new ECApplicationException(ECMessage._ERR_BAD_CMD_PARAMETER,
					this.getClass().getName(), strMethodName, "");
		}
		rspProp.put(ECConstants.EC_VIEWTASKNAME,
				ECConstants.EC_GENERIC_DIRECTVIEW);
		setResponseProperties(rspProp);
	}
}
