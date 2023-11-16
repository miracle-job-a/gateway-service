package com.miracle.memberservice.dto.request;

import lombok.Getter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@ToString
@Getter
public class UserJoinDto {
    private final String email;
    private final String name;
    private final String password;
    private final String phone;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate birth;
    private final String address;

    public UserJoinDto(String email, String name, String password, String phone, LocalDate birth, String address, String detailAddress) {
        this.email = email;
        this.name = name;
        String[] split = password.split("-");
        this.password = split[0]+split[1]+split[2];
        this.phone = phone;
        this.birth = birth;
        this.address = address + " " + detailAddress;
    }
}
