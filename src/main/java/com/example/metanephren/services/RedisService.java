package com.example.metanephren.services;

public interface RedisService {
  void set(String key, String hashKey, String value);

  Object get(String key, String hashKey);
}
