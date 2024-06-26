package com.decade.usercenter.constant;

public interface UserConstant {
    /**
     * session 存储user信息键值
     */
    String USER_LOGIN_STATE = "USER_LOGIN_STATE";

    String NORMAL_SPECIAL_CODE = "0";

    // ------------------------用户权限--------------------------------
    /**
     * 普通用户
     */
    String NORMAL_ROLE = "0";
    /**
     * 管理员用户
     */
    String ADMIN_ROLE = "1";
    /**
     * 超级管理员用户
     */
    String SUPER_ADMIN_ROLE = "9";

    String BAN = "-1";

}
