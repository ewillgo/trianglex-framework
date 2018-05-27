package org.trianglex.common.security.auth;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.trianglex.common.exception.ClientApiException;

import java.time.Duration;

import static org.trianglex.common.constant.PropertiesConstant.SPRING_MVC_AUTH_ASPECT_ORDERED;

@Aspect
@Component
public class AuthorizationAspect implements Ordered {

    private final String appSecretKey;
    private static final int MD5_LENGTH = 32;
    private final LoadingCache<String, String> loadingCache;

    public AuthorizationAspect(CacheLoader<String, String> cacheLoader) {
        this(cacheLoader, null);
    }

    public AuthorizationAspect(CacheLoader<String, String> cacheLoader, String appSecretKey) {
        this.appSecretKey = appSecretKey;
        this.loadingCache = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(Duration.ofDays(1))
                .refreshAfterWrite(Duration.ofHours(12))
                .build(cacheLoader);
    }

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
                    || apiRequest.getSign().length() < MD5_LENGTH) {
                throw new ClientApiException(apiAuthorization.message());
            }

            String originalString = apiRequest.getSign().substring(0, MD5_LENGTH);
            String appSecret = loadingCache.get(apiRequest.getAppKey());
            String clientSign = apiRequest.getSign();
            String serverSign = SignUtils.sign(originalString, appSecret);

            if (!serverSign.equals(clientSign)) {
                throw new ClientApiException(apiAuthorization.message());
            }

            if (!StringUtils.isEmpty(appSecretKey) && !StringUtils.isEmpty(appSecret)) {
                RequestContextHolder.currentRequestAttributes().setAttribute(
                        appSecretKey, appSecret, RequestAttributes.SCOPE_REQUEST);
            }

            break;
        }
    }

    @Override
    public int getOrder() {
        return SPRING_MVC_AUTH_ASPECT_ORDERED;
    }

}
