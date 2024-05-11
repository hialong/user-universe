package com.decade.usercenter.controller;


import com.decade.usercenter.annotation.CheckAuth;
import com.decade.usercenter.common.BaseResponse;
import com.decade.usercenter.common.ErrorCode;
import com.decade.usercenter.common.ResponseUtil;
import com.decade.usercenter.constant.UserConstant;
import com.decade.usercenter.exception.BusinessException;
import com.decade.usercenter.model.domain.User;
import com.decade.usercenter.model.request.UserLoginRequest;
import com.decade.usercenter.model.request.UserRegisterRequest;
import com.decade.usercenter.service.UserService;
import com.decade.usercenter.utils.UserUtil;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * restController返回值默认为json，用户接口
 *
 * @author hailong
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 注册请求
     *
     * @param userRegisterRequest 用户注册请求
     * @return 注册成功用户id
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.NULL_PARAMS, "null request");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String specialCode = userRegisterRequest.getSpecialCode();
        if (specialCode == null) {
            specialCode = UserConstant.NORMAL_SPECIAL_CODE;
        }
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        Long result = userService.userRegister(userAccount, userPassword, checkPassword, specialCode);
        return ResponseUtil.ok(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 登录请求
     * @param request          http请求
     * @return 返回脱敏登录用户
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.NULL_PARAMS, "null request");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.NULL_PARAMS);
        }
        User user = userService.doLoginIn(userAccount, userPassword, request);
        return ResponseUtil.ok(user);
    }

    /**
     * 用户登出
     *
     * @param request http请求
     * @return 用户登出状态，1正常登出
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogOut(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NULL_PARAMS, "null request");
        }
        int i = userService.userlogout(request);
        return ResponseUtil.ok(i);
    }

    /**
     * 模糊查询
     *
     * @param userName userName
     * @param request  http请求
     * @return 用户列表
     */
    @GetMapping("/query")
    @CheckAuth(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<User>> queryUser(String userName, HttpServletRequest request) {
        // 查询数据应当脱敏,暂时这么写，后续有其他敏感信息要写工具类
        List<User> users = userService.queryUser(userName);
        List<User> collect = users.stream().map(UserUtil::getSafeUser).collect(Collectors.toList());
        return ResponseUtil.ok(collect);
    }

    /**
     * 分页查询
     *
     * @param user     user
     * @param pageNum  页码
     * @param pageSize 页面大小
     * @param request  请求（用来鉴权）
     * @return 分页对象
     */
    @PostMapping("/queryByPage")
    @CheckAuth(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<PageInfo<User>> queryUserByPage(@RequestBody User user,
                                                        @RequestParam("pageNum") Integer pageNum,
                                                        @RequestParam("pageSize") Integer pageSize,
                                                        HttpServletRequest request) {
        PageInfo<User> userByPage = userService.queryUserByPage(user, pageNum, pageSize);
        return ResponseUtil.ok(userByPage);
    }


    /**
     * 更新用户信息
     *
     * @param user     用户信息
     * @param request  请求
     * @return 是否更新成功
     */
    @PostMapping("/updateUser")
    @CheckAuth(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody User user, HttpServletRequest request) {
        if (user == null) {
            throw new BusinessException(ErrorCode.NULL_PARAMS, "null request");
        }
        boolean updated = userService.updateById(user);
        return ResponseUtil.ok(updated);
    }

    /**
     * 删除用户,逻辑删除
     *
     * @param id 用户id
     * @param request 网络请求
     * @return 是否删除成功
     */
    @PostMapping("/deleteUser")
    @CheckAuth(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestParam("id") long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "invalid id {}", id);
        }
        Boolean removeFlag = userService.removeById(id);
        return ResponseUtil.ok(removeFlag);
    }

    /**
     * 获取用户信息
     * 实现：如果用户信息变更比较频繁则需要查表来确定用户信息
     * 另外要注意判断用户状态，后续如果有用户状态封号的，也要做相应的返回
     *
     * @param request 请求
     * @return 最新的用户信息
     */
    @GetMapping("/currentUser")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        return ResponseUtil.ok(userService.getCurrentUser(request));
    }


    ///**
    // * 用户鉴权，后续可以通过数据库控参修改成不同接口不同权限
    // *
    // * @param request   请求
    // * @param commandId 接口的权限控制参数，没有控参之前默认null就行
    // * @return 是否鉴权通过
    // */
    //public boolean checkAuth(HttpServletRequest request, String commandId) {
    //    if (StringUtils.isEmpty(commandId)) {
    //        log.info("There is currently no control parameter configuration and will be added later.");
    //    }
    //    // null强转User不会报错，所以问题不大
    //    User userObj = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
    //    if (ObjectUtils.isEmpty(userObj)) {
    //        return false;
    //    }
    //    if (userObj.getUserRole().toString().equals(UserConstant.ADMIN_ROLE) || userObj.getUserRole().toString().equals(UserConstant.SUPER_ADMIN_ROLE)) {
    //        return true;
    //    }
    //    return false;
    //}
}
