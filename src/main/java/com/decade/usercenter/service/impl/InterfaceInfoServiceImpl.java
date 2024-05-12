package com.decade.usercenter.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.decade.usercenter.common.ErrorCode;
import com.decade.usercenter.constant.CommonConstant;
import com.decade.usercenter.exception.BusinessException;
import com.decade.usercenter.exception.ThrowUtils;
import com.decade.usercenter.mapper.InterfaceInfoMapper;
import com.decade.usercenter.model.domain.InterfaceInfo;
import com.decade.usercenter.model.dto.InterfaceInfo.InterfaceInfoQueryRequest;
import com.decade.usercenter.service.InterfaceInfoService;

import com.decade.usercenter.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService {


    public static final int MAX_REQUEST_NUMBER  = 500;

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if(interfaceInfo == null){
            throw new BusinessException(ErrorCode.INVALID_PARAMS);
        }
        String name = interfaceInfo.getName();
        String description = interfaceInfo.getDescription();
        String url = interfaceInfo.getUrl();
        Integer status = interfaceInfo.getStatus();
        String operationType = interfaceInfo.getOperationType();
        // 创建时参数不能为空
        if(add){
            ThrowUtils.throwIf(StringUtils.isAnyBlank(name,url,operationType),ErrorCode.INVALID_PARAMS);
        }
        //参数校验 status目前就两个状态，一个是0关闭，一个是1打开，其他的状态不能传入
        ThrowUtils.throwIf(status != 0 && status != 1,ErrorCode.INVALID_PARAMS,"status参数非法");
        if(description.length()>MAX_REQUEST_NUMBER){
            throw new BusinessException(ErrorCode.INVALID_PARAMS,"description参数过长");
        }
    }

    @Override
    public QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        if(interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        Long id = interfaceInfoQueryRequest.getId();
        String name = interfaceInfoQueryRequest.getName();
        String description = interfaceInfoQueryRequest.getDescription();
        String url = interfaceInfoQueryRequest.getUrl();
        String requestHeader = interfaceInfoQueryRequest.getRequestHeader();
        String responseHeader = interfaceInfoQueryRequest.getResponseHeader();
        Integer status = interfaceInfoQueryRequest.getStatus();
        String operationType = interfaceInfoQueryRequest.getOperationType();
        Long userId = interfaceInfoQueryRequest.getUserId();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        // 拼接查询条件
        queryWrapper.eq(id != null && id > 0, "id", id);
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.like(StringUtils.isNotBlank(url), "url", url);
        queryWrapper.like(StringUtils.isNotBlank(requestHeader), "requestHeader", requestHeader);
        queryWrapper.like(StringUtils.isNotBlank(responseHeader), "responseHeader", responseHeader);
        queryWrapper.eq(status != null && status > 0, "status", status);
        queryWrapper.eq(StringUtils.isNotBlank(operationType), "operationType", operationType);
        // 只查询未删除的
        queryWrapper.eq("flag", 0);
        // 这里首先是判断sortFiled中是否有sql注入的情况，其次是是判断sortOrder是否是升序还是降序，
        // 然后给上排序字段，如果排序字段为空，那就默认排序 order by null 表示不排序
        queryWrapper.orderBy(SqlUtils.validateSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}




