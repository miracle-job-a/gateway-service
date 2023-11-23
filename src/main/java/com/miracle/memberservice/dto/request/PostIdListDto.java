package com.miracle.memberservice.dto.request;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/*
* CompanyService에서 받은 공고 id를 user에 count수를 요청하는 Dto
* */
@ToString
@Getter
public class PostIdListDto {

    private final List<Long> id = new ArrayList<>();

}
