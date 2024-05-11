package com.decade.usercenter.controller;


import com.decade.usercenter.annotation.CheckAuth;
import com.decade.usercenter.common.ResponseUtil;
import com.decade.usercenter.constant.UserConstant;
import com.decade.usercenter.model.dto.InterfaceInfo.InterfaceInfoAddRequest;
import com.decade.usercenter.model.dto.InterfaceInfo.InterfaceInfoUpdateRequest;
import com.decade.usercenter.service.InterfaceInfoService;
import com.decade.usercenter.service.UserService;
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
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

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
    private InterfaceInfoService interfaceIndoService;

    @Resource
    private UserService userService;

    private final static Gson GSON = new Gson();

    // region 增删改查

    /**
     * 创建
     *
     * @param interfaceIndoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceIndoAddRequest,
                                               HttpServletRequest request) {
        if (interfaceIndoAddRequest == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceIndoAddRequest, interfaceInfo);
        //后面优化就是写一个类似这个方法进行验证 interfaceIndoService.validInterfaceInfo(interfaceInfo, true);
        User loginUser = userService.getCurrentUser(request);
        interfaceInfo.setUserId(loginUser.getId());
        boolean result = interfaceIndoService.save(interfaceInfo);
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
        InterfaceInfo oldInterfaceInfo = interfaceIndoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        boolean b = interfaceIndoService.removeById(id);
        return ResponseUtil.ok(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param interfaceIndoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @CheckAuth(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest
     interfaceIndoUpdateRequest) {
        if (interfaceIndoUpdateRequest == null || interfaceIndoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceIndoUpdateRequest, interfaceInfo);
        long id = interfaceIndoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceIndoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = interfaceIndoService.updateById(interfaceInfo);
        return ResponseUtil.ok(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    //@GetMapping("/get/vo")
    //public BaseResponse<InterfaceInfoVO> getInterfaceInfoVOById(long id, HttpServletRequest request) {
    //    if (id <= 0) {
    //        throw new BusinessException(ErrorCode.INVALID_PARAMS);
    //    }
    //    InterfaceInfo interfaceInfo = interfaceIndoService.getById(id);
    //    if (interfaceInfo == null) {
    //        throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
    //    }
    //    return ResultUtils.success(interfaceIndoService.getInterfaceInfoVO(interfaceInfo, request));
    //}
    //
    ///**
    // * 分页获取列表（封装类）
    // *
    // * @param interfaceIndoQueryRequest
    // * @param request
    // * @return
    // */
    //@PostMapping("/list/page/vo")
    //public BaseResponse<Page<InterfaceInfoVO>> listInterfaceInfoVOByPage(@RequestBody InterfaceInfoQueryRequest
    // interfaceIndoQueryRequest,
    //        HttpServletRequest request) {
    //    long current = interfaceIndoQueryRequest.getCurrent();
    //    long size = interfaceIndoQueryRequest.getPageSize();
    //    // 限制爬虫
    //    ThrowUtils.throwIf(size > 20, ErrorCode.INVALID_PARAMS);
    //    Page<InterfaceInfo> interfaceIndoPage = interfaceIndoService.page(new Page<>(current, size),
    //            interfaceIndoService.getQueryWrapper(interfaceIndoQueryRequest));
    //    return ResultUtils.success(interfaceIndoService.getInterfaceInfoVOPage(interfaceIndoPage, request));
    //}
    //
    ///**
    // * 分页获取当前用户创建的资源列表
    // *
    // * @param interfaceIndoQueryRequest
    // * @param request
    // * @return
    // */
    //@PostMapping("/my/list/page/vo")
    //public BaseResponse<Page<InterfaceInfoVO>> listMyInterfaceInfoVOByPage(@RequestBody InterfaceInfoQueryRequest
    // interfaceIndoQueryRequest,
    //        HttpServletRequest request) {
    //    if (interfaceIndoQueryRequest == null) {
    //        throw new BusinessException(ErrorCode.INVALID_PARAMS);
    //    }
    //    User loginUser = userService.getCurrentUser(request);
    //    interfaceIndoQueryRequest.setUserId(loginUser.getId());
    //    long current = interfaceIndoQueryRequest.getCurrent();
    //    long size = interfaceIndoQueryRequest.getPageSize();
    //    // 限制爬虫
    //    ThrowUtils.throwIf(size > 20, ErrorCode.INVALID_PARAMS);
    //    Page<InterfaceInfo> interfaceIndoPage = interfaceIndoService.page(new Page<>(current, size),
    //            interfaceIndoService.getQueryWrapper(interfaceIndoQueryRequest));
    //    return ResultUtils.success(interfaceIndoService.getInterfaceInfoVOPage(interfaceIndoPage, request));
    //}
    //
    //// endregion
    //
    ///**
    // * 分页搜索（从 ES 查询，封装类）
    // *
    // * @param interfaceIndoQueryRequest
    // * @param request
    // * @return
    // */
    //@PostMapping("/search/page/vo")
    //public BaseResponse<Page<InterfaceInfoVO>> searchInterfaceInfoVOByPage(@RequestBody InterfaceInfoQueryRequest
    // interfaceIndoQueryRequest,
    //        HttpServletRequest request) {
    //    long size = interfaceIndoQueryRequest.getPageSize();
    //    // 限制爬虫
    //    ThrowUtils.throwIf(size > 20, ErrorCode.INVALID_PARAMS);
    //    Page<InterfaceInfo> interfaceIndoPage = interfaceIndoService.searchFromEs(interfaceIndoQueryRequest);
    //    return ResultUtils.success(interfaceIndoService.getInterfaceInfoVOPage(interfaceIndoPage, request));
    //}
    //
    ///**
    // * 编辑（用户）
    // *
    // * @param interfaceIndoEditRequest
    // * @param request
    // * @return
    // */
    //@PostMapping("/edit")
    //public BaseResponse<Boolean> editInterfaceInfo(@RequestBody InterfaceInfoEditRequest interfaceIndoEditRequest,
    // HttpServletRequest request) {
    //    if (interfaceIndoEditRequest == null || interfaceIndoEditRequest.getId() <= 0) {
    //        throw new BusinessException(ErrorCode.INVALID_PARAMS);
    //    }
    //    InterfaceInfo interfaceInfo = new InterfaceInfo();
    //    BeanUtils.copyProperties(interfaceIndoEditRequest, interfaceInfo);
    //    List<String> tags = interfaceIndoEditRequest.getTags();
    //    if (tags != null) {
    //        interfaceInfo.setTags(GSON.toJson(tags));
    //    }
    //    // 参数校验
    //    interfaceIndoService.validInterfaceInfo(interfaceInfo, false);
    //    User loginUser = userService.getCurrentUser(request);
    //    long id = interfaceIndoEditRequest.getId();
    //    // 判断是否存在
    //    InterfaceInfo oldInterfaceInfo = interfaceIndoService.getById(id);
    //    ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
    //    // 仅本人或管理员可编辑
    //    if (!oldInterfaceInfo.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
    //        throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
    //    }
    //    boolean result = interfaceIndoService.updateById(interfaceInfo);
    //    return ResultUtils.success(result);
    //}

}
