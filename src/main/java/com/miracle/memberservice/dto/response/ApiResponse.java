package com.miracle.memberservice.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ApiResponse {
    private final int httpStatus;
    private final String message;
}
