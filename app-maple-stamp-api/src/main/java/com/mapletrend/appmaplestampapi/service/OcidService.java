package com.mapletrend.appmaplestampapi.service;

import com.mapletrend.nexonopenapicore.api.OcidApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OcidService {

    private final OcidApi ocidApi;

    public String getOcid(String characterName) {
        return ocidApi.fetchOcid(characterName).getOcid();
    }
}
