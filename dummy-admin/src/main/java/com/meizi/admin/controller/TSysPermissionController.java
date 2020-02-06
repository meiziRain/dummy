package com.meizi.admin.controller;

import com.meizi.admin.auth.DefaultJwtUserDetails;
import com.meizi.admin.constants.AdminConstants;
import com.meizi.admin.entity.TSysPermission;
import com.meizi.admin.entity.TSysUser;
import com.meizi.admin.mapper.TSysPermissionMapper;
import com.meizi.admin.model.Result;
import com.meizi.admin.service.ITSysPermissionService;
import com.meizi.admin.service.ITSysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 权限信息表 前端控制器
 * </p>
 *
 * @author meizi
 * @since 2020-01-07
 */
@RestController
@RequestMapping("/permission")
public class TSysPermissionController {

    @Autowired
    ITSysPermissionService tSysPermissionService;

    @Autowired
    private ITSysUserService tSysUserService;

    @Resource
    private TSysPermissionMapper tSysPermissionMapper;

    /**
     * 返回全部的权限，新增角色时下拉选择
     *
     * @return
     */
    @PostMapping(value = "/tree")
    public Result getTree( DefaultJwtUserDetails userDetails) {
        Long operationId = userDetails.getId();
        if(AdminConstants.ADMIN_USER_ID.equals(operationId)){
            List<TSysPermission> tSysMenuList = tSysPermissionService.findByPid(AdminConstants.DEFAULT_PARENT_ID);
            return Result.succeed(tSysPermissionService.getPermissionTree(tSysMenuList));
        }else{
            TSysUser tSysUser = tSysUserService.findById(userDetails.getId());
            final List<TSysPermission> finalMenuList = new ArrayList<>();
            tSysUser.getRoles().forEach(tSysRole ->{
                finalMenuList.addAll(tSysPermissionMapper.findByRoleId(tSysRole.getId()));
            });
            return Result.succeed(tSysPermissionService.getMenuTreeByList(finalMenuList));
        }
    }

    @PostMapping(value = "/create")
    public Result create(DefaultJwtUserDetails userDetails, @Valid @Validated @RequestBody TSysPermission domain) {
        return tSysPermissionService.create(userDetails, domain);
    }

    @PostMapping(value = "/query")
    public Result getPermissions(@RequestBody TSysPermission tSysPermission) {
        List<TSysPermission> permissionDomains = tSysPermissionService.selectPermissionsByCondition(tSysPermission.getName());
        return Result.succeed(tSysPermissionService.buildTree(permissionDomains));
    }

    @PostMapping(value = "/update")
    public Result update(DefaultJwtUserDetails userDetails, @Valid @RequestBody TSysPermission domain) {
        return tSysPermissionService.update(userDetails, domain);
    }

    @PostMapping(value = "/delete")
    public Result delete(@RequestBody TSysPermission tSysPermission) {
        if (tSysPermissionService.delete(tSysPermission.getId())) {
            return Result.succeed("删除权限成功");
        }
        return Result.failed("删除权限失败");
    }
}
