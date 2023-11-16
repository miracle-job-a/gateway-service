package com.miracle.memberservice.dto.response;

import lombok.Getter;

@Getter
public class ErrorApiResponse extends ApiResponse {

    private final String code;
    private final String exception;

    public ErrorApiResponse(int httpStatus, String message, String code, String exception) {
        super(httpStatus, message);
        this.code = code;
        this.exception = exception;
    }
}