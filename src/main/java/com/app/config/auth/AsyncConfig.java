package com.app.config.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

//  This bean enables the sharing of the security context between async threads
    @Bean
    public DelegatingSecurityContextAsyncTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor delegate = new ThreadPoolTaskExecutor();
        delegate.setCorePoolSize(5);
        delegate.setMaxPoolSize(10);
        delegate.setQueueCapacity(100);
        delegate.setThreadNamePrefix("sse-async-");
        delegate.initialize();

        return new DelegatingSecurityContextAsyncTaskExecutor(delegate);
    }
}