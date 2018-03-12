package org.trianglex.common.database.mybatis;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisConfig {

    @Bean
    @ConditionalOnMissingBean
    public MyBatisInterceptor myBatisInterceptor() {
        return new MyBatisInterceptor();
    }

}
