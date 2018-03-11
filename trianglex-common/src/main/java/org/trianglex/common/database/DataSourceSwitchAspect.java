package org.trianglex.common.database;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Order(-1)
@Component
public class DataSourceSwitchAspect {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceSwitchAspect.class);

    @Before("@annotation(dataSource)")
    public void before(DataSource dataSource) {
        if (DataSourceContextHolder.containsDataSourceName(dataSource.value())) {
            DataSourceContextHolder.setCurrentDataSourceName(dataSource.value());
        } else {
            logger.error("Could not found data source name.");
        }
    }

    @After("@annotation(dataSource)")
    public void after(DataSource dataSource) {
        DataSourceContextHolder.removeCurrentDataSourceName();
    }

}
