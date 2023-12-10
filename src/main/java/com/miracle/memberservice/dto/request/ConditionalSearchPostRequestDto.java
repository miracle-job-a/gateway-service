package com.miracle.memberservice.dto.request;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@ToString
@EqualsAndHashCode
public class ConditionalSearchPostRequestDto {

    private final Set<Long> stackIdSet;
    private final Integer career;
    private final Set<Long> jobIdSet;
    private final Set<String> addressSet;
    private final Boolean includeEnded;

    @Builder
    public ConditionalSearchPostRequestDto(Set<Long> stackIdSet, Integer career, Set<Long> jobIdSet, Set<String> addressSet, boolean includeEnded) {
        this.stackIdSet = stackIdSet;
        this.career = career;
        this.jobIdSet = jobIdSet;
        this.addressSet = addressSet;
        this.includeEnded = includeEnded;
    }

    public ConditionalSearchPostRequestDto() {
        this.stackIdSet = new HashSet<>();
        this.career = 0;
        this.jobIdSet = new HashSet<>();
        this.addressSet = new HashSet<>();
        this.includeEnded = false;
    }


}
