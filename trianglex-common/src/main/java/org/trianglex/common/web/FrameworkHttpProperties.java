package org.trianglex.common.web;

import org.springframework.boot.context.properties.ConfigurationProperties;

import static org.trianglex.common.constant.PropertiesConstant.FRAMEWORK_HTTP_CONFIG_PREFIX;

@ConfigurationProperties(prefix = FRAMEWORK_HTTP_CONFIG_PREFIX)
public class FrameworkHttpProperties extends AbstractHttpProperties {

    public static final String NAME = "restTemplate";
}
