package com.decade.usercenter.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * user table
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * User id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * user name
     */
    private String userName;

    /**
     * user account
     */
    private String userAccount;

    /**
     * display avatar for user 
     */
    private String avatarUrl;

    /**
     * sex
     */
    private Integer gender;

    /**
     * password
     */
    private String userPassword;

    /**
     * subscriber identity
     */
    private String phone;

    /**
     * email address
     */
    private String email;

    /**
     * status 0 -normal
     */
    private Integer userStatus;

    /**
     * status 0 -normal 9-superAdmin
     */
    private Integer userRole;

    /**
     * 
     */
    private Date updateTime;

    /**
     * the record create time
     */
    private Date createTime;

    /**
     * isDelete
     */
    @TableLogic
    private Integer flag;

    /**
     * 特别编号，注册时使用，校验
     */
    private String specialCode;
    /**
     * 用户等级积分
     */
    private Long score;

    /**
     * 扩展字段
     */
    private String extend_1;

    /**
     * 扩展字段
     */
    private String extend_2;

    /**
     * 扩展字段
     */
    private String extend_3;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}