package com.meizi.admin.configuration;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.meizi.admin.db.DefaultMetaObjectHandler;
import com.meizi.admin.db.DefaultMybatisPlusConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @program: dummy
 * @description:
 * @author: Meizi
 * @create: 2020-01-07 10:50
 **/

@Configuration
@MapperScan({"com.meizi.admin.mapper*"})
@Import(DefaultMetaObjectHandler.class)
@EnableTransactionManagement
public class AdminMybatisPlusConfiguration {
}

