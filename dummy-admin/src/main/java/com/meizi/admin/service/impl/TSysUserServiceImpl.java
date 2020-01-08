package com.meizi.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.meizi.admin.auth.DefaultJwtUserDetails;
import com.meizi.admin.entity.TSysRole;
import com.meizi.admin.entity.TSysUser;
import com.meizi.admin.entity.TSysUserRole;
import com.meizi.admin.mapper.TSysUserMapper;
import com.meizi.admin.model.Result;
import com.meizi.admin.service.ITSysRoleService;
import com.meizi.admin.service.ITSysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 后台用户信息表 服务实现类
 * </p>
 *
 * @author meizi
 * @since 2020-01-06
 */
@Service
public class TSysUserServiceImpl extends ServiceImpl<TSysUserMapper, TSysUser> implements ITSysUserService {
    @Autowired
    private ITSysRoleService tSysRoleService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createUser(TSysUser domain) {
        // 新增用户
        TSysUser exitsUser = this.findByUserName(domain.getUserName());
        if (null != exitsUser) {
            throw new RuntimeException("该账号已存在");
        }
        baseMapper.insert(domain);
        return true;
    }

    @Override
    public TSysUser findByUserName(String userName) {
        QueryWrapper<TSysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name", userName);
        TSysUser tSysUser = baseMapper.selectOne(wrapper);
        if (Objects.nonNull(tSysUser) && Objects.nonNull(tSysUser.getId())) {
            List<TSysRole> tSysRoleList = tSysRoleService.findRolesByUserId(tSysUser.getId());
            tSysUser.setRoles(tSysRoleList);
            tSysUser.setIsOwner(tSysRoleService.isGetOwner(tSysRoleList));
        }
        return tSysUser;
    }

    @Override
    public boolean deleteUser(TSysUser domain) {
        return retBool(baseMapper.deleteById(domain.getId()));
    }

    @Override
    public boolean updateByUser(TSysUser user) {
        return  retBool(baseMapper.updateById(user));
    }
}
