package com.meizi.admin.mapper;

import com.meizi.admin.entity.TSysPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 权限信息表 Mapper 接口
 * </p>
 *
 * @author meizi
 * @since 2020-01-07
 */
public interface TSysPermissionMapper extends BaseMapper<TSysPermission> {

    @Select("SELECT T1.* FROM T_SYS_PERMISSION T1, T_SYS_ROLE_PERMISSION T2 WHERE T1.ID = T2.PERMISSION_ID AND T2.ROLE_ID = #{roleId}")
    List<TSysPermission> findByRoleId(@Param("roleId") Long roleId);
}
