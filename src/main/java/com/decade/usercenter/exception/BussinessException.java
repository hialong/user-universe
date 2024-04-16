package com.decade.usercenter.exception;

import com.decade.usercenter.common.ErrorCode;

import java.text.MessageFormat;

public class BussinessException extends RuntimeException{
    private final int code;

    private final String description;

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public BussinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BussinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BussinessException(ErrorCode errorCode,String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    /**
     * 针对不同日志需求自行写对应的错误描述，类似“('hello!{0} xxx{1}','jack','tom')”
     * @param errorCode 错误码
     * @param description 描述
     * @param object 模板对象
     */
    public BussinessException(ErrorCode errorCode,String description,Object... object) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = MessageFormat.format(description, object);
    }
}
