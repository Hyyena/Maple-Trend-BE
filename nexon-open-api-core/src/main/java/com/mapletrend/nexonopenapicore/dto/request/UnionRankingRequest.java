package com.mapletrend.nexonopenapicore.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@Builder
@Getter
public class UnionRankingRequest {

    private String ocid;
    private String date;
    private String worldName;
}
