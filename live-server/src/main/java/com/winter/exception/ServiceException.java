package com.winter.exception;

import com.winter.common.pojo.ResponseType;
import lombok.Data;

@Data
public class ServiceException extends RuntimeException{
    private String message;
    private String code;

    public ServiceException(ResponseType responseType) {
        this.message = responseType.getMsg();
        this.code = responseType.getCode();
    }

    public ServiceException(String message){
        this.message = message;
        this.code = "500";
    }
}
