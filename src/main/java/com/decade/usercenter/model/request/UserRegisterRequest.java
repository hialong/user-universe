package com.decade.usercenter.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户注册请求
 *
 * @author hailong
 *
 */
@Data
public class UserRegisterRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -7111418039613087819L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;
}
