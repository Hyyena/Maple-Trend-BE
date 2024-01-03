package com.mapletrend.appmaplestampapi.service;

import com.mapletrend.nexonopenapicore.dto.response.BasicResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("기본 정보 조회 서비스 테스트")
@SpringBootTest
class BasicServiceTest {

    @Autowired
    private BasicService basicService;

    @DisplayName("기본 정보 조회")
    @Test
    void shouldReturnBasic() {
        String ocid = "877ce24fcb3c0c914c7d7b382c05fe40";
        String date = "2024-01-02";

        BasicResponse basicResponse = basicService.getBasic(ocid, date);
        System.out.println(basicResponse);
    }
}