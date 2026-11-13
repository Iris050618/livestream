package com.winter.enums;

public enum LiveStatus {
    /**
     *
     */
    ON_LINE("ON_LINE","在线"),
    OFF_LINE("OFF_LINE","离线");
    private final String type;
    private final String msg;

    LiveStatus(String type, String msg){
        this.type = type;
        this.msg = msg;
    }
}
