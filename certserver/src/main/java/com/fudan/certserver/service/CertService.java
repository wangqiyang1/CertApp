package com.fudan.certserver.service;

import com.fudan.certserver.common.ServerResponse;

public interface CertService {

    public ServerResponse userRegister(String userId , String userName);

    public ServerResponse uploadCert(String certId , String userId , String certName , String school , String policy , String certMessage);

    public ServerResponse queryUser(String userId);

    public ServerResponse queryCert(String certId , String prvkey);

    public ServerResponse queryCertWithUserId(String certId , String userId , String prvkey);

    public ServerResponse computeHash(String message);

}
