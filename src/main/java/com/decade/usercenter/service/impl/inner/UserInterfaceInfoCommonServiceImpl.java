package com.decade.usercenter.service.impl.inner;

import com.decade.hapicommon.model.domain.InterfaceInfo;
import com.decade.hapicommon.model.domain.UserInterfaceInfo;
import com.decade.hapicommon.service.UserInterfaceInfoCommonService;
import com.decade.usercenter.common.ErrorCode;
import com.decade.usercenter.exception.BusinessException;
import com.decade.usercenter.exception.ThrowUtils;
import com.decade.usercenter.mapper.InterfaceInfoMapper;
import com.decade.usercenter.service.UserInterfaceInfoService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;


@DubboService
public class UserInterfaceInfoCommonServiceImpl implements UserInterfaceInfoCommonService {

    @Resource
    UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    InterfaceInfoMapper interfaceInfoMapper;

    /**
     * 调用次数增加
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
    }

    @Override
    public UserInterfaceInfo getUserInterfaceInfo(long interfaceInfoId, long userId) {
        //通过usrId查当前用户的leftNum。如果小于等于0，则不允许调用。抛出异常
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.lambdaQuery()
                .eq(UserInterfaceInfo::getUserId, userId)
                .eq(UserInterfaceInfo::getInterfaceInfoId, interfaceInfoId)
                .one();
        //这段逻辑是如果为null的话，则我们往数据库里面插入一条信息，leftNum给50次
        if(userInterfaceInfo == null){
            InterfaceInfo interfaceInfo = interfaceInfoMapper.selectById(interfaceInfoId);
            if(interfaceInfo == null){
                throw new BusinessException(ErrorCode.INVALID_PARAMS, "接口不存在");
            }
            userInterfaceInfo = new UserInterfaceInfo();
            userInterfaceInfo.setInterfaceInfoId(interfaceInfoId);
            userInterfaceInfo.setUserId(userId);
            userInterfaceInfo.setLeftNum(50);
            userInterfaceInfoService.save(userInterfaceInfo);
        }
        return userInterfaceInfo;
    }
}

