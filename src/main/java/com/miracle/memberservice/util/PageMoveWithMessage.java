package com.miracle.memberservice.util;

import com.miracle.memberservice.dto.response.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class PageMoveWithMessage {
    private final String pageName;
    private final String errorMessage;
    private Object id;
}
