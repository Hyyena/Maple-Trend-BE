package com.mapletrend.appmaplestampapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(org.springframework.web.servlet.config.annotation.CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3003", "https://www.mapletrend.co.kr")
                .allowedMethods("GET", "POST")
                .allowCredentials(true);
    }
}
