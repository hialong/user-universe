package com.decade.usercenter.utils;


import com.decade.hapicommon.model.domain.User;

public class UserUtil {

    /**
     * 获取用户脱敏数据
     * @return safeUser数据
     */
    public static User getSafeUser(User user){
        if(user == null){
            return null;
        }
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
        safetyUser.setUpdateTime(user.getUpdateTime());
        safetyUser.setAccessKey(user.getAccessKey());
        safetyUser.setUserRole(user.getUserRole());
        safetyUser.setSpecialCode(user.getSpecialCode());
        return safetyUser;
    }

}
