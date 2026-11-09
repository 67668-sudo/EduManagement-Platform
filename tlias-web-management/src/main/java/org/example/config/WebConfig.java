package org.example.config;

import org.example.interceptor.ReFreshInterceptor;
import org.example.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Autowired
//    private org.example.interceptor.DemoInterceptor demoInterceptor;
    private final TokenInterceptor tokenInterceptor;
    private final ReFreshInterceptor reFreshInterceptor;

    // 构造器注入：如果 Bean 不存在，启动时立刻报错，不会悄悄变成 null
    public WebConfig(TokenInterceptor tokenInterceptor,
                     ReFreshInterceptor reFreshInterceptor) {
        this.tokenInterceptor = tokenInterceptor;
        this.reFreshInterceptor = reFreshInterceptor;
    }
    @Override//自己定义的拦截器需要配置添加到springMVC中
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/login/**").order(1);

        registry.addInterceptor(reFreshInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/login/**").order(0);
    }

}
