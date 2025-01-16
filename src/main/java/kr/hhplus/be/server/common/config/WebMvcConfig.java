package kr.hhplus.be.server.common.config;

import kr.hhplus.be.server.common.handler.QueueCreateInterceptor;
import kr.hhplus.be.server.common.handler.QueueInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final QueueCreateInterceptor queueCreateInterceptor;
    private final QueueInterceptor queueInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(queueCreateInterceptor).addPathPatterns("/api/v1/queues");
        registry.addInterceptor(queueInterceptor).addPathPatterns(
                "/api/v1/users/**",
                "/api/v1/concerts/**",
                "/api/v1/reservations",
                "/api/v1/payments");
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
