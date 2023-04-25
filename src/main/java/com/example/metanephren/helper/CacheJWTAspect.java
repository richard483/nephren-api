package com.example.metanephren.helper;

import com.example.metanephren.helper.annotation.CacheJWT;
import com.example.metanephren.helper.util.AspectUtil;
import com.example.metanephren.helper.util.JWTUtil;
import com.example.metanephren.servicesImpl.RedisServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class CacheJWTAspect {
  private static final String HASH_KEY = "JWT";
  private final RedisServiceImpl redisService;
  private final JWTUtil jwtUtil;

  @Autowired
  public CacheJWTAspect(RedisServiceImpl redisService, JWTUtil jwtUtil) {
    this.redisService = redisService;
    this.jwtUtil = jwtUtil;
  }

  @Around("@annotation(com.example.metanephren.helper.annotation.CacheJWT)")
  public Object jwtCache(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
    CacheJWT cacheJWT = methodSignature.getMethod().getAnnotation(CacheJWT.class);
    String username = AspectUtil.parseExpression(cacheJWT.userName(), proceedingJoinPoint);
    String existingToken = (String) redisService.get("JWT#" + username, HASH_KEY);

    if (existingToken != null && !jwtUtil.isJWTExpiredWithoutException(existingToken)) {
      log.debug("#CacheJWTAspect returned token from existing data on redis");
      return existingToken;
    }
    Object proceedJoinPoint = proceedingJoinPoint.proceed();

    if (proceedJoinPoint != null) {
      redisService.set("JWT#" + username, HASH_KEY, proceedJoinPoint.toString());
      log.debug("#CacheJWTAspect storing new generated token to redis");
    }

    return proceedJoinPoint;
  }
}
