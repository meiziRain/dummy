package com.meizi.admin.entity;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色信息表
 * </p>
 *
 * @author meizi
 * @since 2020-01-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class TSysRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 角色名称
     */
    @TableField("ROLE_NAME")
    private String roleName;

    /**
     * 描述
     */
    @TableField("ROLE_DESC")
    private String roleDesc;

    /**
     * 上级角色
     */
    @TableField("PID")
    private Long pid;

    /**
     * 系统角色不能修改 1 表示 系统角色 0表示 非系统角色
     */
    @TableField("IS_SYSTEM")
    private Integer isSystem;

    /**
     * 数据权限范围:1,全部数据权限， 2 ，本级数据权限， 3.本级及下级机构数据权限，4.自定义的数据权限
     */
    @TableField("DATA_SCOPE")
    private Integer dataScope;

    /**
     * 角色英文别名
     */
    @TableField("ALIAS")
    private String alias;

    /**
     * 是否删除 1是 0否
     */
    @TableField("IS_DEL")
    private Integer isDel;

    /**
     * 扩展参数
     */
    @TableField("EXTRA")
    private String extra;

    /**
     * 是否只能查看自己私有数据 1是 0否
     */
    @TableField("IS_OWNER")
    private Boolean isOwner;


}
