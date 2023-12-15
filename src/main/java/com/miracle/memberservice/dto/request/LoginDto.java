package com.miracle.memberservice.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@ToString
@Getter
public class LoginDto {
    @Setter
    private String email;
    private final String password;
    private final String memberType;
    private final String postId;
    private final String companyId;
    private final String postType;

    public LoginDto(String email, String password, String memberType, String postId, String companyId, String postType) {
        this.email = email;
        this.password = password;
        this.memberType = memberType;
        this.postId = postId;
        this.companyId = companyId;
        this.postType = postType;
    }
}
