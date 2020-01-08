package com.meizi.admin.service;

import com.meizi.admin.entity.TSysPermission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.meizi.admin.entity.TSysUser;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 权限信息表 服务类
 * </p>
 *
 * @author meizi
 * @since 2020-01-07
 */
public interface ITSysPermissionService extends IService<TSysPermission> {
    /**
     * 生成权限集合
     *
     * @param user
     * @return
     */
    Collection<GrantedAuthority> mapToGrantedAuthorities(TSysUser user);

    /**
     * 根据角色ID获取权限集合
     * @param roleId
     * @return
     */
    @Cacheable(key = "#p0")
    List<TSysPermission> findByRoleId(long roleId);
}
