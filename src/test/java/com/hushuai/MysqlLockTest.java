package com.hushuai;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * created by it_hushuai
 * 2020/5/3 21:55
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MysqlLockTest {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Test
    public void initRedis(){
        redisTemplate.boundValueOps("stock").set("50");
    }
}
