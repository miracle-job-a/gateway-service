package com.miracle.memberservice.service;

import com.miracle.memberservice.dto.request.*;
import com.miracle.memberservice.dto.response.*;
import com.miracle.memberservice.util.ApiResponseToList;
import com.miracle.memberservice.util.Const;
import com.miracle.memberservice.util.PageMoveWithMessage;
import com.miracle.memberservice.util.ServiceCall;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@Slf4j
public class CompanyService {

    public ResponseEntity<String> bnoCertify(CompanyCheckBnoRequestDto bno, HttpSession session) {

        ApiResponse response = ServiceCall.post(session, bno, "company", "/company/bno");

        return ResponseEntity.status(response.getHttpStatus()).body(response.getMessage());
    }

    public PageMoveWithMessage join(CompanyJoinDto companyJoinDto, HttpSession session) {

        ApiResponse response = ServiceCall.post(session, companyJoinDto, "company", "/company/signup");

        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("guest/company-join", response.getMessage());

        return new PageMoveWithMessage("index");
    }

    public PageMoveWithMessage login(LoginDto loginDto, HttpSession session) {

        ApiResponse response = ServiceCall.post(session, loginDto, loginDto.getMemberType(), "/company/login");

        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("guest/company-login", response.getMessage());

        LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) response.getData();

        CompanyLoginResponseDto dto = CompanyLoginResponseDto.builder()
                .id(data.get("id"))
                .email(data.get("email"))
                .bno(data.get("bno"))
                .build();

        return new PageMoveWithMessage("index", dto);
    }

    public ResponseEntity<String> duplicateEmail(HttpSession session, String email) {

        ApiResponse response = ServiceCall.post(session, new CompanyCheckEmailRequestDto(email), "company", "/company/email");

        return ResponseEntity.status(response.getHttpStatus()).body(response.getMessage());
    }

    //공고관리 목록
    public PageMoveWithMessage postList(HttpSession session) {

        Long id = (Long) session.getAttribute("id");

        ApiResponse response = ServiceCall.get(session, "company", "/company/" + id + "/posts/latest");

        if (response.getHttpStatus() != 200) return new PageMoveWithMessage("index", response.getMessage());

        List<ManagePostsResponseDto> postList = ApiResponseToList.postList(response.getData());

        return new PageMoveWithMessage("company/post-list", postList);
    }

    // MZ 공고 등록
    public PageMoveWithMessage createMZ(MzPostDto mzPostDto, HttpSession session) {
        ApiResponse response = ServiceCall.post(session, mzPostDto, "company", "/mzPost");

        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("/company/post/mzPost", response.getMessage());

        return new PageMoveWithMessage("/company/postlist");
    }

    public PageMoveWithMessage formPost(HttpSession session) {
        Long companyId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.get(session, "company", "/company/" + companyId + "/info");

        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("/company/post/list", response.getMessage());

        LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) response.getData();

        List<CompanyFaqResponseDto> dtos = ApiResponseToList.faqList(data.get("faqList"));

        PostCommonDataResponseDto info = PostCommonDataResponseDto.builder()
                .name(data.get("name"))
                .ceoName(data.get("ceoName"))
                .photo(data.get("photo"))
                .employeeNum(data.get("employeeNum"))
                .address(data.get("address"))
                .introduction(data.get("introduction"))
                .faqList(dtos)
                .build();


        return new PageMoveWithMessage("company/normal-post", info);
    }

    public PageMoveWithMessage createPost(HttpSession session, PostRequestDto postRequestDto) {
        Long companyId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.post(session, postRequestDto, Const.RequestHeader.COMPANY, "/company/" + companyId + "/post");

        return new PageMoveWithMessage("redirect:/v1/company/post/list", response.getMessage());
    }

    public PageMoveWithMessage getPostDetail(HttpSession session, Long postId) {
        Long companyId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.get(session, Const.RequestHeader.COMPANY, "/company/" + companyId + "/posts/" + postId);

        LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) response.getData();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String postType = (String) data.get("postType");
        PostResponseDto dto;

        PostResponseDto.PostResponseDtoBuilder builder = PostResponseDto.builder()
                .career((Integer) data.get("career"))
                .postType(postType)
                .benefit((String) data.get("benefit"))
                .process((String) data.get("process"))
                .notice((String) data.get("notice"))
                .mainTask((String) data.get("mainTask"))
                .qualification((String) data.get("qualification"))
                .title((String) data.get("title"))
                .specialSkill((String) data.get("specialSkill"))
                .tool((String) data.get("tool"))
                .workCondition((String) data.get("workCondition"))
                .workAddress((String) data.get("workAddress"))
                .endDate(LocalDate.parse((String) data.get("endDate"), formatter))
                .questionList(ApiResponseToList.questionList(data.get("questionList")))
                .stackIdSet((ArrayList<Integer>) data.get("stackIdSet"))
                .jobIdSet((ArrayList<Integer>) data.get("jobIdSet"))
                .closed((Boolean) data.get("closed"));

        if (postType.equals("NORMAL")) {
            dto = builder.build();
        } else {
            dto = builder
                    .testStartDate(LocalDate.parse((String) data.get("testStartDate"), formatter))
                    .testEndDate(LocalDate.parse((String) data.get("testEndDate"), formatter))
                    .build();
        }

        return new PageMoveWithMessage("company/post-detail", dto);
    }

    public PageMoveWithMessage deletePost(HttpSession session, Long postId){
        Long companyId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.delete(session, Const.RequestHeader.COMPANY, "/company/" + companyId + "/posts/" + postId);

        return new PageMoveWithMessage("redirect:/v1/company/post/list", response.getMessage());
    }

    public PageMoveWithMessage closePost(HttpSession session, Long postId){
        Long companyId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.get(session, Const.RequestHeader.COMPANY, "/company/" + companyId + "/posts/" + postId + "/close");

        return new PageMoveWithMessage("redirect:/v1/company/post/detail", response.getMessage());
    }

    public PageMoveWithMessage faqList(HttpSession session) {
        Long companyId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.get(session, "company", "/company/" + companyId + "/faqs");

        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("index", response.getMessage());

        List<CompanyFaqResponseDto> dtos = ApiResponseToList.faqList(response.getData());

        return new PageMoveWithMessage("company/faq", dtos);
    }

    public PageMoveWithMessage addFaq(HttpSession session, CompanyFaqRequestDto companyFaqRequestDto) {
        Long companyId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.post(session, companyFaqRequestDto, "company", "/company/" + companyId + "/faq");
        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("redirect:/v1/company/faq", response.getMessage());
        return new PageMoveWithMessage("redirect:/v1/company/faq");
    }

    public PageMoveWithMessage deleteFaq(HttpSession session, String faqId) {
        Long companyId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.delete(session, "company", "/company/" + companyId + "/faqs/" + faqId);
        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("redirect:/v1/company/faq", response.getMessage());
        return new PageMoveWithMessage("redirect:/v1/company/faq");
    }

}
