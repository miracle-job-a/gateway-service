package com.miracle.memberservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@ToString
public class UserBaseInfoResponseDto {

    private final String email;
    private final String name;
    private final String phone;
    private final String birth;
    private final String address;
    private final List<StackResponseDto> stackList;
    private final List<JobResponseDto> jobList;

    @Builder
    public UserBaseInfoResponseDto(Object email, Object name, Object phone, Object birth
    , Object address, List<StackResponseDto> stackList, List<JobResponseDto> jobList){
        this.email = String.valueOf(email);
        this.name = String.valueOf(name);
        this.phone = String.valueOf(phone);
        this.birth = String.valueOf(birth);
        this.address = String.valueOf(address);
        this.stackList = stackList;
        this.jobList = jobList;
    }
}
