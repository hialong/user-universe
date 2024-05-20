package com.decade.usercenter.model.dto.InterfaceInfo;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


/**
 * 添加请求
 *
 * @author hailong
 */
@Data
public class InterfaceInfoAddRequest implements Serializable {

    /**
     * 用户名
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 请求类型
     */
    private String operationType;

    private static final long serialVersionUID = 1L;
}