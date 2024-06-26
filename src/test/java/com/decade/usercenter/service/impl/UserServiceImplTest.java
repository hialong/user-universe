package com.decade.usercenter.service.impl;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.decade.hapicommon.model.domain.User;
import com.decade.usercenter.service.UserService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 用户测试服务
 *
 * @author hailong
 */
@SpringBootTest
public class UserServiceImplTest {

    @Resource
    private UserService userService;

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setUserName("admin");
        user.setUserAccount("admin");
        user.setAvatarUrl("https://profile-avatar.csdnimg.cn/dfd1762b2e5f45b78b0db77e95a64301_weixin_44056920.jpg!1");
        user.setGender(0);
        user.setUserPassword("admin");
        user.setPhone("admin123");
        user.setEmail("admin@qq.com");
        boolean save = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(save);
    }

    @Test
    void testRegex() {
        String validPattern = "^[a-zA-Z0-9_]+$";
        Matcher matcher = Pattern.compile(validPattern).matcher("Chl");
        Assertions.assertTrue(matcher.find());
        matcher = Pattern.compile(validPattern).matcher("C h");
        Assertions.assertFalse(matcher.find());
    }

    @Test
    void testQueryUser() {
        User user = new User();
        user.setUserName("123");
        user.setUserStatus(0);
        PageInfo<User> userPageInfo = userService.queryUserByPage(user, 0, 10);
        System.out.println(userPageInfo);
    }

    @Test
    void userRegister() {
        String userAccount = "hailong";
        String userPassword = "";
        String checkPassword = "12345678";
        try {
            long result = userService.userRegister(userAccount, userPassword, checkPassword,"0");
            Assertions.assertEquals(-1, result);
            userAccount = "c";
            result = userService.userRegister(userAccount, userPassword, checkPassword,"0");
            Assertions.assertEquals(-1, result);
            userPassword = "1234567";
            checkPassword = "1234567";
            result = userService.userRegister(userAccount, userPassword, checkPassword,"0");
            Assertions.assertEquals(-1, result);
            userPassword = "12345678";
            checkPassword = "12345678";
            userAccount = "admin";
            result = userService.userRegister(userAccount, userPassword, checkPassword,"0");
            Assertions.assertEquals(-1, result);
            userAccount = "c   h  l";
            result = userService.userRegister(userAccount, userPassword, checkPassword,"0");
            Assertions.assertEquals(-1, result);
            checkPassword = "123456789";
            result = userService.userRegister(userAccount, userPassword, checkPassword,"0");
            Assertions.assertEquals(-1, result);
            checkPassword = "12345678";
            userAccount = "hailong111"+ UUID.randomUUID();
            result = userService.userRegister(userAccount, userPassword, checkPassword,"0");
            Assertions.assertTrue(result > 0);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}