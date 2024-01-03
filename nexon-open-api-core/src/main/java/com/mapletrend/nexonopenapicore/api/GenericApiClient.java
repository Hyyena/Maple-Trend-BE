package com.mapletrend.nexonopenapicore.api;

import com.mapletrend.nexonopenapicore.config.NexonApiConfig;
import com.mapletrend.nexonopenapicore.exception.ApiDataParsingException;
import com.mapletrend.nexonopenapicore.exception.ApiRequestException;
import java.net.URI;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Log4j2
public class GenericApiClient {

    private final RestTemplate restTemplate;
    private final NexonApiConfig nexonApiConfig;

    protected ResponseEntity<String> executeApiRequest(URI url, HttpEntity<String> entity) {
        try {
            return restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {
            });
        } catch (Exception e) {
            throw new ApiRequestException("API 요청 중 오류가 발생했습니다.", e);
        }
    }

    protected HttpEntity<String> createHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("x-nxopen-api-key", nexonApiConfig.getNexonApiKey());
        return new HttpEntity<>(headers);
    }

    protected JSONObject parseJson(String response) {
        try {
            JSONParser jsonParser = new JSONParser();
            return (JSONObject) jsonParser.parse(response);
        } catch (ParseException e) {
            throw new ApiDataParsingException("JSON 파싱 중 오류가 발생했습니다.", e);
        }
    }
}
