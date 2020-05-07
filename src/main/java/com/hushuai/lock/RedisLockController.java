package com.hushuai.lock;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.TimeUnit;

/**
 * created by it_hushuai
 * 2020/5/4 16:01
 */
@Controller
@RequestMapping("reduce")
public class RedisLockController {
    private static String lockKey = "product_001";

    private static final InterProcessMutex lock;
    static {
        String connectString = "192.168.18.130:2181";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework curatorClient = CuratorFrameworkFactory.newClient(connectString, 60000, 15000,retryPolicy);
        curatorClient.start();
        lock = new InterProcessMutex(curatorClient, "/lock_path");
    }

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedissonClient client;

    @RequestMapping("stock")
    public String reduceStock(){
        RLock lock = client.getLock(lockKey);
        try {
            lock.lock(30, TimeUnit.SECONDS);
            int stock = Integer.parseInt(redisTemplate.opsForValue().get("stock"));
            if(stock > 0){
                try {
                    Thread.sleep(100);//模拟减库存消耗时间
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int newStock = stock - 1;
                redisTemplate.opsForValue().set("stock", newStock + "");
                System.out.println("扣减成功，库存剩余" + newStock + "");
            }else {
                System.out.println("扣减失败，库存不足");
            }
        }finally {
            lock.unlock();
        }
        return "";
    }

    @RequestMapping("stock2")
    public String reduceStock2() throws Exception {
        if(lock.acquire(30, TimeUnit.SECONDS)){
            try {
                int stock = Integer.parseInt(redisTemplate.opsForValue().get("stock"));
                if(stock > 0){
                    try {
                        Thread.sleep(100);//模拟减库存消耗时间
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int newStock = stock - 1;
                    redisTemplate.opsForValue().set("stock", newStock + "");
                    System.out.println("扣减成功，库存剩余" + newStock + "");
                }else {
                    System.out.println("扣减失败，库存不足");
                }
            }  finally {
                lock.release();
            }
        }
        return "";
    }
}
