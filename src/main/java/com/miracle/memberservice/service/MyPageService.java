package com.miracle.memberservice.service;

import com.miracle.memberservice.dto.request.InterviewRequestDto;
import com.miracle.memberservice.dto.request.QnaDto;
import com.miracle.memberservice.dto.response.ApiResponse;
import com.miracle.memberservice.dto.response.ApplicationLetterListResponseDto;
import com.miracle.memberservice.dto.response.CoverLetterInApplicationLetterResponseDto;
import com.miracle.memberservice.dto.response.ResumeInApplicationLetterResponseDto;
import com.miracle.memberservice.util.ApiResponseToList;
import com.miracle.memberservice.util.Const;
import com.miracle.memberservice.util.PageMoveWithMessage;
import com.miracle.memberservice.util.ServiceCall;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;

@Service
@Slf4j
public class MyPageService {

    // 지원현황 목록 불러오기
    public PageMoveWithMessage applicationLetterList(HttpSession session, int startPage){
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.getUserParamList(session, Const.RequestHeader.USER, "/user/" + userId + "/application-letter", startPage, startPage + 4);

        if (response.getHttpStatus() != 200) return new PageMoveWithMessage("/v1", response.getMessage());

        List<List<ApplicationLetterListResponseDto>> letter = ApiResponseToList.applicationLetterList(response.getData());
        return new PageMoveWithMessage("user/apply-list", letter);
    }


    // 지원 이력서 조회하기
    public PageMoveWithMessage resumeInApplicationLetterDetail(HttpSession session, Long applicationLetterId){
        Long userId = (Long) session.getAttribute("id");

        ApiResponse response = ServiceCall.get(session, Const.RequestHeader.USER, "/user/"+userId+"/application-letter/"+applicationLetterId+"/resume");

        LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) response.getData();

        ResumeInApplicationLetterResponseDto dto = ResumeInApplicationLetterResponseDto.builder()
                .resumeTitle((String) data.get("resumeTitle"))
                .userName((String) data.get("userName"))
                .userEmail((String) data.get("userEmail"))
                .userCareer((Integer) data.get("userCareer"))
                .userBirth((String) data.get("userBirth"))
                .userPhone((String) data.get("userPhone"))
                .userAddress((String) data.get("userAddress"))
                .userJob((String) data.get("userJob"))
                .userStackIdSet((ArrayList<Integer>) data.get("userStackIdSet"))
                .userEducation((String) data.get("userEducation"))
                .userGitLink((String) data.get("userGitLink"))
                .userCareerDetailList((List<String>) data.get("userCareerDetailList"))
                .userProjectList((List<String>) data.get("userProjectList"))
                .userEtcList((List<String>) data.get("userEtcList"))
                .build();

        return new PageMoveWithMessage("user/submitted-resume", dto);
    }

    // 지원 자소서 조회하기
    public PageMoveWithMessage coverLetterInApplicationLetterDetail(HttpSession session, Long applicationLetterId){
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.get(session, Const.RequestHeader.USER, "/user/" + userId + "/application-letter/" + applicationLetterId + "/cover-letter");

        LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) response.getData();

        CoverLetterInApplicationLetterResponseDto letter = CoverLetterInApplicationLetterResponseDto.builder()
                .coverLetterTitle((String) data.get("coverLetterTitle"))
                .qnaList((List<QnaDto>) data.get("qnaList"))
                .build();

        return new PageMoveWithMessage("/user/submitted-coverLetter", letter);
    }

    // 면접 생성
    public PageMoveWithMessage createInterview(HttpSession session, InterviewRequestDto requestDto) {
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.post(session, requestDto, Const.RequestHeader.USER, "/user/" + userId + "/interview");

        return new PageMoveWithMessage("redirect:/v1/user/my-page/apply-list/1", response.getMessage());
    }

}
