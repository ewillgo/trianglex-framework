package org.trianglex.common.spring;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.trianglex.common.log.LoggingProperties;
import org.trianglex.common.web.AbstractHttpProperties;
import org.trianglex.common.web.HttpClientInterceptor;
import org.trianglex.common.web.RestTemplateErrorHandler;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public abstract class FrameworkUtils {

    private static final Logger logger = LoggerFactory.getLogger(FrameworkUtils.class);

    private FrameworkUtils() {
    }

    public static RestTemplate getRestTemplate(String name,
                                               List<HttpMessageConverter<?>> messageConverters,
                                               LoggingProperties loggingProperties,
                                               AbstractHttpProperties httpProperties) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(messageConverters);
        OkHttpClient.Builder okHttpClientBuilder = okHttpClientBuilder(httpProperties);
        okHttpClientBuilder.addInterceptor(new HttpClientInterceptor(loggingProperties));
        restTemplate.setRequestFactory(new OkHttp3ClientHttpRequestFactory(okHttpClientBuilder.build()));
        return enhanceRestTemplate(name, restTemplate);
    }

    public static RestTemplate getRestTemplate(String name,
                                               List<HttpMessageConverter<?>> messageConverters,
                                               OkHttpClient okHttpClient) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(messageConverters);
        restTemplate.setRequestFactory(new OkHttp3ClientHttpRequestFactory(okHttpClient));
        restTemplate.setErrorHandler(new RestTemplateErrorHandler());
        return enhanceRestTemplate(name, restTemplate);
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

        logger.info("[{}] Message converters: {}", name, restTemplate.getMessageConverters().stream().map(c -> c.getClass().getSimpleName()).collect(Collectors.joining(", ")));
        return restTemplate;
    }

    public static OkHttpClient.Builder okHttpClientBuilder(AbstractHttpProperties properties) {
        return new okhttp3.OkHttpClient
                .Builder()
                .followRedirects(properties.isFollowRedirects())
                .followSslRedirects(properties.isFollowSslRedirects())
                .readTimeout(properties.getReadTimeout().toMillis(), TimeUnit.MILLISECONDS)
                .connectTimeout(properties.getConnectTimeout().toMillis(), TimeUnit.MILLISECONDS)
                .writeTimeout(properties.getWriteTimeout().toMillis(), TimeUnit.MILLISECONDS)
                .connectionPool(new ConnectionPool(properties.getMaxIdleConnections(),
                        properties.getKeepAliveDuration().getSeconds(), TimeUnit.SECONDS));
    }
}
