package com.meizi.admin.service.impl;

import com.meizi.admin.entity.TSysRole;
import com.meizi.admin.mapper.TSysRoleMapper;
import com.meizi.admin.service.ITSysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 * @author meizi
 * @since 2020-01-06
 */
@Service
public class TSysRoleServiceImpl extends ServiceImpl<TSysRoleMapper, TSysRole> implements ITSysRoleService {
    @Override
    public List<TSysRole> findRolesByUserId(Long userId) {
        return baseMapper.findRolesByUserId(userId);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean isGetOwner(List<TSysRole> tSysRoleList){
        boolean isOwner = true;
        for(TSysRole tSysRole : tSysRoleList){
            if(Boolean.FALSE.equals(tSysRole.getIsOwner())){
                isOwner = false;
                break;
            }
        }
        return isOwner;
    }
}
