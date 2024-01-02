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
public class StarforceRequest {

    private String nexonApiKey;
    private String count;
    private String date;
    private String cursor;
}
