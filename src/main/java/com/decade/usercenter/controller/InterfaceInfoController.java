package com.decade.usercenter.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.decade.apiassignclientsdk.client.HapiClient;
import com.decade.apiassignclientsdk.model.Someting;
import com.decade.usercenter.annotation.CheckAuth;
import com.decade.usercenter.common.IdRequest;
import com.decade.usercenter.common.ResponseUtil;
import com.decade.usercenter.constant.UserConstant;
import com.decade.usercenter.enums.InterfaceInfoStatusEnum;
import com.decade.usercenter.model.dto.InterfaceInfo.InterfaceInfoAddRequest;
import com.decade.usercenter.model.dto.InterfaceInfo.InterfaceInfoQueryRequest;
import com.decade.usercenter.model.dto.InterfaceInfo.InterfaceInfoUpdateRequest;
import com.decade.usercenter.model.dto.InterfaceInfo.InvokeRequest;
import com.decade.usercenter.service.InterfaceInfoService;
import com.decade.usercenter.service.UserService;
import com.decade.usercenter.utils.UserUtil;
import com.google.gson.Gson;
import com.decade.usercenter.common.BaseResponse;
import com.decade.usercenter.common.DeleteRequest;
import com.decade.usercenter.common.ErrorCode;
import com.decade.usercenter.exception.BusinessException;
import com.decade.usercenter.exception.ThrowUtils;
import com.decade.usercenter.model.domain.InterfaceInfo;
import com.decade.usercenter.model.domain.User;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 接口信息
 *
 * @author hailong
 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private HapiClient hapiClient;
    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;

    private final static Gson GSON = new Gson();

    // region 增删改查

    /**
     * 创建
     *
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest,
                                               HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);
        interfaceInfoService.validInterfaceInfo(interfaceInfo, true);
        User loginUser = userService.getCurrentUser(request);
        interfaceInfo.setUserId(loginUser.getId());
        boolean result = interfaceInfoService.save(interfaceInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newInterfaceInfoId = interfaceInfo.getId();
        return ResponseUtil.ok(newInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest,
                                                     HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS);
        }
        User user = userService.getCurrentUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        boolean b = interfaceInfoService.removeById(id);
        return ResponseUtil.ok(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param interfaceInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @CheckAuth(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest
     interfaceInfoUpdateRequest) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() < 0) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
        // 参数校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, false);
        long id = interfaceInfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResponseUtil.ok(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResponseUtil.ok(interfaceInfo);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param interfaceInfoQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(@RequestBody InterfaceInfoQueryRequest
     interfaceInfoQueryRequest,
                                                                       HttpServletRequest request) {
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.INVALID_PARAMS);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size),
                interfaceInfoService.getQueryWrapper(interfaceInfoQueryRequest));
        return ResponseUtil.ok(interfaceInfoPage);
    }


    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param interfaceInfoQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page")
    public BaseResponse<Page<InterfaceInfo>> listMyInterfaceInfoByPage(@RequestBody InterfaceInfoQueryRequest
     interfaceInfoQueryRequest,
            HttpServletRequest request) {
        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS);
        }
        User loginUser = userService.getCurrentUser(request);
        interfaceInfoQueryRequest.setUserId(loginUser.getId());
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.INVALID_PARAMS);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size),
                interfaceInfoService.getQueryWrapper(interfaceInfoQueryRequest));
        return ResponseUtil.ok(interfaceInfoPage);
    }

    // endregion
    @PostMapping("/online")
    @CheckAuth(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() < 0) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS);
        }
        //校验该接口是否存在
        Long id = idRequest.getId();
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);

        //TODO 判断接口能否调用,后面改成按照地址去调用判断是不是404啥的
        String des = hapiClient.doSomething(new Someting("test", "test"));
        if(StringUtils.isBlank(des)){
            throw new BusinessException(ErrorCode.INTERNAL_ERROR,"接口调用不成功");
        }
        //更新状态
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResponseUtil.ok(result);
    }

    @PostMapping("/offline")
    @CheckAuth(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> offlineInterfaceInfo(@RequestBody IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() < 0) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS);
        }
        //校验该接口是否存在
        Long id = idRequest.getId();
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        //更新状态
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResponseUtil.ok(result);
    }

    /**
     * 用户调用接口
     * @param invokeRequest
     * @param request
     * @return
     */
    @PostMapping("/invoke")
    public BaseResponse<Object> invokeInterfaceInfo(@RequestBody InvokeRequest invokeRequest,HttpServletRequest request) {
        if (invokeRequest == null || invokeRequest.getId() < 0) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS);
        }
        //校验该接口是否存在
        Long id = invokeRequest.getId();
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null||oldInterfaceInfo.getStatus()==0, ErrorCode.NOT_FOUND_ERROR,"接口不存在或者接口已关闭");
        //todo: 简单模拟一下调用，后面再改成专门的调用方式,以及，getLoginUser方法
        User currentUser = userService.getCurrentUser(request);
        HapiClient hapiClient1 = new HapiClient("abcdefg", currentUser.getAccessKey());
        Gson gson = new Gson();
        Someting someting = gson.fromJson(invokeRequest.getUserRequestParams(), Someting.class);
        String result = hapiClient1.doSomething(someting);
        return ResponseUtil.ok(result);
    }

}
