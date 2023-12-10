package com.miracle.memberservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@ToString
public class UserInfoResponseDto {

    private final Long id;
    private final String name;
    private final String birth;
    private final String phone;
    private final String address;
    private final ArrayList<Integer> stackIdSet;

    @Builder
    public UserInfoResponseDto(Long id, String name, String birth, String phone, String address, ArrayList<Integer> stackIdSet) {
        this.id = id;
        this.name = name;
        this.birth = birth;
        this.phone = phone;
        this.address = address;
        this.stackIdSet = stackIdSet;
    }
}
