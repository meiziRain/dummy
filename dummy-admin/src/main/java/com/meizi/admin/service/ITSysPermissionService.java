package com.meizi.admin.service;

import cn.hutool.db.PageResult;
import com.meizi.admin.auth.DefaultJwtUserDetails;
import com.meizi.admin.entity.TSysPermission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.meizi.admin.entity.TSysUser;
import com.meizi.admin.model.Result;
import org.springframework.cache.annotation.CacheEvict;
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
     * permission tree
     * @param permissions
     * @return
     */
    @Cacheable(key = "'tree'")
    Object getPermissionTree(List<TSysPermission> permissions);

    Object getMenuTreeByList(List<TSysPermission> menuDomains);

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


    /**
     * create
     * @param domain
     * @return
     */
    @CacheEvict(allEntries = true)
    boolean create(TSysPermission domain);

    /**
     * create
     * @param userDetails
     * @param domain
     * @return
     */
    @CacheEvict(allEntries = true)
    Result create(DefaultJwtUserDetails userDetails, TSysPermission domain);

    /**
     * query
     * @param id
     * @return
     */
    @Cacheable(key = "#p0")
    TSysPermission findById(long id);

    /**
     * 根据条件查询权限
     * @return
     */
    @Cacheable(key = "'name:'+ #p0")
    List<TSysPermission> selectPermissionsByCondition(String name);

    /**
     * build Tree
     * @param permissionDomains
     * @return
     */
    List buildTree(List<TSysPermission> permissionDomains);

    @CacheEvict(allEntries = true)
    Result update(DefaultJwtUserDetails userDetails, TSysPermission domain);

    /**
     * update
     * @param domain
     * @return
     */
    @CacheEvict(allEntries = true)
    boolean update(TSysPermission domain);

    /**
     * delete
     * @param id
     * @return
     */
    @CacheEvict(allEntries = true)
    boolean delete(Long id);

    /**
     * findByPid
     *
     * @param pid
     * @return
     */
    @Cacheable(key = "'pid:'+#p0")
    List<TSysPermission> findByPid(long pid);
}
