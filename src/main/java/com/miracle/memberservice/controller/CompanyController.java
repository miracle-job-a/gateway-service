package com.miracle.memberservice.controller;

import com.miracle.memberservice.dto.request.MzPostDto;
import com.miracle.memberservice.service.CompanyService;
import com.miracle.memberservice.util.PageMoveWithMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/v1/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }

    @GetMapping("/post/create")
    public String detail() {
        return "company/detail-page";
    }

    @GetMapping("/postlist")
    public String postList(){ return "company/post-list"; }

    // 공고 생성 폼 이동 (구현 X, 프로트 페이지만 이동)
    @GetMapping("/post-form")
    public String postForm(){ return "company/mz-post"; }

    // 기업 자주하는 질문페이지 이동
    @GetMapping("/faq")
    public String faqList(){ return "company/faq"; }

    // TODO MZ 공고 등록요청
    @PostMapping("/post/mzPost")
    public String createMZ(@ModelAttribute MzPostDto mzPostDto, Model model, HttpSession session) {
        PageMoveWithMessage pmwm = companyService.createMZ(mzPostDto, session);
        model.addAttribute("errorMessage", pmwm.getErrorMessage());
        return pmwm.getPageName();
    }

}
