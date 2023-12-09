package com.miracle.memberservice.service;

import com.miracle.memberservice.dto.request.InterviewRequestDto;
import com.miracle.memberservice.dto.request.PostIdRequestDto;
import com.miracle.memberservice.dto.request.QnaDto;
import com.miracle.memberservice.dto.response.*;
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
    public PageMoveWithMessage applicationLetterList(HttpSession session, int startPage, String sort){
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.getUserParamListSort(session, Const.RequestHeader.USER, "/user/" + userId + "/application-letter", startPage, startPage + 4, sort);

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

    // 면접 수정
    public PageMoveWithMessage updateInterview(HttpSession session, InterviewRequestDto requestDto, Long interviewId, Long applicationLetterId) {
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.put(session, requestDto, Const.RequestHeader.USER, "/user/" + userId + "/interview/" + interviewId);

        return new PageMoveWithMessage("redirect:/v1/user/my-page/interview/" + applicationLetterId + "/" + interviewId, response.getMessage());
    }

    // 면접 조회
    public PageMoveWithMessage interviewDetail(HttpSession session, Long interviewId){
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.get(session, Const.RequestHeader.USER, "/user/" + userId + "/interview/" + interviewId);

        LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) response.getData();

        InterviewResponseDto interview = InterviewResponseDto.builder()
                .qnaList((List<QnaDto>) data.get("qnaList")).build();

        return new PageMoveWithMessage("/user/interview-detail", interview);
    }

    // 면접 삭제
    public PageMoveWithMessage deleteInterview(HttpSession session, Long interviewId){
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.delete(session, Const.RequestHeader.USER, "/user/" + userId + "/interview/" + interviewId);

        return new PageMoveWithMessage("redirect:/v1/user/my-page/apply-list/1");
    }

    // 공고 id로 기업 정보조회
    public List<CompanyNameResponseDto> getCompanyInfo(HttpSession session, Set<Long> postId){
        ApiResponse response = ServiceCall.post(session, new PostIdRequestDto(postId), Const.RequestHeader.COMPANY, "/company/posts");
        if (response.getData() instanceof  Boolean) return null;

        return ApiResponseToList.companyInfo(response.getData());
    }

}
