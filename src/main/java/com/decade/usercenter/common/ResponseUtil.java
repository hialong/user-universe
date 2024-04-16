package com.decade.usercenter.common;

/**
 * 返回公共返回类工具类
 *
 * @author hailong
 */
public class ResponseUtil {
    /**
     * 成功返回
     * @param data 请求信息
     * @return 封装返回通用对象
     * @param <T> 返回信息类型
     */
    public static <T> BaseResponse<T> ok(T data) {
        return new BaseResponse<>(200, data, "ok");
    }

    /**
     * 失败返回
     * @param data 请求信息
     * @return 封装返回通用对象
     * @param <T> 返回信息类型
     */
    public static <T> BaseResponse<T> error(T data) {
        return new BaseResponse<>(500, data, "500 faild");
    }

    /**
     * 失败返回
     * @param errorCode 错误码
     * @return 封装返回通用对象
     * @param <T> 返回信息类型
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    public static <T> BaseResponse<T> error(ErrorCode errorCode,String message) {
        return new BaseResponse<>(errorCode,message);
    }

    public static <T> BaseResponse<T> error(int code,String message) {
        return new BaseResponse<>(code,message,"");
    }

    public static <T> BaseResponse<T> error(int code,String message,String description) {
        return new BaseResponse<>(code,message,description);
    }

}
