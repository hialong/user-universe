package com.decade.usercenter.exception;

import com.decade.usercenter.common.BaseResponse;
import com.decade.usercenter.common.ErrorCode;
import com.decade.usercenter.common.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * @author hailong
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse bussinessHandler(BusinessException e) {
        log.error("Business Exception" + e.getMessage(), e);
        return ResponseUtil.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse bussinessHandler(RuntimeException e) {
        log.error("Runtime Exception", e);
        return ResponseUtil.error(ErrorCode.INTERNAL_ERROR, e.getMessage());
    }
}
