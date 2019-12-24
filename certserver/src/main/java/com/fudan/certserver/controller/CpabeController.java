package com.fudan.certserver.controller;

import com.fudan.certserver.common.ServerResponse;
import com.fudan.certserver.service.CpabeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Cpabe/")
public class CpabeController {

    @Autowired
    private CpabeService cpabeService;

    /**
     * 生成私钥
     * @param attrStr
     * @return
     */
    @PostMapping(value = "keygen")
    @ResponseBody
    public ServerResponse keygen(@RequestParam(value="attrStr",required=true) String attrStr){
        return cpabeService.keygen(attrStr);
    }

    /**
     * 属性加密
     * @param policy
     * @param cert
     * @return
     */
    @PostMapping(value = "enc")
    @ResponseBody
    public ServerResponse enc(@RequestParam(value="policy",required=true) String policy,
                              @RequestParam(value="cert",required=true) String cert){
        return cpabeService.enc(policy,cert);
    }

    /**
     * 属性解密
     * @param prvkey
     * @param cert_aes
     * @return
     */
    @PostMapping(value = "dec")
    @ResponseBody
    public ServerResponse dec(@RequestParam(value="prvkey",required=true) String prvkey,
                              @RequestParam(value="cert_aes",required=true) String cert_aes){
        return cpabeService.dec(prvkey,cert_aes);
    }

}
