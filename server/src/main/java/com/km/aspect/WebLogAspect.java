package com.km.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

@Aspect
@Component
public class WebLogAspect {

    private static final Logger logger = Logger.getLogger(WebLogAspect.class.getName());

    private static final String start = "-----------------------start---------------------------";
    private static final String end = "-----------------------end---------------------------";

    @Pointcut("execution(public * com.km.controller.*.*(..))")
    public void webLog() {}

    @Before("webLog()")
    public void doBefore(@NotNull JoinPoint joinPoint) throws Throwable {
        logger.log(Level.INFO, start);

        Signature signature = joinPoint.getSignature();
        logger.log(Level.INFO,"Class Method    "+ signature.getDeclaringTypeName() + signature.getName());
        logger.log(Level.INFO,"参数   "+ Arrays.toString(joinPoint.getArgs()));
    }

    @After("webLog()")
    public void doAfter() {
        logger.log(Level.INFO, end);
    }

    @Around("webLog()")
    public Object doAround(@NotNull ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        var time = System.currentTimeMillis();

        Object proceed = proceedingJoinPoint.proceed();
        logger.log(Level.INFO, proceed == null?"null":proceed.toString());

        logger.log(Level.INFO, "耗时：" + (System.currentTimeMillis() - time) + "毫秒");

        return proceed;
    }
}
