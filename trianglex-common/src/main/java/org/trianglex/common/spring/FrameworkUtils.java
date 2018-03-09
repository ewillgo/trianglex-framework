package org.trianglex.common.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Collectors;

public abstract class FrameworkUtils {

    private static final Logger logger = LoggerFactory.getLogger(FrameworkUtils.class);

    private FrameworkUtils() {
    }

    public static RestTemplate enhanceRestTemplate(String name, RestTemplate restTemplate) {

        if (restTemplate == null) {
            throw new IllegalArgumentException("RestTemplate could not be null.");
        }

        restTemplate.getMessageConverters().removeIf(converter ->
                !(converter instanceof MappingJackson2HttpMessageConverter) &&
                        !(converter instanceof ByteArrayHttpMessageConverter) &&
                        !(converter instanceof MappingJackson2XmlHttpMessageConverter) &&
                        !(converter instanceof StringHttpMessageConverter));

        logger.info("[{}] Message converters: {}", name, restTemplate.getMessageConverters().stream().map(c -> c.getClass().getSimpleName()).collect(Collectors.joining(",")));
        return restTemplate;
    }
}
