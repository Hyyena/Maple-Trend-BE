package com.mapletrend.nexonopenapicore.api;

import com.mapletrend.nexonopenapicore.config.NexonApiConfig;
import com.mapletrend.nexonopenapicore.dto.request.OcidRequest;
import com.mapletrend.nexonopenapicore.dto.response.OcidResponse;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
public class OcidApi {

    private final GenericApiClient genericApiClient;
    private final NexonApiConfig nexonApiConfig;

    public OcidResponse fetchOcid(String characterName) {
        log.info("<OCID 조회 시작> 캐릭터명: {}", characterName);
        OcidRequest ocidRequest = createOcidRequest(characterName);
        URI requestUri = buildRequestUrl(ocidRequest);
        HttpEntity<String> entity = genericApiClient.createHttpEntity();

        ResponseEntity<String> response = genericApiClient.executeApiRequest(requestUri, entity);
        log.info("<OCID 조회 종료> 캐릭터명: {}", characterName);
        return parsingOcidJson(response.getBody());
    }

    private OcidRequest createOcidRequest(String characterName) {
        return OcidRequest.builder().characterName(characterName).build();
    }

    private URI buildRequestUrl(OcidRequest ocidRequest) {
        String callbackUrl = nexonApiConfig.getNexonServer() + "/id";
        log.info("callbackUrl: {}", callbackUrl);
        return buildUriComponents(ocidRequest, callbackUrl).toUri();
    }

    private UriComponents buildUriComponents(OcidRequest ocidRequest, String callbackUrl) {
        String encodedCharacterName = UriUtils.encodeQueryParam(ocidRequest.getCharacterName(), "UTF-8");
        return UriComponentsBuilder.fromHttpUrl(callbackUrl)
                .queryParam("character_name", encodedCharacterName)
                .build(true);
    }

    private OcidResponse parsingOcidJson(String response) {
        JSONObject jsonObject = genericApiClient.parseJson(response);
        String ocid = (String) jsonObject.get("ocid");
        return OcidResponse.builder().ocid(ocid).build();
    }
}
