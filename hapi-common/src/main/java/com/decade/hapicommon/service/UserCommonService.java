package com.decade.hapicommon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.decade.hapicommon.model.domain.User;

/**
 *
 */
public interface UserCommonService {

    /**
     * 根据访问密钥和密钥获取用户信息，判断用户是否拥有权限
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);


}
