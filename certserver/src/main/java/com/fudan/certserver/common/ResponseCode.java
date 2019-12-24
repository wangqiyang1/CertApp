package com.fudan.certserver.common;

//返回类定义
public enum ResponseCode {
    SUCCESS(0,"SUCCESS"),
    ERROR(1,"ERROR"),
    ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT");

    private int code;
    private String desc;

    ResponseCode(int code, String desc){
        this.code=code;
        this.desc=desc;
    }

    public int getCode(){
        return code;
    }
    public String getDesc(){
        return desc;
    }
}
