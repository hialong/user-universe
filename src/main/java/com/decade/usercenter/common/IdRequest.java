package com.decade.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 一般的id请求封装
 * @author hailong
 */
@Data
public class IdRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
