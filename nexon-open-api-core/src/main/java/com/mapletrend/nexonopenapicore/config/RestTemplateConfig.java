package com.mapletrend.nexonopenapicore.config;

import com.mapletrend.nexonopenapicore.exception.RequestExecutionException;
import java.io.IOException;
import java.time.Duration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    public static final int CONNECT_TIMEOUT_SECONDS = 5;
    public static final int READ_TIMEOUT_SECONDS = 5;
    public static final int MAX_RETRY_COUNT = 3;
    public static final long RETRY_INTERVAL_MILLIS = 1000L;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(CONNECT_TIMEOUT_SECONDS))
                .setReadTimeout(Duration.ofSeconds(READ_TIMEOUT_SECONDS))
                .additionalInterceptors(retryInterceptor(), loggingInterceptor())
                .build();
    }

    @Bean
    public RetryTemplate retryTemplate() {
        return createRetryTemplate();
    }

    public ClientHttpRequestInterceptor retryInterceptor() {
        return ((request, body, execution) -> {
            RetryTemplate retryTemplate = createRetryTemplate();

            try {
                return retryTemplate.execute(context -> execution.execute(request, body));
            } catch (IOException e) {
                throw new RequestExecutionException("Request execution failed", e);
            }
        });
    }

    public RetryTemplate createRetryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        // 재시도 정책
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(MAX_RETRY_COUNT);
        retryTemplate.setRetryPolicy(retryPolicy);

        // 재시도 주기 정책
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();

        // 1초 간격으로 재시도
        backOffPolicy.setBackOffPeriod(RETRY_INTERVAL_MILLIS);
        retryTemplate.setBackOffPolicy(backOffPolicy);

        return retryTemplate;
    }

    public ClientHttpRequestInterceptor loggingInterceptor() {
        return new LoggingIntercepter();
    }
}

