package com.meizi.admin.service;

import com.meizi.admin.auth.DefaultJwtUserDetails;
import com.meizi.admin.entity.TSysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.meizi.admin.model.Result;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

/**
 * <p>
 * 后台用户信息表 服务类
 * </p>
 *
 * @author meizi
 * @since 2020-01-06
 */
public interface ITSysUserService extends IService<TSysUser> {

    /**
     * create
     * @param domain
     * @return
     */
    @CacheEvict(allEntries = true)
    boolean createUser(TSysUser domain);

    TSysUser findByUserName(String userName);



    TSysUser findByUserEmail(String Email);

    /**
     * delete
     * @param domain
     * @return
     */
    @CacheEvict(allEntries = true)
    boolean deleteUser(TSysUser domain);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @CacheEvict(allEntries = true)
    boolean updateByUser(TSysUser user);

    /**
     * get
     *
     * @param id
     * @return
     */
    @Cacheable(key = "#p0")
    TSysUser findById(long id);

}
