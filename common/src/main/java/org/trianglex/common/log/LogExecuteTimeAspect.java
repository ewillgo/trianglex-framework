package org.trianglex.common.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static org.trianglex.common.constant.PropertiesConstant.SPRING_MVC_LOGTIME_ASPECT_ORDERED;

/**
 * Created by liangwenwei on 2018-03-27.
 */
@Aspect
@Component
@Order(SPRING_MVC_LOGTIME_ASPECT_ORDERED)
public class LogExecuteTimeAspect {

    private static final ThreadLocal<Long> START_TIME = new ThreadLocal<>();
    private static final Logger logger = LoggerFactory.getLogger(LogExecuteTimeAspect.class);

    @Before("@annotation(logExecuteTime)")
    public void before(LogExecuteTime logExecuteTime) {
        START_TIME.set(System.nanoTime());
    }

    @After("@annotation(logExecuteTime)")
    public void after(JoinPoint joinPoint, LogExecuteTime logExecuteTime) {
        logger.info("[{}.{}] spend time: {}", joinPoint.getTarget().getClass().getSimpleName(), joinPoint.getSignature().getName(), getSpendTime());
    }

    private String getSpendTime() {
        return String.format("%.2fms", (System.nanoTime() - START_TIME.get()) / 1e6d);
    }
}
