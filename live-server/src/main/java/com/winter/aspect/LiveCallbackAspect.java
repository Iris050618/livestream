package com.winter.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 直播回调函数，只允许内部调用，如果是外部非法的请求要pass掉
 */
@Aspect
@Component
public class LiveCallbackAspect {
    @Pointcut("execution(* com.winter.app.controller.LiveCallbackController.*(..))")
    public void callbackMethods() {
    }

    @Before("callbackMethods()")
    public void logBefore() {

    }
}
