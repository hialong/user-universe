package com.decade.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.decade.hapicommon.model.domain.InterfaceInfo;
import com.decade.hapicommon.model.domain.UserInterfaceInfo;
import com.decade.usercenter.annotation.CheckAuth;
import com.decade.usercenter.common.BaseResponse;
import com.decade.usercenter.common.ErrorCode;
import com.decade.usercenter.common.ResponseUtil;
import com.decade.usercenter.constant.UserConstant;
import com.decade.usercenter.exception.BusinessException;
import com.decade.usercenter.mapper.UserInterfaceInfoMapper;
import com.decade.usercenter.model.vo.InterfaceInfoVo;
import com.decade.usercenter.service.InterfaceInfoService;
import com.decade.usercenter.service.UserInterfaceInfoService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @GetMapping("/top/interface/invokeSum")
    @CheckAuth(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<InterfaceInfoVo>> topInterfaceInvokeSum() {
        //获取top3使用率的接口
        List<UserInterfaceInfo> userInterfaceInfos = userInterfaceInfoMapper.listTopInvokeInterfaceInfo(3);
        if(userInterfaceInfos == null){
            return ResponseUtil.ok(new ArrayList<>());
        }
        //将获取到的接口数据分组。变成前面是接口id，后面是接口调用的接口list
        Map<Long, List<UserInterfaceInfo>> collect = userInterfaceInfos.stream().collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));
        QueryWrapper<InterfaceInfo> interfaceInfoQueryWrapper = new QueryWrapper<>();
        interfaceInfoQueryWrapper.in("id",collect.keySet());
        List<InterfaceInfo> interfaceInfos = interfaceInfoService.list(interfaceInfoQueryWrapper);

        if(interfaceInfos == null){
            throw new BusinessException(ErrorCode.INVALID_PARAMS,"数据库中存在脏数据");
        }
        //拿到数据以后封装成vo对象
        List<InterfaceInfoVo> interfaceInfoVoList = interfaceInfos.stream().map(interfaceInfo -> {
            InterfaceInfoVo interfaceInfoVo = new InterfaceInfoVo();
            BeanUtils.copyProperties(interfaceInfo, interfaceInfoVo);
            interfaceInfoVo.setTotalNum(collect.get(interfaceInfo.getId()).get(0).getTotalNum());
            return interfaceInfoVo;
        }).collect(Collectors.toList());
        return ResponseUtil.ok(interfaceInfoVoList);
    }

}
