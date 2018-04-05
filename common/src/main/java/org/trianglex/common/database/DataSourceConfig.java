package org.trianglex.common.database;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.trianglex.common.database.mybatis.MyBatisConfig;

@Configuration
@EnableAspectJAutoProxy
@EnableTransactionManagement
@Import({DataSourceRegister.class, MyBatisConfig.class})
@ComponentScan(basePackages = "**.service.**")
public class DataSourceConfig {

    @ConditionalOnMissingBean
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(AbstractRoutingDataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    @ConditionalOnMissingBean
    public DataSourceSwitchAspect dataSourceSwitchAspect() {
        return new DataSourceSwitchAspect();
    }
}
