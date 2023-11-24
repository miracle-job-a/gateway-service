package com.miracle.memberservice.service;

import com.miracle.memberservice.dto.request.*;
import com.miracle.memberservice.dto.response.ApiResponse;
import com.miracle.memberservice.dto.response.CompanyFaqResponseDto;
import com.miracle.memberservice.dto.response.CompanyLoginResponseDto;
import com.miracle.memberservice.util.PageMoveWithMessage;
import com.miracle.memberservice.util.ServiceCall;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
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

        String id = (String) session.getAttribute("companyId");

        ApiResponse response = ServiceCall.get(session, "company", "/postList/" + id);

        if (response.getHttpStatus() != 200)
            return null;

        LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) response.getData();

        //TODO
        // 공고 id만 담음
        List<Long> postId = (List<Long>) data.get("id");
        PostIdListDto postIdListDto = new PostIdListDto();
        postIdListDto.getId().addAll(postId);

        return null;
    }

    // MZ 공고 등록
    public PageMoveWithMessage createMZ(MzPostDto mzPostDto, HttpSession session) {
        ApiResponse response = ServiceCall.post(session, mzPostDto, "company", "/mzPost");

        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("/company/post/mzPost", response.getMessage());

        return new PageMoveWithMessage("/company/postlist");
    }

    public PageMoveWithMessage faqList(HttpSession session) {
        Long companyId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.get(session, "company", "/company/" + companyId + "/faqs");

        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("index", response.getMessage());

        ArrayList data = (ArrayList) response.getData();
        int size = data.size();

        List<CompanyFaqResponseDto> dtos = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) data.get(i);

            dtos.add(CompanyFaqResponseDto.builder()
                    .id(map.get("id"))
                    .question(map.get("question"))
                    .answer(map.get("answer"))
                    .build());
        }
        return new PageMoveWithMessage("company/faq", dtos);
    }

    public PageMoveWithMessage addFaq(HttpSession session, CompanyFaqRequestDto companyFaqRequestDto) {
        Long companyId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.post(session, companyFaqRequestDto,"company", "/company/" + companyId + "/faq");
        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("redirect:/v1/company/faq", response.getMessage());
        return new PageMoveWithMessage("redirect:/v1/company/faq");
    }

    public PageMoveWithMessage deleteFaq(HttpSession session, String faqId) {
        Long companyId = (Long) session.getAttribute("id");
        ApiResponse response = ServiceCall.delete(session,"company", "/company/" + companyId + "/faqs/" + faqId);
        if (response.getHttpStatus() != 200)
            return new PageMoveWithMessage("redirect:/v1/company/faq", response.getMessage());
        return new PageMoveWithMessage("redirect:/v1/company/faq");
    }

}
