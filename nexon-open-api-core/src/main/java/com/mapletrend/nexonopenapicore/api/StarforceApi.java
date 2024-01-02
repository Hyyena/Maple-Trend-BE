package com.mapletrend.nexonopenapicore.api;

import com.mapletrend.nexonopenapicore.config.NexonApiConfig;
import com.mapletrend.nexonopenapicore.dto.request.StarforceRequest;
import com.mapletrend.nexonopenapicore.dto.response.StarforceHistoryResponse;
import com.mapletrend.nexonopenapicore.dto.response.StarforceResponse;
import com.mapletrend.nexonopenapicore.exception.ApiDataParsingException;
import com.mapletrend.nexonopenapicore.exception.ApiRequestException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
@Log4j2
public class StarforceApi {

    private final RestTemplate restTemplate;
    private final NexonApiConfig nexonApiConfig;

    public StarforceResponse fetchStarforceHistory(String count, String date) {
        log.info("<스타포스 정보 조회 시작> 조회 개수: {}, 조회 날짜: {}", count, date);
        StarforceRequest starforceRequest = createStarforceRequest(count, date);
        URI requestUri = buildRequestUrl(starforceRequest);
        HttpEntity<String> entity = createHttpEntity();

        ResponseEntity<String> response = executeApiRequest(requestUri, entity);
        log.info("<스타포스 정보 조회 종료> 조회 개수: {}, 조회 날짜: {}", count, date);
        return parsingStarforceJson(response.getBody());
    }

    protected StarforceRequest createStarforceRequest(String count, String date) {
        return StarforceRequest.builder().count(count).date(date).build();
    }

    private URI buildRequestUrl(StarforceRequest starforceRequest) {
        String callbackUrl = nexonApiConfig.getNexonServer() + "/history/starforce";
        log.info("callbackUrl: {}", callbackUrl);
        return buildUriComponents(starforceRequest, callbackUrl).toUri();
    }

    private UriComponents buildUriComponents(StarforceRequest starforceRequest, String callbackUrl) {
        return UriComponentsBuilder.fromHttpUrl(callbackUrl)
                .queryParam("count", starforceRequest.getCount())
                .queryParam("date", starforceRequest.getDate())
                .build(true);
    }

    private HttpEntity<String> createHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("x-nxopen-api-key", nexonApiConfig.getNexonApiKey());

        return new HttpEntity<>(headers);
    }

    private ResponseEntity<String> executeApiRequest(URI requestUri, HttpEntity<String> entity) {
        try {
            return restTemplate.exchange(requestUri, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {
            });
        } catch (Exception e) {
            throw new ApiRequestException("executeApiRequest error", e);
        }
    }

    private StarforceResponse parsingStarforceJson(String response) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(response);
            Long count = (Long) jsonObject.get("count");
            List<StarforceHistoryResponse> starforceHistory = new ArrayList<>();
            JSONArray starforceHistoryDetatil = (JSONArray) jsonObject.get("starforce_history");

            for (Object object : starforceHistoryDetatil) {
                JSONObject history = (JSONObject) object;
                StarforceHistoryResponse starforceHistoryResponse = StarforceHistoryResponse.builder()
                        .id((String) history.get("id"))
                        .characterName((String) history.get("character_name"))
                        .worldName((String) history.get("world_name"))
                        .dateCreate((String) history.get("date_create"))
                        .build();
                starforceHistory.add(starforceHistoryResponse);
            }

            return StarforceResponse.builder()
                    .count(count)
                    .starforceHistory(starforceHistory)
                    .build();
        } catch (ParseException e) {
            throw new ApiDataParsingException("parsingStarforceJson error", e);
        }
    }
}
