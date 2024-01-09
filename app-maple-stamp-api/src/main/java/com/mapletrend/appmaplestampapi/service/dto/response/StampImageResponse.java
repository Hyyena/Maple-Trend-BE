package com.mapletrend.appmaplestampapi.service.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Getter
public class StampImageResponse {

    private final byte[] stampImage;
}
