package com.mapletrend.appmaplestampapi.service;

import com.mapletrend.nexonopenapicore.api.UnionRankingApi;
import com.mapletrend.nexonopenapicore.dto.response.UnionRankingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnionRankingService {

    private final UnionRankingApi unionRankingApi;

    public UnionRankingResponse getUnionRanking(String ocid, String date, String worldName) {
        return unionRankingApi.fetchUnionRanking(ocid, date, worldName);
    }
}
