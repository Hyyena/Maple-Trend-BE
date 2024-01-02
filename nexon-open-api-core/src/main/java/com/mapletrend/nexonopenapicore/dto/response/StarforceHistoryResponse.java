package com.mapletrend.nexonopenapicore.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Builder
@Getter
@ToString
public class StarforceHistoryResponse {

    private String id;
    private String characterName;
    private String worldName;
    private String dateCreate;
}
