package com.miracle.memberservice.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PageMoveWithMessage {
    private final String pageName;
    private final String errorMessage;
}
