package com.seminarhub.aop;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Log4j2
@Component
public class LogAdvice {
//    @Before( "execution(* com.seminarhub..*(..))")
//    public void logBefore(ProceedingJoinPoint proceedingJoinPoint){
//        log.info("========================");
//    }
    @Around( "execution(* com.seminarhub..*(..))")
    public Object logTime(ProceedingJoinPoint proceedingJoinPoint){
        log.info("========================");
        long start = System.currentTimeMillis();

        log.info("Target: " + proceedingJoinPoint.getTarget());
        log.info("Param: " + Arrays.toString(proceedingJoinPoint.getArgs()));

        //invoke Method
        Object result = null;

        try{
            result = proceedingJoinPoint.proceed();
        } catch (Throwable e){
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        log.info("TIME: "+ ( end - start ));
        return result;
    }

    @AfterThrowing(pointcut = "execution(* com.seminarhub..*(..))", throwing = "exception")
    public void logException(Exception exception){
        log.info("Exception Found");
        log.info("exception: "+exception);
    }
}
