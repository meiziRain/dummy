package com.meizi.admin.service.impl;

import com.meizi.admin.entity.TSysPermission;
import com.meizi.admin.entity.TSysRole;
import com.meizi.admin.entity.TSysUser;
import com.meizi.admin.mapper.TSysPermissionMapper;
import com.meizi.admin.service.ITSysPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限信息表 服务实现类
 * </p>
 *
 * @author meizi
 * @since 2020-01-07
 */
@Service
public class TSysPermissionServiceImpl extends ServiceImpl<TSysPermissionMapper, TSysPermission> implements ITSysPermissionService {

    /**
     * 注意:这里需要根据role去查询一遍permissions信息。
     * 现在role.getPermissions是没有数据的
     *
     * @param user
     * @return
     */
    @Override
    public Collection<GrantedAuthority> mapToGrantedAuthorities(TSysUser user) {
        List<TSysRole> roles = user.getRoles();
        return roles.stream().flatMap(role -> findByRoleId(role.getId()).stream())
                .map(permission -> new SimpleGrantedAuthority(permission.getPermissionName()))
                .collect(Collectors.toList());
    }

    /**
     * 根据角色ID查询所拥有的权限信息
     * @param roleId
     * @return
     */
    @Override
    public List<TSysPermission> findByRoleId(long roleId) {
        return baseMapper.findByRoleId(roleId);
    }

}
