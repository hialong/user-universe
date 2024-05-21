package com.decade.usercenter.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.decade.hapicommon.model.domain.UserInterfaceInfo;

import java.util.List;

/**
 * @Entity com.decade.usercenter.model.domain.UserInterfaceInfo
 */
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

   List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);
}




