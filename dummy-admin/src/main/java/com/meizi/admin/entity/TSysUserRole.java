package com.meizi.admin.entity;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户角色表
 * </p>
 *
 * @author meizi
 * @since 2020-01-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class TSysUserRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableField("USER_ID")
    private Long userId;

    /**
     * 角色ID
     */
    @TableField("ROLE_ID")
    private Long roleId;

    /**
     * 扩展参数
     */
    @TableField("EXTRA")
    private String extra;

}
