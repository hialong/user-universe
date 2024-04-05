package com.decade.usercenter.controller;

import com.decade.usercenter.constant.UserConstant;
import com.decade.usercenter.model.domain.User;
import com.decade.usercenter.model.request.UserLoginRequest;
import com.decade.usercenter.model.request.UserRegisterRequest;
import com.decade.usercenter.service.service.UserService;
import com.decade.usercenter.utils.UserUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * restController返回值默认为json，用户接口
 *
 * @author hailong
 */
@RestController
@Slf4j
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
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        return userService.userRegister(userAccount, userPassword, checkPassword);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 登录请求
     * @param request          http请求
     * @return 返回脱敏登录用户
     */
    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        return userService.doLoginIn(userAccount, userPassword, request);
    }

    /**
     * 模糊查询
     *
     * @param userName userName
     * @return 用户列表
     */
    @GetMapping("/query")
    public List<User> queryUser(String userName, HttpServletRequest request) {
        if (!checkAuth(request, null)) {
            log.info("实际上应该check里面报异常，后续修改");
            return new ArrayList<>();
        }
        // 查询数据应当脱敏,暂时这么写，后续有其他敏感信息要写工具类
        List<User> users = userService.queryUser(userName);
        return users.stream().map(UserUtil::getSafeUser).collect(Collectors.toList());
    }

    /**
     * 删除用户,逻辑删除
     *
     * @param id 用户id
     * @return 是否删除成功
     */
    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long id, HttpServletRequest request) {
        if (id <= 0 || !checkAuth(request, null)) {
            return false;
        }
        return userService.removeById(id);
    }

    /**
     * 用户鉴权，后续可以通过数据库控参修改成不同接口不同权限
     *
     * @param request   请求
     * @param commandId 接口的权限控制参数，没有控参之前默认null就行
     * @return 是否鉴权通过
     */
    public boolean checkAuth(HttpServletRequest request, String commandId) {
        if (StringUtils.isEmpty(commandId)) {
            log.info("There is currently no control parameter configuration and will be added later.");
        }
        // null强转User不会报错，所以问题不大
        User userObj = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if(ObjectUtils.isEmpty(userObj)){
            return false;
        }
        if(userObj.getUserRole().equals(UserConstant.ADMIN_ROLE) || userObj.getUserRole().equals(UserConstant.SUPER_ADMIN_ROLE)){
            return true;
        }
        return false;
    }
}
