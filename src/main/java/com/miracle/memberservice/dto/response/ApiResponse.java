package com.miracle.memberservice.dto.response;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private int httpStatus;
    private String message;
    private String code;
    private String exception;
    private T data;
}
