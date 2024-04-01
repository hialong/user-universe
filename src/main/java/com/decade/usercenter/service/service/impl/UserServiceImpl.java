package com.decade.usercenter.service.service.impl;

import java.util.Date;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.decade.usercenter.model.domain.User;
import com.decade.usercenter.service.service.UserService;
import com.decade.usercenter.mapper.mapper.UserMapper;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户服务实现类
 *
 * @author hailong
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;
    /**
     * 盐值
     */
    private static final String SALT = "abcd";
    private static final String USER_LOGIN_STATE = "USER_LOGIN_STATE";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1.校验
        if (StringUtils.isAnyBlank(userAccount, userAccount, checkPassword)) {
            return -1;
        }
        if (userAccount.length() < 4) {
            return -1;
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8 || !StringUtils.equals(userPassword,
                checkPassword)) {
            return -1;
        }
        // 正则过滤特殊字符
        String validPattern = "^[a-zA-Z0-9_]+$";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (!matcher.find()) {
            return -1;
        }
        // 账户不能重复 （后查表）
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userAccount", userAccount);
        long count = count(userQueryWrapper);
        if (count >= 1) {
            return -1;
        }
        // 密码加密
        String result = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        User user = new User();

        user.setUserAccount(userAccount);
        user.setUserPassword(result);
        boolean save = save(user);
        if (!save) {
            return -1;
        }
        return user.getId();
    }

    @Override
    public User doLoginIn(String userAccount, String userPassword, HttpServletRequest request) {
        // 1.校验
        if (StringUtils.isAnyBlank(userAccount, userAccount)) {
            // TODO 修改为异常逻辑
            return null;
        }
        if (userAccount.length() < 4) {
            return null;
        }
        if (userPassword.length() < 8) {
            return null;
        }
        // 正则过滤特殊字符
        String validPattern = "^[a-zA-Z0-9_]+$";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (!matcher.find()) {
            return null;
        }
        // 登录
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        userQueryWrapper.eq("userAccount", userAccount);
        userQueryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(userQueryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("login faild ,can not find account or password incorrect ");
            return null;
        }
        // 用户脱敏
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUserName(user.getUserName());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setCreateTime(user.getCreateTime());
        // 记录用户登录状态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;

    }
}




