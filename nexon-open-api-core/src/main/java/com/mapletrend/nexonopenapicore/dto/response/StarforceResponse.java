package com.mapletrend.nexonopenapicore.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@Builder
@Getter
@ToString
public class StarforceResponse {

    private Long count;
    private List<StarforceHistoryResponse> starforceHistory;
}
