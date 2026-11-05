package com.aliyun;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//放进 IOC：它告诉 Spring：“去把 AliyunOSSProperties.class 这个类给我实例化（new 出来），放进 IoC 容器里。”
//绑定数据：因为 AliyunOSSProperties 类上面有 @ConfigurationProperties(prefix = "aliyun.oss")，
//Spring 会顺手把 yml 里 aliyun.oss 下面的一堆数据，塞进这个刚 new 出来的对象里。
@EnableConfigurationProperties(AliyunOSSProperties.class)
@Configuration
public class AliyunOSSAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public AliyunOSSOperator aliyunOSSOperator(AliyunOSSProperties aliyunOSSProperties) {
        return new AliyunOSSOperator(aliyunOSSProperties);
    }
}
