package com.example.metanephren.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl {
  private final RedisTemplate<String, String> stringRedisTemplate;

  @Value("${redis-hash-key}") private String hashKey;

  @Autowired
  public RedisServiceImpl(RedisTemplate<String, String> stringRedisTemplate) {
    this.stringRedisTemplate = stringRedisTemplate;
  }

  public void set(String key, String value) {
    stringRedisTemplate.opsForHash().put(key, hashKey, value);
  }

  public Object get(String key) {
    return stringRedisTemplate.opsForHash().get(key, hashKey);
  }

  public Boolean hasKey(String key) {
    return stringRedisTemplate.opsForHash().hasKey(key, hashKey);
  }
}
