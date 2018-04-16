package org.trianglex.common.web;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.trianglex.common.log.LoggingProperties;
import org.trianglex.common.spring.FrameworkUtils;

import java.util.Arrays;

import static org.trianglex.common.web.FrameworkHttpProperties.NAME;
import static org.trianglex.common.web.HttpProperties.RAW_NAME;

@Configuration
@Import({HttpProperties.class, FrameworkHttpProperties.class})
public class HttpConfig {

    @Autowired
    private HttpProperties httpProperties;

    @Autowired
    private FrameworkHttpProperties frameworkHttpProperties;

    @Autowired
    private LoggingProperties loggingProperties;

    @Autowired
    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private StringHttpMessageConverter stringHttpMessageConverter;

    @Bean(name = RAW_NAME)
    public RestTemplate httpRawRestTemplate() {
        return FrameworkUtils.getRestTemplate(RAW_NAME,
                Arrays.asList(mappingJackson2HttpMessageConverter, stringHttpMessageConverter),
                loggingProperties, httpProperties);
    }

    @Bean
    @Primary
    @LoadBalanced
    public RestTemplate restTemplate(OkHttpClient okHttpClient) {
        return FrameworkUtils.getRestTemplate(NAME,
                Arrays.asList(mappingJackson2HttpMessageConverter, stringHttpMessageConverter), okHttpClient);
    }

    @Bean
    public OkHttpClient okHttpClient() {
        OkHttpClient.Builder builder = FrameworkUtils.okHttpClientBuilder(frameworkHttpProperties);
        builder.addInterceptor(new HttpClientInterceptor(loggingProperties));
        return builder.build();
    }
}
