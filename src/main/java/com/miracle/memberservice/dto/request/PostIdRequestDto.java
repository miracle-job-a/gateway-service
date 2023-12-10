package com.miracle.memberservice.dto.request;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@ToString
@Getter
public class PostIdRequestDto {

    private final Set<Long> postId;

    public PostIdRequestDto(Set<Long> postIdList) {
        Set<Long> objects = new HashSet<>();
        for (Long integer : postIdList) {
            objects.add(integer.longValue());
        }
        this.postId = objects;
    }

}
