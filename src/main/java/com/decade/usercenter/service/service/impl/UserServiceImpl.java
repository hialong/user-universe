package com.decade.usercenter.service.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.decade.usercenter.constant.UserConstant;
import com.decade.usercenter.model.domain.User;
import com.decade.usercenter.service.service.UserService;
import com.decade.usercenter.mapper.UserMapper;

import com.decade.usercenter.utils.UserUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
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

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword,String specialCode) {
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
        //邀请码功能待完善，反向推导邀请用户，然后给邀请用户加分或者记录,注册成功后再操作
        if(!specialCode.equals(UserConstant.NORMAL_SPECIAL_CODE)){
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("specialCode",specialCode);
            // 注意这里要保证只查到一个，查到多个默认拿第一个
            List<User> users = userMapper.selectList(queryWrapper);
            if (users.size() != 1){
                log.error("邀请码错误");
                return -1;
            }
            User inviteUser = users.get(0);
            UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
            userUpdateWrapper.eq("id",inviteUser.getId());
            userUpdateWrapper.set("score",inviteUser.getScore()+1);
            update(userUpdateWrapper);
        }
        return user.getId();
    }

    @Override
    public User doLoginIn(String userAccount, String userPassword, HttpServletRequest request) {
        // 1.校验
        if (StringUtils.isAnyBlank(userAccount, userAccount)) {
            // 修改为异常逻辑
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
        User safetyUser = UserUtil.getSafeUser(user);
        // 记录用户登录状态
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, safetyUser);
        return safetyUser;

    }

    @Override
    public List<User> queryUser(String userName) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        // 模糊查询
        if (StringUtils.isNotBlank(userName)) {
            userQueryWrapper.like("userName", userName);
        }
        return userMapper.selectList(userQueryWrapper);
    }

    @Override
    public int userLogOut(HttpServletRequest request) {
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return 1;
    }
}




