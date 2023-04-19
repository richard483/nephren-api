package com.example.metanephren.helper.util;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

@Slf4j
public class AspectUtil {
  private static final ExpressionParser parser = new SpelExpressionParser();

  public static String parseExpression(String key, ProceedingJoinPoint proceedingJoinPoint) {
    try {
      MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
      Expression expression = parser.parseExpression(key);

      MethodBasedEvaluationContext methodBasedEvaluationContext = new MethodBasedEvaluationContext(
          proceedingJoinPoint.getThis(),
          methodSignature.getMethod(),
          proceedingJoinPoint.getArgs(),
          new DefaultParameterNameDiscoverer());
      return (String) expression.getValue(methodBasedEvaluationContext);
    } catch (Exception e) {
      log.info("#AspectUtil error 'parseExpression' fkr key: {} with error ; {}", key, e.getMessage());
      return null;
    }
  }
}
