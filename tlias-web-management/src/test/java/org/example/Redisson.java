package org.example;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

public class Redisson {
    @Resource
    private RedissonClient redissonClient;

    @Test
    public void testRedisson() throws InterruptedException {
        RLock lock = redissonClient.getLock("anyLock");
        boolean isLocked = lock.tryLock(1,10, TimeUnit.SECONDS);
        if (isLocked) {
            lock.unlock();
        }
        System.out.println(redissonClient);
    }
}
