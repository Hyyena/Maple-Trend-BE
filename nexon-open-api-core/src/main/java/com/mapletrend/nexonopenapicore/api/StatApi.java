package com.mapletrend.nexonopenapicore.api;

import com.mapletrend.nexonopenapicore.config.NexonApiConfig;
import com.mapletrend.nexonopenapicore.dto.request.StatRequest;
import com.mapletrend.nexonopenapicore.dto.response.FinalStatResponse;
import com.mapletrend.nexonopenapicore.dto.response.StatResponse;
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

@Component
@RequiredArgsConstructor
@Log4j2
public class StatApi {

    private final GenericApiClient genericApiClient;
    private final NexonApiConfig nexonApiConfig;

    public StatResponse fetchStat(String ocid, String date) {
        log.info("<스탯 조회 시작> OCID: {}, 날짜: {}", ocid, date);
        StatRequest statRequest = createStatRequest(ocid, date);
        URI requestUri = buildRequestUrl(statRequest);
        HttpEntity<String> entity = genericApiClient.createHttpEntity();

        ResponseEntity<String> response = genericApiClient.executeApiRequest(requestUri, entity);
        log.info("<스탯 조회 종료> OCID: {}, 날짜: {}", ocid, date);
        return parsingStatJson(response.getBody());
    }

    private StatRequest createStatRequest(String ocid, String date) {
        return StatRequest.builder()
                .ocid(ocid)
                .date(date)
                .build();
    }

    private URI buildRequestUrl(StatRequest statRequest) {
        String callbackUrl = nexonApiConfig.getNexonServer() + "/character/stat";
        log.info("callbackUrl: {}", callbackUrl);
        return buildUriComponents(statRequest, callbackUrl).toUri();
    }

    private UriComponents buildUriComponents(StatRequest statRequest, String callbackUrl) {
        return UriComponentsBuilder.fromHttpUrl(callbackUrl)
                .queryParam("ocid", statRequest.getOcid())
                .queryParam("date", statRequest.getDate())
                .build(true);
    }

    private StatResponse parsingStatJson(String response) {
        JSONObject jsonObject = genericApiClient.parseJson(response);
        List<FinalStatResponse> finalStatResponse = new ArrayList<>();
        JSONArray finalStat = (JSONArray) jsonObject.get("final_stat");
        for (Object object : finalStat) {
            JSONObject finalStatObject = (JSONObject) object;
            String statName = (String) finalStatObject.get("stat_name");
            if (statName.equals("전투력")) {
                String statValue = (String) finalStatObject.get("stat_value");
                finalStatResponse.add(FinalStatResponse.builder()
                        .statName(statName)
                        .statValue(statValue)
                        .build());
            }
        }
        return StatResponse.builder()
                .finalStat(finalStatResponse)
                .build();
    }
}
