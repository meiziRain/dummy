package com.meizi.admin.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 权限信息表
 * </p>
 *
 * @author meizi
 * @since 2020-01-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class TSysPermission extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 权限名称
     */
    @TableField("PERMISSION_NAME")
    private String permissionName;

    /**
     * 别名
     */
    @TableField("ALIAS")
    private String alias;

    /**
     * 父ID
     */
    @TableField("PID")
    private Long pid;

    /**
     * 是否删除（1是0否）
     */
    @TableLogic
    private Boolean isDel = false;

    /**
     * 扩展参数
     */
    @TableField("EXTRA")
    private String extra;

    /**
     * 子权限
     */
    @TableField(exist = false)
    private List<TSysPermission> children;

    @TableField(exist = false)
    private String name;

    /**
     * 该节点是否是其它节点的子节点
     */
    @TableField(exist = false)
    private Boolean isChild = false;

}
