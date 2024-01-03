package com.mapletrend.appmaplestampapi.service;

import com.mapletrend.nexonopenapicore.dto.response.UnionRankingResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("유니온 랭킹 조회 서비스 테스트")
@SpringBootTest
class UnionRankingServiceTest {

    @Autowired
    private UnionRankingService unionRankingService;

    @DisplayName("유니온 랭킹 조회")
    @Test
    void shouldReturnUnionRankingResponse() {
        String ocid = "877ce24fcb3c0c914c7d7b382c05fe40";
        String date = "2024-01-02";
        String worldName = "루나";

        UnionRankingResponse unionRankingResponse = unionRankingService.getUnionRanking(ocid, date, worldName);
        System.out.println(unionRankingResponse);
    }
}