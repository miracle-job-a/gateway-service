package com.miracle.memberservice.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class IdEditDto {
    private final List<Integer> idList;
}
