package com.meizi.admin.security.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author jie
 * @date 2018-11-30
 */
@Getter
@Setter
public class AdminAuthorizationUser {

    /**
     * 用户名
     */
    @NotBlank
    private String username;

    /**
     * 密码
     */
    @NotBlank
    private String password;

    /**
     * 图形验证码
     */
    @NotBlank
    private String imageCode;

    @Override
    public String toString() {
        return "{username=" + username  + ", imageCode=" +imageCode + ", password= ******}";
    }
}
