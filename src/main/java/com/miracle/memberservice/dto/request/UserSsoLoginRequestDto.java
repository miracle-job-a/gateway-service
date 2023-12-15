package com.miracle.memberservice.dto.request;

import lombok.Getter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@ToString
public class UserSsoLoginRequestDto {

    private final String email;
    private final String name;
    private final String password;
    private final String uid;
    private final String phone;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate birth;
    private final String address;
    private final String detailAddress;
    private final String memberType = "user";
    private final String postId;
    private final String companyId;
    private final String postType;
    private final String sso;

    public UserSsoLoginRequestDto(String email, String name, String password, String uid,
                                  String phone, LocalDate birth, String address,
                                  String detailAddress, String postId, String companyId, String postType, String sso) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.uid = uid;
        this.phone = phone;
        this.birth = birth;
        this.address = address;
        this.detailAddress = detailAddress;
        this.postId = postId;
        this.companyId = companyId;
        this.postType = postType;
        this.sso = sso;
    }
}
