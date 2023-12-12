package com.miracle.memberservice.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@ToString
public class UserUpdateInfoRequestDto {

    private final Set<Long> stackIdSet;
    private final String phone;
    private final String address;
    @Setter
    private String password;

    public UserUpdateInfoRequestDto(String password, Set<Long> stackIdSet, String phone, String address, String detailAddress) {
        this.password = password;
        this.stackIdSet = stackIdSet;
        String[] split = phone.split("-");
        this.phone = split[0]+split[1]+split[2];
        this.address = address + " " + detailAddress;
    }

}
