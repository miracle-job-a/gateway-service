package com.miracle.memberservice.service;

import com.miracle.memberservice.dto.request.LoginDto;
import com.miracle.memberservice.dto.request.ResumeRequestDto;
import com.miracle.memberservice.dto.request.ResumeUpdateRequestDto;
import com.miracle.memberservice.dto.request.UserJoinDto;
import com.miracle.memberservice.dto.response.*;
import com.miracle.memberservice.util.ApiResponseToList;
import com.miracle.memberservice.util.Const;
import com.miracle.memberservice.util.PageMoveWithMessage;
import com.miracle.memberservice.util.ServiceCall;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

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

        return new PageMoveWithMessage("user/resume-form", info);
    }

    public PageMoveWithMessage addResume(HttpSession session, ResumeRequestDto resumeRequestDto) {
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.post(session, resumeRequestDto, "user", "/user/"+userId+"/resume");
        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("redirect:/v1/user/resume/form", response.getMessage());
        return new PageMoveWithMessage("redirect:/v1/user/resumes");
    }

    public PageMoveWithMessage resumeList(HttpSession session){
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.get(session, "user", "/user/"+userId+"/resume");
        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("/v1/user", response.getMessage());

        List<ResumeListResponseDto> resumeList = ApiResponseToList.resumeList(response.getData());

        return new PageMoveWithMessage("user/resumes", resumeList);
    }

    public PageMoveWithMessage getResumeDetail(HttpSession session, Long resumeId) {
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.getParam(session, Const.RequestHeader.USER, "/user/" + userId + "/resume/" + resumeId , "requester","USER");

        LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) response.getData();

        ResumeResponseDto info = ResumeResponseDto.builder()
                .id(Long.valueOf((Integer) data.get("id")))
                .title((String) data.get("title"))
                .photo((String) data.get("photo"))
                .career((Integer) data.get("career"))
                .birth((String) data.get("birth"))
                .phone((String) data.get("phone"))
                .education((String) data.get("education"))
                .gitLink((String) data.get("gitLink"))
                .jobIdSet((ArrayList<Integer>) data.get("jobIdSet"))
                .stackIdSet((ArrayList<Integer>) data.get("stackIdSet"))
                .careerDetailList((List<String>) data.get("careerDetailList"))
                .projectList((List<String>) data.get("projectList"))
                .etcList((List<String>) data.get("etcList"))
                .build();

        return new PageMoveWithMessage("user/resume-detail", info);
    }

    public PageMoveWithMessage deleteResume(HttpSession session, Long resumeId) {
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.delete(session, Const.RequestHeader.USER, "/user/" + userId + "/resume/" + resumeId);

        return new PageMoveWithMessage("redirect:/v1/user/resumes", response.getMessage());
    }

    public PageMoveWithMessage updateResume(HttpSession session, ResumeUpdateRequestDto requestDto, Long resumeId) {
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.put(session, requestDto, "user", "/user/"+userId+"/resume/"+resumeId);
        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("redirect:/v1/user/resume//detail/"+resumeId, response.getMessage());
        return new PageMoveWithMessage("redirect:/v1/user/resume/detail/"+resumeId);
    }

}
