package com.mapletrend.nexonopenapicore.api;

import com.mapletrend.nexonopenapicore.config.NexonApiConfig;
import com.mapletrend.nexonopenapicore.dto.request.BasicRequest;
import com.mapletrend.nexonopenapicore.dto.response.BasicResponse;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
@Log4j2
public class BasicApi {

    private final GenericApiClient genericApiClient;
    private final NexonApiConfig nexonApiConfig;

    public BasicResponse fetchBasic(String ocid, String date) {
        log.info("<기본 정보 조회 시작> OCID: {}, 날짜: {}", ocid, date);
        BasicRequest basicRequest = createBasicRequest(ocid, date);
        URI requestUri = buildRequestUrl(basicRequest);
        HttpEntity<String> entity = genericApiClient.createHttpEntity();

        ResponseEntity<String> response = genericApiClient.executeApiRequest(requestUri, entity);
        log.info("<기본 정보 조회 종료> OCID: {}, 날짜: {}", ocid, date);
        return parsingBasicJson(response.getBody());
    }

    private BasicRequest createBasicRequest(String ocid, String date) {
        return BasicRequest.builder()
                .ocid(ocid)
                .date(date)
                .build();
    }

    private URI buildRequestUrl(BasicRequest basicRequest) {
        String callbackUrl = nexonApiConfig.getNexonServer() + "/character/basic";
        log.info("callbackUrl: {}", callbackUrl);
        return buildUriComponents(basicRequest, callbackUrl).toUri();
    }

    private UriComponents buildUriComponents(BasicRequest basicRequest, String callbackUrl) {
        return UriComponentsBuilder.fromHttpUrl(callbackUrl)
                .queryParam("ocid", basicRequest.getOcid())
                .queryParam("date", basicRequest.getDate())
                .build(true);
    }

    private BasicResponse parsingBasicJson(String response) {
        JSONObject jsonObject = genericApiClient.parseJson(response);
        String characterName = (String) jsonObject.get("character_name");
        long characterLevel = (long) jsonObject.get("character_level");
        return BasicResponse.builder()
                .characterName(characterName)
                .characterLevel(characterLevel)
                .build();
    }
}
