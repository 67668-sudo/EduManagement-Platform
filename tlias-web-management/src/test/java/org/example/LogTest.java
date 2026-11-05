package org.example;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class LogTest {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LogTest.class);
    @Test
    public void testLog(){
        System.out.println(LocalDateTime.now() + " : 开始计算...");

        int sum = 0;
        int[] nums = {1, 5, 3, 2, 1, 4, 5, 4, 6, 7, 4, 34, 2, 23};
        for (int num : nums) {
            sum += num;
        }
        
        log.info("计算结果为: {}", sum);
        log.debug("结束计算...");
        log.trace("结束计算...");
        log.warn("结束计算...");
        log.error("结束计算...");
    }

}
