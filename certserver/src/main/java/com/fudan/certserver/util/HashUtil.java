package com.fudan.certserver.util;

import org.apache.commons.codec.digest.DigestUtils;

public class HashUtil {
    //盛昌哈希值具体方法
    public static String getHashStr(String message){
        String hashStr = DigestUtils.sha256Hex(message);
        System.out.println(hashStr);
        return hashStr;
    }

    public static void main(String[] args) {
        String temp = HashUtil.getHashStr("{\"message\":\"this is a test transaction 15\"},{\"message\":\"this is a test transaction 16\"},{\"message\":\"this is a test transaction 18\"}");
        System.out.println(temp);
    }
}
