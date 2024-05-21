package com.decade.hapicommon.service;


import com.decade.hapicommon.model.domain.UserInterfaceInfo;

/**
 *
 */
public interface UserInterfaceInfoCommonService{

    /**
     * 接口调用统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);

    /**
     * 获取接口调用次数,状态等信息
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    UserInterfaceInfo getUserInterfaceInfo(long interfaceInfoId, long userId);
}
