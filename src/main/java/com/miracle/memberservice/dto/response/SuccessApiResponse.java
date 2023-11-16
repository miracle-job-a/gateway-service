package com.miracle.memberservice.dto.response;

import lombok.Getter;

@Getter
public class SuccessApiResponse<T> extends ApiResponse {
    private final T data;

    public SuccessApiResponse(int httpStatus, String message, T data) {
        super(httpStatus, message);
        this.data=data;
    }
}
