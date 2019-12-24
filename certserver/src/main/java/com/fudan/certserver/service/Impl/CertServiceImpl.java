package com.fudan.certserver.service.Impl;

import co.junwei.cpabe.Cpabe;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fudan.certserver.common.ServerResponse;
import com.fudan.certserver.service.CertService;
import com.fudan.certserver.util.CertHander;
import com.fudan.certserver.util.Constant;
import com.fudan.certserver.util.HashUtil;
import org.springframework.stereotype.Service;

import javax.json.JsonObject;
import java.io.IOException;

@Service
public class CertServiceImpl implements CertService {
    //用户注册
    @Override
    public ServerResponse userRegister(String userId, String userName) {

        try {
            CertHander.userRegister(userName,userId);
            return ServerResponse.createBySuccessMessage("userRegister success");
        }catch (Exception e){
            return ServerResponse.createByErrorMessage("userRegister fail");
        }
    }
    //证书上传
    @Override
    public ServerResponse uploadCert(String certId , String userId ,
            String certName , String school , String policy , String certMessage ) {
        String hashStr = HashUtil.getHashStr(certMessage);
        if(policy == null || "".equals(policy)){
            try {
                CertHander.upLoadCert(certId,userId,certName,school,"",certMessage,hashStr);
                return ServerResponse.createBySuccessMessage("证书上传成功,证书未被属性加密");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            try {
                String certAes = Cpabe.enc_nofile(Constant.pubfile,policy,certMessage);
                CertHander.upLoadCert(certId,userId,certName,school,policy,certAes,hashStr);
                System.out.println("certAes:"+certAes);
                return ServerResponse.createBySuccessMessage("证书上传成功，证书已被属性加密");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ServerResponse.createByErrorMessage("证书上传失败");
    }
    //用户查询
    @Override
    public ServerResponse queryUser(String userId) {

        try {
            String userInfo = CertHander.queryUser(userId);
            return ServerResponse.createBySuccess(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ServerResponse.createByErrorMessage("查询用户失败");
    }
    //证书查询
    @Override
    public ServerResponse queryCert(String certId , String prvkey) {
        try {
            String certInfo = CertHander.queryCert(certId);
            JSONObject jsonObject = JSONObject.parseObject(certInfo);
            String certAes = jsonObject.getString("certMessage");
            System.out.println("certAes:"+certAes);
            try {
                //prvkey = prvkey==null ? "" : prvkey;
                String cert = Cpabe.dec_nofile(Constant.pubfile,prvkey,certAes);
                jsonObject.put("certMessage",cert);
                return ServerResponse.createBySuccess("属性解密成功",jsonObject.toJSONString());
            } catch (Exception e) {
                e.printStackTrace();
                return ServerResponse.createBySuccess("属性解密失败",certInfo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ServerResponse.createByErrorMessage("查询证书失败");
    }
    //证书查询
    @Override
    public ServerResponse queryCertWithUserId(String certId, String userId , String prvkey) {
        try {
            String certInfo = CertHander.queryCertWithUserID(certId,userId);
            JSONObject jsonObject = JSONObject.parseObject(certInfo);
            String certAes = jsonObject.getString("certMessage");
            try {
                String cert = Cpabe.dec_nofile(Constant.pubfile,prvkey,certAes);
                jsonObject.put("certMessage",cert);
                return ServerResponse.createBySuccess("属性解密成功",jsonObject.toJSONString());
            } catch (Exception e) {
                e.printStackTrace();
                return ServerResponse.createBySuccess("未进行属性解密",certInfo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ServerResponse.createByErrorMessage("查询证书失败");
    }
    //哈希结算
    @Override
    public ServerResponse computeHash(String message){
        if(message != null && !"".equals(message))
            return ServerResponse.createBySuccess(HashUtil.getHashStr(message));
        return ServerResponse.createByErrorMessage("输入不能为空");
    }
}
