package org.trianglex.common.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.HashMap;
import java.util.Map;

public class DataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private static final String DATA_SOURCE_PREFIX = "db.config";
    private static final String DATA_SOURCE_NAMES = DATA_SOURCE_PREFIX + ".names";
    private static final Logger logger = LoggerFactory.getLogger(DataSourceRegister.class);

    private Binder binder;
    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        this.binder = new Binder(ConfigurationPropertySources.from(((ConfigurableEnvironment) environment).getPropertySources()));
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        String[] names = environment.getRequiredProperty(DATA_SOURCE_NAMES).replaceAll("\\s+", "").split(",");

        HikariDataSource dataSource = null;
        HikariDataSource defaultDataSource = null;
        Map<Object, Object> targetDataSources = new HashMap<>();

        for (String name : names) {
            String dataSourceName = String.format("%s.%s", DATA_SOURCE_PREFIX, name);
            BindResult<HikariConfig> bindResult = binder.bind(dataSourceName, Bindable.of(HikariConfig.class));

            dataSource = new HikariDataSource(bindResult.get());
            if (defaultDataSource == null) {
                defaultDataSource = dataSource;
            }

            DataSourceContextHolder.setDataSourceName(dataSourceName);
            targetDataSources.put(dataSourceName, dataSource);
        }

        GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
        genericBeanDefinition.setBeanClass(DynamicDataSource.class);
        genericBeanDefinition.setSynthetic(true);

        MutablePropertyValues mutablePropertyValues = genericBeanDefinition.getPropertyValues();
        mutablePropertyValues.addPropertyValue("defaultTargetDataSource", defaultDataSource);
        mutablePropertyValues.addPropertyValue("targetDataSources", targetDataSources);
        registry.registerBeanDefinition("dataSource", genericBeanDefinition);

        logger.info("DataSource registered. {}", DataSourceContextHolder.getDataSourceNames());
    }

    static class DynamicDataSource extends AbstractRoutingDataSource {

        @Override
        protected Object determineCurrentLookupKey() {
            return DataSourceContextHolder.peekCurrentDataSourceName();
        }

    }
}
