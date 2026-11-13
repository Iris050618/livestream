package com.winter.common.pojo;

public enum PortType {
    H5("H5", "手机端"),
    WEB("WEB","控制台");

    private String type;
    private String msg;
    PortType(String type, String msg){
        this.type = type;
        this.msg = msg;
    }

    public String getType(){
        return this.type;
    }

    public boolean isWeb(){
        return WEB.getType().equals(this.type);
    }
}
