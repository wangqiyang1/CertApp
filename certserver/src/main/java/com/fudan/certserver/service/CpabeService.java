package com.fudan.certserver.service;

import com.fudan.certserver.common.ServerResponse;

public interface CpabeService {

    public ServerResponse keygen(String attr_str);

    public ServerResponse enc(String policy , String cert );

    public ServerResponse dec(String prvkey , String cert_aes);
}
