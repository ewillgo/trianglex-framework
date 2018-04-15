package org.trianglex.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.trianglex.common.controller.GlobalExceptionController;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Import(GlobalExceptionController.class)
public class WebMvcConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);

    @Autowired
    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private StringHttpMessageConverter stringHttpMessageConverter;

    @Autowired
    private ByteArrayHttpMessageConverter byteArrayHttpMessageConverter;

    @Autowired
    private MappingJackson2XmlHttpMessageConverter mappingJackson2XmlHttpMessageConverter;

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentTypeStrategy(new ContentNegotiationManager())
                .ignoreAcceptHeader(false)
                .useRegisteredExtensionsOnly(false);
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.clear();
        converters.add(mappingJackson2HttpMessageConverter);
        converters.add(stringHttpMessageConverter);
        converters.add(byteArrayHttpMessageConverter);
        converters.add(mappingJackson2XmlHttpMessageConverter);
        logger.info("SpringBoot message converters: {}",
                converters.stream().map((converter) -> converter.getClass().getSimpleName()).collect(Collectors.joining(", ")));
    }
}
