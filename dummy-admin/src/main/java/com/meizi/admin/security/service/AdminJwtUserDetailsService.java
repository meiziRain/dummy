package com.meizi.admin.security.service;

import com.meizi.admin.auth.DefaultJwtUserDetails;
import com.meizi.admin.entity.TSysUser;
import com.meizi.admin.service.ITSysPermissionService;
import com.meizi.admin.service.ITSysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author TianLei
 * @date 2019/06/05
 */
@Service("adminJwtUserDetailsService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AdminJwtUserDetailsService implements UserDetailsService {

    @Autowired
    private ITSysPermissionService tSysPermissionService;

    @Autowired
    private ITSysUserService tSysUserService;

    @Override
    public DefaultJwtUserDetails loadUserByUsername(String userName) {
        TSysUser user = tSysUserService.findByUserName(userName);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException(userName + " 用户不存在");
        }

        return createJwtUser(user);
    }


    // 邮箱地址
    public DefaultJwtUserDetails loadUserByEmail(String email) {
        TSysUser user = tSysUserService.findByUserEmail(email);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException(email + " 用户不存在");
        }

        return createJwtUser(user);
    }

    public DefaultJwtUserDetails createJwtUser(TSysUser user) {
        return new DefaultJwtUserDetails(
                user.getId(),
                user.getUserName(),
                user.getPassword(),
                user.getUserEmail(),
                user.getUserPhone(),
                user.getUserSex() == null ? 0 : Integer.valueOf(user.getUserSex()),
                tSysPermissionService.mapToGrantedAuthorities(user),
                user.getEnabled(),
                user.getCreateAt()
        );
    }
}
