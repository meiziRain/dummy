package com.meizi.admin.mapper;

import com.meizi.admin.entity.TSysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 角色信息表 Mapper 接口
 * </p>
 *
 * @author meizi
 * @since 2020-01-06
 */
public interface TSysRoleMapper extends BaseMapper<TSysRole> {
    @Select("SELECT T1.* FROM T_SYS_ROLE T1, T_SYS_USER_ROLE T2 WHERE T1.ID = T2.ROLE_ID AND T2.USER_ID = #{userId} ORDER BY ROLE_ID ASC")
    List<TSysRole> findRolesByUserId(@Param("userId") Long userId);
}
