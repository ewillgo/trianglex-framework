package org.trianglex.common.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(LoggingProperties.class)
public class LogConfig {

    @Value("${spring.cloud.config.profile:dev}")
    private String profile;

    @Autowired
    private LoggingProperties loggingProperties;

    @Bean
    @ConditionalOnMissingBean
    public SpringMvcLoggingFilter springMvcLoggingFilter() {
        loggingProperties.setLogLevel(LogLevel.valueOf(profile.toUpperCase()));
        return new SpringMvcLoggingFilter(loggingProperties);
    }
}
