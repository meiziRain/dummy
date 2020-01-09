package com.meizi.admin.controller;

import com.meizi.admin.constants.AdminConstants;
import com.meizi.admin.entity.TSysUser;
import com.meizi.admin.model.Result;
import com.meizi.admin.service.ITSysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 后台用户信息表 前端控制器
 * </p>
 *
 * @author meizi
 * @since 2020-01-06
 */
@RestController
@RequestMapping("/user")
public class TSysUserController {

    @Autowired
    private transient PasswordEncoder passwordEncoder;


    @Autowired
    private transient ITSysUserService tSysUserService;

    @PostMapping(value = "/query")
    public Result query(@RequestParam String username) {
        TSysUser tSysUser = tSysUserService.findByUserName(username);
        return Result.succeed(tSysUser);
    }

    @PostMapping(value = "/update")
    public Result update(@RequestBody TSysUser domain) {
        if (tSysUserService.updateByUser(domain)) {
            return Result.succeed("修改成功");
        } else {
            return Result.succeed("修改失败");
        }
    }

    @PostMapping(value = "/create")
    public Result create(@RequestBody TSysUser domain) {
        domain.setPassword(passwordEncoder.encode(AdminConstants.DEF_ADMIN_PASSWORD));
        if (tSysUserService.createUser(domain)) {
            return Result.succeed("创建成功");
        } else {
            return Result.succeed("创建失败");
        }
    }

    @PostMapping(value = "/delete")
    public Result delete(@RequestBody TSysUser domain) {
        if (tSysUserService.deleteUser(domain)) {
            return Result.succeed("删除成功");
        } else {
            return Result.succeed("删除失败");
        }
    }
}
