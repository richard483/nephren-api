package com.example.metanephren.helper;

import com.example.metanephren.helper.annotation.CacheJWT;
import com.example.metanephren.helper.util.AspectUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class CacheJWTAspect {
  @Around("@annotation(com.example.metanephren.helper.annotation.CacheJWT)")
  public Object jwtCache(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
    CacheJWT cacheJWT = methodSignature.getMethod().getAnnotation(CacheJWT.class);
    String username = AspectUtil.parseExpression(cacheJWT.userName(), proceedingJoinPoint);
    log.info("#CacheJWTAspect username : {}", username);
    return proceedingJoinPoint.proceed();
  }
}
