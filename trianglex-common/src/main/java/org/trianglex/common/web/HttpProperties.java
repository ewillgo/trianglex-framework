package org.trianglex.common.web;

import org.springframework.boot.context.properties.ConfigurationProperties;

import static org.trianglex.common.constant.PropertiesConstant.HTTP_CONFIG_PREFIX;

@ConfigurationProperties(prefix = HTTP_CONFIG_PREFIX)
public class HttpProperties extends AbstractHttpProperties {

    public static final String RAW_NAME = "httpRawRestTemplate";

}
