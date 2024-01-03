package com.mapletrend.appmaplestampapi.service;

import com.mapletrend.nexonopenapicore.api.BasicApi;
import com.mapletrend.nexonopenapicore.dto.response.BasicResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicService {

    private final BasicApi basicApi;

    public BasicResponse getBasic(String ocid, String date) {
        return basicApi.fetchBasic(ocid, date);
    }
}
