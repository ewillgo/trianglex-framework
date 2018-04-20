package org.trianglex.common.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.trianglex.common.constant.PropertiesConstant.SPRING_MVC_LOGGING_FILTER_ORDERED;

public class SpringMvcLoggingFilter extends OncePerRequestFilter implements Ordered {

    private final LoggingProperties loggingProperties;
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringMvcLoggingFilter.class);

    public SpringMvcLoggingFilter(LoggingProperties loggingProperties) {
        this.loggingProperties = loggingProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (!LogHelper.logIfNecessary(request.getRequestURI(), loggingProperties, LOGGER)) {
            filterChain.doFilter(request, response);
            return;
        }

        LogBuilder logBuilder = new LogBuilder(request, response, loggingProperties.getLogLevel());

        try {
            LOGGER.info("{}", logBuilder.buildRequestLog());
            logBuilder.setStartTime(System.nanoTime());
            filterChain.doFilter(logBuilder.getHttpServletRequest(), logBuilder.getHttpServletResponse());
        } finally {
            // When filter chain raise a exception, end time will be zero,
            // so put it into finally-block.
            logBuilder.setEndTime(System.nanoTime());
            LOGGER.info("{}\n", logBuilder.buildResponseLog(response.getContentType()));
        }
    }

    @Override
    public int getOrder() {
        return SPRING_MVC_LOGGING_FILTER_ORDERED;
    }
}
