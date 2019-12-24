package com.fudan.certserver.service.Impl;

import co.junwei.cpabe.Cpabe;
import com.fudan.certserver.common.ServerResponse;
import com.fudan.certserver.service.CpabeService;
import com.fudan.certserver.util.Constant;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Service
public class CpabeServiceImpl implements CpabeService {
    //私钥生成
    @Override
    public ServerResponse keygen(String attr_str) {
        try {
            String prvkey =  Cpabe.keygen_nofile(Constant.pubfile,Constant.mskfile,attr_str);
            return ServerResponse.createBySuccess(prvkey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ServerResponse.createByErrorMessage("获取私钥失败");
    }
    //属性加密
    @Override
    public ServerResponse enc(String policy, String cert) {
        try {
            String certAes = Cpabe.enc_nofile("key/pub_key",policy,cert);
            return ServerResponse.createBySuccess(certAes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ServerResponse.createByErrorMessage("加密失败");
    }
    //属性解密
    @Override
    public ServerResponse dec(String prvkey, String cert_aes) {
        try {
            String cert = Cpabe.dec_nofile("key/pub_key",prvkey,cert_aes);
            return ServerResponse.createBySuccess(cert);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ServerResponse.createByErrorMessage("解密失败");
    }
}
