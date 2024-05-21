package com.decade.usercenter.model.vo;

import com.decade.hapicommon.model.domain.InterfaceInfo;
import lombok.Data;

@Data
public class InterfaceInfoVo extends InterfaceInfo {
    /**
     * 调用次数
     */
    private Integer totalNum;
    private static final long serialVersionUID = 1L;

}
