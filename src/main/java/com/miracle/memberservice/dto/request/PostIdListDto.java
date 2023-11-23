package com.miracle.memberservice.dto.request;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
public class PostIdListDto {

    private final List<Long> id = new ArrayList<>();

}
