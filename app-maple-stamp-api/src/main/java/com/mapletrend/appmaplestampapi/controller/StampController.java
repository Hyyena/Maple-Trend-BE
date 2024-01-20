package com.mapletrend.appmaplestampapi.controller;

import com.mapletrend.appmaplestampapi.service.BasicService;
import com.mapletrend.appmaplestampapi.service.OcidService;
import com.mapletrend.appmaplestampapi.service.StampService;
import com.mapletrend.appmaplestampapi.service.StarforceService;
import com.mapletrend.appmaplestampapi.service.StatService;
import com.mapletrend.appmaplestampapi.service.UnionRankingService;
import com.mapletrend.appmaplestampapi.service.dto.request.StampRequest;
import com.mapletrend.appmaplestampapi.service.dto.response.ApiResponse;
import com.mapletrend.appmaplestampapi.service.dto.response.StampImageResponse;
import com.mapletrend.maplestampdomainmariadb.repository.StampRepository;
import com.mapletrend.nexonopenapicore.dto.response.BasicResponse;
import com.mapletrend.nexonopenapicore.dto.response.FinalStatResponse;
import com.mapletrend.nexonopenapicore.dto.response.StarforceHistoryResponse;
import com.mapletrend.nexonopenapicore.dto.response.StarforceResponse;
import com.mapletrend.nexonopenapicore.dto.response.StatResponse;
import com.mapletrend.nexonopenapicore.dto.response.UnionRankingDetailResponse;
import com.mapletrend.nexonopenapicore.dto.response.UnionRankingResponse;
import io.micrometer.core.annotation.Timed;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/v1/maple")
public class StampController {

    private final StampService stampService;
    private final StarforceService starforceService;
    private final OcidService ocidService;
    private final UnionRankingService unionRankingService;
    private final BasicService basicService;
    private final StatService statService;

    private final StampRepository stampRepository;

    @Timed(value = "createStamp")
    @PostMapping("/stamp")
    public ApiResponse<Object> createStamp(
            @RequestParam("inven_nickname") String invenNickname,
            @RequestParam("maple_nickname") String mapleNickname,
            @RequestBody StampRequest stampRequest
    ) {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.KOREA);
        String formattedToday = today.format(formatter);
        String apiUpdateToday = getApiUpdateToday();
        String formattedYesterday = yesterday.format(formatter);

        StarforceResponse starforceResponse = starforceService.getStarforce(stampRequest.getNexonApiKey(), "10",
                formattedToday);
        List<StarforceHistoryResponse> starforceHistoryResponse = starforceResponse.getStarforceHistory();
        if (starforceHistoryResponse != null && !starforceHistoryResponse.isEmpty()) {
            log.info("starforceHistoryResponse: {}", starforceHistoryResponse);
            String characterName = starforceHistoryResponse.get(0).getCharacterName();
            String worldName = starforceHistoryResponse.get(0).getWorldName();
            String dateCreate = starforceHistoryResponse.get(0).getDateCreate();
            log.info("characterName: {}, worldName: {}, dateCreate: {}", characterName, worldName, dateCreate);

            if (!characterName.equals(mapleNickname)) {
                return ApiResponse.<Object>builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .message("스타포스를 시도한 본인 캐릭터가 아닙니다.")
                        .build();
            }

            String ocid = ocidService.getOcid(characterName);
            log.info("ocid: {}", ocid);

            UnionRankingResponse unionRankingResponse = unionRankingService
                    .getUnionRanking(ocid, formattedYesterday, worldName);
            log.info("unionRankingResponse: {}", unionRankingResponse);
            List<UnionRankingDetailResponse> unionRankingDetailResponse = unionRankingResponse.getRanking();
            if (unionRankingDetailResponse != null && !unionRankingDetailResponse.isEmpty()) {
                log.info("unionRankingDetailResponse: {}", unionRankingDetailResponse);
                String mainCharacterName = unionRankingDetailResponse.get(0).getCharacterName();
                long unionLevel = unionRankingDetailResponse.get(0).getUnionLevel();
                log.info("mainCharacterName: {}, unionLevel: {}", mainCharacterName, unionLevel);

                // TODO: 메인캐릭터와 다른 경우 메인캐릭터로 ocid 조회
                if (!mainCharacterName.equals(characterName)) {
                    return ApiResponse.<Object>builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("메인 캐릭터가 아닙니다.")
                            .build();
                }

                BasicResponse basicResponse = basicService.getBasic(ocid, apiUpdateToday);
                log.info("basicResponse: {}", basicResponse);
                long characterLevel = basicResponse.getCharacterLevel();
                log.info("characterLevel: {}", characterLevel);

                StatResponse statResponse = statService.getStat(ocid, apiUpdateToday);
                log.info("statResponse: {}", statResponse);
                List<FinalStatResponse> finalStatResponse = statResponse.getFinalStat();
                if (finalStatResponse != null && !finalStatResponse.isEmpty()) {
                    log.info("finalStatResponse: {}", finalStatResponse);
                    String battlePower = finalStatResponse.get(0).getStatValue();
                    log.info("battlePower: {}", battlePower);

                    String formattedCharacterLevel = formatCharacterLevel(characterLevel);
                    String formattedBattlePower = formatBattlePower(battlePower);
                    String formattedUnionLevel = formatUnionLevel(unionLevel);

                    log.info("formattedCharacterLevel: {}", formattedCharacterLevel);
                    log.info("formattedBattlePower: {}", formattedBattlePower);
                    log.info("formattedUnionLevel: {}", formattedUnionLevel);

                    String uuid = UUID.randomUUID().toString();

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("invenNickname", invenNickname);
                    jsonObject.put("formattedCharacterLevel", formattedCharacterLevel);
                    jsonObject.put("formattedBattlePower", formattedBattlePower);
                    jsonObject.put("formattedUnionLevel", formattedUnionLevel);
                    jsonObject.put("uuid", uuid);
                    jsonObject.put("date", formattedToday);

                    jsonObject.put("characterName", characterName);
                    jsonObject.put("characterLevel", characterLevel);
                    jsonObject.put("battlePower", battlePower);
                    jsonObject.put("unionLevel", unionLevel);
                    jsonObject.put("worldName", worldName);
                    jsonObject.put("ocid", ocid);
                    jsonObject.put("nexonApiKey", stampRequest.getNexonApiKey());

                    byte[] stampImage = stampService.createStampImage(jsonObject);
                    log.info(stampImage);

                    return ApiResponse.<Object>builder()
                            .statusCode(HttpStatus.CREATED.value())
                            .message("스탬프 생성 성공")
                            .data(stampImage)
                            .build();
                }
            } else {
                return ApiResponse.<Object>builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .message("인장 발급 실패")
                        .build();
            }
        }

        return ApiResponse.<Object>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("스탬프 생성 성공")
                .build();
    }

    private String formatCharacterLevel(long characterLevel) {
        if (characterLevel < 260) {
            return "메린이";
        } else {
            return ((characterLevel / 5) * 5) + "+";
        }
    }

    private String formatBattlePower(String battlePower) {
        long power = Long.parseLong(battlePower);

        if (power < 5_000_000) {
            return "메린이";
        } else if (power < 10_000_000) {
            return "500만+";
        } else if (power < 100_000_000) {
            long millions = (power / 10_000_000);
            return millions + "000만+";
        } else {
            long billions = power / 100_000_000;
            long millions = (power % 100_000_000) / 10_000_000;

            if (millions == 0) {
                return billions + "억+";
            } else {
                return billions + "억 " + millions + "000만+";
            }
        }
    }

    private String formatUnionLevel(long unionLevel) {
        return (unionLevel / 500 * 500) + "+";
    }

    private String getApiUpdateToday() {
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime apiUpdateTime = LocalDateTime.of(today, LocalTime.of(1, 5));
        LocalDate queryDate;
        if (now.isBefore(apiUpdateTime)) {
            queryDate = today.minusDays(2);
        } else {
            queryDate = today.minusDays(1);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.KOREA);
        return queryDate.format(formatter);
    }

    @GetMapping("/stamp/{invenNickname}")
    @Timed(value = "findStampByInvenNickname")
    public ApiResponse<StampImageResponse> findStampByInvenNickname(
            @PathVariable("invenNickname") String invenNickname
    ) {
        byte[] stampImage = stampService.getStampImage(invenNickname);
        if (stampImage.length == 0) {
            return ApiResponse.<StampImageResponse>fail(
                    HttpStatus.NOT_FOUND.value(),
                    "스탬프 조회 실패"
            );
        }

        return ApiResponse.<StampImageResponse>success(
                "스탬프 조회 성공",
                StampImageResponse.builder()
                        .stampImage(stampImage)
                        .build()
        );
    }
}