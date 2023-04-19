package com.example.metanephren.servicesImpl;

import com.example.metanephren.services.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService {
  private final RedisTemplate<String, String> stringRedisTemplate;

  @Autowired
  public RedisServiceImpl(RedisTemplate<String, String> stringRedisTemplate) {
    this.stringRedisTemplate = stringRedisTemplate;
  }

  @Override
  public void set(String key, String value) {
    stringRedisTemplate.opsForValue().set(key, value);
  }

  @Override
  public Object get(String key) {
    return stringRedisTemplate.opsForValue().get(key);
  }
}
