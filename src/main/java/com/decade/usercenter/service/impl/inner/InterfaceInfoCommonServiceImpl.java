package com.decade.usercenter.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.decade.hapicommon.model.domain.InterfaceInfo;
import com.decade.hapicommon.service.InterfaceInfoCommonService;
import com.decade.usercenter.common.ErrorCode;
import com.decade.usercenter.exception.BusinessException;
import com.decade.usercenter.mapper.InterfaceInfoMapper;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;


/**
 *
 */
@DubboService
public class InterfaceInfoCommonServiceImpl implements InterfaceInfoCommonService {

    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public InterfaceInfo getInterfaceInfo(String url, String method) {
       if(StringUtils.isAnyBlank(url,method)){
           throw new BusinessException(ErrorCode.INVALID_PARAMS);
       }
        QueryWrapper<InterfaceInfo> interfaceInfoQueryWrapper = new QueryWrapper<>();
       interfaceInfoQueryWrapper.eq("url",url);
       interfaceInfoQueryWrapper.eq("operationType",method);
       return interfaceInfoMapper.selectOne(interfaceInfoQueryWrapper);
    }
}




