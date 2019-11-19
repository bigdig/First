package com.crm.com.landray.ws;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

public class AxisClient {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String endpoint = "http://uim.sh.bank-of-china.com:9090/uim/services/TokenService";
		String operationName = "getUser";
		String paramName0 = "arg0";
		String paramName1 = "arg1";
		String admin = "YWRtaW4sMTI3Mzc0NDU0MTYyNSwxMjczNzQ2MzQxNjI1LA%3D%3D";
		String zuoj = "MzEwMTE0ODAwNDA1MDgyLDEyNzY3NDAwNTkxODcsMTI3Njc0MTg1OTE4Nyw%3D";
		String url = "http://be.sh.bank-of-china.com:9080/be";
		String token = zuoj;
		try {
			Service service = new Service();
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(endpoint));
			call.setOperationName(operationName);
			call.addParameter(paramName0, org.apache.axis.Constants.XSD_STRING,
					javax.xml.rpc.ParameterMode.INOUT);
			call.addParameter(paramName1, org.apache.axis.Constants.XSD_STRING,
					javax.xml.rpc.ParameterMode.INOUT);

			call.setReturnType(org.apache.axis.Constants.XSD_STRING);

			String v = (String) call.invoke(new Object[] { token, url });
			System.out.println(v);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

}
