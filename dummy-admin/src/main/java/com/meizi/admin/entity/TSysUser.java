package com.meizi.admin.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 后台用户信息表
 * </p>
 *
 * @author meizi
 * @since 2020-01-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class TSysUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名称
     */
    @TableField("NICK_NAME")
    private String nickName;

    /**
     * 用户账号
     */
    @TableField("USER_NAME")
    private String userName;

    /**
     * 密码
     */
    @TableField("PASSWORD")
    private String password;

    /**
     * 电话
     */
    @TableField("USER_PHONE")
    private String userPhone;

    /**
     * 邮箱
     */
    @TableField("USER_EMAIL")
    private String userEmail;

    /**
     * 性别
     */
    @TableField("USER_SEX")
    private String userSex;

    /**
     * 最后一次登陆时间
     */
    @TableField("LAST_LOGIN_TIME")
    private LocalDateTime lastLoginTime;

    /**
     * 登录次数
     */
    @TableField("LOGIN_COUNT")
    private Long loginCount;

    /**
     * 是否删除（1是0否）
     */
    @TableField("IS_DEL")
    private Boolean isDel;

    /**
     * 是否启用（1是0否）
     */
    @TableField("ENABLED")
    private Boolean enabled;

    /**
     * 扩展参数
     */
    @TableField("EXTRA")
    private String extra;


    @TableField(exist = false)
    private List<TSysRole> roles;

    /**
     * 是否只能查看自己私有数据
     */
    @TableField(exist = false)
    private Boolean isOwner = false;
}
