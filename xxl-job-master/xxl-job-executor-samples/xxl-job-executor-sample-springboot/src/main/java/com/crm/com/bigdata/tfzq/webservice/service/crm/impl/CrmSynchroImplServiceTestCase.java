/**
 * CrmSynchroImplServiceTestCase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.crm.com.bigdata.tfzq.webservice.service.crm.impl;

import com.crm.com.bigdata.tfzq.webservice.service.crm.Contacts;
import com.crm.com.bigdata.tfzq.webservice.service.crm.Customer;

public class CrmSynchroImplServiceTestCase extends junit.framework.TestCase {
    public CrmSynchroImplServiceTestCase(java.lang.String name) {
        super(name);
    }
    public CrmSynchroImplServiceTestCase() {}

    public void testCrmSynchroImplPortWSDL() throws Exception {
        javax.xml.rpc.ServiceFactory serviceFactory = javax.xml.rpc.ServiceFactory.newInstance();
        java.net.URL url = new java.net.URL(new com.crm.com.bigdata.tfzq.webservice.service.crm.impl.CrmSynchroImplServiceLocator().getCrmSynchroImplPortAddress() + "?WSDL");
        javax.xml.rpc.Service service = serviceFactory.createService(url, new com.crm.com.bigdata.tfzq.webservice.service.crm.impl.CrmSynchroImplServiceLocator().getServiceName());
        assertTrue(service != null);
    }

    public int test1CrmSynchroImplPortSaveOrUpdateCustomer(Customer customer) throws Exception {
    	com.crm.com.bigdata.tfzq.webservice.service.crm.impl.CrmSynchroImplServiceSoapBindingStub binding;
        try {
            binding = (com.crm.com.bigdata.tfzq.webservice.service.crm.impl.CrmSynchroImplServiceSoapBindingStub)
                          new com.crm.com.bigdata.tfzq.webservice.service.crm.impl.CrmSynchroImplServiceLocator().getCrmSynchroImplPort();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        int value = -3;
        value = binding.saveOrUpdateCustomer(customer);
        // TBD - validate results
        return value;
    }

    public int test2CrmSynchroImplPortDeleteContacts(String contactsId) throws Exception {
    	com.crm.com.bigdata.tfzq.webservice.service.crm.impl.CrmSynchroImplServiceSoapBindingStub binding;
        try {
            binding = (com.crm.com.bigdata.tfzq.webservice.service.crm.impl.CrmSynchroImplServiceSoapBindingStub)
                          new com.crm.com.bigdata.tfzq.webservice.service.crm.impl.CrmSynchroImplServiceLocator().getCrmSynchroImplPort();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        int value = -3;
        value = binding.deleteContacts(contactsId, 442595874);
        System.out.println(value);
        // TBD - validate results
        return value;
    }

    public int test3CrmSynchroImplPortDeleteCustomer(String customerId) throws Exception {
    	com.crm.com.bigdata.tfzq.webservice.service.crm.impl.CrmSynchroImplServiceSoapBindingStub binding;
        try {
            binding = (com.crm.com.bigdata.tfzq.webservice.service.crm.impl.CrmSynchroImplServiceSoapBindingStub)
                          new com.crm.com.bigdata.tfzq.webservice.service.crm.impl.CrmSynchroImplServiceLocator().getCrmSynchroImplPort();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        int value = -3;
        value = binding.deleteCustomer(customerId, 442595874);
        System.out.println(value);
        // TBD - validate results
        return value;
    }

    public int test4CrmSynchroImplPortSaveOrUpdateContacts(Contacts contacts) throws Exception {
    	com.crm.com.bigdata.tfzq.webservice.service.crm.impl.CrmSynchroImplServiceSoapBindingStub binding;
        try {
            binding = (com.crm.com.bigdata.tfzq.webservice.service.crm.impl.CrmSynchroImplServiceSoapBindingStub)
                          new com.crm.com.bigdata.tfzq.webservice.service.crm.impl.CrmSynchroImplServiceLocator().getCrmSynchroImplPort();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        int value = -3;
        value = binding.saveOrUpdateContacts(contacts);
        System.out.println(value);
        // TBD - validate results
        return value;
    }

}
