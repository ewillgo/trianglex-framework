package org.trianglex.common.database;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import static org.trianglex.common.constant.PropertiesConstant.SPRING_MVC_DATASOURCE_ASPECT_ORDERED;

@Aspect
@Component
public class DataSourceSwitchAspect implements Ordered {

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

    @Override
    public int getOrder() {
        return SPRING_MVC_DATASOURCE_ASPECT_ORDERED;
    }
}
