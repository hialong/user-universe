package com.decade.usercenter.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.decade.usercenter.common.ErrorCode;
import com.decade.usercenter.constant.CommonConstant;
import com.decade.usercenter.exception.BusinessException;
import com.decade.usercenter.exception.ThrowUtils;
import com.decade.usercenter.model.domain.InterfaceInfo;
import com.decade.usercenter.model.domain.UserInterfaceInfo;
import com.decade.usercenter.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import com.decade.usercenter.service.UserInterfaceInfoService;
import com.decade.usercenter.mapper.UserInterfaceInfoMapper;
import com.decade.usercenter.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService{

    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if(userInterfaceInfo == null){
            throw new BusinessException(ErrorCode.INVALID_PARAMS);
        }
        Long userId = userInterfaceInfo.getUserId();
        Long interfaceInfoId = userInterfaceInfo.getInterfaceInfoId();
        Integer totalNum = userInterfaceInfo.getTotalNum();
        Integer leftNum = userInterfaceInfo.getLeftNum();
        Integer status = userInterfaceInfo.getStatus();
        if(status == 0){
            throw new BusinessException(ErrorCode.INVALID_PARAMS,"封禁状态无法修改");
        }
        if(interfaceInfoId<0||leftNum<0||totalNum<0){
            throw new BusinessException(ErrorCode.INVALID_PARAMS,"参数错误");
        }
        // 创建时参数不能为空
        if(add){
            if(userId==null||userId<0){
                throw new BusinessException(ErrorCode.INVALID_PARAMS,"当前用户不存在");
            }
        }
    }

    @Override
    public QueryWrapper<UserInterfaceInfo> getQueryWrapper(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        if(userInterfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        Long id = userInterfaceInfoQueryRequest.getId();
        Long userId = userInterfaceInfoQueryRequest.getUserId();
        Long interfaceInfoId = userInterfaceInfoQueryRequest.getInterfaceInfoId();
        Integer totalNum = userInterfaceInfoQueryRequest.getTotalNum();
        Integer leftNum = userInterfaceInfoQueryRequest.getLeftNum();
        Integer status = userInterfaceInfoQueryRequest.getStatus();
        String sortField = userInterfaceInfoQueryRequest.getSortField();
        String sortOrder = userInterfaceInfoQueryRequest.getSortOrder();
        // 拼接查询条件
        queryWrapper.eq(id != null && id > 0, "id", id);
        queryWrapper.eq(userId != null && userId > 0, "userId", userId);
        queryWrapper.eq(interfaceInfoId != null && interfaceInfoId > 0, "interfaceInfoId", interfaceInfoId);
        queryWrapper.eq(totalNum != null && totalNum > 0, "totalNum", totalNum);
        queryWrapper.eq(leftNum != null && leftNum > 0, "leftNum", leftNum);
        queryWrapper.eq(status != null && status > 0, "status", status);
        // 只查询未删除的
        queryWrapper.eq("flag", 0);
        // 这里首先是判断sortFiled中是否有sql注入的情况，其次是是判断sortOrder是否是升序还是降序，
        // 然后给上排序字段，如果排序字段为空，那就默认排序 order by null 表示不排序
        queryWrapper.orderBy(SqlUtils.validateSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 调用次数增加
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        if (interfaceInfoId < 0 || userId < 0){
            throw new BusinessException(ErrorCode.INVALID_PARAMS);
        }
        //todo 后续优化一下invoke检测能力，比如同时调用的分布式锁等，可以尝试使用虚拟线程
        //通过usrId查当前用户的leftNum。如果小于等于0，则不允许调用。抛出异常
        UserInterfaceInfo userInterfaceInfo = this.lambdaQuery()
                .eq(UserInterfaceInfo::getUserId, userId)
                .eq(UserInterfaceInfo::getInterfaceInfoId, interfaceInfoId)
                .one();
        ThrowUtils.throwIf(userInterfaceInfo == null || userInterfaceInfo.getLeftNum() <= 0, ErrorCode.INVALID_PARAMS
                , "调用接口不存在或者用户剩余调用次数不足");
        return this.update()
                .setSql("leftNum = leftNum - 1,totalNum = totalNum + 1")
                .eq("interfaceInfoId", interfaceInfoId)
                .eq("userId", userId)
                .gt("leftNum", 0)
                .update();
    }
}




