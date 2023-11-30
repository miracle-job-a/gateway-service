package com.miracle.memberservice.service;

import com.miracle.memberservice.dto.request.LoginDto;
import com.miracle.memberservice.dto.request.ResumeRequestDto;
import com.miracle.memberservice.dto.request.UserJoinDto;
import com.miracle.memberservice.dto.response.*;
import com.miracle.memberservice.util.PageMoveWithMessage;
import com.miracle.memberservice.util.ServiceCall;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<String> duplicateEmail(HttpSession session, String email) {

        ApiResponse response = ServiceCall.get(session, "user", "/user/check-email/" + email);

        if (Boolean.TRUE.equals(response.getData()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getMessage());

        return ResponseEntity.status(response.getHttpStatus()).body(response.getMessage());
    }

    public PageMoveWithMessage formResume(HttpSession session){
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.get(session, "user", "/user/" + userId + "/base-info");


        if(response.getHttpStatus() != 200)
            return new PageMoveWithMessage("/user/resumes", response.getMessage());

        LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) response.getData();

        UserBaseInfoResponseDto info = UserBaseInfoResponseDto.builder()
                .email(data.get("email"))
                .name(data.get("name"))
                .phone(data.get("phone"))
                .birth(data.get("birth"))
                .address(data.get("address"))
                .build();

        return new PageMoveWithMessage("user/resumeForm", info);
    }

    public PageMoveWithMessage addResume(HttpSession session, ResumeRequestDto resumeRequestDto) {
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.post(session, resumeRequestDto, "user", "/user/"+userId+"/resume");
        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("redirect:/v1/user/resume/form", response.getMessage());
        return new PageMoveWithMessage("redirect:/v1/user/resumes");
    }

    /*
    * toolchain {
		languageVersion.set(JavaLanguageVersion.of(17))
	}
	*
	* DB_password=5002;DB_url=jdbc:mysql://localhost:3306/miracle_user;DB_username=root
    * */
}
