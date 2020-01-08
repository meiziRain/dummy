package com.meizi.admin.db;

/**
 * @program: dummy
 * @description:
 * @author: Meizi
 * @create: 2020-01-07 10:55
 **/

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * 自定义填充公共字段
 * @author TianLei
 */
public class DefaultMetaObjectHandler implements MetaObjectHandler {
    /**
     * 更新时间
     */
    private final static String UPDATE_AT = "updateAt";
    /**
     * 创建时间
     */
    private final static String CREATE_AT = "createAt";

    /**
     * 插入填充，字段为空自动填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Object createAt = getFieldValByName(CREATE_AT, metaObject);
        Object updateAt = getFieldValByName(UPDATE_AT, metaObject);
        if (createAt == null || updateAt == null) {
            Date date = new Date();
            if (createAt == null) {
                setFieldValByName(CREATE_AT, date, metaObject);
            }
            if (updateAt == null) {
                setFieldValByName(UPDATE_AT, date, metaObject);
            }
        }
    }

    /**
     * 更新填充
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        //mybatis-plus版本2.0.9+
        setFieldValByName(UPDATE_AT, new Date(), metaObject);
    }
}

