package com.meizi.admin.db;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.Properties;

/**
 * mybatis-plus默认配置, 包括分页插件自动识别， 打印SQL（只有dev和test环境打印）
 * @author TianLei
 * @date 2019/05/04
 */
public class DefaultMybatisPlusConfiguration {
    /**
     * 分页插件，自动识别数据库类型
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

}
