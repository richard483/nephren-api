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
  public void set(String key, String hashKey, String value) {
    stringRedisTemplate.opsForHash().put(key, hashKey, value);
  }

  @Override
  public Object get(String key, String hashKey) {
    return stringRedisTemplate.opsForHash().get(key, hashKey);
  }
}
