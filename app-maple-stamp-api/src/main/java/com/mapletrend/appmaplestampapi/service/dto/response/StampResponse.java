package com.mapletrend.appmaplestampapi.service.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Getter
public class StampResponse {

    private final String uuid;
    private final String nexonApiKey;
    private final String ocid;
    private final String invenNickname;
    private final String characterName;
    private final String worldName;
    private final long characterLevel;
    private final String battlePower;
    private final long unionLevel;
    private final String formattedCharacterLevel;
    private final String formattedBattlePower;
    private final String formattedUnionLevel;
    private final String date;
    private final StampImageResponse stampImageResponse;
}
