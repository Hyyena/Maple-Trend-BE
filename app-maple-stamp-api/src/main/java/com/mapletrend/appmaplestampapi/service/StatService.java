package com.mapletrend.appmaplestampapi.service;

import com.mapletrend.nexonopenapicore.api.StatApi;
import com.mapletrend.nexonopenapicore.dto.response.StatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatService {

    private final StatApi statApi;

    public StatResponse getStat(String ocid, String date) {
        return statApi.fetchStat(ocid, date);
    }
}
