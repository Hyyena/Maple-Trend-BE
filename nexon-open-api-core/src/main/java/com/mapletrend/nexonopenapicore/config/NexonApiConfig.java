package com.mapletrend.nexonopenapicore.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class NexonApiConfig {

    @Value("${nexon.server}")
    private String nexonServer;

    @Value("${nexon.api}")
    private String nexonApiKey;
}
