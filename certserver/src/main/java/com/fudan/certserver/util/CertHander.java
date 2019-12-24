package com.fudan.certserver.util;

import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;

import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;
//证书业务具体处理类
public class CertHander {
    public static void main(String[] args) throws Exception {
        String user1 = queryUser("123456");
        String cert1 = queryCert("000001");
        //upLoadCert("000002","123456","复旦大学毕业证书","fudan","school:fudan 1of1","张三 于 2019年 毕业于 复旦大学","ASDFG");
        String user2 = queryUser("123456");
        String cert2 = queryCertWithUserID("000002","123456");
        System.out.println(user1);
        System.out.println(cert1);
        System.out.println(cert2);
        System.out.println(user2);
    }

    //用户注册具体实现
    public static void userRegister(String userName , String userId) throws Exception{
        HFClient client = HFClient.createNewInstance();
        Channel channel = initChannel(client);

        // 构建proposal
        TransactionProposalRequest req = client.newTransactionProposalRequest();
        // 指定要调用的chaincode
        ChaincodeID cid = ChaincodeID.newBuilder().setName("mycc").build();
        req.setChaincodeID(cid);
        req.setFcn("userRegister");
        req.setArgs(userName,userId);
        System.out.println("Executing for " + "userRegister");
        // 发送proprosal
        Collection<ProposalResponse> resps = channel.sendTransactionProposal(req);

        // 提交给orderer节点
        channel.sendTransaction(resps);
    }

    /**
     * 更新账本具体实现
     * @throws Exception
     */
    public static void upLoadCert(String... cert) throws Exception {
        HFClient client = HFClient.createNewInstance();
        Channel channel = initChannel(client);

        // 构建proposal
        TransactionProposalRequest req = client.newTransactionProposalRequest();
        // 指定要调用的chaincode
        ChaincodeID cid = ChaincodeID.newBuilder().setName("mycc").build();
        req.setChaincodeID(cid);
        req.setFcn("certUpload");
/*        String certId = cert[0];
        String userId = cert[1];
        String certName = cert[2];
        String school = cert[3];
        String attrpolic = cert[4];
        String aertMessage = cert[5];
        String hashStr = cert[6];*/
        //req.setArgs("000001", "123456","成绩单","fudan","none","语文：91，英语：100","QWERT");
        req.setArgs(cert);
        System.out.println("Executing for " + "certUpload");
        // 发送proprosal
        Collection<ProposalResponse> resps = channel.sendTransactionProposal(req);

        // 提交给orderer节点
        channel.sendTransaction(resps);
    }

    /**
     * 查询账本具体实现
     * @throws Exception
     */
    public static String queryUser(String userId) throws Exception {
        HFClient client = HFClient.createNewInstance();
        Channel channel = initChannel(client);

        String key = userId;

        // 构建proposal
        QueryByChaincodeRequest req = client.newQueryProposalRequest();
        // 指定要调用的chaincode
        ChaincodeID cid = ChaincodeID.newBuilder().setName("mycc").build();
        req.setChaincodeID(cid);
        req.setFcn("queryUser");
        req.setArgs(key);
        System.out.println("Querying for user , userId is " + key);
        Collection<ProposalResponse> resps = channel.queryByChaincode(req);

        String response="";

        for (ProposalResponse resp : resps) {
            String payload = new String(resp.getChaincodeActionResponsePayload());
            System.out.println("response: " + payload);
            response = payload;
        }
        return response;
    }
    //查询证书具体实现
    public static String queryCert(String certId) throws Exception{
        HFClient client = HFClient.createNewInstance();
        Channel channel = initChannel(client);

        String key = certId;

        // 构建proposal
        QueryByChaincodeRequest req = client.newQueryProposalRequest();
        // 指定要调用的chaincode
        ChaincodeID cid = ChaincodeID.newBuilder().setName("mycc").build();
        req.setChaincodeID(cid);
        req.setFcn("queryCert");
        req.setArgs(key);
        System.out.println("Querying for cert, certId is " + key);
        Collection<ProposalResponse> resps = channel.queryByChaincode(req);

        String response="";

        for (ProposalResponse resp : resps) {
            String payload = new String(resp.getChaincodeActionResponsePayload());
            System.out.println("response: " + payload);
            response = payload;
        }
        return response;
    }
    //查询证书具体方法
    public static String queryCertWithUserID(String certId,String userId) throws Exception{
        HFClient client = HFClient.createNewInstance();
        Channel channel = initChannel(client);


        // 构建proposal
        QueryByChaincodeRequest req = client.newQueryProposalRequest();
        // 指定要调用的chaincode
        ChaincodeID cid = ChaincodeID.newBuilder().setName("mycc").build();
        req.setChaincodeID(cid);
        req.setFcn("queryCertWithUserId");
        req.setArgs(certId,userId);
        System.out.println("Querying for cert, certId is " + certId+", userId is "+ userId);
        Collection<ProposalResponse> resps = channel.queryByChaincode(req);

        String response="";

        for (ProposalResponse resp : resps) {
            String payload = new String(resp.getChaincodeActionResponsePayload());
            System.out.println("response: " + payload);
            response = payload;
        }
        return response;
    }
    //初始化通道
    private static Channel initChannel(HFClient client) throws Exception {
        CryptoSuite cs = CryptoSuite.Factory.getCryptoSuite();
        client.setCryptoSuite(cs);

        client.setUserContext(
                new LocalUser(
                        "admin","Org1MSP",
                        CertUtils.loadEnrollment("cert", "admin")
                )
        );

        // 初始化channel
        Channel channel = client.newChannel("certchannel");
        channel.addPeer(client.newPeer("peer0", "grpc://127.0.0.1:7051"));
        // 指定排序节点地址, 无论是后面执行查询还是更新都必须指定排序节点
        channel.addOrderer(client.newOrderer("orderer", "grpc://127.0.0.1:7050"));
        channel.initialize();

        return channel;
    }
}

/**
 * User接口实现类
 */
class LocalUser implements User {
    private String name;
    private String mspId;
    private Enrollment enrollment;

    public LocalUser(String name, String mspId,Enrollment enrollment) {
        this.name = name;
        this.mspId = mspId;
        this.enrollment = enrollment;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Set<String> getRoles() {
        return Collections.emptySet();
    }

    @Override
    public String getAccount() {
        return "";
    }

    @Override
    public String getAffiliation() {
        return "";
    }

    @Override
    public Enrollment getEnrollment() {
        return this.enrollment;
    }

    @Override
    public String getMspId() {
        return this.mspId;
    }
}
