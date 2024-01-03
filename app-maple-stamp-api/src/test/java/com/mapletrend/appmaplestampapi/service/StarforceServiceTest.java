package com.mapletrend.appmaplestampapi.service;

import com.mapletrend.nexonopenapicore.config.NexonApiConfig;
import com.mapletrend.nexonopenapicore.dto.response.StarforceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("스타포스 정보 조회 서비스 테스트")
@SpringBootTest
class StarforceServiceTest {

    private final String count = "10";
    private final String date = "2024-01-02";

    @Autowired
    private NexonApiConfig nexonApiConfig;

    @Autowired
    private StarforceService starforceService;

    @DisplayName("스타포스 정보 조회")
    @Test
    void shouldReturnStarforceResponse() {
        StarforceResponse actualStarforceResponse = starforceService.getStarforce(nexonApiConfig.getNexonApiKey(),
                count, date);
        System.out.println(actualStarforceResponse);
    }
}