package com.miracle.memberservice.service;

import com.miracle.memberservice.dto.request.*;
import com.miracle.memberservice.dto.response.*;
import com.miracle.memberservice.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.servlet.http.PushBuilder;
import java.lang.reflect.Constructor;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyPageService {

    private final S3Method s3Method;

    // 지원현황 목록 불러오기
    public PageMoveWithMessage applicationLetterList(HttpSession session, int startPage, String sort) {
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.getUserParamListSort(session, Const.RequestHeader.USER, "/user/" + userId + "/application-letter", startPage, startPage + 4, sort);

        if (response.getHttpStatus() != 200) return new PageMoveWithMessage("/v1", response.getMessage());

        List<List<ApplicationLetterListResponseDto>> letter = ApiResponseToList.applicationLetterList(response.getData());
        return new PageMoveWithMessage("user/apply-list", letter);
    }

    // 지원취소 (지원서 삭제)
    public PageMoveWithMessage deleteApplicationLetter(HttpSession session, Long applicationLetterId) {
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.delete(session, Const.RequestHeader.USER, "/user/" + userId + "/application-letter/" + applicationLetterId);

        return new PageMoveWithMessage("redirect:/v1/user/my-page/apply-list/1");
    }

    // 지원상태 변경하기
    public PageMoveWithMessage updateApplicationLetter(HttpSession session, Long applicationLetterId, String applicationStatus) {
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.putParam(session, Const.RequestHeader.USER,
                "/user/" + userId + "/application-letter/" + applicationLetterId, "applicationStatus", applicationStatus);

        return new PageMoveWithMessage("redirect:/v1/user/my-page/apply-list/1");
    }

    // 지원 이력서 조회하기
    public PageMoveWithMessage resumeInApplicationLetterDetail(HttpSession session, Long applicationLetterId, Long userId) {
        ApiResponse response;
        if (Objects.nonNull(userId)) {
            response = ServiceCall.getAnother(session, Const.RequestHeader.USER, "/user/" + userId + "/application-letter/" + applicationLetterId + "/resume", userId);
        } else {
            userId = (Long) session.getAttribute("id");
            response = ServiceCall.get(session, Const.RequestHeader.USER, "/user/" + userId + "/application-letter/" + applicationLetterId + "/resume");
        }

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
                .photo(s3Method.getUrl(Const.RequestHeader.RESUME, (String) data.get("userPhoto")))
                .userCareerDetailList((List<String>) data.get("userCareerDetailList"))
                .userProjectList((List<String>) data.get("userProjectList"))
                .userEtcList((List<String>) data.get("userEtcList"))
                .build();

        return new PageMoveWithMessage("user/submitted-resume", dto);
    }

    // 지원 자소서 조회하기
    public PageMoveWithMessage coverLetterInApplicationLetterDetail(HttpSession session, Long applicationLetterId, Long userId) {
        ApiResponse response;
        if (Objects.nonNull(userId)) {
            response = ServiceCall.getAnother(session, Const.RequestHeader.USER, "/user/" + userId + "/application-letter/" + applicationLetterId + "/cover-letter", userId);
        } else {
            userId = (Long) session.getAttribute("id");
            response = ServiceCall.get(session, Const.RequestHeader.USER, "/user/" + userId + "/application-letter/" + applicationLetterId + "/cover-letter");
        }
        LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) response.getData();

        CoverLetterInApplicationLetterResponseDto letter = CoverLetterInApplicationLetterResponseDto.builder()
                .coverLetterTitle((String) data.get("coverLetterTitle"))
                .qnaList((List<QnaDto>) data.get("qnaList"))
                .build();

        return new PageMoveWithMessage("user/submitted-coverLetter", letter);
    }

    // 면접 생성
    public PageMoveWithMessage createInterview(HttpSession session, InterviewRequestDto requestDto, Long applicationLetterId) {
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.post(session, requestDto, Const.RequestHeader.USER, "/user/" + userId + "/interview");
        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("redirect:/v1/user/my-page/interview/form/" + applicationLetterId, "같은 문항이 들어갈 수 없습니다.");
        return new PageMoveWithMessage("redirect:/v1/user/my-page/apply-list/1", response.getMessage());
    }

    // 면접 수정
    public PageMoveWithMessage updateInterview(HttpSession session, InterviewRequestDto requestDto, Long interviewId, Long applicationLetterId) {
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.put(session, requestDto, Const.RequestHeader.USER, "/user/" + userId + "/interview/" + interviewId);

        return new PageMoveWithMessage("redirect:/v1/user/my-page/interview/" + applicationLetterId + "/" + interviewId, response.getMessage());
    }

    // 면접 조회
    public PageMoveWithMessage interviewDetail(HttpSession session, Long interviewId) {
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.get(session, Const.RequestHeader.USER, "/user/" + userId + "/interview/" + interviewId);

        LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) response.getData();

        InterviewResponseDto interview = InterviewResponseDto.builder()
                .qnaList((List<QnaDto>) data.get("qnaList")).build();

        return new PageMoveWithMessage("user/interview-detail", interview);
    }

    // 면접 삭제
    public PageMoveWithMessage deleteInterview(HttpSession session, Long interviewId) {
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.delete(session, Const.RequestHeader.USER, "/user/" + userId + "/interview/" + interviewId);

        return new PageMoveWithMessage("redirect:/v1/user/my-page/apply-list/1");
    }

    // 공고 id로 기업 정보조회
    public List<CompanyNameResponseDto> getCompanyInfo(HttpSession session, Set<Long> postId) {
        ApiResponse response = ServiceCall.post(session, new PostIdRequestDto(postId), Const.RequestHeader.COMPANY, "/company/posts");
        if (response.getData() instanceof Boolean) return null;

        return ApiResponseToList.companyInfo(session, response.getData());
    }

    // 유저 정보 조회
    public PageMoveWithMessage userInfo(HttpSession session) {
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.get(session, Const.RequestHeader.USER, "/user/" + userId);
        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("redirect:/v1", response.getMessage());
        Map<String, Object> data = (LinkedHashMap<String, Object>) response.getData();
        UserInfoResponseDto info = UserInfoResponseDto.builder()
                .id(Long.valueOf((Integer) data.get("id")))
                .name((String) data.get("name"))
                .birth((String) data.get("birth"))
                .phone((String) data.get("phone"))
                .address((String) data.get("address"))
                .stackIdSet((ArrayList<Integer>) data.get("stackIdSet"))
                .build();
        return new PageMoveWithMessage("user/user-info", info);
    }

    // 수정폼 접근 인증 api
    public PageMoveWithMessage validationUser(HttpSession session, LoginDto loginDto) {
        ApiResponse response = ServiceCall.post(session, loginDto, Const.RequestHeader.USER, "/user/login");
        if (response.getHttpStatus() != 200) {
            return new PageMoveWithMessage("user/validation", response.getMessage());
        }

        return new PageMoveWithMessage("user/modify-form", response.getMessage());
    }

    // 수정폼 요청
    public PageMoveWithMessage modifyUserInfo(HttpSession session) {
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.get(session, Const.RequestHeader.USER, "/user/" + userId);
        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("redirect:/v1", response.getMessage());
        Map<String, Object> data = (LinkedHashMap<String, Object>) response.getData();
        UserInfoResponseDto info = UserInfoResponseDto.builder()
                .id(Long.valueOf((Integer) data.get("id")))
                .name((String) data.get("name"))
                .birth((String) data.get("birth"))
                .phone((String) data.get("phone"))
                .address((String) data.get("address"))
                .stackIdSet((ArrayList<Integer>) data.get("stackIdSet"))
                .build();
        return new PageMoveWithMessage("user/modify-form", info);
    }

    public PageMoveWithMessage updateUserInfo(HttpSession session, UserUpdateInfoRequestDto requestDto) {
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.put(session, requestDto, Const.RequestHeader.USER, "/user/" + userId);
        if (response.getHttpStatus() != 200) {
            return new PageMoveWithMessage("user/modify-form", response.getMessage());
        }
        session.removeAttribute("password");
        return new PageMoveWithMessage("redirect:/v1/user/my-page/my-info");
    }

    public PageMoveWithMessage signoutUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.delete(session, Const.RequestHeader.USER, "/user/" + userId);
        if (response.getHttpStatus() != 200) return new PageMoveWithMessage("redirect:/v1/user/my-page/my-info");

        deleteResumePhotos(userId);

        session.invalidate();
        return new PageMoveWithMessage("redirect:/v1");
    }

    private void deleteResumePhotos(Long userId) {
        for (int i = 1; i < 6; i++) {
            s3Method.deleteFile(Const.RequestHeader.RESUME, userId + "_" + i);
        }
    }

}
