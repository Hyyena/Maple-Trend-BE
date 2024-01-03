package com.mapletrend.nexonopenapicore.api;

import com.mapletrend.nexonopenapicore.config.NexonApiConfig;
import com.mapletrend.nexonopenapicore.dto.request.UnionRankingRequest;
import com.mapletrend.nexonopenapicore.dto.response.UnionRankingDetailResponse;
import com.mapletrend.nexonopenapicore.dto.response.UnionRankingResponse;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

@Component
@RequiredArgsConstructor
@Log4j2
public class UnionRankingApi {

    private final GenericApiClient genericApiClient;
    private final NexonApiConfig nexonApiConfig;

    public UnionRankingResponse fetchUnionRanking(String ocid, String date, String worldName) {
        UnionRankingRequest unionRankingRequest = createUnionRankingRequest(ocid, date, worldName);
        URI requestUri = buildRequestUrl(unionRankingRequest);
        HttpEntity<String> entity = genericApiClient.createHttpEntity();

        ResponseEntity<String> response = genericApiClient.executeApiRequest(requestUri, entity);
        log.info("<유니온 랭킹 조회 완료> OCID: {}, 날짜: {}, 월드명: {}", ocid, date, worldName);
        return parsingUnionRankingJson(response.getBody());
    }

    private UnionRankingRequest createUnionRankingRequest(String ocid, String date, String worldName) {
        return UnionRankingRequest.builder()
                .ocid(ocid)
                .date(date)
                .worldName(worldName)
                .build();
    }

    private URI buildRequestUrl(UnionRankingRequest unionRankingRequest) {
        String callbackUrl = nexonApiConfig.getNexonServer() + "/ranking/union";
        log.info("callbackUrl: {}", callbackUrl);
        return buildUriComponents(unionRankingRequest, callbackUrl).toUri();
    }

    private UriComponents buildUriComponents(UnionRankingRequest unionRankingRequest, String callbackUrl) {
        String encodedWorldName = UriUtils.encodeQueryParam(unionRankingRequest.getWorldName(), "UTF-8");
        return UriComponentsBuilder.fromHttpUrl(callbackUrl)
                .queryParam("ocid", unionRankingRequest.getOcid())
                .queryParam("date", unionRankingRequest.getDate())
                .queryParam("world_name", encodedWorldName)
                .build(true);
    }

    private UnionRankingResponse parsingUnionRankingJson(String response) {
        JSONObject jsonObject = genericApiClient.parseJson(response);
        List<UnionRankingDetailResponse> unionRankingDetail = new ArrayList<>();
        JSONArray rankings = (JSONArray) jsonObject.get("ranking");
        for (Object object : rankings) {
            JSONObject ranking = (JSONObject) object;
            UnionRankingDetailResponse unionRankingDetailResponse = UnionRankingDetailResponse.builder()
                    .date((String) ranking.get("date"))
                    .ranking((long) ranking.get("ranking"))
                    .characterName((String) ranking.get("character_name"))
                    .unionLevel((long) ranking.get("union_level"))
                    .build();
            unionRankingDetail.add(unionRankingDetailResponse);
        }
        return UnionRankingResponse.builder().ranking(unionRankingDetail).build();
    }
}
