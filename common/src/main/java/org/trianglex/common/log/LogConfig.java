package org.trianglex.common.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.StringUtils;

@Configuration
@Import(LoggingProperties.class)
public class LogConfig {

    @Value("${spring.cloud.config.profile:}")
    private String cloudProfile;

    @Value("${spring.profiles.active:}")
    private String bootProfile;

    private static final String DEFAULT_PROFILE = "dev";

    @Autowired
    private LoggingProperties loggingProperties;

    @Bean
    @ConditionalOnMissingBean
    public SpringMvcLoggingFilter springMvcLoggingFilter() {
        String profile = DEFAULT_PROFILE;

        if (!StringUtils.isEmpty(cloudProfile)) {
            profile = cloudProfile;
        } else if (!StringUtils.isEmpty(bootProfile)) {
            profile = bootProfile;
        }

        loggingProperties.setLogLevel(LogLevel.valueOf(profile.toUpperCase()));
        return new SpringMvcLoggingFilter(loggingProperties);
    }

    @Bean
    public LogExecuteTimeAspect logExecuteTimeAspect() {
        return new LogExecuteTimeAspect();
    }
}
