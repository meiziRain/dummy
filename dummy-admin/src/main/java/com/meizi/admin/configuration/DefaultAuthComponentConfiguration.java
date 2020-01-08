package com.meizi.admin.configuration;

import com.meizi.admin.auth.DefaultJwtTokenUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultAuthComponentConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DefaultJwtTokenUtil jwtTokenUtil(){
        return new DefaultJwtTokenUtil();
    }
}
