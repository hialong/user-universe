package com.decade.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 一般的删除请求
 * @author hailong
 */
@Data
public class DeleteRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
