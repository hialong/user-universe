package com.decade.usercenter.model.dto.InterfaceInfo;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 一般的id请求封装
 * @author hailong
 */
@Data
@TableName(value = "interface_info")
public class InvokeRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 用户请求参数
     */
    private String userRequestParams;

    private static final long serialVersionUID = 1L;
}
