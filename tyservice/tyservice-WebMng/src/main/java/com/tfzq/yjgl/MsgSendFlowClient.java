package com.tfzq.yjgl;


import com.tfzq.yjgl.MsgSendFlow;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;


public class MsgSendFlowClient {

    //private static 
    private Service service = new Service();

    public MsgSendFlow sendMsg(String url,MsgSendFlow msgSendFlow) throws  RemoteException, ServiceException {

        Call call = (Call) service.createCall();
        call.setTargetEndpointAddress(url);
        //这里的QName的ns和wsdd文件中的对应
        QName qn = new QName("urn:MsgSendFlow", "MsgSendFlow");
        //这里是将对象序列化和反序列化的配置
        call.registerTypeMapping(MsgSendFlow.class, qn, new BeanSerializerFactory(MsgSendFlow.class, qn), new BeanDeserializerFactory(MsgSendFlow.class, qn));
        call.setOperationName("sendMsg");
        //设置参数类型
        call.addParameter("msgSendFlow", qn, ParameterMode.IN);
        call.setReturnClass(MsgSendFlow.class);
        msgSendFlow = (MsgSendFlow) call.invoke(new Object[] { msgSendFlow });
        return msgSendFlow;
    }

    public static void main(String[] args) throws Exception {
        MsgSendFlow msgSendFlow =new MsgSendFlow();
        msgSendFlow.setWs_wpd("SERVICES_PWD0122");
        msgSendFlow.setRs_id("from_out_sys_serviceid");
        msgSendFlow.setMsg_creater("sysadmin");
        msgSendFlow.setReceive_nums("13621835652");
        msgSendFlow.setMsg_content("测试流程发起移动审批！");

        try {
        	String url = "http://localhost:8888/tfzqkm/services/MsgSendFlow?wsdl";
        	MsgSendFlowClient client = new MsgSendFlowClient();
            msgSendFlow = client.sendMsg(url,msgSendFlow);
            System.out.println("msgSendFlow>>>>>>>>>>>>rs_value="+msgSendFlow.getRs_value());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

}
