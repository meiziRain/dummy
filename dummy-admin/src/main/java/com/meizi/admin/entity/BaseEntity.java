package com.meizi.admin.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: dummy
 * @description: 所有entity的父类
 * @author: Meizi
 * @create: 2020-01-03 13:30
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseEntity  implements Serializable {
    /**
     * 主键ID
     */
    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 创建人
     */
    @TableField("create_by")
    private Long createBy;
    /**
     * 修改人
     */
    @TableField("update_by")
    private Long updateBy;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createAt;
    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateAt;
}
