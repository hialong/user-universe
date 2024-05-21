package com.decade.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.decade.hapicommon.model.domain.User;
import com.github.pagehelper.PageInfo;
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
     * 获取当前登录用户
     * @param request 请求
     * @return 当前登录用户
     */
    User getCurrentUser(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param request 请求
     * @return 是否管理员
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param user 用户信息
     * @return 是否管理员
     */
    boolean isAdmin(User user);

    /**
     * 用户登出
     * @param request 请求
     */
    int userlogout(HttpServletRequest request);

    /**
     * 分页查询
     * @param user 用户信息
     * @param pageNum 页码
     * @param pageSize 页面大小
     * @return 分页信息
     */
    PageInfo<User> queryUserByPage(User user, Integer pageNum, Integer pageSize);
}
