package com.mapletrend.appmaplestampapi.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("OCID 조회 서비스 테스트")
@SpringBootTest
class OcidServiceTest {

    @Autowired
    private OcidService ocidService;

    @DisplayName("OCID 조회")
    @Test
    void shouldReturnOcid() {
        String characterName = "엄고별";
        String actualOcid = ocidService.getOcid(characterName);
        System.out.println(actualOcid);
    }
}