package com.decade.usercenter.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.decade.hapicommon.model.domain.User;
import com.decade.hapicommon.service.UserCommonService;
import com.decade.usercenter.common.ErrorCode;
import com.decade.usercenter.exception.BusinessException;
import com.decade.usercenter.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;



/**
 * 用户服务实现类
 *
 * @author hailong
 */
@DubboService
public class UserServiceCommonImpl extends ServiceImpl<UserMapper, User>
        implements UserCommonService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User getInvokeUser(String accessKey) {
        // 通过accessKey和secretKey查询用户
        if (StringUtils.isAnyBlank(accessKey)){
            throw new BusinessException(ErrorCode.INVALID_PARAMS);
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("accessKey", accessKey);

        return userMapper.selectOne(userQueryWrapper);
    }
}




