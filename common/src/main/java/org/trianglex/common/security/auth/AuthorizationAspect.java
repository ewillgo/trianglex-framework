package org.trianglex.common.security.auth;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.trianglex.common.exception.ApiErrorException;

import java.time.Duration;

import static org.trianglex.common.constant.PropertiesConstant.SPRING_MVC_AUTH_ASPECT_ORDERED;

@Aspect
@Component
public class AuthorizationAspect implements Ordered {

    private final LoadingCache<String, String> loadingCache;

    public AuthorizationAspect(CacheLoader<String, String> cacheLoader) {
        this.loadingCache = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(Duration.ofDays(1))
                .refreshAfterWrite(Duration.ofHours(12))
                .build(cacheLoader);
    }

    private static final String CHARSET = "UTF-8";

    @Before("@annotation(apiAuthorization)")
    public void before(JoinPoint joinPoint, ApiAuthorization apiAuthorization) {
        Object[] args = joinPoint.getArgs();

        if (args == null || args.length == 0) {
            return;
        }

        for (Object arg : args) {

            if (!(arg instanceof ApiRequest)) {
                continue;
            }

            ApiRequest apiRequest = (ApiRequest) arg;

            if (StringUtils.isEmpty(apiRequest.getAppKey())
                    || StringUtils.isEmpty(apiRequest.getSign())
                    || StringUtils.isEmpty(apiRequest.getOriginalString())) {
                throw new ApiErrorException(apiAuthorization.message());
            }

            String originalString;
            try {
                originalString = new String(
                        Base64Utils.decodeFromUrlSafeString(apiRequest.getOriginalString()), CHARSET);
            } catch (Exception e) {
                throw new ApiErrorException(apiAuthorization.message(), e);
            }

            String appSecret = loadingCache.get(apiRequest.getAppKey());
            String clientSign = apiRequest.getSign();
            String serverSign = SignUtils.sign(originalString, appSecret);

            if (!serverSign.equals(clientSign)) {
                throw new ApiErrorException(apiAuthorization.message());
            }

            break;
        }
    }

    @Override
    public int getOrder() {
        return SPRING_MVC_AUTH_ASPECT_ORDERED;
    }

}
