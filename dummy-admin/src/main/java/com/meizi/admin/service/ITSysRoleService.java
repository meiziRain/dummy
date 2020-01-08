package com.meizi.admin.service;

import com.meizi.admin.entity.TSysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.cache.annotation.CacheEvict;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务类
 * </p>
 *
 * @author meizi
 * @since 2020-01-06
 */
public interface ITSysRoleService extends IService<TSysRole> {
    /**
     * 根据用户ID查询角色集合
     *
     * @param userId
     * @return
     */
    @CacheEvict(allEntries = true)
    List<TSysRole> findRolesByUserId(Long userId);


    boolean isGetOwner(List<TSysRole> tSysRoleList);
}
