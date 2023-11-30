package com.miracle.memberservice.dto.request;

import lombok.Getter;
import lombok.ToString;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ToString
@Getter
public class JobRequestDto {

    private final Set<Long> id;

    public JobRequestDto(List<Integer> idList) {
        Set<Long> objects = new HashSet<>();
        for (Integer integer : idList) {
            objects.add(integer.longValue());
        }
        this.id = objects;
    }
}
