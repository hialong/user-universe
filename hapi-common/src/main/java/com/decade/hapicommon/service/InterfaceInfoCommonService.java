package com.decade.hapicommon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.decade.hapicommon.model.domain.InterfaceInfo;


/**
 *
 */
public interface InterfaceInfoCommonService {

    /**
     * 根据url和method获取接口信息
     * @param url
     * @param method
     * @return
     */
    InterfaceInfo getInterfaceInfo(String url, String method);

}
