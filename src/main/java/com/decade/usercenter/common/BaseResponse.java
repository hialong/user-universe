package com.decade.usercenter.common;

import lombok.Data;

/**
 * 公共返回类
 *
 * @param <T> 数据体类
 * @author hailong
 */
@Data
public class BaseResponse<T> {

    /**
     * 返回结果码
     */
    private int Code;
    /**
     * 返回信息
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 返回信息描述
     */
    private String description;

    public BaseResponse(int code, T data, String message,String description) {
        Code = code;
        this.message = message;
        this.data = data;
        this.description = description;
    }

    public BaseResponse(int code, T data, String message) {
        Code = code;
        this.message = message;
        this.data = data;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "","");
    }


    public BaseResponse(ErrorCode errorCode){
        this(errorCode.getCode(),null,errorCode.getMessage(),errorCode.getDescription());
    }
    public BaseResponse(ErrorCode errorCode,String message){
        this(errorCode.getCode(),null,message,errorCode.getDescription());
    }

    public BaseResponse(ErrorCode errorCode,String message,String description){
        this(errorCode.getCode(),null,message,description);
    }

    public BaseResponse(int code,String message,String description){
        this(code,null,message,description);
    }
}
