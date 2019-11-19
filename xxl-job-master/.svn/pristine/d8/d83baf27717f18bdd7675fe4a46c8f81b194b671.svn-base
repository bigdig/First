/**
 * CrmSynchroImplServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.crm.com.bigdata.tfzq.webservice.service.crm.impl;

public class CrmSynchroImplServiceLocator extends org.apache.axis.client.Service implements com.crm.com.bigdata.tfzq.webservice.service.crm.impl.CrmSynchroImplService {

    public CrmSynchroImplServiceLocator() {
    }


    public CrmSynchroImplServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CrmSynchroImplServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CrmSynchroImplPort
//    private java.lang.String CrmSynchroImplPort_address = "http://192.168.41.108:8888/tfzqkm/ws/crmSynchro";
    private java.lang.String CrmSynchroImplPort_address = "http://192.168.43.87:8888/tfzqkm/ws/crmSynchro";

    public java.lang.String getCrmSynchroImplPortAddress() {
        return CrmSynchroImplPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CrmSynchroImplPortWSDDServiceName = "CrmSynchroImplPort";

    public java.lang.String getCrmSynchroImplPortWSDDServiceName() {
        return CrmSynchroImplPortWSDDServiceName;
    }

    public void setCrmSynchroImplPortWSDDServiceName(java.lang.String name) {
        CrmSynchroImplPortWSDDServiceName = name;
    }

    public com.crm.com.bigdata.tfzq.webservice.service.crm.CrmSynchro getCrmSynchroImplPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CrmSynchroImplPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCrmSynchroImplPort(endpoint);
    }

    public com.crm.com.bigdata.tfzq.webservice.service.crm.CrmSynchro getCrmSynchroImplPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
        	com.crm.com.bigdata.tfzq.webservice.service.crm.impl.CrmSynchroImplServiceSoapBindingStub _stub = new com.crm.com.bigdata.tfzq.webservice.service.crm.impl.CrmSynchroImplServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getCrmSynchroImplPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCrmSynchroImplPortEndpointAddress(java.lang.String address) {
        CrmSynchroImplPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.crm.com.bigdata.tfzq.webservice.service.crm.CrmSynchro.class.isAssignableFrom(serviceEndpointInterface)) {
            	com.crm.com.bigdata.tfzq.webservice.service.crm.impl.CrmSynchroImplServiceSoapBindingStub _stub = new com.crm.com.bigdata.tfzq.webservice.service.crm.impl.CrmSynchroImplServiceSoapBindingStub(new java.net.URL(CrmSynchroImplPort_address), this);
                _stub.setPortName(getCrmSynchroImplPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("CrmSynchroImplPort".equals(inputPortName)) {
            return getCrmSynchroImplPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://impl.crm.service.webservice.tfzq.bigdata.com/", "CrmSynchroImplService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://impl.crm.service.webservice.tfzq.bigdata.com/", "CrmSynchroImplPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("CrmSynchroImplPort".equals(portName)) {
            setCrmSynchroImplPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
