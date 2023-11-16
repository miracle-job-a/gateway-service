package com.miracle.memberservice.dto.response;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ServiceApiResponse<T> extends ApiResponse{
    private final String code;
    private final String exception;
    private final T data;

    public ServiceApiResponse(int httpStatus, String message, String code, String exception, T data) {
        super(httpStatus, message);
        this.code = code;
        this.exception = exception;
        this.data = data;
    }
}
