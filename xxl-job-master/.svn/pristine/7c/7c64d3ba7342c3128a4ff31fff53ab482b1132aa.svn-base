package com.crm.com.landray.ws;

import java.rmi.RemoteException;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

public class Client {

	public static void main(String[] args) {

		String TEMP_URI = "http://WebXml.com.cn/";

		String endpoint = "http://www.webxml.com.cn/webservices/DomesticAirline.asmx";
		String operationName = "getDomesticCity";
		try {
			Service service = new Service();
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress(endpoint);
			call.setOperationName(new QName(TEMP_URI, operationName));
			// call.addParameter(new QName(TEMP_URI, "URL"),
			// org.apache.axis.encoding.XMLType.XSD_STRING,
			// javax.xml.rpc.ParameterMode.INOUT);

			call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);

			call.setUseSOAPAction(true);
			call.setSOAPActionURI(TEMP_URI + operationName);

			Object v = call.invoke(new Object[] {});
			System.out.println(v);

		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
}
