package com.decade.usercenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.decade.hapicommon.model.domain.User;


import java.util.List;

/**
 * @Entity generator.domain.User
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 模糊查询user
     * @param user user
     * @return userList
     */

    List<User> findUserByPage(User user);
}




