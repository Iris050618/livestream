package com.winter.common.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Response<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private String code;

    private String message;

//    private String appId;

//    private String dataType;

    private T result;

    public Response() {
        super();
    }

    public Response(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public Response(String code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public static <T>Response<T> success(String message, T result){
        ResponseType type = ResponseType.SUCCESS;
        return new Response<>(type.getCode(),message,result);
    }

    public static <T>Response<T> success(T result){
        ResponseType type = ResponseType.SUCCESS;
        return new Response<>(type.getCode(),type.getMsg(),result);
    }

    public static <T>Response<T> success(){
        ResponseType type = ResponseType.SUCCESS;
        return new Response<>(type.getCode(),type.getMsg(),null);
    }

    public static <T>Response<T> failed(String message, T result){
        ResponseType type = ResponseType.FAILED;
        return new Response<>(type.getCode(),message,result);
    }

    public static <T>Response<T> failed(String message){
        ResponseType type = ResponseType.FAILED;
        return new Response<>(type.getCode(),message);
    }

    public static <T>Response<T> failed(){
        ResponseType type = ResponseType.FAILED;
        return new Response<>(type.getCode(),type.getMsg());
    }

    public static <T>Response<T> failed(ResponseType type){
        return new Response<>(type.getCode(),type.getMsg());
    }

    public static <T>Response<T> failed(ResponseType type, T result){
        return new Response<>(type.getCode(),type.getMsg(), result);
    }
}