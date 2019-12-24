package com.fudan.certserver.controller;

import com.fudan.certserver.common.ServerResponse;
import com.fudan.certserver.service.CertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//证书Contrntroller进入类
@RestController
@RequestMapping("/certWeb/")
public class CertController {

    @Autowired
    private CertService certService;

    /**
     * 用户注册接口
     * @param userId
     * @param userName
     * @return
     */
    @PostMapping(value = "userRegister")
    @ResponseBody
    public ServerResponse userRegister(@RequestParam(value="userId",required=true) String userId,
                                       @RequestParam(value="userName",required=true) String userName){
        return certService.userRegister(userId,userName);
    }

    /**
     * 证书上传接口
     * @param certId
     * @param userId
     * @param certName
     * @param school
     * @param policy
     * @param certMessage
     * @return
     */
    @PostMapping(value = "uploadCert")
    @ResponseBody
    public ServerResponse uploadCert(@RequestParam(value="certId",required=true) String certId,
                                     @RequestParam(value="userId",required=true) String userId,
                                     @RequestParam(value="certName",required=true) String certName,
                                     @RequestParam(value="school",required=true) String school,
                                     @RequestParam(value="policy",required=true) String policy,
                                     @RequestParam(value="certMessage",required=true) String certMessage){
        return certService.uploadCert(certId,userId,certName,school,policy,certMessage);
    }

    /**
     * 用户查询接口
     * @param userId
     * @return
     */
    @PostMapping(value = "queryUser")
    @ResponseBody
    public ServerResponse queryUser(@RequestParam(value="userId",required=true) String userId){
        return certService.queryUser(userId);
    }

    /**
     * 证书查询接口1
     * @param certId
     * @param prvkey
     * @return
     */
    @PostMapping(value = "queryCert")
    @ResponseBody
    public ServerResponse queryCert(@RequestParam(value="certId",required=true) String certId,
                                    @RequestParam(value="prvkey",required=false) String prvkey){
        return certService.queryCert(certId,prvkey);
    }

    /**
     * 正整数查询接口2
     * @param certId
     * @param userId
     * @param prvkey
     * @return
     */
    @PostMapping(value = "queryCertWithUserId")
    @ResponseBody
    public ServerResponse queryCertWithUserId(@RequestParam(value="certId",required=true) String certId,
                                              @RequestParam(value="userId",required=true) String userId,
                                              @RequestParam(value="prvkey",required=false) String prvkey){
        return certService.queryCertWithUserId(certId,userId,prvkey);
    }

    /**
     * 哈希计算接口
     * @param message
     * @return
     */
    @RequestMapping(value = "computeHash")
    @ResponseBody
    public ServerResponse queryCert(@RequestParam(value="message",required=true) String message){
        return certService.computeHash(message);
    }
}
