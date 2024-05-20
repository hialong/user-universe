package com.decade.usercenter.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.decade.usercenter.model.domain.UserInterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.decade.usercenter.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;

/**
 *
 */
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {
    /**
     * 校验
     *
     * @param userInterfaceInfo
     * @param add
     */
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    /**
     * 获取查询条件
     *
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    QueryWrapper<UserInterfaceInfo> getQueryWrapper(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest);

    boolean invokeCount(long interfaceInfoId, long userId);
}
