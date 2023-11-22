package com.miracle.memberservice.service;

import com.miracle.memberservice.dto.request.LoginDto;
import com.miracle.memberservice.dto.request.UserJoinDto;
import com.miracle.memberservice.dto.response.ApiResponse;
import com.miracle.memberservice.dto.response.UserLoginResponseDto;
import com.miracle.memberservice.util.PageMoveWithMessage;
import com.miracle.memberservice.util.ServiceCall;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;

@Service
@Slf4j
public class UserService {

    /**
     * 유저 회원가입 요청 API
     *
     * @param userJoinDto 회원가입 폼 태그에 있는 정보들
     * @param session     Token값 추출을 위한 세션
     * @return 회원가입 성공하면 메인 페이지 반환, 실패하면 에러메시지와 함께 다시 user-join 반환
     */
    public PageMoveWithMessage join(UserJoinDto userJoinDto, HttpSession session) {

        ApiResponse response = ServiceCall.post(session, userJoinDto, "user", "/user/join");

        if (response.getHttpStatus() != 200) return new PageMoveWithMessage("guest/user-join", response.getMessage());

        return new PageMoveWithMessage("index");
    }

    public PageMoveWithMessage login(LoginDto loginDto, HttpSession session) {

        ApiResponse response = ServiceCall.post(session, loginDto, "user", "/user/login");

        if (response.getHttpStatus() != 200) return new PageMoveWithMessage("guest/user-login", response.getMessage());

        LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) response.getData();

        UserLoginResponseDto dto = UserLoginResponseDto.builder()
                .id(data.get("id"))
                .email(data.get("email"))
                .name(data.get("name"))
                .build();

        return new PageMoveWithMessage("index", dto);
    }
}
