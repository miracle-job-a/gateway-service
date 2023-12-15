package com.miracle.memberservice.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.miracle.memberservice.dto.request.*;
import com.miracle.memberservice.dto.response.*;
import com.miracle.memberservice.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final S3Method s3Method;

    /**
     * 유저 회원가입 요청 API
     *
     * @param userJoinDto 회원가입 폼 태그에 있는 정보들
     * @param session     Token값 추출을 위한 세션
     * @return 회원가입 성공하면 메인 페이지 반환, 실패하면 에러메시지와 함께 다시 user-join 반환
     */
    public PageMoveWithMessage join(UserJoinDto userJoinDto, HttpSession session) {

        ApiResponse response = ServiceCall.post(session, userJoinDto, Const.RequestHeader.USER, "/user/join");

        if (response.getHttpStatus() != 200) return new PageMoveWithMessage("guest/user-join", response.getMessage());

        return new PageMoveWithMessage("guest/user-login");
    }

    public PageMoveWithMessage login(LoginDto loginDto, HttpSession session) {

        ApiResponse response = ServiceCall.post(session, loginDto, Const.RequestHeader.USER, "/user/login");

        if (response.getHttpStatus() != 200) return new PageMoveWithMessage("guest/user-login", response.getMessage());

        Map<String, Object> data = (LinkedHashMap<String, Object>) response.getData();

        UserLoginResponseDto dto = UserLoginResponseDto.builder()
                .id(data.get("id"))
                .email(data.get("email"))
                .name(data.get("name"))
                .build();

        if (Objects.nonNull(loginDto.getPostId()))
            return new PageMoveWithMessage("redirect:/v1/click/post/" + loginDto.getPostId() + "/detail", dto);
        return new PageMoveWithMessage("redirect:/v1", dto);
    }

    public ResponseEntity<String> duplicateEmail(HttpSession session, String email) {

        ApiResponse response = ServiceCall.get(session, Const.RequestHeader.USER, "/user/check-email/" + email);

        if (Boolean.TRUE.equals(response.getData()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getMessage());

        return ResponseEntity.status(response.getHttpStatus()).body(response.getMessage());
    }

    public PageMoveWithMessage formResume(HttpSession session) {
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.get(session, Const.RequestHeader.USER, "/user/" + userId + "/base-info");


        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("user/resumes", response.getMessage());

        Map<String, Object> data = (LinkedHashMap<String, Object>) response.getData();

        UserBaseInfoResponseDto info = UserBaseInfoResponseDto.builder()
                .email(data.get("email"))
                .name(data.get("name"))
                .phone(data.get("phone"))
                .birth(data.get("birth"))
                .address(data.get("address"))
                .stackIdSet(data.get("stackIdSet"))
                .build();

        return new PageMoveWithMessage("user/resume-form", info);
    }

    public PageMoveWithMessage addResume(HttpSession session, ResumeRequestDto resumeRequestDto, MultipartFile photo) throws IOException {
        Long userId = (Long) session.getAttribute("id");

        String fileName = nextNumberOfPhoto(userId);
        resumeRequestDto.setPhoto(fileName);

        ApiResponse response = ServiceCall.post(session, resumeRequestDto, Const.RequestHeader.USER, "/user/" + userId + "/resume");
        if (response.getHttpStatus() != 201 || Objects.isNull(fileName))
            return new PageMoveWithMessage("redirect:/v1/user/resume/form", response.getMessage());

        s3Method.uploadFile(photo, Const.RequestHeader.RESUME, fileName);

        if (Objects.nonNull(resumeRequestDto.getPostId()))
            return new PageMoveWithMessage("redirect:/v1/click/post/" + resumeRequestDto.getPostId() + "/detail", resumeRequestDto);
        return new PageMoveWithMessage("redirect:/v1/user/resumes");
    }

    private String nextNumberOfPhoto(Long userId) {
        for (int i = 1; i < 6; i++) {
            String fileName = userId + "_" + i;
            try {
                s3Method.getFile(Const.RequestHeader.RESUME, fileName);
            } catch (AmazonS3Exception e) {
                return fileName;
            }
        }
        return null;
    }

    public PageMoveWithMessage resumeList(HttpSession session) {
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.get(session, Const.RequestHeader.USER, "/user/" + userId + "/resume");
        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("/v1/user", response.getMessage());

        List<ResumeListResponseDto> resumeList = ApiResponseToList.resumeList(response.getData());

        return new PageMoveWithMessage("user/resumes", resumeList);
    }

    public PageMoveWithMessage getResumeDetail(HttpSession session, Long resumeId) {
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.getParam(session, Const.RequestHeader.USER, "/user/" + userId + "/resume/" + resumeId, "requester", "USER");

        Map<String, Object> data = (LinkedHashMap<String, Object>) response.getData();

        ResumeResponseDto info = ResumeResponseDto.builder()
                .id(Long.valueOf((Integer) data.get("id")))
                .title((String) data.get("title"))
                .photo((String) data.get("photo"))
                .career((Integer) data.get("career"))
                .birth((String) data.get("birth"))
                .phone((String) data.get("phone"))
                .education((String) data.get("education"))
                .gitLink((String) data.get("gitLink"))
                .open((Boolean) data.get("open"))
                .jobIdSet((ArrayList<Integer>) data.get("jobIdSet"))
                .stackIdSet((ArrayList<Integer>) data.get("stackIdSet"))
                .careerDetailList((List<String>) data.get("careerDetailList"))
                .projectList((List<String>) data.get("projectList"))
                .etcList((List<String>) data.get("etcList"))
                .build();

        return new PageMoveWithMessage("user/resume-detail", info);
    }

    public PageMoveWithMessage deleteResume(HttpSession session, Long resumeId, String photo) {
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.delete(session, Const.RequestHeader.USER, "/user/" + userId + "/resume/" + resumeId);

        s3Method.deleteFile(Const.RequestHeader.RESUME, photo);
        return new PageMoveWithMessage("redirect:/v1/user/resumes", response.getMessage());
    }

    public PageMoveWithMessage updateResume(HttpSession session, ResumeRequestDto requestDto, Long resumeId, MultipartFile file) throws IOException {
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.put(session, requestDto, Const.RequestHeader.USER, "/user/" + userId + "/resume/" + resumeId);

        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("redirect:/v1/user/resume/detail/" + resumeId, response.getMessage());

        String photo = requestDto.getPhoto();
        String originalFilename = file.getOriginalFilename();
        if (!Strings.isBlank(originalFilename)) {
            s3Method.deleteFile(Const.RequestHeader.RESUME, photo);
            s3Method.uploadFile(file, Const.RequestHeader.RESUME, photo);
        }

        return new PageMoveWithMessage("redirect:/v1/user/resume/detail/" + resumeId);
    }

    public PageMoveWithMessage createCoverLetter(HttpSession session, CoverLetterPostRequestDto requestDto, QnaListDto qnaListDto) {
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.post(session, requestDto, Const.RequestHeader.USER, "/user/" + userId + "/cover-letter");
        if (response.getHttpStatus() != 201)
            return new PageMoveWithMessage("redirect:/v1/user/cover-letter/form", "같은 문항이 들어갈 수 없습니다.");
        if (Objects.nonNull(qnaListDto.getPostId()))
            return new PageMoveWithMessage("redirect:/v1/click/post/" + qnaListDto.getPostId() + "/detail", qnaListDto);
        return new PageMoveWithMessage("redirect:/v1/user/cover-letters/1", response.getMessage());
    }

    public PageMoveWithMessage coverLetterList(HttpSession session, int strNum, String sort) {
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.getUserParamListSort(session, Const.RequestHeader.USER, "/user/" + userId + "/cover-letter", strNum, strNum + 4, sort);
        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("redirect:/v1", response.getMessage());

        List<List<CoverLetterListResponseDto>> letterList = ApiResponseToList.coverLetterList(response.getData());

        return new PageMoveWithMessage("user/cover-letters", letterList);
    }

    public PageMoveWithMessage coverLetterListSearch(HttpSession session, int strNum, String word) {
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.getUserParamListSearch(session, Const.RequestHeader.USER, "/user/" + userId + "/cover-letter/search", strNum, strNum + 4, word);
        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("redirect:/v1", response.getMessage());

        List<List<CoverLetterListResponseDto>> letterList = ApiResponseToList.coverLetterList(response.getData());

        return new PageMoveWithMessage("user/cover-letters", letterList);
    }

    public PageMoveWithMessage deleteCoverLetter(HttpSession session, Long coverLetterId) {
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.delete(session, Const.RequestHeader.USER, "/user/" + userId + "/cover-letter/" + coverLetterId);

        return new PageMoveWithMessage("redirect:/v1/user/cover-letters/1", response.getMessage());
    }

    public PageMoveWithMessage coverLetterDetail(HttpSession session, Long coverLetterId) {
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.get(session, Const.RequestHeader.USER, "/user/" + userId + "/cover-letter/" + coverLetterId);

        LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) response.getData();

        CoverLetterResponseDto letter = CoverLetterResponseDto.builder()
                .id(Long.valueOf((Integer) data.get("id")))
                .title((String) data.get("title"))
                .modifiedAt((String) data.get("modifiedAt"))
                .qnaList((List<QnaDto>) data.get("qnaList")).build();

        return new PageMoveWithMessage("user/coverLetter-detail", letter);
    }

    public PageMoveWithMessage updateCoverLetter(HttpSession session, CoverLetterPostRequestDto requestDto, Long coverLetterId) {
        Long userId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.put(session, requestDto, Const.RequestHeader.USER, "/user/" + userId + "/cover-letter/" + coverLetterId);
        return new PageMoveWithMessage("redirect:/v1/user/cover-letter/detail/" + coverLetterId, response.getMessage());
    }

    public ApplicationLetterResponseDto applyPopup(HttpSession session, Long userId) {
        ApiResponse response = ServiceCall.get(session, Const.RequestHeader.USER, "/user/" + userId + "/application-letter/popup");

        Map<String, Object> data = (LinkedHashMap<String, Object>) response.getData();
        return ApplicationLetterResponseDto.builder()
                .resumeList(ApiResponseToList.resumeTitleList(data.get("resumeList")))
                .coverLetterList(ApiResponseToList.coverLetterTitleList(data.get("coverLetterList")))
                .build();
    }

    public PageMoveWithMessage apply(HttpSession session, ApplicationLetterPostRequestDto dto, Long companyId) {
        Long userId = (Long) session.getAttribute("id");

        if (applyStatus(session, dto.getPostId()) != 200)
            return new PageMoveWithMessage("redirect:/v1/click/post/" + dto.getPostId() + "/detail?companyId=" + companyId + "&postType=" + dto.getPostType(), "공고가 마감되어 지원이 불가능합니다.", dto);

        ApiResponse response = ServiceCall.post(session, dto, Const.RequestHeader.USER, "/user/" + userId + "/application-letter");

        return new PageMoveWithMessage("redirect:/v1/click/post/" + dto.getPostId() + "/detail?companyId=" + companyId + "&postType=" + dto.getPostType(), response.getMessage(), dto);
    }

    private int applyStatus(HttpSession session, Long postId) {
        ApiResponse response = ServiceCall.get(session, Const.RequestHeader.COMPANY, "/company/posts/" + postId);
        return response.getHttpStatus();
    }

    public PageMoveWithMessage applicantList(HttpSession session, Long postId, String sort, int startPage) {
        ApiResponse response = ServiceCall.getUserParamListSort(session, Const.RequestHeader.USER, "/post/" + postId + "/applicant/list", startPage, startPage + 4, sort);

        return new PageMoveWithMessage("company/applicant-list", ApiResponseToList.applicantList(response.getData()));
    }

    public PageMoveWithMessage getUserJoinCountByDay(HttpSession session, int year, int month) {
        ApiResponse response = ServiceCall.getParamListForCount(session, Const.RequestHeader.USER, "/user");
        if (response.getHttpStatus() != 200) {
            return new PageMoveWithMessage("admin/main", response.getMessage());
        }

        List<List<LinkedHashMap<String, Object>>> userData = (List<List<LinkedHashMap<String, Object>>>) response.getData();

        List<LinkedHashMap<String, Object>> filteredUserData = userData.stream()
                .flatMap(List::stream)
                .filter(user -> {
                    String joinDate = (String) user.get("joinDate");
                    LocalDate userJoinDate = LocalDate.parse(joinDate);
                    return userJoinDate.getYear() == year && userJoinDate.getMonthValue() == month;
                })
                .collect(Collectors.toList());

        Map<Integer, Long> userJoinCountByDay = ApiResponseToList.getUserJoinCountByDay(Collections.singletonList(filteredUserData), year, month);

        List<List<Object>> chartData = new ArrayList<>();
        for (Map.Entry<Integer, Long> entry : userJoinCountByDay.entrySet()) {
            chartData.add(Arrays.asList(entry.getKey(), entry.getValue()));
        }

        return new PageMoveWithMessage("admin/user-join-count", userJoinCountByDay);
    }
}