package com.mapletrend.appmaplestampapi.service;

import com.mapletrend.nexonopenapicore.api.StarforceApi;
import com.mapletrend.nexonopenapicore.dto.response.StarforceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StarforceService {

    private final StarforceApi starforceApi;

    public StarforceResponse getStarforce(String count, String date) {
        return starforceApi.fetchStarforceHistory(count, date);
    }
}
