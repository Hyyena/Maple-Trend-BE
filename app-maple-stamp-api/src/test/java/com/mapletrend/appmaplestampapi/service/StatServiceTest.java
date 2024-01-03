package com.mapletrend.appmaplestampapi.service;

import com.mapletrend.nexonopenapicore.dto.response.StatResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("스탯 정보 조회 서비스 테스트")
@SpringBootTest
class StatServiceTest {

    @Autowired
    private StatService statService;

    @DisplayName("스탯 정보 조회")
    @Test
    void shouldReturnStat() {
        String ocid = "877ce24fcb3c0c914c7d7b382c05fe40";
        String date = "2024-01-02";

        StatResponse actualStatResponse = statService.getStat(ocid, date);
        System.out.println(actualStatResponse);
    }
}