package com.decade.usercenter.service.service;

import com.decade.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * 用户服务
 *
 * @author hailong
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return 新用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword,String specialCode);

    /**
     * 用户登录
     *
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @return user 用户对象
     */
    User doLoginIn(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 查询用户，模糊查询
     * @param userName userName
     * @return 用户列表
     */
    List<User> queryUser(String userName);

    /**
     * 用户登出
     * @param request 请求
     */
    int userlogout(HttpServletRequest request);
}
