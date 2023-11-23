package com.miracle.memberservice.dto.response;

import lombok.*;

@Getter
@ToString
@RequiredArgsConstructor
public class ApiResponse {
    private final int httpStatus;
    private final String message;
    private final String code;
    private final String exception;
    private final Object data;

    public ApiResponse() {
        this.httpStatus = 0;
        this.message = null;
        this.code = null;
        this.exception = null;
        this.data = null;
    }
}
