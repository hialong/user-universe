package com.decade.usercenter.common;

/**
 * 错误码
 * 4xx表示请求错误
 * 5xx表示服务器错误
 *
 * @author hailong
 */
public enum ErrorCode {
    /**
     * 无效参数
     */
    INVALID_PARAMS(40000, "无效参数或参数错误", ""),

    /**
     * 参数为null
     */
    NULL_PARAMS(40001,"参数不能为null",""),
    /**
     * 未登录
     */
    NOT_LOGIN(40100,"未登录",""),
    /**
     * 权限不足
     */
    NO_AUTH(40101,"权限不足",""),

    /**
     * 用户已经注册
     */
    DUPLICATE_USER(40102,"用户已被注册",""),

    /**
     * 用户不存在
     */
    USER_DOESNOT_EXIT(40103,"用户不存在",""),
    /**
     * 操作失败
     */
    OPERATION_ERROR(40104,"操作失败",""),

    /**
     * 未找到数据
     */
    NOT_FOUND_ERROR(40105,"请求数据不存在",""),

    /**
     * 内部异常
     */
    INTERNAL_ERROR(50000,"internal system error","unknown error");


    /**
     * 状态码信息
     */
    private final int code;
    /**
     * 状态码信息
     */
    private final String message;
    /**
     * 状态码描述
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
