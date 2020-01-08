package com.meizi.admin.controller;

import cn.hutool.crypto.SecureUtil;
import com.meizi.admin.auth.DefaultJwtTokenUtil;
import com.meizi.admin.auth.DefaultJwtUserDetails;
import com.meizi.admin.entity.TSysUser;
import com.meizi.admin.model.Result;
import com.meizi.admin.security.model.AdminAuthorizationUser;
import com.meizi.admin.security.model.AuthenticationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: dummy
 * @description:
 * @author: Meizi
 * @create: 2020-01-06 15:53
 **/

@RestController
@RequestMapping("/auth")
public class AdminAuthenticationController {

    @Autowired
    private transient DefaultJwtTokenUtil jwtTokenUtil;

    @Autowired
    @Qualifier("adminJwtUserDetailsService")
    private transient UserDetailsService userDetailsService;

    @Autowired
    private transient PasswordEncoder passwordEncoder;

    /**
     * 登录授权
     *
     * @param authorizationUser
     * @return
     */
    @PostMapping(value = "/login")
    public Result login(@RequestBody AdminAuthorizationUser authorizationUser, HttpServletRequest request) {
        final DefaultJwtUserDetails jwtUser = (DefaultJwtUserDetails) userDetailsService.loadUserByUsername(authorizationUser.getUsername());
        if (!passwordEncoder.matches(authorizationUser.getPassword(), jwtUser.getPassword())) {
            throw new AccountExpiredException("密码错误");
        }

        if (!jwtUser.isEnabled()) {
            throw new AccountExpiredException("账号已停用，请联系管理员");
        }

        // 生成令牌
        final String token = jwtTokenUtil.generateToken(jwtUser);
        // 返回 token
        return Result.succeed(new AuthenticationInfo(token, jwtUser));
    }

    @PostMapping(value = "/hi")
    public Result sayHi() {
        return Result.succeed("Hi");
    }
}
