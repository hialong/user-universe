package com.decade.usercenter.model.dto.InterfaceInfo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.decade.usercenter.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询请求
 * @author hailong
 */
@TableName(value ="interface_info")
@Data
public class InterfaceInfoQueryRequest extends PageRequest implements Serializable {

    /**
     * 主键
     */
    private Long id;

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
     * 接口状态（0-关闭，1-开启）
     */
    private Integer status;

    /**
     * 请求类型
     */
    private String operationType;


    private static final long serialVersionUID = 1L;

}